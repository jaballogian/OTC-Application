package com.otc.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.internal.Util;
import com.otc.application.R;
import com.otc.application.item.ItemVideoPembelajaran;
import com.otc.application.others.CommonMethods;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.otc.application.others.ReadDataFromFirebase;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import javax.sql.DataSource;

public class ActivityHomeVideoWatchPembelajaran extends AppCompatActivity {

    private DatabaseReference videoReference;
    private String subbabDatabaseReference, materiDatabaseReference, videoDatabaseReference, urlVideo, deskripsiVideo, judulVideo;
    private HashMap<String, String> userDataHashMap;
    private CommonMethods commonMethods;
    private ReadDataFromFirebase readDataFromFirebase;
    private Toolbar toolbar;
    private ImageButton homeImageButton;
    private TextView deskripsiKontenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_video_watch_pembelajaran);

        commonMethods = new CommonMethods(this);
        commonMethods.blockScreenCapture(this);

        readDataFromFirebase = new ReadDataFromFirebase(this);

        homeImageButton = (ImageButton) findViewById(R.id.homeImageButton);
        homeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.simpleMoveToAnotherActivity(ActivityHomeVideoWatchPembelajaran.this, ActivityHome.class, true);
            }
        });

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

        readDataFromFirebase.readValueArrayListFromDatabase(videoReference, new ReadDataFromFirebase.FirebaseCallbackHashMap() {
            @Override
            public void onCallback(HashMap<String, String> hashMap) {
                urlVideo = hashMap.get("URL");
                deskripsiVideo = hashMap.get("DESKRIPSI");
                judulVideo = hashMap.get("JUDUL");
                deskripsiKontenTextView = (TextView) findViewById(R.id.deskripsiKontenTextView);
                deskripsiKontenTextView.setText(deskripsiVideo);
                Log.d("hashMap", hashMap.toString());

                playVideo(urlVideo);
                setToolbar(judulVideo);
            }
        });
    }

    private void setToolbar(String inputTitle){
        toolbar = (Toolbar) findViewById(R.id.toolbarWatchVideo);
        setSupportActionBar(toolbar);
        toolbar.setTitle(inputTitle);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.simpleMoveToAnotherActivity(ActivityHomeVideoWatchPembelajaran.this, ActivityHomeVideoPembelajaran.class, true);
            }
        });
    }

    private void playVideo(String inputURL){
        SimpleExoPlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;
        String videoURL = inputURL;

        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI = Uri.parse(videoURL);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }catch (Exception e){
            Log.e("ActivityWatchVideo", e.toString());
        }
    }
}
