package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BusinessTypeCeData implements Parcelable {

    /**
     * APPROVAL_STATUS : A
     * ACTIVE_IND : Y
     * BUSINESS_TYPE_CODE : 1
     * BUSINESS_TYPE_DESC : AGRICULTURE AND ALLIED ACTIVITIES
     * BUSINESS_TYPE_PK_ID : 1
     */

    @SerializedName("APPROVAL_STATUS")
    private String approvalStatus;
    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("BUSINESS_TYPE_CODE")
    private String businessTypeCode;
    @SerializedName("BUSINESS_TYPE_DESC")
    private String businessTypeDesc;
    @SerializedName("BUSINESS_TYPE_PK_ID")
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

    public String getBusinessTypeDesc() {
        return businessTypeDesc;
    }

    public void setBusinessTypeDesc(String businessTypeDesc) {
        this.businessTypeDesc = businessTypeDesc;
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
        dest.writeString(this.businessTypeDesc);
        dest.writeInt(this.id);
    }

    public BusinessTypeCeData() {
    }

    protected BusinessTypeCeData(Parcel in) {
        this.approvalStatus = in.readString();
        this.active = in.readString();
        this.businessTypeCode = in.readString();
        this.businessTypeDesc = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<BusinessTypeCeData> CREATOR = new Parcelable.Creator<BusinessTypeCeData>() {
        @Override
        public BusinessTypeCeData createFromParcel(Parcel source) {
            return new BusinessTypeCeData(source);
        }

        @Override
        public BusinessTypeCeData[] newArray(int size) {
            return new BusinessTypeCeData[size];
        }
    };
}
