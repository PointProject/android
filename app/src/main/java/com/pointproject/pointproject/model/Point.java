package com.pointproject.pointproject.model;

public class Point {
    private int id;

    private Zone zone;

    private double latitude;

    private double longitude;

    private int numberInSequence;

    public Point() {
    }

    public int getId() {
        return id;
    }

    public Zone getZone() {
        return zone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getNumberInSequence() {
        return numberInSequence;
    }

    public void setNumberInSequence(int numberInSequence) {
        this.numberInSequence = numberInSequence;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", zone=" + zone +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", number in sequence=" + numberInSequence +
                '}';
    }
}
