package com.procialize.bayer2020.ui.notification.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.ui.notification.model.Notification;
import com.procialize.bayer2020.ui.notification.networking.NotificationRepository;


public class NotificationsViewModel extends ViewModel {

    private NotificationRepository notificationRepository = NotificationRepository.getInstance();
    MutableLiveData<Notification> notificationData = new MutableLiveData<>();

    MutableLiveData<LoginOrganizer> submitNotificationData = new MutableLiveData<>();
    public void getNotification(String token, String event_id,String pageSize, String pageNumber) {
        notificationRepository = NotificationRepository.getInstance();
        notificationData = notificationRepository.getNotificationList(token,event_id, pageSize, pageNumber);
    }

    public LiveData<Notification> getNotificationList() {
        return notificationData;
    }

    public void submitNotificationData(String token, String event_id, String schedule_time, String question) {
        notificationRepository = NotificationRepository.getInstance();
        submitNotificationData = notificationRepository.sendNotification(token, event_id,schedule_time,
                question);
    }

    public LiveData<LoginOrganizer> submitNotification() {
        return submitNotificationData;
    }
}