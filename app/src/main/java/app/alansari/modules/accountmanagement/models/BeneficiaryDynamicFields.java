package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import app.alansari.Utils.Constants;

/**
 * Created by Parveen Dala on 03 April, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BeneficiaryDynamicFields implements Parcelable {

    public static final Creator<BeneficiaryDynamicFields> CREATOR = new Creator<BeneficiaryDynamicFields>() {
        @Override
        public BeneficiaryDynamicFields createFromParcel(Parcel source) {
            return new BeneficiaryDynamicFields(source);
        }

        @Override
        public BeneficiaryDynamicFields[] newArray(int size) {
            return new BeneficiaryDynamicFields[size];
        }
    };
    /**
     * GTW_BENF_MND_FIELD_PK_ID : 1
     * COUNTRY_ID : 26
     * CURRENCY_ID : 26
     * BANK_ID : 0
     * COUNTRY : INDIA
     * CURRENCY : INDIAN RUPEE
     * FIELD : IBAN Number
     * TYPE : TEXT
     * MANDATORY : 0
     * VISIBLE : 0
     * PREFIX : 0
     * LENGTH : 0
     */

    @SerializedName("GTW_BENF_MND_FIELD_PK_ID")
    private String id;
    @SerializedName("FIELD_ID")
    private String fieldId;
    @SerializedName("COUNTRY_ID")
    private String countryId;
    @SerializedName("CURRENCY_ID")
    private String currencyId;
    @SerializedName("BANK_ID")
    private String bankId;
    @SerializedName("COUNTRY")
    private String countryName;
    @SerializedName("CURRENCY")
    private String currencyName;
    @SerializedName("FIELD")
    private String fieldName;
    @SerializedName("TYPE")
    private String fieldType;
    @SerializedName("MANDATORY")
    private String isMandatory;
    @SerializedName("VISIBLE")
    private String isVisible;
    @SerializedName("PREFIX")
    private String prefix;
    @SerializedName("MIN_LENGTH")
    private String minLength;
    @SerializedName("LENGTH")
    private String length;
    @SerializedName("INPUT_TYPE")
    private String inputType;
    @SerializedName("FIELD_PK_ID")
    private String fieldPkId;
    @SerializedName("FIELD_VALUE")
    private String fieldValue;
    @SerializedName("FIELD_VALUE_CODE")
    private String fieldValueCode;
    private String errorMessage;

    @SerializedName("FIELD_KEY")
    private String field_key;


    private boolean isStatic = false;

    public String getField_key() {
        return field_key;
    }

    public void setField_key(String field_key) {
        this.field_key = field_key;
    }

    public BeneficiaryDynamicFields(String fieldName, String isVisible, String isMandatory, String fieldType, boolean isStatic) {
        this.id = "";
        this.fieldId = "";
        this.countryId = "";
        this.currencyId = "";
        this.bankId = "";
        this.countryName = "";
        this.currencyName = "";
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.isMandatory = isMandatory;
        this.isVisible = isVisible;

        this.prefix = "";
        this.length = "0";
        this.isStatic = isStatic;
        this.inputType = Constants.INPUT_TYPE_TEXT;
    }

    public BeneficiaryDynamicFields() {
    }

    protected BeneficiaryDynamicFields(Parcel in) {
        this.id = in.readString();
        this.fieldId = in.readString();
        this.countryId = in.readString();
        this.currencyId = in.readString();
        this.bankId = in.readString();
        this.countryName = in.readString();
        this.currencyName = in.readString();
        this.fieldName = in.readString();
        this.fieldType = in.readString();
        this.isMandatory = in.readString();
        this.isVisible = in.readString();
        this.prefix = in.readString();
        this.minLength = in.readString();
        this.length = in.readString();
        this.inputType = in.readString();
        this.fieldPkId = in.readString();
        this.fieldValue = in.readString();
        this.fieldValueCode = in.readString();
        this.errorMessage = in.readString();
        this.field_key=in.readString();
        this.isStatic = in.readByte() != 0;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public String getFieldPkId() {
        return fieldPkId;
    }

    public void setFieldPkId(String fieldPkId) {
        this.fieldPkId = fieldPkId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldValueCode() {
        return fieldValueCode;
    }

    public void setFieldValueCode(String fieldValueCode) {
        this.fieldValueCode = fieldValueCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fieldId);
        dest.writeString(this.countryId);
        dest.writeString(this.currencyId);
        dest.writeString(this.bankId);
        dest.writeString(this.countryName);
        dest.writeString(this.currencyName);
        dest.writeString(this.fieldName);
        dest.writeString(this.fieldType);
        dest.writeString(this.isMandatory);
        dest.writeString(this.isVisible);
        dest.writeString(this.prefix);
        dest.writeString(this.minLength);
        dest.writeString(this.length);
        dest.writeString(this.inputType);
        dest.writeString(this.fieldPkId);
        dest.writeString(this.fieldValue);
        dest.writeString(this.fieldValueCode);
        dest.writeString(this.field_key);
        dest.writeString(this.errorMessage);
        dest.writeByte(this.isStatic ? (byte) 1 : (byte) 0);
    }
}
