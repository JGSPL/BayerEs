package com.procialize.eventapp.ui.SpotQuiz.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.SpotQuiz.networking.SpotQuizSubmittedRepository;
import com.procialize.eventapp.ui.quiz.model.QuizSubmit;

public class SpotQuizSubmitViewModel extends ViewModel {

    MutableLiveData<QuizSubmit> quizsubit = new MutableLiveData<>();
    private SpotQuizSubmittedRepository quizRepository = SpotQuizSubmittedRepository.getInstance();

    public void quizSubmit(String token, String event_id, String quiz_question_id, String quiz_options_id) {

        quizRepository = SpotQuizSubmittedRepository.getInstance();
        quizsubit = quizRepository.quizSubmit(token, event_id, quiz_question_id, quiz_options_id);
    }


    public LiveData<QuizSubmit> quizSubmit() {
        return quizsubit;
    }
}
