package app.alansari.modules.remittance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class TxnRemittanceModel implements Parcelable {

	@SerializedName("SESSION_TIMER")
	private Object sESSIONTIMER;

	@SerializedName("OTP_TIMER")
	private Object oTPTIMER;

	@SerializedName("SESSION_TIMER_MSG")
	private Object sESSIONTIMERMSG;

	@SerializedName("OTP_TIMER_MSG")
	private Object oTPTIMERMSG;

	@SerializedName("PROFILE_UPDATE_FLAG")
	private Object pROFILEUPDATEFLAG;

	@SerializedName("TXN_NUMBER_OF_STEPS")
	private int tXNNUMBEROFSTEPS;

	@SerializedName("RESULT")
	private List<RESULTItem> rESULT;

	@SerializedName("STATUS_MSG")
	private String sTATUSMSG;

	@SerializedName("BENEF_PIC_FILE_EXTN")
	private Object bENEFPICFILEEXTN;

	@SerializedName("BENEF_PIC_FILE_SIZE")
	private Object bENEFPICFILESIZE;

	@SerializedName("BENEF_PIC_COMP_LIMIT")
	private Object bENEFPICCOMPLIMIT;

	@SerializedName("BENEF_PIC_ENC_ENABLED")
	private Object bENEFPICENCENABLED;

	@SerializedName("STATUS")
	private Object sTATUS;

	@SerializedName("MESSAGE")
	private String mESSAGE;

	@SerializedName("USER_STATUS")
	private Object uSERSTATUS;

	@SerializedName("OTP")
	private Object oTP;

	@SerializedName("AMOUNT")
	private Object aMOUNT;

	@SerializedName("STATUS_CODE")
	private int sTATUSCODE;

	@SerializedName("PROMO_ACTIVE")
	private Object pROMOACTIVE;

	@SerializedName("PROMO_LIST_ACTIVE")
	private Object pROMOLISTACTIVE;

	@SerializedName("NEXT_RECORD")
	private int nEXTRECORD;

	@SerializedName("WU_CARD_NO")
	private Object wUCARDNO;

	@SerializedName("WU_SENDER_NAME")
	private Object wUSENDERNAME;

	@SerializedName("WU_LOOKUP_PROMO_CODE")
	private Object wULOOKUPPROMOCODE;

	@SerializedName("WU_CARD_NEXT_RECEIVER_CONTEXT")
	private Object wUCARDNEXTRECEIVERCONTEXT;

	@SerializedName("WU_TOTAL_POINTS_EARNED")
	private Object wUTOTALPOINTSEARNED;

	@SerializedName("WU_RECEIVER_MORE_FLAG")
	private Object wURECEIVERMOREFLAG;

	@SerializedName("SESSION_ID")
	private Object sESSIONID;

	@SerializedName("SCREEN_MSG")
	private Object sCREENMSG;

	@SerializedName("ID")
	private String iD;

	protected TxnRemittanceModel(Parcel in) {
		tXNNUMBEROFSTEPS = in.readInt();
		sTATUSMSG = in.readString();
		mESSAGE = in.readString();
		sTATUSCODE = in.readInt();
		nEXTRECORD = in.readInt();
		iD = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tXNNUMBEROFSTEPS);
		dest.writeString(sTATUSMSG);
		dest.writeString(mESSAGE);
		dest.writeInt(sTATUSCODE);
		dest.writeInt(nEXTRECORD);
		dest.writeString(iD);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<TxnRemittanceModel> CREATOR = new Creator<TxnRemittanceModel>() {
		@Override
		public TxnRemittanceModel createFromParcel(Parcel in) {
			return new TxnRemittanceModel(in);
		}

		@Override
		public TxnRemittanceModel[] newArray(int size) {
			return new TxnRemittanceModel[size];
		}
	};

	public void setSESSIONTIMER(Object sESSIONTIMER){
		this.sESSIONTIMER = sESSIONTIMER;
	}

	public Object getSESSIONTIMER(){
		return sESSIONTIMER;
	}

	public void setOTPTIMER(Object oTPTIMER){
		this.oTPTIMER = oTPTIMER;
	}

	public Object getOTPTIMER(){
		return oTPTIMER;
	}

	public void setSESSIONTIMERMSG(Object sESSIONTIMERMSG){
		this.sESSIONTIMERMSG = sESSIONTIMERMSG;
	}

	public Object getSESSIONTIMERMSG(){
		return sESSIONTIMERMSG;
	}

	public void setOTPTIMERMSG(Object oTPTIMERMSG){
		this.oTPTIMERMSG = oTPTIMERMSG;
	}

	public Object getOTPTIMERMSG(){
		return oTPTIMERMSG;
	}

	public void setPROFILEUPDATEFLAG(Object pROFILEUPDATEFLAG){
		this.pROFILEUPDATEFLAG = pROFILEUPDATEFLAG;
	}

	public Object getPROFILEUPDATEFLAG(){
		return pROFILEUPDATEFLAG;
	}

	public void setTXNNUMBEROFSTEPS(int tXNNUMBEROFSTEPS){
		this.tXNNUMBEROFSTEPS = tXNNUMBEROFSTEPS;
	}

	public int getTXNNUMBEROFSTEPS(){
		return tXNNUMBEROFSTEPS;
	}

	public void setRESULT(List<RESULTItem> rESULT){
		this.rESULT = rESULT;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	public void setSTATUSMSG(String sTATUSMSG){
		this.sTATUSMSG = sTATUSMSG;
	}

	public String getSTATUSMSG(){
		return sTATUSMSG;
	}

	public void setBENEFPICFILEEXTN(Object bENEFPICFILEEXTN){
		this.bENEFPICFILEEXTN = bENEFPICFILEEXTN;
	}

	public Object getBENEFPICFILEEXTN(){
		return bENEFPICFILEEXTN;
	}

	public void setBENEFPICFILESIZE(Object bENEFPICFILESIZE){
		this.bENEFPICFILESIZE = bENEFPICFILESIZE;
	}

	public Object getBENEFPICFILESIZE(){
		return bENEFPICFILESIZE;
	}

	public void setBENEFPICCOMPLIMIT(Object bENEFPICCOMPLIMIT){
		this.bENEFPICCOMPLIMIT = bENEFPICCOMPLIMIT;
	}

	public Object getBENEFPICCOMPLIMIT(){
		return bENEFPICCOMPLIMIT;
	}

	public void setBENEFPICENCENABLED(Object bENEFPICENCENABLED){
		this.bENEFPICENCENABLED = bENEFPICENCENABLED;
	}

	public Object getBENEFPICENCENABLED(){
		return bENEFPICENCENABLED;
	}

	public void setSTATUS(Object sTATUS){
		this.sTATUS = sTATUS;
	}

	public Object getSTATUS(){
		return sTATUS;
	}

	public void setMESSAGE(String mESSAGE){
		this.mESSAGE = mESSAGE;
	}

	public String getMESSAGE(){
		return mESSAGE;
	}

	public void setUSERSTATUS(Object uSERSTATUS){
		this.uSERSTATUS = uSERSTATUS;
	}

	public Object getUSERSTATUS(){
		return uSERSTATUS;
	}

	public void setOTP(Object oTP){
		this.oTP = oTP;
	}

	public Object getOTP(){
		return oTP;
	}

	public void setAMOUNT(Object aMOUNT){
		this.aMOUNT = aMOUNT;
	}

	public Object getAMOUNT(){
		return aMOUNT;
	}

	public void setSTATUSCODE(int sTATUSCODE){
		this.sTATUSCODE = sTATUSCODE;
	}

	public int getSTATUSCODE(){
		return sTATUSCODE;
	}

	public void setPROMOACTIVE(Object pROMOACTIVE){
		this.pROMOACTIVE = pROMOACTIVE;
	}

	public Object getPROMOACTIVE(){
		return pROMOACTIVE;
	}

	public void setPROMOLISTACTIVE(Object pROMOLISTACTIVE){
		this.pROMOLISTACTIVE = pROMOLISTACTIVE;
	}

	public Object getPROMOLISTACTIVE(){
		return pROMOLISTACTIVE;
	}

	public void setNEXTRECORD(int nEXTRECORD){
		this.nEXTRECORD = nEXTRECORD;
	}

	public int getNEXTRECORD(){
		return nEXTRECORD;
	}

	public void setWUCARDNO(Object wUCARDNO){
		this.wUCARDNO = wUCARDNO;
	}

	public Object getWUCARDNO(){
		return wUCARDNO;
	}

	public void setWUSENDERNAME(Object wUSENDERNAME){
		this.wUSENDERNAME = wUSENDERNAME;
	}

	public Object getWUSENDERNAME(){
		return wUSENDERNAME;
	}

	public void setWULOOKUPPROMOCODE(Object wULOOKUPPROMOCODE){
		this.wULOOKUPPROMOCODE = wULOOKUPPROMOCODE;
	}

	public Object getWULOOKUPPROMOCODE(){
		return wULOOKUPPROMOCODE;
	}

	public void setWUCARDNEXTRECEIVERCONTEXT(Object wUCARDNEXTRECEIVERCONTEXT){
		this.wUCARDNEXTRECEIVERCONTEXT = wUCARDNEXTRECEIVERCONTEXT;
	}

	public Object getWUCARDNEXTRECEIVERCONTEXT(){
		return wUCARDNEXTRECEIVERCONTEXT;
	}

	public void setWUTOTALPOINTSEARNED(Object wUTOTALPOINTSEARNED){
		this.wUTOTALPOINTSEARNED = wUTOTALPOINTSEARNED;
	}

	public Object getWUTOTALPOINTSEARNED(){
		return wUTOTALPOINTSEARNED;
	}

	public void setWURECEIVERMOREFLAG(Object wURECEIVERMOREFLAG){
		this.wURECEIVERMOREFLAG = wURECEIVERMOREFLAG;
	}

	public Object getWURECEIVERMOREFLAG(){
		return wURECEIVERMOREFLAG;
	}

	public void setSESSIONID(Object sESSIONID){
		this.sESSIONID = sESSIONID;
	}

	public Object getSESSIONID(){
		return sESSIONID;
	}

	public void setSCREENMSG(Object sCREENMSG){
		this.sCREENMSG = sCREENMSG;
	}

	public Object getSCREENMSG(){
		return sCREENMSG;
	}

	public void setID(String iD){
		this.iD = iD;
	}

	public String getID(){
		return iD;
	}

	@Override
 	public String toString(){
		return 
			"TxnRemittanceModel{" + 
			"sESSION_TIMER = '" + sESSIONTIMER + '\'' + 
			",oTP_TIMER = '" + oTPTIMER + '\'' + 
			",sESSION_TIMER_MSG = '" + sESSIONTIMERMSG + '\'' + 
			",oTP_TIMER_MSG = '" + oTPTIMERMSG + '\'' + 
			",pROFILE_UPDATE_FLAG = '" + pROFILEUPDATEFLAG + '\'' + 
			",tXN_NUMBER_OF_STEPS = '" + tXNNUMBEROFSTEPS + '\'' + 
			",rESULT = '" + rESULT + '\'' + 
			",sTATUS_MSG = '" + sTATUSMSG + '\'' + 
			",bENEF_PIC_FILE_EXTN = '" + bENEFPICFILEEXTN + '\'' + 
			",bENEF_PIC_FILE_SIZE = '" + bENEFPICFILESIZE + '\'' + 
			",bENEF_PIC_COMP_LIMIT = '" + bENEFPICCOMPLIMIT + '\'' + 
			",bENEF_PIC_ENC_ENABLED = '" + bENEFPICENCENABLED + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",mESSAGE = '" + mESSAGE + '\'' + 
			",uSER_STATUS = '" + uSERSTATUS + '\'' + 
			",oTP = '" + oTP + '\'' + 
			",aMOUNT = '" + aMOUNT + '\'' + 
			",sTATUS_CODE = '" + sTATUSCODE + '\'' + 
			",pROMO_ACTIVE = '" + pROMOACTIVE + '\'' + 
			",pROMO_LIST_ACTIVE = '" + pROMOLISTACTIVE + '\'' + 
			",nEXT_RECORD = '" + nEXTRECORD + '\'' + 
			",wU_CARD_NO = '" + wUCARDNO + '\'' + 
			",wU_SENDER_NAME = '" + wUSENDERNAME + '\'' + 
			",wU_LOOKUP_PROMO_CODE = '" + wULOOKUPPROMOCODE + '\'' + 
			",wU_CARD_NEXT_RECEIVER_CONTEXT = '" + wUCARDNEXTRECEIVERCONTEXT + '\'' + 
			",wU_TOTAL_POINTS_EARNED = '" + wUTOTALPOINTSEARNED + '\'' + 
			",wU_RECEIVER_MORE_FLAG = '" + wURECEIVERMOREFLAG + '\'' + 
			",sESSION_ID = '" + sESSIONID + '\'' + 
			",sCREEN_MSG = '" + sCREENMSG + '\'' + 
			",iD = '" + iD + '\'' + 
			"}";
		}
}