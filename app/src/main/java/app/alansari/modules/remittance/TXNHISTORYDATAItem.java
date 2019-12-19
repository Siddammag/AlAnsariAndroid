package app.alansari.modules.remittance;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class TXNHISTORYDATAItem implements Parcelable {

	@SerializedName("REM_TXN_PK_ID")
	private int rEMTXNPKID;

	@SerializedName("REM_TXN_REF_NUM")
	private String rEMTXNREFNUM;

	@SerializedName("GTW_USER_MSTR_FK_ID")
	private String gTWUSERMSTRFKID;

	@SerializedName("TXN_STATUS")
	private String tXNSTATUS;

	@SerializedName("TXN_CODE")
	private String tXNCODE;

	@SerializedName("CHANNEL")
	private String cHANNEL;

	@SerializedName("TOTAL_TXN_AMT")
	private String tOTALTXNAMT;

	@SerializedName("TXN_TYPE")
	private String tXNTYPE;

	@SerializedName("TXN_REC_TYPE")
	private String tXNRECTYPE;

	@SerializedName("TXN_PAY_TYPE")
	private String tXNPAYTYPE;

	@SerializedName("TXN_EXP_LIMIT")
	private String tXNEXPLIMIT;

	@SerializedName("TXN_EXP_DESC")
	private String tXNEXPDESC;

	@SerializedName("CREATED_DATE")
	private String cREATEDDATE;

	@SerializedName("BENEFICIARY_ID")
	private String bENEFICIARYID;

	@SerializedName("RATE")
	private String rATE;

	@SerializedName("COST_PRICE")
	private String cOSTPRICE;

	@SerializedName("TOTAL_VALUE_AED")
	private String tOTALVALUEAED;

	@SerializedName("SERVICE_TYPE")
	private String sERVICETYPE;

	@SerializedName("INVOICE_FLAG")
	private String iNVOICEFLAG;

	@SerializedName("REJECTION_MESSAGE")
	private String rEJECTIONMESSAGE;

	@SerializedName("AAE_DOC_NUM")
	private String aAEDOCNUM;

	@SerializedName("AREX_MEM_CARD_FK_ID")
	private String aREXMEMCARDFKID;

	@SerializedName("AREX_MEM_CARD_NUM")
	private String aREXMEMCARDNUM;

	@SerializedName("CURRENT_TIME")
	private String cURRENTTIME;

	@SerializedName("CCY_DESC")
	private String cCYDESC;

	@SerializedName("BANK_NAME")
	private String bANKNAME;

	@SerializedName("FCY_AMT")
	private String fCYAMT;

	@SerializedName("AED_AMT")
	private String aEDAMT;

	@SerializedName("NET_TOTAL")
	private String nETTOTAL;

	@SerializedName("BENF_NAME")
	private String bENFNAME;

	@SerializedName("BRANCH_NAME")
	private String bRANCHNAME;

	@SerializedName("BRANCH_CODE")
	private String bRANCHCODE;

	@SerializedName("BENEF_MOBILE_NUM")
	private String bENEFMOBILENUM;

	@SerializedName("PAYOUT_STATE")
	private String pAYOUTSTATE;

	@SerializedName("PAYOUT_CITY")
	private String pAYOUTCITY;

	@SerializedName("MTCN")
	private String mTCN;

	@SerializedName("BENEF_ACC_NUM")
	private String bENEFACCNUM;

	@SerializedName("PAYMENT_MODE")
	private String pAYMENTMODE;

	@SerializedName("BENEF_BANK")
	private String bENEFBANK;

	@SerializedName("BENEF_BRANCH")
	private String bENEFBRANCH;

	@SerializedName("CE_MEM_CARD_NUM")
	private String cEMEMCARDNUM;

	@SerializedName("CE_MEM_CARD_FK_ID")
	private String cEMEMCARDFKID;

	@SerializedName("SOURCE_AGENT_NAME")
	private String sOURCEAGENTNAME;

	@SerializedName("CARD_HOLDER_NAME")
	private String cARDHOLDERNAME;

	@SerializedName("CARD_TYPE")
	private String cARDTYPE;

	@SerializedName("CCY_CODE")
	private String cCYCODE;

	@SerializedName("CARD_NUMBER")
	private String cARDNUMBER;

	@SerializedName("CREATED_DATE_STR")
	private String cREATEDDATESTR;

	protected TXNHISTORYDATAItem(Parcel in) {
		rEMTXNPKID = in.readInt();
		rEMTXNREFNUM = in.readString();
		tXNSTATUS = in.readString();
		tXNCODE = in.readString();
		cHANNEL = in.readString();
		tOTALTXNAMT = in.readString();
		tXNTYPE = in.readString();
		tXNRECTYPE = in.readString();
		tXNPAYTYPE = in.readString();
		cREATEDDATE = in.readString();
		tOTALVALUEAED = in.readString();
		sERVICETYPE = in.readString();
		iNVOICEFLAG = in.readString();
		aAEDOCNUM = in.readString();
		aREXMEMCARDFKID = in.readString();
		aREXMEMCARDNUM = in.readString();
		cURRENTTIME = in.readString();
		cCYDESC = in.readString();
		nETTOTAL = in.readString();
		bENFNAME = in.readString();
		bENEFACCNUM = in.readString();
		pAYMENTMODE = in.readString();
		bENEFBANK = in.readString();
		bENEFBRANCH = in.readString();
		cREATEDDATESTR = in.readString();
		rEJECTIONMESSAGE=in.readString();
		fCYAMT=in.readString();
		bENEFMOBILENUM=in.readString();
	}

	public static final Creator<TXNHISTORYDATAItem> CREATOR = new Creator<TXNHISTORYDATAItem>() {
		@Override
		public TXNHISTORYDATAItem createFromParcel(Parcel in) {
			return new TXNHISTORYDATAItem(in);
		}

		@Override
		public TXNHISTORYDATAItem[] newArray(int size) {
			return new TXNHISTORYDATAItem[size];
		}
	};

	public void setREMTXNPKID(int rEMTXNPKID){
		this.rEMTXNPKID = rEMTXNPKID;
	}

	public int getREMTXNPKID(){
		return rEMTXNPKID;
	}

	public void setREMTXNREFNUM(String rEMTXNREFNUM){
		this.rEMTXNREFNUM = rEMTXNREFNUM;
	}

	public String getREMTXNREFNUM(){
		return rEMTXNREFNUM;
	}

	public void setGTWUSERMSTRFKID(String gTWUSERMSTRFKID){
		this.gTWUSERMSTRFKID = gTWUSERMSTRFKID;
	}

	public String getGTWUSERMSTRFKID(){
		return gTWUSERMSTRFKID;
	}

	public void setTXNSTATUS(String tXNSTATUS){
		this.tXNSTATUS = tXNSTATUS;
	}

	public String getTXNSTATUS(){
		return tXNSTATUS;
	}

	public void setTXNCODE(String tXNCODE){
		this.tXNCODE = tXNCODE;
	}

	public String getTXNCODE(){
		return tXNCODE;
	}

	public void setCHANNEL(String cHANNEL){
		this.cHANNEL = cHANNEL;
	}

	public String getCHANNEL(){
		return cHANNEL;
	}

	public void setTOTALTXNAMT(String tOTALTXNAMT){
		this.tOTALTXNAMT = tOTALTXNAMT;
	}

	public String getTOTALTXNAMT(){
		return tOTALTXNAMT;
	}

	public void setTXNTYPE(String tXNTYPE){
		this.tXNTYPE = tXNTYPE;
	}

	public String getTXNTYPE(){
		return tXNTYPE;
	}

	public void setTXNRECTYPE(String tXNRECTYPE){
		this.tXNRECTYPE = tXNRECTYPE;
	}

	public String getTXNRECTYPE(){
		return tXNRECTYPE;
	}

	public void setTXNPAYTYPE(String tXNPAYTYPE){
		this.tXNPAYTYPE = tXNPAYTYPE;
	}

	public String getTXNPAYTYPE(){
		return tXNPAYTYPE;
	}

	public void setTXNEXPLIMIT(String tXNEXPLIMIT){
		this.tXNEXPLIMIT = tXNEXPLIMIT;
	}

	public String getTXNEXPLIMIT(){
		return tXNEXPLIMIT;
	}

	public void setTXNEXPDESC(String tXNEXPDESC){
		this.tXNEXPDESC = tXNEXPDESC;
	}

	public String getTXNEXPDESC(){
		return tXNEXPDESC;
	}

	public void setCREATEDDATE(String cREATEDDATE){
		this.cREATEDDATE = cREATEDDATE;
	}

	public String getCREATEDDATE(){
		return cREATEDDATE;
	}

	public void setBENEFICIARYID(String bENEFICIARYID){
		this.bENEFICIARYID = bENEFICIARYID;
	}

	public String getBENEFICIARYID(){
		return bENEFICIARYID;
	}

	public void setRATE(String rATE){
		this.rATE = rATE;
	}

	public String getRATE(){
		return rATE;
	}

	public void setCOSTPRICE(String cOSTPRICE){
		this.cOSTPRICE = cOSTPRICE;
	}

	public String getCOSTPRICE(){
		return cOSTPRICE;
	}

	public void setTOTALVALUEAED(String tOTALVALUEAED){
		this.tOTALVALUEAED = tOTALVALUEAED;
	}

	public String getTOTALVALUEAED(){
		return tOTALVALUEAED;
	}

	public void setSERVICETYPE(String sERVICETYPE){
		this.sERVICETYPE = sERVICETYPE;
	}

	public String getSERVICETYPE(){
		return sERVICETYPE;
	}

	public void setINVOICEFLAG(String iNVOICEFLAG){
		this.iNVOICEFLAG = iNVOICEFLAG;
	}

	public String getINVOICEFLAG(){
		return iNVOICEFLAG;
	}

	public void setREJECTIONMESSAGE(String rEJECTIONMESSAGE){
		this.rEJECTIONMESSAGE = rEJECTIONMESSAGE;
	}

	public String getREJECTIONMESSAGE(){
		return rEJECTIONMESSAGE;
	}

	public void setAAEDOCNUM(String aAEDOCNUM){
		this.aAEDOCNUM = aAEDOCNUM;
	}

	public String getAAEDOCNUM(){
		return aAEDOCNUM;
	}

	public void setAREXMEMCARDFKID(String aREXMEMCARDFKID){
		this.aREXMEMCARDFKID = aREXMEMCARDFKID;
	}

	public String getAREXMEMCARDFKID(){
		return aREXMEMCARDFKID;
	}

	public void setAREXMEMCARDNUM(String aREXMEMCARDNUM){
		this.aREXMEMCARDNUM = aREXMEMCARDNUM;
	}

	public String getAREXMEMCARDNUM(){
		return aREXMEMCARDNUM;
	}

	public void setCURRENTTIME(String cURRENTTIME){
		this.cURRENTTIME = cURRENTTIME;
	}

	public String getCURRENTTIME(){
		return cURRENTTIME;
	}

	public void setCCYDESC(String cCYDESC){
		this.cCYDESC = cCYDESC;
	}

	public String getCCYDESC(){
		return cCYDESC;
	}

	public void setBANKNAME(String bANKNAME){
		this.bANKNAME = bANKNAME;
	}

	public String getBANKNAME(){
		return bANKNAME;
	}

	public void setFCYAMT(String fCYAMT){
		this.fCYAMT = fCYAMT;
	}

	public String getFCYAMT(){
		return fCYAMT;
	}

	public void setAEDAMT(String aEDAMT){
		this.aEDAMT = aEDAMT;
	}

	public String getAEDAMT(){
		return aEDAMT;
	}

	public void setNETTOTAL(String nETTOTAL){
		this.nETTOTAL = nETTOTAL;
	}

	public String getNETTOTAL(){
		return nETTOTAL;
	}

	public void setBENFNAME(String bENFNAME){
		this.bENFNAME = bENFNAME;
	}

	public String getBENFNAME(){
		return bENFNAME;
	}

	public void setBRANCHNAME(String bRANCHNAME){
		this.bRANCHNAME = bRANCHNAME;
	}

	public String getBRANCHNAME(){
		return bRANCHNAME;
	}

	public void setBRANCHCODE(String bRANCHCODE){
		this.bRANCHCODE = bRANCHCODE;
	}

	public String getBRANCHCODE(){
		return bRANCHCODE;
	}

	public void setBENEFMOBILENUM(String bENEFMOBILENUM){
		this.bENEFMOBILENUM = bENEFMOBILENUM;
	}

	public String getBENEFMOBILENUM(){
		return bENEFMOBILENUM;
	}

	public void setPAYOUTSTATE(String pAYOUTSTATE){
		this.pAYOUTSTATE = pAYOUTSTATE;
	}

	public String getPAYOUTSTATE(){
		return pAYOUTSTATE;
	}

	public void setPAYOUTCITY(String pAYOUTCITY){
		this.pAYOUTCITY = pAYOUTCITY;
	}

	public String getPAYOUTCITY(){
		return pAYOUTCITY;
	}

	public void setMTCN(String mTCN){
		this.mTCN = mTCN;
	}

	public String getMTCN(){
		return mTCN;
	}

	public void setBENEFACCNUM(String bENEFACCNUM){
		this.bENEFACCNUM = bENEFACCNUM;
	}

	public String getBENEFACCNUM(){
		return bENEFACCNUM;
	}

	public void setPAYMENTMODE(String pAYMENTMODE){
		this.pAYMENTMODE = pAYMENTMODE;
	}

	public String getPAYMENTMODE(){
		return pAYMENTMODE;
	}

	public void setBENEFBANK(String bENEFBANK){
		this.bENEFBANK = bENEFBANK;
	}

	public String getBENEFBANK(){
		return bENEFBANK;
	}

	public void setBENEFBRANCH(String bENEFBRANCH){
		this.bENEFBRANCH = bENEFBRANCH;
	}

	public String getBENEFBRANCH(){
		return bENEFBRANCH;
	}

	public void setCEMEMCARDNUM(String cEMEMCARDNUM){
		this.cEMEMCARDNUM = cEMEMCARDNUM;
	}

	public String getCEMEMCARDNUM(){
		return cEMEMCARDNUM;
	}

	public void setCEMEMCARDFKID(String cEMEMCARDFKID){
		this.cEMEMCARDFKID = cEMEMCARDFKID;
	}

	public String getCEMEMCARDFKID(){
		return cEMEMCARDFKID;
	}

	public void setSOURCEAGENTNAME(String sOURCEAGENTNAME){
		this.sOURCEAGENTNAME = sOURCEAGENTNAME;
	}

	public String getSOURCEAGENTNAME(){
		return sOURCEAGENTNAME;
	}

	public void setCARDHOLDERNAME(String cARDHOLDERNAME){
		this.cARDHOLDERNAME = cARDHOLDERNAME;
	}

	public String getCARDHOLDERNAME(){
		return cARDHOLDERNAME;
	}

	public void setCARDTYPE(String cARDTYPE){
		this.cARDTYPE = cARDTYPE;
	}

	public String getCARDTYPE(){
		return cARDTYPE;
	}

	public void setCCYCODE(String cCYCODE){
		this.cCYCODE = cCYCODE;
	}

	public String getCCYCODE(){
		return cCYCODE;
	}

	public void setCARDNUMBER(String cARDNUMBER){
		this.cARDNUMBER = cARDNUMBER;
	}

	public String getCARDNUMBER(){
		return cARDNUMBER;
	}

	public void setCREATEDDATESTR(String cREATEDDATESTR){
		this.cREATEDDATESTR = cREATEDDATESTR;
	}

	public String getCREATEDDATESTR(){
		return cREATEDDATESTR;
	}

	@Override
 	public String toString(){
		return
			"TXNHISTORYDATAItem{" +
			"rEM_TXN_PK_ID = '" + rEMTXNPKID + '\'' +
			",rEM_TXN_REF_NUM = '" + rEMTXNREFNUM + '\'' +
			",gTW_USER_MSTR_FK_ID = '" + gTWUSERMSTRFKID + '\'' +
			",tXN_STATUS = '" + tXNSTATUS + '\'' +
			",tXN_CODE = '" + tXNCODE + '\'' +
			",cHANNEL = '" + cHANNEL + '\'' +
			",tOTAL_TXN_AMT = '" + tOTALTXNAMT + '\'' +
			",tXN_TYPE = '" + tXNTYPE + '\'' +
			",tXN_REC_TYPE = '" + tXNRECTYPE + '\'' +
			",tXN_PAY_TYPE = '" + tXNPAYTYPE + '\'' +
			",tXN_EXP_LIMIT = '" + tXNEXPLIMIT + '\'' +
			",tXN_EXP_DESC = '" + tXNEXPDESC + '\'' +
			",cREATED_DATE = '" + cREATEDDATE + '\'' +
			",bENEFICIARY_ID = '" + bENEFICIARYID + '\'' +
			",rATE = '" + rATE + '\'' +
			",cOST_PRICE = '" + cOSTPRICE + '\'' +
			",tOTAL_VALUE_AED = '" + tOTALVALUEAED + '\'' +
			",sERVICE_TYPE = '" + sERVICETYPE + '\'' +
			",iNVOICE_FLAG = '" + iNVOICEFLAG + '\'' +
			",rEJECTION_MESSAGE = '" + rEJECTIONMESSAGE + '\'' +
			",aAE_DOC_NUM = '" + aAEDOCNUM + '\'' +
			",aREX_MEM_CARD_FK_ID = '" + aREXMEMCARDFKID + '\'' +
			",aREX_MEM_CARD_NUM = '" + aREXMEMCARDNUM + '\'' +
			",cURRENT_TIME = '" + cURRENTTIME + '\'' +
			",cCY_DESC = '" + cCYDESC + '\'' +
			",bANK_NAME = '" + bANKNAME + '\'' +
			",fCY_AMT = '" + fCYAMT + '\'' +
			",aED_AMT = '" + aEDAMT + '\'' +
			",nET_TOTAL = '" + nETTOTAL + '\'' +
			",bENF_NAME = '" + bENFNAME + '\'' +
			",bRANCH_NAME = '" + bRANCHNAME + '\'' +
			",bRANCH_CODE = '" + bRANCHCODE + '\'' +
			",bENEF_MOBILE_NUM = '" + bENEFMOBILENUM + '\'' +
			",pAYOUT_STATE = '" + pAYOUTSTATE + '\'' +
			",pAYOUT_CITY = '" + pAYOUTCITY + '\'' +
			",mTCN = '" + mTCN + '\'' +
			",bENEF_ACC_NUM = '" + bENEFACCNUM + '\'' +
			",pAYMENT_MODE = '" + pAYMENTMODE + '\'' +
			",bENEF_BANK = '" + bENEFBANK + '\'' +
			",bENEF_BRANCH = '" + bENEFBRANCH + '\'' +
			",cE_MEM_CARD_NUM = '" + cEMEMCARDNUM + '\'' +
			",cE_MEM_CARD_FK_ID = '" + cEMEMCARDFKID + '\'' +
			",sOURCE_AGENT_NAME = '" + sOURCEAGENTNAME + '\'' +
			",cARD_HOLDER_NAME = '" + cARDHOLDERNAME + '\'' +
			",cARD_TYPE = '" + cARDTYPE + '\'' +
			",cCY_CODE = '" + cCYCODE + '\'' +
			",cARD_NUMBER = '" + cARDNUMBER + '\'' +
			",cREATED_DATE_STR = '" + cREATEDDATESTR + '\'' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(rEMTXNPKID);
		dest.writeString(rEMTXNREFNUM);
		dest.writeString(tXNSTATUS);
		dest.writeString(tXNCODE);
		dest.writeString(cHANNEL);
		dest.writeString(tOTALTXNAMT);
		dest.writeString(tXNTYPE);
		dest.writeString(tXNRECTYPE);
		dest.writeString(tXNPAYTYPE);
		dest.writeString(cREATEDDATE);
		dest.writeString(tOTALVALUEAED);
		dest.writeString(sERVICETYPE);
		dest.writeString(iNVOICEFLAG);
		dest.writeString(aAEDOCNUM);
		dest.writeString(aREXMEMCARDFKID);
		dest.writeString(aREXMEMCARDNUM);
		dest.writeString(cURRENTTIME);
		dest.writeString(cCYDESC);
		dest.writeString(nETTOTAL);
		dest.writeString(bENFNAME);
		dest.writeString(bENEFACCNUM);
		dest.writeString(pAYMENTMODE);
		dest.writeString(bENEFBANK);
		dest.writeString(bENEFBRANCH);
		dest.writeString(cREATEDDATESTR);
		dest.writeString(rEJECTIONMESSAGE);
		dest.writeString(fCYAMT);
		dest.writeString(bENEFMOBILENUM);
	}
}