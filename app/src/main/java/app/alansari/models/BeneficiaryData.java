package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BeneficiaryData implements Parcelable, Cloneable {

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
    String firstName;
    String lastName;
    @SerializedName("MEM_BENF_DTL_PK_ID")
    private String beneficiaryId;
    @SerializedName("NAME")
    private String name;
    @SerializedName("ARABIC_NAME")
    private String arabicName;
    @SerializedName("ACCOUNT_NUMBER")
    private String accountNumber;
    @SerializedName("ACCOUNT_TYPE")
    private String accountType;
    private String accountTypeId;
    @SerializedName("CONTACT_NUMBER")
    private String mobileNumber;
    @SerializedName("ADDRESS")
    private String address;
    @SerializedName("NATIONALITY_CODE")
    private String nationalityId;
    @SerializedName("NATIONALITY_DESC")
    private String nationality;
    @SerializedName("IBAN_NUMBER")
    private String IBANNumber;
    @SerializedName("BRANCH_CODE")
    private String branchCode;
    @SerializedName("BANK_BRANCH_NAME")
    private String branchName;
    @SerializedName("MODULE_NAME")
    private String moduleName;
    @SerializedName("IFSC_CODE")
    private String ifscCode;
    @SerializedName("SWIFT")
    private String swift;
    @SerializedName("SORT_CODE")
    private String sortCode;
    @SerializedName("TRANSIT_CODE")
    private String transitCode;
    @SerializedName("INSTITUTION_CODE")
    private String institutionCode;
    @SerializedName("ROUTING_CODE")
    private String routingCode;
    @SerializedName("BSB")
    private String bsb;
    @SerializedName("BENF_IMAGE")
    private String benImage;
    private String memberId;
    private String memberAREXId;
    private String memberCEId;
    @SerializedName("BANK_DATA")
    private BankData bankData;
    private BranchData branchData;
    @SerializedName("COUNTRY_DATA")
    private CountryData countryData;
    private TxnAmountData txnAmountData;
    @SerializedName("SERVICE_TYPE_DATA")
    private ServiceTypeData serviceTypeData;
    private PaymentModeData paymentModeData;
    @SerializedName("USER_ACCOUNT_DATA")
    private UserAccountData userAccountData;
    @SerializedName("AAE_BANK_DATA")
    private AAEBankData aaeBankData;
    private String additionalInfoTimeStamp;
    private String transferType, transactionNumber, transactionDate;
    private String additionalFieldTitle, additionalFieldValue;
    private int tempData = 0;
    @SerializedName("BENEFICIARY_TYPE")
    private int beneficiaryType = 1;
    //New
    private int layoutType = 1;
    private String vat = "";
    private String discount = "";
    private String roundoff = "";
    private String pgsFlag = "";
    private String pgsBankCode = "";
    private String promoCode = "";

    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("COUNTRY_CODE")
    private String countryCode;


    public BeneficiaryData() {
        pgsFlag = "";
        pgsBankCode = "";
        promoCode = "";
        vat = "";
        discount = "";
        roundoff = "";
        beneficiaryId = "";
        memberId = "";
        memberAREXId = "";
        memberCEId = "";
        name = "";
        firstName = "";
        lastName = "";
        address = "";
        transactionDate = "";
        accountNumber = "";
        accountTypeId = "";
        accountType = "";
        mobileNumber = "";
        transferType = "";
        transactionNumber = "";
        additionalInfoTimeStamp = "";
        tempData = 0;
        beneficiaryType = 1;
        bankData = new BankData();
        branchData = new BranchData();
        countryData = new CountryData();
        txnAmountData = new TxnAmountData();
        serviceTypeData = new ServiceTypeData();
        paymentModeData = new PaymentModeData();
        userAccountData = new UserAccountData();
        aaeBankData = new AAEBankData();
    }

    public BeneficiaryData(int tempData) {
        txnAmountData = new TxnAmountData();
        this.tempData = tempData;
    }

    protected BeneficiaryData(Parcel in) {
        this.benImage = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.beneficiaryId = in.readString();
        this.name = in.readString();
        this.arabicName = in.readString();
        this.accountNumber = in.readString();
        this.accountType = in.readString();
        this.accountTypeId = in.readString();
        this.mobileNumber = in.readString();
        this.address = in.readString();
        this.nationalityId = in.readString();
        this.nationality = in.readString();
        this.IBANNumber = in.readString();
        this.branchCode = in.readString();
        this.branchName = in.readString();
        this.moduleName=in.readString();
        this.ifscCode = in.readString();
        this.swift = in.readString();
        this.sortCode = in.readString();
        this.transitCode = in.readString();
        this.institutionCode = in.readString();
        this.routingCode = in.readString();
        this.bsb = in.readString();
        this.memberId = in.readString();
        this.memberAREXId = in.readString();
        this.memberCEId = in.readString();
        this.bankData = in.readParcelable(BankData.class.getClassLoader());
        this.branchData = in.readParcelable(BranchData.class.getClassLoader());
        this.countryData = in.readParcelable(CountryData.class.getClassLoader());
        this.txnAmountData = in.readParcelable(TxnAmountData.class.getClassLoader());
        this.serviceTypeData = in.readParcelable(ServiceTypeData.class.getClassLoader());
        this.paymentModeData = in.readParcelable(PaymentModeData.class.getClassLoader());
        this.userAccountData = in.readParcelable(UserAccountData.class.getClassLoader());
        this.aaeBankData = in.readParcelable(AAEBankData.class.getClassLoader());
        this.additionalInfoTimeStamp = in.readString();
        this.transferType = in.readString();
        this.transactionNumber = in.readString();
        this.transactionDate = in.readString();
        this.additionalFieldTitle = in.readString();
        this.additionalFieldValue = in.readString();
        this.tempData = in.readInt();
        this.beneficiaryType = in.readInt();
        this.layoutType = in.readInt();
        this.vat = in.readString();
        this.discount = in.readString();
        this.roundoff = in.readString();
        this.pgsFlag = in.readString();
        this.pgsBankCode = in.readString();
        this.promoCode = in.readString();
        this.bankCode=in.readString();
        this.countryCode=in.readString();
    }

    public String getBenImage() {
        return benImage;
    }

    public void setBenImage(String benImage) {
        this.benImage = benImage;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberAREXId() {
        return memberAREXId;
    }

    public void setMemberAREXId(String memberAREXId) {
        this.memberAREXId = memberAREXId;
    }

    public String getMemberCEId() {
        return memberCEId;
    }

    public void setMemberCEId(String memberCEId) {
        this.memberCEId = memberCEId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIBANNumber() {
        return IBANNumber;
    }

    public void setIBANNumber(String IBANNumber) {
        this.IBANNumber = IBANNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(String accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMobile() {
        return mobileNumber;
    }

    public void setMobile(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getTransitCode() {
        return transitCode;
    }

    public void setTransitCode(String transitCode) {
        this.transitCode = transitCode;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getAdditionalInfoTimeStamp() {
        return additionalInfoTimeStamp;
    }

    public void setAdditionalInfoTimeStamp(String additionalInfoTimeStamp) {
        this.additionalInfoTimeStamp = additionalInfoTimeStamp;
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

    public int getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(int beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
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

    public CountryData getCountryData() {
        return countryData;
    }

    public void setCountryData(CountryData countryData) {
        this.countryData = countryData;
    }

    public TxnAmountData getTxnAmountData() {
        return txnAmountData;
    }

    public void setTxnAmountData(TxnAmountData txnAmountData) {
        this.txnAmountData = txnAmountData;
    }

    public ServiceTypeData getServiceTypeData() {
        return serviceTypeData;
    }

    public void setServiceTypeData(ServiceTypeData serviceTypeData) {
        this.serviceTypeData = serviceTypeData;
    }

    public PaymentModeData getPaymentModeData() {
        return paymentModeData;
    }

    public void setPaymentModeData(PaymentModeData paymentModeData) {
        this.paymentModeData = paymentModeData;
    }

    public UserAccountData getUserAccountData() {
        return userAccountData;
    }

    public void setUserAccountData(UserAccountData userAccountData) {
        this.userAccountData = userAccountData;
    }

    public AAEBankData getAaeBankData() {
        return aaeBankData;
    }

    public void setAaeBankData(AAEBankData aaeBankData) {
        this.aaeBankData = aaeBankData;
    }

    public int getLayoutType() {
        return layoutType;
    }

    // New
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getTempData() {
        return tempData;
    }

    public void setTempData(int tempData) {
        this.tempData = tempData;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRoundoff() {
        return roundoff;
    }

    public void setRoundoff(String roundoff) {
        this.roundoff = roundoff;
    }

    public String getPgsFlag() {
        return pgsFlag;
    }

    public void setPgsFlag(String pgsFlag) {
        this.pgsFlag = pgsFlag;
    }

    public String getPgsBankCode() {
        return pgsBankCode;
    }

    public void setPgsBankCode(String pgsBankCode) {
        this.pgsBankCode = pgsBankCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.benImage);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.beneficiaryId);
        dest.writeString(this.name);
        dest.writeString(this.arabicName);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountType);
        dest.writeString(this.accountTypeId);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.address);
        dest.writeString(this.nationalityId);
        dest.writeString(this.nationality);
        dest.writeString(this.IBANNumber);
        dest.writeString(this.branchCode);
        dest.writeString(this.branchName);
        dest.writeString(this.moduleName);
        dest.writeString(this.ifscCode);
        dest.writeString(this.swift);
        dest.writeString(this.sortCode);
        dest.writeString(this.transitCode);
        dest.writeString(this.institutionCode);
        dest.writeString(this.routingCode);
        dest.writeString(this.bsb);
        dest.writeString(this.memberId);
        dest.writeString(this.memberAREXId);
        dest.writeString(this.memberCEId);
        dest.writeParcelable(this.bankData, flags);
        dest.writeParcelable(this.branchData, flags);
        dest.writeParcelable(this.countryData, flags);
        dest.writeParcelable(this.txnAmountData, flags);
        dest.writeParcelable(this.serviceTypeData, flags);
        dest.writeParcelable(this.paymentModeData, flags);
        dest.writeParcelable(this.userAccountData, flags);
        dest.writeParcelable(this.aaeBankData, flags);
        dest.writeString(this.additionalInfoTimeStamp);
        dest.writeString(this.transferType);
        dest.writeString(this.transactionNumber);
        dest.writeString(this.transactionDate);
        dest.writeString(this.additionalFieldTitle);
        dest.writeString(this.additionalFieldValue);
        dest.writeInt(this.tempData);
        dest.writeInt(this.beneficiaryType);
        dest.writeInt(this.layoutType);
        dest.writeString(this.vat);
        dest.writeString(this.discount);
        dest.writeString(this.roundoff);
        dest.writeString(this.pgsFlag);
        dest.writeString(this.pgsBankCode);
        dest.writeString(this.promoCode);
        dest.writeString(this.bankCode);
        dest.writeString(this.countryCode);
    }
}
