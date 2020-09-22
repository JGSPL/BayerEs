package com.procialize.eventapp.ui.profile.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.profile.model.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private static ProfileRepository profileRepository;
    MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();
    MultipartBody.Part body;
   public static ProfileRepository getInstance() {
        if (profileRepository == null) {
            profileRepository = new ProfileRepository();
        }
        return profileRepository;
    }

    private APIService profileApi;

    public ProfileRepository() {
        profileApi = ApiUtils.getAPIService();
    }

    /**
     * @param event_id
     * @return
     */
    public MutableLiveData<Profile> getProfile(String token,String event_id) {
        profileApi = ApiUtils.getAPIService();
        profileApi.getProfile(token,event_id).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    profileMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                try {
                    profileMutableLiveData.setValue(null);
                }catch (Exception e)
                {}
            }
        });

        return profileMutableLiveData;
    }

    /**
     * @param event_id
     * @return
     */
    public MutableLiveData<Profile> updateProfile(String token,String event_id, String first_name, String last_name, String
            designation, String city, String email, String mobile, String company_name, String profile_pic) {
        RequestBody mEvent_id = RequestBody.create(MediaType.parse("text/plain"), event_id);
        RequestBody mFirst_name = RequestBody.create(MediaType.parse("text/plain"), first_name);
        RequestBody mLast_name = RequestBody.create(MediaType.parse("text/plain"), last_name);
        RequestBody mDesignation = RequestBody.create(MediaType.parse("text/plain"), designation);
        RequestBody mCity = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody mEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody mCompany_name = RequestBody.create(MediaType.parse("text/plain"), company_name);

        profileApi = ApiUtils.getAPIService();
        if(!profile_pic.isEmpty()) {
            File file = new File(profile_pic);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), file);
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
            profileApi.updateProfile(token,mEvent_id, mFirst_name, mLast_name, mDesignation, mCity, mEmail, mMobile,mCompany_name,body).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if (response.isSuccessful()) {
                        profileMutableLiveData.postValue (response.body());
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    profileMutableLiveData.postValue (null);
                }
            });
        }
        else
        {
            profileApi.updateProfile(token,mEvent_id, mFirst_name, mLast_name, mDesignation, mCity, mEmail, mMobile,mCompany_name).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if (response.isSuccessful()) {
                        profileMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    profileMutableLiveData.setValue(null);
                }
            });
        }



        return profileMutableLiveData;
    }
}
