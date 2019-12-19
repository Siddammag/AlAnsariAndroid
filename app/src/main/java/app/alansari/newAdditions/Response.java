package app.alansari.newAdditions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable{

	@SerializedName("TRANSACTION_HISTORY_DETAIL_WC")
	private List<TRANSACTIONHISTORYDETAILWCItem> tRANSACTIONHISTORYDETAILWC;

	protected Response(Parcel in) {
		tRANSACTIONHISTORYDETAILWC = in.createTypedArrayList(TRANSACTIONHISTORYDETAILWCItem.CREATOR);
	}

	public static final Creator<Response> CREATOR = new Creator<Response>() {
		@Override
		public Response createFromParcel(Parcel in) {
			return new Response(in);
		}

		@Override
		public Response[] newArray(int size) {
			return new Response[size];
		}
	};

	public void setTRANSACTIONHISTORYDETAILWC(List<TRANSACTIONHISTORYDETAILWCItem> tRANSACTIONHISTORYDETAILWC){
		this.tRANSACTIONHISTORYDETAILWC = tRANSACTIONHISTORYDETAILWC;
	}

	public List<TRANSACTIONHISTORYDETAILWCItem> getTRANSACTIONHISTORYDETAILWC(){
		return tRANSACTIONHISTORYDETAILWC;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"tRANSACTION_HISTORY_DETAIL_WC = '" + tRANSACTIONHISTORYDETAILWC + '\'' + 
			"}";
		}


	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(tRANSACTIONHISTORYDETAILWC);
	}
}