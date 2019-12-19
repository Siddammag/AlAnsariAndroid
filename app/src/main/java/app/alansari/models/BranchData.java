package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 02 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BranchData implements Parcelable {

    /*"BANK_CODE": "9999042260001",
            "BANK_NAME": "AXIS BANK",
            "BRANCH_CODE": "762",
            "BRANCH_NAME": "MANDYA",
            "ACTIVE_IND": "N",
            "EFT_CODE": "UTIB0000796",
            "BENEF_BRANCH_PK_ID": 70485,
            "BRANCH_NAME_AR": null,
            "EFT_TYPE": "EFT"*/

    @SerializedName("BENEF_BRANCH_PK_ID")
    private String id;
    @SerializedName("BRANCH_CODE")
    private String branchCode;
    @SerializedName("BRANCH_NAME")
    private String branchName;
    @SerializedName("BRANCH_NAME_AR")
    private String branchNameArabic;
    @SerializedName("BRANCH_ADDRESS")
    private String branchAddress;
    @SerializedName("EFT_CODE")
    private String eftCode;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("ACTIVE_IND")
    private String isActive;

    public BranchData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchNameArabic() {
        return branchNameArabic;
    }

    public void setBranchNameArabic(String branchNameArabic) {
        this.branchNameArabic = branchNameArabic;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getEftCode() {
        return eftCode;
    }

    public void setEftCode(String eftCode) {
        this.eftCode = eftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.branchCode);
        dest.writeString(this.branchName);
        dest.writeString(this.branchNameArabic);
        dest.writeString(this.branchAddress);
        dest.writeString(this.eftCode);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCode);
        dest.writeString(this.isActive);
    }

    protected BranchData(Parcel in) {
        this.id = in.readString();
        this.branchCode = in.readString();
        this.branchName = in.readString();
        this.branchNameArabic = in.readString();
        this.branchAddress = in.readString();
        this.eftCode = in.readString();
        this.bankName = in.readString();
        this.bankCode = in.readString();
        this.isActive = in.readString();
    }

    public static final Creator<BranchData> CREATOR = new Creator<BranchData>() {
        @Override
        public BranchData createFromParcel(Parcel source) {
            return new BranchData(source);
        }

        @Override
        public BranchData[] newArray(int size) {
            return new BranchData[size];
        }
    };
}