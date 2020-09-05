package com.procialize.eventapp.Constants;


import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.GetterSetter.validateOTP;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

     String HeaderToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8";

    @POST("event_api_call/commonLogin")
    @FormUrlEncoded
    Call<LoginOrganizer> LoginWithOrganizer(@Field("organizer_id") String organizer_id,
                                            @Field("username") String username);


    @POST("event_api_call/validateOTP")
    @FormUrlEncoded
    Call<validateOTP> validateOTP(@Field("organizer_id") String organizer_id,
                                  @Field("username") String username,
                                  @Field("otp") String otp);


    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/NewsFeedFetch")
    @FormUrlEncoded
    Call<FetchNewsfeedMultiple> NewsFeedFetchMultiple(@Field("event_id") String event_id,
                                                      @Field("pageSize") String pageSize,
                                                      @Field("pageNumber") String pageNumber);

    @Multipart
    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/PostNewsFeed")
    Call<LoginOrganizer> postNewsFeed(@Part("event_id") RequestBody description,
                                      @Part("post_content") RequestBody post_content,
                                      @Part List<MultipartBody.Part> files,
                                      @Part List<MultipartBody.Part> thumbFiles);

    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/PostComment")
    @FormUrlEncoded
    Call<LoginOrganizer> PostComment(
                                    @Field("event_id") String Event_id,
                                    @Field("news_feed_id") String news_feed_id,
                                    @Field("comment_data") String comment_data,
                                    @Field("type") String Type);

    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/CommentFetch")
    @FormUrlEncoded
    Call<Comment> getComment(@Field("event_id") String event_id,
                                    @Field("news_feed_id") String news_feed_id/*,
                                    @Field("pageSize") String pageSize,
                                    @Field("pageNumber") String pageNumber*/);

    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/PostHide")
    @FormUrlEncoded
    Call<LoginOrganizer> PostHide(@Field("event_id") String event_id,
                                                      @Field("news_feed_id") String news_feed_id);

    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/ReportPost")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportPost(@Field("event_id") String event_id,
                                  @Field("news_feed_id") String news_feed_id,
                                    @Field("content") String content);

    @Headers("authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJmaXJzdF9uYW1lIjoiUHJlZXRpIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJTaW5naCIsIm1vYmlsZSI6Ijk5ODczMTU2ODIiLCJlbWFpbCI6InByZWV0aUBwcm9jaWFsaXplLmluIiwicmVmcmVzaF90b2tlbiI6IjMwYWEyNWM1NjFhYjZhMWNiNjhlMzFkM2JiMmI0Zjk3YmNhNzJhYWIiLCJ1c2VyX3R5cGUiOiJBIiwidmVyaWZ5X290cCI6IjEiLCJwcm9maWxlX3BpYyI6IiBodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC9kZWZhdWx0LnBuZyIsImlzX2dvZCI6IjAiLCJ0aW1lIjoxNTk5Mjc5NTc3LCJleHBpcnlfdGltZSI6MTU5OTI4MzE3N30.QQI2TlxLBkL5Jot5wEH89nCGhySqtcnicJ0vKRSIyC8")
    @POST("NewsFeed_api/ReportUser")
    @FormUrlEncoded
    Call<LoginOrganizer> ReportUser(@Field("event_id") String event_id,
                                    @Field("reported_user_id") String reported_user_id,
                                    @Field("news_feed_id") String news_feed_id,
                                    @Field("content") String content);

    @Headers("authorization: "+HeaderToken)
    @POST("NewsFeed_api/PostLike")
    @FormUrlEncoded
    Call<LoginOrganizer> PostLike(@Field("event_id") String event_id,
                                    @Field("news_feed_id") String news_feed_id);


    @Headers("authorization: "+HeaderToken)
    @POST("NewsFeed_api/DeletePost")
    @FormUrlEncoded
    Call<LoginOrganizer> DeletePost(@Field("event_id") String event_id,
                                  @Field("news_feed_id") String news_feed_id);

}
