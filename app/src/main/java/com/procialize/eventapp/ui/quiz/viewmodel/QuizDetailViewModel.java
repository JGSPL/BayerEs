package com.procialize.eventapp.ui.quiz.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.model.QuizSubmit;
import com.procialize.eventapp.ui.quiz.networking.QuizDetailRepository;

public class QuizDetailViewModel extends ViewModel {

    private QuizDetailRepository quizRepository = QuizDetailRepository.getInstance();
    MutableLiveData<QuizListing> quizData = new MutableLiveData<>();
    MutableLiveData<QuizSubmit> quizsubit = new MutableLiveData<>();

    public void getQuizList(String token, String event_id) {

        quizRepository = QuizDetailRepository.getInstance();
        quizData = quizRepository.getQuizList(token, event_id);
    }

    public void quizSubmit(String token, String event_id, String quiz_question_id, String quiz_options_id) {

        quizRepository = QuizDetailRepository.getInstance();
        quizsubit = quizRepository.quizSubmit(token, event_id, quiz_question_id, quiz_options_id);
    }

    public LiveData<QuizListing> getQuizList() {
        return quizData;
    }

    public LiveData<QuizSubmit> quizSubmit() {
        return quizsubit;
    }

}
