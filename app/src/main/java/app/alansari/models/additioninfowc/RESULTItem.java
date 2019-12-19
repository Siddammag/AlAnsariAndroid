package app.alansari.models.additioninfowc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RESULTItem implements Parcelable{

	@SerializedName("USER_PK_ID")
	private Object uSERPKID;

	@SerializedName("CHARGE_AMOUNT")
	private Object cHARGEAMOUNT;

	@SerializedName("TXN_PURPOSE_LIST")
	private List<TXNPURPOSELISTItem> tXNPURPOSELIST;

	@SerializedName("SOURCE_OF_FUND")
	private List<SOURCEOFFUNDItem> sOURCEOFFUND;


	@SerializedName("RATE")
	private Object rATE;

	@SerializedName("BANK_ID")
	private Object bANKID;

	@SerializedName("VALUE")
	private Object vALUE;

	@SerializedName("DISC_AMOUNT")
	private Object dISCAMOUNT;

	@SerializedName("FIELD_KEY")
	private Object fIELDKEY;

	@SerializedName("CCY_ID")
	private Object cCYID;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("TXN_AMOUNT")
	private Object tXNAMOUNT;

	@SerializedName("NET_TOTAL")
	private Object nETTOTAL;

	@SerializedName("RESPONSE_MSG")
	private Object rESPONSEMSG;

	@SerializedName("SERVICE_ID")
	private Object sERVICEID;

	@SerializedName("CCY_CODE")
	private Object cCYCODE;

	@SerializedName("BANK_CODE")
	private Object bANKCODE;

	@SerializedName("ROUNDED_OFF_AMOUNT")
	private Object rOUNDEDOFFAMOUNT;

	@SerializedName("TYPE")
	private String tYPE;

	@SerializedName("TOTAL_VALUE_AED")
	private Object tOTALVALUEAED;



	@SerializedName("FIELD_ID")
	private String fIELDID;

	@SerializedName("NAME")
	private Object nAME;

	@SerializedName("MEM_PK_ID")
	private Object mEMPKID;

	@SerializedName("TRANSFER_TYPE")
	private Object tRANSFERTYPE;

	@SerializedName("COMM_AMOUNT")
	private Object cOMMAMOUNT;

	@SerializedName("TITLE")
	private String tITLE;

	@SerializedName("SERVICE_TYPE")
	private Object sERVICETYPE;

	@SerializedName("CCY_DESC")
	private Object cCYDESC;

	@SerializedName("MANDATORY_ADDITIONAL_INFO_FIELDS")
	private String mANDATORYADDITIONALINFOFIELDS;

	protected RESULTItem(Parcel in) {
		sTATUS = in.readString();
		tYPE = in.readString();
		fIELDID = in.readString();
		tITLE = in.readString();
		this.tXNPURPOSELIST = in.createTypedArrayList(TXNPURPOSELISTItem.CREATOR);
		this.sOURCEOFFUND = in.createTypedArrayList(SOURCEOFFUNDItem.CREATOR);
	}

	public static final Creator<RESULTItem> CREATOR = new Creator<RESULTItem>() {
		@Override
		public RESULTItem createFromParcel(Parcel in) {
			return new RESULTItem(in);
		}

		@Override
		public RESULTItem[] newArray(int size) {
			return new RESULTItem[size];
		}
	};

	public void setUSERPKID(Object uSERPKID){
		this.uSERPKID = uSERPKID;
	}

	public Object getUSERPKID(){
		return uSERPKID;
	}

	public void setCHARGEAMOUNT(Object cHARGEAMOUNT){
		this.cHARGEAMOUNT = cHARGEAMOUNT;
	}

	public Object getCHARGEAMOUNT(){
		return cHARGEAMOUNT;
	}

	public void setTXNPURPOSELIST(List<TXNPURPOSELISTItem> tXNPURPOSELIST){
		this.tXNPURPOSELIST = tXNPURPOSELIST;
	}



	public List<TXNPURPOSELISTItem> getTXNPURPOSELIST(){
		return  tXNPURPOSELIST;
	}


	public List<SOURCEOFFUNDItem> getsOURCEOFFUND() {
		return  sOURCEOFFUND;
	}



	public void setsOURCEOFFUND(List<SOURCEOFFUNDItem> sOURCEOFFUND) {
		this.sOURCEOFFUND = sOURCEOFFUND;
	}

	public void setRATE(Object rATE){
		this.rATE = rATE;
	}

	public Object getRATE(){
		return rATE;
	}

	public void setBANKID(Object bANKID){
		this.bANKID = bANKID;
	}

	public Object getBANKID(){
		return bANKID;
	}

	public void setVALUE(Object vALUE){
		this.vALUE = vALUE;
	}

	public Object getVALUE(){
		return vALUE;
	}

	public void setDISCAMOUNT(Object dISCAMOUNT){
		this.dISCAMOUNT = dISCAMOUNT;
	}

	public Object getDISCAMOUNT(){
		return dISCAMOUNT;
	}

	public void setFIELDKEY(Object fIELDKEY){
		this.fIELDKEY = fIELDKEY;
	}

	public Object getFIELDKEY(){
		return fIELDKEY;
	}

	public void setCCYID(Object cCYID){
		this.cCYID = cCYID;
	}

	public Object getCCYID(){
		return cCYID;
	}

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setTXNAMOUNT(Object tXNAMOUNT){
		this.tXNAMOUNT = tXNAMOUNT;
	}

	public Object getTXNAMOUNT(){
		return tXNAMOUNT;
	}

	public void setNETTOTAL(Object nETTOTAL){
		this.nETTOTAL = nETTOTAL;
	}

	public Object getNETTOTAL(){
		return nETTOTAL;
	}

	public void setRESPONSEMSG(Object rESPONSEMSG){
		this.rESPONSEMSG = rESPONSEMSG;
	}

	public Object getRESPONSEMSG(){
		return rESPONSEMSG;
	}

	public void setSERVICEID(Object sERVICEID){
		this.sERVICEID = sERVICEID;
	}

	public Object getSERVICEID(){
		return sERVICEID;
	}

	public void setCCYCODE(Object cCYCODE){
		this.cCYCODE = cCYCODE;
	}

	public Object getCCYCODE(){
		return cCYCODE;
	}

	public void setBANKCODE(Object bANKCODE){
		this.bANKCODE = bANKCODE;
	}

	public Object getBANKCODE(){
		return bANKCODE;
	}

	public void setROUNDEDOFFAMOUNT(Object rOUNDEDOFFAMOUNT){
		this.rOUNDEDOFFAMOUNT = rOUNDEDOFFAMOUNT;
	}

	public Object getROUNDEDOFFAMOUNT(){
		return rOUNDEDOFFAMOUNT;
	}

	public void setTYPE(String tYPE){
		this.tYPE = tYPE;
	}

	public String getTYPE(){
		return tYPE;
	}

	public void setTOTALVALUEAED(Object tOTALVALUEAED){
		this.tOTALVALUEAED = tOTALVALUEAED;
	}

	public Object getTOTALVALUEAED(){
		return tOTALVALUEAED;
	}



	public void setFIELDID(String fIELDID){
		this.fIELDID = fIELDID;
	}

	public String getFIELDID(){
		return fIELDID;
	}

	public void setNAME(Object nAME){
		this.nAME = nAME;
	}

	public Object getNAME(){
		return nAME;
	}

	public void setMEMPKID(Object mEMPKID){
		this.mEMPKID = mEMPKID;
	}

	public Object getMEMPKID(){
		return mEMPKID;
	}

	public void setTRANSFERTYPE(Object tRANSFERTYPE){
		this.tRANSFERTYPE = tRANSFERTYPE;
	}

	public Object getTRANSFERTYPE(){
		return tRANSFERTYPE;
	}

	public void setCOMMAMOUNT(Object cOMMAMOUNT){
		this.cOMMAMOUNT = cOMMAMOUNT;
	}

	public Object getCOMMAMOUNT(){
		return cOMMAMOUNT;
	}

	public void setTITLE(String tITLE){
		this.tITLE = tITLE;
	}

	public String getTITLE(){
		return tITLE;
	}

	public void setSERVICETYPE(Object sERVICETYPE){
		this.sERVICETYPE = sERVICETYPE;
	}

	public Object getSERVICETYPE(){
		return sERVICETYPE;
	}

	public void setCCYDESC(Object cCYDESC){
		this.cCYDESC = cCYDESC;
	}

	public Object getCCYDESC(){
		return cCYDESC;
	}

	public void setMANDATORYADDITIONALINFOFIELDS(String mANDATORYADDITIONALINFOFIELDS){
		this.mANDATORYADDITIONALINFOFIELDS = mANDATORYADDITIONALINFOFIELDS;
	}

	public String getMANDATORYADDITIONALINFOFIELDS(){
		return mANDATORYADDITIONALINFOFIELDS;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"uSER_PK_ID = '" + uSERPKID + '\'' + 
			",cHARGE_AMOUNT = '" + cHARGEAMOUNT + '\'' + 
			",tXN_PURPOSE_LIST = '" + tXNPURPOSELIST + '\'' + 
			",rATE = '" + rATE + '\'' + 
			",bANK_ID = '" + bANKID + '\'' + 
			",vALUE = '" + vALUE + '\'' + 
			",dISC_AMOUNT = '" + dISCAMOUNT + '\'' + 
			",fIELD_KEY = '" + fIELDKEY + '\'' + 
			",cCY_ID = '" + cCYID + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",tXN_AMOUNT = '" + tXNAMOUNT + '\'' + 
			",nET_TOTAL = '" + nETTOTAL + '\'' + 
			",rESPONSE_MSG = '" + rESPONSEMSG + '\'' + 
			",sERVICE_ID = '" + sERVICEID + '\'' + 
			",cCY_CODE = '" + cCYCODE + '\'' + 
			",bANK_CODE = '" + bANKCODE + '\'' + 
			",rOUNDED_OFF_AMOUNT = '" + rOUNDEDOFFAMOUNT + '\'' + 
			",tYPE = '" + tYPE + '\'' + 
			",tOTAL_VALUE_AED = '" + tOTALVALUEAED + '\'' + 
			",sOURCE_OF_FUND = '" + sOURCEOFFUND + '\'' + 
			",fIELD_ID = '" + fIELDID + '\'' + 
			",nAME = '" + nAME + '\'' + 
			",mEM_PK_ID = '" + mEMPKID + '\'' + 
			",tRANSFER_TYPE = '" + tRANSFERTYPE + '\'' + 
			",cOMM_AMOUNT = '" + cOMMAMOUNT + '\'' + 
			",tITLE = '" + tITLE + '\'' + 
			",sERVICE_TYPE = '" + sERVICETYPE + '\'' + 
			",cCY_DESC = '" + cCYDESC + '\'' + 
			",mANDATORY_ADDITIONAL_INFO_FIELDS = '" + mANDATORYADDITIONALINFOFIELDS + '\'' + 
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
		dest.writeString(sTATUS);
		dest.writeString(tYPE);
		dest.writeString(fIELDID);
		dest.writeString(tITLE);
		dest.writeTypedList(this.tXNPURPOSELIST);
		dest.writeTypedList(this.sOURCEOFFUND);

	}
	public static class TXNPURPOSELISTItem implements Parcelable{

		@SerializedName("displayKey")
		private String displayKey;

		@SerializedName("displayValue")
		private String displayValue;

		@SerializedName("primaryKeyValue")
		private Object primaryKeyValue;

		protected TXNPURPOSELISTItem(Parcel in) {
			displayKey = in.readString();
			displayValue = in.readString();
		}

		public static final Creator<TXNPURPOSELISTItem> CREATOR = new Creator<TXNPURPOSELISTItem>() {
			@Override
			public TXNPURPOSELISTItem createFromParcel(Parcel in) {
				return new TXNPURPOSELISTItem(in);
			}

			@Override
			public TXNPURPOSELISTItem[] newArray(int size) {
				return new TXNPURPOSELISTItem[size];
			}
		};

		public void setDisplayKey(String displayKey){
			this.displayKey = displayKey;
		}

		public String getDisplayKey(){
			return displayKey;
		}

		public void setDisplayValue(String displayValue){
			this.displayValue = displayValue;
		}

		public String getDisplayValue(){
			return displayValue;
		}

		public void setPrimaryKeyValue(Object primaryKeyValue){
			this.primaryKeyValue = primaryKeyValue;
		}

		public Object getPrimaryKeyValue(){
			return primaryKeyValue;
		}

		@Override
		public String toString(){
			return
					"TXNPURPOSELISTItem{" +
							"displayKey = '" + displayKey + '\'' +
							",displayValue = '" + displayValue + '\'' +
							",primaryKeyValue = '" + primaryKeyValue + '\'' +
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
			dest.writeString(displayKey);
			dest.writeString(displayValue);
		}
	}

	public static class SOURCEOFFUNDItem implements Parcelable{

		@SerializedName("displayKey")
		private String displayKey;

		@SerializedName("displayValue")
		private String displayValue;

		@SerializedName("primaryKeyValue")
		private Object primaryKeyValue;

		protected SOURCEOFFUNDItem(Parcel in) {
			displayKey = in.readString();
			displayValue = in.readString();
		}

		public static final Creator<SOURCEOFFUNDItem> CREATOR = new Creator<SOURCEOFFUNDItem>() {
			@Override
			public SOURCEOFFUNDItem createFromParcel(Parcel in) {
				return new SOURCEOFFUNDItem(in);
			}

			@Override
			public SOURCEOFFUNDItem[] newArray(int size) {
				return new SOURCEOFFUNDItem[size];
			}
		};

		public void setDisplayKey(String displayKey){
			this.displayKey = displayKey;
		}

		public String getDisplayKey(){
			return displayKey;
		}

		public void setDisplayValue(String displayValue){
			this.displayValue = displayValue;
		}

		public String getDisplayValue(){
			return displayValue;
		}

		public void setPrimaryKeyValue(Object primaryKeyValue){
			this.primaryKeyValue = primaryKeyValue;
		}

		public Object getPrimaryKeyValue(){
			return primaryKeyValue;
		}

		@Override
		public String toString(){
			return
					"SOURCEOFFUNDItem{" +
							"displayKey = '" + displayKey + '\'' +
							",displayValue = '" + displayValue + '\'' +
							",primaryKeyValue = '" + primaryKeyValue + '\'' +
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
			dest.writeString(displayKey);
			dest.writeString(displayValue);
		}
	}
}