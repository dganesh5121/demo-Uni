package com.techindiana.school.parent;
/*
Created By: DGP 04/01/2018
Updated By: DGP
Class Name:ActivitySettingsParentEdit
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Retrofit;


public class ActivitySettingsParentEdit extends BaseActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public ActionBar actionBar;
    File userImgPath;
    //String userIPath="";
    private Context mContext;
    private Retrofit retrofit;
    Button btnSave;
    EditText etFName, etLName, etEmail, etDOB, etMobile, etCAddress;

    ImageView imgProfile;
    private Calendar calendar = Calendar.getInstance();
    private String myFormat = "yyyy-MM-dd"; //In which you need put here
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_parent_edit);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivitySettingsParentEdit.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle("Profile Edit");
            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivitySettingsParentEdit.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    //TODO define the component used in the class ...
    private void Declaration() {
        try {
            btnSave = (Button) findViewById(R.id.sPEBtnSubmit);
            imgProfile = (ImageView) findViewById(R.id.sPEImgUser);
            etFName = (EditText) findViewById(R.id.sPEFEtName);
            etLName = (EditText) findViewById(R.id.sPEEtLName);
            etDOB = (EditText) findViewById(R.id.sPEEtDob);
            etMobile = (EditText) findViewById(R.id.sPEEtPhone);
            etEmail = (EditText) findViewById(R.id.sPEEtEmail);
            etCAddress = (EditText) findViewById(R.id.sPEEtAddress);

            btnSave.setOnClickListener(this);
            imgProfile.setOnClickListener(this);
            etFName.setText(Constant.settingInfoparents.get(0).getFname());
            etLName.setText(Constant.settingInfoparents.get(0).getLname());
            etDOB.setText(Constant.settingInfoparents.get(0).getDob());
            etMobile.setText(Constant.settingInfoparents.get(0).getContactNo());
            etEmail.setText(Constant.settingInfoparents.get(0).getEmail());
            etCAddress.setText(Constant.settingInfoparents.get(0).getAddress());
            etDOB.setOnClickListener(this);

            Picasso.with(ActivitySettingsParentEdit.this).
                    load(Constant.webImgPath + Constant.userImg).
                    fit().placeholder(R.mipmap.login_user_profile_img).
                    error(R.mipmap.login_user_profile_img).
                    into(imgProfile);
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
                case R.id.sPEBtnSubmit:
                    if (etFName.getText().toString().trim().length() == 0) {
                        etFName.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                    } else if (etLName.getText().toString().trim().length() == 0) {
                        etLName.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter your valid Name.</font>"));
                    } else if (etDOB.getText().toString().trim().length() == 0) {
                        etDOB.setError(Html
                                .fromHtml("<font color='#FF0000'>Please select valid date.</font>"));
                    } else if (etMobile.getText().toString().trim().length() < 10 && etMobile.getText().toString().trim().length() < 15) {
                        etMobile.setError(Html
                                .fromHtml("<font color='#FF0000'>Please enter your valid Mobile No</font>"));
                    } else {
                        new updateProfile(ActivitySettingsParentEdit.this).execute();

                    }
                    break;
                case R.id.sPEImgUser:
                    choosProfileImg();
                    break;

                case R.id.sPEEtDob:
                    isFromDate = 1;
                    yy = calendar.get(Calendar.YEAR);
                    mm = calendar.get(Calendar.MONTH);
                    dd = calendar.get(Calendar.DAY_OF_MONTH);


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        pickerDialog = new DatePickerDialog(this,
                                R.style.DialogTheme, this, yy, mm, dd);
                    } else {
                        pickerDialog = new DatePickerDialog(this, this, yy, mm, dd);
                    }
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


    //TODO upload a CV...
    public class updateProfile extends AsyncTask<Void, Void, String> {
        private Context mContext;

        public updateProfile(Context mContext) {
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
                    Toast.makeText(ActivitySettingsParentEdit.this, "Profile updated successfully ....", Toast.LENGTH_LONG).show();

                    onBackPressed();
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
                HttpPost post = new HttpPost(Constant.webPath + "updateParent");
                post.setHeader("API-KEY", Constant.apiKey);
                MultipartEntity reqEntity = new MultipartEntity();
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df1.format(Calendar.getInstance().getTime());
                if (userImgPath != null) {

                    FileBody inerFilePur = new FileBody(userImgPath, "UTF-8");
                    reqEntity.addPart("parent_img", inerFilePur);
                } else {
                }

                reqEntity.addPart("parent_id", new StringBody(Constant.userID));
                 reqEntity.addPart("fname", new StringBody(etFName.getText().toString().trim()));
                 reqEntity.addPart("lname", new StringBody(etLName.getText().toString().trim()));
                 reqEntity.addPart("dob", new StringBody(etDOB.getText().toString().trim()));
                 reqEntity.addPart("contact_no", new StringBody(etMobile.getText().toString().trim()));
                 reqEntity.addPart("email", new StringBody(etEmail.getText().toString().trim()));
                 reqEntity.addPart("address", new StringBody(etCAddress.getText().toString().trim()));
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
            Typeface tf = Typeface.createFromAsset(ActivitySettingsParentEdit.this.getAssets(), getResources().getString(R.string.font_regular));
            final Dialog dialog = new Dialog(ActivitySettingsParentEdit.this);
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
                                Uri photoUri = FileProvider.getUriForFile(ActivitySettingsParentEdit.this, getPackageName() + ".provider", userImgPath);
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
        int result = ContextCompat.checkSelfPermission(ActivitySettingsParentEdit.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }


    //TODO Requesting permission
    private void requestStoragePermission(int i) {

        ActivityCompat.requestPermissions(ActivitySettingsParentEdit.this, new String[]{
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
                    Toast.makeText(ActivitySettingsParentEdit.this, "Oops you just denied the permission",
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

                        pathHolder = ActivitySettingsParentEdit.PathUtil.getPath(ActivitySettingsParentEdit.this, uri);
                        if (!new File(pathHolder).exists()) {
                            Toast.makeText(context, "Please select local sdcard images ...", Toast.LENGTH_LONG).show();
                        } else {
                            Uri uri1 = Uri.fromFile(new File(pathHolder));
                            Glide.with(ActivitySettingsParentEdit.this).
                                    load(uri1).
                                    //  placeholder(R.mipmap.login_user_profile_img).
                                            error(R.mipmap.login_user_profile_img).
                                    into(imgProfile);
                            userImgPath = new File(pathHolder);
                            // userIPath=pathHolder;
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(ActivitySettingsParentEdit.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case 8:
                try {
                    if (resultCode == RESULT_OK) {
                        if (userImgPath.exists()) {
                            Uri uri = Uri.fromFile(userImgPath);
                            Glide.with(ActivitySettingsParentEdit.this).
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
}
