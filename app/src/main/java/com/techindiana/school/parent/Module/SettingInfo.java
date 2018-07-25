
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingInfo {

    @SerializedName("parent")
    @Expose
    private List<SettingInfoparent> settingInfoparent = null;
    @SerializedName("childs")
    @Expose
    private List<SettingInfochild> settingInfochilds = null;

    public List<SettingInfoparent> getSettingInfoparent() {
        return settingInfoparent;
    }

    public void setSettingInfoparent(List<SettingInfoparent> settingInfoparent) {
        this.settingInfoparent = settingInfoparent;
    }

    public List<SettingInfochild> getSettingInfochilds() {
        return settingInfochilds;
    }

    public void setSettingInfochilds(List<SettingInfochild> settingInfochilds) {
        this.settingInfochilds = settingInfochilds;
    }

}
