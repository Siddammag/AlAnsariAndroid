package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 19 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BeneficiaryTypeCeData implements Parcelable {

    public static final Parcelable.Creator<BeneficiaryTypeCeData> CREATOR = new Parcelable.Creator<BeneficiaryTypeCeData>() {
        @Override
        public BeneficiaryTypeCeData createFromParcel(Parcel source) {
            return new BeneficiaryTypeCeData(source);
        }

        @Override
        public BeneficiaryTypeCeData[] newArray(int size) {
            return new BeneficiaryTypeCeData[size];
        }
    };
    /**
     * ACTIVE_IND : Y
     * BENEFICIARY_ID : 3
     * BENEFICIARY_TYPE_DESC : CORPORATE
     * BENEFICIARY_TYPE_CODE : COR
     */

    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("BENEFICIARY_ID")
    private int id;
    @SerializedName("BENEFICIARY_TYPE_DESC")
    private String beneficiaryTypeDesc;
    @SerializedName("BENEFICIARY_TYPE_CODE")
    private String beneficiaryTypeCode;

    public BeneficiaryTypeCeData() {
    }

    protected BeneficiaryTypeCeData(Parcel in) {
        this.active = in.readString();
        this.id = in.readInt();
        this.beneficiaryTypeDesc = in.readString();
        this.beneficiaryTypeCode = in.readString();
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeneficiaryTypeDesc() {
        return beneficiaryTypeDesc;
    }

    public void setBeneficiaryTypeDesc(String beneficiaryTypeDesc) {
        this.beneficiaryTypeDesc = beneficiaryTypeDesc;
    }

    public String getBeneficiaryTypeCode() {
        return beneficiaryTypeCode;
    }

    public void setBeneficiaryTypeCode(String beneficiaryTypeCode) {
        this.beneficiaryTypeCode = beneficiaryTypeCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.active);
        dest.writeInt(this.id);
        dest.writeString(this.beneficiaryTypeDesc);
        dest.writeString(this.beneficiaryTypeCode);
    }
}
