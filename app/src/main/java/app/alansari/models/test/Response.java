package app.alansari.models.test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class Response implements Parcelable {

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

	protected Response(Parcel in) {
		tXNNUMBEROFSTEPS = in.readInt();
		rESULT = in.createTypedArrayList(RESULTItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tXNNUMBEROFSTEPS);
		dest.writeTypedList(rESULT);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Response> CREATOR = new Creator<Response>() {
		@Override
		public Response createFromParcel(Parcel in) {
			return new Response(in);
		}

		@Override
		public Response[] newArray(int size) {
			return new Response[size];
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

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"sESSION_TIMER = '" + sESSIONTIMER + '\'' + 
			",oTP_TIMER = '" + oTPTIMER + '\'' + 
			",sESSION_TIMER_MSG = '" + sESSIONTIMERMSG + '\'' + 
			",oTP_TIMER_MSG = '" + oTPTIMERMSG + '\'' + 
			",pROFILE_UPDATE_FLAG = '" + pROFILEUPDATEFLAG + '\'' + 
			",tXN_NUMBER_OF_STEPS = '" + tXNNUMBEROFSTEPS + '\'' + 
			",rESULT = '" + rESULT + '\'' + 
			"}";
		}
}