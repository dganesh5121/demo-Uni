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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techindiana.school.parent.ActivityExtraClassDetails;
import com.techindiana.school.parent.Module.ExtraClassDayWiseInfo;
import com.techindiana.school.parent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterExtraClass extends RecyclerView.Adapter<AdapterExtraClass.ItemRowHolder> {
    private Context mContext;
    private ArrayList<ExtraClassDayWiseInfo> extraClassDayWiseInfos;


    public AdapterExtraClass(Context mContext, ArrayList<ExtraClassDayWiseInfo> extraClassDayWiseInfos) {
        this.mContext = mContext;
        this.extraClassDayWiseInfos = extraClassDayWiseInfos;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_extra_class, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            itemRowHolder.tvName.setText(extraClassDayWiseInfos.get(i).getTitle());



            String inputSPattern = "HH:mm:ss";
            String outputSPattern = "hh:mm a";
            SimpleDateFormat inputSFormat = new SimpleDateFormat(inputSPattern);
            SimpleDateFormat outputSFormat = new SimpleDateFormat(outputSPattern);

            Date sTime = null;
            Date eTime = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sTime = inputSFormat.parse(extraClassDayWiseInfos.get(i).getEndTime());
                eTime = inputSFormat.parse(extraClassDayWiseInfos.get(i).getEndTime());
                //   tvTime.setText(outputTFormat.format(sT));
                itemRowHolder.tvSTime.setText(outputSFormat.format(sTime));
                itemRowHolder.tvETime.setText(outputSFormat.format(eTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }


           // itemRowHolder.tvSTime.setText(extraClassDayWiseInfos.get(i).getEndTime());
           // itemRowHolder.tvETime.setText(extraClassDayWiseInfos.get(i).getEndTime());
            itemRowHolder.tvDisc.setText(extraClassDayWiseInfos.get(i).getDescription());
            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityExtraClassDetails.class);
                    intent.putExtra("extraClass",  extraClassDayWiseInfos.get(i));
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != extraClassDayWiseInfos ? extraClassDayWiseInfos.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvSTime,tvETime,tvDisc;

        LinearLayout llyParent;
        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.ecTvTitle);
            tvSTime = (TextView) view.findViewById(R.id.ecTvSTime);
            tvETime = (TextView) view.findViewById(R.id.ecTvETime);
            tvDisc = (TextView) view.findViewById(R.id.ecTvDisc);
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
