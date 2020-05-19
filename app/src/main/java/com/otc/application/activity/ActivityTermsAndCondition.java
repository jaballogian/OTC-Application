package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.otc.application.R;
import com.otc.application.others.CommonMethods;

public class ActivityTermsAndCondition extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        toolbar = (Toolbar) findViewById(R.id.toolbarSyaratDanKetentuan);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.simpleMoveToAnotherActivity(ActivityTermsAndCondition.this, ActivitySignUp.class, true);
            }
        });
    }
}
