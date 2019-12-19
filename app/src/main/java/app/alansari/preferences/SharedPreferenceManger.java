package app.alansari.preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

import app.alansari.AppController;
import app.alansari.Utils.Constants;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.QuickSendModel;
import app.alansari.models.ServiceTypeData;

/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class SharedPreferenceManger {

    public static void clearPreferences() {
        AppController.getInstance().getSharedPreferencesContext().edit().clear().apply();
    }

    public static void setPrefVal(String key, Object value, VALUE_TYPE vType) {
        switch (vType) {
            case BOOLEAN:
                AppController.getInstance().getSharedPreferencesContext().edit().putBoolean(key, (Boolean) value).apply();
                break;
            case INTEGER:
                AppController.getInstance().getSharedPreferencesContext().edit().putInt(key, (Integer) value).apply();
                break;
            case STRING:
                AppController.getInstance().getSharedPreferencesContext().edit().putString(key, (String) value).apply();
                break;
            case FLOAT:
                AppController.getInstance().getSharedPreferencesContext().edit().putFloat(key, (Float) value).apply();
                break;
            case LONG:
                AppController.getInstance().getSharedPreferencesContext().edit().putLong(key, (Long) value).apply();
                break;
            default:
                break;
        }
    }

    public static Object getPrefVal(String key, Object defValue, VALUE_TYPE vType) {
        Object object;
        switch (vType) {
            case BOOLEAN:
                object = AppController.getInstance().getSharedPreferencesContext().getBoolean(key, (Boolean) defValue);
                break;
            case INTEGER:
                object = AppController.getInstance().getSharedPreferencesContext().getInt(key, (Integer) defValue);
                break;
            case STRING:
                object = AppController.getInstance().getSharedPreferencesContext().getString(key, (String) defValue);
                break;
            case FLOAT:
                object = AppController.getInstance().getSharedPreferencesContext().getFloat(key, (Float) defValue);
                break;
            case LONG:
                object = AppController.getInstance().getSharedPreferencesContext().getLong(key, (Long) defValue);
                break;
            default:
                throw new NullPointerException();
        }
        return object;
    }

    /**
     * Save Quick Send Data To SharedPreference
     *
     * @param gson
     * @param quickSendData
     */
    public static void saveQuickSendData(Gson gson, ArrayList<QuickSendModel> quickSendData) {
        SharedPreferenceManger.setPrefVal(Constants.QUICK_SEND_DATA_PREF, gson.toJson(quickSendData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load Quick Send Data From SharedPreference
     *
     * @return
     */
    public static ArrayList<QuickSendModel> loadQuickSendData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.QUICK_SEND_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return (ArrayList<QuickSendModel>) new Gson().fromJson(json, new TypeToken<ArrayList<QuickSendModel>>() {
            }.getType());
        }
        return null;
    }

    /**
     * Save Service Type Data To SharedPreference
     *
     * @param gson
     * @param serviceTypeData
     */
    public static void saveServiceTypeData(Gson gson, ArrayList<ServiceTypeData> serviceTypeData) {
        SharedPreferenceManger.setPrefVal(Constants.SERVICE_TYPE_DATA_PREF, gson.toJson(serviceTypeData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load Service Type Data From SharedPreference
     *
     * @return
     */
    public static ArrayList<ServiceTypeData> loadServiceTypeData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.SERVICE_TYPE_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return (ArrayList<ServiceTypeData>) new Gson().fromJson(json, new TypeToken<ArrayList<ServiceTypeData>>() {
            }.getType());
        }
        return null;
    }

    /**
     * Save Country Data To SharedPreference
     *
     * @param gson
     * @param countryData
     */
    public static void saveCountriesData(Gson gson, ArrayList<CountryData> countryData) {
        SharedPreferenceManger.setPrefVal(Constants.COUNTRY_DATA_PREF, gson.toJson(countryData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load Country Data From SharedPreference
     *
     * @return
     */
    public static ArrayList<CountryData> loadCountriesData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return (ArrayList<CountryData>) new Gson().fromJson(json, new TypeToken<ArrayList<CountryData>>() {
            }.getType());
        }
        return null;
    }

    /**
     * Save Country Data To SharedPreference
     *
     * @param gson
     * @param countryData
     */
    public static void saveWuCountriesData(Gson gson, ArrayList<CountryData> countryData) {
        SharedPreferenceManger.setPrefVal(Constants.WU_COUNTRY_DATA_PREF, gson.toJson(countryData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load Country Data From SharedPreference
     *
     * @return
     */
    public static ArrayList<CountryData> loadWuCountriesData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.WU_COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return (ArrayList<CountryData>) new Gson().fromJson(json, new TypeToken<ArrayList<CountryData>>() {
            }.getType());
        }
        return null;
    }

    /**
     * Load Country Details From SharedPreference
     *
     * @param countryCode
     * @return
     */
    public static CountryData loadCountryDetails(String countryCode, String serviceType) {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            ArrayList<CountryData> countryData = (ArrayList<CountryData>) new Gson().fromJson(json, new TypeToken<ArrayList<CountryData>>() {
            }.getType());
            for (int i = 0; i < countryData.size(); i++) {
                if ((serviceType.equalsIgnoreCase(Constants.SERVICE_TYPE_VALUE_TRANSFER) && countryData.get(i).getCountryCodeAREX().equalsIgnoreCase(countryCode)) || (serviceType.equalsIgnoreCase(Constants.SERVICE_TYPE_INSTANT_TRANSFER) && countryData.get(i).getCountryCodeCE().equalsIgnoreCase(countryCode))) {
                    return countryData.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Save Selected Country Data To SharedPreference
     *
     * @param gson
     * @param countryData
     */
    public static void saveSelectedCountryData(Gson gson, CountryData countryData) {
        SharedPreferenceManger.setPrefVal(Constants.SELECTED_COUNTRY_DATA_PREF, gson.toJson(countryData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load Selected Country Data From SharedPreference
     *
     * @return
     */
    public static CountryData loadSelectedCountryData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.SELECTED_COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return new Gson().fromJson(json, CountryData.class);
        }
        return null;
    }

    /**
     * Save UAE Country Data To SharedPreference
     *
     * @param gson
     * @param countryData
     */
    public static void saveUAECountryData(Gson gson, CountryData countryData) {
        SharedPreferenceManger.setPrefVal(Constants.UAE_COUNTRY_DATA_PREF, gson.toJson(countryData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load UAE Country Data From SharedPreference
     *
     * @return
     */
    public static CountryData loadUAECountryData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.UAE_COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return new Gson().fromJson(json, CountryData.class);
        }
        return null;
    }

    /**
     * Save UAE Country Data To SharedPreference
     *
     * @param gson
     * @param countryData
     */
    public static void saveWuUAECountryData(Gson gson, CountryData countryData) {
        SharedPreferenceManger.setPrefVal(Constants.WU_UAE_COUNTRY_DATA_PREF, gson.toJson(countryData), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Load UAE Country Data From SharedPreference
     *
     * @return
     */
    public static CountryData loadWuUAECountryData() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.WU_UAE_COUNTRY_DATA_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return new Gson().fromJson(json, CountryData.class);
        }
        return null;
    }

    public static BeneficiaryData getSendMoneyDataDummy() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.SEND_MONEY_RESPONSE_DUMMY_PREF, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (json != null) {
            return new Gson().fromJson(json, BeneficiaryData.class);
        }
        return null;
    }

    public static void setSendMoneyDataDummy(BeneficiaryData beneficiaryData) {
        SharedPreferenceManger.setPrefVal(Constants.SEND_MONEY_RESPONSE_DUMMY_PREF, new Gson().toJson(beneficiaryData).toString(), SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static boolean justLoggedIn() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.JUST_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setMessage(String message) {
        SharedPreferenceManger.setPrefVal(Constants.MESSAGE, message, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void saveRateReviewTime(Calendar calendar) {
        SharedPreferenceManger.setPrefVal(Constants.SAVE_RATE_REVIEW_TIME, new Gson().toJson(calendar), VALUE_TYPE.STRING);
    }

    public static Calendar getRateReviewTime() {
        String json = (String) SharedPreferenceManger.getPrefVal(Constants.SAVE_RATE_REVIEW_TIME, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        try {
            if (json != null)
                return new Gson().fromJson(json, Calendar.class);
        } catch (Exception exp) {
            return null;
        }
        return null;
    }

    public enum VALUE_TYPE {
        BOOLEAN, INTEGER, STRING, FLOAT, LONG
    }

}