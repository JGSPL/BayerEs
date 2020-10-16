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
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class LiveStreamingActivity extends AppCompatActivity {
    JzvdStd videoview;
    TextView tvDescription;
    //YouTubePlayerView youTubeView;
    String YouvideoId;
    YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);


        Intent intent = getIntent();
        final String livestream_link = intent.getStringExtra("livestream_link");
        /*videoview = findViewById(R.id.videoview);
        videoview.setUp(livestream_link, ""
                , JzvdStd.SCREEN_NORMAL);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);*/

        tvDescription = (TextView) findViewById(R.id.tvDescription);
        makeTextViewResizable(tvDescription, 1, "View More", true);

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, NewsFeedFragment.newInstance(), "")
                .commit();*/

       // youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_view);
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

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder
    addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                      final int maxLine, final String spanableText, final boolean viewMore)
    {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 1, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
        }
        return ssb;
    }
}