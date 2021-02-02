package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.adapter.PestListAdapter;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestList;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;
import com.procialize.bayer2020.ui.catalogue.model.ProductType;
import com.procialize.bayer2020.ui.loyalityleap.view.ScheameOfferDetail_Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class PestFragment extends Fragment implements PestListAdapter.ProductAdapterListner {
    SwipeRefreshLayout productrefresh;
    RecyclerView productTypeRv;
    ProgressBar progressBar;
    View rootView;
    private ConnectionDetector cd;
    private APIService eventApi;
    MutableLiveData<FetchPestList> FetchProductTypeList = new MutableLiveData<>();
    String eventid;
    String token;
    LinearLayout linMain;
    String Imageurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_pestlist,
                container, false);
        cd = ConnectionDetector.getInstance(getContext());
        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getContext(), EVENT_ID);
        // eventid = "1";
        Log.e("token===>", token);
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

        productrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productrefresh.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    getProductType(token,eventid);
                } else {
                    if (productrefresh.isRefreshing()) {
                        productrefresh.setRefreshing(false);
                    }
                    Utility.createShortSnackBar(linMain, "No internet connection");
                }
            }
        });
        return rootView;
    }

    public MutableLiveData<FetchPestList> getProductType(String token, String eventid) {
        if (productrefresh.isRefreshing()) {
            productrefresh.setRefreshing(false);
        }
        eventApi = ApiUtils.getAPIService();

        eventApi.PestTypeList(token,eventid,"","1","")
                .enqueue(new Callback<FetchPestList>() {
                    @Override
                    public void onResponse(Call<FetchPestList> call, Response<FetchPestList> response) {
                        if (response.isSuccessful()) {
                            FetchProductTypeList.setValue(response.body());
                            Imageurl = response.body().getPest_imagepath();

                            String strCommentList =response.body().getDetail();

                            RefreashToken refreashToken = new RefreashToken(getContext());
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Pest_item> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Pest_item>>() {}.getType());

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
                        else
                        {
                            FetchProductTypeList.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchPestList> call, Throwable t) {
                        FetchProductTypeList.setValue(null);
                    }
                });

        return FetchProductTypeList;
    }


    public void setupEventAdapter(List<Pest_item> productList) {
        PestListAdapter productypeAdapter = new PestListAdapter(getContext(), productList, PestFragment.this, Imageurl);
        //productTypeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        // use a linear layout manager
        int columns = 2;
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
    public void onContactSelected(Pest_item pollList) {
        getActivity().startActivity(new Intent(getContext(), PestTypeActivity.class)
                .putExtra("PestType", (Serializable) pollList));
    }
}
