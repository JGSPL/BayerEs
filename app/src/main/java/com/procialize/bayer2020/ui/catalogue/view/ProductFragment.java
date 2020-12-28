package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.procialize.bayer2020.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.bayer2020.ui.catalogue.adapter.ProductTypeAdapter;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductType;
import com.procialize.bayer2020.ui.catalogue.model.ProductType;
import com.procialize.bayer2020.ui.livepoll.adapter.LivePollAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class ProductFragment extends Fragment implements ProductTypeAdapter.ProductAdapterListner {
    SwipeRefreshLayout productrefresh;
    RecyclerView productTypeRv;
    ProgressBar progressBar;
    View rootView;
    private ConnectionDetector cd;
    private APIService eventApi;
    MutableLiveData<FetchProductType> FetchProductTypeList = new MutableLiveData<>();
    String eventid;
    String token;
    LinearLayout linMain;
    String Imageurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         rootView = inflater.inflate(R.layout.fragment_productlist,
                container, false);
        cd = ConnectionDetector.getInstance(getContext());

        token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getContext(), EVENT_ID);
       // eventid = "1";

        productTypeRv = rootView.findViewById(R.id.productTypeRv);
        productrefresh = rootView.findViewById(R.id.productrefresh);
        progressBar = rootView.findViewById(R.id.progressBar);
        linMain = rootView.findViewById(R.id.linMain);

        if (cd.isConnectingToInternet()) {

            getProductType(token,eventid);
        } else {
            if (productrefresh.isRefreshing()) {
                productrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(linMain, "No internet connection");


        }
        return rootView;
    }

    public MutableLiveData<FetchProductType> getProductType(String token, String eventid) {
        if (productrefresh.isRefreshing()) {
            productrefresh.setRefreshing(false);
        }
        eventApi = ApiUtils.getAPIService();

        eventApi.ProductTypeList(token,eventid,"","1",""
        )
                .enqueue(new Callback<FetchProductType>() {
                    @Override
                    public void onResponse(Call<FetchProductType> call, Response<FetchProductType> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getProduct_type_imagepath();

                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(getContext());
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                           List<ProductType> eventLists = gson.fromJson(data, new TypeToken<ArrayList<ProductType>>() {}.getType());

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
                    public void onFailure(Call<FetchProductType> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<ProductType> productList) {
        ProductTypeAdapter productypeAdapter = new ProductTypeAdapter(getContext(), productList, ProductFragment.this, Imageurl);
       // productTypeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        // use a linear layout manager
        int columns = 1;
        productTypeRv.setLayoutManager(new GridLayoutManager(getContext(), columns));

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
            Utility.createShortSnackBar(linMain, "No internet connection");


        }
    }


    @Override
    public void onContactSelected(ProductType pollList) {
        getActivity().startActivity(new Intent(getContext(), ProductListActivity.class)
                .putExtra("ProductType", (Serializable) pollList));
    }
}
