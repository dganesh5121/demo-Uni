package com.techindiana.school.parent;
/*
Created By: DGP 27/12/2017
Updated By: DGP
Class Name: ActivityDiscussions
Class Details: discussion  list shown which user has not seen yet or done after he log off from app...
*/

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techindiana.school.parent.Adapter.AdapterChatMsg;
import com.techindiana.school.parent.Module.TeacherMessagesInfo;
import com.techindiana.school.parent.Module.TeacherMessagesResp;
import com.techindiana.school.parent.Recorder.widget.VoiceRecorderView;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Utils.VerticalLineDecorator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityChatDetails extends BaseActivity implements View.OnClickListener {


    
    
    
    
    Timer timer = new Timer();
    public ActionBar actionBar;
    RecyclerView.LayoutManager manager;

    LinearLayout homeLyNoData;

    private RecyclerView invoiceRV;
    ArrayList<TeacherMessagesInfo> notificationInfos;
    List<TeacherMessagesInfo> notification;
    AdapterChatMsg adapterChatMsg;
    private Context mContext;
    private Retrofit retrofit;

    String fileType = "";


    EditText etPost;
    ImageView imgVoice, imgSend;
    String filePath = "";





    private final Handler handler = new Handler();
    protected VoiceRecorderView voiceRecorderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_details);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityChatDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(Constant.teacherName);
            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);
            actionBar.setDisplayShowTitleEnabled(true);
            Methods.getParameter(mContext);

        } catch (Exception ignored) {
        }

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityChatDetails.this, viewGroup);
        try {
            Declaration();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {
            Constant.fileUrl = "";




            homeLyNoData = (LinearLayout) findViewById(R.id.lessonLlyNoData);
            invoiceRV = (RecyclerView) findViewById(R.id.lessonRcRV);
            invoiceRV.setLayoutManager(manager);
            notification = new ArrayList<>();

            adapterChatMsg = new AdapterChatMsg(this, notification);
            adapterChatMsg.setLoadMoreListener(new AdapterChatMsg.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    invoiceRV.post(new Runnable() {
                        @Override
                        public void run() {
                            int index = notification.size() ;
                            loadMoreMsg(index);
                        }
                    });
                }
            });
            invoiceRV.setHasFixedSize(true);
           // LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
           // mLayoutManager.setReverseLayout(true);
           // mLayoutManager.setStackFromEnd(true);
// Set the layout manager to your recyclerview
          //  invoiceRV.setLayoutManager(mLayoutManager);
            invoiceRV.setLayoutManager(new LinearLayoutManager(context));
            invoiceRV.addItemDecoration(new VerticalLineDecorator(2));
            invoiceRV.setAdapter(adapterChatMsg);
            
            
            
            
            etPost = (EditText) findViewById(R.id.lessonDEtPost);
            imgVoice = (ImageView) findViewById(R.id.lessonDImgVoice);
            imgSend = (ImageView) findViewById(R.id.lessonDImgSend);
            imgSend.setOnClickListener(this);
            imgVoice.setOnClickListener(this);

            getMsg(0);

            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @SuppressWarnings("unchecked")
                        public void run() {
                            try {
                                getMsgBackgroung();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 5000, 4000);

            voiceRecorderView = (VoiceRecorderView) findViewById(R.id.voice_recorder);
            imgVoice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    }

                    return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                        @Override
                        public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                            Log.e("voiceFilePath=", voiceFilePath + "  time = " + voiceTimeLength);

                            Toast.makeText(mContext, "voiceFilePath="+ voiceFilePath + "  time = " + voiceTimeLength, Toast.LENGTH_SHORT).show();
                            filePath=voiceFilePath;
                            new sendMsg(ActivityChatDetails.this).execute();
                        }
                    });
                }
            });
            if (!isReadStorageAllowed())
            requestStoragePermission(8);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this,
             Manifest.permission.RECORD_AUDIO);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(int i) {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, i);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    //TODO: Web call for Notification Details...
    private void getMsg(int index) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<TeacherMessagesResp> call = restInterface.getTeacherMessages(Constant.apiKey, Constant.childID, Constant.teacherId, Constant.userID,  ""+index);
            call.enqueue(new Callback<TeacherMessagesResp>() {
                @Override
                public void onResponse(Call<TeacherMessagesResp> call, Response<TeacherMessagesResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                notificationInfos = (ArrayList<TeacherMessagesInfo>) response.body().getTeacherMessagesInfo();

                                if (notificationInfos.size() > 0) {
                                    //adapterUnReadDiscussion = new AdapterChatMsg(context, notificationInfos);
                                    //  invoiceRV.setAdapter(adapterUnReadDiscussion);
                                    notification.addAll(notificationInfos);
                                    adapterChatMsg.notifyDataChanged();
                                } else {
                                    invoiceRV.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ActivityChatDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<TeacherMessagesResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }

    //TODO: Web call for load more Notification Details...
    private void loadMoreMsg(int index) {
        try {
            //add loading progress view
            notification.add(new TeacherMessagesInfo("load"));
            adapterChatMsg.notifyItemInserted(notification.size()-1);
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report
        
            Call<TeacherMessagesResp> call = restInterface.getTeacherMessages(Constant.apiKey, Constant.childID, Constant.teacherId, Constant.userID,  ""+index);
            call.enqueue(new Callback<TeacherMessagesResp>() {
                @Override
                public void onResponse(Call<TeacherMessagesResp> call, Response<TeacherMessagesResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                notificationInfos = (ArrayList<TeacherMessagesInfo>) response.body().getTeacherMessagesInfo();
                                //remove loading view
                                notification.remove(notification.size()-1);

                                List<TeacherMessagesInfo> result = notificationInfos;
                                if(result.size()>0){
                                    //add loaded data
                                    notification.addAll(result);
                                }else{//result size 0 means there is no more data available at server
                                    adapterChatMsg.setMoreDataAvailable(false);
                                    //telling adapter to stop calling load more as no more server data available
                                 //   Toast.makeText(context,"No More Data Available",Toast.LENGTH_SHORT).show();
                                }
                                adapterChatMsg.notifyDataChanged();

                            } else {
                                notification.remove(notification.size()-1);
                                adapterChatMsg.setMoreDataAvailable(false);
                                adapterChatMsg.notifyDataChanged();
                                //telling adapter to stop calling load more as no more server data available
                             //   Toast.makeText(context,"No More Data Available",Toast.LENGTH_SHORT).show();
                                //   Toast.makeText(ActivityChatDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<TeacherMessagesResp> call, Throwable t) {
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
            switch (v.getId()) {
                case R.id.lessonDImgSend:
                    try {
                        if (etPost.getText().toString().trim().length() > 0) {
                            new sendMsg(ActivityChatDetails.this).execute();
                        } else {
                            Toast.makeText(mContext, "Please add some comment...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO: Web call for Notification Details...
    private void getMsgBackgroung() {
        try {

            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<TeacherMessagesResp> call = restInterface.getNewTeacherMessages(Constant.apiKey, Constant.childID, Constant.teacherId, Constant.userID);
            call.enqueue(new Callback<TeacherMessagesResp>() {
                @Override
                public void onResponse(Call<TeacherMessagesResp> call, Response<TeacherMessagesResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                notificationInfos = (ArrayList<TeacherMessagesInfo>) response.body().getTeacherMessagesInfo();
                               // notification.clear();
                                if (notificationInfos.size() > 0) {
                                    //adapterUnReadDiscussion = new AdapterChatMsg(context, notificationInfos);
                                    //  invoiceRV.setAdapter(adapterUnReadDiscussion);
                                    notification.addAll(0,notificationInfos);
                                    adapterChatMsg.notifyDataChanged();
                                } else {
                                    invoiceRV.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                             //   Toast.makeText(ActivityChatDetails.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }catch (Exception e){
                        e.getMessage();

                    }
                }

                @Override
                public void onFailure(Call<TeacherMessagesResp> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    public class sendMsg extends AsyncTask<Void, Void, String> {
        private Context mContext;

        public sendMsg(Context mContext) {
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
                    etPost.setText("");
                    JSONArray ja_data = jo_result.getJSONArray("data");
                    for (int i = 0; i <ja_data.length() ; i++) {
                        JSONObject jObj = ja_data.getJSONObject(i);
                        notification.add(0,new TeacherMessagesInfo(
                                jObj.getString("message_id"),
                                jObj.getString("sender_type"),
                                jObj.getString("msg_content"),
                                jObj.getString("msg_time"),
                                jObj.getString("type")));
                    }
                    adapterChatMsg.notifyDataChanged();
                 //   getBGData();
                   // getMsgBackgroung();
                }
            } catch (JSONException e) {
                hideProgress();
                e.printStackTrace();
                //  Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                hideProgress();
                Log.e("Doc Add:- ", "" + e.getMessage());
            }
        }

        private String doFileUpload() {
            String response_str = null;
            try {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Constant.webPath + "sendMessageToTeacher");
                post.setHeader("API-KEY", Constant.apiKey);


                MultipartEntity reqEntity = new MultipartEntity();
                //Charset chars = Charset.forName("UTF-8");
              //  DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              //  String date = df1.format(Calendar.getInstance().getTime());
                if (filePath.length() > 0) {
                    File purInterFilePath = new File(filePath);
                    FileBody inerFilePur = new FileBody(purInterFilePath, "UTF-8");
                    reqEntity.addPart("audio_msg", inerFilePur);
                    reqEntity.addPart("type", new StringBody("audio"));
                    reqEntity.addPart("message", new StringBody(""));
                } else {
                    reqEntity.addPart("message", new StringBody(etPost.getText().toString().trim()));
                    reqEntity.addPart("type", new StringBody("text"));
                }

                reqEntity.addPart("school_id", new StringBody(Constant.childSchoolId));
                reqEntity.addPart("student_id", new StringBody(Constant.childID));
                reqEntity.addPart("teacher_id", new StringBody(Constant.teacherId));
                reqEntity.addPart("parent_id", new StringBody(Constant.userID));
                reqEntity.addPart("class_id", new StringBody(Constant.childClassId));
                reqEntity.addPart("div_id", new StringBody(Constant.childDivId));



                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();
                response_str = EntityUtils.toString(resEntity);
                Log.e("Upload Resonse", "" + response_str);
            } catch (NullPointerException e) {
                // hideProgress();
                e.getMessage();
                Log.e("NPE:- ", "" + e.getMessage());
            } catch (Exception ex) {
                hideProgress();
                //   Toast.makeText(mContext, "File not found. Please try to upload new file... ", Toast.LENGTH_SHORT).show();
                //    Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            return response_str;
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
