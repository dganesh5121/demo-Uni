package com.techindiana.school.parent.Adapter;
/*
Created By: DGP 22/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.ActivityNotificationDetails;
import com.techindiana.school.parent.Module.NotificationInfo;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by TechIndiana on 26-07-2017.
 */

/**
 * Created by sab99r
 */
public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<NotificationInfo> notificationInfo;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public AdapterNotification(Context context, List<NotificationInfo> notificationInfo) {
        this.context = context;
        this.notificationInfo = notificationInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new PlaceHolder(inflater.inflate(R.layout.item_notification,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.rw_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((PlaceHolder)holder).bindData(notificationInfo.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

            if (notificationInfo.get(position).type==null) {
                return TYPE_MOVIE;
            }else{
                return TYPE_LOAD;
            }

    }

    @Override
    public int getItemCount() {
        return notificationInfo.size();
    }

    /* VIEW HOLDERS */

    static class PlaceHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvRating;
        TextView tvDate;
        TextView tvTime;
        ImageView img;
        LinearLayout llyParent;
        public PlaceHolder(View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.rwNotiTvName);
            tvRating=(TextView)itemView.findViewById(R.id.rwNotiTvDes);
            tvDate=(TextView)itemView.findViewById(R.id.rwNotiTvDate);
            tvTime=(TextView)itemView.findViewById(R.id.rwNotiTvTime);
            img=(ImageView)itemView.findViewById(R.id.rwNotiImgName);
            llyParent=(LinearLayout)itemView.findViewById(R.id.llRowBagpack);
        }

        void bindData(final NotificationInfo notificationInfo){
            tvTitle.setText(notificationInfo.getTitle());
            tvRating.setText(notificationInfo.getDescription());
            String[] separated = notificationInfo.getCreatedOn().split(" ");


            String inputDPattern = "yyyy-MM-dd";
            String outputDPattern = "d MMM yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputDPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputDPattern);

            Date sdate = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sdate = inputFormat.parse(separated[0].toString());
                tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }



            String inputTPattern = "HH:mm:ss";
            String outputTPattern = "hh:mm a";
            SimpleDateFormat inputTFormat = new SimpleDateFormat(inputTPattern);
            SimpleDateFormat outputTFormat = new SimpleDateFormat(outputTPattern);

            Date sT = null;

            // String[] createdDate = notificationInfos.get(i).getCreatedOn().split(" ");
            try {
                sT = inputTFormat.parse(separated[1].toString());
                tvTime.setText(outputTFormat.format(sT));

            } catch (ParseException e) {
                e.printStackTrace();
            }




          //  tvDate.setText(separated[0].toString());
          //  tvTime.setText(separated[1].toString());
            llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityNotificationDetails.class);
                    intent.putExtra("notification",  notificationInfo);
                    context.startActivity(intent);
                }
            });

            if (Constant.childImg.length() > 0) {
                Glide.with(context).
                        load(Constant.webImgPath + Constant.childImg).

                        into(img);
            } else {
                Glide.with(context).
                        load(R.mipmap.splash_screen_logo).
                        placeholder(R.mipmap.splash_screen_logo).
                        error(R.mipmap.splash_screen_logo).
                        into(img);
            }

          /*  if (Constant.childImg != null) {
                if (notificationInfo.getImage().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath +noticeBoardInfo.getImage()).
                            //  placeholder(R.mipmap.splash_screen_logo).
                                    centerCrop().
                            //error(R.mipmap.splash_screen_logo).
                                    into(img);
                } else {
                    Glide.with(context).
                            load(R.mipmap.splash_screen_logo).
                            placeholder(R.mipmap.splash_screen_logo).
                            error(R.mipmap.splash_screen_logo).
                            into(img);
                }
            } else {
                Glide.with(context).
                        load(R.mipmap.splash_screen_logo).
                        placeholder(R.mipmap.splash_screen_logo).
                        error(R.mipmap.splash_screen_logo).
                        into(img);
            }*/
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
