package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Parveen Dala on 26 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TxnAmountData implements Parcelable {

    private String youSend, youGet, totalToPay, fee, rate;
    private CountryData.CurrencyData youSendCurrencyData, youGetCurrencyData;

    public TxnAmountData() {
    }

    public String getYouSend() {
        return youSend;
    }

    public void setYouSend(String youSend) {
        this.youSend = youSend;
    }

    public String getYouGet() {
        return youGet;
    }

    public void setYouGet(String youGet) {
        this.youGet = youGet;
    }

    public String getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(String totalToPay) {
        this.totalToPay = totalToPay;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public CountryData.CurrencyData getYouSendCurrencyData() {
        return youSendCurrencyData;
    }

    public void setYouSendCurrencyData(CountryData.CurrencyData youSendCurrencyData) {
        this.youSendCurrencyData = youSendCurrencyData;
    }

    public CountryData.CurrencyData getYouGetCurrencyData() {
        return youGetCurrencyData;
    }

    public void setYouGetCurrencyData(CountryData.CurrencyData youGetCurrencyData) {
        this.youGetCurrencyData = youGetCurrencyData;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(youSend);
        dest.writeString(youGet);
        dest.writeString(totalToPay);
        dest.writeString(fee);
        dest.writeString(rate);
        dest.writeParcelable(youGetCurrencyData, 0);
        dest.writeParcelable(youSendCurrencyData, 0);
    }

    //parcel part
    public TxnAmountData(Parcel in) {
        this.youSend = in.readString();
        this.youGet = in.readString();
        this.totalToPay = in.readString();
        this.fee = in.readString();
        this.rate = in.readString();
        this.youGetCurrencyData = in.readParcelable(CountryData.CurrencyData.class.getClassLoader());
        this.youSendCurrencyData = in.readParcelable(CountryData.CurrencyData.class.getClassLoader());
    }

    public static final Creator<TxnAmountData> CREATOR = new Creator<TxnAmountData>() {

        @Override
        public TxnAmountData createFromParcel(Parcel source) {
            return new TxnAmountData(source);
        }

        @Override
        public TxnAmountData[] newArray(int size) {
            return new TxnAmountData[size];
        }
    };
}
