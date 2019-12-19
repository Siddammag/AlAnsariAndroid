package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BeneficiaryBranchCeData implements Parcelable {

    public static final Creator<BeneficiaryBranchCeData> CREATOR = new Creator<BeneficiaryBranchCeData>() {
        @Override
        public BeneficiaryBranchCeData createFromParcel(Parcel source) {
            return new BeneficiaryBranchCeData(source);
        }

        @Override
        public BeneficiaryBranchCeData[] newArray(int size) {
            return new BeneficiaryBranchCeData[size];
        }
    };
    /**
     * TRANSFER_TYPE :
     * CHANNEL_ID :
     * CODE : 216
     * ACTIVE_IND : Y
     * NAME : ABS
     * BANK_NAME : AL NOAMAN EXCHANGE
     * BANK_CODE : 062030002
     * ID : 216
     */

    @SerializedName("TRANSFER_TYPE")
    private String transferType;
    @SerializedName("CODE")
    private String code;
    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("NAME")
    private String name;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("ID")
    private int id;
    @SerializedName("BRANCH_ADDRESS")
    private String branchAddress;

    public BeneficiaryBranchCeData() {
    }

    protected BeneficiaryBranchCeData(Parcel in) {
        this.transferType = in.readString();
        this.code = in.readString();
        this.active = in.readString();
        this.name = in.readString();
        this.bankName = in.readString();
        this.bankCode = in.readString();
        this.id = in.readInt();
        this.branchAddress = in.readString();
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transferType);
        dest.writeString(this.code);
        dest.writeString(this.active);
        dest.writeString(this.name);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCode);
        dest.writeInt(this.id);
        dest.writeString(this.branchAddress);
    }
}
