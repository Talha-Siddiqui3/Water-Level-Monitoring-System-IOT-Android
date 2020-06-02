package com.rove.paanisystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                i=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.book_open_splashscreen,R.anim.fakeanim);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
