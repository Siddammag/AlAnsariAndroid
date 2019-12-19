package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class SubPurposeCeData implements Parcelable {

    /**
     * SUB_PURPOSE_DESC : BUYING HOUSE APARTMENT VILLA
     * SUB_PURPOSE_ID : 34
     * PURPOSE_FK_ID : 6
     */

    @SerializedName("SUB_PURPOSE_DESC")
    private String name;
    @SerializedName("SUB_PURPOSE_ID")
    private int id;
    @SerializedName("PURPOSE_FK_ID")
    private String pouposeId;

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

    public String getPouposeId() {
        return pouposeId;
    }

    public void setPouposeId(String pouposeId) {
        this.pouposeId = pouposeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeString(this.pouposeId);
    }

    public SubPurposeCeData() {
    }

    protected SubPurposeCeData(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.pouposeId = in.readString();
    }

    public static final Parcelable.Creator<SubPurposeCeData> CREATOR = new Parcelable.Creator<SubPurposeCeData>() {
        @Override
        public SubPurposeCeData createFromParcel(Parcel source) {
            return new SubPurposeCeData(source);
        }

        @Override
        public SubPurposeCeData[] newArray(int size) {
            return new SubPurposeCeData[size];
        }
    };
}
