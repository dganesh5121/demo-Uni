package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.techindiana.school.parent.Database.DatabaseHelper;
import com.techindiana.school.parent.Database.ParentDBAL;
import com.techindiana.school.parent.Module.LoginInfo;
import com.techindiana.school.parent.Module.LoginResp;
import com.techindiana.school.parent.Module.ParentChild;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivitySplash extends BaseActivity {
    DisplayMetrics metrics;

    Retrofit retrofit;
    Message msg = new Message();

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        retrofit = RetrofitUtils.getRetrofit();

        try {


            // Creation of database and tables
            DatabaseHelper dh = new DatabaseHelper(ActivitySplash.this);
            SQLiteDatabase db = dh.getWritableDatabase();
            db.close();
            dh.close();
            ParentDBAL.getUserDetailsDB(ActivitySplash.this);
            ParentDBAL.getChildDetails(ActivitySplash.this);
            //get the size of the icon ....
            Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);
            Constant.iconHeight = d.getIntrinsicHeight();
            if (Methods.isOnline(this)) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Constant.device_token=refreshedToken;
            }
            //get the size of the screen ...
            metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Constant.screenWidth = metrics.widthPixels;
            Constant.screenHeight = metrics.heightPixels;

            //currnet date ....
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Constant.currentDate = df.format(c.getTime());
            Constant.deviceName = getDeviceName();

            //get android phone information
            Constant.deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
            try {
                Constant.deviceIpAdress = Methods.getLocalIpAddress(ActivitySplash.this);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if(Constant.userID.equals("0") || Constant.userID.length()==0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {

                            Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                            startActivity(intent);
                            finish();

                        } catch (RuntimeException e) {
                            e.getMessage();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }, 3000);

            }else{
//                Intent intent = new Intent(ActivitySplash.this, ActivityHome.class);
//                startActivity(intent);
//                finish();
                if (!Methods.isOnline(ActivitySplash.this)) {
                    checkNetwork();
                } else {
                    checkLogin();
                }
            }


        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void checkNetwork() {
        try{
            Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(ActivitySplash.this);
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
            tv_ExitMsg.setText(getResources().getString(R.string.err_network_failure_message));
            tv_ExitYes.setTypeface(tf);
            tv_ExitYes.setText("Exit");
            tv_ExitNo.setTypeface(tf);
            tv_ExitNo.setText("Refresh");
            tv_ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    dialog.dismiss();
                }
            });

            tv_ExitNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (!Methods.isOnline(ActivitySplash.this)) {
                        checkNetwork();
                    } else {
                        checkLogin();
                    }

                }
            });
            dialog.show();
        }catch (Exception e){
            e.getMessage();
        }
    }

    //TODO: Web call for login
    private void checkLogin() {
        try {

            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
            Call<LoginResp> call = restInterface.getLogin(Constant.apiKey, Constant.userEmail, Constant.userPassword, Constant.deviceName, Constant.deviceId, Constant.deviceType, Constant.device_token);
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


                            ParentDBAL.updateUserDetails(ActivitySplash.this,
                                    userInfo.getParentId().toString(),
                                    userInfo.getFname().toString(),
                                    userInfo.getLname().toString(),
                                    userInfo.getEmail().toString(),
                                    userInfo.getContactNo().toString(),
                                    imgVal,
                                    Constant.userPassword,
                                    userInfo.getActiveStudentId().toString()
                            );
                            ArrayList<ParentChild> parentChild = (ArrayList<ParentChild>) response.body().getLoginInfo().getParentChilds();
                            if (parentChild.size() > 0) {
                                try {
                                    ParentDBAL.deleteChild(ActivitySplash.this);
                                    for (int i = 0; i < parentChild.size(); i++) {
                                        ParentDBAL.insertChildDetails(ActivitySplash.this,
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
                                    ParentDBAL.getChildDetails(ActivitySplash.this);
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                            Intent intent = new Intent(ActivitySplash.this, ActivityHome.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(ActivitySplash.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ParentDBAL.userLogoOut(ActivitySplash.this);
                        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<LoginResp> call, Throwable t) {



                }
            });
        } catch (Exception e) {

        }
    }



}
