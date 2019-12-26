package app.alansari.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import app.alansari.AppController;
import app.alansari.ChangePinActivity;
import app.alansari.DashboardActivity;
import app.alansari.LoginActivity;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.fcm.FCMUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.preferences.SharedPreferenceManger;

/**
 * Created by Parveen Dala on 01 November, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class RegisterEmailFragment extends Fragment implements OnWebServiceResult, View.OnClickListener {

    private View view;
    private Intent intent;
    private Context context;
    private String mobileNum;
    private EditText etEmail, etPassword;
    //Temp Var
    private int reqCodeAdd = 192;
    private JSONObject response;

     private  Context mContext;

    private FirebaseAnalytics mFirebaseAnalytics;
    /**
     * User E Exchange registration SOAP.
     */
    private void eExRegisterSoap() {
        SoapObject request = new SoapObject(Constants.NAME_SPACE, Constants.REGISTRATION_EEXCHANGE_METHOD);
        request.addProperty("username", etEmail.getText().toString().trim());
        request.addProperty("password", etPassword.getText().toString().trim());
        request.addProperty("channel", Constants.CHANNEL_ID_VALUE);
        CallAddr.callWSThreadSoapPrimitive(Constants.REGISTRATION_EEXCHANGE_SOAP_URL, Constants.SOAP_ACTION + Constants.REGISTRATION_EEXCHANGE_METHOD, request, CommonUtils.SERVICE_TYPE.REGISTER_EEXCHANGE_SOAP, this);
        CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.REGISTER_EEXCHANGE_SOAP.toString(), false);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case REGISTER_EEXCHANGE_SOAP:
                try {
                    switch (status) {
                        case 1000:
                            mobileNum = response.getString(Constants.STATUS_MSG);
                            //new FCMUtils().sendRegistrationToServer(context, CommonUtils.getFCMToken());
                            mobileRegister();
                            break;
                        case 1001:
                        case 1002:
                        case 1003:
                            Toast.makeText(context, response.getString(Constants.STATUS_MSG), Toast.LENGTH_SHORT).show();
                            break;
                        case 0:
                        default:
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            case REGISTRATION_EEXCHANGE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            mobileNum = response.getString(Constants.OTP);
                            String userStatus = response.getString(Constants.USER_STATUS);
                            CommonUtils.setUserStatus(userStatus);
                            new FCMUtils().sendRegistrationToServer(context, CommonUtils.getFCMToken());
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                                if (userData != null) {
                                   // CommonUtils.setUserData(userData);
                                    new CommonUtils().setUserData(userData);
                                    mobileNum = userData.getMobileNum();

                                    if (CommonUtils.isLoggedIn() && CommonUtils.getUserMobile().equalsIgnoreCase(mobileNum)) {

                                        mFirebaseAnalytics.logEvent("Login", null);
                                        intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().finish();
                                        return;
                                    }
                                }
                            }
                            switch (userStatus) {
                                case Constants.EUVDN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.EUVDN_CODE);
                                    break;
                                case Constants.EGNAV:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.EGNAV_CODE);
                                    break;
                                case Constants.UVDV:
                                    goToSetPIN(Constants.UVDV_CODE);
                                    break;
                                case Constants.UVDN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.UVDN_CODE);
                                    break;
                                case Constants.GVAN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GVAN_CODE);
                                    break;
                                case Constants.GNAN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GNAN_CODE);
                                    break;
                                case Constants.GNAV:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GNAV_CODE);
                                    break;
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_invalid_user_credentials), Toast.LENGTH_SHORT).show();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_unable_to_send_otp), Toast.LENGTH_SHORT).show();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.EXCEPTION)) {
                                CommonUtils.alertBox(context, response.getString(Constants.MESSAGE));
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
            case REGISTER_EX_MOBILE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            String userStatus = response.getString(Constants.USER_STATUS);
                            CommonUtils.setUserStatus(userStatus);
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                                if (userData != null) {
                                    //CommonUtils.setUserData(userData);
                                    new CommonUtils().setUserData(userData);
                                    mobileNum = userData.getMobileNum();
                                }
                            }
                            switch (userStatus) {
                                case Constants.UVDV:
                                    goToSetPIN(Constants.UVDV_CODE);
                                    break;
                                case Constants.EUVDN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.EUVDN_CODE);
                                    break;
                                case Constants.EGNAV:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.EGNAV_CODE);
                                    break;
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * User Mobile registration.
     */
    private void mobileRegister() {
        String username = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().registerMobile(username, CommonUtils.getDeviceDetails(context), mobileNum, "1"), Constants.REGISTRATION_MOBILE_URL, CommonUtils.SERVICE_TYPE.REGISTER_EX_MOBILE, Request.Method.PUT, this);
        AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REGISTER_EX_MOBILE.toString());
        CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.REGISTER_EX_MOBILE.toString(), false);
    }

    private void goToVerifyOTP(String userStatus, String otp, int reqCode) {
        // Toast.makeText(context, "OTP:- " + otp, Toast.LENGTH_LONG).show();
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, userStatus);
        intent.putExtra(Constants.PIN, otp);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, reqCode);
    }

    private void goToSetPIN(int requestCode) {
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.USER_STATUS, CommonUtils.getUserStatus());
        intent.putExtra(Constants.TYPE, Constants.SETUP_PIN);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.EUVDN_CODE || requestCode == Constants.EGNAV_CODE) {
                goToSetPIN(requestCode + reqCodeAdd);
            } else if (requestCode == Constants.UVDV_CODE || requestCode == Constants.EUVDN_CODE + reqCodeAdd || requestCode == Constants.EGNAV_CODE + reqCodeAdd) {
                goToHome();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_exchange_layout, container, false);
        mContext = getActivity();
        assert mContext != null;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        init();
    }

    private void init() {
        etEmail = (EditText) view.findViewById(R.id.reg_email);
        etPassword = (EditText) view.findViewById(R.id.reg_password);
        view.findViewById(R.id.reg_submit_btn).setOnClickListener(this);
        view.findViewById(R.id.reg_back_btn).setOnClickListener(this);
        // etEmail.setText("sulthan.r@aol.com");
        // etPassword.setText("Test1234");

    }

    private void goToHome() {
        SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
        intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_submit_btn:
                if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    etEmail.setError(getString(R.string.error_enter_valid_exid));
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                    etPassword.setError(getString(R.string.error_enter_valid_password));
                    etPassword.requestFocus();
                } else {
                    eExchangeRegister();
                }
                break;
            case R.id.reg_back_btn:
                getActivity().onBackPressed();
                break;
        }
    }

    /**
     * User E Exchange registration.
     */
    private void eExchangeRegister() {
        String username = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().registerEexchange(username, CommonUtils.getDeviceDetails(context), etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), "1"), Constants.REGISTRATION_EEXCHANGE_URL, CommonUtils.SERVICE_TYPE.REGISTRATION_EEXCHANGE, Request.Method.PUT, this);
        AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REGISTRATION_EEXCHANGE.toString());
        CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.REGISTRATION_EEXCHANGE.toString(), false);
    }
}