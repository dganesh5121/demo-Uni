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
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Module.ActivityCDayWise;
import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityCenterDetails extends BaseActivity {

    public ActionBar actionBar;
    ActivityCDayWise cDayWise;
    private Context mContext;
    TextView tvTitle, tvSDate, tvSTime, tvEDate, tvETime, tvDetails, tvPrice;
    ImageView imgUser, imgNotice;
    FloatingActionButton floatingActionButton;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_center_details);

        mContext = ActivityCenterDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        retrofit = RetrofitUtils.getRetrofit();
        cDayWise = (ActivityCDayWise) getIntent().getSerializableExtra("activity");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mActivityCenter));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityCenterDetails.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dilogEnroll();
                }
            });
            tvTitle = (TextView) findViewById(R.id.acTvTitle);
            tvPrice = (TextView) findViewById(R.id.acTvPrice);

            tvSDate = (TextView) findViewById(R.id.acTvSDate);
            tvSTime = (TextView) findViewById(R.id.acTvSTime);
            tvEDate = (TextView) findViewById(R.id.acTvEDate);
            tvETime = (TextView) findViewById(R.id.acTvETime);
            tvDetails = (TextView) findViewById(R.id.acTvNoticeDetails);
            imgUser = (ImageView) findViewById(R.id.acImgUser);
            imgNotice = (ImageView) findViewById(R.id.acImgNotice);


            tvTitle.setText(cDayWise.getTitle());
            tvPrice.setText(cDayWise.getFees());

            String inputDPattern = "yyyy-MM-dd";
            String outputDPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputDPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputDPattern);

            Date sdate = null;
            Date edate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(cDayWise.getStartDate());
                edate = inputFormat.parse(cDayWise.getEndDate());
                tvSDate.setText(outputFormat.format(sdate));
                tvEDate.setText(outputFormat.format(edate));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            // tvSDate.setText(cDayWise.getStartDate());
            //  tvEDate.setText(cDayWise.getEndDate());


            String inputSPattern = "HH:mm:ss";
            String outputSPattern = "hh:mm a";
            SimpleDateFormat inputSFormat = new SimpleDateFormat(inputSPattern);
            SimpleDateFormat outputSFormat = new SimpleDateFormat(outputSPattern);

            Date sTime = null;
            Date eTime = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sTime = inputSFormat.parse(cDayWise.getStartTime());
                eTime = inputSFormat.parse(cDayWise.getEndTime());
                //   tvTime.setText(outputTFormat.format(sT));
                tvSTime.setText(outputSFormat.format(sTime));
                tvETime.setText(outputSFormat.format(eTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // tvSTime.setText(cDayWise.getStartTime());
            //tvETime.setText(cDayWise.getEndTime());
            tvDetails.setText(cDayWise.getDescription());
            if (cDayWise.getIsEnrolled().equals("0"))
                floatingActionButton.setImageResource(R.mipmap.non_enrolled_ic);
            else
                floatingActionButton.setImageResource(R.mipmap.enrolled_ic);
            if (cDayWise.getImage() != null) {
                if (cDayWise.getImage().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath + cDayWise.getImage()).
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
                            load(Constant.webImgPath + Constant.childImg).
                            //  placeholder(R.mipmap.splash_screen_logo).
                            //   centerCrop().
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
        try {
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
                    load(Constant.webImgPath + cDayWise.getImage()).

                    into(imgZoom);
            dialog.show();
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void dilogEnroll() {
        try {
            Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_enroll);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            ImageView imgEnroll = (ImageView) dialog.findViewById(R.id.dgImgEnroll);
            ImageView imgNEnroll = (ImageView) dialog.findViewById(R.id.dgImgNEnroll);
            TextView tvEventName = (TextView) dialog.findViewById(R.id.dgTvEventName);
            tvEventName.setText(cDayWise.getTitle());


            imgEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cDayWise.getIsEnrolled().equals("1")) {
                        dialog.dismiss();
                    } else {
                        enrollActivity("yes");
                        dialog.dismiss();
                    }

                }
            });
            imgNEnroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cDayWise.getIsEnrolled().equals("0")) {
                        dialog.dismiss();
                    } else {
                        enrollActivity("no");
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for forgot password
    private void enrollActivity(String enroll) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<BaseResp> call = restInterface.enroll(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, cDayWise.getId(), "" + cDayWise.getEnrolledId(), Constant.childID, enroll);
            call.enqueue(new Callback<BaseResp>() {

                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            Toast.makeText(ActivityCenterDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(ActivityCenterDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResp> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            hideProgress();
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
