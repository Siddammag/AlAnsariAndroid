package app.alansari.models.travalcardvalidateflag;

import android.os.Parcel;
import android.os.Parcelable;

import app.alansari.models.getCharges.ResultItem;

public class TravelCardAdapterItem implements Parcelable {
    TravelCardFlag travelCardFlag;
    ResultItem resultItem;
    boolean showSingleLine;
    String toCurrency;
    String fromCurrency;
    int direction;

    public TravelCardAdapterItem() {
    }

    protected TravelCardAdapterItem(Parcel in) {
        travelCardFlag = in.readParcelable(TravelCardFlag.class.getClassLoader());
        resultItem = in.readParcelable(ResultItem.class.getClassLoader());
        showSingleLine = in.readByte() != 0;
        toCurrency = in.readString();
        fromCurrency = in.readString();
        direction = in.readInt();
    }

    public static final Creator<TravelCardAdapterItem> CREATOR = new Creator<TravelCardAdapterItem>() {
        @Override
        public TravelCardAdapterItem createFromParcel(Parcel in) {
            return new TravelCardAdapterItem(in);
        }

        @Override
        public TravelCardAdapterItem[] newArray(int size) {
            return new TravelCardAdapterItem[size];
        }
    };

    public TravelCardFlag getTravelCardFlag() {
        return travelCardFlag;
    }

    public void setTravelCardFlag(TravelCardFlag travelCardFlag) {
        this.travelCardFlag = travelCardFlag;
    }

    public ResultItem getResultItem() {
        return resultItem;
    }

    public void setResultItem(ResultItem resultItem) {
        this.resultItem = resultItem;
    }

    public boolean isShowSingleLine() {
        return showSingleLine;
    }

    public void setShowSingleLine(boolean showSingleLine) {
        this.showSingleLine = showSingleLine;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(travelCardFlag, i);
        parcel.writeParcelable(resultItem, i);
        parcel.writeByte((byte) (showSingleLine ? 1 : 0));
        parcel.writeString(toCurrency);
        parcel.writeString(fromCurrency);
        parcel.writeInt(direction);
    }
}
