package com.lostfound.controller;

import com.lostfound.dao.ItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/my-reports")
public class MyReportsServlet extends HttpServlet {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            req.setAttribute("items", itemDAO.findByUser(userId));
        } catch (SQLException e) {
            log("Could not load reports", e);
            req.setAttribute("error", "Could not load your reports right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/my-reports.jsp").forward(req, resp);
    }
}