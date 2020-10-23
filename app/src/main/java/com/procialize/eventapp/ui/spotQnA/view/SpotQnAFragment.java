package com.procialize.eventapp.ui.spotQnA.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.spotQnA.adapter.SpotQnAAdapter;
import com.procialize.eventapp.ui.spotQnA.model.FetchSpotQnA;
import com.procialize.eventapp.ui.spotQnA.model.SpotQnA;
import com.procialize.eventapp.ui.spotQnA.model.SpotQnAList;
import com.procialize.eventapp.ui.spotQnA.viewModel.SpotQnAViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.PROFILE_PIC_MEDIA_PATH;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;

public class SpotQnAFragment extends Fragment implements View.OnClickListener ,SpotQnAAdapter.EventAdapterListner{

    TextView tv_ask_question;
    SpotQnAViewModel spotQnAViewModel;
    String api_token, eventid;
    SpotQnAAdapter spotQnAAdapter;
    RecyclerView rv_spot_qna;
    FrameLayout fl_main;
    Agenda agenda;
    String noOfLikes = "0";
    String likeStatus = "";
    SwipeRefreshLayout  question_refresh;
    public static SpotQnAFragment newInstance() {
        return new SpotQnAFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_spot_q_n_a, container, false);
        spotQnAViewModel = ViewModelProviders.of(this).get(SpotQnAViewModel.class);

        fl_main = root.findViewById(R.id.fl_main);
        tv_ask_question = root.findViewById(R.id.tv_ask_question);
        question_refresh = root.findViewById(R.id.question_refresh);
        rv_spot_qna = root.findViewById(R.id.rv_spot_qna);
        tv_ask_question.setOnClickListener(this);
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");


        getDataFromApi();

        question_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                question_refresh.setRefreshing(false);
                if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
                    //currentPage = PAGE_START;
                    getDataFromApi();
                } else {
                    try {
                        Utility.createShortSnackBar(fl_main, "No Internet Connection");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_question:
                Bundle bundle = new Bundle();
                bundle.putSerializable("agendaDetails", (Serializable) agenda);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AskQuestionFragment fragInfo = new AskQuestionFragment();
                fragInfo.setArguments(bundle);
                transaction.replace(R.id.fragment_frame, fragInfo);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    public void setupAgendaAdapter(List<SpotQnA> spotQnAS) {
        spotQnAAdapter = new SpotQnAAdapter(getContext(), spotQnAS, SpotQnAFragment.this);
        rv_spot_qna.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_spot_qna.setAdapter(spotQnAAdapter);
        spotQnAAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLikeClicked(SpotQnA spotQnA, int position,final TextView liketext,final ImageView likeImage) {
        ApiUtils.getAPIService().PostQnALikeSession(api_token, eventid, spotQnA.getId()).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call,
                                   Response<LikePost> response) {
                if (response.isSuccessful()) {
                    likeStatus = response.body().getLike_status();
                    noOfLikes = liketext.getText().toString().split(" ")[0];
                    if (likeStatus.equalsIgnoreCase("1")) {
                        int LikeCount = Integer.parseInt(noOfLikes) + 1;
                        if (LikeCount == 1) {
                            liketext.setText(LikeCount + " Like");
                        } else {
                            liketext.setText(LikeCount + " Likes");
                        }

                        likeImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_active_like));
                    } else {
                        if (Integer.parseInt(noOfLikes) > 0) {
                            int LikeCount = Integer.parseInt(noOfLikes) - 1;
                            if (LikeCount == 1) {
                                liketext.setText(LikeCount + " Like");
                            } else {
                                liketext.setText(LikeCount + " Likes");
                            }
                            likeImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_like));
                            noOfLikes = "0";
                        }
                    }
                    Utility.createShortSnackBar(fl_main, response.body().getHeader().get(0).getMsg());
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                // liketPostUpdate.setValue(null);
                Utility.createShortSnackBar(fl_main, "Failure..");
            }
        });
    }

    public void getDataFromApi()
    {
        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            spotQnAViewModel.getSpotQnA(api_token,eventid,agenda.getSession_id(),
                    "100",
                    "1");
            spotQnAViewModel.getSpotQnAList().observe(getActivity(), new Observer<FetchSpotQnA>() {
                @Override
                public void onChanged(FetchSpotQnA fetchSpotQnA) {
                    if(!fetchSpotQnA.getDetail().isEmpty()) {
                        String strCommentList = fetchSpotQnA.getDetail();
                        RefreashToken refreashToken = new RefreashToken(getContext());
                        String data = refreashToken.decryptedData(strCommentList);
                        try {
                            Gson gson = new Gson();
                            List<SpotQnAList> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<SpotQnAList>>() {
                            }.getType());
                            if (agendaLists != null) {
                                if (agendaLists.size() > 0) {
                                    List<SpotQnA> spotQnAs = agendaLists.get(0).getSession_question_list();
                                    setupAgendaAdapter(spotQnAs);

                                    String strFilePath = CommonFunction.stripquotes(agendaLists.get(0).getProfile_pic_url_path());
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(PROFILE_PIC_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                                    SharedPreference.putPref(getActivity(), map);

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (spotQnAViewModel != null && spotQnAViewModel.getSpotQnAList().hasObservers()) {
                            spotQnAViewModel.getSpotQnAList().removeObservers(getActivity());
                        }
                    }
                }
            });
        }
    }
}