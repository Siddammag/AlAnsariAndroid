package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TradeLicenseDateTypeCeData implements Parcelable {

    /**
     * name : ISSUE DATE
     * code : I
     */

    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
    }

    public TradeLicenseDateTypeCeData() {
    }

    protected TradeLicenseDateTypeCeData(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
    }

    public static final Parcelable.Creator<TradeLicenseDateTypeCeData> CREATOR = new Parcelable.Creator<TradeLicenseDateTypeCeData>() {
        @Override
        public TradeLicenseDateTypeCeData createFromParcel(Parcel source) {
            return new TradeLicenseDateTypeCeData(source);
        }

        @Override
        public TradeLicenseDateTypeCeData[] newArray(int size) {
            return new TradeLicenseDateTypeCeData[size];
        }
    };
}
