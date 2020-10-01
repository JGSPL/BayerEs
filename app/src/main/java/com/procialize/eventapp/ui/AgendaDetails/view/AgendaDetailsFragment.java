package com.procialize.eventapp.ui.AgendaDetails.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.AgendaDetails.viewmodel.AgendaViewModel;

public class AgendaDetailsFragment extends Fragment {

    private AgendaViewModel agendaFragment;

    public static AgendaDetailsFragment newInstance() {

        return new AgendaDetailsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agendaFragment = ViewModelProviders.of(this).get(AgendaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agenda_details, container, false);

        return root;
    }
}