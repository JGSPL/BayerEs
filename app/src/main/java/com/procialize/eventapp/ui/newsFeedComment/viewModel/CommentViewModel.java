package com.procialize.eventapp.ui.newsFeedComment.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.newsFeedComment.networking.GifRepository;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.networking.PostNewsFeedRepository;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<GifResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> mediaImage = new MutableLiveData<ArrayList<String>>();
    private MutableLiveData<ArrayList<String>> mediaImageThumb = new MutableLiveData<>();
    private MutableLiveData<GifResponse> searchGifmutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> anon_id = new MutableLiveData<String>();
    private GifRepository gifRepository;
    private CommentRepository commentRepository;
    MutableLiveData<LoginOrganizer> commentLiveData = new MutableLiveData<>();
    ArrayList<String> imagesSelectednew = new ArrayList<>();
    ArrayList<String> imagesSelectednew1 = new ArrayList<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
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

    public void getNewsFeedMedia(List<News_feed_media> newsFeedMedia) {

        for (int i = 0; i < newsFeedMedia.size(); i++) {
            imagesSelectednew.add(newsFeedMedia.get(i).getMedia_file());
            imagesSelectednew1.add(newsFeedMedia.get(i).getThumb_image());
        }
        mediaImage.setValue(imagesSelectednew);
        mediaImageThumb.setValue(imagesSelectednew1);
    }

    public LiveData<ArrayList<String>> getNewsFeedMediaData() {

        return mediaImage;
    }
    public LiveData<ArrayList<String>>  getNewsFeedMediaThumb() {

        return mediaImageThumb;
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


    public void postComment(String event_id, String news_feed_id, String comment_data,String comment_type) {//, String[] mediaFile, String[] mediaFileThumb) {
        if (!comment_data.isEmpty()) {
            commentRepository = CommentRepository.getInstance();
            commentLiveData = commentRepository.postComment(event_id,news_feed_id, comment_data,comment_type);
        }
    }

    public LiveData<LoginOrganizer> postCommentResponse() {
        isValid.setValue(false);
        return commentLiveData;
    }

    public void validation(String postStatus) {
        if (postStatus.isEmpty()) {
            isValid.setValue(false);
        } else {
            isValid.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getIsValid() {
        return isValid;
    }
}
