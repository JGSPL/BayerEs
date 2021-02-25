package com.procialize.bayer2020.ui.catalogue.view;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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
import com.procialize.bayer2020.ui.catalogue.model.PestTypeItem;
import com.procialize.bayer2020.ui.catalogue.model.Pest_item;

import org.jsoup.Jsoup;

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
    private PestTypeItem pest_item;
    TextView tv_details;
    APIService eventApi;
    String token, eventid, imageurl, pestId = "1";
    LinearLayout linMain;
    String spannedString;
    String postStatus;
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


        pest_item = (PestTypeItem) getArguments().getSerializable("PestType");
        token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);

        tv_details = rootView.findViewById(R.id.tv_details);
        linMain = rootView.findViewById(R.id.linMain);
       // tv_details.setText(pest_item.getProduct_long_description());
        if (pest_item.getProduct_long_description().contains("\n")) {
            postStatus =pest_item.getProduct_long_description().trim().replace("\n", "<br/>");
        } else {
            postStatus = pest_item.getProduct_long_description().trim();
        }
        spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
            tv_details.setText(Utility.trimTrailingWhitespace(strPost));
        } else {
            Spanned strPost = Html.fromHtml(spannedString);
            tv_details.setText(Utility.trimTrailingWhitespace(strPost));
        }
        getDataFromApi(token, eventid);
        return rootView;
    }

    public void getDataFromApi(String token, String eventid) {

        eventApi = ApiUtils.getAPIService();

        eventApi.PestDetails(token, eventid, pestId)
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