package app.alansari.models.getCharges;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetCharges implements Parcelable{

	@SerializedName("VAT_DISCOUNT_CODE")
	private Object vATDISCOUNTCODE;

	@SerializedName("VAT_DISCOUNT_PP")
	private Object vATDISCOUNTPP;

	@SerializedName("RATE")
	private Object rATE;

	@SerializedName("STATUS_CODE")
	private int sTATUSCODE;

	@SerializedName("VAT_ROUNDINGOFF_PP")
	private Object vATROUNDINGOFFPP;

	@SerializedName("VAT_ROUNDINGOFF_CODE")
	private Object vATROUNDINGOFFCODE;

	@SerializedName("result")
	private List<ResultItem> result;

	@SerializedName("STATUS_MSG")
	private String sTATUSMSG;

	@SerializedName("MESSAGE")
	private String mESSAGE;

	@SerializedName("VAT_ROUNDINGOFF")
	private String vATROUNDINGOFF;

	@SerializedName("ADDITIONAL_CHARGES_PP")
	private Object aDDITIONALCHARGESPP;

	@SerializedName("AMOUNT")
	private Object aMOUNT;

	@SerializedName("VAT_CHARGES_CODE")
	private Object vATCHARGESCODE;

	@SerializedName("TOTAL_AMOUNT_PP")
	private Object tOTALAMOUNTPP;

	@SerializedName("TOTAL_PRIORITY_PAY_CHARGES_PP")
	private Object tOTALPRIORITYPAYCHARGESPP;

	@SerializedName("CHARGES_ON_US")
	private String cHARGESONUS;

	@SerializedName("VAT_CHARGES_PP")
	private Object vATCHARGESPP;

	@SerializedName("VAT_DISCOUNT")
	private String vATDISCOUNT;

	@SerializedName("TOTAL_AMOUNT")
	private String tOTALAMOUNT;

	@SerializedName("VAT_CHARGES")
	private String vATCHARGES;

	protected GetCharges(Parcel in) {
		sTATUSCODE = in.readInt();
		sTATUSMSG = in.readString();
		mESSAGE = in.readString();
		vATROUNDINGOFF = in.readString();
		cHARGESONUS = in.readString();
		vATDISCOUNT = in.readString();
		tOTALAMOUNT = in.readString();
		vATCHARGES = in.readString();
	}

	public static final Creator<GetCharges> CREATOR = new Creator<GetCharges>() {
		@Override
		public GetCharges createFromParcel(Parcel in) {
			return new GetCharges(in);
		}

		@Override
		public GetCharges[] newArray(int size) {
			return new GetCharges[size];
		}
	};

	public void setVATDISCOUNTCODE(Object vATDISCOUNTCODE){
		this.vATDISCOUNTCODE = vATDISCOUNTCODE;
	}

	public Object getVATDISCOUNTCODE(){
		return vATDISCOUNTCODE;
	}

	public void setVATDISCOUNTPP(Object vATDISCOUNTPP){
		this.vATDISCOUNTPP = vATDISCOUNTPP;
	}

	public Object getVATDISCOUNTPP(){
		return vATDISCOUNTPP;
	}

	public void setRATE(Object rATE){
		this.rATE = rATE;
	}

	public Object getRATE(){
		return rATE;
	}

	public void setSTATUSCODE(int sTATUSCODE){
		this.sTATUSCODE = sTATUSCODE;
	}

	public int getSTATUSCODE(){
		return sTATUSCODE;
	}

	public void setVATROUNDINGOFFPP(Object vATROUNDINGOFFPP){
		this.vATROUNDINGOFFPP = vATROUNDINGOFFPP;
	}

	public Object getVATROUNDINGOFFPP(){
		return vATROUNDINGOFFPP;
	}

	public void setVATROUNDINGOFFCODE(Object vATROUNDINGOFFCODE){
		this.vATROUNDINGOFFCODE = vATROUNDINGOFFCODE;
	}

	public Object getVATROUNDINGOFFCODE(){
		return vATROUNDINGOFFCODE;
	}

	public void setResult(List<ResultItem> result){
		this.result = result;
	}

	public List<ResultItem> getResult(){
		return result;
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

	public void setVATROUNDINGOFF(String vATROUNDINGOFF){
		this.vATROUNDINGOFF = vATROUNDINGOFF;
	}

	public String getVATROUNDINGOFF(){
		return vATROUNDINGOFF;
	}

	public void setADDITIONALCHARGESPP(Object aDDITIONALCHARGESPP){
		this.aDDITIONALCHARGESPP = aDDITIONALCHARGESPP;
	}

	public Object getADDITIONALCHARGESPP(){
		return aDDITIONALCHARGESPP;
	}

	public void setAMOUNT(Object aMOUNT){
		this.aMOUNT = aMOUNT;
	}

	public Object getAMOUNT(){
		return aMOUNT;
	}

	public void setVATCHARGESCODE(Object vATCHARGESCODE){
		this.vATCHARGESCODE = vATCHARGESCODE;
	}

	public Object getVATCHARGESCODE(){
		return vATCHARGESCODE;
	}

	public void setTOTALAMOUNTPP(Object tOTALAMOUNTPP){
		this.tOTALAMOUNTPP = tOTALAMOUNTPP;
	}

	public Object getTOTALAMOUNTPP(){
		return tOTALAMOUNTPP;
	}

	public void setTOTALPRIORITYPAYCHARGESPP(Object tOTALPRIORITYPAYCHARGESPP){
		this.tOTALPRIORITYPAYCHARGESPP = tOTALPRIORITYPAYCHARGESPP;
	}

	public Object getTOTALPRIORITYPAYCHARGESPP(){
		return tOTALPRIORITYPAYCHARGESPP;
	}

	public void setCHARGESONUS(String cHARGESONUS){
		this.cHARGESONUS = cHARGESONUS;
	}

	public String getCHARGESONUS(){
		return cHARGESONUS;
	}

	public void setVATCHARGESPP(Object vATCHARGESPP){
		this.vATCHARGESPP = vATCHARGESPP;
	}

	public Object getVATCHARGESPP(){
		return vATCHARGESPP;
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

	@Override
 	public String toString(){
		return 
			"GetCharges{" + 
			"vAT_DISCOUNT_CODE = '" + vATDISCOUNTCODE + '\'' + 
			",vAT_DISCOUNT_PP = '" + vATDISCOUNTPP + '\'' + 
			",rATE = '" + rATE + '\'' + 
			",sTATUS_CODE = '" + sTATUSCODE + '\'' + 
			",vAT_ROUNDINGOFF_PP = '" + vATROUNDINGOFFPP + '\'' + 
			",vAT_ROUNDINGOFF_CODE = '" + vATROUNDINGOFFCODE + '\'' + 
			",result = '" + result + '\'' + 
			",sTATUS_MSG = '" + sTATUSMSG + '\'' + 
			",mESSAGE = '" + mESSAGE + '\'' + 
			",vAT_ROUNDINGOFF = '" + vATROUNDINGOFF + '\'' + 
			",aDDITIONAL_CHARGES_PP = '" + aDDITIONALCHARGESPP + '\'' + 
			",aMOUNT = '" + aMOUNT + '\'' + 
			",vAT_CHARGES_CODE = '" + vATCHARGESCODE + '\'' + 
			",tOTAL_AMOUNT_PP = '" + tOTALAMOUNTPP + '\'' + 
			",tOTAL_PRIORITY_PAY_CHARGES_PP = '" + tOTALPRIORITYPAYCHARGESPP + '\'' + 
			",cHARGES_ON_US = '" + cHARGESONUS + '\'' + 
			",vAT_CHARGES_PP = '" + vATCHARGESPP + '\'' + 
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
		dest.writeInt(sTATUSCODE);
		dest.writeString(sTATUSMSG);
		dest.writeString(mESSAGE);
		dest.writeString(vATROUNDINGOFF);
		dest.writeString(cHARGESONUS);
		dest.writeString(vATDISCOUNT);
		dest.writeString(tOTALAMOUNT);
		dest.writeString(vATCHARGES);
	}
}