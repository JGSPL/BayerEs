package com.procialize.eventapp.ui.eventinfo.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.eventinfo.model.EventInfo;
import com.procialize.eventapp.ui.eventinfo.model.EventInfoDetails;
import com.procialize.eventapp.ui.eventinfo.viewmodel.EventInfoViewModel;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_INFO_MEDIA_PATH;

public class EventInfoActivity extends AppCompatActivity {

    EventInfoViewModel eventInfoViewModel;
    TextView tv_header,tv_date,tv_address,tv_Description;
    LinearLayout ll_main;
    ImageView iv_event,iv_back;
    ProgressBar progressView;
    String event_id;
    String api_token = "";
    SupportMapFragment mapFragment;

    public static EventInfoActivity newInstance() {
        return new EventInfoActivity();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_eventinfo);

        eventInfoViewModel = ViewModelProviders.of(this).get(EventInfoViewModel.class);



        iv_back = findViewById(R.id.iv_back);
        tv_header = findViewById(R.id.tv_header);
        tv_date = findViewById(R.id.tv_date);
        tv_address = findViewById(R.id.tv_address);
        ll_main = findViewById(R.id.ll_main);
        progressView = findViewById(R.id.progressView);
        tv_Description = findViewById(R.id.tv_Description);
        iv_event = findViewById(R.id.iv_event);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        CommonFunction.showBackgroundImage(EventInfoActivity.this, ll_main);

        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            eventInfoViewModel.getEvent(api_token, event_id);
            eventInfoViewModel.getEventInfo().observeForever( new Observer<EventInfo>() {
                @Override
                public void onChanged(EventInfo like) {
                    if (like != null) {
                        String strCommentList =like.getDetail();
                        RefreashToken refreashToken = new RefreashToken(EventInfoActivity.this);
                        String data = refreashToken.decryptedData(strCommentList);
                       /* Gson gson = new Gson();
                        eventDataArray = gson.fromJson(data, new TypeToken<ArrayList<EventInfo>>() {
                        }.getType());*/
                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<EventInfoDetails> eventInfos = new Gson().fromJson(jsonArray, new TypeToken<List<EventInfoDetails>>(){}.getType());
                        if(eventInfos.size()>0) {
                           String strEvent_id = eventInfos.get(0).getEvent_id();
                           String strEvent_name = eventInfos.get(0).getEvent_name();
                           String strEvent_start_date = eventInfos.get(0).getEvent_start_date();
                           String strEvent_end_date = eventInfos.get(0).getEvent_end_date();
                           String strEvent_description = eventInfos.get(0).getEvent_description();
                           final String strEvent_location = eventInfos.get(0).getEvent_location();
                           String strEvent_city = eventInfos.get(0).getEvent_city();
                           final String strEvent_latitude = eventInfos.get(0).getEvent_latitude();
                           final String strEvent_longitude = eventInfos.get(0).getEvent_longitude();
                           String strEvent_image = eventInfos.get(0).getEvent_image();
                           String strHeader_image = eventInfos.get(0).getHeader_image();
                           String strEackground_image = eventInfos.get(0).getBackground_image();

                            tv_header.setText(strEvent_name);
                            tv_date.setText(CommonFunction.convertEventDate(strEvent_start_date)+" - "+CommonFunction.convertEventDate(strEvent_end_date));
                            tv_address.setText(strEvent_location);
                            tv_Description.setText(strEvent_description);

                            String strFilePath1 = like.getFile_path();
                            String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(strFilePath1));
                            String mediaPath1 = strFilePath.replace("\\", "");
                            HashMap<String, String> map = new HashMap<>();
                            map.put(EVENT_INFO_MEDIA_PATH, mediaPath1);
                            Glide.with(getApplicationContext())
                                    .load(mediaPath1+strEvent_image)
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
                                    }).into(iv_event);

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    LatLng currentLocation = new LatLng(Double.parseDouble(strEvent_latitude),Double.parseDouble(strEvent_longitude));
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(currentLocation)
                                            .title(strEvent_location));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                    // Zoom in, animating the camera.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                }
                            });

                        }
                    }

                    if (eventInfoViewModel != null && eventInfoViewModel.getEventInfo().hasObservers()) {
                        eventInfoViewModel.getEventInfo().removeObservers(EventInfoActivity.this);
                    }
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}