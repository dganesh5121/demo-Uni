
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeacherMessagesInfo {

    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("sender_type")
    @Expose
    private String senderType;
    @SerializedName("msg_content")
    @Expose
    private String msgContent;
    @SerializedName("msg_time")
    @Expose
    private String msgTime;
    @SerializedName("type")
    @Expose
    private String type;
    public String type1;
    public TeacherMessagesInfo(String messageId, String senderType, String msgContent, String msgTime, String type ) {
        this.messageId=messageId;
        this.senderType=senderType;
        this.msgContent=msgContent;
        this.msgTime=msgTime;
        this.type=type;
    }
    public TeacherMessagesInfo(String type1) {
        this.type1 = type1;
    }
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
