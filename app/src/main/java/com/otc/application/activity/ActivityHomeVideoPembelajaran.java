package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.R;
import com.otc.application.adapter.GridProgramAdapter;
import com.otc.application.adapter.ListVideoPembelajaranAdapter;
import com.otc.application.item.ItemProgram;
import com.otc.application.item.ItemVideoPembelajaran;
import com.otc.application.others.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityHomeVideoPembelajaran extends AppCompatActivity {

    private DatabaseReference videoPembelajaranReference;
    private HashMap<String, String> userDataHashMap;
    private ArrayList<String> babArrayList;
    private CommonMethods commonMethods;
    private RecyclerView babRecylerView;
    private ArrayList<ItemVideoPembelajaran> videoPembelajaranArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_video_pembelajaran);

        commonMethods = new CommonMethods(this);

        userDataHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("userDataHashMap");
        videoPembelajaranReference = FirebaseDatabase.getInstance().getReference().child("Peta Materi").child("KSN")
                .child(userDataHashMap.get(getString(R.string.jenjang_sekolah)))
                .child(userDataHashMap.get(getString(R.string.bidang)).toUpperCase());

        babArrayList = new ArrayList<String>();
        commonMethods.readKeyArrayListFromDatabase(videoPembelajaranReference, new CommonMethods.FirebaseCallbackArrayList() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {
                babArrayList = arrayList;

                babRecylerView =(RecyclerView) findViewById(R.id.videoRecylerView);

                videoPembelajaranArrayList = new ArrayList<ItemVideoPembelajaran>();
                assignArrayListStringToArrayListProgram(babArrayList, videoPembelajaranArrayList);
                setValueToRecylerView();
            }
        });
    }

    private void assignArrayListStringToArrayListProgram(ArrayList<String> inputSubBabArrayList, ArrayList<ItemVideoPembelajaran> inputItemProgramArrayList){
        for(int i = 0; i < inputSubBabArrayList.size(); i++){
            ItemVideoPembelajaran itemVideoPembelajaran = new ItemVideoPembelajaran();
            itemVideoPembelajaran.setSubBabVideo(inputSubBabArrayList.get(i));
            inputItemProgramArrayList.add(itemVideoPembelajaran);
        }
    }

    private void setValueToRecylerView(){
        babRecylerView.setLayoutManager(new LinearLayoutManager(this));
        ListVideoPembelajaranAdapter listVideoPembelajaranAdapter = new ListVideoPembelajaranAdapter(videoPembelajaranArrayList);
        babRecylerView.setAdapter(listVideoPembelajaranAdapter);
    }
}
