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
import com.techindiana.school.parent.Module.NoticeBoardInfo;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityNoticeBoardDetails extends BaseActivity {

    public ActionBar actionBar;
    NoticeBoardInfo noticeBoardInfo;
    private Context mContext;
TextView tvTitle,tvCategory,tvDate,tvTime,tvDetails;
ImageView imgUser,imgNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_noticeboard_details);

        mContext = ActivityNoticeBoardDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noticeBoardInfo = (NoticeBoardInfo) getIntent().getSerializableExtra("notice");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mNoticeBoard));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityNoticeBoardDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            tvTitle=(TextView)findViewById(R.id.noticeTvTitle);
            tvCategory=(TextView)findViewById(R.id.noticeTvCategory);
            tvDate=(TextView)findViewById(R.id.noticeTvDate);
            tvTime=(TextView)findViewById(R.id.noticeTvTime);
            tvDetails=(TextView)findViewById(R.id.noticeTvNoticeDetails);
             imgUser=(ImageView)findViewById(R.id.noticeImgUser);
             imgNotice=(ImageView)findViewById(R.id.noticeImgNotice);


            tvTitle.setText(noticeBoardInfo.getTitle());
            tvCategory.setText(noticeBoardInfo.getCategory());


            String inputDPattern = "yyyy-MM-dd";
            String outputDPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputDPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputDPattern);

            Date sdate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(noticeBoardInfo.getNbDate());
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
                sT = inputTFormat.parse(noticeBoardInfo.getNbTime());
                tvTime.setText(outputTFormat.format(sT));

            } catch (ParseException e) {
                e.printStackTrace();
            }




          //  tvDate.setText(noticeBoardInfo.getNbDate());
           // tvTime.setText(noticeBoardInfo.getNbTime());
            tvDetails.setText(noticeBoardInfo.getDescription());

            if (noticeBoardInfo.getImage() != null) {
                if (noticeBoardInfo.getImage().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath + noticeBoardInfo.getImage()).
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
                    load(Constant.webImgPath + noticeBoardInfo.getImage()).

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
