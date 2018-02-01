package com.pointproject.pointproject.network.callback;

import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.network.response.TokenResponse;

public interface UserCallback{
    void onSuccess(Token token);

    void onError(NetworkError error);
}
