package com.procialize.eventapp.Constants;


import com.google.gson.JsonObject;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.resendOTP;
import com.procialize.eventapp.GetterSetter.validateOTP;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedLike.model.Like;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.profile.model.Profile;
import com.procialize.eventapp.ui.splash.view.SplashAcivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService{

    String HeaderToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIwNTE0M2JmOTI0NzcwYTk5MTdlZjNhMWU5MjY4MGE3NTU5M2M1NDZiIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiJodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC8xNTk5NTczNjM0ODMzNC5qcGciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTcyNzQ0MiwiZXhwaXJ5X3RpbWUiOjE1OTk3MzEwNDJ9.HcJDPuJMtS_o8Q6FrzUmHWNulrPzNcAzAhodkCa9E0M";

    @POST("event_api_call/commonLogin")
    @FormUrlEncoded
    Call<LoginOrganizer> LoginWithOrganizer(@Field("organizer_id") String organizer_id,
                                            @Field("username") String username);

    @POST("event_api_call/resendOTP")
    @FormUrlEncoded
    Call<resendOTP> ResendOTP(@Field("organizer_id") String organizer_id,
                              @Field("username") String username);


    @POST("event_api_call/validateOTP")
    @FormUrlEncoded
    Call<validateOTP> validateOTP(@Field("organizer_id") String organizer_id,
                                  @Field("username") String username,
                                  @Field("otp") String otp);


    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/NewsFeedFetch")
    @FormUrlEncoded
    Call<FetchNewsfeedMultiple> NewsFeedFetchMultiple(@Header("authorization") String auth,
                                                      @Field("event_id") String event_id,
                                                      @Field("pageSize") String pageSize,
                                                      @Field("pageNumber") String pageNumber);

   // @Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/NewsFeedDetailFetch")
    @FormUrlEncoded
    Call<FetchNewsfeedMultiple> NewsFeedDetailFetch(@Header("authorization") String authorization,
                                                    @Field("event_id") String event_id,
                                                    @Field("news_feed_id") String news_feed_id);

    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostNewsFeed")
    Call<LoginOrganizer> postNewsFeed(@Header("authorization") String authorization,
                                      @Part("event_id") RequestBody event_id,
                                      @Part("post_content") RequestBody post_content,
                                      @Part List<MultipartBody.Part> files,
                                      @Part List<MultipartBody.Part> thumbFiles);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostComment")
    @FormUrlEncoded
    Call<LoginOrganizer> PostComment(@Header("authorization") String authorization,
            @Field("event_id") String Event_id,
            @Field("news_feed_id") String news_feed_id,
            @Field("comment_data") String comment_data,
            @Field("type") String Type);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/DeleteComment")
    @FormUrlEncoded
    Call<LoginOrganizer> DeleteComment(@Header("authorization") String authorization,
            @Field("event_id") String event_id,
            @Field("comment_id") String comment_id);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/CommentHide")
    @FormUrlEncoded
    Call<LoginOrganizer> CommentHide(@Header("authorization") String authorization,
            @Field("event_id") String event_id,
            @Field("comment_id") String comment_id);

   // @Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/CommentFetch")
    @FormUrlEncoded
    Call<Comment> getComment(@Header("authorization") String authorization,@Field("event_id") String event_id,
                             @Field("news_feed_id") String news_feed_id/*,
                                    @Field("pageSize") String pageSize,
                                    @Field("pageNumber") String pageNumber*/);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostHide")
    @FormUrlEncoded
    Call<LoginOrganizer> PostHide(@Header("authorization") String authorization,@Field("event_id") String event_id,
                                  @Field("news_feed_id") String news_feed_id);

   // @Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/ReportPost")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportPost(@Header("authorization") String authorization,
                                    @Field("event_id") String event_id,
                                    @Field("news_feed_id") String news_feed_id,
                                    @Field("content") String content);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/ReportUser")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportUser(@Header("authorization") String authorization,
                                    @Field("event_id") String event_id,
                                    @Field("reported_user_id") String reported_user_id,
                                    @Field("news_feed_id") String news_feed_id,
                                    @Field("content") String content);
    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/ReportCommentUser")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportCommentUser(@Header("authorization") String authorization,
                                    @Field("event_id") String event_id,
                                    @Field("reported_user_id") String reported_user_id,
                                    @Field("comment_id") String comment_id,
                                    @Field("content") String content);


    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/ReportComment")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportComment(@Header("authorization") String authorization,@Field("event_id") String event_id,
                                       @Field("comment_id") String comment_id,
                                       @Field("content") String content);

   // @Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostLike")
    @FormUrlEncoded
    Call<LikePost> PostLike(@Header("authorization") String authorization, @Field("event_id") String event_id,
                            @Field("news_feed_id") String news_feed_id);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostLike")
    @FormUrlEncoded
    Call<LikePost> PostLikeFromComment(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                       @Field("news_feed_id") String news_feed_id);


    // @Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/DeletePost")
    @FormUrlEncoded
    Call<LoginOrganizer> DeletePost(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                    @Field("news_feed_id") String news_feed_id);


    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/LikeFetch")
    @FormUrlEncoded
    Call<Like> getLikes(@Header("authorization") String authorization,
                        @Field("event_id") String event_id,
                        @Field("news_feed_id") String news_feed_id,
                        @Field("pageSize") String pageSize,
                        @Field("pageNumber") String pageNumber);

    // @Headers("authorization: " + HeaderToken)
    @POST("event_api_call/refreshToken")
    @FormUrlEncoded
    Call<validateOTP> getRefreashToken(@Field("organizer_id") String organizer_id,
                                    @Field("username") String username,
                                    @Field("otp") String otp,
                                    @Field("access_token") String access_token);

    @POST("event_api_call/eventList")
    @FormUrlEncoded
    Call<Event> getEventList(@Header("authorization") String auth,
                             @Field("organizer_id") String organizer_id,
                             @Field("search_text") String search_text);

    // @Headers("authorization: " + HeaderToken)
    @POST("event_api_call/updateDeviceInfo")
    @FormUrlEncoded
    Call<UpdateDeviceInfo> updateDeviceInfo(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                            @Field("device_token") String device_token,
                                            @Field("platform") String platform,
                                            @Field("device") String device,
                                            @Field("os_version") String os_version,
                                            @Field("app_version") String app_version);

    // @Headers("authorization: " + HeaderToken)
    @POST("event_api_call/getProfileInfo")
    @FormUrlEncoded
    Call<Profile> getProfile(@Header("authorization") String authorization, @Field("event_id") String event_id);


    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("event_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("designation") RequestBody designation,
                                @Part("city") RequestBody city,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("company_name") RequestBody company_name,
                                @Part MultipartBody.Part filename);

    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("event_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("designation") RequestBody designation,
                                @Part("city") RequestBody city,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("company_name") RequestBody company_name);

    //Attendee Api
    // @Headers("authorization: " + HeaderToken)
    @POST("Attendee_api/AttendeeList")
    @FormUrlEncoded
    Call<FetchAttendee> AttendeeList(@Header("authorization") String auth,
                                     @Field("event_id") String organizer_id,
                                     @Field("search_text") String search_text,
                                     @Field("pageNumber") String pageNumber,
                                     @Field("pageSize") String pageSize);


 @POST("Attendee_api/UpdateChatUserInfo")
 @FormUrlEncoded
 Call<LoginOrganizer> UpdateChatUserInfo(@Header("authorization") String auth,
                                  @Field("event_id") String organizer_id,
                                  @Field("firebase_id") String search_text,
                                         @Field("firebase_name") String firebase_name,
                                  @Field("firebase_username") String pageNumber);

}