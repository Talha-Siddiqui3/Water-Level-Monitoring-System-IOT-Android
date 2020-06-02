package com.rove.paanisystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.rove.paanisystem.Misc.Common_Util;
import com.rove.paanisystem.Misc.MyApplicationClass;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MainActivity extends MyBaseClass {
    private OkHttpClient client;
    private EchoWebSocketListener listener;
    private WebSocket ws;
    private RelativeLayout startRl, stopRl;
    private final float MAX_HEIGHT = 132f;
    private WaveLoadingView waveLoadingView;
    private Common_Util cu = new Common_Util();
    private boolean connected = false;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private RelativeLayout rootRl;
    private boolean currentClosingOurselves = false;
    private boolean currentlyAnimating = true;
    private AudioManager am;
    private MediaPlayer player;
    private boolean canPLayAlarm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootRl = findViewById(R.id.root_rl);
        addCommonViews(rootRl, this);
        hideProgressBar();
        waveLoadingView = findViewById(R.id.waveLoadingView);
        //waveLoadingView.endAnimation();
        rootRl = findViewById(R.id.root_rl);
        startRl = findViewById(R.id.start_rl);
        stopRl = findViewById(R.id.stop_rl);
        client = new OkHttpClient();
        startRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(MyApplicationClass.buttonClick70);
                showLoadingView();
                startWebSocket();
                initializeServerListener();
            }
        });
        stopRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(MyApplicationClass.buttonClick70);
                if (connected) {
                    //ws.send("sleep");
                    // connected=false;
                    ws.close(NORMAL_CLOSURE_STATUS, null);
                    currentClosingOurselves = true;
                    cu.ToasterShort(MainActivity.this, "Monitoring system stopped successfully :)");
                    setNotAvailable();
                } else {
                    cu.ToasterShort(MainActivity.this, "Your monitoring system is already stopped");
                }
            }
        });
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        player = MediaPlayer.create(this, notification);
        setPlayer(player);

    }


    private void resetAlarm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canPLayAlarm = true;
            }
        }, 600000);
    }


    private void startAlarm() {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        player.setLooping(true);
        player.start();
    }


    private void showOverFlowAlertBox() {
        if (canPLayAlarm) {
            canPLayAlarm = false;
            resetAlarm();
            startAlarm();
            showCustomError("WARNING: Water is about to overflow, please turn off your motor.");
        }

    }

    private void showWaterLevelTooLowAlertBox() {
        if (canPLayAlarm) {
            canPLayAlarm = false;
            resetAlarm();
            startAlarm();
            showCustomError("WARNING: Water levels are too low, please turn on your motor.");
        }
    }


    private void startWebSocket() {
        client.connectionPool().evictAll();
        Request request = new Request.Builder().url("ws://192.168.0.103:81/").build();
        listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
    }

    private void initializeServerListener() {
        listener.requestParamListener = new RequestParamListener() {
            @Override
            public void getDistance(String distance) {
                String percentage;
                float currentValue = 0;
                if (distance.equals("0")) {
                    percentage = "N/A";
                } else {
                    float receivedValueTemp = Float.parseFloat(distance);
                    //Log.i("ASFDASFASF", String.valueOf(receivedValueTemp));
                    /*if (receivedValueTemp > MAX_HEIGHT) {
                        receivedValueTemp = MAX_HEIGHT;
                    }
                    currentValue = (1 - (receivedValueTemp / MAX_HEIGHT)) * 100;*/
                    currentValue = (float) ((receivedValueTemp - MAX_HEIGHT) / -1.2289);
                    percentage = currentValue + "%";
                    if (currentValue < 25 || currentValue > 80) {
                        setRedColour();
                    } else {
                        setGreenColour();
                    }

                    if (currentValue < 20) {
                        showWaterLevelTooLowAlertBox();
                    }

                    if (currentValue > 94) {
                        showOverFlowAlertBox();
                    }
                }
                updatePercentage(percentage, currentValue);
            }

            @Override
            public void onConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connected = true;
                        //waveLoadingView.startAnimation();
                        hideLoadingView();
                    }
                });
            }

            @Override
            public void onFailure(String reason) {
                if (!currentClosingOurselves) {
                    hideLoadingView();
                    String error;
                    if (connected) {
                        error = "Connection to sensor interrupted due to an unknown error.\nPlease try again in a while";
                        setNotAvailable();
                    } else {
                        error = "Could not connect to the water sensor.\nPlease try again in a while";
                    }
                    showCustomError(error);
                    connected = false;
                } else {
                    currentClosingOurselves = false;
                }
            }

            @Override
            public void onDisconnect() {
                connected = false;
            }
        };
    }


    private void updatePercentage(final String percentage, final float progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waveLoadingView.setCenterTitle(percentage);
                if (!percentage.equals("N/A")) {
                    waveLoadingView.setProgressValue(Math.round(progress));
                    if (progress == 0 && currentlyAnimating) {
                        waveLoadingView.setAmplitudeRatio(0);
                        waveLoadingView.endAnimation();
                        currentlyAnimating = false;
                    } else if (progress != 0 && !currentlyAnimating) {
                        waveLoadingView.setAmplitudeRatio(50);
                        waveLoadingView.startAnimation();
                        currentlyAnimating = true;
                    }
                }
            }
        });
    }

    private void setNotAvailable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waveLoadingView.setCenterTitleColor(Color.parseColor("#FFA500"));
                waveLoadingView.setAmplitudeRatio(50);
                waveLoadingView.startAnimation();
                waveLoadingView.setProgressValue(50);
                waveLoadingView.setCenterTitle("N/A");
                waveLoadingView.invalidate();
            }
        });
    }


    private void setRedColour() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waveLoadingView.setCenterTitleColor(Color.parseColor("#ff3838"));
            }
        });
    }

    private void setGreenColour() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waveLoadingView.setCenterTitleColor(Color.parseColor("#00ff00"));
            }
        });
    }


}
