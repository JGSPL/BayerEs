package com.procialize.bayer2020.ui.newsFeedLike.networking;


import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.ui.newsFeedLike.model.Like;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeRepository {

    MutableLiveData<LoginOrganizer> commentData = new MutableLiveData<>();
    MutableLiveData<Like> likeList = new MutableLiveData<>();
    private static LikeRepository likeRepository;

    public static LikeRepository getInstance() {
        if (likeRepository == null) {
            likeRepository = new LikeRepository();
        }
        return likeRepository;
    }

    private APIService likeApi;

    public LikeRepository() {
        likeApi = ApiUtils.getAPIService();
    }


    public MutableLiveData<Like> getLikeList(String token,String event_id, String news_feed_id, String pageSize, String pageNumber) {
        likeApi.getLikes(token,event_id,
                news_feed_id,pageSize,
                pageNumber)
                .enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                        if (response.isSuccessful()) {
                            likeList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        likeList.setValue(null);
                    }
                });

        return likeList;
    }

}
