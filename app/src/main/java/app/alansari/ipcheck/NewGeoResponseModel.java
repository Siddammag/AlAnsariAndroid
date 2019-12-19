package app.alansari.ipcheck;

import com.google.gson.annotations.SerializedName;

public class NewGeoResponseModel {

    @SerializedName("data")
    private Data data;

    @SerializedName("status")
    private String status;

    @SerializedName("MESSAGE")
    private String message;

    @SerializedName("STATUS_MSG")
    private String status_msg;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    @Override
    public String toString() {
        return
                "NewGeoResponseModel{" +
                        "data = '" + data + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}