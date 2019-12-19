package app.alansari.models.travelcardreloadcurrency;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TravelCardCurrencyModel implements Parcelable{

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

	protected TravelCardCurrencyModel(Parcel in) {
		iSOCCYCODE = in.readString();
		fLAG = in.readString();
		cCYCODE = in.readString();
		bALANCE = in.readString();
		cCYDESC = in.readString();
	}

	public static final Creator<TravelCardCurrencyModel> CREATOR = new Creator<TravelCardCurrencyModel>() {
		@Override
		public TravelCardCurrencyModel createFromParcel(Parcel in) {
			return new TravelCardCurrencyModel(in);
		}

		@Override
		public TravelCardCurrencyModel[] newArray(int size) {
			return new TravelCardCurrencyModel[size];
		}
	};

	public void setAEDVALUE(Object aEDVALUE){
		this.aEDVALUE = aEDVALUE;
	}

	public Object getAEDVALUE(){
		return aEDVALUE;
	}

	public void setVIEWLOWRATE(Object vIEWLOWRATE){
		this.vIEWLOWRATE = vIEWLOWRATE;
	}

	public Object getVIEWLOWRATE(){
		return vIEWLOWRATE;
	}

	public void setBUYHIGHD(Object bUYHIGHD){
		this.bUYHIGHD = bUYHIGHD;
	}

	public Object getBUYHIGHD(){
		return bUYHIGHD;
	}

	public void setVIEWHIGHRATE(Object vIEWHIGHRATE){
		this.vIEWHIGHRATE = vIEWHIGHRATE;
	}

	public Object getVIEWHIGHRATE(){
		return vIEWHIGHRATE;
	}

	public void setRATE(Object rATE){
		this.rATE = rATE;
	}

	public Object getRATE(){
		return rATE;
	}

	public void setISOCCYCODE(String iSOCCYCODE){
		this.iSOCCYCODE = iSOCCYCODE;
	}

	public String getISOCCYCODE(){
		return iSOCCYCODE;
	}

	public void setWALLETCREATIONDATE(Object wALLETCREATIONDATE){
		this.wALLETCREATIONDATE = wALLETCREATIONDATE;
	}

	public Object getWALLETCREATIONDATE(){
		return wALLETCREATIONDATE;
	}

	public void setMDFACTOR(Object mDFACTOR){
		this.mDFACTOR = mDFACTOR;
	}

	public Object getMDFACTOR(){
		return mDFACTOR;
	}

	public void setTXNTYPE(Object tXNTYPE){
		this.tXNTYPE = tXNTYPE;
	}

	public Object getTXNTYPE(){
		return tXNTYPE;
	}

	public void setFLAG(String fLAG){
		this.fLAG = fLAG;
	}

	public String getFLAG(){
		return fLAG;
	}

	public void setWCCURRENCYFLAH(Object wCCURRENCYFLAH){
		this.wCCURRENCYFLAH = wCCURRENCYFLAH;
	}

	public Object getWCCURRENCYFLAH(){
		return wCCURRENCYFLAH;
	}

	public void setMYWALLETID(Object mYWALLETID){
		this.mYWALLETID = mYWALLETID;
	}

	public Object getMYWALLETID(){
		return mYWALLETID;
	}

	public void setCCYCODE(String cCYCODE){
		this.cCYCODE = cCYCODE;
	}

	public String getCCYCODE(){
		return cCYCODE;
	}

	public void setCURRENTBALANCE(Object cURRENTBALANCE){
		this.cURRENTBALANCE = cURRENTBALANCE;
	}

	public Object getCURRENTBALANCE(){
		return cURRENTBALANCE;
	}

	public void setRowCount(Object rowCount){
		this.rowCount = rowCount;
	}

	public Object getRowCount(){
		return rowCount;
	}

	public void setSELLLOW(Object sELLLOW){
		this.sELLLOW = sELLLOW;
	}

	public Object getSELLLOW(){
		return sELLLOW;
	}

	public void setDISPLAYAMOUNT(Object dISPLAYAMOUNT){
		this.dISPLAYAMOUNT = dISPLAYAMOUNT;
	}

	public Object getDISPLAYAMOUNT(){
		return dISPLAYAMOUNT;
	}

	public void setDISPLAYVALUEAED(Object dISPLAYVALUEAED){
		this.dISPLAYVALUEAED = dISPLAYVALUEAED;
	}

	public Object getDISPLAYVALUEAED(){
		return dISPLAYVALUEAED;
	}

	public void setRECAMOUNT(Object rECAMOUNT){
		this.rECAMOUNT = rECAMOUNT;
	}

	public Object getRECAMOUNT(){
		return rECAMOUNT;
	}

	public void setFCYVALUE(Object fCYVALUE){
		this.fCYVALUE = fCYVALUE;
	}

	public Object getFCYVALUE(){
		return fCYVALUE;
	}

	public void setTXNMEDCCY(Object tXNMEDCCY){
		this.tXNMEDCCY = tXNMEDCCY;
	}

	public Object getTXNMEDCCY(){
		return tXNMEDCCY;
	}

	public void setBALANCE(String bALANCE){
		this.bALANCE = bALANCE;
	}

	public String getBALANCE(){
		return bALANCE;
	}

	public void setVIEWDEFAULTRATE(Object vIEWDEFAULTRATE){
		this.vIEWDEFAULTRATE = vIEWDEFAULTRATE;
	}

	public Object getVIEWDEFAULTRATE(){
		return vIEWDEFAULTRATE;
	}

	public void setCOSTPRICE(Object cOSTPRICE){
		this.cOSTPRICE = cOSTPRICE;
	}

	public Object getCOSTPRICE(){
		return cOSTPRICE;
	}

	public void setDISPLAYRATE(Object dISPLAYRATE){
		this.dISPLAYRATE = dISPLAYRATE;
	}

	public Object getDISPLAYRATE(){
		return dISPLAYRATE;
	}

	public void setCCYDESC(String cCYDESC){
		this.cCYDESC = cCYDESC;
	}

	public String getCCYDESC(){
		return cCYDESC;
	}

	@Override
 	public String toString(){
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
		dest.writeString(bALANCE);
		dest.writeString(cCYDESC);
	}
}