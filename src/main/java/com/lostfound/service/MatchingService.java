package com.lostfound.service;

import com.lostfound.model.Item;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MatchingService {

    public static final double STRONG_THRESHOLD = 75;
    public static final double POSSIBLE_THRESHOLD = 60;

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "with", "and", "of", "in", "on", "at", "to",
            "is", "it", "my", "has", "have", "was", "for", "near", "this", "that");

    /** Compares one LOST item with one FOUND item. */
    public MatchResult evaluate(Item lost, Item found) {
        MatchResult r = new MatchResult();

        r.add(categoryScore(lost.getCategory(), found.getCategory()), "Same category");
        r.add(colorScore(lost.getColor(), found.getColor()), "Same color");

        double loc = locationScore(lost.getLocation(), found.getLocation());
        r.add(loc, loc >= 25 ? "Same location" : "Nearby location");

        double date = dateScore(lost.getItemDate(), found.getItemDate());
        r.add(date, date >= 20 ? "Same date" : "Similar date");

        r.add(descriptionScore(lost.getDescription(), found.getDescription()),
                "Similar description");
        return r;
    }

    public double categoryScore(String a, String b) {
        return sameText(a, b) ? 25 : 0;
    }

    public double colorScore(String a, String b) {
        return sameText(a, b) ? 15 : 0;
    }

    public double locationScore(String a, String b) {
        String x = normalize(a);
        String y = normalize(b);
        if (x.isEmpty() || y.isEmpty()) return 0;
        if (x.equals(y)) return 25;
        if (x.contains(y) || y.contains(x)) return 15;   // "central library" vs "library"
        return 0;
    }

    public double dateScore(LocalDate a, LocalDate b) {
        if (a == null || b == null) return 0;
        long days = Math.abs(ChronoUnit.DAYS.between(a, b));
        if (days == 0) return 20;
        if (days == 1) return 15;
        if (days <= 3) return 10;
        return 0;
    }

    public double descriptionScore(String a, String b) {
        Set<String> x = keywords(a);
        Set<String> y = keywords(b);
        if (x.isEmpty() || y.isEmpty()) return 0;

        Set<String> common = new HashSet<>(x);
        common.retainAll(y);
        return (common.size() * 15.0) / Math.max(x.size(), y.size());
    }

    public String label(double score) {
        if (score >= STRONG_THRESHOLD) return "Strong match";
        if (score >= POSSIBLE_THRESHOLD) return "Possible match";
        return "No match";
    }

    // ---------- helpers ----------

    private static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

    /** Two empty values are NOT a match. */
    private static boolean sameText(String a, String b) {
        String x = normalize(a);
        return !x.isEmpty() && x.equals(normalize(b));
    }

    private static Set<String> keywords(String text) {
        return Arrays.stream(normalize(text).split("[^a-z0-9]+"))
                .filter(w -> w.length() >= 2)
                .filter(w -> !STOP_WORDS.contains(w))
                .collect(Collectors.toSet());
    }
}