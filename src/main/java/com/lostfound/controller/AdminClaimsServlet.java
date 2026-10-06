package com.lostfound.controller;

import com.lostfound.dao.ClaimDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/claims")
public class AdminClaimsServlet extends HttpServlet {

    private final ClaimDAO claimDAO = new ClaimDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("claims", claimDAO.findPending());
        } catch (SQLException e) {
            log("Could not load pending claims", e);
            req.setAttribute("error", "Could not load claims right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/claims.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String action = req.getParameter("action");
        String base = req.getContextPath() + "/admin/claims?done=";
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if ("approve".equals(action)) {
                resp.sendRedirect(base + (claimDAO.approve(id) ? "approved" : "failed"));
            } else if ("reject".equals(action)) {
                resp.sendRedirect(base + (claimDAO.reject(id) ? "rejected" : "failed"));
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log("Claim review failed", e);
            resp.sendRedirect(base + "error");
        }
    }
}