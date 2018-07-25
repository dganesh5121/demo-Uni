package com.techindiana.school.parent.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class CustomMap extends MapView {

    private ViewParent mViewParent;

    public CustomMap(Context context) {
        super(context);
    }

    public CustomMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMap(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setViewParent(@Nullable final ViewParent viewParent) { //any ViewGroup
        mViewParent = viewParent;
    }

    public CustomMap(Context context, GoogleMapOptions options) {
        super(context, options);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                getParent().getParent().requestDisallowInterceptTouchEvent(true);
              //  Timber.d("Inside if of action down");
                break;
            case MotionEvent.ACTION_UP:

                getParent().getParent().requestDisallowInterceptTouchEvent(false);
              //  Timber.d("Inside if of action up");

                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}