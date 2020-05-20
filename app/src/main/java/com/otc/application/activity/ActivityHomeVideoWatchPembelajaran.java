package com.otc.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.otc.application.R;
import com.otc.application.others.CommonMethods;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.otc.application.others.ReadDataFromFirebase;

import java.util.HashMap;

public class ActivityHomeVideoWatchPembelajaran extends AppCompatActivity {

    private DatabaseReference videoReference;
    private String subbabDatabaseReference, materiDatabaseReference, videoDatabaseReference, urlVideo, deskripsiVideo, judulVideo;
    private HashMap<String, String> userDataHashMap;
    private CommonMethods commonMethods;
    private ReadDataFromFirebase readDataFromFirebase;
    private Toolbar toolbar;
    private ImageButton homeImageButton;
    private TextView deskripsiKontenTextView;
    private ImageView fullscreenButton;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer exoPlayer;
    private boolean fullscreen = false;

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
                setFullScreenButton();
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

    private void setFullScreenButton(){

        fullscreenButton = exoPlayerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(ActivityHomeVideoWatchPembelajaran.this, R.drawable.ic_fullscreen_open_white_24dp));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    exoPlayerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(ActivityHomeVideoWatchPembelajaran.this, R.drawable.ic_fullscreen_exit_white_24dp));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    exoPlayerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        exoPlayer.release();
        super.onDestroy();
    }
}
