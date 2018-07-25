package com.techindiana.school.parent.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techindiana.school.parent.Adapter.AdapterManagement;
import com.techindiana.school.parent.Module.ManagementTeamInfo;
import com.techindiana.school.parent.Module.ManagementTeamResp;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Utils.VerticalLineDecorator;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class Management extends BaseFragment {

    private View rootView;
    private Retrofit retrofit;
    RecyclerView.LayoutManager managerGallery;
    RecyclerView.Adapter adapterGallery;
    RecyclerView rvManag;
    LinearLayout homeLyNoData;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_management, container, false);
        retrofit = RetrofitUtils.getRetrofit();
        homeLyNoData = (LinearLayout) rootView.findViewById(R.id.homeLlyNoData);
        rvManag = (RecyclerView) rootView.findViewById(R.id.homeRcRV);

        rvManag.setHasFixedSize(true);
        rvManag.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvManag.addItemDecoration(new VerticalLineDecorator(2));


        getInfo();
        return rootView;
    }

    //TODO: Web call for Notification Details...
    private void getInfo() {
        try {
            showProgress(getActivity());
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<ManagementTeamResp> call = restInterface.getManagementTeam(Constant.apiKey, Constant.childSchoolId );
            call.enqueue(new Callback<ManagementTeamResp>() {
                @Override
                public void onResponse(Call<ManagementTeamResp> call, Response<ManagementTeamResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                ArrayList<ManagementTeamInfo> managementTeamInfos = (ArrayList<ManagementTeamInfo>) response.body().getManagementTeamInfo();

                                if (managementTeamInfos.size() > 0) {
                                    adapterGallery = new AdapterManagement(getActivity(), managementTeamInfos);
                                    rvManag.setAdapter(adapterGallery);
                                } else {
                                    rvManag.setVisibility(View.GONE);
                                    homeLyNoData.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }catch (Exception e){
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<ManagementTeamResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


}
