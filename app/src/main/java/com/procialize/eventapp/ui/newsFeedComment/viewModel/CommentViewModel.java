package com.procialize.eventapp.ui.newsFeedComment.viewModel;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.newsFeedComment.networking.GifRepository;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.networking.PostNewsFeedRepository;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel extends ViewModel {
    Dialog dialog;
    private MutableLiveData<GifResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<GifResponse> searchGifmutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> anon_id = new MutableLiveData<String>();
    private GifRepository gifRepository;
    private CommentRepository commentRepository = CommentRepository.getInstance();
    MutableLiveData<LoginOrganizer> commentLiveData = new MutableLiveData<>();
    MutableLiveData<Comment> commentData = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    String event_id = "1";
    MutableLiveData<LoginOrganizer> commentHide = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> reportCommentPost = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> commentDelete = new MutableLiveData<>();

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

    public void postComment(String event_id, String news_feed_id, String comment_data, String comment_type) {//, String[] mediaFile, String[] mediaFileThumb) {
        if (!comment_data.isEmpty()) {
            commentRepository = CommentRepository.getInstance();
            commentLiveData = commentRepository.postComment(event_id, news_feed_id, comment_data, comment_type);
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

    public void getComment(String event_id, String news_feed_id, String pageSize, String pageNumber) {
        commentRepository = CommentRepository.getInstance();
        commentData = commentRepository.getCommentList(event_id, news_feed_id);//, pageSize,pageNumber);
    }

    public LiveData<Comment> getCommentList() {
        return commentData;
    }


    public void deleteComment(String event_id, String news_feed_id, String comment_id, int position) {
        commentRepository = CommentRepository.getInstance();
        commentDelete = commentRepository.deleteNewsFeedComment(event_id,
                news_feed_id,
                comment_id,
                position);

    }

    public LiveData<LoginOrganizer> commentDelete() {
        return commentDelete;
    }

    public void hideComment(String event_id, String comment_id) {
        commentRepository = CommentRepository.getInstance();
        commentHide = commentRepository.hideComment(event_id,
                comment_id);

    }

    public LiveData<LoginOrganizer> commentHide() {
        return commentHide;
    }

    public void reportUser(String event_id,String reported_user_id,String news_feed_id,String content) {
        commentRepository = CommentRepository.getInstance();
        commentHide = commentRepository.reportUser( event_id, reported_user_id, news_feed_id, content);

    }

    public LiveData<LoginOrganizer> reportUserData() {
        return commentHide;
    }

    public void reportComment(String event_id,String reported_user_id,String content) {
        commentRepository = CommentRepository.getInstance();
        reportCommentPost = commentRepository.reportComment( event_id, reported_user_id, content);

    }

    public LiveData<LoginOrganizer> reportCommentPostData() {
        return reportCommentPost;
    }
}