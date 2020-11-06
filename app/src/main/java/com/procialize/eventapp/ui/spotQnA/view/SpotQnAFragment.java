package com.procialize.eventapp.ui.spotQnA.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
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
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.PROFILE_PIC_MEDIA_PATH;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;

public class SpotQnAFragment extends Fragment implements View.OnClickListener, SpotQnAAdapter.EventAdapterListner {

    TextView tv_ask_question,tv_cancel;
    SpotQnAViewModel spotQnAViewModel;
    String api_token, eventid;
    SpotQnAAdapter spotQnAAdapter;
    RecyclerView rv_spot_qna;
    FrameLayout fl_main;
    Agenda agenda;
    String noOfLikes = "0";
    String likeStatus = "";
    SwipeRefreshLayout question_refresh;
    LinearLayout ll_ask_question, ll_qna,ll_post_status;
    TextView tv_count,tv_send_question;
    EditText et_question;
    LinearLayout ll_cancel, ll_send_question,ll_cancel_inner;

    int pageNumber = 1;
    int pageSize = 30;

    int totalPages = 0;
    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    LinearLayoutManager linearLayoutManager;

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
        ll_ask_question = root.findViewById(R.id.ll_ask_question);
        ll_post_status = root.findViewById(R.id.ll_post_status);
        ll_qna = root.findViewById(R.id.ll_qna);
        tv_ask_question.setOnClickListener(this);
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_spot_qna.setLayoutManager(mLayoutManager);
        spotQnAAdapter = new SpotQnAAdapter(getActivity()/*, FragmentNewsFeed.this*/, SpotQnAFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_spot_qna.setLayoutManager(linearLayoutManager);
        rv_spot_qna.setItemAnimator(new DefaultItemAnimator());
        rv_spot_qna.setAdapter(spotQnAAdapter);
        spotQnAAdapter.notifyDataSetChanged();
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


        rv_spot_qna.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                Log.e("currentPage", currentPage + "");
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //Ask Question
        tv_send_question = root.findViewById(R.id.tv_send_question);
        tv_cancel = root.findViewById(R.id.tv_cancel);
        tv_count = root.findViewById(R.id.tv_count);
        et_question = root.findViewById(R.id.et_question);
        ll_cancel = root.findViewById(R.id.ll_cancel);
        ll_cancel_inner = root.findViewById(R.id.ll_cancel_inner);
        ll_send_question = root.findViewById(R.id.ll_send_question);
        fl_main = root.findViewById(R.id.fl_main);
        ll_cancel.setOnClickListener(this);
        ll_send_question.setOnClickListener(this);

        et_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_count.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setDynamicColor();
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_question:
                ll_ask_question.setVisibility(View.VISIBLE);
                ll_qna.setVisibility(View.GONE);
              /*  Bundle bundle = new Bundle();
                bundle.putSerializable("agendaDetails", (Serializable) agenda);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AskQuestionFragment fragInfo = new AskQuestionFragment();
                fragInfo.setArguments(bundle);
                transaction.replace(R.id.fragment_frame, fragInfo);
                transaction.addToBackStack(null);
                transaction.commit();*/
                break;
            case R.id.ll_cancel:
                ll_ask_question.setVisibility(View.GONE);
                ll_qna.setVisibility(View.VISIBLE);
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, SpotQnAFragment.newInstance(), "")
                        .commit();*/
                break;
            case R.id.ll_send_question:
                if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
                    ll_send_question.setEnabled(true);
                    final String question = et_question.getText().toString().trim();

                    if (!question.isEmpty()) {
                        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
                        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

                        spotQnAViewModel.submitSpotQnA(api_token, eventid, agenda.getSession_id(), question);
                        spotQnAViewModel.submitSpotQnAList().observe(getActivity(), new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(final LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    ll_ask_question.setVisibility(View.GONE);
                                    ll_qna.setVisibility(View.VISIBLE);

                                    getDataFromApi();
                                    Utility.createShortSnackBar(fl_main, loginOrganizer.getHeader().get(0).getMsg());
                                    et_question.setText("");
                                } else {
                                    getDataFromApi();
                                    ll_send_question.setEnabled(true);
                                    Utility.createShortSnackBar(fl_main, loginOrganizer.getHeader().get(0).getMsg());
                                }
                                if (spotQnAViewModel != null && spotQnAViewModel.submitSpotQnAList().hasObservers()) {
                                    spotQnAViewModel.submitSpotQnAList().removeObservers(getActivity());
                                }
                            }
                        });
                    } else {
                        ll_send_question.setEnabled(true);
                        Utility.createShortSnackBar(fl_main, "Please enter some question..");
                    }

                } else {
                    Utility.createShortSnackBar(fl_main, "No Internet Connection..!");
                }
                break;
        }
    }

    public void setupAgendaAdapter(List<SpotQnA> spotQnAS) {
        /*spotQnAAdapter = new SpotQnAAdapter(getContext(), spotQnAS, SpotQnAFragment.this);
        rv_spot_qna.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_spot_qna.setAdapter(spotQnAAdapter);
        spotQnAAdapter.notifyDataSetChanged();*/

        if (spotQnAAdapter == null) {
           /* newsfeedAdapter = new NewsFeedAdapter(getContext(), newsfeedArrayList, NewsFeedFragment.this);
            recycler_feed.setLayoutManager(new LinearLayoutManager(getContext()));*/
            rv_spot_qna.setAdapter(spotQnAAdapter);
            rv_spot_qna.setItemAnimator(new DefaultItemAnimator());
        } else {
            spotQnAAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLikeClicked(SpotQnA spotQnA, final int position, final TextView liketext, final ImageView likeImage) {
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
                        List<SpotQnA> spotQnA = spotQnAAdapter.getSpotQnAList();
                        spotQnA.get(position).setLike_flag("1");
                        spotQnA.get(position).setTotal_likes(LikeCount + "");

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

                            List<SpotQnA> spotQnA = spotQnAAdapter.getSpotQnAList();
                            spotQnA.get(position).setLike_flag("0");
                            spotQnA.get(position).setTotal_likes(LikeCount + "");
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

    public void getDataFromApi() {
        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            currentPage = PAGE_START;
            ApiUtils.getAPIService().spotQnAFetch(api_token, eventid, agenda.getSession_id(),
                    pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<FetchSpotQnA>() {
                        @Override
                        public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                            if (response.isSuccessful()) {
                                String strCommentList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(getContext());
                                String data = refreashToken.decryptedData(strCommentList);
                                try {
                                    spotQnAAdapter.removeLoadingFooter();
                                    isLoading = false;
                                    Gson gson = new Gson();
                                    List<SpotQnA> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<SpotQnA>>() {
                                    }.getType());
                                    if (agendaLists != null) {
                                        if (agendaLists.size() > 0) {

                                          /*  String strFilePath = CommonFunction.stripquotes(agendaLists.get(0).getProfile_pic_url_path());
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(PROFILE_PIC_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                                            SharedPreference.putPref(getActivity(), map);*/
                                            String totRecords = response.body().getTotalRecords();

                                            Long longTotalRecords = Long.parseLong(totRecords);

                                            if (longTotalRecords < pageSize) {
                                                totalPages = 1;
                                            } else {
                                                if ((int) (longTotalRecords % pageSize) == 0) {
                                                    totalPages = (int) (longTotalRecords / pageSize);
                                                } else {
                                                    totalPages = (int) (longTotalRecords / pageSize) + 1;
                                                }
                                            }

                                            Log.e("totalPages", "" + totalPages);

                                            //List<SpotQnA> spotQnAs = agendaLists.get
                                            /*spotQnAAdapter.getSpotQnAList().clear();
                                            spotQnAAdapter.notifyDataSetChanged();*/
                                            spotQnAAdapter.getSpotQnAList().clear();
                                            spotQnAAdapter.notifyDataSetChanged();
                                            spotQnAAdapter.addAll(agendaLists);
                                            setupAgendaAdapter(agendaLists);

                                            if (currentPage <= totalPages)
                                                spotQnAAdapter.addLoadingFooter();
                                            else isLastPage = true;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                            Utility.createShortSnackBar(fl_main, "Failure..!");
                        }
                    });
        }
    }

    public void loadNextPage() {
        Log.e("pageNumber", "loadNextPage: " + currentPage);
        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            ApiUtils.getAPIService().spotQnAFetch(api_token, eventid, agenda.getSession_id(),
                    pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<FetchSpotQnA>() {
                        @Override
                        public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                            if (response.isSuccessful()) {
                                spotQnAAdapter.removeLoadingFooter();
                                isLoading = false;
                                String strCommentList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(getContext());
                                String data = refreashToken.decryptedData(strCommentList);
                                try {
                                    Gson gson = new Gson();
                                    List<SpotQnA> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<SpotQnA>>() {
                                    }.getType());
                                    if (agendaLists != null) {
                                        if (agendaLists.size() > 0) {
                                            //List<SpotQnA> spotQnAs = agendaLists.get(0).getSession_question_list();
                                            spotQnAAdapter.addAll(agendaLists);

                                            setupAgendaAdapter(agendaLists);

                                            if (currentPage != totalPages)
                                                spotQnAAdapter.addLoadingFooter();
                                            else
                                                isLastPage = true;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                            //Utility.createShortSnackBar(fl_main, "Failure..!");
                        }
                    });
        }
    }

    public void setDynamicColor()
    {
        tv_ask_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_1)));
        tv_ask_question.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_2)));

        ll_cancel.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_1)));
        ll_cancel_inner.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_2)));
        tv_cancel.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_1)));
        ll_send_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_1)));
        tv_send_question.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_2)));

        et_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_2)));
        ll_post_status.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        et_question.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        et_question.setHintTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));

    }
}