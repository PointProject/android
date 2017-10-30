package com.pointproject.pointproject.model;

import java.util.Set;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class User {

    private int id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private int money;

    private int phone;

    private int age;

    private City city;

    private Level level;

    private Set<MoneyPoint> moneyPoints;

    private Set<Race> races;

    private int expNum;

    public User(){

    }


}
