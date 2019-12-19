package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Parveen Dala on 03 January, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class AdditionalInfoData implements Parcelable {


    public static final Creator<AdditionalInfoData> CREATOR = new Creator<AdditionalInfoData>() {
        @Override
        public AdditionalInfoData createFromParcel(Parcel source) {
            return new AdditionalInfoData(source);
        }

        @Override
        public AdditionalInfoData[] newArray(int size) {
            return new AdditionalInfoData[size];
        }
    };
    /**
     * ADDITIONAL_INFO_PK_ID : 1
     * TYPE : DROPDOWN
     * TITLE : REASON FOR TRANSFER
     * MANDATORY : 1
     * VALUES : [{"ADDITIONAL_INFO_VALUE_PK_ID":"1","VALUE":"FOR PERSONAL USE"},{"ADDITIONAL_INFO_VALUE_PK_ID":"1","VALUE":"FOR COMPNY USE"},{"ADDITIONAL_INFO_VALUE_PK_ID":"1","VALUE":"CORPORATE FUNDS"}]
     */

    @SerializedName("ADDITIONAL_INFO_PK_ID")
    private String id;
    @SerializedName("TYPE")
    private String fieldType;
    @SerializedName("TITLE")
    private String title;
    @SerializedName("MANDATORY")
    private String isMandatory;
    @SerializedName("INPUT_TYPE")
    private String inputType;
    private String errorMessage;
    @SerializedName("VALUES")
    private List<ValuesData> valuesData;

    public AdditionalInfoData() {
    }

    protected AdditionalInfoData(Parcel in) {
        this.id = in.readString();
        this.fieldType = in.readString();
        this.title = in.readString();
        this.isMandatory = in.readString();
        this.inputType = in.readString();
        this.errorMessage = in.readString();
        this.valuesData = in.createTypedArrayList(ValuesData.CREATOR);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ValuesData> getValuesData() {
        return valuesData;
    }

    public void setValuesData(List<ValuesData> valuesData) {
        this.valuesData = valuesData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fieldType);
        dest.writeString(this.title);
        dest.writeString(this.isMandatory);
        dest.writeString(this.inputType);
        dest.writeString(this.errorMessage);
        dest.writeTypedList(this.valuesData);
    }

    public static class ValuesData implements Parcelable {
        public static final Creator<ValuesData> CREATOR = new Creator<ValuesData>() {
            @Override
            public ValuesData createFromParcel(Parcel in) {
                return new ValuesData(in);
            }

            @Override
            public ValuesData[] newArray(int size) {
                return new ValuesData[size];
            }
        };
        /**
         * ADDITIONAL_INFO_VALUE_PK_ID : 1
         * VALUE : FOR PERSONAL USE
         */

        @SerializedName("ADDITIONAL_INFO_VALUE_PK_ID")
        private String id;
        @SerializedName("VALUE")
        private String title;

        protected ValuesData(Parcel in) {
            id = in.readString();
            title = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
        }
    }
}
