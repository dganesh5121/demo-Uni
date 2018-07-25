
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePageInfo {

    @SerializedName("banner")
    @Expose
    private List<HomePageBanner> homePageBanner = null;

    @SerializedName("notice")
    @Expose
    private List<HomePageNotice> homePageNotice = null;

    @SerializedName("gallery")
    @Expose
    private List<HomePageGallery> homePageGallery = null;

    @SerializedName("notification")
    @Expose
    private List<HomePageNotification> homePageNotification = null;

    public List<HomePageBanner> getHomePageBanner() {
        return homePageBanner;
    }

    public void setHomePageBanner(List<HomePageBanner> homePageBanner) {
        this.homePageBanner = homePageBanner;
    }

    public List<HomePageNotice> getHomePageNotice() {
        return homePageNotice;
    }

    public void setHomePageNotice(List<HomePageNotice> homePageNotice) {
        this.homePageNotice = homePageNotice;
    }

    public List<HomePageGallery> getHomePageGallery() {
        return homePageGallery;
    }

    public void setHomePageGallery(List<HomePageGallery> homePageGallery) {
        this.homePageGallery = homePageGallery;
    }

    public List<HomePageNotification> getHomePageNotification() {
        return homePageNotification;
    }

    public void setHomePageNotification(List<HomePageNotification> homePageNotification) {
        this.homePageNotification = homePageNotification;
    }

}
