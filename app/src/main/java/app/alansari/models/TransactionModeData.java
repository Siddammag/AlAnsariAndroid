package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 27 December, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TransactionModeData implements Parcelable {


    /**
     * SERVICE_PK_ID : 1
     * SERVICE_NAME : VALUE TRANSFER
     * SERVICE_MAPPING : AREX
     * REMIT_TIME : 1-3 DAYS
     * STATUS : 1
     */

    @SerializedName("SERVICE_PK_ID")
    private String id;
    @SerializedName("SERVICE_NAME")
    private String name;
    @SerializedName("SERVICE_MAPPING")
    private String mapping;
    @SerializedName("REMIT_TIME")
    private String remitTime;
    @SerializedName("STATUS")
    private String status;

    public TransactionModeData() {
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

    public String getRemitTime() {
        return remitTime;
    }

    public void setRemitTime(String remitTime) {
        this.remitTime = remitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mapping);
        dest.writeString(remitTime);
        dest.writeString(status);
    }

    protected TransactionModeData(Parcel in) {
        id = in.readString();
        name = in.readString();
        mapping = in.readString();
        remitTime = in.readString();
        status = in.readString();
    }

    public static final Creator<TransactionModeData> CREATOR = new Creator<TransactionModeData>() {
        @Override
        public TransactionModeData createFromParcel(Parcel in) {
            return new TransactionModeData(in);
        }

        @Override
        public TransactionModeData[] newArray(int size) {
            return new TransactionModeData[size];
        }
    };
}
