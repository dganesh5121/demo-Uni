
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutContentResp {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private AboutContentInfo aboutContentInfo;
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

    public AboutContentInfo getAboutContentInfo() {
        return aboutContentInfo;
    }

    public void setAboutContentInfo(AboutContentInfo aboutContentInfo) {
        this.aboutContentInfo = aboutContentInfo;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
