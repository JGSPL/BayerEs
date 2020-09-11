package com.procialize.eventapp.ui.attendee.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendeeRepository {
    private static AttendeeRepository attendeeRepository;
    MutableLiveData<FetchAttendee> fetchattendeeList = new MutableLiveData<>();
    
    public static AttendeeRepository getInstance() {
        if (attendeeRepository == null) {
            attendeeRepository = new AttendeeRepository();
        }
        return attendeeRepository;
    }

    private APIService eventApi;

    public AttendeeRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchAttendee> getAttendeeList(String token ,String organizer_id, String search_text) {//, String pageSize, String pageNumber) {
        eventApi.AttendeeList(token,organizer_id,
                search_text)
                .enqueue(new Callback<FetchAttendee>() {
                    @Override
                    public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {
                        if (response.isSuccessful()) {
                            fetchattendeeList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAttendee> call, Throwable t) {
                        fetchattendeeList.setValue(null);
                    }
                });

        return fetchattendeeList;
    }

}
