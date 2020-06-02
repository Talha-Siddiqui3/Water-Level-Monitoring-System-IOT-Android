package com.rove.paanisystem;

public interface RequestParamListener {
    void getDistance(String distance);

    void onConnect();

    void onFailure(String reason);

    void onDisconnect();

}
