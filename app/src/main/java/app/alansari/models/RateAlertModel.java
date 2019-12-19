package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 21 February, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class RateAlertModel implements Parcelable {


    /**
     * RATE_ALERT_PK_ID : 4
     * DESC_CCY_CODE : 26
     * DESC_CCY_NAME : INR
     * AMOUNT : 18
     * USER_FK_ID : 123
     * CREATED_DATE : 1488264989000
     * MODIFIED_DATE : null
     * STATUS : A
     */

    @SerializedName("RATE_ALERT_PK_ID")
    private String id;
    @SerializedName("DESC_CCY_CODE")
    private String descCcyCode;
    @SerializedName("DESC_CCY_NAME")
    private String descCcyName;
    @SerializedName("FROM_CCY_CODE")
    private String fromCcyCode;
    @SerializedName("FROM_CCY_NAME")
    private String fromCcyName;
    @SerializedName("AMOUNT")
    private String amount;
    @SerializedName("USER_FK_ID")
    private String userId;
    @SerializedName("CREATED_DATE")
    private String createdDate;
    @SerializedName("MODIFIED_DATE")
    private String modifiedDate;
    @SerializedName("STATUS")
    private String status;

    public RateAlertModel() {
    }


    protected RateAlertModel(Parcel in) {
        id = in.readString();
        descCcyCode = in.readString();
        descCcyName = in.readString();
        fromCcyCode = in.readString();
        fromCcyName = in.readString();
        amount = in.readString();
        userId = in.readString();
        createdDate = in.readString();
        modifiedDate = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(descCcyCode);
        dest.writeString(descCcyName);
        dest.writeString(fromCcyCode);
        dest.writeString(fromCcyName);
        dest.writeString(amount);
        dest.writeString(userId);
        dest.writeString(createdDate);
        dest.writeString(modifiedDate);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RateAlertModel> CREATOR = new Creator<RateAlertModel>() {
        @Override
        public RateAlertModel createFromParcel(Parcel in) {
            return new RateAlertModel(in);
        }

        @Override
        public RateAlertModel[] newArray(int size) {
            return new RateAlertModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescCcyCode() {
        return descCcyCode;
    }

    public void setDescCcyCode(String descCcyCode) {
        this.descCcyCode = descCcyCode;
    }

    public String getDescCcyName() {
        return descCcyName;
    }

    public void setDescCcyName(String descCcyName) {
        this.descCcyName = descCcyName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromCcyCode() {
        return fromCcyCode;
    }

    public void setFromCcyCode(String fromCcyCode) {
        this.fromCcyCode = fromCcyCode;
    }

    public String getFromCcyName() {
        return fromCcyName;
    }

    public void setFromCcyName(String fromCcyName) {
        this.fromCcyName = fromCcyName;
    }


}