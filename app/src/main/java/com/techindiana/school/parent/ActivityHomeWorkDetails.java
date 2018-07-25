package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Module.HomeworkDayInfo;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityHomeWorkDetails extends BaseActivity {

    public ActionBar actionBar;
    HomeworkDayInfo homeworkDayInfo;
    private Context mContext;
TextView tvTitle,tvSDate,tvEDate,tvDetails;
ImageView imgUser,imgNotice,imgStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_work_details);

        mContext = ActivityHomeWorkDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeworkDayInfo = (HomeworkDayInfo) getIntent().getSerializableExtra("homeWork");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mHomeWork));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityHomeWorkDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            tvTitle=(TextView)findViewById(R.id.hwTvTitle);
            tvSDate=(TextView)findViewById(R.id.hwTvSDate);
            tvEDate=(TextView)findViewById(R.id.hwTvEDate);
            tvDetails=(TextView)findViewById(R.id.hwTvNoticeDetails);
             imgUser=(ImageView)findViewById(R.id.hwImgUser);
             imgNotice=(ImageView)findViewById(R.id.hwImgNotice);
             imgStatus=(ImageView)findViewById(R.id.hwImgStatus);


            tvTitle.setText(homeworkDayInfo.getTitle());
           // tvDate.setText(homeworkDayInfo.getNbDate());
          //  tvTime.setText(homeworkDayInfo.getNbTime());
            tvDetails.setText(homeworkDayInfo.getDescription());

            if(homeworkDayInfo.getHwStatus().equals("1")) {
                imgStatus.setBackgroundResource(R.mipmap.present_ic);
            }else if(homeworkDayInfo.getHwStatus().equals("0")) {
                imgStatus.setBackgroundResource(R.mipmap.absent_ic);
            }else{
                imgStatus.setBackgroundResource(R.mipmap.halfday_ic);
            }


            String[] createdDate = homeworkDayInfo.getCreatedOn().split(" ");

            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "d MMM yy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;

            try {
                sdate = inputFormat.parse(createdDate[0]);
                tvSDate.setText(outputFormat.format(sdate));
                edate = inputFormat.parse(homeworkDayInfo.getCompletionDate());
                tvEDate.setText(outputFormat.format(edate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (homeworkDayInfo.getImage() != null) {
                if (homeworkDayInfo.getImage().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath + homeworkDayInfo.getImage()).
                            //  placeholder(R.mipmap.splash_screen_logo).
                                    centerCrop().
                            //error(R.mipmap.splash_screen_logo).
                                    into(imgNotice);
                    imgNotice.setVisibility(View.VISIBLE);
                    imgNotice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dilogToImg();
                        }
                    });
                } else {
                    imgNotice.setVisibility(View.GONE);
                }
            } else {
                imgNotice.setVisibility(View.GONE);
            }

            if (Constant.childImg != null) {
                if (Constant.childImg.toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath +Constant.childImg).
                            //  placeholder(R.mipmap.splash_screen_logo).
                                   // centerCrop().
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

    private void dilogToImg() {
        try{
            Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_img);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            ImageView imgZoom = (ImageView) dialog.findViewById(R.id.imgZoom);
            ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgZoom);
            Glide.with(context).
                    load(Constant.webImgPath + homeworkDayInfo.getImage()).

                            into(imgZoom);
            dialog.show();
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
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
