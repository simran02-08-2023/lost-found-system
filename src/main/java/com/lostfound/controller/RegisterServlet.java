package com.lostfound.controller;

import com.lostfound.dao.UserDAO;
import com.lostfound.model.User;

import com.lostfound.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
   
    private final UserDAO userDAO = new UserDAO();

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name") == null ? "" : req.getParameter("name").trim();
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
        String password = req.getParameter("password");

        String error = null;
        if (name.isEmpty()) {
            error = "Please enter your name.";
        } else if (!email.contains("@")) {
            error = "Please enter a valid email.";
        } else if (password == null || password.length() < 8) {
            error = "Password must be at least 8 characters.";
        }

        try {
            if (error == null && userDAO.findByEmail(email) != null) {
                error = "This email is already registered.";
            }
            if (error == null) {
                User u = new User();
                u.setName(name);
                u.setEmail(email);
                u.setPassword(PasswordUtil.hash(password));
                userDAO.create(u);
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        } catch (SQLException e) {
            log("Registration failed", e);
            error = "Something went wrong. Please try again.";
        }

        req.setAttribute("error", error);
        req.setAttribute("name", name);
        req.setAttribute("email", email);
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}