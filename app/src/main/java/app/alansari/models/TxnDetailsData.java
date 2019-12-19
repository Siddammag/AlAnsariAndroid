package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.alansari.newAdditions.Response;


/**
 * Created by Parveen Dala on 23 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TxnDetailsData implements Parcelable {
    public static final Parcelable.Creator<TxnDetailsData> CREATOR = new Parcelable.Creator<TxnDetailsData>() {
        @Override
        public TxnDetailsData createFromParcel(Parcel source) {
            return new TxnDetailsData(source);
        }

        @Override
        public TxnDetailsData[] newArray(int size) {
            return new TxnDetailsData[size];
        }
    };
    /**
     * REM_TXN_PK_ID : 4
     * GTW_USER_MSTR_FK_ID : 1
     * AREX_MEM_CARD_FK_ID : 30156
     * BENEFICIARY_ID :
     * AREX_MEM_CARD_NUM :
     * CE_MEM_CARD_FK_ID :
     * CE_MEM_CARD_NUM :
     * BRANCH_CODE :
     * REM_TXN_REF_NUM :
     * TXN_CODE : 779477
     * TXN_STATUS : PENDING
     * TXN_REC_TYPE : BT
     * COST_PRICE :
     * TOTAL_TXN_AMT : 5050
     * TOTAL_VALUE_AED :
     * RATE :
     * TXN_PAY_TYPE : PAY AT BRANCH
     * TXN_EXP_LIMIT : 4
     * TXN_EXP_DESC : Expire in 4 Hours
     * TXN_TYPE : DT
     * CREATED_BY :
     * CREATED_DATE : 1485090214000
     * EXP_MODIFIED_BY :
     * EXP_MODIFIED_DATE :
     * MODIFIED_DATE :
     * MODIFIED_BY :
     * BENEFICIARY_DATA : {"TXN_DT_BENF_NO_PK_ID":"4","TXN_DT_SEND_FK_ID":"4","BENF_NAME_EN":"HOLLAND KNIGHT LLP","BENF_NAME_AR":"P kumar","BENF_ACC_NUM":"20250912","BENF_BANK":"WELLS FARGO BANK NA","BENF_BRANCH":"PLAZA BUILDING PA4918","PAYMENT_MODE":"PAY AT BRANCH","DEST_COUNTRY_CODE":"92","DEST_COUNTRY":"UNITED STATES OF AMERICA","BENF_NATIONALITY":"","BENF_NATIONALITY_CODE":"","BENF_CONTACT_NUM":"","ADD_TO_MEM_CARD":"","BENEF_NUM":"","ORIGNAL_TXNO":"","BENF_ADDRESS":""}
     * TRANSACTION_DATA : {"REM_DT_SEND_PK_ID":"4","REM_TXN_FK_ID":"4","TXN_AMOUNT":"90000","CHARGE_AMOUNT":"50","COMM_AMOUNT":"1520","DISC_AMOUNT":"1456","NET_TOTAL":"20141","RATE":"18","TOTAL_VALUE_AED":"5000","ROUNDED_OFF_AMOUNT":"","CCY_CODE":"26","CCY_DESC":"Indian Rupees","SALE_TYPE":"","COST_PRICE":"","BANK_CODE":"0","BANK_NAME":"WELLS FARGO BANK NA","BRANCH_CODE":"0","BRANCH_TYPE":"","BRANCH_NAME":"PLAZA BUILDING PA4918","CREATED_BY":"","CREATED_DATE":"1485090214884","STATUS":""}
     */

    @SerializedName("REM_TXN_PK_ID")
    private String id;
    @SerializedName("GTW_USER_MSTR_FK_ID")
    private String gtwUserMstrFkId;
    @SerializedName("AREX_MEM_CARD_FK_ID")
    private String arexMemCardFkId;
    @SerializedName("BENEFICIARY_ID")
    private String beneficiaryId;
    @SerializedName("AREX_MEM_CARD_NUM")
    private String arexMemCardNum;
    @SerializedName("CE_MEM_CARD_FK_ID")
    private String ceMemCardFkId;
    @SerializedName("CE_MEM_CARD_NUM")
    private String ceMemCardNum;
    @SerializedName("BRANCH_CODE")
    private String branchCode;
    @SerializedName("BRANCH_NAME")
    private String branchName;
    @SerializedName("REM_TXN_REF_NUM")
    private String txnReferenceNum;
    @SerializedName("TXN_CODE")
    private String txnCode;
    @SerializedName("TXN_STATUS")
    private String txnStatus;
    @SerializedName("TXN_REC_TYPE")
    private String txnRecType;
    @SerializedName("COST_PRICE")
    private String costPrice;
    @SerializedName("TOTAL_TXN_AMT")
    private String totalTxnAmount;
    @SerializedName("TOTAL_VALUE_AED")
    private String totalValueAed;
    @SerializedName("RATE")
    private String rate;
    @SerializedName("TXN_PAY_TYPE")
    private String txnPayType;
    @SerializedName("TXN_EXP_LIMIT")
    private String txnExpLimit;
    @SerializedName("TXN_EXP_DESC")
    private String txnExpDesc;
    @SerializedName("TXN_TYPE")
    private String txnType;
    @SerializedName("SERVICE_TYPE")
    private String serviceType;
    @SerializedName("CREATED_BY")
    private String createdBy;
    @SerializedName("CREATED_DATE")
    private String createdDate;
    @SerializedName("CURRENT_TIME")
    private String currentTime;
    @SerializedName("EXP_MODIFIED_BY")
    private String expModifiedBy;
    @SerializedName("EXP_MODIFIED_DATE")
    private String expModifiedDate;
    @SerializedName("MODIFIED_DATE")
    private String modifiedDate;
    @SerializedName("MODIFIED_BY")
    private String modifiedBy;
    @SerializedName("AAE_BANK_NAME")
    private String aaeBankName;
    @SerializedName("REJECTION_MESSAGE")
    private String rejectionMessage;
    @SerializedName("INVOICE_FLAG")
    private String invoiceFlag;
    @SerializedName("AAE_ACCOUNT_NUMBER")
    private String aaeAccountNumber;
    @SerializedName("AAE_IBAN_NUMBER")
    private String aaeIbanNumber;
    @SerializedName("URL")
    private String url;
    @SerializedName("SUCCESS_URL")
    private String successUrl;
    @SerializedName("FAILURE_URL")
    private String failureUrl;
    @SerializedName("BANK_TRANSFER_FLOW")
    private String transferFlow;
    @SerializedName("BENEFICIARY_DATA")
    private BeneficiaryData beneficiaryData;
    @SerializedName("TRANSACTION_DATA")
    private TransactionData transactionData;
    @SerializedName("BENEFICIARY_DATA_TT")
    private BeneficiaryData beneficiaryDataTt;
    @SerializedName("TRANSACTION_DATA_TT")
    private TransactionData transactionDataTt;
    @SerializedName("BENEFICIARY_DATA_C_CE")
    private BeneficiaryData beneficiaryDataTCE;
    @SerializedName("TRANSACTION_DATA_C_CE")
    private TransactionData transactionDataCE;
    @SerializedName("BENEFICIARY_DATA_CC")
    private BeneficiaryData beneficiaryDataTCC;
    /*@SerializedName("TRANSACTION_DATA_C_CC")
    private TransactionData transactionDataCC;*/
    //-------------------------------DKG--------------------------------------
    @SerializedName("TRANSACTION_DATA_CC")
    private TransactionData transactionDataCC;

    //------------------------------------------------------------------------
    @SerializedName("TRANSACTION_DATA_WU")
    private TransactionDataWu transactionDataWu;
    @SerializedName("BENEFICIARY_DATA_WU")
    private BeneficiaryDataWu beneficiaryDataWu;
    //-------------------------------------BT--------------------------------------
    @SerializedName("TRANSACTION_DATA_CE")
    private TransactionData transactionDataBTCE;
    @SerializedName("BENEFICIARY_DATA_CE")
    private BeneficiaryData beneficiaryDataBTCE;

//-----------------------------------------------------------------------------

    @SerializedName("TRANSACTION_HISTORY_DATA_WC")
    private TRANSACTIONHISTORYDATAWC transactionHistroyData;


    @SerializedName("TRANSACTION_HISTORY_DETAIL_WC")
    private List<TRANSACTIONHISTORYDETAILWCItem> tRANSACTIONHISTORYDETAILWC;


    @SerializedName("TRANSACTION_DATA_WC")
    private TRANSACTIONDATAWC tRANSACTIONDATAWC;

    @SerializedName("TRANSACTION_DETAILS_WC")
    private List<TRANSACTIONDETAILSWCItem> tRANSACTIONDETAILSWC;




//----------------------------------------------------------------------------

    @SerializedName("BENF_NAME")
    private String wuBeneficiaryName;
    @SerializedName("BANK_NAME")
    private String wuBankName;
    @SerializedName("FCY_AMT")
    private String wuFcyAmt;
    @SerializedName("CCY_DESC")
    private String wuCcyDesc;
    @SerializedName("AED_AMT")
    private String wuaedAmt;
    @SerializedName("PAYOUT_CITY")
    private String payoutCity;
    @SerializedName("PAYOUT_STATE")
    private String payoutState;
    @SerializedName("MTCN")
    private String mtcn;
    @SerializedName("NET_TOTAL")
    private String netTotal;
    @SerializedName("BENEF_MOBILE_NUMBER")
    private String benefMobileNumber;

    @SerializedName("BENEF_PIC_FILE_EXTN")
    private String benefPicFileExtn;

    public TxnDetailsData() {
    }

    protected TxnDetailsData(Parcel in) {
        this.id = in.readString();
        this.gtwUserMstrFkId = in.readString();
        this.arexMemCardFkId = in.readString();
        this.beneficiaryId = in.readString();
        this.arexMemCardNum = in.readString();
        this.ceMemCardFkId = in.readString();
        this.ceMemCardNum = in.readString();
        this.branchCode = in.readString();
        this.txnReferenceNum = in.readString();
        this.txnCode = in.readString();
        this.txnStatus = in.readString();
        this.txnRecType = in.readString();
        this.costPrice = in.readString();
        this.totalTxnAmount = in.readString();
        this.totalValueAed = in.readString();
        this.rate = in.readString();
        this.txnPayType = in.readString();
        this.txnExpLimit = in.readString();
        this.txnExpDesc = in.readString();
        this.txnType = in.readString();
        this.serviceType = in.readString();
        this.createdBy = in.readString();
        this.createdDate = in.readString();
        this.currentTime = in.readString();
        this.expModifiedBy = in.readString();
        this.expModifiedDate = in.readString();
        this.modifiedDate = in.readString();
        this.modifiedBy = in.readString();
        this.aaeBankName = in.readString();
        this.rejectionMessage = in.readString();
        this.invoiceFlag = in.readString();
        this.aaeAccountNumber = in.readString();
        this.aaeIbanNumber = in.readString();
        this.url = in.readString();
        this.successUrl = in.readString();
        this.failureUrl = in.readString();
        this.transferFlow = in.readString();
        this.beneficiaryData = in.readParcelable(BeneficiaryData.class.getClassLoader());
        this.transactionData = in.readParcelable(TransactionData.class.getClassLoader());
        this.beneficiaryDataTt = in.readParcelable(BeneficiaryData.class.getClassLoader());
        this.transactionDataTt = in.readParcelable(TransactionData.class.getClassLoader());
        this.transactionDataWu = in.readParcelable(TransactionDataWu.class.getClassLoader());
        this.beneficiaryDataWu = in.readParcelable(BeneficiaryDataWu.class.getClassLoader());
        this.transactionDataBTCE = in.readParcelable(TransactionData.class.getClassLoader());
        this.beneficiaryDataBTCE = in.readParcelable(BeneficiaryData.class.getClassLoader());
        this.transactionHistroyData = in.readParcelable(TRANSACTIONHISTORYDATAWC.class.getClassLoader());
        this.tRANSACTIONHISTORYDETAILWC = in.createTypedArrayList(TRANSACTIONHISTORYDETAILWCItem.CREATOR);

        this.tRANSACTIONDATAWC = in.readParcelable(TRANSACTIONDATAWC.class.getClassLoader());
        this.tRANSACTIONDETAILSWC = in.createTypedArrayList(TRANSACTIONDETAILSWCItem.CREATOR);


        this.wuBeneficiaryName = in.readString();
        this.wuBankName = in.readString();
        this.wuFcyAmt = in.readString();
        this.wuCcyDesc = in.readString();
        this.wuaedAmt = in.readString();
        this.payoutState = in.readString();
        this.payoutCity = in.readString();
        this.branchName = in.readString();
        this.mtcn = in.readString();
        this.netTotal = in.readString();
        this.benefMobileNumber = in.readString();
        this.benefPicFileExtn=in.readString();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPayoutCity() {
        return payoutCity;
    }

    public void setPayoutCity(String payoutCity) {
        this.payoutCity = payoutCity;
    }

    public String getPayoutState() {
        return payoutState;
    }

    public void setPayoutState(String payoutState) {
        this.payoutState = payoutState;
    }

    public String getWuaedAmt() {
        return wuaedAmt;
    }

    public void setWuaedAmt(String wuaedAmt) {
        this.wuaedAmt = wuaedAmt;
    }

    public String getWuCcyDesc() {
        return wuCcyDesc;
    }

    public void setWuCcyDesc(String wuCcyDesc) {
        this.wuCcyDesc = wuCcyDesc;
    }

    public String getWuFcyAmt() {
        return wuFcyAmt;
    }

    public void setWuFcyAmt(String wuFcyAmt) {
        this.wuFcyAmt = wuFcyAmt;
    }

    public String getWuBankName() {
        return wuBankName;
    }

    public void setWuBankName(String wuBankName) {
        this.wuBankName = wuBankName;
    }

    public String getWuBeneficiaryName() {
        return wuBeneficiaryName;
    }



    public List<TRANSACTIONHISTORYDETAILWCItem> gettRANSACTIONHISTORYDETAILWC() {
        return tRANSACTIONHISTORYDETAILWC;
    }

    public void settRANSACTIONHISTORYDETAILWC(List<TRANSACTIONHISTORYDETAILWCItem> tRANSACTIONHISTORYDETAILWC) {
        this.tRANSACTIONHISTORYDETAILWC = tRANSACTIONHISTORYDETAILWC;
    }


    public TRANSACTIONDATAWC gettRANSACTIONDATAWC() {
        return tRANSACTIONDATAWC;
    }

    public void settRANSACTIONDATAWC(TRANSACTIONDATAWC tRANSACTIONDATAWC) {
        this.tRANSACTIONDATAWC = tRANSACTIONDATAWC;
    }

    public List<TRANSACTIONDETAILSWCItem> gettRANSACTIONDETAILSWC() {
        return tRANSACTIONDETAILSWC;
    }

    public void settRANSACTIONDETAILSWC(List<TRANSACTIONDETAILSWCItem> tRANSACTIONDETAILSWC) {
        this.tRANSACTIONDETAILSWC = tRANSACTIONDETAILSWC;
    }

    public void setWuBeneficiaryName(String wuBeneficiaryName) {
        this.wuBeneficiaryName = wuBeneficiaryName;

    }

    public String getMtcn() {
        return mtcn;
    }

    public void setMtcn(String mtcn) {
        this.mtcn = mtcn;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getBenefMobileNumber() {
        return benefMobileNumber;
    }

    public void setBenefMobileNumber(String benefMobileNumber) {
        this.benefMobileNumber = benefMobileNumber;
    }

    public String getBenefPicFileExtn() {
        return benefPicFileExtn;
    }

    public void setBenefPicFileExtn(String benefPicFileExtn) {
        this.benefPicFileExtn = benefPicFileExtn;
    }

    public TransactionDataWu getTransactionDataWu() {
        return transactionDataWu;
    }

    public void setTransactionDataWu(TransactionDataWu transactionDataWu) {
        this.transactionDataWu = transactionDataWu;
    }


    public TRANSACTIONHISTORYDATAWC getTransactionHistroyData() {
        return transactionHistroyData;
    }

    public void setTransactionHistroyData(TRANSACTIONHISTORYDATAWC transactionHistroyData) {
        this.transactionHistroyData = transactionHistroyData;
    }





    public void setTRANSACTIONHISTORYDETAILWC(List<TRANSACTIONHISTORYDETAILWCItem> tRANSACTIONHISTORYDETAILWC) {
        this.tRANSACTIONHISTORYDETAILWC = tRANSACTIONHISTORYDETAILWC;
    }

    public List<TRANSACTIONHISTORYDETAILWCItem> getTRANSACTIONHISTORYDETAILWC() {
        return tRANSACTIONHISTORYDETAILWC;
    }


    public BeneficiaryDataWu getBeneficiaryDataWu() {
        return beneficiaryDataWu;
    }

    public void setBeneficiaryDataWu(BeneficiaryDataWu beneficiaryDataWu) {
        this.beneficiaryDataWu = beneficiaryDataWu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGtwUserMstrFkId() {
        return gtwUserMstrFkId;
    }

    public void setGtwUserMstrFkId(String gtwUserMstrFkId) {
        this.gtwUserMstrFkId = gtwUserMstrFkId;
    }

    public String getArexMemCardFkId() {
        return arexMemCardFkId;
    }

    public void setArexMemCardFkId(String arexMemCardFkId) {
        this.arexMemCardFkId = arexMemCardFkId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getArexMemCardNum() {
        return arexMemCardNum;
    }

    public void setArexMemCardNum(String arexMemCardNum) {
        this.arexMemCardNum = arexMemCardNum;
    }

    public String getCeMemCardFkId() {
        return ceMemCardFkId;
    }

    public void setCeMemCardFkId(String ceMemCardFkId) {
        this.ceMemCardFkId = ceMemCardFkId;
    }

    public String getCeMemCardNum() {
        return ceMemCardNum;
    }

    public void setCeMemCardNum(String ceMemCardNum) {
        this.ceMemCardNum = ceMemCardNum;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getTxnReferenceNum() {
        return txnReferenceNum;
    }

    public void setTxnReferenceNum(String txnReferenceNum) {
        this.txnReferenceNum = txnReferenceNum;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getTxnRecType() {
        return txnRecType;
    }

    public void setTxnRecType(String txnRecType) {
        this.txnRecType = txnRecType;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getTotalTxnAmount() {
        return totalTxnAmount;
    }

    public void setTotalTxnAmount(String totalTxnAmount) {
        this.totalTxnAmount = totalTxnAmount;
    }

    public String getTotalValueAed() {
        return totalValueAed;
    }

    public void setTotalValueAed(String totalValueAed) {
        this.totalValueAed = totalValueAed;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTxnPayType() {
        return txnPayType;
    }

    public void setTxnPayType(String txnPayType) {
        this.txnPayType = txnPayType;
    }

    public String getTxnExpLimit() {
        return txnExpLimit;
    }

    public void setTxnExpLimit(String txnExpLimit) {
        this.txnExpLimit = txnExpLimit;
    }

    public String getTxnExpDesc() {
        return txnExpDesc;
    }

    public void setTxnExpDesc(String txnExpDesc) {
        this.txnExpDesc = txnExpDesc;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getExpModifiedBy() {
        return expModifiedBy;
    }

    public void setExpModifiedBy(String expModifiedBy) {
        this.expModifiedBy = expModifiedBy;
    }

    public String getExpModifiedDate() {
        return expModifiedDate;
    }

    public void setExpModifiedDate(String expModifiedDate) {
        this.expModifiedDate = expModifiedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAaeBankName() {
        return aaeBankName;
    }

    public void setAaeBankName(String aaeBankName) {
        this.aaeBankName = aaeBankName;
    }

    public String getRejectionMessage() {
        return rejectionMessage;
    }

    public void setRejectionMessage(String rejectionMessage) {
        this.rejectionMessage = rejectionMessage;
    }

    public String getAaeAccountNumber() {
        return aaeAccountNumber;
    }

    public void setAaeAccountNumber(String aaeAccountNumber) {
        this.aaeAccountNumber = aaeAccountNumber;
    }

    public String getAaeIbanNumber() {
        return aaeIbanNumber;
    }

    public void setAaeIbanNumber(String aaeIbanNumber) {
        this.aaeIbanNumber = aaeIbanNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String Url) {
        this.url = Url;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getTransferFlow() {
        return transferFlow;
    }

    public void setTransferFlow(String transferFlow) {
        this.transferFlow = transferFlow;
    }

    public String getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(String invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public BeneficiaryData getBeneficiaryData() {
        return beneficiaryData;
    }

    public void setBeneficiaryData(BeneficiaryData beneficiaryData) {
        this.beneficiaryData = beneficiaryData;
    }

    public TransactionData getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    public BeneficiaryData getBeneficiaryDataTt() {
        return beneficiaryDataTt;
    }

    public void setBeneficiaryDataTt(BeneficiaryData beneficiaryDataTt) {
        this.beneficiaryDataTt = beneficiaryDataTt;
    }


    public TransactionData getTransactionDataTt() {
        return transactionDataTt;
    }

    public void setTransactionDataTt(TransactionData transactionDataTt) {
        this.transactionDataTt = transactionDataTt;
    }


    //----------------------------------------------------------------------------------------------

    public BeneficiaryData getBeneficiaryDataBTCE() {
        return beneficiaryDataBTCE;
    }

    public void setBeneficiaryDataBTCE(BeneficiaryData beneficiaryDataBTCE) {
        this.beneficiaryDataBTCE = beneficiaryDataBTCE;
    }


    public TransactionData getTransactionDataBTCE() {
        return transactionDataBTCE;
    }

    public void setTransactionDataBTCE(TransactionData transactionDataBTCE) {
        this.transactionDataBTCE = transactionDataBTCE;
    }


    //----------------------------------------------------------------------------------------------


    public BeneficiaryData getBeneficiaryDataTCE() {
        return beneficiaryDataTCE;
    }

    public void setBeneficiaryDataTCE(BeneficiaryData beneficiaryDataTCE) {
        this.beneficiaryDataTCE = beneficiaryDataTCE;
    }

    public TransactionData getTransactionDataCE() {
        return transactionDataCE;
    }

    public void setTransactionDataCE(TransactionData transactionDataCE) {
        this.transactionDataCE = transactionDataCE;
    }

    public BeneficiaryData getBeneficiaryDataTCC() {
        return beneficiaryDataTCC;
    }

    public void setBeneficiaryDataTCC(BeneficiaryData beneficiaryDataTCC) {
        this.beneficiaryDataTCC = beneficiaryDataTCC;
    }

    public TransactionData getTransactionDataCC() {
        return transactionDataCC;
    }

    public void setTransactionDataCC(TransactionData transactionDataCC) {
        this.transactionDataCC = transactionDataCC;
    }

    @Override
    public String toString() {
        return "TxnDetailsData{" +
                "transferFlow='" + transferFlow + '\'' +
                '}';
    }

    public static class BeneficiaryData implements Parcelable {

        public static final Creator<BeneficiaryData> CREATOR = new Creator<BeneficiaryData>() {
            @Override
            public BeneficiaryData createFromParcel(Parcel source) {
                return new BeneficiaryData(source);
            }

            @Override
            public BeneficiaryData[] newArray(int size) {
                return new BeneficiaryData[size];
            }
        };
        /**
         * TXN_DT_BENF_NO_PK_ID : 4
         * TXN_DT_SEND_FK_ID : 4
         * BENF_NAME_EN : HOLLAND KNIGHT LLP
         * BENF_NAME_AR : P kumar
         * BENF_ACC_NUM : 20250912
         * BENF_BANK : WELLS FARGO BANK NA
         * BENF_BRANCH : PLAZA BUILDING PA4918
         * PAYMENT_MODE : PAY AT BRANCH
         * DEST_COUNTRY_CODE : 92
         * DEST_COUNTRY : UNITED STATES OF AMERICA
         * BENF_NATIONALITY :
         * BENF_NATIONALITY_CODE :
         * BENF_CONTACT_NUM :
         * ADD_TO_MEM_CARD :
         * BENEF_NUM :
         * ORIGNAL_TXNO :
         * BENF_ADDRESS :
         */

        @SerializedName("TXN_DT_BENF_NO_PK_ID")
        private String id;
        @SerializedName("TXN_DT_SEND_FK_ID")
        private String remDtSendFkId;
        @SerializedName("TT_BENF_NO_PK_ID")
        private String idTt;
        @SerializedName("TT_SALE_TXN_FK_ID")
        private String remDtSendFkIdTt;
        @SerializedName("BENF_NAME_EN")
        private String beneficiaryName;
        @SerializedName("BENF_NAME_AR")
        private String BeneficiaryNameAr;
        @SerializedName("BENF_ACC_NUM")
        private String accountNumber;
        @SerializedName("BANK_NAME")
        private String bankName;
        @SerializedName("BRANCH_NAME")
        private String branchName;
        @SerializedName("PAYMENT_MODE")
        private String payMode;
        @SerializedName("DEST_COUNTRY_CODE")
        private String countryCode;
        @SerializedName("DEST_COUNTRY")
        private String countryName;
        @SerializedName("BENF_NATIONALITY")
        private String nationality;
        @SerializedName("BENF_NATIONALITY_CODE")
        private String nationalityCode;
        @SerializedName("BENF_CONTACT_NUM")
        private String mobileNumber;
        @SerializedName("ADD_TO_MEM_CARD")
        private String addToMemCard;
        @SerializedName("BENEF_NUM")
        private String beneficiaryNumber;
        @SerializedName("ORIGNAL_TXNO")
        private String originalTxnNumber;
        @SerializedName("BENF_ADDRESS")
        private String address;
        @SerializedName("RECEIVER_FULL_NAME")
        private String full_name;

//---------------------------------------------DKG--------------------------------------------------

        @SerializedName("CARD_HOLDER_NAME")
        private String card_holder_name;
        @SerializedName("CCY_DESC")
        private String ccy_desc;
        @SerializedName("CARD_TYPE")
        private String card_type;
        @SerializedName("CARD_NUM")
        private String card_number;
        @SerializedName("REC_MOBILENO")
        private String mobile_number;
        @SerializedName("BEN_ACC_NO")
        private String bene_account_no;
        @SerializedName("BEN_BRANCH_NAME")
        private String bene_branch_name;


//--------------------------------------------------------------------------------------------------


        public BeneficiaryData() {
        }

        protected BeneficiaryData(Parcel in) {
            this.id = in.readString();
            this.remDtSendFkId = in.readString();
            this.idTt = in.readString();
            this.remDtSendFkIdTt = in.readString();
            this.beneficiaryName = in.readString();
            this.BeneficiaryNameAr = in.readString();
            this.accountNumber = in.readString();
            this.bankName = in.readString();
            this.branchName = in.readString();
            this.payMode = in.readString();
            this.countryCode = in.readString();
            this.countryName = in.readString();
            this.nationality = in.readString();
            this.nationalityCode = in.readString();
            this.mobileNumber = in.readString();
            this.addToMemCard = in.readString();
            this.beneficiaryNumber = in.readString();
            this.originalTxnNumber = in.readString();
            this.address = in.readString();
            this.full_name = in.readString();
            this.card_holder_name = in.readString();
            this.ccy_desc = in.readString();
            this.card_type = in.readString();
            this.card_number = in.readString();
            this.mobile_number = in.readString();
            this.bene_account_no = in.readString();
            this.bene_branch_name = in.readString();

        }

        public String getBene_branch_name() {
            return bene_branch_name;
        }

        public void setBene_branch_name(String bene_branch_name) {
            this.bene_branch_name = bene_branch_name;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getCard_holder_name() {
            return card_holder_name;
        }

        public void setCard_holder_name(String card_holder_name) {
            this.card_holder_name = card_holder_name;
        }

        public String getCcy_desc() {
            return ccy_desc;
        }

        public void setCcy_desc(String ccy_desc) {
            this.ccy_desc = ccy_desc;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getBene_account_no() {
            return bene_account_no;
        }

        public void setBene_account_no(String bene_account_no) {
            this.bene_account_no = bene_account_no;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getId() {
            return id != null ? id : idTt;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemDtSendFkId() {
            return remDtSendFkId != null ? remDtSendFkId : remDtSendFkIdTt;
        }

        public void setRemDtSendFkId(String remDtSendFkId) {
            this.remDtSendFkId = remDtSendFkId;
        }

        public String getIdTt() {
            return idTt;
        }

        public void setIdTt(String idTt) {
            this.idTt = idTt;
        }

        public String getRemDtSendFkIdTt() {
            return remDtSendFkIdTt;
        }

        public void setRemDtSendFkIdTt(String remDtSendFkIdTt) {
            this.remDtSendFkIdTt = remDtSendFkIdTt;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public void setBeneficiaryName(String beneficiaryName) {
            this.beneficiaryName = beneficiaryName;
        }

        public String getBeneficiaryNameAr() {
            return BeneficiaryNameAr;
        }

        public void setBeneficiaryNameAr(String BeneficiaryNameAr) {
            this.BeneficiaryNameAr = BeneficiaryNameAr;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getPayMode() {
            return payMode;
        }

        public void setPayMode(String payMode) {
            this.payMode = payMode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getNationalityCode() {
            return nationalityCode;
        }

        public void setNationalityCode(String nationalityCode) {
            this.nationalityCode = nationalityCode;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getAddToMemCard() {
            return addToMemCard;
        }

        public void setAddToMemCard(String addToMemCard) {
            this.addToMemCard = addToMemCard;
        }

        public String getBeneficiaryNumber() {
            return beneficiaryNumber;
        }

        public void setBeneficiaryNumber(String beneficiaryNumber) {
            this.beneficiaryNumber = beneficiaryNumber;
        }

        public String getOriginalTxnNumber() {
            return originalTxnNumber;
        }

        public void setOriginalTxnNumber(String originalTxnNumber) {
            this.originalTxnNumber = originalTxnNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.remDtSendFkId);
            dest.writeString(this.idTt);
            dest.writeString(this.remDtSendFkIdTt);
            dest.writeString(this.beneficiaryName);
            dest.writeString(this.BeneficiaryNameAr);
            dest.writeString(this.accountNumber);
            dest.writeString(this.bankName);
            dest.writeString(this.branchName);
            dest.writeString(this.payMode);
            dest.writeString(this.countryCode);
            dest.writeString(this.countryName);
            dest.writeString(this.nationality);
            dest.writeString(this.nationalityCode);
            dest.writeString(this.mobileNumber);
            dest.writeString(this.addToMemCard);
            dest.writeString(this.beneficiaryNumber);
            dest.writeString(this.originalTxnNumber);
            dest.writeString(this.address);
            dest.writeString(this.full_name);
            dest.writeString(this.card_holder_name);
            dest.writeString(this.ccy_desc);
            dest.writeString(this.card_type);
            dest.writeString(this.card_number);
            dest.writeString(this.mobile_number);
            dest.writeString(this.bene_account_no);
            dest.writeString(this.bene_branch_name);

        }


    }

    public static class TransactionData implements Parcelable {

        public static final Creator<TransactionData> CREATOR = new Creator<TransactionData>() {
            @Override
            public TransactionData createFromParcel(Parcel source) {
                return new TransactionData(source);
            }

            @Override
            public TransactionData[] newArray(int size) {
                return new TransactionData[size];
            }
        };
        /**
         * REM_DT_SEND_PK_ID : 4
         * REM_TXN_FK_ID : 4
         * TXN_AMOUNT : 90000
         * CHARGE_AMOUNT : 50
         * COMM_AMOUNT : 1520
         * DISC_AMOUNT : 1456
         * NET_TOTAL : 20141
         * RATE : 18
         * TOTAL_VALUE_AED : 5000
         * ROUNDED_OFF_AMOUNT :
         * CCY_CODE : 26
         * CCY_DESC : Indian Rupees
         * SALE_TYPE :
         * COST_PRICE :
         * BANK_CODE : 0
         * BANK_NAME : WELLS FARGO BANK NA
         * BRANCH_CODE : 0
         * BRANCH_TYPE :
         * BRANCH_NAME : PLAZA BUILDING PA4918
         * CREATED_BY :
         * CREATED_DATE : 1485090214884
         * STATUS :
         * DOC_NUMBER:
         */


        @SerializedName("REM_DT_SEND_PK_ID")
        private String remDtSendPkId;
        @SerializedName("REM_TT_SALE_PK_ID")
        private String remDtSendPkIdTt;
        @SerializedName("REM_TXN_FK_ID")
        private String remTxnFkId;
        @SerializedName("TXN_AMT")
        private String txnAmount;
        @SerializedName("CHARGE_AMOUNT")
        private String chargeAmount;
        @SerializedName("COMM_AMOUNT")
        private String commAmount;
        @SerializedName("DISC_AMOUNT")
        private String discAmount;
        @SerializedName("NET_TOTAL")
        private String netTotal;
        @SerializedName("RATE")
        private String rate;
        @SerializedName("TOTAL_VALUE_AED")
        private String totalValueAed;
        @SerializedName("ROUNDED_OFF_AMOUNT")
        private String roundedOffAmount;
        @SerializedName("CCY_CODE")
        private String ccyCode;
        @SerializedName("CCY_DESC")
        private String ccyName;

        @SerializedName("SALE_TYPE")
        private String saleType;
        @SerializedName("COST_PRICE")
        private String costPrice;
        @SerializedName("BANK_CODE")
        private String bankCode;
        @SerializedName("BANK_NAME")
        private String bankName;
        @SerializedName("BRANCH_CODE")
        private String branchCode;
        @SerializedName("BRANCH_TYPE")
        private String branchType;
        @SerializedName("BRANCH_NAME")
        private String branchName;
        @SerializedName("CREATED_BY")
        private String createdBy;
        @SerializedName("CREATED_DATE")
        private String createdDate;
        @SerializedName("STATUS")
        private String status;
        @SerializedName("PAYOUT_AGENT_NAME")
        private String agent_name;
        @SerializedName("SEND_AMOUNT")
        private String send_amount;

        @SerializedName("ROUTING_BANK_NAME")
        private String routine_bank_name;

        @SerializedName("BEN_BANK_NAME")
        private String benBankName;

        @SerializedName("DOC_NUMBER")
        private String doc_number;

        public TransactionData() {
        }

        protected TransactionData(Parcel in) {
            this.remDtSendPkId = in.readString();
            this.remDtSendPkIdTt = in.readString();
            this.remTxnFkId = in.readString();
            this.txnAmount = in.readString();
            this.chargeAmount = in.readString();
            this.commAmount = in.readString();
            this.discAmount = in.readString();
            this.netTotal = in.readString();
            this.rate = in.readString();
            this.totalValueAed = in.readString();
            this.roundedOffAmount = in.readString();
            this.ccyCode = in.readString();
            this.ccyName = in.readString();
            this.saleType = in.readString();
            this.costPrice = in.readString();
            this.bankCode = in.readString();
            this.bankName = in.readString();
            this.branchCode = in.readString();
            this.branchType = in.readString();
            this.branchName = in.readString();
            this.createdBy = in.readString();
            this.createdDate = in.readString();
            this.status = in.readString();
            this.agent_name = in.readString();
            this.send_amount = in.readString();
            this.routine_bank_name = in.readString();
            this.doc_number = in.readString();
            this.benBankName=in.readString();
        }

        public String getRoutine_bank_name() {
            return routine_bank_name;
        }

        public void setRoutine_bank_name(String routine_bank_name) {
            this.routine_bank_name = routine_bank_name;
        }

        public String getSend_amount() {
            return send_amount;
        }

        public void setSend_amount(String send_amount) {
            this.send_amount = send_amount;
        }

        public String getAgent_name() {
            return agent_name;
        }

        public void setAgent_name(String agent_name) {
            this.agent_name = agent_name;
        }

        public String getRemDtSendPkId() {
            return remDtSendPkId != null ? remDtSendPkId : remDtSendPkIdTt;
        }

        public void setRemDtSendPkId(String remDtSendPkId) {
            this.remDtSendPkId = remDtSendPkId;
        }

        public String getRemDtSendFkIdTt() {
            return remDtSendPkIdTt;
        }

        public void setRemDtSendFkIdTt(String remDtSendPkIdTt) {
            this.remDtSendPkIdTt = remDtSendPkIdTt;
        }

        public String getRemTxnFkId() {
            return remTxnFkId;
        }

        public void setRemTxnFkId(String remTxnFkId) {
            this.remTxnFkId = remTxnFkId;
        }

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public String getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(String chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public String getCommAmount() {
            return commAmount;
        }

        public void setCommAmount(String commAmount) {
            this.commAmount = commAmount;
        }

        public String getDiscAmount() {
            return discAmount;
        }

        public void setDiscAmount(String discAmount) {
            this.discAmount = discAmount;
        }

        public String getNetTotal() {
            return netTotal;
        }

        public void setNetTotal(String netTotal) {
            this.netTotal = netTotal;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTotalValueAed() {
            return totalValueAed;
        }

        public void setTotalValueAed(String totalValueAed) {
            this.totalValueAed = totalValueAed;
        }

        public String getRoundedOffAmount() {
            return roundedOffAmount;
        }

        public void setRoundedOffAmount(String roundedOffAmount) {
            this.roundedOffAmount = roundedOffAmount;
        }

        public String getCcyCode() {
            return ccyCode;
        }

        public void setCcyCode(String ccyCode) {
            this.ccyCode = ccyCode;
        }

        public String getCcyName() {
            return ccyName;
        }

        public void setCcyName(String ccyName) {
            this.ccyName = ccyName;
        }

        public String getSaleType() {
            return saleType;
        }

        public void setSaleType(String saleType) {
            this.saleType = saleType;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
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

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getBranchType() {
            return branchType;
        }

        public void setBranchType(String branchType) {
            this.branchType = branchType;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDoc_number() {
            return doc_number;
        }

        public void setDoc_number(String doc_number) {
            this.doc_number = doc_number;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public String getBenBankName() {
            return benBankName;
        }

        public void setBenBankName(String benBankName) {
            this.benBankName = benBankName;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.remDtSendPkId);
            dest.writeString(this.remDtSendPkIdTt);
            dest.writeString(this.remTxnFkId);
            dest.writeString(this.txnAmount);
            dest.writeString(this.chargeAmount);
            dest.writeString(this.commAmount);
            dest.writeString(this.discAmount);
            dest.writeString(this.netTotal);
            dest.writeString(this.rate);
            dest.writeString(this.totalValueAed);
            dest.writeString(this.roundedOffAmount);
            dest.writeString(this.ccyCode);
            dest.writeString(this.ccyName);
            dest.writeString(this.saleType);
            dest.writeString(this.costPrice);
            dest.writeString(this.bankCode);
            dest.writeString(this.bankName);
            dest.writeString(this.branchCode);
            dest.writeString(this.branchType);
            dest.writeString(this.branchName);
            dest.writeString(this.createdBy);
            dest.writeString(this.createdDate);
            dest.writeString(this.status);
            dest.writeString(this.agent_name);
            dest.writeString(this.send_amount);
            dest.writeString(this.routine_bank_name);
            dest.writeString(this.doc_number);
            dest.writeString(this.benBankName);
        }


    }

    public static class TransactionDataWu implements Parcelable {
        public static final Creator<TransactionDataWu> CREATOR = new Creator<TransactionDataWu>() {
            @Override
            public TransactionDataWu createFromParcel(Parcel in) {
                return new TransactionDataWu(in);
            }

            @Override
            public TransactionDataWu[] newArray(int size) {
                return new TransactionDataWu[size];
            }
        };
        private String wuTaxRate;
        private String aaeCreatedDate;
        private String modifiedBy;
        private String additionalServiceCharges;
        private String deliveryCharges;
        private String remTxnFkId;
        private String wuTerminalId;
        private String mtcn;
        private String aaeTillNumber;
        private String promoReqMsg;
        private String messageFee;
        private String sourceCountryDesc;
        private String promoMessage;
        private String vatCharges;
        private String deliveryOptionDesc;
        private String testAnswer;
        private String ansariCharges;
        private String ansariDrawMessage;
        private String amlStatus;
        private String snpIndicator;
        private String deliveryServiceTypeCd;
        private String aeeMemCardNo;
        private String commission;
        private String totalAedAmount;
        private String sourceCountryCode;
        private String currencyDesc;
        private String charges;
        private String wuCancelFlag;
        private String status;
        private String feeEnqType;
        private String accGenerated;
        private String amountInAeed;
        private String aaeCreatedBy;
        private String aeeBranchName;
        private String wuAccNumberOfBranch;
        private String testQuestion;
        private String totalPointsEarned;
        private String termsAndConditions;
        private String destCountryCode;
        private String discount;
        private String docNum;
        private String sendFcyCode;
        private String aaeCreaterName;
        private String totalAedAmountWu;
        private String storeType;
        private String wuCurrencyCode;
        private String txnMode;
        private String deliveryOption;
        private String wuLocationId;
        private String isTestQAvailable;
        private String modifierName;
        private String wuBranchOfTxn;
        private String pointsEarned;
        private String aaeReferenceNo;
        private String promoCodeDesc;
        private String remarks;
        private String stagingBuffer;
        private String cashReceived;
        private String rate;
        private String mediumCcy;
        private String wuEnquiryFlag;
        private String drawScreenMessage;
        private String wuMobileNumber;
        private String wuMemCardNo;
        private String vatDiscount;
        private String senderMessage;
        private String wuCountryCode;
        private String wuTaxWorkSheet;
        private String promoCode;
        private String smExtCashPkId;
        private String ipAddress;
        private String txnType;
        private String wuCreatedBy;
        private String deliveryServiceTypeDesc;
        private String currencyCode;
        private String aaeBranchCode;
        private String balanceToPay;
        private String roundedOffAmt;
        private String roundingOff;
        private String filingDate;
        private String mtcn16;
        private String wuEnrollFlag;
        private String modifiedDate;
        private String promoDiscount;
        private String schCancelFlag;
        private String sendAmount;
        private String filingTime;
        private String wuLookupPromoCode;
        private String destCountryDesc;
        private String totalTxnAmount;
        private String channel;
        private String promoResMsg;

        protected TransactionDataWu(Parcel in) {
            wuTaxRate = in.readString();
            aaeCreatedDate = in.readString();
            modifiedBy = in.readString();
            additionalServiceCharges = in.readString();
            deliveryCharges = in.readString();
            remTxnFkId = in.readString();
            wuTerminalId = in.readString();
            mtcn = in.readString();
            aaeTillNumber = in.readString();
            promoReqMsg = in.readString();
            messageFee = in.readString();
            sourceCountryDesc = in.readString();
            promoMessage = in.readString();
            vatCharges = in.readString();
            deliveryOptionDesc = in.readString();
            testAnswer = in.readString();
            ansariCharges = in.readString();
            ansariDrawMessage = in.readString();
            amlStatus = in.readString();
            snpIndicator = in.readString();
            deliveryServiceTypeCd = in.readString();
            aeeMemCardNo = in.readString();
            commission = in.readString();
            totalAedAmount = in.readString();
            sourceCountryCode = in.readString();
            currencyDesc = in.readString();
            charges = in.readString();
            wuCancelFlag = in.readString();
            status = in.readString();
            feeEnqType = in.readString();
            accGenerated = in.readString();
            amountInAeed = in.readString();
            aaeCreatedBy = in.readString();
            aeeBranchName = in.readString();
            wuAccNumberOfBranch = in.readString();
            testQuestion = in.readString();
            totalPointsEarned = in.readString();
            termsAndConditions = in.readString();
            destCountryCode = in.readString();
            discount = in.readString();
            docNum = in.readString();
            sendFcyCode = in.readString();
            aaeCreaterName = in.readString();
            totalAedAmountWu = in.readString();
            storeType = in.readString();
            wuCurrencyCode = in.readString();
            txnMode = in.readString();
            deliveryOption = in.readString();
            wuLocationId = in.readString();
            isTestQAvailable = in.readString();
            modifierName = in.readString();
            wuBranchOfTxn = in.readString();
            pointsEarned = in.readString();
            aaeReferenceNo = in.readString();
            promoCodeDesc = in.readString();
            remarks = in.readString();
            stagingBuffer = in.readString();
            cashReceived = in.readString();
            rate = in.readString();
            mediumCcy = in.readString();
            wuEnquiryFlag = in.readString();
            drawScreenMessage = in.readString();
            wuMobileNumber = in.readString();
            wuMemCardNo = in.readString();
            vatDiscount = in.readString();
            senderMessage = in.readString();
            wuCountryCode = in.readString();
            wuTaxWorkSheet = in.readString();
            promoCode = in.readString();
            smExtCashPkId = in.readString();
            ipAddress = in.readString();
            txnType = in.readString();
            wuCreatedBy = in.readString();
            deliveryServiceTypeDesc = in.readString();
            currencyCode = in.readString();
            aaeBranchCode = in.readString();
            balanceToPay = in.readString();
            roundedOffAmt = in.readString();
            roundingOff = in.readString();
            filingDate = in.readString();
            mtcn16 = in.readString();
            wuEnrollFlag = in.readString();
            modifiedDate = in.readString();
            promoDiscount = in.readString();
            schCancelFlag = in.readString();
            sendAmount = in.readString();
            filingTime = in.readString();
            wuLookupPromoCode = in.readString();
            destCountryDesc = in.readString();
            totalTxnAmount = in.readString();
            channel = in.readString();
            promoResMsg = in.readString();
        }

        public String getWuTaxRate() {
            return wuTaxRate;
        }

        public void setWuTaxRate(String wuTaxRate) {
            this.wuTaxRate = wuTaxRate;
        }

        public String getAaeCreatedDate() {
            return aaeCreatedDate;
        }

        public void setAaeCreatedDate(String aaeCreatedDate) {
            this.aaeCreatedDate = aaeCreatedDate;
        }

        public String

        getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getAdditionalServiceCharges() {
            return additionalServiceCharges;
        }

        public void setAdditionalServiceCharges(String additionalServiceCharges) {
            this.additionalServiceCharges = additionalServiceCharges;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getRemTxnFkId() {
            return remTxnFkId;
        }

        public void setRemTxnFkId(String remTxnFkId) {
            this.remTxnFkId = remTxnFkId;
        }

        public String getWuTerminalId() {
            return wuTerminalId;
        }

        public void setWuTerminalId(String wuTerminalId) {
            this.wuTerminalId = wuTerminalId;
        }

        public String getMtcn() {
            return mtcn;
        }

        public void setMtcn(String mtcn) {
            this.mtcn = mtcn;
        }

        public String

        getAaeTillNumber() {
            return aaeTillNumber;
        }

        public void setAaeTillNumber(String aaeTillNumber) {
            this.aaeTillNumber = aaeTillNumber;
        }

        public String

        getPromoReqMsg() {
            return promoReqMsg;
        }

        public void setPromoReqMsg(String promoReqMsg) {
            this.promoReqMsg = promoReqMsg;
        }

        public String getMessageFee() {
            return messageFee;
        }

        public void setMessageFee(String messageFee) {
            this.messageFee = messageFee;
        }

        public String getSourceCountryDesc() {
            return sourceCountryDesc;
        }

        public void setSourceCountryDesc(String sourceCountryDesc) {
            this.sourceCountryDesc = sourceCountryDesc;
        }

        public String

        getPromoMessage() {
            return promoMessage;
        }

        public void setPromoMessage(String promoMessage) {
            this.promoMessage = promoMessage;
        }

        public String getVatCharges() {
            return vatCharges;
        }

        public void setVatCharges(String vatCharges) {
            this.vatCharges = vatCharges;
        }

        public String

        getDeliveryOptionDesc() {
            return deliveryOptionDesc;
        }

        public void setDeliveryOptionDesc(String deliveryOptionDesc) {
            this.deliveryOptionDesc = deliveryOptionDesc;
        }

        public String

        getTestAnswer() {
            return testAnswer;
        }

        public void setTestAnswer(String testAnswer) {
            this.testAnswer = testAnswer;
        }

        public String

        getAnsariCharges() {
            return ansariCharges;
        }

        public void setAnsariCharges(String ansariCharges) {
            this.ansariCharges = ansariCharges;
        }

        public String

        getAnsariDrawMessage() {
            return ansariDrawMessage;
        }

        public void setAnsariDrawMessage(String ansariDrawMessage) {
            this.ansariDrawMessage = ansariDrawMessage;
        }

        public String

        getAmlStatus() {
            return amlStatus;
        }

        public void setAmlStatus(String amlStatus) {
            this.amlStatus = amlStatus;
        }

        public String getSnpIndicator() {
            return snpIndicator;
        }

        public void setSnpIndicator(String snpIndicator) {
            this.snpIndicator = snpIndicator;
        }

        public String getDeliveryServiceTypeCd() {
            return deliveryServiceTypeCd;
        }

        public void setDeliveryServiceTypeCd(String deliveryServiceTypeCd) {
            this.deliveryServiceTypeCd = deliveryServiceTypeCd;
        }

        public String getAeeMemCardNo() {
            return aeeMemCardNo;
        }

        public void setAeeMemCardNo(String aeeMemCardNo) {
            this.aeeMemCardNo = aeeMemCardNo;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getTotalAedAmount() {
            return totalAedAmount;
        }

        public void setTotalAedAmount(String totalAedAmount) {
            this.totalAedAmount = totalAedAmount;
        }

        public String getSourceCountryCode() {
            return sourceCountryCode;
        }

        public void setSourceCountryCode(String sourceCountryCode) {
            this.sourceCountryCode = sourceCountryCode;
        }

        public String getCurrencyDesc() {
            return currencyDesc;
        }

        public void setCurrencyDesc(String currencyDesc) {
            this.currencyDesc = currencyDesc;
        }

        public String getCharges() {
            return charges;
        }

        public void setCharges(String charges) {
            this.charges = charges;
        }

        public String

        getWuCancelFlag() {
            return wuCancelFlag;
        }

        public void setWuCancelFlag(String wuCancelFlag) {
            this.wuCancelFlag = wuCancelFlag;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFeeEnqType() {
            return feeEnqType;
        }

        public void setFeeEnqType(String feeEnqType) {
            this.feeEnqType = feeEnqType;
        }

        public String

        getAccGenerated() {
            return accGenerated;
        }

        public void setAccGenerated(String accGenerated) {
            this.accGenerated = accGenerated;
        }

        public String getAmountInAeed() {
            return amountInAeed;
        }

        public void setAmountInAeed(String amountInAeed) {
            this.amountInAeed = amountInAeed;
        }

        public String getAaeCreatedBy() {
            return aaeCreatedBy;
        }

        public void setAaeCreatedBy(String aaeCreatedBy) {
            this.aaeCreatedBy = aaeCreatedBy;
        }

        public String getAeeBranchName() {
            return aeeBranchName;
        }

        public void setAeeBranchName(String aeeBranchName) {
            this.aeeBranchName = aeeBranchName;
        }

        public String

        getWuAccNumberOfBranch() {
            return wuAccNumberOfBranch;
        }

        public void setWuAccNumberOfBranch(String wuAccNumberOfBranch) {
            this.wuAccNumberOfBranch = wuAccNumberOfBranch;
        }

        public String

        getTestQuestion() {
            return testQuestion;
        }

        public void setTestQuestion(String testQuestion) {
            this.testQuestion = testQuestion;
        }

        public String getTotalPointsEarned() {
            return totalPointsEarned;
        }

        public void setTotalPointsEarned(String totalPointsEarned) {
            this.totalPointsEarned = totalPointsEarned;
        }

        public String

        getTermsAndConditions() {
            return termsAndConditions;
        }

        public void setTermsAndConditions(String termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
        }

        public String getDestCountryCode() {
            return destCountryCode;
        }

        public void setDestCountryCode(String destCountryCode) {
            this.destCountryCode = destCountryCode;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDocNum() {
            return docNum;
        }

        public void setDocNum(String docNum) {
            this.docNum = docNum;
        }

        public String getSendFcyCode() {
            return sendFcyCode;
        }

        public void setSendFcyCode(String sendFcyCode) {
            this.sendFcyCode = sendFcyCode;
        }

        public String getAaeCreaterName() {
            return aaeCreaterName;
        }

        public void setAaeCreaterName(String aaeCreaterName) {
            this.aaeCreaterName = aaeCreaterName;
        }

        public String getTotalAedAmountWu() {
            return totalAedAmountWu;
        }

        public void setTotalAedAmountWu(String totalAedAmountWu) {
            this.totalAedAmountWu = totalAedAmountWu;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getWuCurrencyCode() {
            return wuCurrencyCode;
        }

        public void setWuCurrencyCode(String wuCurrencyCode) {
            this.wuCurrencyCode = wuCurrencyCode;
        }

        public String getTxnMode() {
            return txnMode;
        }

        public void setTxnMode(String txnMode) {
            this.txnMode = txnMode;
        }

        public String

        getDeliveryOption() {
            return deliveryOption;
        }

        public void setDeliveryOption(String deliveryOption) {
            this.deliveryOption = deliveryOption;
        }

        public String

        getWuLocationId() {
            return wuLocationId;
        }

        public void setWuLocationId(String wuLocationId) {
            this.wuLocationId = wuLocationId;
        }

        public String

        getIsTestQAvailable() {
            return isTestQAvailable;
        }

        public void setIsTestQAvailable(String isTestQAvailable) {
            this.isTestQAvailable = isTestQAvailable;
        }

        public String

        getModifierName() {
            return modifierName;
        }

        public void setModifierName(String modifierName) {
            this.modifierName = modifierName;
        }

        public String

        getWuBranchOfTxn() {
            return wuBranchOfTxn;
        }

        public void setWuBranchOfTxn(String wuBranchOfTxn) {
            this.wuBranchOfTxn = wuBranchOfTxn;
        }

        public String getPointsEarned() {
            return pointsEarned;
        }

        public void setPointsEarned(String pointsEarned) {
            this.pointsEarned = pointsEarned;
        }

        public String getAaeReferenceNo() {
            return aaeReferenceNo;
        }

        public void setAaeReferenceNo(String aaeReferenceNo) {
            this.aaeReferenceNo = aaeReferenceNo;
        }

        public String

        getPromoCodeDesc() {
            return promoCodeDesc;
        }

        public void setPromoCodeDesc(String promoCodeDesc) {
            this.promoCodeDesc = promoCodeDesc;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getStagingBuffer() {
            return stagingBuffer;
        }

        public void setStagingBuffer(String stagingBuffer) {
            this.stagingBuffer = stagingBuffer;
        }

        public String getCashReceived() {
            return cashReceived;
        }

        public void setCashReceived(String cashReceived) {
            this.cashReceived = cashReceived;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getMediumCcy() {
            return mediumCcy;
        }

        public void setMediumCcy(String mediumCcy) {
            this.mediumCcy = mediumCcy;
        }

        public String

        getWuEnquiryFlag() {
            return wuEnquiryFlag;
        }

        public void setWuEnquiryFlag(String wuEnquiryFlag) {
            this.wuEnquiryFlag = wuEnquiryFlag;
        }

        public String

        getDrawScreenMessage() {
            return drawScreenMessage;
        }

        public void setDrawScreenMessage(String drawScreenMessage) {
            this.drawScreenMessage = drawScreenMessage;
        }

        public String getWuMobileNumber() {
            return wuMobileNumber;
        }

        public void setWuMobileNumber(String wuMobileNumber) {
            this.wuMobileNumber = wuMobileNumber;
        }

        public String getWuMemCardNo() {
            return wuMemCardNo;
        }

        public void setWuMemCardNo(String wuMemCardNo) {
            this.wuMemCardNo = wuMemCardNo;
        }

        public String

        getVatDiscount() {
            return vatDiscount;
        }

        public void setVatDiscount(String vatDiscount) {
            this.vatDiscount = vatDiscount;
        }

        public String

        getSenderMessage() {
            return senderMessage;
        }

        public void setSenderMessage(String senderMessage) {
            this.senderMessage = senderMessage;
        }

        public String getWuCountryCode() {
            return wuCountryCode;
        }

        public void setWuCountryCode(String wuCountryCode) {
            this.wuCountryCode = wuCountryCode;
        }

        public String getWuTaxWorkSheet() {
            return wuTaxWorkSheet;
        }

        public void setWuTaxWorkSheet(String wuTaxWorkSheet) {
            this.wuTaxWorkSheet = wuTaxWorkSheet;
        }

        public String

        getPromoCode() {
            return promoCode;
        }

        public void setPromoCode(String promoCode) {
            this.promoCode = promoCode;
        }

        public String getSmExtCashPkId() {
            return smExtCashPkId;
        }

        public void setSmExtCashPkId(String smExtCashPkId) {
            this.smExtCashPkId = smExtCashPkId;
        }

        public String

        getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getTxnType() {
            return txnType;
        }

        public void setTxnType(String txnType) {
            this.txnType = txnType;
        }

        public String getWuCreatedBy() {
            return wuCreatedBy;
        }

        public void setWuCreatedBy(String wuCreatedBy) {
            this.wuCreatedBy = wuCreatedBy;
        }

        public String getDeliveryServiceTypeDesc() {
            return deliveryServiceTypeDesc;
        }

        public void setDeliveryServiceTypeDesc(String deliveryServiceTypeDesc) {
            this.deliveryServiceTypeDesc = deliveryServiceTypeDesc;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAaeBranchCode() {
            return aaeBranchCode;
        }

        public void setAaeBranchCode(String aaeBranchCode) {
            this.aaeBranchCode = aaeBranchCode;
        }

        public String getBalanceToPay() {
            return balanceToPay;
        }

        public void setBalanceToPay(String balanceToPay) {
            this.balanceToPay = balanceToPay;
        }

        public String

        getRoundedOffAmt() {
            return roundedOffAmt;
        }

        public void setRoundedOffAmt(String roundedOffAmt) {
            this.roundedOffAmt = roundedOffAmt;
        }

        public String

        getRoundingOff() {
            return roundingOff;
        }

        public void setRoundingOff(String roundingOff) {
            this.roundingOff = roundingOff;
        }

        public String getFilingDate() {
            return filingDate;
        }

        public void setFilingDate(String filingDate) {
            this.filingDate = filingDate;
        }

        public String getMtcn16() {
            return mtcn16;
        }

        public void setMtcn16(String mtcn16) {
            this.mtcn16 = mtcn16;
        }

        public String

        getWuEnrollFlag() {
            return wuEnrollFlag;
        }

        public void setWuEnrollFlag(String wuEnrollFlag) {
            this.wuEnrollFlag = wuEnrollFlag;
        }

        public String

        getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String

        getPromoDiscount() {
            return promoDiscount;
        }

        public void setPromoDiscount(String promoDiscount) {
            this.promoDiscount = promoDiscount;
        }

        public String

        getSchCancelFlag() {
            return schCancelFlag;
        }

        public void setSchCancelFlag(String schCancelFlag) {
            this.schCancelFlag = schCancelFlag;
        }

        public String getSendAmount() {
            return sendAmount;
        }

        public void setSendAmount(String sendAmount) {
            this.sendAmount = sendAmount;
        }

        public String getFilingTime() {
            return filingTime;
        }

        public void setFilingTime(String filingTime) {
            this.filingTime = filingTime;
        }

        public String

        getWuLookupPromoCode() {
            return wuLookupPromoCode;
        }

        public void setWuLookupPromoCode(String wuLookupPromoCode) {
            this.wuLookupPromoCode = wuLookupPromoCode;
        }

        public String getDestCountryDesc() {
            return destCountryDesc;
        }

        public void setDestCountryDesc(String destCountryDesc) {
            this.destCountryDesc = destCountryDesc;
        }

        public String getTotalTxnAmount() {
            return totalTxnAmount;
        }

        public void setTotalTxnAmount(String totalTxnAmount) {
            this.totalTxnAmount = totalTxnAmount;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getPromoResMsg() {
            return promoResMsg;
        }

        public void setPromoResMsg(String promoResMsg) {
            this.promoResMsg = promoResMsg;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(wuTaxRate);
            dest.writeString(aaeCreatedDate);
            dest.writeString(modifiedBy);
            dest.writeString(additionalServiceCharges);
            dest.writeString(deliveryCharges);
            dest.writeString(remTxnFkId);
            dest.writeString(wuTerminalId);
            dest.writeString(mtcn);
            dest.writeString(aaeTillNumber);
            dest.writeString(promoReqMsg);
            dest.writeString(messageFee);
            dest.writeString(sourceCountryDesc);
            dest.writeString(promoMessage);
            dest.writeString(vatCharges);
            dest.writeString(deliveryOptionDesc);
            dest.writeString(testAnswer);
            dest.writeString(ansariCharges);
            dest.writeString(ansariDrawMessage);
            dest.writeString(amlStatus);
            dest.writeString(snpIndicator);
            dest.writeString(deliveryServiceTypeCd);
            dest.writeString(aeeMemCardNo);
            dest.writeString(commission);
            dest.writeString(totalAedAmount);
            dest.writeString(sourceCountryCode);
            dest.writeString(currencyDesc);
            dest.writeString(charges);
            dest.writeString(wuCancelFlag);
            dest.writeString(status);
            dest.writeString(feeEnqType);
            dest.writeString(accGenerated);
            dest.writeString(amountInAeed);
            dest.writeString(aaeCreatedBy);
            dest.writeString(aeeBranchName);
            dest.writeString(wuAccNumberOfBranch);
            dest.writeString(testQuestion);
            dest.writeString(totalPointsEarned);
            dest.writeString(termsAndConditions);
            dest.writeString(destCountryCode);
            dest.writeString(discount);
            dest.writeString(docNum);
            dest.writeString(sendFcyCode);
            dest.writeString(aaeCreaterName);
            dest.writeString(totalAedAmountWu);
            dest.writeString(storeType);
            dest.writeString(wuCurrencyCode);
            dest.writeString(txnMode);
            dest.writeString(deliveryOption);
            dest.writeString(wuLocationId);
            dest.writeString(isTestQAvailable);
            dest.writeString(modifierName);
            dest.writeString(wuBranchOfTxn);
            dest.writeString(pointsEarned);
            dest.writeString(aaeReferenceNo);
            dest.writeString(promoCodeDesc);
            dest.writeString(remarks);
            dest.writeString(stagingBuffer);
            dest.writeString(cashReceived);
            dest.writeString(rate);
            dest.writeString(mediumCcy);
            dest.writeString(wuEnquiryFlag);
            dest.writeString(drawScreenMessage);
            dest.writeString(wuMobileNumber);
            dest.writeString(wuMemCardNo);
            dest.writeString(vatDiscount);
            dest.writeString(senderMessage);
            dest.writeString(wuCountryCode);
            dest.writeString(wuTaxWorkSheet);
            dest.writeString(promoCode);
            dest.writeString(smExtCashPkId);
            dest.writeString(ipAddress);
            dest.writeString(txnType);
            dest.writeString(wuCreatedBy);
            dest.writeString(deliveryServiceTypeDesc);
            dest.writeString(currencyCode);
            dest.writeString(aaeBranchCode);
            dest.writeString(balanceToPay);
            dest.writeString(roundedOffAmt);
            dest.writeString(roundingOff);
            dest.writeString(filingDate);
            dest.writeString(mtcn16);
            dest.writeString(wuEnrollFlag);
            dest.writeString(modifiedDate);
            dest.writeString(promoDiscount);
            dest.writeString(schCancelFlag);
            dest.writeString(sendAmount);
            dest.writeString(filingTime);
            dest.writeString(wuLookupPromoCode);
            dest.writeString(destCountryDesc);
            dest.writeString(totalTxnAmount);
            dest.writeString(channel);
            dest.writeString(promoResMsg);
        }
    }

    public static class BeneficiaryDataWu implements Parcelable {
        public static final Creator<BeneficiaryDataWu> CREATOR = new Creator<BeneficiaryDataWu>() {
            @Override
            public BeneficiaryDataWu createFromParcel(Parcel in) {
                return new BeneficiaryDataWu(in);
            }

            @Override
            public BeneficiaryDataWu[] newArray(int size) {
                return new BeneficiaryDataWu[size];
            }
        };
        private String receiverNameType;
        private String receiverLastName;
        private String receiverContactNum;
        private String receiverIssueDate;
        private String receiverDob;
        private String receiverExpDate;
        private String payoutCity;
        private String payoutState;
        private String benExtDtlPkId;
        private String receiverNationalityCode;
        private String receiverIdIssuePlace;
        private String relationship;
        private String receiverIdType;
        private String smExtCashFkId;
        private String receiverNationalityDesc;
        private String benefType;
        private String receiverMiddleName;
        private String receiverIdNum;
        private String receiverFirstName;

        protected BeneficiaryDataWu(Parcel in) {
            receiverNameType = in.readString();
            receiverLastName = in.readString();
            receiverContactNum = in.readString();
            receiverIssueDate = in.readString();
            receiverDob = in.readString();
            receiverExpDate = in.readString();
            payoutCity = in.readString();
            payoutState = in.readString();
            benExtDtlPkId = in.readString();
            receiverNationalityCode = in.readString();
            receiverIdIssuePlace = in.readString();
            relationship = in.readString();
            receiverIdType = in.readString();
            smExtCashFkId = in.readString();
            receiverNationalityDesc = in.readString();
            benefType = in.readString();
            receiverMiddleName = in.readString();
            receiverIdNum = in.readString();
            receiverFirstName = in.readString();
        }

        public String getReceiverNameType() {
            return receiverNameType;
        }

        public void setReceiverNameType(String receiverNameType) {
            this.receiverNameType = receiverNameType;
        }

        public String getReceiverLastName() {
            return receiverLastName;
        }

        public void setReceiverLastName(String receiverLastName) {
            this.receiverLastName = receiverLastName;
        }

        public String getReceiverContactNum() {
            return receiverContactNum;
        }

        public void setReceiverContactNum(String receiverContactNum) {
            this.receiverContactNum = receiverContactNum;
        }

        public String getReceiverIssueDate() {
            return receiverIssueDate;
        }

        public void setReceiverIssueDate(String receiverIssueDate) {
            this.receiverIssueDate = receiverIssueDate;
        }

        public String getReceiverDob() {
            return receiverDob;
        }

        public void setReceiverDob(String receiverDob) {
            this.receiverDob = receiverDob;
        }

        public String getReceiverExpDate() {
            return receiverExpDate;
        }

        public void setReceiverExpDate(String receiverExpDate) {
            this.receiverExpDate = receiverExpDate;
        }

        public String getPayoutCity() {
            return payoutCity;
        }

        public void setPayoutCity(String payoutCity) {
            this.payoutCity = payoutCity;
        }

        public String getPayoutState() {
            return payoutState;
        }

        public void setPayoutState(String payoutState) {
            this.payoutState = payoutState;
        }

        public String getBenExtDtlPkId() {
            return benExtDtlPkId;
        }

        public void setBenExtDtlPkId(String benExtDtlPkId) {
            this.benExtDtlPkId = benExtDtlPkId;
        }

        public String getReceiverNationalityCode() {
            return receiverNationalityCode;
        }

        public void setReceiverNationalityCode(String receiverNationalityCode) {
            this.receiverNationalityCode = receiverNationalityCode;
        }

        public String getReceiverIdIssuePlace() {
            return receiverIdIssuePlace;
        }

        public void setReceiverIdIssuePlace(String receiverIdIssuePlace) {
            this.receiverIdIssuePlace = receiverIdIssuePlace;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getReceiverIdType() {
            return receiverIdType;
        }

        public void setReceiverIdType(String receiverIdType) {
            this.receiverIdType = receiverIdType;
        }

        public String getSmExtCashFkId() {
            return smExtCashFkId;
        }

        public void setSmExtCashFkId(String smExtCashFkId) {
            this.smExtCashFkId = smExtCashFkId;
        }

        public String getReceiverNationalityDesc() {
            return receiverNationalityDesc;
        }

        public void setReceiverNationalityDesc(String receiverNationalityDesc) {
            this.receiverNationalityDesc = receiverNationalityDesc;
        }

        public String getBenefType() {
            return benefType;
        }

        public void setBenefType(String benefType) {
            this.benefType = benefType;
        }

        public String getReceiverMiddleName() {
            return receiverMiddleName;
        }

        public void setReceiverMiddleName(String receiverMiddleName) {
            this.receiverMiddleName = receiverMiddleName;
        }

        public String getReceiverIdNum() {
            return receiverIdNum;
        }

        public void setReceiverIdNum(String receiverIdNum) {
            this.receiverIdNum = receiverIdNum;
        }

        public String getReceiverFirstName() {
            return receiverFirstName;
        }

        public void setReceiverFirstName(String receiverFirstName) {
            this.receiverFirstName = receiverFirstName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(receiverNameType);
            dest.writeString(receiverLastName);
            dest.writeString(receiverContactNum);
            dest.writeString(receiverIssueDate);
            dest.writeString(receiverDob);
            dest.writeString(receiverExpDate);
            dest.writeString(payoutCity);
            dest.writeString(payoutState);
            dest.writeString(benExtDtlPkId);
            dest.writeString(receiverNationalityCode);
            dest.writeString(receiverIdIssuePlace);
            dest.writeString(relationship);
            dest.writeString(receiverIdType);
            dest.writeString(smExtCashFkId);
            dest.writeString(receiverNationalityDesc);
            dest.writeString(benefType);
            dest.writeString(receiverMiddleName);
            dest.writeString(receiverIdNum);
            dest.writeString(receiverFirstName);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.gtwUserMstrFkId);
        dest.writeString(this.arexMemCardFkId);
        dest.writeString(this.beneficiaryId);
        dest.writeString(this.arexMemCardNum);
        dest.writeString(this.ceMemCardFkId);
        dest.writeString(this.ceMemCardNum);
        dest.writeString(this.branchCode);
        dest.writeString(this.txnReferenceNum);
        dest.writeString(this.txnCode);
        dest.writeString(this.txnStatus);
        dest.writeString(this.txnRecType);
        dest.writeString(this.costPrice);
        dest.writeString(this.totalTxnAmount);
        dest.writeString(this.totalValueAed);
        dest.writeString(this.rate);
        dest.writeString(this.txnPayType);
        dest.writeString(this.txnExpLimit);
        dest.writeString(this.txnExpDesc);
        dest.writeString(this.txnType);
        dest.writeString(this.serviceType);
        dest.writeString(this.createdBy);
        dest.writeString(this.createdDate);
        dest.writeString(this.currentTime);
        dest.writeString(this.expModifiedBy);
        dest.writeString(this.expModifiedDate);
        dest.writeString(this.modifiedDate);
        dest.writeString(this.modifiedBy);
        dest.writeString(this.aaeBankName);
        dest.writeString(this.rejectionMessage);
        dest.writeString(this.invoiceFlag);
        dest.writeString(this.aaeAccountNumber);
        dest.writeString(this.aaeIbanNumber);
        dest.writeString(this.url);
        dest.writeString(this.successUrl);
        dest.writeString(this.failureUrl);
        dest.writeString(this.transferFlow);
        dest.writeParcelable(this.beneficiaryData, flags);
        dest.writeParcelable(this.transactionData, flags);
        dest.writeParcelable(this.beneficiaryDataTt, flags);
        dest.writeParcelable(this.transactionDataTt, flags);
        dest.writeParcelable(this.transactionDataWu, flags);
        dest.writeParcelable(this.beneficiaryDataWu, flags);
        dest.writeParcelable(this.transactionDataBTCE, flags);
        dest.writeParcelable(this.beneficiaryDataBTCE, flags);
        dest.writeParcelable(this.transactionHistroyData, flags);
        // dest.writeParcelable(this.transactionHistroyDetail, flags);
        dest.writeTypedList(tRANSACTIONHISTORYDETAILWC);
        dest.writeParcelable(this.tRANSACTIONDATAWC, flags);
        // dest.writeParcelable(this.transactionHistroyDetail, flags);
        dest.writeTypedList(tRANSACTIONDETAILSWC);

        dest.writeString(this.wuBeneficiaryName);
        dest.writeString(this.wuBankName);
        dest.writeString(this.wuFcyAmt);
        dest.writeString(this.wuCcyDesc);
        dest.writeString(this.wuaedAmt);
        dest.writeString(this.payoutState);
        dest.writeString(this.payoutCity);
        dest.writeString(this.branchName);
        dest.writeString(this.mtcn);
        dest.writeString(this.netTotal);
        dest.writeString(this.benefMobileNumber);
        dest.writeString(this.benefPicFileExtn);
    }
//--------------------------------------------------------------------------------------------------


    public static class TRANSACTIONHISTORYDATAWC implements Parcelable {

        @SerializedName("CREATED_DATE")
        private long cREATEDDATE;

        @SerializedName("CHARGES")
        private int cHARGES;

        @SerializedName("DOC_NUM")
        private long dOCNUM;

        @SerializedName("TXN_STATUS")
        private String tXNSTATUS;

        @SerializedName("CARD_STATUS")
        private String cARDSTATUS;

        @SerializedName("CARD_NUMBER")
        private String cARDNUMBER;

        @SerializedName("CUSTOMER_NAME")
        private String cUSTOMERNAME;

        @SerializedName("SUB_TOTAL")
        private double sUBTOTAL;

        @SerializedName("NET_TOTAL")
        private double nETTOTAL;

        @SerializedName("CUSTOMER_MOBILE_NO")
        private String cUSTOMERMOBILENO;

        @SerializedName("ACCOUNT_NUMBER")
        private Object aCCOUNTNUMBER;

        @SerializedName("VAT_DISCOUNT")
        private int vATDISCOUNT;

        @SerializedName("VAT_CHARGES")
        private int vATCHARGES;

        protected TRANSACTIONHISTORYDATAWC(Parcel in) {
            cREATEDDATE = in.readLong();
            cHARGES = in.readInt();
            dOCNUM = in.readLong();
            tXNSTATUS = in.readString();
            cARDSTATUS = in.readString();
            cARDNUMBER = in.readString();
            cUSTOMERNAME = in.readString();
            sUBTOTAL = in.readDouble();
            nETTOTAL = in.readDouble();
            cUSTOMERMOBILENO = in.readString();
            vATDISCOUNT = in.readInt();
            vATCHARGES = in.readInt();
        }

        public static final Creator<TRANSACTIONHISTORYDATAWC> CREATOR = new Creator<TRANSACTIONHISTORYDATAWC>() {
            @Override
            public TRANSACTIONHISTORYDATAWC createFromParcel(Parcel in) {
                return new TRANSACTIONHISTORYDATAWC(in);
            }

            @Override
            public TRANSACTIONHISTORYDATAWC[] newArray(int size) {
                return new TRANSACTIONHISTORYDATAWC[size];
            }
        };

        public void setCREATEDDATE(long cREATEDDATE) {
            this.cREATEDDATE = cREATEDDATE;
        }

        public long getCREATEDDATE() {
            return cREATEDDATE;
        }

        public void setCHARGES(int cHARGES) {
            this.cHARGES = cHARGES;
        }

        public int getCHARGES() {
            return cHARGES;
        }

        public void setDOCNUM(long dOCNUM) {
            this.dOCNUM = dOCNUM;
        }

        public long getDOCNUM() {
            return dOCNUM;
        }

        public void setTXNSTATUS(String tXNSTATUS) {
            this.tXNSTATUS = tXNSTATUS;
        }

        public String getTXNSTATUS() {
            return tXNSTATUS;
        }

        public void setCARDSTATUS(String cARDSTATUS) {
            this.cARDSTATUS = cARDSTATUS;
        }

        public String getCARDSTATUS() {
            return cARDSTATUS;
        }

        public void setCARDNUMBER(String cARDNUMBER) {
            this.cARDNUMBER = cARDNUMBER;
        }

        public String getCARDNUMBER() {
            return cARDNUMBER;
        }

        public void setCUSTOMERNAME(String cUSTOMERNAME) {
            this.cUSTOMERNAME = cUSTOMERNAME;
        }

        public String getCUSTOMERNAME() {
            return cUSTOMERNAME;
        }

        public void setSUBTOTAL(double sUBTOTAL) {
            this.sUBTOTAL = sUBTOTAL;
        }

        public double getSUBTOTAL() {
            return sUBTOTAL;
        }

        public void setNETTOTAL(double nETTOTAL) {
            this.nETTOTAL = nETTOTAL;
        }

        public double getNETTOTAL() {
            return nETTOTAL;
        }

        public void setCUSTOMERMOBILENO(String cUSTOMERMOBILENO) {
            this.cUSTOMERMOBILENO = cUSTOMERMOBILENO;
        }

        public String getCUSTOMERMOBILENO() {
            return cUSTOMERMOBILENO;
        }

        public void setACCOUNTNUMBER(Object aCCOUNTNUMBER) {
            this.aCCOUNTNUMBER = aCCOUNTNUMBER;
        }

        public Object getACCOUNTNUMBER() {
            return aCCOUNTNUMBER;
        }

        public void setVATDISCOUNT(int vATDISCOUNT) {
            this.vATDISCOUNT = vATDISCOUNT;
        }

        public int getVATDISCOUNT() {
            return vATDISCOUNT;
        }

        public void setVATCHARGES(int vATCHARGES) {
            this.vATCHARGES = vATCHARGES;
        }

        public int getVATCHARGES() {
            return vATCHARGES;
        }

        @Override
        public String toString() {
            return
                    "TRANSACTIONHISTORYDATAWC{" +
                            "cREATED_DATE = '" + cREATEDDATE + '\'' +
                            ",cHARGES = '" + cHARGES + '\'' +
                            ",dOC_NUM = '" + dOCNUM + '\'' +
                            ",tXN_STATUS = '" + tXNSTATUS + '\'' +
                            ",cARD_STATUS = '" + cARDSTATUS + '\'' +
                            ",cARD_NUMBER = '" + cARDNUMBER + '\'' +
                            ",cUSTOMER_NAME = '" + cUSTOMERNAME + '\'' +
                            ",sUB_TOTAL = '" + sUBTOTAL + '\'' +
                            ",nET_TOTAL = '" + nETTOTAL + '\'' +
                            ",cUSTOMER_MOBILE_NO = '" + cUSTOMERMOBILENO + '\'' +
                            ",aCCOUNT_NUMBER = '" + aCCOUNTNUMBER + '\'' +
                            ",vAT_DISCOUNT = '" + vATDISCOUNT + '\'' +
                            ",vAT_CHARGES = '" + vATCHARGES + '\'' +
                            "}";
        }

        /**
         * Describe the kinds of special objects contained in this Parcelable
         * instance's marshaled representation. For example, if the object will
         * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
         * the return value of this method must include the
         * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
         *
         * @return a bitmask indicating the set of special object types marshaled
         * by this Parcelable object instance.
         */
        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * Flatten this object in to a Parcel.
         *
         * @param dest  The Parcel in which the object should be written.
         * @param flags Additional flags about how the object should be written.
         *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(cREATEDDATE);
            dest.writeInt(cHARGES);
            dest.writeLong(dOCNUM);
            dest.writeString(tXNSTATUS);
            dest.writeString(cARDSTATUS);
            dest.writeString(cARDNUMBER);
            dest.writeString(cUSTOMERNAME);
            dest.writeDouble(sUBTOTAL);
            dest.writeDouble(nETTOTAL);
            dest.writeString(cUSTOMERMOBILENO);
            dest.writeInt(vATDISCOUNT);
            dest.writeInt(vATCHARGES);
        }
    }

//--------------------------------------------------------------------------------------------------


//----------------------------------------SATURDAY----------------------------------------------------------

    public static class TRANSACTIONHISTORYDETAILWCItem implements Parcelable{

        @SerializedName("TOTAL_VALUE_AED")
        private double tOTALVALUEAED;

        @SerializedName("AMOUNT")
        private double aMOUNT;

        @SerializedName("RATE")
        private double rATE;

        @SerializedName("CCY_CODE")
        private String cCYCODE;

        @SerializedName("FLAG")
        private Object fLAG;

        @SerializedName("CCY_DESC")
        private String cCYDESC;

        protected TRANSACTIONHISTORYDETAILWCItem(Parcel in) {
            tOTALVALUEAED = in.readDouble();
            aMOUNT = in.readDouble();
            rATE = in.readDouble();
            cCYCODE = in.readString();
            cCYDESC = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(tOTALVALUEAED);
            dest.writeDouble(aMOUNT);
            dest.writeDouble(rATE);
            dest.writeString(cCYCODE);
            dest.writeString(cCYDESC);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TRANSACTIONHISTORYDETAILWCItem> CREATOR = new Creator<TRANSACTIONHISTORYDETAILWCItem>() {
            @Override
            public TRANSACTIONHISTORYDETAILWCItem createFromParcel(Parcel in) {
                return new TRANSACTIONHISTORYDETAILWCItem(in);
            }

            @Override
            public TRANSACTIONHISTORYDETAILWCItem[] newArray(int size) {
                return new TRANSACTIONHISTORYDETAILWCItem[size];
            }
        };

        public void setTOTALVALUEAED(double tOTALVALUEAED){
            this.tOTALVALUEAED = tOTALVALUEAED;
        }

        public double getTOTALVALUEAED(){
            return tOTALVALUEAED;
        }

        public void setAMOUNT(double aMOUNT){
            this.aMOUNT = aMOUNT;
        }

        public double getAMOUNT(){
            return aMOUNT;
        }

        public void setRATE(double rATE){
            this.rATE = rATE;
        }

        public double getRATE(){
            return rATE;
        }

        public void setCCYCODE(String cCYCODE){
            this.cCYCODE = cCYCODE;
        }

        public String getCCYCODE(){
            return cCYCODE;
        }

        public void setFLAG(Object fLAG){
            this.fLAG = fLAG;
        }

        public Object getFLAG(){
            return fLAG;
        }

        public void setCCYDESC(String cCYDESC){
            this.cCYDESC = cCYDESC;
        }

        public String getCCYDESC(){
            return cCYDESC;
        }

        @Override
        public String toString(){
            return
                    "TRANSACTIONHISTORYDETAILWCItem{" +
                            "tOTAL_VALUE_AED = '" + tOTALVALUEAED + '\'' +
                            ",aMOUNT = '" + aMOUNT + '\'' +
                            ",rATE = '" + rATE + '\'' +
                            ",cCY_CODE = '" + cCYCODE + '\'' +
                            ",fLAG = '" + fLAG + '\'' +
                            ",cCY_DESC = '" + cCYDESC + '\'' +
                            "}";
        }
    }

    public static class TRANSACTIONDATAWC implements Parcelable{

        @SerializedName("emiratesIdNumber")
        private String emiratesIdNumber;

        @SerializedName("approvedBy")
        private Object approvedBy;

        @SerializedName("reloadedByEmiratesId")
        private Object reloadedByEmiratesId;

        @SerializedName("discount")
        private int discount;

        @SerializedName("subTotal")
        private double subTotal;

        @SerializedName("updatedDate")
        private Object updatedDate;

        @SerializedName("commision")
        private int commision;

        /* @SerializedName("reloadCharges")
         private int reloadCharges;*/
        @SerializedName("reloadCharges")
        private float reloadCharges;

        @SerializedName("approverName")
        private Object approverName;

        @SerializedName("emiratesIdExpireDate")
        private long emiratesIdExpireDate;

        @SerializedName("poBox")
        private String poBox;

        @SerializedName("saleExtHeaderPkId")
        private int saleExtHeaderPkId;

        @SerializedName("emiratesIdIssueDate")
        private long emiratesIdIssueDate;

        @SerializedName("cardStatus")
        private String cardStatus;

        @SerializedName("updatedBy")
        private Object updatedBy;

        @SerializedName("reversalFlag")
        private Object reversalFlag;

        @SerializedName("custLastName")
        private String custLastName;

        @SerializedName("roundingOff")
        private int roundingOff;

        @SerializedName("customerName")
        private String customerName;

        @SerializedName("createrName")
        private String createrName;

        @SerializedName("balToPay")
        private int balToPay;

        @SerializedName("branchCode")
        private String branchCode;

        @SerializedName("approvedDate")
        private Object approvedDate;

        @SerializedName("txnCode")
        private int txnCode;

        @SerializedName("customerMobileNum")
        private String customerMobileNum;

        @SerializedName("cardRegNum")
        private Object cardRegNum;

        @SerializedName("nationality")
        private String nationality;

        @SerializedName("loadedCardNumber")
        private String loadedCardNumber;

        @SerializedName("nationalityDesc")
        private String nationalityDesc;

        @SerializedName("accGenerated")
        private String accGenerated;

        @SerializedName("rejectionReason")
        private Object rejectionReason;

        @SerializedName("primaryCardNumber")
        private String primaryCardNumber;

        @SerializedName("vatCharges")
        private String vatCharges;

        @SerializedName("docNum")
        private long docNum;

        @SerializedName("purposeOfTxn")
        private String purposeOfTxn;

        @SerializedName("remTxnFkId")
        private int remTxnFkId;

        @SerializedName("reloadedByName")
        private Object reloadedByName;

        @SerializedName("subTotalCostPrice")
        private double subTotalCostPrice;

        @SerializedName("sourceOfFund")
        private String sourceOfFund;

        @SerializedName("reloadedBy")
        private String reloadedBy;

        @SerializedName("txnStatus")
        private Object txnStatus;

        @SerializedName("email")
        private String email;

        @SerializedName("approvalStatus")
        private Object approvalStatus;

        @SerializedName("address")
        private String address;

        @SerializedName("retryCount")
        private int retryCount;

        @SerializedName("tillNumber")
        private Object tillNumber;

        @SerializedName("branchName")
        private String branchName;

        @SerializedName("vatDiscount")
        private int vatDiscount;

        @SerializedName("dateOfBirth")
        private long dateOfBirth;

        @SerializedName("accountNumber")
        private Object accountNumber;

        @SerializedName("createdDate")
        private long createdDate;

        @SerializedName("createdBy")
        private String createdBy;

        @SerializedName("purposeOfTxnDtl")
        private Object purposeOfTxnDtl;

        @SerializedName("memCardNumber")
        private long memCardNumber;

        @SerializedName("netTotal")
        private double netTotal;

        @SerializedName("cancelflag")
        private String cancelflag;

        @SerializedName("cashReceived")
        private double cashReceived;

        protected TRANSACTIONDATAWC(Parcel in) {
            emiratesIdNumber = in.readString();
            discount = in.readInt();
            subTotal = in.readDouble();
            commision = in.readInt();
            reloadCharges = in.readFloat();
            emiratesIdExpireDate = in.readLong();
            poBox = in.readString();
            saleExtHeaderPkId = in.readInt();
            emiratesIdIssueDate = in.readLong();
            cardStatus = in.readString();
            custLastName = in.readString();
            roundingOff = in.readInt();
            customerName = in.readString();
            createrName = in.readString();
            balToPay = in.readInt();
            branchCode = in.readString();
            txnCode = in.readInt();
            customerMobileNum = in.readString();
            nationality = in.readString();
            loadedCardNumber = in.readString();
            nationalityDesc = in.readString();
            accGenerated = in.readString();
            primaryCardNumber = in.readString();
            vatCharges = in.readString();
            docNum = in.readLong();
            purposeOfTxn = in.readString();
            remTxnFkId = in.readInt();
            subTotalCostPrice = in.readDouble();
            sourceOfFund = in.readString();
            reloadedBy = in.readString();
            email = in.readString();
            address = in.readString();
            retryCount = in.readInt();
            branchName = in.readString();
            vatDiscount = in.readInt();
            dateOfBirth = in.readLong();
            createdDate = in.readLong();
            createdBy = in.readString();
            memCardNumber = in.readLong();
            netTotal = in.readDouble();
            cancelflag = in.readString();
            cashReceived = in.readDouble();
        }

        public static final Creator<TRANSACTIONDATAWC> CREATOR = new Creator<TRANSACTIONDATAWC>() {
            @Override
            public TRANSACTIONDATAWC createFromParcel(Parcel in) {
                return new TRANSACTIONDATAWC(in);
            }

            @Override
            public TRANSACTIONDATAWC[] newArray(int size) {
                return new TRANSACTIONDATAWC[size];
            }
        };

        public void setEmiratesIdNumber(String emiratesIdNumber){
            this.emiratesIdNumber = emiratesIdNumber;
        }

        public String getEmiratesIdNumber(){
            return emiratesIdNumber;
        }

        public void setApprovedBy(Object approvedBy){
            this.approvedBy = approvedBy;
        }

        public Object getApprovedBy(){
            return approvedBy;
        }

        public void setReloadedByEmiratesId(Object reloadedByEmiratesId){
            this.reloadedByEmiratesId = reloadedByEmiratesId;
        }

        public Object getReloadedByEmiratesId(){
            return reloadedByEmiratesId;
        }

        public void setDiscount(int discount){
            this.discount = discount;
        }

        public int getDiscount(){
            return discount;
        }

        public void setSubTotal(double subTotal){
            this.subTotal = subTotal;
        }

        public double getSubTotal(){
            return subTotal;
        }

        public void setUpdatedDate(Object updatedDate){
            this.updatedDate = updatedDate;
        }

        public Object getUpdatedDate(){
            return updatedDate;
        }

        public void setCommision(int commision){
            this.commision = commision;
        }

        public int getCommision(){
            return commision;
        }

        public void setReloadCharges(float reloadCharges){
            this.reloadCharges = reloadCharges;
        }

        public float getReloadCharges(){
            return reloadCharges;
        }

        public void setApproverName(Object approverName){
            this.approverName = approverName;
        }

        public Object getApproverName(){
            return approverName;
        }

        public void setEmiratesIdExpireDate(long emiratesIdExpireDate){
            this.emiratesIdExpireDate = emiratesIdExpireDate;
        }

        public long getEmiratesIdExpireDate(){
            return emiratesIdExpireDate;
        }

        public void setPoBox(String poBox){
            this.poBox = poBox;
        }

        public String getPoBox(){
            return poBox;
        }

        public void setSaleExtHeaderPkId(int saleExtHeaderPkId){
            this.saleExtHeaderPkId = saleExtHeaderPkId;
        }

        public int getSaleExtHeaderPkId(){
            return saleExtHeaderPkId;
        }

        public void setEmiratesIdIssueDate(long emiratesIdIssueDate){
            this.emiratesIdIssueDate = emiratesIdIssueDate;
        }

        public long getEmiratesIdIssueDate(){
            return emiratesIdIssueDate;
        }

        public void setCardStatus(String cardStatus){
            this.cardStatus = cardStatus;
        }

        public String getCardStatus(){
            return cardStatus;
        }

        public void setUpdatedBy(Object updatedBy){
            this.updatedBy = updatedBy;
        }

        public Object getUpdatedBy(){
            return updatedBy;
        }

        public void setReversalFlag(Object reversalFlag){
            this.reversalFlag = reversalFlag;
        }

        public Object getReversalFlag(){
            return reversalFlag;
        }

        public void setCustLastName(String custLastName){
            this.custLastName = custLastName;
        }

        public String getCustLastName(){
            return custLastName;
        }

        public void setRoundingOff(int roundingOff){
            this.roundingOff = roundingOff;
        }

        public int getRoundingOff(){
            return roundingOff;
        }

        public void setCustomerName(String customerName){
            this.customerName = customerName;
        }

        public String getCustomerName(){
            return customerName;
        }

        public void setCreaterName(String createrName){
            this.createrName = createrName;
        }

        public String getCreaterName(){
            return createrName;
        }

        public void setBalToPay(int balToPay){
            this.balToPay = balToPay;
        }

        public int getBalToPay(){
            return balToPay;
        }

        public void setBranchCode(String branchCode){
            this.branchCode = branchCode;
        }

        public String getBranchCode(){
            return branchCode;
        }

        public void setApprovedDate(Object approvedDate){
            this.approvedDate = approvedDate;
        }

        public Object getApprovedDate(){
            return approvedDate;
        }

        public void setTxnCode(int txnCode){
            this.txnCode = txnCode;
        }

        public int getTxnCode(){
            return txnCode;
        }

        public void setCustomerMobileNum(String customerMobileNum){
            this.customerMobileNum = customerMobileNum;
        }

        public String getCustomerMobileNum(){
            return customerMobileNum;
        }

        public void setCardRegNum(Object cardRegNum){
            this.cardRegNum = cardRegNum;
        }

        public Object getCardRegNum(){
            return cardRegNum;
        }

        public void setNationality(String nationality){
            this.nationality = nationality;
        }

        public String getNationality(){
            return nationality;
        }

        public void setLoadedCardNumber(String loadedCardNumber){
            this.loadedCardNumber = loadedCardNumber;
        }

        public String getLoadedCardNumber(){
            return loadedCardNumber;
        }

        public void setNationalityDesc(String nationalityDesc){
            this.nationalityDesc = nationalityDesc;
        }

        public String getNationalityDesc(){
            return nationalityDesc;
        }

        public void setAccGenerated(String accGenerated){
            this.accGenerated = accGenerated;
        }

        public String getAccGenerated(){
            return accGenerated;
        }

        public void setRejectionReason(Object rejectionReason){
            this.rejectionReason = rejectionReason;
        }

        public Object getRejectionReason(){
            return rejectionReason;
        }

        public void setPrimaryCardNumber(String primaryCardNumber){
            this.primaryCardNumber = primaryCardNumber;
        }

        public String getPrimaryCardNumber(){
            return primaryCardNumber;
        }

        public void setVatCharges(String vatCharges){
            this.vatCharges = vatCharges;
        }

        public String getVatCharges(){
            return vatCharges;
        }

        public void setDocNum(long docNum){
            this.docNum = docNum;
        }

        public long getDocNum(){
            return docNum;
        }

        public void setPurposeOfTxn(String purposeOfTxn){
            this.purposeOfTxn = purposeOfTxn;
        }

        public String getPurposeOfTxn(){
            return purposeOfTxn;
        }

        public void setRemTxnFkId(int remTxnFkId){
            this.remTxnFkId = remTxnFkId;
        }

        public int getRemTxnFkId(){
            return remTxnFkId;
        }

        public void setReloadedByName(Object reloadedByName){
            this.reloadedByName = reloadedByName;
        }

        public Object getReloadedByName(){
            return reloadedByName;
        }

        public void setSubTotalCostPrice(double subTotalCostPrice){
            this.subTotalCostPrice = subTotalCostPrice;
        }

        public double getSubTotalCostPrice(){
            return subTotalCostPrice;
        }

        public void setSourceOfFund(String sourceOfFund){
            this.sourceOfFund = sourceOfFund;
        }

        public String getSourceOfFund(){
            return sourceOfFund;
        }

        public void setReloadedBy(String reloadedBy){
            this.reloadedBy = reloadedBy;
        }

        public String getReloadedBy(){
            return reloadedBy;
        }

        public void setTxnStatus(Object txnStatus){
            this.txnStatus = txnStatus;
        }

        public Object getTxnStatus(){
            return txnStatus;
        }

        public void setEmail(String email){
            this.email = email;
        }

        public String getEmail(){
            return email;
        }

        public void setApprovalStatus(Object approvalStatus){
            this.approvalStatus = approvalStatus;
        }

        public Object getApprovalStatus(){
            return approvalStatus;
        }

        public void setAddress(String address){
            this.address = address;
        }

        public String getAddress(){
            return address;
        }

        public void setRetryCount(int retryCount){
            this.retryCount = retryCount;
        }

        public int getRetryCount(){
            return retryCount;
        }

        public void setTillNumber(Object tillNumber){
            this.tillNumber = tillNumber;
        }

        public Object getTillNumber(){
            return tillNumber;
        }

        public void setBranchName(String branchName){
            this.branchName = branchName;
        }

        public String getBranchName(){
            return branchName;
        }

        public void setVatDiscount(int vatDiscount){
            this.vatDiscount = vatDiscount;
        }

        public int getVatDiscount(){
            return vatDiscount;
        }

        public void setDateOfBirth(long dateOfBirth){
            this.dateOfBirth = dateOfBirth;
        }

        public long getDateOfBirth(){
            return dateOfBirth;
        }

        public void setAccountNumber(Object accountNumber){
            this.accountNumber = accountNumber;
        }

        public Object getAccountNumber(){
            return accountNumber;
        }

        public void setCreatedDate(long createdDate){
            this.createdDate = createdDate;
        }

        public long getCreatedDate(){
            return createdDate;
        }

        public void setCreatedBy(String createdBy){
            this.createdBy = createdBy;
        }

        public String getCreatedBy(){
            return createdBy;
        }

        public void setPurposeOfTxnDtl(Object purposeOfTxnDtl){
            this.purposeOfTxnDtl = purposeOfTxnDtl;
        }

        public Object getPurposeOfTxnDtl(){
            return purposeOfTxnDtl;
        }

        public void setMemCardNumber(long memCardNumber){
            this.memCardNumber = memCardNumber;
        }

        public long getMemCardNumber(){
            return memCardNumber;
        }

        public void setNetTotal(double netTotal){
            this.netTotal = netTotal;
        }

        public double getNetTotal(){
            return netTotal;
        }

        public void setCancelflag(String cancelflag){
            this.cancelflag = cancelflag;
        }

        public String getCancelflag(){
            return cancelflag;
        }

        public void setCashReceived(double cashReceived){
            this.cashReceived = cashReceived;
        }

        public double getCashReceived(){
            return cashReceived;
        }

        @Override
        public String toString(){
            return
                    "TRANSACTIONDATAWC{" +
                            "emiratesIdNumber = '" + emiratesIdNumber + '\'' +
                            ",approvedBy = '" + approvedBy + '\'' +
                            ",reloadedByEmiratesId = '" + reloadedByEmiratesId + '\'' +
                            ",discount = '" + discount + '\'' +
                            ",subTotal = '" + subTotal + '\'' +
                            ",updatedDate = '" + updatedDate + '\'' +
                            ",commision = '" + commision + '\'' +
                            ",reloadCharges = '" + reloadCharges + '\'' +
                            ",approverName = '" + approverName + '\'' +
                            ",emiratesIdExpireDate = '" + emiratesIdExpireDate + '\'' +
                            ",poBox = '" + poBox + '\'' +
                            ",saleExtHeaderPkId = '" + saleExtHeaderPkId + '\'' +
                            ",emiratesIdIssueDate = '" + emiratesIdIssueDate + '\'' +
                            ",cardStatus = '" + cardStatus + '\'' +
                            ",updatedBy = '" + updatedBy + '\'' +
                            ",reversalFlag = '" + reversalFlag + '\'' +
                            ",custLastName = '" + custLastName + '\'' +
                            ",roundingOff = '" + roundingOff + '\'' +
                            ",customerName = '" + customerName + '\'' +
                            ",createrName = '" + createrName + '\'' +
                            ",balToPay = '" + balToPay + '\'' +
                            ",branchCode = '" + branchCode + '\'' +
                            ",approvedDate = '" + approvedDate + '\'' +
                            ",txnCode = '" + txnCode + '\'' +
                            ",customerMobileNum = '" + customerMobileNum + '\'' +
                            ",cardRegNum = '" + cardRegNum + '\'' +
                            ",nationality = '" + nationality + '\'' +
                            ",loadedCardNumber = '" + loadedCardNumber + '\'' +
                            ",nationalityDesc = '" + nationalityDesc + '\'' +
                            ",accGenerated = '" + accGenerated + '\'' +
                            ",rejectionReason = '" + rejectionReason + '\'' +
                            ",primaryCardNumber = '" + primaryCardNumber + '\'' +
                            ",vatCharges = '" + vatCharges + '\'' +
                            ",docNum = '" + docNum + '\'' +
                            ",purposeOfTxn = '" + purposeOfTxn + '\'' +
                            ",remTxnFkId = '" + remTxnFkId + '\'' +
                            ",reloadedByName = '" + reloadedByName + '\'' +
                            ",subTotalCostPrice = '" + subTotalCostPrice + '\'' +
                            ",sourceOfFund = '" + sourceOfFund + '\'' +
                            ",reloadedBy = '" + reloadedBy + '\'' +
                            ",txnStatus = '" + txnStatus + '\'' +
                            ",email = '" + email + '\'' +
                            ",approvalStatus = '" + approvalStatus + '\'' +
                            ",address = '" + address + '\'' +
                            ",retryCount = '" + retryCount + '\'' +
                            ",tillNumber = '" + tillNumber + '\'' +
                            ",branchName = '" + branchName + '\'' +
                            ",vatDiscount = '" + vatDiscount + '\'' +
                            ",dateOfBirth = '" + dateOfBirth + '\'' +
                            ",accountNumber = '" + accountNumber + '\'' +
                            ",createdDate = '" + createdDate + '\'' +
                            ",createdBy = '" + createdBy + '\'' +
                            ",purposeOfTxnDtl = '" + purposeOfTxnDtl + '\'' +
                            ",memCardNumber = '" + memCardNumber + '\'' +
                            ",netTotal = '" + netTotal + '\'' +
                            ",cancelflag = '" + cancelflag + '\'' +
                            ",cashReceived = '" + cashReceived + '\'' +
                            "}";
        }


        @Override
        public int describeContents() {
            return 0;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(emiratesIdNumber);
            dest.writeInt(discount);
            dest.writeDouble(subTotal);
            dest.writeInt(commision);
            dest.writeFloat(reloadCharges);
            dest.writeLong(emiratesIdExpireDate);
            dest.writeString(poBox);
            dest.writeInt(saleExtHeaderPkId);
            dest.writeLong(emiratesIdIssueDate);
            dest.writeString(cardStatus);
            dest.writeString(custLastName);
            dest.writeInt(roundingOff);
            dest.writeString(customerName);
            dest.writeString(createrName);
            dest.writeInt(balToPay);
            dest.writeString(branchCode);
            dest.writeInt(txnCode);
            dest.writeString(customerMobileNum);
            dest.writeString(nationality);
            dest.writeString(loadedCardNumber);
            dest.writeString(nationalityDesc);
            dest.writeString(accGenerated);
            dest.writeString(primaryCardNumber);
            dest.writeString(vatCharges);
            dest.writeLong(docNum);
            dest.writeString(purposeOfTxn);
            dest.writeInt(remTxnFkId);
            dest.writeDouble(subTotalCostPrice);
            dest.writeString(sourceOfFund);
            dest.writeString(reloadedBy);
            dest.writeString(email);
            dest.writeString(address);
            dest.writeInt(retryCount);
            dest.writeString(branchName);
            dest.writeInt(vatDiscount);
            dest.writeLong(dateOfBirth);
            dest.writeLong(createdDate);
            dest.writeString(createdBy);
            dest.writeLong(memCardNumber);
            dest.writeDouble(netTotal);
            dest.writeString(cancelflag);
            dest.writeDouble(cashReceived);
        }
    }


    public static class TRANSACTIONDETAILSWCItem implements Parcelable {

        @SerializedName("saleDetailPkId")
        private int saleDetailPkId;

        @SerializedName("amount")
        private double amount;

        @SerializedName("costPriceValue")
        private double costPriceValue;

        @SerializedName("rate")
        private double rate;

        @SerializedName("ccyCode")
        private String ccyCode;

        @SerializedName("ccyDesc")
        private String ccyDesc;

        @SerializedName("saleHeaderFkId")
        private int saleHeaderFkId;

        @SerializedName("totalValAed")
        private double totalValAed;

        @SerializedName("factor")
        private String factor;

        @SerializedName("costPriceRate")
        private double costPriceRate;

        protected TRANSACTIONDETAILSWCItem(Parcel in) {
            saleDetailPkId = in.readInt();
            amount = in.readDouble();
            costPriceValue = in.readDouble();
            rate = in.readDouble();
            ccyCode = in.readString();
            ccyDesc = in.readString();
            saleHeaderFkId = in.readInt();
            totalValAed = in.readDouble();
            factor = in.readString();
            costPriceRate = in.readDouble();
        }

        public static final Creator<TRANSACTIONDETAILSWCItem> CREATOR = new Creator<TRANSACTIONDETAILSWCItem>() {
            @Override
            public TRANSACTIONDETAILSWCItem createFromParcel(Parcel in) {
                return new TRANSACTIONDETAILSWCItem(in);
            }

            @Override
            public TRANSACTIONDETAILSWCItem[] newArray(int size) {
                return new TRANSACTIONDETAILSWCItem[size];
            }
        };

        public void setSaleDetailPkId(int saleDetailPkId){
            this.saleDetailPkId = saleDetailPkId;
        }

        public int getSaleDetailPkId(){
            return saleDetailPkId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setCostPriceValue(double costPriceValue){
            this.costPriceValue = costPriceValue;
        }

        public double getCostPriceValue(){
            return costPriceValue;
        }

        public void setRate(double rate){
            this.rate = rate;
        }

        public double getRate(){
            return rate;
        }

        public void setCcyCode(String ccyCode){
            this.ccyCode = ccyCode;
        }

        public String getCcyCode(){
            return ccyCode;
        }

        public void setCcyDesc(String ccyDesc){
            this.ccyDesc = ccyDesc;
        }

        public String getCcyDesc(){
            return ccyDesc;
        }

        public void setSaleHeaderFkId(int saleHeaderFkId){
            this.saleHeaderFkId = saleHeaderFkId;
        }

        public int getSaleHeaderFkId(){
            return saleHeaderFkId;
        }

        public void setTotalValAed(double totalValAed){
            this.totalValAed = totalValAed;
        }

        public double getTotalValAed(){
            return totalValAed;
        }

        public void setFactor(String factor){
            this.factor = factor;
        }

        public String getFactor(){
            return factor;
        }

        public void setCostPriceRate(double costPriceRate){
            this.costPriceRate = costPriceRate;
        }

        public double getCostPriceRate(){
            return costPriceRate;
        }

        @Override
        public String toString(){
            return
                    "TRANSACTIONDETAILSWCItem{" +
                            "saleDetailPkId = '" + saleDetailPkId + '\'' +
                            ",amount = '" + amount + '\'' +
                            ",costPriceValue = '" + costPriceValue + '\'' +
                            ",rate = '" + rate + '\'' +
                            ",ccyCode = '" + ccyCode + '\'' +
                            ",ccyDesc = '" + ccyDesc + '\'' +
                            ",saleHeaderFkId = '" + saleHeaderFkId + '\'' +
                            ",totalValAed = '" + totalValAed + '\'' +
                            ",factor = '" + factor + '\'' +
                            ",costPriceRate = '" + costPriceRate + '\'' +
                            "}";
        }


        @Override
        public int describeContents() {
            return 0;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(saleDetailPkId);
            dest.writeDouble(amount);
            dest.writeDouble(costPriceValue);
            dest.writeDouble(rate);
            dest.writeString(ccyCode);
            dest.writeString(ccyDesc);
            dest.writeInt(saleHeaderFkId);
            dest.writeDouble(totalValAed);
            dest.writeString(factor);
            dest.writeDouble(costPriceRate);
        }
    }
}




//--------------------------------------------------------------------------------------------------


