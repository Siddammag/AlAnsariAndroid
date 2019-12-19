package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardData implements Parcelable {


    public static final Parcelable.Creator<CreditCardData> CREATOR = new Parcelable.Creator<CreditCardData>() {
        @Override
        public CreditCardData createFromParcel(Parcel source) {
            return new CreditCardData(source);
        }

        @Override
        public CreditCardData[] newArray(int size) {
            return new CreditCardData[size];
        }
    };
    /**
     * MEM_CC_PK_ID : 1010
     * MEM_CARD_FK_ID : 213
     * NAME : Sri
     * CARD_NUMBER : 123456789
     * CARD_TYPE_CODE :
     * CARD_TYPE_DESC : VISA
     * BANK_CODE : 1054
     * BANK_NAME : HDFC Bank
     * PAYMENT_DATE : 2017-02-11
     * REMINDER : 2
     * CREATED_DATE :
     * APPROVAL_STATUS : A
     * MSG_CORRELATION_NO :
     * REPORT_UPDATE_FLAG :
     * IS_DELETED : N
     * DELETED_DATE :
     * DELETED_BY :
     */

    @SerializedName("MEM_CC_PK_ID")
    private String id;
    @SerializedName("MEM_CARD_FK_ID")
    private String memCardFkId;
    @SerializedName("NAME")
    private String name;
    @SerializedName("CARD_NUMBER")
    private String cardNumber;
    @SerializedName("CARD_TYPE_CODE")
    private String cardTypeCode;
    @SerializedName("CARD_TYPE_DESC")
    private String cardTypeName;
    @SerializedName("BANK_CODE")
    private String bankCode;
    @SerializedName("BANK_NAME")
    private String bankName;
    @SerializedName("PAYMENT_DATE")
    private String paymentDate = "1";
    @SerializedName("REMINDER")
    private String reminder;
    @SerializedName("CREATED_DATE")
    private String createdDate;
    @SerializedName("APPROVAL_STATUS")
    private String approvalStatus;
    @SerializedName("MSG_CORRELATION_NO")
    private String msgCorrelationNumber;
    @SerializedName("REPORT_UPDATE_FLAG")
    private String reportUpdateFlag;
    @SerializedName("IS_DELETED")
    private String isDeleted;
    @SerializedName("DELETED_DATE")
    private String deletedDate;
    @SerializedName("DELETED_BY")
    private String deletedBy;

    public CreditCardData() {
    }


    public CreditCardData(int layoutType, String name1) {
        switch (layoutType) {
            case 0:
                name = "Homer Simpson";
                bankName = "Axis Bank";
                break;
            case 1:
                name = "Parveen Kumar";
                bankName = "SBI Bank";
                break;
            case 2:
                name = "Fugenx Tech";
                bankName = "ICICI Bank";
                break;
            case 3:
                name = "Bengaluru Ltd.";
                bankName = "HDFC Bank";
                break;
            case 4:
                name = "Android Team";
                bankName = "SBI Bank";
                break;
            default:
                name = "P Kumar";
                bankName = "Axis Bank";
                break;
        }
        cardNumber = "4591658410327594";
        cardTypeName = "MASTER";
        paymentDate = "2016-01-01";
    }

    protected CreditCardData(Parcel in) {
        this.id = in.readString();
        this.memCardFkId = in.readString();
        this.name = in.readString();
        this.cardNumber = in.readString();
        this.cardTypeCode = in.readString();
        this.cardTypeName = in.readString();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.paymentDate = in.readString();
        this.reminder = in.readString();
        this.createdDate = in.readString();
        this.approvalStatus = in.readString();
        this.msgCorrelationNumber = in.readString();
        this.reportUpdateFlag = in.readString();
        this.isDeleted = in.readString();
        this.deletedDate = in.readString();
        this.deletedBy = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemCardFkId() {
        return memCardFkId;
    }

    public void setMemCardFkId(String memCardFkId) {
        this.memCardFkId = memCardFkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
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

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getMsgCorrelationNumber() {
        return msgCorrelationNumber;
    }

    public void setMsgCorrelationNumber(String msgCorrelationNumber) {
        this.msgCorrelationNumber = msgCorrelationNumber;
    }

    public String getReportUpdateFlag() {
        return reportUpdateFlag;
    }

    public void setReportUpdateFlag(String reportUpdateFlag) {
        this.reportUpdateFlag = reportUpdateFlag;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.memCardFkId);
        dest.writeString(this.name);
        dest.writeString(this.cardNumber);
        dest.writeString(this.cardTypeCode);
        dest.writeString(this.cardTypeName);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.paymentDate);
        dest.writeString(this.reminder);
        dest.writeString(this.createdDate);
        dest.writeString(this.approvalStatus);
        dest.writeString(this.msgCorrelationNumber);
        dest.writeString(this.reportUpdateFlag);
        dest.writeString(this.isDeleted);
        dest.writeString(this.deletedDate);
        dest.writeString(this.deletedBy);
    }
}
