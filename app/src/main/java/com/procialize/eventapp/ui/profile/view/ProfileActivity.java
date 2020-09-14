package com.procialize.eventapp.ui.profile.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.profile.model.Profile;
import com.procialize.eventapp.ui.profile.model.ProfileDetails;
import com.procialize.eventapp.ui.profile.viewModel.ProfileActivityViewModel;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Constants.Constant.REQUEST_CAMERA;
import static com.procialize.eventapp.Constants.Constant.SELECT_FILE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_GCM_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_TOKEN;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.procialize.eventapp.Utility.Utility.getImageUri;
import static com.procialize.eventapp.Utility.Utility.getRealPathFromURI;
import static com.procialize.eventapp.ui.profile.viewModel.ProfileActivityViewModel.mCurrentPhotoPath;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 101;
    LinearLayout ll_name, ll_last_name, ll_designation, ll_company_name, ll_city, ll_email, ll_mobile, ll_main;
    EditText et_first_name, et_last_name, et_designation, et_company_name, et_city, et_email, et_mobile;
    ImageView iv_profile, iv_change_profile, iv_back;
    TextView tv_profile_pic;
    Button btn_save;
    ProfileActivityViewModel profileActivityViewModel;
    String event_id , profile_pic = "";
    ProgressBar progressView;
    private String userChoosenTask = "";
    ConnectionDetector connectionDetector;
    UCrop.Options options;
    File file;
    String api_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        options = new UCrop.Options();

       /* options.setToolbarColor(Color.parseColor(colorActive));
        options.setCropFrameColor(Color.parseColor(colorActive));
        options.setLogoColor(Color.parseColor(colorActive));
        options.setActiveWidgetColor(Color.parseColor(colorActive));
        options.setLogoColor(Color.parseColor(colorActive));*/
        options.setToolbarTitle("Edit Profile");

        options.setToolbarCancelDrawable(R.color.transperent);

        profileActivityViewModel = ViewModelProviders.of(this).get(ProfileActivityViewModel.class);
        connectionDetector = ConnectionDetector.getInstance(this);

        progressView = findViewById(R.id.progressView);
        ll_main = findViewById(R.id.ll_main);
        ll_name = findViewById(R.id.ll_name);
        ll_last_name = findViewById(R.id.ll_last_name);
        ll_designation = findViewById(R.id.ll_designation);
        ll_company_name = findViewById(R.id.ll_company_name);
        ll_city = findViewById(R.id.ll_city);
        ll_email = findViewById(R.id.ll_email);
        ll_mobile = findViewById(R.id.ll_mobile);
        iv_back = findViewById(R.id.iv_back);

        iv_profile = findViewById(R.id.iv_profile);
        iv_change_profile = findViewById(R.id.iv_change_profile);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_designation = findViewById(R.id.et_designation);
        et_company_name = findViewById(R.id.et_company_name);
        et_city = findViewById(R.id.et_city);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        tv_profile_pic = findViewById(R.id.tv_profile_pic);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        iv_change_profile.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        api_token = SharedPreference.getPref(this,AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this,EVENT_ID);

        if (connectionDetector.isConnectingToInternet()) {
            profileActivityViewModel.getProfile(api_token,event_id);
            profileActivityViewModel.getProfileDetails().observe(this, new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    List<ProfileDetails> profileDetails = profile.getProfileDetails();
                    if (profileDetails.size() > 0) {
                        et_first_name.setText(profileDetails.get(0).getFirst_name());
                        et_last_name.setText(profileDetails.get(0).getLast_name());
                        et_designation.setText(profileDetails.get(0).getDesignation());
                        et_company_name.setText(profileDetails.get(0).getCompany_name());
                        et_city.setText(profileDetails.get(0).getCity());
                        et_email.setText(profileDetails.get(0).getEmail());
                        et_mobile.setText(profileDetails.get(0).getMobile());

                        if (profileDetails.get(0).getProfile_picture().trim() != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(profileDetails.get(0).getProfile_picture().trim())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressView.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressView.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(iv_profile);
                        }

                    }

                    if (profileActivityViewModel != null && profileActivityViewModel.getProfileDetails().hasObservers()) {
                        profileActivityViewModel.getProfileDetails().removeObservers(ProfileActivity.this);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String first_name = et_first_name.getText().toString().trim();
                String last_name = et_last_name.getText().toString().trim();
                String designation = et_designation.getText().toString().trim();
                String company_name = et_company_name.getText().toString().trim();
                String city = et_city.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String mobile = et_mobile.getText().toString().trim();
                String profile_pic = tv_profile_pic.getText().toString();

                profileActivityViewModel.updateProfile(api_token,event_id,
                        first_name,
                        last_name,
                        designation,
                        city,
                        email,
                        mobile, company_name,
                        profile_pic);
                profileActivityViewModel.UpdateProfileDetails().observe(this, new Observer<Profile>() {
                    @Override
                    public void onChanged(Profile profile) {
                        if (profile != null) {
                            if (profile.getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                Utility.createShortSnackBar(ll_main, profile.getHeader().get(0).getMsg());
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put(KEY_FNAME,profile.getProfileDetails().get(0).getFirst_name());
                                        map.put(KEY_LNAME, profile.getProfileDetails().get(0).getLast_name());
                                        map.put(KEY_EMAIL, profile.getProfileDetails().get(0).getEmail());
                                        map.put(KEY_PASSWORD, "");
                                        map.put(KEY_DESIGNATION, profile.getProfileDetails().get(0).getDesignation());
                                        map.put(KEY_COMPANY, profile.getProfileDetails().get(0).getCompany_name());
                                        map.put(KEY_MOBILE, profile.getProfileDetails().get(0).getMobile());
                                        map.put(KEY_TOKEN, "");
                                        map.put(KEY_CITY, profile.getProfileDetails().get(0).getCity());
                                        map.put(KEY_GCM_ID, "");
                                        map.put(KEY_PROFILE_PIC, profile.getProfileDetails().get(0).getProfile_picture());
                                        map.put(KEY_ATTENDEE_ID, profile.getProfileDetails().get(0).getAttendee_id());
                                        map.put(ATTENDEE_STATUS, profile.getProfileDetails().get(0).getIs_god());
                                        map.put(IS_LOGIN, "true");
                                        SharedPreference.putPref(ProfileActivity.this, map);

                                        profileActivityViewModel.openMainActivity(ProfileActivity.this);
                                    }
                                }, 100);

                            } else {
                                Utility.createShortSnackBar(ll_main, profile.getHeader().get(0).getMsg());
                            }
                        } else {
                            Utility.createShortSnackBar(ll_main, "failure..!");
                        }

                        if (profileActivityViewModel != null && profileActivityViewModel.UpdateProfileDetails().hasObservers()) {
                            profileActivityViewModel.UpdateProfileDetails().removeObservers(ProfileActivity.this);
                        }
                    }
                });

                break;
            case R.id.iv_change_profile:

                selectImage();

                break;
            case R.id.iv_back:
                onBackPressed();

                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {
                        boolean cameraResult = Utility.checkCameraPermission(ProfileActivity.this);
                        if (cameraResult) {
                            profileActivityViewModel.cameraIntent(ProfileActivity.this);
                        }
                    }

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result) {
                        profileActivityViewModel.galleryIntent(ProfileActivity.this);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                onSelectFromCropResult(resultUri);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(getApplicationContext(), tempUri));

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2).withOptions(options)
                        .withMaxResultSize(200, 200)
                        .start(this);

                tv_profile_pic.setText(destination.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Glide.with(this).load(bm)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        iv_profile.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(iv_profile);
    }

    private void onSelectFromCropResult(Uri tempUri) {

        if (tempUri != null) {

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            file = new File(tempUri.getPath());

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();

                tv_profile_pic.setText(tempUri.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Glide.with(this).load(tempUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        iv_profile.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(iv_profile);
//        profileIV.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Uri tempUri;
            if (data != null) {
                if (data.getData() != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    tempUri = getImageUri(getApplicationContext(), thumbnail);

                    UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                            .withAspectRatio(2, 2).withOptions(options)
                            .withMaxResultSize(200, 200)
                            .start(this);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    file = new File(getRealPathFromURI(ProfileActivity.this, tempUri));
                    tv_profile_pic.setText(tempUri.getPath());
                } else {
                    file = new File(mCurrentPhotoPath);

                    tempUri = Uri.fromFile(file);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                            .withAspectRatio(2, 2).withOptions(options)
                            .withMaxResultSize(200, 200)
                            .start(this);

                    tv_profile_pic.setText(tempUri.getPath());
                }
            } else {
                file = new File(mCurrentPhotoPath);

                tempUri = Uri.fromFile(file);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2).withOptions(options)
                        .withMaxResultSize(200, 200)
                        .start(this);
                tv_profile_pic.setText(tempUri.getPath());
            }

//        profileIV.setImageURI(tempUri);

            Glide.with(this).load(tempUri)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).circleCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            iv_profile.setImageResource(R.drawable.profilepic_placeholder);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(iv_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    profileActivityViewModel.cameraIntent(ProfileActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //profileActivityViewModel.cameraIntent(ProfileActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}