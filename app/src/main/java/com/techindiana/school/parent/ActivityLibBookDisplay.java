package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import com.techindiana.school.parent.Utils.Methods;
import com.techindiana.school.parent.Vars.Constant;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ActivityLibBookDisplay extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    PDFView pdfView;
    Integer pageNumber = 0;
    public ActionBar actionBar;

    String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_my_book_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        try {
            assert actionBar != null;
            //actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
            actionBar.setTitle(Constant.fileName);
            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Methods.setFont(ActivityLibBookDisplay.this, viewGroup);
        try {
            Declaration();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void Declaration() {
        try {
            pdfView = (PDFView) findViewById(R.id.pdfView);
            new RetrievePDFStream().execute(Constant.webImgPath+Constant.filePath);
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    class RetrievePDFStream extends AsyncTask<String ,Void,InputStream>
    {
        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            try {
                //   pdfView.fromStream(inputStream).load();
                pdfView.fromStream(inputStream)
                        .defaultPage(0)
                        .onPageChange(ActivityLibBookDisplay.this)
                        .enableAnnotationRendering(true)
                        .onLoad(ActivityLibBookDisplay.this)
                        .scrollHandle(new DefaultScrollHandle(ActivityLibBookDisplay.this))
                        .spacing(10) // in dp
                        .onPageError(ActivityLibBookDisplay.this)
                        .load();
                hideProgress();
            }catch (Exception e){
                e.getMessage();
                hideProgress();
            }
        }

        @Override
        protected InputStream doInBackground(String... strings) {
           // showProgress();
            InputStream inputStream =null;
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                if(httpURLConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            }
            catch (Exception e)
            {
                hideProgress();
                return null;
            }

            return  inputStream;
        }
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //  setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }
    //TODO on navigation icon click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
