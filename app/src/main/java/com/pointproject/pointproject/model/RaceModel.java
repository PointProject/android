package com.pointproject.pointproject.model;


public class RaceModel {
    private Integer participants;
    private Double reward;
    private Double area;
    private Double timeAlive;
    private Double startTime;

    public RaceModel(Integer participants, Double reward, Double area, Double timeAlive, Double startTime) {
        this.participants = participants;
        this.reward = reward;
        this.area = area;
        this.timeAlive = timeAlive;
        this.startTime = startTime;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getTimeAlive() {
        return timeAlive;
    }

    public void setTimeAlive(Double timeAlive) {
        this.timeAlive = timeAlive;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }
}
