package com.procialize.eventapp.ui.quiz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.networking.QuizDetailRepository;

public class QuizDetailViewModel extends ViewModel {

    private QuizDetailRepository quizRepository = QuizDetailRepository.getInstance();
    MutableLiveData<QuizListing> quizData = new MutableLiveData<>();

    public void getQuizList(String token, String event_id) {

        quizRepository = QuizDetailRepository.getInstance();
        quizData = quizRepository.getQuizList(token, event_id);
    }

    public LiveData<QuizListing> getQuizList() {
        return quizData;
    }

}
