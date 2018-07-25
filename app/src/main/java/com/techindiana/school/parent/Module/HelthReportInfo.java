
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelthReportInfo {

    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("div_name")
    @Expose
    private String divName;
    @SerializedName("right_eye")
    @Expose
    private String rightEye;
    @SerializedName("left_eye")
    @Expose
    private String leftEye;
    @SerializedName("eye_comment")
    @Expose
    private String eyeComment;
    @SerializedName("right_ear")
    @Expose
    private String rightEar;
    @SerializedName("right_ear_dblevel")
    @Expose
    private String rightEarDblevel;
    @SerializedName("left_ear")
    @Expose
    private String leftEar;
    @SerializedName("left_ear_dblevel")
    @Expose
    private String leftEarDblevel;
    @SerializedName("ear_comment")
    @Expose
    private String earComment;
    @SerializedName("scoliosis")
    @Expose
    private String scoliosis;
    @SerializedName("lordosis")
    @Expose
    private String lordosis;
    @SerializedName("kyphosis")
    @Expose
    private String kyphosis;
    @SerializedName("body_comment")
    @Expose
    private String bodyComment;
    @SerializedName("dr_comment")
    @Expose
    private String drComment;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public String getRightEye() {
        return rightEye;
    }

    public void setRightEye(String rightEye) {
        this.rightEye = rightEye;
    }

    public String getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(String leftEye) {
        this.leftEye = leftEye;
    }

    public String getEyeComment() {
        return eyeComment;
    }

    public void setEyeComment(String eyeComment) {
        this.eyeComment = eyeComment;
    }

    public String getRightEar() {
        return rightEar;
    }

    public void setRightEar(String rightEar) {
        this.rightEar = rightEar;
    }

    public String getRightEarDblevel() {
        return rightEarDblevel;
    }

    public void setRightEarDblevel(String rightEarDblevel) {
        this.rightEarDblevel = rightEarDblevel;
    }

    public String getLeftEar() {
        return leftEar;
    }

    public void setLeftEar(String leftEar) {
        this.leftEar = leftEar;
    }

    public String getLeftEarDblevel() {
        return leftEarDblevel;
    }

    public void setLeftEarDblevel(String leftEarDblevel) {
        this.leftEarDblevel = leftEarDblevel;
    }

    public String getEarComment() {
        return earComment;
    }

    public void setEarComment(String earComment) {
        this.earComment = earComment;
    }

    public String getScoliosis() {
        return scoliosis;
    }

    public void setScoliosis(String scoliosis) {
        this.scoliosis = scoliosis;
    }

    public String getLordosis() {
        return lordosis;
    }

    public void setLordosis(String lordosis) {
        this.lordosis = lordosis;
    }

    public String getKyphosis() {
        return kyphosis;
    }

    public void setKyphosis(String kyphosis) {
        this.kyphosis = kyphosis;
    }

    public String getBodyComment() {
        return bodyComment;
    }

    public void setBodyComment(String bodyComment) {
        this.bodyComment = bodyComment;
    }

    public String getDrComment() {
        return drComment;
    }

    public void setDrComment(String drComment) {
        this.drComment = drComment;
    }

}
