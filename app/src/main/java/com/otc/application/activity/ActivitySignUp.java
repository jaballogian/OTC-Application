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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.otc.application.CommonMethods;
import com.otc.application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitySignUp extends AppCompatActivity {

    private TextView textViewSudahPunyaAkunMasukAja, textViewPersetujuan;
    private CommonMethods commonMethods;
    private DatabaseReference halamanPendaftaranReference, jenisAkunReference, jenjangSekolahReference;
    private AppCompatSpinner spinnerJenisAkun, spinnerJenjangSekolah, spinnerTipeSekolah, spinnerBidang;
    private Button buttonMasuk;
    private TextInputEditText textInputEditTextNamaLengkap, textInputEditTextEmail, textInputEditTextKataSandi, textInputEditTextNomorWAHandphone, textInputEditTextIDLine, textInputEditTextInstagram;
    private HashMap<String, String> userData, userDataFromEditText, userDataFromSpinner;
    private HashMap<String, EditText> userDataHashMapEditText;
    private ArrayList<Spinner> arrayListSpinner;

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

        userData = new HashMap<>();

        spinnerJenisAkun = (AppCompatSpinner) findViewById(R.id.spinnerJenisAkun);
        commonMethods.readArrayListFromDatabase(jenisAkunReference, spinnerJenisAkun);

        spinnerJenjangSekolah = (AppCompatSpinner) findViewById(R.id.spinnerJenjangSekolah);
        commonMethods.readArrayListFromDatabase(jenjangSekolahReference, spinnerJenjangSekolah);

        arrayListSpinner = new ArrayList<Spinner>();
        arrayListSpinner.add(spinnerJenisAkun);
        arrayListSpinner.add(spinnerJenjangSekolah);

        userDataFromSpinner = commonMethods.handleSpinnderOnItemClicked(arrayListSpinner);

        spinnerTipeSekolah = (AppCompatSpinner) findViewById(R.id.spinnerTipeSekolah);
        //TODO: read define and read "tipe sekolah" from database here

        textInputEditTextNamaLengkap = (TextInputEditText) findViewById(R.id.textInputEditTextNamaLengkap);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextKataSandi = (TextInputEditText) findViewById(R.id.textInputEditTextKataSandi);
        textInputEditTextNomorWAHandphone = (TextInputEditText) findViewById(R.id.textInputEditTextNomorWAHandphone);
        textInputEditTextIDLine = (TextInputEditText) findViewById(R.id.textInputEditTextIDLine);
        textInputEditTextInstagram = (TextInputEditText) findViewById(R.id.textInputEditTextInstagram);

        userDataHashMapEditText = new HashMap<>();
        userDataHashMapEditText.put(getString(R.string.nama_lengkap_beserta_gelar), textInputEditTextNamaLengkap);
        userDataHashMapEditText.put(getString(R.string.email), textInputEditTextEmail);
        userDataHashMapEditText.put(getString(R.string.kata_sandi), textInputEditTextKataSandi);
        userDataHashMapEditText.put(getString(R.string.nomor_wa_handphone), textInputEditTextNomorWAHandphone);
        userDataHashMapEditText.put(getString(R.string.instagram_opsional), textInputEditTextInstagram);
        userDataHashMapEditText.put(getString(R.string.id_line_opsional), textInputEditTextIDLine);

        userDataFromEditText = new HashMap<>();

        spinnerBidang = (AppCompatSpinner) findViewById(R.id.spinnerBidang);

        buttonMasuk = (Button) findViewById(R.id.buttonMasuk);
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDataFromEditText = commonMethods.getAllEditTextValueAndConvertItToHashMap(userDataHashMapEditText);
                userData.putAll(userDataFromEditText);
                userData.putAll(userDataFromSpinner);
                Log.d("userData", userData.toString());
            }
        });
    }

    private void moveToActivityTermsAndCondition(){
        Intent intent = new Intent(ActivitySignUp.this, ActivityTermsAndCondition.class);
        startActivity(intent);
    }

//    private void handleSpinnderOnItemClicked (final Spinner inputSpinner){
//
//        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                String key = "";
//                String value = parent.getItemAtPosition(position).toString();
//                if(inputSpinner == spinnerJenisAkun){
//                    key = getString(R.string.jenis_akun);
//                }
//                else if(inputSpinner == spinnerJenjangSekolah){
//                    key = getString(R.string.jenjang_sekolah);
//                }
//                Log.d("keyInputSpinner", key + " " + value);
//                userData.put(key, value);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//                // TODO Auto-generated method stub
//            }
//        });
//    }
}
