package com.procialize.bayer2020.ui.survey.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyRepository {

    MutableLiveData<FetchAgenda> eulaList = new MutableLiveData<>();
    private static SurveyRepository PrivacyPolicyRepository;

    public static SurveyRepository getInstance() {
        if (PrivacyPolicyRepository == null) {
            PrivacyPolicyRepository = new SurveyRepository();
        }
        return PrivacyPolicyRepository;
    }

    private APIService privacyPolicyApi;

    public SurveyRepository() {
        privacyPolicyApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchAgenda> getSurvey(String auth,
                                                  String event_id, String pageSize, String pageNumber) {
        privacyPolicyApi.getSurvey(auth,
                event_id, pageSize, pageNumber)
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            try {
                                eulaList.postValue(response.body());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        eulaList.postValue(null);
                    }
                });

        return eulaList;
    }
}
