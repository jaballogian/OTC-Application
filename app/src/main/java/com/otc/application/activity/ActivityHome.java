package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.R;
import com.otc.application.adapter.GridProgramAdapter;
import com.otc.application.item.ItemProgram;
import com.otc.application.others.CommonMethods;

import java.util.ArrayList;

public class ActivityHome extends AppCompatActivity {

    private DatabaseReference namaProgramReference;
    private ArrayList<ItemProgram> programArrayList;
    private ArrayList<String> namaProgramArrayList;
    private CommonMethods commonMethods;
    private RecyclerView programRecylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkingUser();

        commonMethods = new CommonMethods(this);

        namaProgramReference = FirebaseDatabase.getInstance().getReference().child("Halaman").child("Home").child("Program");

        commonMethods.readKeyArrayListFromDatabase(namaProgramReference, new CommonMethods.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {

                namaProgramArrayList = new ArrayList<String>();
                namaProgramArrayList = arrayList;

                programRecylerView = (RecyclerView) findViewById(R.id.programRecylerView);

                programArrayList = new ArrayList<ItemProgram>();
                assignArrayListStringToArrayListProgram(namaProgramArrayList, programArrayList);
                setValueToRecylerView();
            }
        });
    }

    private void checkingUser(){

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            moveToActivitySignIn();
        }
    }

    private void moveToActivitySignIn(){
        Intent toActivitySignIn = new Intent(ActivityHome.this, ActivitySignIn.class);
        toActivitySignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivitySignIn);
        finish();
    }

    private void assignArrayListStringToArrayListProgram(ArrayList<String> inputNamaProgramArrayList, ArrayList<ItemProgram> inputItemProgramArrayList){
        for(int i = 0; i < inputNamaProgramArrayList.size(); i++){
            ItemProgram itemProgram = new ItemProgram();
            itemProgram.setNamaProgram(inputNamaProgramArrayList.get(i));
            inputItemProgramArrayList.add(itemProgram);
        }
    }

    private void setValueToRecylerView(){
        programRecylerView.setLayoutManager(new GridLayoutManager(this, 3));
        GridProgramAdapter gridProgramAdapter = new GridProgramAdapter(programArrayList);
        programRecylerView.setAdapter(gridProgramAdapter);
    }
}
