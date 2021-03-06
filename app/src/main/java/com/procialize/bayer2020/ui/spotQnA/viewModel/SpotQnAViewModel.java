package com.procialize.bayer2020.ui.spotQnA.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;
import com.procialize.bayer2020.ui.spotQnA.networking.SpotQnARepository;

public class SpotQnAViewModel extends ViewModel {

    private SpotQnARepository spotQnARepository = SpotQnARepository.getInstance();
    MutableLiveData<FetchSpotQnA> fetchQnAData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> submitQnAData = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    public void getSpotQnA(String token, String event_id, String session_id, String pageSize, String pageNumber) {
        spotQnARepository = SpotQnARepository.getInstance();
        fetchQnAData = spotQnARepository.getSpotQnAList(token, event_id, session_id,
                pageSize,
                pageNumber);
    }

    public LiveData<FetchSpotQnA> getSpotQnAList() {
        return fetchQnAData;
    }

    public void submitSpotQnA(String token, String event_id, String session_id, String question) {
        spotQnARepository = SpotQnARepository.getInstance();
        submitQnAData = spotQnARepository.submitSpotQnA(token, event_id, session_id,
                question);
    }

    public LiveData<LoginOrganizer> submitSpotQnAList() {
        isValid.setValue(false);
        return submitQnAData;
    }
}
