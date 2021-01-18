package com.procialize.bayer2020.ui.EULA.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EulaRepository {

    MutableLiveData<FetchAgenda> eulaList = new MutableLiveData<>();
    private static EulaRepository eulaRepository;

    public static EulaRepository getInstance() {
        if (eulaRepository == null) {
            eulaRepository = new EulaRepository();
        }
        return eulaRepository;
    }

    private APIService eulaApi;

    public EulaRepository() {
        eulaApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchAgenda> getEulaInfo() {
        eulaApi.getEula()
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
