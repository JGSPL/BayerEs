package com.procialize.bayer2020.ui.storelocator.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.storelocator.model.ClusterMarkerLocation;
import com.procialize.bayer2020.ui.storelocator.model.distributerProfile;
import com.procialize.bayer2020.ui.storelocator.model.distributer_item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;

public class StorelocatorMoveActivity  extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener {
    private Marker myMarker;
    Dialog dialog;

    private String api_token;
    ImageView iv_back;
    protected GoogleMap map;
    LocationManager locationManager;
    ClusterManager<ClusterMarkerLocation> clusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        new RefreashToken(StorelocatorMoveActivity.this).callGetRefreashToken(StorelocatorMoveActivity.this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        marshmallowGPSPremissionCheck();


        clusterManager = new ClusterManager<ClusterMarkerLocation>(this, map);

        setUpMapIfNeeded();


    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    marshmallowGPSPremissionCheck();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void marshmallowGPSPremissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && StorelocatorMoveActivity.this.checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && StorelocatorMoveActivity.this.checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            //   gps functions.
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
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

                        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                //Your code where exception occurs goes here...
                                setUpMap();


                                LatLngBounds boundsIndia = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

                                int padding = 0; // offset from edges of the map in pixels
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsIndia, padding);
                                map.animateCamera(cameraUpdate);



                                //Check code
                                // map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));
                                if (ActivityCompat.checkSelfPermission(StorelocatorMoveActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StorelocatorMoveActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                map.setMyLocationEnabled(true);


                            }
                        });

                        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition cameraPosition) {

                                //Log.i("centerLat",cameraPosition.target.latitude);

                               // Log.i("centerLong",cameraPosition.target.longitude);
                            }
                        });

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

        if(marker.getSnippet().equalsIgnoreCase("main")) {
            myMarker.hideInfoWindow();
            marker.hideInfoWindow();

            map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            getDistributerNameList(api_token, "1", marker.getTitle());
            marker.setVisible(false);
        }else {
            marker.hideInfoWindow();
            myMarker.hideInfoWindow();
            try {

                CameraPosition target = CameraPosition.builder().target(marker.getPosition()).zoom(10).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
                String str = marker.getSnippet();
                String[] splitStr = str.split("\\@+");
                openMoreDetails(splitStr[0].toString(), splitStr[1].toString(), splitStr[2].toString());

            }catch (Exception e){
                Toast.makeText(StorelocatorMoveActivity.this, "Invalid Lat-Long ", Toast.LENGTH_SHORT).show();

            }

        }


        // }
        return true;

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
                        RefreashToken refreashToken = new RefreashToken(StorelocatorMoveActivity.this);
                        String data = refreashToken.decryptedData(strEventList);

                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<distributer_item> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<distributer_item>>() {
                        }.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();


                        //Check code currrent location
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String provider = locationManager.getBestProvider(criteria, true);
                        if (ActivityCompat.checkSelfPermission(StorelocatorMoveActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StorelocatorMoveActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(provider);


/*
                        if (location != null) {

                            LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraPosition position = map.getCameraPosition();

                            try {
                                CameraPosition target1 = CameraPosition.builder().target(target).zoom(8).build();
                                map.moveCamera(CameraUpdateFactory.newCameraPosition(target1));
                                map.addCircle(new CircleOptions()
                                                .center(target)
                                                .radius(250000)
                                                .strokeWidth(0f)
                                        */
/*.fillColor(0x550000FF)*//*
);
                                map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here!").snippet("Consider yourself located"));


                            }catch (Exception e){

                            }

                            CameraPosition.Builder builder = new CameraPosition.Builder();
                            builder.zoom(8);
                            builder.target(target);

                            //  map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                        }
*/


                        ////////////////////////////////////////////
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

                                View marker = ((LayoutInflater)StorelocatorMoveActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                                TextView tv_marker_text = (TextView) marker.findViewById(R.id.tv_marker_text);
                                tv_marker_text.setText(profileDetails.get(i).getDistributor_count());

                                myMarker = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(
                                                Double.parseDouble(profileDetails.get(i).getLatitude()),
                                                Double.parseDouble(profileDetails.get(i).getLongitude())))
                                        .title(profileDetails.get(i).getState())
                                        .snippet("main")
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(StorelocatorMoveActivity.this, marker))));
                                // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                map.setOnMarkerClickListener(StorelocatorMoveActivity.this);
                                myMarker.hideInfoWindow();



                            }


                        }
                    }catch (Exception e){
                        Toast.makeText(StorelocatorMoveActivity.this, response.body().getHeader().get(0).getMsg(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(StorelocatorMoveActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                try {
                    Toast.makeText(StorelocatorMoveActivity.this, "Failure", Toast.LENGTH_SHORT).show();
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
                        RefreashToken refreashToken = new RefreashToken(StorelocatorMoveActivity.this);
                        String data = refreashToken.decryptedData(strEventList);

                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<distributerProfile> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<distributerProfile>>() {
                        }.getType());

                        // List<ProfileDetails> profileDetails = profile.getProfileDetails();

                        if (profileDetails.size() > 0) {


                            for (int i = 0; i < profileDetails.size(); i++) {
                                // Create a marker for each city in the JSON data.

                                View marker = ((LayoutInflater)StorelocatorMoveActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
                                TextView tv_marker_text = (TextView) marker.findViewById(R.id.tv_marker_text);
                                tv_marker_text.setText("1");

                                map.addMarker(new MarkerOptions()
                                        .title(profileDetails.get(i).getState())
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(StorelocatorMoveActivity.this, marker)))
                                        .snippet(profileDetails.get(i).getCompany_name()+ "@" + profileDetails.get(i).getMobile()+ "@" + profileDetails.get(i).getAddress())
                                        .position(new LatLng(
                                                Double.parseDouble(profileDetails.get(i).getLatitude()),
                                                Double.parseDouble(profileDetails.get(i).getLongitude())
                                        )));

                                //marker.hideInfoWindow();



                                // clusterManager.addItem( new ClusterMarkerLocation( new LatLng(  Double.parseDouble(profileDetails.get(i).getLatitude()), Double.parseDouble(profileDetails.get(i).getLongitude() ) )));



                            }


                        }
                    }catch (Exception e){
                        Toast.makeText(StorelocatorMoveActivity.this, response.body().getHeader().get(0).getMsg(), Toast.LENGTH_SHORT).show();


                    }

                } else {
                    Toast.makeText(StorelocatorMoveActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                try {
                    Toast.makeText(StorelocatorMoveActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void openMoreDetails(final String company, final String mobile, final String address) {


        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_maptext);


        TextView tvCompanyName = dialog.findViewById(R.id.tvCompanyName);
        TextView tvMobile = dialog.findViewById(R.id.tvMobile);
        TextView tvAddress = dialog.findViewById(R.id.tvAddress);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        TextView tvcallnow = dialog.findViewById(R.id.tvcallnow);
        TextView tvmessageWh = dialog.findViewById(R.id.tvmessageWh);

        tvCompanyName.setText(company);
        tvMobile.setText("Mobile: "+ mobile);
        tvAddress.setText("Address: "+ address);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        tvcallnow.setText("Call Now: "+mobile);

        if(tvcallnow != null){
            Linkify.addLinks(tvcallnow, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter,Linkify.sPhoneNumberTransformFilter);
            tvcallnow.setMovementMethod(LinkMovementMethod.getInstance());
        }

        tvmessageWh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String contact =  "+91"+  mobile; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = StorelocatorMoveActivity.this.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(StorelocatorMoveActivity.this
                            , "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        dialog.show();

    }
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Toast.makeText(StorelocatorMoveActivity.this,  String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
