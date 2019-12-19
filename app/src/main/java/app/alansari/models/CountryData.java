package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Parveen Dala on 28 December, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class CountryData implements Parcelable {

    public static final Creator<CountryData> CREATOR = new Creator<CountryData>() {

        @Override
        public CountryData createFromParcel(Parcel source) {
            return new CountryData(source);
        }

        @Override
        public CountryData[] newArray(int size) {
            return new CountryData[size];
        }
    };
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
    @SerializedName("NATIONALITY_ID")
    private String nationalityId;
    @SerializedName("NATIONALITY")
    private String nationality;
    @SerializedName("FLAG")
    private String flag;
    @SerializedName("SCREEN_MESSAGE")
    private String background;
    @SerializedName("AREX_CCY")
    private String ccyAREX;
    @SerializedName("CE_CCY")
    private String ccyCE;
    @SerializedName("WU_COUNTRY_CODE")
    private String wuCountryCode;
    @SerializedName("CURRENCIES")
    private ArrayList<CurrencyData> currencyData;
    /*@SerializedName("SCREEN_MESSAGE")
    public String screenMessage;*/

    public CountryData() {
        currencyData = new ArrayList<>();
    }

    //parcel part
    public CountryData(Parcel in) {
        this.id = in.readString();
        this.countryCodeAREX = in.readString();
        this.countryCodeCE = in.readString();
        this.latinName = in.readString();
        this.arabicName = in.readString();
        this.nationality = in.readString();
        this.flag = in.readString();
       // this.screenMessage=in.readString();
        this.background = in.readString();
        this.ccyAREX = in.readString();
        this.ccyCE = in.readString();
        this.wuCountryCode = in.readString();
        this.currencyData = in.readArrayList(CurrencyData.class.getClassLoader());
    }

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

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
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

   /* public String getscreenMessage() {
        return screenMessage;
    }

    public void setscreenMessage(String screenMessage) {
        this.screenMessage = screenMessage;
    }*/

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

    public String getWuCountryCode() {
        return wuCountryCode;
    }

    public void setWuCountryCode(String wuCountryCode) {
        this.wuCountryCode = wuCountryCode;
    }

    public ArrayList<CurrencyData> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(ArrayList<CurrencyData> currencyData) {
        this.currencyData = currencyData;
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
        dest.writeString(wuCountryCode);
        dest.writeList(currencyData);
        //dest.writeString(screenMessage);
    }



    public static class CurrencyData implements Parcelable {


        public static final Creator<CurrencyData> CREATOR = new Creator<CurrencyData>() {
            @Override
            public CurrencyData createFromParcel(Parcel in) {
                return new CurrencyData(in);
            }

            @Override
            public CurrencyData[] newArray(int size) {
                return new CurrencyData[size];
            }
        };
        /**
         * CURRENCY_CODE : 26
         * NAME : INR
         * DEFAULT_STATUS : 1
         */

        @SerializedName("CURRENCY_CODE")
        private String currencyCode;
        @SerializedName("NAME")
        private String name;
        @SerializedName("DEFAULT_STATUS")
        private String defaultStatus;

        public CurrencyData() {
        }

        public CurrencyData(int value) {
            if (value == 1) {
                this.currencyCode = "99";
                this.name = "AED";
                this.defaultStatus = "1";
            } else if (value == 2) {
                this.currencyCode = "26";
                this.name = "INR";
                this.defaultStatus = "1";
            } else if (value == 3) {
                this.currencyCode = "92";
                this.name = "USD";
                this.defaultStatus = "0";
            } else {
                this.currencyCode = "90";
                this.name = "YED";
                this.defaultStatus = "0";
            }
        }

        public CurrencyData(String currencyCode, String name, String defaultStatus) {
            this.currencyCode = currencyCode;
            this.name = name;
            this.defaultStatus = defaultStatus;
        }

        protected CurrencyData(Parcel in) {
            currencyCode = in.readString();
            name = in.readString();
            defaultStatus = in.readString();
        }

        public CurrencyData getUAECurrencyData() {
            this.currencyCode = "91";
            this.name = "AED";
            this.defaultStatus = "1";
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(currencyCode);
            dest.writeString(name);
            dest.writeString(defaultStatus);

        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
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
}