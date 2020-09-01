package com.procialize.eventapp.Constants;



import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.validateOTP;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("event_api_call/commonLogin")
    @FormUrlEncoded
    Call<LoginOrganizer> LoginWithOrganizer(@Field("organizer_id") String organizer_id,
                                            @Field("username") String username);


    @POST("event_api_call/validateOTP")
    @FormUrlEncoded
    Call<validateOTP> validateOTP(@Field("organizer_id") String organizer_id,
                                  @Field("username") String username,
                                  @Field("otp") String otp);
}
