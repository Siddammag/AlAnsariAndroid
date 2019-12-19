package app.alansari.models.TravelCardReloadModel;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class TravelCardInfo implements Parcelable{

	@SerializedName("CARD_HOLDER_NAME")
	private String cARDHOLDERNAME;

	@SerializedName("WC_PK_ID")
	private String wCPKID;

	@SerializedName("ACCOUNT_TYPE")
	private String aCCOUNTTYPE;

	@SerializedName("AUTH_REP_MEM_CARD_NO")
	private String aUTHREPMEMCARDNO;

	@SerializedName("AGENT_ACCOUNT_NUM")
	private String aGENTACCOUNTNUM;

	@SerializedName("AUTH_REP_MEM_FK_ID")
	private String aUTHREPMEMFKID;

	@SerializedName("MEM_WC_FK_ID")
	private String mEMWCFKID;

	@SerializedName("CARD_NUMBER")
	private String cARDNUMBER;
	private String promoCode = "";

	private String additionalInfoTimeStamp;


    protected TravelCardInfo(Parcel in) {
        cARDHOLDERNAME = in.readString();
        wCPKID = in.readString();
        aCCOUNTTYPE = in.readString();
        aUTHREPMEMCARDNO = in.readString();
        aGENTACCOUNTNUM = in.readString();
        aUTHREPMEMFKID = in.readString();
        mEMWCFKID = in.readString();
        cARDNUMBER = in.readString();
		this.promoCode = in.readString();
		this.additionalInfoTimeStamp = in.readString();
    }

    public static final Creator<TravelCardInfo> CREATOR = new Creator<TravelCardInfo>() {
        @Override
        public TravelCardInfo createFromParcel(Parcel in) {
            return new TravelCardInfo(in);
        }

        @Override
        public TravelCardInfo[] newArray(int size) {
            return new TravelCardInfo[size];
        }
    };

    public void setCARDHOLDERNAME(String cARDHOLDERNAME){
		this.cARDHOLDERNAME = cARDHOLDERNAME;
	}

	public String getCARDHOLDERNAME(){
		return cARDHOLDERNAME;
	}

	public void setWCPKID(String wCPKID){
		this.wCPKID = wCPKID;
	}

	public String getWCPKID(){
		return wCPKID;
	}

	public void setACCOUNTTYPE(String aCCOUNTTYPE){
		this.aCCOUNTTYPE = aCCOUNTTYPE;
	}

	public String getACCOUNTTYPE(){
		return aCCOUNTTYPE;
	}

	public void setAUTHREPMEMCARDNO(String aUTHREPMEMCARDNO){
		this.aUTHREPMEMCARDNO = aUTHREPMEMCARDNO;
	}

	public String getAUTHREPMEMCARDNO(){
		return aUTHREPMEMCARDNO;
	}

	public void setAGENTACCOUNTNUM(String aGENTACCOUNTNUM){
		this.aGENTACCOUNTNUM = aGENTACCOUNTNUM;
	}

	public String getAGENTACCOUNTNUM(){
		return aGENTACCOUNTNUM;
	}

	public void setAUTHREPMEMFKID(String aUTHREPMEMFKID){
		this.aUTHREPMEMFKID = aUTHREPMEMFKID;
	}

	public String getAUTHREPMEMFKID(){
		return aUTHREPMEMFKID;
	}

	public void setMEMWCFKID(String mEMWCFKID){
		this.mEMWCFKID = mEMWCFKID;
	}

	public String getMEMWCFKID(){
		return mEMWCFKID;
	}

	public void setCARDNUMBER(String cARDNUMBER){
		this.cARDNUMBER = cARDNUMBER;
	}

	public String getCARDNUMBER(){
		return cARDNUMBER;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}


	public String getAdditionalInfoTimeStamp() {
		return additionalInfoTimeStamp;
	}

	public void setAdditionalInfoTimeStamp(String additionalInfoTimeStamp) {
		this.additionalInfoTimeStamp = additionalInfoTimeStamp;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"cARD_HOLDER_NAME = '" + cARDHOLDERNAME + '\'' + 
			",wC_PK_ID = '" + wCPKID + '\'' + 
			",aCCOUNT_TYPE = '" + aCCOUNTTYPE + '\'' + 
			",aUTH_REP_MEM_CARD_NO = '" + aUTHREPMEMCARDNO + '\'' + 
			",aGENT_ACCOUNT_NUM = '" + aGENTACCOUNTNUM + '\'' + 
			",aUTH_REP_MEM_FK_ID = '" + aUTHREPMEMFKID + '\'' + 
			",mEM_WC_FK_ID = '" + mEMWCFKID + '\'' + 
			",cARD_NUMBER = '" + cARDNUMBER + '\'' + 
			"}";
		}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cARDHOLDERNAME);
        dest.writeString(wCPKID);
        dest.writeString(aCCOUNTTYPE);
        dest.writeString(aUTHREPMEMCARDNO);
        dest.writeString(aGENTACCOUNTNUM);
        dest.writeString(aUTHREPMEMFKID);
        dest.writeString(mEMWCFKID);
        dest.writeString(cARDNUMBER);
		dest.writeString(this.promoCode);
		dest.writeString(this.additionalInfoTimeStamp);
    }
}