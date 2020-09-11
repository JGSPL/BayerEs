package com.procialize.eventapp.ui.newsfeed.networking;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsfeedRepository {
    private static NewsfeedRepository newsRepository;
    MutableLiveData<FetchNewsfeedMultiple> newsData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> newsDataUploaded = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> reportPostUpdate = new MutableLiveData<>();
    MutableLiveData<LikePost> liketPostUpdate = new MutableLiveData<>();

    MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();

    public static NewsfeedRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new NewsfeedRepository();
        }
        return newsRepository;
    }

    private APIService newsfeedApi;

    public NewsfeedRepository() {
        newsfeedApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchNewsfeedMultiple> getNewsFeed(String token, String event_id, String pageSize, String pageNumber) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.NewsFeedFetchMultiple(token, event_id, pageSize, pageNumber).enqueue(new Callback<FetchNewsfeedMultiple>() {
            @Override
            public void onResponse(Call<FetchNewsfeedMultiple> call,
                                   Response<FetchNewsfeedMultiple> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FetchNewsfeedMultiple> call, Throwable t) {
                newsData.setValue(null);

            }
        });
        return newsData;
    }

    private APIService postNewsFeedApi;

    public MutableLiveData<LoginOrganizer> postNewsFeed(String token, String event_id, String Post_content, List<UploadMultimedia> resultList) {
        postNewsFeedApi = ApiUtils.getAPIService();
        RequestBody mevent_id = RequestBody.create(MediaType.parse("text/plain"), event_id);
        RequestBody mPost_content = RequestBody.create(MediaType.parse("text/plain"), Post_content);
        List<MultipartBody.Part> parts = new ArrayList<>();
        List<MultipartBody.Part> thumbParts = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            File file;
            if (resultList.get(i).getMimeType().contains("gif")) {
                file = new File(resultList.get(i).getMedia_file());
            } else {
                if (!resultList.get(i).getCompressedPath().isEmpty()) {
                    file = new File(resultList.get(i).getCompressedPath());
                } else {
                    file = new File(resultList.get(i).getMedia_file());
                }
            }
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("media_file[]", file.getName(), RequestBody.create(MediaType.parse(resultList.get(i).getMimeType()), file));
            parts.add(filePart);
        }

        for (int i = 0; i < resultList.size(); i++) {
            File file = new File(resultList.get(i).getMedia_file_thumb());
            String fileName = "";
            if (file.getName().indexOf(".") > 0) {
                fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            }
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("media_file_thumb[]", fileName + ".png", RequestBody.create(MediaType.parse("image/png"), file));
            thumbParts.add(filePart);
        }
        postNewsFeedApi.postNewsFeed(token, mevent_id, mPost_content, parts, thumbParts)//,Media_file,Media_file_thumb)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            Log.d("PostResponse", response.body().getHeader().get(0).getMsg());
                            newsDataUploaded.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        Log.d("PostResponse", t.getMessage() + "==>Failure");
                        newsDataUploaded.setValue(null);
                    }
                });
        return newsDataUploaded;
    }

    //-----------Update Flag after uploding-----------------
    public MutableLiveData<Boolean> updateIsUplodedIntoDb(Context context, String folderUniqueId) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.uploadMultimediaDao().updateIsUploded(folderUniqueId);
        isUpdated.setValue(true);
        return isUpdated;
    }

    public MutableLiveData<LoginOrganizer> PostHide(String token, String event_id, String news_feed_id) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.PostHide(token, event_id, news_feed_id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportPostUpdate.setValue(response.body());
                    newsRepository.getNewsFeed(token, event_id, "30", "1");
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportPostUpdate.setValue(null);

            }
        });
        return reportPostUpdate;
    }

    public LiveData<LikePost> getLikeActivity() {
        return liketPostUpdate;
    }

    public LiveData<LoginOrganizer> getPostActivity() {
        return reportPostUpdate;
    }


    public MutableLiveData<LoginOrganizer> ReportPost(String token, String event_id, String news_feed_id, String content) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.ReportPost(token, event_id, news_feed_id, content).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportPostUpdate.setValue(response.body());
                    newsRepository.getNewsFeed(token, event_id, "30", "1");

                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportPostUpdate.setValue(null);

            }
        });
        return reportPostUpdate;
    }


    public MutableLiveData<LoginOrganizer> ReportUser(String token, String event_id, String attn_id, String news_feed_id, String content) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.ReportUser(token, event_id, attn_id, news_feed_id, content).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportPostUpdate.setValue(response.body());
                    newsRepository.getNewsFeed(token, event_id, "30", "1");

                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportPostUpdate.setValue(null);

            }
        });
        return reportPostUpdate;
    }

    public MutableLiveData<LoginOrganizer> DeletePost(String token, String event_id, String news_feed_id) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.DeletePost(token, event_id, news_feed_id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    reportPostUpdate.setValue(response.body());
                    newsRepository.getNewsFeed(token, event_id, "30", "1");

                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                reportPostUpdate.setValue(null);

            }
        });
        return reportPostUpdate;
    }


    public MutableLiveData<LikePost> PostLike(String token, String event_id, String news_feed_id) {
        newsfeedApi = ApiUtils.getAPIService();

        newsfeedApi.PostLike(token, event_id, news_feed_id).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call,
                                   Response<LikePost> response) {
                if (response.isSuccessful()) {
                    liketPostUpdate.setValue(response.body());
                    newsRepository.getNewsFeed(token,event_id, "30", "1");

                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                liketPostUpdate.setValue(null);

            }
        });
        return liketPostUpdate;
    }

    public LiveData<LikePost> likePostData() {
        return liketPostUpdate;
    }

}
