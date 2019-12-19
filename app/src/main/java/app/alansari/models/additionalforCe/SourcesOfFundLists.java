package app.alansari.models.additionalforCe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SourcesOfFundLists implements Parcelable {

	@SerializedName("displayKey")
	private String displayKey;

	@SerializedName("displayValue")
	private String displayValue;

	@SerializedName("primaryKeyValue")
	private Object primaryKeyValue;

	protected SourcesOfFundLists(Parcel in) {
		displayKey = in.readString();
		displayValue = in.readString();
	}

	public static final Creator<SourcesOfFundLists> CREATOR = new Creator<SourcesOfFundLists>() {
		@Override
		public SourcesOfFundLists createFromParcel(Parcel in) {
			return new SourcesOfFundLists(in);
		}

		@Override
		public SourcesOfFundLists[] newArray(int size) {
			return new SourcesOfFundLists[size];
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(displayKey);
		dest.writeString(displayValue);
	}
}