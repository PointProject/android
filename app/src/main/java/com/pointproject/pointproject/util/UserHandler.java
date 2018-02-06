package com.pointproject.pointproject.util;

import com.pointproject.pointproject.model.User;

public class UserHandler {

    private UserHandler(){}

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User loginUser){
        user = loginUser;
    }

}
