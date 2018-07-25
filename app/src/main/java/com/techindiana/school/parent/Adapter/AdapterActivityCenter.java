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
import android.widget.TableRow;
import android.widget.TextView;

import com.techindiana.school.parent.ActivityCenterDetails;
import com.techindiana.school.parent.Module.ActivityCDayWise;
import com.techindiana.school.parent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterActivityCenter extends RecyclerView.Adapter<AdapterActivityCenter.ItemRowHolder> {
    private Context mContext;
    private ArrayList<ActivityCDayWise> activityCDayWises;


    public AdapterActivityCenter(Context mContext, ArrayList<ActivityCDayWise> activityCDayWises) {
        this.mContext = mContext;
        this.activityCDayWises = activityCDayWises;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_activity_center, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            if(i==0){
                itemRowHolder.trHeader.setVisibility(View.VISIBLE);
            }else{
                itemRowHolder.trHeader.setVisibility(View.GONE);
            }
            itemRowHolder.tvName.setText(activityCDayWises.get(i).getTitle());



            String inputSPattern = "HH:mm:ss";
            String outputSPattern = "hh:mm a";
            SimpleDateFormat inputSFormat = new SimpleDateFormat(inputSPattern);
            SimpleDateFormat outputSFormat = new SimpleDateFormat(outputSPattern);

            Date sTime = null;
            Date eTime = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sTime = inputSFormat.parse(activityCDayWises.get(i).getEndTime());
                eTime = inputSFormat.parse(activityCDayWises.get(i).getEndTime());
             //   tvTime.setText(outputTFormat.format(sT));
                itemRowHolder.tvSTime.setText(outputSFormat.format(sTime));
                itemRowHolder.tvETime.setText(outputSFormat.format(eTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

           // itemRowHolder.tvSTime.setText(activityCDayWises.get(i).getEndTime());
          //  itemRowHolder.tvETime.setText(activityCDayWises.get(i).getEndTime());
            itemRowHolder.tvDisc.setText(activityCDayWises.get(i).getDescription());
            if(activityCDayWises.get(i).getIsEnrolled().equals("1")) {
                itemRowHolder.trIndicator.setBackgroundColor(Color.parseColor("#45C858"));
                itemRowHolder.imgEnroll.setBackgroundResource(R.mipmap.enrolled_ic);
            }else{
                itemRowHolder.trIndicator.setBackgroundColor(Color.parseColor("#F56161"));
                itemRowHolder.imgEnroll.setBackgroundResource(R.mipmap.non_enrolled_ic);
            }

            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityCenterDetails.class);
                    intent.putExtra("activity",  activityCDayWises.get(i));
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != activityCDayWises ? activityCDayWises.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvSTime,tvETime,tvDisc;
        private ImageView imgEnroll;
        LinearLayout llyParent;
TableRow trIndicator,trHeader;
        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.aCTvTitle);
            tvSTime = (TextView) view.findViewById(R.id.aCTvSTime);
            tvETime = (TextView) view.findViewById(R.id.aCTvETime);
            tvDisc = (TextView) view.findViewById(R.id.aCTvDisc);
            imgEnroll = (ImageView) view.findViewById(R.id.aCImgEnroll);
            trIndicator=(TableRow)view.findViewById(R.id.aCTrIndicator);
            trHeader=(TableRow)view.findViewById(R.id.aCTrHeader);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvSTime.setTypeface(type);
            tvETime.setTypeface(type);
            tvDisc.setTypeface(type);
        }
    }
}
