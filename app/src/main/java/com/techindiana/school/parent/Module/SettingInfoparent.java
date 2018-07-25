
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingInfoparent {

    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("contact_no")
    @Expose
    private String contactNo;
    @SerializedName("active_student_id")
    @Expose
    private String activeStudentId;
    @SerializedName("notification")
    @Expose
    private String notification;
    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("has_sub_parent")
    @Expose
    private String hasSubParent;

    @SerializedName("parent_type")
    @Expose
    private String parent_type;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getActiveStudentId() {
        return activeStudentId;
    }

    public void setActiveStudentId(String activeStudentId) {
        this.activeStudentId = activeStudentId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getHasSubParent() {
        return hasSubParent;
    }

    public void setHasSubParent(String hasSubParent) {
        this.hasSubParent = hasSubParent;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }
}
