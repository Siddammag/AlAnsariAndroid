package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 12 February, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class CreditCardBankData implements Parcelable {

    /**
     * CREDIT_CARD_SETUP_PK_ID : 256
     * BANK_CODE : 9999080000002
     * BANK_NAME : ADIB CC
     * CREDIT_CARD_TYPE : VISA
     */

    @SerializedName("CREDIT_CARD_SETUP_PK_ID")
    private String creditCardSetupPkId;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("CREDIT_CARD_TYPE")
    private String creditCardType;

    public String getCreditCardSetupPkId() {
        return creditCardSetupPkId;
    }

    public void setCreditCardSetupPkId(String creditCardSetupPkId) {
        this.creditCardSetupPkId = creditCardSetupPkId;
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

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.creditCardSetupPkId);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.creditCardType);
    }

    public CreditCardBankData() {
    }

    protected CreditCardBankData(Parcel in) {
        this.creditCardSetupPkId = in.readString();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.creditCardType = in.readString();
    }

    public static final Parcelable.Creator<CreditCardBankData> CREATOR = new Parcelable.Creator<CreditCardBankData>() {
        @Override
        public CreditCardBankData createFromParcel(Parcel source) {
            return new CreditCardBankData(source);
        }

        @Override
        public CreditCardBankData[] newArray(int size) {
            return new CreditCardBankData[size];
        }
    };
}
