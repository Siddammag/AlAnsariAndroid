package app.alansari.models.transactionhistroy;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WALLETDETAILSItem implements Parcelable{

	@SerializedName("TOTAL_VALUE_AED")
	private double tOTALVALUEAED;

	@SerializedName("AMOUNT")
	private double aMOUNT;

	@SerializedName("RATE")
	private double rATE;

	@SerializedName("CCY_CODE")
	private String cCYCODE;

	@SerializedName("FLAG")
	private String fLAG;

	@SerializedName("CCY_DESC")
	private String cCYDESC;

	protected WALLETDETAILSItem(Parcel in) {
		tOTALVALUEAED = in.readDouble();
		aMOUNT = in.readDouble();
		rATE = in.readDouble();
		cCYCODE = in.readString();
		fLAG = in.readString();
		cCYDESC = in.readString();
	}

	public static final Creator<WALLETDETAILSItem> CREATOR = new Creator<WALLETDETAILSItem>() {
		@Override
		public WALLETDETAILSItem createFromParcel(Parcel in) {
			return new WALLETDETAILSItem(in);
		}

		@Override
		public WALLETDETAILSItem[] newArray(int size) {
			return new WALLETDETAILSItem[size];
		}
	};

	public void setTOTALVALUEAED(double tOTALVALUEAED){
		this.tOTALVALUEAED = tOTALVALUEAED;
	}

	public double getTOTALVALUEAED(){
		return tOTALVALUEAED;
	}

	public double getaMOUNT() {
		return aMOUNT;
	}

	public void setaMOUNT(double aMOUNT) {
		this.aMOUNT = aMOUNT;
	}

	public void setRATE(double rATE){
		this.rATE = rATE;
	}

	public double getRATE(){
		return rATE;
	}

	public void setCCYCODE(String cCYCODE){
		this.cCYCODE = cCYCODE;
	}

	public String getCCYCODE(){
		return cCYCODE;
	}

	public void setFLAG(String fLAG){
		this.fLAG = fLAG;
	}

	public String getFLAG(){
		return fLAG;
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
			"WALLETDETAILSItem{" + 
			"tOTAL_VALUE_AED = '" + tOTALVALUEAED + '\'' + 
			",aMOUNT = '" + aMOUNT + '\'' + 
			",rATE = '" + rATE + '\'' + 
			",cCY_CODE = '" + cCYCODE + '\'' + 
			",fLAG = '" + fLAG + '\'' + 
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
		dest.writeDouble(tOTALVALUEAED);
		dest.writeDouble(aMOUNT);
		dest.writeDouble(rATE);
		dest.writeString(cCYCODE);
		dest.writeString(fLAG);
		dest.writeString(cCYDESC);
	}
}