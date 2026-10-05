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
        try {
            req.setAttribute("items", itemDAO.findAllActive());
        } catch (SQLException e) {
            log("Could not load items", e);
            req.setAttribute("error", "Could not load items right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
    }
}