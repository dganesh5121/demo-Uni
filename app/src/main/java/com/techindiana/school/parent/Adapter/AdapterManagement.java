package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Module.ManagementTeamInfo;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.util.ArrayList;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterManagement extends RecyclerView.Adapter<AdapterManagement.ItemRowHolder> {
    private Context mContext;
    private ArrayList<ManagementTeamInfo> managementTeamInfo;


    public AdapterManagement(Context mContext, ArrayList<ManagementTeamInfo> managementTeamInfo) {
        this.mContext = mContext;
        this.managementTeamInfo = managementTeamInfo;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_managment, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            itemRowHolder.tvName.setText(managementTeamInfo.get(i).getName());
            itemRowHolder.tvDesignation.setText(managementTeamInfo.get(i).getDesignation());
            itemRowHolder.tvDesc.setText(managementTeamInfo.get(i).getDescription());
            if (managementTeamInfo.get(i).getImage() != null) {
                if (managementTeamInfo.get(i).getImage().length() > 0) {
                    Glide.with(mContext).
                            load(Constant.webImgPath + managementTeamInfo.get(i).getImage()).
                            placeholder(R.mipmap.splash_screen_logo).
                            centerCrop().
                            error(R.mipmap.splash_screen_logo).
                            into(itemRowHolder.img);
                } else {
                    Glide.with(mContext).
                            load(R.mipmap.splash_screen_logo).
                            placeholder(R.mipmap.splash_screen_logo).
                            error(R.mipmap.splash_screen_logo).
                            into(itemRowHolder.img);
                }
            } else {
                Glide.with(mContext).
                        load(R.mipmap.splash_screen_logo).
                        placeholder(R.mipmap.splash_screen_logo).
                        error(R.mipmap.splash_screen_logo).
                        into(itemRowHolder.img);
            }


        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != managementTeamInfo ? managementTeamInfo.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesignation, tvDesc;
        private ImageView img;
        LinearLayout llyParent;

        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.mngTvName);
            tvDesignation = (TextView) view.findViewById(R.id.mngTvDesignation);
            tvDesc = (TextView) view.findViewById(R.id.mngTvDes);
            img = (ImageView) view.findViewById(R.id.rwGalleryImg);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvDesignation.setTypeface(type);
            tvDesc.setTypeface(type);
        }
    }
}
