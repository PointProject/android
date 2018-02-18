package com.pointproject.pointproject.network.callback;

import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.network.response.NetworkError;

public interface UserTokenCallback {
    void onSuccess(Token token);

    void onError(NetworkError error);
}
