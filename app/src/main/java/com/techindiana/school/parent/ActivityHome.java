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
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.joaquimley.faboptions.FabOptions;
import com.squareup.picasso.Picasso;
import com.techindiana.school.parent.Adapter.AdapterHomeGallery;
import com.techindiana.school.parent.Adapter.AdapterHomeNotification;
import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Module.HomePageBanner;
import com.techindiana.school.parent.Module.HomePageGallery;
import com.techindiana.school.parent.Module.HomePageNotice;
import com.techindiana.school.parent.Module.HomePageNotification;
import com.techindiana.school.parent.Module.HomePageResp;
import com.techindiana.school.parent.SlidingDeck.SlidingDeck;
import com.techindiana.school.parent.SlidingDeck.SlidingDeckAdapter;
import com.techindiana.school.parent.SlidingDeck.SlidingDeckModel;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityHome extends BaseActivity implements View.OnClickListener {

    public ActionBar actionBar;
    public TextView tvUserName;
    public ImageView imgChild1, imgChild2, imgChild3;
    public ImageView imgAChild1, imgAChild2, imgAChild3;
    public TextView tvNameChild1, tvNameChild2, tvNameChild3;
    DrawerLayout drawer;
    NavigationView mNavigationView;

    private Retrofit retrofit;

    private SlidingDeck slidingDeck;
    private SlidingDeckAdapter slidingAdapter;
    private FabOptions mFabOptions;
    TableRow homeMnTrHome, homeMnTrNoticeBoard, homeMnTrTimeTable, homeMnTrActictyCenter, homeMnTrHomeWork, homeMnTrAttendance, homeMnTrExtraClasses, homeMnTrBusTracking, homeMnTrChat, homeMnTrGallery, homeMnTrHealthReport, homeMnTrProgressReport, homeMnTrPolls, homeMnTrSettings, homeMnTrSchoolInfo;
ImageView homeBanner;
    RecyclerView.LayoutManager managerGallery;
    RecyclerView.Adapter adapterGallery;
    RecyclerView rvGallery;

    RecyclerView rvNotification;
    RecyclerView.LayoutManager managerNotification;
    RecyclerView.Adapter adapterNotification;

    LinearLayout homeLyNoData;
    private Context mContext;
    ArrayList<HomePageBanner> homePageBanners;
    int imgPos=0;
    private final Handler handler = new Handler();
    Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            mContext = ActivityHome.this;

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            retrofit = RetrofitUtils.getRetrofit();
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            mNavigationView = (NavigationView) findViewById(R.id.nav_view);
            Declaration();

            try {
                assert actionBar != null;
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.mipmap.menu_ic);
                actionBar.setTitle(getString(R.string.app_name));

                Spannable text = new SpannableString(actionBar.getTitle());
                text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                actionBar.setTitle(text);
                actionBar.setDisplayShowTitleEnabled(true);
            } catch (Exception ignored) {
            }
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            Methods.setFont(mContext, viewGroup);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    //TODO all component declaraation ...
    private void Declaration() {
        try {

            rvGallery = (RecyclerView) findViewById(R.id.homeRvGallery);
            managerGallery = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvGallery.setLayoutManager(managerGallery);



            rvNotification = (RecyclerView) findViewById(R.id.homeRvNotification);
            managerNotification = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvNotification.setLayoutManager(managerNotification);

            mFabOptions = (FabOptions) findViewById(R.id.fab_options);
            mFabOptions.setOnClickListener(this);

       homeBanner = (ImageView) findViewById(R.id.homeBanner);
            imgChild1 = (ImageView) mNavigationView.findViewById(R.id.homeImgUser);
            imgChild2 = (ImageView) mNavigationView.findViewById(R.id.homeImgUser2);
            imgChild3 = (ImageView) mNavigationView.findViewById(R.id.homeImgUser3);
            imgAChild1 = (ImageView) mNavigationView.findViewById(R.id.homeImgAUser);
            imgAChild2 = (ImageView) mNavigationView.findViewById(R.id.homeImgAUser2);
            imgAChild3 = (ImageView) mNavigationView.findViewById(R.id.homeImgAUser3);
            tvNameChild1 = (TextView) mNavigationView.findViewById(R.id.homeTvUserName);
            tvNameChild2 = (TextView) mNavigationView.findViewById(R.id.homeTvUserName2);
            tvNameChild3 = (TextView) mNavigationView.findViewById(R.id.homeTvUserName3);

            imgChild1.setOnClickListener(this);
            imgChild2.setOnClickListener(this);
            imgChild3.setOnClickListener(this);
            tvNameChild1.setOnClickListener(this);
            tvNameChild2.setOnClickListener(this);
            tvNameChild3.setOnClickListener(this);

            homeMnTrHome = (TableRow) mNavigationView.findViewById(R.id.homeMnTrHome);
            homeMnTrNoticeBoard = (TableRow) mNavigationView.findViewById(R.id.homeMnTrNoticeBoard);
            homeMnTrTimeTable = (TableRow) mNavigationView.findViewById(R.id.homeMnTrTimeTable);
            homeMnTrActictyCenter = (TableRow) mNavigationView.findViewById(R.id.homeMnTrActivityCenter);
            homeMnTrHomeWork = (TableRow) mNavigationView.findViewById(R.id.homeMnTrHomeWork);
            homeMnTrAttendance = (TableRow) mNavigationView.findViewById(R.id.homeMnTrAttendance);
            homeMnTrExtraClasses = (TableRow) mNavigationView.findViewById(R.id.homeMnTrExtraClass);
            homeMnTrBusTracking = (TableRow) mNavigationView.findViewById(R.id.homeMnTrBusTracking);
            homeMnTrChat = (TableRow) mNavigationView.findViewById(R.id.homeMnTrChat);
            homeMnTrGallery = (TableRow) mNavigationView.findViewById(R.id.homeMnTrGallery);
            homeMnTrHealthReport = (TableRow) mNavigationView.findViewById(R.id.homeMnTrHelathReport);
            homeMnTrProgressReport = (TableRow) mNavigationView.findViewById(R.id.homeMnTrProgressReport);
            homeMnTrPolls = (TableRow) mNavigationView.findViewById(R.id.homeMnTrPolls);
            homeMnTrSettings = (TableRow) mNavigationView.findViewById(R.id.homeMnTrSetting);
            homeMnTrSchoolInfo = (TableRow) mNavigationView.findViewById(R.id.homeMnTrSchoolInfo);

            homeMnTrHome.setOnClickListener(this);
            homeMnTrNoticeBoard.setOnClickListener(this);
            homeMnTrTimeTable.setOnClickListener(this);
            homeMnTrActictyCenter.setOnClickListener(this);
            homeMnTrHomeWork.setOnClickListener(this);
            homeMnTrAttendance.setOnClickListener(this);
            homeMnTrExtraClasses.setOnClickListener(this);
            homeMnTrBusTracking.setOnClickListener(this);
            homeMnTrChat.setOnClickListener(this);
            homeMnTrGallery.setOnClickListener(this);
            homeMnTrHealthReport.setOnClickListener(this);
            homeMnTrProgressReport.setOnClickListener(this);
            homeMnTrPolls.setOnClickListener(this);
            homeMnTrSettings.setOnClickListener(this);
            homeMnTrSchoolInfo.setOnClickListener(this);
            // diplayChildMenu();
            initializeSlidingDeck();

        } catch (Exception e) {

        }
    }


    private void initializeSlidingDeck() {

        slidingDeck = (SlidingDeck) findViewById(R.id.slidingDeck);

        //slidingDeck.setEmptyView(findViewById(R.id.emptyView));
        slidingDeck.setSwipeEventListener(new SlidingDeck.SwipeEventListener() {
            @Override
            public void onSwipe(SlidingDeck parent, View item) {
                SlidingDeckModel model = (SlidingDeckModel) item.getTag();
                slidingAdapter.remove(model);
                slidingAdapter.insert(model, slidingAdapter.getCount());
                slidingAdapter.notifyDataSetChanged();
            }
        });
        slidingDeck.setOnClickListener(this);
        if (!Methods.isOnline(ActivityHome.this)) {
            Methods.alertMsg(ActivityHome.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
        } else {

            getHomePageDat();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!Methods.isOnline(ActivityHome.this)) {
                //   Methods.alertMsg(ActivityHome.this, getResources().getString(R.string.err_network_failure_title), getResources().getString(R.string.err_network_failure_message), "OK");
            } else {

                getHomePageDatBackground();
              //  ParentDBAL.getUserDetailsDB(this);
               // ParentDBAL.getChildDetails(ActivityHome.this);
                diplayChildMenu();

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void diplayChildMenu() {


        try {

            if (Constant.childList.size() > 2) {
                imgChild1.setVisibility(View.VISIBLE);
                imgChild2.setVisibility(View.VISIBLE);
                imgChild3.setVisibility(View.VISIBLE);
                tvNameChild1.setVisibility(View.VISIBLE);
                tvNameChild2.setVisibility(View.VISIBLE);
                tvNameChild3.setVisibility(View.VISIBLE);
            } else if (Constant.childList.size() > 1) {
                imgChild1.setVisibility(View.VISIBLE);
                imgChild2.setVisibility(View.VISIBLE);
                imgChild3.setVisibility(View.INVISIBLE);
                tvNameChild1.setVisibility(View.VISIBLE);
                tvNameChild2.setVisibility(View.VISIBLE);
                tvNameChild3.setVisibility(View.INVISIBLE);
            } else if (Constant.childList.size() > 0) {
                imgChild1.setVisibility(View.VISIBLE);
                imgChild2.setVisibility(View.INVISIBLE);
                imgChild3.setVisibility(View.INVISIBLE);
                tvNameChild1.setVisibility(View.VISIBLE);
                tvNameChild2.setVisibility(View.INVISIBLE);
                tvNameChild3.setVisibility(View.INVISIBLE);
            }
            for (int i = 0; i < Constant.childList.size(); i++) {
                if (Constant.childList.get(i).get("childID").equals(Constant.child1)) {
                    Picasso.with(ActivityHome.this).
                            load(Constant.webImgPath + Constant.childImg).
                            placeholder(R.mipmap.login_user_profile_img).
                            error(R.mipmap.login_user_profile_img).
                            into(imgChild1);
                    if (Constant.childVarified.equals("1")) {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.active_child_ic).
                                into(imgAChild1);
                    } else {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.deactiv_child_ic).
                                into(imgAChild1);
                    }

                    tvNameChild1.setText(Constant.childFName + " " + Constant.childLName);
                } else if (Constant.childList.get(i).get("childID").equals(Constant.child2)) {
                    Picasso.with(ActivityHome.this).
                            load(Constant.webImgPath + Constant.childList.get(i).get("childImg")).
                            placeholder(R.mipmap.login_user_profile_img).
                            error(R.mipmap.login_user_profile_img).
                            into(imgChild2);
                    if (Constant.childList.get(i).get("childVarified").equals("1")) {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.active_child_ic).
                                into(imgAChild2);
                    } else {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.deactiv_child_ic).
                                into(imgAChild2);
                    }
                    tvNameChild2.setText(Constant.childList.get(i).get("childFName"));
                } else if (Constant.childList.get(i).get("childID").equals(Constant.child3)) {
                    Picasso.with(ActivityHome.this).
                            load(Constant.webImgPath + Constant.childList.get(i).get("childImg")).
                            placeholder(R.mipmap.login_user_profile_img).
                            error(R.mipmap.login_user_profile_img).
                            into(imgChild3);
                    if (Constant.childList.get(i).get("childVarified").equals("1")) {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.active_child_ic).
                                into(imgAChild3);
                    } else {
                        Picasso.with(ActivityHome.this).
                                load(R.mipmap.deactiv_child_ic).
                                into(imgAChild3);
                    }
                    tvNameChild3.setText(Constant.childList.get(i).get("childFName"));
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        String tempId;
        try {
            switch (v.getId()) {
                case R.id.homeImgUser:
                    break;
                case R.id.homeImgUser2:
                    try {
                        tempId = Constant.child1;
                        Constant.child1 = Constant.child2;
                        Constant.child2 = tempId;
                        for (int i = 0; i < Constant.childList.size(); i++) {
                            if (Constant.childList.get(i).get("childID").equals(Constant.child1)) {
                                Constant.childID = Constant.childList.get(i).get("childID");
                                Constant.childFName = Constant.childList.get(i).get("childFName");
                                Constant.childLName = Constant.childList.get(i).get("childLName");
                                Constant.childSchoolId = Constant.childList.get(i).get("childSchoolId");
                                Constant.childClassId = Constant.childList.get(i).get("childClassId");
                                Constant.childDivId = Constant.childList.get(i).get("childDivId");
                                Constant.childImg = Constant.childList.get(i).get("childImg");
                                Constant.childVarified = Constant.childList.get(i).get("childVarified");
                                Constant.childActive = Constant.childList.get(i).get("childActive");
                            }
                        }
                        diplayChildMenu();
                        switchStudent();
                        getHomePageDatBackground();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;
                case R.id.homeImgUser3:
                    try {
                        tempId = Constant.child1;
                        Constant.child1 = Constant.child3;
                        Constant.child3 = tempId;
                        for (int i = 0; i < Constant.childList.size(); i++) {
                            if (Constant.childList.get(i).get("childID").equals(Constant.child1)) {
                                Constant.childID = Constant.childList.get(i).get("childID");
                                Constant.childFName = Constant.childList.get(i).get("childFName");
                                Constant.childLName = Constant.childList.get(i).get("childLName");
                                Constant.childSchoolId = Constant.childList.get(i).get("childSchoolId");
                                Constant.childClassId = Constant.childList.get(i).get("childClassId");
                                Constant.childDivId = Constant.childList.get(i).get("childDivId");
                                Constant.childImg = Constant.childList.get(i).get("childImg");
                                Constant.childVarified = Constant.childList.get(i).get("childVarified");
                                Constant.childActive = Constant.childList.get(i).get("childActive");

                            }
                        }
                        diplayChildMenu();
                        switchStudent();
                        getHomePageDatBackground();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;
                case R.id.homeMnTrHome:
                    intent = new Intent(ActivityHome.this, ActivityHome.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.slidingDeck:
                case R.id.homeMnTrNoticeBoard:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityNoticeBoard.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrTimeTable:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityTimeTable.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.faboptions_activity_center:
                case R.id.homeMnTrActivityCenter:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        mFabOptions.setButtonColor(R.id.faboptions_activity_center, R.color.white);
                        intent = new Intent(ActivityHome.this, ActivityCenter.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;

                case R.id.faboptions_home_work:
                case R.id.homeMnTrHomeWork:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        mFabOptions.setButtonColor(R.id.faboptions_home_work, R.color.white);
                        intent = new Intent(ActivityHome.this, ActivityHomeWork.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.faboptions_attendance:
                case R.id.homeMnTrAttendance:

                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        mFabOptions.setButtonColor(R.id.faboptions_attendance, R.color.white);
                        intent = new Intent(ActivityHome.this, ActivityAttendance.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrExtraClass:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityExtraClass.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.faboptions_bus_tracking:
                case R.id.homeMnTrBusTracking:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        mFabOptions.setButtonColor(R.id.faboptions_bus_tracking, R.color.white);
                        intent = new Intent(ActivityHome.this, ActivityBusTracking.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrChat:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityChat.class);
                           startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrGallery:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityGallery.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrHelathReport:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityHealthReport.class);
                        startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrProgressReport:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityHome.class);
                        //  startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrPolls:
                    if (Constant.childVarified.equals("1") && Constant.childActive.equals("1")) {
                        intent = new Intent(ActivityHome.this, ActivityHome.class);
                        //  startActivity(intent);
                    } else {
                        dilogChildNotVerified();
                    }
                    break;
                case R.id.homeMnTrSetting:
                    intent = new Intent(ActivityHome.this, ActivitySettings.class);
                    startActivity(intent);
                    break;
                case R.id.homeMnTrSchoolInfo:
                    intent = new Intent(ActivityHome.this, ActivitySchoolInfo.class);
                    startActivity(intent);
                    break;

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void dilogChildNotVerified() {
        try {
            if (drawer.isDrawerOpen(mNavigationView)) {
                drawer.closeDrawer(mNavigationView);
            }
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
            tv_ExitMsg.setText(Constant.childFName + " is not yet verified from school admin.\n Please do contact to school admin for more information.");
            tv_ExitNo.setText("OK");
            tv_ExitMsg.setTypeface(tf);
            tv_ExitYes.setVisibility(View.GONE);
            tv_ExitNo.setTypeface(tf);
            tv_ExitYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
    private void getHomePageDat() {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<HomePageResp> call = restInterface.getDashboardUpdates(Constant.apiKey, Constant.childSchoolId, Constant.childClassId, Constant.childDivId, Constant.childID, Constant.userID);
            call.enqueue(new Callback<HomePageResp>() {
                @Override
                public void onResponse(Call<HomePageResp> call, Response<HomePageResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                              homePageBanners = (ArrayList<HomePageBanner>) response.body().getHomePageInfo().getHomePageBanner();
                                ArrayList<HomePageNotice> homePageNotices = (ArrayList<HomePageNotice>) response.body().getHomePageInfo().getHomePageNotice();
                                ArrayList<HomePageGallery> homePageGalleries = (ArrayList<HomePageGallery>) response.body().getHomePageInfo().getHomePageGallery();
                                ArrayList<HomePageNotification> homePageNotifications = (ArrayList<HomePageNotification>) response.body().getHomePageInfo().getHomePageNotification();
                               /* if(homePageBanners.size()>0)
                                {
                                    try{
                                        TimerTask doAsynchronousTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler.post(new Runnable() {
                                                    @SuppressWarnings("unchecked")
                                                    public void run() {
                                                        try {
                                                            Picasso.with(ActivityHome.this).
                                                                    load(Constant.webImgPath +homePageBanners.get(imgPos).getImage()).
                                                                    placeholder(R.drawable.home_bg).
                                                                    error(R.drawable.home_bg).
                                                                    into(homeBanner);
                                                            if(homePageBanners.size()-1<=imgPos)
                                                                imgPos=0;
                                                            else
                                                                imgPos=imgPos+1;
                                                        } catch (Exception e) {
                                                            e.getMessage();
                                                        }
                                                    }
                                                });
                                            }
                                        };
                                        timer.schedule(doAsynchronousTask, 000, 5000);
                                    }catch (Exception e){
                                        e.getMessage();
                                    }
                                }*/

                                if (homePageNotices.size() > 0) {
                                    slidingAdapter = new SlidingDeckAdapter(ActivityHome.this);
                                    for (int i = 0; i < homePageNotices.size(); i++) {
                                        slidingAdapter.add(new SlidingDeckModel(
                                                homePageNotices.get(i).getId(),
                                                homePageNotices.get(i).getCategory(),
                                                homePageNotices.get(i).getTitle(),
                                                homePageNotices.get(i).getDescription(),
                                                homePageNotices.get(i).getNbDate(),
                                                homePageNotices.get(i).getNbTime(),
                                                homePageNotices.get(i).getImage()
                                        ));


                                    }
                                    slidingDeck.setAdapter(slidingAdapter);
                                } else {
                                    slidingDeck.setVisibility(View.GONE);
                                }


                                if (homePageGalleries.size() > 0) {
                                    adapterGallery = new AdapterHomeGallery(context, homePageGalleries);
                                    rvGallery.setAdapter(adapterGallery);
                                }

                                if (homePageNotifications.size() > 0) {
                                    adapterNotification = new AdapterHomeNotification(context, homePageNotifications);
                                    rvNotification.setAdapter(adapterNotification);
                                }

                            } else {
                                Toast.makeText(ActivityHome.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<HomePageResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    //TODO: Web call for Notification Details...
    private void getHomePageDatBackground() {
        try {
            //     showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<HomePageResp> call = restInterface.getDashboardUpdates(Constant.apiKey, Constant.childSchoolId, Constant.childClassId, Constant.childDivId, Constant.childID, Constant.userID);
            call.enqueue(new Callback<HomePageResp>() {
                @Override
                public void onResponse(Call<HomePageResp> call, Response<HomePageResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                homePageBanners = (ArrayList<HomePageBanner>) response.body().getHomePageInfo().getHomePageBanner();
                                ArrayList<HomePageNotice> homePageNotices = (ArrayList<HomePageNotice>) response.body().getHomePageInfo().getHomePageNotice();
                                ArrayList<HomePageGallery> homePageGalleries = (ArrayList<HomePageGallery>) response.body().getHomePageInfo().getHomePageGallery();
                                ArrayList<HomePageNotification> homePageNotifications = (ArrayList<HomePageNotification>) response.body().getHomePageInfo().getHomePageNotification();

                              if(homePageBanners.size()>0){
                                  try{


                                      Runnable  r = new Runnable(){
                                          public void run(){
                                              Picasso.with(ActivityHome.this).
                                                      load(Constant.webImgPath +homePageBanners.get(imgPos).getImage()).
                                                      into(homeBanner);
                                              imgPos++;
                                              if(imgPos >= homePageBanners.size()){
                                                  imgPos= 0;
                                              }
                                              homeBanner.postDelayed(this, 8000); //set to go off again in 8 seconds.
                                          }
                                      };
                                      homeBanner.postDelayed(r,8000);

//
//                                      TimerTask doAsynchronousTask = new TimerTask() {
//                                          @Override
//                                          public void run() {
//                                              handler.post(new Runnable() {
//                                                  @SuppressWarnings("unchecked")
//                                                  public void run() {
//                                                      try {
//                                                          Picasso.with(ActivityHome.this).
//                                                                  load(Constant.webImgPath +homePageBanners.get(imgPos).getImage()).
//                                                                  placeholder(R.drawable.home_bg).
//                                                                  error(R.drawable.home_bg).
//                                                                  into(homeBanner);
//                                                          if(imgPos < homePageBanners.size()-1)
//                                                              imgPos++;
//                                                          else
//                                                              imgPos=0;
//                                                      } catch (Exception e) {
//                                                          e.getMessage();
//                                                      }
//                                                  }
//                                              });
//                                          }
//                                      };
//                                      timer.schedule(doAsynchronousTask, 0, 15000);
                                  }catch (Exception e){
                                      e.getMessage();
                                  }
                              }

                                if (homePageNotices.size() > 0) {
                                    slidingAdapter = new SlidingDeckAdapter(ActivityHome.this);
                                    for (int i = 0; i < homePageNotices.size(); i++) {
                                        slidingAdapter.add(new SlidingDeckModel(
                                                homePageNotices.get(i).getId(),
                                                homePageNotices.get(i).getCategory(),
                                                homePageNotices.get(i).getTitle(),
                                                homePageNotices.get(i).getDescription(),
                                                homePageNotices.get(i).getNbDate(),
                                                homePageNotices.get(i).getNbTime(),
                                                homePageNotices.get(i).getImage()
                                        ));


                                    }
                                    slidingDeck.setAdapter(slidingAdapter);
                                    slidingDeck.setVisibility(View.VISIBLE);
                                } else {
                                    slidingDeck.setVisibility(View.GONE);
                                }


                                if (homePageGalleries.size() > 0) {
                                    adapterGallery = new AdapterHomeGallery(context, homePageGalleries);
                                    rvGallery.setAdapter(adapterGallery);
                                }

                                if (homePageNotifications.size() > 0) {
                                    adapterNotification = new AdapterHomeNotification(context, homePageNotifications);
                                    rvNotification.setAdapter(adapterNotification);
                                }

                            } else {
                                Toast.makeText(ActivityHome.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        //  hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        //  hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<HomePageResp> call, Throwable t) {
                    //      hideProgress();
                }
            });
        } catch (Exception e) {
            //   hideProgress();
        }
    }




    //TODO: Web call for Notification Details...
    private void switchStudent() {
        try {
            //     showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);

            Call<BaseResp> call = restInterface.switchStudent(Constant.apiKey, Constant.childSchoolId,  Constant.childID, Constant.userID);
            call.enqueue(new Callback<BaseResp>() {
                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {

                            } else {
                                switchStudent();
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<BaseResp> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //TODO on navigation icon click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(mNavigationView)) {
                    drawer.closeDrawer(mNavigationView);
                } else {
                    drawer.openDrawer(mNavigationView);
                }
                break;

            case R.id.actionLib:
                intent = new Intent(ActivityHome.this, ActivityLibBooks.class);
                startActivity(intent);
                break;

            case R.id.actionNotification:
                intent = new Intent(ActivityHome.this, ActivityNotification.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //TODO  on Back Pressed ...
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

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
            tv_ExitYes.setTypeface(tf);
            tv_ExitNo.setTypeface(tf);
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
                }
            });
            dialog.show();
        }
    }

}
