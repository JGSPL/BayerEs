package com.procialize.eventapp.Constants;


import com.procialize.eventapp.ui.newsFeedComment.model.GifId;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface TenorApiService {

    @POST("anonid?")
    @FormUrlEncoded
    Call<GifId> GifIdPost(@Field("key") String key);

   @POST("trending?")
    @FormUrlEncoded
    Call<GifResponse> Result(@Field("key") String key,
                             @Field("anon_id") String anon_id);


     @POST("search?")
    @FormUrlEncoded
    Call<GifResponse> search(@Field("q") String query,
                             @Field("key") String key,
                             @Field("anon_id") String anon_id);
}
