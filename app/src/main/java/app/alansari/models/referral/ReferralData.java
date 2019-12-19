package app.alansari.models.referral;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ReferralData implements Parcelable {

	@SerializedName("MOBILE_REFERRAL_PK_ID")
	private int mOBILEREFERRALPKID;

	@SerializedName("IMG_URL")
	private String iMGURL;

	@SerializedName("MESSAGE_CONTENT1")
	private String mESSAGECONTENT1;

	@SerializedName("MESSAGE_CONTENT2")
	private String mESSAGECONTENT2;

	@SerializedName("MESSAGE_CONTENT3")
	private String mESSAGECONTENT3;

	@SerializedName("MESSAGE_CONTENT4")
	private String mESSAGECONTENT4;

	@SerializedName("MESSAGE_CONTENT5")
	private String mESSAGECONTENT5;

	@SerializedName("SUB_URL")
	private String sUBURL;

	@SerializedName("TERMS_AND_CONDITIONS")
	private Object tERMSANDCONDITIONS;

	@SerializedName("TERMS_AND_CONDITIONS_STR")
	private String tERMSANDCONDITIONSSTR;

	@SerializedName("START_DATE")
	private long sTARTDATE;

	@SerializedName("END_DATE")
	private long eNDDATE;

	@SerializedName("ACTIVE_IND")
	private String aCTIVEIND;

	@SerializedName("CREATED_BY")
	private String cREATEDBY;

	@SerializedName("CREATED_DATE")
	private long cREATEDDATE;

	@SerializedName("MODIFIED_BY")
	private String mODIFIEDBY;

	@SerializedName("MODIFIED_DATE")
	private long mODIFIEDDATE;

	@SerializedName("IMG_URL1")
	private String iMGURL1;

	protected ReferralData(Parcel in) {
		mOBILEREFERRALPKID = in.readInt();
		iMGURL = in.readString();
		mESSAGECONTENT1 = in.readString();
		mESSAGECONTENT2 = in.readString();
		mESSAGECONTENT3 = in.readString();
		mESSAGECONTENT4 = in.readString();
		mESSAGECONTENT5 = in.readString();
		sUBURL = in.readString();
		tERMSANDCONDITIONSSTR = in.readString();
		sTARTDATE = in.readLong();
		eNDDATE = in.readLong();
		aCTIVEIND = in.readString();
		cREATEDBY = in.readString();
		cREATEDDATE = in.readLong();
		mODIFIEDBY = in.readString();
		mODIFIEDDATE = in.readLong();
		iMGURL1 = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mOBILEREFERRALPKID);
		dest.writeString(iMGURL);
		dest.writeString(mESSAGECONTENT1);
		dest.writeString(mESSAGECONTENT2);
		dest.writeString(mESSAGECONTENT3);
		dest.writeString(mESSAGECONTENT4);
		dest.writeString(mESSAGECONTENT5);
		dest.writeString(sUBURL);
		dest.writeString(tERMSANDCONDITIONSSTR);
		dest.writeLong(sTARTDATE);
		dest.writeLong(eNDDATE);
		dest.writeString(aCTIVEIND);
		dest.writeString(cREATEDBY);
		dest.writeLong(cREATEDDATE);
		dest.writeString(mODIFIEDBY);
		dest.writeLong(mODIFIEDDATE);
		dest.writeString(iMGURL1);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ReferralData> CREATOR = new Creator<ReferralData>() {
		@Override
		public ReferralData createFromParcel(Parcel in) {
			return new ReferralData(in);
		}

		@Override
		public ReferralData[] newArray(int size) {
			return new ReferralData[size];
		}
	};

	public void setMOBILEREFERRALPKID(int mOBILEREFERRALPKID){
		this.mOBILEREFERRALPKID = mOBILEREFERRALPKID;
	}

	public int getMOBILEREFERRALPKID(){
		return mOBILEREFERRALPKID;
	}

	public void setIMGURL(String iMGURL){
		this.iMGURL = iMGURL;
	}

	public String getIMGURL(){
		return iMGURL;
	}

	public void setMESSAGECONTENT1(String mESSAGECONTENT1){
		this.mESSAGECONTENT1 = mESSAGECONTENT1;
	}

	public String getMESSAGECONTENT1(){
		return mESSAGECONTENT1;
	}

	public void setMESSAGECONTENT2(String mESSAGECONTENT2){
		this.mESSAGECONTENT2 = mESSAGECONTENT2;
	}

	public String getMESSAGECONTENT2(){
		return mESSAGECONTENT2;
	}

	public void setMESSAGECONTENT3(String mESSAGECONTENT3){
		this.mESSAGECONTENT3 = mESSAGECONTENT3;
	}

	public String getMESSAGECONTENT3(){
		return mESSAGECONTENT3;
	}

	public void setMESSAGECONTENT4(String mESSAGECONTENT4){
		this.mESSAGECONTENT4 = mESSAGECONTENT4;
	}

	public String getMESSAGECONTENT4(){
		return mESSAGECONTENT4;
	}

	public void setMESSAGECONTENT5(String mESSAGECONTENT5){
		this.mESSAGECONTENT5 = mESSAGECONTENT5;
	}

	public String getMESSAGECONTENT5(){
		return mESSAGECONTENT5;
	}

	public void setSUBURL(String sUBURL){
		this.sUBURL = sUBURL;
	}

	public String getSUBURL(){
		return sUBURL;
	}

	public void setTERMSANDCONDITIONS(Object tERMSANDCONDITIONS){
		this.tERMSANDCONDITIONS = tERMSANDCONDITIONS;
	}

	public Object getTERMSANDCONDITIONS(){
		return tERMSANDCONDITIONS;
	}

	public void setTERMSANDCONDITIONSSTR(String tERMSANDCONDITIONSSTR){
		this.tERMSANDCONDITIONSSTR = tERMSANDCONDITIONSSTR;
	}

	public String getTERMSANDCONDITIONSSTR(){
		return tERMSANDCONDITIONSSTR;
	}

	public void setSTARTDATE(long sTARTDATE){
		this.sTARTDATE = sTARTDATE;
	}

	public long getSTARTDATE(){
		return sTARTDATE;
	}

	public void setENDDATE(long eNDDATE){
		this.eNDDATE = eNDDATE;
	}

	public long getENDDATE(){
		return eNDDATE;
	}

	public void setACTIVEIND(String aCTIVEIND){
		this.aCTIVEIND = aCTIVEIND;
	}

	public String getACTIVEIND(){
		return aCTIVEIND;
	}

	public void setCREATEDBY(String cREATEDBY){
		this.cREATEDBY = cREATEDBY;
	}

	public String getCREATEDBY(){
		return cREATEDBY;
	}

	public void setCREATEDDATE(long cREATEDDATE){
		this.cREATEDDATE = cREATEDDATE;
	}

	public long getCREATEDDATE(){
		return cREATEDDATE;
	}

	public void setMODIFIEDBY(String mODIFIEDBY){
		this.mODIFIEDBY = mODIFIEDBY;
	}

	public String getMODIFIEDBY(){
		return mODIFIEDBY;
	}

	public void setMODIFIEDDATE(long mODIFIEDDATE){
		this.mODIFIEDDATE = mODIFIEDDATE;
	}

	public long getMODIFIEDDATE(){
		return mODIFIEDDATE;
	}

	public void setIMGURL1(String iMGURL1){
		this.iMGURL1 = iMGURL1;
	}

	public String getIMGURL1(){
		return iMGURL1;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"mOBILE_REFERRAL_PK_ID = '" + mOBILEREFERRALPKID + '\'' + 
			",iMG_URL = '" + iMGURL + '\'' + 
			",mESSAGE_CONTENT1 = '" + mESSAGECONTENT1 + '\'' + 
			",mESSAGE_CONTENT2 = '" + mESSAGECONTENT2 + '\'' + 
			",mESSAGE_CONTENT3 = '" + mESSAGECONTENT3 + '\'' + 
			",mESSAGE_CONTENT4 = '" + mESSAGECONTENT4 + '\'' + 
			",mESSAGE_CONTENT5 = '" + mESSAGECONTENT5 + '\'' + 
			",sUB_URL = '" + sUBURL + '\'' + 
			",tERMS_AND_CONDITIONS = '" + tERMSANDCONDITIONS + '\'' + 
			",tERMS_AND_CONDITIONS_STR = '" + tERMSANDCONDITIONSSTR + '\'' + 
			",sTART_DATE = '" + sTARTDATE + '\'' + 
			",eND_DATE = '" + eNDDATE + '\'' + 
			",aCTIVE_IND = '" + aCTIVEIND + '\'' + 
			",cREATED_BY = '" + cREATEDBY + '\'' + 
			",cREATED_DATE = '" + cREATEDDATE + '\'' + 
			",mODIFIED_BY = '" + mODIFIEDBY + '\'' + 
			",mODIFIED_DATE = '" + mODIFIEDDATE + '\'' + 
			",iMG_URL1 = '" + iMGURL1 + '\'' + 
			"}";
		}
}