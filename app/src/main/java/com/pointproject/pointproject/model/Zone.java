package com.pointproject.pointproject.model;

import java.util.Set;


public class Zone {

    private int id;

    private City city;

    private String title;

    private String fillColor;

    private String strokeColor;

    private Set<Point> points;

    private Set<MoneyPoint> moneyPoints;

    public Zone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
    }

    public Set<MoneyPoint> getMoneyPoints() {
        return moneyPoints;
    }

    public void setMoneyPoints(Set<MoneyPoint> moneyPoints) {
        this.moneyPoints = moneyPoints;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", city=" + city +
                ", title='" + title + '\'' +
                ", fillColor='" + fillColor + '\'' +
                ", strokeColor='" + strokeColor + '\'' +
                ", points=" + points +
                ", moneyPoints=" + moneyPoints +
                '}';
    }
}
