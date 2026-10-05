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

@WebServlet("/report-item")
public class ReportItemServlet extends HttpServlet {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/report-item.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String type = clean(req.getParameter("type"));
        String title = clean(req.getParameter("title"));
        String category = clean(req.getParameter("category"));
        String description = clean(req.getParameter("description"));
        String color = clean(req.getParameter("color"));
        String location = clean(req.getParameter("location"));
        String dateText = clean(req.getParameter("itemDate"));

        String error = null;
        LocalDate date = null;

        if (!type.equals("LOST") && !type.equals("FOUND")) {
            error = "Please choose Lost or Found.";
        } else if (title.isEmpty() || title.length() > 150) {
            error = "Please enter a title (max 150 characters).";
        } else if (category.isEmpty()) {
            error = "Please choose a category.";
        } else if (location.isEmpty()) {
            error = "Please enter the location.";
        } else {
            try {
                date = LocalDate.parse(dateText);
                if (date.isAfter(LocalDate.now())) {
                    error = "The date cannot be in the future.";
                }
            } catch (DateTimeParseException e) {
                error = "Please enter a valid date.";
            }
        }

        if (error == null) {
            try {
                Item item = new Item();
                item.setUserId((Integer) req.getSession().getAttribute("userId"));
                item.setType(type);
                item.setTitle(title);
                item.setCategory(category);
                item.setDescription(description);
                item.setColor(color);
                item.setLocation(location);
                item.setItemDate(date);
                itemDAO.create(item);

                resp.sendRedirect(req.getContextPath() + "/items?reported=1");
                return;
            } catch (SQLException e) {
                log("Could not save item", e);
                error = "Something went wrong. Please try again.";
            }
        }

        req.setAttribute("error", error);
        req.setAttribute("type", type);
        req.setAttribute("title", title);
        req.setAttribute("category", category);
        req.setAttribute("description", description);
        req.setAttribute("color", color);
        req.setAttribute("location", location);
        req.setAttribute("itemDate", dateText);
        req.getRequestDispatcher("/WEB-INF/views/report-item.jsp").forward(req, resp);
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }
}