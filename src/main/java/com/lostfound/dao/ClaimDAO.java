package com.lostfound.dao;

import com.lostfound.model.Claim;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClaimDAO {

    /** Saves a new claim. */
    public void create(int itemId, int claimantId, String description) throws SQLException {
        String sql = "INSERT INTO claims (item_id, claimant_id, description) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, claimantId);
            ps.setString(3, description);
            ps.executeUpdate();
        }
    }

    /** True if this user already has a PENDING claim on this item. */
    public boolean hasPendingClaim(int itemId, int claimantId) throws SQLException {
        String sql = "SELECT 1 FROM claims "
                + "WHERE item_id = ? AND claimant_id = ? AND status = 'PENDING'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, claimantId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** All claims made by one user (the "My claims" page). */
    public List<Claim> findByClaimant(int claimantId) throws SQLException {
        String sql = "SELECT c.id, c.item_id, c.claimant_id, c.description, c.status, "
                + "c.created_at, i.title "
                + "FROM claims c JOIN items i ON i.id = c.item_id "
                + "WHERE c.claimant_id = ? ORDER BY c.created_at DESC";
        List<Claim> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claimantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Claim c = new Claim();
                    c.setId(rs.getInt("id"));
                    c.setItemId(rs.getInt("item_id"));
                    c.setClaimantId(rs.getInt("claimant_id"));
                    c.setDescription(rs.getString("description"));
                    c.setStatus(rs.getString("status"));
                    c.setCreatedAt(rs.getString("created_at"));
                    c.setItemTitle(rs.getString("title"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    /** All claims on one item, newest first (the finder sees these; no claimant identity). */
    public List<Claim> findByItem(int itemId) throws SQLException {
        String sql = "SELECT id, item_id, claimant_id, description, status, created_at "
                + "FROM claims WHERE item_id = ? ORDER BY created_at DESC";
        List<Claim> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Claim c = new Claim();
                    c.setId(rs.getInt("id"));
                    c.setItemId(rs.getInt("item_id"));
                    c.setClaimantId(rs.getInt("claimant_id"));
                    c.setDescription(rs.getString("description"));
                    c.setStatus(rs.getString("status"));
                    c.setCreatedAt(rs.getString("created_at"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    /** All PENDING claims with item title and claimant name (the admin page). */
    public List<Claim> findPending() throws SQLException {
        String sql = "SELECT c.id, c.item_id, c.claimant_id, c.description, c.status, "
                + "c.created_at, i.title, u.name "
                + "FROM claims c "
                + "JOIN items i ON i.id = c.item_id "
                + "JOIN users u ON u.id = c.claimant_id "
                + "WHERE c.status = 'PENDING' ORDER BY c.created_at";
        List<Claim> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Claim c = new Claim();
                c.setId(rs.getInt("id"));
                c.setItemId(rs.getInt("item_id"));
                c.setClaimantId(rs.getInt("claimant_id"));
                c.setDescription(rs.getString("description"));
                c.setStatus(rs.getString("status"));
                c.setCreatedAt(rs.getString("created_at"));
                c.setItemTitle(rs.getString("title"));
                c.setClaimantName(rs.getString("name"));
                list.add(c);
            }
        }
        return list;
    }

    /** Approves a claim. All updates succeed together or none do (JDBC transaction). */
    public boolean approve(int claimId) throws SQLException {
        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);              // start the transaction

            int itemId;
            int claimantId;
            // lock the claim row so two admins cannot approve it at the same time
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT item_id, claimant_id FROM claims "
                    + "WHERE id = ? AND status = 'PENDING' FOR UPDATE")) {
                ps.setInt(1, claimId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {              // already handled by someone else
                        con.rollback();
                        return false;
                    }
                    itemId = rs.getInt("item_id");
                    claimantId = rs.getInt("claimant_id");
                }
            }

            // 1. item becomes CLAIMED, but only if it is still ACTIVE
            if (run(con, "UPDATE items SET status = 'CLAIMED' "
                    + "WHERE id = ? AND status = 'ACTIVE'", itemId) == 0) {
                con.rollback();
                return false;
            }
            // 2. this claim becomes APPROVED
            run(con, "UPDATE claims SET status = 'APPROVED' WHERE id = ?", claimId);

            // 3. every other pending claim on the same item is rejected
            run(con, "UPDATE claims SET status = 'REJECTED' "
                    + "WHERE item_id = ? AND id <> ? AND status = 'PENDING'", itemId, claimId);

            // 4. the claimant's match with this found item is accepted
            run(con, "UPDATE matches m JOIN items l ON l.id = m.lost_item_id "
                    + "SET m.status = 'ACCEPTED' "
                    + "WHERE m.found_item_id = ? AND l.user_id = ?", itemId, claimantId);

            con.commit();                          // everything worked: save it all
            return true;

        } catch (SQLException e) {
            con.rollback();                        // anything failed: undo it all
            throw e;
        } finally {
            con.close();
        }
    }

    public boolean reject(int claimId) throws SQLException {
        String sql = "UPDATE claims SET status = 'REJECTED' "
                + "WHERE id = ? AND status = 'PENDING'";
        try (Connection con = DBConnection.getConnection()) {
            return run(con, sql, claimId) > 0;
        }
    }

    /** Runs one UPDATE with integer parameters and returns the rows changed. */
    private static int run(Connection con, String sql, int... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setInt(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }
}