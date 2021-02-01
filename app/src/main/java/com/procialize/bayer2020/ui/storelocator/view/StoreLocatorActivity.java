package com.procialize.bayer2020.ui.storelocator.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.EULA.EulaActivity;
import com.procialize.bayer2020.ui.EULA.viewmodel.EulaViewModel;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.storelocator.model.ClusterMarkerLocation;
import com.procialize.bayer2020.ui.storelocator.model.distributerProfile;
import com.procialize.bayer2020.ui.storelocator.model.distributer_item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class StoreLocatorActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener
{
    private Marker myMarker;

    private  String api_token;
    ImageView iv_back;
    protected GoogleMap map;
    ClusterManager<ClusterMarkerLocation> clusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        new RefreashToken(StoreLocatorActivity.this).callGetRefreashToken(StoreLocatorActivity.this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpMapIfNeeded();
       clusterManager = new ClusterManager<ClusterMarkerLocation>( this, map );


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googlemap) {
                    map = googlemap;
                    if (map != null) {
                        setUpMap();

                        /*LatLng norway = new LatLng(62.455102,17.345599);

                        CameraPosition target = CameraPosition.builder().target(norway).zoom(14).build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(target));*/
                        LatLngBounds boundsIndia = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
                        int padding = 0; // offset from edges of the map in pixels
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsIndia, padding);
                        map.animateCamera(cameraUpdate);


                    }



                }
            });

        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

       /* if (marker.equals(myMarker))
        {*/
            //handle click here
            getDistributerNameList(api_token,"1",marker.getTitle());

       // }
        return false;

    }

    private void setUpMap() {
        new Thread(new Runnable() {
            public void run() {
                getDistributerList(api_token,"1");
            }
        }).start();
    }

    void getDistributerList(final String api_token, String id) {
        ApiUtils.getAPIService().DistributorCountFetch(api_token,"1").enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                if (response.isSuccessful()) {
                    try {
                        String strEventList = response.body().getDetail();
                        RefreashToken refreashToken = new RefreashToken(StoreLocatorActivity.this);
                        String data = refreashToken.decryptedData(strEventList);

                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<distributer_item> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<distributer_item>>() {
                        }.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();

                        if (profileDetails.size() > 0) {


                            for (int i = 0; i < profileDetails.size(); i++) {
                                // Create a marker for each city in the JSON data.
                               /* map.addMarker(new MarkerOptions()
                                        .title(profileDetails.get(i).getState())
                                        .snippet(profileDetails.get(0).getDistributor_count() +
                                                profileDetails.get(0).getCity())
                                        //.snippet(Integer.toString(jsonObj.getInt("population")))
                                        .position(new LatLng(
                                                Double.parseDouble(profileDetails.get(i).getLatitude()),
                                                Double.parseDouble(profileDetails.get(i).getLongitude())
                                        ))
                                );*/
                                myMarker = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(
                                                Double.parseDouble(profileDetails.get(i).getLatitude()),
                                                Double.parseDouble(profileDetails.get(i).getLongitude())))
                                        .title(profileDetails.get(i).getState())
                                        .snippet(profileDetails.get(0).getDistributor_count() +
                                                profileDetails.get(0).getCity())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                map.setOnMarkerClickListener(StoreLocatorActivity.this);


                            }


                        }
                    }catch (Exception e){
                        Toast.makeText(StoreLocatorActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(StoreLocatorActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                try {
                    Toast.makeText(StoreLocatorActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    void getDistributerNameList( String api_token,String id, String state) {
        ApiUtils.getAPIService().DistributorListFetch(api_token,"1",state).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                if (response.isSuccessful()) {
                    try {
                        String strEventList = response.body().getDetail();
                        RefreashToken refreashToken = new RefreashToken(StoreLocatorActivity.this);
                        String data = refreashToken.decryptedData(strEventList);

                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<distributerProfile> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<distributerProfile>>() {
                        }.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();

                        if (profileDetails.size() > 0) {


                            for (int i = 0; i < profileDetails.size(); i++) {
                                // Create a marker for each city in the JSON data.
                                map.addMarker(new MarkerOptions()
                                        .title(profileDetails.get(i).getState())
                                        .snippet(profileDetails.get(0).getCity())
                                        //.snippet(Integer.toString(jsonObj.getInt("population")))
                                        .position(new LatLng(
                                                Double.parseDouble(profileDetails.get(i).getLatitude()),
                                                Double.parseDouble(profileDetails.get(i).getLongitude())
                                        ))
                                );

                               // clusterManager.addItem( new ClusterMarkerLocation( new LatLng(  Double.parseDouble(profileDetails.get(i).getLatitude()), Double.parseDouble(profileDetails.get(i).getLongitude() ) )));



                            }


                        }
                    }catch (Exception e){
                        Toast.makeText(StoreLocatorActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(StoreLocatorActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                try {
                    Toast.makeText(StoreLocatorActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }



}

