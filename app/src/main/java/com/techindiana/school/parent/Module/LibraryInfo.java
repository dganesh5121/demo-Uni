
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    public String type;

    public LibraryInfo(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

}
