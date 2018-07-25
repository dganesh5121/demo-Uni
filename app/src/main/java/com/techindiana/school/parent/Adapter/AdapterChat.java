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
import com.techindiana.school.parent.ActivityChatDetails;
import com.techindiana.school.parent.Module.TeachersListInfo;
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

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ItemRowHolder> {
    private Context mContext;
    private ArrayList<TeachersListInfo> teachersListInfos;
    private Retrofit retrofit;

    public AdapterChat(Context mContext, ArrayList<TeachersListInfo> teachersListInfos) {
        this.mContext = mContext;
        this.teachersListInfos = teachersListInfos;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {
            itemRowHolder.tvName.setText(teachersListInfos.get(i).getFname()+" "+teachersListInfos.get(i).getLname());
           // itemRowHolder.tvDes.setText(teachersListInfos.get(i).getGDate());

            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "d MMM yyyy hh:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(teachersListInfos.get(i).getLastMsgTime());
                itemRowHolder.tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }


       //     itemRowHolder.tvDate.setText(teachersListInfos.get(i).getLastMsgTime());
            itemRowHolder.tvSub.setText(teachersListInfos.get(i).getSubName());
            if(teachersListInfos.get(i).getUnreadCount().length()>0 && Integer.parseInt(teachersListInfos.get(i).getUnreadCount().toString())>0)
            itemRowHolder.tvUnreadCount.setText(teachersListInfos.get(i).getUnreadCount());
            else
                itemRowHolder.tvUnreadCount.setVisibility(View.GONE);
            if (teachersListInfos.get(i).getProfileImg() != null) {
                if (teachersListInfos.get(i).getProfileImg().length() > 0) {
                    Glide.with(mContext).
                            load(Constant.webImgPath + teachersListInfos.get(i).getProfileImg()).
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

            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.teacherId=teachersListInfos.get(i).getTeacherId();
                    Constant.teacherImg=teachersListInfos.get(i).getProfileImg();
                    Constant.teacherName=teachersListInfos.get(i).getFname()+" "+teachersListInfos.get(i).getLname();
                    Intent intent = new Intent(mContext, ActivityChatDetails.class);
                   mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != teachersListInfos ? teachersListInfos.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDes,tvDate,tvSub,tvUnreadCount;
        private ImageView img;
        LinearLayout llyParent, llyBg;

        ItemRowHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.rwChatTvName);
            tvDate = (TextView) view.findViewById(R.id.rwChatTvDate);
           // tvDes = (TextView) view.findViewById(R.id.rwChatTvDes);
            tvSub = (TextView) view.findViewById(R.id.rwChatTvSubject);
            tvUnreadCount = (TextView) view.findViewById(R.id.rwtvUnreadCount);

            img = (ImageView) view.findViewById(R.id.rwChatImg);

            llyParent = (LinearLayout) view.findViewById(R.id.llRowBagpack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvDate.setTypeface(type);
        //    tvDes.setTypeface(type);
            tvSub.setTypeface(type);
        }
    }
}
