package com.procialize.eventapp.ui.newsFeedComment.networking;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    MutableLiveData<LoginOrganizer> commentData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> deleteCommentData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> hideCommentData = new MutableLiveData<>();
    MutableLiveData<Comment> commentList = new MutableLiveData<>();
    private static CommentRepository commentRepository;
    MutableLiveData<LoginOrganizer> reportUser = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> reportComment = new MutableLiveData<>();
    MutableLiveData<LikePost> likePost = new MutableLiveData<>();
    MutableLiveData<FetchNewsfeedMultiple> newsfeedData = new MutableLiveData<>();
    public static CommentRepository getInstance() {
        if (commentRepository == null) {
            commentRepository = new CommentRepository();
        }
        return commentRepository;
    }

    private APIService commentApi;

    public CommentRepository() {
        commentApi = ApiUtils.getAPIService();
    }

    /**
     *
     * @param event_id
     * @param news_feed_id
     * @param comment_data
     * @param commentType
     * @return
     */
    public MutableLiveData<LoginOrganizer> postComment(String token,String event_id, String news_feed_id, String comment_data, String commentType) {
        commentApi.PostComment(token,event_id,
                news_feed_id,
                comment_data,
                commentType)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            commentData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        commentData.setValue(null);
                    }
                });

        return commentData;
    }

    /**
     *
     * @param event_id
     * @param news_feed_id
     * @param comment_id
     * @param position
     * @return
     */
    public MutableLiveData<LoginOrganizer> deleteNewsFeedComment(String token,String event_id, String news_feed_id, String comment_id, int position) {
        commentApi = ApiUtils.getAPIService();
        commentApi.DeleteComment(token,event_id, comment_id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    deleteCommentData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                deleteCommentData.setValue(null);
            }
        });

        return deleteCommentData;
    }

    /**
     *
     * @param event_id
     * @param comment_id
     * @return
     */
    public MutableLiveData<LoginOrganizer> hideComment(String token, String event_id, String comment_id) {
        commentApi = ApiUtils.getAPIService();
        commentApi.CommentHide(token,event_id, comment_id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    hideCommentData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                hideCommentData.setValue(null);
            }
        });

        return hideCommentData;
    }

    /**
     *
     * @param event_id
     * @param news_feed_id
     * @return
     */
    public MutableLiveData<Comment> getCommentList(String token,String event_id, String news_feed_id) {//, String pageSize, String pageNumber) {
        commentApi.getComment(token,event_id,
                news_feed_id/*,
                pageSize,
                pageNumber*/)
                .enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            commentList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        commentList.setValue(null);
                    }
                });

        return commentList;
    }

    /**
     *
     * @param event_id
     * @param reported_user_id
     * @param news_feed_id
     * @param content
     * @return
     */
    public MutableLiveData<LoginOrganizer> reportUser(String token,String event_id,String reported_user_id,String news_feed_id,String content) {
        commentApi = ApiUtils.getAPIService();

        commentApi.ReportUser(token,event_id, reported_user_id, news_feed_id, content).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportUser.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportUser.setValue(null);

            }
        });
        return reportUser;
    }

    /**
     *
     * @param event_id
     * @param comment_id
     * @param content
     * @return
     */
    public MutableLiveData<LoginOrganizer> reportComment(String token,String event_id,String comment_id,String content) {
        commentApi = ApiUtils.getAPIService();

        commentApi.ReportComment(token,event_id, comment_id,  content).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportComment.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportComment.setValue(null);

            }
        });
        return reportComment;
    }

    /**
     *
     * @param event_id
     * @param news_feed_id
     * @return
     */
    public MutableLiveData<LikePost> PostLike(String token,String event_id, String news_feed_id) {
        commentApi = ApiUtils.getAPIService();

        commentApi.PostLikeFromComment(token,event_id, news_feed_id).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {
                if (response.isSuccessful()) {
                    likePost.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                likePost.setValue(null);

            }
        });
        return likePost;
    }

    /**
     *
     * @param eventId
     * @param newsFeedId
     * @return
     */
    public MutableLiveData<FetchNewsfeedMultiple> getNewsFeedDetails(String token,String eventId, String newsFeedId) {
        commentApi = ApiUtils.getAPIService();

        commentApi.NewsFeedDetailFetch(token,eventId,newsFeedId).enqueue(new Callback<FetchNewsfeedMultiple>() {
            @Override
            public void onResponse(Call<FetchNewsfeedMultiple> call,
                                   Response<FetchNewsfeedMultiple> response) {
                if (response.isSuccessful()) {
                    newsfeedData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FetchNewsfeedMultiple> call, Throwable t) {
                newsfeedData.setValue(null);

            }
        });
        return newsfeedData;
    }



}
