package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 28 February, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TxnDetailsCeCashPayout implements Parcelable {

    public static final Parcelable.Creator<TxnDetailsCeCashPayout> CREATOR = new Parcelable.Creator<TxnDetailsCeCashPayout>() {
        @Override
        public TxnDetailsCeCashPayout createFromParcel(Parcel source) {
            return new TxnDetailsCeCashPayout(source);
        }

        @Override
        public TxnDetailsCeCashPayout[] newArray(int size) {
            return new TxnDetailsCeCashPayout[size];
        }
    };
    /**
     * REM_TXN_PK_ID : 5
     * GTW_USER_MSTR_FK_ID :
     * CE_MEM_CARD_FK_ID :
     * BENEFICIARY_ID :
     * CE_MEM_CARD_NUM :
     * BRANCH_CODE :
     * REM_TXN_REF_NUM :
     * TXN_CODE : 173375
     * TXN_STATUS : PENDING
     * TXN_REC_TYPE :
     * COST_PRICE :
     * TOTAL_TXN_AMT : 601
     * TOTAL_VALUE_AED :
     * RATE :
     * TXN_PAY_TYPE : BANK TRANSFER
     * TXN_EXP_LIMIT : 4
     * TXN_EXP_DESC : Expire in 4 Hours
     * TXN_TYPE : CP
     * CREATED_BY :
     * CREATED_DATE : 1487486548000
     * EXP_MODIFIED_BY :
     * EXP_MODIFIED_DATE :
     * MODIFIED_DATE :
     * MODIFIED_BY : null
     * URL : null
     * SUCCESS_URL : null
     * FAILURE_URL : null
     * CHANNEL_ID :
     * SERVICE_TYPE : CE
     * TRANSACTION_DATA : null
     * BENEFICIARY_DATA : null
     * TRANSACTION_DATA_TT : {"SEND_MONEY_CASH_PAYOUT_PK_ID":"14","GTW_REM_TXN_FK_ID":"5","SEND_AMOUNT":"12375.7","SEND_AMOUNT_USD":"null","SEND_AMOUNT_AED":"566","BENE_CHARGES":"35","BENE_CHARGES_USD":"null","BENE_CHARGES_AED":"35","TOTAL":"566","TOTAL_USD":"null","TOTAL_AED":"566","RATE":"21.87","LOCAL_CURR_AMOUNT_VALUE":"'","LOACL_CURR_CHARGES":"null","DISCOUNT":"null","CCY_CODE":"26","CCY_DESC":"INR","CREATER_NAME":"null","DEST_COUNTRY_CODE":"26","REMARKS":"null","CASH_COLLECTED_BY":"null","CASH_COLLECTOR_NAME":"null","TXN_MODE":"CP","FACTOR":"null","CREATED_DATE":"1487486548000","CREATED_BY":"null","STATUS":"null","CASH_REC":"null","BALANCE_TO_PAY":"'","AGENT_ORIGIN_BRANCH":"null","SUB_AGENT_CODE":"null","SUB_AGENT_NAME":"null","PO_COST_PRICE":"null","AGENT_COST_PRICE":"null","AE_TILL_NUMBER":"null","ORIGINATING_BRANCH_CODE":"'","ORIGINATING_BRANCH_NAME":"null","AGENT_VERIFICATION_STATUS":"null","PO_VERIFICATION_STATUS":"null","ACCOUNT_GENERATED":"null","AML_REJECT_REASON":"null","ADDITIONAL_SERVICES":"null","SOURCE_AGENT_CODE":"null","SOURCE_AGENT_NAME":"null","LOACL_CURR_CHARGES_USD":"null","LOACL_CURR_CHARGES_AED":"null","DOC_NUMBER":"null","BLOCKED_BY":"null","REQUEST_TYPE":"null","PO_TOTAL_AMT":"null","COMMISSION":"null","PO_LOCAL_CURR_AMOUNT_VALUE":"null","AGENT_REF_NUM":"null","CHARGE_TYPE":"null","PAYOUT_COMMISSION":"null","LOCAL_CCY_COST_PRICE":"null","PO_LOCAL_CURR_AMOUNT_VALUE_AED":"null","PO_LOCAL_CURR_AMOUNT_VALUE_USD":"null","PAYOUT_COMMISSION_AED":"null","PAYOUT_COMMISSION_USD":"null","DISCOUNT_USD":"null","DISCOUNT_AED":"null","COMMISSION_USD":"null","COMMISSION_AED":"null","LOCAL_CCY_TO_USD_CONV_RATE":"null","AED_TO_USD_CONV_RATE":"null","LOCAL_CURR_AMOUNT_VALUE_AED":"null","LOCAL_CURR_AMOUNT_VALUE_USD":"null","PO_TOTAL_AMT_AED":"null","PO_TOTAL_AMT_USD":"null","CREATED_DATE_AGENT_TIMEZONE":"null","TOTAL_FX_GENERATED":"null","DYNAMIC_TEMPLATE_ID":"null","SOURCE_COUNTRY_CODE":"null","SOURCE_COUNTRY_NAME":"null","SEND_CCY_USD_CONV_RATE":"null","CREATED_BRANCH_FK_ID":"null","ADDTL_SERVICE_ID":"null","WS_FAILURE_REASON":"null","WS_UPDATE_DATE":"null","AML_EDD_ASSIGNED_TO":"null","ANSARI_DRAW_MESSAGE":"null","SEND_AGENT_SHARE_FX":"null","RECEIVE_AGENT_SHARE_FX":"null","PO_SHARE_FX":"null","SETTLEMENT_TYPE":"null","RATES_APPROVAL_FK_ID":"null","WINDOW_TYPE":"null","PAID_FLAG":"null","VERIFIED_BY":"null","VERIFIED_DATE":"null","VERIFIER_NAME":"null","BLOCK_MODE":"null","CASH_COLLECTED_BRANCH":"null","SEND_FX_GAIN_SHARE_FLAG":"null","DEST_COUNTRY_NAME":"INDIA","PAYOUT_CCY":"null","PAYOUT_AGENT_CODE":"null","PAYOUT_AGENT_NAME":"null","PAYOUT_SUB_AGENT_CODE":"null","PAYOUT_SUB_AGENT_NAME":"null","FILE_NAME":"null","BNK_DEPOSIT_ACCOUNT_NUMBER":"null","BNK_DEPOSIT_BANK_CODE":"null","BNK_DEPOSIT_BANK_NAME":"null","BNK_DEPOSIT_BRANCH_CODE":"null","BNK_DEPOSIT_BRANCH_NAME":"null","BNK_DEPOSIT_BRANCH_ADDR":"null","DRAW_SCREEN_MESSAGE":null}
     * BENEFICIARY_DATA_TT : {"CASH_PAY_REC_PK_ID":"14","CASH_PAY_MAIN_REC_FK_ID":"14","RECEIVER_FULL_NAME":"IAN JON","BENF_FINAL_NAME":"IAN JON","BEN_ACC_NO":"1234567890123","BEN_ACCOUNT_TYPE":"0","BEN_BRANCH_NAME":"TEST CANARA BRANCH","BEN_BRANCH_CODE":"1","BANK_BRANCH_ADDR":"null","REC_COUNTRY_CODE":"26","REC_COUNTRY_NAME":"INDIA","BEN_NATIONALITY":"FILIPINO","REC_MOBILENO":"3456789012","REC_ADDRESS":"TEST BENEFICIARY ADDRESS","DOB":"null","PLACE_OF_BIRTH":"null","ACCOUNT_TYPE":"0","IBAN_NO":"null","RESIDENTIAL_STATUS":"null","MIDDLE_NAME":"null","FAMILY_NAME":"null","PROFESSION":"null","EMPLOYER_NAME":"null","BEN_EFT_CODE":"null","BANK_PURPOSE_CODE":"null","BANK_PURPOSE":"null","IDENTIFICATION_DTLS":"null","RESIDENCY_NUMBER":"null","BEN_EFT_TYPE":"null","PROOF_ID_EXP_DATE_BEN":"null","BEN_PLACE_OF_ISSUE":"null","BEN_ID_EXPIRY_DATE":"null","BEN_COM_REG_NO":"null","BEN_COMM_REG_PLACE_OF_ISSUE":"null","BEN_TYPE_OF_ACTIVITY_KEY":"null","BEN_PURPOSE_OF_TXN_KEY":"null","BENEFICIARY_TYPE_ID":"null","BENEFICIARY_TYPE_DESC":"null","REC_FIRST_NAME":"null","REC_SECOND_NAME":"null","REC_THIRD_NAME":"null","REC_LAST_NAME":"null","REC_ISD_PREFIX":"null","MESSAGE_TO_REC":"null","BANK_CHANNEL_CODE":null}
     */
    @SerializedName("REM_TXN_PK_ID")
    private String id;
    @SerializedName("GTW_USER_MSTR_FK_ID")
    private String gtwUserMstrFkId;
    @SerializedName("CE_MEM_CARD_FK_ID")
    private String ceMemCardFkId;
    @SerializedName("BENEFICIARY_ID")
    private String beneficiaryId;
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
    @SerializedName("REJECTION_MESSAGE")
    private String rejectionMessage;
    @SerializedName("BANK_TRANSFER_FLOW")
    private String transferFlow;
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
    private String CHANNEL_ID;
    @SerializedName("SERVICE_TYPE")
    private String serviceType;
    @SerializedName("TRANSACTION_DATA")
    private TransactionData transactionData;
    @SerializedName("BENEFICIARY_DATA")
    private BeneficiaryData beneficiaryData;
    @SerializedName("TRANSACTION_DATA_TT")
    private TransactionDataTt transactionDataTt;
    @SerializedName("BENEFICIARY_DATA_TT")
    private BeneficiaryDataTt beneficiaryDataTt;
    @SerializedName("INVOICE_FLAG")
    private String invoiceFlag;


    public TxnDetailsCeCashPayout() {
    }

    protected TxnDetailsCeCashPayout(Parcel in) {
        this.id = in.readString();
        this.gtwUserMstrFkId = in.readString();
        this.ceMemCardFkId = in.readString();
        this.beneficiaryId = in.readString();
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
        this.createdBy = in.readString();
        this.createdDate = in.readString();
        this.expModifiedBy = in.readString();
        this.expModifiedDate = in.readString();
        this.modifiedDate = in.readString();
        this.modifiedBy = in.readString();
        this.url = in.readString();
        this.successUrl = in.readString();
        this.failureUrl = in.readString();
        this.CHANNEL_ID = in.readString();
        this.serviceType = in.readString();
        this.invoiceFlag=in.readString();
        this.transactionData = in.readParcelable(TransactionData.class.getClassLoader());
        this.beneficiaryData = in.readParcelable(BeneficiaryData.class.getClassLoader());
        this.transactionDataTt = in.readParcelable(TransactionDataTt.class.getClassLoader());
        this.beneficiaryDataTt = in.readParcelable(BeneficiaryDataTt.class.getClassLoader());
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
        txnDetailsData.setAaeIbanNumber(aaeIbanNumber);
        txnDetailsData.setRejectionMessage(rejectionMessage);
        txnDetailsData.setTransferFlow(transferFlow);
        txnDetailsData.setTxnRecType(txnType);
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
        txnDetailsData.setUrl(url);
        txnDetailsData.setSuccessUrl(successUrl);
        txnDetailsData.setFailureUrl(failureUrl);
        txnDetailsData.setRejectionMessage(rejectionMessage);
        txnDetailsData.setInvoiceFlag(invoiceFlag);

        if (txnType.equalsIgnoreCase("BT")) {
            txnDetailsData.setTxnType("DT");
            // Transaction Data
            txnDetailsData.setTransactionData(new TxnDetailsData.TransactionData());
            txnDetailsData.getTransactionData().setRemDtSendPkId(transactionData.id);
            txnDetailsData.getTransactionData().setRemTxnFkId(transactionData.gtwRemTxnFkId);
            txnDetailsData.getTransactionData().setTxnAmount(transactionData.sendAmount);
            txnDetailsData.getTransactionData().setChargeAmount(transactionData.benfCharges);
            txnDetailsData.getTransactionData().setCommAmount("");
            txnDetailsData.getTransactionData().setDiscAmount("");
            txnDetailsData.getTransactionData().setNetTotal(transactionData.total);
            txnDetailsData.getTransactionData().setRate(transactionData.rate);
            txnDetailsData.getTransactionData().setTotalValueAed(transactionData.totalAED);
            txnDetailsData.getTransactionData().setRoundedOffAmount("");
            txnDetailsData.getTransactionData().setCcyCode(transactionData.ccyCode);
            txnDetailsData.getTransactionData().setCcyName(transactionData.ccyName);
            txnDetailsData.getTransactionData().setSaleType("");
            txnDetailsData.getTransactionData().setCostPrice("");
            txnDetailsData.getTransactionData().setBankCode(transactionData.getBenBankCode());
            txnDetailsData.getTransactionData().setBankName(transactionData.getBenBankName());
            txnDetailsData.getTransactionData().setBranchCode(beneficiaryData.getBenBranchCode());
            txnDetailsData.getTransactionData().setBranchType("");
            txnDetailsData.getTransactionData().setBranchName(beneficiaryData.getBenBranchName());
            txnDetailsData.getTransactionData().setCreatedBy("");
            txnDetailsData.getTransactionData().setCreatedDate(transactionData.createdDate);
            txnDetailsData.getTransactionData().setStatus(transactionData.status);

            if(transactionData != null) {
                if(transactionData.doc_number != null) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }
                /*if(!transactionData.doc_number.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }*/
            } else if(transactionDataTt != null) {
                if(transactionDataTt.DOC_NUMBER != null) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
                /*if(!transactionDataTt.DOC_NUMBER.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }*/
            }

            /*if(transactionData.doc_number != null) {
                if(!transactionData.doc_number.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                } else if(!transactionDataTt.DOC_NUMBER.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
            }*/

           /* if( txnDetailsData.getTransactionData().getDoc_number()!=null) {
                if(transactionData.doc_number != "--")

                if(txnDetailsData.getTransactionData().getDoc_number().equalsIgnoreCase("--")){
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }else {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
            }*/

            // Beneficiary Data (Cash Payout)
            txnDetailsData.setBeneficiaryData(new TxnDetailsData.BeneficiaryData());
            txnDetailsData.getBeneficiaryData().setId(beneficiaryData.id);
            txnDetailsData.getBeneficiaryData().setRemDtSendFkId(beneficiaryData.sendBankTransferFkId);
            txnDetailsData.getBeneficiaryData().setIdTt(beneficiaryData.id);
            txnDetailsData.getBeneficiaryData().setRemDtSendFkIdTt(beneficiaryData.sendBankTransferFkId);
            txnDetailsData.getBeneficiaryData().setBeneficiaryName(beneficiaryData.receiverFullName != null ? beneficiaryData.receiverFullName : beneficiaryData.benfFinalName);
            txnDetailsData.getBeneficiaryData().setBeneficiaryNameAr(beneficiaryData.receiverFullName != null ? beneficiaryData.receiverFullName : beneficiaryData.benfFinalName);
            txnDetailsData.getBeneficiaryData().setAccountNumber(beneficiaryData.benfAccNo);
            txnDetailsData.getBeneficiaryData().setBankName(transactionData.getBenBankName());
            txnDetailsData.getBeneficiaryData().setBranchName(beneficiaryData.benBranchName);
            txnDetailsData.getBeneficiaryData().setPayMode(txnPayType);
            txnDetailsData.getBeneficiaryData().setCountryCode(beneficiaryData.benCountryCode);
            txnDetailsData.getBeneficiaryData().setCountryName(beneficiaryData.benCountryName);
            txnDetailsData.getBeneficiaryData().setNationality(beneficiaryData.benNationality);
            txnDetailsData.getBeneficiaryData().setNationalityCode("");
            txnDetailsData.getBeneficiaryData().setMobileNumber(beneficiaryData.benMobileNo);
            txnDetailsData.getBeneficiaryData().setAddToMemCard("");
            txnDetailsData.getBeneficiaryData().setBeneficiaryNumber("");
            txnDetailsData.getBeneficiaryData().setOriginalTxnNumber("");
            txnDetailsData.getBeneficiaryData().setAddress(beneficiaryData.benAddress);

        } else {
            txnDetailsData.setTxnType("TT");
            // Transaction Data
            txnDetailsData.setTransactionData((new TxnDetailsData.TransactionData()));
            txnDetailsData.getTransactionData().setRemDtSendPkId(transactionDataTt.sendMoneyCashPayoutPkId);
            txnDetailsData.getTransactionData().setRemTxnFkId(transactionDataTt.gtwRemTxnFkId);
            txnDetailsData.getTransactionData().setTxnAmount(transactionDataTt.sendAmount);
            txnDetailsData.getTransactionData().setChargeAmount(transactionDataTt.benfCharges);
            txnDetailsData.getTransactionData().setCommAmount("");
            txnDetailsData.getTransactionData().setDiscAmount("");
            txnDetailsData.getTransactionData().setNetTotal(transactionDataTt.total);
            txnDetailsData.getTransactionData().setRate(transactionDataTt.rate);
            txnDetailsData.getTransactionData().setTotalValueAed(transactionDataTt.totalAED);
            txnDetailsData.getTransactionData().setRoundedOffAmount("");
            txnDetailsData.getTransactionData().setCcyCode(transactionDataTt.ccyCode);
            txnDetailsData.getTransactionData().setCcyName(transactionDataTt.ccyName);
            txnDetailsData.getTransactionData().setSaleType("");
            txnDetailsData.getTransactionData().setCostPrice("");
            txnDetailsData.getTransactionData().setBankCode(transactionDataTt.getPAYOUT_AGENT_CODE());
            txnDetailsData.getTransactionData().setBankName(transactionDataTt.getPAYOUT_AGENT_NAME());
            txnDetailsData.getTransactionData().setBranchCode(beneficiaryDataTt.getBenBranchCode());
            txnDetailsData.getTransactionData().setBranchType("");
            txnDetailsData.getTransactionData().setBranchName(beneficiaryDataTt.getBenBranchName());
            txnDetailsData.getTransactionData().setCreatedBy("");
            txnDetailsData.getTransactionData().setCreatedDate(transactionDataTt.createdDate);
            txnDetailsData.getTransactionData().setStatus(transactionDataTt.status);
            //txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);

            if(transactionData != null) {
                if(transactionData.doc_number != null) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }
                /*if(!transactionData.doc_number.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }*/
            } else if(transactionDataTt != null) {
                if(transactionDataTt.DOC_NUMBER != null) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
                /*if(!transactionDataTt.DOC_NUMBER.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }*/
            }

            /*if(transactionData.doc_number != null) {
                if(!transactionData.doc_number.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                } else if(!transactionDataTt.DOC_NUMBER.equals("--")) {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
            }*/

           /* if( txnDetailsData.getTransactionData().getDoc_number()!=null) {
                if(txnDetailsData.getTransactionData().getDoc_number().equalsIgnoreCase("--")){
                    txnDetailsData.getTransactionData().setDoc_number(transactionData.doc_number);
                }else {
                    txnDetailsData.getTransactionData().setDoc_number(transactionDataTt.DOC_NUMBER);
                }
            }*/
            // Beneficiary Data (Cash Payout)
            txnDetailsData.setBeneficiaryData(new TxnDetailsData.BeneficiaryData());
            txnDetailsData.getBeneficiaryData().setId(beneficiaryDataTt.cashPayRecPkId);
            txnDetailsData.getBeneficiaryData().setRemDtSendFkId(beneficiaryDataTt.cashPayMainRecFkId);
            txnDetailsData.getBeneficiaryData().setIdTt(beneficiaryDataTt.cashPayRecPkId);
            txnDetailsData.getBeneficiaryData().setRemDtSendFkIdTt(beneficiaryDataTt.cashPayMainRecFkId);
            txnDetailsData.getBeneficiaryData().setBeneficiaryName(beneficiaryDataTt.receiverFullName != null ? beneficiaryDataTt.receiverFullName : beneficiaryDataTt.benfFinalName);
            txnDetailsData.getBeneficiaryData().setBeneficiaryNameAr(beneficiaryDataTt.receiverFullName != null ? beneficiaryDataTt.receiverFullName : beneficiaryDataTt.benfFinalName);
            txnDetailsData.getBeneficiaryData().setAccountNumber(beneficiaryDataTt.benfAccNo);
            txnDetailsData.getBeneficiaryData().setBankName(transactionDataTt.getPAYOUT_AGENT_NAME());
            txnDetailsData.getBeneficiaryData().setBranchName(beneficiaryDataTt.benBranchName);
            txnDetailsData.getBeneficiaryData().setPayMode(txnPayType);
            txnDetailsData.getBeneficiaryData().setCountryCode(beneficiaryDataTt.recCountryCode);
            txnDetailsData.getBeneficiaryData().setCountryName(beneficiaryDataTt.recCountryName);
            txnDetailsData.getBeneficiaryData().setNationality(beneficiaryDataTt.benNationality);
            txnDetailsData.getBeneficiaryData().setNationalityCode("");
            txnDetailsData.getBeneficiaryData().setMobileNumber(beneficiaryDataTt.recMobileNo);
            txnDetailsData.getBeneficiaryData().setAddToMemCard("");
            txnDetailsData.getBeneficiaryData().setBeneficiaryNumber("");
            txnDetailsData.getBeneficiaryData().setOriginalTxnNumber("");
            txnDetailsData.getBeneficiaryData().setAddress(beneficiaryDataTt.recAddress);
        }
        return txnDetailsData;
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

    public String getCeMemCardFkId() {
        return ceMemCardFkId;
    }

    public void setCeMemCardFkId(String ceMemCardFkId) {
        this.ceMemCardFkId = ceMemCardFkId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public TransactionData getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    public BeneficiaryData getBeneficiaryData() {
        return beneficiaryData;
    }

    public void setBeneficiaryData(BeneficiaryData beneficiaryData) {
        this.beneficiaryData = beneficiaryData;
    }

    public TransactionDataTt getTransactionDataTt() {
        return transactionDataTt;
    }

    public void setTransactionDataTt(TransactionDataTt transactionDataTt) {
        this.transactionDataTt = transactionDataTt;
    }

    public BeneficiaryDataTt getBeneficiaryDataTt() {
        return beneficiaryDataTt;
    }

    public void setBeneficiaryDataTt(BeneficiaryDataTt beneficiaryDataTt) {
        this.beneficiaryDataTt = beneficiaryDataTt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.gtwUserMstrFkId);
        dest.writeString(this.ceMemCardFkId);
        dest.writeString(this.beneficiaryId);
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
        dest.writeString(this.createdBy);
        dest.writeString(this.createdDate);
        dest.writeString(this.expModifiedBy);
        dest.writeString(this.expModifiedDate);
        dest.writeString(this.modifiedDate);
        dest.writeString(this.modifiedBy);
        dest.writeString(this.url);
        dest.writeString(this.successUrl);
        dest.writeString(this.failureUrl);
        dest.writeString(this.CHANNEL_ID);
        dest.writeString(this.serviceType);
        dest.writeString(this.invoiceFlag);
        dest.writeParcelable(this.transactionData, flags);
        dest.writeParcelable(this.beneficiaryData, flags);
        dest.writeParcelable(this.transactionDataTt, flags);
        dest.writeParcelable(this.beneficiaryDataTt, flags);
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
         * SEND_MONEY_BANK_TRANSFER_PK_ID : 1
         * GTW_REM_TXN_FK_ID : 18
         * SEND_AMOUNT : 667.132
         * SEND_AMOUNT_USD : null
         * SEND_AMOUNT_AED : 150.04
         * BENE_CHARGES : 28.04
         * TOTAL : 150.04
         * RATE : 5.47
         * TOTAL_AED : 122
         * DISCOUNT_AED : null
         * DISCOUNT_USD : null
         * COMMISSION_USD : null
         * COMMISSION_AED : null
         * CCY_CODE : 91
         * CCY_DESC : null
         * BEN_BANK_CODE : 5649
         * BEN_BANK_NAME : NAGHEDI NIA EXCHANGE
         * SOURCE_AGENT_NAME : null
         * ROUTING_BANK_CODE : null
         * ROUTING_BANK_NAME : null
         * CREATED_BY : null
         * CREATED_DATE : 1488353910579
         * TXN_MODE : DT
         * SERVICE_ID : 2
         * COUNTRY_CODE : 91
         * COUNTRY_NAME : UAE
         * SOURCE_COUNTRY_CODE : null
         * SOURCE_AGENT_CODE : null
         * BENE_CHARGES_AED : 28.04
         * DISCOUNT : null
         * CASH_REC : null
         * REMARKS : null
         * BENE_CHARGES_USD : null
         * TOTAL_USD : null
         * COMMISSION : null
         * AGENT_RECEIVED_DATE : null
         * STATUS : null
         */

        @SerializedName("SEND_MONEY_BANK_TRANSFER_PK_ID")
        private String id;
        @SerializedName("GTW_REM_TXN_FK_ID")
        private String gtwRemTxnFkId;
        @SerializedName("SEND_AMOUNT")
        private String sendAmount;
        @SerializedName("SEND_AMOUNT_USD")
        private String sendAmountUSD;
        @SerializedName("SEND_AMOUNT_AED")
        private String sendAmountAED;
        @SerializedName("BENE_CHARGES")
        private String benfCharges;
        @SerializedName("TOTAL")
        private String total;
        @SerializedName("RATE")
        private String rate;
        @SerializedName("TOTAL_AED")
        private String totalAED;
        @SerializedName("DISCOUNT_AED")
        private String discountAED;
        @SerializedName("DISCOUNT_USD")
        private String discountUSD;
        @SerializedName("COMMISSION_USD")
        private String commissionUSD;
        @SerializedName("COMMISSION_AED")
        private String commissionAED;
        @SerializedName("CCY_CODE")
        private String ccyCode;
        @SerializedName("CCY_DESC")
        private String ccyName;
        @SerializedName("BEN_BANK_CODE")
        private String benBankCode;
        @SerializedName("BEN_BANK_NAME")
        private String benBankName;
        @SerializedName("SOURCE_AGENT_NAME")
        private String sourceAgentName;
        @SerializedName("ROUTING_BANK_CODE")
        private String routingBankCode;
        @SerializedName("ROUTING_BANK_NAME")
        private String routingBankName;
        @SerializedName("CREATED_BY")
        private String createdBy;
        @SerializedName("CREATED_DATE")
        private String createdDate;
        @SerializedName("TXN_MODE")
        private String txnMode;
        @SerializedName("SERVICE_ID")
        private String serviceId;
        @SerializedName("COUNTRY_CODE")
        private String countryCode;
        @SerializedName("COUNTRY_NAME")
        private String countryName;
        @SerializedName("SOURCE_AGENT_CODE")
        private String sourceAgentCode;
        @SerializedName("BENE_CHARGES_AED")
        private String beneChargesAED;
        @SerializedName("DISCOUNT")
        private String discount;
        @SerializedName("CASH_REC")
        private String cashRec;
        @SerializedName("REMARKS")
        private String remarks;
        @SerializedName("BENE_CHARGES_USD")
        private String beneChargesUSD;
        @SerializedName("TOTAL_USD")
        private String totalUSD;
        @SerializedName("COMMISSION")
        private String commission;
        @SerializedName("AGENT_RECEIVED_DATE")
        private String agentReceivedDate;
        @SerializedName("STATUS")
        private String status;
        @SerializedName("TXN_AMT")
        private String txnAmount;
        @SerializedName("DOC_NUMBER")
        private String doc_number;

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public TransactionData() {
        }

        protected TransactionData(Parcel in) {
            this.id = in.readString();
            this.gtwRemTxnFkId = in.readString();
            this.sendAmount = in.readString();
            this.sendAmountUSD = in.readString();
            this.sendAmountAED = in.readString();
            this.benfCharges = in.readString();
            this.total = in.readString();
            this.rate = in.readString();
            this.totalAED = in.readString();
            this.discountAED = in.readString();
            this.discountUSD = in.readString();
            this.commissionUSD = in.readString();
            this.commissionAED = in.readString();
            this.ccyCode = in.readString();
            this.ccyName = in.readString();
            this.benBankCode = in.readString();
            this.benBankName = in.readString();
            this.sourceAgentName = in.readString();
            this.routingBankCode = in.readString();
            this.routingBankName = in.readString();
            this.createdBy = in.readString();
            this.createdDate = in.readString();
            this.txnMode = in.readString();
            this.serviceId = in.readString();
            this.countryCode = in.readString();
            this.countryName = in.readString();
            this.sourceAgentCode = in.readString();
            this.beneChargesAED = in.readString();
            this.discount = in.readString();
            this.cashRec = in.readString();
            this.remarks = in.readString();
            this.beneChargesUSD = in.readString();
            this.totalUSD = in.readString();
            this.commission = in.readString();
            this.agentReceivedDate = in.readString();
            this.status = in.readString();
            this.txnAmount=in.readString();
            this.doc_number = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGtwRemTxnFkId() {
            return gtwRemTxnFkId;
        }

        public void setGtwRemTxnFkId(String gtwRemTxnFkId) {
            this.gtwRemTxnFkId = gtwRemTxnFkId;
        }

        public String getSendAmount() {
            return sendAmount;
        }

        public void setSendAmount(String sendAmount) {
            this.sendAmount = sendAmount;
        }

        public String getSendAmountUSD() {
            return sendAmountUSD;
        }

        public void setSendAmountUSD(String sendAmountUSD) {
            this.sendAmountUSD = sendAmountUSD;
        }

        public String getSendAmountAED() {
            return sendAmountAED;
        }

        public void setSendAmountAED(String sendAmountAED) {
            this.sendAmountAED = sendAmountAED;
        }

        public String getBenfCharges() {
            return benfCharges;
        }

        public void setBenfCharges(String benfCharges) {
            this.benfCharges = benfCharges;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTotalAED() {
            return totalAED;
        }

        public void setTotalAED(String totalAED) {
            this.totalAED = totalAED;
        }

        public String getDiscountAED() {
            return discountAED;
        }

        public void setDiscountAED(String discountAED) {
            this.discountAED = discountAED;
        }

        public String getDiscountUSD() {
            return discountUSD;
        }

        public void setDiscountUSD(String discountUSD) {
            this.discountUSD = discountUSD;
        }

        public String getCommissionUSD() {
            return commissionUSD;
        }

        public void setCommissionUSD(String commissionUSD) {
            this.commissionUSD = commissionUSD;
        }

        public String getCommissionAED() {
            return commissionAED;
        }

        public void setCommissionAED(String commissionAED) {
            this.commissionAED = commissionAED;
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

        public String getBenBankCode() {
            return benBankCode;
        }

        public void setBenBankCode(String benBankCode) {
            this.benBankCode = benBankCode;
        }

        public String getBenBankName() {
            return benBankName;
        }

        public void setBenBankName(String benBankName) {
            this.benBankName = benBankName;
        }

        public String getSourceAgentName() {
            return sourceAgentName;
        }

        public void setSourceAgentName(String sourceAgentName) {
            this.sourceAgentName = sourceAgentName;
        }

        public String getRoutingBankCode() {
            return routingBankCode;
        }

        public void setRoutingBankCode(String routingBankCode) {
            this.routingBankCode = routingBankCode;
        }

        public String getRoutingBankName() {
            return routingBankName;
        }

        public void setRoutingBankName(String routingBankName) {
            this.routingBankName = routingBankName;
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

        public String getTxnMode() {
            return txnMode;
        }

        public void setTxnMode(String txnMode) {
            this.txnMode = txnMode;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
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

        public String getSourceAgentCode() {
            return sourceAgentCode;
        }

        public void setSourceAgentCode(String sourceAgentCode) {
            this.sourceAgentCode = sourceAgentCode;
        }

        public String getBeneChargesAED() {
            return beneChargesAED;
        }

        public void setBeneChargesAED(String beneChargesAED) {
            this.beneChargesAED = beneChargesAED;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getCashRec() {
            return cashRec;
        }

        public void setCashRec(String cashRec) {
            this.cashRec = cashRec;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getBeneChargesUSD() {
            return beneChargesUSD;
        }

        public void setBeneChargesUSD(String beneChargesUSD) {
            this.beneChargesUSD = beneChargesUSD;
        }

        public String getTotalUSD() {
            return totalUSD;
        }

        public void setTotalUSD(String totalUSD) {
            this.totalUSD = totalUSD;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getAgentReceivedDate() {
            return agentReceivedDate;
        }

        public void setAgentReceivedDate(String agentReceivedDate) {
            this.agentReceivedDate = agentReceivedDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.gtwRemTxnFkId);
            dest.writeString(this.sendAmount);
            dest.writeString(this.sendAmountUSD);
            dest.writeString(this.sendAmountAED);
            dest.writeString(this.benfCharges);
            dest.writeString(this.total);
            dest.writeString(this.rate);
            dest.writeString(this.totalAED);
            dest.writeString(this.discountAED);
            dest.writeString(this.discountUSD);
            dest.writeString(this.commissionUSD);
            dest.writeString(this.commissionAED);
            dest.writeString(this.ccyCode);
            dest.writeString(this.ccyName);
            dest.writeString(this.benBankCode);
            dest.writeString(this.benBankName);
            dest.writeString(this.sourceAgentName);
            dest.writeString(this.routingBankCode);
            dest.writeString(this.routingBankName);
            dest.writeString(this.createdBy);
            dest.writeString(this.createdDate);
            dest.writeString(this.txnMode);
            dest.writeString(this.serviceId);
            dest.writeString(this.countryCode);
            dest.writeString(this.countryName);
            dest.writeString(this.sourceAgentCode);
            dest.writeString(this.beneChargesAED);
            dest.writeString(this.discount);
            dest.writeString(this.cashRec);
            dest.writeString(this.remarks);
            dest.writeString(this.beneChargesUSD);
            dest.writeString(this.totalUSD);
            dest.writeString(this.commission);
            dest.writeString(this.agentReceivedDate);
            dest.writeString(this.status);
            dest.writeString(this.txnAmount);

        }
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
         * BANK_TRANSFER_BEN_PK_ID : 1
         * SEND_BANK_TRANSFER_FK_ID : 1
         * RECEIVER_FULL_NAME : SUZAN FATHALLA MOHAMED HABIB
         * BENF_FINAL_NAME : SUZAN FATHALLA MOHAMED HABIB
         * BEN_ACCOUNT_TYPE : null
         * BEN_ACC_NO : null
         * IBAN_NO : null
         * BEN_MOBILENO : null
         * BEN_ADDRESS : KISH ISLAND
         * ACCOUNT_TYPE : null
         * BEN_BRANCH_NAME : KISH
         * BEN_BRANCH_CODE : null
         * BANK_BRANCH_ADDR :
         * BEN_COUNTRY_CODE : 91
         * BEN_COUNTRY_NAME : UNITED ARAB EMIRATES
         * BEN_NATIONALITY : null
         * DOB : null
         * BEN_ISD_PREFIX :
         */

        @SerializedName("BANK_TRANSFER_BEN_PK_ID")
        private String id;
        @SerializedName("SEND_BANK_TRANSFER_FK_ID")
        private String sendBankTransferFkId;
        @SerializedName("RECEIVER_FULL_NAME")
        private String receiverFullName;
        @SerializedName("BENF_FINAL_NAME")
        private String benfFinalName;
        @SerializedName("BEN_ACCOUNT_TYPE")
        private String benAccountType;
        @SerializedName("BEN_ACC_NO")
        private String benfAccNo;
        @SerializedName("IBAN_NO")
        private String ibanNo;
        @SerializedName("BEN_MOBILENO")
        private String benMobileNo;
        @SerializedName("BEN_ADDRESS")
        private String benAddress;
        @SerializedName("ACCOUNT_TYPE")
        private String accountType;
        @SerializedName("BEN_BRANCH_NAME")
        private String benBranchName;
        @SerializedName("BEN_BRANCH_CODE")
        private String benBranchCode;
        @SerializedName("BANK_BRANCH_ADDR")
        private String benBranchAddr;
        @SerializedName("BEN_COUNTRY_CODE")
        private String benCountryCode;
        @SerializedName("BEN_COUNTRY_NAME")
        private String benCountryName;
        @SerializedName("BEN_NATIONALITY")
        private String benNationality;
        @SerializedName("DOB")
        private String dob;
        @SerializedName("BEN_ISD_PREFIX")
        private String benIsdPrefix;

        public BeneficiaryData() {
        }

        protected BeneficiaryData(Parcel in) {
            this.id = in.readString();
            this.sendBankTransferFkId = in.readString();
            this.receiverFullName = in.readString();
            this.benfFinalName = in.readString();
            this.benAccountType = in.readString();
            this.benfAccNo = in.readString();
            this.ibanNo = in.readString();
            this.benMobileNo = in.readString();
            this.benAddress = in.readString();
            this.accountType = in.readString();
            this.benBranchName = in.readString();
            this.benBranchCode = in.readString();
            this.benBranchAddr = in.readString();
            this.benCountryCode = in.readString();
            this.benCountryName = in.readString();
            this.benNationality = in.readString();
            this.dob = in.readString();
            this.benIsdPrefix = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSendBankTransferFkId() {
            return sendBankTransferFkId;
        }

        public void setSendBankTransferFkId(String sendBankTransferFkId) {
            this.sendBankTransferFkId = sendBankTransferFkId;
        }

        public String getReceiverFullName() {
            return receiverFullName;
        }

        public void setReceiverFullName(String receiverFullName) {
            this.receiverFullName = receiverFullName;
        }

        public String getBenfFinalName() {
            return benfFinalName;
        }

        public void setBenfFinalName(String benfFinalName) {
            this.benfFinalName = benfFinalName;
        }

        public String getBenAccountType() {
            return benAccountType;
        }

        public void setBenAccountType(String benAccountType) {
            this.benAccountType = benAccountType;
        }

        public String getBenfAccNo() {
            return benfAccNo;
        }

        public void setBenfAccNo(String benfAccNo) {
            this.benfAccNo = benfAccNo;
        }

        public String getIbanNo() {
            return ibanNo;
        }

        public void setIbanNo(String ibanNo) {
            this.ibanNo = ibanNo;
        }

        public String getBenMobileNo() {
            return benMobileNo;
        }

        public void setBenMobileNo(String benMobileNo) {
            this.benMobileNo = benMobileNo;
        }

        public String getBenAddress() {
            return benAddress;
        }

        public void setBenAddress(String benAddress) {
            this.benAddress = benAddress;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBenBranchName() {
            return benBranchName;
        }

        public void setBenBranchName(String benBranchName) {
            this.benBranchName = benBranchName;
        }

        public String getBenBranchCode() {
            return benBranchCode;
        }

        public void setBenBranchCode(String benBranchCode) {
            this.benBranchCode = benBranchCode;
        }

        public String getBenBranchAddr() {
            return benBranchAddr;
        }

        public void setBenBranchAddr(String benBranchAddr) {
            this.benBranchAddr = benBranchAddr;
        }

        public String getBenCountryCode() {
            return benCountryCode;
        }

        public void setBenCountryCode(String benCountryCode) {
            this.benCountryCode = benCountryCode;
        }

        public String getBenCountryName() {
            return benCountryName;
        }

        public void setBenCountryName(String benCountryName) {
            this.benCountryName = benCountryName;
        }

        public String getBenNationality() {
            return benNationality;
        }

        public void setBenNationality(String benNationality) {
            this.benNationality = benNationality;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getBenIsdPrefix() {
            return benIsdPrefix;
        }

        public void setBenIsdPrefix(String benIsdPrefix) {
            this.benIsdPrefix = benIsdPrefix;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.sendBankTransferFkId);
            dest.writeString(this.receiverFullName);
            dest.writeString(this.benfFinalName);
            dest.writeString(this.benAccountType);
            dest.writeString(this.benfAccNo);
            dest.writeString(this.ibanNo);
            dest.writeString(this.benMobileNo);
            dest.writeString(this.benAddress);
            dest.writeString(this.accountType);
            dest.writeString(this.benBranchName);
            dest.writeString(this.benBranchCode);
            dest.writeString(this.benBranchAddr);
            dest.writeString(this.benCountryCode);
            dest.writeString(this.benCountryName);
            dest.writeString(this.benNationality);
            dest.writeString(this.dob);
            dest.writeString(this.benIsdPrefix);

        }
    }


    public static class TransactionDataTt implements Parcelable {

        public static final Creator<TransactionDataTt> CREATOR = new Creator<TransactionDataTt>() {
            @Override
            public TransactionDataTt createFromParcel(Parcel source) {
                return new TransactionDataTt(source);
            }

            @Override
            public TransactionDataTt[] newArray(int size) {
                return new TransactionDataTt[size];
            }
        };
        /**
         * SEND_MONEY_CASH_PAYOUT_PK_ID : 14
         * GTW_REM_TXN_FK_ID : 5
         * SEND_AMOUNT : 12375.7
         * SEND_AMOUNT_USD : null
         * SEND_AMOUNT_AED : 566
         * BENE_CHARGES : 35
         * BENE_CHARGES_USD : null
         * BENE_CHARGES_AED : 35
         * TOTAL : 566
         * TOTAL_USD : null
         * TOTAL_AED : 566
         * RATE : 21.87
         * LOCAL_CURR_AMOUNT_VALUE : '
         * LOACL_CURR_CHARGES : null
         * DISCOUNT : null
         * CCY_CODE : 26
         * CCY_DESC : INR
         * CREATER_NAME : null
         * DEST_COUNTRY_CODE : 26
         * REMARKS : null
         * CASH_COLLECTED_BY : null
         * CASH_COLLECTOR_NAME : null
         * TXN_MODE : CP
         * FACTOR : null
         * CREATED_DATE : 1487486548000
         * CREATED_BY : null
         * STATUS : null
         * CASH_REC : null
         * BALANCE_TO_PAY : '
         * AGENT_ORIGIN_BRANCH : null
         * SUB_AGENT_CODE : null
         * SUB_AGENT_NAME : null
         * PO_COST_PRICE : null
         * AGENT_COST_PRICE : null
         * AE_TILL_NUMBER : null
         * ORIGINATING_BRANCH_CODE : '
         * ORIGINATING_BRANCH_NAME : null
         * AGENT_VERIFICATION_STATUS : null
         * PO_VERIFICATION_STATUS : null
         * ACCOUNT_GENERATED : null
         * AML_REJECT_REASON : null
         * ADDITIONAL_SERVICES : null
         * SOURCE_AGENT_CODE : null
         * SOURCE_AGENT_NAME : null
         * LOACL_CURR_CHARGES_USD : null
         * LOACL_CURR_CHARGES_AED : null
         * DOC_NUMBER : null
         * BLOCKED_BY : null
         * REQUEST_TYPE : null
         * PO_TOTAL_AMT : null
         * COMMISSION : null
         * PO_LOCAL_CURR_AMOUNT_VALUE : null
         * AGENT_REF_NUM : null
         * CHARGE_TYPE : null
         * PAYOUT_COMMISSION : null
         * LOCAL_CCY_COST_PRICE : null
         * PO_LOCAL_CURR_AMOUNT_VALUE_AED : null
         * PO_LOCAL_CURR_AMOUNT_VALUE_USD : null
         * PAYOUT_COMMISSION_AED : null
         * PAYOUT_COMMISSION_USD : null
         * DISCOUNT_USD : null
         * DISCOUNT_AED : null
         * COMMISSION_USD : null
         * COMMISSION_AED : null
         * LOCAL_CCY_TO_USD_CONV_RATE : null
         * AED_TO_USD_CONV_RATE : null
         * LOCAL_CURR_AMOUNT_VALUE_AED : null
         * LOCAL_CURR_AMOUNT_VALUE_USD : null
         * PO_TOTAL_AMT_AED : null
         * PO_TOTAL_AMT_USD : null
         * CREATED_DATE_AGENT_TIMEZONE : null
         * TOTAL_FX_GENERATED : null
         * DYNAMIC_TEMPLATE_ID : null
         * SOURCE_COUNTRY_CODE : null
         * SOURCE_COUNTRY_NAME : null
         * SEND_CCY_USD_CONV_RATE : null
         * CREATED_BRANCH_FK_ID : null
         * ADDTL_SERVICE_ID : null
         * WS_FAILURE_REASON : null
         * WS_UPDATE_DATE : null
         * AML_EDD_ASSIGNED_TO : null
         * ANSARI_DRAW_MESSAGE : null
         * SEND_AGENT_SHARE_FX : null
         * RECEIVE_AGENT_SHARE_FX : null
         * PO_SHARE_FX : null
         * SETTLEMENT_TYPE : null
         * RATES_APPROVAL_FK_ID : null
         * WINDOW_TYPE : null
         * PAID_FLAG : null
         * VERIFIED_BY : null
         * VERIFIED_DATE : null
         * VERIFIER_NAME : null
         * BLOCK_MODE : null
         * CASH_COLLECTED_BRANCH : null
         * SEND_FX_GAIN_SHARE_FLAG : null
         * DEST_COUNTRY_NAME : INDIA
         * PAYOUT_CCY : null
         * PAYOUT_AGENT_CODE : null
         * PAYOUT_AGENT_NAME : null
         * PAYOUT_SUB_AGENT_CODE : null
         * PAYOUT_SUB_AGENT_NAME : null
         * FILE_NAME : null
         * BNK_DEPOSIT_ACCOUNT_NUMBER : null
         * BNK_DEPOSIT_BANK_CODE : null
         * BNK_DEPOSIT_BANK_NAME : null
         * BNK_DEPOSIT_BRANCH_CODE : null
         * BNK_DEPOSIT_BRANCH_NAME : null
         * BNK_DEPOSIT_BRANCH_ADDR : null
         * DRAW_SCREEN_MESSAGE : null
         */

        @SerializedName("SEND_MONEY_CASH_PAYOUT_PK_ID")
        private String sendMoneyCashPayoutPkId;
        @SerializedName("GTW_REM_TXN_FK_ID")
        private String gtwRemTxnFkId;
        @SerializedName("SEND_AMOUNT")
        private String sendAmount;
        @SerializedName("SEND_AMOUNT_USD")
        private String sendAmountUSD;
        @SerializedName("SEND_AMOUNT_AED")
        private String sendAmountAED;
        @SerializedName("BENE_CHARGES")
        private String benfCharges;
        @SerializedName("BENE_CHARGES_USD")
        private String benfChargesUSD;
        @SerializedName("BENE_CHARGES_AED")
        private String benfChargesAED;
        @SerializedName("TOTAL")
        private String total;
        @SerializedName("TOTAL_USD")
        private String totalUSD;
        @SerializedName("TOTAL_AED")
        private String totalAED;
        @SerializedName("RATE")
        private String rate;
        @SerializedName("LOCAL_CURR_AMOUNT_VALUE")
        private String localCurrAmountValue;
        @SerializedName("LOACL_CURR_CHARGES")
        private String localCurrCharges;
        @SerializedName("DISCOUNT")
        private String discount;
        @SerializedName("CCY_CODE")
        private String ccyCode;
        @SerializedName("CCY_DESC")
        private String ccyName;
        @SerializedName("SOURCE_AGENT_CODE")
        private String sourceAgentCode;
        @SerializedName("SOURCE_AGENT_NAME")
        private String sourceAgentName;
        @SerializedName("CREATER_NAME")
        private String createrName;
        @SerializedName("DEST_COUNTRY_CODE")
        private String destCountryCode;
        @SerializedName("REMARKS")
        private String remarks;
        @SerializedName("CASH_COLLECTED_BY")
        private String cashCollectedBy;
        @SerializedName("CASH_COLLECTOR_NAME")
        private String cashCollectorName;
        @SerializedName("TXN_MODE")
        private String txnMode;
        @SerializedName("FACTOR")
        private String factor;
        @SerializedName("CREATED_DATE")
        private String createdDate;
        @SerializedName("CREATED_BY")
        private String createdBy;
        @SerializedName("STATUS")
        private String status;
        @SerializedName("CASH_REC")
        private String cashRec;
        @SerializedName("BALANCE_TO_PAY")
        private String balanceToPay;
        @SerializedName("AGENT_ORIGIN_BRANCH")
        private String agentOriginBranch;
        @SerializedName("SUB_AGENT_CODE")
        private String subAgentCode;
        @SerializedName("SUB_AGENT_NAME")
        private String subAgentName;
        @SerializedName("PO_COST_PRICE")
        private String poCostPrice;
        @SerializedName("AGENT_COST_PRICE")
        private String agentCostPrice;
        @SerializedName("AE_TILL_NUMBER")
        private String aeTillNumber;
        private String ORIGINATING_BRANCH_CODE;
        private String ORIGINATING_BRANCH_NAME;
        private String AGENT_VERIFICATION_STATUS;
        private String PO_VERIFICATION_STATUS;
        private String ACCOUNT_GENERATED;
        private String AML_REJECT_REASON;
        private String ADDITIONAL_SERVICES;
        private String LOACL_CURR_CHARGES_USD;
        private String LOACL_CURR_CHARGES_AED;
        private String DOC_NUMBER;
        private String BLOCKED_BY;
        private String REQUEST_TYPE;
        private String PO_TOTAL_AMT;
        private String COMMISSION;
        private String PO_LOCAL_CURR_AMOUNT_VALUE;
        private String AGENT_REF_NUM;
        private String CHARGE_TYPE;
        private String PAYOUT_COMMISSION;
        private String LOCAL_CCY_COST_PRICE;
        private String PO_LOCAL_CURR_AMOUNT_VALUE_AED;
        private String PO_LOCAL_CURR_AMOUNT_VALUE_USD;
        private String PAYOUT_COMMISSION_AED;
        private String PAYOUT_COMMISSION_USD;
        private String DISCOUNT_USD;
        private String DISCOUNT_AED;
        private String COMMISSION_USD;
        private String COMMISSION_AED;
        private String LOCAL_CCY_TO_USD_CONV_RATE;
        private String AED_TO_USD_CONV_RATE;
        private String LOCAL_CURR_AMOUNT_VALUE_AED;
        private String LOCAL_CURR_AMOUNT_VALUE_USD;
        private String PO_TOTAL_AMT_AED;
        private String PO_TOTAL_AMT_USD;
        private String CREATED_DATE_AGENT_TIMEZONE;
        private String TOTAL_FX_GENERATED;
        private String DYNAMIC_TEMPLATE_ID;
        private String SOURCE_COUNTRY_CODE;
        private String SOURCE_COUNTRY_NAME;
        private String SEND_CCY_USD_CONV_RATE;
        private String CREATED_BRANCH_FK_ID;
        private String ADDTL_SERVICE_ID;
        private String WS_FAILURE_REASON;
        private String WS_UPDATE_DATE;
        private String AML_EDD_ASSIGNED_TO;
        private String ANSARI_DRAW_MESSAGE;
        private String SEND_AGENT_SHARE_FX;
        private String RECEIVE_AGENT_SHARE_FX;
        private String PO_SHARE_FX;
        private String SETTLEMENT_TYPE;
        private String RATES_APPROVAL_FK_ID;
        private String WINDOW_TYPE;
        private String PAID_FLAG;
        private String VERIFIED_BY;
        private String VERIFIED_DATE;
        private String VERIFIER_NAME;
        private String BLOCK_MODE;
        private String CASH_COLLECTED_BRANCH;
        private String SEND_FX_GAIN_SHARE_FLAG;
        private String DEST_COUNTRY_NAME;
        private String PAYOUT_CCY;
        private String PAYOUT_AGENT_CODE;
        private String PAYOUT_AGENT_NAME;
        private String PAYOUT_SUB_AGENT_CODE;
        private String PAYOUT_SUB_AGENT_NAME;
        private String FILE_NAME;
        private String BNK_DEPOSIT_ACCOUNT_NUMBER;
        @SerializedName("BNK_DEPOSIT_BANK_CODE")
        private String bnkDepositBankCode;
        @SerializedName("BNK_DEPOSIT_BANK_NAME")
        private String bnkDepositBankName;
        @SerializedName("BNK_DEPOSIT_BRANCH_CODE")
        private String bnkDepositBranchCode;
        @SerializedName("BNK_DEPOSIT_BRANCH_NAME")
        private String bnkDepositBranchName;
        @SerializedName("BNK_DEPOSIT_BRANCH_ADDR")
        private String bnkDepositBranchAddr;
        @SerializedName("DRAW_SCREEN_MESSAGE")
        private String drawScreenMessage;

        public TransactionDataTt() {
        }

        protected TransactionDataTt(Parcel in) {
            this.sendMoneyCashPayoutPkId = in.readString();
            this.gtwRemTxnFkId = in.readString();
            this.sendAmount = in.readString();
            this.sendAmountUSD = in.readString();
            this.sendAmountAED = in.readString();
            this.benfCharges = in.readString();
            this.benfChargesUSD = in.readString();
            this.benfChargesAED = in.readString();
            this.total = in.readString();
            this.totalUSD = in.readString();
            this.totalAED = in.readString();
            this.rate = in.readString();
            this.localCurrAmountValue = in.readString();
            this.localCurrCharges = in.readString();
            this.discount = in.readString();
            this.ccyCode = in.readString();
            this.ccyName = in.readString();
            this.sourceAgentCode = in.readString();
            this.sourceAgentName = in.readString();
            this.createrName = in.readString();
            this.destCountryCode = in.readString();
            this.remarks = in.readString();
            this.cashCollectedBy = in.readString();
            this.cashCollectorName = in.readString();
            this.txnMode = in.readString();
            this.factor = in.readString();
            this.createdDate = in.readString();
            this.createdBy = in.readString();
            this.status = in.readString();
            this.cashRec = in.readString();
            this.balanceToPay = in.readString();
            this.agentOriginBranch = in.readString();
            this.subAgentCode = in.readString();
            this.subAgentName = in.readString();
            this.poCostPrice = in.readString();
            this.agentCostPrice = in.readString();
            this.aeTillNumber = in.readString();
            this.ORIGINATING_BRANCH_CODE = in.readString();
            this.ORIGINATING_BRANCH_NAME = in.readString();
            this.AGENT_VERIFICATION_STATUS = in.readString();
            this.PO_VERIFICATION_STATUS = in.readString();
            this.ACCOUNT_GENERATED = in.readString();
            this.AML_REJECT_REASON = in.readString();
            this.ADDITIONAL_SERVICES = in.readString();
            this.LOACL_CURR_CHARGES_USD = in.readString();
            this.LOACL_CURR_CHARGES_AED = in.readString();
            this.DOC_NUMBER = in.readString();
            this.BLOCKED_BY = in.readString();
            this.REQUEST_TYPE = in.readString();
            this.PO_TOTAL_AMT = in.readString();
            this.COMMISSION = in.readString();
            this.PO_LOCAL_CURR_AMOUNT_VALUE = in.readString();
            this.AGENT_REF_NUM = in.readString();
            this.CHARGE_TYPE = in.readString();
            this.PAYOUT_COMMISSION = in.readString();
            this.LOCAL_CCY_COST_PRICE = in.readString();
            this.PO_LOCAL_CURR_AMOUNT_VALUE_AED = in.readString();
            this.PO_LOCAL_CURR_AMOUNT_VALUE_USD = in.readString();
            this.PAYOUT_COMMISSION_AED = in.readString();
            this.PAYOUT_COMMISSION_USD = in.readString();
            this.DISCOUNT_USD = in.readString();
            this.DISCOUNT_AED = in.readString();
            this.COMMISSION_USD = in.readString();
            this.COMMISSION_AED = in.readString();
            this.LOCAL_CCY_TO_USD_CONV_RATE = in.readString();
            this.AED_TO_USD_CONV_RATE = in.readString();
            this.LOCAL_CURR_AMOUNT_VALUE_AED = in.readString();
            this.LOCAL_CURR_AMOUNT_VALUE_USD = in.readString();
            this.PO_TOTAL_AMT_AED = in.readString();
            this.PO_TOTAL_AMT_USD = in.readString();
            this.CREATED_DATE_AGENT_TIMEZONE = in.readString();
            this.TOTAL_FX_GENERATED = in.readString();
            this.DYNAMIC_TEMPLATE_ID = in.readString();
            this.SOURCE_COUNTRY_CODE = in.readString();
            this.SOURCE_COUNTRY_NAME = in.readString();
            this.SEND_CCY_USD_CONV_RATE = in.readString();
            this.CREATED_BRANCH_FK_ID = in.readString();
            this.ADDTL_SERVICE_ID = in.readString();
            this.WS_FAILURE_REASON = in.readString();
            this.WS_UPDATE_DATE = in.readString();
            this.AML_EDD_ASSIGNED_TO = in.readString();
            this.ANSARI_DRAW_MESSAGE = in.readString();
            this.SEND_AGENT_SHARE_FX = in.readString();
            this.RECEIVE_AGENT_SHARE_FX = in.readString();
            this.PO_SHARE_FX = in.readString();
            this.SETTLEMENT_TYPE = in.readString();
            this.RATES_APPROVAL_FK_ID = in.readString();
            this.WINDOW_TYPE = in.readString();
            this.PAID_FLAG = in.readString();
            this.VERIFIED_BY = in.readString();
            this.VERIFIED_DATE = in.readString();
            this.VERIFIER_NAME = in.readString();
            this.BLOCK_MODE = in.readString();
            this.CASH_COLLECTED_BRANCH = in.readString();
            this.SEND_FX_GAIN_SHARE_FLAG = in.readString();
            this.DEST_COUNTRY_NAME = in.readString();
            this.PAYOUT_CCY = in.readString();
            this.PAYOUT_AGENT_CODE = in.readString();
            this.PAYOUT_AGENT_NAME = in.readString();
            this.PAYOUT_SUB_AGENT_CODE = in.readString();
            this.PAYOUT_SUB_AGENT_NAME = in.readString();
            this.FILE_NAME = in.readString();
            this.BNK_DEPOSIT_ACCOUNT_NUMBER = in.readString();
            this.bnkDepositBankCode = in.readString();
            this.bnkDepositBankName = in.readString();
            this.bnkDepositBranchCode = in.readString();
            this.bnkDepositBranchName = in.readString();
            this.bnkDepositBranchAddr = in.readString();
            this.drawScreenMessage = in.readString();
        }

        public String getSendMoneyCashPayoutPkId() {
            return sendMoneyCashPayoutPkId;
        }

        public void setSendMoneyCashPayoutPkId(String sendMoneyCashPayoutPkId) {
            this.sendMoneyCashPayoutPkId = sendMoneyCashPayoutPkId;
        }

        public String getGtwRemTxnFkId() {
            return gtwRemTxnFkId;
        }

        public void setGtwRemTxnFkId(String gtwRemTxnFkId) {
            this.gtwRemTxnFkId = gtwRemTxnFkId;
        }

        public String getSendAmount() {
            return sendAmount;
        }

        public void setSendAmount(String sendAmount) {
            this.sendAmount = sendAmount;
        }

        public String getSendAmountUSD() {
            return sendAmountUSD;
        }

        public void setSendAmountUSD(String sendAmountUSD) {
            this.sendAmountUSD = sendAmountUSD;
        }

        public String getSendAmountAED() {
            return sendAmountAED;
        }

        public void setSendAmountAED(String sendAmountAED) {
            this.sendAmountAED = sendAmountAED;
        }

        public String getBenfCharges() {
            return benfCharges;
        }

        public void setBenfCharges(String benfCharges) {
            this.benfCharges = benfCharges;
        }

        public String getBenfChargesUSD() {
            return benfChargesUSD;
        }

        public void setBenfChargesUSD(String benfChargesUSD) {
            this.benfChargesUSD = benfChargesUSD;
        }

        public String getBenfChargesAED() {
            return benfChargesAED;
        }

        public void setBenfChargesAED(String benfChargesAED) {
            this.benfChargesAED = benfChargesAED;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotalUSD() {
            return totalUSD;
        }

        public void setTotalUSD(String totalUSD) {
            this.totalUSD = totalUSD;
        }

        public String getTotalAED() {
            return totalAED;
        }

        public void setTotalAED(String totalAED) {
            this.totalAED = totalAED;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getLocalCurrAmountValue() {
            return localCurrAmountValue;
        }

        public void setLocalCurrAmountValue(String localCurrAmountValue) {
            this.localCurrAmountValue = localCurrAmountValue;
        }

        public String getLocalCurrCharges() {
            return localCurrCharges;
        }

        public void setLocalCurrCharges(String localCurrCharges) {
            this.localCurrCharges = localCurrCharges;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
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

        public String getCreaterName() {
            return createrName;
        }

        public void setCreaterName(String createrName) {
            this.createrName = createrName;
        }

        public String getDestCountryCode() {
            return destCountryCode;
        }

        public void setDestCountryCode(String destCountryCode) {
            this.destCountryCode = destCountryCode;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getCashCollectedBy() {
            return cashCollectedBy;
        }

        public void setCashCollectedBy(String cashCollectedBy) {
            this.cashCollectedBy = cashCollectedBy;
        }

        public String getCashCollectorName() {
            return cashCollectorName;
        }

        public void setCashCollectorName(String cashCollectorName) {
            this.cashCollectorName = cashCollectorName;
        }

        public String getTxnMode() {
            return txnMode;
        }

        public void setTxnMode(String txnMode) {
            this.txnMode = txnMode;
        }

        public String getFactor() {
            return factor;
        }

        public void setFactor(String factor) {
            this.factor = factor;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCashRec() {
            return cashRec;
        }

        public void setCashRec(String cashRec) {
            this.cashRec = cashRec;
        }

        public String getBalanceToPay() {
            return balanceToPay;
        }

        public void setBalanceToPay(String balanceToPay) {
            this.balanceToPay = balanceToPay;
        }

        public String getAgentOriginBranch() {
            return agentOriginBranch;
        }

        public void setAgentOriginBranch(String agentOriginBranch) {
            this.agentOriginBranch = agentOriginBranch;
        }

        public String getSubAgentCode() {
            return subAgentCode;
        }

        public void setSubAgentCode(String subAgentCode) {
            this.subAgentCode = subAgentCode;
        }

        public String getSubAgentName() {
            return subAgentName;
        }

        public void setSubAgentName(String subAgentName) {
            this.subAgentName = subAgentName;
        }

        public String getPoCostPrice() {
            return poCostPrice;
        }

        public void setPoCostPrice(String poCostPrice) {
            this.poCostPrice = poCostPrice;
        }

        public String getAgentCostPrice() {
            return agentCostPrice;
        }

        public void setAgentCostPrice(String agentCostPrice) {
            this.agentCostPrice = agentCostPrice;
        }

        public String getAeTillNumber() {
            return aeTillNumber;
        }

        public void setAeTillNumber(String aeTillNumber) {
            this.aeTillNumber = aeTillNumber;
        }

        public String getORIGINATING_BRANCH_CODE() {
            return ORIGINATING_BRANCH_CODE;
        }

        public void setORIGINATING_BRANCH_CODE(String ORIGINATING_BRANCH_CODE) {
            this.ORIGINATING_BRANCH_CODE = ORIGINATING_BRANCH_CODE;
        }

        public String getORIGINATING_BRANCH_NAME() {
            return ORIGINATING_BRANCH_NAME;
        }

        public void setORIGINATING_BRANCH_NAME(String ORIGINATING_BRANCH_NAME) {
            this.ORIGINATING_BRANCH_NAME = ORIGINATING_BRANCH_NAME;
        }

        public String getAGENT_VERIFICATION_STATUS() {
            return AGENT_VERIFICATION_STATUS;
        }

        public void setAGENT_VERIFICATION_STATUS(String AGENT_VERIFICATION_STATUS) {
            this.AGENT_VERIFICATION_STATUS = AGENT_VERIFICATION_STATUS;
        }

        public String getPO_VERIFICATION_STATUS() {
            return PO_VERIFICATION_STATUS;
        }

        public void setPO_VERIFICATION_STATUS(String PO_VERIFICATION_STATUS) {
            this.PO_VERIFICATION_STATUS = PO_VERIFICATION_STATUS;
        }

        public String getACCOUNT_GENERATED() {
            return ACCOUNT_GENERATED;
        }

        public void setACCOUNT_GENERATED(String ACCOUNT_GENERATED) {
            this.ACCOUNT_GENERATED = ACCOUNT_GENERATED;
        }

        public String getAML_REJECT_REASON() {
            return AML_REJECT_REASON;
        }

        public void setAML_REJECT_REASON(String AML_REJECT_REASON) {
            this.AML_REJECT_REASON = AML_REJECT_REASON;
        }

        public String getADDITIONAL_SERVICES() {
            return ADDITIONAL_SERVICES;
        }

        public void setADDITIONAL_SERVICES(String ADDITIONAL_SERVICES) {
            this.ADDITIONAL_SERVICES = ADDITIONAL_SERVICES;
        }

        public String getSourceAgentCode() {
            return sourceAgentCode;
        }

        public void setSourceAgentCode(String sourceAgentCode) {
            this.sourceAgentCode = sourceAgentCode;
        }

        public String getSourceAgentName() {
            return sourceAgentName;
        }

        public void setSourceAgentName(String sourceAgentName) {
            this.sourceAgentName = sourceAgentName;
        }

        public String getLOACL_CURR_CHARGES_USD() {
            return LOACL_CURR_CHARGES_USD;
        }

        public void setLOACL_CURR_CHARGES_USD(String LOACL_CURR_CHARGES_USD) {
            this.LOACL_CURR_CHARGES_USD = LOACL_CURR_CHARGES_USD;
        }

        public String getLOACL_CURR_CHARGES_AED() {
            return LOACL_CURR_CHARGES_AED;
        }

        public void setLOACL_CURR_CHARGES_AED(String LOACL_CURR_CHARGES_AED) {
            this.LOACL_CURR_CHARGES_AED = LOACL_CURR_CHARGES_AED;
        }

        public String getDOC_NUMBER() {
            return DOC_NUMBER;
        }

        public void setDOC_NUMBER(String DOC_NUMBER) {
            this.DOC_NUMBER = DOC_NUMBER;
        }

        public String getBLOCKED_BY() {
            return BLOCKED_BY;
        }

        public void setBLOCKED_BY(String BLOCKED_BY) {
            this.BLOCKED_BY = BLOCKED_BY;
        }

        public String getREQUEST_TYPE() {
            return REQUEST_TYPE;
        }

        public void setREQUEST_TYPE(String REQUEST_TYPE) {
            this.REQUEST_TYPE = REQUEST_TYPE;
        }

        public String getPO_TOTAL_AMT() {
            return PO_TOTAL_AMT;
        }

        public void setPO_TOTAL_AMT(String PO_TOTAL_AMT) {
            this.PO_TOTAL_AMT = PO_TOTAL_AMT;
        }

        public String getCOMMISSION() {
            return COMMISSION;
        }

        public void setCOMMISSION(String COMMISSION) {
            this.COMMISSION = COMMISSION;
        }

        public String getPO_LOCAL_CURR_AMOUNT_VALUE() {
            return PO_LOCAL_CURR_AMOUNT_VALUE;
        }

        public void setPO_LOCAL_CURR_AMOUNT_VALUE(String PO_LOCAL_CURR_AMOUNT_VALUE) {
            this.PO_LOCAL_CURR_AMOUNT_VALUE = PO_LOCAL_CURR_AMOUNT_VALUE;
        }

        public String getAGENT_REF_NUM() {
            return AGENT_REF_NUM;
        }

        public void setAGENT_REF_NUM(String AGENT_REF_NUM) {
            this.AGENT_REF_NUM = AGENT_REF_NUM;
        }

        public String getCHARGE_TYPE() {
            return CHARGE_TYPE;
        }

        public void setCHARGE_TYPE(String CHARGE_TYPE) {
            this.CHARGE_TYPE = CHARGE_TYPE;
        }

        public String getPAYOUT_COMMISSION() {
            return PAYOUT_COMMISSION;
        }

        public void setPAYOUT_COMMISSION(String PAYOUT_COMMISSION) {
            this.PAYOUT_COMMISSION = PAYOUT_COMMISSION;
        }

        public String getLOCAL_CCY_COST_PRICE() {
            return LOCAL_CCY_COST_PRICE;
        }

        public void setLOCAL_CCY_COST_PRICE(String LOCAL_CCY_COST_PRICE) {
            this.LOCAL_CCY_COST_PRICE = LOCAL_CCY_COST_PRICE;
        }

        public String getPO_LOCAL_CURR_AMOUNT_VALUE_AED() {
            return PO_LOCAL_CURR_AMOUNT_VALUE_AED;
        }

        public void setPO_LOCAL_CURR_AMOUNT_VALUE_AED(String PO_LOCAL_CURR_AMOUNT_VALUE_AED) {
            this.PO_LOCAL_CURR_AMOUNT_VALUE_AED = PO_LOCAL_CURR_AMOUNT_VALUE_AED;
        }

        public String getPO_LOCAL_CURR_AMOUNT_VALUE_USD() {
            return PO_LOCAL_CURR_AMOUNT_VALUE_USD;
        }

        public void setPO_LOCAL_CURR_AMOUNT_VALUE_USD(String PO_LOCAL_CURR_AMOUNT_VALUE_USD) {
            this.PO_LOCAL_CURR_AMOUNT_VALUE_USD = PO_LOCAL_CURR_AMOUNT_VALUE_USD;
        }

        public String getPAYOUT_COMMISSION_AED() {
            return PAYOUT_COMMISSION_AED;
        }

        public void setPAYOUT_COMMISSION_AED(String PAYOUT_COMMISSION_AED) {
            this.PAYOUT_COMMISSION_AED = PAYOUT_COMMISSION_AED;
        }

        public String getPAYOUT_COMMISSION_USD() {
            return PAYOUT_COMMISSION_USD;
        }

        public void setPAYOUT_COMMISSION_USD(String PAYOUT_COMMISSION_USD) {
            this.PAYOUT_COMMISSION_USD = PAYOUT_COMMISSION_USD;
        }

        public String getDISCOUNT_USD() {
            return DISCOUNT_USD;
        }

        public void setDISCOUNT_USD(String DISCOUNT_USD) {
            this.DISCOUNT_USD = DISCOUNT_USD;
        }

        public String getDISCOUNT_AED() {
            return DISCOUNT_AED;
        }

        public void setDISCOUNT_AED(String DISCOUNT_AED) {
            this.DISCOUNT_AED = DISCOUNT_AED;
        }

        public String getCOMMISSION_USD() {
            return COMMISSION_USD;
        }

        public void setCOMMISSION_USD(String COMMISSION_USD) {
            this.COMMISSION_USD = COMMISSION_USD;
        }

        public String getCOMMISSION_AED() {
            return COMMISSION_AED;
        }

        public void setCOMMISSION_AED(String COMMISSION_AED) {
            this.COMMISSION_AED = COMMISSION_AED;
        }

        public String getLOCAL_CCY_TO_USD_CONV_RATE() {
            return LOCAL_CCY_TO_USD_CONV_RATE;
        }

        public void setLOCAL_CCY_TO_USD_CONV_RATE(String LOCAL_CCY_TO_USD_CONV_RATE) {
            this.LOCAL_CCY_TO_USD_CONV_RATE = LOCAL_CCY_TO_USD_CONV_RATE;
        }

        public String getAED_TO_USD_CONV_RATE() {
            return AED_TO_USD_CONV_RATE;
        }

        public void setAED_TO_USD_CONV_RATE(String AED_TO_USD_CONV_RATE) {
            this.AED_TO_USD_CONV_RATE = AED_TO_USD_CONV_RATE;
        }

        public String getLOCAL_CURR_AMOUNT_VALUE_AED() {
            return LOCAL_CURR_AMOUNT_VALUE_AED;
        }

        public void setLOCAL_CURR_AMOUNT_VALUE_AED(String LOCAL_CURR_AMOUNT_VALUE_AED) {
            this.LOCAL_CURR_AMOUNT_VALUE_AED = LOCAL_CURR_AMOUNT_VALUE_AED;
        }

        public String getLOCAL_CURR_AMOUNT_VALUE_USD() {
            return LOCAL_CURR_AMOUNT_VALUE_USD;
        }

        public void setLOCAL_CURR_AMOUNT_VALUE_USD(String LOCAL_CURR_AMOUNT_VALUE_USD) {
            this.LOCAL_CURR_AMOUNT_VALUE_USD = LOCAL_CURR_AMOUNT_VALUE_USD;
        }

        public String getPO_TOTAL_AMT_AED() {
            return PO_TOTAL_AMT_AED;
        }

        public void setPO_TOTAL_AMT_AED(String PO_TOTAL_AMT_AED) {
            this.PO_TOTAL_AMT_AED = PO_TOTAL_AMT_AED;
        }

        public String getPO_TOTAL_AMT_USD() {
            return PO_TOTAL_AMT_USD;
        }

        public void setPO_TOTAL_AMT_USD(String PO_TOTAL_AMT_USD) {
            this.PO_TOTAL_AMT_USD = PO_TOTAL_AMT_USD;
        }

        public String getCREATED_DATE_AGENT_TIMEZONE() {
            return CREATED_DATE_AGENT_TIMEZONE;
        }

        public void setCREATED_DATE_AGENT_TIMEZONE(String CREATED_DATE_AGENT_TIMEZONE) {
            this.CREATED_DATE_AGENT_TIMEZONE = CREATED_DATE_AGENT_TIMEZONE;
        }

        public String getTOTAL_FX_GENERATED() {
            return TOTAL_FX_GENERATED;
        }

        public void setTOTAL_FX_GENERATED(String TOTAL_FX_GENERATED) {
            this.TOTAL_FX_GENERATED = TOTAL_FX_GENERATED;
        }

        public String getDYNAMIC_TEMPLATE_ID() {
            return DYNAMIC_TEMPLATE_ID;
        }

        public void setDYNAMIC_TEMPLATE_ID(String DYNAMIC_TEMPLATE_ID) {
            this.DYNAMIC_TEMPLATE_ID = DYNAMIC_TEMPLATE_ID;
        }

        public String getSOURCE_COUNTRY_CODE() {
            return SOURCE_COUNTRY_CODE;
        }

        public void setSOURCE_COUNTRY_CODE(String SOURCE_COUNTRY_CODE) {
            this.SOURCE_COUNTRY_CODE = SOURCE_COUNTRY_CODE;
        }

        public String getSOURCE_COUNTRY_NAME() {
            return SOURCE_COUNTRY_NAME;
        }

        public void setSOURCE_COUNTRY_NAME(String SOURCE_COUNTRY_NAME) {
            this.SOURCE_COUNTRY_NAME = SOURCE_COUNTRY_NAME;
        }

        public String getSEND_CCY_USD_CONV_RATE() {
            return SEND_CCY_USD_CONV_RATE;
        }

        public void setSEND_CCY_USD_CONV_RATE(String SEND_CCY_USD_CONV_RATE) {
            this.SEND_CCY_USD_CONV_RATE = SEND_CCY_USD_CONV_RATE;
        }

        public String getCREATED_BRANCH_FK_ID() {
            return CREATED_BRANCH_FK_ID;
        }

        public void setCREATED_BRANCH_FK_ID(String CREATED_BRANCH_FK_ID) {
            this.CREATED_BRANCH_FK_ID = CREATED_BRANCH_FK_ID;
        }

        public String getADDTL_SERVICE_ID() {
            return ADDTL_SERVICE_ID;
        }

        public void setADDTL_SERVICE_ID(String ADDTL_SERVICE_ID) {
            this.ADDTL_SERVICE_ID = ADDTL_SERVICE_ID;
        }

        public String getWS_FAILURE_REASON() {
            return WS_FAILURE_REASON;
        }

        public void setWS_FAILURE_REASON(String WS_FAILURE_REASON) {
            this.WS_FAILURE_REASON = WS_FAILURE_REASON;
        }

        public String getWS_UPDATE_DATE() {
            return WS_UPDATE_DATE;
        }

        public void setWS_UPDATE_DATE(String WS_UPDATE_DATE) {
            this.WS_UPDATE_DATE = WS_UPDATE_DATE;
        }

        public String getAML_EDD_ASSIGNED_TO() {
            return AML_EDD_ASSIGNED_TO;
        }

        public void setAML_EDD_ASSIGNED_TO(String AML_EDD_ASSIGNED_TO) {
            this.AML_EDD_ASSIGNED_TO = AML_EDD_ASSIGNED_TO;
        }

        public String getANSARI_DRAW_MESSAGE() {
            return ANSARI_DRAW_MESSAGE;
        }

        public void setANSARI_DRAW_MESSAGE(String ANSARI_DRAW_MESSAGE) {
            this.ANSARI_DRAW_MESSAGE = ANSARI_DRAW_MESSAGE;
        }

        public String getSEND_AGENT_SHARE_FX() {
            return SEND_AGENT_SHARE_FX;
        }

        public void setSEND_AGENT_SHARE_FX(String SEND_AGENT_SHARE_FX) {
            this.SEND_AGENT_SHARE_FX = SEND_AGENT_SHARE_FX;
        }

        public String getRECEIVE_AGENT_SHARE_FX() {
            return RECEIVE_AGENT_SHARE_FX;
        }

        public void setRECEIVE_AGENT_SHARE_FX(String RECEIVE_AGENT_SHARE_FX) {
            this.RECEIVE_AGENT_SHARE_FX = RECEIVE_AGENT_SHARE_FX;
        }

        public String getPO_SHARE_FX() {
            return PO_SHARE_FX;
        }

        public void setPO_SHARE_FX(String PO_SHARE_FX) {
            this.PO_SHARE_FX = PO_SHARE_FX;
        }

        public String getSETTLEMENT_TYPE() {
            return SETTLEMENT_TYPE;
        }

        public void setSETTLEMENT_TYPE(String SETTLEMENT_TYPE) {
            this.SETTLEMENT_TYPE = SETTLEMENT_TYPE;
        }

        public String getRATES_APPROVAL_FK_ID() {
            return RATES_APPROVAL_FK_ID;
        }

        public void setRATES_APPROVAL_FK_ID(String RATES_APPROVAL_FK_ID) {
            this.RATES_APPROVAL_FK_ID = RATES_APPROVAL_FK_ID;
        }

        public String getWINDOW_TYPE() {
            return WINDOW_TYPE;
        }

        public void setWINDOW_TYPE(String WINDOW_TYPE) {
            this.WINDOW_TYPE = WINDOW_TYPE;
        }

        public String getPAID_FLAG() {
            return PAID_FLAG;
        }

        public void setPAID_FLAG(String PAID_FLAG) {
            this.PAID_FLAG = PAID_FLAG;
        }

        public String getVERIFIED_BY() {
            return VERIFIED_BY;
        }

        public void setVERIFIED_BY(String VERIFIED_BY) {
            this.VERIFIED_BY = VERIFIED_BY;
        }

        public String getVERIFIED_DATE() {
            return VERIFIED_DATE;
        }

        public void setVERIFIED_DATE(String VERIFIED_DATE) {
            this.VERIFIED_DATE = VERIFIED_DATE;
        }

        public String getVERIFIER_NAME() {
            return VERIFIER_NAME;
        }

        public void setVERIFIER_NAME(String VERIFIER_NAME) {
            this.VERIFIER_NAME = VERIFIER_NAME;
        }

        public String getBLOCK_MODE() {
            return BLOCK_MODE;
        }

        public void setBLOCK_MODE(String BLOCK_MODE) {
            this.BLOCK_MODE = BLOCK_MODE;
        }

        public String getCASH_COLLECTED_BRANCH() {
            return CASH_COLLECTED_BRANCH;
        }

        public void setCASH_COLLECTED_BRANCH(String CASH_COLLECTED_BRANCH) {
            this.CASH_COLLECTED_BRANCH = CASH_COLLECTED_BRANCH;
        }

        public String getSEND_FX_GAIN_SHARE_FLAG() {
            return SEND_FX_GAIN_SHARE_FLAG;
        }

        public void setSEND_FX_GAIN_SHARE_FLAG(String SEND_FX_GAIN_SHARE_FLAG) {
            this.SEND_FX_GAIN_SHARE_FLAG = SEND_FX_GAIN_SHARE_FLAG;
        }

        public String getDEST_COUNTRY_NAME() {
            return DEST_COUNTRY_NAME;
        }

        public void setDEST_COUNTRY_NAME(String DEST_COUNTRY_NAME) {
            this.DEST_COUNTRY_NAME = DEST_COUNTRY_NAME;
        }

        public String getPAYOUT_CCY() {
            return PAYOUT_CCY;
        }

        public void setPAYOUT_CCY(String PAYOUT_CCY) {
            this.PAYOUT_CCY = PAYOUT_CCY;
        }

        public String getPAYOUT_AGENT_CODE() {
            return PAYOUT_AGENT_CODE;
        }

        public void setPAYOUT_AGENT_CODE(String PAYOUT_AGENT_CODE) {
            this.PAYOUT_AGENT_CODE = PAYOUT_AGENT_CODE;
        }

        public String getPAYOUT_AGENT_NAME() {
            return PAYOUT_AGENT_NAME;
        }

        public void setPAYOUT_AGENT_NAME(String PAYOUT_AGENT_NAME) {
            this.PAYOUT_AGENT_NAME = PAYOUT_AGENT_NAME;
        }

        public String getPAYOUT_SUB_AGENT_CODE() {
            return PAYOUT_SUB_AGENT_CODE;
        }

        public void setPAYOUT_SUB_AGENT_CODE(String PAYOUT_SUB_AGENT_CODE) {
            this.PAYOUT_SUB_AGENT_CODE = PAYOUT_SUB_AGENT_CODE;
        }

        public String getPAYOUT_SUB_AGENT_NAME() {
            return PAYOUT_SUB_AGENT_NAME;
        }

        public void setPAYOUT_SUB_AGENT_NAME(String PAYOUT_SUB_AGENT_NAME) {
            this.PAYOUT_SUB_AGENT_NAME = PAYOUT_SUB_AGENT_NAME;
        }

        public String getFILE_NAME() {
            return FILE_NAME;
        }

        public void setFILE_NAME(String FILE_NAME) {
            this.FILE_NAME = FILE_NAME;
        }

        public String getBNK_DEPOSIT_ACCOUNT_NUMBER() {
            return BNK_DEPOSIT_ACCOUNT_NUMBER;
        }

        public void setBNK_DEPOSIT_ACCOUNT_NUMBER(String BNK_DEPOSIT_ACCOUNT_NUMBER) {
            this.BNK_DEPOSIT_ACCOUNT_NUMBER = BNK_DEPOSIT_ACCOUNT_NUMBER;
        }

        public String getBnkDepositBankCode() {
            return bnkDepositBankCode;
        }

        public void setBnkDepositBankCode(String bnkDepositBankCode) {
            this.bnkDepositBankCode = bnkDepositBankCode;
        }

        public String getBnkDepositBankName() {
            return bnkDepositBankName;
        }

        public void setBnkDepositBankName(String bnkDepositBankName) {
            this.bnkDepositBankName = bnkDepositBankName;
        }

        public String getBnkDepositBranchCode() {
            return bnkDepositBranchCode;
        }

        public void setBnkDepositBranchCode(String bnkDepositBranchCode) {
            this.bnkDepositBranchCode = bnkDepositBranchCode;
        }

        public String getBnkDepositBranchName() {
            return bnkDepositBranchName;
        }

        public void setBnkDepositBranchName(String bnkDepositBranchName) {
            this.bnkDepositBranchName = bnkDepositBranchName;
        }

        public String getBnkDepositBranchAddr() {
            return bnkDepositBranchAddr;
        }

        public void setBnkDepositBranchAddr(String bnkDepositBranchAddr) {
            this.bnkDepositBranchAddr = bnkDepositBranchAddr;
        }

        public String getDrawScreenMessage() {
            return drawScreenMessage;
        }

        public void setDrawScreenMessage(String drawScreenMessage) {
            this.drawScreenMessage = drawScreenMessage;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.sendMoneyCashPayoutPkId);
            dest.writeString(this.gtwRemTxnFkId);
            dest.writeString(this.sendAmount);
            dest.writeString(this.sendAmountUSD);
            dest.writeString(this.sendAmountAED);
            dest.writeString(this.benfCharges);
            dest.writeString(this.benfChargesUSD);
            dest.writeString(this.benfChargesAED);
            dest.writeString(this.total);
            dest.writeString(this.totalUSD);
            dest.writeString(this.totalAED);
            dest.writeString(this.rate);
            dest.writeString(this.localCurrAmountValue);
            dest.writeString(this.localCurrCharges);
            dest.writeString(this.discount);
            dest.writeString(this.ccyCode);
            dest.writeString(this.ccyName);
            dest.writeString(this.sourceAgentCode);
            dest.writeString(this.sourceAgentName);
            dest.writeString(this.createrName);
            dest.writeString(this.destCountryCode);
            dest.writeString(this.remarks);
            dest.writeString(this.cashCollectedBy);
            dest.writeString(this.cashCollectorName);
            dest.writeString(this.txnMode);
            dest.writeString(this.factor);
            dest.writeString(this.createdDate);
            dest.writeString(this.createdBy);
            dest.writeString(this.status);
            dest.writeString(this.cashRec);
            dest.writeString(this.balanceToPay);
            dest.writeString(this.agentOriginBranch);
            dest.writeString(this.subAgentCode);
            dest.writeString(this.subAgentName);
            dest.writeString(this.poCostPrice);
            dest.writeString(this.agentCostPrice);
            dest.writeString(this.aeTillNumber);
            dest.writeString(this.ORIGINATING_BRANCH_CODE);
            dest.writeString(this.ORIGINATING_BRANCH_NAME);
            dest.writeString(this.AGENT_VERIFICATION_STATUS);
            dest.writeString(this.PO_VERIFICATION_STATUS);
            dest.writeString(this.ACCOUNT_GENERATED);
            dest.writeString(this.AML_REJECT_REASON);
            dest.writeString(this.ADDITIONAL_SERVICES);
            dest.writeString(this.LOACL_CURR_CHARGES_USD);
            dest.writeString(this.LOACL_CURR_CHARGES_AED);
            dest.writeString(this.DOC_NUMBER);
            dest.writeString(this.BLOCKED_BY);
            dest.writeString(this.REQUEST_TYPE);
            dest.writeString(this.PO_TOTAL_AMT);
            dest.writeString(this.COMMISSION);
            dest.writeString(this.PO_LOCAL_CURR_AMOUNT_VALUE);
            dest.writeString(this.AGENT_REF_NUM);
            dest.writeString(this.CHARGE_TYPE);
            dest.writeString(this.PAYOUT_COMMISSION);
            dest.writeString(this.LOCAL_CCY_COST_PRICE);
            dest.writeString(this.PO_LOCAL_CURR_AMOUNT_VALUE_AED);
            dest.writeString(this.PO_LOCAL_CURR_AMOUNT_VALUE_USD);
            dest.writeString(this.PAYOUT_COMMISSION_AED);
            dest.writeString(this.PAYOUT_COMMISSION_USD);
            dest.writeString(this.DISCOUNT_USD);
            dest.writeString(this.DISCOUNT_AED);
            dest.writeString(this.COMMISSION_USD);
            dest.writeString(this.COMMISSION_AED);
            dest.writeString(this.LOCAL_CCY_TO_USD_CONV_RATE);
            dest.writeString(this.AED_TO_USD_CONV_RATE);
            dest.writeString(this.LOCAL_CURR_AMOUNT_VALUE_AED);
            dest.writeString(this.LOCAL_CURR_AMOUNT_VALUE_USD);
            dest.writeString(this.PO_TOTAL_AMT_AED);
            dest.writeString(this.PO_TOTAL_AMT_USD);
            dest.writeString(this.CREATED_DATE_AGENT_TIMEZONE);
            dest.writeString(this.TOTAL_FX_GENERATED);
            dest.writeString(this.DYNAMIC_TEMPLATE_ID);
            dest.writeString(this.SOURCE_COUNTRY_CODE);
            dest.writeString(this.SOURCE_COUNTRY_NAME);
            dest.writeString(this.SEND_CCY_USD_CONV_RATE);
            dest.writeString(this.CREATED_BRANCH_FK_ID);
            dest.writeString(this.ADDTL_SERVICE_ID);
            dest.writeString(this.WS_FAILURE_REASON);
            dest.writeString(this.WS_UPDATE_DATE);
            dest.writeString(this.AML_EDD_ASSIGNED_TO);
            dest.writeString(this.ANSARI_DRAW_MESSAGE);
            dest.writeString(this.SEND_AGENT_SHARE_FX);
            dest.writeString(this.RECEIVE_AGENT_SHARE_FX);
            dest.writeString(this.PO_SHARE_FX);
            dest.writeString(this.SETTLEMENT_TYPE);
            dest.writeString(this.RATES_APPROVAL_FK_ID);
            dest.writeString(this.WINDOW_TYPE);
            dest.writeString(this.PAID_FLAG);
            dest.writeString(this.VERIFIED_BY);
            dest.writeString(this.VERIFIED_DATE);
            dest.writeString(this.VERIFIER_NAME);
            dest.writeString(this.BLOCK_MODE);
            dest.writeString(this.CASH_COLLECTED_BRANCH);
            dest.writeString(this.SEND_FX_GAIN_SHARE_FLAG);
            dest.writeString(this.DEST_COUNTRY_NAME);
            dest.writeString(this.PAYOUT_CCY);
            dest.writeString(this.PAYOUT_AGENT_CODE);
            dest.writeString(this.PAYOUT_AGENT_NAME);
            dest.writeString(this.PAYOUT_SUB_AGENT_CODE);
            dest.writeString(this.PAYOUT_SUB_AGENT_NAME);
            dest.writeString(this.FILE_NAME);
            dest.writeString(this.BNK_DEPOSIT_ACCOUNT_NUMBER);
            dest.writeString(this.bnkDepositBankCode);
            dest.writeString(this.bnkDepositBankName);
            dest.writeString(this.bnkDepositBranchCode);
            dest.writeString(this.bnkDepositBranchName);
            dest.writeString(this.bnkDepositBranchAddr);
            dest.writeString(this.drawScreenMessage);
        }
    }

    public static class BeneficiaryDataTt implements Parcelable {

        public static final Creator<BeneficiaryDataTt> CREATOR = new Creator<BeneficiaryDataTt>() {
            @Override
            public BeneficiaryDataTt createFromParcel(Parcel source) {
                return new BeneficiaryDataTt(source);
            }

            @Override
            public BeneficiaryDataTt[] newArray(int size) {
                return new BeneficiaryDataTt[size];
            }
        };
        /**
         * CASH_PAY_REC_PK_ID : 14
         * CASH_PAY_MAIN_REC_FK_ID : 14
         * RECEIVER_FULL_NAME : IAN JON
         * BENF_FINAL_NAME : IAN JON
         * BEN_ACC_NO : 1234567890123
         * BEN_ACCOUNT_TYPE : 0
         * BEN_BRANCH_NAME : TEST CANARA BRANCH
         * BEN_BRANCH_CODE : 1
         * BANK_BRANCH_ADDR : null
         * REC_COUNTRY_CODE : 26
         * REC_COUNTRY_NAME : INDIA
         * BEN_NATIONALITY : FILIPINO
         * REC_MOBILENO : 3456789012
         * REC_ADDRESS : TEST BENEFICIARY ADDRESS
         * DOB : null
         * PLACE_OF_BIRTH : null
         * ACCOUNT_TYPE : 0
         * IBAN_NO : null
         * RESIDENTIAL_STATUS : null
         * MIDDLE_NAME : null
         * FAMILY_NAME : null
         * PROFESSION : null
         * EMPLOYER_NAME : null
         * BEN_EFT_CODE : null
         * BANK_PURPOSE_CODE : null
         * BANK_PURPOSE : null
         * IDENTIFICATION_DTLS : null
         * RESIDENCY_NUMBER : null
         * BEN_EFT_TYPE : null
         * PROOF_ID_EXP_DATE_BEN : null
         * BEN_PLACE_OF_ISSUE : null
         * BEN_ID_EXPIRY_DATE : null
         * BEN_COM_REG_NO : null
         * BEN_COMM_REG_PLACE_OF_ISSUE : null
         * BEN_TYPE_OF_ACTIVITY_KEY : null
         * BEN_PURPOSE_OF_TXN_KEY : null
         * BENEFICIARY_TYPE_ID : null
         * BENEFICIARY_TYPE_DESC : null
         * REC_FIRST_NAME : null
         * REC_SECOND_NAME : null
         * REC_THIRD_NAME : null
         * REC_LAST_NAME : null
         * REC_ISD_PREFIX : null
         * MESSAGE_TO_REC : null
         * BANK_CHANNEL_CODE : null
         */

        @SerializedName("CASH_PAY_REC_PK_ID")
        private String cashPayRecPkId;
        @SerializedName("CASH_PAY_MAIN_REC_FK_ID")
        private String cashPayMainRecFkId;
        @SerializedName("RECEIVER_FULL_NAME")
        private String receiverFullName;
        @SerializedName("BENF_FINAL_NAME")
        private String benfFinalName;
        @SerializedName("BEN_ACC_NO")
        private String benfAccNo;
        @SerializedName("BEN_ACCOUNT_TYPE")
        private String benAccountType;
        @SerializedName("BEN_BRANCH_NAME")
        private String benBranchName;
        @SerializedName("BEN_BRANCH_CODE")
        private String benBranchCode;
        @SerializedName("BANK_BRANCH_ADDR")
        private String bankBranchAddr;
        @SerializedName("REC_COUNTRY_CODE")
        private String recCountryCode;
        @SerializedName("REC_COUNTRY_NAME")
        private String recCountryName;
        @SerializedName("BEN_NATIONALITY")
        private String benNationality;
        @SerializedName("REC_MOBILENO")
        private String recMobileNo;
        @SerializedName("REC_ADDRESS")
        private String recAddress;
        @SerializedName("DOB")
        private String dob;
        @SerializedName("PLACE_OF_BIRTH")
        private String placeOfBirth;
        @SerializedName("ACCOUNT_TYPE")
        private String accountType;
        @SerializedName("IBAN_NO")
        private String ibanNo;
        @SerializedName("RESIDENTIAL_STATUS")
        private String residentialStatus;
        @SerializedName("MIDDLE_NAME")
        private String middleName;
        @SerializedName("FAMILY_NAME")
        private String familyName;
        @SerializedName("PROFESSION")
        private String profession;
        @SerializedName("EMPLOYER_NAME")
        private String employerName;
        @SerializedName("BEN_EFT_CODE")
        private String benEftCode;
        @SerializedName("BANK_PURPOSE_CODE")
        private String bankPurposeCode;
        @SerializedName("BANK_PURPOSE")
        private String bankPurpose;
        @SerializedName("IDENTIFICATION_DTLS")
        private String identificationDtls;
        @SerializedName("RESIDENCY_NUMBER")
        private String residencyNumber;
        @SerializedName("BEN_EFT_TYPE")
        private String benEftType;
        private String PROOF_ID_EXP_DATE_BEN;
        private String BEN_PLACE_OF_ISSUE;
        private String BEN_ID_EXPIRY_DATE;
        private String BEN_COM_REG_NO;
        private String BEN_COMM_REG_PLACE_OF_ISSUE;
        private String BEN_TYPE_OF_ACTIVITY_KEY;
        private String BEN_PURPOSE_OF_TXN_KEY;
        @SerializedName("BENEFICIARY_TYPE_ID")
        private String beneficiaryTypeId;
        @SerializedName("BENEFICIARY_TYPE_DESC")
        private String beneficiaryTypeDesc;
        @SerializedName("REC_FIRST_NAME")
        private String recFirstName;
        @SerializedName("REC_SECOND_NAME")
        private String recSecondName;
        @SerializedName("REC_THIRD_NAME")
        private String recThirdName;
        @SerializedName("REC_LAST_NAME")
        private String recLastName;
        @SerializedName("REC_ISD_PREFIX")
        private String recIsdPrefix;
        private String MESSAGE_TO_REC;
        @SerializedName("BANK_CHANNEL_CODE")
        private String bankChannelCode;

        public BeneficiaryDataTt() {
        }

        protected BeneficiaryDataTt(Parcel in) {
            this.cashPayRecPkId = in.readString();
            this.cashPayMainRecFkId = in.readString();
            this.receiverFullName = in.readString();
            this.benfFinalName = in.readString();
            this.benfAccNo = in.readString();
            this.benAccountType = in.readString();
            this.benBranchName = in.readString();
            this.benBranchCode = in.readString();
            this.bankBranchAddr = in.readString();
            this.recCountryCode = in.readString();
            this.recCountryName = in.readString();
            this.benNationality = in.readString();
            this.recMobileNo = in.readString();
            this.recAddress = in.readString();
            this.dob = in.readString();
            this.placeOfBirth = in.readString();
            this.accountType = in.readString();
            this.ibanNo = in.readString();
            this.residentialStatus = in.readString();
            this.middleName = in.readString();
            this.familyName = in.readString();
            this.profession = in.readString();
            this.employerName = in.readString();
            this.benEftCode = in.readString();
            this.bankPurposeCode = in.readString();
            this.bankPurpose = in.readString();
            this.identificationDtls = in.readString();
            this.residencyNumber = in.readString();
            this.benEftType = in.readString();
            this.PROOF_ID_EXP_DATE_BEN = in.readString();
            this.BEN_PLACE_OF_ISSUE = in.readString();
            this.BEN_ID_EXPIRY_DATE = in.readString();
            this.BEN_COM_REG_NO = in.readString();
            this.BEN_COMM_REG_PLACE_OF_ISSUE = in.readString();
            this.BEN_TYPE_OF_ACTIVITY_KEY = in.readString();
            this.BEN_PURPOSE_OF_TXN_KEY = in.readString();
            this.beneficiaryTypeId = in.readString();
            this.beneficiaryTypeDesc = in.readString();
            this.recFirstName = in.readString();
            this.recSecondName = in.readString();
            this.recThirdName = in.readString();
            this.recLastName = in.readString();
            this.recIsdPrefix = in.readString();
            this.MESSAGE_TO_REC = in.readString();
            this.bankChannelCode = in.readString();
        }

        public String getCashPayRecPkId() {
            return cashPayRecPkId;
        }

        public void setCashPayRecPkId(String cashPayRecPkId) {
            this.cashPayRecPkId = cashPayRecPkId;
        }

        public String getCashPayMainRecFkId() {
            return cashPayMainRecFkId;
        }

        public void setCashPayMainRecFkId(String cashPayMainRecFkId) {
            this.cashPayMainRecFkId = cashPayMainRecFkId;
        }

        public String getReceiverFullName() {
            return receiverFullName;
        }

        public void setReceiverFullName(String receiverFullName) {
            this.receiverFullName = receiverFullName;
        }

        public String getBenfFinalName() {
            return benfFinalName;
        }

        public void setBenfFinalName(String benfFinalName) {
            this.benfFinalName = benfFinalName;
        }

        public String getBenfAccNo() {
            return benfAccNo;
        }

        public void setBenfAccNo(String benfAccNo) {
            this.benfAccNo = benfAccNo;
        }

        public String getBenAccountType() {
            return benAccountType;
        }

        public void setBenAccountType(String benAccountType) {
            this.benAccountType = benAccountType;
        }

        public String getBenBranchName() {
            return benBranchName;
        }

        public void setBenBranchName(String benBranchName) {
            this.benBranchName = benBranchName;
        }

        public String getBenBranchCode() {
            return benBranchCode;
        }

        public void setBenBranchCode(String benBranchCode) {
            this.benBranchCode = benBranchCode;
        }

        public String getBankBranchAddr() {
            return bankBranchAddr;
        }

        public void setBankBranchAddr(String bankBranchAddr) {
            this.bankBranchAddr = bankBranchAddr;
        }

        public String getRecCountryCode() {
            return recCountryCode;
        }

        public void setRecCountryCode(String recCountryCode) {
            this.recCountryCode = recCountryCode;
        }

        public String getRecCountryName() {
            return recCountryName;
        }

        public void setRecCountryName(String recCountryName) {
            this.recCountryName = recCountryName;
        }

        public String getBenNationality() {
            return benNationality;
        }

        public void setBenNationality(String benNationality) {
            this.benNationality = benNationality;
        }

        public String getRecMobileNo() {
            return recMobileNo;
        }

        public void setRecMobileNo(String recMobileNo) {
            this.recMobileNo = recMobileNo;
        }

        public String getRecAddress() {
            return recAddress;
        }

        public void setRecAddress(String recAddress) {
            this.recAddress = recAddress;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPlaceOfBirth() {
            return placeOfBirth;
        }

        public void setPlaceOfBirth(String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getIbanNo() {
            return ibanNo;
        }

        public void setIbanNo(String ibanNo) {
            this.ibanNo = ibanNo;
        }

        public String getResidentialStatus() {
            return residentialStatus;
        }

        public void setResidentialStatus(String residentialStatus) {
            this.residentialStatus = residentialStatus;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getEmployerName() {
            return employerName;
        }

        public void setEmployerName(String employerName) {
            this.employerName = employerName;
        }

        public String getBenEftCode() {
            return benEftCode;
        }

        public void setBenEftCode(String benEftCode) {
            this.benEftCode = benEftCode;
        }

        public String getBankPurposeCode() {
            return bankPurposeCode;
        }

        public void setBankPurposeCode(String bankPurposeCode) {
            this.bankPurposeCode = bankPurposeCode;
        }

        public String getBankPurpose() {
            return bankPurpose;
        }

        public void setBankPurpose(String bankPurpose) {
            this.bankPurpose = bankPurpose;
        }

        public String getIdentificationDtls() {
            return identificationDtls;
        }

        public void setIdentificationDtls(String identificationDtls) {
            this.identificationDtls = identificationDtls;
        }

        public String getResidencyNumber() {
            return residencyNumber;
        }

        public void setResidencyNumber(String residencyNumber) {
            this.residencyNumber = residencyNumber;
        }

        public String getBenEftType() {
            return benEftType;
        }

        public void setBenEftType(String benEftType) {
            this.benEftType = benEftType;
        }

        public String getPROOF_ID_EXP_DATE_BEN() {
            return PROOF_ID_EXP_DATE_BEN;
        }

        public void setPROOF_ID_EXP_DATE_BEN(String PROOF_ID_EXP_DATE_BEN) {
            this.PROOF_ID_EXP_DATE_BEN = PROOF_ID_EXP_DATE_BEN;
        }

        public String getBEN_PLACE_OF_ISSUE() {
            return BEN_PLACE_OF_ISSUE;
        }

        public void setBEN_PLACE_OF_ISSUE(String BEN_PLACE_OF_ISSUE) {
            this.BEN_PLACE_OF_ISSUE = BEN_PLACE_OF_ISSUE;
        }

        public String getBEN_ID_EXPIRY_DATE() {
            return BEN_ID_EXPIRY_DATE;
        }

        public void setBEN_ID_EXPIRY_DATE(String BEN_ID_EXPIRY_DATE) {
            this.BEN_ID_EXPIRY_DATE = BEN_ID_EXPIRY_DATE;
        }

        public String getBEN_COM_REG_NO() {
            return BEN_COM_REG_NO;
        }

        public void setBEN_COM_REG_NO(String BEN_COM_REG_NO) {
            this.BEN_COM_REG_NO = BEN_COM_REG_NO;
        }

        public String getBEN_COMM_REG_PLACE_OF_ISSUE() {
            return BEN_COMM_REG_PLACE_OF_ISSUE;
        }

        public void setBEN_COMM_REG_PLACE_OF_ISSUE(String BEN_COMM_REG_PLACE_OF_ISSUE) {
            this.BEN_COMM_REG_PLACE_OF_ISSUE = BEN_COMM_REG_PLACE_OF_ISSUE;
        }

        public String getBEN_TYPE_OF_ACTIVITY_KEY() {
            return BEN_TYPE_OF_ACTIVITY_KEY;
        }

        public void setBEN_TYPE_OF_ACTIVITY_KEY(String BEN_TYPE_OF_ACTIVITY_KEY) {
            this.BEN_TYPE_OF_ACTIVITY_KEY = BEN_TYPE_OF_ACTIVITY_KEY;
        }

        public String getBEN_PURPOSE_OF_TXN_KEY() {
            return BEN_PURPOSE_OF_TXN_KEY;
        }

        public void setBEN_PURPOSE_OF_TXN_KEY(String BEN_PURPOSE_OF_TXN_KEY) {
            this.BEN_PURPOSE_OF_TXN_KEY = BEN_PURPOSE_OF_TXN_KEY;
        }

        public String getBeneficiaryTypeId() {
            return beneficiaryTypeId;
        }

        public void setBeneficiaryTypeId(String beneficiaryTypeId) {
            this.beneficiaryTypeId = beneficiaryTypeId;
        }

        public String getBeneficiaryTypeDesc() {
            return beneficiaryTypeDesc;
        }

        public void setBeneficiaryTypeDesc(String beneficiaryTypeDesc) {
            this.beneficiaryTypeDesc = beneficiaryTypeDesc;
        }

        public String getRecFirstName() {
            return recFirstName;
        }

        public void setRecFirstName(String recFirstName) {
            this.recFirstName = recFirstName;
        }

        public String getRecSecondName() {
            return recSecondName;
        }

        public void setRecSecondName(String recSecondName) {
            this.recSecondName = recSecondName;
        }

        public String getRecThirdName() {
            return recThirdName;
        }

        public void setRecThirdName(String recThirdName) {
            this.recThirdName = recThirdName;
        }

        public String getRecLastName() {
            return recLastName;
        }

        public void setRecLastName(String recLastName) {
            this.recLastName = recLastName;
        }

        public String getRecIsdPrefix() {
            return recIsdPrefix;
        }

        public void setRecIsdPrefix(String recIsdPrefix) {
            this.recIsdPrefix = recIsdPrefix;
        }

        public String getMESSAGE_TO_REC() {
            return MESSAGE_TO_REC;
        }

        public void setMESSAGE_TO_REC(String MESSAGE_TO_REC) {
            this.MESSAGE_TO_REC = MESSAGE_TO_REC;
        }

        public String getBankChannelCode() {
            return bankChannelCode;
        }

        public void setBankChannelCode(String bankChannelCode) {
            this.bankChannelCode = bankChannelCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.cashPayRecPkId);
            dest.writeString(this.cashPayMainRecFkId);
            dest.writeString(this.receiverFullName);
            dest.writeString(this.benfFinalName);
            dest.writeString(this.benfAccNo);
            dest.writeString(this.benAccountType);
            dest.writeString(this.benBranchName);
            dest.writeString(this.benBranchCode);
            dest.writeString(this.bankBranchAddr);
            dest.writeString(this.recCountryCode);
            dest.writeString(this.recCountryName);
            dest.writeString(this.benNationality);
            dest.writeString(this.recMobileNo);
            dest.writeString(this.recAddress);
            dest.writeString(this.dob);
            dest.writeString(this.placeOfBirth);
            dest.writeString(this.accountType);
            dest.writeString(this.ibanNo);
            dest.writeString(this.residentialStatus);
            dest.writeString(this.middleName);
            dest.writeString(this.familyName);
            dest.writeString(this.profession);
            dest.writeString(this.employerName);
            dest.writeString(this.benEftCode);
            dest.writeString(this.bankPurposeCode);
            dest.writeString(this.bankPurpose);
            dest.writeString(this.identificationDtls);
            dest.writeString(this.residencyNumber);
            dest.writeString(this.benEftType);
            dest.writeString(this.PROOF_ID_EXP_DATE_BEN);
            dest.writeString(this.BEN_PLACE_OF_ISSUE);
            dest.writeString(this.BEN_ID_EXPIRY_DATE);
            dest.writeString(this.BEN_COM_REG_NO);
            dest.writeString(this.BEN_COMM_REG_PLACE_OF_ISSUE);
            dest.writeString(this.BEN_TYPE_OF_ACTIVITY_KEY);
            dest.writeString(this.BEN_PURPOSE_OF_TXN_KEY);
            dest.writeString(this.beneficiaryTypeId);
            dest.writeString(this.beneficiaryTypeDesc);
            dest.writeString(this.recFirstName);
            dest.writeString(this.recSecondName);
            dest.writeString(this.recThirdName);
            dest.writeString(this.recLastName);
            dest.writeString(this.recIsdPrefix);
            dest.writeString(this.MESSAGE_TO_REC);
            dest.writeString(this.bankChannelCode);
        }
    }
}
