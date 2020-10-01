package com.procialize.eventapp.ui.AgendaDetails.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AgendaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AgendaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Agenda fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
