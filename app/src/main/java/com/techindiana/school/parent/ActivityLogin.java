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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.techindiana.school.parent.Database.ParentDBAL;
import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Module.LoginInfo;
import com.techindiana.school.parent.Module.LoginResp;
import com.techindiana.school.parent.Module.ParentChild;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityLogin extends BaseActivity {
    EditText etEmail, etPassword, etforgotEtEmail, etOTP;
    Button btnLogin;
    TextView tvFPassword;
    TextView tvRegister;
    Context mContext;
    Retrofit retrofit;
    DisplayMetrics metrics;
TextView loginTvAdmissionEnq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        try {
            retrofit = RetrofitUtils.getRetrofit();
            mContext = ActivityLogin.this;
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            Methods.setFont(ActivityLogin.this, viewGroup);

            Declaration();
            if (Methods.isOnline(this)) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Constant.device_token=refreshedToken;
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: variable declaration
    private void Declaration() {
        try {
            etEmail = (EditText) findViewById(R.id.loginEtEmail);
            etPassword = (EditText) findViewById(R.id.loginEtPassword);
            etOTP = (EditText) findViewById(R.id.loginEtOTP);
            etOTP.setText("");
            btnLogin = (Button) findViewById(R.id.loginBtnLogin);
            tvFPassword = (TextView) findViewById(R.id.loginBtnFPassword);
            loginTvAdmissionEnq = (TextView) findViewById(R.id.loginTvAdmissionEnq);
            loginTvAdmissionEnq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityAdmissionEnq.class);
                    startActivity(intent);
                }
            });
            tvFPassword.setPaintFlags(tvFPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvFPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_f_password);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;
                    dialog.getWindow().setAttributes(lp);
                    TextView tv_ExitMsg = (TextView) dialog.findViewById(R.id.tv_ExitMsg);
                    TextView tv_ExitYes = (TextView) dialog.findViewById(R.id.tv_ExitYes);
                    TextView tv_ExitNo = (TextView) dialog.findViewById(R.id.tv_ExitNo);
                    etforgotEtEmail = (EditText) dialog.findViewById(R.id.forgotEtEmail);

                    etforgotEtEmail.setTypeface(tf);
                    tv_ExitMsg.setTypeface(tf);
                    tv_ExitYes.setTypeface(tf);
                    tv_ExitNo.setTypeface(tf);
                    tv_ExitYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (etforgotEtEmail.getText().toString().trim().length() == 0) {
                                etforgotEtEmail.setError(Html
                                        .fromHtml("<font color='#FF0000'>Please enter your valid Email Id</font>"));
                            } else if (etforgotEtEmail.getText().toString().trim().length() != 0) {
                                // E-mail ID validation
                                Matcher matcherObj = Pattern.compile(Constant.regEx).matcher(
                                        etforgotEtEmail.getText().toString().trim());
                                if (!matcherObj.matches()) {
                                    etforgotEtEmail.setError(Html
                                            .fromHtml("<font color='#FF0000'>Please enter your valid Email Id</font>"));
                                } else {
                                    if (!Methods.isOnline(ActivityLogin.this)) {
                                        Methods.alertMsg(ActivityLogin.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
                                    } else {
                                        forgotPassword(etforgotEtEmail.getText().toString().trim());
                                    }
                                    dialog.dismiss();
                                }
                            }
                        }
                    });

                    tv_ExitNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            tvRegister = (TextView) findViewById(R.id.loginLlyRegister);
            tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                    startActivity(intent);
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Methods.isOnline(ActivityLogin.this)) {
                        Methods.alertMsg(ActivityLogin.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
                    } else {
                        if (etEmail.getText().toString().trim().length() == 0) {
                            etEmail.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Email Id</font>"));
                        } else if (etEmail.getText().toString().trim().length() != 0) {
                            // E-mail ID validation
                            Matcher matcherObj = Pattern.compile(Constant.regEx).matcher(
                                    etEmail.getText().toString().trim());
                            if (!matcherObj.matches()) {
                                etEmail.setError(Html
                                        .fromHtml("<font color='#FF0000'>Please enter your valid Email Id</font>"));
                            } else {
                                if (etPassword.getText().toString().trim().length() < 5) {
                                    etPassword
                                            .setError(Html
                                                    .fromHtml("<font color='#FF0000'>Passwords should at least 5 characters</font>"));
                                } else {

                                    getLogin(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
                                }
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for login
    private void getLogin(String str_Email, String str_Pass) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
            Call<LoginResp> call = restInterface.getLogin(Constant.apiKey, str_Email, str_Pass, Constant.deviceName, Constant.deviceId, Constant.deviceType, Constant.device_token);
            call.enqueue(new Callback<LoginResp>() {

                @Override
                public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            LoginInfo userInfo = response.body().getLoginInfo();
                            String imgVal = "";

                            if (userInfo.getProfileImg() != null)
                                imgVal = userInfo.getProfileImg();


                            ParentDBAL.updateUserDetails(mContext,
                                    userInfo.getParentId().toString(),
                                    userInfo.getFname().toString(),
                                    userInfo.getLname().toString(),
                                    userInfo.getEmail().toString(),
                                    userInfo.getContactNo().toString(),
                                    imgVal,
                                    etPassword.getText().toString().trim(),
                                    userInfo.getActiveStudentId().toString()
                            );
                            ArrayList<ParentChild> parentChild = (ArrayList<ParentChild>) response.body().getLoginInfo().getParentChilds();
                            if (parentChild.size() > 0) {
                                try {
                                    ParentDBAL.deleteChild(ActivityLogin.this);
                                    for (int i = 0; i < parentChild.size(); i++) {
                                        ParentDBAL.insertChildDetails(ActivityLogin.this,
                                                parentChild.get(i).getStudentId(),
                                                parentChild.get(i).getFname(),
                                                parentChild.get(i).getLname(),
                                                parentChild.get(i).getSchoolId(),
                                                parentChild.get(i).getClassId(),
                                                parentChild.get(i).getDivId(),
                                                parentChild.get(i).getProfileImg(),
                                                parentChild.get(i).getIsVerified(),
                                                parentChild.get(i).getIsActive()
                                        );
                                    }
                                    ParentDBAL.getChildDetails(ActivityLogin.this);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            Intent intent = new Intent(mContext, ActivityHome.class);
                            startActivity(intent);
                            finish();

                        } else {
                            etPassword.setText("");
                            Toast.makeText(ActivityLogin.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginResp> call, Throwable t) {
                    etPassword.setText("");
                    hideProgress();

                }
            });
        } catch (Exception e) {
            etPassword.setText("");
            hideProgress();
        }
    }

//TODO: Web call for forgot password
    private void forgotPassword(String str_Email) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
            Call<BaseResp> call = restInterface.forgotPassword(Constant.apiKey, str_Email);
            call.enqueue(new Callback<BaseResp>() {

                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            Toast.makeText(ActivityLogin.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            etforgotEtEmail.setText("");
                        } else {
                            Toast.makeText(ActivityLogin.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

}