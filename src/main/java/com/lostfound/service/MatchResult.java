package com.lostfound.service;

import java.util.ArrayList;
import java.util.List;

/** Holds a score and the reasons behind it (shown later on the match page). */
public class MatchResult {

    private double score;
    private final List<String> reasons = new ArrayList<>();

    void add(double points, String reason) {
        if (points > 0) {
            score += points;
            reasons.add(reason);
        }
    }

    public double getScore() { return Math.round(score * 100.0) / 100.0; }
    public List<String> getReasons() { return reasons; }
}