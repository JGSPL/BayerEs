package com.procialize.eventapp.ui.AgendaDetails.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.liveStreaming.view.LiveStreamingActivity;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.io.Serializable;

public class AgendaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AgendaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Agenda fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void openAgendaDetails(Activity activity) {
        activity.startActivity(new Intent(activity, LiveStreamingActivity.class));
    }

}
