package com.procialize.bayer2020.ui.profile.view;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.profile.model.FetchPincode;
import com.procialize.bayer2020.ui.profile.model.Pincode_item;
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

//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

public class ProfilePCOActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 101;
    RelativeLayout ll_main;
    LinearLayout ll_aspociated, ll_sapcode, ll_location, ll_emailid, ll_alternetmobno2, ll_alternetmobno3,
            ll_alternetmobno, ll_organisation, ll_name, ll_last_name, ll_designation, ll_company_name,
            ll_city, ll_email, ll_mobno, ll_bg;
    EditText et_aspociated, et_sapcode, et_pincode, et_emailid, et_alternetmobno, et_organisation, et_first_name, et_last_name, et_designation, et_company_name, et_city, et_email, et_mobile,
            et_state, et_alternetmobno2, et_alternetmobno3;
    ImageView iv_first_name, iv_last_name, iv_designation, iv_company_name, iv_city, iv_email, iv_mobile;
    ImageView iv_profile, iv_change_profile, iv_back;
    RadioGroup radiogroupPCO;
    RadioButton radioButton6, radioButton5, radioButton4, radioButton3, radioButton2, radioButton1;
    View view_down;
    TextView tv_profile_pic, tv_header, txtaltno, et_mobno;
    Button btn_save;
    ProfileActivityViewModel profileActivityViewModel;
    String event_id, profile_pic = "";
    ProgressBar progressView;
    private String userChoosenTask = "";
    ConnectionDetector connectionDetector;
    UCrop.Options options;
    File file;
    Spinner spinner;
    boolean isCheckeddesignation = false;
    String api_token, first_name, last_name, designation, company_name, city, email,
            mobile, is_god, alternate_no, alternate_no2, alternate_no3, license, pincode = "",
            no_of_technician = "", no_of_pco_served = "", associated_since = "", sap_code = "", attendee_id = "", user_type = "", state = "";
    MultipartBody.Part body;
    RadioGroup radiogroupPCOType, radiogroupDesig;
    RadioButton radioPCO, radioOwer, radioother, radioOwner, radioTech, radioManager;
    AutoCompleteTextView atv_pincode;
    TextView txtAnnualOrg, txtDomain, txttectcount, tvOrgnisation, tv_designation;
    CheckBox checkResPest, checkcomPest, checkTermite, checkMosquito;
    String technician, turnOver, altno1, altno2, altNO3;
    ArrayList arrSpecializtion = new ArrayList();
    boolean isCheckedPCO = false, isSpecializationChanged = false;
    String specialization = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pco);

        try {
            Intent intent = getIntent();
            CommonFunction.saveBackgroundImage(ProfilePCOActivity.this, intent.getStringExtra("eventBg"));
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
        ll_alternetmobno = findViewById(R.id.ll_alternetmobno);
        txtaltno = findViewById(R.id.txtaltno);
        ll_alternetmobno2 = findViewById(R.id.ll_alternetmobno2);
        ll_alternetmobno3 = findViewById(R.id.ll_alternetmobno3);
        txtAnnualOrg = findViewById(R.id.txtAnnualOrg);
        txtDomain = findViewById(R.id.txtDomain);
        txttectcount = findViewById(R.id.txttectcount);
        iv_back = findViewById(R.id.iv_back);
        view_down = findViewById(R.id.view_down);
        tvOrgnisation = findViewById(R.id.tvOrgnisation);
        tv_designation = findViewById(R.id.tv_designation);

        iv_profile = findViewById(R.id.iv_profile);
        iv_change_profile = findViewById(R.id.iv_change_profile);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_organisation = findViewById(R.id.et_organisation);
        et_mobno = findViewById(R.id.et_mobno);
        et_emailid = findViewById(R.id.et_emailid);
        et_sapcode = findViewById(R.id.et_sapcode);
        tv_profile_pic = findViewById(R.id.tv_profile_pic);
        radioButton5 = findViewById(R.id.radioButton5);
        radioButton6 = findViewById(R.id.radioButton6);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton1 = findViewById(R.id.radioButton1);
        radiogroupPCOType = findViewById(R.id.radiogroupPCOType);
        radioPCO = findViewById(R.id.radioPCO);
        radioOwer = findViewById(R.id.radioOwer);
        radioother = findViewById(R.id.radioother);
        radiogroupPCO = findViewById(R.id.radiogroupPCO);
        et_aspociated = findViewById(R.id.et_aspociated);
        et_state = findViewById(R.id.et_state);
        et_alternetmobno = findViewById(R.id.et_alternetmobno);
        et_alternetmobno2 = findViewById(R.id.et_alternetmobno2);
        et_alternetmobno3 = findViewById(R.id.et_alternetmobno3);

        et_pincode = findViewById(R.id.et_pincode);
        et_city = findViewById(R.id.et_city);
        checkResPest = findViewById(R.id.checkResPest);
        checkcomPest = findViewById(R.id.checkcomPest);
        checkTermite = findViewById(R.id.checkTermite);
        checkMosquito = findViewById(R.id.checkMosquito);

        radiogroupDesig = findViewById(R.id.radiogroupDesig);
        radioOwner = findViewById(R.id.radioOwner);
        radioTech = findViewById(R.id.radioTech);
        radioManager = findViewById(R.id.radioManager);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        iv_change_profile.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        atv_pincode = findViewById(R.id.atv_pincode);
        atv_pincode.setThreshold(0);

        checkMosquito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                String mosquito;
                isSpecializationChanged = true;
                // Check which checkbox was clicked
                if (checked) {
                    // Do your coding
                    mosquito = (String) checkMosquito.getTag();
                    arrSpecializtion.add("0");
                } else {
                    // Do your coding
                    mosquito = (String) checkMosquito.getTag();
                    arrSpecializtion.remove("0");
                }
            }
        });
        checkResPest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                String mosquito;
                isSpecializationChanged = true;
                // Check which checkbox was clicked
                if (checked) {
                    // Do your coding
                    mosquito = (String) checkResPest.getTag();
                    arrSpecializtion.add("1");
                } else {
                    // Do your coding
                    mosquito = (String) checkResPest.getTag();
                    arrSpecializtion.remove("1");
                }
            }
        });

        checkTermite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                String mosquito;
                isSpecializationChanged = true;
                // Check which checkbox was clicked
                if (checked) {
                    // Do your coding
                    mosquito = (String) checkTermite.getTag();
                    arrSpecializtion.add("3");
                } else {
                    // Do your coding
                    mosquito = (String) checkMosquito.getTag();
                    arrSpecializtion.remove("3");
                }
            }
        });

        checkcomPest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                String mosquito;
                isSpecializationChanged = true;
                // Check which checkbox was clicked
                if (checked) {
                    // Do your coding
                    mosquito = (String) checkMosquito.getTag();
                    arrSpecializtion.add("2");
                } else {
                    // Do your coding
                    mosquito = (String) checkMosquito.getTag();
                    arrSpecializtion.remove("2");
                }
            }
        });


        radiogroupPCO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                isCheckedPCO = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isCheckedPCO) {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    no_of_pco_served = checkedRadioButton.getText().toString();
                    if (no_of_pco_served.equalsIgnoreCase("Less than Rs. 10 lacs")) {
                        turnOver = "0";

                    } else if (no_of_pco_served.equalsIgnoreCase("Rs. 10 to 25 lacs")) {
                        turnOver = "1";
                    } else if (no_of_pco_served.equalsIgnoreCase("Rs. 25 to 50 lacs")) {
                        turnOver = "2";
                    } else if (no_of_pco_served.equalsIgnoreCase("Rs. 50 lacs to 1 crore")) {
                        turnOver = "3";
                    } else if (no_of_pco_served.equalsIgnoreCase("Rs. 1 crore to 1.5 crore")) {
                        turnOver = "4";
                    } else if (no_of_pco_served.equalsIgnoreCase("Above Rs. 2 crore")) {
                        turnOver = "5";
                    } else {
                        turnOver = "";
                    }

                }
            }
        });

        radiogroupDesig.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                isCheckeddesignation = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isCheckeddesignation) {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    String des = checkedRadioButton.getText().toString();
                    if (des.equalsIgnoreCase("Owner")) {
                        designation = "0";

                    } else if (des.equalsIgnoreCase("Technician")) {
                        designation = "1";
                    } else if (des.equalsIgnoreCase("Manager/Admin/Other")) {
                        designation = "2";
                    }
                }
            }
        });

        radiogroupPCOType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    String user_typeText = checkedRadioButton.getText().toString();
                    if (user_typeText.equalsIgnoreCase("Pest Control Operator")) {
                        ll_alternetmobno.setVisibility(View.VISIBLE);

                        ll_alternetmobno2.setVisibility(View.VISIBLE);
                        ll_alternetmobno3.setVisibility(View.VISIBLE);
                        ll_organisation.setVisibility(View.VISIBLE);
                        txtaltno.setVisibility(View.VISIBLE);
                        ll_aspociated.setVisibility(View.VISIBLE);
                        radiogroupPCO.setVisibility(View.VISIBLE);
                        txtAnnualOrg.setVisibility(View.VISIBLE);
                        txtDomain.setVisibility(View.VISIBLE);
                        ll_sapcode.setVisibility(View.VISIBLE);
                        txttectcount.setVisibility(View.VISIBLE);
                        radiogroupDesig.setVisibility(View.VISIBLE);
                        tv_designation.setVisibility(View.VISIBLE);
                        tvOrgnisation.setVisibility(View.VISIBLE);
                        user_type = "PO";

                    } else if (user_typeText.equalsIgnoreCase("Home/office owner")) {
                        ll_alternetmobno.setVisibility(View.GONE);

                        ll_alternetmobno2.setVisibility(View.GONE);
                        ll_alternetmobno3.setVisibility(View.GONE);
                        txtaltno.setVisibility(View.GONE);
                        ll_aspociated.setVisibility(View.GONE);
                        radiogroupPCO.setVisibility(View.GONE);
                        txtAnnualOrg.setVisibility(View.GONE);
                        txtDomain.setVisibility(View.GONE);
                        ll_sapcode.setVisibility(View.GONE);
                        txttectcount.setVisibility(View.GONE);
                        ll_organisation.setVisibility(View.GONE);
                        tvOrgnisation.setVisibility(View.GONE);

                        radiogroupDesig.setVisibility(View.GONE);
                        tv_designation.setVisibility(View.GONE);
                        user_type = "HO";

                    } else {
                        ll_organisation.setVisibility(View.GONE);
                        tvOrgnisation.setVisibility(View.GONE);

                        ll_alternetmobno.setVisibility(View.GONE);
                        ll_alternetmobno2.setVisibility(View.GONE);
                        ll_alternetmobno3.setVisibility(View.GONE);
                        txtaltno.setVisibility(View.GONE);
                        ll_aspociated.setVisibility(View.GONE);
                        radiogroupPCO.setVisibility(View.GONE);
                        txtAnnualOrg.setVisibility(View.GONE);
                        txtDomain.setVisibility(View.GONE);
                        ll_sapcode.setVisibility(View.GONE);
                        txttectcount.setVisibility(View.GONE);
                        radiogroupDesig.setVisibility(View.GONE);
                        tv_designation.setVisibility(View.GONE);
                        user_type = "O";


                    }
                }
            }
        });


        atv_pincode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String searchString = atv_pincode.getText().toString();

                    if (!(searchString.equalsIgnoreCase("") || searchString.equalsIgnoreCase(null))) {

                        getPincode(searchString);
                    }
                }catch (Exception e){

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


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
                        RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                        String data = refreashToken.decryptedData(strEventList);
                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                        }.getType());

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
                            //specialization = profileDetails.get(0).getSpecialization();
                            turnOver = profileDetails.get(0).getTurnover();
                            is_god = profileDetails.get(0).getIs_god();
                            alternate_no = profileDetails.get(0).getAlternate_no();
                            alternate_no2 = profileDetails.get(0).getAlternate_no_2();
                            alternate_no3 = profileDetails.get(0).getAlternate_no_3();
                            state = profileDetails.get(0).getState();
                            // pincode = profileDetails.get(0).getPincode();
                            user_type = profileDetails.get(0).getUser_type();
                            // tv_profile_pic.setText(profileDetails.get(0).getProfile_picture());
                            String strSpecialization = profileDetails.get(0).getSpecialization();
                            if (profileDetails.get(0).getDesignation().equalsIgnoreCase("0")) {
                                radioOwner.setChecked(true);
                            } else if (profileDetails.get(0).getDesignation().equalsIgnoreCase("1")) {
                                radioTech.setChecked(true);
                            } else if (profileDetails.get(0).getDesignation().equalsIgnoreCase("2")) {
                                radioManager.setChecked(true);
                            }

                            if (profileDetails.get(0).getTurnover().equalsIgnoreCase("0")) {
                                radioButton1.setChecked(true);
                            } else if (profileDetails.get(0).getTurnover().equalsIgnoreCase("1")) {
                                radioButton2.setChecked(true);
                            } else if (profileDetails.get(0).getTurnover().equalsIgnoreCase("2")) {
                                radioButton3.setChecked(true);
                            } else if (profileDetails.get(0).getTurnover().equalsIgnoreCase("3")) {
                                radioButton4.setChecked(true);
                            } else if (profileDetails.get(0).getTurnover().equalsIgnoreCase("4")) {
                                radioButton5.setChecked(true);
                            } else if (profileDetails.get(0).getTurnover().equalsIgnoreCase("5")) {
                                radioButton6.setChecked(true);
                            }

                            specialization = profileDetails.get(0).getSpecialization();

                            if (specialization.contains("0"))
                                checkMosquito.setChecked(true);
                            if (specialization.contains("1"))
                                checkResPest.setChecked(true);
                            if (specialization.contains("2"))
                                checkcomPest.setChecked(true);
                            if (specialization.contains("3"))
                                checkTermite.setChecked(true);

                            et_first_name.setText(first_name);
                            et_last_name.setText(last_name);
                            et_mobno.setText(mobile);
                            et_aspociated.setText(associated_since);
                            et_sapcode.setText(no_of_technician);
                            et_organisation.setText(company_name);
                            et_emailid.setText(email);
                            if (alternate_no != null) {
                                et_alternetmobno.setText(alternate_no);
                            }
                            if (alternate_no2 != null) {
                                et_alternetmobno2.setText(alternate_no2);
                            }
                            if (alternate_no3 != null) {
                                et_alternetmobno3.setText(alternate_no3);
                            }
                            atv_pincode.setText(pincode);
                            if (pincode != null && !pincode.isEmpty()) {
                                getState(atv_pincode.getText().toString());
                            }

                            // et_state.setText(state);
                            if (user_type.equalsIgnoreCase("PO")) {
                                ((RadioButton) radiogroupPCOType.getChildAt(0)).setChecked(true);

                            } else if (user_type.equalsIgnoreCase("HO")) {
                                ((RadioButton) radiogroupPCOType.getChildAt(1)).setChecked(true);

                            } else if (user_type.equalsIgnoreCase("O")) {
                                ((RadioButton) radiogroupPCOType.getChildAt(2)).setChecked(true);

                            }


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
                    } else {
                        Toast.makeText(ProfilePCOActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    try {
                        Toast.makeText(ProfilePCOActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            });

        } else {
            first_name = SharedPreference.getPref(getApplicationContext(), KEY_FNAME);
            last_name = SharedPreference.getPref(getApplicationContext(), KEY_LNAME);
            designation = SharedPreference.getPref(getApplicationContext(), KEY_DESIGNATION);
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
                company_name = et_organisation.getText().toString().trim();
                //city = et_pincode.getText().toString().trim();
                email = et_emailid.getText().toString().trim();
                mobile = et_mobno.getText().toString().trim();
                profile_pic = tv_profile_pic.getText().toString();
                alternate_no = et_alternetmobno.getText().toString();
                altno2 = et_alternetmobno2.getText().toString();
                altNO3 = et_alternetmobno3.getText().toString();
                no_of_technician = et_sapcode.getText().toString();
                pincode = atv_pincode.getText().toString();
                city = et_city.getText().toString();
                state = et_state.getText().toString();

                if (connectionDetector.isConnectingToInternet()) {
                    if (user_type.equalsIgnoreCase("PO")) {
                        if (et_first_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();

                        } else if (et_last_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();

                        } else if (atv_pincode.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter pincode for proceed", Toast.LENGTH_SHORT).show();

                        } else if (et_emailid.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_emailid.getText().toString().trim()).matches()) {
                            Toast.makeText(this, "Please enter valid emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else if (et_organisation.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter company name for proceed", Toast.LENGTH_SHORT).show();

                        } else if (isCheckeddesignation == false) {
                            Toast.makeText(this, "Please select designation", Toast.LENGTH_SHORT).show();

                        } else if (isSpecializationChanged) {
                            if (arrSpecializtion.isEmpty()) {
                                Toast.makeText(this, "Please select any specialization", Toast.LENGTH_SHORT).show();

                            } else {
                                specialization = arrSpecializtion.toString();
                                specialization = specialization.substring(1, specialization.length() - 1);

                                btn_save.setEnabled(false);
                                btn_save.setClickable(false);

                                saveProfilePCO(first_name, last_name, designation, company_name, city, email, mobile, profile_pic, alternate_no,
                                        user_type, state, no_of_technician, specialization, turnOver, pincode, altno2,
                                        altNO3);
                            }
                        } else {
                            btn_save.setEnabled(false);
                            btn_save.setClickable(false);
                            saveProfilePCO(first_name, last_name, designation, company_name, city, email, mobile, profile_pic, alternate_no,
                                    user_type, state, no_of_technician, specialization, turnOver, pincode, altno2,
                                    altNO3);
                        }

                    } else if (user_type.equalsIgnoreCase("HO")) {
                        if (et_first_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();

                        } else if (et_last_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();

                        } else if (atv_pincode.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter pincode for proceed", Toast.LENGTH_SHORT).show();

                        } else if (et_emailid.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_emailid.getText().toString().trim()).matches()) {
                            Toast.makeText(this, "Please enter valid emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else {
                            btn_save.setEnabled(false);
                            btn_save.setClickable(false);
                            saveProfileHO(first_name, last_name, mobile, email, pincode, city, state, profile_pic, user_type);
                        }

                    } else {
                        if (et_first_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show();

                        } else if (et_last_name.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show();

                        } else if (atv_pincode.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter pincode for proceed", Toast.LENGTH_SHORT).show();

                        } else if (et_emailid.getText().toString().isEmpty()) {
                            Toast.makeText(this, "Please enter emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_emailid.getText().toString().trim()).matches()) {
                            Toast.makeText(this, "Please enter valid emailId for proceed", Toast.LENGTH_SHORT).show();

                        } else {
                            btn_save.setEnabled(false);
                            btn_save.setClickable(false);
                            saveProfileHO(first_name, last_name, mobile, email, pincode, city, state, profile_pic, user_type);
                        }
                    }
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
                boolean result = Utility.checkPermission(ProfilePCOActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {
                        boolean cameraResult = Utility.checkCameraPermission(ProfilePCOActivity.this);
                        if (cameraResult) {
                            profileActivityViewModel.cameraIntent(ProfilePCOActivity.this);
                        }
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result) {
                        profileActivityViewModel.galleryIntent(ProfilePCOActivity.this);
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

                Toast.makeText(ProfilePCOActivity.this, "Image selected",
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
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
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
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
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
                    file = new File(getRealPathFromURI(ProfilePCOActivity.this, tempUri));
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
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
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
                    profileActivityViewModel.cameraIntent(ProfilePCOActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //profileActivityViewModel.cameraIntent(ProfilePCOActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //profileActivityViewModel.cameraIntent(ProfilePCOActivity.this);
                    try {
                        Intent intent = getIntent();
                        CommonFunction.saveBackgroundImage(ProfilePCOActivity.this, intent.getStringExtra("eventBg"));
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

        startActivity(new Intent(ProfilePCOActivity.this, MainActivity.class));
        finish();
    }


    public void saveProfilePCO(final String first_name, final String last_name, final String designation,
                               final String company_name, final String city, final String email, final String mobile, final String profile_pic, final String alternate_no,
                               final String user_type, final String state, final String technician, final String specializations, final String turnOver, final String pincode, final String maltno2, final String maltno3) {

        if (!profile_pic.isEmpty()) {
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

        RequestBody altno1 = RequestBody.create(MediaType.parse("text/plain"), alternate_no);
        RequestBody altno2 = RequestBody.create(MediaType.parse("text/plain"), maltno2);
        RequestBody altno3 = RequestBody.create(MediaType.parse("text/plain"), maltno3);
        RequestBody mstate = RequestBody.create(MediaType.parse("text/plain"), state);
        RequestBody mnooftect = RequestBody.create(MediaType.parse("text/plain"), technician);
        RequestBody mspecilization = RequestBody.create(MediaType.parse("text/plain"), specializations);
        RequestBody mturnOver = RequestBody.create(MediaType.parse("text/plain"), turnOver);
        RequestBody mPincode = RequestBody.create(MediaType.parse("text/plain"), pincode);

        if (body == null) {

            ApiUtils.getAPIService().updateProfile(api_token, mEvent_id, muser_type, mFirst_name, mLast_name, mDesignation, mCity, mEmail, mMobile,
                    altno1, altno2, altno3, mCompany_name, mstate, mnooftect, mspecilization, mturnOver, mPincode).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                btn_save.setEnabled(true);
                                btn_save.setClickable(true);

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                                }.getType());

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
                                map.put(USER_TYPE, "PO");

                                map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(IS_LOGIN, "true");
                                SharedPreference.putPref(ProfilePCOActivity.this, map);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
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
                    muser_type, mFirst_name, mLast_name, mDesignation, mCity, mEmail, mMobile, altno1, altno2, altno3, mCompany_name, mstate, mnooftect, mspecilization, mturnOver, mPincode,
                    body).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response.body() != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                btn_save.setEnabled(true);
                                btn_save.setClickable(true);

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                                }.getType());

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
                                map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(IS_LOGIN, "true");
                                map.put(USER_TYPE, "PO");

                                SharedPreference.putPref(ProfilePCOActivity.this, map);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
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

    public void saveProfileHO(final String first_name, final String last_name, final String mobile, final String email,
                              final String pincode, final String city, final String state, final String profile_pic,
                              final String user_type) {

        if (!profile_pic.isEmpty()) {
            File file = new File(profile_pic);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/png"), file);
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
        }


        RequestBody mEvent_id = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody mFirst_name = RequestBody.create(MediaType.parse("text/plain"), first_name);
        RequestBody mLast_name = RequestBody.create(MediaType.parse("text/plain"), last_name);
        RequestBody mCity = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody mEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody mMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody muser_type = RequestBody.create(MediaType.parse("text/plain"), user_type);
        RequestBody mstate = RequestBody.create(MediaType.parse("text/plain"), state);
        RequestBody mpincode = RequestBody.create(MediaType.parse("text/plain"), pincode);
        RequestBody mdes = RequestBody.create(MediaType.parse("text/plain"), "");

        if (body == null) {

            ApiUtils.getAPIService().updateProfile(api_token, mEvent_id, muser_type, mFirst_name, mLast_name, mEmail, mMobile, mpincode, mCity, mstate).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                btn_save.setEnabled(true);
                                btn_save.setClickable(true);

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                                }.getType());

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
                                map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(USER_TYPE, profileDetails.get(0).getUser_type());

                                map.put(IS_LOGIN, "true");
                                SharedPreference.putPref(ProfilePCOActivity.this, map);
                              final Handler handler = new Handler();
                                /*  handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
                                    }
                                }, 500);*/
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
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

            ApiUtils.getAPIService().updateProfile(api_token, mEvent_id, muser_type, mFirst_name, mLast_name, mEmail, mMobile, mpincode, mCity, mstate, body).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    try {

                        if (response.body() != null) {
                            if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                btn_save.setEnabled(true);
                                btn_save.setClickable(true);

                                Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());

                                String strEventList = response.body().getProfileDetailsEncrypted();
                                RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                                final ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                                }.getType());

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
                                map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                                map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                                map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                                map.put(IS_LOGIN, "true");
                                SharedPreference.putPref(ProfilePCOActivity.this, map);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
                                    }
                                }, 500);
                                /*handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        profileActivityViewModel.openMainActivity(ProfilePCOActivity.this);
                                        profileActivityViewModel.updateProfileFlag(ProfilePCOActivity.this, "1", profileDetails.get(0).getAttendee_id());
                                    }
                                }, 500);*/
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

    public void setDynamicColor() {
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

    void getPincode(String pin) {
        ApiUtils.getAPIService().PincodeList(api_token, event_id, pin).enqueue(new Callback<FetchPincode>() {
            @Override
            public void onResponse(Call<FetchPincode> call, Response<FetchPincode> response) {
                if (response.isSuccessful()) {
                    try {
                        String strEventList = response.body().getDetail();
                        RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                        String data = refreashToken.decryptedData(strEventList);

                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<Pincode_item> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<Pincode_item>>() {
                        }.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();
                        ArrayList pincodeData = new ArrayList();

                        if (profileDetails.size() > 0) {

                            for (int i = 0; i < profileDetails.size(); i++) {
                                pincodeData.add(profileDetails.get(i).getPincode());

                            }
                            //Creating the instance of ArrayAdapter containing list of language names
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (ProfilePCOActivity.this, android.R.layout.select_dialog_item, pincodeData);

                            atv_pincode.setAdapter(adapter);

                            atv_pincode.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                                        long id) {
                                    Toast.makeText(ProfilePCOActivity.this, " selected", Toast.LENGTH_LONG).show();
                                    pincode = atv_pincode.getText().toString();

                                    if (!pincode.isEmpty() && pincode != null)
                                        getState(atv_pincode.getText().toString());
                                }
                            });

                        }
                    } catch (Exception e) {
                        Toast.makeText(ProfilePCOActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        atv_pincode.setText("");

                    }

                } else {
                    Toast.makeText(ProfilePCOActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchPincode> call, Throwable t) {
                try {
                    Toast.makeText(ProfilePCOActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    void getState(String pin) {
        ApiUtils.getAPIService().CityState(api_token, event_id, pin).enqueue(new Callback<FetchPincode>() {
            @Override
            public void onResponse(Call<FetchPincode> call, Response<FetchPincode> response) {
                if (response.isSuccessful()) {
                    String strEventList = response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(ProfilePCOActivity.this);
                    String data = refreashToken.decryptedData(strEventList);
                    //   JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                   /* ArrayList<Pincode_item> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<Pincode_item>>() {
                    }.getType());*/
                    Pincode_item pincodeLists = new Gson().fromJson(data, new TypeToken<Pincode_item>() {
                    }.getType());
                    if (pincodeLists != null) {
                        et_city.setText(pincodeLists.getCity());
                        et_state.setText(pincodeLists.getState());

                        city = et_city.getText().toString();
                        state = et_state.getText().toString();
                    }

                    /*if (pincodeLists.size() > 0) {
                        ArrayList stateData = new ArrayList();
                        ArrayList cityData = new ArrayList();

                        for(int i = 0; i<profileDetails.size();i++){
                            stateData.add(profileDetails.get(i).getState());
                            cityData.add(profileDetails.get(i).getCity());


                        }
                        et_city.setText(cityData.get(0).toString());
                        et_state.setText(stateData.get(0).toString());



                    }*/

                } else {
                    Toast.makeText(ProfilePCOActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchPincode> call, Throwable t) {
                try {
                    Toast.makeText(ProfilePCOActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }


}
