package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorCityData implements Parcelable {

    public static final Parcelable.Creator<BranchLocatorCityData> CREATOR = new Parcelable.Creator<BranchLocatorCityData>() {
        @Override
        public BranchLocatorCityData createFromParcel(Parcel source) {
            return new BranchLocatorCityData(source);
        }

        @Override
        public BranchLocatorCityData[] newArray(int size) {
            return new BranchLocatorCityData[size];
        }
    };
    /**
     * ID : 1
     * NAME : Bangaluru
     */

    @SerializedName("ID")
    private String id;
    @SerializedName("NAME")
    private String name;

    public BranchLocatorCityData() {
    }

    protected BranchLocatorCityData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
