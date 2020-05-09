package com.otc.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatSpinner;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.encryptionanddecryption.EncryptionAndDecryption;
import com.otc.application.others.CommonMethods;
import com.otc.application.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitySignUp extends AppCompatActivity {

    private TextView textViewSudahPunyaAkunMasukAja, textViewPersetujuan;
    private CommonMethods commonMethods;
    private DatabaseReference halamanPendaftaranReference, jenisAkunReference, jenjangSekolahReference, tipeSekolahReference;
    private AppCompatSpinner spinnerJenisAkun, spinnerJenjangSekolah, spinnerTipeSekolah;
    private Button buttonMasuk;
    private TextInputEditText textInputEditTextNamaLengkap, textInputEditTextEmail, textInputEditTextKataSandi, textInputEditTextNomorWAHandphone, textInputEditTextIDLine, textInputEditTextInstagram;
    private HashMap<String, String> userData, userDataFromEditText, userDataFromSpinner;
    private HashMap<String, EditText> userDataHashMapEditText;
    private ArrayList<Spinner> arrayListSpinner;
    private CheckBox checkBoxPersetujuanPendaftaran;
    private ProgressDialog loading;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String uID;

    public static DatabaseReference bidangReference, bidangSDReference, bidangSMPReference, bidangSMAReference;
    public static AppCompatSpinner spinnerBidang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        commonMethods = new CommonMethods(this);

        loading = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        halamanPendaftaranReference = FirebaseDatabase.getInstance().getReference().child("Halaman").child("Pendaftaran");
        jenisAkunReference = halamanPendaftaranReference.child("Jenis Akun");
        jenjangSekolahReference = halamanPendaftaranReference.child("Jenjang Sekolah");
        tipeSekolahReference = halamanPendaftaranReference.child("Tipe Sekolah");

        bidangReference = halamanPendaftaranReference.child("Bidang");
        bidangSDReference = bidangReference.child("SD").child("Bidang");
        bidangSMPReference = bidangReference.child("SMP").child("Bidang");
        bidangSMAReference = bidangReference.child("SMA").child("Bidang");

        textViewSudahPunyaAkunMasukAja = (TextView) findViewById(R.id.textViewSudahPunyaAkunMasukAja);
        commonMethods.splitTextViewIntoTwoColors(textViewSudahPunyaAkunMasukAja, getString(R.string.sudah_punya_akun_masuk_aja),
                getResources().getColor(R.color.black), getResources().getColor(R.color.light_orange), 0, 17, 18, 29);
        textViewSudahPunyaAkunMasukAja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivitySignIn();
            }
        });

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
        commonMethods.readKeyArrayListFromDatabase(jenisAkunReference, spinnerJenisAkun);

        spinnerJenjangSekolah = (AppCompatSpinner) findViewById(R.id.spinnerJenjangSekolah);
        commonMethods.readKeyArrayListFromDatabase(jenjangSekolahReference, spinnerJenjangSekolah);

        spinnerTipeSekolah = (AppCompatSpinner) findViewById(R.id.spinnerTipeSekolah);
        commonMethods.readKeyArrayListFromDatabase(tipeSekolahReference, spinnerTipeSekolah);

        spinnerBidang = (AppCompatSpinner) findViewById(R.id.spinnerBidang);

        arrayListSpinner = new ArrayList<Spinner>();
        arrayListSpinner.add(spinnerJenisAkun);
        arrayListSpinner.add(spinnerJenjangSekolah);
        arrayListSpinner.add(spinnerTipeSekolah);
        arrayListSpinner.add(spinnerBidang);

        userDataFromSpinner = commonMethods.handleSpinnderOnItemClicked(arrayListSpinner);

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
                        || userDataFromSpinner.get(getString(R.string.jenjang_sekolah)).equals(getString(R.string.jenjang_sekolah))
                        || userDataFromSpinner.get(getString(R.string.tipe_sekolah)).equals(getString(R.string.tipe_sekolah))
                        || userDataFromSpinner.get(getString(R.string.bidang)).equals(getString(R.string.bidang))){
                    Toast.makeText(ActivitySignUp.this, getString(R.string.pilih_jenis_akun_jenjang_akun_dan_bidang_yang_valid), Toast.LENGTH_LONG).show();
                }
                else {
                    userData.putAll(userDataFromEditText);
                    userData.putAll(userDataFromSpinner);

                    userData.put(getString(R.string.kata_sandi), EncryptionAndDecryption.encrypt(userData.get(getString(R.string.kata_sandi))));
                    Log.d("userData", userData.toString());

                    setProgressDialog();
                    registerUser(userData);
                }
            }
        }
        else {
            Toast.makeText(ActivitySignUp.this, getString(R.string.anda_belum_menyetujui_syarat_dan_ketentuan_yang_berlaku), Toast.LENGTH_LONG).show();
        }
    }

    private void moveToActivityHome(){
        Intent toActivityHome = new Intent(ActivitySignUp.this, ActivityHome.class);
        toActivityHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityHome);
        finish();
    }

    private void moveToActivitySignIn(){
        Intent toActivitySignIn = new Intent(ActivitySignUp.this, ActivitySignIn.class);
        startActivity(toActivitySignIn);
        finish();
    }

    private void registerUser(final HashMap<String, String> inputHashMap){
        mAuth.createUserWithEmailAndPassword(inputHashMap.get(getString(R.string.email)), inputHashMap.get(getString(R.string.kata_sandi))).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    saveUserToDatabase(inputHashMap);
                    loading.dismiss();
                }
                else {
                    loading.hide();
                    Toast.makeText(ActivitySignUp.this, R.string.pilih_email_dan_password_yang_berbeda, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUserToDatabase (HashMap<String, String> inputHashMap){
        currentUser = mAuth.getCurrentUser();
        uID = currentUser.getUid();

        DatabaseReference profileReference = FirebaseDatabase.getInstance().getReference().child("Sementara").child("Pengguna").child(uID).child("Profil");
        profileReference.setValue(inputHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    moveToActivityHome();
                    Toast.makeText(ActivitySignUp.this, getString(R.string.selamat_datang), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setProgressDialog(){

        loading.setTitle(R.string.sedang_memproses);
        loading.setMessage(getString(R.string.mohon_tunggu));
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }
}
