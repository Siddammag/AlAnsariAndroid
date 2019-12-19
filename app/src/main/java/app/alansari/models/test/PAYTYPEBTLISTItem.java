package app.alansari.models.test;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class PAYTYPEBTLISTItem implements Parcelable {

	@SerializedName("displayKey")
	private String displayKey;

	@SerializedName("displayValue")
	private String displayValue;

	@SerializedName("primaryKeyValue")
	private String primaryKeyValue;

	protected PAYTYPEBTLISTItem(Parcel in) {
		displayKey = in.readString();
		displayValue = in.readString();
		primaryKeyValue = in.readString();
	}

	public static final Creator<PAYTYPEBTLISTItem> CREATOR = new Creator<PAYTYPEBTLISTItem>() {
		@Override
		public PAYTYPEBTLISTItem createFromParcel(Parcel in) {
			return new PAYTYPEBTLISTItem(in);
		}

		@Override
		public PAYTYPEBTLISTItem[] newArray(int size) {
			return new PAYTYPEBTLISTItem[size];
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
			"PAYTYPEBTLISTItem{" + 
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