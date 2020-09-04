package com.procialize.eventapp.ui.newsFeedLike.networking;


import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeRepository {

    MutableLiveData<LoginOrganizer> commentData = new MutableLiveData<>();
    MutableLiveData<Comment> commentList = new MutableLiveData<>();
    private static LikeRepository likeRepository;

    public static LikeRepository getInstance() {
        if (likeRepository == null) {
            likeRepository = new LikeRepository();
        }
        return likeRepository;
    }

    private APIService commentApi;

    public LikeRepository() {
        commentApi = ApiUtils.getAPIService();
    }


    /*public MutableLiveData<Comment> getLikeList(String event_id, String news_feed_id){//, String pageSize, String pageNumber) {
        commentApi.getLikes(event_id,
                news_feed_id)
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
    }*/

}
