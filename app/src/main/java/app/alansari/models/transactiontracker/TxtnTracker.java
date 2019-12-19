package app.alansari.models.transactiontracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class TxtnTracker implements Parcelable {

	@SerializedName("SESSION_TIMER")
	private String sESSIONTIMER;

	@SerializedName("OTP_TIMER")
	private String oTPTIMER;

	@SerializedName("SESSION_TIMER_MSG")
	private String sESSIONTIMERMSG;

	@SerializedName("OTP_TIMER_MSG")
	private String oTPTIMERMSG;

	@SerializedName("PROFILE_UPDATE_FLAG")
	private String pROFILEUPDATEFLAG;

	@SerializedName("TXN_NUMBER_OF_STEPS")
	private int tXNNUMBEROFSTEPS;

	@SerializedName("RESULT")
	private List<TrackerResult> rESULT;

	@SerializedName("STATUS_MSG")
	private String sTATUSMSG;

	@SerializedName("MESSAGE")
	private String mESSAGE;

	@SerializedName("OTP")
	private String oTP;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("AMOUNT")
	private String aMOUNT;

	@SerializedName("USER_STATUS")
	private String uSERSTATUS;

	@SerializedName("SESSION_ID")
	private String sESSIONID;

	@SerializedName("STATUS_CODE")
	private int sTATUSCODE;

	@SerializedName("PROMO_ACTIVE")
	private String pROMOACTIVE;

	@SerializedName("PROMO_LIST_ACTIVE")
	private String pROMOLISTACTIVE;

	@SerializedName("BENEF_PIC_FILE_EXTN")
	private String bENEFPICFILEEXTN;

	@SerializedName("BENEF_PIC_FILE_SIZE")
	private String bENEFPICFILESIZE;

	@SerializedName("BENEF_PIC_COMP_LIMIT")
	private String bENEFPICCOMPLIMIT;

	@SerializedName("BENEF_PIC_ENC_ENABLED")
	private String bENEFPICENCENABLED;

	@SerializedName("NEXT_RECORD")
	private int nEXTRECORD;

	@SerializedName("WU_CARD_NO")
	private String wUCARDNO;

	@SerializedName("WU_SENDER_NAME")
	private String wUSENDERNAME;

	@SerializedName("WU_LOOKUP_PROMO_CODE")
	private String wULOOKUPPROMOCODE;

	@SerializedName("WU_CARD_NEXT_RECEIVER_CONTEXT")
	private String wUCARDNEXTRECEIVERCONTEXT;

	@SerializedName("WU_TOTAL_POINTS_EARNED")
	private String wUTOTALPOINTSEARNED;

	@SerializedName("WU_RECEIVER_MORE_FLAG")
	private String wURECEIVERMOREFLAG;

	@SerializedName("SCREEN_MSG")
	private String sCREENMSG;

	@SerializedName("ID")
	private String iD;

	protected TxtnTracker(Parcel in) {
		tXNNUMBEROFSTEPS = in.readInt();
		rESULT = in.createTypedArrayList(TrackerResult.CREATOR);
		sTATUSMSG = in.readString();
		sTATUSCODE = in.readInt();
		nEXTRECORD = in.readInt();
	}

	public static final Creator<TxtnTracker> CREATOR = new Creator<TxtnTracker>() {
		@Override
		public TxtnTracker createFromParcel(Parcel in) {
			return new TxtnTracker(in);
		}

		@Override
		public TxtnTracker[] newArray(int size) {
			return new TxtnTracker[size];
		}
	};

	public void setSESSIONTIMER(String sESSIONTIMER){
		this.sESSIONTIMER = sESSIONTIMER;
	}

	public String getSESSIONTIMER(){
		return sESSIONTIMER;
	}

	public void setOTPTIMER(String oTPTIMER){
		this.oTPTIMER = oTPTIMER;
	}

	public String getOTPTIMER(){
		return oTPTIMER;
	}

	public void setSESSIONTIMERMSG(String sESSIONTIMERMSG){
		this.sESSIONTIMERMSG = sESSIONTIMERMSG;
	}

	public String getSESSIONTIMERMSG(){
		return sESSIONTIMERMSG;
	}

	public void setOTPTIMERMSG(String oTPTIMERMSG){
		this.oTPTIMERMSG = oTPTIMERMSG;
	}

	public String getOTPTIMERMSG(){
		return oTPTIMERMSG;
	}

	public void setPROFILEUPDATEFLAG(String pROFILEUPDATEFLAG){
		this.pROFILEUPDATEFLAG = pROFILEUPDATEFLAG;
	}

	public String getPROFILEUPDATEFLAG(){
		return pROFILEUPDATEFLAG;
	}

	public void setTXNNUMBEROFSTEPS(int tXNNUMBEROFSTEPS){
		this.tXNNUMBEROFSTEPS = tXNNUMBEROFSTEPS;
	}

	public int getTXNNUMBEROFSTEPS(){
		return tXNNUMBEROFSTEPS;
	}

	public void setRESULT(List<TrackerResult> rESULT){
		this.rESULT = rESULT;
	}

	public List<TrackerResult> getRESULT(){
		return rESULT;
	}

	public void setSTATUSMSG(String sTATUSMSG){
		this.sTATUSMSG = sTATUSMSG;
	}

	public String getSTATUSMSG(){
		return sTATUSMSG;
	}

	public void setMESSAGE(String mESSAGE){
		this.mESSAGE = mESSAGE;
	}

	public String getMESSAGE(){
		return mESSAGE;
	}

	public void setOTP(String oTP){
		this.oTP = oTP;
	}

	public String getOTP(){
		return oTP;
	}

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setAMOUNT(String aMOUNT){
		this.aMOUNT = aMOUNT;
	}

	public String getAMOUNT(){
		return aMOUNT;
	}

	public void setUSERSTATUS(String uSERSTATUS){
		this.uSERSTATUS = uSERSTATUS;
	}

	public String getUSERSTATUS(){
		return uSERSTATUS;
	}

	public void setSESSIONID(String sESSIONID){
		this.sESSIONID = sESSIONID;
	}

	public String getSESSIONID(){
		return sESSIONID;
	}

	public void setSTATUSCODE(int sTATUSCODE){
		this.sTATUSCODE = sTATUSCODE;
	}

	public int getSTATUSCODE(){
		return sTATUSCODE;
	}

	public void setPROMOACTIVE(String pROMOACTIVE){
		this.pROMOACTIVE = pROMOACTIVE;
	}

	public String getPROMOACTIVE(){
		return pROMOACTIVE;
	}

	public void setPROMOLISTACTIVE(String pROMOLISTACTIVE){
		this.pROMOLISTACTIVE = pROMOLISTACTIVE;
	}

	public String getPROMOLISTACTIVE(){
		return pROMOLISTACTIVE;
	}

	public void setBENEFPICFILEEXTN(String bENEFPICFILEEXTN){
		this.bENEFPICFILEEXTN = bENEFPICFILEEXTN;
	}

	public String getBENEFPICFILEEXTN(){
		return bENEFPICFILEEXTN;
	}

	public void setBENEFPICFILESIZE(String bENEFPICFILESIZE){
		this.bENEFPICFILESIZE = bENEFPICFILESIZE;
	}

	public String getBENEFPICFILESIZE(){
		return bENEFPICFILESIZE;
	}

	public void setBENEFPICCOMPLIMIT(String bENEFPICCOMPLIMIT){
		this.bENEFPICCOMPLIMIT = bENEFPICCOMPLIMIT;
	}

	public String getBENEFPICCOMPLIMIT(){
		return bENEFPICCOMPLIMIT;
	}

	public void setBENEFPICENCENABLED(String bENEFPICENCENABLED){
		this.bENEFPICENCENABLED = bENEFPICENCENABLED;
	}

	public String getBENEFPICENCENABLED(){
		return bENEFPICENCENABLED;
	}

	public void setNEXTRECORD(int nEXTRECORD){
		this.nEXTRECORD = nEXTRECORD;
	}

	public int getNEXTRECORD(){
		return nEXTRECORD;
	}

	public void setWUCARDNO(String wUCARDNO){
		this.wUCARDNO = wUCARDNO;
	}

	public String getWUCARDNO(){
		return wUCARDNO;
	}

	public void setWUSENDERNAME(String wUSENDERNAME){
		this.wUSENDERNAME = wUSENDERNAME;
	}

	public String getWUSENDERNAME(){
		return wUSENDERNAME;
	}

	public void setWULOOKUPPROMOCODE(String wULOOKUPPROMOCODE){
		this.wULOOKUPPROMOCODE = wULOOKUPPROMOCODE;
	}

	public String getWULOOKUPPROMOCODE(){
		return wULOOKUPPROMOCODE;
	}

	public void setWUCARDNEXTRECEIVERCONTEXT(String wUCARDNEXTRECEIVERCONTEXT){
		this.wUCARDNEXTRECEIVERCONTEXT = wUCARDNEXTRECEIVERCONTEXT;
	}

	public String getWUCARDNEXTRECEIVERCONTEXT(){
		return wUCARDNEXTRECEIVERCONTEXT;
	}

	public void setWUTOTALPOINTSEARNED(String wUTOTALPOINTSEARNED){
		this.wUTOTALPOINTSEARNED = wUTOTALPOINTSEARNED;
	}

	public String getWUTOTALPOINTSEARNED(){
		return wUTOTALPOINTSEARNED;
	}

	public void setWURECEIVERMOREFLAG(String wURECEIVERMOREFLAG){
		this.wURECEIVERMOREFLAG = wURECEIVERMOREFLAG;
	}

	public String getWURECEIVERMOREFLAG(){
		return wURECEIVERMOREFLAG;
	}

	public void setSCREENMSG(String sCREENMSG){
		this.sCREENMSG = sCREENMSG;
	}

	public String getSCREENMSG(){
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
			"TxtnTracker{" + 
			"sESSION_TIMER = '" + sESSIONTIMER + '\'' + 
			",oTP_TIMER = '" + oTPTIMER + '\'' + 
			",sESSION_TIMER_MSG = '" + sESSIONTIMERMSG + '\'' + 
			",oTP_TIMER_MSG = '" + oTPTIMERMSG + '\'' + 
			",pROFILE_UPDATE_FLAG = '" + pROFILEUPDATEFLAG + '\'' + 
			",tXN_NUMBER_OF_STEPS = '" + tXNNUMBEROFSTEPS + '\'' + 
			",rESULT = '" + rESULT + '\'' + 
			",sTATUS_MSG = '" + sTATUSMSG + '\'' + 
			",mESSAGE = '" + mESSAGE + '\'' + 
			",oTP = '" + oTP + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",aMOUNT = '" + aMOUNT + '\'' + 
			",uSER_STATUS = '" + uSERSTATUS + '\'' + 
			",sESSION_ID = '" + sESSIONID + '\'' + 
			",sTATUS_CODE = '" + sTATUSCODE + '\'' + 
			",pROMO_ACTIVE = '" + pROMOACTIVE + '\'' + 
			",pROMO_LIST_ACTIVE = '" + pROMOLISTACTIVE + '\'' + 
			",bENEF_PIC_FILE_EXTN = '" + bENEFPICFILEEXTN + '\'' + 
			",bENEF_PIC_FILE_SIZE = '" + bENEFPICFILESIZE + '\'' + 
			",bENEF_PIC_COMP_LIMIT = '" + bENEFPICCOMPLIMIT + '\'' + 
			",bENEF_PIC_ENC_ENABLED = '" + bENEFPICENCENABLED + '\'' + 
			",nEXT_RECORD = '" + nEXTRECORD + '\'' + 
			",wU_CARD_NO = '" + wUCARDNO + '\'' + 
			",wU_SENDER_NAME = '" + wUSENDERNAME + '\'' + 
			",wU_LOOKUP_PROMO_CODE = '" + wULOOKUPPROMOCODE + '\'' + 
			",wU_CARD_NEXT_RECEIVER_CONTEXT = '" + wUCARDNEXTRECEIVERCONTEXT + '\'' + 
			",wU_TOTAL_POINTS_EARNED = '" + wUTOTALPOINTSEARNED + '\'' + 
			",wU_RECEIVER_MORE_FLAG = '" + wURECEIVERMOREFLAG + '\'' + 
			",sCREEN_MSG = '" + sCREENMSG + '\'' + 
			",iD = '" + iD + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tXNNUMBEROFSTEPS);
		dest.writeTypedList(rESULT);
		dest.writeString(sTATUSMSG);
		dest.writeInt(sTATUSCODE);
		dest.writeInt(nEXTRECORD);
	}
}