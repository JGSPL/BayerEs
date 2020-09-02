package com.procialize.eventapp.ui.newsFeedComment.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.networking.GifRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<GifResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<GifResponse> searchGifmutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> anon_id = new MutableLiveData<String>();
    private GifRepository gifRepository;

    public void GetId(String key) {
        gifRepository = GifRepository.getInstance();
        anon_id = gifRepository.getGifId(key);
    }

    public void GetGif(String key, String anon_id) {
        gifRepository = GifRepository.getInstance();
        mutableLiveData = gifRepository.getResult(key, anon_id);
    }

    public void searchGif(String query, String key, String anon_id) {
        gifRepository = GifRepository.getInstance();
        searchGifmutableLiveData = gifRepository.searchGif(query, key, anon_id);
    }

    public LiveData<GifResponse> getGifList() {
        return mutableLiveData;
    }

    public LiveData<GifResponse> searchGifList() {
        return searchGifmutableLiveData;
    }

    public LiveData<String> getAnonId() {
        return anon_id;
    }
}
