package app.alansari.models.travalcardvalidateflag;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TravelCardFlag implements Parcelable {
    @SerializedName("AED_VALUE")
    private Object aEDVALUE;

    @SerializedName("VIEW_LOW_RATE")
    private Object vIEWLOWRATE;

    @SerializedName("BUY_HIGH_D")
    private Object bUYHIGHD;

    @SerializedName("VIEW_HIGH_RATE")
    private Object vIEWHIGHRATE;

    @SerializedName("RATE")
    private Object rATE;

    @SerializedName("ISO_CCY_CODE")
    private String iSOCCYCODE;

    @SerializedName("WALLET_CREATION_DATE")
    private Object wALLETCREATIONDATE;

    @SerializedName("MD_FACTOR")
    private Object mDFACTOR;

    @SerializedName("TXN_TYPE")
    private Object tXNTYPE;

    @SerializedName("FLAG")
    private String fLAG;

    @SerializedName("WC_CURRENCY_FLAH")
    private Object wCCURRENCYFLAH;

    @SerializedName("MY_WALLET_ID")
    private Object mYWALLETID;

    @SerializedName("CCY_CODE")
    private String cCYCODE;

    @SerializedName("CURRENT_BALANCE")
    private Object cURRENTBALANCE;

    @SerializedName("rowCount")
    private Object rowCount;

    @SerializedName("SELL_LOW")
    private Object sELLLOW;

    @SerializedName("DISPLAY_AMOUNT")
    private Object dISPLAYAMOUNT;

    @SerializedName("DISPLAY_VALUE_AED")
    private Object dISPLAYVALUEAED;

    @SerializedName("REC_AMOUNT")
    private Object rECAMOUNT;

    @SerializedName("FCY_VALUE")
    private Object fCYVALUE;

    @SerializedName("TXN_MED_CCY")
    private Object tXNMEDCCY;

    @SerializedName("BALANCE")
    private String bALANCE;

    @SerializedName("VIEW_DEFAULT_RATE")
    private Object vIEWDEFAULTRATE;

    @SerializedName("COST_PRICE")
    private Object cOSTPRICE;

    @SerializedName("DISPLAY_RATE")
    private Object dISPLAYRATE;

    @SerializedName("CCY_DESC")
    private String cCYDESC;

    public TravelCardFlag() {
    }

    public TravelCardFlag(Parcel in) {
        iSOCCYCODE = in.readString();
        fLAG = in.readString();
        cCYCODE = in.readString();
        cCYDESC = in.readString();
        bALANCE=in.readString();
    }

    public static final Creator<TravelCardFlag> CREATOR = new Creator<TravelCardFlag>() {
        @Override
        public TravelCardFlag createFromParcel(Parcel in) {
            return new TravelCardFlag(in);
        }

        @Override
        public TravelCardFlag[] newArray(int size) {
            return new TravelCardFlag[size];
        }
    };

    public Object getAEDVALUE() {
        return aEDVALUE;
    }

    public void setAEDVALUE(Object aEDVALUE) {
        this.aEDVALUE = aEDVALUE;
    }

    public Object getVIEWLOWRATE() {
        return vIEWLOWRATE;
    }

    public void setVIEWLOWRATE(Object vIEWLOWRATE) {
        this.vIEWLOWRATE = vIEWLOWRATE;
    }

    public Object getBUYHIGHD() {
        return bUYHIGHD;
    }

    public void setBUYHIGHD(Object bUYHIGHD) {
        this.bUYHIGHD = bUYHIGHD;
    }

    public Object getVIEWHIGHRATE() {
        return vIEWHIGHRATE;
    }

    public void setVIEWHIGHRATE(Object vIEWHIGHRATE) {
        this.vIEWHIGHRATE = vIEWHIGHRATE;
    }

    public Object getRATE() {
        return rATE;
    }

    public void setRATE(Object rATE) {
        this.rATE = rATE;
    }

    public String getISOCCYCODE() {
        return iSOCCYCODE;
    }

    public void setISOCCYCODE(String iSOCCYCODE) {
        this.iSOCCYCODE = iSOCCYCODE;
    }

    public Object getWALLETCREATIONDATE() {
        return wALLETCREATIONDATE;
    }

    public void setWALLETCREATIONDATE(Object wALLETCREATIONDATE) {
        this.wALLETCREATIONDATE = wALLETCREATIONDATE;
    }

    public Object getMDFACTOR() {
        return mDFACTOR;
    }

    public void setMDFACTOR(Object mDFACTOR) {
        this.mDFACTOR = mDFACTOR;
    }

    public Object getTXNTYPE() {
        return tXNTYPE;
    }

    public void setTXNTYPE(Object tXNTYPE) {
        this.tXNTYPE = tXNTYPE;
    }

    public String getFLAG() {
        return fLAG;
    }

    public void setFLAG(String fLAG) {
        this.fLAG = fLAG;
    }

    public Object getWCCURRENCYFLAH() {
        return wCCURRENCYFLAH;
    }

    public void setWCCURRENCYFLAH(Object wCCURRENCYFLAH) {
        this.wCCURRENCYFLAH = wCCURRENCYFLAH;
    }

    public Object getMYWALLETID() {
        return mYWALLETID;
    }

    public void setMYWALLETID(Object mYWALLETID) {
        this.mYWALLETID = mYWALLETID;
    }

    public String getCCYCODE() {
        return cCYCODE;
    }

    public void setCCYCODE(String cCYCODE) {
        this.cCYCODE = cCYCODE;
    }

    public Object getCURRENTBALANCE() {
        return cURRENTBALANCE;
    }

    public void setCURRENTBALANCE(Object cURRENTBALANCE) {
        this.cURRENTBALANCE = cURRENTBALANCE;
    }

    public Object getRowCount() {
        return rowCount;
    }

    public void setRowCount(Object rowCount) {
        this.rowCount = rowCount;
    }

    public Object getSELLLOW() {
        return sELLLOW;
    }

    public void setSELLLOW(Object sELLLOW) {
        this.sELLLOW = sELLLOW;
    }

    public Object getDISPLAYAMOUNT() {
        return dISPLAYAMOUNT;
    }

    public void setDISPLAYAMOUNT(Object dISPLAYAMOUNT) {
        this.dISPLAYAMOUNT = dISPLAYAMOUNT;
    }

    public Object getDISPLAYVALUEAED() {
        return dISPLAYVALUEAED;
    }

    public void setDISPLAYVALUEAED(Object dISPLAYVALUEAED) {
        this.dISPLAYVALUEAED = dISPLAYVALUEAED;
    }

    public Object getRECAMOUNT() {
        return rECAMOUNT;
    }

    public void setRECAMOUNT(Object rECAMOUNT) {
        this.rECAMOUNT = rECAMOUNT;
    }

    public Object getFCYVALUE() {
        return fCYVALUE;
    }

    public void setFCYVALUE(Object fCYVALUE) {
        this.fCYVALUE = fCYVALUE;
    }

    public Object getTXNMEDCCY() {
        return tXNMEDCCY;
    }

    public void setTXNMEDCCY(Object tXNMEDCCY) {
        this.tXNMEDCCY = tXNMEDCCY;
    }

    public String getBALANCE() {
        return bALANCE;
    }

    public void setBALANCE(String bALANCE) {
        this.bALANCE = bALANCE;
    }

    public Object getVIEWDEFAULTRATE() {
        return vIEWDEFAULTRATE;
    }

    public void setVIEWDEFAULTRATE(Object vIEWDEFAULTRATE) {
        this.vIEWDEFAULTRATE = vIEWDEFAULTRATE;
    }

    public Object getCOSTPRICE() {
        return cOSTPRICE;
    }

    public void setCOSTPRICE(Object cOSTPRICE) {
        this.cOSTPRICE = cOSTPRICE;
    }

    public Object getDISPLAYRATE() {
        return dISPLAYRATE;
    }

    public void setDISPLAYRATE(Object dISPLAYRATE) {
        this.dISPLAYRATE = dISPLAYRATE;
    }

    public String getCCYDESC() {
        return cCYDESC;
    }

    public void setCCYDESC(String cCYDESC) {
        this.cCYDESC = cCYDESC;
    }

    @Override
    public String toString() {
        return
                "RESULTItem{" +
                        "aED_VALUE = '" + aEDVALUE + '\'' +
                        ",vIEW_LOW_RATE = '" + vIEWLOWRATE + '\'' +
                        ",bUY_HIGH_D = '" + bUYHIGHD + '\'' +
                        ",vIEW_HIGH_RATE = '" + vIEWHIGHRATE + '\'' +
                        ",rATE = '" + rATE + '\'' +
                        ",iSO_CCY_CODE = '" + iSOCCYCODE + '\'' +
                        ",wALLET_CREATION_DATE = '" + wALLETCREATIONDATE + '\'' +
                        ",mD_FACTOR = '" + mDFACTOR + '\'' +
                        ",tXN_TYPE = '" + tXNTYPE + '\'' +
                        ",fLAG = '" + fLAG + '\'' +
                        ",wC_CURRENCY_FLAH = '" + wCCURRENCYFLAH + '\'' +
                        ",mY_WALLET_ID = '" + mYWALLETID + '\'' +
                        ",cCY_CODE = '" + cCYCODE + '\'' +
                        ",cURRENT_BALANCE = '" + cURRENTBALANCE + '\'' +
                        ",rowCount = '" + rowCount + '\'' +
                        ",sELL_LOW = '" + sELLLOW + '\'' +
                        ",dISPLAY_AMOUNT = '" + dISPLAYAMOUNT + '\'' +
                        ",dISPLAY_VALUE_AED = '" + dISPLAYVALUEAED + '\'' +
                        ",rEC_AMOUNT = '" + rECAMOUNT + '\'' +
                        ",fCY_VALUE = '" + fCYVALUE + '\'' +
                        ",tXN_MED_CCY = '" + tXNMEDCCY + '\'' +
                        ",bALANCE = '" + bALANCE + '\'' +
                        ",vIEW_DEFAULT_RATE = '" + vIEWDEFAULTRATE + '\'' +
                        ",cOST_PRICE = '" + cOSTPRICE + '\'' +
                        ",dISPLAY_RATE = '" + dISPLAYRATE + '\'' +
                        ",cCY_DESC = '" + cCYDESC + '\'' +
                        "}";
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
        dest.writeString(iSOCCYCODE);
        dest.writeString(fLAG);
        dest.writeString(cCYCODE);
        dest.writeString(cCYDESC);
        dest.writeString(bALANCE);
    }
}
