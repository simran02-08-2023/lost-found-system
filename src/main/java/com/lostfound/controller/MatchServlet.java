package com.lostfound.controller;

import com.lostfound.dao.ItemDAO;
import com.lostfound.dao.MatchDAO;
import com.lostfound.model.Item;
import com.lostfound.model.Match;
import com.lostfound.model.MatchView;

import com.lostfound.service.MatchFinderService;
import com.lostfound.service.MatchResult;
import com.lostfound.service.MatchingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet("/matches")
public class MatchServlet extends HttpServlet {

    private final MatchDAO matchDAO = new MatchDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final MatchingService scoring = new MatchingService();
    private final MatchFinderService finder = new MatchFinderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            List<MatchView> views = new ArrayList<>();
            for (Match m : matchDAO.findByUser(userId)) {
                Item lost = itemDAO.findById(m.getLostItemId());
                Item found = itemDAO.findById(m.getFoundItemId());
                if (lost == null || found == null) continue;

                // recompute so edited items always show an up-to-date score
                MatchResult r = scoring.evaluate(lost, found);
                views.add(new MatchView(m.getId(), r.getScore(),
                        scoring.label(r.getScore()), lost, found, r.getReasons()));
            }
            views.sort(Comparator.comparingDouble(MatchView::getScore).reversed());
            req.setAttribute("matches", views);
        } catch (SQLException e) {
            log("Could not load matches", e);
            req.setAttribute("error", "Could not load matches right now.");
        }
        req.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        String action = req.getParameter("action");

        try {
            if ("recheck".equals(action)) {
                for (Item item : itemDAO.findByUser(userId)) {
                    if ("ACTIVE".equals(item.getStatus())) {
                        finder.findMatchesFor(item);
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/matches?checked=1");
                return;
            }
            if ("reject".equals(action)) {
                matchDAO.reject(Integer.parseInt(req.getParameter("id")), userId);
                resp.sendRedirect(req.getContextPath() + "/matches?rejected=1");
                return;
            }
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            log("Match action failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}