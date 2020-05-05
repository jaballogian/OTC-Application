package com.otc.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkingUser();
    }

    private void checkingUser(){

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            moveToActivitySignIn();
        }
    }

    private void moveToActivitySignIn(){
        Intent toActivitySignIn = new Intent(ActivityHome.this, ActivitySignUp.class);
        toActivitySignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivitySignIn);
        finish();
    }
}
