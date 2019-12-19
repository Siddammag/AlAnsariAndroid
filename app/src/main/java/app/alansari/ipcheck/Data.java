package app.alansari.ipcheck;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("message")
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "message = '" + message + '\'' +
                        "}";
    }
}