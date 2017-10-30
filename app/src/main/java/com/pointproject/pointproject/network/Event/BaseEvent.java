package com.pointproject.pointproject.network.Event;

import com.pointproject.pointproject.network.Response.BaseResponse;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class BaseEvent {
    public int code;
    public String message;

    public BaseEvent(){

    }

    public BaseEvent(BaseResponse response){
        this.code = response.getCode();
        this.message = response.getMessage();
    }
}
