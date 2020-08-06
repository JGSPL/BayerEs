package com.procialize.eventapp.ui.agenda.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.agenda.viewmodel.AgendaViewModel;

public class AgendaFragment extends Fragment {

    private AgendaViewModel agendaFragment;

    public static AgendaFragment newInstance() {

        return new AgendaFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agendaFragment = ViewModelProviders.of(this).get(AgendaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agenda, container, false);

        return root;
    }
}