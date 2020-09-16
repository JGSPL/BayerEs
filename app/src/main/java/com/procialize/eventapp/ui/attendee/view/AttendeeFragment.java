package com.procialize.eventapp.ui.attendee.view;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.attendee.adapter.AttendeeAdapter;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeViewModel;
import com.procialize.eventapp.ui.attendeeChat.ChatActivity;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationScrollListener;

import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;


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
    private List<Attendee> attendeesDBList;
    LinearLayout linear;
    String api_token;
    Attendee attendeeTmp;
    Boolean isVisible = false;
    public static Activity activity;
    int totalPages = 20000;
    int attendeePageNumber = 1;
    int attendeePageSize = 10;
    AttendeeViewModel attendeeViewModel;

    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    LinearLayoutManager linearLayoutManager;
    ImageView iv_search;
    String strAttendeeName = "";
    String attendee_message = "";
    public static AttendeeFragment newInstance() {

        return new AttendeeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendee, container, false);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());

        attendeerecycler = root.findViewById(R.id.recycler_attendee);

        api_token = SharedPreference.getPref(getActivity(),AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(),EVENT_ID);

        searchEt = root.findViewById(R.id.searchEt);
        attendeefeedrefresh = root.findViewById(R.id.swiperefresh_attendee);
        progressBar = root.findViewById(R.id.progressBar);
        linear = root.findViewById(R.id.linear);
        pullrefresh = root.findViewById(R.id.pullrefresh);
        cd = ConnectionDetector.getInstance(getActivity());
        sessionManager = new SessionManager(getContext());
        attendeeViewModel = ViewModelProviders.of(this).get(AttendeeViewModel.class);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        attendeerecycler.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);


       // pullrefresh.setTextColor(Color.parseColor(colorActive));


        mAPIService = ApiUtils.getAPIService();

        iv_search = root.findViewById(R.id.iv_search);
        //searchBtn.setTextColor(getResources().getColor(R.color.colorwhite));
        //searchBtn.setBackgroundColor(Color.parseColor(colorActive));
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAttendeeName = searchEt.getText().toString().trim();
                attendeeAdapter.getAttendeeListFiltered().clear();
                attendeeAdapter.notifyDataSetChanged();
                loadFirstPage();
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        if (cd.isConnectingToInternet()) {
            strAttendeeName =  searchEt.getText().toString().trim();
            if(attendeeAdapter!=null) {
                attendeeAdapter.getAttendeeListFiltered().clear();
                attendeeAdapter.notifyDataSetChanged();
            }
            loadFirstPage();
        } else {
            /*db = procializeDB.getReadableDatabase();

            attendeesDBList = dbHelper.getAllAttendeeDetails("0", attendeePageSize + "");
            attendeeAdapter.addAll(attendeesDBList);*/
        }




        attendeerecycler.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {


                if (cd.isConnectingToInternet()) {
                    isLoading = true;
                    currentPage += 1;

                    loadNextPage();
                } else {
                    /*int totalPages_db = 0;
                    int totRecords = dbHelper.getAttendeeDetails().size();
                    if ((int) (totRecords % attendeePageSize) == 0) {
                        totalPages_db = (int) (totRecords / attendeePageSize);
                    } else {
                        totalPages_db = (int) (totRecords / attendeePageSize) + 1;
                    }

                    if (currentPage < totalPages_db) {

                        attendeeAdapter.removeLoadingFooter();
                        isLoading = false;

                        currentPage += 1;

                        attendeesDBList = dbHelper.getAllAttendeeDetails("" + attendeeAdapter.getAttendeeListFiltered().size(), attendeePageSize + "");
                        attendeeAdapter.addAll(attendeesDBList);
                        attendeefeedrefresh.setRefreshing(false);

                        if (currentPage != totalPages)
                            attendeeAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }*/
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

                    if (attendeeAdapter != null) {
                        /*try {
                            attendeeAdapter.getFilter().filter(s.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //attendeefeedrefresh.setOnRefreshListener(/*this::doRefresh*/);
        return root;
    }
    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
      /*  if (callTopRatedMoviesApi().isExecuted())
            callTopRatedMoviesApi().cancel();*/

        if (cd.isConnectingToInternet()) {
            // TODO: Check if data is stale.
            //  Execute network request if cache is expired; otherwise do not update data.
            strAttendeeName =  searchEt.getText().toString().trim();
            attendeeAdapter.getAttendeeListFiltered().clear();
            attendeeAdapter.notifyDataSetChanged();
            loadFirstPage();
            attendeefeedrefresh.setRefreshing(false);
        } else {
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
    private void loadFirstPage() {
        Log.d("First Page", "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        currentPage = PAGE_START;
        attendeeViewModel.getAttendee(api_token,eventid, "", String.valueOf(attendeePageNumber),String.valueOf(attendeePageSize));
        attendeeViewModel.getEventList().observe(this, new Observer<FetchAttendee>() {
            @Override
            public void onChanged(FetchAttendee event) {
                List<Attendee> eventLists = event.getAttandeeList();
            progressBar.setVisibility(View.GONE);

                setupEventAdapter(eventLists);
            }
        });
          }

    public void setupEventAdapter(List<Attendee> commentList) {
        attendeeAdapter = new AttendeeAdapter(getContext(), commentList, AttendeeFragment.this);
        attendeerecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        attendeerecycler.setAdapter(attendeeAdapter);
        attendeeAdapter.notifyDataSetChanged();
    }


    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        attendeeViewModel.getAttendee(api_token,eventid, "", String.valueOf(attendeePageNumber),String.valueOf(attendeePageSize));
        attendeeViewModel.getEventList().observe(this, new Observer<FetchAttendee>() {
            @Override
            public void onChanged(FetchAttendee event) {
                List<Attendee> eventLists = event.getAttandeeList();
                progressBar.setVisibility(View.GONE);
                attendeeAdapter.removeLoadingFooter();
                isLoading = false;

                List<Attendee> results = event.getAttandeeList();
                attendeeAdapter.addAll(results);
                if (currentPage != totalPages)
                    attendeeAdapter.addLoadingFooter();
                else
                    isLastPage = true;
              //  setupEventAdapter(eventLists);
            }
        });
/*
        mAPIService.AttendeeFetchPost(api_token, eventid, "" + attendeePageSize, "" + currentPage, strAttendeeName).enqueue(new Callback<FetchAttendee>() {
            @Override
            public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {
//                Log.i(TAG, "onResponse: " + currentPage
//                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                attendeeAdapter.removeLoadingFooter();
                isLoading = false;

                List<Attendee> results = fetchResults(response);
                attendeeAdapter.addAll(results);
                insertIntoDb(results, currentPage);
                if (currentPage != totalPages)
                    attendeeAdapter.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            
            public void onFailure(Call<FetchAttendee> call, Throwable t) {
                t.printStackTrace();
                // attendeeAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
*/
    }

    @Override
    public void onContactSelected(Attendee attendee) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("fname", attendee.getFirst_name());
        intent.putExtra("lname", attendee.getLast_name());
        intent.putExtra("company", attendee.getCompany_name());
        intent.putExtra("city", attendee.getCity());
        intent.putExtra("designation", attendee.getDesignation());
        intent.putExtra("prof_pic", attendee.getProfile_picture());
        intent.putExtra("attendee_type", attendee.getAttendee_type());
        intent.putExtra("mobile", attendee.getMobile());
        intent.putExtra("email", attendee.getEmail());
        startActivity(intent);
       // getActivity().finish();
    }
}