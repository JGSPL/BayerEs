package com.procialize.eventapp.ui.quiz.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.quiz.model.QuizListing;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRepository {

    private static QuizRepository quizRepository;
    MutableLiveData<QuizListing> quizList = new MutableLiveData<>();

    public static QuizRepository getInstance() {
        if (quizRepository == null) {
            quizRepository = new QuizRepository();
        }
        return quizRepository;
    }

    private APIService eventApi;

    public QuizRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<QuizListing> getQuizList(String token, String event_id) {
        eventApi.getQuizList(token, event_id)
                .enqueue(new Callback<QuizListing>() {
                    @Override
                    public void onResponse(Call<QuizListing> call, Response<QuizListing> response) {
                        if (response.isSuccessful()) {
                            quizList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<QuizListing> call, Throwable t) {
                        quizList.setValue(null);
                    }
                });

        return quizList;
    }

}
