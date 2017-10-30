package com.pointproject.pointproject.model;

import java.util.Set;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class City {

    private int id;

    private String title;

    private Country country;

    private Set<Zone> zones;

    public City() {
    }
}
