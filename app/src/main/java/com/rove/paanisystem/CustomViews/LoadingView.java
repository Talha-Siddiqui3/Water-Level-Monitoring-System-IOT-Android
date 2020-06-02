package com.rove.paanisystem.CustomViews;
/*Created by Talha Siddiqui on 29/03/2020.
 Copyright (c) 2020 Rove. All rights reserved.
*/


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.rove.paanisystem.Misc.Common_Util;
import com.rove.paanisystem.R;


public class LoadingView extends FrameLayout {
    private View rootView;
    private ObjectAnimator scaleAnim;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.loading_layout, this);
        scaleAnim = new Common_Util().getLoadingScaleAnim(findViewById(R.id.waveLoadingView));
    }


    public void showLoading() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootView.setVisibility(View.VISIBLE);
                rootView.setElevation(35);
                scaleAnim.start();
            }
        });

    }

    public void hideLoading() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootView.setVisibility(View.INVISIBLE);
                scaleAnim.end();
            }
        });

    }


}
