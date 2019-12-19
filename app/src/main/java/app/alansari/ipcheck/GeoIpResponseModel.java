package app.alansari.ipcheck;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Parveen Dala on 05 June, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class GeoIpResponseModel {

    private String as;
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("countryCode")
    private String countryCode;
    private String isp;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;
    private String org;
    private String query;
    private String region;
    private String regionName;
    private Status status;
    private String timezone;
    private String message;

    @Override
    public String toString() {
        return "countryCode: " + countryCode + ", isp: " + isp + ", city: " + city;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public enum Status {
        @SerializedName("success")
        SUCCESS,
        @SerializedName("fail")
        FAIL
    }
}
