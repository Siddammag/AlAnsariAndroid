package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FetchEmailData implements Parcelable {

    @SerializedName("USER_PK_ID")
    private String uSERPKID;

    @SerializedName("EXC_USER_NAME")
    private Object eXCUSERNAME;

    @SerializedName("INVOICE_EMAIL")
    private String iNVOICEEMAIL;

    @SerializedName("AUTHORIZE_BRANCH")
    private String aUTHORIZEBRANCH;

    @SerializedName("AUTHORIZE_DATE")
    private long aUTHORIZEDATE;

    @SerializedName("REGISTRATION_MODE")
    private String rEGISTRATIONMODE;

    @SerializedName("AUTHORIZE_BY")
    private String aUTHORIZEBY;

    @SerializedName("CE_MEM_FK_ID")
    private String cEMEMFKID;

    @SerializedName("STATUS")
    private String sTATUS;

    @SerializedName("REF_NUMBER")
    private String rEFNUMBER;

    @SerializedName("QUICK_SEND")
    private Object qUICKSEND;

    @SerializedName("AML_STATUS")
    private String aMLSTATUS;

    @SerializedName("EMAIL_ALERT_IND")
    private String eMAILALERTIND;

    @SerializedName("USER_TYPE")
    private String uSERTYPE;

    @SerializedName("AREX_MEM_FK_ID")
    private String aREXMEMFKID;

    @SerializedName("MOBILE_NUM")
    private String mOBILENUM;

    @SerializedName("TOTAL_PENDING_TRANSACTIONS")
    private Object tOTALPENDINGTRANSACTIONS;

    protected FetchEmailData(Parcel in) {
        uSERPKID = in.readString();
        iNVOICEEMAIL = in.readString();
        aUTHORIZEBRANCH = in.readString();
        aUTHORIZEDATE = in.readLong();
        rEGISTRATIONMODE = in.readString();
        aUTHORIZEBY = in.readString();
        cEMEMFKID = in.readString();
        sTATUS = in.readString();
        rEFNUMBER = in.readString();
        aMLSTATUS = in.readString();
        eMAILALERTIND = in.readString();
        uSERTYPE = in.readString();
        aREXMEMFKID = in.readString();
        mOBILENUM = in.readString();
    }

    public static final Creator<FetchEmailData> CREATOR = new Creator<FetchEmailData>() {
        @Override
        public FetchEmailData createFromParcel(Parcel in) {
            return new FetchEmailData(in);
        }

        @Override
        public FetchEmailData[] newArray(int size) {
            return new FetchEmailData[size];
        }
    };

    public void setUSERPKID(String uSERPKID) {
        this.uSERPKID = uSERPKID;
    }

    public String getUSERPKID() {
        return uSERPKID;
    }

    public void setEXCUSERNAME(Object eXCUSERNAME) {
        this.eXCUSERNAME = eXCUSERNAME;
    }

    public Object getEXCUSERNAME() {
        return eXCUSERNAME;
    }

    public void setINVOICEEMAIL(String iNVOICEEMAIL) {
        this.iNVOICEEMAIL = iNVOICEEMAIL;
    }

    public String getINVOICEEMAIL() {
        return iNVOICEEMAIL;
    }

    public void setAUTHORIZEBRANCH(String aUTHORIZEBRANCH) {
        this.aUTHORIZEBRANCH = aUTHORIZEBRANCH;
    }

    public String getAUTHORIZEBRANCH() {
        return aUTHORIZEBRANCH;
    }

    public void setAUTHORIZEDATE(long aUTHORIZEDATE) {
        this.aUTHORIZEDATE = aUTHORIZEDATE;
    }

    public long getAUTHORIZEDATE() {
        return aUTHORIZEDATE;
    }

    public void setREGISTRATIONMODE(String rEGISTRATIONMODE) {
        this.rEGISTRATIONMODE = rEGISTRATIONMODE;
    }

    public String getREGISTRATIONMODE() {
        return rEGISTRATIONMODE;
    }

    public void setAUTHORIZEBY(String aUTHORIZEBY) {
        this.aUTHORIZEBY = aUTHORIZEBY;
    }

    public String getAUTHORIZEBY() {
        return aUTHORIZEBY;
    }

    public void setCEMEMFKID(String cEMEMFKID) {
        this.cEMEMFKID = cEMEMFKID;
    }

    public String getCEMEMFKID() {
        return cEMEMFKID;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setREFNUMBER(String rEFNUMBER) {
        this.rEFNUMBER = rEFNUMBER;
    }

    public String getREFNUMBER() {
        return rEFNUMBER;
    }

    public void setQUICKSEND(Object qUICKSEND) {
        this.qUICKSEND = qUICKSEND;
    }

    public Object getQUICKSEND() {
        return qUICKSEND;
    }

    public void setAMLSTATUS(String aMLSTATUS) {
        this.aMLSTATUS = aMLSTATUS;
    }

    public String getAMLSTATUS() {
        return aMLSTATUS;
    }

    public void setEMAILALERTIND(String eMAILALERTIND) {
        this.eMAILALERTIND = eMAILALERTIND;
    }

    public String getEMAILALERTIND() {
        return eMAILALERTIND;
    }

    public void setUSERTYPE(String uSERTYPE) {
        this.uSERTYPE = uSERTYPE;
    }

    public String getUSERTYPE() {
        return uSERTYPE;
    }

    public void setAREXMEMFKID(String aREXMEMFKID) {
        this.aREXMEMFKID = aREXMEMFKID;
    }

    public String getAREXMEMFKID() {
        return aREXMEMFKID;
    }

    public void setMOBILENUM(String mOBILENUM) {
        this.mOBILENUM = mOBILENUM;
    }

    public String getMOBILENUM() {
        return mOBILENUM;
    }

    public void setTOTALPENDINGTRANSACTIONS(Object tOTALPENDINGTRANSACTIONS) {
        this.tOTALPENDINGTRANSACTIONS = tOTALPENDINGTRANSACTIONS;
    }

    public Object getTOTALPENDINGTRANSACTIONS() {
        return tOTALPENDINGTRANSACTIONS;
    }

    @Override
    public String toString() {
        return
                "FetchEmailData{" +
                        "uSER_PK_ID = '" + uSERPKID + '\'' +
                        ",eXC_USER_NAME = '" + eXCUSERNAME + '\'' +
                        ",iNVOICE_EMAIL = '" + iNVOICEEMAIL + '\'' +
                        ",aUTHORIZE_BRANCH = '" + aUTHORIZEBRANCH + '\'' +
                        ",aUTHORIZE_DATE = '" + aUTHORIZEDATE + '\'' +
                        ",rEGISTRATION_MODE = '" + rEGISTRATIONMODE + '\'' +
                        ",aUTHORIZE_BY = '" + aUTHORIZEBY + '\'' +
                        ",cE_MEM_FK_ID = '" + cEMEMFKID + '\'' +
                        ",sTATUS = '" + sTATUS + '\'' +
                        ",rEF_NUMBER = '" + rEFNUMBER + '\'' +
                        ",qUICK_SEND = '" + qUICKSEND + '\'' +
                        ",aML_STATUS = '" + aMLSTATUS + '\'' +
                        ",eMAIL_ALERT_IND = '" + eMAILALERTIND + '\'' +
                        ",uSER_TYPE = '" + uSERTYPE + '\'' +
                        ",aREX_MEM_FK_ID = '" + aREXMEMFKID + '\'' +
                        ",mOBILE_NUM = '" + mOBILENUM + '\'' +
                        ",tOTAL_PENDING_TRANSACTIONS = '" + tOTALPENDINGTRANSACTIONS + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uSERPKID);
        dest.writeString(iNVOICEEMAIL);
        dest.writeString(aUTHORIZEBRANCH);
        dest.writeLong(aUTHORIZEDATE);
        dest.writeString(rEGISTRATIONMODE);
        dest.writeString(aUTHORIZEBY);
        dest.writeString(cEMEMFKID);
        dest.writeString(sTATUS);
        dest.writeString(rEFNUMBER);
        dest.writeString(aMLSTATUS);
        dest.writeString(eMAILALERTIND);
        dest.writeString(uSERTYPE);
        dest.writeString(aREXMEMFKID);
        dest.writeString(mOBILENUM);
    }
}