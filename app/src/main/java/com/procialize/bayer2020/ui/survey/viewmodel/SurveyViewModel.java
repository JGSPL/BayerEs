package com.procialize.bayer2020.ui.survey.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.survey.networking.SurveyRepository;

public class SurveyViewModel extends ViewModel {

    private SurveyRepository surveyRepository = SurveyRepository.getInstance();
    MutableLiveData<FetchAgenda> eulaData = new MutableLiveData<>();

    public void getSurvey(String auth,
                          String event_id, String pageSize, String pageNumber) {
        surveyRepository = SurveyRepository.getInstance();
        eulaData = surveyRepository.getSurvey(auth,
                event_id, pageSize,
                pageNumber);
    }

    public LiveData<FetchAgenda> getSurvey() {
        return eulaData;
    }
}
