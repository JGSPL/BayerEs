package com.procialize.eventapp.ui.speaker.view;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;

import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.session.SessionManager;

import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.eventinfo.view.EventInfoActivity;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.eventapp.ui.speaker.adapter.SpeakerAdapter;
import com.procialize.eventapp.ui.speaker.model.FetchSpeaker;
import com.procialize.eventapp.ui.speaker.model.Speaker;
import com.procialize.eventapp.ui.speaker.roomDB.TableSpeaker;
import com.procialize.eventapp.ui.speaker.viewmodel.SpeakerDtabaseViewModel;
import com.procialize.eventapp.ui.speaker.viewmodel.SpeakerViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;


public class SpeakerFragment  extends Fragment implements SpeakerAdapter.SpeakerAdapterListner, PaginationAdapterCallback {

    RecyclerView speakerrecycler;
    SwipeRefreshLayout speakerfeedrefresh;
    EditText searchEt;

    SpeakerAdapter SpeakerAdapter;
    SessionManager sessionManager;
    String eventid, colorActive;
    TextView pullrefresh;
    String picPath = "";

    // TODO: Rename and change types of parameters

    private APIService mAPIService;
    private ProgressBar progressBar;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<Speaker> attendeeList;
    private List<Speaker> speakerDBList = new ArrayList<>();
    LinearLayout linear;
    String api_token;
    Speaker attendeeTmp;
    Boolean isVisible = false;
    public static Activity activity;
    int totalPages = 20000;
    int attendeePageNumber = 1;
    int attendeePageSize = 10;
    SpeakerViewModel speakerViewModel;
   SpeakerDtabaseViewModel SpeakerDtabaseViewModel;

    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    LinearLayoutManager linearLayoutManager;
    ImageView iv_search;
    String strAttendeeName = "";

    public static SpeakerFragment newInstance() {

        return new SpeakerFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_speaker, container, false);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());

        speakerrecycler = root.findViewById(R.id.recycler_speaker);

        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        iv_search = root.findViewById(R.id.iv_search);

        searchEt = root.findViewById(R.id.searchEt);
        speakerfeedrefresh = root.findViewById(R.id.swiperefresh_speaker);
        progressBar = root.findViewById(R.id.progressBar);
        linear = root.findViewById(R.id.linear);
        pullrefresh = root.findViewById(R.id.pullrefresh);
        cd = ConnectionDetector.getInstance(getActivity());
        sessionManager = new SessionManager(getContext());
        speakerViewModel = ViewModelProviders.of(this).get(SpeakerViewModel.class);

        String eventColor3 = SharedPreference.getPref(getContext(), EVENT_COLOR_4);

        String eventColor3Opacity40 = eventColor3.replace("#", "");


        SpeakerDtabaseViewModel = ViewModelProviders.of(this).get(SpeakerDtabaseViewModel.class);
        searchEt.setHintTextColor(Color.parseColor(eventColor3));
        searchEt.setTextColor(Color.parseColor(eventColor3));
        int color = Color.parseColor(eventColor3);
        iv_search.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        speakerrecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);


        mAPIService = ApiUtils.getAPIService();


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        if (cd.isConnectingToInternet()) {
            strAttendeeName = searchEt.getText().toString().trim();
            if (SpeakerAdapter != null) {
                SpeakerAdapter.getSpeakerListFiltered().clear();
                SpeakerAdapter.notifyDataSetChanged();
            }
            loadFirstPage("");

        } else {
            getAttendeeFromDb();
        }
        speakerrecycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {


                if (cd.isConnectingToInternet()) {
                    isLoading = true;
                    currentPage += 1;

                    loadNextPage();
                } else {
                }
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


        try {
            searchEt.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    try {
                        SpeakerAdapter.getFilter().filter(s.toString());
                        SpeakerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        speakerfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        return root;
    }

    public void getAttendeeFromDb() {
        SpeakerDtabaseViewModel.getSpeakerDetails(getActivity());
        SpeakerDtabaseViewModel.getSpeakerList().observeForever(new Observer<List<TableSpeaker>>() {
            @Override
            public void onChanged(List<TableSpeaker> tableAttendees) {
                for (int i = 0; i < tableAttendees.size(); i++) {
                    final Speaker Speaker = new Speaker();
                    Speaker.setFirebase_status("");
                    Speaker.setMobile(tableAttendees.get(i).getMobile());
                    Speaker.setEmail(tableAttendees.get(i).getEmail());
                    Speaker.setFirebase_id(tableAttendees.get(i).getFirebase_id());
                    Speaker.setFirebase_name(tableAttendees.get(i).getFirebase_name());
                    Speaker.setFirebase_username(tableAttendees.get(i).getFirebase_username());
                    Speaker.setAttendee_id(tableAttendees.get(i).getAttendee_id());
                    Speaker.setFirst_name(tableAttendees.get(i).getFirst_name());
                    Speaker.setLast_name(tableAttendees.get(i).getLast_name());
                    Speaker.setCity(tableAttendees.get(i).getCity());
                    Speaker.setDesignation(tableAttendees.get(i).getDesignation());
                    Speaker.setCompany_name(tableAttendees.get(i).getCompany_name());
                    Speaker.setAttendee_type(tableAttendees.get(i).getAttendee_type());
                    Speaker.setTotal_sms(tableAttendees.get(i).getTotal_sms());
                    Speaker.setProfile_picture(tableAttendees.get(i).getProfile_picture());
                    speakerDBList.add(Speaker);
                }
                setupEventAdapter(speakerDBList);
            }
        });
    }

    private void doRefresh() {

        if (cd.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);
            // TODO: Check if data is stale.
            //  Execute network request if cache is expired; otherwise do not update data.
            strAttendeeName = searchEt.getText().toString().trim();
            SpeakerAdapter.getSpeakerListFiltered().clear();
            SpeakerAdapter.notifyDataSetChanged();
            loadFirstPage("");
            speakerfeedrefresh.setRefreshing(false);
        } else {
            progressBar.setVisibility(View.GONE);
            speakerfeedrefresh.setRefreshing(false);

        }
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void loadFirstPage(String searchtext) {
        Log.d("First Page", "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked


        currentPage = PAGE_START;
        speakerViewModel.getSpeaker(api_token, eventid, searchtext);

        speakerViewModel.getSpeakerList().observe(this, new Observer<FetchSpeaker>() {
            @Override
            public void onChanged(FetchSpeaker event) {
                //List<Speaker> eventLists = event.getSpeakerList();
                if (speakerViewModel != null && speakerViewModel.getSpeakerList().hasObservers()) {
                    speakerViewModel.getSpeakerList().removeObservers(SpeakerFragment.this);
                }
                String strCommentList =event.getDetail();
                if(strCommentList!=null) {
                    RefreashToken refreashToken = new RefreashToken(getContext());
                    String data = refreashToken.decryptedData(strCommentList);
                    Gson gson = new Gson();
                    List<Speaker> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Speaker>>() {
                    }.getType());

                    //Delete All Speaker from local db and insert Speaker
                    if (eventLists != null) {
                        SpeakerDtabaseViewModel.deleteAllSpeaker(getActivity());
                        SpeakerDtabaseViewModel.insertIntoDb(getActivity(), eventLists);

                        progressBar.setVisibility(View.GONE);

                        setupEventAdapter(eventLists);
                    } else {
                        progressBar.setVisibility(View.GONE);

                    }
                }
               /* if (SpeakerDtabaseViewModel != null && SpeakerDtabaseViewModel.getSpeakerList().hasObservers()) {
                    SpeakerDtabaseViewModel.getSpeakerList().removeObservers(SpeakerFragment.this);
                }*/
                if (speakerViewModel != null && speakerViewModel.getSpeakerList().hasObservers()) {
                    speakerViewModel.getSpeakerList().removeObservers(SpeakerFragment.this);
                }
            }
        });

    }

    public void setupEventAdapter(List<Speaker> commentList) {
        SpeakerAdapter = new SpeakerAdapter(getContext(), commentList, SpeakerFragment.this);
        speakerrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        speakerrecycler.setAdapter(SpeakerAdapter);
        SpeakerAdapter.notifyDataSetChanged();
    }


    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        speakerViewModel.getSpeaker(api_token, eventid, "");
        speakerViewModel.getSpeakerList().observe(this, new Observer<FetchSpeaker>() {
            @Override
            public void onChanged(FetchSpeaker event) {
                //List<Speaker> eventLists = event.getSpeakerList();
                progressBar.setVisibility(View.GONE);
                SpeakerAdapter.removeLoadingFooter();
                isLoading = false;

               // List<Speaker> results = event.getSpeakerList();
                String strCommentList =event.getDetail();
                RefreashToken refreashToken = new RefreashToken(getContext());
                String data = refreashToken.decryptedData(strCommentList);
                Gson gson = new Gson();
                List<Speaker> results = gson.fromJson(data, new TypeToken<ArrayList<Speaker>>() {}.getType());

                SpeakerAdapter.addAll(results);
                //insert Speaker in local db
                SpeakerDtabaseViewModel.insertIntoDb(getActivity(), results);

                if (currentPage != totalPages)
                    SpeakerAdapter.addLoadingFooter();
                else
                    isLastPage = true;
                //  setupEventAdapter(eventLists);
            }
        });
    }


    @Override
    public void onContactSelected(Speaker Speaker) {
        getActivity().startActivity(new Intent(getContext(), SpeakerDetailActivity.class)
                .putExtra("Speaker", (Serializable) Speaker));
    }
}