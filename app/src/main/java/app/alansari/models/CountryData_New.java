package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Parveen Dala on 28 December, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class CountryData_New implements Parcelable {


    /**
     * COUNTRY_PK_ID : 123213
     * AREX_COUNTRY_CODE : 23
     * CE_COUNTRY_CODE : 12
     * LATIN_NAME : INDIA
     * ARABIC_NAME : INDIA
     * NATIONALITY : IN
     * FLAG : india.png
     * BACKGROUND : backgroundindia.png
     * AREX_CCY : 18.40
     * CE_CCY : 18.60
     * CURRENCIES : [{"CURRENCY_PK_ID":"12","NAME":"INR","DEFAULT_STATUS":"1"},{"CURRENCY_PK_ID":"13","NAME":"USD","DEFAULT_STATUS":"0"}]
     */

    @SerializedName("COUNTRY_PK_ID")
    private String id;
    @SerializedName("AREX_COUNTRY_CODE")
    private String countryCodeAREX;
    @SerializedName("CE_COUNTRY_CODE")
    private String countryCodeCE;
    @SerializedName("LATIN_NAME")
    private String latinName;
    @SerializedName("ARABIC_NAME")
    private String arabicName;
    @SerializedName("NATIONALITY")
    private String nationality;
    @SerializedName("FLAG")
    private String flag;
    @SerializedName("BACKGROUND")
    private String background;
    @SerializedName("AREX_CCY")
    private String ccyAREX;
    @SerializedName("CE_CCY")
    private String ccyCE;
    @SerializedName("CURRENCIES")
    private List<CurrencyData> currencyData;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCodeAREX() {
        return countryCodeAREX;
    }

    public void setCountryCodeAREX(String countryCodeAREX) {
        this.countryCodeAREX = countryCodeAREX;
    }

    public String getCountryCodeCE() {
        return countryCodeCE;
    }

    public void setCountryCodeCE(String countryCodeCE) {
        this.countryCodeCE = countryCodeCE;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCcyAREX() {
        return ccyAREX;
    }

    public void setCcyAREX(String ccyAREX) {
        this.ccyAREX = ccyAREX;
    }

    public String getCcyCE() {
        return ccyCE;
    }

    public void setCcyCE(String ccyCE) {
        this.ccyCE = ccyCE;
    }

    public List<CurrencyData> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(List<CurrencyData> currencyData) {
        this.currencyData = currencyData;
    }

    public static class CurrencyData {
        /**
         * CURRENCY_PK_ID : 12
         * NAME : INR
         * DEFAULT_STATUS : 1
         */

        @SerializedName("CURRENCY_PK_ID")
        private String id;
        @SerializedName("NAME")
        private String name;
        @SerializedName("DEFAULT_STATUS")
        private String defaultStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefaultStatus() {
            return defaultStatus;
        }

        public void setDefaultStatus(String defaultStatus) {
            this.defaultStatus = defaultStatus;
        }
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(countryCodeAREX);
        dest.writeString(countryCodeCE);
        dest.writeString(latinName);
        dest.writeString(arabicName);
        dest.writeString(nationality);
        dest.writeString(flag);
        dest.writeString(background);
        dest.writeString(ccyAREX);
        dest.writeString(ccyCE);
    }

    //parcel part
    public CountryData_New(Parcel in) {
        this.id = in.readString();
        this.countryCodeAREX = in.readString();
        this.countryCodeCE = in.readString();
        this.latinName = in.readString();
        this.arabicName = in.readString();
        this.nationality = in.readString();
        this.flag = in.readString();
        this.background = in.readString();
        this.ccyAREX = in.readString();
        this.ccyCE = in.readString();
    }

    public static final Creator<CountryData_New> CREATOR = new Creator<CountryData_New>() {

        @Override
        public CountryData_New createFromParcel(Parcel source) {
            return new CountryData_New(source);
        }

        @Override
        public CountryData_New[] newArray(int size) {
            return new CountryData_New[size];
        }
    };
}
