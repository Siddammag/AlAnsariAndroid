package app.alansari.models.TravelCardDatasToAndFrom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DatasToAndFrom implements Parcelable{
    String etTo,etFrom,ToFlag,fromFlag;

    protected DatasToAndFrom(Parcel in) {
        etTo = in.readString();
        etFrom = in.readString();
        ToFlag = in.readString();
        fromFlag = in.readString();
    }

    public static final Creator<DatasToAndFrom> CREATOR = new Creator<DatasToAndFrom>() {
        @Override
        public DatasToAndFrom createFromParcel(Parcel in) {
            return new DatasToAndFrom(in);
        }

        @Override
        public DatasToAndFrom[] newArray(int size) {
            return new DatasToAndFrom[size];
        }
    };

    public String getEtTo() {
        return etTo;
    }

    public void setEtTo(String etTo) {
        this.etTo = etTo;
    }

    public String getEtFrom() {
        return etFrom;
    }

    public void setEtFrom(String etFrom) {
        this.etFrom = etFrom;
    }

    public String getToFlag() {
        return ToFlag;
    }

    public void setToFlag(String toFlag) {
        ToFlag = toFlag;
    }

    public String getFromFlag() {
        return fromFlag;
    }

    public void setFromFlag(String fromFlag) {
        this.fromFlag = fromFlag;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(etTo);
        dest.writeString(etFrom);
        dest.writeString(ToFlag);
        dest.writeString(fromFlag);
    }
}
