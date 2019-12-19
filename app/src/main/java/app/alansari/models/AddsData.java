package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by FuGenX-test on 02-03-2018.
 */

public class AddsData implements Parcelable {

    @SerializedName("image")
    String imageBase64;

    @SerializedName("imageURL")
    String imageURL;

    @SerializedName("message")
    String message;

    public AddsData() {
    }

    public AddsData(String imageBase64, String message) {
        this.imageBase64 = imageBase64;
        this.message = message;
    }

    public AddsData(String imageBase64, String imageURL, String message) {
        this.imageBase64 = imageBase64;
        this.imageURL = imageURL;
        this.message = message;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageBase64);
        dest.writeString(this.imageURL);
        dest.writeString(this.message);
    }

    protected AddsData(Parcel in) {
        this.imageBase64 = in.readString();
        this.imageURL = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<AddsData> CREATOR = new Parcelable.Creator<AddsData>() {
        @Override
        public AddsData createFromParcel(Parcel source) {
            return new AddsData(source);
        }

        @Override
        public AddsData[] newArray(int size) {
            return new AddsData[size];
        }
    };
}
