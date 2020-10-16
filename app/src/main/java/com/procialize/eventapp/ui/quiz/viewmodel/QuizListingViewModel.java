package com.procialize.eventapp.ui.quiz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.networking.QuizRepository;

public class QuizListingViewModel extends ViewModel {

    private QuizRepository quizRepository = QuizRepository.getInstance();
    MutableLiveData<QuizListing> quizData = new MutableLiveData<>();

    public void getQuizList(String token, String event_id) {

        quizRepository = QuizRepository.getInstance();
        quizData = quizRepository.getQuizList(token, event_id);
    }

    public LiveData<QuizListing> getQuizList() {
        return quizData;
    }
}
