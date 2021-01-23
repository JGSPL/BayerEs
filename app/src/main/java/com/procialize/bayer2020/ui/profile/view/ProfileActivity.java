package com.procialize.bayer2020.ui.profile.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.profile.model.Profile;
import com.procialize.bayer2020.ui.profile.model.ProfileDetails;
import com.procialize.bayer2020.ui.profile.viewModel.ProfileActivityViewModel;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import static com.procialize.bayer2020.Constants.Constant.REQUEST_CAMERA;
import static com.procialize.bayer2020.Constants.Constant.SELECT_FILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.PROFILE_PIC;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;
import static com.procialize.bayer2020.Utility.Utility.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.procialize.bayer2020.Utility.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.procialize.bayer2020.Utility.Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.procialize.bayer2020.Utility.Utility.getImageUri;
import static com.procialize.bayer2020.Utility.Utility.getRealPathFromURI;
import static com.procialize.bayer2020.ui.profile.viewModel.ProfileActivityViewModel.mCurrentPhotoPath;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final int RequestPermissionCode = 101;
    RelativeLayout ll_main;
    LinearLayout ll_aspociated, ll_sapcode, ll_location, ll_emailid, ll_alternetmobno, ll_organisation, ll_name, ll_last_name, ll_designation, ll_company_name, ll_city, ll_email, ll_mobno, ll_bg;
    EditText et_aspociated, et_sapcode, et_pincode, et_emailid, et_alternetmobno, et_mobno, et_organisation, et_first_name, et_last_name, et_designation, et_company_name, et_city, et_email, et_mobile,
    et_state ;
    ImageView iv_first_name, iv_last_name, iv_designation, iv_company_name, iv_city, iv_email, iv_mobile;
    ImageView iv_profile, iv_change_profile, iv_back;
    RadioGroup radiogroupPCO;
    RadioButton radioButton4, radioButton3, radioButton2, radioButton1;
    View view_down;
    TextView tv_profile_pic, tv_header;
    Button btn_save;
    ProfileActivityViewModel profileActivityViewModel;
    String event_id, profile_pic = "";
    ProgressBar progressView;
    private String userChoosenTask = "";
    ConnectionDetector connectionDetector;
    UCrop.Options options;
    File file;

    Spinner spinner;
    String api_token, first_name, last_name, designation, company_name, city, email, mobile, is_god, alternate_no, turnover, specialization, license, pincode="",
            no_of_technician="", no_of_pco_served="", associated_since="", sap_code="", attendee_id="", user_type="", state="";
    String year[] = {"1950","1951","1952","1953","1954","1955","1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972","1973",
    "1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000",
    "2001","2002","2003","2004","2005","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021"};
    MultipartBody.Part body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            Intent intent = getIntent();
            CommonFunction.saveBackgroundImage(ProfileActivity.this, intent.getStringExtra("eventBg"));
        } catch (Exception e) {

        }
        //Call Refresh token
        new RefreashToken(this).callGetRefreashToken(this);

        options = new UCrop.Options();

        options.setToolbarTitle("Edit Profile");

        options.setToolbarCancelDrawable(R.color.transperent);

        profileActivityViewModel = ViewModelProviders.of(this).get(ProfileActivityViewModel.class);
        connectionDetector = ConnectionDetector.getInstance(this);

        progressView = findViewById(R.id.progressView);
        ll_main = findViewById(R.id.ll_main);
        ll_bg = findViewById(R.id.ll_bg);
        ll_name = findViewById(R.id.ll_name);
        ll_last_name = findViewById(R.id.ll_last_name);
        ll_organisation = findViewById(R.id.ll_organisation);
        ll_mobno = findViewById(R.id.ll_mobno);
        ll_emailid = findViewById(R.id.ll_emailid);
        ll_location = findViewById(R.id.ll_location);
        ll_sapcode = findViewById(R.id.ll_sapcode);
        ll_aspociated = findViewById(R.id.ll_aspociated);

        iv_back = findViewById(R.id.iv_back);
        view_down = findViewById(R.id.view_down);

        iv_profile = findViewById(R.id.iv_profile);
        iv_change_profile = findViewById(R.id.iv_change_profile);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_organisation = findViewById(R.id.et_organisation);
        et_mobno = findViewById(R.id.et_mobno);
        et_emailid = findViewById(R.id.et_emailid);
        et_sapcode = findViewById(R.id.et_sapcode);
        tv_profile_pic = findViewById(R.id.tv_profile_pic);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton1 = findViewById(R.id.radioButton1);
        radiogroupPCO = findViewById(R.id.radiogroupPCO);

        et_aspociated = findViewById(R.id.et_aspociated);
        et_state = findViewById(R.id.et_state);
        et_alternetmobno = findViewById(R.id.et_alternetmobno);
        et_pincode = findViewById(R.id.et_pincode);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ProfileActivity.this,
                android.R.layout.simple_spinner_item,year);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        radiogroupPCO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    String dataRad = checkedRadioButton.getText().toString();
                    if (dataRad.equalsIgnoreCase("Less than 50")) {
                        no_of_pco_served = "0";
                    } else if (dataRad.equalsIgnoreCase("50-100")) {
                        no_of_pco_served = "1";
                    } else if (dataRad.equalsIgnoreCase("101-200")) {
                        no_of_pco_served = "2";
                    } else if (dataRad.equalsIgnoreCase("Above 200")) {
                        no_of_pco_served = "3";
                    }else{
                        no_of_pco_served = "";
                    }

                }
            }
        });


        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        iv_change_profile.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("Profile", api_token);
        CommonFirebase.firbaseAnalytics(this, "Profile", api_token);


        if (connectionDetector.isConnectingToInternet()) {
            ApiUtils.getAPIService().getProfile(api_token, event_id).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if (response.isSuccessful()) {
                        String strEventList = response.body().getProfileDetailsEncrypted();
                        RefreashToken refreashToken = new RefreashToken(ProfileActivity.this);
                        String data = refreashToken.decryptedData(strEventList);
                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>(){}.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();
                        if (profileDetails.size() > 0) {
                            first_name = profileDetails.get(0).getFirst_name();
                            last_name = profileDetails.get(0).getLast_name();
                            designation = profileDetails.get(0).getDesignation();
                            company_name = profileDetails.get(0).getCompany_name();
                            city = profileDetails.get(0).getCity();
                            email = profileDetails.get(0).getEmail();
                            mobile = profileDetails.get(0).getMobile();
                            sap_code = profileDetails.get(0).getSap_code();
                            associated_since = profileDetails.get(0).getAssociated_since();
                            no_of_pco_served = profileDetails.get(0).getNo_of_pco_served();
                            no_of_technician = profileDetails.get(0).getNo_of_technician();
                            pincode = profileDetails.get(0).getPincode();
                            //   license = profileDetails.get(0).getLicense();
                            specialization = profileDetails.get(0).getSpecialization();
                            turnover = profileDetails.get(0).getTurnover();
                            is_god = profileDetails.get(0).getIs_god();
                            alternate_no = profileDetails.get(0).getAlternate_no();
                            state = profileDetails.get(0).getState();
                            pincode = profileDetails.get(0).getPincode();
                            user_type = profileDetails.get(0).getUser_type();
                            tv_profile_pic.setText(profileDetails.get(0).getProfile_picture());


                            et_first_name.setText(first_name);
                            et_last_name.setText(last_name);
                            et_mobno.setText(mobile);
                            et_aspociated.setText(associated_since);
                            et_sapcode.setText(sap_code);
                            et_organisation.setText(company_name);
                            et_emailid.setText(email);
                            if(alternate_no!=null) {
                                et_alternetmobno.setText(alternate_no);
                            }
                            et_pincode.setText(pincode);
                            et_state.setText(state);


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

                    }else
                    {
                        Toast.makeText(ProfileActivity.this,"Internal server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    try {
                        Toast.makeText(ProfileActivity.this,"Failure", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {}
                }
            });

          /*  profileActivityViewModel.getProfile(api_token, event_id);
            profileActivityViewModel.getProfileDetails().observeForever(new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {

                    String strEventList = profile.getProfileDetailsEncrypted();
                    RefreashToken refreashToken = new RefreashToken(ProfileActivity.this);
                    String data = refreashToken.decryptedData(strEventList);
                    JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                    ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>(){}.getType());

                   // List<ProfileDetails> profileDetails = profile.getProfileDetails();
                    if (profileDetails.size() > 0) {
                        attendee_id = profileDetails.get(0).getAttendee_id();
                        first_name = profileDetails.get(0).getFirst_name();
                        last_name = profileDetails.get(0).getLast_name();
                        designation = profileDetails.get(0).getDesignation();
                        company_name = profileDetails.get(0).getCompany_name();
                        city = profileDetails.get(0).getCity();
                        email = profileDetails.get(0).getEmail();
                        mobile = profileDetails.get(0).getMobile();
                        sap_code = profileDetails.get(0).getSap_code();
                        associated_since = profileDetails.get(0).getAssociated_since();
                        no_of_pco_served = profileDetails.get(0).getNo_of_pco_served();
                        no_of_technician = profileDetails.get(0).getNo_of_technician();
                        pincode = profileDetails.get(0).getPincode();
                     //   license = profileDetails.get(0).getLicense();
                        specialization = profileDetails.get(0).getSpecialization();
                        turnover = profileDetails.get(0).getTurnover();
                        is_god = profileDetails.get(0).getIs_god();
                        alternate_no = profileDetails.get(0).getAlternate_no();
                        state = profileDetails.get(0).getState();
                        pincode = profileDetails.get(0).getPincode();
                        user_type = profileDetails.get(0).getUser_type();
                        tv_profile_pic.setText(profileDetails.get(0).getProfile_picture());


                        et_first_name.setText(first_name);
                        et_last_name.setText(last_name);
                        et_mobno.setText(mobile);
                        et_aspociated.setText(associated_since);
                        et_sapcode.setText(sap_code);
                        et_organisation.setText(company_name);
                        et_emailid.setText(email);
                        if(alternate_no!=null) {
                            et_alternetmobno.setText(alternate_no);
                        }
                        et_pincode.setText(pincode);
                        et_state.setText(state);


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
            });*/
        }
        else
        {
            first_name = SharedPreference.getPref(getApplicationContext(), KEY_FNAME);
            last_name = SharedPreference.getPref(getApplicationContext(), KEY_LNAME);
            designation =SharedPreference.getPref(getApplicationContext(), KEY_DESIGNATION);
            company_name = SharedPreference.getPref(getApplicationContext(), KEY_COMPANY);
            city = SharedPreference.getPref(getApplicationContext(), KEY_CITY);
            email = SharedPreference.getPref(getApplicationContext(), KEY_EMAIL);
            mobile = SharedPreference.getPref(getApplicationContext(), KEY_MOBILE);
            user_type = SharedPreference.getPref(getApplicationContext(), USER_TYPE);

            tv_profile_pic.setText(SharedPreference.getPref(getApplicationContext(), PROFILE_PIC));


            et_first_name.setText(first_name);
            et_last_name.setText(last_name);
            et_designation.setText(designation);
            et_company_name.setText(company_name);
            et_city.setText(city);
            et_email.setText(email);
            et_mobile.setText(mobile);

            if (SharedPreference.getPref(getApplicationContext(), KEY_PROFILE_PIC) != null) {
                Glide.with(getApplicationContext())
                        .load(SharedPreference.getPref(getApplicationContext(), KEY_PROFILE_PIC))
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:

                first_name = et_first_name.getText().toString().trim();
                last_name = et_last_name.getText().toString().trim();
                designation = "";
                company_name = et_organisation.getText().toString().trim();
                city = et_pincode.getText().toString().trim();
                email = et_emailid.getText().toString().trim();
                mobile = et_mobno.getText().toString().trim();
                profile_pic = tv_profile_pic.getText().toString();

                sap_code = et_sapcode.getText().toString();
               // associated_since = et_aspociated.getText().toString();


                no_of_technician = "";
                pincode = "";
                license = "";
                specialization = "";
                turnover = "";
                alternate_no = "";

                if (connectionDetector.isConnectingToInternet()) {
                    saveProfile(first_name,
                            last_name,
                            designation,
                            company_name,
                            city,
                            email,
                            mobile,
                            profile_pic,
                            alternate_no,
                            user_type,
                            sap_code,
                            associated_since,
                            no_of_pco_served);
                   /* updateProfile(
                            first_name,
                            last_name,
                            designation,
                            company_name,
                            city,
                            email,
                            mobile,
                            profile_pic,
                            alternate_no,
                            user_type,
                            sap_code,
                            associated_since,
                            no_of_pco_served*//*, no_of_pco_served, no_of_technician, pincode, license, specialization, turnover*//*);*/
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
//                        CommonFunction.showBackgroundImage(this, ll_main);
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

        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }


    public void updateProfile(final String first_name, final String last_name, final String designation,
                              final String company_name, final String city, final String email, final String mobile, final String profile_pic,final String alternate_no,
                              final String user_type,final String sap_code,final String associated_since, final String no_of_pco_served) {
        profileActivityViewModel.validation(first_name, last_name, designation, company_name,city);
        profileActivityViewModel.getIsValid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.isEmpty()) {
                    btn_save.setClickable(false);
                    btn_save.setEnabled(false);
                    profileActivityViewModel.updateProfile(api_token, event_id, first_name, last_name, designation, city,
                            email, mobile, company_name, profile_pic,user_type, no_of_pco_served,associated_since);
                    profileActivityViewModel.UpdateProfileDetails().observe(ProfileActivity.this, new Observer<Profile>() {
                        @Override
                        public void onChanged(final Profile profile) {
                            if (profile != null) {
                                if (profile.getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                    Utility.createShortSnackBar(ll_main, profile.getHeader().get(0).getMsg());

                                    String strEventList = profile.getProfileDetailsEncrypted();
                                    RefreashToken refreashToken = new RefreashToken(ProfileActivity.this);
                                    String data = refreashToken.decryptedData(strEventList);
                                    JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                    final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>(){}.getType());

                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(KEY_FNAME, profileDetails.get(0).getFirst_name());
                                    map.put(KEY_LNAME, profileDetails.get(0).getLast_name());
                                    map.put(KEY_EMAIL, profileDetails.get(0).getEmail());
                                    map.put(KEY_PASSWORD, "");
                                    map.put(KEY_DESIGNATION, profileDetails.get(0).getDesignation());
                                    map.put(KEY_COMPANY, profileDetails.get(0).getCompany_name());
                                    map.put(KEY_MOBILE, profileDetails.get(0).getMobile());
                                    //map.put(KEY_TOKEN, "");
                                    map.put(KEY_CITY, profileDetails.get(0).getCity());
                                    //map.put(KEY_GCM_ID, "");
                                    map.put(KEY_PROFILE_PIC,profileDetails.get(0).getProfile_picture());
                                    map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                    map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                    map.put(IS_LOGIN, "true");
                                    SharedPreference.putPref(ProfileActivity.this, map);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            profileActivityViewModel.openMainActivity(ProfileActivity.this);
                                            profileActivityViewModel.updateProfileFlag(ProfileActivity.this, event_id, profileDetails.get(0).getAttendee_id());
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

    public void saveProfile(final String first_name, final String last_name, final String designation,
                            final String company_name, final String city, final String email, final String mobile, final String profile_pic,final String alternate_no,
                            final String user_type,final String sap_code,final String associated_since, final String no_of_pco_served) {

        if(!profile_pic.isEmpty()) {
            File file = new File(profile_pic);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), file);
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
        }


        RequestBody mEvent_id = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody mFirst_name = RequestBody.create(MediaType.parse("text/plain"), first_name);
        RequestBody mLast_name = RequestBody.create(MediaType.parse("text/plain"), last_name);
        RequestBody mDesignation = RequestBody.create(MediaType.parse("text/plain"), designation);
        RequestBody mCity = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody mEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody mCompany_name = RequestBody.create(MediaType.parse("text/plain"), company_name);
        RequestBody muser_type = RequestBody.create(MediaType.parse("text/plain"), user_type);
        RequestBody mno_of_pco = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody massociated = RequestBody.create(MediaType.parse("text/plain"), "1950");

        RequestBody altno1 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody altno2 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody altno3 = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mstate = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mnooftect = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mspecilization = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mturnOver = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody mPincode = RequestBody.create(MediaType.parse("text/plain"), "");

        if (body == null) {

            ApiUtils.getAPIService().updateProfile(api_token,mEvent_id,muser_type,/*mFirst_name,mLast_name,mDesignation,mCity,mEmail,mMobile,altno1,altno2,altno3,mCompany_name,mstate,mnooftect,mspecilization,mturnOver, mPincode,*/ massociated, mno_of_pco).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response != null) {
                            if ( response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfileActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>(){}.getType());

                                HashMap<String, String> map = new HashMap<>();
                                map.put(KEY_FNAME, profileDetails.get(0).getFirst_name());
                                map.put(KEY_LNAME, profileDetails.get(0).getLast_name());
                                map.put(KEY_EMAIL, profileDetails.get(0).getEmail());
                                map.put(KEY_PASSWORD, "");
                                map.put(KEY_DESIGNATION, profileDetails.get(0).getDesignation());
                                map.put(KEY_COMPANY, profileDetails.get(0).getCompany_name());
                                map.put(KEY_MOBILE, profileDetails.get(0).getMobile());
                                //map.put(KEY_TOKEN, "");
                                map.put(KEY_CITY, profileDetails.get(0).getCity());
                                //map.put(KEY_GCM_ID, "");
                                map.put(KEY_PROFILE_PIC,profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(IS_LOGIN, "true");
                                SharedPreference.putPref(ProfileActivity.this, map);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfileActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfileActivity.this, "1", profileDetails.get(0).getAttendee_id());
                                    }
                                }, 500);

                            } else {
                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());
                            }
                        } else {
                            Utility.createShortSnackBar(ll_main, "failure..!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    Log.e("hit", "Low network or no network");
                    // Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            ApiUtils.getAPIService().updateProfile(api_token,
                    mEvent_id,
                    muser_type/*,mFirst_name,mLast_name,mDesignation,mCity,mEmail,mMobile,altno1,altno2,altno3,mCompany_name,mstate,mnooftect,mspecilization,mturnOver, mPincode*/,
                    massociated,
                    mno_of_pco/*,
                    body*/).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response.body() != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfileActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>(){}.getType());

                                HashMap<String, String> map = new HashMap<>();
                                map.put(KEY_FNAME, profileDetails.get(0).getFirst_name());
                                map.put(KEY_LNAME, profileDetails.get(0).getLast_name());
                                map.put(KEY_EMAIL, profileDetails.get(0).getEmail());
                                map.put(KEY_PASSWORD, "");
                                map.put(KEY_DESIGNATION, profileDetails.get(0).getDesignation());
                                map.put(KEY_COMPANY, profileDetails.get(0).getCompany_name());
                                map.put(KEY_MOBILE, profileDetails.get(0).getMobile());
                                //map.put(KEY_TOKEN, "");
                                map.put(KEY_CITY, profileDetails.get(0).getCity());
                                //map.put(KEY_GCM_ID, "");
                                map.put(KEY_PROFILE_PIC,profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(IS_LOGIN, "true");
                                SharedPreference.putPref(ProfileActivity.this, map);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfileActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfileActivity.this, "1", profileDetails.get(0).getAttendee_id());
                                    }
                                }, 500);
                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());


                            } else {
                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());
                            }
                        } else {
                            Utility.createShortSnackBar(ll_main, "failure..!");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    Log.e("hit", "Low network or no network");
                    // Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    public void setDynamicColor()
    {
        btn_save.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        ll_bg.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        view_down.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        btn_save.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);

        iv_first_name.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_last_name.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_designation.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_company_name.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_city.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_email.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);
        iv_mobile.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)), PorterDuff.Mode.SRC_ATOP);

        et_first_name.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_last_name.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_designation.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_company_name.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_city.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_email.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_mobile.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        associated_since = parent.getItemAtPosition(position).toString();
      //  Toast.makeText(this, "YOUR SELECTION IS : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}