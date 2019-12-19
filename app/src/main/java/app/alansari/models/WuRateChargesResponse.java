package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WuRateChargesResponse implements Parcelable{

    @SerializedName("PROMO_REQ_MSG")
    private String pROMOREQMSG;

    @SerializedName("RATE")
    private String rATE;

    @SerializedName("CHARGES")
    private String cHARGES;

    @SerializedName("STATUS_CODE")
    private int sTATUSCODE;

    @SerializedName("DEST_PRINCIPAL_AMOUNT")
    private String dESTPRINCIPALAMOUNT;

    @SerializedName("STATUS_MSG")
    private String sTATUSMSG;

    @SerializedName("MESSAGE")
    private String mESSAGE;

    @SerializedName("PROMO_NAME")
    private String pROMONAME;

    @SerializedName("ORIGINAL_PRINCIPLE_AMOUNT")
    private String oRIGINALPRINCIPLEAMOUNT;

    @SerializedName("PROMO_DISCOUNT_AMOUNT")
    private String pROMODISCOUNTAMOUNT;

    @SerializedName("TAX_WORK_SHEET")
    private String tAXWORKSHEET;

    @SerializedName("GROSS_TOTAL_AMOUNT")
    private String gROSSTOTALAMOUNT;

    @SerializedName("DELIVERY_CHARGES")
    private String dELIVERYCHARGES;

    @SerializedName("TAX_RATE")
    private String tAXRATE;

    @SerializedName("PROMO_CODE")
    private String pROMOCODE;

    @SerializedName("TOTAL_TXN_AMOUNT_WO_VAT")
    private String tOTALTXNAMOUNTWOVAT;

    @SerializedName("FEE_ENQ_TXN_TYPE")
    private String fEEENQTXNTYPE;

    @SerializedName("IS_TEST_QUESTION_AVAILABLE")
    private String iSTESTQUESTIONAVAILABLE;

    @SerializedName("REFERENCE_NO")
    private String rEFERENCENO;

    @SerializedName("VAT_CHARGES")
    private String vATCHARGES;

    protected WuRateChargesResponse(Parcel in) {
        pROMOREQMSG = in.readString();
        rATE = in.readString();
        cHARGES = in.readString();
        sTATUSCODE = in.readInt();
        dESTPRINCIPALAMOUNT = in.readString();
        sTATUSMSG = in.readString();
        mESSAGE = in.readString();
        pROMONAME = in.readString();
        oRIGINALPRINCIPLEAMOUNT = in.readString();
        pROMODISCOUNTAMOUNT = in.readString();
        tAXWORKSHEET = in.readString();
        gROSSTOTALAMOUNT = in.readString();
        dELIVERYCHARGES = in.readString();
        tAXRATE = in.readString();
        pROMOCODE = in.readString();
        tOTALTXNAMOUNTWOVAT = in.readString();
        fEEENQTXNTYPE = in.readString();
        iSTESTQUESTIONAVAILABLE = in.readString();
        rEFERENCENO = in.readString();
        vATCHARGES = in.readString();
    }

    public static final Creator<WuRateChargesResponse> CREATOR = new Creator<WuRateChargesResponse>() {
        @Override
        public WuRateChargesResponse createFromParcel(Parcel in) {
            return new WuRateChargesResponse(in);
        }

        @Override
        public WuRateChargesResponse[] newArray(int size) {
            return new WuRateChargesResponse[size];
        }
    };

    public void setPROMOREQMSG(String pROMOREQMSG) {
        this.pROMOREQMSG = pROMOREQMSG;
    }

    public String getPROMOREQMSG() {
        return pROMOREQMSG;
    }

    public void setRATE(String rATE) {
        this.rATE = rATE;
    }

    public String getRATE() {
        return rATE;
    }

    public void setCHARGES(String cHARGES) {
        this.cHARGES = cHARGES;
    }

    public String getCHARGES() {
        return cHARGES;
    }

    public void setSTATUSCODE(int sTATUSCODE) {
        this.sTATUSCODE = sTATUSCODE;
    }

    public int getSTATUSCODE() {
        return sTATUSCODE;
    }

    public void setDESTPRINCIPALAMOUNT(String dESTPRINCIPALAMOUNT) {
        this.dESTPRINCIPALAMOUNT = dESTPRINCIPALAMOUNT;
    }

    public String getDESTPRINCIPALAMOUNT() {
        return dESTPRINCIPALAMOUNT;
    }

    public void setSTATUSMSG(String sTATUSMSG) {
        this.sTATUSMSG = sTATUSMSG;
    }

    public String getSTATUSMSG() {
        return sTATUSMSG;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setPROMONAME(String pROMONAME) {
        this.pROMONAME = pROMONAME;
    }

    public String getPROMONAME() {
        return pROMONAME;
    }

    public void setORIGINALPRINCIPLEAMOUNT(String oRIGINALPRINCIPLEAMOUNT) {
        this.oRIGINALPRINCIPLEAMOUNT = oRIGINALPRINCIPLEAMOUNT;
    }

    public String getORIGINALPRINCIPLEAMOUNT() {
        return oRIGINALPRINCIPLEAMOUNT;
    }

    public void setPROMODISCOUNTAMOUNT(String pROMODISCOUNTAMOUNT) {
        this.pROMODISCOUNTAMOUNT = pROMODISCOUNTAMOUNT;
    }

    public String getPROMODISCOUNTAMOUNT() {
        return pROMODISCOUNTAMOUNT;
    }

    public void setTAXWORKSHEET(String tAXWORKSHEET) {
        this.tAXWORKSHEET = tAXWORKSHEET;
    }

    public String getTAXWORKSHEET() {
        return tAXWORKSHEET;
    }

    public void setGROSSTOTALAMOUNT(String gROSSTOTALAMOUNT) {
        this.gROSSTOTALAMOUNT = gROSSTOTALAMOUNT;
    }

    public String getGROSSTOTALAMOUNT() {
        return gROSSTOTALAMOUNT;
    }

    public void setDELIVERYCHARGES(String dELIVERYCHARGES) {
        this.dELIVERYCHARGES = dELIVERYCHARGES;
    }

    public String getDELIVERYCHARGES() {
        return dELIVERYCHARGES;
    }

    public void setTAXRATE(String tAXRATE) {
        this.tAXRATE = tAXRATE;
    }

    public String getTAXRATE() {
        return tAXRATE;
    }

    public void setPROMOCODE(String pROMOCODE) {
        this.pROMOCODE = pROMOCODE;
    }

    public String getPROMOCODE() {
        return pROMOCODE;
    }

    public void setTOTALTXNAMOUNTWOVAT(String tOTALTXNAMOUNTWOVAT) {
        this.tOTALTXNAMOUNTWOVAT = tOTALTXNAMOUNTWOVAT;
    }

    public String getTOTALTXNAMOUNTWOVAT() {
        return tOTALTXNAMOUNTWOVAT;
    }

    public void setFEEENQTXNTYPE(String fEEENQTXNTYPE) {
        this.fEEENQTXNTYPE = fEEENQTXNTYPE;
    }

    public String getFEEENQTXNTYPE() {
        return fEEENQTXNTYPE;
    }

    public void setISTESTQUESTIONAVAILABLE(String iSTESTQUESTIONAVAILABLE) {
        this.iSTESTQUESTIONAVAILABLE = iSTESTQUESTIONAVAILABLE;
    }

    public String getISTESTQUESTIONAVAILABLE() {
        return iSTESTQUESTIONAVAILABLE;
    }

    public void setREFERENCENO(String rEFERENCENO) {
        this.rEFERENCENO = rEFERENCENO;
    }

    public String getREFERENCENO() {
        return rEFERENCENO;
    }

    public void setVATCHARGES(String vATCHARGES) {
        this.vATCHARGES = vATCHARGES;
    }

    public String getVATCHARGES() {
        return vATCHARGES;
    }

    @Override
    public String toString() {
        return
                "WuRateChargesResponse{" +
                        "pROMO_REQ_MSG = '" + pROMOREQMSG + '\'' +
                        ",rATE = '" + rATE + '\'' +
                        ",cHARGES = '" + cHARGES + '\'' +
                        ",sTATUS_CODE = '" + sTATUSCODE + '\'' +
                        ",dEST_PRINCIPAL_AMOUNT = '" + dESTPRINCIPALAMOUNT + '\'' +
                        ",sTATUS_MSG = '" + sTATUSMSG + '\'' +
                        ",mESSAGE = '" + mESSAGE + '\'' +
                        ",pROMO_NAME = '" + pROMONAME + '\'' +
                        ",oRIGINAL_PRINCIPLE_AMOUNT = '" + oRIGINALPRINCIPLEAMOUNT + '\'' +
                        ",pROMO_DISCOUNT_AMOUNT = '" + pROMODISCOUNTAMOUNT + '\'' +
                        ",tAX_WORK_SHEET = '" + tAXWORKSHEET + '\'' +
                        ",gROSS_TOTAL_AMOUNT = '" + gROSSTOTALAMOUNT + '\'' +
                        ",dELIVERY_CHARGES = '" + dELIVERYCHARGES + '\'' +
                        ",tAX_RATE = '" + tAXRATE + '\'' +
                        ",pROMO_CODE = '" + pROMOCODE + '\'' +
                        ",tOTAL_TXN_AMOUNT_WO_VAT = '" + tOTALTXNAMOUNTWOVAT + '\'' +
                        ",fEE_ENQ_TXN_TYPE = '" + fEEENQTXNTYPE + '\'' +
                        ",iS_TEST_QUESTION_AVAILABLE = '" + iSTESTQUESTIONAVAILABLE + '\'' +
                        ",rEFERENCE_NO = '" + rEFERENCENO + '\'' +
                        ",vAT_CHARGES = '" + vATCHARGES + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pROMOREQMSG);
        dest.writeString(rATE);
        dest.writeString(cHARGES);
        dest.writeInt(sTATUSCODE);
        dest.writeString(dESTPRINCIPALAMOUNT);
        dest.writeString(sTATUSMSG);
        dest.writeString(mESSAGE);
        dest.writeString(pROMONAME);
        dest.writeString(oRIGINALPRINCIPLEAMOUNT);
        dest.writeString(pROMODISCOUNTAMOUNT);
        dest.writeString(tAXWORKSHEET);
        dest.writeString(gROSSTOTALAMOUNT);
        dest.writeString(dELIVERYCHARGES);
        dest.writeString(tAXRATE);
        dest.writeString(pROMOCODE);
        dest.writeString(tOTALTXNAMOUNTWOVAT);
        dest.writeString(fEEENQTXNTYPE);
        dest.writeString(iSTESTQUESTIONAVAILABLE);
        dest.writeString(rEFERENCENO);
        dest.writeString(vATCHARGES);
    }
}