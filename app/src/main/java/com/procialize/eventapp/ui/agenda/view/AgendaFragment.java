package com.procialize.eventapp.ui.agenda.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
        final TextView textView = root.findViewById(R.id.textView);
        agendaFragment.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}