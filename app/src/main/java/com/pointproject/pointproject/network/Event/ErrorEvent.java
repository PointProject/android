package com.pointproject.pointproject.network.Event;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class ErrorEvent {

    private int code;
    private String message;

    public ErrorEvent(){

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
}
