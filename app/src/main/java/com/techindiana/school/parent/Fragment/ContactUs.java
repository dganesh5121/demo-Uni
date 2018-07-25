package com.techindiana.school.parent.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techindiana.school.parent.Module.ContactUsResp;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class ContactUs extends BaseFragment implements OnMapReadyCallback {
    private View rootView;
    private Retrofit retrofit;
    TextView tvAddress, tvEmail, tvWebsite, tvContact, tvEC1, tvEc2, tvName;
    SupportMapFragment mapFrag;
    static GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_contact_us, container, false);
        retrofit = RetrofitUtils.getRetrofit();
        tvName = (TextView) rootView.findViewById(R.id.cUsTvName);
        tvAddress = (TextView) rootView.findViewById(R.id.cUsTvAddress);
        tvEmail = (TextView) rootView.findViewById(R.id.cUsTvEmail);
        tvWebsite = (TextView) rootView.findViewById(R.id.cUsTvWebsite);
        tvContact = (TextView) rootView.findViewById(R.id.cUsTvContactNo);
        tvEC1 = (TextView) rootView.findViewById(R.id.cUsTvEContact1);
        tvEc2 = (TextView) rootView.findViewById(R.id.cUsTvEContact2);
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        getInfo();

        return rootView;
    }

    public static void addMarkerWithCameraZooming(String latitude, String longitude, String title) {
        try {
            LatLng current_latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            mGoogleMap.addMarker(new MarkerOptions().position(current_latlng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            );
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).zoom(12).tilt(30).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.getMessage();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Notification Details...
    private void getInfo() {
        try {
            showProgress(getActivity());
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<ContactUsResp> call = restInterface.contactUs(Constant.apiKey, Constant.childSchoolId);
            call.enqueue(new Callback<ContactUsResp>() {
                @Override
                public void onResponse(Call<ContactUsResp> call, Response<ContactUsResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {

                                tvName.setText(response.body().getContactUsInfo().getSchoolName().trim());
                                tvAddress.setText(response.body().getContactUsInfo().getAddress().trim());
                                tvEmail.setText(response.body().getContactUsInfo().getSchoolEmail().trim());
                                tvWebsite.setText(response.body().getContactUsInfo().getWebsite().trim());
                                tvContact.setText(response.body().getContactUsInfo().getContact().trim());
                                tvEC1.setText(response.body().getContactUsInfo().getContact().trim());
                                tvEc2.setText(response.body().getContactUsInfo().getContact2().trim());
                                addMarkerWithCameraZooming(response.body().getContactUsInfo().getLatitude().trim(), response.body().getContactUsInfo().getLongitude().trim(), response.body().getContactUsInfo().getSchoolName().trim());
                            } else {
                                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<ContactUsResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }

}
