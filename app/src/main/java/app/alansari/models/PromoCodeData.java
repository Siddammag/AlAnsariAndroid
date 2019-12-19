package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PromoCodeData implements Parcelable {

    @SerializedName("PROM_PK_ID")
    private int pROMPKID;

    @SerializedName("DISPLAY_SEQUENCE")
    private int dISPLAYSEQUENCE;

    @SerializedName("ACTIVE_IND")
    private String aCTIVEIND;

    @SerializedName("PROMO_CODE")
    private String pROMOCODE;

    @SerializedName("PROMO_VALUE")
    private String pROMOVALUE;

    @SerializedName("PROMO_MESSAGE")
    private String promoMessage;

    protected PromoCodeData(Parcel in) {
        pROMPKID = in.readInt();
        dISPLAYSEQUENCE = in.readInt();
        aCTIVEIND = in.readString();
        pROMOCODE = in.readString();
        pROMOVALUE = in.readString();
        promoMessage = in.readString();
    }

    public static final Creator<PromoCodeData> CREATOR = new Creator<PromoCodeData>() {
        @Override
        public PromoCodeData createFromParcel(Parcel in) {
            return new PromoCodeData(in);
        }

        @Override
        public PromoCodeData[] newArray(int size) {
            return new PromoCodeData[size];
        }
    };

    public void setPROMPKID(int pROMPKID) {
        this.pROMPKID = pROMPKID;
    }

    public int getPROMPKID() {
        return pROMPKID;
    }

    public void setDISPLAYSEQUENCE(int dISPLAYSEQUENCE) {
        this.dISPLAYSEQUENCE = dISPLAYSEQUENCE;
    }

    public int getDISPLAYSEQUENCE() {
        return dISPLAYSEQUENCE;
    }

    public void setACTIVEIND(String aCTIVEIND) {
        this.aCTIVEIND = aCTIVEIND;
    }

    public String getACTIVEIND() {
        return aCTIVEIND;
    }

    public void setPROMOCODE(String pROMOCODE) {
        this.pROMOCODE = pROMOCODE;
    }

    public String getPROMOCODE() {
        return pROMOCODE;
    }

    public void setPROMOVALUE(String pROMOVALUE) {
        this.pROMOVALUE = pROMOVALUE;
    }

    public String getPROMOVALUE() {
        return pROMOVALUE;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    @Override
    public String toString() {
        return
                "PromoCodeData{" +
                        "pROM_PK_ID = '" + pROMPKID + '\'' +
                        ",dISPLAY_SEQUENCE = '" + dISPLAYSEQUENCE + '\'' +
                        ",aCTIVE_IND = '" + aCTIVEIND + '\'' +
                        ",pROMO_CODE = '" + pROMOCODE + '\'' +
                        ",pROMO_VALUE = '" + pROMOVALUE + '\'' +
                        ",promoMessage = '" + promoMessage + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pROMPKID);
        dest.writeInt(dISPLAYSEQUENCE);
        dest.writeString(aCTIVEIND);
        dest.writeString(pROMOCODE);
        dest.writeString(pROMOVALUE);
        dest.writeString(promoMessage);
    }
}