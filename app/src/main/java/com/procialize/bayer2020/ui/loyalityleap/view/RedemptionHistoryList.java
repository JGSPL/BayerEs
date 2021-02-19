package com.procialize.bayer2020.ui.loyalityleap.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.loyalityleap.adapter.RedeemHistoryAdapter;
import com.procialize.bayer2020.ui.loyalityleap.adapter.RedeemHistoryStatusAdapter;
import com.procialize.bayer2020.ui.loyalityleap.adapter.ScheameOfferAdapter;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistory;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistoryStatus;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemStatusBasicData;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_item;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_item;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_status_item;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class RedemptionHistoryList extends AppCompatActivity implements RedeemHistoryAdapter.ProductAdapterListner, RedeemHistoryStatusAdapter.ProductAdapterListner{
    RecyclerView recycler_mpointcalc,recycler_mpoinStatus;
    ProgressBar progressBar;
    private ConnectionDetector cd;
    MutableLiveData<FetchRedeemHistory> FetchProductTypeList = new MutableLiveData<>();
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

        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (cd.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);

            getProductType(token,eventid);
        } else {
            progressBar.setVisibility(View.GONE);

            Utility.createShortSnackBar(relative, "No internet connection");
        }


        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);

            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RedemptionHistoryList.this, NotificationActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

    }

    public MutableLiveData<FetchRedeemHistory> getProductType(String token, String eventid) {
        eventApi = ApiUtils.getAPIService();

        eventApi.RedemptionHistory(token,eventid,"","1",""
        )
                .enqueue(new Callback<FetchRedeemHistory>() {
                    @Override
                    public void onResponse(Call<FetchRedeemHistory> call, Response<FetchRedeemHistory> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getTotalRecords();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(RedemptionHistoryList.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<redeem_history_item> eventLists = gson.fromJson(data, new TypeToken<ArrayList<redeem_history_item>>() {}.getType());

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
                    public void onFailure(Call<FetchRedeemHistory> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<redeem_history_item> productList) {
        RedeemHistoryAdapter productypeAdapter = new RedeemHistoryAdapter(this, productList, this, Imageurl);
        //recycler_mpointcalc.setLayoutManager(new LinearLayoutManager(getContext()));
        // use a linear layout manager
        int columns = 1;
        recycler_mpointcalc.setLayoutManager(new GridLayoutManager(this, columns));

        recycler_mpointcalc.setAdapter(productypeAdapter);
        productypeAdapter.notifyDataSetChanged();
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
           /* setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
            headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(RedemptionHistoryList.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(RedemptionHistoryList.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(RedemptionHistoryList.this)
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
    public void onContactSelected(redeem_history_item redeemlList) {
        showRedeemdialouge(redeemlList);
    }
    private void showRedeemdialouge(redeem_history_item redeemlList) {

        myDialog = new Dialog(RedemptionHistoryList.this);
        myDialog.setContentView(R.layout.dialog_redemption_history);
        myDialog.setCancelable(false);


       // LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);
        ImageView imgCancel = myDialog.findViewById(R.id.imgClose);
        final TextView txt_Data = myDialog.findViewById(R.id.txt_Data);
        final TextView txt_Data1 = myDialog.findViewById(R.id.txt_Data1);
        final TextView txt_Data2= myDialog.findViewById(R.id.txt_Data2);
        final TextView txt_Data3= myDialog.findViewById(R.id.txt_Data3);

        txt_Data.setText ("Requested Data  : "+ CommonFunction.convertDateRedeem(redeemlList.getRedemption_date()));
        txt_Data1.setText("Cataloged Name  : "+ redeemlList.getProduct_name());
        txt_Data2.setText("Qty             : "+ redeemlList.getQuantity());
        txt_Data3.setText("Points          : "+ redeemlList.getPoints());
        recycler_mpoinStatus = myDialog.findViewById(R.id.recycler_mpoinStatus);

        if (cd.isConnectingToInternet()) {

            getRedeemStatus(token,eventid,redeemlList.getId());
        } else {
            Utility.createShortSnackBar(relative, "No internet connection");


        }
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    public MutableLiveData<FetchRedeemHistoryStatus> getRedeemStatus(String token, String eventid, String  id) {
        eventApi = ApiUtils.getAPIService();

        eventApi.RedemptionHistoryDetails(token,eventid,id)
                .enqueue(new Callback<FetchRedeemHistoryStatus>() {
                    @Override
                    public void onResponse(Call<FetchRedeemHistoryStatus> call, Response<FetchRedeemHistoryStatus> response) {
                        if (response.isSuccessful()) {
                            FetchRedeemStatusList.setValue(response.body());
                           // Imageurl = response.body().get();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(RedemptionHistoryList.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<FetchRedeemStatusBasicData> eventLists = gson.fromJson(data, new TypeToken<ArrayList<FetchRedeemStatusBasicData>>() {}.getType());

                            //Fetch Livepoll list
                            if(eventLists!=null) {

                                progressBar.setVisibility(View.GONE);


                                if(eventLists.size()>0) {

                                    List<redeem_history_status_item> productList = eventLists.get(0).getRedeemHistoryStatusList();

                                    setupEventStatusAdapter(productList);
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
                    public void onFailure(Call<FetchRedeemHistoryStatus> call, Throwable t) {
                        FetchRedeemStatusList.setValue(null);
                    }
                });

        return FetchRedeemStatusList;
    }


    public void setupEventStatusAdapter(List<redeem_history_status_item> productList) {
        RedeemHistoryStatusAdapter productypeAdapter = new RedeemHistoryStatusAdapter(this, productList, this );
        int columns = 1;
        recycler_mpoinStatus.setLayoutManager(new GridLayoutManager(this, columns));

        recycler_mpoinStatus.setAdapter(productypeAdapter);
        productypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactSelected(redeem_history_status_item pollList) {

    }
}