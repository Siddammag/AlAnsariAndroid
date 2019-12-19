package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parveen Dala on 26 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CurrencyCodeData implements Parcelable {

    private String countryId, countryName;
    private String currencyCodeId, currencyCode;

    public CurrencyCodeData() {
        countryId = "";
        countryName = "";
        currencyCodeId = "";
        currencyCode = "";
    }

    public CurrencyCodeData(String currencyCodeId, String currencyCode) {
        this.currencyCodeId = currencyCodeId;
        this.currencyCode = currencyCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCodeId(String currencyCodeId) {
        this.currencyCodeId = currencyCodeId;
    }

    public String getCurrencyCodeId() {
        return currencyCodeId;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(countryId);
        dest.writeString(countryName);
        dest.writeString(currencyCode);
        dest.writeString(currencyCodeId);
    }

    //parcel part
    public CurrencyCodeData(Parcel in) {
        this.countryId = in.readString();
        this.countryName = in.readString();
        this.currencyCode = in.readString();
        this.currencyCodeId = in.readString();
    }

    public static final Creator<CurrencyCodeData> CREATOR = new Creator<CurrencyCodeData>() {

        @Override
        public CurrencyCodeData createFromParcel(Parcel source) {
            return new CurrencyCodeData(source);
        }

        @Override
        public CurrencyCodeData[] newArray(int size) {
            return new CurrencyCodeData[size];
        }
    };
}
