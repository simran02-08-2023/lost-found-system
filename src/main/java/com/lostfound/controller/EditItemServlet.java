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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/edit-item")
public class EditItemServlet extends HttpServlet {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Item item = itemDAO.findById(id);

            // not found, not yours, or not ACTIVE -> pretend it does not exist
            if (item == null || item.getUserId() != userId
                    || !"ACTIVE".equals(item.getStatus())) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("item", item);
            req.getRequestDispatcher("/WEB-INF/views/edit-item.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            log("Could not load item", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");

        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // rebuild the item from the form, so errors can show what was typed
        Item item = new Item();
        item.setId(id);
        item.setUserId(userId);
        item.setTitle(clean(req.getParameter("title")));
        item.setCategory(clean(req.getParameter("category")));
        item.setDescription(clean(req.getParameter("description")));
        item.setColor(clean(req.getParameter("color")));
        item.setLocation(clean(req.getParameter("location")));

        String error = null;
        try {
            item.setItemDate(LocalDate.parse(clean(req.getParameter("itemDate"))));
            if (item.getItemDate().isAfter(LocalDate.now())) {
                error = "The date cannot be in the future.";
            }
        } catch (DateTimeParseException e) {
            error = "Please enter a valid date.";
        }

        if (error == null) {
            if (item.getTitle().isEmpty() || item.getTitle().length() > 150) {
                error = "Please enter a title (max 150 characters).";
            } else if (item.getCategory().isEmpty()) {
                error = "Please choose a category.";
            } else if (item.getLocation().isEmpty()) {
                error = "Please enter the location.";
            }
        }

        if (error == null) {
            try {
                if (itemDAO.update(item)) {
                    resp.sendRedirect(req.getContextPath() + "/my-reports?updated=1");
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            } catch (SQLException e) {
                log("Could not update item", e);
                error = "Something went wrong. Please try again.";
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("item", item);
        req.getRequestDispatcher("/WEB-INF/views/edit-item.jsp").forward(req, resp);
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }
}