package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 02 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BankData implements Parcelable {


    public static final Creator<BankData> CREATOR = new Creator<BankData>() {
        @Override
        public BankData createFromParcel(Parcel source) {
            return new BankData(source);
        }

        @Override
        public BankData[] newArray(int size) {
            return new BankData[size];
        }
    };
    /**
     * COUNTRY_ID : 106
     * BANK_CODE : 1207
     * BANK_NAME : THE KANGRA CENTRAL COOPERATIVE BANK LTD
     * STATUS : 1
     * BANK_NAME_AREX : THE KANGRA CENTRAL COOPERATIVE BANK LTD
     * BANK_CODE_AREX : 1500
     * BANK_NAME_CE : THE KANGRA CENTRAL COOPERATIVE BANK LTD
     * BANK_CODE_CE : 1500
     * GTW_BANK_PK_ID : 21
     * AREX_BT : 1
     * AREX_CP : 0
     * CE_BT : 0
     * CE_CP : 0
     */

    @SerializedName("GTW_BANK_PK_ID")
    private String id;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BANK_NAME_AREX")
    private String bankNameAREX;
    @SerializedName("BANK_CODE_AREX")
    private String bankCodeAREX;
    @SerializedName("BANK_NAME_CE")
    private String bankNameCE;
    @SerializedName("BANK_CODE_CE")
    private String bankCodeCE;
    @SerializedName("COUNTRY_ID")
    private String countryId;
    @SerializedName("IBAN_NUMBER")
    private String IBANNumber;
    private String AREX_BT;
    private String AREX_CP;
    private String CE_BT;
    private String CE_CP;
    @SerializedName("BENEFICIARY_TYPE")
    private int beneficiaryType;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("SERVICE_CP_DESC")
    private String serviceCpDesc;
    @SerializedName("SERVICE_BT_DESC")
    private String serviceBtDesc;
    @SerializedName("REMIT_CP_VALUE")
    private String remitCpValue;
    @SerializedName("REMIT_BT_VALUE")
    private String remitBtValue;
    @SerializedName("REMIT_CP_INSTANT")
    private String remitCpInstant;
    @SerializedName("REMIT_BT_INSTANT")
    private String remitBtInstant;
    @SerializedName("ARABIC_NAME_REQ")
    private String arabicNameReq;

    public BankData() {
    }

    protected BankData(Parcel in) {
        this.id = in.readString();
        this.bankName = in.readString();
        this.bankNameAREX = in.readString();
        this.bankCodeAREX = in.readString();
        this.bankNameCE = in.readString();
        this.bankCodeCE = in.readString();
        this.countryId = in.readString();
        this.IBANNumber = in.readString();
        this.AREX_BT = in.readString();
        this.AREX_CP = in.readString();
        this.CE_BT = in.readString();
        this.CE_CP = in.readString();
        this.beneficiaryType = in.readInt();
        this.status = in.readString();
        this.serviceCpDesc = in.readString();
        this.serviceBtDesc = in.readString();
        this.remitCpValue = in.readString();
        this.remitBtValue = in.readString();
        this.remitCpInstant = in.readString();
        this.remitBtInstant = in.readString();
        this.arabicNameReq = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNameAREX() {
        return bankNameAREX;
    }

    public void setBankNameAREX(String bankNameAREX) {
        this.bankNameAREX = bankNameAREX;
    }

    public String getBankCodeAREX() {
        return bankCodeAREX;
    }

    public void setBankCodeAREX(String bankCodeAREX) {
        this.bankCodeAREX = bankCodeAREX;
    }

    public String getBankNameCE() {
        return bankNameCE;
    }

    public void setBankNameCE(String bankNameCE) {
        this.bankNameCE = bankNameCE;
    }

    public String getBankCodeCE() {
        return bankCodeCE;
    }

    public void setBankCodeCE(String bankCodeCE) {
        this.bankCodeCE = bankCodeCE;
    }

    public String getIBANNumber() {
        return IBANNumber;
    }

    public void setIBANNumber(String IBANNumber) {
        this.IBANNumber = IBANNumber;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getAREX_BT() {
        return AREX_BT;
    }

    public void setAREX_BT(String AREX_BT) {
        this.AREX_BT = AREX_BT;
    }

    public String getAREX_CP() {
        return AREX_CP;
    }

    public void setAREX_CP(String AREX_CP) {
        this.AREX_CP = AREX_CP;
    }

    public String getCE_BT() {
        return CE_BT;
    }

    public void setCE_BT(String CE_BT) {
        this.CE_BT = CE_BT;
    }

    public String getCE_CP() {
        return CE_CP;
    }

    public void setCE_CP(String CE_CP) {
        this.CE_CP = CE_CP;
    }

    public int getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(int beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceCpDesc() {
        return serviceCpDesc;
    }

    public void setServiceCpDesc(String serviceCpDesc) {
        this.serviceCpDesc = serviceCpDesc;
    }

    public String getServiceBtDesc() {
        return serviceBtDesc;
    }

    public void setServiceBtDesc(String serviceBtDesc) {
        this.serviceBtDesc = serviceBtDesc;
    }

    public String getRemitCpValue() {
        return remitCpValue;
    }

    public void setRemitCpValue(String remitCpValue) {
        this.remitCpValue = remitCpValue;
    }

    public String getRemitBtValue() {
        return remitBtValue;
    }

    public void setRemitBtValue(String remitBtValue) {
        this.remitBtValue = remitBtValue;
    }

    public String getRemitCpInstant() {
        return remitCpInstant;
    }

    public void setRemitCpInstant(String remitCpInstant) {
        this.remitCpInstant = remitCpInstant;
    }

    public String getRemitBtInstant() {
        return remitBtInstant;
    }

    public void setRemitBtInstant(String remitBtInstant) {
        this.remitBtInstant = remitBtInstant;
    }

    public String getArabicNameReq() {
        return arabicNameReq;
    }

    public void setArabicNameReq(String arabicNameReq) {
        this.arabicNameReq = arabicNameReq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.bankName);
        dest.writeString(this.bankNameAREX);
        dest.writeString(this.bankCodeAREX);
        dest.writeString(this.bankNameCE);
        dest.writeString(this.bankCodeCE);
        dest.writeString(this.countryId);
        dest.writeString(this.IBANNumber);
        dest.writeString(this.AREX_BT);
        dest.writeString(this.AREX_CP);
        dest.writeString(this.CE_BT);
        dest.writeString(this.CE_CP);
        dest.writeInt(this.beneficiaryType);
        dest.writeString(this.status);
        dest.writeString(this.serviceCpDesc);
        dest.writeString(this.serviceBtDesc);
        dest.writeString(this.remitCpValue);
        dest.writeString(this.remitBtValue);
        dest.writeString(this.remitCpInstant);
        dest.writeString(this.remitBtInstant);
        dest.writeString(this.arabicNameReq);
    }
}
