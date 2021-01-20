package com.procialize.bayer2020.ui.document.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.document.model.Document;
import com.procialize.bayer2020.ui.document.networking.DocumentRepository;


public class DocumentViewModel extends ViewModel {

    private DocumentRepository docRepository = DocumentRepository.getInstance();

    MutableLiveData<Document> docData = new MutableLiveData<>();

    public void getDocumentList(String token, String event_id, String pageSize, String pageNumber) {

        docRepository = DocumentRepository.getInstance();
        docData = docRepository.getDocumentList(token, event_id,pageSize,pageNumber);
    }

    public LiveData<Document> getDocumentList() {
        return docData;
    }
}