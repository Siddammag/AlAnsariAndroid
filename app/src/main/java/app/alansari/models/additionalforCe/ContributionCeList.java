package app.alansari.models.additionalforCe;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

public class ContributionCeList implements Parcelable {

	    @SerializedName("displayKey")
		private String displayKey;

		@SerializedName("displayValue")
		private String displayValue;

		@SerializedName("primaryKeyValue")
		private Object primaryKeyValue;

	protected ContributionCeList(Parcel in) {
		displayKey = in.readString();
		displayValue = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(displayKey);
		dest.writeString(displayValue);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ContributionCeList> CREATOR = new Creator<ContributionCeList>() {
		@Override
		public ContributionCeList createFromParcel(Parcel in) {
			return new ContributionCeList(in);
		}

		@Override
		public ContributionCeList[] newArray(int size) {
			return new ContributionCeList[size];
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
					"RESULTItem{" +
							"displayKey = '" + displayKey + '\'' +
							",displayValue = '" + displayValue + '\'' +
							",primaryKeyValue = '" + primaryKeyValue + '\'' +
							"}";
		}
	}
