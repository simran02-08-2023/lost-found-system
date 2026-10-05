package com.lostfound.model;

import java.util.List;

public class MatchView {
    private final int matchId;
    private final double score;
    private final String label;
    private final Item lost;
    private final Item found;
    private final List<String> reasons;

    public MatchView(int matchId, double score, String label,
                     Item lost, Item found, List<String> reasons) {
        this.matchId = matchId;
        this.score = score;
        this.label = label;
        this.lost = lost;
        this.found = found;
        this.reasons = reasons;
    }

    public int getMatchId() { return matchId; }
    public double getScore() { return score; }
    public String getLabel() { return label; }
    public Item getLost() { return lost; }
    public Item getFound() { return found; }
    public List<String> getReasons() { return reasons; }
}