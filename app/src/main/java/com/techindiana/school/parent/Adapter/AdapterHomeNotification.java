package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.ActivityNotification;
import com.techindiana.school.parent.Module.HomePageNotification;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Retrofit;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterHomeNotification extends RecyclerView.Adapter<AdapterHomeNotification.ItemRowHolder> {
    private Context mContext;
    private ArrayList<HomePageNotification> notificationInfos;
    private Retrofit retrofit;

    public AdapterHomeNotification(Context mContext, ArrayList<HomePageNotification> notificationInfos) {
        this.mContext = mContext;
        this.notificationInfos = notificationInfos;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_notification, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            itemRowHolder.tvName.setText(notificationInfos.get(i).getType());
            itemRowHolder.tvDes.setText(notificationInfos.get(i).getNotification());

            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "d MMM yyyy hh:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;

           // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(notificationInfos.get(i).getCreatedOn());
                itemRowHolder.tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //itemRowHolder.tvDate.setText(notificationInfos.get(i).getCreatedOn());
            String imgUser = "";
            for (int j = 0; j < Constant.childList.size(); j++) {
                if (Constant.childList.get(j).get("childID").equals(notificationInfos.get(i).getStudent_id())) {
                    imgUser = Constant.childList.get(j).get("childImg");
                }
            }

            if (imgUser.length() > 0) {
                Glide.with(mContext).
                        load(Constant.webImgPath + imgUser).

                        into(itemRowHolder.img);
            } else {
                Glide.with(mContext).
                        load(R.mipmap.splash_screen_logo).
                        placeholder(R.mipmap.splash_screen_logo).
                        error(R.mipmap.splash_screen_logo).
                        into(itemRowHolder.img);
            }

            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityNotification.class);

                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != notificationInfos ? notificationInfos.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDate, tvDes;
        ImageView img;
        LinearLayout llyParent;

        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.rwNotiTvName);
            tvDate = (TextView) view.findViewById(R.id.rwNotiTvDate);
            tvDes = (TextView) view.findViewById(R.id.rwNotiTvDes);
            img = (ImageView) view.findViewById(R.id.rwNotiImg);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBagpack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvDate.setTypeface(type);
            tvDes.setTypeface(type);
        }
    }
}
