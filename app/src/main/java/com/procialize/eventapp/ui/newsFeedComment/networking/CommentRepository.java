package com.procialize.eventapp.ui.newsFeedComment.networking;


import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    MutableLiveData<LoginOrganizer> commentData = new MutableLiveData<>();
    private static CommentRepository commentRepository;
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

    public MutableLiveData<LoginOrganizer> postComment(String event_id, String news_feed_id, String comment_data,String commentType) {
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

}
