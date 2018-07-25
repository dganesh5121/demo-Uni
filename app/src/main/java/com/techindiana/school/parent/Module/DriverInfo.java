
package com.techindiana.school.parent.Module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverInfo {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("driver_number")
    @Expose
    private String driverNumber;
    @SerializedName("assistant_name")
    @Expose
    private String assistantName;
    @SerializedName("assistant_mobile")
    @Expose
    private String assistantMobile;
    @SerializedName("log_status")
    @Expose
    private String logStatus;
    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("bus_id")
    @Expose
    private String bus_id;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getAssistantMobile() {
        return assistantMobile;
    }

    public void setAssistantMobile(String assistantMobile) {
        this.assistantMobile = assistantMobile;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }
}
