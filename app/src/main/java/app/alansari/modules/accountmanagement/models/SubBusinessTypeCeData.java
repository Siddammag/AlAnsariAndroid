package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class SubBusinessTypeCeData implements Parcelable {

    /**
     * APPROVAL_STATUS : A
     * ACTIVE_IND : Y
     * BUSINESS_TYPE_CODE : 1
     * SUB_BUSINESS_TYPE_CODE : 0101/
     * SUB_BUSINESS_TYPE_DESC : CULTIVATION OF CROPS
     * SUB_BUSINESS_TYPE_PK_ID : 1
     */

    @SerializedName("APPROVAL_STATUS")
    private String approvalStatus;
    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("BUSINESS_TYPE_CODE")
    private String businessTypeCode;
    @SerializedName("SUB_BUSINESS_TYPE_CODE")
    private String code;
    @SerializedName("SUB_BUSINESS_TYPE_DESC")
    private String name;
    @SerializedName("SUB_BUSINESS_TYPE_PK_ID")
    private int id;

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        dest.writeString(this.approvalStatus);
        dest.writeString(this.active);
        dest.writeString(this.businessTypeCode);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public SubBusinessTypeCeData() {
    }

    protected SubBusinessTypeCeData(Parcel in) {
        this.approvalStatus = in.readString();
        this.active = in.readString();
        this.businessTypeCode = in.readString();
        this.code = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<SubBusinessTypeCeData> CREATOR = new Parcelable.Creator<SubBusinessTypeCeData>() {
        @Override
        public SubBusinessTypeCeData createFromParcel(Parcel source) {
            return new SubBusinessTypeCeData(source);
        }

        @Override
        public SubBusinessTypeCeData[] newArray(int size) {
            return new SubBusinessTypeCeData[size];
        }
    };
}
