package app.alansari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorData implements Parcelable {


    /**
     * STORE_CODE : 24
     * STORE_NAME : AL AIN MALL BRANCH
     * ADDRESS_LINE_1 : AL AIN MALL, AL AIN
     * ADDRESS_LINE_2 : P.O.BOX 16128
     * CITY_ID : 2
     * CITY : AL AIN
     * DISTRICT :
     * STATE : AL AIN
     * COUNTRY : AE
     * POSTAL_CODE :
     * MAIN_PHONE : 03 751 1225
     * HOME_PAGE : HTTP://WWW.ALANSARIEXCHANGE.COM
     * CATEGORIES : CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES
     * OPENING_HOURS : 1:09:00:00:00,2:09:00:00:00,3:09:00:00:00,4:09:00:00:00,5:09:00:00:00,6:14:00:23:00,7:09:00:00:00
     * LATITUDE : 24.222183
     * LONGITUDE : 55.781879
     * IMAGES :
     * DESCRIPTION : MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS
     * EMAIL : ALAINMALL@ALANSARI.AE
     * ALTERNATE_PHONE :
     * MOBILE :
     * FAX : 03 751 7610
     * PAYMENT_TYPES : CASH,MASTERCARD,VISA
     * AD_ICON_URL :
     * AD_PHONE :
     * AD_LANDING_PAGE_URL :
     */

    @SerializedName("STORE_CODE")
    private String id;
    @SerializedName("STORE_NAME")
    private String name;
    @SerializedName("ADDRESS_LINE_1")
    private String addressLine1;
    @SerializedName("ADDRESS_LINE_2")
    private String addressLine2;
    @SerializedName("CITY_ID")
    private String cityId;
    @SerializedName("CITY")
    private String city;
    @SerializedName("DISTRICT")
    private String district;
    @SerializedName("STATE")
    private String state;
    @SerializedName("COUNTRY")
    private String country;
    @SerializedName("POSTAL_CODE")
    private String postalCode;
    @SerializedName("MAIN_PHONE")
    private String mainPhone;
    @SerializedName("HOME_PAGE")
    private String homePageUrl;
    @SerializedName("CATEGORIES")
    private String categories;
    @SerializedName("OPENING_HOURS")
    private String openingHours;
    @SerializedName("LATITUDE")
    private String latitude;
    @SerializedName("LONGITUDE")
    private String longitude;
    @SerializedName("IMAGES")
    private String image;
    @SerializedName("DESCRIPTION")
    private String descreiption;
    @SerializedName("EMAIL")
    private String email;
    @SerializedName("ALTERNATE_PHONE")
    private String alternatePhone;
    @SerializedName("MOBILE")
    private String mobile;
    @SerializedName("FAX")
    private String fax;
    @SerializedName("PAYMENT_TYPES")
    private String paymentType;
    @SerializedName("AD_ICON_URL")
    private String adIconUrl;
    @SerializedName("AD_PHONE")
    private String adPhone;
    @SerializedName("AD_LANDING_PAGE_URL")
    private String adLandingPageUrl;

    public BranchLocatorData(int branchId, int layoutType, String latitude, String longitude) {
        switch (layoutType) {
            case 0:
                name = "Marina Mall";
                addressLine1 = "Financial Centre, Dubai\nInternational Financial Centre - Dubai";
                break;
            default:
                name = "Khalidia Branch";
                addressLine1 = "Saeed Bin Thani Building, Sheikh\nKhalifa Bin Zayed Road, Umm Hurair, Bur";
                break;
        }
        this.city = "";
        this.country = "";
        this.id = String.valueOf(branchId);
        this.latitude = latitude;
        this.longitude = longitude;
    }


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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescreiption() {
        return descreiption;
    }

    public void setDescreiption(String descreiption) {
        this.descreiption = descreiption;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAdIconUrl() {
        return adIconUrl;
    }

    public void setAdIconUrl(String adIconUrl) {
        this.adIconUrl = adIconUrl;
    }

    public String getAdPhone() {
        return adPhone;
    }

    public void setAdPhone(String adPhone) {
        this.adPhone = adPhone;
    }

    public String getAdLandingPageUrl() {
        return adLandingPageUrl;
    }

    public void setAdLandingPageUrl(String adLandingPageUrl) {
        this.adLandingPageUrl = adLandingPageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.addressLine1);
        dest.writeString(this.addressLine2);
        dest.writeString(this.cityId);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.postalCode);
        dest.writeString(this.mainPhone);
        dest.writeString(this.homePageUrl);
        dest.writeString(this.categories);
        dest.writeString(this.openingHours);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.image);
        dest.writeString(this.descreiption);
        dest.writeString(this.email);
        dest.writeString(this.alternatePhone);
        dest.writeString(this.mobile);
        dest.writeString(this.fax);
        dest.writeString(this.paymentType);
        dest.writeString(this.adIconUrl);
        dest.writeString(this.adPhone);
        dest.writeString(this.adLandingPageUrl);
    }

    protected BranchLocatorData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.addressLine1 = in.readString();
        this.addressLine2 = in.readString();
        this.cityId = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.postalCode = in.readString();
        this.mainPhone = in.readString();
        this.homePageUrl = in.readString();
        this.categories = in.readString();
        this.openingHours = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.image = in.readString();
        this.descreiption = in.readString();
        this.email = in.readString();
        this.alternatePhone = in.readString();
        this.mobile = in.readString();
        this.fax = in.readString();
        this.paymentType = in.readString();
        this.adIconUrl = in.readString();
        this.adPhone = in.readString();
        this.adLandingPageUrl = in.readString();
    }

    public static final Parcelable.Creator<BranchLocatorData> CREATOR = new Parcelable.Creator<BranchLocatorData>() {
        @Override
        public BranchLocatorData createFromParcel(Parcel source) {
            return new BranchLocatorData(source);
        }

        @Override
        public BranchLocatorData[] newArray(int size) {
            return new BranchLocatorData[size];
        }
    };
}
