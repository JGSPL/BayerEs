package com.procialize.bayer2020.ui.loyalityleap.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.catalogue.model.ProductType;
import com.procialize.bayer2020.ui.loyalityleap.model.My_point;
import com.procialize.bayer2020.ui.loyalityleap.model.Scheme_offer_item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class ScheameOfferDetail_Activity extends AppCompatActivity {
    String api_token,event_id, docurl;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    Scheme_offer_item ScheameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheame_detail);

        new RefreashToken(ScheameOfferDetail_Activity.this).callGetRefreashToken(ScheameOfferDetail_Activity.this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        ScheameList = (Scheme_offer_item) getIntent().getSerializableExtra("ScheameList");

        final WebView webView = (WebView) findViewById(R.id.webview_scheame);
        setUpToolbar();
        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*String eulaLink = ScheameList.getDescription() ;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

       // webView.loadUrl(eulaLink);
        webView.loadData(eulaLink, "text/html", "UTF-8");*/
        docurl = getIntent().getStringExtra("url");

        String eulaLink = docurl+ScheameList.getDescription() ;

        final WebView webview = (WebView) findViewById(R.id.webview_scheame);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setBackgroundColor(Color.TRANSPARENT);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                ScheameList.getId(),
                "scheme_and_offer_view",
                "scheme_and_offer",
                "7");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------

        getMyPoints();

        webview.clearCache(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        webview.loadUrl(eulaLink);

        webview.setWebViewClient(new WebViewClient() {
            boolean checkhasOnPageStarted = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkhasOnPageStarted = true;
            }

            public void onPageFinished(WebView view, String url) {
                if (view.getTitle().equals(""))
                    view.reload();
            }
        });
    }
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
           /* setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
            headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(ScheameOfferDetail_Activity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ScheameOfferDetail_Activity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ScheameOfferDetail_Activity.this)
                    .load(eventListMediaPath + eventLogo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(headerlogoIv);

        }
    }
    void getMyPoints( ) {
        ApiUtils.getAPIService().SchemeAndOfferRead(api_token,"1",ScheameList.getId()).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {

                  /*  String strEventList = response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(this);
                    String data = refreashToken.decryptedData(strEventList);*/
                  /*  My_point pincodeLists = new Gson().fromJson(data, new TypeToken<My_point>() {
                    }.getType());
                    if(pincodeLists!=null){
                        txtMyPoint.setText(pincodeLists.getMypoint());
                        txtMyRank.setText(pincodeLists.getRank());
                        txtUnreadCount.setText(pincodeLists.getSchemeUnreadCount());
                    }*/


                } else {
                    // Toast.makeText(getContext(), "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                try {
                    //  Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

}
