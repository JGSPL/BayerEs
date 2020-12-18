package com.procialize.bayer2020.ui.attendee.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.attendee.adapter.AttendeeAdapter;
import com.procialize.bayer2020.ui.attendee.model.Attendee;
import com.procialize.bayer2020.ui.attendee.model.FetchAttendee;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendee.viewmodel.AttendeeDatabaseViewModel;
import com.procialize.bayer2020.ui.attendee.viewmodel.AttendeeViewModel;
import com.procialize.bayer2020.ui.attendeeChat.ChatActivity;
import com.procialize.bayer2020.ui.attendeeChat.viewmodel.AttendeeChatCountViewModel;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationScrollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.ui.newsfeed.adapter.PaginationListener.PAGE_START;


public class AttendeeFragment extends Fragment implements AttendeeAdapter.AttendeeAdapterListner, PaginationAdapterCallback {

    RecyclerView attendeerecycler;
    SwipeRefreshLayout attendeefeedrefresh;
    EditText searchEt;

    AttendeeAdapter attendeeAdapter;
    SessionManager sessionManager;
    String eventid, colorActive;
    TextView pullrefresh;
    String picPath = "";
    // TODO: Rename and change types of parameters

    private APIService mAPIService;
    private ProgressBar progressBar;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<Attendee> attendeeList;
    private List<Attendee> attendeesDBList = new ArrayList<>();
    LinearLayout linear;
    String api_token;
    Attendee attendeeTmp;
    Boolean isVisible = false;
    public static Activity activity;
    int totalPages = 20000;
    int attendeePageNumber = 1;
    int attendeePageSize = 100;
    AttendeeViewModel attendeeViewModel;
    AttendeeDatabaseViewModel attendeeDatabaseViewModel;
    public static AttendeeChatCountViewModel  attendeeChatCountViewModel ;
    IntentFilter spotChatFilter;

    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    LinearLayoutManager linearLayoutManager;
    ImageView iv_search;
    String strAttendeeName = "";
    String attendee_message = "";
    SpotChatReciever spotChatReciever;

    public static AttendeeFragment newInstance() {

        return new AttendeeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendee, container, false);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());

        attendeerecycler = root.findViewById(R.id.recycler_attendee);

        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        iv_search = root.findViewById(R.id.iv_search);

        searchEt = root.findViewById(R.id.searchEt);
        attendeefeedrefresh = root.findViewById(R.id.swiperefresh_attendee);
        progressBar = root.findViewById(R.id.progressBar);
        linear = root.findViewById(R.id.linear);
        pullrefresh = root.findViewById(R.id.pullrefresh);
        cd = ConnectionDetector.getInstance(getActivity());
        sessionManager = new SessionManager(getContext());
        attendeeViewModel = ViewModelProviders.of(this).get(AttendeeViewModel.class);

        String eventColor3 = SharedPreference.getPref(getContext(), EVENT_COLOR_4);

        String eventColor3Opacity40 = eventColor3.replace("#", "");


        attendeeDatabaseViewModel = ViewModelProviders.of(this).get(AttendeeDatabaseViewModel.class);
        attendeeChatCountViewModel = ViewModelProviders.of(this).get(AttendeeChatCountViewModel.class);

        searchEt.setHintTextColor(Color.parseColor(eventColor3));
        searchEt.setTextColor(Color.parseColor(eventColor3));
        int color = Color.parseColor(eventColor3);
       iv_search.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        attendeerecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);


        // pullrefresh.setTextColor(Color.parseColor(colorActive));

        CommonFirebase.crashlytics("Attendee", api_token);
        CommonFirebase.firbaseAnalytics(getActivity(), "Attendee", api_token);
        mAPIService = ApiUtils.getAPIService();

        //searchBtn.setTextColor(getResources().getColor(R.color.colorwhite));
        //searchBtn.setBackgroundColor(Color.parseColor(colorActive));
        /*iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAttendeeName = searchEt.getText().toString().trim();
                attendeeAdapter.getAttendeeListFiltered().clear();
                attendeeAdapter.notifyDataSetChanged();
                loadFirstPage(strAttendeeName);
            }
        });*/

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        if (cd.isConnectingToInternet()) {
            strAttendeeName = searchEt.getText().toString().trim();
            if (attendeeAdapter != null) {
                attendeeAdapter.getAttendeeListFiltered().clear();
                attendeeAdapter.notifyDataSetChanged();
            }
            loadFirstPage("");

        } else {
            getAttendeeFromDb();
            progressBar.setVisibility(View.GONE);
            attendeefeedrefresh.setRefreshing(false);

        }
        attendeerecycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {


                if (cd.isConnectingToInternet()) {
                    isLoading = true;
                    currentPage += 1;

                    loadNextPage();
                } else {
                    progressBar.setVisibility(View.GONE);
                    attendeefeedrefresh.setRefreshing(false);


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
                        attendeeAdapter.getFilter().filter(s.toString());
                        attendeeAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            spotChatReciever = new SpotChatReciever();
            spotChatFilter = new IntentFilter(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(spotChatReciever, spotChatFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        attendeefeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        return root;
    }

    public void getAttendeeFromDb() {
        attendeeDatabaseViewModel.getAttendeeDetails(getActivity());
        attendeeDatabaseViewModel.getAttendeeList().observeForever(new Observer<List<TableAttendee>>() {
            @Override
            public void onChanged(List<TableAttendee> tableAttendees) {
                for (int i = 0; i < tableAttendees.size(); i++) {
                    final Attendee attendee = new Attendee();
                    attendee.setFirebase_status("");
                    attendee.setMobile(tableAttendees.get(i).getMobile());
                    attendee.setEmail(tableAttendees.get(i).getEmail());
                    attendee.setFirebase_id(tableAttendees.get(i).getFirebase_id());
                    attendee.setFirebase_name(tableAttendees.get(i).getFirebase_name());
                    attendee.setFirebase_username(tableAttendees.get(i).getFirebase_username());
                    attendee.setAttendee_id(tableAttendees.get(i).getAttendee_id());
                    attendee.setFirst_name(tableAttendees.get(i).getFirst_name());
                    attendee.setLast_name(tableAttendees.get(i).getLast_name());
                    attendee.setCity(tableAttendees.get(i).getCity());
                    attendee.setDesignation(tableAttendees.get(i).getDesignation());
                    attendee.setCompany_name(tableAttendees.get(i).getCompany_name());
                    attendee.setAttendee_type(tableAttendees.get(i).getAttendee_type());
                    attendee.setTotal_sms(tableAttendees.get(i).getTotal_sms());
                    attendee.setProfile_picture(tableAttendees.get(i).getProfile_picture());
                    attendee.setFirebase_status(tableAttendees.get(0).getFirebase_status());
                    attendeesDBList.add(attendee);
                }
                setupEventAdapter(attendeesDBList);
            }
        });
    }

    private void doRefresh() {

      /*  if (callTopRatedMoviesApi().isExecuted())
            callTopRatedMoviesApi().cancel();*/

        if (cd.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);
            // TODO: Check if data is stale.
            //  Execute network request if cache is expired; otherwise do not update data.
            strAttendeeName = searchEt.getText().toString().trim();
            attendeeAdapter.getAttendeeListFiltered().clear();
            attendeeDatabaseViewModel.deleteAllAttendee(getActivity());


            attendeeAdapter.notifyDataSetChanged();
            loadFirstPage("");
            attendeefeedrefresh.setRefreshing(false);
        } else {
            progressBar.setVisibility(View.GONE);
            attendeefeedrefresh.setRefreshing(false);

            /*progressBar.setVisibility(View.GONE);
            attendeeAdapter.getAttendeeListFiltered().clear();
            attendeeAdapter.notifyDataSetChanged();
            attendeesDBList = dbHelper.getAllAttendeeDetails("0", attendeePageSize + "");
            attendeeAdapter.addAll(attendeesDBList);
            attendeefeedrefresh.setRefreshing(false);*/
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
        attendeeViewModel.getAttendee(api_token, eventid, searchtext, String.valueOf(attendeePageNumber), String.valueOf(attendeePageSize));
        attendeeViewModel.getAttendeeList().observe(this, new Observer<FetchAttendee>() {
            @Override
            public void onChanged(FetchAttendee event) {
                //List<Attendee> eventLists = event.getAttandeeList();
                if(event !=null){

                String strCommentList =event.getDetail();
                RefreashToken refreashToken = new RefreashToken(getContext());
                String data = refreashToken.decryptedData(strCommentList);
                Gson gson = new Gson();
                List<Attendee> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Attendee>>() {}.getType());
                if (eventLists != null) {
                    attendeeDatabaseViewModel.deleteAllAttendee(getActivity());
                    //attendeeChatCountViewModel.deleteAllAttendee(getContext());
                    attendeeDatabaseViewModel.insertIntoDb(getActivity(), eventLists);

                    /*String ChatValue =  SharedPreference.getPref(getContext(), SharedPreferencesConstant.Chat_Table);
                    if(ChatValue.equalsIgnoreCase("")) {
                        List<Table_Attendee_Chatcount> attenChatCount = new ArrayList<>();
                        for (int i = 0; i < eventLists.size(); i++) {
                            Table_Attendee_Chatcount attChat = new Table_Attendee_Chatcount();
                            attChat.setChatCount_receId(eventLists.get(i).getFirebase_id());
                            attChat.setChat_count(0);
                            attChat.setChat_count_read("0");
                            attChat.setChat_mess("");
                            attenChatCount.add(attChat);
                        }
                        attendeeChatCountViewModel.insertIntoDb(getContext(), attenChatCount);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(ChatValue, "1");
                        SharedPreference.putPref(getContext(), map);
                    }*/
                    progressBar.setVisibility(View.GONE);

                    setupEventAdapter(eventLists);
                }



                }

                //Delete All attendee from local db and insert attendee


                if (attendeeViewModel != null && attendeeViewModel.getAttendeeList().hasObservers()) {
                    attendeeViewModel.getAttendeeList().removeObservers(getActivity());
                }
            }
        });
    }

    public void setupEventAdapter(List<Attendee> commentList) {
        List<Attendee> attendeeList = new ArrayList<>();
        if(commentList!=null) {
            for (int i = 0; i < commentList.size(); i++) {
                if (!commentList.get(i).getAttendee_id().equalsIgnoreCase(SharedPreference.getPref(getActivity(), KEY_ATTENDEE_ID))) {
                    attendeeList.add(commentList.get(i));
                }
            }
            attendeeAdapter = new AttendeeAdapter(getContext(), attendeeList, AttendeeFragment.this);
            attendeerecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeeAdapter.notifyDataSetChanged();
        }
    }


    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        attendeeViewModel.getAttendee(api_token, eventid, "", String.valueOf(attendeePageNumber), String.valueOf(attendeePageSize));
        attendeeViewModel.getAttendeeList().observe(this, new Observer<FetchAttendee>() {
            @Override
            public void onChanged(FetchAttendee event) {
                List<Attendee> eventLists = event.getAttandeeList();
                progressBar.setVisibility(View.GONE);
                attendeeAdapter.removeLoadingFooter();
                isLoading = false;

                /*List<Attendee> results = event.getAttandeeList();
                attendeeAdapter.addAll(results);
                //insert attendee in local db
                attendeeDatabaseViewModel.insertIntoDb(getActivity(), results);*/
                String strCommentList =event.getDetail();
                RefreashToken refreashToken = new RefreashToken(getContext());
                String data = refreashToken.decryptedData(strCommentList);
                Gson gson = new Gson();
                List<Attendee> results = gson.fromJson(data, new TypeToken<ArrayList<Attendee>>() {}.getType());
                if (results != null) {
                    attendeeAdapter.addAll(results);
                    //insert attendee in local db
                    attendeeDatabaseViewModel.insertIntoDb(getActivity(), results);
                }


                if (currentPage != totalPages)
                    attendeeAdapter.addLoadingFooter();
                else
                    isLastPage = true;
                //  setupEventAdapter(eventLists);
            }
        });
    }

    @Override
    public void onContactSelected(Attendee attendee) {
        if (!(attendee.getFirebase_status().equalsIgnoreCase("0"))) {
            getActivity().startActivity(new Intent(getContext(), ChatActivity.class)
                    .putExtra("page", "ListPage")
                    .putExtra("Attendee", (Serializable) attendee));
        } else {
            getActivity().startActivity(new Intent(getContext(), AttendeeDetailActivity.class)
                    .putExtra("Attendee", (Serializable) attendee));
        }
        // getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {
            strAttendeeName = searchEt.getText().toString().trim();
            if (attendeeAdapter != null) {
                attendeeAdapter.getAttendeeListFiltered().clear();
                attendeeAdapter.notifyDataSetChanged();
            }
            loadFirstPage("");

        } else {
           // getAttendeeFromDb();
            attendeefeedrefresh.setRefreshing(false);
            progressBar.setVisibility(View.GONE);

        }
    }

    private class SpotChatReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            try{
                attendeeAdapter.notifyDataSetChanged();
                attendeerecycler.smoothScrollToPosition(0);}catch (Exception e)
            {e.printStackTrace();}
        }
    }

}