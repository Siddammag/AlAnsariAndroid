package app.alansari.models.TravelCardReloadModel;




import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TravelCardModel implements Parcelable {

	@SerializedName("USER_STATUS")
	private Object uSERSTATUS;

	@SerializedName("PROMO_LIST_ACTIVE")
	private Object pROMOLISTACTIVE;

	@SerializedName("OTP")
	private Object oTP;

	@SerializedName("SESSION_TIMER")
	private Object sESSIONTIMER;

	@SerializedName("WU_RECEIVER_MORE_FLAG")
	private Object wURECEIVERMOREFLAG;

	@SerializedName("MESSAGE")
	private String mESSAGE;

	@SerializedName("STATUS_MSG")
	private String sTATUSMSG;

	@SerializedName("STATUS")
	private Object sTATUS;

	@SerializedName("AMOUNT")
	private Object aMOUNT;

	@SerializedName("SESSION_TIMER_MSG")
	private Object sESSIONTIMERMSG;

	@SerializedName("OTP_TIMER_MSG")
	private Object oTPTIMERMSG;

	@SerializedName("BENEF_PIC_COMP_LIMIT")
	private Object bENEFPICCOMPLIMIT;

	@SerializedName("WU_CARD_NEXT_RECEIVER_CONTEXT")
	private Object wUCARDNEXTRECEIVERCONTEXT;

	@SerializedName("ID")
	private Object iD;

	@SerializedName("OTP_TIMER")
	private Object oTPTIMER;

	@SerializedName("WU_CARD_NO")
	private Object wUCARDNO;

	@SerializedName("PROFILE_UPDATE_FLAG")
	private Object pROFILEUPDATEFLAG;

	@SerializedName("PROMO_ACTIVE")
	private Object pROMOACTIVE;

	@SerializedName("BENEF_PIC_FILE_SIZE")
	private Object bENEFPICFILESIZE;

	@SerializedName("BENEF_PIC_ENC_ENABLED")
	private Object bENEFPICENCENABLED;

	@SerializedName("STATUS_CODE")
	private int sTATUSCODE;

	@SerializedName("NEXT_RECORD")
	private int nEXTRECORD;

	@SerializedName("WU_SENDER_NAME")
	private Object wUSENDERNAME;

	@SerializedName("BENEF_PIC_FILE_EXTN")
	private Object bENEFPICFILEEXTN;

	@SerializedName("SESSION_ID")
	private Object sESSIONID;

	@SerializedName("RESULT")
	private List<TravelCardInfo> rESULT;

	@SerializedName("WU_TOTAL_POINTS_EARNED")
	private Object wUTOTALPOINTSEARNED;

	@SerializedName("WU_LOOKUP_PROMO_CODE")
	private Object wULOOKUPPROMOCODE;


	protected TravelCardModel(Parcel in) {
		mESSAGE = in.readString();
		sTATUSMSG = in.readString();
		sTATUSCODE = in.readInt();
		nEXTRECORD = in.readInt();
		rESULT = in.createTypedArrayList(TravelCardInfo.CREATOR);
	}

	public static final Creator<TravelCardModel> CREATOR = new Creator<TravelCardModel>() {
		@Override
		public TravelCardModel createFromParcel(Parcel in) {
			return new TravelCardModel(in);
		}

		@Override
		public TravelCardModel[] newArray(int size) {
			return new TravelCardModel[size];
		}
	};

	public void setUSERSTATUS(Object uSERSTATUS){
		this.uSERSTATUS = uSERSTATUS;
	}

	public Object getUSERSTATUS(){
		return uSERSTATUS;
	}

	public void setPROMOLISTACTIVE(Object pROMOLISTACTIVE){
		this.pROMOLISTACTIVE = pROMOLISTACTIVE;
	}

	public Object getPROMOLISTACTIVE(){
		return pROMOLISTACTIVE;
	}

	public void setOTP(Object oTP){
		this.oTP = oTP;
	}

	public Object getOTP(){
		return oTP;
	}

	public void setSESSIONTIMER(Object sESSIONTIMER){
		this.sESSIONTIMER = sESSIONTIMER;
	}

	public Object getSESSIONTIMER(){
		return sESSIONTIMER;
	}

	public void setWURECEIVERMOREFLAG(Object wURECEIVERMOREFLAG){
		this.wURECEIVERMOREFLAG = wURECEIVERMOREFLAG;
	}

	public Object getWURECEIVERMOREFLAG(){
		return wURECEIVERMOREFLAG;
	}

	public void setMESSAGE(String mESSAGE){
		this.mESSAGE = mESSAGE;
	}

	public String getMESSAGE(){
		return mESSAGE;
	}

	public void setSTATUSMSG(String sTATUSMSG){
		this.sTATUSMSG = sTATUSMSG;
	}

	public String getSTATUSMSG(){
		return sTATUSMSG;
	}

	public void setSTATUS(Object sTATUS){
		this.sTATUS = sTATUS;
	}

	public Object getSTATUS(){
		return sTATUS;
	}

	public void setAMOUNT(Object aMOUNT){
		this.aMOUNT = aMOUNT;
	}

	public Object getAMOUNT(){
		return aMOUNT;
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

	public void setBENEFPICCOMPLIMIT(Object bENEFPICCOMPLIMIT){
		this.bENEFPICCOMPLIMIT = bENEFPICCOMPLIMIT;
	}

	public Object getBENEFPICCOMPLIMIT(){
		return bENEFPICCOMPLIMIT;
	}

	public void setWUCARDNEXTRECEIVERCONTEXT(Object wUCARDNEXTRECEIVERCONTEXT){
		this.wUCARDNEXTRECEIVERCONTEXT = wUCARDNEXTRECEIVERCONTEXT;
	}

	public Object getWUCARDNEXTRECEIVERCONTEXT(){
		return wUCARDNEXTRECEIVERCONTEXT;
	}

	public void setID(Object iD){
		this.iD = iD;
	}

	public Object getID(){
		return iD;
	}

	public void setOTPTIMER(Object oTPTIMER){
		this.oTPTIMER = oTPTIMER;
	}

	public Object getOTPTIMER(){
		return oTPTIMER;
	}

	public void setWUCARDNO(Object wUCARDNO){
		this.wUCARDNO = wUCARDNO;
	}

	public Object getWUCARDNO(){
		return wUCARDNO;
	}

	public void setPROFILEUPDATEFLAG(Object pROFILEUPDATEFLAG){
		this.pROFILEUPDATEFLAG = pROFILEUPDATEFLAG;
	}

	public Object getPROFILEUPDATEFLAG(){
		return pROFILEUPDATEFLAG;
	}

	public void setPROMOACTIVE(Object pROMOACTIVE){
		this.pROMOACTIVE = pROMOACTIVE;
	}

	public Object getPROMOACTIVE(){
		return pROMOACTIVE;
	}

	public void setBENEFPICFILESIZE(Object bENEFPICFILESIZE){
		this.bENEFPICFILESIZE = bENEFPICFILESIZE;
	}

	public Object getBENEFPICFILESIZE(){
		return bENEFPICFILESIZE;
	}

	public void setBENEFPICENCENABLED(Object bENEFPICENCENABLED){
		this.bENEFPICENCENABLED = bENEFPICENCENABLED;
	}

	public Object getBENEFPICENCENABLED(){
		return bENEFPICENCENABLED;
	}

	public void setSTATUSCODE(int sTATUSCODE){
		this.sTATUSCODE = sTATUSCODE;
	}

	public int getSTATUSCODE(){
		return sTATUSCODE;
	}

	public void setNEXTRECORD(int nEXTRECORD){
		this.nEXTRECORD = nEXTRECORD;
	}

	public int getNEXTRECORD(){
		return nEXTRECORD;
	}

	public void setWUSENDERNAME(Object wUSENDERNAME){
		this.wUSENDERNAME = wUSENDERNAME;
	}

	public Object getWUSENDERNAME(){
		return wUSENDERNAME;
	}

	public void setBENEFPICFILEEXTN(Object bENEFPICFILEEXTN){
		this.bENEFPICFILEEXTN = bENEFPICFILEEXTN;
	}

	public Object getBENEFPICFILEEXTN(){
		return bENEFPICFILEEXTN;
	}

	public void setSESSIONID(Object sESSIONID){
		this.sESSIONID = sESSIONID;
	}

	public Object getSESSIONID(){
		return sESSIONID;
	}

	public void setRESULT(List<TravelCardInfo> rESULT){
		this.rESULT = rESULT;
	}

	public List<TravelCardInfo> getRESULT(){
		return rESULT;
	}

	public void setWUTOTALPOINTSEARNED(Object wUTOTALPOINTSEARNED){
		this.wUTOTALPOINTSEARNED = wUTOTALPOINTSEARNED;
	}

	public Object getWUTOTALPOINTSEARNED(){
		return wUTOTALPOINTSEARNED;
	}

	public void setWULOOKUPPROMOCODE(Object wULOOKUPPROMOCODE){
		this.wULOOKUPPROMOCODE = wULOOKUPPROMOCODE;
	}

	public Object getWULOOKUPPROMOCODE(){
		return wULOOKUPPROMOCODE;
	}

	@Override
 	public String toString(){
		return 
			"TravelCardModel{" + 
			"uSER_STATUS = '" + uSERSTATUS + '\'' + 
			",pROMO_LIST_ACTIVE = '" + pROMOLISTACTIVE + '\'' + 
			",oTP = '" + oTP + '\'' + 
			",sESSION_TIMER = '" + sESSIONTIMER + '\'' + 
			",wU_RECEIVER_MORE_FLAG = '" + wURECEIVERMOREFLAG + '\'' + 
			",mESSAGE = '" + mESSAGE + '\'' + 
			",sTATUS_MSG = '" + sTATUSMSG + '\'' + 
			",sTATUS = '" + sTATUS + '\'' + 
			",aMOUNT = '" + aMOUNT + '\'' + 
			",sESSION_TIMER_MSG = '" + sESSIONTIMERMSG + '\'' + 
			",oTP_TIMER_MSG = '" + oTPTIMERMSG + '\'' + 
			",bENEF_PIC_COMP_LIMIT = '" + bENEFPICCOMPLIMIT + '\'' + 
			",wU_CARD_NEXT_RECEIVER_CONTEXT = '" + wUCARDNEXTRECEIVERCONTEXT + '\'' + 
			",iD = '" + iD + '\'' + 
			",oTP_TIMER = '" + oTPTIMER + '\'' + 
			",wU_CARD_NO = '" + wUCARDNO + '\'' + 
			",pROFILE_UPDATE_FLAG = '" + pROFILEUPDATEFLAG + '\'' + 
			",pROMO_ACTIVE = '" + pROMOACTIVE + '\'' + 
			",bENEF_PIC_FILE_SIZE = '" + bENEFPICFILESIZE + '\'' + 
			",bENEF_PIC_ENC_ENABLED = '" + bENEFPICENCENABLED + '\'' + 
			",sTATUS_CODE = '" + sTATUSCODE + '\'' + 
			",nEXT_RECORD = '" + nEXTRECORD + '\'' + 
			",wU_SENDER_NAME = '" + wUSENDERNAME + '\'' + 
			",bENEF_PIC_FILE_EXTN = '" + bENEFPICFILEEXTN + '\'' + 
			",sESSION_ID = '" + sESSIONID + '\'' + 
			",rESULT = '" + rESULT + '\'' + 
			",wU_TOTAL_POINTS_EARNED = '" + wUTOTALPOINTSEARNED + '\'' + 
			",wU_LOOKUP_PROMO_CODE = '" + wULOOKUPPROMOCODE + '\'' + 
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
		dest.writeString(mESSAGE);
		dest.writeString(sTATUSMSG);
		dest.writeInt(sTATUSCODE);
		dest.writeInt(nEXTRECORD);
		dest.writeTypedList(rESULT);
	}
}