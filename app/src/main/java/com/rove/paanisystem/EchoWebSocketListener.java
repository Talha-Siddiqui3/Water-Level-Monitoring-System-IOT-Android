package com.rove.paanisystem;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    public RequestParamListener requestParamListener;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send("WORK");
        requestParamListener.onConnect();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
      requestParamListener.getDistance(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
       requestParamListener.onDisconnect();

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i("AFASFASFASF",t.getMessage() + response);
        requestParamListener.onFailure(t.getMessage());
    }
}

