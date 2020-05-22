package com.otc.application.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.R;
import com.otc.application.activity.ActivityHomeVideoPembelajaran;
import com.otc.application.activity.ActivitySignIn;
import com.otc.application.adapter.GridProgramAdapter;
import com.otc.application.item.ItemProgram;
import com.otc.application.others.CommonMethods;
import com.otc.application.others.ReadDataFromFirebase;

import java.util.ArrayList;
import java.util.HashMap;

public class BerandaFragment extends Fragment {

    private DatabaseReference databaseReference, namaProgramReference, profilPenggunaReference;
    private ArrayList<ItemProgram> programArrayList;
    private ArrayList<String> namaProgramArrayList;
    private CommonMethods commonMethods;
    private ReadDataFromFirebase readDataFromFirebase;
    private RecyclerView programRecylerView;
    private TextView namaPenggunaTextView, namaSekolahTextView, jenisAkunTextView;
    private HashMap<String, String> userDataHashMap;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uID;
    private ImageButton signOutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_beranda, container, false);

        commonMethods = new CommonMethods(getContext());
        readDataFromFirebase = new ReadDataFromFirebase(getContext());

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            moveToActivitySignIn();
        }
        else {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            uID = currentUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            namaProgramReference = databaseReference.child("Halaman").child("Home").child("Program");

            readDataFromFirebase.readKeyArrayListFromDatabase(namaProgramReference, new ReadDataFromFirebase.FirebaseCallbackArrayList() {
                @Override
                public void onCallback(ArrayList<String> arrayList) {

                    namaProgramArrayList = new ArrayList<String>();
                    namaProgramArrayList = arrayList;

                    programRecylerView = (RecyclerView) root.findViewById(R.id.programRecylerView);

                    programArrayList = new ArrayList<ItemProgram>();
                    assignArrayListStringToArrayListProgram(namaProgramArrayList, programArrayList);
                    setValueToRecylerView();
                }
            });

            namaPenggunaTextView = (TextView) root.findViewById(R.id.namaPenggunaTextView);
            namaSekolahTextView = (TextView) root.findViewById(R.id.namaSekolahTextView);
            jenisAkunTextView = (TextView) root.findViewById(R.id.jenisAkunTextView);

            profilPenggunaReference = databaseReference.child("Sementara").child("Pengguna").child(uID).child("Profil");

            userDataHashMap = new HashMap<String, String>();
            readDataFromFirebase.readValueArrayListFromDatabase(profilPenggunaReference, new ReadDataFromFirebase.FirebaseCallbackHashMap() {
                @Override
                public void onCallback(HashMap<String, String> hashMap) {
                    userDataHashMap = hashMap;

                    namaPenggunaTextView.setText(userDataHashMap.get(getString(R.string.nama_lengkap_beserta_gelar)));
                    //TODO: set text of "namaSekolahTextView" here
                    jenisAkunTextView.setText(userDataHashMap.get(getString(R.string.jenis_akun)));
                }
            });
        }

        signOutButton = (ImageButton) root.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                commonMethods.simpleMoveToAnotherActivity(getContext(), ActivitySignIn.class, true);
            }
        });

        return root;
    }

    private void moveToActivitySignIn(){
        Intent toActivitySignIn = new Intent(getContext(), ActivitySignIn.class);
        toActivitySignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivitySignIn);
        getActivity().finish();
    }

    private void assignArrayListStringToArrayListProgram(ArrayList<String> inputNamaProgramArrayList, ArrayList<ItemProgram> inputItemProgramArrayList){
        for(int i = 0; i < inputNamaProgramArrayList.size(); i++){
            ItemProgram itemProgram = new ItemProgram();
            itemProgram.setNamaProgram(inputNamaProgramArrayList.get(i));
            inputItemProgramArrayList.add(itemProgram);
        }
    }

    private void setValueToRecylerView(){
        programRecylerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
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
            Intent intent = new Intent(getContext(), ActivityHomeVideoPembelajaran.class);
            intent.putExtra("userDataHashMap", userDataHashMap);
            intent.putExtra("databaseReference", "");
            intent.putExtra("keyDatabaseReference", "bab");
            startActivity(intent);
            getActivity().finish();
        }
    }
}
