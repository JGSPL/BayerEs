package com.procialize.bayer2020.ui.upskill;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;

import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class UpskillFragment extends Fragment {

    View rootView;
    ImageView iv_banner;
    TextView tv_info;
    SwipeRefreshLayout srl_upskill;
    RecyclerView rv_upskill;
    String api_token, eventid;

    public UpskillFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upskill, container, false);

        iv_banner = rootView.findViewById(R.id.iv_banner);
        tv_info = rootView.findViewById(R.id.tv_info);
        srl_upskill = rootView.findViewById(R.id.srl_upskill);
        rv_upskill = rootView.findViewById(R.id.rv_upskill);
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

        //getDataFromApi();
        return rootView;
    }

    /*private void getDataFromApi() {
        ApiUtils.getAPIService().agendaFetch(api_token, eventid)
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(getActivity());
                            String data = refreashToken.decryptedData(strCommentList);
                            try {
                                Gson gson = new Gson();
                                List<AgendaList> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<AgendaList>>() {
                                }.getType());
                                if (agendaLists != null) {
                                    if (agendaLists.size() > 0) {
                                        agendaDetails = agendaLists.get(0).getAgenda_list().get(0);
                                        sessionName = agendaDetails.getSession_name();
                                        sessionId = agendaDetails.getSession_id();
                                        sessionShortDescription = agendaDetails.getSession_short_description();
                                        //sessionDescription = agendaDetails.getSession_description();

                                        tv_header.setText(sessionName);

                                        getActiveUserCount();
                                        try {
                                            Date dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(agendaDetails.getSession_start_time());
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(dateFormat);
                                            DateFormat format2 = new SimpleDateFormat("EEEE");
                                            String finalDay = format2.format(dateFormat);
                                            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                                            tv_event_date.setText(finalDay + " " + CommonFunction.convertSessionDate(agendaDetails.getSession_start_time()));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Date currentTime = Calendar.getInstance().getTime();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String strCurrnetDate = dateFormat.format(currentTime);

                                        tv_event_name.setText(SharedPreference.getPref(getActivity(), EVENT_NAME));

                                        if (CommonFunction.isTimeBetweenTwoTime(agendaDetails.getSession_start_time(),
                                                agendaDetails.getSession_end_time(), strCurrnetDate) && !agendaDetails.getLivestream_link().isEmpty()) {
                                            tv_nothing_streaming_currently.setVisibility(View.GONE);
                                            ll_active_user_count.setVisibility(View.VISIBLE);
                                            ll_youtube.setVisibility(View.VISIBLE);
                                            mPlayerView.setVisibility(View.VISIBLE);
                                            livestream_link = agendaDetails.getLivestream_link();
                                            if (CommonFunction.isYoutubeUrl(livestream_link)) {
                                                *//*if (youtube_stream_url.contains("https://www.youtube.com/watch?v")) {*//*
                                                // Initializing video player with developer key
                                                youTubePlayerFragment.initialize(getResources().getString(R.string.maps_api_key),
                                                        new YouTubePlayer.OnInitializedListener() {

                                                            @Override
                                                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                                                                boolean wasRestored) {
                                                                if (!wasRestored) {

                                                                    // String youtube_stream_url = livestream_link;
                                                                    //YouvideoId = livestream_link.substring(livestream_link.lastIndexOf("=") + 1);
                                                                    YouvideoId = getVideoId(livestream_link);
                                                                    youTubePlayer = player;
                                                                    //set the player style default
                                                                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                                                    //cue the 1st video by default
                                                                    youTubePlayer.cueVideo(YouvideoId);//YouvideoId);
                                                                } else {

                                                                }
                                                            }

                                                            @Override
                                                            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                                                                youTubePlayer.play();
                                                                youTubePlayer.getFullscreenControlFlags();
                                                                Log.e("", "Youtube Player View initialization failed");
                                                            }
                                                        });
                                                ll_youtube.setVisibility(View.VISIBLE);
                                                mPlayerView.setVisibility(View.GONE);
                                            } else {
                                                ll_youtube.setVisibility(View.GONE);
                                                mPlayerView.setVisibility(View.VISIBLE);

                                                // Handle hiding/showing of ActionBar
                                                // mPlayerView.addOnFullscreenListener(this);
                                                // Keep the screen on during playback
                                                new KeepScreenOnHandler(mPlayerView, getActivity().getWindow());
                                                // Load a media source
                                                try {
                                                    Bitmap bitmap = retriveVideoFrameFromVideo(livestream_link);
                                                    filePath = CommonFunction.saveImage(getActivity(), bitmap, "livestreamthumb");
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
                                        } else {
                                            ll_youtube.setVisibility(View.GONE);
                                            mPlayerView.setVisibility(View.GONE);
                                            tv_nothing_streaming_currently.setVisibility(View.VISIBLE);
                                            ll_active_user_count.setVisibility(View.GONE);
                                            //tv_nothing_streaming_currently.setVisibility(View.GONE);
                                        }
                                        if (sessionShortDescription.contains("\n")) {
                                            sessionShortDescription = sessionShortDescription.trim().replace("\n", "<br/>");
                                        } else {
                                            sessionShortDescription = sessionShortDescription.trim();
                                        }
                                        String spannedString = String.valueOf(Jsoup.parse(sessionShortDescription)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                                            tvDescription.setText(Utility.trimTrailingWhitespace(strPost));
                                        } else {
                                            Spanned strPost = Html.fromHtml(spannedString);
                                            tvDescription.setText(Utility.trimTrailingWhitespace(strPost));
                                        }
                                        //tvDescription.setText(sessionShortDescription.trim());


                                        tvDescription.setShowingLine(1);
                                        tvDescription.addShowMoreText(" View More");
                                        tvDescription.addShowLessText(" View Less");

                                           *//* tvDescription.setShowMoreColor(Color.RED); // or other color
                                            tvDescription.setShowLessTextColor(Color.RED); // or other color*//*

                                            *//*if (sessionShortDescription.length() > 30) {
                                                CommonFunction.makeTextViewResizable(tvDescription, 1, " View More", true);
                                            }*//*

                                            *//*Bundle bundle = new Bundle();
                                            bundle.putSerializable("agendaDetails", (Serializable) agendaDetails);
                                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            SpotGroupChatFragmentForWebinar fragInfo = new SpotGroupChatFragmentForWebinar();
                                            fragInfo.setArguments(bundle);
                                            transaction.replace(R.id.fragment_frame2, fragInfo);
                                            transaction.commitAllowingStateLoss();*//*
                                        TabPageRedirection(0);

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        //fetchAgendaList.setValue(null);
                    }
                });

    }*/
}