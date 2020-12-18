package com.procialize.bayer2020.ui.newsFeedLike.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.newsFeedLike.model.Like;
import com.procialize.bayer2020.ui.newsFeedLike.networking.LikeRepository;

public class LikeViewModel extends ViewModel {
    private LikeRepository likeRepository = LikeRepository.getInstance();
    MutableLiveData<Like> likeData = new MutableLiveData<>();

    public void getLike(String token, String event_id, String news_feed_id, String pageSize, String pageNumber) {
        likeRepository = LikeRepository.getInstance();
        likeData = likeRepository.getLikeList(token,event_id, news_feed_id, pageSize, pageNumber);
    }

    public LiveData<Like> getLikeList() {
        return likeData;
    }
}
