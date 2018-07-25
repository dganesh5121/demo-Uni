package com.techindiana.school.parent.Vars;
/*
Created By: DGP 18/12/2017
Updated By: DGP
Class Name:
Class Details:
*/

import com.techindiana.school.parent.Module.SettingInfochild;
import com.techindiana.school.parent.Module.SettingInfoparent;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant {
    public static final String RESP_SUCCESS = "1";
    // String Variable ...

    /*   parent variables*/
    public static String userID = "0";
    public static String userFName = "";
    public static String userLName = "";
    public static String userPassword = "";
    public static String userEmail = "";
    public static String userPhone1 = "";
    public static String userImg = "";
    public static String userActiveStudentId = "0";

    /*Child veriables*/
    public static String childID = "0";
    public static String childFName = "";
    public static String childLName = "";
    public static String childSchoolId = "";
    public static String childClassId = "";
    public static String childDivId = "";
    public static String childImg = "";
    public static String childVarified = "0";
    public static String childActive = "0";

/*Device info*/
    public static String currentDate;
    public static String deviceType = "a";
    public static String deviceName = "a";
    public static String deviceId;
    public static String deviceIpAdress;
    public static String device_token = "";

 /*   Web Info*/
    public static String language = "en";
    public static String shareSub = "Share the application google play link ...";
    public static String shareMessage = "https://play.google.com/store/apps/details?id=om.techindiana.school.parent";
    public static String webImgPath = "http://genius-infotech.co.uk/dev/tech_school/";
    public static String webPath = "http://genius-infotech.co.uk/dev/tech_school/api/parentapp/";
    public static String apiKey = "ffba83483041e53fc1e7a3fbea763447";
    public static String regEx = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";//
    public static String[] arrayMonth = new String[] {
            "Jan", "Feb", "March", "April", "May",
            "Jun", "July", "Aug", "Sep", "Oct","Nov","Dec"
    };
    // Int Variable ...
    public static int screenHeight = 40;
    public static int screenWidth = 40;
    public static int iconHeight = 50;//icon height
    public static int finish = 0;


    // Float Variable ...
    // Array Variable ...
    // Array Lisjava.lang.Stringt Variable ...
    public static ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();


    public static String child1="";
    public static String child2="";
    public static String child3="";
    public static ArrayList<SettingInfoparent> settingInfoparents;
    public static ArrayList<SettingInfochild> settingInfochild;
    public static String childToBeEdit;

    public static String fileName="";
    public static String filePath="";
    public static String fileUrl="";
    public static String teacherId="";
    public static String teacherName="";
    public static String teacherImg="";
    public static ArrayList<HashMap<String, String>> aParentList= new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> aChildList= new ArrayList<HashMap<String, String>>();
    public static int childAddPos=0;
}

