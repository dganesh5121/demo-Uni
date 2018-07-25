package com.techindiana.school.parent;
/*
Created By: DGP 01/01/2018
Updated By: DGP
Class Name:
Class Details:
*/

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.techindiana.school.parent.Module.ClassesNameInfo;
import com.techindiana.school.parent.Module.ClassesNameResp;
import com.techindiana.school.parent.Module.DivisionNameInfo;
import com.techindiana.school.parent.Module.DivisionNameResp;
import com.techindiana.school.parent.Module.SchoolNameInfo;
import com.techindiana.school.parent.Module.SchoolNameResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityRegisterChild extends BaseActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public ActionBar actionBar;
    File userImgPath;
    //String userIPath="";
    private Context mContext;
    private Retrofit retrofit;
    Button btnSave;
    EditText etFName, etLName, etDOB, etRollNo, etBloodGr;
    Spinner spiSchoolName, spiClassName, spiDivName;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    String spiSchoolVal = "", spiClassVal = "", spiDivVal = "";
    ArrayList<SchoolNameInfo> schoolNameInfos;
    ArrayList<ClassesNameInfo> classesNameInfos;
    ArrayList<DivisionNameInfo> divisionNameInfos;

    final ArrayList<String> arraySchool = new ArrayList<String>();
    final ArrayList<String> arrayClass = new ArrayList<String>();
    final ArrayList<String> arrayDiv = new ArrayList<String>();

    ImageView imgProfile;
    private Calendar calendar = Calendar.getInstance();
    private String myFormat = "yyyy-MM-dd"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    TextView tvPrivious, tvAddChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_child);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityRegisterChild.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle("Child Profile Add");
            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityRegisterChild.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        try {
            if (Constant.aChildList.size() > Constant.childAddPos) {
                btnSave.setVisibility(View.GONE);
                etFName.setText(Constant.aChildList.get(Constant.childAddPos).get("fName"));
                etLName.setText(Constant.aChildList.get(Constant.childAddPos).get("lName"));
                etDOB.setText(Constant.aChildList.get(Constant.childAddPos).get("DOB"));
                if (Constant.aChildList.get(Constant.childAddPos).get("gender").equals("male"))
                    rbMale.isChecked();
                else
                    rbFemale.isChecked();

                etRollNo.setText(Constant.aChildList.get(Constant.childAddPos).get("rollNo"));
                etBloodGr.setText(Constant.aChildList.get(Constant.childAddPos).get("blood"));
                userImgPath = new File(Constant.aParentList.get(0).get("img"));
                Uri uri1 = Uri.fromFile(new File(Constant.aParentList.get(Constant.childAddPos).get("img")));
                Picasso.with(ActivityRegisterChild.this).
                        load(uri1).
                        fit().placeholder(R.mipmap.login_user_profile_img).
                        error(R.mipmap.login_user_profile_img).
                        into(imgProfile);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        super.onResume();
    }

    //TODO define the component used in the class ...
    private void Declaration() {
        try {
            btnSave = (Button) findViewById(R.id.sChdBtnSubmit);
            imgProfile = (ImageView) findViewById(R.id.sChdImgUser);
            etFName = (EditText) findViewById(R.id.sChdFEtName);
            etLName = (EditText) findViewById(R.id.sChdEtLName);
            etDOB = (EditText) findViewById(R.id.sChdEtDob);
            etRollNo = (EditText) findViewById(R.id.sChdEtRollNo);
            etBloodGr = (EditText) findViewById(R.id.sChdEtBloodGr);
            rbMale = (RadioButton) findViewById(R.id.sChdRbMale);
            rbFemale = (RadioButton) findViewById(R.id.sChdRbFemale);
            btnSave.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
            tvPrivious = (TextView) findViewById(R.id.rgChildTvPrevious);
            tvPrivious.setOnClickListener(this);
            tvAddChild = (TextView) findViewById(R.id.rgChildTvAddChild);
            tvAddChild.setOnClickListener(this);
            if (Constant.childAddPos == 2) {
                tvAddChild.setVisibility(View.INVISIBLE);
            }

            spiSchoolName = (Spinner) findViewById(R.id.sChdSpiSchool);
            spiClassName = (Spinner) findViewById(R.id.sChdSpiClass);
            spiDivName = (Spinner) findViewById(R.id.sChdSpiDivision);
            spiSchoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (position == 0) {
                            arrayClass.clear();
                            arrayClass.add("Select Class");
                            ResetAdapter(spiClassName, arrayClass);
                            spiClassVal = "";
                            arrayDiv.clear();
                            arrayDiv.add("Select Division");
                            ResetAdapter(spiDivName, arrayDiv);
                            spiDivVal = "";

                        } else {
                            spiSchoolVal = schoolNameInfos.get(position - 1).getId().toString();


                            arrayClass.clear();
                            arrayClass.add("Select Class");
                            ResetAdapter(spiClassName, arrayClass);
                            spiClassVal = "";

                            arrayDiv.clear();
                            arrayDiv.add("Select Division");
                            ResetAdapter(spiDivName, arrayDiv);
                            spiDivVal = "";

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
                            arrayDiv.clear();
                            arrayDiv.add("Select Division");
                            ResetAdapter(spiDivName, arrayDiv);
                            spiDivVal = "";
                        } else {
                            arrayDiv.clear();
                            arrayDiv.add("Select Division");
                            ResetAdapter(spiDivName, arrayDiv);
                            spiDivVal = "";
                            spiClassVal = classesNameInfos.get(position - 1).getId().toString();
                            getDiviOfClass();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spiDivName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (position == 0) {

                        } else {

                            spiDivVal = divisionNameInfos.get(position - 1).getId().toString();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            etDOB.setOnClickListener(this);


            getSchool();
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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dueDate = sdf.format(calendar.getTime());

        etDOB.setText(dueDate);


    }

    int isFromDate = 1;
    int yy, mm, dd;
    DatePickerDialog pickerDialog;
    WindowManager.LayoutParams lp;

    //TODO onclick list...
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.rgChildTvPrevious:
                    AddChildToList(0);
                    onBackPressed();
                    break;

                case R.id.rgChildTvAddChild:
                    AddChildToList(1);
                    if (Constant.childAddPos < 2) {
                        Constant.childAddPos = Constant.childAddPos + 1;
                        Intent intent = new Intent(ActivityRegisterChild.this, ActivityRegisterChild.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(mContext, "You can add maximum 3 child only...", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.sChdBtnSubmit:
                    if (etFName.getText().toString().trim().length() == 0) {
                        etFName.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                    } else if (etLName.getText().toString().trim().length() == 0) {
                        etLName.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                    } else if (etDOB.getText().toString().trim().length() == 0) {
                        etDOB.setError(Html
                                .fromHtml("<font color='#FF0000'>Please select valid date.</font>"));
                    } else if (spiSchoolVal.toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Please select one of the school from list", Toast.LENGTH_SHORT).show();
                    } else if (spiClassVal.toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Please select one of the Class from list", Toast.LENGTH_SHORT).show();
                    } else if (spiDivVal.toString().trim().length() == 0) {
                        Toast.makeText(mContext, "Please select one of the Division from list", Toast.LENGTH_SHORT).show();
                    } else if (etRollNo.getText().toString().trim().length() == 0) {
                        etRollNo.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter valid Roll No.</font>"));
                    } else {
                        AddChildToList(1);
                        new registerChild(ActivityRegisterChild.this).execute();

                    }
                    break;
                case R.id.sChdImgUser:
                    choosProfileImg();
                    break;

                case R.id.sChdEtDob:
                    isFromDate = 1;
                    yy = calendar.get(Calendar.YEAR);
                    mm = calendar.get(Calendar.MONTH);
                    dd = calendar.get(Calendar.DAY_OF_MONTH);
                    Calendar c = Calendar.getInstance();
                    c.set(yy-4, mm, dd);
                    Calendar cMin = Calendar.getInstance();
                    cMin.set(yy-80, mm, dd);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pickerDialog = new DatePickerDialog(this,
                                R.style.DialogTheme, this, yy, mm, dd);
                    } else {
                        pickerDialog = new DatePickerDialog(this, this, yy, mm, dd);
                    }
                    pickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    pickerDialog.getDatePicker().setMinDate(cMin.getTimeInMillis());
//                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    lp = new WindowManager.LayoutParams();
                    lp.copyFrom(pickerDialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;
                    pickerDialog.getWindow().setAttributes(lp);
                    pickerDialog.show();
                    break;


            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void AddChildToList(int isSubmit) {
        try {
            HashMap<String, String> h1 = new HashMap<String, String>();
            if (isSubmit == 1) {

                    if (etFName.getText().toString().trim().length() == 0) {
                    etFName.setError(Html
                            .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                } else if (etLName.getText().toString().trim().length() == 0) {
                    etLName.setError(Html
                            .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                } else if (etDOB.getText().toString().trim().length() == 0) {
                    etDOB.setError(Html
                            .fromHtml("<font color='#FF0000'>Please select valid date.</font>"));
                } else if (spiSchoolVal.toString().trim().length() == 0) {
                    Toast.makeText(mContext, "Please select one of the school from list", Toast.LENGTH_SHORT).show();
                } else if (spiClassVal.toString().trim().length() == 0) {
                    Toast.makeText(mContext, "Please select one of the Class from list", Toast.LENGTH_SHORT).show();
                } else if (spiDivVal.toString().trim().length() == 0) {
                    Toast.makeText(mContext, "Please select one of the Division from list", Toast.LENGTH_SHORT).show();
                } else if (etRollNo.getText().toString().trim().length() == 0) {
                    etRollNo.setError(Html
                            .fromHtml("<font color='#FF0000'>Please enter valid Roll No.</font>"));
                } else {
                        if (Constant.aChildList.size() >Constant.childAddPos) {
                            if (userImgPath != null)
                                h1.put("img", userImgPath.getAbsolutePath());
                            else
                                h1.put("img", "");
                            h1.put("fName", etFName.getText().toString().trim());
                            h1.put("lName", etLName.getText().toString().trim());
                            h1.put("DOB", etDOB.getText().toString().trim());
                            if (rbMale.isChecked())
                                h1.put("gender", "male");
                            else
                                h1.put("gender", "female");
                            h1.put("schoolName", spiSchoolVal);
                            h1.put("className", spiClassVal);
                            h1.put("divName", spiDivVal);
                            h1.put("rollNo", etRollNo.getText().toString().trim());
                            h1.put("blood", etBloodGr.getText().toString().trim());

                            Constant.aChildList.set(Constant.childAddPos, h1);

                        } else {
                            if (userImgPath != null)
                                h1.put("img", userImgPath.getAbsolutePath());
                            else
                                h1.put("img", "");
                            h1.put("fName", etFName.getText().toString().trim());
                            h1.put("lName", etLName.getText().toString().trim());
                            h1.put("DOB", etDOB.getText().toString().trim());
                            if (rbMale.isChecked())
                                h1.put("gender", "male");
                            else
                                h1.put("gender", "female");
                            h1.put("schoolName", spiSchoolVal);
                            h1.put("className", spiClassVal);
                            h1.put("divName", spiDivVal);
                            h1.put("rollNo", etRollNo.getText().toString().trim());
                            h1.put("blood", etBloodGr.getText().toString().trim());
                            Constant.aChildList.add(h1);
                        }

                }
            } else {
                if (etFName.getText().toString().trim().length() == 0 &&etLName.getText().toString().trim().length() == 0 &&
                        etDOB.getText().toString().trim().length() ==0 && etRollNo.getText().toString().trim().length() == 0 ) {

                }else {
                    if (Constant.aChildList.size() > Constant.childAddPos) {
                        if (userImgPath != null)
                            h1.put("img", userImgPath.getAbsolutePath());
                        else
                            h1.put("img", "");
                        h1.put("fName", etFName.getText().toString().trim());
                        h1.put("lName", etLName.getText().toString().trim());
                        h1.put("DOB", etDOB.getText().toString().trim());
                        if (rbMale.isChecked())
                            h1.put("gender", "male");
                        else
                            h1.put("gender", "female");
                        h1.put("schoolName", spiSchoolVal);
                        h1.put("className", spiClassVal);
                        h1.put("divName", spiDivVal);
                        h1.put("rollNo", etRollNo.getText().toString().trim());
                        h1.put("blood", etBloodGr.getText().toString().trim());

                        Constant.aChildList.set(Constant.childAddPos, h1);

                    } else {
                        if (userImgPath != null)
                            h1.put("img", userImgPath.getAbsolutePath());
                        else
                            h1.put("img", "");
                        h1.put("fName", etFName.getText().toString().trim());
                        h1.put("lName", etLName.getText().toString().trim());
                        h1.put("DOB", etDOB.getText().toString().trim());
                        if (rbMale.isChecked())
                            h1.put("gender", "male");
                        else
                            h1.put("gender", "female");
                        h1.put("schoolName", spiSchoolVal);
                        h1.put("className", spiClassVal);
                        h1.put("divName", spiDivVal);
                        h1.put("rollNo", etRollNo.getText().toString().trim());
                        h1.put("blood", etBloodGr.getText().toString().trim());
                        Constant.aChildList.add(h1);
                    }
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO upload a CV...
    public class registerChild extends AsyncTask<Void, Void, String> {
        private Context mContext;

        public registerChild(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            return doFileUpload();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgress();
            Log.e("Doc Added Response", "" + result);
            try {
                JSONObject jo_result = new JSONObject(result);
                if (jo_result.getString("success").equals("1")) {
                    Toast.makeText(ActivityRegisterChild.this, "Child added successfully ....", Toast.LENGTH_LONG).show();
                    Constant.aChildList.clear();
                    Constant.aParentList.clear();
                    finish();
                }
            } catch (JSONException e) {
                hideProgress();
                e.printStackTrace();
            } catch (NullPointerException e) {
                hideProgress();
                Log.e("Doc Add:- ", "" + e.getMessage());
            }
        }

        private String doFileUpload() {
            String response_str = null;
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constant.webPath + "register");
                post.setHeader("API-KEY", Constant.apiKey);
                MultipartEntity reqEntity = new MultipartEntity();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df1.format(Calendar.getInstance().getTime());
                if (Constant.aParentList.get(0).get("img").length() > 0) {

                    FileBody inerFilePur = new FileBody(new File(Constant.aParentList.get(0).get("img")), "UTF-8");
                    reqEntity.addPart("parent_img", inerFilePur);
                } else {
                }



                JSONArray jsonArrayChild = new JSONArray();
                for (int i = 0; i < Constant.aChildList.size(); i++) {
                    JSONObject student = new JSONObject();
                    try {
                        student.put("fname", Constant.aChildList.get(i).get("fName"));
                        student.put("lname", Constant.aChildList.get(i).get("lName"));
                        student.put("dob", Constant.aChildList.get(i).get("DOB"));
                        student.put("gender", Constant.aChildList.get(i).get("gender"));
                        student.put("school_id", Constant.aChildList.get(i).get("schoolName"));
                        student.put("class_id", Constant.aChildList.get(i).get("className"));
                        student.put("div_id", Constant.aChildList.get(i).get("divName"));
                        student.put("roll_no", Constant.aChildList.get(i).get("rollNo"));
                        student.put("blood_grp", Constant.aChildList.get(i).get("blood"));

                        if (Constant.aChildList.get(i).get("img").length() > 0) {

                            FileBody inerFilePur = new FileBody(new File(Constant.aChildList.get(i).get("img")), "UTF-8");
                            reqEntity.addPart("child_img"+(i+1), inerFilePur);
                        } else {
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    jsonArrayChild.put(student);
                }

                reqEntity.addPart("fname", new StringBody(Constant.aParentList.get(0).get("fName")));
                reqEntity.addPart("lname", new StringBody(Constant.aParentList.get(0).get("lName")));
                reqEntity.addPart("dob", new StringBody(Constant.aParentList.get(0).get("DOB")));
                reqEntity.addPart("contact_no", new StringBody(Constant.aParentList.get(0).get("mNo")));
                reqEntity.addPart("email", new StringBody(Constant.aParentList.get(0).get("emailId")));
                reqEntity.addPart("password", new StringBody(Constant.aParentList.get(0).get("password")));
                reqEntity.addPart("address", new StringBody(Constant.aParentList.get(0).get("cAddress")));
                reqEntity.addPart("childs", new StringBody(jsonArrayChild.toString()));



                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                response_str = EntityUtils.toString(resEntity);
                Log.e("Upload Resonse", "" + response_str);
            } catch (NullPointerException e) {
                hideProgress();
                e.getMessage();
                Log.e("NPE:- ", "" + e.getMessage());
            } catch (Exception ex) {
                hideProgress();
                Toast.makeText(mContext, "File not found. Please try to upload new file... ", Toast.LENGTH_SHORT).show();
            }
            return response_str;
        }
    }

    //TODO choose the file form gallery or from camera ....
    private void choosProfileImg() {
        try {
            Typeface tf = Typeface.createFromAsset(ActivityRegisterChild.this.getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(ActivityRegisterChild.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_file_choos);
            dialog.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            TextView tv_ExitMsg = (TextView) dialog.findViewById(R.id.tv_ExitMsg);
            TextView tv_Camera = (TextView) dialog.findViewById(R.id.choosTvCamera);
            TextView tv_Gallery = (TextView) dialog.findViewById(R.id.choosTvGallery);
            TextView tv_Cancel = (TextView) dialog.findViewById(R.id.choosTvCancel);
            tv_ExitMsg.setTypeface(tf);
            tv_ExitMsg.setText("Please select the file type to be upload...");
            tv_Camera.setTypeface(tf);
            tv_Gallery.setTypeface(tf);
            tv_Cancel.setTypeface(tf);

            tv_Camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isReadStorageAllowed()) {
                        try {
                            File wallpaperDirectory = new File(
                                    Environment.getExternalStorageDirectory() + "/" + getResources().
                                            getString(R.string.app_name));
                            if (!wallpaperDirectory.exists()) {
                                wallpaperDirectory.mkdirs();
                            }

                            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                                // File photoFile = null;
                                try {
                                    userImgPath = createImageFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                                Uri photoUri = FileProvider.getUriForFile(ActivityRegisterChild.this, getPackageName() + ".provider", userImgPath);
                                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(pictureIntent, 8);
                            }


                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        requestStoragePermission(8);
                    }
                    dialog.dismiss();
                }
            });

            tv_Gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isReadStorageAllowed()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 7);
                    } else {
                        requestStoragePermission(7);
                    }
                    dialog.dismiss();
                }
            });

            tv_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }


    //TODO We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(ActivityRegisterChild.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }


    //TODO Requesting permission
    private void requestStoragePermission(int i) {

        ActivityCompat.requestPermissions(ActivityRegisterChild.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, i);
    }

    //TODO This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            //Checking the request code of our request
            if (requestCode == 7) {
                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(ActivityRegisterChild.this, "Oops you just denied the permission",
                            Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 8) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO  get the user selected file path...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String pathHolder = null;
                    try {

                        pathHolder = ActivityRegisterChild.PathUtil.getPath(ActivityRegisterChild.this, uri);
                        if (!new File(pathHolder).exists()) {
                            Toast.makeText(context, "Please select local sdcard images ...", Toast.LENGTH_LONG).show();
                        } else {
                            Uri uri1 = Uri.fromFile(new File(pathHolder));
                            Glide.with(ActivityRegisterChild.this).
                                    load(uri1).
                                    //  placeholder(R.mipmap.login_user_profile_img).
                                            error(R.mipmap.login_user_profile_img).
                                    into(imgProfile);
                            userImgPath = new File(pathHolder);
                            // userIPath=pathHolder;
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(ActivityRegisterChild.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case 8:
                try {
                    if (resultCode == RESULT_OK) {
                        if (userImgPath.exists()) {
                            Uri uri = Uri.fromFile(userImgPath);
                            Glide.with(ActivityRegisterChild.this).
                                    load(uri).
                                    // placeholder(R.mipmap.login_user_profile_img).
                                            error(R.mipmap.login_user_profile_img).
                                    into(imgProfile);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
        }


    }

    //TODO function to get the path to the selected file...
    public static class PathUtil {

        @SuppressLint("NewApi")
        public static String getPath(Context context, Uri uri) throws URISyntaxException {
            final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
            String selection = null;
            String[] selectionArgs = null;
            if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("image".equals(type)) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[]{split[1]};
                }
            }
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }


        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String imageFilePath = image.getAbsolutePath();

        return image;
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
                                int sPos = 0;
                                arraySchool.clear();
                                arraySchool.add("Select School");
                                for (int i = 0; i < schoolNameInfos.size(); i++) {
                                    if (Constant.aChildList.size() > Constant.childAddPos) {
                                        if (schoolNameInfos.get(i).getId().equals(Constant.aChildList.get(Constant.childAddPos).get("schoolName")))
                                            sPos = i + 1;
                                    }
                                    arraySchool.add(schoolNameInfos.get(i).getSchoolName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityRegisterChild.this,
                                        R.layout.list_item_facility, arraySchool);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiSchoolName.setAdapter(adapterarrayCategory);
                                spiSchoolName.setSelection(sPos);
                            }
                        } else {
                            Toast.makeText(ActivityRegisterChild.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                                int cPos = 0;
                                arrayClass.clear();
                                arrayClass.add("Select Class");
                                for (int i = 0; i < classesNameInfos.size(); i++) {
                                    if (Constant.aChildList.size() > Constant.childAddPos) {
                                        if (classesNameInfos.get(i).getId().equals(Constant.aChildList.get(Constant.childAddPos).get("className")))
                                            cPos = i + 1;
                                    }
                                    arrayClass.add(classesNameInfos.get(i).getClassName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityRegisterChild.this,
                                        R.layout.list_item_facility, arrayClass);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiClassName.setAdapter(adapterarrayCategory);
                                spiClassName.setSelection(cPos);
                            }
                        } else {
                            Toast.makeText(ActivityRegisterChild.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    //TODO: Web call for Class Details...
    private void getDiviOfClass() {
        try {
            showProgress();
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<DivisionNameResp> call = restInterface.getAllDivisions(Constant.apiKey, spiSchoolVal, spiClassVal);
            call.enqueue(new Callback<DivisionNameResp>() {

                @Override
                public void onResponse(Call<DivisionNameResp> call, Response<DivisionNameResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            divisionNameInfos = (ArrayList<DivisionNameInfo>) response.body().getDivisionNameInfo();
                            if (divisionNameInfos.size() > 0) {
                                int dPos = 0;
                                arrayDiv.clear();
                                arrayDiv.add("Select Division");
                                for (int i = 0; i < divisionNameInfos.size(); i++) {
                                    if (Constant.aChildList.size() > Constant.childAddPos) {
                                        if (divisionNameInfos.get(i).getId().equals(Constant.aChildList.get(Constant.childAddPos).get("divName")))
                                            dPos = i + 1;
                                    }
                                    arrayDiv.add(divisionNameInfos.get(i).getDivName());
                                }
                                ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityRegisterChild.this,
                                        R.layout.list_item_facility, arrayDiv);
                                adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                spiDivName.setAdapter(adapterarrayCategory);
                                spiDivName.setSelection(dPos);
                            }
                        } else {
                            Toast.makeText(ActivityRegisterChild.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<DivisionNameResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    private void ResetAdapter(Spinner spinner, ArrayList<String> array) {
        try {

            ArrayAdapter<String> adapterarrayCategory = new ArrayAdapter<String>(ActivityRegisterChild.this,
                    R.layout.list_item_facility, array);
            adapterarrayCategory.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(adapterarrayCategory);
            spinner.setSelection(0);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        if (Constant.childAddPos > 0) {
            Constant.childAddPos = Constant.childAddPos - 1;
            Intent intent = new Intent(ActivityRegisterChild.this, ActivityRegisterChild.class);
            startActivity(intent);
            finish();
        } else if (Constant.childAddPos == 0) {
            Intent intent = new Intent(ActivityRegisterChild.this, ActivityRegister.class);
            startActivity(intent);
            finish();
        }
            finish();

    }
}
