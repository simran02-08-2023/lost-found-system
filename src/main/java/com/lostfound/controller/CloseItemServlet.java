package com.lostfound.controller;

import com.lostfound.dao.ItemDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/close-item")
public class CloseItemServlet extends HttpServlet {

    private final ItemDAO itemDAO = new ItemDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            boolean done = itemDAO.close(id, userId);
            resp.sendRedirect(req.getContextPath() + "/my-reports?" + (done ? "closed=1" : "failed=1"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log("Could not close item", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}