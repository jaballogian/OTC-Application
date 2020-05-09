package com.otc.application.others;

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

    public void splitTextViewIntoTwoColors(TextView inputTextView, String inputString, int inputColor1, int inputColor2, int start1, int end1, int start2, int end2){

        SpannableString spannableString = new SpannableString(inputString);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(inputString);

        ForegroundColorSpan foregroundColorSpanBlack = new ForegroundColorSpan(inputColor1);
        ForegroundColorSpan foregroundColorSpanLightOrange = new ForegroundColorSpan(inputColor2);

        spannableStringBuilder.setSpan(foregroundColorSpanBlack, start1, end1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(foregroundColorSpanLightOrange, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        inputTextView.setText(spannableStringBuilder);
    }

    public interface FirebaseCallback{
        void onCallback(ArrayList<String> arrayList);
    }

    public ArrayList<String> readKeyArrayListFromDatabase(DatabaseReference inputDatabaseReference, final FirebaseCallback inputFirebaseCallback){

        final ArrayList<String> outputArrayList = new ArrayList<String>();
        inputDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    value = ds.getKey();
                    outputArrayList.add(value);
                }
                inputFirebaseCallback.onCallback(outputArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return outputArrayList;
    }

    public void readKeyArrayListFromDatabase(DatabaseReference inputDatabaseReference, final Spinner inputSpinner){

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

                populateArrayListToSpinner(context, inputSpinner, arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateArrayListToSpinner(Context context, Spinner inputSpinner, ArrayList<String> inputArrayList){

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, inputArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(arrayAdapter);
        inputSpinner.setSelection(0, true);
    }

    public HashMap<String, String> getAllEditTextValueAndConvertItToHashMap(HashMap<String, EditText> inputHashMap){

        HashMap<String, String> outputHashMap = new HashMap<>();
        for (Map.Entry<String, EditText> entry : inputHashMap.entrySet()){
            outputHashMap.put(entry.getKey(), entry.getValue().getText().toString());
        }
        return outputHashMap;
    }

    public HashMap<String, String> handleSpinnderOnItemClicked (final ArrayList<Spinner> inputArrayListSpinner){

        final HashMap<String, String> outputHashMap = new HashMap<>();
        for(final Spinner element: inputArrayListSpinner){

            element.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String key = element.getAdapter().getItem(0).toString();
                    String value = parent.getItemAtPosition(position).toString();
                    outputHashMap.put(key, value);

                    if(key.equals(context.getString(R.string.jenjang_sekolah))){
                        if(value.equals(context.getString(R.string.sd))){
                            readKeyArrayListFromDatabase(ActivitySignUp.bidangSDReference, ActivitySignUp.spinnerBidang);
                            Log.d("reference", ActivitySignUp.bidangSDReference.toString());
                        }
                        else if(value.equals(context.getString(R.string.smp))){
                            readKeyArrayListFromDatabase(ActivitySignUp.bidangSMPReference, ActivitySignUp.spinnerBidang);
                            Log.d("reference", ActivitySignUp.bidangSMPReference.toString());
                        }
                        else if(value.equals(context.getString(R.string.sma))){
                            readKeyArrayListFromDatabase(ActivitySignUp.bidangSMAReference, ActivitySignUp.spinnerBidang);
                            Log.d("reference", ActivitySignUp.bidangSMAReference.toString());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // TODO Auto-generated method stub
                }
            });
        }

        return outputHashMap;
    }
}
