package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 22 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class AAEBankData implements Parcelable {


    public static final Parcelable.Creator<AAEBankData> CREATOR = new Parcelable.Creator<AAEBankData>() {
        @Override
        public AAEBankData createFromParcel(Parcel source) {
            return new AAEBankData(source);
        }

        @Override
        public AAEBankData[] newArray(int size) {
            return new AAEBankData[size];
        }
    };
    /**
     * IS_SAME_BANK : 1
     * ACC_NAME : AL ANSARI EXCHANGE LLC
     * ACTIVE_IND : Y
     * IBAN_NO : AE250240003520958143001
     * ACC_NUM : 003-520-9581430-01
     * AREX_CODE : 9999040001006
     * BANK_PK_ID : 5
     * CREATER_NAME : NOUSHAD
     * CREATED_DATE : 1197570600000
     * MODIFIED_DATE : 1358188200000
     * MODIFIER_NAME : PRAKASH
     * MODIFIED_BY : 100260539
     * BANK_CODE : 802420101
     * BANK_NAME : DUBAI ISLAMIC BANK
     * CREATED_BY : 100260204
     */

    @SerializedName("IS_SAME_BANK")
    private String isSameBank;
    @SerializedName("ACC_NAME")
    private String accountName;
    @SerializedName("ACTIVE_IND")
    private String isActive;
    @SerializedName("IBAN_NO")
    private String IBANNumber;
    @SerializedName("ACC_NUM")
    private String accountNumber;
    @SerializedName("AREX_CODE")
    private String AREXCode;
    @SerializedName("BANK_PK_ID")
    private int bankId;
    @SerializedName("CREATER_NAME")
    private String createrName;
    @SerializedName("CREATED_DATE")
    private long createdDate;
    @SerializedName("MODIFIED_DATE")
    private long modifiedDate;
    @SerializedName("MODIFIER_NAME")
    private String modifierName;
    @SerializedName("MODIFIED_BY")
    private String modifiedBy;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("CREATED_BY")
    private String createdBy;
    @SerializedName("PGS_BANK_CODE")
    private String pgsBankCode;
    @SerializedName("PGS_MESSAGE")
    private String pgsMessage;
    @SerializedName("GTW_PGS_FLAG")
    private String gtwPgsFlag;
    @SerializedName("ALERT_MESSAGE")
    private String alertMessage;
    @SerializedName("ACC_PK_ID")
    private String accPkId;

    public AAEBankData() {
    }

    protected AAEBankData(Parcel in) {
        this.isSameBank = in.readString();
        this.accountName = in.readString();
        this.isActive = in.readString();
        this.IBANNumber = in.readString();
        this.accountNumber = in.readString();
        this.AREXCode = in.readString();
        this.bankId = in.readInt();
        this.createrName = in.readString();
        this.createdDate = in.readLong();
        this.modifiedDate = in.readLong();
        this.modifierName = in.readString();
        this.modifiedBy = in.readString();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.createdBy = in.readString();
        this.pgsBankCode = in.readString();
        this.pgsMessage = in.readString();
        this.gtwPgsFlag = in.readString();
        this.alertMessage = in.readString();
        this.accPkId = in.readString();
    }

    public String getIsSameBank() {
        return isSameBank;
    }

    public void setIsSameBank(String isSameBank) {
        this.isSameBank = isSameBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIBANNumber() {
        return IBANNumber;
    }

    public void setIBANNumber(String IBANNumber) {
        this.IBANNumber = IBANNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAREXCode() {
        return AREXCode;
    }

    public void setAREXCode(String AREXCode) {
        this.AREXCode = AREXCode;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPgsBankCode() {
        return pgsBankCode;
    }

    public void setPgsBankCode(String pgsBankCode) {
        this.pgsBankCode = pgsBankCode;
    }

    public String getPgsMessage() {
        return pgsMessage;
    }

    public void setPgsMessage(String pgsMessage) {
        this.pgsMessage = pgsMessage;
    }

    public String getGtwPgsFlag() {
        return gtwPgsFlag;
    }

    public void setGtwPgsFlag(String gtwPgsFlag) {
        this.gtwPgsFlag = gtwPgsFlag;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getAccPkId() {
        return accPkId;
    }

    public void setAccPkId(String accPkId) {
        this.accPkId = accPkId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isSameBank);
        dest.writeString(this.accountName);
        dest.writeString(this.isActive);
        dest.writeString(this.IBANNumber);
        dest.writeString(this.accountNumber);
        dest.writeString(this.AREXCode);
        dest.writeInt(this.bankId);
        dest.writeString(this.createrName);
        dest.writeLong(this.createdDate);
        dest.writeLong(this.modifiedDate);
        dest.writeString(this.modifierName);
        dest.writeString(this.modifiedBy);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.createdBy);
        dest.writeString(this.pgsBankCode);
        dest.writeString(this.pgsMessage);
        dest.writeString(this.gtwPgsFlag);
        dest.writeString(this.alertMessage);
        dest.writeString(this.accPkId);
    }
}
