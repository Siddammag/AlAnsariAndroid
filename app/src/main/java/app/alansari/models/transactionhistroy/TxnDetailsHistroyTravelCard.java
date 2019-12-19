package app.alansari.models.transactionhistroy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import app.alansari.models.TxnDetailsData;

public class TxnDetailsHistroyTravelCard implements Parcelable{

	@SerializedName("CREATED_DATE")
	private long cREATEDDATE;

	@SerializedName("RATE")
	private Object rATE;

	@SerializedName("WALLET_DETAILS")
	private List<WALLETDETAILSItem> wALLETDETAILS;

	@SerializedName("CHARGES")
	private String cHARGES;

	@SerializedName("TXN_PAY_TYPE")
	private String tXNPAYTYPE;

	@SerializedName("CARD_TYPE")
	private Object cARDTYPE;

	@SerializedName("REFERENCE_NUMBER")
	private Object rEFERENCENUMBER;

	@SerializedName("TXN_STATUS")
	private String tXNSTATUS;

	@SerializedName("INVOICE_FLAG")
	private String iNVOICEFLAG;

	@SerializedName("CARD_NUMBER")
	private String cARDNUMBER;

	@SerializedName("STATUS_MSG")
	private String sTATUSMSG;

	@SerializedName("AMOUNT_IN_AED")
	private String aMOUNTINAED;

	@SerializedName("CHANNEL")
	private Object cHANNEL;

	@SerializedName("CUSTOMER_NAME")
	private String cUSTOMERNAME;

	@SerializedName("TXN_AMOUNT")
	private Object tXNAMOUNT;

	@SerializedName("EXT_REFERENCE_NUMBER")
	private Object eXTREFERENCENUMBER;

	@SerializedName("FCY_AMOUNT")
	private Object fCYAMOUNT;

	@SerializedName("DOC_NUMBER")
	private long dOCNUMBER;

	@SerializedName("SERVICE_TYPE")
	private String sERVICETYPE;

	@SerializedName("VAT_DISCOUNT")
	private String vATDISCOUNT;

	@SerializedName("TOTAL_AMOUNT")
	private String tOTALAMOUNT;

	@SerializedName("VAT_CHARGES")
	private String vATCHARGES;
	@SerializedName("REM_TXN_PK_ID")
	private int id;



	protected TxnDetailsHistroyTravelCard(Parcel in) {
		cREATEDDATE = in.readLong();
		wALLETDETAILS = in.createTypedArrayList(WALLETDETAILSItem.CREATOR);
		cHARGES = in.readString();

		tXNPAYTYPE = in.readString();
		tXNSTATUS = in.readString();
		iNVOICEFLAG = in.readString();
		cARDNUMBER = in.readString();
		sTATUSMSG = in.readString();
		aMOUNTINAED = in.readString();
		cUSTOMERNAME = in.readString();
		dOCNUMBER = in.readLong();
		sERVICETYPE = in.readString();
		vATDISCOUNT = in.readString();
		tOTALAMOUNT = in.readString();
		vATCHARGES = in.readString();
		id=in.readInt();
	}

	public static final Creator<TxnDetailsHistroyTravelCard> CREATOR = new Creator<TxnDetailsHistroyTravelCard>() {
		@Override
		public TxnDetailsHistroyTravelCard createFromParcel(Parcel in) {
			return new TxnDetailsHistroyTravelCard(in);
		}

		@Override
		public TxnDetailsHistroyTravelCard[] newArray(int size) {
			return new TxnDetailsHistroyTravelCard[size];
		}
	};

	public void setCREATEDDATE(long cREATEDDATE){
		this.cREATEDDATE = cREATEDDATE;
	}

	public long getCREATEDDATE(){
		return cREATEDDATE;
	}

	public void setRATE(Object rATE){
		this.rATE = rATE;
	}

	public Object getRATE(){
		return rATE;
	}



	public void setWALLETDETAILS(List<WALLETDETAILSItem> wALLETDETAILS){
		this.wALLETDETAILS = wALLETDETAILS;
	}

	public List<WALLETDETAILSItem> getWALLETDETAILS(){
		return wALLETDETAILS;
	}

	public void setCHARGES(String cHARGES){
		this.cHARGES = cHARGES;
	}

	public String getCHARGES(){
		return cHARGES;
	}

	public void setTXNPAYTYPE(String tXNPAYTYPE){
		this.tXNPAYTYPE = tXNPAYTYPE;
	}

	public String getTXNPAYTYPE(){
		return tXNPAYTYPE;
	}

	public void setCARDTYPE(Object cARDTYPE){
		this.cARDTYPE = cARDTYPE;
	}

	public Object getCARDTYPE(){
		return cARDTYPE;
	}

	public void setREFERENCENUMBER(Object rEFERENCENUMBER){
		this.rEFERENCENUMBER = rEFERENCENUMBER;
	}

	public Object getREFERENCENUMBER(){
		return rEFERENCENUMBER;
	}

	public void setTXNSTATUS(String tXNSTATUS){
		this.tXNSTATUS = tXNSTATUS;
	}

	public String getTXNSTATUS(){
		return tXNSTATUS;
	}

	public void setINVOICEFLAG(String iNVOICEFLAG){
		this.iNVOICEFLAG = iNVOICEFLAG;
	}

	public String getINVOICEFLAG(){
		return iNVOICEFLAG;
	}

	public void setCARDNUMBER(String cARDNUMBER){
		this.cARDNUMBER = cARDNUMBER;
	}

	public String getCARDNUMBER(){
		return cARDNUMBER;
	}

	public void setSTATUSMSG(String sTATUSMSG){
		this.sTATUSMSG = sTATUSMSG;
	}

	public String getSTATUSMSG(){
		return sTATUSMSG;
	}

	public void setAMOUNTINAED(String aMOUNTINAED){
		this.aMOUNTINAED = aMOUNTINAED;
	}

	public String getAMOUNTINAED(){
		return aMOUNTINAED;
	}

	public void setCHANNEL(Object cHANNEL){
		this.cHANNEL = cHANNEL;
	}

	public Object getCHANNEL(){
		return cHANNEL;
	}

	public void setCUSTOMERNAME(String cUSTOMERNAME){
		this.cUSTOMERNAME = cUSTOMERNAME;
	}

	public String getCUSTOMERNAME(){
		return cUSTOMERNAME;
	}

	public void setTXNAMOUNT(Object tXNAMOUNT){
		this.tXNAMOUNT = tXNAMOUNT;
	}

	public Object getTXNAMOUNT(){
		return tXNAMOUNT;
	}

	public void setEXTREFERENCENUMBER(Object eXTREFERENCENUMBER){
		this.eXTREFERENCENUMBER = eXTREFERENCENUMBER;
	}

	public Object getEXTREFERENCENUMBER(){
		return eXTREFERENCENUMBER;
	}

	public void setFCYAMOUNT(Object fCYAMOUNT){
		this.fCYAMOUNT = fCYAMOUNT;
	}

	public Object getFCYAMOUNT(){
		return fCYAMOUNT;
	}

	public void setDOCNUMBER(long dOCNUMBER){
		this.dOCNUMBER = dOCNUMBER;
	}

	public long getDOCNUMBER(){
		return dOCNUMBER;
	}

	public void setSERVICETYPE(String sERVICETYPE){
		this.sERVICETYPE = sERVICETYPE;
	}

	public String getSERVICETYPE(){
		return sERVICETYPE;
	}

	public void setVATDISCOUNT(String vATDISCOUNT){
		this.vATDISCOUNT = vATDISCOUNT;
	}

	public String getVATDISCOUNT(){
		return vATDISCOUNT;
	}

	public void setTOTALAMOUNT(String tOTALAMOUNT){
		this.tOTALAMOUNT = tOTALAMOUNT;
	}

	public String getTOTALAMOUNT(){
		return tOTALAMOUNT;
	}

	public void setVATCHARGES(String vATCHARGES){
		this.vATCHARGES = vATCHARGES;
	}

	public String getVATCHARGES(){
		return vATCHARGES;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"cREATED_DATE = '" + cREATEDDATE + '\'' + 
			",rATE = '" + rATE + '\'' + 
			",wALLET_DETAILS = '" + wALLETDETAILS + '\'' + 
			",cHARGES = '" + cHARGES + '\'' + 
			",tXN_PAY_TYPE = '" + tXNPAYTYPE + '\'' + 
			",cARD_TYPE = '" + cARDTYPE + '\'' + 
			",rEFERENCE_NUMBER = '" + rEFERENCENUMBER + '\'' + 
			",tXN_STATUS = '" + tXNSTATUS + '\'' + 
			",iNVOICE_FLAG = '" + iNVOICEFLAG + '\'' + 
			",cARD_NUMBER = '" + cARDNUMBER + '\'' + 
			",sTATUS_MSG = '" + sTATUSMSG + '\'' + 
			",aMOUNT_IN_AED = '" + aMOUNTINAED + '\'' + 
			",cHANNEL = '" + cHANNEL + '\'' + 
			",cUSTOMER_NAME = '" + cUSTOMERNAME + '\'' + 
			",tXN_AMOUNT = '" + tXNAMOUNT + '\'' + 
			",eXT_REFERENCE_NUMBER = '" + eXTREFERENCENUMBER + '\'' + 
			",fCY_AMOUNT = '" + fCYAMOUNT + '\'' + 
			",dOC_NUMBER = '" + dOCNUMBER + '\'' + 
			",sERVICE_TYPE = '" + sERVICETYPE + '\'' + 
			",vAT_DISCOUNT = '" + vATDISCOUNT + '\'' + 
			",tOTAL_AMOUNT = '" + tOTALAMOUNT + '\'' + 
			",vAT_CHARGES = '" + vATCHARGES + '\'' + 
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
		dest.writeLong(cREATEDDATE);
		dest.writeTypedList(wALLETDETAILS);
		dest.writeString(cHARGES);
		dest.writeString(tXNPAYTYPE);
		dest.writeString(tXNSTATUS);
		dest.writeString(iNVOICEFLAG);
		dest.writeString(cARDNUMBER);
		dest.writeString(sTATUSMSG);
		dest.writeString(aMOUNTINAED);
		dest.writeString(cUSTOMERNAME);
		dest.writeLong(dOCNUMBER);
		dest.writeString(sERVICETYPE);
		dest.writeString(vATDISCOUNT);
		dest.writeString(tOTALAMOUNT);
		dest.writeString(vATCHARGES);
		dest.writeInt(id);
	}



}