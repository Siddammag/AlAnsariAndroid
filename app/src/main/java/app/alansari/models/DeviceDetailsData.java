package app.alansari.models;


import app.alansari.Utils.LogUtils;

/**
 * Created by Parveen Dala on 02 November, 2016.
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class DeviceDetailsData {

    /**
     * deviceId : 1
     * deviceMac : 1
     * devicePushId : 1
     * deviceType : 1
     * deviceName : 1
     * deviceOs : 1
     */

    private String deviceId;
    private String deviceMac;
    private String devicePushId;
    private String deviceType;
    private String deviceName;
    private String deviceOs;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        LogUtils.d("ok", "deviceId " + deviceId);
        this.deviceId = deviceId;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        LogUtils.d("ok", "deviceMac " + deviceMac);
        this.deviceMac = deviceMac;
    }

    public String getDevicePushId() {
        return devicePushId;
    }

    public void setDevicePushId(String devicePushId) {
        LogUtils.d("ok", "devicePushId " + devicePushId);
        this.devicePushId = devicePushId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        LogUtils.d("ok", "deviceType " + deviceType);
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        LogUtils.d("ok", "deviceName " + deviceName);
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        LogUtils.d("ok", "deviceOs " + deviceOs);
        this.deviceOs = deviceOs;
    }
}
