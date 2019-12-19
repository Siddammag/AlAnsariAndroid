package app.alansari.models.transactiontracker;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class TrackerResult implements Parcelable {

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("STATUS_DESC")
	private String sTATUSDESC;

	@SerializedName("MESSAGE")
	private String mESSAGE;

	@SerializedName("ACTIVE_IND")
	private String aCTIVEIND;

	@SerializedName("PRIORITY")
	private String pRIORITY;

	protected TrackerResult(Parcel in) {
		sTATUS = in.readString();
		sTATUSDESC = in.readString();
		mESSAGE = in.readString();
		aCTIVEIND = in.readString();
		pRIORITY = in.readString();
	}

	public static final Creator<TrackerResult> CREATOR = new Creator<TrackerResult>() {
		@Override
		public TrackerResult createFromParcel(Parcel in) {
			return new TrackerResult(in);
		}

		@Override
		public TrackerResult[] newArray(int size) {
			return new TrackerResult[size];
		}
	};

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setSTATUSDESC(String sTATUSDESC){
		this.sTATUSDESC = sTATUSDESC;
	}

	public String getSTATUSDESC(){
		return sTATUSDESC;
	}

	public void setMESSAGE(String mESSAGE){
		this.mESSAGE = mESSAGE;
	}

	public String getMESSAGE(){
		return mESSAGE;
	}

	public void setACTIVEIND(String aCTIVEIND){
		this.aCTIVEIND = aCTIVEIND;
	}

	public String getACTIVEIND(){
		return aCTIVEIND;
	}

	public void setPRIORITY(String pRIORITY){
		this.pRIORITY = pRIORITY;
	}

	public String getPRIORITY(){
		return pRIORITY;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"sTATUS = '" + sTATUS + '\'' + 
			",sTATUS_DESC = '" + sTATUSDESC + '\'' + 
			",mESSAGE = '" + mESSAGE + '\'' + 
			",aCTIVE_IND = '" + aCTIVEIND + '\'' + 
			",pRIORITY = '" + pRIORITY + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sTATUS);
		dest.writeString(sTATUSDESC);
		dest.writeString(mESSAGE);
		dest.writeString(aCTIVEIND);
		dest.writeString(pRIORITY);
	}
}