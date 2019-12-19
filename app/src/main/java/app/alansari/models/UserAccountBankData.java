package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 30 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class UserAccountBankData implements Parcelable {

    public static final Parcelable.Creator<UserAccountBankData> CREATOR = new Parcelable.Creator<UserAccountBankData>() {
        @Override
        public UserAccountBankData createFromParcel(Parcel source) {
            return new UserAccountBankData(source);
        }

        @Override
        public UserAccountBankData[] newArray(int size) {
            return new UserAccountBankData[size];
        }
    };
    /**
     * COUNTRY_CODE : 91
     * BANK_MSTR_PK_ID : 3006
     * BANK_NAME_ARABIC :
     * BANK_NAME_ENGLISH : COMMERCIAL BANK OF DUBAI
     * BRANCH_NAME : SOUQ MURSHID DXB
     * BRANCH_CODE : 1101
     * BANK_CODE : 1101040001005
     * ACTIVE_IND : Y
     * BANK_TYPE : N
     * COUNTRY_DESC : UNITED ARAB EMIRATES
     */

    @SerializedName("COUNTRY_CODE")
    private String countryCode;
    @SerializedName("BENEF_BANK_PK_ID")
    private String id;
    @SerializedName("BANK_NAME_AR")
    private String bankNameArabic;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BRANCH_NAME")
    private String branchName;
    @SerializedName("BRANCH_CODE")
    private String branchCode;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("ACTIVE_IND")
    private String isActive;
    @SerializedName("BANK_TYPE")
    private String bankType;
    @SerializedName("COUNTRY_DESC")
    private String countryName;

    public UserAccountBankData() {
    }

    protected UserAccountBankData(Parcel in) {
        this.countryCode = in.readString();
        this.id = in.readString();
        this.bankNameArabic = in.readString();
        this.bankName = in.readString();
        this.branchName = in.readString();
        this.branchCode = in.readString();
        this.bankCode = in.readString();
        this.isActive = in.readString();
        this.bankType = in.readString();
        this.countryName = in.readString();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankNameArabic() {
        return bankNameArabic;
    }

    public void setBankNameArabic(String bankNameArabic) {
        this.bankNameArabic = bankNameArabic;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
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

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryCode);
        dest.writeString(this.id);
        dest.writeString(this.bankNameArabic);
        dest.writeString(this.bankName);
        dest.writeString(this.branchName);
        dest.writeString(this.branchCode);
        dest.writeString(this.bankCode);
        dest.writeString(this.isActive);
        dest.writeString(this.bankType);
        dest.writeString(this.countryName);
    }
}
