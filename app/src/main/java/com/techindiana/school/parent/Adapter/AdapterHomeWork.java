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

import com.techindiana.school.parent.ActivityHomeWorkDetails;
import com.techindiana.school.parent.Module.HomeworkDayInfo;
import com.techindiana.school.parent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterHomeWork extends RecyclerView.Adapter<AdapterHomeWork.ItemRowHolder> {
    private Context mContext;
    private ArrayList<HomeworkDayInfo> homeworkDayInfos;


    public AdapterHomeWork(Context mContext, ArrayList<HomeworkDayInfo> homeworkDayInfos) {
        this.mContext = mContext;
        this.homeworkDayInfos = homeworkDayInfos;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_work, null);
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
            itemRowHolder.tvName.setText(homeworkDayInfos.get(i).getTitle());
            String[] createdDate = homeworkDayInfos.get(i).getCreatedOn().split(" ");

            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "d MMM yy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;


            try {
                sdate = inputFormat.parse(createdDate[0]);
                itemRowHolder.tvSDate.setText(outputFormat.format(sdate));
             edate = inputFormat.parse(homeworkDayInfos.get(i).getCompletionDate());
                itemRowHolder.tvEDate.setText(outputFormat.format(edate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

           // itemRowHolder.tvSDate.setText(homeworkDayInfos.get(i).getEndTime());
           // itemRowHolder.tvEDate.setText(homeworkDayInfos.get(i).getCompletionDate());


            itemRowHolder.tvDisc.setText(homeworkDayInfos.get(i).getDescription());
            itemRowHolder.tvSubj.setText(homeworkDayInfos.get(i).getSubName());
            if(homeworkDayInfos.get(i).getHwStatus().equals("1")) {
                itemRowHolder.trIndicator.setBackgroundColor(Color.parseColor("#45C858"));
                itemRowHolder.imgStatus.setBackgroundResource(R.mipmap.present_ic);
            }else if(homeworkDayInfos.get(i).getHwStatus().equals("0")) {
                itemRowHolder.trIndicator.setBackgroundColor(Color.parseColor("#F56161"));
                itemRowHolder.imgStatus.setBackgroundResource(R.mipmap.absent_ic);
            }else{
                itemRowHolder.trIndicator.setBackgroundColor(Color.parseColor("#FE8C05"));
                itemRowHolder.imgStatus.setBackgroundResource(R.mipmap.halfday_ic);
            }
            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityHomeWorkDetails.class);
                    intent.putExtra("homeWork",  homeworkDayInfos.get(i));
                    mContext.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != homeworkDayInfos ? homeworkDayInfos.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvSDate,tvEDate,tvDisc,tvSubj;
        private ImageView imgStatus;
        LinearLayout llyParent;
TableRow trIndicator,trHeader;
        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.hwTvTitle);
            tvSDate = (TextView) view.findViewById(R.id.hwTvSDate);
            tvEDate = (TextView) view.findViewById(R.id.hwTvEDate);
            tvDisc = (TextView) view.findViewById(R.id.hwTvDisc);
            tvSubj = (TextView) view.findViewById(R.id.hwTvSubject);
            imgStatus = (ImageView) view.findViewById(R.id.hwImgStatus);
            trIndicator=(TableRow)view.findViewById(R.id.hwTrIndicator);
            trHeader=(TableRow)view.findViewById(R.id.hwTrHeader);

            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvSDate.setTypeface(type);
            tvEDate.setTypeface(type);
            tvDisc.setTypeface(type);
            tvSubj.setTypeface(type);
        }
    }
}
