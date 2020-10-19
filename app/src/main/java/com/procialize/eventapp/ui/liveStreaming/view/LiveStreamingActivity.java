package com.procialize.eventapp.ui.liveStreaming.view;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class LiveStreamingActivity extends AppCompatActivity {
    JzvdStd videoview;
    TextView tvDescription,tv_header;
    //YouTubePlayerView youTubeView;
    String YouvideoId;
    YouTubePlayer youTubePlayer;
    Agenda agendaDetails;
    String sessionName,sessionShortDescription,sessionDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);


        Intent intent = getIntent();
        final String livestream_link = intent.getStringExtra("livestream_link");
        agendaDetails = (Agenda) intent.getSerializableExtra("agendaDetails");
        sessionName = agendaDetails.getSession_name();
        sessionShortDescription = agendaDetails.getSession_short_description();
        sessionDescription = agendaDetails.getSession_description();

        /*videoview = findViewById(R.id.videoview);
        videoview.setUp(livestream_link, ""
                , JzvdStd.SCREEN_NORMAL);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);*/

        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setText(sessionName);
        tvDescription.setText(sessionDescription);
        CommonFunction.makeTextViewResizable(tvDescription, 1, " View More", true);

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, NewsFeedFragment.newInstance(), "")
                .commit();*/

       // youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_view);
        // Initializing video player with developer key
        youTubePlayerFragment.initialize(getResources().getString(R.string.maps_api_key), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {

                    String youtube_stream_url = livestream_link;
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


    }


}