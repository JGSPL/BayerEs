package com.procialize.bayer2020.ui.Contactus.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.Contactus.networking.ContactUsRepository;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;

public class ContactUsViewModel extends ViewModel {
    private ContactUsRepository privacyPolicyRepository = ContactUsRepository.getInstance();
    MutableLiveData<FetchAgenda> eulaData = new MutableLiveData<>();

    public void getContactUs(String auth,
                             String event_id) {
        privacyPolicyRepository = ContactUsRepository.getInstance();
        eulaData = privacyPolicyRepository.getContactUs( auth,
                 event_id);
    }

    public LiveData<FetchAgenda> getContactUsLiveData() {
        return eulaData;
    }
}
