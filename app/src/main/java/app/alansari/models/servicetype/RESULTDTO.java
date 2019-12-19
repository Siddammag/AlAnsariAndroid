package app.alansari.models.servicetype;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class RESULTDTO implements Parcelable {

	@SerializedName("displayKey")
	private String displayKey;

	@SerializedName("displayValue")
	private String displayValue;

	@SerializedName("primaryKeyValue")
	private String primaryKeyValue;

	protected RESULTDTO(Parcel in) {
		displayKey = in.readString();
		displayValue = in.readString();
		primaryKeyValue = in.readString();
	}

	public static final Creator<RESULTDTO> CREATOR = new Creator<RESULTDTO>() {
		@Override
		public RESULTDTO createFromParcel(Parcel in) {
			return new RESULTDTO(in);
		}

		@Override
		public RESULTDTO[] newArray(int size) {
			return new RESULTDTO[size];
		}
	};

	public void setDisplayKey(String displayKey){
		this.displayKey = displayKey;
	}

	public String getDisplayKey(){
		return displayKey;
	}

	public void setDisplayValue(String displayValue){
		this.displayValue = displayValue;
	}

	public String getDisplayValue(){
		return displayValue;
	}

	public void setPrimaryKeyValue(String primaryKeyValue){
		this.primaryKeyValue = primaryKeyValue;
	}

	public String getPrimaryKeyValue(){
		return primaryKeyValue;
	}

	@Override
 	public String toString(){
		return 
			"RESULTDTO{" + 
			"displayKey = '" + displayKey + '\'' + 
			",displayValue = '" + displayValue + '\'' + 
			",primaryKeyValue = '" + primaryKeyValue + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(displayKey);
		dest.writeString(displayValue);
		dest.writeString(primaryKeyValue);
	}
}