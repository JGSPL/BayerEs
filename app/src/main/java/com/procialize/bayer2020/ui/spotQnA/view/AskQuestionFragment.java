package com.procialize.bayer2020.ui.spotQnA.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.agenda.model.Agenda;
import com.procialize.bayer2020.ui.spotQnA.viewModel.SpotQnAViewModel;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AskQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskQuestionFragment extends Fragment implements View.OnClickListener {
    TextView tv_count;
    EditText et_question;
    LinearLayout ll_cancel, ll_send_question;
    SpotQnAViewModel spotQnAViewModel;
    String api_token, eventid;
    Agenda agenda;
    FrameLayout fl_main;

    public static AskQuestionFragment newInstance() {
        return new AskQuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ask_question, container, false);
        spotQnAViewModel = ViewModelProviders.of(this).get(SpotQnAViewModel.class);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");

        tv_count = root.findViewById(R.id.tv_count);
        et_question = root.findViewById(R.id.et_question);
        ll_cancel = root.findViewById(R.id.ll_cancel);
        ll_send_question = root.findViewById(R.id.ll_send_question);
        fl_main = root.findViewById(R.id.fl_main);
        ll_cancel.setOnClickListener(this);
        ll_send_question.setOnClickListener(this);

        et_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_count.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_cancel:
                getFragmentManager().popBackStack();
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, SpotQnAFragment.newInstance(), "")
                        .commit();*/
                break;
            case R.id.ll_send_question:
                if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
                    ll_send_question.setEnabled(true);
                    final String question = et_question.getText().toString().trim();

                    if (!question.isEmpty()) {
                        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
                        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

                        spotQnAViewModel.submitSpotQnA(api_token, eventid, agenda.getSession_id(), question);
                        spotQnAViewModel.submitSpotQnAList().observe(getActivity(), new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    getFragmentManager().popBackStack();
                                    Utility.createShortSnackBar(fl_main, loginOrganizer.getHeader().get(0).getMsg());
                                } else {
                                    ll_send_question.setEnabled(true);
                                    Utility.createShortSnackBar(fl_main, loginOrganizer.getHeader().get(0).getMsg());
                                }
                                if (spotQnAViewModel != null && spotQnAViewModel.submitSpotQnAList().hasObservers()) {
                                    spotQnAViewModel.submitSpotQnAList().removeObservers(getActivity());
                                }
                            }
                        });
                    } else {
                        ll_send_question.setEnabled(true);
                        Utility.createShortSnackBar(fl_main, "Please enter some status to post");
                    }

                } else {
                    Utility.createShortSnackBar(fl_main, "No Internet Connection..!");
                }
                break;
        }
    }
}