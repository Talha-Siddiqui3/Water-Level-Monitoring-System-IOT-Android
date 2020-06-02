package com.rove.paanisystem.Misc;
/*Created by Talha Siddiqui on 29/03/2020.
 Copyright (c) 2020 Rove. All rights reserved.
*/


import android.app.Application;
import android.view.animation.AlphaAnimation;

public class MyApplicationClass extends Application {
    public static AlphaAnimation buttonClick70 = new AlphaAnimation(1F, 0.7F);
    public static AlphaAnimation buttonClick50 = new AlphaAnimation(1F, 0.5F);

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
