package com.procialize.eventapp.ui.newsFeedPost.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.MediaLoader;
import com.procialize.eventapp.ui.newsFeedComment.networking.GifRepository;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.networking.PostNewsFeedRepository;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;

public class PostNewsFeedViewModel extends ViewModel {
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
    private MutableLiveData<ArrayList<AlbumFile>> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> multimediaUploadLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isInsertedIntoDB = new MutableLiveData<>();
    PostNewsFeedRepository postNewsFeedRepository;

    public void init() {
        if (mutableLiveData == null) {
            return;
        }
    }

    public void albumLoader(Activity activity) {
        Album.initialize(AlbumConfig.newBuilder(activity)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        );
    }

    public void selectAlbum(Activity context) {
        Album.album(context)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(10)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(context)
                                .toolBarColor(context.getResources().getColor(R.color.colorPrimary))
                                .statusBarColor(context.getResources().getColor(R.color.design_default_color_background))
                                .title("Select Image/Video")
                                .mediaItemCheckSelector(context.getResources().getColor(R.color.white),
                                        context.getResources().getColor(R.color.colorPrimary))
                                //.bucketItemCheckSelector(getResources().getColor(R.color.white),getResources().getColor(R.color.active_menu))
                                //.title(toolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        try {
                            mAlbumFiles = result;
                            mutableLiveData.postValue(mAlbumFiles);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        //Toast.makeText(SelectImageActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    public void copyFile(String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + "/" + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<AlbumFile>> getSelectedMedia() {
        return mutableLiveData;
    }

    public MutableLiveData<LoginOrganizer> getStatus() {

        return multimediaUploadLiveData;
    }

    public void sendPost(String event_id, String status, ArrayList<SelectedImages> resultList){//, String[] mediaFile, String[] mediaFileThumb) {
        postNewsFeedRepository = PostNewsFeedRepository.getInstance();
        multimediaUploadLiveData = postNewsFeedRepository.postNewsFeed(event_id,status, resultList);//,mediaFile,mediaFileThumb);
    }

    public void insertMultimediaIntoDB(Context context,String postStatus,ArrayList<SelectedImages> resultList) {
        postNewsFeedRepository = PostNewsFeedRepository.getInstance();
        isInsertedIntoDB = postNewsFeedRepository.insertIntoDb(context,postStatus,resultList);
    }

    public MutableLiveData<Boolean> getDBStatus() {
        return isInsertedIntoDB;
    }

    public void startNewsFeedFragment(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

}