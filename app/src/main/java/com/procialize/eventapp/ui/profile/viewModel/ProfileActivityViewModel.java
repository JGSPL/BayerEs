package com.procialize.eventapp.ui.profile.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.ui.profile.model.Profile;
import com.procialize.eventapp.ui.profile.networking.ProfileRepository;
import com.procialize.eventapp.ui.profile.roomDB.ProfileEventId;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.procialize.eventapp.Constants.Constant.SELECT_FILE;

public class ProfileActivityViewModel extends ViewModel {
    ProfileRepository profileRepository;
    public static String mCurrentPhotoPath = "";
    private MutableLiveData<Profile> profileData = new MutableLiveData<>();
    private MutableLiveData<Profile> updateProfile = new MutableLiveData<>();
    MutableLiveData<String> message = new MutableLiveData<>();

    EventAppDB eventAppDB;

    /**
     * To get Profile details
     *
     * @param event_id
     */
    public void getProfile(String token, String event_id) {
        profileRepository = ProfileRepository.getInstance();
        profileData = profileRepository.getProfile(token, event_id);//, pageSize,pageNumber);
    }

    public LiveData<Profile> getProfileDetails() {
        return profileData;
    }

    /**
     * Update Profile Details
     *
     * @param event_id
     * @param first_name
     * @param last_name
     * @param designation
     * @param city
     * @param email
     * @param mobile
     * @param company_name
     * @param profile_pic
     */

    public void updateProfile(String token, String event_id, String first_name, String last_name, String
            designation, String city, String email, String mobile, String company_name, String profile_pic) {
        profileRepository = ProfileRepository.getInstance();
        updateProfile = profileRepository.updateProfile(token,
                event_id,
                first_name,
                last_name,
                designation,
                city,
                email,
                mobile, company_name,
                profile_pic
        );
    }

    public LiveData<Profile> UpdateProfileDetails() {
        return updateProfile;
    }

    public void cameraIntent(Activity activity) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, Constant.REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void galleryIntent(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, SELECT_FILE);
    }

    public void openMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void updateProfileFlag(Activity activity, String eventId) {
        eventAppDB = EventAppDB.getDatabase(activity);
        ProfileEventId profileEventId = new ProfileEventId();
        profileEventId.setFld_event_id(eventId);
        profileEventId.setFld_is_profile_update("1");
        eventAppDB.profileUpdateDao().insertProfileWithEventId(profileEventId);
    }

    public void validation(String first_name, String last_name, String designation, String company_name, String city) {
        if (first_name.isEmpty()) {
            message.setValue("Please Enter First Name");
        } /*else if (last_name.isEmpty()) {
            message.setValue("Please Enter Last Name");
        } */else if (designation.isEmpty()) {
            message.setValue("Please Enter Designation");
        } /*else if (company_name.isEmpty()) {
            message.setValue("Please Enter Company Name");
        }*/ else if (city.isEmpty()) {
            message.setValue("Please Enter City");
        }
        else {message.setValue("");}
    }

    public MutableLiveData<String> getIsValid() {
        return message;
    }


}
