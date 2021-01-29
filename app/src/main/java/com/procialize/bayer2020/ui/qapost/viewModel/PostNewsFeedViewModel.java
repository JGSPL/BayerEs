package com.procialize.bayer2020.ui.qapost.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.MediaLoader;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.qa.view.QnADirectActivity;
import com.procialize.bayer2020.ui.qapost.model.SelectedImages;
import com.procialize.bayer2020.ui.qapost.networking.PostNewsFeedRepository;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostNewsFeedViewModel extends ViewModel {
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
    private MutableLiveData<ArrayList<AlbumFile>> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> multimediaUploadLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isInsertedIntoDB = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    PostNewsFeedRepository postNewsFeedRepository;
    private MutableLiveData<List<TableAttendee>> attendeeList = new MutableLiveData<>();
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

    public void selectImage(Activity context) {
        Album.image(context)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(10)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(context)
                                .toolBarColor(context.getResources().getColor(R.color.colorPrimary))
                                .statusBarColor(context.getResources().getColor(R.color.design_default_color_background))
                                .title("Select Image")
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
            Log.e("tag123", fnfe1.getMessage());

        } catch (Exception e) {
            Log.e("tag123", e.getMessage());
        }
    }

    public MutableLiveData<ArrayList<AlbumFile>> getSelectedMedia() {
        return mutableLiveData;
    }

    public MutableLiveData<LoginOrganizer> getStatus() {

        return multimediaUploadLiveData;
    }

    public void sendPost(String token,String event_id, String status, ArrayList<SelectedImages> resultList) {//, String[] mediaFile, String[] mediaFileThumb) {
        if (!status.isEmpty()) {
            postNewsFeedRepository = PostNewsFeedRepository.getInstance();
            multimediaUploadLiveData = postNewsFeedRepository.postNewsFeed1(token,event_id, status, resultList);//,mediaFile,mediaFileThumb);
        }
    }

    public void insertMultimediaIntoDB(Context context, String postStatus, ArrayList<SelectedImages> resultList, String time) {
        postNewsFeedRepository = PostNewsFeedRepository.getInstance();
        isInsertedIntoDB = postNewsFeedRepository.insertIntoDb(context, postStatus, resultList,time);
    }

    public MutableLiveData<Boolean> getDBStatus() {
        return isInsertedIntoDB;
    }

    public void startNewsFeedFragment(Activity activity) {
        activity.startActivity(new Intent(activity, QnADirectActivity.class).putExtra("from","postNewsFeed"));
    }

    public void validation(String postStatus) {
        if (postStatus.isEmpty()) {
            isValid.setValue(false);
        } else {
            isValid.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getIsValid() {
        return isValid;
    }

    public void showMentionsList(Activity activity,boolean display) {
        com.percolate.caffeine.ViewUtils.showView(activity, R.id.mentions_list_layout);
        if (display) {
            com.percolate.caffeine.ViewUtils.showView(activity, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.hideView(activity, R.id.mentions_empty_view);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(activity, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.showView(activity, R.id.mentions_empty_view);
        }
    }

    public void searchUsers(String query,List<TableAttendee> attendeeList_db) {
        final List<TableAttendee> searchResults = new ArrayList<>();
        if (StringUtils.isNotBlank(query)) {
            query = query.toLowerCase(Locale.US);
            if (attendeeList_db != null && !attendeeList_db.isEmpty()) {
                searchResults.clear();
                attendeeList.setValue(null);
                for (TableAttendee user : attendeeList_db) {
                    final String firstName = user.getFirst_name().toLowerCase();
                    final String lastName = user.getLast_name().toLowerCase();
                    if (firstName.startsWith(query) || lastName.startsWith(query)) {
                        searchResults.add(user);
                    }
                }
            }
        }
        attendeeList.setValue(searchResults);
    }

    public LiveData<List<TableAttendee>> getAttendeeList()
    {
        return attendeeList;
    }



}