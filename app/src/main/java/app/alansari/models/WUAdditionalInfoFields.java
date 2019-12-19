package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WUAdditionalInfoFields implements Parcelable{

    @SerializedName("FIELD_ID")
    private String fieldId;

    @SerializedName("FIELD_VALUE")
    private String fieldValue;

    @SerializedName("NAME_TYPE")
    private Object nameType;

    @SerializedName("FIELD_LIST")
    private Object fieldList;

    @SerializedName("TXN_TYPE")
    private Object txnType;

    @SerializedName("CUST_TYPE")
    private Object custType;

    @SerializedName("IS_BENEF_FIELD")
    private Object isBenefField;

    @SerializedName("MAX_LENGTH")
    private Object maxLength;

    @SerializedName("MIN_LENGTH")
    private Object minLength;

    @SerializedName("COMPONENT_BLOCK_TYPE")
    private Object componentBlockType;

    @SerializedName("MANDATORY")
    private Object mandatory;

    @SerializedName("FIELD_LABEL")
    private String fieldLable;

    @SerializedName("LIST_ID")
    private Object listId;

    @SerializedName("IS_TXN_FIELD")
    private Object isTxnField;

    @SerializedName("FIELD_NAME")
    private Object fieldName;

    @SerializedName("INPUT_TYPE")
    private Object inputType;

    @SerializedName("FIELD_VALUE_CODE")
    private Object fieldValueCode;


    protected WUAdditionalInfoFields(Parcel in) {
        fieldId = in.readString();
        fieldValue = in.readString();
        fieldLable = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fieldId);
        dest.writeString(fieldValue);
        dest.writeString(fieldLable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WUAdditionalInfoFields> CREATOR = new Creator<WUAdditionalInfoFields>() {
        @Override
        public WUAdditionalInfoFields createFromParcel(Parcel in) {
            return new WUAdditionalInfoFields(in);
        }

        @Override
        public WUAdditionalInfoFields[] newArray(int size) {
            return new WUAdditionalInfoFields[size];
        }
    };

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Object getNameType() {
        return nameType;
    }

    public void setNameType(Object nameType) {
        this.nameType = nameType;
    }

    public Object getFieldList() {
        return fieldList;
    }

    public void setFieldList(Object fieldList) {
        this.fieldList = fieldList;
    }

    public Object getTxnType() {
        return txnType;
    }

    public void setTxnType(Object txnType) {
        this.txnType = txnType;
    }

    public Object getCustType() {
        return custType;
    }

    public void setCustType(Object custType) {
        this.custType = custType;
    }

    public Object getIsBenefField() {
        return isBenefField;
    }

    public void setIsBenefField(Object isBenefField) {
        this.isBenefField = isBenefField;
    }

    public Object getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Object maxLength) {
        this.maxLength = maxLength;
    }

    public Object getMinLength() {
        return minLength;
    }

    public void setMinLength(Object minLength) {
        this.minLength = minLength;
    }

    public Object getComponentBlockType() {
        return componentBlockType;
    }

    public void setComponentBlockType(Object componentBlockType) {
        this.componentBlockType = componentBlockType;
    }

    public Object getMandatory() {
        return mandatory;
    }

    public void setMandatory(Object mandatory) {
        this.mandatory = mandatory;
    }

    public String getFieldLable() {
        return fieldLable;
    }

    public void setFieldLable(String fieldLable) {
        this.fieldLable = fieldLable;
    }

    public Object getListId() {
        return listId;
    }

    public void setListId(Object listId) {
        this.listId = listId;
    }

    public Object getIsTxnField() {
        return isTxnField;
    }

    public void setIsTxnField(Object isTxnField) {
        this.isTxnField = isTxnField;
    }

    public Object getFieldName() {
        return fieldName;
    }

    public void setFieldName(Object fieldName) {
        this.fieldName = fieldName;
    }

    public Object getInputType() {
        return inputType;
    }

    public void setInputType(Object inputType) {
        this.inputType = inputType;
    }

    public Object getFieldValueCode() {
        return fieldValueCode;
    }

    public void setFieldValueCode(Object fieldValueCode) {
        this.fieldValueCode = fieldValueCode;
    }
}