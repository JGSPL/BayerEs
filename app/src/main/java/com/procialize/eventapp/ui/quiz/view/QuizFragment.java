package com.procialize.eventapp.ui.quiz.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizViewModel;

public class QuizFragment extends Fragment {


    private QuizViewModel homeViewModel;

    public static QuizFragment newInstance() {

        return new QuizFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);
        View root = inflater.inflate(R.layout.activity_quiz_listing, container, false);

        return root;
    }
}
