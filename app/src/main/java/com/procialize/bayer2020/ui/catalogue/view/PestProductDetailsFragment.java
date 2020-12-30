package com.procialize.bayer2020.ui.catalogue.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestDetail;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;


public class PestProductDetailsFragment extends Fragment {


    View rootView;
    APIService eventApi;
    private FragmentTabHost mTabHostCel;
    ConnectionDetector cd;
    String token, eventid, imageurl, pestId = "1";
    LinearLayout linMain;

    public PestProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pest_product_details, container, false);
        cd = ConnectionDetector.getInstance(getContext());

        linMain = rootView.findViewById(R.id.linMain);
        token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getContext(), EVENT_ID);
        getDataFromApi(token, eventid);
        return rootView;
    }

    public void getDataFromApi(String token, String eventid) {

        eventApi = ApiUtils.getAPIService();

        eventApi.PestDetails(token, "1"/*eventid*/, pestId)
                .enqueue(new Callback<FetchPestDetail>() {
                    @Override
                    public void onResponse(Call<FetchPestDetail> call, Response<FetchPestDetail> response) {
                        if (response.isSuccessful()) {

                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(getContext());
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Pest_item> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Pest_item>>() {
                            }.getType());

                            //Fetch Livepoll list
                            if (eventLists != null) {
                                if (eventLists.size() > 0) {
                                    Utility.createShortSnackBar(linMain, "eventLists");
                                } else {
                                    Utility.createShortSnackBar(linMain, "Failure11");
                                }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTabHostCel = (FragmentTabHost) getActivity().findViewById(R.id.tabhost);
        mTabHostCel.setup(getContext(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab1")
                        .setIndicator(createTabView(getActivity(), "Details")),
                ProductDetailsFragment.class, null);
        mTabHostCel.addTab(
                mTabHostCel.newTabSpec("Tab2")
                        .setIndicator(createTabView(getActivity(), "Recommended products")),
                RecommendedProductFragment.class, null);
    }
}