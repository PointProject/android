package com.pointproject.pointproject.network.callback;


import com.pointproject.pointproject.model.Zone;
import com.pointproject.pointproject.network.response.NetworkError;

import java.util.List;

public interface GetZoneCallback {
    void onSuccess(List<Zone> zones);

    void onError(NetworkError error);
}
