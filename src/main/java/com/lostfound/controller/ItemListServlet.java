package com.lostfound.controller;

import com.lostfound.dao.ItemDAO;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/items")
public class ItemListServlet extends HttpServlet {
    
    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = clean(req.getParameter("q"));
        String type = clean(req.getParameter("type"));
        String category = clean(req.getParameter("category"));
        String location = clean(req.getParameter("location"));

        try {
            req.setAttribute("items", itemDAO.search(q, type, category, location));
        } catch (SQLException e) {
            log("Could not load items", e);
            req.setAttribute("error", "Could not load items right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }
}