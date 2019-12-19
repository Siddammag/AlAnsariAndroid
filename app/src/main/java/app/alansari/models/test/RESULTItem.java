package app.alansari.models.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class RESULTItem implements Parcelable {

	@SerializedName("ACC_PK_ID")
	private int aCCPKID;

	@SerializedName("USER_FK_ID")
	private Object uSERFKID;

	@SerializedName("MEM_PK_ID")
	private Object mEMPKID;

	@SerializedName("MEM_CARD_NO")
	private String mEMCARDNO;

	@SerializedName("BANK_NAME")
	private String bANKNAME;

	@SerializedName("BANK_CODE")
	private String bANKCODE;

	@SerializedName("UPDATED_DATE")
	private Object uPDATEDDATE;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("BANK_BRANCH_NAME")
	private Object bANKBRANCHNAME;

	@SerializedName("IBAN_NUMBER")
	private String iBANNUMBER;

	@SerializedName("CHANNEL_ID")
	private Object cHANNELID;

	@SerializedName("CREATION_DATE")
	private long cREATIONDATE;

	@SerializedName("CREATED_BY")
	private String cREATEDBY;

	@SerializedName("UPDATED_BY")
	private Object uPDATEDBY;

	@SerializedName("PGS_FLAG")
	private String pGSFLAG;

	@SerializedName("PGS_BANK_CODE")
	private String pGSBANKCODE;

	@SerializedName("PGS_MESSAGE")
	private String pGSMESSAGE;

	@SerializedName("ALERT_MESSAGE")
	private String aLERTMESSAGE;

	@SerializedName("ACCOUNT_NUMBER")
	private Object aCCOUNTNUMBER;

	@SerializedName("ACCOUNT_NAME")
	private String aCCOUNTNAME;

	@SerializedName("SESSION_ID")
	private Object sESSIONID;

	@SerializedName("DEVICE_ID")
	private Object dEVICEID;

	@SerializedName("PAY_TYPE_BT_LIST")
	private List<PAYTYPEBTLISTItem> pAYTYPEBTLIST;

	@SerializedName("FT_LEARN_MORE_URL")
	private String fTLEARNMOREURL;

	@SerializedName("OB_LEARN_MORE_URL")
	private String oBLEARNMOREURL;

	protected RESULTItem(Parcel in) {
		aCCPKID = in.readInt();
		mEMCARDNO = in.readString();
		bANKNAME = in.readString();
		bANKCODE = in.readString();
		sTATUS = in.readString();
		iBANNUMBER = in.readString();
		cREATIONDATE = in.readLong();
		cREATEDBY = in.readString();
		pGSFLAG = in.readString();
		pGSBANKCODE = in.readString();
		pGSMESSAGE = in.readString();
		aLERTMESSAGE = in.readString();
		aCCOUNTNAME = in.readString();
		fTLEARNMOREURL = in.readString();
		oBLEARNMOREURL = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(aCCPKID);
		dest.writeString(mEMCARDNO);
		dest.writeString(bANKNAME);
		dest.writeString(bANKCODE);
		dest.writeString(sTATUS);
		dest.writeString(iBANNUMBER);
		dest.writeLong(cREATIONDATE);
		dest.writeString(cREATEDBY);
		dest.writeString(pGSFLAG);
		dest.writeString(pGSBANKCODE);
		dest.writeString(pGSMESSAGE);
		dest.writeString(aLERTMESSAGE);
		dest.writeString(aCCOUNTNAME);
		dest.writeString(fTLEARNMOREURL);
		dest.writeString(oBLEARNMOREURL);
	}

	@Override
	public int describeContents() {
		return 0;
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

	public void setACCPKID(int aCCPKID){
		this.aCCPKID = aCCPKID;
	}

	public int getACCPKID(){
		return aCCPKID;
	}

	public void setUSERFKID(Object uSERFKID){
		this.uSERFKID = uSERFKID;
	}

	public Object getUSERFKID(){
		return uSERFKID;
	}

	public void setMEMPKID(Object mEMPKID){
		this.mEMPKID = mEMPKID;
	}

	public Object getMEMPKID(){
		return mEMPKID;
	}

	public void setMEMCARDNO(String mEMCARDNO){
		this.mEMCARDNO = mEMCARDNO;
	}

	public String getMEMCARDNO(){
		return mEMCARDNO;
	}

	public void setBANKNAME(String bANKNAME){
		this.bANKNAME = bANKNAME;
	}

	public String getBANKNAME(){
		return bANKNAME;
	}

	public void setBANKCODE(String bANKCODE){
		this.bANKCODE = bANKCODE;
	}

	public String getBANKCODE(){
		return bANKCODE;
	}

	public void setUPDATEDDATE(Object uPDATEDDATE){
		this.uPDATEDDATE = uPDATEDDATE;
	}

	public Object getUPDATEDDATE(){
		return uPDATEDDATE;
	}

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setBANKBRANCHNAME(Object bANKBRANCHNAME){
		this.bANKBRANCHNAME = bANKBRANCHNAME;
	}

	public Object getBANKBRANCHNAME(){
		return bANKBRANCHNAME;
	}

	public void setIBANNUMBER(String iBANNUMBER){
		this.iBANNUMBER = iBANNUMBER;
	}

	public String getIBANNUMBER(){
		return iBANNUMBER;
	}

	public void setCHANNELID(Object cHANNELID){
		this.cHANNELID = cHANNELID;
	}

	public Object getCHANNELID(){
		return cHANNELID;
	}

	public void setCREATIONDATE(long cREATIONDATE){
		this.cREATIONDATE = cREATIONDATE;
	}

	public long getCREATIONDATE(){
		return cREATIONDATE;
	}

	public void setCREATEDBY(String cREATEDBY){
		this.cREATEDBY = cREATEDBY;
	}

	public String getCREATEDBY(){
		return cREATEDBY;
	}

	public void setUPDATEDBY(Object uPDATEDBY){
		this.uPDATEDBY = uPDATEDBY;
	}

	public Object getUPDATEDBY(){
		return uPDATEDBY;
	}

	public void setPGSFLAG(String pGSFLAG){
		this.pGSFLAG = pGSFLAG;
	}

	public String getPGSFLAG(){
		return pGSFLAG;
	}

	public void setPGSBANKCODE(String pGSBANKCODE){
		this.pGSBANKCODE = pGSBANKCODE;
	}

	public String getPGSBANKCODE(){
		return pGSBANKCODE;
	}

	public void setPGSMESSAGE(String pGSMESSAGE){
		this.pGSMESSAGE = pGSMESSAGE;
	}

	public String getPGSMESSAGE(){
		return pGSMESSAGE;
	}

	public void setALERTMESSAGE(String aLERTMESSAGE){
		this.aLERTMESSAGE = aLERTMESSAGE;
	}

	public String getALERTMESSAGE(){
		return aLERTMESSAGE;
	}

	public void setACCOUNTNUMBER(Object aCCOUNTNUMBER){
		this.aCCOUNTNUMBER = aCCOUNTNUMBER;
	}

	public Object getACCOUNTNUMBER(){
		return aCCOUNTNUMBER;
	}

	public void setACCOUNTNAME(String aCCOUNTNAME){
		this.aCCOUNTNAME = aCCOUNTNAME;
	}

	public String getACCOUNTNAME(){
		return aCCOUNTNAME;
	}

	public void setSESSIONID(Object sESSIONID){
		this.sESSIONID = sESSIONID;
	}

	public Object getSESSIONID(){
		return sESSIONID;
	}

	public void setDEVICEID(Object dEVICEID){
		this.dEVICEID = dEVICEID;
	}

	public Object getDEVICEID(){
		return dEVICEID;
	}

	public void setPAYTYPEBTLIST(List<PAYTYPEBTLISTItem> pAYTYPEBTLIST){
		this.pAYTYPEBTLIST = pAYTYPEBTLIST;
	}

	public List<PAYTYPEBTLISTItem> getPAYTYPEBTLIST(){
		return pAYTYPEBTLIST;
	}

	public void setFTLEARNMOREURL(String fTLEARNMOREURL){
		this.fTLEARNMOREURL = fTLEARNMOREURL;
	}

	public String getFTLEARNMOREURL(){
		return fTLEARNMOREURL;
	}

	public void setOBLEARNMOREURL(String oBLEARNMOREURL){
		this.oBLEARNMOREURL = oBLEARNMOREURL;
	}

	public String getOBLEARNMOREURL(){
		return oBLEARNMOREURL;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"aCC_PK_ID = '" + aCCPKID + '\'' + 
			",uSER_FK_ID = '" + uSERFKID + '\'' + 
			",mEM_PK_ID = '" + mEMPKID + '\'' + 
			",mEM_CARD_NO = '" + mEMCARDNO + '\'' + 
			",bANK_NAME = '" + bANKNAME + '\'' + 
			",bANK_CODE = '" + bANKCODE + '\'' + 
			",uPDATED_DATE = '" + uPDATEDDATE + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",bANK_BRANCH_NAME = '" + bANKBRANCHNAME + '\'' + 
			",iBAN_NUMBER = '" + iBANNUMBER + '\'' + 
			",cHANNEL_ID = '" + cHANNELID + '\'' + 
			",cREATION_DATE = '" + cREATIONDATE + '\'' + 
			",cREATED_BY = '" + cREATEDBY + '\'' + 
			",uPDATED_BY = '" + uPDATEDBY + '\'' + 
			",pGS_FLAG = '" + pGSFLAG + '\'' + 
			",pGS_BANK_CODE = '" + pGSBANKCODE + '\'' + 
			",pGS_MESSAGE = '" + pGSMESSAGE + '\'' + 
			",aLERT_MESSAGE = '" + aLERTMESSAGE + '\'' + 
			",aCCOUNT_NUMBER = '" + aCCOUNTNUMBER + '\'' + 
			",aCCOUNT_NAME = '" + aCCOUNTNAME + '\'' + 
			",sESSION_ID = '" + sESSIONID + '\'' + 
			",dEVICE_ID = '" + dEVICEID + '\'' + 
			",pAY_TYPE_BT_LIST = '" + pAYTYPEBTLIST + '\'' + 
			",fT_LEARN_MORE_URL = '" + fTLEARNMOREURL + '\'' + 
			",oB_LEARN_MORE_URL = '" + oBLEARNMOREURL + '\'' + 
			"}";
		}
}