package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WuBeneficiaryCountryDetails implements Parcelable{

    @SerializedName("NATIONALITY")
    private String nATIONALITY;

    @SerializedName("COUNTRY_ID")
    private String cOUNTRYID;

    @SerializedName("service_BT_DESC")
    private String serviceBTDESC;

    @SerializedName("WU_COUNTRY_CODE")
    private String wUCOUNTRYCODE;

    @SerializedName("BACKGROUND")
    private String bACKGROUND;

    @SerializedName("FLAG")
    private String fLAG;

    @SerializedName("CE_COUNTRY_CODE")
    private String cECOUNTRYCODE;

    @SerializedName("remit_BT_INSTANT")
    private String remitBTINSTANT;

    @SerializedName("STATUS")
    private String sTATUS;

    @SerializedName("remit_CP_VALUE")
    private String remitCPVALUE;

    @SerializedName("AREX_COUNTRY_CODE")
    private String aREXCOUNTRYCODE;

    @SerializedName("service_CP_DESC")
    private String serviceCPDESC;

    @SerializedName("LATIN_NAME")
    private String lATINNAME;

    @SerializedName("remit_BT_VALUE")
    private String remitBTVALUE;

    @SerializedName("remit_CP_INSTANT")
    private String remitCPINSTANT;

    @SerializedName("ARABIC_NAME")
    private String aRABICNAME;

    protected WuBeneficiaryCountryDetails(Parcel in) {
        nATIONALITY = in.readString();
        cOUNTRYID = in.readString();
        serviceBTDESC = in.readString();
        wUCOUNTRYCODE = in.readString();
        bACKGROUND = in.readString();
        fLAG = in.readString();
        cECOUNTRYCODE = in.readString();
        remitBTINSTANT = in.readString();
        sTATUS = in.readString();
        remitCPVALUE = in.readString();
        aREXCOUNTRYCODE = in.readString();
        serviceCPDESC = in.readString();
        lATINNAME = in.readString();
        remitBTVALUE = in.readString();
        remitCPINSTANT = in.readString();
        aRABICNAME = in.readString();
    }

    public static final Creator<WuBeneficiaryCountryDetails> CREATOR = new Creator<WuBeneficiaryCountryDetails>() {
        @Override
        public WuBeneficiaryCountryDetails createFromParcel(Parcel in) {
            return new WuBeneficiaryCountryDetails(in);
        }

        @Override
        public WuBeneficiaryCountryDetails[] newArray(int size) {
            return new WuBeneficiaryCountryDetails[size];
        }
    };

    public void setNATIONALITY(String nATIONALITY) {
        this.nATIONALITY = nATIONALITY;
    }

    public String getNATIONALITY() {
        return nATIONALITY;
    }

    public void setCOUNTRYID(String cOUNTRYID) {
        this.cOUNTRYID = cOUNTRYID;
    }

    public String getCOUNTRYID() {
        return cOUNTRYID;
    }

    public void setServiceBTDESC(String serviceBTDESC) {
        this.serviceBTDESC = serviceBTDESC;
    }

    public String getServiceBTDESC() {
        return serviceBTDESC;
    }

    public void setWUCOUNTRYCODE(String wUCOUNTRYCODE) {
        this.wUCOUNTRYCODE = wUCOUNTRYCODE;
    }

    public String getWUCOUNTRYCODE() {
        return wUCOUNTRYCODE;
    }

    public void setBACKGROUND(String bACKGROUND) {
        this.bACKGROUND = bACKGROUND;
    }

    public String getBACKGROUND() {
        return bACKGROUND;
    }

    public void setFLAG(String fLAG) {
        this.fLAG = fLAG;
    }

    public String getFLAG() {
        return fLAG;
    }

    public void setCECOUNTRYCODE(String cECOUNTRYCODE) {
        this.cECOUNTRYCODE = cECOUNTRYCODE;
    }

    public String getCECOUNTRYCODE() {
        return cECOUNTRYCODE;
    }

    public void setRemitBTINSTANT(String remitBTINSTANT) {
        this.remitBTINSTANT = remitBTINSTANT;
    }

    public String getRemitBTINSTANT() {
        return remitBTINSTANT;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setRemitCPVALUE(String remitCPVALUE) {
        this.remitCPVALUE = remitCPVALUE;
    }

    public String getRemitCPVALUE() {
        return remitCPVALUE;
    }

    public void setAREXCOUNTRYCODE(String aREXCOUNTRYCODE) {
        this.aREXCOUNTRYCODE = aREXCOUNTRYCODE;
    }

    public String getAREXCOUNTRYCODE() {
        return aREXCOUNTRYCODE;
    }

    public void setServiceCPDESC(String serviceCPDESC) {
        this.serviceCPDESC = serviceCPDESC;
    }

    public String getServiceCPDESC() {
        return serviceCPDESC;
    }

    public void setLATINNAME(String lATINNAME) {
        this.lATINNAME = lATINNAME;
    }

    public String getLATINNAME() {
        return lATINNAME;
    }

    public void setRemitBTVALUE(String remitBTVALUE) {
        this.remitBTVALUE = remitBTVALUE;
    }

    public String getRemitBTVALUE() {
        return remitBTVALUE;
    }

    public void setRemitCPINSTANT(String remitCPINSTANT) {
        this.remitCPINSTANT = remitCPINSTANT;
    }

    public String getRemitCPINSTANT() {
        return remitCPINSTANT;
    }

    public void setARABICNAME(String aRABICNAME) {
        this.aRABICNAME = aRABICNAME;
    }

    public String getARABICNAME() {
        return aRABICNAME;
    }

    @Override
    public String toString() {
        return
                "WuBeneficiaryCountryDetails{" +
                        "nATIONALITY = '" + nATIONALITY + '\'' +
                        ",cOUNTRY_ID = '" + cOUNTRYID + '\'' +
                        ",service_BT_DESC = '" + serviceBTDESC + '\'' +
                        ",wU_COUNTRY_CODE = '" + wUCOUNTRYCODE + '\'' +
                        ",bACKGROUND = '" + bACKGROUND + '\'' +
                        ",fLAG = '" + fLAG + '\'' +
                        ",cE_COUNTRY_CODE = '" + cECOUNTRYCODE + '\'' +
                        ",remit_BT_INSTANT = '" + remitBTINSTANT + '\'' +
                        ",sTATUS = '" + sTATUS + '\'' +
                        ",remit_CP_VALUE = '" + remitCPVALUE + '\'' +
                        ",aREX_COUNTRY_CODE = '" + aREXCOUNTRYCODE + '\'' +
                        ",service_CP_DESC = '" + serviceCPDESC + '\'' +
                        ",lATIN_NAME = '" + lATINNAME + '\'' +
                        ",remit_BT_VALUE = '" + remitBTVALUE + '\'' +
                        ",remit_CP_INSTANT = '" + remitCPINSTANT + '\'' +
                        ",aRABIC_NAME = '" + aRABICNAME + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nATIONALITY);
        dest.writeString(cOUNTRYID);
        dest.writeString(serviceBTDESC);
        dest.writeString(wUCOUNTRYCODE);
        dest.writeString(bACKGROUND);
        dest.writeString(fLAG);
        dest.writeString(cECOUNTRYCODE);
        dest.writeString(remitBTINSTANT);
        dest.writeString(sTATUS);
        dest.writeString(remitCPVALUE);
        dest.writeString(aREXCOUNTRYCODE);
        dest.writeString(serviceCPDESC);
        dest.writeString(lATINNAME);
        dest.writeString(remitBTVALUE);
        dest.writeString(remitCPINSTANT);
        dest.writeString(aRABICNAME);
    }
}