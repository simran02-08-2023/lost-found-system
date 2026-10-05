package com.lostfound.controller;

import com.lostfound.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/test-db")
public class TestDbServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try (Connection con = DBConnection.getConnection()) {
            resp.getWriter().println("Database connected! Catalog: " + con.getCatalog());
        } catch (Exception e) {
            resp.getWriter().println("Connection failed: " + e.getMessage());
        }
    }
}
