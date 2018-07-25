
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryDetailsInfo {

    @SerializedName("gallery_id")
    @Expose
    private String galleryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("gallery_date")
    @Expose
    private String galleryDate;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("images")
    @Expose
    private List<GalleryDetailsImage> galleryDetailsImages = null;

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGalleryDate() {
        return galleryDate;
    }

    public void setGalleryDate(String galleryDate) {
        this.galleryDate = galleryDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<GalleryDetailsImage> getGalleryDetailsImages() {
        return galleryDetailsImages;
    }

    public void setGalleryDetailsImages(List<GalleryDetailsImage> galleryDetailsImages) {
        this.galleryDetailsImages = galleryDetailsImages;
    }

}
