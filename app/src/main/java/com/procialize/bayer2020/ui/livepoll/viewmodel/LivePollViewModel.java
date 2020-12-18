package com.procialize.bayer2020.ui.livepoll.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.livepoll.LivePollRepository;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;

public class LivePollViewModel extends ViewModel {  
    MutableLiveData<FetchLivePoll> livepollDetail = new MutableLiveData<>();
    private LivePollRepository livePollRepository = LivePollRepository.getInstance();

    public void getLivepoll(String token, String event_id) {
        livePollRepository = LivePollRepository.getInstance();
        livepollDetail = livePollRepository.getLivepoll(token,event_id);
    }


    public LiveData<FetchLivePoll> getLivePollList() {
        return livepollDetail;
    }
}
