package com.pointproject.pointproject.network.Event;

import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.Response.UserResponse;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class UserEvent extends BaseEvent {
    private User user;

    public UserEvent(UserResponse response){
        code = response.getCode();
        message = response.getMessage();
        user = response.getUser();
    }
}
