
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginInfo {

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
    @SerializedName("childs")
    @Expose
    private List<ParentChild> parentChilds = null;

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

    public List<ParentChild> getParentChilds() {
        return parentChilds;
    }

    public void setParentChilds(List<ParentChild> parentChilds) {
        this.parentChilds = parentChilds;
    }

}
