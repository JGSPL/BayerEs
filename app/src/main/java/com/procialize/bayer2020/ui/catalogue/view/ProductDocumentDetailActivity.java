package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.Contactus.ContactUsActivity;
import com.procialize.bayer2020.ui.catalogue.model.Product_document_detail;
import com.procialize.bayer2020.ui.catalogue.model.Product_item;
import com.procialize.bayer2020.ui.catalogue.model.product_subpoint_detail;
import com.procialize.bayer2020.ui.loyalityleap.view.ScheameOfferDetail_Activity;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class ProductDocumentDetailActivity extends AppCompatActivity {


    Toolbar mToolbar;
    ImageView headerlogoIv;
    String token, eventid, docurl, productId = "1", DocId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheame_detail);

        setUpToolbar();

        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);


        docurl = getIntent().getStringExtra("url");
        DocId = getIntent().getStringExtra("DocId");
        String eulaLink = docurl ;

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




            webview.clearCache(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        webview.loadUrl(eulaLink);

        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,token,
                eventid,
                DocId,
                "product_document_view",
                "Product Document",
                "10");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (!ProductDocumentDetailActivity.this.isFinishing()) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if (progress == 100) {
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });


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

            String eventLogo = SharedPreference.getPref(ProductDocumentDetailActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ProductDocumentDetailActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ProductDocumentDetailActivity.this)
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

            mToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
    }

}
