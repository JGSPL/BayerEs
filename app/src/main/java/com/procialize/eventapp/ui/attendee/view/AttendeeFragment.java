package com.procialize.eventapp.ui.attendee.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeViewModel;


public class AttendeeFragment extends Fragment {

    private AttendeeViewModel homeViewModel;

    public static AttendeeFragment newInstance() {

        return new AttendeeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(AttendeeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_attendee, container, false);
        return root;
    }
}