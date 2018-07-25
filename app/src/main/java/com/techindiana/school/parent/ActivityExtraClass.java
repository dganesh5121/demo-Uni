package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.techindiana.school.parent.Adapter.AdapterExtraClass;
import com.techindiana.school.parent.Calendar.CustomDayView;
import com.techindiana.school.parent.Module.ExtraClassDayWiseInfo;
import com.techindiana.school.parent.Module.ExtraClassDayWiseResp;
import com.techindiana.school.parent.Module.ExtraClassInfo;
import com.techindiana.school.parent.Module.ExtraClassResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityExtraClass extends BaseActivity {

    public ActionBar actionBar;

    private Context mContext;
    private Retrofit retrofit;
    TextView tvYear;
    TextView tvMonth,tvMonthName;
    //TextView backToday;
    CoordinatorLayout content;
    MonthPager monthPager;
   // RecyclerView rvToDoList;
    //TextView scrollSwitch;
    //TextView themeSwitch;
    TextView nextMonthBtn;
    TextView lastMonthBtn;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    HashMap<String, String> markData = new HashMap<>();
    ArrayList<ExtraClassInfo> extraClassInfos;
    RecyclerView.LayoutManager managerExtraClass;
    RecyclerView.Adapter adapterExtraClass;
    RecyclerView rvExtraClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_center);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityExtraClass.this;
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mExtraClasses));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityExtraClass.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void Declaration() {
        try {
            content = (CoordinatorLayout) findViewById(R.id.content);
            monthPager = (MonthPager) findViewById(R.id.calendar_view);
            //此处强行setViewHeight，毕竟你知道你的日历牌的高度
            monthPager.setViewHeight(Utils.dpi2px(context, 270));
            tvYear = (TextView) findViewById(R.id.show_year_view);
            tvMonth = (TextView) findViewById(R.id.show_month_view);
            tvMonthName = (TextView) findViewById(R.id.show_month_view_name);
            // backToday = (TextView) findViewById(R.id.back_today_button);
            //  scrollSwitch = (TextView) findViewById(R.id.scroll_switch);
            //  themeSwitch = (TextView) findViewById(R.id.theme_switch);
            nextMonthBtn = (TextView) findViewById(R.id.next_month);
            lastMonthBtn = (TextView) findViewById(R.id.last_month);

            rvExtraClass = (RecyclerView) findViewById(R.id.list);
            rvExtraClass.setLayoutManager(new LinearLayoutManager(this));

            // initCurrentDate();
            currentDate = new CalendarDate();
            tvYear.setText(currentDate.getYear() + "");
            tvMonth.setText(currentDate.getMonth() + "");
            tvMonthName.setText("(" + Constant.arrayMonth[currentDate.getMonth() - 1] + ")");
            getDataOfMonthChange(tvMonth.getText().toString().trim() + "", tvYear.getText().toString().trim()+ "");
            getSelectedDateEvent(currentDate.getDay() + "",currentDate.getMonth() + "",currentDate.getYear() + "");


            // initCalendarView();
            // initListener();
            onSelectDateListener = new OnSelectDateListener() {
                @Override
                public void onSelectDate(CalendarDate date) {
                    currentDate = date;
                    tvYear.setText(currentDate.getYear() + "");
                    tvMonth.setText(currentDate.getMonth() + "");
                    tvMonthName.setText("(" + Constant.arrayMonth[currentDate.getMonth() - 1] + ")");
                    getSelectedDateEvent(currentDate.getDay() + "",currentDate.getMonth() + "",currentDate.getYear() + "");

                    //  refreshClickDate(date);
                }

                @Override
                public void onSelectOtherMonth(int offset) {
                    monthPager.selectOtherMonth(offset);
                }
            };

            CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
            calendarAdapter = new CalendarViewAdapter(
                    context,
                    onSelectDateListener,
                    CalendarAttr.WeekArrayType.Monday,
                    customDayView);
            calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
                @Override
                public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                    //     rvToDoList.scrollToPosition(0);
                }
            });
            initMarkData();

            monthPager.setAdapter(calendarAdapter);
            monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
            monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    position = (float) Math.sqrt(1 - Math.abs(position));
                    page.setAlpha(position);
                }
            });


            monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentPage = position;
                    currentCalendars = calendarAdapter.getPagers();
                    if (currentCalendars.get(position % currentCalendars.size()) != null) {
                        CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                        currentDate = date;
                        tvYear.setText(currentDate.getYear() + "");
                        tvMonth.setText(currentDate.getMonth() + "");
                        tvMonthName.setText("(" + Constant.arrayMonth[currentDate.getMonth() - 1] + ")");
                        try {

                            getDataOfMonthChange(tvMonth.getText().toString().trim() + "", tvYear.getText().toString().trim()+ "");
                        }catch (Exception e){
                            e.getMessage();
                        }
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            // initMonthPager();


            //  initToolbarClickListener();
            nextMonthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);

                }
            });

            lastMonthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                }
            });
            if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                //  Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
                calendarAdapter.switchToMonth();
            } else {
                //  Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
                calendarAdapter.switchToWeek(monthPager.getRowIndex());
            }
            try {

                String[] dateYear = Constant.currentDate.split(" ");
                String[] date = dateYear[0].split("-");
                getDataOfMonth(date[1] + "", date[2]);

            } catch (Exception e) {
                e.getMessage();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    /**
     * onWindowFocusChanged
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            CalendarDate today = new CalendarDate();
            calendarAdapter.notifyDataChanged(today);
            //   tvYear.setText(today.getYear() + "year");
            //  tvMonth.setText(today.getMonth() + "");
            tvYear.setText(currentDate.getYear() + "");
            tvMonth.setText(currentDate.getMonth() + "");
            tvMonthName.setText("(" + Constant.arrayMonth[currentDate.getMonth() - 1] + ")");
            //  refreshMonthPager();
            initiated = true;
        }
    }


    private void initMarkData() {
        calendarAdapter.setMarkData(markData);
    }


    //TODO: Web call for month event Details...
    private void getDataOfMonth(String month, String Year) {
        try {
            showProgress();

            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<ExtraClassResp> call = restInterface.getExtraClass(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, month, Year, Constant.childID);
            call.enqueue(new Callback<ExtraClassResp>() {

                @Override
                public void onResponse(Call<ExtraClassResp> call, Response<ExtraClassResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            try {
                                extraClassInfos = (ArrayList<ExtraClassInfo>) response.body().getExtraClassInfo();
                                for (int i = 0; i < extraClassInfos.size(); i++) {
                                    String inputPattern = "yyyy-mm-dd";
                                    String outputPattern = "yyyy-m-d";
                                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                    Date date = null;
                                    String str = null;

                                    try {
                                        date = inputFormat.parse(extraClassInfos.get(i).getDate());
                                        str = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    markData.put(str, "0");
                                }
                                Utils.setMarkData(markData);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        } else {
                            Toast.makeText(ActivityExtraClass.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<ExtraClassResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    //TODO: Web call for change month event Details...
    private void getDataOfMonthChange(String month, String Year) {
        try {
            // showProgress();

            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            Call<ExtraClassResp> call = restInterface.getExtraClass(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, month, Year, Constant.childID);
            call.enqueue(new Callback<ExtraClassResp>() {

                @Override
                public void onResponse(Call<ExtraClassResp> call, Response<ExtraClassResp> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            try {
                                extraClassInfos = (ArrayList<ExtraClassInfo>) response.body().getExtraClassInfo();
                                for (int i = 0; i < extraClassInfos.size(); i++) {
                                    String inputPattern = "yyyy-mm-dd";
                                    String outputPattern = "yyyy-m-d";
                                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                    Date date = null;
                                    String str = null;

                                    try {
                                        date = inputFormat.parse(extraClassInfos.get(i).getDate());
                                        str = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    markData.put(str, "0");
                                }
                                Utils.setMarkData(markData);

                            } catch (Exception e) {
                                e.getMessage();
                            }
                        } else {
                            // Toast.makeText(ActivityCenter.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<ExtraClassResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    //TODO: Web call for Notification Details...
    private void getSelectedDateEvent(String day,String  month,String  Year) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<ExtraClassDayWiseResp> call = restInterface.getExtraClassDayWise(Constant.apiKey, Constant.childSchoolId, Constant.childClassId,
                    Constant.childDivId, day, month, Year, Constant.childID);
            call.enqueue(new Callback<ExtraClassDayWiseResp>() {
                @Override
                public void onResponse(Call<ExtraClassDayWiseResp> call, Response<ExtraClassDayWiseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                ArrayList<ExtraClassDayWiseInfo> extraClassDayWiseInfos = (ArrayList<ExtraClassDayWiseInfo>) response.body().getExtraClassDayWiseInfo();
                                if (extraClassDayWiseInfos.size() > 0) {
                                    adapterExtraClass = new AdapterExtraClass(context, extraClassDayWiseInfos);
                                    rvExtraClass.setAdapter(adapterExtraClass);
                                    // rvACenter.setVisibility(View.VISIBLE);
                                }
                            } else {
                                ArrayList<ExtraClassDayWiseInfo> extraClassDayWiseInfos=null;
                                adapterExtraClass = new AdapterExtraClass(context, extraClassDayWiseInfos);
                                rvExtraClass.setAdapter(adapterExtraClass);
                                //rvACenter.setVisibility(View.GONE);
                                Toast.makeText(ActivityExtraClass.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<ExtraClassDayWiseResp> call, Throwable t) {
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
        inflater.inflate(R.menu.menu_main, menu);
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
                intent = new Intent(ActivityExtraClass.this, ActivityLibBooks.class);
                startActivity(intent);
                break;
            case R.id.actionNotification:
                intent = new Intent(ActivityExtraClass.this, ActivityNotification.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
