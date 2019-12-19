package app.alansari.network;

import android.os.Build;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.alansari.BuildConfig;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.DeviceDetailsData;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class APIRequestParams {

    public JSONObject saveFCMToServer(String userPkId, String deviceId, String fcmToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userPkId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.DEVICE, Constants.DEVICE_ANDROID);
            jsonObject.put(Constants.FCM_TOKEN, fcmToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveFCMToServer:-  " + jsonObject.toString());
        return jsonObject;
    }

    private void addCommonParameters(JSONObject jsonObject) throws JSONException {
        jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
    }


    private String checkValue(String value) {
        LogUtils.d("ok", "value:- " + value);
        return value != null ? value : "";
    }


    public JSONObject registerEXID(String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.EXC_USER_NAME, email);
            jsonObject.put(Constants.EXC_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "registerEXID:-  " + jsonObject.toString());
        return jsonObject;
    }

    public Map<String, String> registerEXIDSoap(String email, String password) {
        Map<String, String> mapObject = new HashMap<>();
        try {
            mapObject.put(Constants.EXC_USER_NAME, email);
            mapObject.put(Constants.EXC_PASSWORD, password);
            mapObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "registerEXIDSoap:-  " + mapObject.toString());
        return mapObject;
    }

    public JSONObject registerEexchange(String UserName, DeviceDetailsData deviceDetailsData, String email, String password, String eExchangeReg) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(UserName));
            jsonObject.put(Constants.EXC_USER_NAME, email);
            jsonObject.put(Constants.EXC_PASSWORD, password);
            jsonObject.put(Constants.DEVICE_ID, deviceDetailsData.getDeviceId());
            jsonObject.put(Constants.DEVICE_MAC, deviceDetailsData.getDeviceMac());
            jsonObject.put(Constants.DEVICE_PUSH_ID, deviceDetailsData.getDevicePushId());
            jsonObject.put(Constants.DEVICE_TYPE, deviceDetailsData.getDeviceType());
            jsonObject.put(Constants.DEVICE_NAME, deviceDetailsData.getDeviceName());
            jsonObject.put(Constants.DEVICE_OS, deviceDetailsData.getDeviceOs());
            jsonObject.put(Constants.EEXCHANGE_REG, eExchangeReg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "registerEexchange:-  " + jsonObject.toString());
        return jsonObject;
    }

    //StorePin-------------------------------------------------------------------------------------------
    public JSONObject storePinValue(String userId, String mobile, DeviceDetailsData deviceDetailsData, String pin) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.DEVICE_ID, deviceDetailsData.getDeviceId());
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put("PIN", pin);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "registerMobile:-  " + jsonObject.toString());
        return jsonObject;

    }

    public JSONObject registerMobile(String UserName, DeviceDetailsData deviceDetailsData, String mobile, String eExchangeReg) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(UserName));
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.DEVICE_ID, deviceDetailsData.getDeviceId());
            jsonObject.put(Constants.DEVICE_MAC, deviceDetailsData.getDeviceMac());
            jsonObject.put(Constants.DEVICE_PUSH_ID, deviceDetailsData.getDevicePushId());
            jsonObject.put(Constants.DEVICE_TYPE, deviceDetailsData.getDeviceType());
            jsonObject.put(Constants.DEVICE_NAME, deviceDetailsData.getDeviceName());
            jsonObject.put(Constants.DEVICE_OS, deviceDetailsData.getDeviceOs());
            jsonObject.put(Constants.EEXCHANGE_REG, eExchangeReg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "registerMobile:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject verifyEID(String mobile, String eidNumber, String expiryDate, String dob, String userId, String deviceId, String userStatus, int badLoginCntr) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.EID_NUMBER, eidNumber);
            jsonObject.put(Constants.EXPIRY_DATE, expiryDate);
            jsonObject.put(Constants.DATE_OF_BIRTH, dob);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.USER_STATUS, userStatus);
            jsonObject.put(Constants.BAD_LOGIN_CNTR, String.valueOf(badLoginCntr));
            //TODO: Remove ID_TYPE_CODE
            //jsonObject.put(Constants.ID_TYPE_CODE, idTypeCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "verifyEID:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject verifyOTP(String userId, String mobile, String otp, String userStatus, DeviceDetailsData deviceDetailsData) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.OTP, otp);
            jsonObject.put(Constants.USER_STATUS, userStatus);
            jsonObject.put(Constants.DEVICE_ID, deviceDetailsData.getDeviceId());
            jsonObject.put(Constants.DEVICE_MAC, deviceDetailsData.getDeviceMac());
            jsonObject.put(Constants.DEVICE_PUSH_ID, deviceDetailsData.getDevicePushId());
            jsonObject.put(Constants.DEVICE_TYPE, deviceDetailsData.getDeviceType());
            jsonObject.put(Constants.DEVICE_NAME, deviceDetailsData.getDeviceName());
            jsonObject.put(Constants.DEVICE_OS, deviceDetailsData.getDeviceOs());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "verifyOTP:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject resendOTP(String mobile, String userPk, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userPk));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "resendOTP:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject resendOTPAfterLogin(String mobile, String userPk, String deviceId, String sessionId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MOBILE_NUM, mobile);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userPk));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "resendOTP:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject fetchDetails(String userId, String mobileNum, String deviceId, String pin, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MOBILE_NUM, mobileNum);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put("PIN", pin);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchDetails:-  " + jsonObject.toString());
        return jsonObject;
    }


    // Select Items

    public JSONObject addQuickSend(String userId, BeneficiaryData beneficiaryData, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, beneficiaryData.getBeneficiaryId());
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            if (Validation.isValidString(beneficiaryData.getName())) {
                jsonObject.put(Constants.NAME, beneficiaryData.getName());
            } else {
                jsonObject.put(Constants.NAME, beneficiaryData.getArabicName());
            }

            jsonObject.put(Constants.TYPE.toUpperCase(), beneficiaryData.getServiceTypeData().getMapping());
            if (beneficiaryData.getBenImage() != null && !TextUtils.isEmpty(beneficiaryData.getBenImage()) && beneficiaryData.getBenImage().trim().length() > 0)
                jsonObject.put(Constants.BENF_IMAGE, beneficiaryData.getBenImage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addQuickSend:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteQuickSend(String quickSendId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.QUICK_SEND_BENF_PK_ID, quickSendId);

            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteQuickSend:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Countries List
     *
     * @param userId
     * @return
     */
    public JSONObject fetchCountriesList(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCountriesList:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Bank List
     *
     * @param userId
     * @param countryId
     * @param type
     * @return
     */
    public JSONObject fetchBanksList(String userId, String countryId, String type, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.COUNTRY_ID, countryId);
            jsonObject.put(Constants.TYPE.toUpperCase(), type);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBanksList:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Benf Bank List
     *
     * @param countryId
     * @return
     */
    public JSONObject fetchBenfBankList(String countryId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.COUNTRY_ID, countryId);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBenfBankList:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Benf Bank Branch List
     *
     * @param bankCode
     * @return
     */
    public JSONObject fetchBenfBankBranchList(String serviceType, String bankCode, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.BANK_CODE, bankCode);

            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBenfBankBranchList:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch User Account Type
     *
     * @param bankCode
     * @return
     */
    public JSONObject fetchAccountType(String bankCode, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAccountType:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch User Account List
     *
     * @param userId
     * @return
     */
    public JSONObject fetchUserAccountList(String userId, String memPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchUserAccountList:-  " + jsonObject.toString());
        return jsonObject;
    }

    // Send Money APIs

    public JSONObject deleteUserAccount(String userAccountId) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_CC_PK_ID, userAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch User Account Bank Branch List
     *
     * @param bankId
     * @return
     */
    public JSONObject fetchUserAccountBankBranchList(String bankId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.BANK_MSTR_PK_ID, bankId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchUserAccountBankBranchList:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Service Types
     *
     * @param userId
     * @return
     */
    public JSONObject fetchServiceTypes(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchServiceTypes:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch CE Currency Data
     *
     * @param countryCode
     * @param memPkId
     * @param transferType
     * @return
     */
    public JSONObject fetchCeCurrencyData(String bankCode, String countryCode, String memPkId, String transferType, String serviceType, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            if (!TextUtils.isEmpty(serviceType))
                jsonObject.put(Constants.SERVICE_TYPE, serviceType);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "getCeCurrencyData:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Calculate Currency For Send Money Flow
     *
     * @param countryCode
     * @param bankCode
     * @param transferType
     * @param serviceType
     * @param amount
     * @param fromCCY
     * @param toCCY
     * @return
     */
    public JSONObject calculateCurrencySendMoney(String memPkId, String countryCode, String bankCode,
                                                 String transferType, String serviceType, String amount,
                                                 String fromCCY, String toCCY, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.AMOUNT, amount);
            jsonObject.put(Constants.FROM_CCY, fromCCY);
            jsonObject.put(Constants.TO_CCY, toCCY);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencySendMoney:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject fetchRateSendMoney(String memPkId, String countryCode, String bankCode,
                                                 String transferType, String serviceType, String amount,
                                                 String fromCCY, String toCCY, String userId, String deviceId, String sessionTime,String MemBenefPkId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.FROM_CCY, fromCCY);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.AMOUNT, amount);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.TO_CCY, toCCY);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.MEM_BENEF_PK_ID,MemBenefPkId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencySendMoney:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Calculate Currency For Credit Card
     *
     * @param creditCardPkId
     * @param amount
     * @return
     */
    public JSONObject calculateCurrencyCreditCard(String creditCardPkId, String amount, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
//            /addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_CC_PK_ID, creditCardPkId);
            jsonObject.put(Constants.AMOUNT, amount);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencyCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }


    /**
     * Check txn Limits
     *
     * @param userId
     * @return
     */
    public JSONObject checkTxnLimits(String userId, String creditCardPkId, String serviceType, String transferType, String totalValueAED, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_CC_PK_ID, creditCardPkId);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.TOTAL_VALUE_AED, totalValueAED);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "checkTxnLimits:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Check txn Limits
     *
     * @param userId
     * @return
     */
    public JSONObject checkTxnLimits(String userId, String memPkId, String beneficiaryId, String countryCode, String serviceType, String transferType, String totalValueAED, String txnAmount, String totalTxnAmout, String rate, String chargeAmount, String ccyCode, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, beneficiaryId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.TOTAL_VALUE_AED, totalValueAED);
            jsonObject.put(Constants.TXN_AMOUNT, txnAmount);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, totalTxnAmout);
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.CHARGE_AMOUNT, chargeAmount);
            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "checkTxnLimits:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch PROMO CODE
     *
     * @param userId
     * @return
     */
    public JSONObject fetchPromoCode(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPromoCode:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Additional Info
     *
     * @param userId
     * @return
     */
    public JSONObject fetchAdditionalInfo(String userId, String countryId, String bankId, String serviceId, String transferType, String arexId, String benId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.COUNTRY_ID, countryId);
            jsonObject.put(Constants.BANK_ID, bankId);
            jsonObject.put(Constants.SERVICE_ID, serviceId);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, benId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAdditionalInfo:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Additional Info Ce
     *
     * @param countryCode
     * @param bankCode
     * @return
     */
    public JSONObject fetchAdditionalInfoCe(String memPkId, String transferType, String totalValueAED, String countryCode, String bankCode, String benId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MEM_PK_ID, checkValue(memPkId));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.DEST_COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.DEST_AGENT_CODE, bankCode);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, benId);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAdditionalInfoCe:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Payment Modes
     *
     * @param userId
     * @return
     */
    public JSONObject fetchPaymentModes(String userId, String totalValueAED, String totalTxnAmount, String fee,String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED.replaceAll(",", "")));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(totalTxnAmount));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(fee));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.SERVICE_TYPE,Constants.TXN_TYPE_CREDIT_CARD);
            jsonObject.put(Constants.SCREEN_TYPE,Constants.TXN_TYPE_CREDIT_CARD);
            jsonObject.put(Constants.MEM_PK_ID,CommonUtils.getMemPkId(Constants.AREX_MAPPING));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Payment Modes
     *
     * @param userId
     * @return
     */
    public JSONObject fetchWuPaymentModes(String userId, String totalValueAED, String totalTxnAmount,String fee, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED.replaceAll(",", "")));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(totalTxnAmount));
            jsonObject.put(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WU);
            jsonObject.put(Constants.SCREEN_TYPE, Constants.TRANSACTION_TYPE_WU);
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(fee));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWcPaymentModes(String userId, String totalTxnAmount, String totalValueAED,String fee, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.SCREEN_TYPE, Constants.TRANSACTION_TYPE_WC);
            jsonObject.put(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WC);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(totalTxnAmount));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED.replaceAll(",", "")));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(fee));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchPaymentModeSelect(String userId, String totalValueAED,String deviceId, String sessionTime,String ceServiceId,String screenType,
                                             String bankCode,String memPkId,String arexId,String benId,String countryCode,String transferType,String txnAmount,String rate,String ccyCode) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.USER_PK_ID, userId);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, "0");
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.CE_SERVICE_ID, ceServiceId);
            jsonObject.put(Constants.SCREEN_TYPE, screenType);
            jsonObject.put(Constants.SERVICE_TYPE,screenType);
            jsonObject.put(Constants.BANK_CODE, bankCode);

            jsonObject.put(Constants.MEM_PK_ID, checkValue(memPkId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, benId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.TXN_AMOUNT, txnAmount);
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.CCY_CODE, ccyCode);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;
    }






    /**
     * Fetch AAE Bank Details
     *
     * @param userId
     * @param bankCode
     * @return
     */
    public JSONObject fetchAAEBankDetails(String userId, String bankCode, String isWu, String deviceId, String sessionTime,String PayType) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.PAY_TYPE_BT,PayType);

            if (isWu.equalsIgnoreCase("Y"))
                jsonObject.put(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WU);
            else if (isWu.equalsIgnoreCase("WC"))
                jsonObject.put(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAAEBankDetails:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Remittance
     *
     * @param userId
     * @param arexMemPkId
     * @param ceMemPkId
     * @param dataObject
     * @return
     */
    public JSONObject remittanceApi(String userId, String arexMemPkId, String ceMemPkId, BeneficiaryData dataObject, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, checkValue(arexMemPkId));
            jsonObject.put(Constants.CE_MEM_PK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.ADDITIONAL_INFO_TITLE, checkValue(dataObject.getAdditionalFieldTitle()));
            jsonObject.put(Constants.ADDITIONAL_INFO_VALUE, checkValue(dataObject.getAdditionalFieldValue()));
            jsonObject.put(Constants.MODE_PK_ID, checkValue(dataObject.getPaymentModeData().getId()));
            jsonObject.put(Constants.MODE_NAME, checkValue(dataObject.getPaymentModeData().getMapping()));
            jsonObject.put(Constants.MODE_DESCRIPTION, checkValue(dataObject.getPaymentModeData().getDescription()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));
            jsonObject.put(Constants.CCY_ID, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_CODE, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_DESC, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getName()));
            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));
            jsonObject.put(Constants.BRANCH_ID, checkValue(dataObject.getBranchData().getId()));
            jsonObject.put(Constants.TIME_STAMP, checkValue(dataObject.getAdditionalInfoTimeStamp()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            if (dataObject.getPromoCode() != null && !TextUtils.isEmpty(dataObject.getPromoCode())) {
                jsonObject.put(Constants.PROMO_CODE, dataObject.getPromoCode());
            }

            if (dataObject.getPgsFlag() != null && !TextUtils.isEmpty(dataObject.getPgsFlag())) {
                jsonObject.put(Constants.PGS_FLAG, checkValue(dataObject.getPgsFlag()));
                jsonObject.put(Constants.PGS_BANK_CODE, checkValue(dataObject.getPgsBankCode()));
            } else {
                jsonObject.put(Constants.PGS_FLAG, "N");
                jsonObject.put(Constants.PGS_BANK_CODE, "");
            }

            if (dataObject.getUserAccountData() != null) {
                jsonObject.put(Constants.ACC_FK_ID, checkValue(dataObject.getUserAccountData().getId()));
                jsonObject.put(Constants.USER_ACC_NUM, checkValue(dataObject.getUserAccountData().getAccountNumber()));
                jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, checkValue(dataObject.getUserAccountData().getAccountName()));
            }
            if (dataObject.getAaeBankData() != null) {
                jsonObject.put(Constants.AAE_BANK_NAME, checkValue(dataObject.getAaeBankData().getBankName()));
                jsonObject.put(Constants.AAE_ACCOUNT_NUM, checkValue(dataObject.getAaeBankData().getAccountNumber()));
                jsonObject.put(Constants.AAE_ACCOUNT_NAME, checkValue(dataObject.getAaeBankData().getAccountName()));
                jsonObject.put(Constants.AREX_CODE, checkValue(dataObject.getAaeBankData().getAREXCode()));
            }
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "remittancePayAtBranchApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Remittance
     *
     * @param userId
     * @param arexMemPkId
     * @param ceMemPkId
     * @param dataObject
     * @return
     */
    public JSONObject priorityPayremittanceApi(String userId, String arexMemPkId, String ceMemPkId, BeneficiaryData dataObject, String TOTAL_AMOUNT_PP, String TOTAL_PRIORITY_PAY_CHARGES_PP, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, checkValue(arexMemPkId));
            jsonObject.put(Constants.CE_MEM_PK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.ADDITIONAL_INFO_TITLE, checkValue(dataObject.getAdditionalFieldTitle()));
            jsonObject.put(Constants.ADDITIONAL_INFO_VALUE, checkValue(dataObject.getAdditionalFieldValue()));
            jsonObject.put(Constants.MODE_PK_ID, checkValue(dataObject.getPaymentModeData().getId()));
            jsonObject.put(Constants.MODE_NAME, checkValue(dataObject.getPaymentModeData().getMapping()));
            jsonObject.put(Constants.MODE_DESCRIPTION, checkValue(dataObject.getPaymentModeData().getDescription()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));
            jsonObject.put(Constants.CCY_ID, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_CODE, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_DESC, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getName()));
            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));
            jsonObject.put(Constants.BRANCH_ID, checkValue(dataObject.getBranchData().getId()));
            jsonObject.put(Constants.TIME_STAMP, checkValue(dataObject.getAdditionalInfoTimeStamp()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            jsonObject.put(Constants.TOTAL_AMOUNT_PP, checkValue(TOTAL_AMOUNT_PP));
            jsonObject.put(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, checkValue(TOTAL_PRIORITY_PAY_CHARGES_PP));

            if (dataObject.getUserAccountData() != null) {
                jsonObject.put(Constants.ACC_FK_ID, checkValue(dataObject.getUserAccountData().getId()));
                jsonObject.put(Constants.USER_ACC_NUM, checkValue(dataObject.getUserAccountData().getAccountNumber()));
                jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, checkValue(dataObject.getUserAccountData().getAccountName()));
            }
            if (dataObject.getAaeBankData() != null) {
                jsonObject.put(Constants.AAE_BANK_NAME, checkValue(dataObject.getAaeBankData().getBankName()));
                jsonObject.put(Constants.AAE_ACCOUNT_NUM, checkValue(dataObject.getAaeBankData().getAccountNumber()));
                jsonObject.put(Constants.AAE_ACCOUNT_NAME, checkValue(dataObject.getAaeBankData().getAccountName()));
                jsonObject.put(Constants.AREX_CODE, checkValue(dataObject.getAaeBankData().getAREXCode()));
            }
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "remittancePayAtBranchApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Remittance
     *
     * @param userId
     * @param arexMemPkId
     * @param dataObject
     * @return
     */
    public JSONObject remittanceCreditCardApi(String userId, String arexMemPkId, BeneficiaryData dataObject, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, checkValue(arexMemPkId));
            jsonObject.put(Constants.CREDIT_CARD_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.MODE_PK_ID, checkValue(dataObject.getPaymentModeData().getId()));
            jsonObject.put(Constants.MODE_NAME, checkValue(dataObject.getPaymentModeData().getMapping()));
            jsonObject.put(Constants.MODE_DESCRIPTION, checkValue(dataObject.getPaymentModeData().getDescription()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


            if (dataObject.getPgsFlag() != null && !TextUtils.isEmpty(dataObject.getPgsFlag())) {
                jsonObject.put(Constants.PGS_FLAG, checkValue(dataObject.getPgsFlag()));
                jsonObject.put(Constants.PGS_BANK_CODE, checkValue(dataObject.getPgsBankCode()));
            } else {
                jsonObject.put(Constants.PGS_FLAG, "N");
                jsonObject.put(Constants.PGS_BANK_CODE, "");
            }

            if (dataObject.getUserAccountData() != null) {
                jsonObject.put(Constants.ACC_FK_ID, checkValue(dataObject.getUserAccountData().getId()));
                jsonObject.put(Constants.USER_ACC_NUM, checkValue(dataObject.getUserAccountData().getAccountNumber()));
                jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, checkValue(dataObject.getUserAccountData().getAccountName()));
            }
            if (dataObject.getAaeBankData() != null) {
                jsonObject.put(Constants.AAE_BANK_NAME, checkValue(dataObject.getAaeBankData().getBankName()));
                jsonObject.put(Constants.AAE_ACCOUNT_NUM, checkValue(dataObject.getAaeBankData().getAccountNumber()));
                jsonObject.put(Constants.AAE_ACCOUNT_NAME, checkValue(dataObject.getAaeBankData().getAccountName()));
                jsonObject.put(Constants.AREX_CODE, checkValue(dataObject.getAaeBankData().getAREXCode()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "remittanceCreditCardApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Submit Reference Number Remittance API
     *
     * @param userId
     * @param txnId
     * @param payModeName
     * @param referenceNumber
     * @return
     */
    public JSONObject submitReferenceNumRemittanceApi(String userId, String txnId, String payModeName, String serviceType, String referenceNumber, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {

            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_PK_ID, checkValue(txnId));
            if (serviceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                jsonObject.put(Constants.MODE_NAME, checkValue(payModeName));
            }
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(serviceType));
            jsonObject.put(Constants.BANK_REFERENCE_NUMBER, checkValue(referenceNumber));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "submitReferenceNumRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject submitTransactionRating(String userId, String txnId, String serviceType, String rating, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_PK_ID, checkValue(txnId));
            if (serviceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                jsonObject.put(Constants.SERVICE_TYPE, Constants.AREX_MAPPING);
            } else {
                jsonObject.put(Constants.SERVICE_TYPE, checkValue(serviceType));
            }

            jsonObject.put(Constants.RATING, checkValue(rating));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "submitTransactionRating:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Set Txn Completed Remittance API
     *
     * @param userId
     * @param txnId
     * @return
     */
    public JSONObject setTxnCompletedRemittanceApi(String userId, String txnId, String status, String serviceType, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_PK_ID, checkValue(txnId));
            jsonObject.put(Constants.STATUS, checkValue(status));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(serviceType));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "setTxnCompletedRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Set Txn Completed Remittance API
     *
     * @param userId
     * @param txnId
     * @return
     */
    public JSONObject setFundCompletedRemittanceApi(String userId, String txnId, String serviceType, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_REF_NUM, checkValue(txnId));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(serviceType));

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "setTxnCompletedRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Send Invoice API
     *
     * @param userId
     * @param txnId
     * @param email
     * @param serviceType
     * @return
     */
    public JSONObject sendInvoiceAPI(String userId, String txnId, String email, String serviceType, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_PK_ID, checkValue(txnId));
            jsonObject.put(Constants.EMAIL, checkValue(email));
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "sendInvoiceAPI:-  " + jsonObject.toString());
        return jsonObject;
    }


    // Account Management
    // Beneficiary Management

    /**
     * Fetch Transactions Remittance API
     *
     * @param userId
     * @param status
     * @param type
     * @return
     */
    public JSONObject fetchTransactionsRemittanceApi(String userId, String status, String type, String startCount, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {

            /*{

"CE_PAGINATION_NO" : "1",
"AREX_PAGINATION_NO" : "1"
}*/
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.STATUS, checkValue(status));
            jsonObject.put(Constants.FROM_RECORD, checkValue(startCount));
            jsonObject.put(Constants.TYPE.toUpperCase(), checkValue(type));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchTransactionsRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Beneficiaries
     *
     * @param userId
     * @param memPkId
     * @param countryCode
     * @param type
     * @return
     */
    public JSONObject fetchBeneficiary(String userId, String memPkId, String countryCode, String type, String startCount, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.TYPE.toUpperCase(), "");
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put(Constants.FROM_RECORD, startCount);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }



    public JSONObject fetchBeneficiary2(String userId,String arexMemPkId,String ceMemPkId, String memPkId, String countryCode, String type, String startCount, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.CE_MEM_PK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.TYPE.toUpperCase(), "CE");
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put(Constants.FROM_RECORD, startCount);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject fetchBeneficiaryDetails(String beneficiaryId, String serviceType, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, beneficiaryId);
            jsonObject.put(Constants.TYPE.toUpperCase(), serviceType);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiaryDetails:-  " + jsonObject.toString());
        return jsonObject;
    }



    //fetchChargesSendMoney
    public JSONObject fetchChargesSendMoney(String fcyAmount, String memPkId,String rate,String aedAmt,String bankCode,String paymentMode,String MemBenefPkId, String transferType,String ccyCode,String countryCode,String serviceType,String userPkId, String deviceId, String sessionTime,String ceServiceId) {
        JSONObject jsonObject = new JSONObject();
        try {
           jsonObject.put(Constants.FCY_AMOUNT,fcyAmount);
           jsonObject.put(Constants.MEM_PK_ID, memPkId);
           jsonObject.put(Constants.RATE, rate);
           addCommonParameters(jsonObject);
           jsonObject.put(Constants.AED_AMOUNT, aedAmt);
           jsonObject.put(Constants.BANK_CODE, bankCode);
           jsonObject.put(Constants.PAYMENT_MODE, paymentMode);
           jsonObject.put(Constants.MEM_BENEF_PK_ID,MemBenefPkId);
           jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
           jsonObject.put(Constants.CCY_CODE, ccyCode);
           jsonObject.put(Constants.COUNTRY_CODE, countryCode);
           jsonObject.put(Constants.SERVICE_TYPE, serviceType);
           jsonObject.put(Constants.USER_PK_ID, userPkId);
           jsonObject.put(Constants.DEVICE_ID, deviceId);
           jsonObject.put(Constants.SESSION_ID, sessionTime);
           jsonObject.put(Constants.CE_SERVICE_ID,ceServiceId);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiaryDetails:-  " + jsonObject.toString());
        return jsonObject;
    }



    //fetchChargesSendMoney
    public JSONObject fetchChargesSendMoneyPreLogin(String transferType, String paymentMode, String fcyAmount, String servicetype, String aedAmount,String countryCode, String ccyCode,String deviceId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.PAYMENT_MODE, checkValue(paymentMode));
            jsonObject.put(Constants.FCY_AMOUNT, fcyAmount);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.SERVICE_TYPE,servicetype);
            jsonObject.put(Constants.AED_AMOUNT, checkValue(aedAmount));
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.DEVICE_ID, deviceId);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;

    }


    //fetchChargesSendMoneyPostLogin
    public JSONObject fetchChargesSendMoneyPostLogin(String transferType, String paymentMode, String fcyAmount, String servicetype, String aedAmount,String countryCode, String ccyCode,String deviceId,String userPkId,String sessionTime){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.PAYMENT_MODE, checkValue(paymentMode));
            jsonObject.put(Constants.FCY_AMOUNT, fcyAmount);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.SERVICE_TYPE,servicetype);
            jsonObject.put(Constants.AED_AMOUNT, checkValue(aedAmount));
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.DEVICE_ID, deviceId);

            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;

    }






    public JSONObject fetchBeneficiaryFields(String userId, String countryId, String currencyId, String bankId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.COUNTRY_ID, countryId);
            jsonObject.put(Constants.CURRENCY_ID, currencyId);
            jsonObject.put(Constants.BANK_ID, bankId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiaryFields:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Add Beneficiary
     *
     * @param userId
     * @param memPkId
     * @param beneficiaryData
     * @return
     */

    public JSONObject addBeneficiary(String userId, String memPkId, BeneficiaryData beneficiaryData, boolean isUpdate) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            if (isUpdate) {
                jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(beneficiaryData.getBeneficiaryId()));
            }
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(beneficiaryData.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(beneficiaryData.getTransferType()));
            jsonObject.put(Constants.FIRST_NAME, checkValue(beneficiaryData.getFirstName()));
            jsonObject.put(Constants.LAST_NAME, checkValue(beneficiaryData.getLastName()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue(beneficiaryData.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? beneficiaryData.getCountryData().getCountryCodeAREX() : beneficiaryData.getCountryData().getCountryCodeCE()));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(beneficiaryData.getCountryData().getLatinName()));
            jsonObject.put(Constants.NATIONALITY_CODE, checkValue(beneficiaryData.getNationalityId()));
            jsonObject.put(Constants.NATIONALITY_NAME, checkValue(beneficiaryData.getNationality()));
            jsonObject.put(Constants.ADDRESS, checkValue(beneficiaryData.getAddress()));
            jsonObject.put(Constants.MOBILE, checkValue(beneficiaryData.getMobile()));
            jsonObject.put(Constants.BANK_CODE, checkValue(beneficiaryData.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? beneficiaryData.getBankData().getBankCodeAREX() : beneficiaryData.getBankData().getBankCodeCE()));
            jsonObject.put(Constants.BANK_NAME, checkValue(beneficiaryData.getBankData().getBankName()));
            jsonObject.put(Constants.BRANCH_CODE, checkValue(beneficiaryData.getBranchData() != null ? beneficiaryData.getBranchData().getBankCode() : ""));
            jsonObject.put(Constants.BRANCH_NAME, checkValue(beneficiaryData.getBranchData() != null ? beneficiaryData.getBranchData().getBranchName() : ""));
            jsonObject.put(Constants.ACCOUNT_NUMBER, checkValue(beneficiaryData.getAccountNumber()));
            jsonObject.put(Constants.IBAN_NUMBER, checkValue(beneficiaryData.getIBANNumber()));
            jsonObject.put(Constants.IFSC_CODE, checkValue(beneficiaryData.getIfscCode()));
            jsonObject.put(Constants.SWIFT, checkValue(beneficiaryData.getSwift()));
            jsonObject.put(Constants.SORT_CODE, checkValue(beneficiaryData.getSortCode()));
            jsonObject.put(Constants.TRANSIT_CODE, checkValue(beneficiaryData.getTransitCode()));
            jsonObject.put(Constants.INSTITUTION_CODE, checkValue(beneficiaryData.getInstitutionCode()));
            jsonObject.put(Constants.ROUTING_CODE, checkValue(beneficiaryData.getRoutingCode()));
            jsonObject.put(Constants.BSB, checkValue(beneficiaryData.getBsb()));
            jsonObject.put(Constants.ACCOUNT_TYPE, checkValue(beneficiaryData.getAccountType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Update Beneficiary
     *
     * @param userId
     * @param memPkId
     * @param beneficiaryData
     * @return
     */

    public JSONObject updateBeneficiary(String userId, String memPkId, BeneficiaryData beneficiaryData) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(beneficiaryData.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(beneficiaryData.getTransferType()));
            jsonObject.put(Constants.FIRST_NAME, checkValue(beneficiaryData.getFirstName()));
            jsonObject.put(Constants.LAST_NAME, checkValue(beneficiaryData.getLastName()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue(beneficiaryData.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? beneficiaryData.getCountryData().getCountryCodeAREX() : beneficiaryData.getCountryData().getCountryCodeCE()));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(beneficiaryData.getCountryData().getLatinName()));
            jsonObject.put(Constants.NATIONALITY_CODE, checkValue(beneficiaryData.getNationalityId()));
            jsonObject.put(Constants.NATIONALITY_NAME, checkValue(beneficiaryData.getNationality()));
            jsonObject.put(Constants.ADDRESS, checkValue(beneficiaryData.getAddress()));
            jsonObject.put(Constants.MOBILE, checkValue(beneficiaryData.getMobile()));
            jsonObject.put(Constants.BANK_CODE, checkValue(beneficiaryData.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? beneficiaryData.getBankData().getBankCodeAREX() : beneficiaryData.getBankData().getBankCodeCE()));
            jsonObject.put(Constants.BANK_NAME, checkValue(beneficiaryData.getBankData().getBankName()));
            jsonObject.put(Constants.BRANCH_CODE, checkValue(beneficiaryData.getBranchData().getBankCode()));
            jsonObject.put(Constants.BANK_BRANCH_NAME, checkValue(beneficiaryData.getBranchData().getBranchName()));
            jsonObject.put(Constants.ACCOUNT_NUMBER, checkValue(beneficiaryData.getAccountNumber()));
            jsonObject.put(Constants.IBAN_NUMBER, checkValue(beneficiaryData.getIBANNumber()));
            jsonObject.put(Constants.ACCOUNT_TYPE, checkValue(beneficiaryData.getAccountType()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Fetch Beneficiary Fields CE
     *
     * @param memPkId
     * @param transferType
     * @param countryCode
     * @param bankCode
     * @return
     */
    public JSONObject fetchBeneficiaryFieldsCe(String memPkId, String transferType, String countryCode, String bankCode, int beneficiaryType, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.DEST_COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.DEST_AGENT_CODE, bankCode);
            jsonObject.put(Constants.BENEFICIARY_TYPE, beneficiaryType);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBeneficiaryFieldsCe:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteBeneficiary(String userBankAccountId, String serviceType, String userPkId, String sessionTime, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, userBankAccountId);
            jsonObject.put(Constants.TYPE.toUpperCase(), serviceType);
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.DEVICE_ID, deviceId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }

    //BeneficiaryDropDowns
    public JSONObject fetchCeBeneficiaryBranch_44(String bankCode, String transferType, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCeBeneficiaryBranch_44:-  " + jsonObject.toString());
        return jsonObject;
    }

    //LoadDropDownList
    //BeneficiaryDropDowns
    //public JSONObject fetchDropDownList(String bankCode, String countryId) {
    public JSONObject fetchDropDownList(String BANK_ID, String FIELD_ID, String FIELD_KEY, String CURRENCY_ID, String COUNTRY_ID, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {


            /*


       {"COUNTRY_ID":"101",

       "GTW_BENF_MND_FIELD_PK_ID":0,
                    "BANK_ID":"5140",

                    "CURRENCY_ID":"19",
                    "FIELD_ID":"6",
                    "FIELD_KEY":"7"}


          */
            jsonObject.put(Constants.BANK_ID, BANK_ID);
            jsonObject.put(Constants.FIELD_ID, FIELD_ID);
            jsonObject.put("FIELD_KEY", FIELD_KEY);
            jsonObject.put(Constants.CURRENCY_ID, CURRENCY_ID);
            jsonObject.put(Constants.COUNTRY_ID, COUNTRY_ID);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


            // jsonObject.put("GTW_BENF_MND_FIELD_PK_ID", "0");




         /*   {
                "BANK_ID" : 1557,
                    "FIELD_ID" : "6",
                    "FIELD_KEY" : "7",
                    "CURRENCY_ID" : "26",
                    "COUNTRY_ID" : 106
            }*/

          /*  jsonObject.put(Constants.TYPE, "");
            jsonObject.put("MANDATORY", "");
            jsonObject.put("COUNTRY", "");
            jsonObject.put("CURRENCY", "");
            jsonObject.put("FIELD", "");
            jsonObject.put("VISIBLE", "");
            jsonObject.put("PREFIX", "");
            jsonObject.put("LENGTH", "");
            jsonObject.put("INPUT_TYPE","");*/









              /*
 {"COUNTRY_ID":"101",
                    "TYPE":null,
                    "CHANNEL_ID":null,
                    "MANDATORY":null,
                    "COUNTRY":null,
                    "GTW_BENF_MND_FIELD_PK_ID":0,
                    "CURRENCY":null,
                    "BANK_ID":"5140",
                    "FIELD":null,
                    "VISIBLE":null,
                    "PREFIX":null,
                    "LENGTH":null,
                    "CURRENCY_ID":"19",
                    "INPUT_TYPE":null,
                    "FIELD_ID":"6",
                    "FIELD_KEY":"7"}


          */


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCeBeneficiaryBranch_44:-  " + jsonObject.toString());
        return jsonObject;
    }


    // User Bank Account Management

    /**
     * Add User Bank Account
     *
     * @param userId
     * @param memPkId
     * @param accountName
     * @param bankCode
     * @param bankName
     * @param branchName
     * @param ibanNumber
     * @return
     */
    public JSONObject addUserBankAccount(String userId, String memPkId, String accountName, String bankCode, String bankName, String branchName, String ibanNumber, String userPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.ACCOUNT_NAME, accountName);
            jsonObject.put(Constants.ACCOUNT_NUMBER, "");
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.BANK_NAME, bankName);
            jsonObject.put(Constants.BANK_BRANCH_NAME, branchName);
            jsonObject.put(Constants.IBAN_NUMBER, ibanNumber);
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addUserBankAccount:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Update User Account
     *
     * @param userId
     * @param memPkId
     * @param accountPkId
     * @param accountName
     * @param bankCode
     * @param bankName
     * @param branchName
     * @param ibanNumber
     * @return
     */
    public JSONObject updateUserBankAccount(String userId, String memPkId, String accountPkId, String accountName, String bankCode, String bankName, String branchName, String ibanNumber, String userPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.ACCOUNT_PK_ID, accountPkId);
            jsonObject.put(Constants.ACCOUNT_NAME, accountName);
            jsonObject.put(Constants.ACCOUNT_NUMBER, "");
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.BANK_NAME, bankName);
            jsonObject.put(Constants.BANK_BRANCH_NAME, branchName);
            jsonObject.put(Constants.IBAN_NUMBER, ibanNumber);
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "updateUserBankAccount:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteUserBankAccount(String userBankAccountId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.ACCOUNT_PK_ID, userBankAccountId);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteUserBankAccount:-  " + jsonObject.toString());
        return jsonObject;
    }

    // Credit Card Management

    /**
     * Fetch Credit Card List
     *
     * @param userId
     * @return
     */
    public JSONObject fetchCreditCardList(String userId, String memPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCreditCardList:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteCreditCard(String creditCardId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_CC_PK_ID, creditCardId);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject fetchBankAccounts(String countryCode, String memberId, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put("MEM_CARD_FK_ID", memberId);
            jsonObject.put("BENEF_TYPE", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBankAccounts:-  " + jsonObject.toString());

        return jsonObject;
    }

    public JSONObject addCreditCard(String userId, String memPkId, String name, String cardNumber, String schemeName, String bankCode, String bankName, String paymentDate, String reminder, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.NAME, name);
            jsonObject.put(Constants.CARD_NUMBER, cardNumber);
            jsonObject.put(Constants.CARD_TYPE_CODE, "");
            jsonObject.put(Constants.CARD_TYPE_DESC, schemeName);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.BANK_NAME, bankName);
            jsonObject.put(Constants.PAYMENT_DATE, paymentDate);
            jsonObject.put(Constants.REMINDER, reminder);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject updateCreditCard(String userId, String memPkId, String creditCardId, String name, String cardNumber, String schemeName, String bankCode, String bankName, String paymentDate, String reminder, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.MEM_CC_PK_ID, creditCardId);
            jsonObject.put(Constants.NAME, name);
            jsonObject.put(Constants.CARD_NUMBER, cardNumber);
            jsonObject.put(Constants.CARD_TYPE_CODE, "");
            jsonObject.put(Constants.CARD_TYPE_DESC, schemeName);
            jsonObject.put(Constants.BANK_CODE, bankCode);
            jsonObject.put(Constants.BANK_NAME, bankName);
            jsonObject.put(Constants.PAYMENT_DATE, paymentDate);
            jsonObject.put(Constants.REMINDER, reminder);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchCreditCards(String memberId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.MEM_CARD_FK_ID, memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCreditCards:-  " + jsonObject.toString());

        return jsonObject;
    }

    // Currency Calculator

    /**
     * Calculate Currency For Currency Calculator
     *
     * @param fromCcy
     * @param toCcy
     * @param amount
     * @param transferType
     * @return
     */
    public JSONObject calculateCurrencyCalculator(String fromCcy, String toCcy, String amount, String transferType, String country, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.FROM_CCY, fromCcy);
            jsonObject.put(Constants.TO_CCY, toCcy);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.AMOUNT, amount);
            jsonObject.put(Constants.COUNTRY_CODE, country);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, checkValue(sessionTime));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencyCalculator:-  " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Foreign Currency
     *
     * @param fromCcy
     * @param fcyTyp
     * @param amount
     * @return
     */
    public JSONObject foreignCurrency(String fromCcy, String toCcy, String fcyTyp, String amount, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.FROM_CCY, fromCcy);
            jsonObject.put(Constants.TO_CCY, toCcy);
            jsonObject.put(Constants.FCY_TYP, fcyTyp);
            jsonObject.put(Constants.CCY_TYP, "0");
            jsonObject.put(Constants.AMOUNT, amount);

            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "foreignCurrency:-  " + jsonObject.toString());
        return jsonObject;
    }


    // Rate Alert
    public JSONObject fetchCurrentRate(String toCcy, String fromCcy, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.TO_CCY, toCcy);
            jsonObject.put(Constants.FROM_CCY, fromCcy);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchCurrentRate:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchSavedRateAlerts(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchSavedRateAlerts:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject saveRateAlert(String userId, String targetCurrencyCode, String targetCurrencyName, String fromCode, String fromName, String amount, String userPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.DESC_CCY_CODE, targetCurrencyCode);
            jsonObject.put(Constants.DESC_CCY_NAME, targetCurrencyName);
            jsonObject.put(Constants.FROM_CCY_CODE, fromCode);
            jsonObject.put(Constants.FROM_CCY_NAME, fromName);
            jsonObject.put(Constants.AMOUNT, amount);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userPkId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteRateAlert(String rateAlertId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.RATE_ALERT_PK_ID, rateAlertId);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject emailNotification(String userId, String email, String indication, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            //addCommonParameters(jsonObject);
            jsonObject.put(Constants.EMAIL, email);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.REM_TXN_PK_ID, "0");
            jsonObject.put(Constants.SERVICE_TYPE, null);
            jsonObject.put(Constants.EMAIL_ALERT_IND, indication);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject userPkId(String userId, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }


    // Branch Locator

    /**
     * Fetch Branch In A City
     *
     * @param cityId
     * @return
     */
    public JSONObject fetchBranchInCity(String cityId, String userId, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.ID.toUpperCase(), cityId);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchBranchInCity:-  " + jsonObject.toString());
        return jsonObject;
    }

    // Contact Us

    public JSONObject contactUs(String userId, String name, String mobile, String email, String subject, String message, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.NAME, checkValue(name));
            jsonObject.put(Constants.MOBILE, checkValue(mobile));
            jsonObject.put(Constants.EMAIL, checkValue(email));
            jsonObject.put(Constants.SUBJECT, checkValue(subject));
            jsonObject.put(Constants.MESSAGE, checkValue(message));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "contactUs:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject requestCallBack(String userId, String mobile) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_FK_ID, userId);
            jsonObject.put(Constants.MOBILE, checkValue(mobile));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "requestCallBack:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchAllBanks(String countryCode, String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put("COUNTRY_CODE", countryCode);
            jsonObject.put("NAME", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAllBanks:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchSubPurpose(String purposeId, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.PURPOSE_ID, purposeId);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchSubPurpose:-  " + jsonObject.toString());
        return jsonObject;
    }


    /* Western Union */

    /**
     * WU_SEND_MONEY
     */
    public JSONObject loadWuNumber(String userId, String arexMemPkId, String wuCardNo, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.WU_CARD_NO, wuCardNo);
            jsonObject.put(Constants.MODE_NAME, "");

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "loadWuNumber:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWUBeneficiary(String userId, String arexMemPkId, String wuCardNo, String startCount, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.WU_CARD_NO, wuCardNo);
            jsonObject.put(Constants.RECEIVER_CONTEXT_NUM, startCount);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWUBeneficiary:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject checkTxnLimitsWu(String charge, String countryCode, String arexUserId, String rate, String serviceType, String totalTxtAmt, String totalValueAed, String transferType, String txtAmt, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.CHARGE_AMOUNT, charge);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.MEM_PK_ID, arexUserId);
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, totalTxtAmt);
            jsonObject.put(Constants.TOTAL_VALUE_AED, totalValueAed);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.TXN_AMOUNT, txtAmt);
            jsonObject.put(Constants.USER_PK_ID, userId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "checkTxnLimitsWu:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject enrollWUCard(String userId, String arexMemPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.WU_CARD_NO, "");
            jsonObject.put(Constants.MODE_NAME, "ENROLL");

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "enrollWUCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject validateWuCard(String userId, String arexMemPkId, String wuCardNumber, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.WU_CARD_NO, wuCardNumber);
            jsonObject.put(Constants.MODE_NAME, "VALIDATE");

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "validateWuCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuCurrencyList(String countryCode, String wuCountryCode, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.WU_ISO_COUNTRY_CODE, wuCountryCode);

            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuCurrencyList:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuBeneficiaryFields(String userPkId, String wuCountryCode, String arexCountryCode, String serviceType, String isSecurityQueAvailable, String aedAmt, String isBeneficiary, String isTxt, String nameType, String benType, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.WU_COUNTRY_CODE, wuCountryCode);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, arexCountryCode);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.IS_SECURITY_QUESTION_AVAILABLE, isSecurityQueAvailable);
            jsonObject.put(Constants.AED_AMOUNT, aedAmt);
            jsonObject.put(Constants.IS_BENEFICIARY, isBeneficiary);
            jsonObject.put(Constants.IS_TXN, isTxt);
            jsonObject.put(Constants.NAME_TYPE, nameType);
            jsonObject.put(Constants.BENEF_TYPE, benType);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuBeneficiaryFields:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuBeneficiaryDetails(String userId, String arexUserId, String wuCardNumber, String receiverIndexNumber, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexUserId);
            jsonObject.put(Constants.WU_CARD_NO, wuCardNumber);
            jsonObject.put(Constants.RECEIVER_INDEX_NUM, receiverIndexNumber);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuBeneficiaryDetails:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchAdditionalInfoWu(String userId, String wuCountryCode, String arexCountryCode, String serviceType, String isSecurityQuestAvailable, String adeAmount, String isBen, String isTxn, String nameType, String benType, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.WU_COUNTRY_CODE, wuCountryCode);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, arexCountryCode);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.IS_SECURITY_QUESTION_AVAILABLE, isSecurityQuestAvailable);
            jsonObject.put(Constants.AED_AMOUNT, adeAmount);
            jsonObject.put(Constants.IS_BENEFICIARY, isBen);
            jsonObject.put(Constants.IS_TXN, isTxn);
            jsonObject.put(Constants.NAME_TYPE, nameType);
            jsonObject.put(Constants.BENEF_TYPE, benType);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAdditionalInfoWu:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuSourceOfFunds(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuSourceOfFunds:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuServices(String countryCode, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuServices:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuCity(String code, String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.COUNTRY_CODE, code);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuCity:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchWuCountryInfo(String userId, String arexUserId, String receiverCountryCode, String currencyCode, String wu, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexUserId);
            jsonObject.put(Constants.WU_COUNTRY_CODE, receiverCountryCode);
            jsonObject.put(Constants.WU_CCY_CODE, currencyCode);
            jsonObject.put(Constants.SERVICE_TYPE, wu);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchWuCountryInfo:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject wuPerformSendMoney(String USER_PK_ID, String AREX_MEM_PK_ID, String WU_COUNTRY_CODE, String AREX_COUNTRY_CODE, String WU_SERVICE_TYPE, String WU_CCY_CODE,
                                         String AREX_CCY_CODE, String WU_PARAM_REF_NO, String SERVICE_TYPE, String TRANSFER_TYPE, String MODE_PK_ID, String MODE_NAME,
                                         String MODE_DESCRIPTION, String TOTAL_VALUE_AED, String TOTAL_NET_AMOUNT, String TXN_AMOUNT, String RATE, String CHARGE_AMOUNT,
                                         String VAT_CHARGES, String PROMO_DISCOUNT_AMOUNT, long TIME_STAMP, String PGS_FLAG, String PGS_BANK_CODE, String TERMS_AND_CONDITION_FLAG,
                                         String WU_PROMO_CODE, String TAX_RATE, String TAX_WORKSHEET, String IS_WU_LOOKUP_PROMO_CODE_SELECTED, String FEE_ENQ_TXN_TYPE,
                                         String WU_LOOKUP_PROMO_CODE, String BENEF_PK_ID, String TOTAL_WU_POINTS, String TEST_QUESTION_FLAG, String BENEF_TYPE, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, USER_PK_ID);
            jsonObject.put(Constants.AREX_MEM_PK_ID, AREX_MEM_PK_ID);
            jsonObject.put(Constants.WU_COUNTRY_CODE, WU_COUNTRY_CODE);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, AREX_COUNTRY_CODE);
            jsonObject.put(Constants.WU_SERVICE_TYPE, WU_SERVICE_TYPE);
            jsonObject.put(Constants.WU_CCY_CODE, WU_CCY_CODE);
            jsonObject.put(Constants.AREX_CCY_CODE, AREX_CCY_CODE);
            jsonObject.put(Constants.WU_PARAM_REF_NO, WU_PARAM_REF_NO);
            jsonObject.put(Constants.SERVICE_TYPE, SERVICE_TYPE);
            jsonObject.put(Constants.TRANSFER_TYPE, TRANSFER_TYPE);
            jsonObject.put(Constants.MODE_PK_ID, MODE_PK_ID);
            jsonObject.put(Constants.MODE_NAME, MODE_NAME);
            jsonObject.put(Constants.MODE_DESCRIPTION, MODE_DESCRIPTION);
            jsonObject.put(Constants.TOTAL_VALUE_AED, TOTAL_VALUE_AED);
            jsonObject.put(Constants.TOTAL_NET_AMOUNT, TOTAL_NET_AMOUNT);
            jsonObject.put(Constants.TXN_AMOUNT, TXN_AMOUNT);
            jsonObject.put(Constants.RATE, RATE);
            jsonObject.put(Constants.CHARGE_AMOUNT, CHARGE_AMOUNT);
            jsonObject.put(Constants.VAT_CHARGES, VAT_CHARGES);
            jsonObject.put(Constants.PROMO_DISCOUNT_AMOUNT, PROMO_DISCOUNT_AMOUNT);
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.TIME_STAMP, TIME_STAMP);
            jsonObject.put(Constants.PGS_FLAG, PGS_FLAG);
            jsonObject.put(Constants.PGS_BANK_CODE, PGS_BANK_CODE);
            jsonObject.put(Constants.TERMS_AND_CONDITION_FLAG, TERMS_AND_CONDITION_FLAG);
            jsonObject.put(Constants.WU_PROMO_CODE, WU_PROMO_CODE);
            jsonObject.put(Constants.TAX_RATE, TAX_RATE);
            jsonObject.put(Constants.TAX_WORKSHEET, TAX_WORKSHEET);
            jsonObject.put(Constants.IS_WU_LOOKUP_PROMO_CODE_SELECTED, IS_WU_LOOKUP_PROMO_CODE_SELECTED);
            jsonObject.put(Constants.FEE_ENQ_TXN_TYPE, FEE_ENQ_TXN_TYPE);
            jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, WU_LOOKUP_PROMO_CODE);
            jsonObject.put(Constants.BENEF_PK_ID, BENEF_PK_ID);
            jsonObject.put(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
            jsonObject.put(Constants.TEST_QUESTION_FLAG, TEST_QUESTION_FLAG);
            jsonObject.put(Constants.BENEF_TYPE, BENEF_TYPE);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "wuPerformSendMoney:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject wuBankTransfer(String CHARGE_AMOUNT, String AAE_ACCOUNT_NAME, String FEE_ENQ_TXN_TYPE, String AAE_ACCOUNT_NUM, String PGS_FLAG, String RATE, String WU_SERVICE_TYPE,
                                     String BENEF_TYPE, String TOTAL_VALUE_AED, String WU_PROMO_CODE, String BENEF_PK_ID, long TIME_STAMP, String IS_WU_LOOKUP_PROMO_CODE_SELECTED,
                                     String MODE_PK_ID, String AREX_MEM_PK_ID, String WU_CCY_CODE, String AREX_CCY_CODE, String TAX_RATE, String USER_PK_ID, String ACC_FK_ID,
                                     String MODE_NAME, String MODE_DESCRIPTION, String WU_PARAM_REF_NO, String USER_ACC_NUM, String SERVICE_TYPE, String TAX_WORKSHEET,
                                     String ACCOUNT_HOLDER_NAME, String PGS_BANK_CODE, String TEST_QUESTION_FLAG, String AREX_CODE, String TOTAL_NET_AMOUNT, String WU_COUNTRY_CODE,
                                     String VAT_CHARGES, String TXN_AMOUNT, String TRANSFER_TYPE, String AAE_BANK_NAME, String AREX_COUNTRY_CODE, String TOTAL_WU_POINTS, String WU_LOOKUP_PROMO_CODE, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHARGE_AMOUNT, CHARGE_AMOUNT);
            jsonObject.put(Constants.AAE_ACCOUNT_NAME, AAE_ACCOUNT_NAME);
            jsonObject.put(Constants.FEE_ENQ_TXN_TYPE, FEE_ENQ_TXN_TYPE);
            jsonObject.put(Constants.AAE_ACCOUNT_NUM, AAE_ACCOUNT_NUM);
            jsonObject.put(Constants.PGS_FLAG, PGS_FLAG);
            jsonObject.put(Constants.RATE, RATE);
            jsonObject.put(Constants.WU_SERVICE_TYPE, WU_SERVICE_TYPE);
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.BENEF_TYPE, BENEF_TYPE);
            jsonObject.put(Constants.TOTAL_VALUE_AED, TOTAL_VALUE_AED);
            jsonObject.put(Constants.WU_PROMO_CODE, WU_PROMO_CODE);
            jsonObject.put(Constants.BENEF_PK_ID, BENEF_PK_ID);
            jsonObject.put(Constants.TIME_STAMP, TIME_STAMP);
            jsonObject.put(Constants.IS_WU_LOOKUP_PROMO_CODE_SELECTED, IS_WU_LOOKUP_PROMO_CODE_SELECTED);
            jsonObject.put(Constants.MODE_PK_ID, MODE_PK_ID);
            jsonObject.put(Constants.AREX_MEM_PK_ID, AREX_MEM_PK_ID);
            jsonObject.put(Constants.WU_CCY_CODE, WU_CCY_CODE);
            jsonObject.put(Constants.AREX_CCY_CODE, AREX_CCY_CODE);
            jsonObject.put(Constants.TAX_RATE, TAX_RATE);
            jsonObject.put(Constants.USER_PK_ID, USER_PK_ID);
            jsonObject.put(Constants.ACC_FK_ID, ACC_FK_ID);
            jsonObject.put(Constants.MODE_NAME, MODE_NAME);
            jsonObject.put(Constants.MODE_DESCRIPTION, MODE_DESCRIPTION);
            jsonObject.put(Constants.WU_PARAM_REF_NO, WU_PARAM_REF_NO);
            jsonObject.put(Constants.USER_ACC_NUM, USER_ACC_NUM);
            jsonObject.put(Constants.SERVICE_TYPE, SERVICE_TYPE);
            jsonObject.put(Constants.TAX_WORKSHEET, TAX_WORKSHEET);
            jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, ACCOUNT_HOLDER_NAME);
            jsonObject.put(Constants.PGS_BANK_CODE, PGS_BANK_CODE);
            jsonObject.put(Constants.TEST_QUESTION_FLAG, TEST_QUESTION_FLAG);
            jsonObject.put(Constants.AREX_CODE, AREX_CODE);
            jsonObject.put(Constants.TOTAL_NET_AMOUNT, TOTAL_NET_AMOUNT);
            jsonObject.put(Constants.WU_COUNTRY_CODE, WU_COUNTRY_CODE);
            jsonObject.put(Constants.VAT_CHARGES, VAT_CHARGES);
            jsonObject.put(Constants.TXN_AMOUNT, TXN_AMOUNT);
            jsonObject.put(Constants.TRANSFER_TYPE, TRANSFER_TYPE);
            jsonObject.put(Constants.AAE_BANK_NAME, AAE_BANK_NAME);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, AREX_COUNTRY_CODE);
            jsonObject.put(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
            jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, WU_LOOKUP_PROMO_CODE);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "wuBankTransfer:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject appVersion() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "appVersion:-  " + jsonObject.toString());
        return jsonObject;
    }
// Siddu..
    public JSONObject fetchAds(String userId, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (Validation.isValidString(userId))
                jsonObject.put(Constants.USER_PK_ID, userId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAds:-  " + jsonObject.toString());
        return jsonObject;
    }



    // Siddu..
    public JSONObject fetchAdsAfterLogin(String userId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (Validation.isValidString(userId))
                jsonObject.put(Constants.USER_PK_ID, userId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put(Constants.SESSION_ID,sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAds:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchAdsNew(String userId, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (Validation.isValidString(userId))
                jsonObject.put(Constants.USER_PK_ID, userId);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            jsonObject.put(Constants.DEVICE_OS, "ANDROID");




        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAds:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject pendingTransaction(String userId, String startCount, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.STATUS, "P");
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.TXN_TYPE_TYPE, "");
            jsonObject.put(Constants.FROM_RECORD, startCount);
            jsonObject.put(Constants.CHANNEL_ID, "M");
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "pendingTransaction:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchPopUps(String userId, String deviceId, String b, String mobile) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (CommonUtils.isLoggedIn()) {
                if (Validation.isValidString(userId))
                    if (userId == null || userId.equalsIgnoreCase("")) {
                        jsonObject.put(Constants.USER_PK_ID, "");
                    } else {
                        jsonObject.put(Constants.USER_PK_ID, userId);
                    }
                jsonObject.put(Constants.DEVICE_ID, deviceId);
                jsonObject.put(Constants.LOGIN_TYPE, b);
                if (mobile == null || mobile.equalsIgnoreCase("")) {
                    jsonObject.put(Constants.MOBILE_NUM, "");
                } else {
                    jsonObject.put(Constants.MOBILE_NUM, mobile);
                }
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAds:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject getingDataInformation(String userId, String arexMemPkId, String ceMemPkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_FK_ID, arexMemPkId);
            jsonObject.put(Constants.CE_MEM_FK_ID, checkValue(ceMemPkId));

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencyCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject userPkIdSessionOut(String userId, String sessionTime, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject addingParameters(String userId, String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject customerProfile(String emailId, String workCompanyName, String userName, String userPkId, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EMAIL_ID, emailId);
            jsonObject.put(Constants.WORK_COMPANY_NAME, workCompanyName);
            jsonObject.put(Constants.USER_NAME_CAP, userName);
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }

// Siddu..
    public JSONObject dynamicUrls(String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "dynamicUrls:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchTravelCardList(String userId, String arexMemFkId, String deviceID, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_FK_ID, arexMemFkId);
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "dynamicUrls:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject addTravelCardReload(String userId, String arexFkId, String name, String cardNumber, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_FK_ID, arexFkId);
            jsonObject.put(Constants.CARD_HOLDER_NAME, name);
            jsonObject.put(Constants.CARD_NUMBER, cardNumber);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject addTravelCard(String userId, String name, String cardNumber, String deviceID, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.CARD_HOLDER_NAME, name);
            jsonObject.put(Constants.CARD_NUMBER, cardNumber);
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject deleteTravelCardReload(String userId, String cardNumber, String cardName, String arexFkId, String requestMode, String authMemFkId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.CARD_NUMBER, cardNumber);
            jsonObject.put(Constants.CARD_HOLDER_NAME, cardName);
            jsonObject.put(Constants.AREX_MEM_FK_ID, arexFkId);
            jsonObject.put(Constants.REQUEST_MODE, requestMode);
            jsonObject.put(Constants.AUTH_REP_MEM_FK_ID, authMemFkId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;


    }

    public JSONObject validateWireCard(String userPkId, String wcPkId, String deviceID, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.WC_PK_ID, wcPkId);

            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;

    }

    public JSONObject fetchWireCardBalance(String userPkId, String arexId, String wcPkId, String deviceID, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexId);
            jsonObject.put(Constants.WC_PK_ID, wcPkId);
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addCreditCard:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject loadTravelcard(String userId, String arexUserId, String deviceID, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexUserId);
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "loadWuNumber:-  " + jsonObject.toString());
        return jsonObject;
    }




    public JSONObject submitReferenceNumTravelCardApi(String userId, String txnId, String payModeName, String serviceType, String referenceNumber, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.REM_TXN_PK_ID, checkValue(txnId));
            jsonObject.put(Constants.MODE_NAME, "BT");

            jsonObject.put(Constants.SERVICE_TYPE, "WC");
            jsonObject.put(Constants.BANK_REFERENCE_NUMBER, checkValue(referenceNumber));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "submitReferenceNumRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;

    }

    public JSONObject transactionTrackerPre(String referenceNumber , String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.TXN_REF_NUMBER, checkValue(referenceNumber));
            jsonObject.put(Constants.DEVICE_ID, deviceID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "submitReferenceNumRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject transactionTrackerPost(String userId,String arexFkId,String ceMemPkId,String referenceNumber,String sessionTime, String deviceID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_FK_ID, arexFkId);
            jsonObject.put(Constants.CE_MEM_FK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.TXN_REF_NUMBER, checkValue(referenceNumber));
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.DEVICE_ID, deviceID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "submitReferenceNumRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchTransactionsRemittanceApi2(String userId, String status, String type, String startCount,String CePaginationNo, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.STATUS, checkValue(status));
            //jsonObject.put(Constants.FROM_RECORD, checkValue(startCount));
            jsonObject.put(Constants.AREX_PAGINATION_NO,checkValue(startCount));
            jsonObject.put(Constants.CE_PAGINATION_NO,checkValue(CePaginationNo));
            jsonObject.put(Constants.TYPE.toUpperCase(), checkValue(type));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchTransactionsRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject fetchTransactionsRemittanceApi3(String userId, String status, String type, String startCount,String CePaginationNo, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.STATUS, checkValue(status));
            jsonObject.put(Constants.FROM_RECORD, checkValue(startCount));
            jsonObject.put(Constants.AREX_PAGINATION_NO,checkValue(startCount));
            jsonObject.put(Constants.CE_PAGINATION_NO,checkValue(CePaginationNo));
            jsonObject.put(Constants.TYPE.toUpperCase(), checkValue(type));
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchTransactionsRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }
    public JSONObject fetchServiceType(String userId,String memPkId, String memBenfId, String CountryCode,String deviceID, String sessionTimeAll) {
        JSONObject jsonObject = new JSONObject();
        try {
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.SERVICE_TYPE, checkValue("CE"));
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.MEM_PK_ID,checkValue(memPkId));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(memBenfId));
            jsonObject.put(Constants.COUNTRY_CODE,checkValue(CountryCode));
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTimeAll);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchTransactionsRemittanceApi:-  " + jsonObject.toString());
        return jsonObject;
    }

    public JSONObject fetchPaymentModeSelectPrelogin(String txnAmount, String transferType, String totalValueAED, String ccyCode,
                                                     String rate, String servicetype,String screenType, String deviceId, String countryCode){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.TXN_AMOUNT, txnAmount);
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED));
            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.SERVICE_TYPE,servicetype);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, "0");
            jsonObject.put(Constants.SCREEN_TYPE, screenType);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;

    }

    public JSONObject fetchPaymentModeSelectPostlogin(String memPkId, String totalValueAED, String rate,String screenType,String txnAmount, String transferType,String countryCode, String ccyCode,
                                                      String servicetype,String userId, String deviceID,String sessionTime ){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MEM_PK_ID,checkValue(memPkId));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(totalValueAED));
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, "0");
            jsonObject.put(Constants.SCREEN_TYPE, screenType);
            jsonObject.put(Constants.TXN_AMOUNT, txnAmount);
            addCommonParameters(jsonObject);
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(transferType));
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.SERVICE_TYPE,servicetype);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;

    }

    public JSONObject referralPopup(String mobileNum, String userId,String deviceID,String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.MOBILE_NUM,checkValue(mobileNum));
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.DEVICE_ID, deviceID);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
            addCommonParameters(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchPaymentModes:-  " + jsonObject.toString());
        return jsonObject;
    }
    //MY_PROFILE_TEMPLATE
    public JSONObject myProfile(String userId, String sessionTime, String deviceId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "deleteRateAlert:-  " + jsonObject.toString());
        return jsonObject;
    }
}


