package com.techindiana.school.parent.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.techindiana.school.parent.Database.DatabaseHelper;
import com.techindiana.school.parent.R;
import com.techindiana.school.parent.Vars.Constant;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;


/**
 * Created by TechIndiana on 16-12-2015.
 */
public class Methods {
    //chage the language of the app;
    public static final String EXTRA_ACTIVITY = "activityFlag";
    public static final String EXTRA_ACTIVITY_HOME = "activityHome";
    public static AlertDialog.Builder builder;
    public static AlertDialog alert;

    public static void setFont(Context context, ViewGroup vg) {
        final String FONT_NAME;
        try {
            FONT_NAME = context.getString(R.string.font_regular);

            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                if (v instanceof ViewGroup)
                    setFont(context, (ViewGroup) v);
                else if (v instanceof TextView) {
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                    // ((TextView) v).setTypeface(Typeface.createFromAsset(context.getResources().getAssets(), FONT_NAME));
                } else if (v instanceof EditText) {
                    ((EditText) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                } else if (v instanceof Button) {
                    ((Button) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                } else if (v instanceof CheckBox) {
                    ((CheckBox) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                } else if (v instanceof RadioButton) {
                    ((RadioButton) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                } else if (v instanceof Spinner) {
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), FONT_NAME));
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public static void updateLanguage(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String language = Constant.language;
        String lang = prefs.getString("locale_override", language);
        updateLanguage(ctx, lang);
    }

    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();

        ctx.getResources().updateConfiguration(cfg, ctx.getResources().getDisplayMetrics());
    }

    // TODO Check Device Network Connection ...
    public static boolean isOnline(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static String getLocalIpAddress(Context context) throws SocketException {
        try {
            // WifiManager wifiMgr = (WifiManager) ApplicationController.getInstance().getSystemService(this.WIFI_SERVICE);
            WifiManager wifiMgr = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            if (wifiMgr.isWifiEnabled()) {
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                String wifiIpAddress = String.format("%d.%d.%d.%d",
                        (ip & 0xff),
                        (ip >> 8 & 0xff),
                        (ip >> 16 & 0xff),
                        (ip >> 24 & 0xff));

                return wifiIpAddress;
            }

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    // TODO Alert No Network on Device ...
    public static void alertNoNetwork(final Context ctx) {
        builder = new AlertDialog.Builder(ctx);
        builder.setMessage(R.string.err_network_failure_title)
                .setMessage(R.string.err_network_failure_message)
                .setCancelable(false)
                .setPositiveButton(R.string.msgYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    // TODO Alert No Network on Device ...
    public static void alertMsg(final Context ctx, String title, String msg, String opt) {
        builder = new AlertDialog.Builder(ctx);
        builder.setMessage(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(opt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                        if (Constant.finish == 1) {
                            ((Activity) ctx).finish();
                        }
                    }
                });
        alert = builder.create();
        alert.show();
    }

    // TODO Alert No Network on Device ...
    public static void alertMsg(final Context ctx, String msg) {
        builder = new AlertDialog.Builder(ctx);
        builder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.msgYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        alert = builder.create();
        alert.show();
    }

    public static void getParameter(Context ctx) {
        try {
            // Creation of database and tables
            DatabaseHelper dh = new DatabaseHelper(ctx);
            SQLiteDatabase db = dh.getWritableDatabase();
            db.close();
            dh.close();
            //ParentDBAL.getUserDetailsDB(ctx);
            //get the size of the icon ....
            Drawable d = ctx.getResources().getDrawable(R.mipmap.ic_launcher);
            Constant.iconHeight = d.getIntrinsicHeight();
            DisplayMetrics metrics;
            //get the size of the screen ...
            metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) ctx
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            Constant.screenWidth = metrics.widthPixels;
            Constant.screenHeight = metrics.heightPixels;

            //currnet date ....
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Constant.currentDate = df.format(c.getTime());
            Constant.deviceName = getDeviceName();

            //get android phone information
            Constant.deviceId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                Constant.deviceIpAdress = Methods.getLocalIpAddress(ctx);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static void getWidth(Context ctx) {
        DisplayMetrics metrics;
        //get the size of the screen ...
        metrics = new
                DisplayMetrics();
        WindowManager windowManager = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().
                getMetrics(metrics);
        Constant.screenWidth = metrics.widthPixels;
        Constant.screenHeight = metrics.heightPixels;
    }

}
