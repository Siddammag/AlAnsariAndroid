package app.alansari.models.additioninfowc;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/*
public class SOURCEOFFUNDItem implements Parcelable{

	@SerializedName("displayKey")
	private String displayKey;

	@SerializedName("displayValue")
	private String displayValue;

	@SerializedName("primaryKeyValue")
	private Object primaryKeyValue;

	protected SOURCEOFFUNDItem(Parcel in) {
		displayKey = in.readString();
		displayValue = in.readString();
	}

	public static final Creator<SOURCEOFFUNDItem> CREATOR = new Creator<SOURCEOFFUNDItem>() {
		@Override
		public SOURCEOFFUNDItem createFromParcel(Parcel in) {
			return new SOURCEOFFUNDItem(in);
		}

		@Override
		public SOURCEOFFUNDItem[] newArray(int size) {
			return new SOURCEOFFUNDItem[size];
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

	public void setPrimaryKeyValue(Object primaryKeyValue){
		this.primaryKeyValue = primaryKeyValue;
	}

	public Object getPrimaryKeyValue(){
		return primaryKeyValue;
	}

	@Override
 	public String toString(){
		return 
			"SOURCEOFFUNDItem{" + 
			"displayKey = '" + displayKey + '\'' + 
			",displayValue = '" + displayValue + '\'' + 
			",primaryKeyValue = '" + primaryKeyValue + '\'' + 
			"}";
		}

	*/
/**
	 * Describe the kinds of special objects contained in this Parcelable
	 * instance's marshaled representation. For example, if the object will
	 * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
	 * the return value of this method must include the
	 * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
	 *
	 * @return a bitmask indicating the set of special object types marshaled
	 * by this Parcelable object instance.
	 *//*

	@Override
	public int describeContents() {
		return 0;
	}

	*/
/**
	 * Flatten this object in to a Parcel.
	 *
	 * @param dest  The Parcel in which the object should be written.
	 * @param flags Additional flags about how the object should be written.
	 *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
	 *//*

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(displayKey);
		dest.writeString(displayValue);
	}
}*/
