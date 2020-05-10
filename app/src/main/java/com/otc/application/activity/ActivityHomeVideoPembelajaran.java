package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    private String urlVideoPembelajaran, keyDatabaseReference, subbabDatabaseReference, materiDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_video_pembelajaran);

        commonMethods = new CommonMethods(this);

        userDataHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("userDataHashMap");
        keyDatabaseReference = getIntent().getExtras().getString("keyDatabaseReference");

        urlVideoPembelajaran = getIntent().getExtras().getString("databaseReference");
        if(keyDatabaseReference.equals("bab")){
            videoPembelajaranReference = FirebaseDatabase.getInstance().getReference().child("Peta Materi").child("KSN")
                    .child(userDataHashMap.get(getString(R.string.jenjang_sekolah)))
                    .child(userDataHashMap.get(getString(R.string.bidang)).toUpperCase());
        }
        else if(keyDatabaseReference.equals("subbab")){

            subbabDatabaseReference = getIntent().getExtras().getString("subbabDatabaseReference");
            videoPembelajaranReference = FirebaseDatabase.getInstance().getReference().child("Peta Materi").child("KSN")
                    .child(userDataHashMap.get(getString(R.string.jenjang_sekolah)))
                    .child(userDataHashMap.get(getString(R.string.bidang)).toUpperCase())
                    .child(subbabDatabaseReference);
        }
        else if(keyDatabaseReference.equals("materi")){
            subbabDatabaseReference = getIntent().getExtras().getString("subbabDatabaseReference");
            materiDatabaseReference = getIntent().getExtras().getString("materiDatabaseReference");
            videoPembelajaranReference = FirebaseDatabase.getInstance().getReference().child("Peta Materi").child("KSN")
                    .child(userDataHashMap.get(getString(R.string.jenjang_sekolah)))
                    .child(userDataHashMap.get(getString(R.string.bidang)).toUpperCase())
                    .child(subbabDatabaseReference)
                    .child(materiDatabaseReference)
                    .child(getString(R.string.video_pembelajaran).toUpperCase())
                    .child(getString(R.string.provinsi).toUpperCase());
        }

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

        listVideoPembelajaranAdapter.setOnItemClickCallback(new ListVideoPembelajaranAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ItemVideoPembelajaran itemVideoPembelajaran) {
                restartActivity(itemVideoPembelajaran);
            }
        });
    }

    private void restartActivity(ItemVideoPembelajaran itemVideoPembelajaran){

        if(keyDatabaseReference.equals("bab")){
            Intent intent = new Intent(ActivityHomeVideoPembelajaran.this, ActivityHomeVideoPembelajaran.class);
            intent.putExtra("userDataHashMap", userDataHashMap);
            keyDatabaseReference = "subbab";
            intent.putExtra("keyDatabaseReference", keyDatabaseReference);
            intent.putExtra("subbabDatabaseReference", itemVideoPembelajaran.getSubBabVideo());
            Log.d("keyDatabaseReference", keyDatabaseReference);
            startActivity(intent);
        }
        else if(keyDatabaseReference.equals("subbab")){
            Intent intent = new Intent(ActivityHomeVideoPembelajaran.this, ActivityHomeVideoPembelajaran.class);
            intent.putExtra("userDataHashMap", userDataHashMap);
            keyDatabaseReference = "materi";
            intent.putExtra("keyDatabaseReference", keyDatabaseReference);
            intent.putExtra("subbabDatabaseReference", subbabDatabaseReference);
            intent.putExtra("materiDatabaseReference", itemVideoPembelajaran.getSubBabVideo());
            Log.d("keyDatabaseReference", keyDatabaseReference);
            startActivity(intent);
        }
        else if(keyDatabaseReference.equals("materi")){
            Intent intent1 = new Intent(ActivityHomeVideoPembelajaran.this, ActivityHomeVideoWatchPembelajaran.class);
            keyDatabaseReference = "video";
            intent1.putExtra("userDataHashMap", userDataHashMap);
            intent1.putExtra("keyDatabaseReference", keyDatabaseReference);
            intent1.putExtra("subbabDatabaseReference", subbabDatabaseReference);
            intent1.putExtra("materiDatabaseReference", materiDatabaseReference);
            intent1.putExtra("videoDatabaseReference", itemVideoPembelajaran.getSubBabVideo());
            Log.d("keyDatabaseReference", keyDatabaseReference);
            startActivity(intent1);
        }
    }
}
