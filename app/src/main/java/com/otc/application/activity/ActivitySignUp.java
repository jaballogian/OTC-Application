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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
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
    private CheckBox checkBoxPersetujuanPendaftaran;

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
        //TODO: define and read "tipe sekolah" from database

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
        //TODO: define and read "bidang" from database

        checkBoxPersetujuanPendaftaran = (CheckBox) findViewById(R.id.checkBoxPersetujuanPendaftaran);

        buttonMasuk = (Button) findViewById(R.id.buttonMasuk);
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleWhenButtonMasukIsClicked();
            }
        });
    }

    private void moveToActivityTermsAndCondition(){
        Intent intent = new Intent(ActivitySignUp.this, ActivityTermsAndCondition.class);
        startActivity(intent);
    }

    private void handleWhenButtonMasukIsClicked(){

        if(checkBoxPersetujuanPendaftaran.isChecked()){
            userDataFromEditText = commonMethods.getAllEditTextValueAndConvertItToHashMap(userDataHashMapEditText);

            if(userDataFromEditText.get(getString(R.string.nama_lengkap_beserta_gelar)).isEmpty()
                    || userDataFromEditText.get(getString(R.string.email)).isEmpty()
                    || userDataFromEditText.get(getString(R.string.kata_sandi)).isEmpty()
                    || userDataFromEditText.get(getString(R.string.nomor_wa_handphone)).isEmpty()){
                Toast.makeText(ActivitySignUp.this, getString(R.string.semua_kolom_yang_bukan_opsional_wajib_diisi), Toast.LENGTH_LONG).show();
            }
            else {

                if(userDataFromSpinner.get(getString(R.string.jenis_akun)).equals(getString(R.string.jenis_akun))
                        || userDataFromSpinner.get(getString(R.string.jenjang_sekolah)).equals(getString(R.string.jenjang_sekolah))){
                    Toast.makeText(ActivitySignUp.this, getString(R.string.pilih_jenis_akun_dan_jenjang_akun_yang_valid), Toast.LENGTH_LONG).show();
                }
                else {
                    userData.putAll(userDataFromEditText);
                    userData.putAll(userDataFromSpinner);
                    Log.d("userData", userData.toString());
                }
            }
        }
        else {
            Toast.makeText(ActivitySignUp.this, getString(R.string.anda_belum_menyetujui_syarat_dan_ketentuan_yang_berlaku), Toast.LENGTH_LONG).show();
        }
    }
}
