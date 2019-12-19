package app.alansari.models.missingparameter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import app.alansari.Utils.Constants;


public class BeneficiaryDynamicFieldsCeNew  implements Parcelable {

    public static final Creator<BeneficiaryDynamicFieldsCeNew> CREATOR = new Creator<BeneficiaryDynamicFieldsCeNew>() {
        @Override
        public BeneficiaryDynamicFieldsCeNew createFromParcel(Parcel source) {
            return new BeneficiaryDynamicFieldsCeNew(source);
        }

        @Override
        public BeneficiaryDynamicFieldsCeNew[] newArray(int size) {
            return new BeneficiaryDynamicFieldsCeNew[size];
        }
    };
    /*@SerializedName("API_TXN_FIELDS_MSTR_PK_ID")
    private String id;*/
    @SerializedName("GTW_BENF_MND_FIELD_PK_ID")
    private String id;
    @SerializedName("FIELD_ID")
    private String fieldId;
    @SerializedName("FIELD")
    private String fieldName;
    @SerializedName("FIELD_VALUE")
    private String fieldValueHtml;
    @SerializedName("TYPE")
    private String fieldType;
    @SerializedName("TXN_TYPE")
    private String txnType;
    @SerializedName("LIST_ID")
    private String listId;
    @SerializedName("COMPONENT_BLOCK_TYPE")
    private String componentBlockType;
    @SerializedName("SM_CASH_COLUMN_NAME")
    private String smCashColumnName;
    @SerializedName("SM_BANK_COLUMN_NAME")
    private String smBankColumnName;
    @SerializedName("RM_CASH_COLUMN_NAME")
    private String rmCashColumnName;
    @SerializedName("KIOSK_ALLOWED")
    private String kioskAllowed;
    @SerializedName("MOBILEAPP_ALLOWED")
    private String mobileAppAllowed;
    @SerializedName("MIN_LENGTH")
    private String minLength;
    @SerializedName("LENGTH")
    private String length;
    @SerializedName("MANDATORY")
    private String isMandatory;
    @SerializedName("INPUT_TYPE")
    private String inputType;
    private String errorMessage;
    @SerializedName("PREFIX")
    private String prefix;
    @SerializedName("VISIBLE")
    private String isVisible;

    @SerializedName("COUNTRY_ID")
    private String countryId;
    @SerializedName("CURRENCY_ID")
    private String currencyId;
    @SerializedName("BANK_ID")
    private String bankId;
    @SerializedName("FIELD_KEY")
    private String field_key;



    public BeneficiaryDynamicFieldsCeNew(String fieldName) {
        this.id = "";
        this.fieldId = "";
        this.fieldName = fieldName;
        this.fieldValueHtml = "";
        this.fieldType = "T";
        this.txnType = "S";
        this.listId = "";
        this.componentBlockType = "";
        this.smCashColumnName = "";
        this.smBankColumnName = "";
        this.rmCashColumnName = "";
        this.kioskAllowed = "1";
        this.mobileAppAllowed = "1";
        this.minLength = "0";
        this.length = "0";
        this.isMandatory = "1";
        this.inputType = Constants.INPUT_TYPE_TEXT;
    }

    public BeneficiaryDynamicFieldsCeNew() {
    }

    protected BeneficiaryDynamicFieldsCeNew(Parcel in) {
        this.id = in.readString();
        this.fieldId = in.readString();
        this.fieldName = in.readString();
        this.fieldValueHtml = in.readString();
        this.fieldType = in.readString();
        this.txnType = in.readString();
        this.listId = in.readString();
        this.componentBlockType = in.readString();
        this.smCashColumnName = in.readString();
        this.smBankColumnName = in.readString();
        this.rmCashColumnName = in.readString();
        this.kioskAllowed = in.readString();
        this.mobileAppAllowed = in.readString();
        this.minLength = in.readString();
        this.length = in.readString();
        this.isMandatory = in.readString();
        this.inputType = in.readString();
        this.errorMessage = in.readString();
        this.prefix=in.readString();
        this.isVisible=in.readString();
        this.bankId=in.readString();
        this.countryId=in.readString();
        this.currencyId=in.readString();
        this.field_key=in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValueHtml() {
        return fieldValueHtml;
    }

    public void setFieldValueHtml(String fieldValueHtml) {
        this.fieldValueHtml = fieldValueHtml;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getComponentBlockType() {
        return componentBlockType;
    }

    public void setComponentBlockType(String componentBlockType) {
        this.componentBlockType = componentBlockType;
    }

    public String getSmCashColumnName() {
        return smCashColumnName;
    }

    public void setSmCashColumnName(String smCashColumnName) {
        this.smCashColumnName = smCashColumnName;
    }

    public String getSmBankColumnName() {
        return smBankColumnName;
    }

    public void setSmBankColumnName(String smBankColumnName) {
        this.smBankColumnName = smBankColumnName;
    }

    public String getRmCashColumnName() {
        return rmCashColumnName;
    }

    public void setRmCashColumnName(String rmCashColumnName) {
        this.rmCashColumnName = rmCashColumnName;
    }

    public String getKioskAllowed() {
        return kioskAllowed;
    }

    public void setKioskAllowed(String kioskAllowed) {
        this.kioskAllowed = kioskAllowed;
    }

    public String getMobileAppAllowed() {
        return mobileAppAllowed;
    }

    public void setMobileAppAllowed(String mobileAppAllowed) {
        this.mobileAppAllowed = mobileAppAllowed;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getField_key() {
        return field_key;
    }

    public void setField_key(String field_key) {
        this.field_key = field_key;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fieldId);
        dest.writeString(this.fieldName);
        dest.writeString(this.fieldValueHtml);
        dest.writeString(this.fieldType);
        dest.writeString(this.txnType);
        dest.writeString(this.listId);
        dest.writeString(this.componentBlockType);
        dest.writeString(this.smCashColumnName);
        dest.writeString(this.smBankColumnName);
        dest.writeString(this.rmCashColumnName);
        dest.writeString(this.kioskAllowed);
        dest.writeString(this.mobileAppAllowed);
        dest.writeString(this.minLength);
        dest.writeString(this.length);
        dest.writeString(this.isMandatory);
        dest.writeString(this.inputType);
        dest.writeString(this.errorMessage);
        dest.writeString(this.prefix);
        dest.writeString(this.isVisible);
        dest.writeString(this.bankId);
        dest.writeString(this.countryId);
        dest.writeString(this.currencyId);
        dest.writeString(this.field_key);
    }
}
