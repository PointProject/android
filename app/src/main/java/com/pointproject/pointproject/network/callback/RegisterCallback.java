package com.pointproject.pointproject.network.callback;


import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.network.response.UserResponse;

public interface RegisterCallback {
    void onSuccess(User user);

    void onError(NetworkError error);
}
