package com.procialize.eventapp.ui.newsfeed.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeedMedia;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;


public class NewsFeedFragment extends Fragment implements NewsFeedAdapter.FeedAdapterListner, View.OnClickListener {
    ArrayList<Newsfeed_detail> newsfeedArrayList = new ArrayList<>();
    NewsFeedAdapter newsfeedAdapter;
    RecyclerView recycler_feed;
    NewsFeedViewModel newsfeedViewModel;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;
    View root;
    SwipeRefreshLayout feedrefresh;
    LinearLayout ll_whats_on_mind;
    String eventid;
    ConnectionDetector connectionDetector;
    UploadMultimediaBackgroundReceiver mReceiver;
    IntentFilter mFilter;
    public static ConstraintLayout cl_main;
    private TextView tv_uploding_multimedia,tv_whats_on_mind;
    String api_token;
    ImageView iv_profile;
    int totalPages = 0;
    int newsFeedPageNumber = 1;
    int newsFeedPageSize = 5;

    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    ConnectionDetector cd;
    LinearLayoutManager linearLayoutManager;

    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Call Refresh token
        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());

        newsfeedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);
        newsFeedDatabaseViewModel = ViewModelProviders.of(this).get(NewsFeedDatabaseViewModel.class);

        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

        Log.d("On news feed fragment", "Yes");

        root = inflater.inflate(R.layout.fragment_home, container, false);
        cl_main = root.findViewById(R.id.cl_main);
        recycler_feed = root.findViewById(R.id.recycler_feed);
        iv_profile = root.findViewById(R.id.iv_profile);
        feedrefresh = root.findViewById(R.id.feedrefresh);
        ll_whats_on_mind = root.findViewById(R.id.ll_whats_on_mind);
        ll_whats_on_mind.setOnClickListener(this);
        tv_whats_on_mind = root.findViewById(R.id.tv_whats_on_mind);
        tv_uploding_multimedia = root.findViewById(R.id.tv_uploding_multimedia);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_feed.setLayoutManager(mLayoutManager);
        newsfeedAdapter = new NewsFeedAdapter(getActivity()/*, FragmentNewsFeed.this*/,NewsFeedFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_feed.setLayoutManager(linearLayoutManager);
        recycler_feed.setItemAnimator(new DefaultItemAnimator());
        recycler_feed.setAdapter(newsfeedAdapter);
        connectionDetector = ConnectionDetector.getInstance(getActivity());


            String profilePic = SharedPreference.getPref(getActivity(), SharedPreferencesConstant.KEY_PROFILE_PIC);
            Glide.with(getActivity())
                    .load(profilePic)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(iv_profile);

        feedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedrefresh.setRefreshing(false);
                if (connectionDetector.isConnectingToInternet()) {
                    newsfeedAdapter.getNewsFeedList().clear();
                    newsfeedAdapter.notifyDataSetChanged();
                    init();
                }
            }
        });
        if (connectionDetector.isConnectingToInternet()) {

            newsfeedAdapter.getNewsFeedList().clear();
            newsfeedAdapter.notifyDataSetChanged();
            init();
            feedrefresh.setRefreshing(false);

        }

        tv_whats_on_mind.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(),EVENT_COLOR_4)));
        tv_whats_on_mind.setAlpha(0.4f);

        if (!CommonFunction.isMyServiceRunning(getActivity(), BackgroundServiceToCompressMedia.class)) {
           /* Intent intent = new Intent(getActivity(), BackgroundServiceToCompressMedia.class);
            getActivity().startService(intent);*/
            /*newsfeedViewModel.getNonCompressesMultimedia(getActivity());
            newsfeedViewModel.getNonCompressedMedia().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                @Override
                public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                    Log.d("count_of_non_compressed", uploadMultimedia.size() + "");
                    if (uploadMultimedia.size() > 0) {
                        newsfeedViewModel.startBackgroundService(getActivity(), uploadMultimedia);
                        newsfeedViewModel.getIsUpdating().observe(getActivity(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(@Nullable Boolean aBoolean) {
                                if (aBoolean) {
                                    showProgressBar();
                                } else {
                                    hideProgressBar();
                                }
                            }
                        });
                    } *//*else {
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(broadcastIntent);
                    }*//*
                }
            });*/
        }
        newsfeedViewModel.startBackgroundService(getActivity());
        newsfeedViewModel.getIsUpdating().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
        mReceiver = new UploadMultimediaBackgroundReceiver();
        mFilter = new IntentFilter(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);

        return root;
    }

    void init() {
        if (connectionDetector.isConnectingToInternet()) {

            newsfeedViewModel.init(api_token, eventid, String.valueOf(newsFeedPageSize), String.valueOf(currentPage));

            newsfeedViewModel.getNewsRepository().observe(this, new Observer<FetchNewsfeedMultiple>() {
                @Override
                public void onChanged(FetchNewsfeedMultiple fetchNewsfeedMultiple) {
                    if (fetchNewsfeedMultiple != null) {
                        List<Newsfeed_detail> feedList = fetchNewsfeedMultiple.getNewsfeed_detail();
                        newsfeedAdapter.addAll(feedList);

                        newsFeedDatabaseViewModel.deleteNewsFeedMediaDataList(getActivity());
                        insertIntoDb(feedList);
                        String mediaPath = fetchNewsfeedMultiple.getMedia_path();
                        totalPages = Integer.parseInt(fetchNewsfeedMultiple.getTotalRecords());

                        HashMap<String, String> map = new HashMap<>();
                        map.put(NEWS_FEED_MEDIA_PATH, mediaPath);
                        SharedPreference.putPref(getActivity(), map);


                        try {
                            if (currentPage <= totalPages) newsfeedAdapter.addLoadingFooter();
                            else isLastPage = true;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (newsfeedViewModel != null && newsfeedViewModel.getNewsRepository().hasObservers()) {
                            newsfeedViewModel.getNewsRepository().removeObservers(NewsFeedFragment.this);
                        }
                    }

                }
            });

        } else {
            getDataFromDb();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                setupRecyclerView();
            }
        }, 100);


        recycler_feed.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

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
    }

    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);


        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        if (newsfeedViewModel != null && newsfeedViewModel.getNewsRepository().hasObservers()) {
            newsfeedViewModel.getNewsRepository().removeObservers(NewsFeedFragment.this);
        }
        newsfeedViewModel.init(api_token, eventid, String.valueOf(newsFeedPageSize), String.valueOf(currentPage));

        newsfeedViewModel.getNewsRepository().observe(this, new Observer<FetchNewsfeedMultiple>() {
            @Override
            public void onChanged(FetchNewsfeedMultiple fetchNewsfeedMultiple) {

                newsfeedAdapter.removeLoadingFooter();
                isLoading = false;

                List<Newsfeed_detail> feedList = fetchNewsfeedMultiple.getNewsfeed_detail();
                if (feedList.size() > 0) {
                    newsfeedAdapter.addAll(feedList);

                }

                if (newsfeedViewModel != null && newsfeedViewModel.getNewsRepository().hasObservers()) {
                    newsfeedViewModel.getNewsRepository().removeObservers(NewsFeedFragment.this);
                }

                if (currentPage != totalPages) {
                    newsfeedAdapter.addLoadingFooter();
                   // newsfeedAdapter.notifyDataSetChanged();
                } else
                    isLastPage = true;


            }
        });
    }


    public void insertIntoDb(List<Newsfeed_detail> feedList) {
        newsFeedDatabaseViewModel.insertIntoDb(getActivity(), feedList);
    }

    public void getDataFromDb() {
        newsFeedDatabaseViewModel.getNewsFeed(getActivity());
        newsFeedDatabaseViewModel.getNewsFeedList().observe(getActivity(), new Observer<List<TableNewsFeed>>() {
            @Override
            public void onChanged(List<TableNewsFeed> tableNewsFeeds) {
                if (tableNewsFeeds != null) {
                    if (newsfeedArrayList.size() > 0) {
                        newsfeedArrayList.clear();
                    }
                    for (int i = 0; i < tableNewsFeeds.size(); i++) {
                        Newsfeed_detail newsfeed_detail = new Newsfeed_detail();
                        newsfeed_detail.setNews_feed_id(tableNewsFeeds.get(i).getNews_feed_id());
                        newsfeed_detail.setType(tableNewsFeeds.get(i).getType());
                        newsfeed_detail.setPost_status(tableNewsFeeds.get(i).getPost_status());
                        newsfeed_detail.setEvent_id(tableNewsFeeds.get(i).getEvent_id());
                        newsfeed_detail.setPost_date(tableNewsFeeds.get(i).getPost_date());
                        newsfeed_detail.setFirst_name(tableNewsFeeds.get(i).getFirst_name());
                        newsfeed_detail.setLast_name(tableNewsFeeds.get(i).getLast_name());
                        newsfeed_detail.setCompany_name(tableNewsFeeds.get(i).getCompany_name());
                        newsfeed_detail.setDesignation(tableNewsFeeds.get(i).getDesignation());
                        newsfeed_detail.setCity_id(tableNewsFeeds.get(i).getCity_id());
                        newsfeed_detail.setProfile_pic(tableNewsFeeds.get(i).getProfile_pic());
                        newsfeed_detail.setAttendee_id(tableNewsFeeds.get(i).getAttendee_id());
                        newsfeed_detail.setAttendee_type(tableNewsFeeds.get(i).getAttendee_type());
                        newsfeed_detail.setLike_flag(tableNewsFeeds.get(i).getLike_flag());
                        newsfeed_detail.setLike_type(tableNewsFeeds.get(i).getLike_type());
                        newsfeed_detail.setTotal_likes(tableNewsFeeds.get(i).getTotal_likes());
                        newsfeed_detail.setTotal_comments(tableNewsFeeds.get(i).getTotal_comments());


                        newsFeedDatabaseViewModel.getNewsFeedMedia(getActivity(), tableNewsFeeds.get(i).getNews_feed_id());
                        newsFeedDatabaseViewModel.getNewsFeedMediaDataList(getActivity(), tableNewsFeeds.get(i).getNews_feed_id()).observe(getActivity(), new Observer<List<TableNewsFeedMedia>>() {
                            @Override
                            public void onChanged(List<TableNewsFeedMedia> tableNewsFeedMedia) {
                                Log.d("tableNewsFeedMedia_", "tableNewsFeedMedia");
                                if (tableNewsFeedMedia != null) {
                                    Log.d("Media_Count", tableNewsFeedMedia.size() + "");
                                    List<News_feed_media> newsFeedMediaList = new ArrayList<>();

                                    for (int j = 0; j < tableNewsFeedMedia.size(); j++) {
                                        Log.d("Media_Id", "Position_" + j + "");
                                        Log.d("Media_file", tableNewsFeedMedia.get(j).getMedia_file());
                                        News_feed_media news_feed_media = new News_feed_media();
                                        news_feed_media.setMedia_id(tableNewsFeedMedia.get(j).getMedia_id());
                                        news_feed_media.setNews_feed_id(tableNewsFeedMedia.get(j).getNews_feed_id());
                                        news_feed_media.setMedia_type(tableNewsFeedMedia.get(j).getMedia_type());
                                        news_feed_media.setMedia_file(tableNewsFeedMedia.get(j).getMedia_file());
                                        news_feed_media.setThumb_image(tableNewsFeedMedia.get(j).getThumb_image());
                                        news_feed_media.setWidth(tableNewsFeedMedia.get(j).getWidth());
                                        news_feed_media.setHeight(tableNewsFeedMedia.get(j).getHeight());
                                        newsFeedMediaList.add(news_feed_media);
                                    }
                                    newsfeed_detail.setNews_feed_media(newsFeedMediaList);
                                }
                            }
                        });
                        newsfeedArrayList.add(i, newsfeed_detail);
                    }
                    if (newsFeedDatabaseViewModel != null && newsFeedDatabaseViewModel.getNewsFeedList().hasObservers()) {
                        newsFeedDatabaseViewModel.getNewsFeedList().removeObservers(getActivity());
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setupRecyclerView();
                        }
                    }, 100);

                }
            }
        });
    }

    private void setupRecyclerView() {
        if (newsfeedAdapter == null) {
           /* newsfeedAdapter = new NewsFeedAdapter(getContext(), newsfeedArrayList, NewsFeedFragment.this);
            recycler_feed.setLayoutManager(new LinearLayoutManager(getContext()));*/
            recycler_feed.setAdapter(newsfeedAdapter);
            recycler_feed.setItemAnimator(new DefaultItemAnimator());
            recycler_feed.setNestedScrollingEnabled(true);
        } else {
            newsfeedAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onContactSelected(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openNewsFeedDetails(getActivity(), feed, position);
    }

    @Override
    public void onCommentClick(Newsfeed_detail feed, int position,int swipeablePosition) {
        newsfeedViewModel.openCommentPage(getActivity(), feed, position,swipeablePosition);
    }

    @Override
    public void onLikeClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openLikePage(getActivity(), feed, position);
    }

    @Override
    public void onShareClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openShareTask(getActivity(), feed, position);

    }

    @Override
    public void onSliderClick(Newsfeed_detail feed, int position) {

        newsfeedViewModel.openFeedDetails(getActivity(), feed, position);

    }

    @Override
    public void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position) {
        newsfeedViewModel.openMoreDetails(getActivity(), feed, position, api_token, eventid, newsfeedAdapter);
    }

    @Override
    public void likeTvViewOnClick(View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext) {
        newsfeedViewModel.openLikeimg(getActivity(), api_token, eventid, feed.getNews_feed_id(), v, feed, position, likeimage, liketext);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_whats_on_mind:
                startActivity(new Intent(getActivity(), PostNewActivity.class));
                break;
        }
    }

    private class UploadMultimediaBackgroundReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            Log.d("service end", "service end");
            try {
                newsfeedViewModel.stopBackgroundService(getActivity());
                uploadData();
                /* newsfeedViewModel.getMediaToUpload(getActivity());
                newsfeedViewModel.getMedia().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                @Override
                public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                Log.d("count_of_is_compressed1", uploadMultimedia.size() + "");
                uploadData();
                }
                });*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadData() {
        Log.d("Service End upload", "in upload");
        if (newsfeedViewModel != null) {
            newsfeedViewModel.getFolderUniqueId(getActivity());
            newsfeedViewModel.getFolderIdList().observe(getActivity(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> folderUniqueIdList) {
                    for (int i = 0; i < folderUniqueIdList.size(); i++) {
                        final String folderUniqueId = folderUniqueIdList.get(i);
                        newsfeedViewModel.getNewsFeedDataAccrodingToFolderUniqueId(getActivity(), folderUniqueId);
                        newsfeedViewModel.getNewsFeedToUpload().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                            @Override
                            public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                                if (uploadMultimedia.size() > 0) {
                                    String postText = uploadMultimedia.get(0).getPost_status();
                                    if (!postText.isEmpty()) {
                                        uploadMultimedia.remove(0);
                                    }

                                    newsfeedViewModel.sendPost(api_token, eventid, postText, uploadMultimedia);
                                    newsfeedViewModel.getPostStatus().observe(getActivity(), new Observer<LoginOrganizer>() {
                                        @Override
                                        public void onChanged(@Nullable LoginOrganizer result) {
                                            if (result != null) {

                                                newsfeedViewModel.updateisUplodedIntoDB(getActivity(), folderUniqueId);
                                                String status = result.getHeader().get(0).getType();
                                                String message = result.getHeader().get(0).getMsg();
                                                Utility.createLongSnackBar(cl_main, message);
                                            } else {
                                                Utility.createLongSnackBar(cl_main, "failure");
                                            }

                                            if (newsfeedViewModel != null && newsfeedViewModel.getPostStatus().hasObservers()) {
                                                newsfeedViewModel.getPostStatus().removeObservers(getActivity());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private void showProgressBar() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_uploding_multimedia.startAnimation(anim);
        tv_uploding_multimedia.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        tv_uploding_multimedia.clearAnimation();
        tv_uploding_multimedia.setVisibility(View.GONE);
    }
}