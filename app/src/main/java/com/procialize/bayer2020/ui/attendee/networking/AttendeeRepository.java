package com.procialize.bayer2020.ui.attendee.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.ui.attendee.model.FetchAttendee;

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

    public MutableLiveData<FetchAttendee> getAttendeeList(String token , String organizer_id, String search_text, String pagenumber, String pageSizr) {//, String pageSize, String pageNumber) {
        eventApi.AttendeeList(token,organizer_id,
                search_text, pagenumber, pageSizr)
                .enqueue(new Callback<FetchAttendee>() {
                    @Override
                    public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {
                        try {
                            if (response.isSuccessful()) {
                                fetchattendeeList.setValue(response.body());
                            }
                        }catch (Exception e)
                        {e.printStackTrace();}
                    }

                    @Override
                    public void onFailure(Call<FetchAttendee> call, Throwable t) {
                        fetchattendeeList.setValue(null);
                    }
                });

        return fetchattendeeList;
    }

}
