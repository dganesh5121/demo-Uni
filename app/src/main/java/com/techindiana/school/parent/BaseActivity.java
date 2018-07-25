package com.techindiana.school.parent;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.techindiana.school.parent.Utils.DialogUtils;


/**
 * Created by Devendra P. Chaudhari on 14-Feb-17.
 */

public class BaseActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;
    protected AlertDialog internetDialog;

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Methods.getParameter(ctx);
        context = this;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (internetDialog != null && internetDialog.isShowing())
            internetDialog.dismiss();
    }

    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgress() {
        if (progressDialog == null)
            progressDialog = DialogUtils.createProgressDialog(context);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

/*    public static void ChangeTitle(final Context ctx, String title, Activity activity) {
        try {
            ((activity) ctx).getSupportActionBar().show();
            ((activity) ctx).getSupportActionBar().setHomeButtonEnabled(true);
            ((activity) ctx).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((activity) ctx).getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back);
            TextView TextViewNewFont = new TextView(ctx);
            TextViewNewFont.setText(title);
            TextViewNewFont.setTextSize(18);
            TextViewNewFont.setSingleLine();
            TextViewNewFont.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewNewFont.setGravity(Gravity.CENTER);

            TextViewNewFont.setTextColor(Color.WHITE);
            ((activity) ctx).actionBar.setCustomView(TextViewNewFont);
            Methods.getParameter(ctx);
        } catch (Exception e) {
            e.getMessage();
        }
    }*/
}