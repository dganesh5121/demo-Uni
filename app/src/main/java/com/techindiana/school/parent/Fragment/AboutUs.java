package com.techindiana.school.parent.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.techindiana.school.parent.Module.AboutContentResp;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestCallInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Belal on 2/3/2016.
 */

//Our class extending fragment
public class AboutUs extends BaseFragment {
TextView tvAboutInfo;
    private View rootView;
    private Retrofit retrofit;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_about_us, container, false);
        retrofit = RetrofitUtils.getRetrofit();
        tvAboutInfo=(TextView)rootView.findViewById(R.id.aboutInfo) ;
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

            Call<AboutContentResp> call = restInterface.getAboutContent(Constant.apiKey, Constant.childSchoolId);
            call.enqueue(new Callback<AboutContentResp>() {
                @Override
                public void onResponse(Call<AboutContentResp> call, Response<AboutContentResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                tvAboutInfo.setText(Html.fromHtml(response.body().getAboutContentInfo().getContent()));
                            } else {
                                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    } catch (Exception e) {
                        e.getMessage();
                        hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<AboutContentResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


}
