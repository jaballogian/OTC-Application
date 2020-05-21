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
import com.otc.application.others.ReadDataFromFirebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;

public class ActivityHomeVideoPembelajaran extends AppCompatActivity {

    private DatabaseReference videoPembelajaranReference;
    private HashMap<String, String> userDataHashMap;
//    private ArrayList<String> babArrayList;
    private ReadDataFromFirebase readDataFromFirebase;
    private RecyclerView babRecylerView;
    private ArrayList<ItemVideoPembelajaran> videoPembelajaranArrayList;
    private String urlVideoPembelajaran, keyDatabaseReference, subbabDatabaseReference, materiDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_video_pembelajaran);

        readDataFromFirebase =new ReadDataFromFirebase(this);

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

//        babArrayList = new ArrayList<String>();
        readDataFromFirebase.readKeyArrayListFromDatabase(videoPembelajaranReference, new ReadDataFromFirebase.FirebaseCallbackArrayList() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {
//                babArrayList = arrayList;

                ArrayList<HashMap> videoArrayList = new ArrayList<HashMap>();

                for(int i = 0; i < arrayList.size(); i++){
                    String element = arrayList.get(i);
                    char firstCharacter = element.charAt(0);
                    int indexOfSpace = element.indexOf(" ");
                    String number = "";
                    String title = "";

                    if(Character.isDigit(firstCharacter)){
                        number = element.substring(0, indexOfSpace);
                        title = element.substring(indexOfSpace);

                        if(number.contains("_")){
                            int indexOfUnderScore = number.indexOf("_");
                            number = number.substring(indexOfUnderScore + 1);
                        }

                        HashMap<String, String> videoHashMap = new HashMap<String, String>();
                        videoHashMap.put("number", number);
                        videoHashMap.put("title", number + "." + title);
                        videoHashMap.put("original", element);
                        videoArrayList.add(videoHashMap);
                    }
                    else {
                        HashMap<String, String> videoHashMap = new HashMap<String, String>();
                        videoHashMap.put("title", element);
                        videoHashMap.put("original", element);
                        videoArrayList.add(videoHashMap);
                    }


                }

                for(int i = 0; i < videoArrayList.size(); i++){
                    for(int j = 0; j < videoArrayList.size(); j++){
                        try{
                            int firstNumber = Integer.valueOf((String) videoArrayList.get(i).get("number"));
                            int secondNumber = Integer.valueOf((String) videoArrayList.get(j).get("number"));
                            if(firstNumber < secondNumber){
                                HashMap<String, String> temp = new HashMap<String, String>();
                                temp = videoArrayList.get(i);
                                videoArrayList.set(i, videoArrayList.get(j));
                                videoArrayList.set(j, temp);
                            }
                        }
                        catch (NumberFormatException error){
                            Log.d("NumberFormatException", error.getMessage());
                        }
                    }
                }

                babRecylerView =(RecyclerView) findViewById(R.id.videoRecylerView);

                videoPembelajaranArrayList = new ArrayList<ItemVideoPembelajaran>();
                assignArrayListStringToArrayListProgram(videoArrayList, videoPembelajaranArrayList);
                setValueToRecylerView();
            }
        });
    }

    private void assignArrayListStringToArrayListProgram(ArrayList<HashMap> inputSubBabArrayList, ArrayList<ItemVideoPembelajaran> inputItemProgramArrayList){
        for(int i = 0; i < inputSubBabArrayList.size(); i++){
            ItemVideoPembelajaran itemVideoPembelajaran = new ItemVideoPembelajaran();
            itemVideoPembelajaran.setSubBabVideo((String) inputSubBabArrayList.get(i).get("original"));
            itemVideoPembelajaran.setShowSubBabVideo((String) inputSubBabArrayList.get(i).get("title"));
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
