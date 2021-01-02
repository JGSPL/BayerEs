package com.procialize.bayer2020.ui.loyalityleap.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.procialize.bayer2020.ui.catalogue.view.ProductListDetailActivity;
import com.procialize.bayer2020.ui.loyalityleap.adapter.ScheameOfferAdapter;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchSchemeOffer;
import com.procialize.bayer2020.ui.loyalityleap.model.Scheme_offer_item;

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

public class ScheameOfferListActivity extends AppCompatActivity implements ScheameOfferAdapter.ProductAdapterListner{
    SwipeRefreshLayout productrefresh;
    RecyclerView productTypeRv;
    ProgressBar progressBar;
    private ConnectionDetector cd;
    MutableLiveData<FetchSchemeOffer> FetchProductTypeList = new MutableLiveData<>();
    String eventid;
    String token;
    RelativeLayout relative;
    String Imageurl;
    private APIService eventApi;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    Scheme_offer_item pestType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemeoffer_list);
        cd = ConnectionDetector.getInstance(this);

        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        // eventid = "1";
      //  pestType = (Scheme_offer_item) getIntent().getSerializableExtra("PestType");

        productTypeRv = findViewById(R.id.productTypeRv);
        productrefresh = findViewById(R.id.productrefresh);
        progressBar = findViewById(R.id.progressBar);
        relative = findViewById(R.id.relative);
        setUpToolbar();
        if (cd.isConnectingToInternet()) {

            getProductType(token,eventid);
        } else {
            if (productrefresh.isRefreshing()) {
                productrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(relative, "No internet connection");


        }

    }

    public MutableLiveData<FetchSchemeOffer> getProductType(String token, String eventid) {
        if (productrefresh.isRefreshing()) {
            productrefresh.setRefreshing(false);
        }
        eventApi = ApiUtils.getAPIService();

        eventApi.SchemeAndOfferList(token,eventid,"","1",""
        )
                .enqueue(new Callback<FetchSchemeOffer>() {
                    @Override
                    public void onResponse(Call<FetchSchemeOffer> call, Response<FetchSchemeOffer> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getImagepath();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(ScheameOfferListActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Scheme_offer_item> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Scheme_offer_item>>() {}.getType());

                            //Fetch Livepoll list
                            if(eventLists!=null) {

                                progressBar.setVisibility(View.GONE);


                                if(eventLists.size()>0) {



                                    setupEventAdapter(eventLists);
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);

                                }

                            }else{

                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchSchemeOffer> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<Scheme_offer_item> productList) {
        ScheameOfferAdapter productypeAdapter = new ScheameOfferAdapter(this, productList, this, Imageurl);
        //productTypeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        // use a linear layout manager
        int columns = 2;
        productTypeRv.setLayoutManager(new GridLayoutManager(this, columns));

        productTypeRv.setAdapter(productypeAdapter);
        productypeAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {

            getProductType(token,eventid);
        } else {
            if (productrefresh.isRefreshing()) {
                productrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(relative, "No internet connection");


        }
    }
    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();
            headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(ScheameOfferListActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ScheameOfferListActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ScheameOfferListActivity.this)
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


  

    @Override
    public void onContactSelected(Scheme_offer_item scheameList) {
        startActivity(new Intent(this, ScheameOfferDetail_Activity.class)
                .putExtra("ScheameList", (Serializable) scheameList));
    }
}
