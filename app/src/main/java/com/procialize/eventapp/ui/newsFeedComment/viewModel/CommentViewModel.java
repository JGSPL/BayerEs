package com.procialize.eventapp.ui.newsFeedComment.viewModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.newsFeedComment.networking.GifRepository;
import com.procialize.eventapp.ui.newsFeedLike.view.LikeActivity;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.networking.PostNewsFeedRepository;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;

import java.io.Serializable;
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
    MutableLiveData<LoginOrganizer> commentHide = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> reportCommentPost = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> commentDelete = new MutableLiveData<>();
    MutableLiveData<LikePost> likePost = new MutableLiveData<>();
    MutableLiveData<FetchNewsfeedMultiple> newsFeedDetails = new MutableLiveData<>();


    //-----------------For Gif---------------------------------

    /**
     * Get Anon id for gif
     * @param key
     */
    public void GetId(String key) {
        gifRepository = GifRepository.getInstance();
        anon_id = gifRepository.getGifId(key);
    }

    public LiveData<String> getAnonId() {
        return anon_id;
    }
    /**
     * Get Gif images
     * @param key
     * @param anon_id
     */
    public void GetGif(String key, String anon_id) {
        gifRepository = GifRepository.getInstance();
        mutableLiveData = gifRepository.getResult(key, anon_id);
    }

    public LiveData<GifResponse> getGifList() {
        return mutableLiveData;
    }

    /**
     * Serach gif accroding to text
     * @param query
     * @param key
     * @param anon_id
     */
    public void searchGif(String query, String key, String anon_id) {
        gifRepository = GifRepository.getInstance();
        searchGifmutableLiveData = gifRepository.searchGif(query, key, anon_id);
    }

    public LiveData<GifResponse> searchGifList() {
        return searchGifmutableLiveData;
    }

    //-------------------------------------------------------


    /** --To Post comment
     *
     * @param event_id
     * @param news_feed_id
     * @param comment_data
     * @param comment_type
     */
    public void postComment(String token, String event_id, String news_feed_id, String comment_data, String comment_type) {//, String[] mediaFile, String[] mediaFileThumb) {
        if (!comment_data.isEmpty()) {
            commentRepository = CommentRepository.getInstance();
            commentLiveData = commentRepository.postComment(token,event_id, news_feed_id, comment_data, comment_type);
        }
    }

    public LiveData<LoginOrganizer> postCommentResponse() {
        isValid.setValue(false);
        return commentLiveData;
    }

    /**
     * Validation for comment
     * @param postStatus
     */
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

    /**
     * Top get Comment List
     * @param event_id
     * @param news_feed_id
     * @param pageSize
     * @param pageNumber
     */
    public void getComment(String token,String event_id, String news_feed_id, String pageSize, String pageNumber) {
        commentRepository = CommentRepository.getInstance();
        commentData = commentRepository.getCommentList(token,event_id, news_feed_id);//, pageSize,pageNumber);
    }

    public LiveData<Comment> getCommentList() {
        return commentData;
    }

    /**
     * To delete comment
     * @param event_id
     * @param news_feed_id
     * @param comment_id
     * @param position
     */
    public void deleteComment(String token,String event_id, String news_feed_id, String comment_id, int position) {
        commentRepository = CommentRepository.getInstance();
        commentDelete = commentRepository.deleteNewsFeedComment(token,event_id,
                news_feed_id,
                comment_id,
                position);

    }

    public LiveData<LoginOrganizer> commentDelete() {
        return commentDelete;
    }

    /**
     * To Hide Comment
     * @param event_id
     * @param comment_id
     */
    public void hideComment(String token, String event_id, String comment_id) {
        commentRepository = CommentRepository.getInstance();
        commentHide = commentRepository.hideComment(token,event_id,
                comment_id);

    }

    public LiveData<LoginOrganizer> commentHide() {
        return commentHide;
    }

    /**
     * To Report User
     * @param event_id
     * @param reported_user_id
     * @param news_feed_id
     * @param content
     */
    public void reportUser(String token,String event_id,String reported_user_id,String news_feed_id,String content) {
        commentRepository = CommentRepository.getInstance();
        commentHide = commentRepository.reportUser(token, event_id, reported_user_id, news_feed_id, content);

    }

    public LiveData<LoginOrganizer> reportUserData() {
        return commentHide;
    }

    /**
     *To Report Comment
     * @param event_id
     * @param reported_user_id
     * @param content
     */
    public void reportComment(String token,String event_id,String reported_user_id,String content) {
        commentRepository = CommentRepository.getInstance();
        reportCommentPost = commentRepository.reportComment( token,event_id, reported_user_id, content);

    }

    public LiveData<LoginOrganizer> reportCommentPostData() {
        return reportCommentPost;
    }

    /**
     * To like post
     * @param event_id
     * @param newsfeedid
     */
    public void likePost(  String token,String event_id,String newsfeedid) {
        commentRepository =CommentRepository.getInstance();
        likePost = commentRepository.PostLike(token,event_id, newsfeedid);
    }

    public LiveData<LikePost> likePostData() {
        return likePost;
    }

    /**
     * Fetch News feed deatils
     * @param eventId
     * @param newsFeedId
     */

    public void getNewsFeedDetails(String token,String eventId,String newsFeedId)
    {
        commentRepository =CommentRepository.getInstance();
        newsFeedDetails = commentRepository.getNewsFeedDetails(token,eventId, newsFeedId);
    }

    public LiveData<FetchNewsfeedMultiple> newsFeedDeatils() {
        return newsFeedDetails;
    }

    //---------------View Like details--------------------------------
    public void openLikePage(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, LikeActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }


}