package app.alansari.modules.remittance;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class RESULTItem implements Parcelable {

	@SerializedName("AREX_PAGINATION_NO")
	private int aREXPAGINATIONNO;

	@SerializedName("CE_PAGINATION_NO")
	private int cEPAGINATIONNO;

	@SerializedName("TXN_HISTORY_DATA")
	private List<TXNHISTORYDATAItem> tXNHISTORYDATA;

	protected RESULTItem(Parcel in) {
		aREXPAGINATIONNO = in.readInt();
		cEPAGINATIONNO = in.readInt();
		tXNHISTORYDATA = in.createTypedArrayList(TXNHISTORYDATAItem.CREATOR);
	}

	public static final Creator<RESULTItem> CREATOR = new Creator<RESULTItem>() {
		@Override
		public RESULTItem createFromParcel(Parcel in) {
			return new RESULTItem(in);
		}

		@Override
		public RESULTItem[] newArray(int size) {
			return new RESULTItem[size];
		}
	};

	public void setAREXPAGINATIONNO(int aREXPAGINATIONNO){
		this.aREXPAGINATIONNO = aREXPAGINATIONNO;
	}

	public int getAREXPAGINATIONNO(){
		return aREXPAGINATIONNO;
	}

	public void setCEPAGINATIONNO(int cEPAGINATIONNO){
		this.cEPAGINATIONNO = cEPAGINATIONNO;
	}

	public int getCEPAGINATIONNO(){
		return cEPAGINATIONNO;
	}

	public void setTXNHISTORYDATA(List<TXNHISTORYDATAItem> tXNHISTORYDATA){
		this.tXNHISTORYDATA = tXNHISTORYDATA;
	}

	public List<TXNHISTORYDATAItem> getTXNHISTORYDATA(){
		return tXNHISTORYDATA;
	}

	@Override
 	public String toString(){
		return 
			"RESULTItem{" + 
			"aREX_PAGINATION_NO = '" + aREXPAGINATIONNO + '\'' + 
			",cE_PAGINATION_NO = '" + cEPAGINATIONNO + '\'' + 
			",tXN_HISTORY_DATA = '" + tXNHISTORYDATA + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(aREXPAGINATIONNO);
		dest.writeInt(cEPAGINATIONNO);
		dest.writeTypedList(tXNHISTORYDATA);
	}
}