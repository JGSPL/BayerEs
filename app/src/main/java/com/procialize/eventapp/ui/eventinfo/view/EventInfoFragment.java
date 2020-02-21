package com.procialize.eventapp.ui.eventinfo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.eventinfo.viewmodel.EventInfoViewModel;
import com.procialize.eventapp.ui.livepoll.viewmodel.LivePollViewModel;

public class EventInfoFragment extends Fragment {

    EventInfoViewModel eventInfoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventInfoViewModel =
                ViewModelProviders.of(this).get(EventInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_eventinfo, container, false);

        return root;
    }
}
