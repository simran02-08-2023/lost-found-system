package com.lostfound.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lostfound.model.Item;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MatchingServiceTest {

    private static final LocalDate D = LocalDate.of(2026, 10, 5);
    private final MatchingService service = new MatchingService();

    private Item item(String category, String color, String location,
                      LocalDate date, String description) {
        Item i = new Item();
        i.setCategory(category);
        i.setColor(color);
        i.setLocation(location);
        i.setItemDate(date);
        i.setDescription(description);
        return i;
    }

    @Test
    void identicalWalletsScore94() {
        Item lost = item("Wallet", "Black", "Library", D, "Black leather wallet with college ID");
        Item found = item("wallet", "black", "library", D, "black leather wallet");

        MatchResult r = service.evaluate(lost, found);

        assertEquals(94.0, r.getScore(), 0.01);
        assertTrue(r.getReasons().contains("Same category"));
        assertTrue(r.getReasons().contains("Same location"));
    }

    @Test
    void unrelatedItemsScoreZero() {
        Item lost = item("Wallet", "Black", "Library", D, "Black leather wallet");
        Item found = item("Other", "Blue", "Canteen", D.minusDays(10), "blue steel bottle");

        assertEquals(0.0, service.evaluate(lost, found).getScore(), 0.01);
    }

    @Test
    void dateScoreSteps() {
        assertEquals(20, service.dateScore(D, D), 0.01);
        assertEquals(15, service.dateScore(D, D.plusDays(1)), 0.01);
        assertEquals(10, service.dateScore(D, D.plusDays(2)), 0.01);
        assertEquals(10, service.dateScore(D, D.minusDays(3)), 0.01);
        assertEquals(0, service.dateScore(D, D.plusDays(4)), 0.01);
        assertEquals(0, service.dateScore(null, D), 0.01);
    }

    @Test
    void locationScoreSteps() {
        assertEquals(25, service.locationScore("Library", "library"), 0.01);
        assertEquals(15, service.locationScore("Central Library", "Library"), 0.01);
        assertEquals(0, service.locationScore("Library", "Canteen"), 0.01);
        assertEquals(0, service.locationScore("", "Library"), 0.01);
    }

    @Test
    void descriptionScoreIgnoresFillerWords() {
        assertEquals(9.0, service.descriptionScore(
                "Black leather wallet with college ID", "Black leather wallet"), 0.01);
        assertEquals(0.0, service.descriptionScore("red umbrella", "blue bottle"), 0.01);
        assertEquals(0.0, service.descriptionScore(null, "black wallet"), 0.01);
    }

    @Test
    void blankColorsAreNotAMatch() {
        assertEquals(0.0, service.colorScore("", ""), 0.01);
        assertEquals(0.0, service.colorScore(null, null), 0.01);
    }

    @Test
    void labelsFollowThresholds() {
        assertEquals("Strong match", service.label(94));
        assertEquals("Possible match", service.label(65));
        assertEquals("No match", service.label(40));
    }
}