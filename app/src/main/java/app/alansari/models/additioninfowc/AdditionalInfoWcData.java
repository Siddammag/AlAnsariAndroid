package app.alansari.models.additioninfowc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AdditionalInfoWcData implements Parcelable{

	@SerializedName("RESULT")
	private List<RESULTItem> rESULT;

	protected AdditionalInfoWcData(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AdditionalInfoWcData> CREATOR = new Creator<AdditionalInfoWcData>() {
		@Override
		public AdditionalInfoWcData createFromParcel(Parcel in) {
			return new AdditionalInfoWcData(in);
		}

		@Override
		public AdditionalInfoWcData[] newArray(int size) {
			return new AdditionalInfoWcData[size];
		}
	};

	public void setRESULT(List<RESULTItem> rESULT){
		this.rESULT = rESULT;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	@Override
 	public String toString(){
		return 
			"AdditionalInfoWcData{" + 
			"rESULT = '" + rESULT + '\'' + 
			"}";
		}
}