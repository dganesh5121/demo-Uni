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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Database.ParentDBAL;
import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Module.SettingInfochild;
import com.techindiana.school.parent.Module.SettingInfoparent;
import com.techindiana.school.parent.Module.SettingResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivitySettings extends BaseActivity implements View.OnClickListener {

    public ActionBar actionBar;
    private Context mContext;
    private Retrofit retrofit;
    FrameLayout childFly1, childFly2, childFly3;
    ImageView imgParent, imgChild1, imgChild2, imgChild3, imgParentEdit,
            imgChild1Edit, imgChild2Edit, imgChild3Edit, imgChild1Status, mgChild2Status, mgChild3Status, imgParentAdd;
    CardView cvAddParent;
    TextView tvParentName, tvParentPhoneNo, tvParentEmail,
            tvChild1Name, tvChild1Gender, tvChild1Class, tvChild1School, tvChild1Year, tvChild1RollNo, tvChild1BloodGr, tvChild1DOB,
            tvChild2Name, tvChild2Gender, tvChild2Class, tvChild2School, tvChild2Year, tvChild2RollNo, tvChild2BloodGr, tvChild2DOB,
            tvChild3Name, tvChild3Gender, tvChild3Class, tvChild3School, tvChild3Year, tvChild3RollNo, tvChild3BloodGr, tvChild3DOB;
    Switch swNotification, swAppLock;
    Button btnAddChild, btnSubmitPassword;
    EditText etOldPassword, etNewPassword, etCPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivitySettings.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mSettings));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivitySettings.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            btnSubmitPassword = (Button) findViewById(R.id.pvBtnChangePassword);
            etOldPassword = (EditText) findViewById(R.id.pvEtOldPassword);
            etNewPassword = (EditText) findViewById(R.id.pvEtNewPassword);
            etCPassword = (EditText) findViewById(R.id.pvEtCPassword);
            btnSubmitPassword.setOnClickListener(this);

            childFly1 = (FrameLayout) findViewById(R.id.pvFlyChild1);
            childFly2 = (FrameLayout) findViewById(R.id.pvFlyChild1);
            childFly3 = (FrameLayout) findViewById(R.id.pvFlyChild1);
            btnAddChild = (Button) findViewById(R.id.pvBtnAddChild);
            imgParent = (ImageView) findViewById(R.id.pvImgParent);
            imgParentEdit = (ImageView) findViewById(R.id.pvTvImgPEdit);
            imgParentAdd = (ImageView) findViewById(R.id.pvImgAddParent);
            cvAddParent = (CardView) findViewById(R.id.pvCvAddParent);


            tvParentName = (TextView) findViewById(R.id.pvTvPName);
            tvParentPhoneNo = (TextView) findViewById(R.id.pvTvPContactNo);
            tvParentEmail = (TextView) findViewById(R.id.pvTvPEmail);

            //  tvParentName.setText(Constant.userFName + " " + Constant.userLName);
            //  tvParentPhoneNo.setText(Constant.userPhone1);
            //  tvParentEmail.setText(Constant.userEmail);


            imgChild1 = (ImageView) findViewById(R.id.pvTvC1Profile);
            imgChild1Edit = (ImageView) findViewById(R.id.pvImgC1Edit);
            imgChild1Status = (ImageView) findViewById(R.id.pvImgC1IsActive);
            tvChild1Name = (TextView) findViewById(R.id.pvTvC1Name);
            tvChild1Gender = (TextView) findViewById(R.id.pvTvC1Gender);
            tvChild1Class = (TextView) findViewById(R.id.pvTvC1DivClass);
            tvChild1School = (TextView) findViewById(R.id.pvTvC1SchoolName);
            tvChild1Year = (TextView) findViewById(R.id.pvTvC1Year);
            tvChild1RollNo = (TextView) findViewById(R.id.pvTvC1RollNo);
            tvChild1BloodGr = (TextView) findViewById(R.id.pvTvC1BloodGr);
            tvChild1DOB = (TextView) findViewById(R.id.pvTvC1DOB);


            imgChild2 = (ImageView) findViewById(R.id.pvTvC2Profile);
            imgChild2Edit = (ImageView) findViewById(R.id.pvImgC2Edit);
            mgChild2Status = (ImageView) findViewById(R.id.pvImgC2IsActive);
            tvChild2Name = (TextView) findViewById(R.id.pvTvC2Name);
            tvChild2Gender = (TextView) findViewById(R.id.pvTvC2Gender);
            tvChild2Class = (TextView) findViewById(R.id.pvTvC2DivClass);
            tvChild2School = (TextView) findViewById(R.id.pvTvC2SchoolName);
            tvChild2Year = (TextView) findViewById(R.id.pvTvC2Year);
            tvChild2RollNo = (TextView) findViewById(R.id.pvTvC2RollNo);
            tvChild2BloodGr = (TextView) findViewById(R.id.pvTvC2BloodGr);
            tvChild2DOB = (TextView) findViewById(R.id.pvTvC2DOB);


            imgChild3Edit = (ImageView) findViewById(R.id.pvImgC3Edit);
            imgChild3 = (ImageView) findViewById(R.id.pvTvC3Profile);
            mgChild3Status = (ImageView) findViewById(R.id.pvImgC3IsActive);
            tvChild3Name = (TextView) findViewById(R.id.pvTvC3Name);
            tvChild3Gender = (TextView) findViewById(R.id.pvTvC3Gender);
            tvChild3Class = (TextView) findViewById(R.id.pvTvC3DivClass);
            tvChild3School = (TextView) findViewById(R.id.pvTvC3SchoolName);
            tvChild3Year = (TextView) findViewById(R.id.pvTvC3Year);
            tvChild3RollNo = (TextView) findViewById(R.id.pvTvC3RollNo);
            tvChild3BloodGr = (TextView) findViewById(R.id.pvTvC3BloodGr);
            tvChild3DOB = (TextView) findViewById(R.id.pvTvC3DOB);


            imgParentEdit.setOnClickListener(ActivitySettings.this);
            imgChild1Edit.setOnClickListener(ActivitySettings.this);
            imgChild2Edit.setOnClickListener(ActivitySettings.this);
            imgChild3Edit.setOnClickListener(ActivitySettings.this);
            imgChild1Status.setOnClickListener(ActivitySettings.this);
            mgChild2Status.setOnClickListener(ActivitySettings.this);
            mgChild3Status.setOnClickListener(ActivitySettings.this);
            btnAddChild.setOnClickListener(ActivitySettings.this);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        try {
            // swNotification, swAppLock
            if (!Methods.isOnline(ActivitySettings.this)) {
                Methods.alertMsg(ActivitySettings.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
            } else {
                getInfo();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        super.onResume();
    }

    //TODO: Web call for Notification Details...
    private void getInfo() {
        try {
            if (Constant.settingInfoparents != null)
                Constant.settingInfochild.clear();
            if (Constant.settingInfoparents != null)
                Constant.settingInfochild.clear();
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<SettingResp> call = restInterface.getSettingInfo(Constant.apiKey, Constant.userID);
            call.enqueue(new Callback<SettingResp>() {
                @Override
                public void onResponse(Call<SettingResp> call, Response<SettingResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                Constant.settingInfoparents = (ArrayList<SettingInfoparent>) response.body().getSettingInfo().getSettingInfoparent();
                                Constant.settingInfochild = (ArrayList<SettingInfochild>) response.body().getSettingInfo().getSettingInfochilds();
                                if (Constant.settingInfoparents != null) {

                                    if (Constant.settingInfoparents.get(0).getProfileImg() != null) {
                                        if (Constant.settingInfoparents.get(0).getProfileImg().toString().length() > 0) {
                                            Glide.with(context).
                                                    load(Constant.webImgPath + Constant.settingInfoparents.get(0).getProfileImg()).
                                                    //  placeholder(R.mipmap.splash_screen_logo).
                                                            centerCrop().
                                                    //error(R.mipmap.splash_screen_logo).
                                                            into(imgParent);
                                        } else {
                                            Glide.with(context).
                                                    load(R.mipmap.splash_screen_logo).
                                                    placeholder(R.mipmap.splash_screen_logo).
                                                    error(R.mipmap.splash_screen_logo).
                                                    into(imgParent);
                                        }
                                    } else {
                                        Glide.with(context).
                                                load(R.mipmap.splash_screen_logo).
                                                placeholder(R.mipmap.splash_screen_logo).
                                                error(R.mipmap.splash_screen_logo).
                                                into(imgParent);
                                    }
                                    if (Constant.settingInfoparents.get(0).getHasSubParent().equals("no")) {
                                        cvAddParent.setVisibility(View.VISIBLE);
                                    } else {
                                        cvAddParent.setVisibility(View.GONE);
                                    }
                                    tvParentName.setText(Constant.settingInfoparents.get(0).getFname() + " " + Constant.settingInfoparents.get(0).getLname());
                                    tvParentPhoneNo.setText(Constant.settingInfoparents.get(0).getContactNo());
                                    tvParentEmail.setText(Constant.settingInfoparents.get(0).getEmail());
                                    String imgVal = "";

                                    if (Constant.settingInfoparents.get(0).getProfileImg() != null)
                                        imgVal = Constant.settingInfoparents.get(0).getProfileImg();


                                    ParentDBAL.updateUserDetails(mContext,
                                            Constant.settingInfoparents.get(0).getParentId().toString(),
                                            Constant.settingInfoparents.get(0).getFname().toString(),
                                            Constant.settingInfoparents.get(0).getLname().toString(),
                                            Constant.settingInfoparents.get(0).getEmail().toString(),
                                            Constant.settingInfoparents.get(0).getContactNo().toString(),
                                            imgVal,
                                            Constant.userPassword,
                                            Constant.settingInfoparents.get(0).getActiveStudentId().toString()
                                    );
                                }

                                if (Constant.settingInfochild != null) {
                                    if (Constant.settingInfochild.size() > 2) {
                                        childFly1.setVisibility(View.VISIBLE);
                                        childFly2.setVisibility(View.VISIBLE);
                                        childFly3.setVisibility(View.VISIBLE);
                                        btnAddChild.setVisibility(View.GONE);
                                    } else if (Constant.settingInfochild.size() > 1) {
                                        childFly1.setVisibility(View.VISIBLE);
                                        childFly2.setVisibility(View.VISIBLE);
                                        childFly3.setVisibility(View.INVISIBLE);
                                        btnAddChild.setVisibility(View.VISIBLE);
                                    } else if (Constant.settingInfochild.size() > 0) {
                                        childFly1.setVisibility(View.VISIBLE);
                                        childFly2.setVisibility(View.INVISIBLE);
                                        childFly3.setVisibility(View.INVISIBLE);
                                        btnAddChild.setVisibility(View.VISIBLE);
                                    }
                                    try {
                                        ParentDBAL.deleteChild(ActivitySettings.this);
                                        for (int i = 0; i < Constant.settingInfochild.size(); i++) {
                                            ParentDBAL.insertChildDetails(ActivitySettings.this,
                                                    Constant.settingInfochild.get(i).getStudentId(),
                                                    Constant.settingInfochild.get(i).getFname(),
                                                    Constant.settingInfochild.get(i).getLname(),
                                                    Constant.settingInfochild.get(i).getSchoolId(),
                                                    Constant.settingInfochild.get(i).getClass_id(),
                                                    Constant.settingInfochild.get(i).getDiv_id(),
                                                    Constant.settingInfochild.get(i).getProfileImg(),
                                                    Constant.settingInfochild.get(i).getIsVerified(),
                                                    Constant.settingInfochild.get(i).getIsActive()
                                            );
                                        }
                                        ParentDBAL.getChildDetails(ActivitySettings.this);
                                    } catch (Exception e) {
                                        e.getMessage();
                                    }
                                    String inputPattern = "yyyy-MM-dd";
                                    String outputPattern = "d MMM yy";
                                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                    Date sdate = null;
                                    Date edate = null;
                                    for (int i = 0; i < Constant.settingInfochild.size(); i++) {
                                        if (i == 0) {
                                            if (Constant.settingInfochild.get(i).getProfileImg() != null) {
                                                if (Constant.settingInfochild.get(i).getProfileImg().toString().length() > 0) {
                                                    Glide.with(context).
                                                            load(Constant.webImgPath + Constant.settingInfochild.get(i).getProfileImg()).
                                                            //  placeholder(R.mipmap.splash_screen_logo).
                                                                    centerCrop().
                                                            //error(R.mipmap.splash_screen_logo).
                                                                    into(imgChild1);
                                                } else {
                                                    Glide.with(context).
                                                            load(R.mipmap.splash_screen_logo).
                                                            placeholder(R.mipmap.splash_screen_logo).
                                                            error(R.mipmap.splash_screen_logo).
                                                            into(imgChild1);
                                                }
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.splash_screen_logo).
                                                        placeholder(R.mipmap.splash_screen_logo).
                                                        error(R.mipmap.splash_screen_logo).
                                                        into(imgChild1);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                imgChild1Edit.setVisibility(View.INVISIBLE);
                                            } else {
                                                imgChild1Edit.setVisibility(View.VISIBLE);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                Glide.with(context).
                                                        load(R.mipmap.active_child_ic).
                                                        placeholder(R.mipmap.active_child_ic).
                                                        error(R.mipmap.active_child_ic).
                                                        into(imgChild1Status);
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.deactiv_child_ic).
                                                        placeholder(R.mipmap.deactiv_child_ic).
                                                        error(R.mipmap.deactiv_child_ic).
                                                        into(imgChild1Status);
                                            }

                                            tvChild1Name.setText(Constant.settingInfochild.get(i).getFname() + " " + Constant.settingInfochild.get(i).getLname());
                                            tvChild1Gender.setText("(" + Constant.settingInfochild.get(i).getGender() + ")");
                                            tvChild1Class.setText(Constant.settingInfochild.get(i).getClassName() + "/" + Constant.settingInfochild.get(i).getDivName());
                                            tvChild1School.setText(" : " + Constant.settingInfochild.get(i).getSchoolName());
                                            tvChild1Year.setText(" : " + Constant.settingInfochild.get(i).getSchool_year());
                                            tvChild1RollNo.setText(" : " + Constant.settingInfochild.get(i).getRollNo());
                                            tvChild1BloodGr.setText(" : " + Constant.settingInfochild.get(i).getBloodGrp());



                                            try {
                                                sdate = inputFormat.parse(Constant.settingInfochild.get(i).getDob());
                                                tvChild1DOB.setText(outputFormat.format(sdate));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                           // tvChild1DOB.setText(Constant.settingInfochild.get(i).getDob());
                                        } else if (i == 1) {
                                            if (Constant.settingInfochild.get(i).getProfileImg() != null) {
                                                if (Constant.settingInfochild.get(i).getProfileImg().toString().length() > 0) {
                                                    Glide.with(context).
                                                            load(Constant.webImgPath + Constant.settingInfochild.get(i).getProfileImg()).
                                                            //  placeholder(R.mipmap.splash_screen_logo).
                                                                    centerCrop().
                                                            //error(R.mipmap.splash_screen_logo).
                                                                    into(imgChild2);
                                                } else {
                                                    Glide.with(context).
                                                            load(R.mipmap.splash_screen_logo).
                                                            placeholder(R.mipmap.splash_screen_logo).
                                                            error(R.mipmap.splash_screen_logo).
                                                            into(imgChild2);
                                                }
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.splash_screen_logo).
                                                        placeholder(R.mipmap.splash_screen_logo).
                                                        error(R.mipmap.splash_screen_logo).
                                                        into(imgChild2);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                imgChild2Edit.setVisibility(View.INVISIBLE);
                                            } else {
                                                imgChild2Edit.setVisibility(View.VISIBLE);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                Glide.with(context).
                                                        load(R.mipmap.active_child_ic).
                                                        placeholder(R.mipmap.active_child_ic).
                                                        error(R.mipmap.active_child_ic).
                                                        into(mgChild2Status);
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.deactiv_child_ic).
                                                        placeholder(R.mipmap.deactiv_child_ic).
                                                        error(R.mipmap.deactiv_child_ic).
                                                        into(mgChild2Status);
                                            }

                                            tvChild2Name.setText(Constant.settingInfochild.get(i).getFname() + " " + Constant.settingInfochild.get(i).getLname());
                                            tvChild2Gender.setText("(" + Constant.settingInfochild.get(i).getGender() + ")");
                                            tvChild2Class.setText(Constant.settingInfochild.get(i).getClassName() + "/" + Constant.settingInfochild.get(i).getDivName());
                                            tvChild2School.setText(" : " + Constant.settingInfochild.get(i).getSchoolName());
                                            tvChild2Year.setText(" : " + Constant.settingInfochild.get(i).getSchool_year());
                                            tvChild2RollNo.setText(" : " + Constant.settingInfochild.get(i).getRollNo());
                                            tvChild2BloodGr.setText(" : " + Constant.settingInfochild.get(i).getBloodGrp());
                                            try {
                                                sdate = inputFormat.parse(Constant.settingInfochild.get(i).getDob());
                                                tvChild2DOB.setText(outputFormat.format(sdate));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                         //   tvChild2DOB.setText(Constant.settingInfochild.get(i).getDob());
                                        } else if (i == 2) {

                                            if (Constant.settingInfochild.get(i).getProfileImg() != null) {
                                                if (Constant.settingInfochild.get(i).getProfileImg().toString().length() > 0) {
                                                    Glide.with(context).
                                                            load(Constant.webImgPath + Constant.settingInfochild.get(i).getProfileImg()).
                                                            //  placeholder(R.mipmap.splash_screen_logo).
                                                                    centerCrop().
                                                            //error(R.mipmap.splash_screen_logo).
                                                                    into(imgChild3);
                                                } else {
                                                    Glide.with(context).
                                                            load(R.mipmap.splash_screen_logo).
                                                            placeholder(R.mipmap.splash_screen_logo).
                                                            error(R.mipmap.splash_screen_logo).
                                                            into(imgChild3);
                                                }
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.splash_screen_logo).
                                                        placeholder(R.mipmap.splash_screen_logo).
                                                        error(R.mipmap.splash_screen_logo).
                                                        into(imgChild3);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                imgChild3Edit.setVisibility(View.INVISIBLE);
                                            } else {
                                                imgChild3Edit.setVisibility(View.VISIBLE);
                                            }

                                            if (Constant.settingInfochild.get(i).getIsActive().equals("1") && Constant.settingInfochild.get(i).getIsVerified().equals("1")) {
                                                Glide.with(context).
                                                        load(R.mipmap.active_child_ic).
                                                        placeholder(R.mipmap.active_child_ic).
                                                        error(R.mipmap.active_child_ic).
                                                        into(mgChild3Status);
                                            } else {
                                                Glide.with(context).
                                                        load(R.mipmap.deactiv_child_ic).
                                                        placeholder(R.mipmap.deactiv_child_ic).
                                                        error(R.mipmap.deactiv_child_ic).
                                                        into(mgChild3Status);
                                            }

                                            tvChild3Name.setText(Constant.settingInfochild.get(i).getFname() + " " + Constant.settingInfochild.get(i).getLname());
                                            tvChild3Gender.setText("(" + Constant.settingInfochild.get(i).getGender() + ")");
                                            tvChild3Class.setText(Constant.settingInfochild.get(i).getClassName() + "/" + Constant.settingInfochild.get(i).getDivName());
                                            tvChild3School.setText(" : " + Constant.settingInfochild.get(i).getSchoolName());
                                            tvChild3Year.setText(" : " + Constant.settingInfochild.get(i).getSchool_year());
                                            tvChild3RollNo.setText(" : " + Constant.settingInfochild.get(i).getRollNo());
                                            tvChild3BloodGr.setText(" : " + Constant.settingInfochild.get(i).getBloodGrp());
                                            try {
                                                sdate = inputFormat.parse(Constant.settingInfochild.get(i).getDob());
                                                tvChild3DOB.setText(outputFormat.format(sdate));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                         //   tvChild3DOB.setText(Constant.settingInfochild.get(i).getDob());
                                        }
                                    }


                                }


                            } else {
                                Toast.makeText(ActivitySettings.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<SettingResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            Intent intent;

            switch (v.getId()) {
                case R.id.pvBtnChangePassword:
                    try {
                        if (etOldPassword.getText().toString().trim().length() < 5) {
                            etOldPassword.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                        } else if (etNewPassword.getText().toString().trim().length() < 5) {
                            etNewPassword
                                    .setError(Html
                                            .fromHtml("<font color='#FF0000'>Passwords should at least 5 characters</font>"));
                        } else if (!etCPassword.getText().toString().trim().equals(etNewPassword.getText().toString().trim())) {
                            etCPassword
                                    .setError(Html
                                            .fromHtml("<font color='#FF0000'>Password does not match.</font>"));
                        } else {

                            changePassword();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;
                case R.id.pvBtnAddChild:
                    intent = new Intent(ActivitySettings.this, ActivitySettingsChildAdd.class);
                    startActivity(intent);
                    break;
                case R.id.pvTvImgPEdit:
                    intent = new Intent(ActivitySettings.this, ActivitySettingsParentEdit.class);
                    startActivity(intent);
                    break;
                case R.id.pvImgC1Edit:
                    Constant.childToBeEdit = Constant.settingInfochild.get(0).getStudentId();
                    intent = new Intent(ActivitySettings.this, ActivitySettingsChildEdit.class);
                    startActivity(intent);
                    break;
                case R.id.pvImgC2Edit:
                    Constant.childToBeEdit = Constant.settingInfochild.get(1).getStudentId();
                    intent = new Intent(ActivitySettings.this, ActivitySettingsChildEdit.class);
                    startActivity(intent);
                    break;
                case R.id.pvImgC3Edit:
                    Constant.childToBeEdit = Constant.settingInfochild.get(2).getStudentId();
                    intent = new Intent(ActivitySettings.this, ActivitySettingsChildEdit.class);
                    startActivity(intent);
                    break;
                case R.id.pvImgC1IsActive:
                    if (Constant.settingInfochild.get(0).getIsVerified().equals("0")) {
                        deleteChild(Constant.settingInfochild.get(0).getStudentId());
                    }
                    break;
                case R.id.pvImgC2IsActive:
                    if (Constant.settingInfochild.get(1).getIsVerified().equals("0")) {
                        deleteChild(Constant.settingInfochild.get(1).getStudentId());
                    }
                    break;
                case R.id.pvImgC3IsActive:
                    if (Constant.settingInfochild.get(2).getIsVerified().equals("0")) {
                        deleteChild(Constant.settingInfochild.get(2).getStudentId());
                    }
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Notification Details...
    private void changePassword() {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<BaseResp> call = restInterface.changePassword(Constant.apiKey, Constant.userID,
                    etOldPassword.getText().toString(), etNewPassword.getText().toString(), etCPassword.getText().toString());
            call.enqueue(new Callback<BaseResp>() {
                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                etCPassword.setText("");
                                etOldPassword.setText("");
                                etNewPassword.setText("");
                                Toast.makeText(ActivitySettings.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ActivitySettings.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<BaseResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    private void deleteChild(final String studentId) {
        try {

            Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_exit);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            TextView tv_ExitMsg = (TextView) dialog.findViewById(R.id.tv_ExitMsg);
            TextView tv_ExitYes = (TextView) dialog.findViewById(R.id.tv_ExitYes);
            TextView tv_ExitNo = (TextView) dialog.findViewById(R.id.tv_ExitNo);
            tv_ExitMsg.setText("Are you sure you want to remove this child from your list?");
            tv_ExitMsg.setTypeface(tf);
            tv_ExitYes.setTypeface(tf);
            tv_ExitNo.setTypeface(tf);
            tv_ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteChildFromList(studentId);
                    dialog.dismiss();
                }
            });

            tv_ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Notification Details...
    private void deleteChildFromList(String studentId) {
        try {
            showProgress();
            String is_active = "0";

                if (studentId.equals(Constant.child1))
                    is_active = "1";

            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<BaseResp> call = restInterface.deleteStudent(Constant.apiKey, Constant.userID, Constant.childID, Constant.childSchoolId, is_active);
            call.enqueue(new Callback<BaseResp>() {
                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                getInfo();
                                Toast.makeText(ActivitySettings.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivitySettings.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<BaseResp> call, Throwable t) {
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
        inflater.inflate(R.menu.menu_setting, menu);
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
                intent = new Intent(ActivitySettings.this, ActivityLibBooks.class);
                startActivity(intent);
                break;
            case R.id.actionNotification:
                intent = new Intent(ActivitySettings.this, ActivityNotification.class);
                startActivity(intent);
                break;
            case R.id.actionLogout:
                Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_exit);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                TextView tv_ExitMsg = (TextView) dialog.findViewById(R.id.tv_ExitMsg);
                TextView tv_ExitYes = (TextView) dialog.findViewById(R.id.tv_ExitYes);
                TextView tv_ExitNo = (TextView) dialog.findViewById(R.id.tv_ExitNo);
                tv_ExitMsg.setTypeface(tf);
                tv_ExitMsg.setText(getResources().getString(R.string.msg_logout));
                tv_ExitYes.setTypeface(tf);
                tv_ExitNo.setTypeface(tf);
                tv_ExitYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParentDBAL.userLogoOut(ActivitySettings.this);
                        finishAffinity();
                        dialog.dismiss();
                    }
                });

                tv_ExitNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
