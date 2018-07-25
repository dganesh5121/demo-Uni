package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.ActivityGallery;
import com.techindiana.school.parent.Module.HomePageGallery;
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

public class AdapterHomeGallery extends RecyclerView.Adapter<AdapterHomeGallery.ItemRowHolder> {
    private Context mContext;
    private ArrayList<HomePageGallery> homePageGalleries;
    private Retrofit retrofit;
    String[] arraySpinner = new String[]{
            "#7986CC", "#F56161", "#F06292", "#BE9117", "#C66BFF"
    };

    public AdapterHomeGallery(Context mContext, ArrayList<HomePageGallery> homePageGalleries) {
        this.mContext = mContext;
        this.homePageGalleries = homePageGalleries;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_gallery, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            itemRowHolder.tvName.setText(homePageGalleries.get(i).getTitle());
            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;


            try {
                sdate = inputFormat.parse(homePageGalleries.get(i).getGDate());
                itemRowHolder.tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }

           // itemRowHolder.tvDate.setText(homePageGalleries.get(i).getGDate());
            if (homePageGalleries.get(i).getUrl() != null) {
                if (homePageGalleries.get(i).getUrl().length() > 0) {
                    Glide.with(mContext).
                            load(Constant.webImgPath + homePageGalleries.get(i).getUrl()).
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
            itemRowHolder.llyBg.setBackgroundColor(Color.parseColor(arraySpinner[i]));
            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityGallery.class);
                  //  intent.putExtra("gallery",  homePageGalleries.get(i));
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != homePageGalleries ? homePageGalleries.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDate;
        private ImageView img;
        LinearLayout llyParent, llyBg;

        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.rwGalleryTvName);
            tvDate = (TextView) view.findViewById(R.id.rwGalleryTvDate);
            img = (ImageView) view.findViewById(R.id.rwGalleryImg);
            llyBg = (LinearLayout) view.findViewById(R.id.rwGalleryLlyBg);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((Constant.screenWidth / 2) - 70, (Constant.screenWidth / 2) - 70);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvDate.setTypeface(type);
        }
    }
}
