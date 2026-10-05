package com.lostfound.service;

import com.lostfound.dao.ItemDAO;
import com.lostfound.dao.MatchDAO;
import com.lostfound.model.Item;
import java.sql.SQLException;
import java.util.List;

/** Compares one item against all active items of the opposite type. */
public class MatchFinderService {

    // Real users should not match their own items. It is false for now so you
    // can test with one account. Set it to true once you test with two accounts.
    private static final boolean SKIP_OWN_ITEMS = false;

    private final ItemDAO itemDAO = new ItemDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final MatchingService scoring = new MatchingService();

    /** Returns how many NEW matches were saved. */
    public int findMatchesFor(Item item) throws SQLException {
        boolean isLost = "LOST".equals(item.getType());
        List<Item> candidates = itemDAO.findActiveByType(isLost ? "FOUND" : "LOST");

        int saved = 0;
        for (Item other : candidates) {
            if (SKIP_OWN_ITEMS && other.getUserId() == item.getUserId()) continue;

            Item lost = isLost ? item : other;
            Item found = isLost ? other : item;

            double score = scoring.evaluate(lost, found).getScore();
            if (score >= MatchingService.POSSIBLE_THRESHOLD
                    && matchDAO.saveIfNew(lost.getId(), found.getId(), score)) {
                saved++;
            }
        }
        return saved;
    }
}