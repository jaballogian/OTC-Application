package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.R;
import com.otc.application.adapter.GridProgramAdapter;
import com.otc.application.item.ItemProgram;
import com.otc.application.others.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityHome extends AppCompatActivity {

    private DatabaseReference databaseReference, namaProgramReference, profilPenggunaReference;
    private ArrayList<ItemProgram> programArrayList;
    private ArrayList<String> namaProgramArrayList;
    private CommonMethods commonMethods;
    private RecyclerView programRecylerView;
    private TextView namaPenggunaTextView, namaSekolahTextView, jenisAkunTextView;
    private HashMap<String, String> userDataHashMap;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkingUser();

        commonMethods = new CommonMethods(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        namaProgramReference = databaseReference.child("Halaman").child("Home").child("Program");

        commonMethods.readKeyArrayListFromDatabase(namaProgramReference, new CommonMethods.FirebaseCallbackArrayList() {
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

        namaPenggunaTextView = (TextView) findViewById(R.id.namaPenggunaTextView);
        namaSekolahTextView = (TextView) findViewById(R.id.namaSekolahTextView);
        jenisAkunTextView = (TextView) findViewById(R.id.jenisAkunTextView);

        profilPenggunaReference = databaseReference.child("Sementara").child("Pengguna").child(uID).child("Profil");

        userDataHashMap = new HashMap<String, String>();
        commonMethods.readValueArrayListFromDatabase(profilPenggunaReference, new CommonMethods.FirebaseCallbackHashMap() {
            @Override
            public void onCallback(HashMap<String, String> hashMap) {
                userDataHashMap = hashMap;

                namaPenggunaTextView.setText(userDataHashMap.get(getString(R.string.nama_lengkap_beserta_gelar)));
                //TODO: set text of "namaSekolahTextView" here
                jenisAkunTextView.setText(userDataHashMap.get(getString(R.string.jenis_akun)));
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
        programRecylerView.setLayoutManager(new GridLayoutManager(this, 4));
        GridProgramAdapter gridProgramAdapter = new GridProgramAdapter(programArrayList);
        programRecylerView.setAdapter(gridProgramAdapter);

        gridProgramAdapter.setOnItemClickCallback(new GridProgramAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ItemProgram itemProgram) {
                moveToAnotherAcvitity(itemProgram);
            }
        });
    }

    private void moveToAnotherAcvitity(ItemProgram inputItemProgram){
        if(inputItemProgram.getNamaProgram().equals(getString(R.string.video_pembelajaran))){
            Intent intent = new Intent(ActivityHome.this, ActivityHomeVideoPembelajaran.class);
            intent.putExtra("userDataHashMap", userDataHashMap);
            intent.putExtra("databaseReference", "");
            intent.putExtra("keyDatabaseReference", "bab");
            startActivity(intent);
            finish();
        }
    }
}
