package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CurrencyData implements Parcelable {

    @SerializedName("flag")
    private String flag;

    @SerializedName("DEFAULT_STATUS")
    private Object dEFAULTSTATUS;

    @SerializedName("CURRENCY_CODE")
    private String cURRENCYCODE;

    @SerializedName("NAME")
    private String nAME;

    @SerializedName("ISO_NAME")
    private String shortName;

    protected CurrencyData(Parcel in) {
        flag = in.readString();
        cURRENCYCODE = in.readString();
        nAME = in.readString();
        shortName = in.readString();
    }

    public static final Creator<CurrencyData> CREATOR = new Creator<CurrencyData>() {
        @Override
        public CurrencyData createFromParcel(Parcel in) {
            return new CurrencyData(in);
        }

        @Override
        public CurrencyData[] newArray(int size) {
            return new CurrencyData[size];
        }
    };

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setDEFAULTSTATUS(Object dEFAULTSTATUS) {
        this.dEFAULTSTATUS = dEFAULTSTATUS;
    }

    public Object getDEFAULTSTATUS() {
        return dEFAULTSTATUS;
    }

    public void setCURRENCYCODE(String cURRENCYCODE) {
        this.cURRENCYCODE = cURRENCYCODE;
    }

    public String getCURRENCYCODE() {
        return cURRENCYCODE;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getNAME() {
        return nAME;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return
                "CurrencyData{" +
                        "flag = '" + flag + '\'' +
                        ",dEFAULT_STATUS = '" + dEFAULTSTATUS + '\'' +
                        ",cURRENCY_CODE = '" + cURRENCYCODE + '\'' +
                        ",nAME = '" + nAME + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flag);
        dest.writeString(cURRENCYCODE);
        dest.writeString(nAME);
        dest.writeString(shortName);
    }
}