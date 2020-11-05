package com.procialize.eventapp.ui.quiz.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.model.QuizSubmit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDetailRepository {

    private static QuizDetailRepository quizRepository;
    MutableLiveData<QuizListing> quizList = new MutableLiveData<>();
    MutableLiveData<QuizSubmit> quizsubmit = new MutableLiveData<>();

    public static QuizDetailRepository getInstance() {
        if (quizRepository == null) {
            quizRepository = new QuizDetailRepository();
        }
        return quizRepository;
    }

    private APIService eventApi;

    public QuizDetailRepository() {
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

    public MutableLiveData<QuizSubmit> quizSubmit(String token, String event_id,String quiz_question_id,String quiz_options_id) {
        eventApi.submitQuiz(token, event_id,quiz_question_id, quiz_options_id)
                .enqueue(new Callback<QuizSubmit>() {
                    @Override
                    public void onResponse(Call<QuizSubmit> call, Response<QuizSubmit> response) {
                        if (response.isSuccessful()) {
                            quizsubmit.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<QuizSubmit> call, Throwable t) {
                        quizsubmit.setValue(null);
                    }
                });

        return quizsubmit;
    }

}
