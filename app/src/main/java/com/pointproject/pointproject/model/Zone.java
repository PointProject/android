package com.pointproject.pointproject.model;

import java.util.Set;

/**
 * Created by xdewnik on 30.10.2017.
 */

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
}
