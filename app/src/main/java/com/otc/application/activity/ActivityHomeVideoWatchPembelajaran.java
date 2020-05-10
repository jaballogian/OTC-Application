package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.otc.application.R;
import com.otc.application.item.ItemVideoPembelajaran;
import com.otc.application.others.CommonMethods;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class ActivityHomeVideoWatchPembelajaran extends AppCompatActivity {

    private DatabaseReference videoReference;
    private String subbabDatabaseReference, materiDatabaseReference, videoDatabaseReference, urlVideo;
    private HashMap<String, String> userDataHashMap;
    private CommonMethods commonMethods;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_video_watch_pembelajaran);

        commonMethods = new CommonMethods(this);

        userDataHashMap = (HashMap<String, String>) getIntent().getSerializableExtra("userDataHashMap");

        subbabDatabaseReference = getIntent().getExtras().getString("subbabDatabaseReference");
        materiDatabaseReference = getIntent().getExtras().getString("materiDatabaseReference");
        videoDatabaseReference = getIntent().getExtras().getString("videoDatabaseReference");

        videoReference = FirebaseDatabase.getInstance().getReference().child("Peta Materi").child("KSN")
                .child(userDataHashMap.get(getString(R.string.jenjang_sekolah)))
                .child(userDataHashMap.get(getString(R.string.bidang)).toUpperCase())
                .child(subbabDatabaseReference)
                .child(materiDatabaseReference)
                .child(getString(R.string.video_pembelajaran).toUpperCase())
                .child(getString(R.string.provinsi).toUpperCase())
                .child(videoDatabaseReference);

        Log.d("videoReference", videoReference.toString());

        commonMethods.readValueArrayListFromDatabase(videoReference, new CommonMethods.FirebaseCallbackHashMap() {
            @Override
            public void onCallback(HashMap<String, String> hashMap) {
                urlVideo = hashMap.get("URL");
                Log.d("hashMap", urlVideo);

                videoView = (VideoView) findViewById(R.id.videoView);
                videoView.setVideoURI(Uri.parse(urlVideo));
                videoView.start();
            }
        });
    }
}
