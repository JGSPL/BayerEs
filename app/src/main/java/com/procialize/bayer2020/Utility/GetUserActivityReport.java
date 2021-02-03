package com.procialize.bayer2020.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetUserActivityReport {
    public String api_access_token;
    public String event_id;
    public String event_type;
    public String page_id;
    public String page_name;

    public String object_id
;
    public String contentType;

    Context context;
    APIService mAPIService;

    public GetUserActivityReport(Context _context,
                                 String _api_access_token,
                                 String _event_id,
                                 String _file_id,
                                 String _event_type,
                                 String _page_name,
                                 String _page_id

    ) {
        context = _context;
        api_access_token = _api_access_token;
        event_id = _event_id;
        event_type = _event_type;
        page_id = _page_id;
        object_id = _file_id;
        page_name = _page_name;
        mAPIService = ApiUtils.getAPIService();
    }



    public void userActivityReport() {
        ApiUtils.getAPIService().getUserActivityReport(api_access_token,
                event_id,
                object_id,
                event_type,
                page_name,
                page_id

)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.isSuccessful()) {
                                    Log.i("hit", "post submitted to API." + response.body().toString());
                                    if (response.isSuccessful()) {
                                       // if (response.body().getStatus().equals("success")) {
                                            SharedPreferences pref = context.getSharedPreferences("Page", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.clear();
                                     //   }
                                    }
                                } else {
                                    Log.e("Message", response.message());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        //fetchSpotQnAList.setValue(null);
                    }
                });

       /* mAPIService.getUserActivityReport(api_access_token,
                event_id,
                event_type,
                page_id,
                object_id
).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("success")) {
                            SharedPreferences pref = context.getSharedPreferences("Page", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                        }
                    }
                } else {
                    Log.e("Message", response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("Error Message", t.getMessage());

            }
        });*/
        
/*RestClient.getInstance().getWebServices().getUserActivityReport(eventId, accessToken, eventType, pageId, fileId, contentType)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("success")) {
                                SharedPreferences pref = context.getSharedPreferences("Page", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                            }
                        } else {
                            Log.e("Message", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error Message", t.getMessage());
                    }
                });*/

    }

}
