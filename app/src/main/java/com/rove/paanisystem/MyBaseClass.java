package com.rove.paanisystem;
/*Created by Talha Siddiqui on 29/03/2020.
 Copyright (c) 2020 Rove. All rights reserved.
*/


import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rove.paanisystem.CustomViews.AlertBox;
import com.rove.paanisystem.CustomViews.AlertBoxButtonListener;
import com.rove.paanisystem.CustomViews.LoadingView;


public abstract class MyBaseClass extends AppCompatActivity {
    private RelativeLayout rootRl;
    private AlertBox alertBox;
    private Activity context;
    private LoadingView loadingView;
    private ProgressBar pb;
    private MediaPlayer player;

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void addCommonViews(RelativeLayout rootRl, Activity context) {
        this.rootRl = rootRl;
        this.context = context;
        addProgressBar();
        addLoadingView();
        addAlertBox();
    }


    private void addAlertBox() {
        alertBox = new AlertBox(context);
        rootRl.addView(alertBox, rootRl.getChildCount());
        alertBox.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    private void addLoadingView() {
        loadingView = new LoadingView(context);
        rootRl.addView(loadingView);
        loadingView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        hideLoadingView();
    }

    private void addProgressBar() {
        pb = new ProgressBar(context);
        rootRl.addView(pb);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) pb.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        pb.setLayoutParams(layoutParams);
        pb.setElevation(30);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void showLoadingView() {
        loadingView.showLoading();
    }

    public void hideLoadingView() {
        loadingView.hideLoading();
    }

    public void showProgressBar() {
        pb.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void hideProgressBar() {
        pb.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void showCustomError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoadingView();
                hideProgressBar();
                alertBox.setDialogType(true);
                alertBox.setDialogMessage(message);
                alertBox.showDialog();
                alertBox.alertBoxButtonListener = new AlertBoxButtonListener() {
                    @Override
                    public void cancelButtonLeftClick() {

                    }

                    @Override
                    public void okButtonRightClick() {

                    }

                    @Override
                    public void okButtonCentreClick() {
                        alertBox.hideDialog();
                        stopAlarm();
                    }
                };
            }
        });


    }


    private void stopAlarm() {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }

    public AlertBox getAlertBox() {
        return alertBox;
    }


}
