package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class DistrictCeData implements Parcelable {

    public static final Parcelable.Creator<DistrictCeData> CREATOR = new Parcelable.Creator<DistrictCeData>() {
        @Override
        public DistrictCeData createFromParcel(Parcel source) {
            return new DistrictCeData(source);
        }

        @Override
        public DistrictCeData[] newArray(int size) {
            return new DistrictCeData[size];
        }
    };
    /**
     * COUNTRY_CODE : 59
     * DISTRICT_NAME : OTHERS
     * DISTRICT_PK_ID : 9999
     */

    @SerializedName("COUNTRY_CODE")
    private String countryCode;
    @SerializedName("DISTRICT_NAME")
    private String name;
    @SerializedName("DISTRICT_PK_ID")
    private int id;

    public DistrictCeData() {
    }

    protected DistrictCeData(Parcel in) {
        this.countryCode = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryCode);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }
}
