package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.otc.application.R;
import com.otc.application.others.CommonMethods;

public class ActivityWelcomeScreen extends AppCompatActivity {

    private static int SPLASH_TIMED_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                CommonMethods.simpleMoveToAnotherActivity(ActivityWelcomeScreen.this, ActivityHome.class, true);
            }
        },SPLASH_TIMED_OUT);
    }
}
