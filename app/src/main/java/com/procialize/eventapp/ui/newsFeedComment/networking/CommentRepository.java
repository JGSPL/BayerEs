package com.procialize.eventapp.ui.newsFeedComment.networking;


import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

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

    public MutableLiveData<LoginOrganizer> postComment(String event_id, String news_feed_id, String comment_data, String commentType) {
        commentApi.PostComment(event_id,
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

    public MutableLiveData<LoginOrganizer> deleteNewsFeedComment(String event_id, String news_feed_id, String comment_id, int position) {
        commentApi = ApiUtils.getAPIService();
        commentApi.DeleteComment(event_id, comment_id).enqueue(new Callback<LoginOrganizer>() {
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

    public MutableLiveData<LoginOrganizer> hideComment(String event_id, String comment_id) {
        commentApi = ApiUtils.getAPIService();
        commentApi.CommentHide(event_id, comment_id).enqueue(new Callback<LoginOrganizer>() {
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

    public MutableLiveData<Comment> getCommentList(String event_id, String news_feed_id) {//, String pageSize, String pageNumber) {
        commentApi.getComment(event_id,
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

    public MutableLiveData<LoginOrganizer> reportUser(String event_id,String reported_user_id,String news_feed_id,String content) {
        commentApi = ApiUtils.getAPIService();

        commentApi.ReportUser(event_id, reported_user_id, news_feed_id, content).enqueue(new Callback<LoginOrganizer>() {
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

    public MutableLiveData<LoginOrganizer> reportComment(String event_id,String comment_id,String content) {
        commentApi = ApiUtils.getAPIService();

        commentApi.ReportComment(event_id, comment_id,  content).enqueue(new Callback<LoginOrganizer>() {
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

    public MutableLiveData<LikePost> PostLike(String event_id, String news_feed_id) {
        commentApi = ApiUtils.getAPIService();

        commentApi.PostLikeFromComment(event_id, news_feed_id).enqueue(new Callback<LikePost>() {
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

}
