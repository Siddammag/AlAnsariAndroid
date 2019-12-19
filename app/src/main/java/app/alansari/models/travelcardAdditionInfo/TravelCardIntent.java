package app.alansari.models.travelcardAdditionInfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TravelCardIntent  implements Parcelable{
    List<DataInOut> dataInOutList;
    String totalAedAmount;


    public List<DataInOut> getDataInOutList() {
        return dataInOutList;
    }

    public void setDataInOutList(List<DataInOut> dataInOutList) {
        this.dataInOutList = dataInOutList;
    }

    public String getTotalAedAmount() {
        return totalAedAmount;
    }

    public void setTotalAedAmount(String totalAedAmount) {
        this.totalAedAmount = totalAedAmount;
    }

    public static Creator<TravelCardIntent> getCREATOR() {
        return CREATOR;
    }

    protected TravelCardIntent(Parcel in) {
        dataInOutList = in.createTypedArrayList(DataInOut.CREATOR);
        totalAedAmount = in.readString();
    }

    public static final Creator<TravelCardIntent> CREATOR = new Creator<TravelCardIntent>() {
        @Override
        public TravelCardIntent createFromParcel(Parcel in) {
            return new TravelCardIntent(in);
        }

        @Override
        public TravelCardIntent[] newArray(int size) {
            return new TravelCardIntent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(dataInOutList);
        parcel.writeString(totalAedAmount);
    }


    public static class DataInOut implements Parcelable {
        String fromFlag;
        String toFlag;
        String from;
        String to;

        public String getFromFlag() {
            return fromFlag;
        }

        public void setFromFlag(String fromFlag) {
            this.fromFlag = fromFlag;
        }

        public String getToFlag() {
            return toFlag;
        }

        public void setToFlag(String toFlag) {
            this.toFlag = toFlag;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        protected DataInOut(Parcel in) {
            fromFlag = in.readString();
            toFlag = in.readString();
            from = in.readString();
            to = in.readString();
        }

        public static final Creator<DataInOut> CREATOR = new Creator<DataInOut>() {
            @Override
            public DataInOut createFromParcel(Parcel in) {
                return new DataInOut(in);
            }

            @Override
            public DataInOut[] newArray(int size) {
                return new DataInOut[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(fromFlag);
            parcel.writeString(toFlag);
            parcel.writeString(from);
            parcel.writeString(to);
        }
    }
}
