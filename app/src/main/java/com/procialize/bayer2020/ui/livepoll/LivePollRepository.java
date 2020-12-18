package com.procialize.bayer2020.ui.livepoll;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivePollRepository {
    private static LivePollRepository LivePollRepository;
    MutableLiveData<FetchLivePoll> FetchLivePollList = new MutableLiveData<>();

    public static LivePollRepository getInstance() {
        if (LivePollRepository == null) {
            LivePollRepository = new LivePollRepository();
        }
        return LivePollRepository;
    }

    private APIService eventApi;

    public LivePollRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchLivePoll> getLivepoll(String token ,String event_id) {
        eventApi.livePollFetch(token,event_id
                )
                .enqueue(new Callback<FetchLivePoll>() {
                    @Override
                    public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {
                        if (response.isSuccessful()) {
                            FetchLivePollList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                        FetchLivePollList.setValue(null);
                    }
                });

        return FetchLivePollList;
    }
}
