
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherMessagesResp {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TeacherMessagesInfo> teacherMessagesInfo = null;
    @SerializedName("command")
    @Expose
    private String command;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TeacherMessagesInfo> getTeacherMessagesInfo() {
        return teacherMessagesInfo;
    }

    public void setTeacherMessagesInfo(List<TeacherMessagesInfo> teacherMessagesInfo) {
        this.teacherMessagesInfo = teacherMessagesInfo;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
