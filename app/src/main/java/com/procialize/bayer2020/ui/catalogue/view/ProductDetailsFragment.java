package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.model.CataloguePestDetails;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestDetail;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {

    View rootView;
    private Pest_item pest_item;
    TextView tv_details;
    APIService eventApi;
    String token, eventid, imageurl, pestId = "1";
LinearLayout linMain;
    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    public static ProductDetailsFragment newInstance(String param1, String param2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_product_details, container, false);


        pest_item = (Pest_item) getArguments().getSerializable("PestType");
        token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

        tv_details = rootView.findViewById(R.id.tv_details);
        linMain = rootView.findViewById(R.id.linMain);
        tv_details.setText(pest_item.getPest_long_description());

        getDataFromApi(token,eventid);
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
                            RefreashToken refreashToken = new RefreashToken(getActivity());
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            CataloguePestDetails eventLists = gson.fromJson(data, new TypeToken<CataloguePestDetails>() {
                            }.getType());

                            //Fetch Livepoll list
                            if (eventLists != null) {
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

}