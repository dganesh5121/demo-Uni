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
import com.techindiana.school.parent.ActivityGalleryDetails;
import com.techindiana.school.parent.Module.GalleryInfo;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by TechIndiana on 26-07-2017.
 */

/**
 * Created by sab99r
 */
public class AdapterGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    String[] arraySpinner = new String[]{
            "#FF8C05", "#7986CC", "#F56161", "#F06292", "#B968C7",
            "#9574CC", "#64B4F6", "#4DB7AD", "#81C884", "#FEA301"
    };
    static Context context;
    List<GalleryInfo> galleryInfos;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public AdapterGallery(Context context, List<GalleryInfo> galleryInfos) {
        this.context = context;
        this.galleryInfos = galleryInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new PlaceHolder(inflater.inflate(R.layout.item_home_gallery, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.rw_load, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            ((PlaceHolder) holder).bindData(galleryInfos.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

        if (galleryInfos.get(position).type == null) {
            return TYPE_MOVIE;
        } else {
            return TYPE_LOAD;
        }

    }

    @Override
    public int getItemCount() {
        return galleryInfos.size();
    }

    /* VIEW HOLDERS */

    class PlaceHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDate;
        private ImageView img;
        LinearLayout llyParent, llyBg;

        public PlaceHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.rwGalleryTvName);
            tvDate = (TextView) view.findViewById(R.id.rwGalleryTvDate);
            img = (ImageView) view.findViewById(R.id.rwGalleryImg);
            llyBg = (LinearLayout) view.findViewById(R.id.rwGalleryLlyBg);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((Constant.screenWidth / 2) - 15, (Constant.screenWidth / 2) - 15);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvDate.setTypeface(type);
        }

        void bindData(final GalleryInfo galleryInfos) {

            tvName.setText(galleryInfos.getTitle());

            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "d MMM yy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date sdate = null;
            Date edate = null;


            try {
                sdate = inputFormat.parse(galleryInfos.getGalleryDate());
                tvDate.setText(outputFormat.format(sdate));

            } catch (ParseException e) {
                e.printStackTrace();
            }

          //  tvDate.setText(galleryInfos.getGalleryDate());
            if (galleryInfos.getImage() != null) {
                if (galleryInfos.getImage().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath + galleryInfos.getImage()).
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
            }
            String randomStr = arraySpinner[new Random().nextInt(arraySpinner.length)];
            llyBg.setBackgroundColor(Color.parseColor(randomStr));
            llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityGalleryDetails.class);
                      intent.putExtra("gallery",  galleryInfos);
                    context.startActivity(intent);
                }
            });
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder {
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
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
