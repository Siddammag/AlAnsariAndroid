package app.alansari.models.travalcardvalidateflag;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelCardValidateResponse{

	@SerializedName("OTP_TIMER")
	private Object oTPTIMER;

	@SerializedName("PROFILE_UPDATE_FLAG")
	private Object pROFILEUPDATEFLAG;

	@SerializedName("SESSION_TIMER_MSG")
	private Object sESSIONTIMERMSG;

	@SerializedName("OTP_TIMER_MSG")
	private Object oTPTIMERMSG;

	@SerializedName("SESSION_TIMER")
	private Object sESSIONTIMER;

	@SerializedName("RESULT")
	private List<TravelCardFlag> rESULT;

	public void setOTPTIMER(Object oTPTIMER){
		this.oTPTIMER = oTPTIMER;
	}

	public Object getOTPTIMER(){
		return oTPTIMER;
	}

	public void setPROFILEUPDATEFLAG(Object pROFILEUPDATEFLAG){
		this.pROFILEUPDATEFLAG = pROFILEUPDATEFLAG;
	}

	public Object getPROFILEUPDATEFLAG(){
		return pROFILEUPDATEFLAG;
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

	public void setSESSIONTIMER(Object sESSIONTIMER){
		this.sESSIONTIMER = sESSIONTIMER;
	}

	public Object getSESSIONTIMER(){
		return sESSIONTIMER;
	}

	public void setRESULT(List<TravelCardFlag> rESULT){
		this.rESULT = rESULT;
	}

	public List<TravelCardFlag> getRESULT(){
		return rESULT;
	}

	@Override
 	public String toString(){
		return 
			"TravelCardValidateResponse{" + 
			"oTP_TIMER = '" + oTPTIMER + '\'' + 
			",pROFILE_UPDATE_FLAG = '" + pROFILEUPDATEFLAG + '\'' + 
			",sESSION_TIMER_MSG = '" + sESSIONTIMERMSG + '\'' + 
			",oTP_TIMER_MSG = '" + oTPTIMERMSG + '\'' + 
			",sESSION_TIMER = '" + sESSIONTIMER + '\'' + 
			",rESULT = '" + rESULT + '\'' + 
			"}";
		}
}