package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 19 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class ResidentialStatusCeData implements Parcelable {

    public static final Parcelable.Creator<ResidentialStatusCeData> CREATOR = new Parcelable.Creator<ResidentialStatusCeData>() {
        @Override
        public ResidentialStatusCeData createFromParcel(Parcel source) {
            return new ResidentialStatusCeData(source);
        }

        @Override
        public ResidentialStatusCeData[] newArray(int size) {
            return new ResidentialStatusCeData[size];
        }
    };
    /**
     * ID : 1
     * NAME : Resident
     */

    @SerializedName("ID")
    private int id;
    @SerializedName("NAME")
    private String name;

    public ResidentialStatusCeData() {
    }

    protected ResidentialStatusCeData(Parcel in) {
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
