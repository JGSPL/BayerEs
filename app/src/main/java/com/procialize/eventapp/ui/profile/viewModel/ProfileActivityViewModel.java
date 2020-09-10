package com.procialize.eventapp.ui.profile.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.profile.networking.ProfileRepository;

public class ProfileActivityViewMdel extends ViewModel {

    ProfileRepository profileRepository;

    /**
     * Top get Profile details
     * @param event_id
     */
    public void getProfile(String event_id) {
        profileRepository = ProfileRepository.getInstance();
        commentData = profileRepository.getClass(event_id);//, pageSize,pageNumber);
    }

    public LiveData<Comment> getCommentList() {
        return commentData;
    }
}
