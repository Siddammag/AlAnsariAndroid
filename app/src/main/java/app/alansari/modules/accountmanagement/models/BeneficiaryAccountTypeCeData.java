package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BeneficiaryAccountTypeCeData implements Parcelable {

    public static final Parcelable.Creator<BeneficiaryAccountTypeCeData> CREATOR = new Parcelable.Creator<BeneficiaryAccountTypeCeData>() {
        @Override
        public BeneficiaryAccountTypeCeData createFromParcel(Parcel source) {
            return new BeneficiaryAccountTypeCeData(source);
        }

        @Override
        public BeneficiaryAccountTypeCeData[] newArray(int size) {
            return new BeneficiaryAccountTypeCeData[size];
        }
    };
    /**
     * BANK_CODE : 9999042260028
     * ACC_TYPE_PK_ID : 225
     * ACCOUNT_CODE : SAVINGS
     * ACCOUNT_TYPE : SAVINGS
     * BENEF_BANK_FK_ID : 194
     */

    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("ACC_TYPE_PK_ID")
    private int id;
    @SerializedName("ACCOUNT_CODE")
    private String accountCode;
    @SerializedName("ACCOUNT_TYPE")
    private String accountType;
    @SerializedName("BENEF_BANK_FK_ID")
    private String benfBankId;

    public BeneficiaryAccountTypeCeData() {
    }

    protected BeneficiaryAccountTypeCeData(Parcel in) {
        this.bankCode = in.readString();
        this.id = in.readInt();
        this.accountCode = in.readString();
        this.accountType = in.readString();
        this.benfBankId = in.readString();
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBenfBankId() {
        return benfBankId;
    }

    public void setBenfBankId(String benfBankId) {
        this.benfBankId = benfBankId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankCode);
        dest.writeInt(this.id);
        dest.writeString(this.accountCode);
        dest.writeString(this.accountType);
        dest.writeString(this.benfBankId);
    }
}
