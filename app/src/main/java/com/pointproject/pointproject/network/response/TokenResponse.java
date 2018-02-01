package com.pointproject.pointproject.network.response;

import com.pointproject.pointproject.model.Token;

public class TokenResponse extends BaseResponse{

    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
