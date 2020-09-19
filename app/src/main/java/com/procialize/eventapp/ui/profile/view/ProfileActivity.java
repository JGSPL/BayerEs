package com.procialize.eventapp.ui.profile.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
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
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
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
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
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
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.procialize.eventapp.Utility.Utility.getImageUri;
import static com.procialize.eventapp.Utility.Utility.getRealPathFromURI;
import static com.procialize.eventapp.ui.profile.viewModel.ProfileActivityViewModel.mCurrentPhotoPath;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 101;
    LinearLayout ll_name, ll_last_name, ll_designation, ll_company_name, ll_city, ll_email, ll_mobile, ll_main;
    EditText et_first_name, et_last_name, et_designation, et_company_name, et_city, et_email, et_mobile;
    ImageView iv_profile, iv_change_profile, iv_back;
    TextView tv_profile_pic, tv_header;
    Button btn_save;
    ProfileActivityViewModel profileActivityViewModel;
    String event_id, profile_pic = "";
    ProgressBar progressView;
    private String userChoosenTask = "";
    ConnectionDetector connectionDetector;
    UCrop.Options options;
    File file;
    String api_token, first_name, last_name, designation, company_name, city, email, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            Intent intent = getIntent();
            CommonFunction.saveBackgroundImage(ProfileActivity.this,intent.getStringExtra("eventBg"));
        }catch (Exception e)
        {}
        //Call Refresh token
        new RefreashToken(this).callGetRefreashToken(this);

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
        tv_header = findViewById(R.id.tv_header);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        iv_change_profile.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        btn_save.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        btn_save.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                CommonFunction.showBackgroundImage(ProfileActivity.this, ll_main);
            }
        }, 2000);

        if (connectionDetector.isConnectingToInternet()) {

            profileActivityViewModel.getProfile(api_token, event_id);
            profileActivityViewModel.getProfileDetails().observeForever(new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    List<ProfileDetails> profileDetails = profile.getProfileDetails();
                    if (profileDetails.size() > 0) {
                        first_name = profileDetails.get(0).getFirst_name();
                        last_name = profileDetails.get(0).getLast_name();
                        designation = profileDetails.get(0).getDesignation();
                        company_name = profileDetails.get(0).getCompany_name();
                        city = profileDetails.get(0).getCity();
                        email = profileDetails.get(0).getEmail();
                        mobile = profileDetails.get(0).getMobile();

                        et_first_name.setText(first_name);
                        et_last_name.setText(last_name);
                        et_designation.setText(designation);
                        et_company_name.setText(company_name);
                        et_city.setText(city);
                        et_email.setText(email);
                        et_mobile.setText(mobile);

                        if (profileDetails.get(0).getProfile_picture().trim() != null) {
                            Glide.with(getApplicationContext())
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
                first_name = et_first_name.getText().toString().trim();
                last_name = et_last_name.getText().toString().trim();
                designation = et_designation.getText().toString().trim();
                company_name = et_company_name.getText().toString().trim();
                city = et_city.getText().toString().trim();
                email = et_email.getText().toString().trim();
                mobile = et_mobile.getText().toString().trim();
                profile_pic = tv_profile_pic.getText().toString();

                if (connectionDetector.isConnectingToInternet()) {
                    updateProfile(
                            first_name,
                            last_name,
                            designation,
                            company_name,
                            city,
                            email,
                            mobile,
                            profile_pic
                    );
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet connection");
                }
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
//                onSelectFromGalleryResult(data);
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                String compressedImagePath = compressImage(picturePath);
//                appDelegate.setPostImagePath(compressedImagePath);

                file = new File(compressedImagePath);

                UCrop.of(selectedImage, Uri.parse(file.getAbsolutePath()))
                        .withAspectRatio(2, 2).withOptions(options)
                        .withMaxResultSize(200, 200)
                        .start(this);

                tv_profile_pic.setText(file.getAbsolutePath());
                // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
//                Glide.with(this).load(compressedImagePath).into(profileIV);


                // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
                // .into(post_thumbnail);

                Toast.makeText(ProfileActivity.this, "Image selected",
                        Toast.LENGTH_SHORT).show();

                cursor.close();
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

        Glide.with(getApplicationContext()).load(bm)
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

        Glide.with(getApplicationContext()).load(tempUri)
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
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //profileActivityViewModel.cameraIntent(ProfileActivity.this);
                    try {
                        Intent intent = getIntent();
                        CommonFunction.saveBackgroundImage(ProfileActivity.this, intent.getStringExtra("eventBg"));
                        CommonFunction.showBackgroundImage(this, ll_main);
                    } catch (Exception e) {
                    }
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
        /*updateProfile(first_name,
                last_name,
                designation,
                company_name,
                city,
                email,
                mobile,
                "");*/
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }

    public void updateProfile(final String first_name, final String last_name, final String designation,
                              final String company_name, final String city, final String email, final String mobile, final String profile_pic) {
        profileActivityViewModel.validation(first_name, last_name, designation, company_name, city);
        profileActivityViewModel.getIsValid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.isEmpty()) {
                    profileActivityViewModel.updateProfile(api_token, event_id, first_name, last_name, designation, city,
                            email, mobile, company_name, profile_pic);
                    profileActivityViewModel.UpdateProfileDetails().observe(ProfileActivity.this,new Observer<Profile>() {
                        @Override
                        public void onChanged(final Profile profile) {
                            if (profile != null) {
                                if (profile.getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                    Utility.createShortSnackBar(ll_main, profile.getHeader().get(0).getMsg());

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(KEY_FNAME, profile.getProfileDetails().get(0).getFirst_name());
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
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            profileActivityViewModel.openMainActivity(ProfileActivity.this);
                                            profileActivityViewModel.updateProfileFlag(ProfileActivity.this, event_id);
                                        }
                                    }, 500);

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
                } else {
                    Utility.createShortSnackBar(ll_main, s);
                }
            }
        });
    }

    private String getRealPathFromURIgallery(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURIgallery(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}