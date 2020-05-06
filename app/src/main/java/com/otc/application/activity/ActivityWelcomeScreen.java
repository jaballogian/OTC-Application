package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.otc.application.R;

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
                moveToMainAcitivty();
            }
        },SPLASH_TIMED_OUT);
    }

    private void moveToMainAcitivty(){
        Intent toActivityMain = new Intent(ActivityWelcomeScreen.this, ActivityHome.class);
        toActivityMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityMain);
        finish();
    }
}
