package com.procialize.bayer2020.ui.notification.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.ui.notification.model.Notification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    MutableLiveData<Notification> likeList = new MutableLiveData<>();
    private static NotificationRepository likeRepository;

    MutableLiveData<LoginOrganizer> submitNotificationData = new MutableLiveData<>();
    public static NotificationRepository getInstance() {
        if (likeRepository == null) {
            likeRepository = new NotificationRepository();
        }
        return likeRepository;
    }

    private APIService notificationApi;

    public NotificationRepository() {
        notificationApi = ApiUtils.getAPIService();
    }


    public MutableLiveData<Notification> getNotificationList(String token, String event_id,  String pageSize, String pageNumber) {
        notificationApi.FetchNotification(token,event_id,
                pageSize,
                pageNumber)
                .enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        if (response.isSuccessful()) {
                            likeList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        likeList.setValue(null);
                    }
                });

        return likeList;
    }

    public MutableLiveData<LoginOrganizer> sendNotification(String token, String event_id, String schedule_time,String question) {
        notificationApi.SendNotification(token, event_id,schedule_time, question)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            submitNotificationData.setValue(response.body());
                        } else {
                            submitNotificationData.setValue(null);
                            Log.e("Error","error");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        submitNotificationData.setValue(null);
                    }
                });
        return submitNotificationData;
    }

}
