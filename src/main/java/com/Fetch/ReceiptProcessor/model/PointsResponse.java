package com.Fetch.ReceiptProcessor.model;

public class PointsResponse {
    private Integer points;

    // Constructors
    public PointsResponse(Integer points) {
        this.points = points;
    }

    public PointsResponse(String id, Integer points) {
        this.points = points;
    }

    // Getters and Setters
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
