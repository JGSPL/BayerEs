package com.procialize.eventapp.ui.agenda.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.adapter.AgendaAdapter;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;
import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.agenda.roomDB.TableAgenda;
import com.procialize.eventapp.ui.agenda.viewmodel.AgendaDatabaseViewModel;
import com.procialize.eventapp.ui.agenda.viewmodel.AgendaViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class AgendaFragment extends Fragment implements AgendaAdapter.AgendaAdapterListner {

    private AgendaViewModel agendaViewModel;
    private AgendaDatabaseViewModel agendaDatabaseViewModel;
    String api_token, eventid;
    AgendaAdapter agendaAdapter;
    SwipeRefreshLayout swiperefresh_agenda;
    RecyclerView recycler_agenda;
    ConstraintLayout cl_main;
    private List<Agenda> agendaDBList = new ArrayList<>();

    public static AgendaFragment newInstance() {

        return new AgendaFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);
        agendaDatabaseViewModel = ViewModelProviders.of(getActivity()).get(AgendaDatabaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_agenda, container, false);

        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        recycler_agenda = root.findViewById(R.id.recycler_agenda);
        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
            getDataFromApi();
        } else {
            getDataFromDB();
        }
        swiperefresh_agenda = root.findViewById(R.id.swiperefresh_agenda);
        cl_main = root.findViewById(R.id.cl_main);
        swiperefresh_agenda.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
                    getDataFromApi();
                } else {
                    swiperefresh_agenda.setRefreshing(false);
                    Utility.createShortSnackBar(cl_main, "No Internet Connection..!");
                }
            }
        });
        return root;
    }

    public void getDataFromApi() {
        agendaViewModel.getAgenda(api_token, eventid);
        agendaViewModel.getAgendaList().observe(this, new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda event) {
                swiperefresh_agenda.setRefreshing(false);
                if(event!=null) {
                    String strCommentList = event.getDetail();
                    RefreashToken refreashToken = new RefreashToken(getContext());
                    String data = refreashToken.decryptedData(strCommentList);
                    try {
                        Gson gson = new Gson();
                        List<AgendaList> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<AgendaList>>() {
                        }.getType());
                        if (agendaLists != null) {
                            agendaDatabaseViewModel.deleteAllAgenda(getActivity());
                            agendaDatabaseViewModel.insertIntoDb(getActivity(), agendaLists.get(0).getAgenda_list());

                            if (agendaLists.size() > 0) {
                                List<Agenda> agenda = agendaLists.get(0).getAgenda_list();
                                setupAgendaAdapter(agenda);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (agendaViewModel != null && agendaViewModel.getAgendaList().hasObservers()) {
                    agendaViewModel.getAgendaList().removeObservers(getActivity());
                }
            }
        });
    }

    public void getDataFromDB() {
        agendaDatabaseViewModel.getAgendaList(getActivity());
        agendaDatabaseViewModel.getAgenda().observeForever(new Observer<List<TableAgenda>>() {
            @Override
            public void onChanged(List<TableAgenda> tableAttendees) {
                for (int i = 0; i < tableAttendees.size(); i++) {
                    final Agenda agenda = new Agenda();

                    agenda.setSession_id(tableAttendees.get(i).getSession_id());
                    agenda.setSession_name(tableAttendees.get(i).getSession_name());
                    agenda.setSession_short_description(tableAttendees.get(i).getSession_short_description());
                    agenda.setSession_description(tableAttendees.get(i).getSession_description());
                    agenda.setSession_start_time(tableAttendees.get(i).getSession_start_time());
                    agenda.setSession_end_time(tableAttendees.get(i).getSession_end_time());
                    agenda.setSession_date(tableAttendees.get(i).getSession_date());
                    agenda.setEvent_id(tableAttendees.get(i).getEvent_id());
                    agenda.setLivestream_link(tableAttendees.get(i).getLivestream_link());
                    agenda.setStar(tableAttendees.get(i).getStar());
                    agenda.setTotal_feedback(tableAttendees.get(i).getTotal_feedback());
                    agenda.setFeedback_comment(tableAttendees.get(i).getFeedback_comment());
                    agenda.setRated(tableAttendees.get(i).getRated());
                    agenda.setReminder_flag(tableAttendees.get(i).getReminder_flag());
                    agendaDBList.add(agenda);
                }
                setupAgendaAdapter(agendaDBList);
            }
        });
    }

    public void setupAgendaAdapter(List<Agenda> agenda) {
        agendaAdapter = new AgendaAdapter(getContext(), agenda, AgendaFragment.this);
        recycler_agenda.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_agenda.setAdapter(agendaAdapter);
        agendaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactSelected(Agenda attendee) {

    }
}