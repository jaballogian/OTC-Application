package com.otc.application;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    public ArrayList<String> readArrayListFromDatabase(DatabaseReference inputDatabaseReference){

        final ArrayList<String> arrayList = new ArrayList<String>();
        inputDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value;
                arrayList.add(dataSnapshot.getKey());
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    value = ds.getKey();
                    arrayList.add(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return arrayList;
    }

    public void populateArrayListToSpinner(Context context, Spinner inputSpinner, ArrayList<String> inputArrayList){

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, inputArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(arrayAdapter);
        inputSpinner.setSelection(0, true);
    }
}
