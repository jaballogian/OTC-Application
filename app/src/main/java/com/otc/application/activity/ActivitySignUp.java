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
    private Spinner spinnerJenisAkun, spinnerJenjangSekolah;

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

        spinnerJenisAkun = (Spinner) findViewById(R.id.spinnerJenisAkun);
        commonMethods.populateArrayListToSpinner(this, spinnerJenisAkun, commonMethods.readArrayListFromDatabase(jenisAkunReference));

        spinnerJenjangSekolah = (Spinner) findViewById(R.id.spinnerJenjangSekolah);
        commonMethods.populateArrayListToSpinner(this, spinnerJenjangSekolah, commonMethods.readArrayListFromDatabase(jenjangSekolahReference));

        spinnerJenisAkun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedOption = parent.getItemAtPosition(position).toString();
                spinnerJenisAkun.setSelection(position);
                Toast.makeText(ActivitySignUp.this, selectedOption, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });
    }

    private void moveToActivityTermsAndCondition(){
        Intent intent = new Intent(ActivitySignUp.this, ActivityTermsAndCondition.class);
        startActivity(intent);
    }
}
