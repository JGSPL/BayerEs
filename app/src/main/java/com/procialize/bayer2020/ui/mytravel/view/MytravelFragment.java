package com.procialize.bayer2020.ui.mytravel.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.bayer2020.R;

import com.procialize.bayer2020.ui.mytravel.viewmodel.MytravelViewModel;

public class MytravelFragment extends Fragment {

    private MytravelViewModel homeViewModel;

    public static MytravelFragment newInstance() {

        return new MytravelFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(MytravelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mytravel, container, false);

        return root;
    }
}