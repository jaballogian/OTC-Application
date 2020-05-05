package com.otc.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class ActivitySignIn extends AppCompatActivity {

    private TextView textViewBelumPunyaAkun;
    private CommonMethods commonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        commonMethods = new CommonMethods(this);

        textViewBelumPunyaAkun = (TextView) findViewById(R.id.textViewBelumPunyaAkun);
        commonMethods.splitTextViewIntoTwoColors(textViewBelumPunyaAkun, getString(R.string.belum_punya_akun_ayo_daftar),
                getResources().getColor(R.color.black), getResources().getColor(R.color.light_orange), 0, 17, 18, 29);

        textViewBelumPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivitySignUp();
            }
        });
    }

    private void moveToActivitySignUp(){
        Intent intent = new Intent(ActivitySignIn.this, ActivitySignUp.class);
        startActivity(intent);
    }
}