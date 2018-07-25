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
import com.techindiana.school.parent.Module.NotificationInfo;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityNotificationDetails extends BaseActivity {

    public ActionBar actionBar;
    NotificationInfo notificationInfo;
    private Context mContext;
TextView tvTitle,tvDate,tvTime,tvDetails;
ImageView imgUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_details);

        mContext = ActivityNotificationDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationInfo = (NotificationInfo) getIntent().getSerializableExtra("notification");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle("Notifications");


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityNotificationDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            tvTitle=(TextView)findViewById(R.id.nfTvTitle);
            tvDate=(TextView)findViewById(R.id.nfTvDate);
            tvTime=(TextView)findViewById(R.id.nfTvTime);
            tvDetails=(TextView)findViewById(R.id.nfTvNoticeDetails);
             imgUser=(ImageView)findViewById(R.id.nfImgUser);


            tvTitle.setText(notificationInfo.getTitle());
            String[] separated = notificationInfo.getCreatedOn().split(" ");

            String inputDPattern = "yyyy-MM-dd";
            String outputDPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputDPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputDPattern);

            Date sdate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(separated[0].toString());
                tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }



            String inputTPattern = "HH:mm:ss";
            String outputTPattern = "hh:mm a";
            SimpleDateFormat inputTFormat = new SimpleDateFormat(inputTPattern);
            SimpleDateFormat outputTFormat = new SimpleDateFormat(outputTPattern);

            Date sT = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sT = inputTFormat.parse(separated[1].toString());
                tvTime.setText(outputTFormat.format(sT));

            } catch (ParseException e) {
                e.printStackTrace();
            }



            //   tvDate.setText(dateS[0]);
          //  tvTime.setText(dateS[1]);
            tvDetails.setText(notificationInfo.getDescription());

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
