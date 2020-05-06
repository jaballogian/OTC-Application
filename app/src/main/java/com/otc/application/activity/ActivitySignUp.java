package com.otc.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.AppCompatSpinner;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.otc.application.CommonMethods;
import com.otc.application.R;

import java.util.ArrayList;

public class ActivitySignUp extends AppCompatActivity {

    private TextView textViewSudahPunyaAkunMasukAja, textViewPersetujuan;
    private CommonMethods commonMethods;
    private DatabaseReference halamanPendaftaranReference, jenisAkunReference, jenjangSekolahReference;
    private AppCompatSpinner spinnerJenisAkun, spinnerJenjangSekolah;
    private ArrayList<String> userData;
    private Button buttonMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        commonMethods = new CommonMethods(this);

        halamanPendaftaranReference = FirebaseDatabase.getInstance().getReference().child("Halaman").child("Pendaftaran");
        jenisAkunReference = halamanPendaftaranReference.child("Jenis Akun");
        jenjangSekolahReference = halamanPendaftaranReference.child("Jenjang Sekolah");

        textViewSudahPunyaAkunMasukAja = (TextView) findViewById(R.id.textViewSudahPunyaAkunMasukAja);
        commonMethods.splitTextViewIntoTwoColors(textViewSudahPunyaAkunMasukAja, getString(R.string.sudah_punya_akun_masuk_aja),
                getResources().getColor(R.color.black), getResources().getColor(R.color.light_orange), 0, 17, 18, 29);

        textViewPersetujuan = (TextView) findViewById(R.id.textViewPersetujuan);
        commonMethods.splitTextViewIntoTwoColors(textViewPersetujuan, getString(R.string.persetujuan_pendafataran),
                getResources().getColor(R.color.black), getResources().getColor(R.color.light_orange), 0, 59, 60, 80);
        textViewPersetujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivityTermsAndCondition();
            }
        });

        userData = new ArrayList<String>();
        userData.add("");
        userData.add("");

        spinnerJenisAkun = (AppCompatSpinner) findViewById(R.id.spinnerJenisAkun);
        commonMethods.readArrayListFromDatabase(jenisAkunReference, spinnerJenisAkun);
        handleSpinnderOnItemClicked(spinnerJenisAkun);

        spinnerJenjangSekolah = (AppCompatSpinner) findViewById(R.id.spinnerJenjangSekolah);
        commonMethods.readArrayListFromDatabase(jenjangSekolahReference, spinnerJenjangSekolah);
        handleSpinnderOnItemClicked(spinnerJenjangSekolah);

        buttonMasuk = (Button) findViewById(R.id.buttonMasuk);
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySignUp.this, userData.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moveToActivityTermsAndCondition(){
        Intent intent = new Intent(ActivitySignUp.this, ActivityTermsAndCondition.class);
        startActivity(intent);
    }

    private void handleSpinnderOnItemClicked (final Spinner inputSpinner){

        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int i = 0;
                if(inputSpinner == spinnerJenisAkun){
                    i = 0;
                }
                else if(inputSpinner == spinnerJenjangSekolah){
                    i = 1;
                }
                userData.set(i, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });
    }
}
