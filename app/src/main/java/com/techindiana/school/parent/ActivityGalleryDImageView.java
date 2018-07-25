package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boredream.bdvideoplayer.BDVideoView;
import com.boredream.bdvideoplayer.listener.SimpleOnVideoControlListener;
import com.boredream.bdvideoplayer.utils.DisplayUtils;
import com.bumptech.glide.Glide;
import com.techindiana.school.parent.Module.GalleryDetailsImage;
import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Utils.VideoDetailInfo;
import com.techindiana.school.parent.Vars.Constant;
import com.techindiana.school.parent.retrofit_utils.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Retrofit;


public class ActivityGalleryDImageView extends BaseActivity {

    public ActionBar actionBar;
    private static ViewPager mPager;
    private static int currentPage = 0;

    private Context mContext;
    private Retrofit retrofit;
    private ArrayList<GalleryDetailsImage> galleryDetailsImages;
    BDVideoView videoView;
    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_d_image_view);
        retrofit = RetrofitUtils.getRetrofit();
        mContext = ActivityGalleryDImageView.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryDetailsImages = (ArrayList<GalleryDetailsImage>) getIntent().getSerializableExtra("galleryD");
        currentPage=getIntent().getExtras().getInt("pos");

        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle("Gallery");


            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.hide();
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityGalleryDImageView.this, viewGroup);
        try {
            Declaration();

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void Declaration() {
        try {

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new AdapterGalleryDImageVideo(ActivityGalleryDImageView.this, galleryDetailsImages));
            mPager.setCurrentItem(currentPage);

            fabLayout1= (LinearLayout) findViewById(R.id.fabLayout1);
            fabLayout2= (LinearLayout) findViewById(R.id.fabLayout2);
           // fabLayout3= (LinearLayout) findViewById(R.id.fabLayout3);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            fab2= (FloatingActionButton) findViewById(R.id.fab2);
          //  fab3 = (FloatingActionButton) findViewById(R.id.fab3);
            fabBGLayout=findViewById(R.id.fabBGLayout);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                    }
                }
            });

            fabBGLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeFABMenu();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }




    public class AdapterGalleryDImageVideo extends PagerAdapter {


        private ArrayList<GalleryDetailsImage> galleryDetailsImages;
        private LayoutInflater inflater;
        private Context context;


        public AdapterGalleryDImageVideo(Context context, ArrayList<GalleryDetailsImage> galleryDetailsImages) {
            this.context = context;
            this.galleryDetailsImages = galleryDetailsImages;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return galleryDetailsImages.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_gallery_d_image_view, view, false);

            assert imageLayout != null;
            try {
                final ImageView imageView = (ImageView) imageLayout
                        .findViewById(R.id.gvDImg);
                videoView = (BDVideoView) imageLayout.findViewById(R.id.gvDVideo);
                if (galleryDetailsImages.get(position).getType().toString().toLowerCase().trim().equals("video")) {
                    videoView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    VideoDetailInfo info = new VideoDetailInfo("", Constant.webImgPath + galleryDetailsImages.get(position).getUrl());
                    videoView.setOnVideoControlListener(new SimpleOnVideoControlListener() {

                        @Override
                        public void onRetry(int errorStatus) {
                            // TODO: 2017/6/20 调用业务接口重新获取数据
                            // get info and call method "videoView.startPlayVideo(info);"
                        }

                        @Override
                        public void onBack() {
                            onBackPressed();
                        }

                        @Override
                        public void onFullScreen() {
                            DisplayUtils.toggleScreenOrientation(ActivityGalleryDImageView.this);
                        }
                    });
                    videoView.startPlayVideo(info);
                } else {
                    videoView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    if (galleryDetailsImages.get(position).getUrl() != null) {
                        if (galleryDetailsImages.get(position).getUrl().toString().length() > 0) {
                            Glide.with(context).
                                    load(Constant.webImgPath + galleryDetailsImages.get(position).getUrl()).
                                            into(imageView);
                        } else {
                            Glide.with(context).
                                    load(R.mipmap.splash_screen_logo).
                                    placeholder(R.mipmap.splash_screen_logo).
                                    error(R.mipmap.splash_screen_logo).
                                    into(imageView);
                        }
                    } else {
                        Glide.with(context).
                                load(R.mipmap.splash_screen_logo).
                                placeholder(R.mipmap.splash_screen_logo).
                                error(R.mipmap.splash_screen_logo).
                                into(imageView);
                    }
                }


                view.addView(imageLayout, 0);
            }catch (Exception e){
                e.getMessage();
            }
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(videoView!=null)
        videoView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
if(videoView!=null)
        videoView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView!=null)
        videoView.onDestroy();
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
       // fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
       // fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
//        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                if(!isFABOpen){
//                    fabLayout1.setVisibility(View.GONE);
//                    fabLayout2.setVisibility(View.GONE);
//                    fabLayout3.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//
    }



    @Override
    public void onBackPressed() {
        if(isFABOpen){
            closeFABMenu();
        }
        if (!DisplayUtils.isPortrait(this)) {
            if(videoView!=null)
            if (!videoView.isLock()) {
                DisplayUtils.toggleScreenOrientation(this);
            }else{
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    //TODO on navigation icon click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
