package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WuCurrencyData implements Parcelable{

    @SerializedName("flag")
    private Object flag;

    @SerializedName("DEFAULT_STATUS")
    private String defaultStatus;

    @SerializedName("WU_CURRENCY_CODE")
    private String wuCurrencyCode;

    @SerializedName("ISO_NAME")
    private Object isoName;

    @SerializedName("CURRENCY_CODE")
    private String currencyCode;

    @SerializedName("NAME")
    private String name;

    @SerializedName("displayKey")
    private String displayKey;

    @SerializedName("displayValue")
    private String displayValue;


    protected WuCurrencyData(Parcel in) {
        defaultStatus = in.readString();
        wuCurrencyCode = in.readString();
        currencyCode = in.readString();
        name = in.readString();
        displayKey = in.readString();
        displayValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(defaultStatus);
        dest.writeString(wuCurrencyCode);
        dest.writeString(currencyCode);
        dest.writeString(name);
        dest.writeString(displayKey);
        dest.writeString(displayValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WuCurrencyData> CREATOR = new Creator<WuCurrencyData>() {
        @Override
        public WuCurrencyData createFromParcel(Parcel in) {
            return new WuCurrencyData(in);
        }

        @Override
        public WuCurrencyData[] newArray(int size) {
            return new WuCurrencyData[size];
        }
    };

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }

    public String getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(String defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public String getWuCurrencyCode() {
        return wuCurrencyCode;
    }

    public void setWuCurrencyCode(String wuCurrencyCode) {
        this.wuCurrencyCode = wuCurrencyCode;
    }

    public Object getIsoName() {
        return isoName;
    }

    public void setIsoName(Object isoName) {
        this.isoName = isoName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayKey() {
        return displayKey;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}