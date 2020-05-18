package com.otc.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.otc.application.encryptionanddecryption.EncryptionAndDecryption;
import com.otc.application.others.CommonMethods;
import com.otc.application.R;

import java.util.HashMap;

public class ActivitySignIn extends AppCompatActivity {

    private TextView textViewBelumPunyaAkun;
    private CommonMethods commonMethods;
    private AppCompatEditText textInputEditTextAlamatEmail, textInputEditTextKataSandi;
    private HashMap<String, EditText> userDataHashMapEditText;
    private Button masukButton;
    private HashMap<String, String> userDataFromEditText;
    private ProgressDialog loading;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        commonMethods = new CommonMethods(this);

        loading = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        textViewBelumPunyaAkun = (TextView) findViewById(R.id.textViewBelumPunyaAkun);
        commonMethods.splitTextViewIntoTwoColors(textViewBelumPunyaAkun, getString(R.string.belum_punya_akun_ayo_daftar),
                getResources().getColor(R.color.black), getResources().getColor(R.color.light_orange), 0, 17, 18, 30);

        textInputEditTextAlamatEmail = (AppCompatEditText) findViewById(R.id.textInputEditTextAlamatEmail);
        textInputEditTextKataSandi = (AppCompatEditText) findViewById(R.id.textInputEditTextKataSandi);

        userDataHashMapEditText = new HashMap<String, EditText>();
        userDataHashMapEditText.put(getString(R.string.email), textInputEditTextAlamatEmail);
        userDataHashMapEditText.put(getString(R.string.kata_sandi), textInputEditTextKataSandi);

        masukButton = (Button) findViewById(R.id.masukButton);
        masukButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWhenButtonMasukIsClicked();
            }
        });

        textViewBelumPunyaAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivitySignUp();
            }
        });
    }

    private void moveToActivitySignUp(){
        Intent intent = new Intent(ActivitySignIn.this, ActivitySignUp.class);
        startActivity(intent);
    }

    private void handleWhenButtonMasukIsClicked(){
        userDataFromEditText = new HashMap<String, String>();
        userDataFromEditText = commonMethods.getAllEditTextValueAndConvertItToHashMap(userDataHashMapEditText);

        if(userDataFromEditText.get(getString(R.string.email)).isEmpty() || userDataFromEditText.get(getString(R.string.kata_sandi)).isEmpty()){
            Toast.makeText(ActivitySignIn.this, getString(R.string.semua_kolom_yang_bukan_opsional_wajib_diisi), Toast.LENGTH_LONG).show();
        }
        else {
            commonMethods.setProgressDialog(this, loading);
            logInUser(userDataFromEditText);
        }
    }

    private void logInUser(HashMap<String, String> inputHashMap){

        mAuth.signInWithEmailAndPassword(inputHashMap.get(getString(R.string.email)), EncryptionAndDecryption.encrypt(inputHashMap.get(getString(R.string.kata_sandi))))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loading.dismiss();
                    moveToActivityHome();
                }
                else{
                    loading.hide();
                    Toast.makeText(ActivitySignIn.this, getString(R.string.email_dan_kata_sandi_tidak_tepat), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveToActivityHome(){
        Intent toActivityHome = new Intent(ActivitySignIn.this, ActivityHome.class);
        toActivityHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityHome);
        finish();
    }
}