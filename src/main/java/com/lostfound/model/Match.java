package com.lostfound.model;

public class Match {
    private int id;
    private int lostItemId;
    private int foundItemId;
    private double score;
    private String status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLostItemId() { return lostItemId; }
    public void setLostItemId(int lostItemId) { this.lostItemId = lostItemId; }
    public int getFoundItemId() { return foundItemId; }
    public void setFoundItemId(int foundItemId) { this.foundItemId = foundItemId; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}