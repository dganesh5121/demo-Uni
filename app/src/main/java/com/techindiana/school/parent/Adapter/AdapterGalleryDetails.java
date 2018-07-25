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
import android.widget.TableRow;

import com.bumptech.glide.Glide;
import com.techindiana.school.parent.ActivityGalleryDImageView;
import com.techindiana.school.parent.Module.GalleryDetailsImage;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.util.ArrayList;

/**
 * Created by TechIndiana on 26-07-2017.
 */

public class AdapterGalleryDetails extends RecyclerView.Adapter<AdapterGalleryDetails.ItemRowHolder> {
    private Context mContext;
    private ArrayList<GalleryDetailsImage> galleryDetailsImages;



    public AdapterGalleryDetails(Context mContext, ArrayList<GalleryDetailsImage> galleryDetailsImages) {
        this.mContext = mContext;
        this.galleryDetailsImages = galleryDetailsImages;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_details, null);
        //Methods.setFont(mContext, viewGroup);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        try {

            if (galleryDetailsImages.get(i).getUrl() != null) {
                if (galleryDetailsImages.get(i).getUrl().length() > 0) {
                    Glide.with(mContext).
                            load(Constant.webImgPath + galleryDetailsImages.get(i).getUrl()).
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

            if(galleryDetailsImages.get(i).getType().toString().toLowerCase().trim().equals("video"))
                itemRowHolder.trIsVideo.setVisibility(View.VISIBLE);
            else
                itemRowHolder.trIsVideo.setVisibility(View.GONE);
            itemRowHolder.llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivityGalleryDImageView.class);
                   intent.putExtra("galleryD",  galleryDetailsImages);
                   intent.putExtra("pos",  i);
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public int getItemCount() {
        return (null != galleryDetailsImages ? galleryDetailsImages.size() : 0);
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {
    TableRow trIsVideo;
        private ImageView img;
        LinearLayout llyParent, llyBg;

        ItemRowHolder(View view) {
            super(view);
            trIsVideo=(TableRow)view.findViewById(R.id.rwGalleryTr);
            img = (ImageView) view.findViewById(R.id.rwGalleryImg);
            llyBg = (LinearLayout) view.findViewById(R.id.rwGalleryLlyBg);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((Constant.screenWidth / 2) - 15, (Constant.screenWidth / 2) - 15);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "sansR.ttf");
        }
    }
}
