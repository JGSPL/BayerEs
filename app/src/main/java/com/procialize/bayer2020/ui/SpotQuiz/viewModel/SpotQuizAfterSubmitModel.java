package com.procialize.bayer2020.ui.SpotQuiz.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.SpotQuiz.networking.SpotQuizSubmittedRepository;
import com.procialize.bayer2020.ui.quiz.model.QuizListing;

public class SpotQuizAfterSubmitModel extends ViewModel {

    private SpotQuizSubmittedRepository quizSubmittedRepository = SpotQuizSubmittedRepository.getInstance();
    MutableLiveData<QuizListing> quizData = new MutableLiveData<>();

    public void getQuizList(String token, String event_id,String session_id) {

        quizSubmittedRepository = SpotQuizSubmittedRepository.getInstance();
        quizData = quizSubmittedRepository.getQuizList(token, event_id,session_id);
    }

    public LiveData<QuizListing> getQuizList() {
        return quizData;
    }


}
