package app.alansari.modules.accountmanagement.models;

import com.google.gson.annotations.SerializedName;

public class WUBeneficiaryDybamicFields {

    @SerializedName("FIELD_ID")
    private String fieldId;

    @SerializedName("FIELD_VALUE")
    private String fieldvalue;

    @SerializedName("FIELD_LABEL")
    private String fieldLable;

    @SerializedName("NAME_TYPE")
    private String nametype;

    @SerializedName("FIELD_LIST")
    private String fieldlist;

    @SerializedName("FIELD_VALUE_TYPE")
    private String fieldvaluetype;

    @SerializedName("TXN_TYPE")
    private String txntype;

    @SerializedName("CUST_TYPE")
    private String custtype;

    @SerializedName("IS_BENEF_FIELD")
    private String isbeneffield;

    @SerializedName("MAX_LENGTH")
    private String maxlength;

    @SerializedName("MIN_LENGTH")
    private String minlength;

    @SerializedName("COMPONENT_BLOCK_TYPE")
    private String componentblocktype;

    @SerializedName("MANDATORY")
    private String mandatory;

    @SerializedName("LIST_ID")
    private String listid;

    @SerializedName("IS_TXN_FIELD")
    private String istxnfield;

    @SerializedName("FIELD_NAME")
    private String fieldname;

    @SerializedName("INPUT_TYPE")
    private String inputtype;

    @SerializedName("FIELD_VALUE_CODE")
    private String fieldvaluecode;

    @SerializedName("LENGTH")
    private String length;

    @SerializedName("PREFIX")
    private String prefix;

    @SerializedName("VISIBLE")
    private String isVisible;

    @SerializedName("FIELD_PK_ID")
    private String fieldPkId;

    private String errorMessage;
    private boolean isStatic = false;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldvalue() {
        return fieldvalue;
    }

    public void setFieldvalue(String fieldvalue) {
        this.fieldvalue = fieldvalue;
    }

    public String getFieldLable() {
        return fieldLable;
    }

    public void setFieldLable(String fieldLable) {
        this.fieldLable = fieldLable;
    }

    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }

    public String getFieldlist() {
        return fieldlist;
    }

    public void setFieldlist(String fieldlist) {
        this.fieldlist = fieldlist;
    }

    public String getFieldvaluetype() {
        return fieldvaluetype;
    }

    public void setFieldvaluetype(String fieldvaluetype) {
        this.fieldvaluetype = fieldvaluetype;
    }

    public String getTxntype() {
        return txntype;
    }

    public void setTxntype(String txntype) {
        this.txntype = txntype;
    }

    public String getCusttype() {
        return custtype;
    }

    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

    public String getIsbeneffield() {
        return isbeneffield;
    }

    public void setIsbeneffield(String isbeneffield) {
        this.isbeneffield = isbeneffield;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getMinlength() {
        return minlength;
    }

    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }

    public String getComponentblocktype() {
        return componentblocktype;
    }

    public void setComponentblocktype(String componentblocktype) {
        this.componentblocktype = componentblocktype;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public String getIstxnfield() {
        return istxnfield;
    }

    public void setIstxnfield(String istxnfield) {
        this.istxnfield = istxnfield;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getInputtype() {
        return inputtype;
    }

    public void setInputtype(String inputtype) {
        this.inputtype = inputtype;
    }

    public String getFieldvaluecode() {
        return fieldvaluecode;
    }

    public void setFieldvaluecode(String fieldvaluecode) {
        this.fieldvaluecode = fieldvaluecode;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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

    public String getFieldPkId() {
        return fieldPkId;
    }

    public void setFieldPkId(String fieldPkId) {
        this.fieldPkId = fieldPkId;
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
}