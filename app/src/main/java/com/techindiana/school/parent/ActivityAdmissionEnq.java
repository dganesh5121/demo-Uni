package com.techindiana.school.parent;
/*
Created By: DGP 01/01/2018
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Module.ClassesNameInfo;
import com.techindiana.school.parent.Module.ClassesNameResp;
import com.techindiana.school.parent.Module.SchoolNameInfo;
import com.techindiana.school.parent.Module.SchoolNameResp;
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


public class ActivityAdmissionEnq extends BaseActivity {


    EditText etFName, etLName, etMobileNo, etEmail, etAddress, etCName;
    Button btnSubmit;
    Spinner spiSchoolName, spiClassName;
    String spiSchoolVal = "", spiClassVal = "";

    Context mContext;
    Retrofit retrofit;
    ArrayList<SchoolNameInfo> schoolNameInfos;
    ArrayList<ClassesNameInfo> classesNameInfos;
    final ArrayList<String> arraySchool = new ArrayList<String>();
    final ArrayList<String> arrayClass = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admission_enq);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityAdmissionEnq.this;
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityAdmissionEnq.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            etFName = (EditText) findViewById(R.id.admEtFName);
            etLName = (EditText) findViewById(R.id.admEtLName);
            etMobileNo = (EditText) findViewById(R.id.admEtPhone);
            etEmail = (EditText) findViewById(R.id.admEtEmail);
            etAddress = (EditText) findViewById(R.id.admEtAddress);
            etCName = (EditText) findViewById(R.id.admEtCName);
            btnSubmit = (Button) findViewById(R.id.admBtnSubmit);
            spiSchoolName = (Spinner) findViewById(R.id.admSpiSchool);
            spiClassName = (Spinner) findViewById(R.id.admSpiClass);
            spiSchoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (position == 0) {
                            arrayClass.clear();
                            arrayClass.add("Select Class");
                            ResetAdapter(spiClassName, arrayClass);
                            spiClassVal = "";
                        } else {
                            spiSchoolVal = schoolNameInfos.get(position - 1).getId().toString();
                            arrayClass.clear();
                            arrayClass.add("Select Class");
                            ResetAdapter(spiClassName, arrayClass);
                            spiClassVal = "";
                            getClassOfSchool();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spiClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (position == 0) {

                        } else {
                            spiClassVal = classesNameInfos.get(position - 1).getId().toString();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Methods.isOnline(ActivityAdmissionEnq.this)) {
                        Methods.alertMsg(ActivityAdmissionEnq.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
                    } else {
                        if (etFName.getText().toString().trim().length() == 0) {
                            etFName.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Name</font>"));
                        } else if (etLName.getText().toString().trim().length() == 0) {
                            etLName.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Name</font>"));
                        } else if (etEmail.getText().toString().trim().length() == 0) {
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
                                if (etMobileNo.getText().toString().trim().length() < 10 || etMobileNo.getText().toString().trim().length() > 15) {
                                    etMobileNo
                                            .setError(Html
                                                    .fromHtml("<font color='#FF0000'>Please enter your valid Phone no.</font>"));
                                } else if (etCName.getText().toString().trim().length() == 0) {
                                    etCName.setError(Html
                                            .fromHtml("<font color='#FF0000'>Please enter your valid Name</font>"));
                                } else if (spiSchoolVal.length() == 0) {
                                    Toast.makeText(mContext, "Select at list one School from list...", Toast.LENGTH_SHORT).show();
                                } else if (spiClassVal.length() == 0) {
                                    Toast.makeText(mContext, "Select at list one Class...", Toast.LENGTH_SHORT).show();
                                } else {
                                    admissionEnquiry();
                                }
                            }
                        }
                    }
                }
            });
            getSchool();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Subject Details...
    private void getSchool() {
        try {
            showProgress();
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<SchoolNameResp> call = restInterface.getSchools(Constant.apiKey);
            call.enqueue(new Callback<SchoolNameResp>() {

                @Override
                public void onResponse(Call<SchoolNameResp> call, Response<SchoolNameResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            schoolNameInfos = (ArrayList<SchoolNameInfo>) response.body().getSchoolNameInfo();

                            if (schoolNameInfos.size() > 0) {
                                arraySchool.clear();
                                arraySchool.add("Select School");
                                for (int i = 0; i < schoolNameInfos.size(); i++) {
                                    arraySchool.add(schoolNameInfos.get(i).getSchoolName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityAdmissionEnq.this,
                                        R.layout.list_item_facility, arraySchool);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiSchoolName.setAdapter(adapterarrayCategory);
                                spiSchoolName.setSelection(0);
                            }
                        } else {
                            Toast.makeText(ActivityAdmissionEnq.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<SchoolNameResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }

    //TODO: Web call for Class Details...
    private void getClassOfSchool() {
        try {
            showProgress();
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<ClassesNameResp> call = restInterface.getAllClasses(Constant.apiKey, spiSchoolVal);
            call.enqueue(new Callback<ClassesNameResp>() {

                @Override
                public void onResponse(Call<ClassesNameResp> call, Response<ClassesNameResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            classesNameInfos = (ArrayList<ClassesNameInfo>) response.body().getClassesNameInfo();
                            if (classesNameInfos.size() > 0) {
                                arrayClass.clear();
                                arrayClass.add("Select Class");
                                for (int i = 0; i < classesNameInfos.size(); i++) {
                                    arrayClass.add(classesNameInfos.get(i).getClassName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityAdmissionEnq.this,
                                        R.layout.list_item_facility, arrayClass);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiClassName.setAdapter(adapterarrayCategory);
                                spiClassName.setSelection(0);
                            }
                        } else {
                            Toast.makeText(ActivityAdmissionEnq.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<ClassesNameResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    private void ResetAdapter(Spinner spinner, ArrayList<String> array) {
        try {

            ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityAdmissionEnq.this,
                    R.layout.list_item_facility, array);
            adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(adapterarrayCategory);
            spinner.setSelection(0);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Subject Details...
    private void admissionEnquiry() {
        try {
            showProgress();

            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<BaseResp> call = restInterface.admissionEnquiry(Constant.apiKey
                    , etFName.getText().toString().trim(), etLName.getText().toString().trim(), etMobileNo.getText().toString().trim(), etEmail.getText().toString().trim()
                    , etAddress.getText().toString().trim(), etCName.getText().toString().trim(), spiSchoolVal, spiClassVal
            );
            call.enqueue(new Callback<BaseResp>() {

                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            Toast.makeText(ActivityAdmissionEnq.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(ActivityAdmissionEnq.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
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

}
