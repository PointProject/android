package com.pointproject.pointproject.network.Response;

import com.pointproject.pointproject.model.User;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class UserResponse {
    private int code;
    private String message;
    private User user;

    public UserResponse(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
