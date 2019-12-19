package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 27 February, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class AccountTypeData implements Parcelable {


    public static final Parcelable.Creator<AccountTypeData> CREATOR = new Parcelable.Creator<AccountTypeData>() {
        @Override
        public AccountTypeData createFromParcel(Parcel source) {
            return new AccountTypeData(source);
        }

        @Override
        public AccountTypeData[] newArray(int size) {
            return new AccountTypeData[size];
        }
    };
    /**
     * accTypePkId : 177
     * accountCode : SAVINGS
     * accountType : SAVINGS
     * benefBankFkId : 172
     * bankCode : 9999042260006
     */

    @SerializedName("accTypePkId")
    private int id;
    @SerializedName("accountCode")
    private String code;
    @SerializedName("accountType")
    private String name;
    @SerializedName("benefBankFkId")
    private int benfBankFkId;
    private String bankCode;

    public AccountTypeData() {
    }

    protected AccountTypeData(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.name = in.readString();
        this.benfBankFkId = in.readInt();
        this.bankCode = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBenfBankFkId() {
        return benfBankFkId;
    }

    public void setBenfBankFkId(int benfBankFkId) {
        this.benfBankFkId = benfBankFkId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.benfBankFkId);
        dest.writeString(this.bankCode);
    }
}