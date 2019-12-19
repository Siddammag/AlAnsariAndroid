package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class ProfessionCeData implements Parcelable {

    /**
     * BUSINESS_CODE : ENG
     * BUSINESS_DESC : Auditor
     * IND_PK_ID : 1
     */

    @SerializedName("BUSINESS_CODE")
    private String businessCode;
    @SerializedName("BUSINESS_DESC")
    private String name;
    @SerializedName("IND_PK_ID")
    private int id;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
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
        dest.writeString(this.businessCode);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public ProfessionCeData() {
    }

    protected ProfessionCeData(Parcel in) {
        this.businessCode = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<ProfessionCeData> CREATOR = new Parcelable.Creator<ProfessionCeData>() {
        @Override
        public ProfessionCeData createFromParcel(Parcel source) {
            return new ProfessionCeData(source);
        }

        @Override
        public ProfessionCeData[] newArray(int size) {
            return new ProfessionCeData[size];
        }
    };
}
