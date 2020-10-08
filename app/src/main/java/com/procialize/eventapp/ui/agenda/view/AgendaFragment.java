package com.procialize.eventapp.ui.agenda.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;
import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.agenda.viewmodel.AgendaViewModel;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.speaker.model.Speaker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class AgendaFragment extends Fragment {

    private AgendaViewModel agendaViewModel;
    String api_token, eventid;

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

        agendaViewModel.getAgenda(api_token, eventid);
        agendaViewModel.getAgendaList().observe(this, new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda event) {
                String strCommentList = event.getDetail();
                RefreashToken refreashToken = new RefreashToken(getContext());
                String data = refreashToken.decryptedData(strCommentList);
                try {
                    Gson gson = new Gson();
                    List<AgendaList> eventLists = gson.fromJson(data, new TypeToken<ArrayList<AgendaList>>() {}.getType());
                    List<AgendaList> eventLists1 =eventLists;
                    /*List<AgendaList> eventLists = gson.fromJson(data, new TypeToken<ArrayList<AgendaList>>() {
                    }.getType());
                    if (eventLists != null) {
                        //setupAgendaAdapter(eventLists);
                    }*/
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

    public void setupAgendaAdapter(List<Agenda> commentList) {
        List<Attendee> attendeeList = new ArrayList<>();
        if (commentList != null) {
            /*attendeeAdapter = new AttendeeAdapter(getContext(), attendeeList, AgendaFragment.this);
            attendeerecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            attendeerecycler.setAdapter(attendeeAdapter);
            attendeeAdapter.notifyDataSetChanged();*/
        }
    }
}