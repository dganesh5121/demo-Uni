package com.techindiana.school.parent.Database;
/*
Created By: DGP 23/02/2018
Updated By: DGP
Class Name:DatabaseHelper
Class Details: Initial database is created
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "parent.sqlite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL("CREATE TABLE tblLogin(id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "userID TEXT ,userFName TEXT ,userLName TEXT ," +
                    "userEmail TEXT  ,userPhone1 TEXT , userImg TEXT ,  userPassword TEXT,  active_student_id TEXT)");

            db.execSQL("INSERT INTO tblLogin( userID, userFName , userLName , userEmail " +
                    ", userPhone1, userImg, userPassword, active_student_id)" +
                    " values('0','','','','','','','')");

              db.execSQL("CREATE TABLE tblChild(id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                      "childID TEXT ,childFName TEXT ,childLName TEXT ," +
                    "childSchoolId TEXT  ,childClassId TEXT , childDivId TEXT , childImg TEXT ," +
                      " childVarified TEXT ,  childActive TEXT)");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        try {

            db.execSQL("DROP TABLE IF EXISTS tblLogin");
            db.execSQL("DROP TABLE IF EXISTS tblChild");
            onCreate(db);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}