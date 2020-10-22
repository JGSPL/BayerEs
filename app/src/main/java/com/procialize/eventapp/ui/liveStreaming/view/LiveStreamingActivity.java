package com.procialize.eventapp.ui.liveStreaming.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.cast.framework.CastContext;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.costumTools.KeepScreenOnHandler;
import com.procialize.eventapp.ui.AgendaDetails.view.AgendaDetailsActivity;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;
import com.procialize.eventapp.ui.spotQnA.view.SpotQnAFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;

public class LiveStreamingActivity extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {
    JzvdStd videoview;
    TextView tvDescription, tv_header;
    //YouTubePlayerView youTubeView;
    String YouvideoId;
    YouTubePlayer youTubePlayer;
    Agenda agendaDetails;
    String sessionName, sessionShortDescription, sessionDescription, sessionId;
    private JWPlayerView mPlayerView;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    //private CastContext mCastContext;
    LinearLayout ll_youtube, ll_bottom, ll_main;
    AppBarLayout appBarLayout;
    String youtube_stream_url;
    String filePath;
    ImageView iv_back;
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);


        Intent intent = getIntent();
        final String livestream_link = intent.getStringExtra("livestream_link");
        agendaDetails = (Agenda) intent.getSerializableExtra("agendaDetails");
        sessionName = agendaDetails.getSession_name();
        sessionId = agendaDetails.getSession_id();
        sessionShortDescription = agendaDetails.getSession_short_description();
        sessionDescription = agendaDetails.getSession_description();

        /*videoview = findViewById(R.id.videoview);
        videoview.setUp(livestream_link, ""
                , JzvdStd.SCREEN_NORMAL);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);*/

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_youtube = (LinearLayout) findViewById(R.id.ll_youtube);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setText(sessionName);
        tvDescription.setText(sessionDescription);
        CommonFunction.makeTextViewResizable(tvDescription, 1, " View More", true);
        setDynamicColor();

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, SpotQnAFragment.newInstance(), "")
                .commit();*/


        youtube_stream_url = livestream_link;
        mPlayerView = findViewById(R.id.jwplayer);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_view);

        if (youtube_stream_url.contains("https://www.youtube.com/watch?v")) {
            // youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            // Initializing video player with developer key
            youTubePlayerFragment.initialize(getResources().getString(R.string.maps_api_key), new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                    boolean wasRestored) {
                    if (!wasRestored) {

                        // String youtube_stream_url = livestream_link;
                        YouvideoId = youtube_stream_url.substring(youtube_stream_url.lastIndexOf("=") + 1);

                        String[] parts = youtube_stream_url.split("=");
                        String part1 = parts[0]; // 004
                        String videoId = parts[0]; // 034556
                        String[] parts1 = videoId.split("&index");
                        String url = parts1[0];
                        String[] parts2 = videoId.split("&list");
                        String url2 = parts2[0];
                        Log.e("videoid", YouvideoId);
                        youTubePlayer = player;
                        //set the player style default
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        //cue the 1st video by default
                        youTubePlayer.cueVideo(YouvideoId);

                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    youTubePlayer.play();
                    youTubePlayer.getFullscreenControlFlags();
                    //print or show error if initialization failed
                    Log.e("", "Youtube Player View initialization failed");
                }
            });
            ll_youtube.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            ll_youtube.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);

            // Handle hiding/showing of ActionBar
            mPlayerView.addOnFullscreenListener(this);
            // Keep the screen on during playback
            new KeepScreenOnHandler(mPlayerView, getWindow());
            // Load a media source
            try {
                Bitmap bitmap = retriveVideoFrameFromVideo(livestream_link);
                filePath = CommonFunction.saveImage(this, bitmap, "livestreamthumb");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            PlaylistItem pi = new PlaylistItem.Builder()
                    .file(livestream_link)
                    .image(filePath)
                    .build();

            mPlayerView.load(pi);

            // Get a reference to the CastContext
            //  mCastContext = CastContext.getSharedInstance(this);
        }

        ColorStateList iconsColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        //Color.parseColor(colorunselect),
                        Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)),
                        Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4))
                });
        bottom_navigation.setItemIconTintList(iconsColorStates);

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_comment:
                        break;
                    case R.id.navigation_livepoll:
                        break;
                    case R.id.navigation_quiz:
                        break;
                    case R.id.navigation_qna:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("agendaDetails", (Serializable) agendaDetails);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        SpotQnAFragment fragInfo = new SpotQnAFragment();
                        fragInfo.setArguments(bundle);
                        transaction.replace(R.id.fragment_frame, fragInfo);
                        transaction.commit();
                        break;
                }

                return true;
            }
        });
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
                appBarLayout.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
            } else {
                actionBar.show();
                appBarLayout.setVisibility(View.VISIBLE);
                ll_bottom.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE,
                true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDynamicColor() {
        CommonFunction.showBackgroundImage(this, ll_main);
        tvDescription.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}