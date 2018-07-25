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
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Module.ExtraClassDayWiseInfo;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityExtraClassDetails extends BaseActivity {

    public ActionBar actionBar;
    ExtraClassDayWiseInfo extraClassDayWiseInfo;
    private Context mContext;
TextView tvTitle,tvDate,tvTime,tvDetails;
ImageView imgUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_extra_class_details);

        mContext = ActivityExtraClassDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extraClassDayWiseInfo = (ExtraClassDayWiseInfo) getIntent().getSerializableExtra("extraClass");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mExtraClasses));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityExtraClassDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            tvTitle=(TextView)findViewById(R.id.ecTvTitle);
            tvDate=(TextView)findViewById(R.id.ecTvDate);
            tvTime=(TextView)findViewById(R.id.ecTvTime);
            tvDetails=(TextView)findViewById(R.id.ecTvNoticeDetails);
             imgUser=(ImageView)findViewById(R.id.ecImgUser);


            tvTitle.setText(extraClassDayWiseInfo.getTitle());

            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(extraClassDayWiseInfo.getDate());
               tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }

           // tvDate.setText(extraClassDayWiseInfo.getDate());

            String inputSPattern = "HH:mm:ss";
            String outputSPattern = "hh:mm a";
            SimpleDateFormat inputSFormat = new SimpleDateFormat(inputSPattern);
            SimpleDateFormat outputSFormat = new SimpleDateFormat(outputSPattern);

            Date sTime = null;
            Date eTime = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sTime = inputSFormat.parse(extraClassDayWiseInfo.getStartTime());
                eTime = inputSFormat.parse(extraClassDayWiseInfo.getEndTime());
                tvTime.setText(outputSFormat.format(sTime)+" To "+outputSFormat.format(eTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

          //  tvTime.setText(extraClassDayWiseInfo.getStartTime() +" To "+extraClassDayWiseInfo.getEndTime());
            tvDetails.setText(extraClassDayWiseInfo.getDescription());

            if (Constant.childImg != null) {
                if (Constant.childImg.toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath +Constant.childImg).
                            //  placeholder(R.mipmap.splash_screen_logo).
                                  //  centerCrop().
                            //error(R.mipmap.splash_screen_logo).
                                    into(imgUser);
                } else {
                    Glide.with(context).
                            load(R.mipmap.splash_screen_logo).
                            placeholder(R.mipmap.splash_screen_logo).
                            error(R.mipmap.splash_screen_logo).
                            into(imgUser);
                }
            } else {
                Glide.with(context).
                        load(R.mipmap.splash_screen_logo).
                        placeholder(R.mipmap.splash_screen_logo).
                        error(R.mipmap.splash_screen_logo).
                        into(imgUser);
            }

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


}
