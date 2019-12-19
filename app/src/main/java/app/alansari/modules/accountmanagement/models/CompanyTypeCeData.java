package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class CompanyTypeCeData implements Parcelable {

    /**
     * CORP_PK_ID : 1
     * BUSINESS_CODE : 1
     * BUSINESS_DESC : EXCHANGE
     */

    @SerializedName("CORP_PK_ID")
    private int id;
    @SerializedName("BUSINESS_CODE")
    private String businessCode;
    @SerializedName("BUSINESS_DESC")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.businessCode);
        dest.writeString(this.name);
    }

    public CompanyTypeCeData() {
    }

    protected CompanyTypeCeData(Parcel in) {
        this.id = in.readInt();
        this.businessCode = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CompanyTypeCeData> CREATOR = new Parcelable.Creator<CompanyTypeCeData>() {
        @Override
        public CompanyTypeCeData createFromParcel(Parcel source) {
            return new CompanyTypeCeData(source);
        }

        @Override
        public CompanyTypeCeData[] newArray(int size) {
            return new CompanyTypeCeData[size];
        }
    };
}
