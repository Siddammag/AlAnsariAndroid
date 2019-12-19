package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parveen Dala on 02 March, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardRemittanceData implements Parcelable {

    public static final Creator<CreditCardRemittanceData> CREATOR = new Creator<CreditCardRemittanceData>() {

        @Override
        public CreditCardRemittanceData createFromParcel(Parcel source) {
            return new CreditCardRemittanceData(source);
        }

        @Override
        public CreditCardRemittanceData[] newArray(int size) {
            return new CreditCardRemittanceData[size];
        }
    };


    private String id;


    private BankData bankData;
    private BranchData branchData;
    private TxnAmountData txnAmountData;
    private PaymentModeData paymentModeData;
    private String aaeBankId, aaeBankName;
    private String transactionNumber, transactionDate;
    private String additionalFieldTitle, additionalFieldValue;

    public CreditCardRemittanceData() {
        id = "";
        aaeBankId = "";
        aaeBankName = "";
        transactionDate = "";
        transactionNumber = "";
        bankData = new BankData();
        branchData = new BranchData();
        txnAmountData = new TxnAmountData();
        paymentModeData = new PaymentModeData();
    }

    //parcel part
    public CreditCardRemittanceData(Parcel in) {
        this.id = in.readString();
        this.aaeBankId = in.readString();
        this.aaeBankName = in.readString();
        this.transactionNumber = in.readString();
        this.transactionDate = in.readString();
        this.additionalFieldTitle = in.readString();
        this.additionalFieldValue = in.readString();
        this.bankData = in.readParcelable(BankData.class.getClassLoader());
        this.branchData = in.readParcelable(BranchData.class.getClassLoader());
        this.txnAmountData = in.readParcelable(TxnAmountData.class.getClassLoader());
        this.paymentModeData = in.readParcelable(PaymentModeData.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAAEBankId() {
        return aaeBankId;
    }

    public void setAAEBankId(String aaeBankId) {
        this.aaeBankId = aaeBankId;
    }

    public String getAAEBankName() {
        return aaeBankName;
    }

    public void setAAEBankName(String aaeBankName) {
        this.aaeBankName = aaeBankName;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAdditionalFieldTitle() {
        return additionalFieldTitle;
    }

    public void setAdditionalFieldTitle(String additionalFieldTitle) {
        this.additionalFieldTitle = additionalFieldTitle;
    }

    public String getAdditionalFieldValue() {
        return additionalFieldValue;
    }

    public void setAdditionalFieldValue(String additionalFieldValue) {
        this.additionalFieldValue = additionalFieldValue;
    }

    public BankData getBankData() {
        return bankData;
    }

    public void setBankData(BankData bankData) {
        this.bankData = bankData;
    }

    public BranchData getBranchData() {
        return branchData;
    }

    public void setBranchData(BranchData branchData) {
        this.branchData = branchData;
    }

    public TxnAmountData getTxnAmountData() {
        return txnAmountData;
    }

    public void setTxnAmountData(TxnAmountData txnAmountData) {
        this.txnAmountData = txnAmountData;
    }

    public PaymentModeData getPaymentModeData() {
        return paymentModeData;
    }

    public void setPaymentModeData(PaymentModeData paymentModeData) {
        this.paymentModeData = paymentModeData;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(aaeBankName);
        dest.writeString(aaeBankName);
        dest.writeString(transactionNumber);
        dest.writeString(transactionDate);
        dest.writeString(additionalFieldTitle);
        dest.writeString(additionalFieldValue);
        dest.writeParcelable(bankData, 0);
        dest.writeParcelable(branchData, 0);
        dest.writeParcelable(txnAmountData, 0);
        dest.writeParcelable(paymentModeData, 0);
    }
}
