package com.procialize.bayer2020.ui.EULA.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.EULA.networking.EulaRepository;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;


public class EulaViewModel extends ViewModel {
    private EulaRepository eulaRepository = EulaRepository.getInstance();
    MutableLiveData<FetchAgenda> eulaData = new MutableLiveData<>();

    public void getEula() {
        eulaRepository = EulaRepository.getInstance();
        eulaData = eulaRepository.getEulaInfo();
    }

    public LiveData<FetchAgenda> getEulaInfo() {
        return eulaData;
    }
}
