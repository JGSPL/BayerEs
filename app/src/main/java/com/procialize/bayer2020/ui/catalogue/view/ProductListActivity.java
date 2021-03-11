package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.procialize.bayer2020.ui.catalogue.adapter.ProductListAdapter;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductList;
import com.procialize.bayer2020.ui.catalogue.model.ProductType;
import com.procialize.bayer2020.ui.catalogue.model.Product_item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.ProductAdapterListner{
    SwipeRefreshLayout productrefresh;
    RecyclerView productTypeRv;
    ProgressBar progressBar;
    private ConnectionDetector cd;
    MutableLiveData<FetchProductList> FetchProductTypeList = new MutableLiveData<>();
    String eventid;
    String token;
    RelativeLayout relative;
    String Imageurl;
    private APIService eventApi;
    private ProductType productType;
    Toolbar mToolbar;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        cd = ConnectionDetector.getInstance(this);

        token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        // eventid = "1";
        Intent intent = getIntent();
        productType = (ProductType) getIntent().getSerializableExtra("ProductType");

        productTypeRv = findViewById(R.id.productTypeRv);
        productrefresh = findViewById(R.id.productrefresh);
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

        TextView productTitle = findViewById(R.id.productTitle);
        productTitle.setText(productType.getProduct_type_name());

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------
        if (cd.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);

            getProductType(token,eventid);
        } else {
            progressBar.setVisibility(View.GONE);

            if (productrefresh.isRefreshing()) {
                productrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(relative, "No internet connection");
        }

        productrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productrefresh.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    progressBar.setVisibility(View.VISIBLE);

                    getProductType(token,eventid);
                } else {
                    progressBar.setVisibility(View.GONE);

                    if (productrefresh.isRefreshing()) {
                        productrefresh.setRefreshing(false);
                    }
                    Utility.createShortSnackBar(relative, "No internet connection");
                }
            }
        });
    }

    public MutableLiveData<FetchProductList> getProductType(String token, String eventid) {
        if (productrefresh.isRefreshing()) {
            productrefresh.setRefreshing(false);
        }
        eventApi = ApiUtils.getAPIService();

        eventApi.ProductList(token,eventid,productType.getId(),"","1","")
                .enqueue(new Callback<FetchProductList>() {
                    @Override
                    public void onResponse(Call<FetchProductList> call, Response<FetchProductList> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getProduct_imagepath();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(ProductListActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Product_item> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Product_item>>() {}.getType());

                            //Fetch Livepoll list
                            if(eventLists!=null) {
                                progressBar.setVisibility(View.GONE);
                                if(eventLists.size()>0) {
                                     setupEventAdapter(eventLists);
                                }else{
                                    Utility.createShortSnackBar(relative, "Data not available");

                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }else{
                                Utility.createShortSnackBar(relative, "Data not available");

                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchProductList> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<Product_item> productList) {
        
        ProductListAdapter productypeAdapter = new ProductListAdapter(this, productList, ProductListActivity.this, Imageurl);
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

    @Override
    public void onContactSelected(Product_item product_item) {
        startActivity(new Intent(this, ProductListDetailActivity.class)
                .putExtra("Imageurl", Imageurl)
                .putExtra("Product", (Serializable) product_item));
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

            String eventLogo = SharedPreference.getPref(ProductListActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(ProductListActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(ProductListActivity.this)
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
