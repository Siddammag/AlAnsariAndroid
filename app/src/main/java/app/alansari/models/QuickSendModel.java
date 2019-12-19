package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 25 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class QuickSendModel implements Parcelable {

    /**
     * TYPE : AREX
     * USER_FK_ID : 1
     * NAME : RADFG
     * STATUS : A
     * QUICK_SEND_BENF_PK_ID : 2
     * MEM_BENF_DTL_PK_ID : 1
     * CREATED_DATE : 1485350381000
     */

    @SerializedName("TYPE")
    private String serviceType;
    @SerializedName("USER_FK_ID")
    private String userId;
    @SerializedName("NAME")
    private String name;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("BENF_IMAGE")
    private String benImage;
    @SerializedName("QUICK_SEND_BENF_PK_ID")
    private String id;
    @SerializedName("MEM_BENF_DTL_PK_ID")
    private String beneficiaryId;
    @SerializedName("CREATED_DATE")
    private long createdDate;

    public String getBenImage() {
        return benImage;
    }

    public void setBenImage(String benImage) {
        this.benImage = benImage;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serviceType);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeString(this.id);
        dest.writeString(this.benImage);
        dest.writeString(this.beneficiaryId);
        dest.writeLong(this.createdDate);
    }

    public QuickSendModel() {
    }

    protected QuickSendModel(Parcel in) {
        this.serviceType = in.readString();
        this.userId = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.id = in.readString();
        this.benImage = in.readString();
        this.beneficiaryId = in.readString();
        this.createdDate = in.readLong();
    }

    public static final Parcelable.Creator<QuickSendModel> CREATOR = new Parcelable.Creator<QuickSendModel>() {
        @Override
        public QuickSendModel createFromParcel(Parcel source) {
            return new QuickSendModel(source);
        }

        @Override
        public QuickSendModel[] newArray(int size) {
            return new QuickSendModel[size];
        }
    };
}