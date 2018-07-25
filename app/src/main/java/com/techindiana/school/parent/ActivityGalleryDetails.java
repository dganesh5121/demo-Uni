package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techindiana.school.parent.Adapter.AdapterGalleryDetails;
import com.techindiana.school.parent.Module.GalleryDetailsImage;
import com.techindiana.school.parent.Module.GalleryDetailsResp;
import com.techindiana.school.parent.Module.GalleryInfo;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Utils.VerticalLineDecorator;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityGalleryDetails extends BaseActivity {

    public ActionBar actionBar;
    RecyclerView.LayoutManager managerGallery;
    RecyclerView.Adapter adapterGallery;
    RecyclerView rvGallery;
    LinearLayout homeLyNoData;
    
    private Context mContext;
    private Retrofit retrofit;
    GalleryInfo galleryInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityGalleryDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryInfos = (GalleryInfo) getIntent().getSerializableExtra("gallery");


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(galleryInfos.getTitle());


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityGalleryDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {

            homeLyNoData = (LinearLayout) findViewById(R.id.homeLlyNoData);
            rvGallery = (RecyclerView) findViewById(R.id.homeRcRV);
            rvGallery.setHasFixedSize(true);
            rvGallery.setLayoutManager(new GridLayoutManager(context, 2));
            rvGallery.addItemDecoration(new VerticalLineDecorator(2));



            getNotification();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        try {

        } catch (Exception e) {
            e.getMessage();
        }
    }



    //TODO on navigation icon click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: Web call for Notification Details...
    private void getNotification() {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<GalleryDetailsResp> call = restInterface.getGalleryDetails(Constant.apiKey, Constant.childSchoolId,galleryInfos.getGalleryId()  );
            call.enqueue(new Callback<GalleryDetailsResp>() {
                @Override
                public void onResponse(Call<GalleryDetailsResp> call, Response<GalleryDetailsResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                ArrayList<GalleryDetailsImage>   galleryInfosDetailsImages = (ArrayList<GalleryDetailsImage>) response.body().getGalleryDetailsInfo().getGalleryDetailsImages();

                                if (galleryInfosDetailsImages.size() > 0) {
                                    adapterGallery = new AdapterGalleryDetails(context, galleryInfosDetailsImages);
                                    rvGallery.setAdapter(adapterGallery);
                                } else {
                                    rvGallery.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ActivityGalleryDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<GalleryDetailsResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }



}
