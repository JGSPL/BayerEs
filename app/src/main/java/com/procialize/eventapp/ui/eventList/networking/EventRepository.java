package com.procialize.eventapp.ui.eventList.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {
    private static EventRepository eventRepository;
    MutableLiveData<Event> eventList = new MutableLiveData<>();
    public static EventRepository getInstance() {
        if (eventRepository == null) {
            eventRepository = new EventRepository();
        }
        return eventRepository;
    }

    private APIService eventApi;

    public EventRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<Event> getEventList(String organizer_id, String search_text) {//, String pageSize, String pageNumber) {
        eventApi.getEventList(organizer_id,
                search_text)
                .enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (response.isSuccessful()) {
                            eventList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        eventList.setValue(null);
                    }
                });

        return eventList;
    }
}
