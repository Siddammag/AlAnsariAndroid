package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class PurposeCeData implements Parcelable {


    public static final Parcelable.Creator<PurposeCeData> CREATOR = new Parcelable.Creator<PurposeCeData>() {
        @Override
        public PurposeCeData createFromParcel(Parcel source) {
            return new PurposeCeData(source);
        }

        @Override
        public PurposeCeData[] newArray(int size) {
            return new PurposeCeData[size];
        }
    };
    /**
     * TXN_TYPE : S
     * PURPOSE_ID : 1
     * PUPOSES : PERSONAL NEEDS
     */

    @SerializedName("TXN_TYPE")
    private String txnType;
    @SerializedName("PURPOSE_ID")
    private int id;
    @SerializedName("PUPOSES")
    private String name;

    public PurposeCeData() {
    }

    protected PurposeCeData(Parcel in) {
        this.txnType = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.txnType);
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }
}
