package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MyEmiratespersonalInfoData implements Parcelable{
    @SerializedName("USER_PK_ID")
    String userPkId;
    @SerializedName("AREX_MEM_FK_ID")
    String arexMemFkId;
    @SerializedName("CE_MEM_FK_ID")
    String ceMemFkId;
    @SerializedName("EID_NUMBER")
    String eidNumber;
    @SerializedName("EID_CARD_NUMBER")
    String eidCardnumber;
    @SerializedName("ID_EXPIRTY_DATE")
    String idExpirtyDate;

    protected MyEmiratespersonalInfoData(Parcel in) {
        userPkId = in.readString();
        arexMemFkId = in.readString();
        ceMemFkId = in.readString();
        eidNumber = in.readString();
        eidCardnumber = in.readString();
        idExpirtyDate = in.readString();
    }

    public static final Creator<MyEmiratespersonalInfoData> CREATOR = new Creator<MyEmiratespersonalInfoData>() {
        @Override
        public MyEmiratespersonalInfoData createFromParcel(Parcel in) {
            return new MyEmiratespersonalInfoData(in);
        }

        @Override
        public MyEmiratespersonalInfoData[] newArray(int size) {
            return new MyEmiratespersonalInfoData[size];
        }
    };

    public String getUserPkId() {
        return userPkId;
    }

    public void setUserPkId(String userPkId) {
        this.userPkId = userPkId;
    }

    public String getArexMemFkId() {
        return arexMemFkId;
    }

    public void setArexMemFkId(String arexMemFkId) {
        this.arexMemFkId = arexMemFkId;
    }

    public String getCeMemFkId() {
        return ceMemFkId;
    }

    public void setCeMemFkId(String ceMemFkId) {
        this.ceMemFkId = ceMemFkId;
    }

    public String getEidNumber() {
        return eidNumber;
    }

    public void setEidNumber(String eidNumber) {
        this.eidNumber = eidNumber;
    }

    public String getEidCardnumber() {
        return eidCardnumber;
    }

    public void setEidCardnumber(String eidCardnumber) {
        this.eidCardnumber = eidCardnumber;
    }

    public String getIdExpirtyDate() {
        return idExpirtyDate;
    }

    public void setIdExpirtyDate(String idExpirtyDate) {
        this.idExpirtyDate = idExpirtyDate;
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
        dest.writeString(userPkId);
        dest.writeString(arexMemFkId);
        dest.writeString(ceMemFkId);
        dest.writeString(eidNumber);
        dest.writeString(eidCardnumber);
        dest.writeString(idExpirtyDate);
    }
}
