package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BusinessActivitiesCeData implements Parcelable {

    public static final Parcelable.Creator<BusinessActivitiesCeData> CREATOR = new Parcelable.Creator<BusinessActivitiesCeData>() {
        @Override
        public BusinessActivitiesCeData createFromParcel(Parcel source) {
            return new BusinessActivitiesCeData(source);
        }

        @Override
        public BusinessActivitiesCeData[] newArray(int size) {
            return new BusinessActivitiesCeData[size];
        }
    };
    /**
     * BUSINESS_ACTIVITY_PK_ID : 1
     * BUSINESS_ACTIVITY_DESC : MOBILE PHONES
     */

    @SerializedName("BUSINESS_ACTIVITY_PK_ID")
    private int id;
    @SerializedName("BUSINESS_ACTIVITY_DESC")
    private String name;

    public BusinessActivitiesCeData() {
    }

    protected BusinessActivitiesCeData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }
}
