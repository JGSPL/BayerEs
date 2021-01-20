package com.procialize.bayer2020.ui.EULA;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.EULA.viewmodel.EulaViewModel;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;


public class EulaActivity extends AppCompatActivity {
    String api_token,event_id;
    EulaViewModel eulaviewmodel;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);

        new RefreashToken(EulaActivity.this).callGetRefreashToken(EulaActivity.this);
        eulaviewmodel = ViewModelProviders.of(this).get(EulaViewModel.class);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final WebView webView = (WebView) findViewById(R.id.webview_eual);


        eulaviewmodel.getEula();
        eulaviewmodel.getEulaInfo().observeForever(new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda fetchAgenda) {
                if(fetchAgenda!=null)
                {
                    if(fetchAgenda.getHeader().get(0).getType().equalsIgnoreCase("success"))
                    {
                        String eulaLink = fetchAgenda.getDetail();
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);

                        webView.loadUrl(eulaLink);
                    }
                }
            }
        });
    }
}