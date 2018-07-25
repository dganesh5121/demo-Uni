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
import android.support.v7.widget.LinearLayoutManager;
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

import com.techindiana.school.parent.Adapter.AdapterChat;
import com.techindiana.school.parent.Module.TeachersListInfo;
import com.techindiana.school.parent.Module.TeachersListResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Utils.VerticalLineDecorator;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityChat extends BaseActivity {

    public ActionBar actionBar;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapterChat;
    RecyclerView rvChat;
    LinearLayout homeLyNoData;
    private Context mContext;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityChat.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mChat));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityChat.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {

            homeLyNoData = (LinearLayout) findViewById(R.id.homeLlyNoData);

            rvChat = (RecyclerView) findViewById(R.id.homeRcRV);
            rvChat.setLayoutManager(manager);
            rvChat.setHasFixedSize(true);
            rvChat.setLayoutManager(new LinearLayoutManager(context));
            rvChat.addItemDecoration(new VerticalLineDecorator(2));


            getList();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getListBackground();
                }
            }, 0, 10000);

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
    private void getList() {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
            Call<TeachersListResp> call = restInterface.getTeachers(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, Constant.childID, Constant.userID);
            call.enqueue(new Callback<TeachersListResp>() {
                @Override
                public void onResponse(Call<TeachersListResp> call, Response<TeachersListResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                ArrayList<TeachersListInfo> getTeachersListInfo = (ArrayList<TeachersListInfo>) response.body().getTeachersListInfo();

                                if (getTeachersListInfo.size() > 0) {
                                    adapterChat = new AdapterChat(context, getTeachersListInfo);
                                    rvChat.setAdapter(adapterChat);

                                } else {
                                    rvChat.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ActivityChat.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<TeachersListResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }

    //TODO: Web call for Notification Details...
    private void getListBackground() {
        try {
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
            Call<TeachersListResp> call = restInterface.getTeachers(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, Constant.childID, Constant.userID);
            call.enqueue(new Callback<TeachersListResp>() {
                @Override
                public void onResponse(Call<TeachersListResp> call, Response<TeachersListResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                ArrayList<TeachersListInfo> getTeachersListInfo = (ArrayList<TeachersListInfo>) response.body().getTeachersListInfo();

                                if (getTeachersListInfo.size() > 0) {
                                    adapterChat = new AdapterChat(context, getTeachersListInfo);
                                    rvChat.setAdapter(adapterChat);

                                } else {
                                    rvChat.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ActivityChat.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<TeachersListResp> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }


}
