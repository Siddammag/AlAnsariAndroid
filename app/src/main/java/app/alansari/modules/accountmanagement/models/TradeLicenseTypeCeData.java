package app.alansari.modules.accountmanagement.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 15 July, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class TradeLicenseTypeCeData implements Parcelable {

    /**
     * COUNTRY_CODE : 24
     * ACTIVE_IND : Y
     * ID_TYPE_DESC : RESIDENT PERMIT
     * ID_PROOF_PK_ID : 573
     * ID_PROOF_TYPE : I
     * GRACE_DAYS : 90
     * ID_ALLOWED_FOR_WPS : Y
     */

    @SerializedName("COUNTRY_CODE")
    private String countryCode;
    @SerializedName("ACTIVE_IND")
    private String active;
    @SerializedName("ID_TYPE_DESC")
    private String idTypeDesc;
    @SerializedName("ID_PROOF_PK_ID")
    private int id;
    @SerializedName("ID_PROOF_TYPE")
    private String idProofType;
    @SerializedName("GRACE_DAYS")
    private String graceDays;
    @SerializedName("ID_ALLOWED_FOR_WPS")
    private String idAllowedForWps;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIdTypeDesc() {
        return idTypeDesc;
    }

    public void setIdTypeDesc(String idTypeDesc) {
        this.idTypeDesc = idTypeDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(String graceDays) {
        this.graceDays = graceDays;
    }

    public String getIdAllowedForWps() {
        return idAllowedForWps;
    }

    public void setIdAllowedForWps(String idAllowedForWps) {
        this.idAllowedForWps = idAllowedForWps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryCode);
        dest.writeString(this.active);
        dest.writeString(this.idTypeDesc);
        dest.writeInt(this.id);
        dest.writeString(this.idProofType);
        dest.writeString(this.graceDays);
        dest.writeString(this.idAllowedForWps);
    }

    public TradeLicenseTypeCeData() {
    }

    protected TradeLicenseTypeCeData(Parcel in) {
        this.countryCode = in.readString();
        this.active = in.readString();
        this.idTypeDesc = in.readString();
        this.id = in.readInt();
        this.idProofType = in.readString();
        this.graceDays = in.readString();
        this.idAllowedForWps = in.readString();
    }

    public static final Parcelable.Creator<TradeLicenseTypeCeData> CREATOR = new Parcelable.Creator<TradeLicenseTypeCeData>() {
        @Override
        public TradeLicenseTypeCeData createFromParcel(Parcel source) {
            return new TradeLicenseTypeCeData(source);
        }

        @Override
        public TradeLicenseTypeCeData[] newArray(int size) {
            return new TradeLicenseTypeCeData[size];
        }
    };
}
