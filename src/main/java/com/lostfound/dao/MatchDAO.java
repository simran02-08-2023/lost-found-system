package com.lostfound.dao;

import com.lostfound.model.Match;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    /** Saves a match unless this lost/found pair already exists. */
    public boolean saveIfNew(int lostId, int foundId, double score) throws SQLException {
        String sql = "INSERT IGNORE INTO matches (lost_item_id, found_item_id, score) "
                + "VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lostId);
            ps.setInt(2, foundId);
            ps.setDouble(3, score);
            return ps.executeUpdate() > 0;
        }
    }

    /** Matches where the user owns either item, skipping rejected or inactive ones. */
    public List<Match> findByUser(int userId) throws SQLException {
        String sql = "SELECT m.id, m.lost_item_id, m.found_item_id, m.score, m.status "
                + "FROM matches m "
                + "JOIN items l ON l.id = m.lost_item_id "
                + "JOIN items f ON f.id = m.found_item_id "
                + "WHERE (l.user_id = ? OR f.user_id = ?) "
                + "AND m.status <> 'REJECTED' "
                + "AND l.status = 'ACTIVE' AND f.status = 'ACTIVE' "
                + "ORDER BY m.score DESC";
        List<Match> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Match m = new Match();
                    m.setId(rs.getInt("id"));
                    m.setLostItemId(rs.getInt("lost_item_id"));
                    m.setFoundItemId(rs.getInt("found_item_id"));
                    m.setScore(rs.getDouble("score"));
                    m.setStatus(rs.getString("status"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    /** "Not a match": allowed only if the user owns one of the two items. */
    public boolean reject(int matchId, int userId) throws SQLException {
        String sql = "UPDATE matches m "
                + "JOIN items l ON l.id = m.lost_item_id "
                + "JOIN items f ON f.id = m.found_item_id "
                + "SET m.status = 'REJECTED' "
                + "WHERE m.id = ? AND (l.user_id = ? OR f.user_id = ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        }
    }
}