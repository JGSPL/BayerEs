package com.procialize.bayer2020.ui.document.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.document.model.Document;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentRepository {

    private static DocumentRepository docRepository;
    MutableLiveData<Document> docList = new MutableLiveData<>();

    public static DocumentRepository getInstance() {
        if (docRepository == null) {
            docRepository = new DocumentRepository();
        }
        return docRepository;
    }

    private APIService eventApi;

    public DocumentRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<Document> getDocumentList(String token, String event_id, String pageSize, String pageNumber) {
        eventApi.getDocumentList(token, event_id,pageSize,pageNumber)
                .enqueue(new Callback<Document>() {
                    @Override
                    public void onResponse(Call<Document> call, Response<Document> response) {
                        if (response.isSuccessful()) {
                            docList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Document> call, Throwable t) {
                       // docList.setValue(null);
                    }
                });

        return docList;
    }
}
