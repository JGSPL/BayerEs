package com.procialize.bayer2020.ui.qa.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnARepository {
    private static QnARepository spotQnARepository;
    MutableLiveData<FetchSpotQnA> fetchSpotQnAList = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> submitSpotQnAData = new MutableLiveData<>();

    public static QnARepository getInstance() {
        if (spotQnARepository == null) {
            spotQnARepository = new QnARepository();
        }
        return spotQnARepository;
    }

    private APIService eventApi;

    public QnARepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchSpotQnA> getDirectQnAList(String token, String event_id,
                                                        String pageSize,
                                                        String pageNumber) {
        eventApi.directQnAFetch(token, event_id,
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


    public MutableLiveData<LoginOrganizer> submitDirectQnA(String token, String event_id,String question) {
        eventApi.PostQnADirect(token, event_id, question)
            .enqueue(new Callback<LoginOrganizer>() {
                @Override
                public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                    if (response.isSuccessful()) {
                        submitSpotQnAData.setValue(response.body());
                    } else {
                        submitSpotQnAData.setValue(null);
                        Log.e("Error","error");
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