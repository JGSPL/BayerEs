package com.procialize.eventapp.ui.agenda.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.agenda.adapter.AgendaAdapter;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;
import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.agenda.viewmodel.AgendaViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class AgendaFragment extends Fragment implements AgendaAdapter.AgendaAdapterListner {

    private AgendaViewModel agendaViewModel;
    String api_token, eventid;
    AgendaAdapter agendaAdapter;
    RecyclerView recycler_agenda;

    public static AgendaFragment newInstance() {

        return new AgendaFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agenda, container, false);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        recycler_agenda = root.findViewById(R.id.recycler_agenda);
        agendaViewModel.getAgenda(api_token, eventid);
        agendaViewModel.getAgendaList().observe(this, new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda event) {
                String strCommentList = event.getDetail();
                RefreashToken refreashToken = new RefreashToken(getContext());
                String data = refreashToken.decryptedData(strCommentList);
                try {
                    Gson gson = new Gson();
                    List<AgendaList> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<AgendaList>>() {
                    }.getType());
                    if (agendaLists != null) {
                        setupAgendaAdapter(agendaLists);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (agendaViewModel != null && agendaViewModel.getAgendaList().hasObservers()) {
                    agendaViewModel.getAgendaList().removeObservers(getActivity());
                }
            }
        });
        return root;
    }

    public void setupAgendaAdapter(List<AgendaList> agendaLists) {
        if (agendaLists.size() > 0) {
            List<Agenda> agenda = agendaLists.get(0).getAgenda_list();
            agendaAdapter = new AgendaAdapter(getContext(), agenda, AgendaFragment.this);
            recycler_agenda.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_agenda.setAdapter(agendaAdapter);
            agendaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(Agenda attendee) {

    }
}