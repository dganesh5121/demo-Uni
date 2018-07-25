package com.techindiana.school.parent.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techindiana.school.parent.Module.BaseResp;
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
public class Feedback extends BaseFragment {

    TextView tvSend;
    private View rootView;
    EditText etFeedback;
    private Retrofit retrofit;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_feedback, container, false);
        retrofit = RetrofitUtils.getRetrofit();
        tvSend=(TextView)rootView.findViewById(R.id.feedbackTvSend) ;
        etFeedback=(EditText)rootView.findViewById(R.id.feedbackEtEmail);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFeedback.getText().toString().trim().length() == 0) {
                    etFeedback.setError(Html
                            .fromHtml("<font color='#FF0000'>Please enter your valid feedback</font>"));
                } else {
                    sendFeedBack();
                }
            }
        });

        return rootView;
    }

    //TODO: Web call for Notification Details...
    private void sendFeedBack() {
        try {
            showProgress(getActivity());
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);
            //Calling method to get whether report

            Call<BaseResp> call = restInterface.sendFeedBack(Constant.apiKey, Constant.childSchoolId,Constant.userID,etFeedback.getText().toString().trim());
            call.enqueue(new Callback<BaseResp>() {
                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {
                                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<BaseResp> call, Throwable t) {
                    hideProgress();
                }
            });
        } catch (Exception e) {
            hideProgress();
        }
    }


}
