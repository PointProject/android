package com.pointproject.pointproject.network.response;

import com.pointproject.pointproject.model.User;

public class UserResponse extends BaseResponse{

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                '}';
    }
}
