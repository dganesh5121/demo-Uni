package com.techindiana.school.parent.Utils;

import com.boredream.bdvideoplayer.bean.IVideoInfo;

public class VideoDetailInfo implements IVideoInfo {

    public String title;
    public String videoPath;



    public VideoDetailInfo(String title, String videoPath) {
        this.title = title;
        this.videoPath = videoPath;
    }

    @Override
    public String getVideoTitle() {
        return title;
    }
    public void setVideoTitle(String title) {
        this.title = title;
    }
    @Override
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
