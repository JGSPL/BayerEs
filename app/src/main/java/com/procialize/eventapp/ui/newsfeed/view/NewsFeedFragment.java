package com.procialize.eventapp.ui.newsfeed.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import android.widget.ImageView;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.home.viewmodel.HomeViewModel;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;

public class NewsFeedFragment extends Fragment implements NewsFeedAdapter.FeedAdapterListner, View.OnClickListener {
    ArrayList<Newsfeed_detail> newsfeedArrayList = new ArrayList<>();
    // List<Newsfeed_detail> feedList = new ArrayList<>();
    NewsFeedAdapter newsfeedAdapter;
    RecyclerView recycler_feed;
    NewsFeedViewModel newsfeedViewModel;
    View root;
    SwipeRefreshLayout feedrefresh;
    LinearLayout ll_whats_on_mind;
    String eventid = "1";
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiJlNTcwY2JhMTMxODUwYjgzYjc4ZjE4M2FlZDI0MmM3MjI3YzQ1MTVhIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiIgaHR0cHM6XC9cL3N0YWdlLWFkbWluLnByb2NpYWxpemUubGl2ZVwvYmFzZWFwcFwvdXBsb2Fkc1wvdXNlclwvZGVmYXVsdC5wbmciLCJ0aW1lIjoxNTk4ODUxMTU3LCJleHBpcnlfdGltZSI6MTU5ODg1NDc1N30.QtQwWAr8TKKGAqLwTbJtosSxDhrDrhdyH_sH1A-0qes";
    ConnectionDetector connectionDetector;
    UploadMultimediaBackgroundReceiver mReceiver;
    IntentFilter mFilter;
    public static ConstraintLayout cl_main;
    private TextView tv_uploding_multimedia;
    String reaction_type;


    public static NewsFeedFragment newInstance() {

        return new NewsFeedFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsfeedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        Log.d("On news feed fragment","Yes");

        root = inflater.inflate(R.layout.fragment_home, container, false);
        cl_main = root.findViewById(R.id.cl_main);
        recycler_feed = root.findViewById(R.id.recycler_feed);
        feedrefresh = root.findViewById(R.id.feedrefresh);
        ll_whats_on_mind = root.findViewById(R.id.ll_whats_on_mind);
        ll_whats_on_mind.setOnClickListener(this);
        tv_uploding_multimedia = root.findViewById(R.id.tv_uploding_multimedia);
        connectionDetector = ConnectionDetector.getInstance(getActivity());

        feedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedrefresh.setRefreshing(false);
                if (connectionDetector.isConnectingToInternet()) {
                    init();
                }
            }
        });

        init();

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
            newsfeedViewModel.init("","");

            newsfeedViewModel.getNewsRepository().observe(this, new Observer<FetchNewsfeedMultiple>() {
                @Override
                public void onChanged(FetchNewsfeedMultiple fetchNewsfeedMultiple) {
                    List<Newsfeed_detail> feedList = fetchNewsfeedMultiple.getNewsfeed_detail();
                    if (newsfeedArrayList.size() > 0) {
                        newsfeedArrayList.clear();
                    }
                    newsfeedArrayList.addAll(feedList);
                    String mediaPath = fetchNewsfeedMultiple.getMedia_path();
                    SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NEWS_FEED_MEDIA_PATH, mediaPath);
                    editor.commit();
                    newsfeedAdapter.notifyDataSetChanged();
                }
            });
        }
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        if (newsfeedAdapter == null) {
            newsfeedAdapter = new NewsFeedAdapter(getContext(), newsfeedArrayList, NewsFeedFragment.this);
            recycler_feed.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onCommentClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openCommentPage(getActivity(), feed, position);
    }

    @Override
    public void onLikeClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openLikePage(getActivity(), feed, position);
    }

    @Override
    public void onSliderClick(Newsfeed_detail feed, int position) {

        newsfeedViewModel.openFeedDetails(getActivity(), feed, position);

    }

    @Override
    public void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position) {
        newsfeedViewModel.openMoreDetails(  getActivity(), feed, position);

    }

    @Override
    public void likeTvViewOnClick(View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext) {
        int count = Integer.parseInt(feed.getTotal_likes());

        Drawable drawables = likeimage.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawables).getBitmap();

        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_like)).getBitmap();


//        if(!drawables[2].equals(R.drawable.ic_like)){
        if (bitmap != bitmap2) {
            reaction_type = "";
            feed.setLike_flag("");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
//            likeimage.setBackgroundResource(R.drawable.ic_like);
            if (ConnectionDetector.getInstance(getContext()).isConnectingToInternet()) {
                newsfeedViewModel.openLikeimg(eventid,feed.getNews_feed_id());
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            try {

                if (count > 0) {
                    count = count - 1;
                    feed.setTotal_likes(String.valueOf(count));

                    if (count == 1) {
                        liketext.setText(count + " Like");
                    } else {
                        liketext.setText(count + " Likes");
                    }

                    feed.setTotal_likes(String.valueOf(count));

                } else {
                    liketext.setText("0" + " Likes");
                    feed.setTotal_likes("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            feed.setLike_flag("1");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_like));
           /*int color = Color.parseColor(colorActive);
            likeimage.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);*/
            reaction_type = "0";
            if (ConnectionDetector.getInstance(getContext()).isConnectingToInternet()) {
                newsfeedViewModel.openLikeimg(eventid,feed.getNews_feed_id());
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            try {

                count = count + 1;
                if (count == 1) {
                    liketext.setText(count + " Like");
                } else {
                    liketext.setText(count + " Likes");
                }

                feed.setTotal_likes(String.valueOf(count));

                feed.setTotal_likes(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
        }
    }

    public void uploadData() {
        Log.d("Service End upload","in upload");
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
                         /*       newsfeedViewModel.getIsUploading().observe(getActivity(), new Observer<Boolean>() {
                                    @Override
                                    public void onChanged(Boolean aBoolean) {
                                        if(!aBoolean)
                                        {*/
                                newsfeedViewModel.sendPost(eventid, postText, uploadMultimedia);
                                newsfeedViewModel.getPostStatus().observe(getActivity(), new Observer<LoginOrganizer>() {
                                    @Override
                                    public void onChanged(@Nullable LoginOrganizer result) {
                                        if (result != null) {

                                            newsfeedViewModel.updateisUplodedIntoDB(getActivity(), folderUniqueId);
                                            String status = result.getHeader().get(0).getType();
                                            String message = result.getHeader().get(0).getMsg();
                                            Snackbar.make(cl_main, message, Snackbar.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            Snackbar.make(cl_main, "failure", Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });
                                       /* }
                                    }
                                });*/
                            }
                        }
                    });
                }
            }
        });
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