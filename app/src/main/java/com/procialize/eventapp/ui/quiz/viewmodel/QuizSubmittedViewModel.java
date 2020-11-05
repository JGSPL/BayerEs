package com.procialize.eventapp.ui.quiz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.networking.QuizSubmittedRepository;

public class QuizSubmittedViewModel extends ViewModel {

    private QuizSubmittedRepository quizSubmittedRepository = QuizSubmittedRepository.getInstance();
    MutableLiveData<QuizListing> quizData = new MutableLiveData<>();

    public void getQuizList(String token, String event_id) {

        quizSubmittedRepository = QuizSubmittedRepository.getInstance();
        quizData = quizSubmittedRepository.getQuizList(token, event_id);
    }

    public LiveData<QuizListing> getQuizList() {
        return quizData;
    }


}
