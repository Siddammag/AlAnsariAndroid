package app.alansari.models.getCharges;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ResultItem implements Parcelable{

	@SerializedName("USER_PK_ID")
	private Object uSERPKID;

	@SerializedName("AED_AMOUNT")
	private String aEDAMOUNT;

	@SerializedName("WC_ACCOUNT_NUMBER")
	private String wCACCOUNTNUMBER;

	@SerializedName("RATE")
	private String rATE;

	@SerializedName("FCY_AMOUNT")
	private String fCYAMOUNT;

	@SerializedName("TO_CCY")
	private String tOCCY;

	@SerializedName("CCY_CODE")
	private String cCYCODE;

	@SerializedName("reloadSlabCharge")
	private String reloadSlabCharge;

	@SerializedName("FROM_CCY")
	private String fROMCCY;

	protected ResultItem(Parcel in) {
		aEDAMOUNT = in.readString();
		wCACCOUNTNUMBER = in.readString();
		rATE = in.readString();
		fCYAMOUNT = in.readString();
		tOCCY = in.readString();
		cCYCODE = in.readString();
		reloadSlabCharge = in.readString();
		fROMCCY = in.readString();
	}

	public static final Creator<ResultItem> CREATOR = new Creator<ResultItem>() {
		@Override
		public ResultItem createFromParcel(Parcel in) {
			return new ResultItem(in);
		}

		@Override
		public ResultItem[] newArray(int size) {
			return new ResultItem[size];
		}
	};

	public void setUSERPKID(Object uSERPKID){
		this.uSERPKID = uSERPKID;
	}

	public Object getUSERPKID(){
		return uSERPKID;
	}

	public void setAEDAMOUNT(String aEDAMOUNT){
		this.aEDAMOUNT = aEDAMOUNT;
	}

	public String getAEDAMOUNT(){
		return aEDAMOUNT;
	}

	public void setWCACCOUNTNUMBER(String wCACCOUNTNUMBER){
		this.wCACCOUNTNUMBER = wCACCOUNTNUMBER;
	}

	public String getWCACCOUNTNUMBER(){
		return wCACCOUNTNUMBER;
	}

	public void setRATE(String rATE){
		this.rATE = rATE;
	}

	public String getRATE(){
		return rATE;
	}

	public void setFCYAMOUNT(String fCYAMOUNT){
		this.fCYAMOUNT = fCYAMOUNT;
	}

	public String getFCYAMOUNT(){
		return fCYAMOUNT;
	}

	public void setTOCCY(String tOCCY){
		this.tOCCY = tOCCY;
	}

	public String getTOCCY(){
		return tOCCY;
	}

	public void setCCYCODE(String cCYCODE){
		this.cCYCODE = cCYCODE;
	}

	public String getCCYCODE(){
		return cCYCODE;
	}

	public void setReloadSlabCharge(String reloadSlabCharge){
		this.reloadSlabCharge = reloadSlabCharge;
	}

	public String getReloadSlabCharge(){
		return reloadSlabCharge;
	}

	public void setFROMCCY(String fROMCCY){
		this.fROMCCY = fROMCCY;
	}

	public String getFROMCCY(){
		return fROMCCY;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"uSER_PK_ID = '" + uSERPKID + '\'' + 
			",aED_AMOUNT = '" + aEDAMOUNT + '\'' + 
			",wC_ACCOUNT_NUMBER = '" + wCACCOUNTNUMBER + '\'' + 
			",rATE = '" + rATE + '\'' + 
			",fCY_AMOUNT = '" + fCYAMOUNT + '\'' + 
			",tO_CCY = '" + tOCCY + '\'' + 
			",cCY_CODE = '" + cCYCODE + '\'' + 
			",reloadSlabCharge = '" + reloadSlabCharge + '\'' + 
			",fROM_CCY = '" + fROMCCY + '\'' + 
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
		dest.writeString(aEDAMOUNT);
		dest.writeString(wCACCOUNTNUMBER);
		dest.writeString(rATE);
		dest.writeString(fCYAMOUNT);
		dest.writeString(tOCCY);
		dest.writeString(cCYCODE);
		dest.writeString(reloadSlabCharge);
		dest.writeString(fROMCCY);
	}
}