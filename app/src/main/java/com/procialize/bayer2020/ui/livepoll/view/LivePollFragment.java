package com.procialize.bayer2020.ui.livepoll.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.livepoll.viewmodel.LivePollViewModel;

public class LivePollFragment extends Fragment {

    LivePollViewModel livePollViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        livePollViewModel =
                ViewModelProviders.of(this).get(LivePollViewModel.class);
        View root = inflater.inflate(R.layout.fragment_livepoll, container, false);

        return root;
    }
}
