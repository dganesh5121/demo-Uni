package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.techindiana.school.parent.Module.HelthReportResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityHealthReport extends BaseActivity {

    public ActionBar actionBar;

    private Context mContext;
    private Retrofit retrofit;
    TextView tvEyeL, tvEyeR, tvEyeComment, tvEarL, tvEarR,tvEarLDbl, tvEarRDbl, tvEarComment, tvSco, tvLor, tvLyp, tvScoComment, tvDrComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_report);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityHealthReport.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mHealthReport));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityHealthReport.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            tvEyeL = (TextView) findViewById(R.id.hrTvEyeL);
            tvEyeR = (TextView) findViewById(R.id.hrTvEyeR);
            tvEyeComment = (TextView) findViewById(R.id.hrTvEyeComment);
            tvEarL = (TextView) findViewById(R.id.hrTvEarL);
            tvEarR = (TextView) findViewById(R.id.hrTvEarR);
            tvEarLDbl = (TextView) findViewById(R.id.hrTvEarLDbl);
            tvEarRDbl = (TextView) findViewById(R.id.hrTvEarRDbl);
            tvEarComment = (TextView) findViewById(R.id.hrTvEarComment);
            tvSco = (TextView) findViewById(R.id.hrTvScoliosis);
            tvLor = (TextView) findViewById(R.id.hrTvLordosis);
            tvLyp = (TextView) findViewById(R.id.hrTvKyphosis);
            tvScoComment = (TextView) findViewById(R.id.hrTvScoliosisComment);
            tvDrComment = (TextView) findViewById(R.id.hrTvDrComment);
            getInfo();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //TODO: Web call for Notification Details...
    private void getInfo() {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<HelthReportResp> call = restInterface.helthReport(Constant.apiKey, Constant.childSchoolId, Constant.childID);
            call.enqueue(new Callback<HelthReportResp>() {
                @Override
                public void onResponse(Call<HelthReportResp> call, Response<HelthReportResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                if(response.body().getHelthReportInfo().getLeftEye().equals("1"))
                                tvEyeL.setText("Pass");
                                else
                                    tvEyeL.setText("Fail");

                                if(response.body().getHelthReportInfo().getRightEye().equals("1"))
                                tvEyeR.setText("Pass");
                                else
                                tvEyeR.setText("Fail");

                                tvEyeComment.setText(response.body().getHelthReportInfo().getEyeComment());
                                if(response.body().getHelthReportInfo().getLeftEar().equals("1"))
                                tvEarL.setText("Pass");
                                else
                                tvEarL.setText("Fail");

                                if(response.body().getHelthReportInfo().getRightEar().equals("1"))
                                tvEarR.setText("Pass");
                                else
                                tvEarR.setText("Fail");

                                tvEarLDbl.setText(response.body().getHelthReportInfo().getLeftEarDblevel());
                                tvEarRDbl.setText(response.body().getHelthReportInfo().getRightEarDblevel());
                                tvEarComment.setText(response.body().getHelthReportInfo().getEarComment());

                                if(response.body().getHelthReportInfo().getScoliosis().equals("1"))
                                tvSco.setText("Pass");
                                else
                                tvSco.setText("Fail");

                                if(response.body().getHelthReportInfo().getLordosis().equals("1"))
                                tvLor.setText("Pass");
                                else
                                tvLor.setText("Fail");

                                if(response.body().getHelthReportInfo().getKyphosis().equals("1"))
                                tvLyp.setText("Pass");
                                else
                                tvLyp.setText("Fail");

                                tvScoComment.setText(response.body().getHelthReportInfo().getBodyComment());
                                tvDrComment.setText(response.body().getHelthReportInfo().getDrComment());
                            } else {
                                Toast.makeText(ActivityHealthReport.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<HelthReportResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
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
                intent = new Intent(ActivityHealthReport.this, ActivityLibBooks.class);
                startActivity(intent);
                break;
            case R.id.actionNotification:
                intent = new Intent(ActivityHealthReport.this, ActivityNotification.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
