package com.techindiana.school.parent.Database;

/*
Created By: DGP 23/02/2018
Updated By: DGP
Class Name:ParentDBAL
Class Details: all the data base function or opration done in this file
*/

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techindiana.school.parent.Vars.Constant;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Retrofit;


public class ParentDBAL {
    private static Retrofit retrofit;

    // TODO Get User details from local database
    public static void getUserDetailsDB(Context context) {
        try {
            Cursor c1 = null;

            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getReadableDatabase();

            c1 = db.rawQuery("SELECT * FROM tblLogin", null);
            c1.moveToFirst();
            Constant.userID = c1.getString(c1.getColumnIndex("userID"));
            Constant.userFName = c1.getString(c1.getColumnIndex("userFName"));
            Constant.userLName = c1.getString(c1.getColumnIndex("userLName"));
            Constant.userEmail = c1.getString(c1.getColumnIndex("userEmail"));
            Constant.userPhone1 = c1.getString(c1.getColumnIndex("userPhone1"));
            Constant.userImg = c1.getString(c1.getColumnIndex("userImg"));
            Constant.userPassword = c1.getString(c1.getColumnIndex("userPassword"));
            Constant.userActiveStudentId = c1.getString(c1.getColumnIndex("active_student_id"));
            db.close();
            dh.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    // TODO It would update the login states
    public static void updateUserDetails(Context context, String userID, String userFName, String userLName,
                                         String userEmail,
                                         String userPhone1, String img, String password, String userActiveStudentId) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getWritableDatabase();
            db.execSQL("UPDATE tblLogin SET "
                    + "userID='" + userID
                    + "' ,userFName='" + userFName
                    + "' ,userLName='" + userLName
                    + "' ,userEmail='" + userEmail
                    + "' , userPhone1 = '" + userPhone1
                    + "' ,userImg='" + img
                    + "' ,userPassword='" + password
                    + "' ,active_student_id='" + userActiveStudentId
                    + "'");
            db.close();
            dh.close();
            getUserDetailsDB(context);
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // TODO It would update the login states
    public static void insertChildDetails(Context context, String childID, String childFName, String childLName,
                                          String childSchoolId,
                                          String childClassId, String childDivId, String childImg, String childVarified, String childActive) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getWritableDatabase();
            db.execSQL("INSERT INTO tblChild" +
                    "( childID, childFName , childLName , childSchoolId " +
                    ", childClassId, childDivId, childImg, childVarified, childActive)" +
                    " values('" + childID + "','" + childFName + "','" + childLName +
                    "','" + childSchoolId + "','" + childClassId + "','" + childDivId +
                    "','" + childImg + "','" + childVarified + "','" + childActive +
                    "')");
            db.close();
            dh.close();
            // getUserDetailsDB(context);
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // TODO Get User details from local database
    public static void getChildDetails(Context context) {
        try {
            Cursor c1 = null;

            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getReadableDatabase();

            c1 = db.rawQuery("SELECT * FROM tblChild", null);
            c1.moveToFirst();
            Constant.childList = new ArrayList<HashMap<String, String>>();
            if (Constant.childList.size() > 0)
                Constant.childList.clear();
            Constant.child1="";
            Constant.child2="";
            Constant.child3="";
            for (int i = 0; i < c1.getCount(); i++) {

                HashMap<String, String> h1 = new HashMap<String, String>();

                h1.put("childID", c1.getString(c1.getColumnIndex("childID")));
                h1.put("childFName", c1.getString(c1.getColumnIndex("childFName")));
                h1.put("childLName", c1.getString(c1.getColumnIndex("childLName")));
                h1.put("childSchoolId", c1.getString(c1.getColumnIndex("childSchoolId")));
                h1.put("childClassId", c1.getString(c1.getColumnIndex("childClassId")));
                h1.put("childDivId", c1.getString(c1.getColumnIndex("childDivId")));
                h1.put("childImg", c1.getString(c1.getColumnIndex("childImg")));
                h1.put("childVarified", c1.getString(c1.getColumnIndex("childVarified")));
                h1.put("childActive", c1.getString(c1.getColumnIndex("childActive")));
                Constant.childList.add(h1);

                if (Constant.userActiveStudentId.equals(c1.getString(c1.getColumnIndex("childID")))) {
                    Constant.child1 = c1.getString(c1.getColumnIndex("childID"));
                    Constant.childID = c1.getString(c1.getColumnIndex("childID"));
                    Constant.childFName = c1.getString(c1.getColumnIndex("childFName"));
                    Constant.childLName = c1.getString(c1.getColumnIndex("childLName"));
                    Constant.childSchoolId = c1.getString(c1.getColumnIndex("childSchoolId"));
                    Constant.childClassId = c1.getString(c1.getColumnIndex("childClassId"));
                    Constant.childDivId = c1.getString(c1.getColumnIndex("childDivId"));
                    Constant.childImg = c1.getString(c1.getColumnIndex("childImg"));
                    Constant.childVarified = c1.getString(c1.getColumnIndex("childVarified"));
                    Constant.childActive = c1.getString(c1.getColumnIndex("childActive"));
                } else if (Constant.child2.length() == 0) {
                    Constant.child2 = c1.getString(c1.getColumnIndex("childID"));
                } else if (Constant.child3.length() == 0) {
                    Constant.child3 = c1.getString(c1.getColumnIndex("childID"));
                }

                c1.moveToNext();
            }

            db.close();
            dh.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }


/*    // TODO Get User details from local database
    public static void getChildSettingDetails(Context context) {
        try {
            Cursor c1 = null;
            retrofit = RetrofitUtils.getRetrofit();
            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getReadableDatabase();

            c1 = db.rawQuery("SELECT * FROM tblChild", null);
            c1.moveToFirst();
            Constant.childList = new ArrayList<HashMap<String, String>>();
            if (Constant.childList.size() > 0)
                Constant.childList.clear();
            Constant.child2="";
            Constant.child3="";
            for (int i = 0; i < c1.getCount(); i++) {

                HashMap<String, String> h1 = new HashMap<String, String>();

                h1.put("childID", c1.getString(c1.getColumnIndex("childID")));
                h1.put("childFName", c1.getString(c1.getColumnIndex("childFName")));
                h1.put("childLName", c1.getString(c1.getColumnIndex("childLName")));
                h1.put("childSchoolId", c1.getString(c1.getColumnIndex("childSchoolId")));
                h1.put("childClassId", c1.getString(c1.getColumnIndex("childClassId")));
                h1.put("childDivId", c1.getString(c1.getColumnIndex("childDivId")));
                h1.put("childImg", c1.getString(c1.getColumnIndex("childImg")));
                h1.put("childVarified", c1.getString(c1.getColumnIndex("childVarified")));
                h1.put("childActive", c1.getString(c1.getColumnIndex("childActive")));
                Constant.childList.add(h1);


                if (Constant.child1.length() > 0) {
                    if (Constant.child1.equals(c1.getString(c1.getColumnIndex("childID")))) {
                        Constant.child1 = c1.getString(c1.getColumnIndex("childID"));
                        Constant.childID = c1.getString(c1.getColumnIndex("childID"));
                        Constant.childFName = c1.getString(c1.getColumnIndex("childFName"));
                        Constant.childLName = c1.getString(c1.getColumnIndex("childLName"));
                        Constant.childSchoolId = c1.getString(c1.getColumnIndex("childSchoolId"));
                        Constant.childClassId = c1.getString(c1.getColumnIndex("childClassId"));
                        Constant.childDivId = c1.getString(c1.getColumnIndex("childDivId"));
                        Constant.childImg = c1.getString(c1.getColumnIndex("childImg"));
                        Constant.childVarified = c1.getString(c1.getColumnIndex("childVarified"));
                        Constant.childActive = c1.getString(c1.getColumnIndex("childActive"));
                    }else if (Constant.child2.length() == 0) {
                        Constant.child2 = c1.getString(c1.getColumnIndex("childID"));
                    } else if (Constant.child3.length() == 0) {
                        Constant.child3 = c1.getString(c1.getColumnIndex("childID"));
                    }
                }else {
                    if (Constant.child1.length() == 0) {
                        Constant.child1 = c1.getString(c1.getColumnIndex("childID"));
                        Constant.childID = c1.getString(c1.getColumnIndex("childID"));
                        Constant.childFName = c1.getString(c1.getColumnIndex("childFName"));
                        Constant.childLName = c1.getString(c1.getColumnIndex("childLName"));
                        Constant.childSchoolId = c1.getString(c1.getColumnIndex("childSchoolId"));
                        Constant.childClassId = c1.getString(c1.getColumnIndex("childClassId"));
                        Constant.childDivId = c1.getString(c1.getColumnIndex("childDivId"));
                        Constant.childImg = c1.getString(c1.getColumnIndex("childImg"));
                        Constant.childVarified = c1.getString(c1.getColumnIndex("childVarified"));
                        Constant.childActive = c1.getString(c1.getColumnIndex("childActive"));
                        switchStudent();
                    } else if (Constant.child2.length() == 0) {
                        Constant.child2 = c1.getString(c1.getColumnIndex("childID"));
                    } else if (Constant.child3.length() == 0) {
                        Constant.child3 = c1.getString(c1.getColumnIndex("childID"));
                    }
                }
                c1.moveToNext();
            }

            db.close();
            dh.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    //TODO: Web call for Notification Details...
    private static void switchStudent() {
        try {
            //     showProgress();
            //Creating Rest Services
            RestCallInterface restInterface = retrofit.create(RestCallInterface.class);

            Call<BaseResp> call = restInterface.switchStudent(Constant.apiKey, Constant.childSchoolId,  Constant.childID, Constant.userID);
            call.enqueue(new Callback<BaseResp>() {
                @Override
                public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess().equals(Constant.RESP_SUCCESS)) {

                            } else {
                                switchStudent();
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<BaseResp> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }

*/

    // TODO logout
    public static void userLogoOut(Context context) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getWritableDatabase();
             db.execSQL("UPDATE tblLogin SET "
                    + "userID='0"
                    + "' ,userFName='"
                    + "' ,userLName='"
                    + "' ,userEmail='"
                    + "' , userPhone1 = '"
                    + "' ,userImg='"
                    + "' ,userPassword='"
                    + "' ,active_student_id='"
                    + "'");

            db.execSQL("delete from tblChild");
            db.close();
            dh.close();
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    // TODO logout
    public static void deleteChild(Context context) {
        try {
            DatabaseHelper dh = new DatabaseHelper(context);
            SQLiteDatabase db = dh.getWritableDatabase();
            db.execSQL("delete from tblChild");
            db.close();
            dh.close();
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }


}