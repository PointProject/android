package com.pointproject.pointproject.network.Response;

/**
 * Created by xdewnik on 30.10.2017.
 */

public class BaseResponse {
    private String message;
    private int code;

    public BaseResponse(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
