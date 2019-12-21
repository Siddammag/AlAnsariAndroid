package app.alansari;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Stack;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.fcm.FCMUtils;
import app.alansari.keypadview.BigButtonView;
import app.alansari.listeners.OnFingerprintAuthentication;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.FetchEmailData;
import app.alansari.models.UserData;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.modules.sendmoney.PendingTransActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.preferences.SharedPreferenceManger.VALUE_TYPE;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_EMAIL_NOTIFICATION;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, BigButtonView.OnPressListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private static final String KEY_NAME = "alAnsari_Key";
    JSONObject userDetails;
    String userEmailId = "";
    private Intent intent;
    private Context context;
    private Button btnChange;
    private View pinTextLayout, pinLayout;
    private TextView tvMobile, tvForgetPin;
    private ImageView ivPin1, ivPin2, ivPin3, ivPin4;
    private TextView tvPin1, tvPin2, tvPin3, tvPin4, mCurrentlyFocusedEditText;
    /**
     * KeyPad
     */
    private boolean goBack, isRunning;
    private int passwordLength = 4;
    private int incorrectInputTimes = 0;
    private String correctPassword = null;
    private String setPinValue = null;
    private BigButtonView[] bigButtonViews;
    private TextView leftButton, rightButton, tvPinTitle;
    private Stack<String> passwordStack = new Stack<>();
    //TouchId
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    //Data
    private String mobileNum = null, source, target, statusCode, message;
    private Dialog appVersionDialog;
    private String sessionTime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    /**
     * Prevent click 2 or above buttons at the same time.
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            for (int i = 0; i < bigButtonViews.length; i++) bigButtonViews[i].clearAnimation();
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        btnChange = (Button) findViewById(R.id.change_btn);
        tvMobile = (TextView) findViewById(R.id.mobile);
        tvForgetPin = (TextView) findViewById(R.id.forget_pin);
        tvPinTitle = (TextView) findViewById(R.id.enter_pin_label);

        btnChange.setOnClickListener(this);
        tvForgetPin.setOnClickListener(this);

        pinTextLayout = findViewById(R.id.pin_layout_text);
        pinLayout = findViewById(R.id.pin_layout);
        tvPin1 = (TextView) findViewById(R.id.pin1_text);
        tvPin2 = (TextView) findViewById(R.id.pin2_text);
        tvPin3 = (TextView) findViewById(R.id.pin3_text);
        tvPin4 = (TextView) findViewById(R.id.pin4_text);

        ivPin1 = (ImageView) findViewById(R.id.pin1);
        ivPin2 = (ImageView) findViewById(R.id.pin2);
        ivPin3 = (ImageView) findViewById(R.id.pin3);
        ivPin4 = (ImageView) findViewById(R.id.pin4);

        tvPin1.setText(null);
        tvPin2.setText(null);
        tvPin3.setText(null);
        tvPin4.setText(null);
        mCurrentlyFocusedEditText = tvPin1;

        setCorrectPassword((String) SharedPreferenceManger.getPrefVal(Constants.PIN, null, VALUE_TYPE.STRING));

        // Number password
        bigButtonViews = new BigButtonView[10];
        bigButtonViews[0] = (BigButtonView) findViewById(R.id.button_0);
        bigButtonViews[1] = (BigButtonView) findViewById(R.id.button_1);
        bigButtonViews[2] = (BigButtonView) findViewById(R.id.button_2);
        bigButtonViews[3] = (BigButtonView) findViewById(R.id.button_3);
        bigButtonViews[4] = (BigButtonView) findViewById(R.id.button_4);
        bigButtonViews[5] = (BigButtonView) findViewById(R.id.button_5);
        bigButtonViews[6] = (BigButtonView) findViewById(R.id.button_6);
        bigButtonViews[7] = (BigButtonView) findViewById(R.id.button_7);
        bigButtonViews[8] = (BigButtonView) findViewById(R.id.button_8);
        bigButtonViews[9] = (BigButtonView) findViewById(R.id.button_9);
        String[] texts = getResources().getStringArray(R.array.default_big_button_text);
        for (int i = 0; i < bigButtonViews.length; i++) {
            bigButtonViews[i].setOnPressListener(this);
            bigButtonViews[i].setText(texts[i]);
        }
        leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setText("Show");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInputType();
            }
        });

        rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateDeletePress();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context = this;
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("SIGN IN");
//----------------------------------------Remove while Publish--------------------------------------

       /* ((TextView) findViewById(R.id.toolbar_title)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CustomerProfile.class);
                startActivity(intent);
                finish();
            }
        });*/

//--------------------------------------------------------------------------------------------------
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.svg_toolbar_home);
        if (getIntent().getExtras() != null) {
            source = getIntent().getExtras().getString(Constants.SOURCE, Constants.SOURCE_SPLASH_ACTIVITY);
            target = getIntent().getExtras().getString(Constants.TARGET, Constants.SOURCE_DASHBOARD_ACTIVITY);

            if (source != null && source.equalsIgnoreCase(Constants.SOURCE_FCM_ACTIVITY)) {
                if (AppController.isPinVerified()) {
                    goToHome();
                    return;
                }
            }
        }
//        FCMUtils.checkFCM(context);

        mobileNum = (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, VALUE_TYPE.STRING);
        if (mobileNum == null || SharedPreferenceManger.getPrefVal(Constants.PIN, null, VALUE_TYPE.STRING) == null) {
            intent = new Intent(context, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        init();
        setFocusListener();
        setOnTextChangeListener();
        tvMobile.setText(mobileNum.substring(3));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && (Boolean) SharedPreferenceManger.getPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, VALUE_TYPE.BOOLEAN)) {
            setTouchId();
        } else {
            tvPinTitle.setText("Please enter PIN");
        }

       /* if (TapTargetViewUtils.getTutStatus(context, Constants.TUT_LOGIN_SCREEN)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTargetViewUtils.getTargetView(btnChange, getString(R.string.tut_login_change_btn_title), getString(R.string.tut_login_change_btn_text)),
                            TapTargetViewUtils.getTargetView(tvForgetPin, getString(R.string.tut_login_forgot_pin_title), getString(R.string.tut_login_forgot_pin_text))).listener(TapTargetViewUtils.getTapTargetListener(context, Constants.TUT_LOGIN_SCREEN)).start();
        }*/
        new FCMUtils().sendRegistrationToServer(context, CommonUtils.getFCMToken());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void setTouchId() {
        try {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if (!keyguardManager.isKeyguardSecure()) {
                //Toast.makeText(this, "Lock screen security not enabled in Settings", Toast.LENGTH_LONG).show();
                tvPinTitle.setText("Please enter PIN");
                return;
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Fingerprint authentication permission not enabled", Toast.LENGTH_LONG).show();
                tvPinTitle.setText("Please enter PIN");
                return;
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // This happens when no fingerprints are registered.
                //Toast.makeText(this, "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show();
                tvPinTitle.setText("Please enter PIN");
                return;
            }

            generateKey();
            if (cipherInit()) {
                tvPinTitle.setText("Please enter PIN or Use Touch ID");
                cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintHandler helper = new FingerprintHandler(context, new OnFingerprintAuthentication() {
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {

                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {

                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        String pin = (String) SharedPreferenceManger.getPrefVal(Constants.PIN, null, VALUE_TYPE.STRING);
                        onSuccess(pin);
                    }
                });
                helper.startAuth(fingerprintManager, cryptoObject);
            }
        } catch (Exception ex) {
            tvPinTitle.setText("Please enter PIN");
        }
    }

    private void refreshScreen() {
        if (getIntent().getExtras() != null) {
            source = getIntent().getExtras().getString(Constants.SOURCE, Constants.SOURCE_SPLASH_ACTIVITY);
            target = getIntent().getExtras().getString(Constants.TARGET, Constants.SOURCE_DASHBOARD_ACTIVITY);

            if (source != null && source.equalsIgnoreCase(Constants.SOURCE_FCM_ACTIVITY)) {
                if (AppController.isPinVerified()) {
                    goToHome();
                    return;
                }
            }
        }
//        FCMUtils.checkFCM(context);

        mobileNum = (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, VALUE_TYPE.STRING);
        if (mobileNum == null || SharedPreferenceManger.getPrefVal(Constants.PIN, null, VALUE_TYPE.STRING) == null) {
            intent = new Intent(context, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        init();
        setFocusListener();
        setOnTextChangeListener();
        tvMobile.setText(mobileNum.substring(3));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && (Boolean) SharedPreferenceManger.getPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, VALUE_TYPE.BOOLEAN)) {
            setTouchId();
        } else {
            tvPinTitle.setText("Please enter PIN");
        }

        /*if (TapTargetViewUtils.getTutStatus(context, Constants.TUT_LOGIN_SCREEN)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTargetViewUtils.getTargetView(btnChange, getString(R.string.tut_login_change_btn_title), getString(R.string.tut_login_change_btn_text)),
                            TapTargetViewUtils.getTargetView(tvForgetPin, getString(R.string.tut_login_forgot_pin_title), getString(R.string.tut_login_forgot_pin_text))).listener(TapTargetViewUtils.getTapTargetListener(context, Constants.TUT_LOGIN_SCREEN)).start();
        }*/ // todo (removed tutorial from login screen)

        if (appVersionDialog == null) {
            appVersionDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            appVersionDialog.setCanceledOnTouchOutside(false);
            appVersionDialog.setContentView(R.layout.app_version_dialog);


            appVersionDialog.findViewById(app.alansari.R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appVersionDialog.dismiss();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                    }
                }
            });
        }

    }

    private void setFocusListener() {
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mCurrentlyFocusedEditText = (TextView) v;
            }
        };
        tvPin1.setOnFocusChangeListener(onFocusChangeListener);
        tvPin2.setOnFocusChangeListener(onFocusChangeListener);
        tvPin3.setOnFocusChangeListener(onFocusChangeListener);
        tvPin4.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void setOnTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCurrentlyFocusedEditText.getText().length() >= 1 && mCurrentlyFocusedEditText != tvPin4) {
                    mCurrentlyFocusedEditText.focusSearch(View.FOCUS_DOWN).requestFocus();
                } else if (mCurrentlyFocusedEditText.getText().length() >= 1 && mCurrentlyFocusedEditText == tvPin4) {
                } else {
                    String currentValue = mCurrentlyFocusedEditText.getText().toString();
                    if (currentValue.length() <= 0 && goBack) {
                        mCurrentlyFocusedEditText.focusSearch(View.FOCUS_UP).requestFocus();
                        goBack = false;
                        mCurrentlyFocusedEditText.setText("");
                    }
                }
            }
        };
        tvPin1.addTextChangedListener(textWatcher);
        tvPin2.addTextChangedListener(textWatcher);
        tvPin3.addTextChangedListener(textWatcher);
        tvPin4.addTextChangedListener(textWatcher);
    }

    public void simulateDeletePress() {
        if (passwordStack.size() > 0) {
            passwordStack.pop();
        }
        if (mCurrentlyFocusedEditText != null && mCurrentlyFocusedEditText.getText().toString().equals("")) {
            goBack = true;
            mCurrentlyFocusedEditText.setText("");
        } else {
            goBack = false;
            mCurrentlyFocusedEditText.setText("");
        }
        updatePinBall(false);
    }

    private void onSuccess(String setPinValue) {
        fetchDetails(setPinValue);
    }

    private void onFailed() {
        incorrectInputTimes++;
        showError();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deleteAll();
            }
        }, 300);
    }

    private void deleteAll() {
        for (int i = 0; i < passwordLength; i++) {
            simulateDeletePress();
            passwordStack.clear();
        }
    }

    /**
     * User registration.
     *
     * @param nowPasswordString
     */
    private void fetchDetails(String nowPasswordString) {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (isRunning) {
                    return;
                }
                isRunning = true;
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchDetails(CommonUtils.getUserId(), mobileNum, CommonUtils.getDeviceID(context), nowPasswordString, sessionTime), Constants.FETCH_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_DETAILS, Request.Method.PUT, this);
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_DETAILS.toString());
                CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_DETAILS.toString(), false);
            } else {
                onFailed();
                Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            onFailed();
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOTP() {
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, Constants.VOTP);
        intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
        startActivityForResult(intent, Constants.VOTP_CODE);
    }

    private void verifyOTPException() {
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, Constants.VOTP);
        intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
        startActivityForResult(intent, Constants.VOTP_EXCEPTION_4003);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case RESEND_OTP:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            Constants.mCallWeb = 1;
                            verifyOTP();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;

            case RESEND_OTP_EXCEPTION:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            verifyOTPException();
                            onFailed();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }

                break;
            case FETCH_DETAILS:
                if (status == 1) {
//-------------------------------------------DKG----------------------------------------------------
                    isRunning = false;
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            userDetails = response;
                            checkFetchDetails(userDetails);
                            Log.e("cbashcashcb",""+response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_ACTIVE_IND));
                            SharedPreferenceManger.setPrefVal(Constants.SESSION_ID, response.getString(Constants.SESSION_ID), SharedPreferenceManger.VALUE_TYPE.STRING);
                            SharedPreferenceManger.setPrefVal(Constants.REFERRAL_ACTIVE_IND, response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_ACTIVE_IND), SharedPreferenceManger.VALUE_TYPE.STRING);
                            SharedPreferenceManger.setPrefVal(Constants.REFERRAL_CODE,response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_CODE), SharedPreferenceManger.VALUE_TYPE.STRING);

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.PIN_FAILURE)) {
                            if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4035")) {
                                onFailed();
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.EXCEPTION)) {
                            if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4004")) {
                                onFailed();
                                //CommonUtils.alertBox(this,response.getString(Constants.MESSAGE));
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            } else if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4003")) {
                                // goToSetPIN(CommonUtils.getUserStatus(), CommonUtils.getUserMobile(), Constants.UVDN_CODE);
                                generateOTPException();
                                //verifyOTPException();


                            /* else if (response.getString(Constants.STATUS).equals("L") && response.getString("USER_STATUS").equalsIgnoreCase(Constants.UVDN)) {
                                userDetails = response;
                                checkFetchDetails(userDetails);
                            }*/
                            } else {
                                onFailed();
                                //CommonUtils.alertBox(this,response.getString(Constants.MESSAGE));
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            onFailed();
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                        onFailed();
                    }
                } else {
                    isRunning = false;
                    onFailed();
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
//--------------------------------------------------------------------------------------------------

                /* case FETCH_DETAILS:
                if (status == 1) {
                    userDetails = response;
                    checkFetchDetails(userDetails);
                } else {
                    onFailed();
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;*/
            case EMAIL_NOTIFICATION:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            checkFetchDetails(userDetails);
                            CommonUtils.setInvoiceEmail(userEmailId.trim());
                            Toast.makeText(context, "Email Saved Sucessfully", Toast.LENGTH_SHORT).show();
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
            case FETCH_EMAIL_NOTIFICATION:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<FetchEmailData> fetchEmailData = (ArrayList<FetchEmailData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<FetchEmailData>>() {
                                }.getType());
                                if (fetchEmailData != null && fetchEmailData.size() > 0) {
                                    if (CommonUtils.isEmailValid(fetchEmailData.get(0).getINVOICEEMAIL())) {
                                        checkFetchDetails(userDetails);
                                        return;
                                    } else
                                        showEmailSubmitDialog(context);
                                } else
                                    showEmailSubmitDialog(context);
                            }
                            showEmailSubmitDialog(context);
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
        }
    }

    private void generateOTPException() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTP(mobileNum, userPkId, LogoutCalling.getDeviceID(context)), Constants.RESEND_OTP_URL, CommonUtils.SERVICE_TYPE.RESEND_OTP_EXCEPTION, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP_EXCEPTION.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP_EXCEPTION.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFetchDetails(JSONObject response) {
        try {
            statusCode = response.getString(Constants.STATUS_CODE);
            message = response.getString(Constants.MESSAGE);
            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                String userStatus = response.getString(Constants.USER_STATUS);
                LogUtils.v("message", message);
                LogUtils.v("userStatus", userStatus);
                if (statusCode.equalsIgnoreCase("799")) {
                    ((TextView) appVersionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(message);
                    appVersionDialog.show();
                } else {
                    if (statusCode.equalsIgnoreCase("698") || statusCode.equalsIgnoreCase("699")) {
                        SharedPreferenceManger.setMessage(message);
                    }
                    CommonUtils.setUserStatus(userStatus);
                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                        UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                        if (userData != null) {
                            userData.setTotalPendingTransactions(response.getString(Constants.ID));
                            //CommonUtils.setUserData(userData);
                            new CommonUtils().setUserData(userData);
                        }

                        LogUtils.d("sjbcvbsvjbvjb", "" + userStatus + "  " + userData.getStatus() + response.getString(Constants.PROFILE_UPDATE_FLAG));

                        //here perfect
                        if (userStatus.equalsIgnoreCase(Constants.UVDN) && userData.getStatus().equalsIgnoreCase("CR")) {
                            intent = new Intent(context, VerifyEIDActivity.class);
                            intent.putExtra(Constants.TYPE, Constants.CHANGE_PIN);
                            intent.putExtra(Constants.MOBILE_NUM, mobileNum);
                           // intent.putExtra(Constants.USER_STATUS, Constants.FPIN);
                            intent.putExtra(Constants.USER_STATUS, Constants.UVDN);
                            startActivity(intent);
                        } else if (userData.getStatus().equalsIgnoreCase("CR") || userData.getStatus().equalsIgnoreCase("N") || userData.getStatus().equalsIgnoreCase("B")) {
                            intent = new Intent(context, GoToBranchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra(Constants.PROFILE_UPDATE_FLAG, response.getString(Constants.PROFILE_UPDATE_FLAG));
                            startActivity(intent);
                            finish();
                        } else if (userStatus.equalsIgnoreCase(Constants.UVDV) && userData.getStatus().equalsIgnoreCase("VR")) {
                            goToHome();
                        } else if (userStatus.equalsIgnoreCase(Constants.UVDN) && userData.getStatus().equalsIgnoreCase("VR")) {
                            intent = new Intent(context, ChangePinActivity.class);
                            intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
                            intent.putExtra(Constants.TYPE, Constants.NO_PIN);
                            intent.putExtra(Constants.USER_STATUS, Constants.LUVDN);
                            intent.putExtra(Constants.PIN, response.getString(Constants.OTP));
                            intent.putExtra(Constants.MOBILE_NUM, mobileNum);
                            startActivityForResult(intent, Constants.LUVDN_CODE);
                            deleteAll();
                        } else if (userStatus.equalsIgnoreCase(Constants.UVDN) && userData.getStatus().equalsIgnoreCase("L")) {
                            intent = new Intent(context, ChangePinActivity.class);
                            intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
                            intent.putExtra(Constants.TYPE, Constants.NO_PIN);
                            intent.putExtra(Constants.USER_STATUS, Constants.LUVDN);
                            intent.putExtra(Constants.PIN, response.getString(Constants.OTP));
                            intent.putExtra(Constants.MOBILE_NUM, mobileNum);
                            startActivityForResult(intent, Constants.LUVDN_CODE);
                            deleteAll();
                        }

                    } else {
                        SharedPreferenceManger.clearPreferences();
                        AppController.setIsPinVerified(false);
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.putExtra("goto", "registration");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        ((AppCompatActivity) context).finish();
                    }
                }

            } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                onFailed();
                Toast.makeText(context, getString(R.string.error_unable_to_send_otp), Toast.LENGTH_SHORT).show();
            } else {
                onFailed();
                if (statusCode.equalsIgnoreCase("799")) {
                    ((TextView) appVersionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(message);
                    appVersionDialog.show();
                }
            }
        } catch (Exception ex) {
            onFailed();
            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        } finally {
            isRunning = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.LUVDN_CODE) {
                goToHome();
            } else if (requestCode == Constants.VOTP_CODE) {
                String status = CommonUtils.getStatus();
                if (status.equalsIgnoreCase("CR") || status.equalsIgnoreCase("N") || status.equalsIgnoreCase("B")) {
                    goToSetPIN(CommonUtils.getUserStatus(), CommonUtils.getUserMobile(), Constants.UVDN_CODE);

                } else if (data.getStringExtra(Constants.RESULT) != null && data.getStringExtra(Constants.RESULT).equalsIgnoreCase("0")) {
                    SharedPreferenceManger.clearPreferences();
                    AppController.setIsPinVerified(false);
                    intent = new Intent(context, RegisterActivity.class);
                    intent.putExtra("goto", "registration");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((AppCompatActivity) context).finish();
                } else {
                    intent = new Intent(context, VerifyEIDActivity.class);
                    intent.putExtra(Constants.TYPE, Constants.CHANGE_PIN);
                    intent.putExtra(Constants.MOBILE_NUM, mobileNum);
                    intent.putExtra(Constants.USER_STATUS, Constants.FPIN);
                    startActivity(intent);
                }
            } else if (requestCode == Constants.UVDN_CODE && resultCode == Activity.RESULT_OK) {
                intent = new Intent(context, GoToBranchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (requestCode == Constants.VOTP_EXCEPTION_4003 && resultCode == Activity.RESULT_OK) {
                //goToSetPINSETUP(CommonUtils.getUserMobile(), Constants.UVDN_CODE);
                goToSetPINSETUP(CommonUtils.getUserMobile(), Constants.LUVDN_CODE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            source = intent.getExtras().getString(Constants.SOURCE, Constants.SOURCE_SPLASH_ACTIVITY);
            target = intent.getExtras().getString(Constants.TARGET, Constants.SOURCE_DASHBOARD_ACTIVITY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshScreen();
    }

    private void goToHome() {
        if (target == null || target.equalsIgnoreCase(Constants.SOURCE_DASHBOARD_ACTIVITY)) {
            SharedPreferenceManger.setPrefVal(Constants.JUST_LOGGED_IN, true, VALUE_TYPE.BOOLEAN);
            intent = new Intent(context, DashboardActivity.class);
        } else if (target.equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
            intent = new Intent(context, PendingTransActivity.class);
        } else if (target.equalsIgnoreCase(Constants.SOURCE_BENEFICIARY_ACTIVITY)) {
            intent = new Intent(context, BeneficiaryActivity.class);
            intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
        } else if (target.equalsIgnoreCase(Constants.SOURCE_CREDIT_CARD_PAYMENT_ACTIVITY)) {
            intent = new Intent(context, CreditCardPaymentActivity.class);
            intent.putExtra(Constants.ID, getIntent().getExtras().getString(Constants.ID, null));
        }

        if (!AppController.isPinVerified()) {
            intent.putExtra(Constants.SOURCE, source);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AppController.setIsPinVerified(true);
        }
        startActivity(intent);
        finish();
    }

    private void goToSetPIN(String userStatus, String mobileNum, int reqCode) {
        //FCMUtils.checkFCM(context);

        Intent intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.TYPE, Constants.CHANGE_PIN);
        intent.putExtra(Constants.USER_STATUS, userStatus);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, reqCode);
    }

    private void goToSetPINSETUP(String mobileNum, int reqCode) {
        //FCMUtils.checkFCM(context);

        Intent intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.TYPE, Constants.CHANGE_PIN);
        intent.putExtra(Constants.USER_STATUS, "");
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, reqCode);
    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                CommonUtils.setFCMServerStatus(false);
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.forget_pin:
                generateOTP();
                break;
        }
    }

    /**
     * From the button views.
     *
     * @param string The string from button views.
     */
    @Override
//Checking password locally here--------------------------------------------------------------------
    public void onPress(String string) {
        if (correctPassword == null) {
            throw new RuntimeException("The correct password has NOT been set!");
        }

        if (passwordStack.size() >= passwordLength) return;
        passwordStack.push(string);
        updatePinBall(true);
        mCurrentlyFocusedEditText.setText(string);
        StringBuilder nowPassword = new StringBuilder("");
        for (String s : passwordStack) {
            nowPassword.append(s);
        }
        String nowPasswordString = nowPassword.toString();
//---------------------------------------NEW--------------------------------------------------------
       /* if (correctPassword.length() > nowPasswordString.length()) {
            setPinValue=nowPasswordString;
            onSuccess();
        } else {
            onFailed();
        }*/
        if (correctPassword.length() > nowPasswordString.length()) {

        } else if (nowPasswordString.length() == 4) {
            setPinValue = nowPasswordString;
            onSuccess(setPinValue);
        } else {
            onFailed();
        }


//-----------------------------OLD------------------------------------------------------------------
       /* if (correctPassword.equals(nowPasswordString)) {
            onSuccess();
        } else {
            if (correctPassword.length() > nowPasswordString.length()) {
            } else {
                onFailed();
            }
        }*/
//--------------------------------------------------------------------------------------------------
    }

    private void updatePinBall(boolean state) {
        if (mCurrentlyFocusedEditText == tvPin1) {
            ivPin1.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        } else if (mCurrentlyFocusedEditText == tvPin2) {
            ivPin2.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        } else if (mCurrentlyFocusedEditText == tvPin3) {
            ivPin3.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        } else if (mCurrentlyFocusedEditText == tvPin4) {
            ivPin4.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        }
    }

    private void showError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_error);
        if (pinTextLayout.getVisibility() == View.VISIBLE)
            pinTextLayout.startAnimation(shake);
        else
            pinLayout.startAnimation(shake);
    }

    private void changeInputType() {
        if (pinTextLayout.getVisibility() == View.VISIBLE) {
            pinTextLayout.setVisibility(View.INVISIBLE);
            pinLayout.setVisibility(View.VISIBLE);
            leftButton.setText("Show");
        } else {
            pinTextLayout.setVisibility(View.VISIBLE);
            pinLayout.setVisibility(View.INVISIBLE);
            leftButton.setText("Hide");
        }
    }

    /**
     * Set the length of the password.
     * Default length is 4.
     *
     * @param passwordLength
     */
    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        passwordStack.clear();
        correctPassword = null;
    }

    /**
     * Set the target password.
     *
     * @param correctPassword The target password.
     */
    public void setCorrectPassword(String correctPassword) {
        setPasswordLength(correctPassword.length());
        this.correctPassword = correctPassword;
        LogUtils.d("ok", "Correct Pa" + correctPassword);
    }

    /**
     * Return the incorrect input times.
     *
     * @return Incorrect input times.
     */
    public int getIncorrectInputTimes() {
        return incorrectInputTimes;
    }

    /**
     * You can use this to reset the incorrect input times.
     *
     * @param incorrectInputTimes The incorrect input times.
     */
    public void setIncorrectInputTimes(int incorrectInputTimes) {
        this.incorrectInputTimes = incorrectInputTimes;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        } catch (Exception e) {
            return false;
        }
    }

    public void showEmailSubmitDialog(final Context mContext) {
        final Dialog dialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_email, null);
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        final EditText etEmail = view.findViewById(R.id.email);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        tvTitle.setText(getString(R.string.please_submit_email));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userEmailId = etEmail.getText().toString();
                if (CommonUtils.isEmailValid(userEmailId)) {
                    submitEmail(userEmailId);
                    dialog.dismiss();
                } else
                    etEmail.setError(mContext.getString(R.string.error_enter_valid_email));
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void fetchEmail() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkId((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)), Constants.FETCH_EMAIL_NOTIFICATION_URL, FETCH_EMAIL_NOTIFICATION, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_EMAIL_NOTIFICATION.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_EMAIL_NOTIFICATION.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_EMAIL_NOTIFICATION.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitEmail(String email) {
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), email, "Y", LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
        AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
        AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
    }

    private void somethingWentWrongToast() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }




    private void generateOTP() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTP(mobileNum, userPkId, LogoutCalling.getDeviceID(context)), Constants.RESEND_OTP_URL, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }


}