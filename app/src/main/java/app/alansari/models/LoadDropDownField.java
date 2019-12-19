package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



public class LoadDropDownField  implements Parcelable {


    public static final Parcelable.Creator<LoadDropDownField> CREATOR = new Parcelable.Creator<LoadDropDownField>() {
        @Override
        public LoadDropDownField createFromParcel(Parcel source) {
            return new LoadDropDownField(source);
        }

        @Override
        public LoadDropDownField[] newArray(int size) {
            return new LoadDropDownField[size];
        }
    };
    /**
     * "displayKey":"DUBAI",
     "displayValue":"DUBAI",
     "primaryKeyValue":"001"
     */

    @SerializedName("displayKey")
    private String display_key;
    @SerializedName("displayValue")
    private String display_value;
    @SerializedName("primaryKeyValue")
    private String primary_key_value;

    public String getDisplay_value() {
        return display_value;
    }

    public void setDisplay_value(String display_value) {
        this.display_value = display_value;
    }

    public LoadDropDownField() {
    }

    public String getDisplay_key() {
        return display_key;
    }

    public void setDisplay_key(String display_key) {
        this.display_key = display_key;
    }



    public String getPrimary_key_value() {
        return primary_key_value;
    }

    public void setPrimary_key_value(String primary_key_value) {
        this.primary_key_value = primary_key_value;
    }

    protected LoadDropDownField(Parcel in) {
        this.display_key = in.readString();
        this.display_value = in.readString();
        this.primary_key_value = in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.display_key);
        dest.writeString(this.display_value);
        dest.writeString(this.primary_key_value);
    }
}
