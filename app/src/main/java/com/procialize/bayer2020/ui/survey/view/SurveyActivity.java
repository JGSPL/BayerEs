package com.procialize.bayer2020.ui.survey.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.survey.adapter.SurveyAdapter;
import com.procialize.bayer2020.ui.survey.model.Survey;
import com.procialize.bayer2020.ui.survey.viewmodel.SurveyViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class SurveyActivity extends AppCompatActivity implements SurveyAdapter.SurveyAdapterListner {
    SurveyViewModel surveyViewModel;
    String api_token,event_id;
    ImageView iv_back;
    LinearLayout ll_main;
    SurveyAdapter surevyAdapter;
    RecyclerView surevyrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        new RefreashToken(SurveyActivity.this).callGetRefreashToken(SurveyActivity.this);
        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        //event_id = "1";
        iv_back = findViewById(R.id.iv_back);
        ll_main = findViewById(R.id.ll_main);
        surevyrecycler = findViewById(R.id.surevyrecycler);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //--------------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                Constant.pageVisited,
                "39",
                "0");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------

        surveyViewModel.getSurvey(api_token,"1"/*event_id*/,"100","1");
        surveyViewModel.getSurvey().observeForever(new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda fetchAgenda) {
                if(fetchAgenda!=null)
                {
                    if(fetchAgenda.getHeader().get(0).getType().equalsIgnoreCase("success"))
                    {
                        String contactUsLink = fetchAgenda.getDetail();
                        RefreashToken refreashToken = new RefreashToken(SurveyActivity.this);
                        String data = refreashToken.decryptedData(contactUsLink);
                        try {
                            Gson gson = new Gson();
                            List<Survey> contactUsList = gson.fromJson(data, new TypeToken<ArrayList<Survey>>() {
                            }.getType());
                            if (contactUsList != null) {
                                setupEventAdapter(contactUsList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Utility.createShortSnackBar(ll_main,fetchAgenda.getHeader().get(0).getMsg());
                    }
                }
            }
        });
    }

    public void setupEventAdapter(List<Survey> commentList) {
        if(commentList!=null) {

            surevyAdapter = new SurveyAdapter(SurveyActivity.this, commentList, SurveyActivity.this);
            surevyrecycler.setLayoutManager(new LinearLayoutManager(SurveyActivity.this));
            surevyrecycler.setAdapter(surevyAdapter);
            surevyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(Survey survey) {
        Uri webpage = Uri.parse(survey.getSurvey_url());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }
}