package com.procialize.bayer2020.ui.Contactus.networking;

import androidx.lifecycle.MutableLiveData;


import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsRepository {

    MutableLiveData<FetchAgenda> eulaList = new MutableLiveData<>();
    private static ContactUsRepository PrivacyPolicyRepository;

    public static ContactUsRepository getInstance() {
        if (PrivacyPolicyRepository == null) {
            PrivacyPolicyRepository = new ContactUsRepository();
        }
        return PrivacyPolicyRepository;
    }

    private APIService privacyPolicyApi;

    public ContactUsRepository() {
        privacyPolicyApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchAgenda> getContactUs(String auth,
                                                     String event_id) {
        privacyPolicyApi.getContactUs(auth,
                event_id)
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
