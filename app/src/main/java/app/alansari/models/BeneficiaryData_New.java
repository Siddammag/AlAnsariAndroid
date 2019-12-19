package app.alansari.models;

import app.alansari.Utils.Constants;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BeneficiaryData_New implements Parcelable {

    @SerializedName("MEM_BENF_DTL_PK_ID")
    private String beneficiaryId;
    @SerializedName("NAME")
    private String name;
    String firstName;
    String lastName;
    private String bankId;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BANK_CODE")
    private String bankCode;
    private String bankBranchId;
    @SerializedName("BANK_BRANCH_NAME")
    private String bankBranchName;
    private String bankBranchCode;
    @SerializedName("ACCOUNT_NUMBER")
    private String accountNumber;
    @SerializedName("ACCOUNT_TYPE")
    private String accountType;
    private String accountTypeId;
    @SerializedName("CONTACT_NUMBER")
    private String mobileNumber;
    private String countryId;
    private String countryName;
    @SerializedName("COUNTRY_CODE")
    private String countryCode;
    @SerializedName("ADDRESS")
    private String address;
    @SerializedName("IBAN_NUMBER")
    private String IBANNumber;

    private String memberId;
    private String memberAREXId;
    private String memberCEId;
    private String nationalityId, nationality;

    private String aaeBankId, aaeBankName;
    private String serviceType, transferType, paymentMode, transactionNumber, transactionDate;
    private TxnAmountData currencyData;
    private int tempData = 0;


    //New
    private int layoutType = 1;


    public BeneficiaryData_New() {
        beneficiaryId = "";
        memberId = "";
        memberAREXId = "";
        memberCEId = "";
        name = "";
        firstName = "";
        lastName = "";
        countryId = "";
        countryName = "";
        nationalityId = "";
        nationality = "";
        address = "";
        bankId = "";
        bankName = "";
        aaeBankId = "";
        aaeBankName = "";
        transactionDate = "";
        bankBranchId = "";
        bankBranchName = "";
        bankCode = "";
        accountNumber = "";
        accountTypeId = "";
        accountType = "";
        mobileNumber = "";
        IBANNumber = "";
        serviceType = "";
        transferType = "";
        paymentMode = "";
        transactionNumber = "";
        tempData = 0;
        currencyData = new TxnAmountData();
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberAREXId(String memberAREXId) {
        this.memberAREXId = memberAREXId;
    }

    public String getMemberAREXId() {
        return memberAREXId;
    }

    public void setMemberCEId(String memberCEId) {
        this.memberCEId = memberCEId;
    }

    public String getMemberCEId() {
        return memberCEId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setAAEBankId(String aaeBankId) {
        this.aaeBankId = aaeBankId;
    }

    public String getAAEBankId() {
        return aaeBankId;
    }

    public void setAAEBankName(String aaeBankName) {
        this.aaeBankName = aaeBankName;
    }

    public String getAAEBankName() {
        return aaeBankName;
    }

    public void setBankBranchId(String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setMobile(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobile() {
        return mobileNumber;
    }

    public void setIBANNumber(String IBANNumber) {
        this.IBANNumber = IBANNumber;
    }

    public String getIBANNumber() {
        return IBANNumber;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setCurrencyData(TxnAmountData currencyData) {
        this.currencyData = currencyData;
    }

    public TxnAmountData getCurrencyData() {
        return currencyData;
    }


// New

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setTempData(int tempData) {
        this.tempData = tempData;
    }

    public int getTempData() {
        return tempData;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(beneficiaryId);
        dest.writeString(memberId);
        dest.writeString(memberAREXId);
        dest.writeString(memberCEId);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(countryId);
        dest.writeString(countryCode);
        dest.writeString(countryName);
        dest.writeString(nationalityId);
        dest.writeString(nationality);
        dest.writeString(bankId);
        dest.writeString(bankName);
        dest.writeString(aaeBankName);
        dest.writeString(aaeBankName);
        dest.writeString(bankBranchId);
        dest.writeString(bankBranchName);
        dest.writeString(bankCode);
        dest.writeString(IBANNumber);
        dest.writeString(accountNumber);
        dest.writeString(accountTypeId);
        dest.writeString(accountType);
        dest.writeString(mobileNumber);
        dest.writeString(serviceType);
        dest.writeString(transferType);
        dest.writeString(paymentMode);
        dest.writeString(transactionNumber);
        dest.writeString(transactionDate);
        dest.writeParcelable(currencyData, 0);
    }

    //parcel part
    public BeneficiaryData_New(Parcel in) {
        this.beneficiaryId = in.readString();
        this.memberId = in.readString();
        this.memberAREXId = in.readString();
        this.memberCEId = in.readString();
        this.name = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.address = in.readString();
        this.countryId = in.readString();
        this.countryCode = in.readString();
        this.countryName = in.readString();
        this.nationalityId = in.readString();
        this.nationality = in.readString();
        this.bankId = in.readString();
        this.bankName = in.readString();
        this.aaeBankId = in.readString();
        this.aaeBankName = in.readString();
        this.bankBranchId = in.readString();
        this.bankBranchName = in.readString();
        this.bankCode = in.readString();
        this.IBANNumber = in.readString();
        this.accountNumber = in.readString();
        this.accountTypeId = in.readString();
        this.accountType = in.readString();
        this.mobileNumber = in.readString();
        this.serviceType = in.readString();
        this.transferType = in.readString();
        this.paymentMode = in.readString();
        this.transactionNumber = in.readString();
        this.transactionDate = in.readString();
        this.currencyData = in.readParcelable(TxnAmountData.class.getClassLoader());
    }

    public static final Creator<BeneficiaryData_New> CREATOR = new Creator<BeneficiaryData_New>() {

        @Override
        public BeneficiaryData_New createFromParcel(Parcel source) {
            return new BeneficiaryData_New(source);
        }

        @Override
        public BeneficiaryData_New[] newArray(int size) {
            return new BeneficiaryData_New[size];
        }
    };
}
