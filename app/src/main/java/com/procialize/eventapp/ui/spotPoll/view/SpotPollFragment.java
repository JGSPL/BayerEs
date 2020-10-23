package com.procialize.eventapp.ui.spotPoll.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.eventinfo.view.EventInfoActivity;
import com.procialize.eventapp.ui.livepoll.model.FetchLivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll;
import com.procialize.eventapp.ui.livepoll.model.Logo;
import com.procialize.eventapp.ui.livepoll.view.LivePollActivity;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class SpotPollFragment extends Fragment {
    String api_token, eventid;
    ImageView iv_logo;
    MutableLiveData<FetchLivePoll> FetchLivePollList = new MutableLiveData<>();
    private APIService eventApi;
    ProgressBar progressBar,progressView;
    Agenda agenda;
    CardView Pollcard;
    TextView  tvPollTitle;
    Button btnPollStart;

    public static SpotPollFragment newInstance() {

        return new SpotPollFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_poll_logo, container, false);
        
        iv_logo = root.findViewById(R.id.iv_logo);
        progressBar = root.findViewById(R.id.progressBar);
        progressView = root.findViewById(R.id.progressView);
        Pollcard = root.findViewById(R.id.Pollcard);
        tvPollTitle = root.findViewById(R.id.tvPollTitle);
        btnPollStart = root.findViewById(R.id.btnPollStart);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");

        CommonFunction.showBackgroundImage(getContext(), Pollcard);
        btnPollStart.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        btnPollStart.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_2)));
        tvPollTitle.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));



        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            getLivepoll(api_token,eventid);

        } else {
        }
        
        return root;
    }

    public MutableLiveData<FetchLivePoll> getLivepoll(String token, String eventid) {
        eventApi = ApiUtils.getAPIService();

        eventApi.SpotLivePollFetch(token,eventid,agenda.getSession_id())
                .enqueue(new Callback<FetchLivePoll>() {
                    @Override
                    public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {
                        if (response.isSuccessful()) {
                            FetchLivePollList.setValue(response.body());
                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(getContext());
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Logo> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Logo>>() {}.getType());

                            //Fetch Livepoll list
                            if(eventLists!=null) {

                                progressBar.setVisibility(View.GONE);

                                List<LivePoll> PollLists = eventLists.get(0).getLivePoll_list();

                                if(PollLists.size()>0) {

                                    Glide.with(getContext())
                                            .load(eventLists.get(0).getLogo_url_path() + eventLists.get(0).getLive_poll_logo().getApp_livepoll_logo())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    progressView.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progressView.setVisibility(View.GONE);

                                                    return false;
                                                }
                                            }).into(iv_logo);


                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    progressView.setVisibility(View.GONE);

                                }


                            }else{

                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                        FetchLivePollList.setValue(null);
                    }
                });

        return FetchLivePollList;
    }

}
