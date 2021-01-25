package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestDetails;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestRecommendedProducts;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestDetail;
import com.procialize.bayer2020.ui.catalogue.model.PestTypeItem;
import com.procialize.bayer2020.ui.catalogue.model.Pest_detail;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;


public class PestProductDetailsActivity extends AppCompatActivity {


    APIService eventApi;
    private FragmentTabHost mTabHostCel;
    ConnectionDetector cd;
    String token, eventid, imageurl, pestId = "1",Imageurl;
    LinearLayout linMain;

    ImageView headerlogoIv;
    Toolbar mToolbar;
    PestTypeItem pest_item;
    TextView tv_title;
    ImageView iv_cover;
    List<CataloguePestRecommendedProducts> recommendedeProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_product_details);

        cd = ConnectionDetector.getInstance(this);
        tv_title = findViewById(R.id.tv_title);
        linMain = findViewById(R.id.linMain);
        iv_cover = findViewById(R.id.iv_cover);
        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        mTabHostCel = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHostCel.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        pest_item = (PestTypeItem) getIntent().getSerializableExtra("PestType");
        Imageurl =  getIntent().getStringExtra("Imageurl");

        Bundle b = new Bundle();
        b.putSerializable("PestType", (Serializable) pest_item);
        b.putSerializable("recommendedeProductList", (Serializable) recommendedeProductList);
        mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab1")
                        .setIndicator(createTabView(this, "Details")),
                ProductDetailsFragment.class, b);

        setUpToolbar();

        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getDataFromApi(token, eventid);

    }

    public void getDataFromApi(String token, String eventid) {

        eventApi = ApiUtils.getAPIService();

        eventApi.PestDetails(token, eventid, pestId)
                .enqueue(new Callback<FetchPestDetail>() {
                    @Override
                    public void onResponse(Call<FetchPestDetail> call, Response<FetchPestDetail> response) {
                        if (response.isSuccessful()) {

                            String strCommentList = response.body().getDetail();

                            RefreashToken refreashToken = new RefreashToken(PestProductDetailsActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            CataloguePestDetails eventLists = gson.fromJson(data, new TypeToken<CataloguePestDetails>() {
                            }.getType());

                            //Fetch Livepoll list
                            if (eventLists != null) {
                                recommendedeProductList = eventLists.getPest_recommended_product();
                                Bundle b = new Bundle();
                                b.putSerializable("PestType", (Serializable) pest_item);
                                b.putSerializable("recommendedeProductList", (Serializable) recommendedeProductList);
                                b.putString("strRecommendedPath",eventLists.getRecommended_product_imagepath());
                                mTabHostCel.addTab(
                                        mTabHostCel.newTabSpec("Tab2")
                                                .setIndicator(createTabView(PestProductDetailsActivity.this, "Recommended products")),
                                        RecommendedProductFragment.class, b);
                                tv_title.setText(pest_item.getProduct_short_description());
                                Glide.with(PestProductDetailsActivity.this)
                                        .load(eventLists.getPest_imagepath()+pest_item.getProduct_image())
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(iv_cover);
                            } else {
                                Utility.createShortSnackBar(linMain, "Failure22");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchPestDetail> call, Throwable t) {
                        Utility.createShortSnackBar(linMain, "Failure");
                    }
                });


    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalogue_tab_bg, null);

        LinearLayout linTab = view.findViewById(R.id.linTab);
        //  linTab.setBackgroundColor(Color.parseColor(colorActive));
        //  linTab.setBackground(R.drawable.a);
        TextView tv = view.findViewById(R.id.tabsText);
        tv.setText(text);

        return view;
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

            String eventLogo = SharedPreference.getPref(PestProductDetailsActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(PestProductDetailsActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(PestProductDetailsActivity.this)
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
}