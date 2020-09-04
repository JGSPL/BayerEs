package com.procialize.eventapp.ui.newsFeedComment.networking;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.TenorApiService;
import com.procialize.eventapp.ui.newsFeedComment.model.GifId;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GifRepository {
    private TenorApiService mAPItenorService;
    private static GifRepository gifRepository;
    MutableLiveData<GifResponse> gifData = new MutableLiveData<GifResponse>();
    MutableLiveData<GifResponse> serchGifData = new MutableLiveData<GifResponse>();
    MutableLiveData<String> anon_id  = new MutableLiveData<>();
    public static GifRepository getInstance() {
        if (gifRepository == null) {
            gifRepository = new GifRepository();
        }
        return gifRepository;
    }

    public MutableLiveData<String> getGifId(String key) {
        mAPItenorService = ApiUtils.getTenorAPIService();
        mAPItenorService.GifIdPost(key).enqueue(new Callback<GifId>() {
            @Override
            public void onResponse(Call<GifId> call, Response<GifId> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    anon_id.setValue(response.body().getAnon_id());
                } else {
                    // Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    //  emojibar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GifId> call, Throwable t) {
                anon_id.setValue(null);
                Log.d("failure","failure");
                //Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                // emojibar.setVisibility(View.GONE);
            }
        });
        return anon_id;
    }

    public MutableLiveData<GifResponse> getResult(String key, String id) {
        mAPItenorService.Result(key, id).enqueue(new Callback<GifResponse>() {
            private GifResponse aaaa;

            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    gifData.setValue(response.body());
                    //gifData.setValue(response.body().getResults());
                } else {
                    //emojibar.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                //emojibar.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
        return gifData;
    }

    public MutableLiveData<GifResponse> searchGif(String query,String key, String id) {
        mAPItenorService.search(query,key, id).enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    serchGifData.setValue(response.body());
                } else {
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
            }
        });
        return serchGifData;
    }
}