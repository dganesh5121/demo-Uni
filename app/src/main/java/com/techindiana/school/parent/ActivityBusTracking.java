package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techindiana.school.parent.Module.DriverInfo;
import com.techindiana.school.parent.Module.DriverLastLocationResp;
import com.techindiana.school.parent.Module.DriverResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityBusTracking extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public ActionBar actionBar;

    private Context mContext;
    private Retrofit retrofit;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;

    Spinner  spiDriverName;
    String  spiDriverVal = "",busVal = "";
    private Marker positionMarker;
    ArrayList<DriverInfo> driverInfos;
    final ArrayList<String> arrayDriver = new ArrayList<String>();
String titel="", dec="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_tracking);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityBusTracking.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mBusTracking));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityBusTracking.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
            } else {
                showGPSDisabledAlertToUser();
            }
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            spiDriverName = (Spinner) findViewById(R.id.busTSpiDId);
            spiDriverName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (position == 0) {
                            busVal="";
                            spiDriverVal = "";
                            titel="";
                        } else {
                            spiDriverVal = driverInfos.get(position - 1).getDriverId().toString();
                            busVal= driverInfos.get(position - 1).getBus_id().toString();
                            titel= driverInfos.get(position - 1).getNumber();
                            dec=  driverInfos.get(position - 1).getDriverName()+"  "+       driverInfos.get(position - 1).getDriverNumber()+" \n "+
                                    driverInfos.get(position - 1).getAssistantName()+"  "+   driverInfos.get(position - 1).getAssistantMobile();
                            getDriverLocation();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            driverName();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Subject Details...
    private void driverName() {
        try {
            showProgress();

            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<DriverResp> call = restInterface.getDriver(Constant.apiKey ,Constant.childSchoolId,Constant.userID           );
            call.enqueue(new Callback<DriverResp>() {

                @Override
                public void onResponse(Call<DriverResp> call, Response<DriverResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            driverInfos = (ArrayList<DriverInfo>) response.body().getDriverInfo();
                            if (driverInfos.size() > 0) {
                                arrayDriver.clear();
                                arrayDriver.add("Select Driver");
                                for (int i = 0; i < driverInfos.size(); i++) {
                                    arrayDriver.add(driverInfos.get(i).getDriverName()+" / "+driverInfos.get(i).getAssistantName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityBusTracking.this,
                                        R.layout.list_item_facility, arrayDriver);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiDriverName.setAdapter(adapterarrayCategory);
                                spiDriverName.setSelection(0);
                            }
                        } else {
                            Toast.makeText(ActivityBusTracking.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<DriverResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }

    //TODO: Web call for Class Details...
    private void getDriverLocation() {
        try {
            showProgress();
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);

            Call<DriverLastLocationResp> call = restInterface.getDriverLastLocation(Constant.apiKey,Constant.childSchoolId,Constant.userID,
                    spiDriverVal,busVal);
            call.enqueue(new Callback<DriverLastLocationResp>() {

                @Override
                public void onResponse(Call<DriverLastLocationResp> call, Response<DriverLastLocationResp> response) {

                    if (response.isSuccessful()) {
                        if (positionMarker != null) {
                            positionMarker.remove();
                        }
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {

                            final MarkerOptions positionMarkerOptions = new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(response.body().getDriverLastLocation().getLatitude()), Double.parseDouble(response.body().getDriverLastLocation().getLongitude())))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_marker))
                                    .title(titel)
                                    .snippet(String.valueOf(Html.fromHtml(dec)))
                                    .anchor(0.5f, 0.5f);
                            positionMarker = mMap.addMarker(positionMarkerOptions);

                        } else {
                            Toast.makeText(ActivityBusTracking.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<DriverLastLocationResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }



    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
           mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.radio_button_active));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
      //  Toast.makeText(this, ""+latLng.latitude+"  "+latLng.longitude, Toast.LENGTH_SHORT).show();
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //TODO on navigation icon click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.actionLib:
                intent = new Intent(ActivityBusTracking.this, ActivityLibBooks.class);
                startActivity(intent);
                break;
            case R.id.actionNotification:
                intent = new Intent(ActivityBusTracking.this, ActivityNotification.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
