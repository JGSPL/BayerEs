package com.procialize.eventapp.ui.speaker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;

import com.procialize.eventapp.ui.speaker.viewmodel.SpeakerViewModel;

public class SpeakerFragment extends Fragment {

    private SpeakerViewModel homeViewModel;

    public static SpeakerFragment newInstance() {

        return new SpeakerFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(SpeakerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_speaker, container, false);
//        final TextView textView = root.findViewById(R.id.textView);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}