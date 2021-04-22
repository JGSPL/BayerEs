package com.procialize.bayer2020.Constants;


import com.procialize.bayer2020.GetterSetter.BaseResponse;
import com.procialize.bayer2020.GetterSetter.LoginOrgamizerToken;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.GetterSetter.resendOTP;
import com.procialize.bayer2020.GetterSetter.validateOTP;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.attendee.model.FetchAttendee;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestDetail;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestList;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestTypeList;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductDetail;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductList;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductType;
import com.procialize.bayer2020.ui.document.model.Document;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.bayer2020.ui.eventinfo.model.EventInfo;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchPurchageHistory;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistory;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistoryStatus;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRequestToRedeem;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchSchemeOffer;
import com.procialize.bayer2020.ui.loyalityleap.model.Fetchm_Point;
import com.procialize.bayer2020.ui.newsFeedComment.model.Comment;
import com.procialize.bayer2020.ui.newsFeedComment.model.LikePost;
import com.procialize.bayer2020.ui.newsFeedLike.model.Like;
import com.procialize.bayer2020.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.bayer2020.ui.notification.model.Notification;
import com.procialize.bayer2020.ui.profile.model.FetchPincode;
import com.procialize.bayer2020.ui.profile.model.Profile;
import com.procialize.bayer2020.ui.quiz.model.QuizListing;
import com.procialize.bayer2020.ui.quiz.model.QuizSubmit;
import com.procialize.bayer2020.ui.speaker.model.FetchSpeaker;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;
import com.procialize.bayer2020.ui.upskill.model.UpskillContent;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService {

//    String HeaderToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIwNTE0M2JmOTI0NzcwYTk5MTdlZjNhMWU5MjY4MGE3NTU5M2M1NDZiIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiJodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC8xNTk5NTczNjM0ODMzNC5qcGciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTcyNzQ0MiwiZXhwaXJ5X3RpbWUiOjE1OTk3MzEwNDJ9.HcJDPuJMtS_o8Q6FrzUmHWNulrPzNcAzAhodkCa9E0M";

    @POST("login_api_call/commonLogin")
    @FormUrlEncoded
    Call<LoginOrgamizerToken> LoginWithOrganizer(@Field("organizer_id") String organizer_id,
                                                 @Field("username") String username);

    @POST("login_api_call/resendOTP")
    @FormUrlEncoded
    Call<resendOTP> ResendOTP(@Field("organizer_id") String organizer_id,
                              @Field("username") String username);


    @POST("login_api_call/validateOTP")
    @FormUrlEncoded
    Call<validateOTP> validateOTP(@Field("organizer_id") String organizer_id,
                                  @Field("username") String username,
                                  @Field("otp") String otp,
                                  @Field("vToken") String vToken);

    @POST("login_api_call/enrollLeapFlag")
    @FormUrlEncoded
    Call<LoginOrganizer> enrollLeapFlag(@Header("authorization") String organizer_id,
                                  @Field("event_id") String event_id,
                                  @Field("enrollleapflag") String enrollleapflag,
                                        @Field("enroll_reason") String enroll_reason);



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
    Call<Comment> getComment(@Header("authorization") String authorization, @Field("event_id") String event_id,
                             @Field("news_feed_id") String news_feed_id,
                             @Field("pageSize") String pageSize,
                             @Field("pageNumber") String pageNumber);

    //@Headers("authorization: " + HeaderToken)
    @POST("NewsFeed_api/PostHide")
    @FormUrlEncoded
    Call<LoginOrganizer> PostHide(@Header("authorization") String authorization, @Field("event_id") String event_id,
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
    Call<LoginOrganizer> ReportComment(@Header("authorization") String authorization, @Field("event_id") String event_id,
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
    @POST("login_api_call/refreshToken")
    @FormUrlEncoded
    Call<validateOTP> getRefreashToken(@Field("organizer_id") String organizer_id,
                                       /*@Field("username") String username,
                                       @Field("otp") String otp,*/
                                       @Field("access_token") String access_token);

    @POST("event_api_call/eventList")
    @FormUrlEncoded
    Call<Event> getEventList(@Header("authorization") String auth,
                             @Field("organizer_id") String organizer_id,
                             @Field("search_text") String search_text);

    @POST("quiz_api/QuizFetch")
    @FormUrlEncoded
    Call<QuizListing> getQuizList(@Header("authorization") String auth,
                                  @Field("event_id") String event_id);

    @POST("quiz_api/QuizSubmit")
    @FormUrlEncoded
    Call<QuizSubmit> submitQuiz(@Header("authorization") String auth,
                                @Field("event_id") String event_id,
                                @Field("quiz_id") String quiz_id,
                                @Field("quiz_options_id") String quiz_options_id);

    @POST("Quiz_api/SpotQuizSubmit")
    @FormUrlEncoded
    Call<QuizSubmit> submitSpotQuiz(@Header("authorization") String auth,
                                    @Field("event_id") String event_id,
                                    @Field("quiz_id") String quiz_id,
                                    @Field("quiz_options_id") String quiz_options_id);

    @POST("event_api_call/updateDeviceInfo")
    @FormUrlEncoded
    Call<UpdateDeviceInfo> updateDeviceInfo(@Header("authorization") String authorization,
                                            @Field("event_id") String event_id,
                                            @Field("device_token") String device_token,
                                            @Field("platform") String platform,
                                            @Field("device") String device,
                                            @Field("os_version") String os_version,
                                            @Field("app_version") String app_version);

    @POST("login_api_call/getProfileInfo")
    @FormUrlEncoded
    Call<Profile> getProfile(@Header("authorization") String authorization, @Field("event_id") String event_id);


    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("login_api_call/updateProfile")
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
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("designation") RequestBody designation,
                                @Part("city") RequestBody city,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("company_name") RequestBody company_name);

    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("associated_since") RequestBody associated_since,
                                @Part("no_of_pco_served") RequestBody no_of_pco_served,
                                @Part MultipartBody.Part filename);

    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("associated_since") RequestBody associated_since,
                                @Part("no_of_pco_served") RequestBody no_of_pco_served
                               );

    @Multipart
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("designation") RequestBody designation,
                                @Part("city") RequestBody city,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("alternate_no") RequestBody alternate_no,
                                @Part("alternate_no_2") RequestBody alternate_no_2,
                                @Part("alternate_no_3") RequestBody alternate_no_3,
                                @Part("company_name") RequestBody company_name,
                                @Part("state") RequestBody state,
                                @Part("no_of_technician") RequestBody no_of_technician,
                                @Part("specialization") RequestBody specialization,
                                @Part("turnover") RequestBody turnover,
                                @Part("pincode") RequestBody pincode
                               );
    @Multipart
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("designation") RequestBody designation,
                                @Part("city") RequestBody city,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("alternate_no") RequestBody alternate_no,
                                @Part("alternate_no_2") RequestBody alternate_no_2,
                                @Part("alternate_no_3") RequestBody alternate_no_3,
                                @Part("company_name") RequestBody company_name,
                                @Part("state") RequestBody state,
                                @Part("no_of_technician") RequestBody no_of_technician,
                                @Part("specialization") RequestBody specialization,
                                @Part("turnover") RequestBody turnover,
                                @Part("pincode") RequestBody pincode,
                                @Part MultipartBody.Part filename);


    @Multipart
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("pincode") RequestBody pincode,
                                @Part("city") RequestBody city,
                                @Part("state") RequestBody state);

    @Multipart
    @POST("login_api_call/updateProfile")
    Call<Profile> updateProfile(@Header("authorization") String authorization,
                                @Part("event_id") RequestBody event_id,
                                @Part("user_type") RequestBody user_type,
                                @Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("email") RequestBody email,
                                @Part("mobile") RequestBody mobile,
                                @Part("pincode") RequestBody pincode,
                                @Part("city") RequestBody city,
                                @Part("state") RequestBody state,
                                @Part MultipartBody.Part filename);

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
                                            @Field("firebase_username") String pageNumber,
                                            @Field("firebase_status") String firebase_status);

    @POST("Attendee_api/UpdateChatUserInfo")
    @FormUrlEncoded
    Call<LoginOrganizer> UpdateChatUserInfo(@Header("authorization") String auth,
                                            @Field("event_id") String organizer_id,
                                            @Field("firebase_id") String search_text,
                                            @Field("firebase_name") String firebase_name,
                                            @Field("firebase_username") String pageNumber);

    @POST("Attendee_api/UpdateChatStatus")
    @FormUrlEncoded
    Call<LoginOrganizer> UpdateChatStatus(@Header("authorization") String auth,
                                          @Field("event_id") String organizer_id,
                                          @Field("receiver_id") String search_text);

    @POST("Speaker_api/SpeakerList")
    @FormUrlEncoded
    Call<FetchSpeaker> SpeakerList(@Header("authorization") String auth,
                                   @Field("event_id") String organizer_id,
                                   @Field("search_text") String search_text);

    @POST("Speaker_api/SpeakerDetailFetch")
    @FormUrlEncoded
    Call<FetchSpeaker> SpeakerDetailFetch(@Header("authorization") String auth,
                                          @Field("event_id") String organizer_id,
                                          @Field("speaker_id") String speaker_id);

    @POST("Speaker_api/RateSpeaker")
    @FormUrlEncoded
    Call<LoginOrganizer> RateSpeaker(@Header("authorization") String auth,
                                     @Field("event_id") String organizer_id,
                                     @Field("target_id") String target_id,
                                     @Field("rating") String rating);

    @POST("event_api_call/eventInfoFetch")
    @FormUrlEncoded
    Call<EventInfo> eventInfoFetch(@Header("authorization") String auth,
                                   @Field("event_id") String event_id);

    @POST("Agenda_api/AgendaSeprateFetch")
    @FormUrlEncoded
    Call<FetchAgenda> agendaFetch(@Header("authorization") String auth,
                                  @Field("event_id") String event_id);

    @POST("Livepoll_api/LivePollFetch")
    @FormUrlEncoded
    Call<FetchLivePoll> livePollFetch(@Header("authorization") String authkey,
                                      @Field("event_id") String event_id);

    @POST("Livepoll_api/SpotLivePollFetch")
    @FormUrlEncoded
    Call<FetchLivePoll> SpotLivePollFetch(@Header("authorization") String authkey,
                                          @Field("event_id") String event_id,
                                          @Field("session_id") String session_id);

    @POST("Quiz_api/SpotQuizFetch")
    @FormUrlEncoded
    Call<QuizListing> SpotQuizFetch(@Header("authorization") String authkey,
                                    @Field("event_id") String event_id,
                                    @Field("session_id") String session_id);

    @POST("Livepoll_api/SpotLivePollSubmit")
    @FormUrlEncoded
    Call<LoginOrganizer> SpotLivePollSubmit(@Header("authorization") String authkey,
                                            @Field("event_id") String event_id,
                                            @Field("live_poll_id") String live_poll_id,
                                            @Field("live_poll_options_id") String live_poll_options_id);

    @POST("Agenda_api/RateSession")
    @FormUrlEncoded
    Call<LoginOrganizer> rateAgendaApi(@Header("authorization") String authorization,
                                       @Field("event_id") String Event_id,
                                       @Field("target_id") String target_id,
                                       @Field("rating") String rating);

    @POST("Agenda_api/ReminderSession")
    @FormUrlEncoded
    Call<LoginOrganizer> setReminderAgenda(@Header("authorization") String authorization,
                                           @Field("event_id") String Event_id,
                                           @Field("session_id") String session_id,
                                           @Field("reminder_flag") String reminder_flag,
                                           @Field("reminder_id") String reminder_id);


    @POST("Livepoll_api/LivePollSubmit")
    @FormUrlEncoded
    Call<FetchLivePoll> LivePollSubmit(@Header("authorization") String authkey,
                                       @Field("event_id") String event_id,
                                       @Field("live_poll_id") String live_poll_id,
                                       @Field("live_poll_options_id") String live_poll_options_id);

    @POST("QA_api/QASessionFetch")
    @FormUrlEncoded
    Call<FetchSpotQnA> spotQnAFetch(@Header("authorization") String auth,
                                    @Field("event_id") String event_id,
                                    @Field("session_id") String session_id,
                                    @Field("pageSize") String pageSize,
                                    @Field("pageNumber") String pageNumber
    );

    @POST("QA_api/QASessionLike")
    @FormUrlEncoded
    Call<LikePost> PostQnALikeSession(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                      @Field("question_id") String question_id);

    @POST("QA_api/QASessionPost")
    @FormUrlEncoded
    Call<LoginOrganizer> PostQnASession(@Header("authorization") String authorization, @Field("event_id") String event_id
            , @Field("session_id") String session_id, @Field("question") String question);

    //Bayer Es Api List
    @POST("Catalogue_api/ProductTypeList")
    @FormUrlEncoded
    Call<FetchProductType> ProductTypeList(@Header("authorization") String authkey,
                                           @Field("event_id") String event_id,
                                           @Field("search_text") String search_text,
                                           @Field("pageNumber") String pageNumber,
                                           @Field("pageSize") String pageSize);

    @POST("Catalogue_api/ProductList")
    @FormUrlEncoded
    Call<FetchProductList> ProductList(@Header("authorization") String authkey,
                                       @Field("event_id") String event_id,
                                       @Field("product_type_id") String product_type_id,
                                       @Field("search_text") String search_text,
                                       @Field("pageNumber") String pageNumber,
                                       @Field("pageSize") String pageSize);

    @POST("Catalogue_api/ProductDetails")
    @FormUrlEncoded
    Call<FetchProductDetail> ProductDetails(@Header("authorization") String authkey,
                                            @Field("event_id") String event_id,
                                            @Field("product_id") String search_text);

    @POST("Catalogue_api/PestTypeList")
    @FormUrlEncoded
    Call<FetchPestList> PestTypeList(@Header("authorization") String authkey,
                                     @Field("event_id") String event_id,
                                     @Field("search_text") String search_text,
                                     @Field("pageNumber") String pageNumber,
                                     @Field("pageSize") String pageSize);

    @POST("Catalogue_api/PestList")
    @FormUrlEncoded
    Call<FetchPestTypeList> PestList(@Header("authorization") String authkey,
                                     @Field("event_id") String event_id,
                                     @Field("pest_type_id") String product_type_id,
                                     @Field("search_text") String search_text,
                                     @Field("pageNumber") String pageNumber,
                                     @Field("pageSize") String pageSize);


    @POST("Catalogue_api/PestDetails")
    @FormUrlEncoded
    Call<FetchPestDetail> PestDetails(@Header("authorization") String authkey,
                                      @Field("event_id") String event_id,
                                      @Field("pest_id") String search_text);

    //Loyality Leap
    @POST("Loyalty_api/MyPointFetch")
    @FormUrlEncoded
    Call<FetchAgenda> MyPointFetch(@Header("authorization") String authkey,
                                              @Field("event_id") String event_id);

    @POST("Loyalty_api/SchemeAndOfferRead")
    @FormUrlEncoded
    Call<LoginOrganizer> SchemeAndOfferRead(@Header("authorization") String authkey,
                                              @Field("event_id") String event_id,
                                              @Field("scheme_id") String scheme_id);


    @POST("Loyalty_api/SchemeAndOfferList")
    @FormUrlEncoded
    Call<FetchSchemeOffer> SchemeAndOfferList(@Header("authorization") String authkey,
                                              @Field("event_id") String event_id,
                                              @Field("search_text") String search_text,
                                              @Field("pageNumber") String pageNumber,
                                              @Field("pageSize") String pageSize);

    @POST("Loyalty_api/RedemptionHistory")
    @FormUrlEncoded
    Call<FetchRedeemHistory> RedemptionHistory(@Header("authorization") String authkey,
                                               @Field("event_id") String event_id,
                                               @Field("search_text") String search_text,
                                               @Field("pageNumber") String pageNumber,
                                               @Field("pageSize") String pageSize);

    @POST("Loyalty_api/RedemptionHistoryDetails")
    @FormUrlEncoded
    Call<FetchRedeemHistoryStatus> RedemptionHistoryDetails(@Header("authorization") String authkey,
                                                            @Field("event_id") String event_id,
                                                            @Field("redemption_request_id") String search_text);


    @POST("Loyalty_api/RedeemRequestList")
    @FormUrlEncoded
    Call<FetchRequestToRedeem> RedeemRequestList(@Header("authorization") String authkey,
                                                 @Field("event_id") String event_id,
                                                 @Field("search_text") String search_text,
                                                 @Field("pageNumber") String pageNumber,
                                                 @Field("pageSize") String pageSize);

    @POST("Loyalty_api/RedeemRequest")
    @FormUrlEncoded
    Call<LoginOrganizer> RedeemRequest(@Header("authorization") String authkey,
                                       @Field("event_id") String event_id,
                                       @Field("product_code") String product_code,
                                       @Field("product_value") String product_name,
                                       @Field("qty") String no_of_quantity,
                                       @Field("email") String email,
                                       @Field("address") String address);

    @POST("Loyalty_api/PurchaseHistoryFetch")
    @FormUrlEncoded
    Call<FetchPurchageHistory> PurchaseHistoryFetch(@Header("authorization") String authkey,
                                                    @Field("event_id") String event_id);
    @POST("Loyalty_api/MpointFetch")
    @FormUrlEncoded
    Call<Fetchm_Point> MpointFetch(@Header("authorization") String authkey,
                                   @Field("event_id") String event_id);

    @POST("training/UpskillList")
    @FormUrlEncoded
    Call<FetchAgenda> UpskillList(@Header("authorization") String authkey,
                                           @Field("event_id") String event_id,
                                           @Field("pageSize") String pageSize,
                                           @Field("pageNumber") String pageNumber,
                                           @Field("search_text") String search_text);
    @GET("Event_api_call/Eula")
    Call<FetchAgenda> getEula();

    @GET("Event_api_call/PrivacyPolicy")
    Call<FetchAgenda> getPrivacyPolicy();

    @POST("Event_api_call/ContactUs")
    @FormUrlEncoded
    Call<FetchAgenda> getContactUs(@Header("authorization") String auth,
                                   @Field("event_id") String event_id);

    @POST("Event_api_call/SurveyList")
    @FormUrlEncoded
    Call<FetchAgenda> getSurvey(@Header("authorization") String auth,
                                @Field("event_id") String event_id,
                                @Field("pageSize") String pageSize,
                                @Field("pageNumber") String pageNumber);

    @POST("training/getUpskillContent")
    @FormUrlEncoded
    Call<UpskillContent> UpskillContent(@Header("authorization") String authkey,
                                        @Field("event_id") String event_id,
                                        @Field("training_id") String training_id);


    @POST("download_api/DocumentsFetch")
    @FormUrlEncoded
    Call<Document> getDocumentList(@Header("authorization") String auth,
                                   @Field("event_id") String event_id,
                                   @Field("pageSize") String pageSize,
                                   @Field("pageNumber") String pageNumber);

    @POST("login_api_call/PincodeList")
    @FormUrlEncoded
    Call<FetchPincode> PincodeList(@Header("authorization") String auth,
                                   @Field("event_id") String event_id,
                                   @Field("search_text") String search_text);

    @POST("login_api_call/CityState")
    @FormUrlEncoded
    Call<FetchPincode> CityState(@Header("authorization") String auth,
                                 @Field("event_id") String event_id,
                                 @Field("pincode") String pincode);

    @POST("event_api_call/FAQFetch")
    @FormUrlEncoded
    Call<FetchAgenda> FAQFetch(@Header("authorization") String auth,
                                 @Field("event_id") String event_id);

    @POST("training/add_training_analytics")
    @FormUrlEncoded
    Call<FetchAgenda> AddTrainingAnalytics(@Header("authorization") String auth,
                                           @Field("event_id") String event_id,
                                           @Field("training_id") String training_id,
                                           @Field("type") String type,
                                           @Field("post_id") String post_id,
                                           @Field("last_submit") String last_submit);

    @POST("training/LivePollSubmit")
    @FormUrlEncoded
    Call<FetchAgenda> UpskillLivePollSubmit(@Header("authorization") String auth,
                                            @Field("event_id") String event_id,
                                            @Field("live_poll_id") String live_poll_id,
                                            @Field("live_poll_options_id") String live_poll_options_id);


    @POST("training/training_quiz_reply")
    @FormUrlEncoded
    Call<QuizSubmit> TrainingQuizReply(@Header("authorization") String auth,
                                       @Field("event_id") String event_id,
                                       @Field("quiz_id") String quiz_id,
                                       @Field("quiz_options_id") String quiz_options_id,
                                       @Field("folder_id") String folder_id);


    @POST("QA_api/NewsFeedFetch")
    @FormUrlEncoded
    Call<FetchSpotQnA> directQnAFetch(@Header("authorization") String auth,
                                      @Field("event_id") String event_id,
                                      @Field("pageSize") String pageSize,
                                      @Field("pageNumber") String pageNumber
    );


    @Multipart
    //@Headers("authorization: " + HeaderToken)
    @POST("QA_api/PostNewsFeed")
    Call<LoginOrganizer> postNewsFeed1(@Header("authorization") String authorization,
                                       @Part("event_id") RequestBody event_id,
                                       @Part("post_content") RequestBody post_content,
                                       @Part List<MultipartBody.Part> files,
                                       @Part List<MultipartBody.Part> thumbFiles);


    @POST("QA_api/QADirectLike")
    @FormUrlEncoded
    Call<LikePost> PostQnALikeDirect(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                     @Field("question_id") String question_id);

    @POST("QA_api/QADirectPost")
    @FormUrlEncoded
    Call<LoginOrganizer> PostQnADirect(@Header("authorization") String authorization, @Field("event_id") String event_id
            , @Field("question") String question);

    @POST("Notification_api/NotificationFetch")
    @FormUrlEncoded
    Call<Notification> FetchNotification(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                         @Field("pageSize") String pageSize, @Field("pageNumber") String pageNumber);

    @POST("Notification_api/SendNotification")
    @FormUrlEncoded
    Call<LoginOrganizer> SendNotification(@Header("authorization") String authorization, @Field("event_id") String event_id,
                                          @Field("schedule_time") String Schedule_time, @Field("content") String content);

    @POST("store_locator_api/DistributorCountFetch")
    @FormUrlEncoded
    Call<FetchAgenda> DistributorCountFetch(@Header("authorization") String authorization,
                                               @Field("event_id") String event_id);

    @POST("store_locator_api/DistributorListFetch")
    @FormUrlEncoded
    Call<FetchAgenda> DistributorListFetch(@Header("authorization") String authorization,
                                               @Field("event_id") String event_id,
                                              @Field("state") String state);

    @POST("analytics_api/all_analytics")
    @FormUrlEncoded
    Call<LoginOrganizer> getUserActivityReport(@Header("authorization") String api_access_token,
                                             @Field("event_id") String event_id,
                                             @Field("object_id") String file_id,
                                             @Field("activity_type") String event_type,
                                             @Field("page_name") String page_name,
                                             @Field("page_id") String page_id);

    @POST("event_api_call/logout")
    @FormUrlEncoded
    Call<LoginOrganizer> logout(@Header("authorization") String api_access_token,
                                @Field("event_id") String event_id);
}