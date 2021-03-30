package com.procialize.bayer2020.ui.loyalityleap.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.login.view.LoginActivity;
import com.procialize.bayer2020.ui.loyalityleap.model.My_point;
import com.procialize.bayer2020.ui.profile.model.Pincode_item;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;


public class LoyalityLeapFragment  extends Fragment {
    ImageView imgScheame,imgredeemHistory,imgrequestRedeem,imgPuchageHistory, imgCalc;
    String api_token;
    TextView txtUnreadCount, txtMyRank,txtMyPoint;
    RelativeLayout relScheame, relMain;
    ProgressBar progressBar;
    ConnectionDetector cd;

    public static LoyalityLeapFragment newInstance() {

        return new LoyalityLeapFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_loyality_leap, container, false);
        imgScheame = root.findViewById(R.id.imgScheame);
        imgredeemHistory = root.findViewById(R.id.imgredeemHistory);
        imgrequestRedeem = root.findViewById(R.id.imgrequestRedeem);
        imgPuchageHistory = root.findViewById(R.id.imgPuchageHistory);
        cd = ConnectionDetector.getInstance(getContext());

        txtUnreadCount = root.findViewById(R.id.txtUnreadCount);
        txtMyRank = root.findViewById(R.id.txtMyRank);
        txtMyPoint = root.findViewById(R.id.txtMyPoint);
        relScheame = root.findViewById(R.id.relScheame);
        imgCalc = root.findViewById(R.id.imgCalc);
        progressBar = root.findViewById(R.id.progressBar);
        relMain = root.findViewById(R.id.relMain);
        api_token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);
        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        if (cd.isConnectingToInternet()) {
            progressBar.setVisibility(View.VISIBLE);

            getMyPoints();

        } else {
            progressBar.setVisibility(View.GONE);

            Utility.createShortSnackBar(relMain, "No internet connection");


        }


        relScheame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScheameOfferListActivity.class));

            }
        });

        imgredeemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RedemptionHistoryList.class));

            }
        });

        imgrequestRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RequestToRedeemActivity.class));

            }
        });

        imgPuchageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PurchaseHistoryActivity.class));

            }
        });

        imgCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MPointActivity.class));

            }
        });
        return root;

    }

    void getMyPoints( ) {
        progressBar.setVisibility(View.VISIBLE);
        ApiUtils.getAPIService().MyPointFetch(api_token,"1").enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    String strEventList = response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(getContext());
                    String data = refreashToken.decryptedData(strEventList);
                    //   JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                   /* ArrayList<Pincode_item> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<Pincode_item>>() {
                    }.getType());*/
                    My_point pincodeLists = new Gson().fromJson(data, new TypeToken<My_point>() {
                    }.getType());
                    if(pincodeLists!=null){
                        try {
                            int rank = Integer.parseInt(pincodeLists.getSchemeUnreadCount());
                            if(rank<10 && rank!=0){
                                txtUnreadCount.setText("0"+pincodeLists.getSchemeUnreadCount());

                            }else{
                                txtUnreadCount.setText(pincodeLists.getSchemeUnreadCount());
                            }
                            txtMyPoint.setText(pincodeLists.getMypoint());
                            txtMyRank.setText(pincodeLists.getRank());
                        }catch (Exception e){
                            Utility.createShortSnackBar(relMain, e.getMessage());

                        }
                    }


                } else {
                    if (response.body() != null) {
                    } else {
                        SessionManager.clearCurrentEvent(getContext());
                        SessionManager.logoutUser(getContext());
                        //EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                        EventAppDB.getDatabase(getContext()).newsFeedDao().deleteNewsFeed();
                        EventAppDB.getDatabase(getContext()).newsFeedDao().deleteNewsFeedMedia();
                       startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                   // Toast.makeText(getContext(), "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                try {
                  //  Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getMyPoints();

    }
}
