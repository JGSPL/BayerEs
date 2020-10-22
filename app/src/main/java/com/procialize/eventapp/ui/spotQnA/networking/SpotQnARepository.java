package com.procialize.eventapp.ui.spotQnA.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.spotQnA.model.FetchSpotQnA;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotQnARepository {
    private static SpotQnARepository spotQnARepository;
    MutableLiveData<FetchSpotQnA> fetchSpotQnAList = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> submitSpotQnAData = new MutableLiveData<>();

    public static SpotQnARepository getInstance() {
        if (spotQnARepository == null) {
            spotQnARepository = new SpotQnARepository();
        }
        return spotQnARepository;
    }

    private APIService eventApi;

    public SpotQnARepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchSpotQnA> getSpotQnAList(String token, String event_id, String session_id,
                                                        String pageSize,
                                                        String pageNumber) {
        eventApi.spotQnAFetch(token, event_id, session_id,
                pageSize,
                pageNumber)
                .enqueue(new Callback<FetchSpotQnA>() {
                    @Override
                    public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                        if (response.isSuccessful()) {
                            fetchSpotQnAList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                        fetchSpotQnAList.setValue(null);
                    }
                });

        return fetchSpotQnAList;
    }


    public MutableLiveData<LoginOrganizer> submitSpotQnA(String token, String event_id, String session_id,
                                                        String question) {
        eventApi.PostQnASession(token, event_id, session_id,
                question)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            submitSpotQnAData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        submitSpotQnAData.setValue(null);
                    }
                });

        return submitSpotQnAData;
    }
}