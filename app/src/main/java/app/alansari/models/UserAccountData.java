package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Parveen Dala on 22 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class UserAccountData implements Parcelable {

    public static final Creator<UserAccountData> CREATOR = new Creator<UserAccountData>() {

        @Override
        public UserAccountData createFromParcel(Parcel source) {
            return new UserAccountData(source);
        }

        @Override
        public UserAccountData[] newArray(int size) {
            return new UserAccountData[size];
        }
    };

    /**
     * BANK_ID : 9000004000
     * STATUS : A
     * BANK_NAME : AXIS BANK LTD
     * BANK_BRANCH_NAME : null
     * IBAN_NUMBER : 123456789
     * ACCOUNT_PK_ID : 4
     * USER_PK_ID : 1
     * ACCOUNT_NUMBER : 123456789
     * ACCOUNT_NAME : CURRENT
     */

    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("BANK_BRANCH_NAME")
    private String bankBranchName;
    @SerializedName("IBAN_NUMBER")
    private String IBANNumber;
    @SerializedName("ACC_PK_ID")
    private String id;
    @SerializedName("USER_PK_ID")
    private String userPkId;
    @SerializedName("ACCOUNT_NUMBER")
    private String accountNumber;
    @SerializedName("ACCOUNT_NAME")
    private String accountName;

    @SerializedName("PAY_TYPE_BT_LIST")
    private List<PAYTYPEBTLISTItem> pAYTYPEBTLIST;

    @SerializedName("FT_LEARN_MORE_URL")
    private String fTLEARNMOREURL;

    @SerializedName("OB_LEARN_MORE_URL")
    private String oBLEARNMOREURL;


    public UserAccountData() {
    }

    public UserAccountData(int layoutType, String name1) {
        switch (layoutType) {
            case 0:
                accountName = "Parveen Kumar";
                bankBranchName = "Bengaluru";
                accountNumber = "123456789123456";
                bankName = "Axis Bank";
                IBANNumber = "5643216543213";
                break;
            case 1:
                accountName = "David Warner";
                bankBranchName = "Dubai";
                accountNumber = "658456789368452";
                bankName = "Dubai Islamic Bank";
                IBANNumber = "2316165432154";
                break;
            case 2:
                accountName = "Aboo Backer";
                bankBranchName = "Delhi";
                accountNumber = "578456789368854";
                bankName = "HDFC Bank";
                IBANNumber = "987516546547";
                break;
        }
    }


    //parcel part
    public UserAccountData(Parcel in) {
        this.id = in.readString();
        this.userPkId = in.readString();
        this.accountName = in.readString();
        this.accountNumber = in.readString();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.bankBranchName = in.readString();
        this.IBANNumber = in.readString();
        this.status = in.readString();
        this.fTLEARNMOREURL = in.readString();
        this.oBLEARNMOREURL = in.readString();
        //this.pAYTYPEBTLIST=in.readArrayList(PAYTYPEBTLISTItem.class.getClassLoader());
        //this.pAYTYPEBTLIST=in.readArrayList(null);
        this.pAYTYPEBTLIST=in.readArrayList(PAYTYPEBTLISTItem.class.getClassLoader());

    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getIBANNumber() {
        return IBANNumber;
    }

    public void setIBANNumber(String IBANNumber) {
        this.IBANNumber = IBANNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPkId() {
        return userPkId;
    }

    public void setUserPkId(String userPkId) {
        this.userPkId = userPkId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public void setPAYTYPEBTLIST(List<PAYTYPEBTLISTItem> pAYTYPEBTLIST){
        this.pAYTYPEBTLIST = pAYTYPEBTLIST;
    }

    public List<PAYTYPEBTLISTItem> getPAYTYPEBTLIST(){
        return pAYTYPEBTLIST;
    }

    public void setFTLEARNMOREURL(String fTLEARNMOREURL){
        this.fTLEARNMOREURL = fTLEARNMOREURL;
    }

    public String getFTLEARNMOREURL(){
        return fTLEARNMOREURL;
    }

    public void setOBLEARNMOREURL(String oBLEARNMOREURL){
        this.oBLEARNMOREURL = oBLEARNMOREURL;
    }

    public String getOBLEARNMOREURL(){
        return oBLEARNMOREURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userPkId);
        dest.writeString(accountName);
        dest.writeString(accountNumber);
        dest.writeString(bankCode);
        dest.writeString(bankName);
        dest.writeString(bankBranchName);
        dest.writeString(IBANNumber);
        dest.writeString(status);
        dest.writeString(fTLEARNMOREURL);
        dest.writeString(oBLEARNMOREURL);
        dest.writeList(pAYTYPEBTLIST);
    }


    public static class PAYTYPEBTLISTItem implements Parcelable {

        @SerializedName("displayKey")
        private String displayKey;

        @SerializedName("displayValue")
        private String displayValue;

        @SerializedName("primaryKeyValue")
        private String primaryKeyValue;

        protected PAYTYPEBTLISTItem(Parcel in) {
            displayKey = in.readString();
            displayValue = in.readString();
            primaryKeyValue = in.readString();
        }

        public static final Creator<PAYTYPEBTLISTItem> CREATOR = new Creator<PAYTYPEBTLISTItem>() {
            @Override
            public PAYTYPEBTLISTItem createFromParcel(Parcel in) {
                return new PAYTYPEBTLISTItem(in);
            }

            @Override
            public PAYTYPEBTLISTItem[] newArray(int size) {
                return new PAYTYPEBTLISTItem[size];
            }
        };

        public void setDisplayKey(String displayKey){
            this.displayKey = displayKey;
        }

        public String getDisplayKey(){
            return displayKey;
        }

        public void setDisplayValue(String displayValue){
            this.displayValue = displayValue;
        }

        public String getDisplayValue(){
            return displayValue;
        }

        public void setPrimaryKeyValue(String primaryKeyValue){
            this.primaryKeyValue = primaryKeyValue;
        }

        public String getPrimaryKeyValue(){
            return primaryKeyValue;
        }

        @Override
        public String toString(){
            return
                    "PAYTYPEBTLISTItem{" +
                            "displayKey = '" + displayKey + '\'' +
                            ",displayValue = '" + displayValue + '\'' +
                            ",primaryKeyValue = '" + primaryKeyValue + '\'' +
                            "}";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(displayKey);
            dest.writeString(displayValue);
            dest.writeString(primaryKeyValue);
        }
    }
}
