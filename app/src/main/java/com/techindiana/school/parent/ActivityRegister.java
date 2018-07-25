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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;


public class ActivityRegister extends BaseActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    EditText etFName, etLName, etDob, etphone, etEmail, etAddress, etPassword, etCPassword;

    TextView tvNext, tvLogin;

    Context mContext;
    Retrofit retrofit;
    ImageView imgUser;
    File userImgPath;


    private Calendar calendar = Calendar.getInstance();
    private String myFormat = "yyyy-MM-dd"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_parent);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityRegister.this;
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityRegister.this, viewGroup);
        try {
            Declaration();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onResume() {
        try {
            if (Constant.aParentList.size() > 0) {
                etFName.setText(Constant.aParentList.get(0).get("fName"));
                etLName.setText(Constant.aParentList.get(0).get("lName"));
                etDob.setText(Constant.aParentList.get(0).get("DOB"));
                etphone.setText(Constant.aParentList.get(0).get("mNo"));
                etEmail.setText(Constant.aParentList.get(0).get("emailId"));
                etAddress.setText(Constant.aParentList.get(0).get("cAddress"));
                etPassword.setText(Constant.aParentList.get(0).get("password"));
                etCPassword.setText(Constant.aParentList.get(0).get("password"));
                userImgPath = new File(Constant.aParentList.get(0).get("img"));
                Uri uri1 = Uri.fromFile(new File(Constant.aParentList.get(0).get("img")));
                //  displayImage(userImgPath.getAbsolutePath(), 8);
                Picasso.with(ActivityRegister.this).
                        load(uri1).
                        fit().placeholder(R.mipmap.login_user_profile_img).
                        error(R.mipmap.login_user_profile_img).
                        into(imgUser);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        super.onResume();
    }

    private void Declaration() {
        try {

            tvNext = (TextView) findViewById(R.id.rgParentTvNext);
            tvLogin = (TextView) findViewById(R.id.rgParentTvLogin);
            tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (etFName.getText().toString().trim().length() == 0) {
                            etFName.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                        } else if (etLName.getText().toString().trim().length() == 0) {
                            etLName.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                        } else if (etDob.getText().toString().trim().length() == 0) {
                            etDob.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please select valid date.</font>"));
                        } else if (etphone.getText().toString().trim().length() < 10 && etphone.getText().toString().trim().length() > 15) {
                            etphone.setError(Html
                                    .fromHtml("<font color='#FF0000'>Please enter your valid Mobile No.</font>"));
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
                                if (etAddress.getText().toString().trim().length() < 10) {
                                    etAddress
                                            .setError(Html
                                                    .fromHtml("<font color='#FF0000'>Please enter your valid Address.</font>"));
                                } else if (etPassword.getText().toString().trim().length() < 5) {
                                    etPassword
                                            .setError(Html
                                                    .fromHtml("<font color='#FF0000'>Passwords should at least 5 characters</font>"));
                                } else if (!etCPassword.getText().toString().trim().equals(etPassword.getText().toString().trim())) {
                                    etCPassword
                                            .setError(Html
                                                    .fromHtml("<font color='#FF0000'>Password does not match.</font>"));
                                } else {


                                    if(Constant.aParentList.size()>0){
                                        HashMap<String, String> h1 = new HashMap<String, String>();
                                        if(userImgPath!=null)
                                        h1.put("img", userImgPath.getAbsolutePath());
                                        else
                                            h1.put("img", "");
                                        h1.put("fName", etFName.getText().toString().trim());
                                        h1.put("lName", etLName.getText().toString().trim());
                                        h1.put("DOB", etDob.getText().toString().trim());
                                        h1.put("mNo", etphone.getText().toString().trim());
                                        h1.put("emailId", etEmail.getText().toString().trim());
                                        h1.put("cAddress", etAddress.getText().toString().trim());
                                        h1.put("password", etPassword.getText().toString().trim());
                                        Constant.aParentList.set(0,h1);

                                    }else {
                                        HashMap<String, String> h1 = new HashMap<String, String>();
                                        if(userImgPath!=null)
                                            h1.put("img", userImgPath.getAbsolutePath());
                                        else
                                            h1.put("img", "");
                                        h1.put("fName", etFName.getText().toString().trim());
                                        h1.put("lName", etLName.getText().toString().trim());
                                        h1.put("DOB", etDob.getText().toString().trim());
                                        h1.put("mNo", etphone.getText().toString().trim());
                                        h1.put("emailId", etEmail.getText().toString().trim());
                                        h1.put("cAddress", etAddress.getText().toString().trim());
                                        h1.put("password", etPassword.getText().toString().trim());
                                        Constant.aParentList.add(h1);
                                    }
                                    Constant.childAddPos=0;
                                    Intent intent = new Intent(ActivityRegister.this, ActivityRegisterChild.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            etFName = (EditText) findViewById(R.id.registerFEtName);
            etLName = (EditText) findViewById(R.id.registerEtLName);
            etphone = (EditText) findViewById(R.id.registerEtPhone);
            etDob = (EditText) findViewById(R.id.registerEtDOB);
            etEmail = (EditText) findViewById(R.id.registerEtEmail);
            etAddress = (EditText) findViewById(R.id.registerEtAddress);
            etPassword = (EditText) findViewById(R.id.registerEtPassword);
            etCPassword = (EditText) findViewById(R.id.registerEtCPassword);
            imgUser = (ImageView) findViewById(R.id.registerImgUser);
            imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choosProfileImg();
                }
            });
            etDob.setOnClickListener(this);
            etDob.setText("");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String dueDate = sdf.format(calendar.getTime());

        etDob.setText(dueDate);


    }

    int isFromDate = 1;
    int yy, mm, dd;
    DatePickerDialog pickerDialog;
    WindowManager.LayoutParams lp;

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.registerEtDOB:
                    isFromDate = 1;
                    yy = calendar.get(Calendar.YEAR);
                    mm = calendar.get(Calendar.MONTH);
                    dd = calendar.get(Calendar.DAY_OF_MONTH);
                    Calendar c = Calendar.getInstance();
                    c.set(yy-18, mm, dd);
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

    private void choosProfileImg() {
        try {
            Typeface tf = Typeface.createFromAsset(ActivityRegister.this.getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(ActivityRegister.this);
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
                                Uri photoUri = FileProvider.getUriForFile(ActivityRegister.this, getPackageName() + ".provider", userImgPath);
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


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String imageFilePath = image.getAbsolutePath();

        return image;
    }


    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(ActivityRegister.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    private void requestStoragePermission(int i) {

        ActivityCompat.requestPermissions(ActivityRegister.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, i);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            //Checking the request code of our request
            if (requestCode == 7) {
                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(ActivityRegister.this, "Oops you just denied the permission",
                            Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 8) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Open your camera here.
                                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 8);*/
                    File wallpaperDirectory = new File(
                            Environment.getExternalStorageDirectory() + "/" + getResources().
                                    getString(R.string.app_name));
                    // have the object build the directory structure, if needed.
                    if (!wallpaperDirectory.exists()) {
                        wallpaperDirectory.mkdirs();
                    }
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    userImgPath = new File(wallpaperDirectory, Calendar.getInstance()
                            .getTimeInMillis() + ".jpg");
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(userImgPath));
                    startActivityForResult(i, 8);

                } else {

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String pathHolder = null;
                    try {
                        pathHolder = PathUtil.getPath(ActivityRegister.this, uri);
                        if (!new File(pathHolder).exists()) {
                            Toast.makeText(context, "Please select local sdcard images ...", Toast.LENGTH_LONG).show();
                        } else {
                            //  Toast.makeText(getActivity(), "" + pathHolder, Toast.LENGTH_SHORT).show();

                            Uri uri1 = Uri.fromFile(new File(pathHolder));
                            //  displayImage(userImgPath.getAbsolutePath(), 8);
                            Picasso.with(ActivityRegister.this).
                                    load(uri1).
                                    fit().placeholder(R.mipmap.login_user_profile_img).
                                    error(R.mipmap.login_user_profile_img).
                                    into(imgUser);
                            userImgPath = new File(pathHolder);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(ActivityRegister.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 8:
                try {
                    if (resultCode == RESULT_OK) {
                        //Toast.makeText(getActivity(), "" + outImg.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        if (userImgPath.exists()) {
                            Uri uri = Uri.fromFile(userImgPath);
                            //  displayImage(userImgPath.getAbsolutePath(), 8);
                            Picasso.with(ActivityRegister.this).
                                    load(uri).
                                    fit().placeholder(R.mipmap.login_user_profile_img).
                                    error(R.mipmap.login_user_profile_img).
                                    into(imgUser);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
        }
    }


    public static class PathUtil {
        /*
         * Gets the file path of the given Uri.
         */
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

}
