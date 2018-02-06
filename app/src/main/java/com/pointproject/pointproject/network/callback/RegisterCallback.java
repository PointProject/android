package com.pointproject.pointproject.network.callback;


import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.response.NetworkError;

public interface RegisterCallback {
    void onSuccess(User user);

    void onError(NetworkError error);
}
