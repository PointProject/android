package com.pointproject.pointproject.model;


public class MoneyPoint {
    private int id;


    private User gameUser;

    private int value;

    private double latitude;

    private double longitude;

    private int isActivated;

    private Zone zone;

    private Race race;

    public MoneyPoint() {
    }

    @Override
    public String toString() {
        return "MoneyPoint{" +
                "id=" + id +
                ", gameUser=" + gameUser +
                ", value=" + value +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isActivated=" + isActivated +
                ", zone=" + zone +
                ", race=" + race +
                '}';
    }
}
