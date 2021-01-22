package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.adapter.UpskillAdapter;
import com.procialize.bayer2020.ui.upskill.model.UpSkill;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class UpskillFragment extends Fragment implements UpskillAdapter.UpskillListAdapterListner {

    View rootView;
    ImageView iv_banner;
    TextView tv_info;
    SwipeRefreshLayout srl_upskill;
    RecyclerView rv_upskill;
    String api_token, eventid;
    UpskillAdapter upskillAdapter;

    public static UpskillFragment newInstance() {
        return new UpskillFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upskill, container, false);

        iv_banner = rootView.findViewById(R.id.iv_banner);
        tv_info = rootView.findViewById(R.id.tv_info);
        srl_upskill = rootView.findViewById(R.id.srl_upskill);
        rv_upskill = rootView.findViewById(R.id.rv_upskill);
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);


        //api_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibGFzdF9uYW1lIjoiU2luZ2giLCJtb2JpbGUiOiI5OTg3MzE1NjgyIiwiZW1haWwiOiJwcmVldGlAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIzNjQwNDlmNzcwNzJlZGM0MWI0M2IxZmM3NjIwZGU5ODU3ZDJiOTM4IiwidXNlcl90eXBlIjoiRCIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9zdGF0dXMiOiIwIiwiZW5yb2xsbGVhcGZsYWciOiIwIiwiZXZlbnRfaWQiOiIxIiwiYWNjZXNzX3Rva2VuIjoiMzY0MDQ5Zjc3MDcyZWRjNDFiNDNiMWZjNzYyMGRlOTg1N2QyYjkzOCIsInByb2ZpbGVfcGljIjoiaHR0cHM6XC9cL2JheWVyLWVzLnMzLmFtYXpvbmF3cy5jb21cL3VwbG9hZHNcL3VzZXJcL2RlZmF1bHQucG5nIiwiaXNfZ29kIjoiMCIsInRvdGFsX2V2ZW50IjoxLCJ0aW1lIjoxNjEwOTY1NzE2LCJleHBpcnlfdGltZSI6MTYxMDk2OTMxNn0.9BsbzdnaRX8eVI9lcrUZZwTzHQhsJl-1aKg2Gaiio3o";
        getDataFromApi();
        return rootView;
    }

    private void getDataFromApi() {
        ApiUtils.getAPIService().UpskillList(api_token, eventid, "10", "1", "")
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            String strUpskillList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(getActivity());
                            String data = refreashToken.decryptedData(strUpskillList);
                            try {

                                Gson gson = new Gson();
                                UpSkill upskillLists = gson.fromJson(data, new TypeToken<UpSkill>() {
                                }.getType());
                                if (upskillLists != null) {

                                    Glide.with(getActivity())
                                            .load(upskillLists.getApp_upskill_logo())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    return false;
                                                }
                                            }).into(iv_banner);

                                    tv_info.setText(upskillLists.getApp_upskill_description());
                                    setupEventAdapter(upskillLists.getTrainingList());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        Log.e("Message", t.getMessage());
                    }
                });

    }

    public void setupEventAdapter(List<UpskillList> commentList) {
        if (commentList != null) {
            upskillAdapter = new UpskillAdapter(getActivity(), commentList, this);
            rv_upskill.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_upskill.setAdapter(upskillAdapter);
            upskillAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(UpskillList upskillList) {
        startActivity(new Intent(getActivity(),UpskillDetailsFirstActivity.class)
                .putExtra("upskill_info", (Serializable) upskillList));

    }
}