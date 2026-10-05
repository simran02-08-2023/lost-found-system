package com.lostfound.controller;

import com.lostfound.dao.ItemDAO;
import com.lostfound.model.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/item-details")
public class ItemDetailsServlet extends HttpServlet {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Item item = itemDAO.findById(id);
            int myId = (Integer) req.getSession().getAttribute("userId");
            boolean isOwner = item != null && item.getUserId() == myId;

            // hide closed/claimed items from everyone except the owner
            if (item == null || (!"ACTIVE".equals(item.getStatus()) && !isOwner)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            req.setAttribute("item", item);
            req.setAttribute("isOwner", isOwner);
            req.getRequestDispatcher("/WEB-INF/views/item-details.jsp").forward(req, resp);

        } catch (SQLException e) {
            log("Could not load item", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}