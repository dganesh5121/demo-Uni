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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techindiana.school.parent.Adapter.AdapterGallery;
import com.techindiana.school.parent.Module.GalleryInfo;
import com.techindiana.school.parent.Module.GalleryResp;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Utils.VerticalLineDecorator;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivityGallery extends BaseActivity {

    public ActionBar actionBar;
    RecyclerView.LayoutManager manager;
    LinearLayout homeLyNoData;
    private Context mContext;
    private Retrofit retrofit;
    private RecyclerView invoiceRV;
    ArrayList<GalleryInfo> galleryInfos;
    List<GalleryInfo> galleryListDat;
    AdapterGallery adapterGallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityGallery.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(getResources().getString(R.string.mGallery));


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityGallery.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {

            homeLyNoData = (LinearLayout) findViewById(R.id.homeLlyNoData);
            invoiceRV = (RecyclerView) findViewById(R.id.homeRcRV);
            invoiceRV.setLayoutManager(manager);
            galleryListDat = new ArrayList<>();

            adapterGallery = new AdapterGallery(this, galleryListDat);
            adapterGallery.setLoadMoreListener(new AdapterGallery.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    invoiceRV.post(new Runnable() {
                        @Override
                        public void run() {
                            int index = galleryListDat.size() ;
                            loadMoreNotification(index);
                        }
                    });
                    }
            });
            invoiceRV.setHasFixedSize(true);

            invoiceRV.setLayoutManager(new GridLayoutManager(context, 2));
            invoiceRV.addItemDecoration(new VerticalLineDecorator(2));
            invoiceRV.setAdapter(adapterGallery);


            getNotification(0);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        try {

        } catch (Exception e) {
            e.getMessage();
        }
    }




    //TODO: Web call for Notification Details...
    private void getNotification(int index) {
        try {
            showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<GalleryResp> call = restInterface.getAllGallery(Constant.apiKey, Constant.childSchoolId,  ""+index);
            call.enqueue(new Callback<GalleryResp>() {
                @Override
                public void onResponse(Call<GalleryResp> call, Response<GalleryResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                            galleryInfos = (ArrayList<GalleryInfo>) response.body().getGalleryInfo();

                                if (galleryInfos.size() > 0) {
                                   //adapterUnReadDiscussion = new AdapterGallery(context, galleryInfos);
                                  //  invoiceRV.setAdapter(adapterUnReadDiscussion);
                                    galleryListDat.addAll(galleryInfos);
                                    adapterGallery.notifyDataChanged();
                                } else {
                                    invoiceRV.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(ActivityGallery.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<GalleryResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


    //TODO: Web call for load more Notification Details...
    private void loadMoreNotification(int index) {
        try {
            //add loading progress view
            galleryListDat.add(new GalleryInfo("load"));
            adapterGallery.notifyItemInserted(galleryListDat.size()-1);
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<GalleryResp> call = restInterface.getAllGallery(Constant.apiKey, Constant.childSchoolId, ""+index);
            call.enqueue(new Callback<GalleryResp>() {
                @Override
                public void onResponse(Call<GalleryResp> call, Response<GalleryResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                galleryInfos = (ArrayList<GalleryInfo>) response.body().getGalleryInfo();
                                //remove loading view
                                galleryListDat.remove(galleryListDat.size()-1);

                                List<GalleryInfo> result = galleryInfos;
                                if(result.size()>0){
                                    //add loaded data
                                    galleryListDat.addAll(result);
                                }else{//result size 0 means there is no more data available at server
                                    adapterGallery.setMoreDataAvailable(false);
                                    //telling adapter to stop calling load more as no more server data available
                                    Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                                }
                                adapterGallery.notifyDataChanged();

                            } else {
                                galleryListDat.remove(galleryListDat.size()-1);
                                adapterGallery.setMoreDataAvailable(false);
                                adapterGallery.notifyDataChanged();
                                //telling adapter to stop calling load more as no more server data available
                                Toast.makeText(context,"No More Data Available",Toast.LENGTH_LONG).show();
                             //   Toast.makeText(ActivityNotification.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<GalleryResp> call, Throwable t) {
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
                intent = new Intent(ActivityGallery.this, ActivityLibBooks.class);
                startActivity(intent);
                break;
            case R.id.actionNotification:
                intent = new Intent(ActivityGallery.this, ActivityNotification.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
