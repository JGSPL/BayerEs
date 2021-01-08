package com.procialize.bayer2020.ui.loyalityleap.view;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.loyalityleap.adapter.RequestToRedeemAdapter;
import com.procialize.bayer2020.ui.loyalityleap.adapter.RedeemHistoryStatusAdapter;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRequestToRedeem;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistoryStatus;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemStatusBasicData;
import com.procialize.bayer2020.ui.loyalityleap.model.RequestToRedeem;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_item;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_status_item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class RequestToRedeemActivity extends AppCompatActivity implements RequestToRedeemAdapter.ProductAdapterListner{
    RecyclerView recycler_mpointcalc,recycler_mpoinStatus;
    ProgressBar progressBar;
    private ConnectionDetector cd;
    MutableLiveData<FetchRequestToRedeem> FetchProductTypeList = new MutableLiveData<>();
    MutableLiveData<FetchRedeemHistoryStatus> FetchRedeemStatusList = new MutableLiveData<>();
    String eventid;
    String token;
    RelativeLayout relative;
    String Imageurl;
    private APIService eventApi;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    redeem_history_item pestType;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeption_history_list);
        cd = ConnectionDetector.getInstance(this);

        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        // eventid = "1";
        //  pestType = (redeem_history_item) getIntent().getSerializableExtra("PestType");

        recycler_mpointcalc = findViewById(R.id.recycler_mpointcalc);
        progressBar = findViewById(R.id.progressBar);
        relative = findViewById(R.id.relative);
        setUpToolbar();
        if (cd.isConnectingToInternet()) {

            getProductType(token,eventid);
        } else {
            Utility.createShortSnackBar(relative, "No internet connection");


        }

    }

    public MutableLiveData<FetchRequestToRedeem> getProductType(String token, String eventid) {
        eventApi = ApiUtils.getAPIService();

        eventApi.RedeemRequestList(token,eventid,"","1",""
        )
                .enqueue(new Callback<FetchRequestToRedeem>() {
                    @Override
                    public void onResponse(Call<FetchRequestToRedeem> call, Response<FetchRequestToRedeem> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getTotalRecords();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(RequestToRedeemActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<RequestToRedeem> eventLists = gson.fromJson(data, new TypeToken<ArrayList<RequestToRedeem>>() {}.getType());

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
                    public void onFailure(Call<FetchRequestToRedeem> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<RequestToRedeem> productList) {
        RequestToRedeemAdapter requestAdapter = new RequestToRedeemAdapter(this, productList, this, Imageurl);
        //recycler_mpointcalc.setLayoutManager(new LinearLayoutManager(getContext()));
        // use a linear layout manager
        int columns = 1;
        recycler_mpointcalc.setLayoutManager(new GridLayoutManager(this, columns));

        recycler_mpointcalc.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {

            getProductType(token,eventid);
        } else {
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

            String eventLogo = SharedPreference.getPref(RequestToRedeemActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(RequestToRedeemActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(RequestToRedeemActivity.this)
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
    public void onContactSelected(RequestToRedeem pollList) {

    }
}