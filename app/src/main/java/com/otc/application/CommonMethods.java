package com.otc.application;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

public class CommonMethods {

    private Context context;

    public CommonMethods(Context context){
        this.context = context;
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
}
