package com.procialize.bayer2020.ui.speaker.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.speaker.model.FetchSpeaker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerRepository {
    private static SpeakerRepository speakerRepository;
    MutableLiveData<FetchSpeaker> fetchSpeakerList = new MutableLiveData<>();

    public static SpeakerRepository getInstance() {
        if (speakerRepository == null) {
            speakerRepository = new SpeakerRepository();
        }
        return speakerRepository;
    }

    private APIService eventApi;

    public SpeakerRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchSpeaker> getSpeakerList(String token ,String organizer_id, String search_text) {
        eventApi.SpeakerList(token,organizer_id,
                search_text)
                .enqueue(new Callback<FetchSpeaker>() {
                    @Override
                    public void onResponse(Call<FetchSpeaker> call, Response<FetchSpeaker> response) {
                        if (response.isSuccessful()) {
                            fetchSpeakerList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                        fetchSpeakerList.setValue(null);
                    }
                });

        return fetchSpeakerList;
    }
}
