package com.lostfound.controller;

import com.lostfound.dao.ClaimDAO;
import com.lostfound.dao.ItemDAO;
import com.lostfound.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/claims")
public class ClaimServlet extends HttpServlet {

    private final ClaimDAO claimDAO = new ClaimDAO();
    private final ItemDAO itemDAO = new ItemDAO();

    /** Shows the logged-in user's own claims. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            req.setAttribute("claims", claimDAO.findByClaimant(userId));
        } catch (SQLException e) {
            log("Could not load claims", e);
            req.setAttribute("error", "Could not load your claims right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/claims.jsp").forward(req, resp);
    }

    /** Submits a new claim. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");

        int itemId;
        try {
            itemId = Integer.parseInt(req.getParameter("itemId"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String description = req.getParameter("description") == null
                ? "" : req.getParameter("description").trim();
        String back = req.getContextPath() + "/item-details?id=" + itemId + "&claimError=";

        try {
            Item item = itemDAO.findById(itemId);

            if (item == null || !"FOUND".equals(item.getType())
                    || !"ACTIVE".equals(item.getStatus())) {
                resp.sendRedirect(back + "unavailable");
            } else if (item.getUserId() == userId) {
                resp.sendRedirect(back + "own");
            } else if (description.length() < 10 || description.length() > 1000) {
                resp.sendRedirect(back + "short");
            } else if (claimDAO.hasPendingClaim(itemId, userId)) {
                resp.sendRedirect(back + "exists");
            } else {
                claimDAO.create(itemId, userId, description);
                resp.sendRedirect(req.getContextPath() + "/claims?submitted=1");
            }
        } catch (SQLException e) {
            log("Could not save claim", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
