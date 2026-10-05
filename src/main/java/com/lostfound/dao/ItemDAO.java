package com.lostfound.dao;

import com.lostfound.model.Item;
import com.lostfound.util.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void create(Item item) throws SQLException {
        String sql = "INSERT INTO items "
                + "(user_id, type, title, category, description, color, location, item_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getUserId());
            ps.setString(2, item.getType());
            ps.setString(3, item.getTitle());
            ps.setString(4, item.getCategory());
            ps.setString(5, item.getDescription());
            ps.setString(6, item.getColor());
            ps.setString(7, item.getLocation());
            ps.setDate(8, Date.valueOf(item.getItemDate()));
            ps.executeUpdate();
        }
    }

    public List<Item> findAllActive() throws SQLException {
        String sql = "SELECT id, user_id, type, title, category, description, "
                + "color, location, item_date, status, image_path "
                + "FROM items WHERE status = 'ACTIVE' ORDER BY created_at DESC";
        List<Item> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
        public Item findById(int id) throws SQLException {
        String sql = "SELECT id, user_id, type, title, category, description, "
                + "color, location, item_date, status, image_path "
                + "FROM items WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Item> search(String keyword, String type,
                             String category, String location) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT id, user_id, type, title, category, description, "
                + "color, location, item_date, status, image_path "
                + "FROM items WHERE status = 'ACTIVE'");
        List<Object> params = new ArrayList<>();

        if (!keyword.isEmpty()) {
            sql.append(" AND (title LIKE ? OR description LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (type.equals("LOST") || type.equals("FOUND")) {
            sql.append(" AND type = ?");
            params.add(type);
        }
        if (!category.isEmpty()) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        if (!location.isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location + "%");
        }
        sql.append(" ORDER BY created_at DESC");

        List<Item> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
        public List<Item> findByUser(int userId) throws SQLException {
        String sql = "SELECT id, user_id, type, title, category, description, "
                + "color, location, item_date, status, image_path "
                + "FROM items WHERE user_id = ? ORDER BY created_at DESC";
        List<Item> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** Closes the item only if it belongs to this user and is still ACTIVE. */
    public boolean close(int itemId, int userId) throws SQLException {
        String sql = "UPDATE items SET status = 'CLOSED' "
                + "WHERE id = ? AND user_id = ? AND status = 'ACTIVE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Updates the item only if it belongs to this user and is still ACTIVE. */
    public boolean update(Item item) throws SQLException {
        String sql = "UPDATE items SET title = ?, category = ?, description = ?, "
                + "color = ?, location = ?, item_date = ? "
                + "WHERE id = ? AND user_id = ? AND status = 'ACTIVE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getTitle());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getDescription());
            ps.setString(4, item.getColor());
            ps.setString(5, item.getLocation());
            ps.setDate(6, Date.valueOf(item.getItemDate()));
            ps.setInt(7, item.getId());
            ps.setInt(8, item.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        Item i = new Item();
        i.setId(rs.getInt("id"));
        i.setUserId(rs.getInt("user_id"));
        i.setType(rs.getString("type"));
        i.setTitle(rs.getString("title"));
        i.setCategory(rs.getString("category"));
        i.setDescription(rs.getString("description"));
        i.setColor(rs.getString("color"));
        i.setLocation(rs.getString("location"));
        Date d = rs.getDate("item_date");
        i.setItemDate(d == null ? null : d.toLocalDate());
        i.setStatus(rs.getString("status"));
        i.setImagePath(rs.getString("image_path"));
        return i;
    }
}