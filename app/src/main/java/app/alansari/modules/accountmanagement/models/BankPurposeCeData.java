package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class BankPurposeCeData implements Parcelable {

    public static final Parcelable.Creator<BankPurposeCeData> CREATOR = new Parcelable.Creator<BankPurposeCeData>() {
        @Override
        public BankPurposeCeData createFromParcel(Parcel source) {
            return new BankPurposeCeData(source);
        }

        @Override
        public BankPurposeCeData[] newArray(int size) {
            return new BankPurposeCeData[size];
        }
    };
    /**
     * APPROVAL_STATUS : A
     * CREATED_DATE : 1345925066000
     * MSG_CORRELATION_NO : null
     * MODIFIED_DATE : 1350465693000
     * CREATED_BY : thomas
     * MODIFIED_BY : abedi
     * CREATER_NAME : THOMAS VARUGHESE
     * MODIFIER_NAME : MIR ABUL QASIM ABEDI
     * APPROVED_BY : thomas
     * APPROVER_NAME : THOMAS VARUGHESE
     * APPROVED_DATE : 1350466044000
     * LAST_UPD_CORRELATION_NO : 294
     * ACTIVE_IND : Y
     * REASON_TO_EDIT : FULL DESCRIPTION
     * BENEF_BANK_PURPOSE_MSTR_PK_ID : 192
     * BENEF_BANK_CODE : 9999042260027
     * BENEF_BANK_NAME : UNION BANK OF INDIA
     * PURPOSE_CODE : NA 5
     * PURPOSE_DESC : PAYMENT TO COOP. HOUSING SOCIETIES, GOVT. HOUSING SCHEMES OR ESTATE DEVELOPERS FOR ACQUISITION OF RESIDENTIAL FLATS IN INDIA IN INDIVIDUAL NAMES SUBJECT TO COMPLIANCE OF REGULATIONS THEREOF BY THE NON-RESIDENT INDIANS
     */

    @SerializedName("APPROVAL_STATUS")
    private String approvalStatus;
    @SerializedName("CREATED_DATE")
    private long createdDate;
    @SerializedName("MODIFIED_DATE")
    private long modifiedDate;
    @SerializedName("CREATED_BY")
    private String createdBy;
    @SerializedName("MODIFIED_BY")
    private String modifiedBy;
    @SerializedName("CREATER_NAME")
    private String createrName;
    @SerializedName("MODIFIER_NAME")
    private String modifierName;
    @SerializedName("APPROVED_BY")
    private String approvedBy;
    @SerializedName("APPROVER_NAME")
    private String approverName;
    @SerializedName("APPROVED_DATE")
    private long approvedDate;
    @SerializedName("LAST_UPD_CORRELATION_NO")
    private String lastUpdCorrelationNo;
    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("REASON_TO_EDIT")
    private String reasonToEdit;
    @SerializedName("BENEF_BANK_PURPOSE_MSTR_PK_ID")
    private int id;
    @SerializedName("BENEF_BANK_CODE")
    private String bankCode;
    @SerializedName("BENEF_BANK_NAME")
    private String bankName;
    @SerializedName("PURPOSE_CODE")
    private String purposeCode;
    @SerializedName("PURPOSE_DESC")
    private String purposeDesc;

    public BankPurposeCeData() {
    }

    protected BankPurposeCeData(Parcel in) {
        this.approvalStatus = in.readString();
        this.createdDate = in.readLong();
        this.modifiedDate = in.readLong();
        this.createdBy = in.readString();
        this.modifiedBy = in.readString();
        this.createrName = in.readString();
        this.modifierName = in.readString();
        this.approvedBy = in.readString();
        this.approverName = in.readString();
        this.approvedDate = in.readLong();
        this.lastUpdCorrelationNo = in.readString();
        this.active = in.readString();
        this.reasonToEdit = in.readString();
        this.id = in.readInt();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.purposeCode = in.readString();
        this.purposeDesc = in.readString();
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public long getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(long approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getLastUpdCorrelationNo() {
        return lastUpdCorrelationNo;
    }

    public void setLastUpdCorrelationNo(String lastUpdCorrelationNo) {
        this.lastUpdCorrelationNo = lastUpdCorrelationNo;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getReasonToEdit() {
        return reasonToEdit;
    }

    public void setReasonToEdit(String reasonToEdit) {
        this.reasonToEdit = reasonToEdit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getPurposeDesc() {
        return purposeDesc;
    }

    public void setPurposeDesc(String purposeDesc) {
        this.purposeDesc = purposeDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approvalStatus);
        dest.writeLong(this.createdDate);
        dest.writeLong(this.modifiedDate);
        dest.writeString(this.createdBy);
        dest.writeString(this.modifiedBy);
        dest.writeString(this.createrName);
        dest.writeString(this.modifierName);
        dest.writeString(this.approvedBy);
        dest.writeString(this.approverName);
        dest.writeLong(this.approvedDate);
        dest.writeString(this.lastUpdCorrelationNo);
        dest.writeString(this.active);
        dest.writeString(this.reasonToEdit);
        dest.writeInt(this.id);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.purposeCode);
        dest.writeString(this.purposeDesc);
    }
}
