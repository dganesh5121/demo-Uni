package com.techindiana.school.parent.retrofit_utils.restUtils;


import com.techindiana.school.parent.Module.AboutContentResp;
import com.techindiana.school.parent.Module.ActivityCDayWiseResp;
import com.techindiana.school.parent.Module.ActivityCenterResp;
import com.techindiana.school.parent.Module.AttendanceResp;
import com.techindiana.school.parent.Module.BaseResp;
import com.techindiana.school.parent.Module.ClassesNameResp;
import com.techindiana.school.parent.Module.ContactUsResp;
import com.techindiana.school.parent.Module.DivisionNameResp;
import com.techindiana.school.parent.Module.DriverLastLocationResp;
import com.techindiana.school.parent.Module.DriverResp;
import com.techindiana.school.parent.Module.ExtraClassDayWiseResp;
import com.techindiana.school.parent.Module.ExtraClassResp;
import com.techindiana.school.parent.Module.GalleryDetailsResp;
import com.techindiana.school.parent.Module.GalleryResp;
import com.techindiana.school.parent.Module.HelthReportResp;
import com.techindiana.school.parent.Module.HomePageResp;
import com.techindiana.school.parent.Module.HomeworkDayResp;
import com.techindiana.school.parent.Module.HomeworkResp;
import com.techindiana.school.parent.Module.LibraryResp;
import com.techindiana.school.parent.Module.LoginResp;
import com.techindiana.school.parent.Module.ManagementTeamResp;
import com.techindiana.school.parent.Module.NoticeBoardResp;
import com.techindiana.school.parent.Module.NotificationResp;
import com.techindiana.school.parent.Module.SchoolNameResp;
import com.techindiana.school.parent.Module.SettingResp;
import com.techindiana.school.parent.Module.TeacherMessagesResp;
import com.techindiana.school.parent.Module.TeachersListResp;
import com.techindiana.school.parent.Module.TimeTableResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by DGP .
 */
public interface RestCallInterface {


    @FormUrlEncoded
    @POST("login")
    Call<LoginResp> getLogin(
            @Header("API-KEY") String API_KEY,
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_name") String device_name,
            @Field("device_id") String device_id,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<BaseResp> forgotPassword(
            @Header("API-KEY") String API_KEY,
            @Field("email") String email);


    @FormUrlEncoded
    @POST("getDashboardUpdates")
    Call<HomePageResp> getDashboardUpdates(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("stud_id") String stud_id,
            @Field("parent_id") String parent_id
            );



@FormUrlEncoded
    @POST("switchStudent")
    Call<BaseResp> switchStudent(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("student_id") String stud_id,
            @Field("parent_id") String parent_id
            );


@FormUrlEncoded
    @POST("deleteStudent")
    Call<BaseResp> deleteStudent(
            @Header("API-KEY") String API_KEY,
            @Field("parent_id") String school_id,
            @Field("student_id") String stud_id,
            @Field("school_id") String parent_id,
            @Field("is_active") String is_active
            );

 @FormUrlEncoded
    @POST("getTimeTable")
    Call<TimeTableResp> getTimeTable(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("stud_id") String stud_id
            );

 @FormUrlEncoded
    @POST("getNotification")
    Call<NotificationResp> getNotification(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("student_id") String student_id,
            @Field("start") String start);



 @FormUrlEncoded
    @POST("getTeachers")
    Call<TeachersListResp> getTeachers(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("student_id") String student_id,
            @Field("parent_id") String parent_id);


 @FormUrlEncoded
    @POST("getTeacherMessages")
    Call<TeacherMessagesResp> getTeacherMessages(
            @Header("API-KEY") String API_KEY,
            @Field("student_id") String student_id,
            @Field("teacher_id") String teacher_id,
            @Field("parent_id") String parent_id,
            @Field("start") String start);

@FormUrlEncoded
    @POST("getNewTeacherMessages")
    Call<TeacherMessagesResp> getNewTeacherMessages(
            @Header("API-KEY") String API_KEY,
            @Field("student_id") String student_id,
            @Field("teacher_id") String teacher_id,
            @Field("parent_id") String parent_id
            );

/*@FormUrlEncoded
    @POST("getTimeTable")
    Call<NotificationResp> getTimeTable(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("student_id") String student_id,
            @Field("start") String start);*/

@FormUrlEncoded
    @POST("getNoticeBoard")
    Call<NoticeBoardResp> getNoticeBoard(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("stud_id") String stud_id,
            @Field("start") String start);

@FormUrlEncoded
    @POST("getAllGallery")
    Call<GalleryResp> getAllGallery(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("start") String start);



@FormUrlEncoded
    @POST("getLibraryData")
    Call<LibraryResp> getLibraryData(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("student_id") String student_id,
            @Field("parent_id") String parent_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("start") String start);

  @FormUrlEncoded
    @POST("getGalleryDetails")
    Call<GalleryDetailsResp> getGalleryDetails(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("gallery_id") String gallery_id);

    @GET("getSchools  ")
    Call<SchoolNameResp> getSchools  (
            @Header("API-KEY") String API_KEY);

    @FormUrlEncoded
    @POST("getAllClasses")
    Call<ClassesNameResp> getAllClasses(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id
            );
  @FormUrlEncoded
    @POST("getAllDivisions")
    Call<DivisionNameResp> getAllDivisions(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id
            );



 @FormUrlEncoded
    @POST("changePassword")
    Call<BaseResp> changePassword(
            @Header("API-KEY") String API_KEY,
            @Field("parent_id") String parent_id,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password,
            @Field("confirm_password") String confirm_password
            );

    @FormUrlEncoded
    @POST("admissionEnquiry")
    Call<BaseResp> admissionEnquiry(
            @Header("API-KEY") String API_KEY,
            @Field("fname") String fname,
            @Field("lname") String lname,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address") String address,
            @Field("child_name") String child_name,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id);


@FormUrlEncoded
    @POST("getActivity")
    Call<ActivityCenterResp> getActivity(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);

@FormUrlEncoded
    @POST("getActivityDayWise")
    Call<ActivityCDayWiseResp> getActivityDayWise(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("day") String day,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);



@FormUrlEncoded
    @POST("enroll")
    Call<BaseResp> enroll(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("activity_id") String activity_id,
            @Field("enrolled_id") String enrolled_id,
            @Field("stud_id") String stud_id,
            @Field("enrolled_act") String enrolled_act);

@FormUrlEncoded
    @POST("getAttendance")
    Call<AttendanceResp> getAttendance(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);

@FormUrlEncoded
    @POST("getExtraClass")
    Call<ExtraClassResp> getExtraClass(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);


    @FormUrlEncoded
    @POST("getExtraClassDayWise")
    Call<ExtraClassDayWiseResp> getExtraClassDayWise(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("day") String day,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);

@FormUrlEncoded
    @POST("getHomeworkMonthWise")
    Call<HomeworkResp> getHomeworkMonthWise(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);

    @FormUrlEncoded
    @POST("getHomework")
    Call<HomeworkDayResp> getHomework(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("class_id") String class_id,
            @Field("div_id") String div_id,
            @Field("day") String day,
            @Field("month") String month,
            @Field("year") String year,
            @Field("stud_id") String stud_id);

    @FormUrlEncoded
    @POST("helthReport")
    Call<HelthReportResp> helthReport(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("student_id") String student_id
    );


    @FormUrlEncoded
    @POST("contactUs")
    Call<ContactUsResp> contactUs(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id
    );


    @FormUrlEncoded
    @POST("getAboutContent")
    Call<AboutContentResp> getAboutContent(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id
    );

    @FormUrlEncoded
    @POST("getManagementTeam")
    Call<ManagementTeamResp> getManagementTeam(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id
    );


 @FormUrlEncoded
    @POST("sendFeedBack")
    Call<BaseResp> sendFeedBack(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("parent_id") String parent_id,
            @Field("message") String message
    );

@FormUrlEncoded
    @POST("getDriver")
    Call<DriverResp> getDriver(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("parent_id") String parent_id
    );


@FormUrlEncoded
    @POST("getDriverLastLocation")
    Call<DriverLastLocationResp> getDriverLastLocation(
            @Header("API-KEY") String API_KEY,
            @Field("school_id") String school_id,
            @Field("parent_id") String parent_id,
            @Field("driver_id") String driver_id,
            @Field("bus_id") String bus_id
    );

@FormUrlEncoded
    @POST("getSettingInfo")
    Call<SettingResp> getSettingInfo(
            @Header("API-KEY") String API_KEY,
            @Field("parent_id") String parent_id
    );


}