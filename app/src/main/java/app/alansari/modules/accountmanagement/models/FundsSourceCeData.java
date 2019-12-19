package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class FundsSourceCeData implements Parcelable {

    /**
     * FUNDS_SOURCE_ID : 1
     * FUNDS_SOURCES : SALARY
     */

    @SerializedName("FUNDS_SOURCE_ID")
    private int id;
    @SerializedName("FUNDS_SOURCES")
    private String name;

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

    public FundsSourceCeData() {
    }

    protected FundsSourceCeData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<FundsSourceCeData> CREATOR = new Parcelable.Creator<FundsSourceCeData>() {
        @Override
        public FundsSourceCeData createFromParcel(Parcel source) {
            return new FundsSourceCeData(source);
        }

        @Override
        public FundsSourceCeData[] newArray(int size) {
            return new FundsSourceCeData[size];
        }
    };
}
