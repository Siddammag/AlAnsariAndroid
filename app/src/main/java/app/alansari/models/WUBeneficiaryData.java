package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WUBeneficiaryData implements Parcelable {

    public WUBeneficiaryData() {
    }

    @SerializedName("receiverLastName")
    private String receiverLastName;

    @SerializedName("arexCountryCode")
    private String arexCountryCode;

    @SerializedName("receiverMiddleName")
    private String receiverMiddleName;

    @SerializedName("idType")
    private String idType;

    @SerializedName("receiverCountryName")
    private String receiverCountryName;

    @SerializedName("referenceNo")
    private String referenceNo;

    @SerializedName("receiverCountryCode")
    private String receiverCountryCode;

    @SerializedName("city")
    private String city;

    @SerializedName("cityCode")
    private String cityCode;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("idPlaceOfIssue")
    private String idPlaceOfIssue;

    @SerializedName("receiverContactNumber")
    private String receiverContactNumber;

    @SerializedName("idNumber")
    private String idNumber;

    @SerializedName("receiverAddress")
    private String receiverAddress;

    @SerializedName("receiverNameType")
    private String receiverNameType;

    @SerializedName("receiverContactIsdCode")
    private String receiverContactIsdCode;

    @SerializedName("receiverCurrencyCode")
    private String receiverCurrencyCode;

    @SerializedName("receiverCurrencyName")
    private String receiverCurrencyName;

    @SerializedName("arexCurrencyCode")
    private String arexCurrencyCode;

    @SerializedName("idCountryOfIssue")
    private String idCountryOfIssue;

    @SerializedName("receiverFirstName")
    private String receiverFirstName;

    @SerializedName("state")
    private String state;

    @SerializedName("debtorAccountNumber")
    private String debtorAccountNumber;

    @SerializedName("receiverIndexNumber")
    private String receiverIndexNumber;

    @SerializedName("countryDetails")
    private WuBeneficiaryCountryDetails countryDetails;

    @SerializedName("additionalInfoList")
    private ArrayList<WUAdditionalInfoFields> additionalInfoList;

    @SerializedName("WU_LOOKUP_PROMO_CODE")
    private String promoCode;

    private BankData bankData;
    private String vat = "";
    private TxnAmountData txnAmountData;
    private String additionalInfoTimeStamp;

    protected WUBeneficiaryData(Parcel in) {
        receiverLastName = in.readString();
        arexCountryCode = in.readString();
        receiverMiddleName = in.readString();
        idType = in.readString();
        receiverCountryName = in.readString();
        referenceNo = in.readString();
        receiverCountryCode = in.readString();
        city = in.readString();
        cityCode = in.readString();
        companyName = in.readString();
        idPlaceOfIssue = in.readString();
        receiverContactNumber = in.readString();
        idNumber = in.readString();
        receiverAddress = in.readString();
        receiverNameType = in.readString();
        receiverContactIsdCode = in.readString();
        receiverCurrencyCode = in.readString();
        receiverCurrencyName = in.readString();
        arexCurrencyCode = in.readString();
        idCountryOfIssue = in.readString();
        receiverFirstName = in.readString();
        state = in.readString();
        debtorAccountNumber = in.readString();
        receiverIndexNumber = in.readString();
        countryDetails = in.readParcelable(WuBeneficiaryCountryDetails.class.getClassLoader());
        additionalInfoList = in.createTypedArrayList(WUAdditionalInfoFields.CREATOR);
        bankData = in.readParcelable(BankData.class.getClassLoader());
        vat = in.readString();
        promoCode = in.readString();
        txnAmountData = in.readParcelable(TxnAmountData.class.getClassLoader());
        additionalInfoTimeStamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(receiverLastName);
        dest.writeString(arexCountryCode);
        dest.writeString(receiverMiddleName);
        dest.writeString(idType);
        dest.writeString(receiverCountryName);
        dest.writeString(referenceNo);
        dest.writeString(receiverCountryCode);
        dest.writeString(city);
        dest.writeString(cityCode);
        dest.writeString(companyName);
        dest.writeString(idPlaceOfIssue);
        dest.writeString(receiverContactNumber);
        dest.writeString(idNumber);
        dest.writeString(receiverAddress);
        dest.writeString(receiverNameType);
        dest.writeString(receiverContactIsdCode);
        dest.writeString(receiverCurrencyCode);
        dest.writeString(receiverCurrencyName);
        dest.writeString(arexCurrencyCode);
        dest.writeString(idCountryOfIssue);
        dest.writeString(receiverFirstName);
        dest.writeString(state);
        dest.writeString(debtorAccountNumber);
        dest.writeString(receiverIndexNumber);
        dest.writeParcelable(countryDetails, flags);
        dest.writeTypedList(additionalInfoList);
        dest.writeParcelable(bankData, flags);
        dest.writeString(vat);
        dest.writeString(promoCode);
        dest.writeParcelable(txnAmountData, flags);
        dest.writeString(additionalInfoTimeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WUBeneficiaryData> CREATOR = new Creator<WUBeneficiaryData>() {
        @Override
        public WUBeneficiaryData createFromParcel(Parcel in) {
            return new WUBeneficiaryData(in);
        }

        @Override
        public WUBeneficiaryData[] newArray(int size) {
            return new WUBeneficiaryData[size];
        }
    };

    public String getReceiverLastName() {
        return receiverLastName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public String getArexCountryCode() {
        return arexCountryCode;
    }

    public void setArexCountryCode(String arexCountryCode) {
        this.arexCountryCode = arexCountryCode;
    }

    public String getReceiverMiddleName() {
        return receiverMiddleName;
    }

    public void setReceiverMiddleName(String receiverMiddleName) {
        this.receiverMiddleName = receiverMiddleName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getReceiverCountryName() {
        return receiverCountryName;
    }

    public void setReceiverCountryName(String receiverCountryName) {
        this.receiverCountryName = receiverCountryName;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getReceiverCountryCode() {
        return receiverCountryCode;
    }

    public void setReceiverCountryCode(String receiverCountryCode) {
        this.receiverCountryCode = receiverCountryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIdPlaceOfIssue() {
        return idPlaceOfIssue;
    }

    public void setIdPlaceOfIssue(String idPlaceOfIssue) {
        this.idPlaceOfIssue = idPlaceOfIssue;
    }

    public String getReceiverContactNumber() {
        return receiverContactNumber;
    }

    public void setReceiverContactNumber(String receiverContactNumber) {
        this.receiverContactNumber = receiverContactNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverNameType() {
        return receiverNameType;
    }

    public void setReceiverNameType(String receiverNameType) {
        this.receiverNameType = receiverNameType;
    }

    public String getReceiverContactIsdCode() {
        return receiverContactIsdCode;
    }

    public void setReceiverContactIsdCode(String receiverContactIsdCode) {
        this.receiverContactIsdCode = receiverContactIsdCode;
    }

    public String getReceiverCurrencyCode() {
        return receiverCurrencyCode;
    }

    public void setReceiverCurrencyCode(String receiverCurrencyCode) {
        this.receiverCurrencyCode = receiverCurrencyCode;
    }

    public String getReceiverCurrencyName() {
        return receiverCurrencyName;
    }

    public void setReceiverCurrencyName(String receiverCurrencyName) {
        this.receiverCurrencyName = receiverCurrencyName;
    }

    public String getArexCurrencyCode() {
        return arexCurrencyCode;
    }

    public void setArexCurrencyCode(String arexCurrencyCode) {
        this.arexCurrencyCode = arexCurrencyCode;
    }

    public String getIdCountryOfIssue() {
        return idCountryOfIssue;
    }

    public void setIdCountryOfIssue(String idCountryOfIssue) {
        this.idCountryOfIssue = idCountryOfIssue;
    }

    public String getReceiverFirstName() {
        return receiverFirstName;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDebtorAccountNumber() {
        return debtorAccountNumber;
    }

    public void setDebtorAccountNumber(String debtorAccountNumber) {
        this.debtorAccountNumber = debtorAccountNumber;
    }

    public String getReceiverIndexNumber() {
        return receiverIndexNumber;
    }

    public void setReceiverIndexNumber(String receiverIndexNumber) {
        this.receiverIndexNumber = receiverIndexNumber;
    }

    public WuBeneficiaryCountryDetails getCountryDetails() {
        return countryDetails;
    }

    public void setCountryDetails(WuBeneficiaryCountryDetails countryDetails) {
        this.countryDetails = countryDetails;
    }

    public ArrayList<WUAdditionalInfoFields> getAdditionalInfoList() {
        return additionalInfoList;
    }

    public void setAdditionalInfoList(ArrayList<WUAdditionalInfoFields> additionalInfoList) {
        this.additionalInfoList = additionalInfoList;
    }

    public BankData getBankData() {
        return bankData;
    }

    public void setBankData(BankData bankData) {
        this.bankData = bankData;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public TxnAmountData getTxnAmountData() {
        return txnAmountData;
    }

    public void setTxnAmountData(TxnAmountData txnAmountData) {
        this.txnAmountData = txnAmountData;
    }

    public String getAdditionalInfoTimeStamp() {
        return additionalInfoTimeStamp;
    }

    public void setAdditionalInfoTimeStamp(String additionalInfoTimeStamp) {
        this.additionalInfoTimeStamp = additionalInfoTimeStamp;
    }
}