package com.procialize.bayer2020.Constants;


import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.GetterSetter.resendOTP;
import com.procialize.bayer2020.GetterSetter.validateOTP;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.attendee.model.FetchAttendee;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestDetail;
import com.procialize.bayer2020.ui.catalogue.model.FetchPestList;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductList;
import com.procialize.bayer2020.ui.catalogue.model.FetchProductType;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.bayer2020.ui.eventinfo.model.EventInfo;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistory;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemHistoryStatus;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRequestToRedeem;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchSchemeOffer;
import com.procialize.bayer2020.ui.newsFeedComment.model.Comment;
import com.procialize.bayer2020.ui.newsFeedComment.model.LikePost;
import com.procialize.bayer2020.ui.newsFeedLike.model.Like;
import com.procialize.bayer2020.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.bayer2020.ui.profile.model.Profile;
import com.procialize.bayer2020.ui.quiz.model.QuizListing;
import com.procialize.bayer2020.ui.quiz.model.QuizSubmit;
import com.procialize.bayer2020.ui.speaker.model.FetchSpeaker;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface APIService {

//    String HeaderToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIwNTE0M2JmOTI0NzcwYTk5MTdlZjNhMWU5MjY4MGE3NTU5M2M1NDZiIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiJodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC8xNTk5NTczNjM0ODMzNC5qcGciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTcyNzQ0MiwiZXhwaXJ5X3RpbWUiOjE1OTk3MzEwNDJ9.HcJDPuJMtS_o8Q6FrzUmHWNulrPzNcAzAhodkCa9E0M";

    @POST("login_api_call/commonLogin")
    @FormUrlEncoded
    Call<LoginOrganizer> LoginWithOrganizer(@Field("organizer_id") String organizer_id,
                                            @Field("username") String username);

    @POST("login_api_call/resendOTP")
    @FormUrlEncoded
    Call<resendOTP> ResendOTP(@Field("organizer_id") String organizer_id,
                              @Field("username") String username);


    @POST("login_api_call/validateOTP")
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
                               ,@Field("session_id") String session_id,@Field("question") String question);

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

    @POST("Catalogue_api/PestTypeList")
    @FormUrlEncoded
    Call<FetchPestList> PestTypeList(@Header("authorization") String authkey,
                                 @Field("event_id") String event_id,
                                     @Field("search_text") String search_text,
                                 @Field("pageNumber") String pageNumber,
                                 @Field("pageSize") String pageSize);

    @POST("Catalogue_api/PestList")
    @FormUrlEncoded
    Call<FetchPestList> PestList(@Header("authorization") String authkey,
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
}