package app.alansari.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import app.alansari.AppController;
import app.alansari.ChangePinActivity;
import app.alansari.DashboardActivity;
import app.alansari.GoToBranchActivity;
import app.alansari.LoginActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.VerifyEIDActivity;
import app.alansari.fcm.FCMUtils;
import app.alansari.keypadview.AsteriskPasswordTransformationMethod;
import app.alansari.keypadview.BigButtonView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.newAdditions.CustomerProfile;
import app.alansari.preferences.SharedPreferenceManger;

/**
 * Created by Parveen Dala on 01 November, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class RegisterMobileFragment extends Fragment implements OnWebServiceResult, BigButtonView.OnPressListener, View.OnClickListener {

    private View view;
    private Intent intent;
    private Context context;
    private String mobileNum;
    private int reqCodeAdd = 192;
    private ProgressDialog pDialog;
    private ObjectAnimator objectAnimator, objectAnimator2;

    // Register Mobile

    /**
     * Key Pad
     */
    private int passwordLength = 9;
    private BigButtonView[] bigButtonViews;
    private TextView leftButton, rightButton;
    private TextView tvMobile;
    private Button btnGo;
    private AsteriskPasswordTransformationMethod pwdMethod;
    private String GNAV_MESSAGE = "";
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * From the button views.
     *
     * @param string The string from button views.
     */
    @Override
    public void onPress(String string) {
        if (tvMobile != null && tvMobile.getText().length() == 0 && string.equalsIgnoreCase("0"))
            return;
        if (tvMobile != null && tvMobile.getText().length() < passwordLength) {
            if (Validation.isValidTextViewValue(tvMobile))
                tvMobile.setText(tvMobile.getText() + string);
            else
                tvMobile.setText(string);
            checkMobileStatus();
        }
    }

    private void checkMobileStatus() {
        if (tvMobile != null) {
            if (tvMobile.getText().length() == passwordLength) {
                btnGo.setVisibility(View.VISIBLE);
            } else {
                btnGo.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        try {
            CommonUtils.hideLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (sType) {
            case REGISTER_MOBILE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                            String userStatus = response.getString(Constants.USER_STATUS);
                            String message = response.getString(Constants.MESSAGE);
                            String statusCode = response.getString(Constants.STATUS_CODE);
                            LogUtils.v("message", message);
                            LogUtils.v("userStatus", userStatus);
                            if (statusCode.equalsIgnoreCase("698") || statusCode.equalsIgnoreCase("699") || statusCode.equalsIgnoreCase("697")) {
                                SharedPreferenceManger.setMessage(message);
                            }
                            CommonUtils.setUserStatus(userStatus);
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                                if (userData != null) {
                                    SharedPreferenceManger.setPrefVal(Constants.PIN, "", SharedPreferenceManger.VALUE_TYPE.STRING);
                                    // CommonUtils.setUserData(userData);
                                    new CommonUtils().setUserData(userData);
                                    mobileNum = userData.getMobileNum();
                                }
                            }


                            switch (userStatus) {
                                case Constants.UVDV:
                                    goToSetPIN(Constants.UVDV_CODE, message);
                                    break;
                                case Constants.UVDN:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.UVDN_CODE, message);
                                    break;
                                case Constants.GVAN:
                                    //  Toast.makeText(context, "GVAN", Toast.LENGTH_SHORT).show();
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GVAN_CODE, message);
                                    break;
                                case Constants.GNAN:
                                    GNAV_MESSAGE = message;
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GNAN_CODE, message);
                                    break;
                                case Constants.GNAV:
                                    goToVerifyOTP(userStatus, response.getString(Constants.OTP), Constants.GNAV_CODE, message);
                                    break;
                            }



                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            if (context != null)
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.EXCEPTION)) {
                            if (context != null)
                                //CommonUtils.alertBox(context,response.getString(Constants.MESSAGE));
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else {
                            if (context != null)
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        if (context != null)
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    if (context != null)
                        Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void goToSetPIN(int requestCode, String message) {
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.USER_STATUS, CommonUtils.getUserStatus());
        intent.putExtra(Constants.TYPE, Constants.SETUP_PIN);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        intent.putExtra(Constants.MESSAGE, message);
        startActivityForResult(intent, requestCode);
    }

    private void goToVerifyOTP(String userStatus, String otp, int reqCode, String message) {
        //Toast.makeText(context, "OTP:- " + otp, Toast.LENGTH_LONG).show();
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, userStatus);
        intent.putExtra(Constants.MESSAGE, message);
        intent.putExtra(Constants.PIN, otp);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, reqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.UVDV_CODE) {
                goToHome();
            } else if (requestCode == Constants.UVDN_CODE || requestCode == Constants.GNAV_CODE) {
                //goToVerifyEID();
                //AddFcmMethod()
                new FCMUtils().sendRegistrationToServer(context, CommonUtils.getFCMToken());
                goToVerifyEID();

//            } else if (requestCode == Constants.GVAN_CODE || requestCode == Constants.GNAN_CODE) {
            } else if (requestCode == Constants.GVAN_CODE) {
                goToSetPIN(requestCode + reqCodeAdd, "");
            } else if ((requestCode == Constants.GVAN_CODE + reqCodeAdd) || (requestCode == Constants.GNAN_CODE + reqCodeAdd)) {
                String message = data.getExtras().getString(Constants.MESSAGE);
                goToBranch(message);

            } else if (requestCode == Constants.GNAN_CODE) {
                Intent intent = new Intent(context, CustomerProfile.class);
                intent.putExtra("message", GNAV_MESSAGE);
                intent.putExtra(Constants.TYPE, Constants.SETUP_PIN);
                intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
                intent.putExtra(Constants.USER_STATUS, CommonUtils.getUserStatus());
                startActivityForResult(intent, 2000);
            } else if (requestCode == 2000) {
                goToSetPIN(Constants.GNAN_CODE + reqCodeAdd, "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(app.alansari.R.layout.register_mobile_layout, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        return view;
    }

    @Override
    public void onAttach(Context mcontext) {
        super.onAttach(mcontext);
        context = mcontext;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // context = getActivity();
        init();
    }    /**
     * User registration.
     */
    private void mobileRegister() {

        //TODO Remove Dummy Data
//        CommonUtils.dummyLogin(context);
//        goToSetPIN(Constants.UVDV_CODE);

        //TODO Uncomment


        String number = String.valueOf(tvMobile.getText().toString().trim().charAt(0));
        if (number.equalsIgnoreCase("5")) {
            String username = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().registerMobile(username, CommonUtils.getDeviceDetails(context), mobileNum, "0"), Constants.REGISTRATION_MOBILE_URL, CommonUtils.SERVICE_TYPE.REGISTER_MOBILE, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REGISTER_MOBILE.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REGISTER_MOBILE.toString(), false);
        } else {
            Toast.makeText(context, "Please Enter UAE Number", Toast.LENGTH_SHORT).show();
            //CommonUtils.alertBox(context,"Please Enter UAE Number");
        }
        // if (stringBuilder) {

    }/* else {

            String username = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().registerMobile(username, CommonUtils.getDeviceDetails(context), mobileNum, "0"), Constants.REGISTRATION_MOBILE_URL, CommonUtils.SERVICE_TYPE.REGISTER_MOBILE, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REGISTER_MOBILE.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REGISTER_MOBILE.toString(), false);
        }*/
    //}

    private void init() {
        //Mobile
        btnGo = (Button) view.findViewById(app.alansari.R.id.btn_go);
        tvMobile = (TextView) view.findViewById(app.alansari.R.id.mobile);
        pwdMethod = new AsteriskPasswordTransformationMethod();
        btnGo.setOnClickListener(this);

        // Number password
        bigButtonViews = new BigButtonView[10];
        bigButtonViews[0] = (BigButtonView) view.findViewById(app.alansari.R.id.button_0);
        bigButtonViews[1] = (BigButtonView) view.findViewById(app.alansari.R.id.button_1);
        bigButtonViews[2] = (BigButtonView) view.findViewById(app.alansari.R.id.button_2);
        bigButtonViews[3] = (BigButtonView) view.findViewById(app.alansari.R.id.button_3);
        bigButtonViews[4] = (BigButtonView) view.findViewById(app.alansari.R.id.button_4);
        bigButtonViews[5] = (BigButtonView) view.findViewById(app.alansari.R.id.button_5);
        bigButtonViews[6] = (BigButtonView) view.findViewById(app.alansari.R.id.button_6);
        bigButtonViews[7] = (BigButtonView) view.findViewById(app.alansari.R.id.button_7);
        bigButtonViews[8] = (BigButtonView) view.findViewById(app.alansari.R.id.button_8);
        bigButtonViews[9] = (BigButtonView) view.findViewById(app.alansari.R.id.button_9);
        String[] texts = getResources().getStringArray(app.alansari.R.array.default_big_button_text);
        for (int i = 0; i < bigButtonViews.length; i++) {
            bigButtonViews[i].setOnPressListener(this);
            bigButtonViews[i].setText(texts[i]);
        }

        leftButton = (TextView) view.findViewById(app.alansari.R.id.left_button);
        leftButton.setText("Back");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        rightButton = (TextView) view.findViewById(app.alansari.R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateDeletePress();
            }
        });
    }

    /**
     * onDelete Press
     */
    public void simulateDeletePress() {
        if (!TextUtils.isEmpty(tvMobile.getText().toString().trim())) {
            tvMobile.setText(tvMobile.getText().toString().substring(0, tvMobile.getText().length() - 1));
            checkMobileStatus();
        }
    }

    private void goToHome() {
        try {
            SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goToVerifyEID() {
        intent = new Intent(context, VerifyEIDActivity.class);
        intent.putExtra(Constants.TYPE, Constants.SETUP_PIN);
        intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
        intent.putExtra(Constants.USER_STATUS, CommonUtils.getUserStatus());
        startActivity(intent);
    }

    private void goToBranch(String message) {
        try {
            SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            mFirebaseAnalytics.logEvent("Reference_Number_Screen", null);
            Log.i("Reference_Number_Screen", "Success in Login Screen");

            intent = new Intent(context, GoToBranchActivity.class);
            intent.putExtra(Constants.MESSAGE, message);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.btn_go:
                if (tvMobile.getText().length() == passwordLength) {
                    mobileNum = Constants.MOBILE_CODE + tvMobile.getText().toString().trim();
                    if (CommonUtils.isLoggedIn() && CommonUtils.getUserMobile().equalsIgnoreCase(mobileNum)) {
                        intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    } else
                        mobileRegister();
                }
                break;
        }
    }
}