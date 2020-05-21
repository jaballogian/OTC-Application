package com.otc.application.others;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.otc.application.R;
import com.otc.application.activity.ActivitySignUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommonMethods {

    private Context context;

    public CommonMethods(Context context){
        this.context = context;
    }

    public void blockScreenCapture(Activity inputActivity){
        inputActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void splitTextViewIntoTwoColors(TextView inputTextView, String inputString, int inputColor1, int inputColor2, int start1, int end1, int start2, int end2){

        SpannableString spannableString = new SpannableString(inputString);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(inputString);

        ForegroundColorSpan foregroundColorSpanBlack = new ForegroundColorSpan(inputColor1);
        ForegroundColorSpan foregroundColorSpanLightOrange = new ForegroundColorSpan(inputColor2);

        spannableStringBuilder.setSpan(foregroundColorSpanBlack, start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(foregroundColorSpanLightOrange, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        inputTextView.setText(spannableStringBuilder);
    }

    public void setProgressDialog(Context context, ProgressDialog inputProgressDialog){

        inputProgressDialog.setTitle(R.string.sedang_memproses);
        inputProgressDialog.setMessage(context.getString(R.string.mohon_tunggu));
        inputProgressDialog.setCanceledOnTouchOutside(false);
        inputProgressDialog.show();
    }

    public static void simpleMoveToAnotherActivity(Context sourceActivity, Class destinationActivity, boolean inputFlagState){
        Intent intent = new Intent(sourceActivity, destinationActivity);
        if(inputFlagState){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        sourceActivity.startActivity(intent);
        ((Activity) sourceActivity).finish();
    }
}
