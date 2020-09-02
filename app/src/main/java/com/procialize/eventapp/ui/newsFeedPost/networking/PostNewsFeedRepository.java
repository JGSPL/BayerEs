package com.procialize.eventapp.ui.newsFeedPost.networking;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimediaDao;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class PostNewsFeedRepository {
    private static PostNewsFeedRepository postNewsFeedRepository;
    MutableLiveData<LoginOrganizer> newsData = new MutableLiveData<>();
    MutableLiveData<Boolean> isInserted = new MutableLiveData<>();
    EventAppDB eventAppDB;

    public static PostNewsFeedRepository getInstance() {
        if (postNewsFeedRepository == null) {
            postNewsFeedRepository = new PostNewsFeedRepository();
        }
        return postNewsFeedRepository;
    }

    private APIService postNewsFeedApi;

    public PostNewsFeedRepository() {
        postNewsFeedApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<LoginOrganizer> postNewsFeed(String event_id, String Post_content, List<SelectedImages> resultList) {
        postNewsFeedApi = ApiUtils.getAPIService();
        RequestBody mevent_id = RequestBody.create(MediaType.parse("text/plain"), event_id);
        RequestBody mPost_content = RequestBody.create(MediaType.parse("text/plain"), Post_content);
        List<MultipartBody.Part> parts = new ArrayList<>();
        List<MultipartBody.Part> thumbParts = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            File file = new File(resultList.get(i).getmPath());
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("media_file[]", file.getName(), RequestBody.create(MediaType.parse(resultList.get(i).getmMimeType()), file));
            parts.add(filePart);
        }

        for (int i = 0; i < resultList.size(); i++) {
            File file = new File(resultList.get(i).getmThumbPath());
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("media_file_thumb[]", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            thumbParts.add(filePart);
        }
        postNewsFeedApi.postNewsFeed(mevent_id, mPost_content, parts, thumbParts)//,Media_file,Media_file_thumb)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            newsData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        newsData.setValue(null);
                    }
                });
        return newsData;
    }

    public MutableLiveData<Boolean> insertIntoDb(Context context, String postStatus, ArrayList<SelectedImages> resultList) {
        eventAppDB = EventAppDB.getDatabase(context);

        int previousCount = eventAppDB.uploadMultimediaDao().getRowCount();
        Date date = new Date();
        long time = date.getTime();

        if (!postStatus.isEmpty()) {
            UploadMultimedia uploadMultimediaPostStatus = new UploadMultimedia();
            uploadMultimediaPostStatus.setPost_status(postStatus);
            uploadMultimediaPostStatus.setMedia_file("");
            uploadMultimediaPostStatus.setMedia_file_thumb("");
            uploadMultimediaPostStatus.setNews_feed_id("");
            uploadMultimediaPostStatus.setIs_uploaded("0");
            uploadMultimediaPostStatus.setMedia_type("");
            uploadMultimediaPostStatus.setCompressedPath("");
            uploadMultimediaPostStatus.setFolderUniqueId(String.valueOf(time));
            uploadMultimediaPostStatus.setMimeType("");
            eventAppDB.uploadMultimediaDao().insertMultimediaToUpload(uploadMultimediaPostStatus);
        }

        for (int i = 0; i < resultList.size(); i++) {
            UploadMultimedia uploadMultimedia = new UploadMultimedia();
            uploadMultimedia.setPost_status("");
            uploadMultimedia.setMedia_file(resultList.get(i).getmOriginalFilePath());
            uploadMultimedia.setMedia_file_thumb(resultList.get(i).getmThumbPath());
            uploadMultimedia.setNews_feed_id("");
            uploadMultimedia.setIs_uploaded("0");
            uploadMultimedia.setMedia_type(resultList.get(i).getmMediaType());
            uploadMultimedia.setCompressedPath("");
            uploadMultimedia.setFolderUniqueId(String.valueOf(time));
            uploadMultimedia.setMimeType(resultList.get(i).getmMimeType());

            eventAppDB.uploadMultimediaDao().insertMultimediaToUpload(uploadMultimedia);
        }

        int currentCount = eventAppDB.uploadMultimediaDao().getRowCount();
        Log.d("Tot_Count", String.valueOf(eventAppDB.uploadMultimediaDao().getRowCount()));
        if (previousCount != currentCount) {
            isInserted.setValue(true);
            return isInserted;
        } else {
            isInserted.setValue(false);
            return isInserted;
        }
    }
}