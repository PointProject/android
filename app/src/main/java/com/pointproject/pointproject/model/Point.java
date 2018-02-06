package com.pointproject.pointproject.model;

public class Point {
    private int id;

    private Zone zone;

    private double latitude;

    private double longitude;

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

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", zone=" + zone +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
