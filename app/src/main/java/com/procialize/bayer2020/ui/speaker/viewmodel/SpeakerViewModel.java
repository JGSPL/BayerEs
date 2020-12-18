package com.procialize.bayer2020.ui.speaker.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.speaker.model.FetchSpeaker;
import com.procialize.bayer2020.ui.speaker.networking.SpeakerRepository;

public class SpeakerViewModel extends ViewModel {



    private SpeakerRepository speakerRepository = SpeakerRepository.getInstance();
    MutableLiveData<FetchSpeaker> fetchSpeakerData = new MutableLiveData<>();

    public void getSpeaker(String token, String organizer_id, String search_text) {
        speakerRepository = SpeakerRepository.getInstance();
        fetchSpeakerData = speakerRepository.getSpeakerList(token,organizer_id, search_text);
    }


    public LiveData<FetchSpeaker> getSpeakerList() {
        return fetchSpeakerData;
    }


}

