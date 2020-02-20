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


public class EventInfoFragment extends Fragment {

    private EventInfoViewModel homeViewModel;

    public static EventInfoFragment newInstance() {

        return new EventInfoFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(EventInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_eventinfo, container, false);
        return root;
    }
}