package com.pointproject.pointproject.model;

import java.util.Set;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class Race {

    private int id;

    private Set<User> gameUsers;

    private MoneyPoint moneyPoint;

    private String startTime;

    private String duration;

    public Race() {
    }
}
