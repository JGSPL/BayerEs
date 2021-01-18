package com.procialize.bayer2020.ui.Privacypolicy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.Privacypolicy.networking.PrivacyPolicyRepository;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;


public class PrivacyPolicyViewModel extends ViewModel {
    private PrivacyPolicyRepository privacyPolicyRepository = PrivacyPolicyRepository.getInstance();
    MutableLiveData<FetchAgenda> eulaData = new MutableLiveData<>();

    public void getPrivacyPolicy() {
        privacyPolicyRepository = PrivacyPolicyRepository.getInstance();
        eulaData = privacyPolicyRepository.getPrivacyPolicyInfo();
    }

    public LiveData<FetchAgenda> getPrivacyPolicyInfo() {
        return eulaData;
    }
}
