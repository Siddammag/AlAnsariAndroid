package app.alansari.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Parveen Dala on 15 December, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class UserData {
    /**
     * QUICK_SEND : []
     * USER_PK_ID : 1
     * STATUS : VR
     * MOBILE_NUM : 971507852374
     * EXC_USER_NAME : null
     * USER_TYPE : I
     * REF_NUMBER : null
     * CE_MEM_FK_ID : null
     * TOTAL_PENDING_TRANSACTIONS : 10
     * AREX_MEM_FK_ID : 32348
     */

    @SerializedName("USER_PK_ID")
    private String userId;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("MOBILE_NUM")
    private String mobileNum;
    @SerializedName("EXC_USER_NAME")
    private String excUserName;
    @SerializedName("INVOICE_EMAIL")
    private String invoiceEmail;
    @SerializedName("USER_TYPE")
    private String userType;
    @SerializedName("REF_NUMBER")
    private String referenceNum;
    @SerializedName("CE_MEM_FK_ID")
    private String ceMemId;
    @SerializedName("TOTAL_PENDING_TRANSACTIONS")
    private String totalPendingTransactions;
    @SerializedName("AREX_MEM_FK_ID")
    private String arexMemId;
    @SerializedName("QUICK_SEND")
    private List<QuickSendModel> quickSendData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getExcUserName() {
        return excUserName;
    }

    public void setExcUserName(String excUserName) {
        this.excUserName = excUserName;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getReferenceNum() {
        return referenceNum;
    }

    public void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    }

    public String getCeMemId() {
        return ceMemId;
    }

    public void setCeMemId(String ceMemId) {
        this.ceMemId = ceMemId;
    }

    public String getTotalPendingTransactions() {
        return totalPendingTransactions;
    }

    public void setTotalPendingTransactions(String totalPendingTransactions) {
        this.totalPendingTransactions = totalPendingTransactions;
    }

    public String getArexMemId() {
        return arexMemId;
    }

    public void setArexMemId(String arexMemId) {
        this.arexMemId = arexMemId;
    }

    public List<QuickSendModel> getQuickSendData() {
        return quickSendData;
    }

    public void setQuickSendData(List<QuickSendModel> quickSendData) {
        this.quickSendData = quickSendData;
    }
}
