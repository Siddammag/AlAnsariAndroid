package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 23 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TxnDetailsCreditCardData implements Parcelable {

    public static final Creator<TxnDetailsCreditCardData> CREATOR = new Creator<TxnDetailsCreditCardData>() {
        @Override
        public TxnDetailsCreditCardData createFromParcel(Parcel source) {
            return new TxnDetailsCreditCardData(source);
        }

        @Override
        public TxnDetailsCreditCardData[] newArray(int size) {
            return new TxnDetailsCreditCardData[size];
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
    @SerializedName("REM_TXN_REF_NUM")
    private String txnReferenceNum;
    @SerializedName("TXN_CODE")
    private String txnCode;
    @SerializedName("TXN_STATUS")
    private String txnStatus;
    @SerializedName("BANK_TRANSFER_FLOW")
    private String transferFlow;
    @SerializedName("REJECTION_MESSAGE")
    private String rejectionMessage;
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
    @SerializedName("BENEFICIARY_DATA_CC")
    private BeneficiaryData beneficiaryData;
    @SerializedName("TRANSACTION_DATA_CC")
    private TransactionData transactionData;
    @SerializedName("INVOICE_FLAG")
    private String invoiceFlag;

    public TxnDetailsCreditCardData() {
    }

    protected TxnDetailsCreditCardData(Parcel in) {
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
        this.rejectionMessage = in.readString();
        this.transferFlow = in.readString();
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
        this.aaeAccountNumber = in.readString();
        this.aaeIbanNumber = in.readString();
        this.url = in.readString();
        this.successUrl = in.readString();
        this.failureUrl = in.readString();
        this.invoiceFlag=in.readString();
        this.beneficiaryData = in.readParcelable(BeneficiaryData.class.getClassLoader());
        this.transactionData = in.readParcelable(TransactionData.class.getClassLoader());
    }

    public String getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(String invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
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

    public String getRejectionMessage() {
        return rejectionMessage;
    }

    public void setRejectionMessage(String rejectionMessage) {
        this.rejectionMessage = rejectionMessage;
    }

    public String getTransferFlow() {
        return transferFlow;
    }

    public void setTransferFlow(String transferFlow) {
        this.transferFlow = transferFlow;
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

    public TxnDetailsData getTxnDetailsData() {
        TxnDetailsData txnDetailsData = new TxnDetailsData();
        txnDetailsData.setId(id);
        txnDetailsData.setGtwUserMstrFkId(gtwUserMstrFkId);
        txnDetailsData.setBeneficiaryId(beneficiaryId);
        txnDetailsData.setCeMemCardFkId(ceMemCardFkId);
        txnDetailsData.setCeMemCardNum(ceMemCardNum);
        txnDetailsData.setBranchCode(branchCode);
        txnDetailsData.setTxnReferenceNum(txnReferenceNum);
        txnDetailsData.setTxnCode(txnCode);
        txnDetailsData.setTxnStatus(txnStatus);
        txnDetailsData.setRejectionMessage(rejectionMessage);
        txnDetailsData.setTransferFlow(transferFlow);
        txnDetailsData.setTxnRecType(txnRecType);
        txnDetailsData.setCostPrice(costPrice);
        txnDetailsData.setTotalTxnAmount(totalTxnAmount);
        txnDetailsData.setTotalValueAed(totalValueAed);
        txnDetailsData.setRate(rate);
        txnDetailsData.setTxnPayType(txnPayType);
        txnDetailsData.setTxnExpLimit(txnExpLimit);
        txnDetailsData.setTxnExpDesc(txnExpDesc);
        txnDetailsData.setServiceType(serviceType);
        txnDetailsData.setCreatedBy(createdBy);
        txnDetailsData.setCreatedDate(createdDate);
        txnDetailsData.setCurrentTime(currentTime);
        txnDetailsData.setExpModifiedBy(expModifiedBy);
        txnDetailsData.setExpModifiedDate(expModifiedDate);
        txnDetailsData.setModifiedDate(modifiedDate);
        txnDetailsData.setModifiedBy(modifiedBy);
        txnDetailsData.setAaeBankName(aaeBankName);
        txnDetailsData.setAaeAccountNumber(aaeAccountNumber);
        txnDetailsData.setAaeIbanNumber(aaeIbanNumber);
        txnDetailsData.setUrl(url);
        txnDetailsData.setSuccessUrl(successUrl);
        txnDetailsData.setFailureUrl(failureUrl);
        txnDetailsData.setInvoiceFlag(invoiceFlag);
        txnDetailsData.setTxnType("CC");
        // Transaction Data
        txnDetailsData.setTransactionData(new TxnDetailsData.TransactionData());
        txnDetailsData.getTransactionData().setRemDtSendPkId(transactionData.id);
        txnDetailsData.getTransactionData().setRemTxnFkId(transactionData.remTxnFkId);
        txnDetailsData.getTransactionData().setTxnAmount(beneficiaryData.txnAmount);
        txnDetailsData.getTransactionData().setChargeAmount(beneficiaryData.charges);
        txnDetailsData.getTransactionData().setCommAmount("");
        txnDetailsData.getTransactionData().setDiscAmount("");
        txnDetailsData.getTransactionData().setNetTotal(beneficiaryData.totalAmount);
        //txnDetailsData.getTransactionData().setRate(beneficiaryData.rate);
        txnDetailsData.getTransactionData().setTotalValueAed(beneficiaryData.totalAmount);
        txnDetailsData.getTransactionData().setRoundedOffAmount("");
        txnDetailsData.getTransactionData().setCcyCode(beneficiaryData.ccyCode);
        txnDetailsData.getTransactionData().setCcyName(beneficiaryData.ccyDesc);
        txnDetailsData.getTransactionData().setSaleType("");
        txnDetailsData.getTransactionData().setCostPrice("");
        txnDetailsData.getTransactionData().setBankCode(beneficiaryData.bankCode);
        txnDetailsData.getTransactionData().setBankName(beneficiaryData.bankName);
        txnDetailsData.getTransactionData().setBranchCode(transactionData.branchCode);
        txnDetailsData.getTransactionData().setBranchType("");
        txnDetailsData.getTransactionData().setBranchName("");
        txnDetailsData.getTransactionData().setCreatedBy("");
        txnDetailsData.getTransactionData().setCreatedDate(transactionData.createdDate);
        txnDetailsData.getTransactionData().setStatus("");
        //txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);

        // Beneficiary Data (Cash Payout)
        txnDetailsData.setBeneficiaryData(new TxnDetailsData.BeneficiaryData());
        txnDetailsData.getBeneficiaryData().setId(beneficiaryData.id);
        txnDetailsData.getBeneficiaryData().setRemDtSendFkId(beneficiaryData.ccTxnFkId);
        txnDetailsData.getBeneficiaryData().setIdTt(beneficiaryData.id);
        txnDetailsData.getBeneficiaryData().setRemDtSendFkIdTt(beneficiaryData.ccTxnFkId);
        txnDetailsData.getBeneficiaryData().setBeneficiaryName(beneficiaryData.cardHolderName);
        txnDetailsData.getBeneficiaryData().setBeneficiaryNameAr(beneficiaryData.cardHolderName);
        txnDetailsData.getBeneficiaryData().setAccountNumber(beneficiaryData.cardNumber);
        txnDetailsData.getBeneficiaryData().setBankName(beneficiaryData.bankName);
        txnDetailsData.getBeneficiaryData().setBranchName(beneficiaryData.cardType);
        txnDetailsData.getBeneficiaryData().setPayMode(txnPayType);
        txnDetailsData.getBeneficiaryData().setCountryCode("");
        txnDetailsData.getBeneficiaryData().setCountryName("");
        txnDetailsData.getBeneficiaryData().setNationality("");
        txnDetailsData.getBeneficiaryData().setNationalityCode("");
        txnDetailsData.getBeneficiaryData().setMobileNumber("");
        txnDetailsData.getBeneficiaryData().setAddToMemCard("");
        txnDetailsData.getBeneficiaryData().setBeneficiaryNumber("");
        txnDetailsData.getBeneficiaryData().setOriginalTxnNumber("");
        txnDetailsData.getBeneficiaryData().setAddress("");


        return txnDetailsData;
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
        dest.writeString(this.rejectionMessage);
        dest.writeString(this.transferFlow);
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
        dest.writeString(this.aaeAccountNumber);
        dest.writeString(this.aaeIbanNumber);
        dest.writeString(this.url);
        dest.writeString(this.successUrl);
        dest.writeString(this.failureUrl);
        dest.writeParcelable(this.beneficiaryData, flags);
        dest.writeParcelable(this.transactionData, flags);
        dest.writeString(this.invoiceFlag);
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
         * CC_PK_ID : 0
         * CC_TXN_FK_ID : 1
         * CARD_HOLDER_NAME : Parveen Kumar Ji
         * CARD_NUM : 5123423432235423
         * CARD_TYPE : MASTER
         * CARD_REF_NUM : null
         * BANK_CODE : 9999080000017
         * BANK_NAME : MASHREQ BK CC
         * TXN_AMT : 960
         * CHARGES : 57.05
         * CHARGE_TO : null
         * TOTAL_AMOUNT : 112.05
         * CCY_CODE : 91
         * CCY_DESC : AED
         * CREATED_DATE : 1488455175000
         */

        @SerializedName("CC_PK_ID")
        private String id;
        @SerializedName("CC_TXN_FK_ID")
        private String ccTxnFkId;
        @SerializedName("CARD_HOLDER_NAME")
        private String cardHolderName;
        @SerializedName("CARD_NUM")
        private String cardNumber;
        @SerializedName("CARD_TYPE")
        private String cardType;
        @SerializedName("CARD_REF_NUM")
        private String cardRefNumber;
        @SerializedName("BANK_CODE")
        private String bankCode;
        @SerializedName("BANK_NAME")
        private String bankName;
        @SerializedName("TXN_AMT")
        private String txnAmount;
        @SerializedName("CHARGES")
        private String charges;
        @SerializedName("CHARGE_TO")
        private String chargeTo;
        @SerializedName("TOTAL_AMOUNT")
        private String totalAmount;
        @SerializedName("CCY_CODE")
        private String ccyCode;
        @SerializedName("CCY_DESC")
        private String ccyDesc;
        @SerializedName("CREATED_DATE")
        private String creadtedDate;

        public BeneficiaryData() {
        }

        protected BeneficiaryData(Parcel in) {
            this.id = in.readString();
            this.ccTxnFkId = in.readString();
            this.cardHolderName = in.readString();
            this.cardNumber = in.readString();
            this.cardType = in.readString();
            this.cardRefNumber = in.readString();
            this.bankCode = in.readString();
            this.bankName = in.readString();
            this.txnAmount = in.readString();
            this.charges = in.readString();
            this.chargeTo = in.readString();
            this.totalAmount = in.readString();
            this.ccyCode = in.readString();
            this.ccyDesc = in.readString();
            this.creadtedDate = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCcTxnFkId() {
            return ccTxnFkId;
        }

        public void setCcTxnFkId(String ccTxnFkId) {
            this.ccTxnFkId = ccTxnFkId;
        }

        public String getCardHolderName() {
            return cardHolderName;
        }

        public void setCardHolderName(String cardHolderName) {
            this.cardHolderName = cardHolderName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getCardRefNumber() {
            return cardRefNumber;
        }

        public void setCardRefNumber(String cardRefNumber) {
            this.cardRefNumber = cardRefNumber;
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

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public String getCharges() {
            return charges;
        }

        public void setCharges(String charges) {
            this.charges = charges;
        }

        public String getChargeTo() {
            return chargeTo;
        }

        public void setChargeTo(String chargeTo) {
            this.chargeTo = chargeTo;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getCcyCode() {
            return ccyCode;
        }

        public void setCcyCode(String ccyCode) {
            this.ccyCode = ccyCode;
        }

        public String getCcyDesc() {
            return ccyDesc;
        }

        public void setCcyDesc(String ccyDesc) {
            this.ccyDesc = ccyDesc;
        }

        public String getCreadtedDate() {
            return creadtedDate;
        }

        public void setCreadtedDate(String creadtedDate) {
            this.creadtedDate = creadtedDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.ccTxnFkId);
            dest.writeString(this.cardHolderName);
            dest.writeString(this.cardNumber);
            dest.writeString(this.cardType);
            dest.writeString(this.cardRefNumber);
            dest.writeString(this.bankCode);
            dest.writeString(this.bankName);
            dest.writeString(this.txnAmount);
            dest.writeString(this.charges);
            dest.writeString(this.chargeTo);
            dest.writeString(this.totalAmount);
            dest.writeString(this.ccyCode);
            dest.writeString(this.ccyDesc);
            dest.writeString(this.creadtedDate);
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
         * CASH_COL_HEAD_TXN_NO_PK_ID : 1
         * REM_TXN_FK_ID : 7
         * TXN_STATUS : PENDING
         * TXN_MODE : null
         * TXN_AMT : 960
         * BRANCH_CODE : null
         * REMARKS : null
         * APPROVAL_STATUS : A
         * CREATED_DATE : 1488455175000
         */

        @SerializedName("CASH_COL_HEAD_TXN_NO_PK_ID")
        private String id;
        @SerializedName("REM_TXN_FK_ID")
        private String remTxnFkId;
        @SerializedName("TXN_STATUS")
        private String txnStatus;
        @SerializedName("TXN_MODE")
        private String txnMode;
        @SerializedName("TXN_AMT")
        private String txnAmount;
        @SerializedName("BRANCH_CODE")
        private String branchCode;
        @SerializedName("REMARKS")
        private String remarks;
        @SerializedName("APPROVAL_STATUS")
        private String approvalStatus;
        @SerializedName("CREATED_DATE")
        private String createdDate;
        @SerializedName("DOC_NUMBER")
        private String doc_number;

        public TransactionData() {
        }

        protected TransactionData(Parcel in) {
            this.id = in.readString();
            this.remTxnFkId = in.readString();
            this.txnStatus = in.readString();
            this.txnMode = in.readString();
            this.txnAmount = in.readString();
            this.branchCode = in.readString();
            this.remarks = in.readString();
            this.approvalStatus = in.readString();
            this.createdDate = in.readString();
            this.doc_number = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemTxnFkId() {
            return remTxnFkId;
        }

        public void setRemTxnFkId(String remTxnFkId) {
            this.remTxnFkId = remTxnFkId;
        }

        public String getTxnStatus() {
            return txnStatus;
        }

        public void setTxnStatus(String txnStatus) {
            this.txnStatus = txnStatus;
        }

        public String getTxnMode() {
            return txnMode;
        }

        public void setTxnMode(String txnMode) {
            this.txnMode = txnMode;
        }

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.remTxnFkId);
            dest.writeString(this.txnStatus);
            dest.writeString(this.txnMode);
            dest.writeString(this.txnAmount);
            dest.writeString(this.branchCode);
            dest.writeString(this.remarks);
            dest.writeString(this.approvalStatus);
            dest.writeString(this.createdDate);
        }
    }
}
