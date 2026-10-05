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