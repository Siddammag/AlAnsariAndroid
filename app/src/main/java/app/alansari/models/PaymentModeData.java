package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 03 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class PaymentModeData implements Parcelable {


    /**
     * STATUS : 1
     * MODE_PK_ID : 1
     * MODE_NAME : PAY AT BRANCH
     * MODE_DESCRIPTION : SIMPLY DUMMY TEXT OF THE PRINTING AND TYPE SETTING INDUSTRY
     */


    @SerializedName("MODE_PK_ID")
    private String id;
    @SerializedName("MODE_NAME")
    private String name;
    @SerializedName("MAPPING")
    private String mapping;
    @SerializedName("MODE_DESCRIPTION")
    private String description;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("MESSAGE")
    private String message;
    @SerializedName("ADDITIONAL_CHARGES")
    private String additionalCharges;
    @SerializedName("TOTAL_TXN_AMOUNT")
    private String totalTxnAmount;

    public PaymentModeData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getTotalTxnAmount() {
        return totalTxnAmount;
    }

    public void setTotalTxnAmount(String totalTxnAmount) {
        this.totalTxnAmount = totalTxnAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mapping);
        dest.writeString(this.description);
        dest.writeString(this.status);
        dest.writeString(this.message);
        dest.writeString(this.additionalCharges);
        dest.writeString(this.totalTxnAmount);
    }

    protected PaymentModeData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mapping = in.readString();
        this.description = in.readString();
        this.status = in.readString();
        this.message = in.readString();
        this.additionalCharges = in.readString();
        this.totalTxnAmount = in.readString();
    }

    public static final Creator<PaymentModeData> CREATOR = new Creator<PaymentModeData>() {
        @Override
        public PaymentModeData createFromParcel(Parcel source) {
            return new PaymentModeData(source);
        }

        @Override
        public PaymentModeData[] newArray(int size) {
            return new PaymentModeData[size];
        }
    };
}
