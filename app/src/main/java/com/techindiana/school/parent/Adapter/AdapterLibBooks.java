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
import com.techindiana.school.parent.ActivityLibBookDisplay;
import com.techindiana.school.parent.ActivityLibBookVideoPlay;
import com.techindiana.school.parent.Module.LibraryInfo;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.util.List;

/**
 * Created by TechIndiana on 26-07-2017.
 */

/**
 * Created by sab99r
 */
public class AdapterLibBooks extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<LibraryInfo> libraryInfos;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public AdapterLibBooks(Context context, List<LibraryInfo> libraryInfos) {
        this.context = context;
        this.libraryInfos = libraryInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new PlaceHolder(inflater.inflate(R.layout.item_book, parent, false));
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
            ((PlaceHolder) holder).bindData(libraryInfos.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {

        if (libraryInfos.get(position).type == null) {
            return TYPE_MOVIE;
        } else {
            return TYPE_LOAD;
        }

    }

    @Override
    public int getItemCount() {
        return libraryInfos.size();
    }

    /* VIEW HOLDERS */

    class PlaceHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvSubject;
        private ImageView img;
        LinearLayout llyParent,llyIsVideo;

        public PlaceHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.rwBookTvName);
            tvSubject = (TextView) view.findViewById(R.id.rwBookTvSubject);
            img = (ImageView) view.findViewById(R.id.rwBookImg);
            llyIsVideo = (LinearLayout) view.findViewById(R.id.rwLlyIsVideo);
            llyParent = (LinearLayout) view.findViewById(R.id.llRowBagpack);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((Constant.screenWidth / 2) - 15, (Constant.screenWidth / 2) - 15);
            llyParent.setLayoutParams(parms);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "sansR.ttf");
            tvName.setTypeface(type);
            tvSubject.setTypeface(type);
        }

        void bindData(final LibraryInfo libraryInfos) {
            String fileName = libraryInfos.getFileName().substring(libraryInfos.getFileName().lastIndexOf('/') + 1);
            tvName.setText(fileName.substring(0, fileName.lastIndexOf('.')));
            tvSubject.setText(libraryInfos.getSubject());
            if (libraryInfos.getThumbnail() != null) {
                if (libraryInfos.getThumbnail().toString().length() > 0) {
                    Glide.with(context).
                            load(Constant.webImgPath + libraryInfos.getThumbnail()).
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
            if(libraryInfos.getFileType().equals("file"))
            llyIsVideo.setVisibility(View.GONE);
            else
                llyIsVideo.setVisibility(View.VISIBLE);

            llyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileName = libraryInfos.getFileName().substring(libraryInfos.getFileName().lastIndexOf('/') + 1);
                    Constant.fileName=fileName.substring(0, fileName.lastIndexOf('.'));
                    Constant.filePath = libraryInfos.getFileName();
                    if(libraryInfos.getFileType().equals("file")) {
                       Intent intent = new Intent(context, ActivityLibBookDisplay.class);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, ActivityLibBookVideoPlay.class);
                        context.startActivity(intent);
                    }
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
