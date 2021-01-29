package com.procialize.bayer2020.ui.qa.viewmodel;

import android.app.Activity;
import android.app.Dialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.qa.networking.QnARepository;
import com.procialize.bayer2020.ui.qa.view.QnADirectActivity;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;
import com.procialize.bayer2020.ui.spotQnA.networking.SpotQnARepository;

public class QADirectViewModel extends ViewModel {

    MutableLiveData<FetchSpotQnA> fetchQnAData = new MutableLiveData<>();
    private QnARepository qnARepository = QnARepository.getInstance();
    MutableLiveData<LoginOrganizer> submitQnAData = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();


    public void submitDirectQnA(String token, String event_id, String question) {
        qnARepository = QnARepository.getInstance();
        submitQnAData = qnARepository.submitDirectQnA(token, event_id,
                question);
    }

    public LiveData<LoginOrganizer> submitDirectQnAList() {
        isValid.setValue(false);
        return submitQnAData;
    }

    public void getDirectQnA(String token, String event_id, String pageSize, String pageNumber) {
        qnARepository = QnARepository.getInstance();
        fetchQnAData = qnARepository.getDirectQnAList(token, event_id,
                pageSize,
                pageNumber);
    }

    public LiveData<FetchSpotQnA> getDirectQnAList() {
        return fetchQnAData;
    }
}
