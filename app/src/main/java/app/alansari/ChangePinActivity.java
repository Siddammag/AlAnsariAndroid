package app.alansari;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Stack;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.keypadview.AsteriskPasswordTransformationMethod;
import app.alansari.keypadview.BigButtonView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.preferences.SharedPreferenceManger.VALUE_TYPE;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.RESET_PIN;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.STORE_PIN;
import static app.alansari.Utils.Constants.mOtpTimer;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 11 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class ChangePinActivity extends AppCompatActivity implements BigButtonView.OnPressListener, OnWebServiceResult, View.OnClickListener, LogOutTimerUtil.LogOutListener {

    JSONObject userDetails;
    String statusCode;
    private String mobileNum, userStatus, message;
    private Intent intent;
    private Context context;
    private String enteredPassword;
    private int screenLayout, pinType;
    private AsteriskPasswordTransformationMethod pwdMethod;
    private TextView tvMobile, tvPin1, tvPin2, tvPin3, tvPin4, tvPin5, tvPin6, mCurrentlyFocusedEditText, tvPinLabel, tvResendOTP;
    private ImageView ivPin1, ivPin2, ivPin3, ivPin4, ivPin5, ivPin6;
    private LinearLayout pinTextLayout, pinLayout;
    /**
     * Key Pad
     */
    private boolean goBack = false;
    private int passwordLength = 4;
    private int incorrectInputTimes = 0, enterPinCounts = 0;
    private String localPassword = null;
    private BigButtonView[] bigButtonViews;
    private TextView leftButton, rightButton;
    private Stack<String> passwordStack = new Stack<>();
    private CountDownTimer mCountDownTimer;
    /**
     * Result Layout
     */

    private TextView tvResultTitle, tvResultText;
    private Button btnResultOk;
    private String sessionTime;

    private FirebaseAnalytics mFirebaseAnalytics;

    public void alertBox(final Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogTheme);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFirebaseAnalytics.logEvent("Login", null);
                Log.i("Login", "Success in Login");
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                finish();
            }
        });

        alertDialog.show();
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin_activity);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("PIN CHANGE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getExtras() != null) {
            mobileNum = getIntent().getExtras().getString(Constants.MOBILE_NUM);
            userStatus = getIntent().getExtras().getString(Constants.USER_STATUS);
            message = getIntent().getExtras().getString(Constants.MESSAGE);
            screenLayout = getIntent().getExtras().getInt(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
            pinType = getIntent().getExtras().getInt(Constants.TYPE, Constants.ENTER_PIN);
        } else {
            screenLayout = Constants.SCREEN_LAYOUT_PIN;
            pinType = Constants.ENTER_PIN;
        }
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        Log.e("sbshvbhb", "" + message + "  " + userStatus);
        init();
        setFocusListener();
        setOnTextChangeListener();

        // ---------------------------------------DKG-------------------------------------------
        // OnWaitForOtp(120);
        // -------------------------------------------------------------------------------------
    }

    private void init() {

        tvMobile = (TextView) findViewById(R.id.mobile);
        tvPinLabel = (TextView) findViewById(R.id.enter_pin_label);
        pinTextLayout = (LinearLayout) findViewById(R.id.pin_text_layout);
        pinLayout = (LinearLayout) findViewById(R.id.pin_layout);

        tvPin1 = (TextView) findViewById(R.id.pin1_text);
        tvPin2 = (TextView) findViewById(R.id.pin2_text);
        tvPin3 = (TextView) findViewById(R.id.pin3_text);
        tvPin4 = (TextView) findViewById(R.id.pin4_text);
        tvPin5 = (TextView) findViewById(R.id.pin5_text);
        tvPin6 = (TextView) findViewById(R.id.pin6_text);
        ivPin1 = (ImageView) findViewById(R.id.pin1);
        ivPin2 = (ImageView) findViewById(R.id.pin2);
        ivPin3 = (ImageView) findViewById(R.id.pin3);
        ivPin4 = (ImageView) findViewById(R.id.pin4);
        ivPin5 = (ImageView) findViewById(R.id.pin5);
        ivPin6 = (ImageView) findViewById(R.id.pin6);
        pwdMethod = new AsteriskPasswordTransformationMethod();

        checkScreenLayout();

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
        leftButton.setText("Hide");

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

        //Result
        tvResultTitle = (TextView) findViewById(R.id.result_title);
        tvResultText = (TextView) findViewById(R.id.result_text);
        btnResultOk = (Button) findViewById(R.id.result_ok_btn);
        btnResultOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAnalytics.logEvent("App_Loading", null);
                Log.i("App_Loading" , "Success in Change pin activity");
                intent = new Intent(context, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
        if (mobileNum != null)
            tvMobile.setText(mobileNum.substring(3));
        else
            tvMobile.setText("");
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
        tvPin5.setOnFocusChangeListener(onFocusChangeListener);
        tvPin6.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void setOnTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              /*  findViewById(R.id.resend_otp).setVisibility(View.VISIBLE);
                findViewById(R.id.resend_otp_timer).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.resend_otp)).setText("Resend OTP");
                mCountDownTimer.cancel();*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                if (mCurrentlyFocusedEditText.getText().length() >= 1 && (screenLayout == Constants.SCREEN_LAYOUT_PIN ? mCurrentlyFocusedEditText != tvPin4 : mCurrentlyFocusedEditText != tvPin6)) {
                    mCurrentlyFocusedEditText.focusSearch(View.FOCUS_DOWN).requestFocus();
                } else if (mCurrentlyFocusedEditText.getText().length() >= 1 && (screenLayout == Constants.SCREEN_LAYOUT_PIN ? mCurrentlyFocusedEditText == tvPin4 : mCurrentlyFocusedEditText == tvPin6)) {
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
        tvPin5.addTextChangedListener(textWatcher);
        tvPin6.addTextChangedListener(textWatcher);
    }

    private void checkScreenLayout() {
        mCurrentlyFocusedEditText = tvPin1;
        tvPin1.setText(null);
        tvPin2.setText(null);
        tvPin3.setText(null);
        tvPin4.setText(null);
        tvPin5.setText(null);
        tvPin6.setText(null);
        mCurrentlyFocusedEditText = tvPin1;
        if (screenLayout == Constants.SCREEN_LAYOUT_PIN) {
            pinTextLayout.setWeightSum(4f);
            pinLayout.setWeightSum(4f);
            findViewById(R.id.mobile_layout).setVisibility(View.VISIBLE);
            setViewVisibility(View.GONE);
            tvPin4.setNextFocusDownId(R.id.pin4_text);
            setPinSpacing(findViewById(R.id.pin1_view), tvPin1.getId(), true);
            setPinSpacing(findViewById(R.id.pin2_view), tvPin2.getId(), true);
            setPinSpacing(findViewById(R.id.pin3_view), tvPin3.getId(), true);
            setPinSpacing(findViewById(R.id.pin4_view), tvPin4.getId(), true);
            passwordLength = 4;
            tvPinLabel.setText("Set your PIN");
            if (pinType == Constants.SETUP_PIN) {
                ((TextView) findViewById(R.id.toolbar_title)).setText("PIN SETUP");
                if (!getIntent().getBooleanExtra(Constants.HIDE_TERMS, false))
                    new IndemnityActivity(context, R.style.CustomDialogThemeLightBg).show();
            } else if (pinType == Constants.CHANGE_PIN) {
                ((TextView) findViewById(R.id.toolbar_title)).setText("PIN CHANGE");
            }
        } else {
            pinTextLayout.setWeightSum(6f);
            pinLayout.setWeightSum(6f);
            setViewVisibility(View.VISIBLE);
            findViewById(R.id.mobile_layout).setVisibility(View.GONE);
            findViewById(R.id.resend_otp).setOnClickListener(this);
            tvPin4.setNextFocusDownId(R.id.pin5_text);
            setPinSpacing(findViewById(R.id.pin1_view), tvPin1.getId(), false);
            setPinSpacing(findViewById(R.id.pin2_view), tvPin2.getId(), false);
            setPinSpacing(findViewById(R.id.pin3_view), tvPin3.getId(), false);
            setPinSpacing(findViewById(R.id.pin4_view), tvPin4.getId(), false);
            passwordLength = 6;
            tvPinLabel.setText("Please enter the 6 digit code sent to +" + mobileNum);
            ((TextView) findViewById(R.id.toolbar_title)).setText("MOBILE NUMBER \nVERIFICATION");
        }
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

    private void setViewVisibility(int visibility) {
        findViewById(R.id.pin5_text_layout).setVisibility(visibility);
        findViewById(R.id.pin6_text_layout).setVisibility(visibility);
        findViewById(R.id.pin5_layout).setVisibility(visibility);
        findViewById(R.id.pin6_layout).setVisibility(visibility);
        findViewById(R.id.resend_otp).setVisibility(visibility);
    }

    private void setPinSpacing(View view, int textView, boolean add) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (add) {
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, textView);
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, textView);
        } else {
            layoutParams.removeRule(RelativeLayout.ALIGN_LEFT);
            layoutParams.removeRule(RelativeLayout.ALIGN_RIGHT);
        }
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
        } else if (mCurrentlyFocusedEditText == tvPin5) {
            ivPin5.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        } else if (mCurrentlyFocusedEditText == tvPin6) {
            ivPin6.setBackground(ContextCompat.getDrawable(context, state ? R.drawable.pin_code_round_full : R.drawable.pin_code_round_empty));
        }
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
        // CommonUtils.isLoggedIn() && CommonUtils.getUserId() != null && CommonUtils.getUserMobile() != null && CommonUtils.getPIN() != null
    }

    private void OnWaitForOtp(int Seconds) {


        mCountDownTimer = new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onTick(long millisUntilFinished) {
                findViewById(R.id.resend_otp).setVisibility(View.VISIBLE);
                findViewById(R.id.resend_otp).setClickable(false);
                ((TextView) findViewById(R.id.resend_otp)).setTextColor(getResources().getColor(R.color.color_Grey));


                findViewById(R.id.resend_otp_timer).setVisibility(View.VISIBLE);
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                ((TextView) findViewById(R.id.resend_otp_timer)).setText("in: " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));


            }

            public void onFinish() {
                findViewById(R.id.resend_otp).setVisibility(View.VISIBLE);
                findViewById(R.id.resend_otp).setClickable(true);
                findViewById(R.id.resend_otp_timer).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.resend_otp)).setText("Resend OTP");
                ((TextView) findViewById(R.id.resend_otp)).setTextColor(getResources().getColor(R.color.colorECEAEA));
            }
        };
        mCountDownTimer.start();


    }

    /**
     * From the button views.
     *
     * @param string The string from button views.
     */
    @Override
    public void onPress(String string) {
        if (passwordStack.size() >= passwordLength) return;
        passwordStack.push(string);
        updatePinBall(true);
        mCurrentlyFocusedEditText.setText(string);
        StringBuilder nowPassword = new StringBuilder("");
        for (String s : passwordStack) {
            nowPassword.append(s);
        }
        enteredPassword = nowPassword.toString();

        if (passwordLength == enteredPassword.length()) {
            if (screenLayout == Constants.SCREEN_LAYOUT_PIN) {
                if (enterPinCounts == 0) {
                    if (passwordLength == enteredPassword.length()) {
                        localPassword = enteredPassword;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enterPinCounts = 1;
                                tvPinLabel.setText("Confirm PIN");
                                for (int i = 0; i < passwordLength; i++) {
                                    simulateDeletePress();
                                    passwordStack.clear();
                                }
                            }
                        }, 100);
                        return;
                    }
                } else {
                    if (localPassword.equals(enteredPassword)) {
                        if (pinType == Constants.CHANGE_PIN) {
                            SharedPreferenceManger.setPrefVal(Constants.PIN, localPassword, VALUE_TYPE.STRING);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //findViewById(R.id.change_pin_result_layout).setVisibility(View.VISIBLE);
                                    storePin(enteredPassword);


                                }
                            }, 100);
                        } else if (pinType == Constants.SETUP_PIN) {
                            SharedPreferenceManger.setPrefVal(Constants.PIN, localPassword, VALUE_TYPE.STRING);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                                        enableTouchIdForAllCondition();
                                        //storePin(enteredPassword);

                                    else
                                        storePin(enteredPassword);
                                }
                            }, 100);
                        }
                    } else if (passwordLength == enteredPassword.length()) {
                        incorrectPIN();
                    }
                }
            } else if (screenLayout == Constants.SCREEN_LAYOUT_OTP) {
                verifyOTP();
            }
        }
    }

    private void storePin(String enteredPassword) {
        if (Constants.mCallWeb == 0) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().storePinValue((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), mobileNum, CommonUtils.getDeviceDetails(context), enteredPassword), Constants.PRELOGIN_STORE_PIN, STORE_PIN, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(STORE_PIN.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, STORE_PIN.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), STORE_PIN.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }

        } else {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().storePinValue((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), mobileNum, CommonUtils.getDeviceDetails(context), enteredPassword), Constants.PRELOGIN_RESET_PIN, RESET_PIN, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(RESET_PIN.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, RESET_PIN.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), RESET_PIN.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
            Constants.mCallWeb = 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void enableTouchIdForAllCondition() {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            //Device is not having fingerprint sensor
            if (!fingerprintManager.isHardwareDetected()) {
                SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                storePin(enteredPassword);
                return;
            }


            //Require atleast on finger print
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //touchDone();

            } else {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    //To.setText("Register at least one fingerprint in Settings");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            context, R.style.MyAlertDialogTheme);
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Register at least one fingerprint in Settings");
                    alertDialog.setCancelable(false);
                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                            storePin(enteredPassword);

                        }
                    });
                    alertDialog.show();

                } else {
                    final Dialog forgetPinDialog = new Dialog(this, R.style.CustomDialogThemeLightBg);
                    forgetPinDialog.setCanceledOnTouchOutside(true);
                    forgetPinDialog.setContentView(R.layout.forget_pin_dialog);
                    forgetPinDialog.findViewById(R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                            forgetPinDialog.dismiss();
                            storePin(enteredPassword);
                            // touchDone();
                        }
                    });

                    forgetPinDialog.findViewById(R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                            forgetPinDialog.dismiss();
                            storePin(enteredPassword);
                            // touchDone();
                        }
                    });
                    forgetPinDialog.show();


                }

            }

    }

    /**
     * Clear text fields on Incorrect PIN.
     */
    private void incorrectPIN() {
        incorrectInputTimes++;
        showError();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((pinType == Constants.CHANGE_PIN || pinType == Constants.SETUP_PIN) && enterPinCounts == 1) {
                    tvPinLabel.setText("Enter PIN");
                    passwordLength = 4;
                    localPassword = null;
                    enterPinCounts = 0;
                }
                for (int i = 0; i < passwordLength; i++) {
                    simulateDeletePress();
                    passwordStack.clear();
                }
                //mCallTimer();
            }
        }, 300);
    }

    /**
     * Send entered OTP to server to verify.
     */
    private void verifyOTP() {
        try {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().verifyOTP(CommonUtils.getUserId(), mobileNum, enteredPassword, userStatus, CommonUtils.getDeviceDetails(context)), Constants.VERIFY_OTP_URL, CommonUtils.SERVICE_TYPE.VERIFY_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VERIFY_OTP.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.VERIFY_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Shake Animation on Incorrect PIN.
     */
    private void showError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_error);
        if (pinTextLayout.getVisibility() == View.VISIBLE)
            pinTextLayout.startAnimation(shake);
        else
            pinLayout.startAnimation(shake);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void enableTouchId() {
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

//Device is not having fingerprint sensor
        if (!fingerprintManager.isHardwareDetected()) {
            SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            storePin(enteredPassword);
            return;
        }

        if (!keyguardManager.isKeyguardSecure()) {
            //touchDone();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            //touchDone();
            return;
        }

        if (fingerprintManager != null && (!fingerprintManager.isHardwareDetected() || !fingerprintManager.hasEnrolledFingerprints())) {
            // touchDone();
            return;
        }

//Require atleast on finger print


        final Dialog forgetPinDialog = new Dialog(this, R.style.CustomDialogThemeLightBg);
        forgetPinDialog.setCanceledOnTouchOutside(true);
        forgetPinDialog.setContentView(R.layout.forget_pin_dialog);
        forgetPinDialog.findViewById(R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                forgetPinDialog.dismiss();
                storePin(enteredPassword);
                // touchDone();
            }
        });

        forgetPinDialog.findViewById(R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManger.setPrefVal(Constants.IS_TOUCH_ID_ENABLED, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                forgetPinDialog.dismiss();
                storePin(enteredPassword);
                // touchDone();
            }
        });
        forgetPinDialog.show();
    }

    private void touchDone() {
        intent = new Intent();
        intent.putExtra(Constants.TYPE, pinType);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void mCallTimer() {
        findViewById(R.id.resend_otp).setVisibility(View.VISIBLE);
        findViewById(R.id.resend_otp_timer).setVisibility(View.VISIBLE);
        if (mOtpTimer > 0) {
            OnWaitForOtp(mOtpTimer);
        } else {
            OnWaitForOtp(120);

        }

        // mCountDownTimer.start();
    }

    /**
     * API response
     */
    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case VERIFY_OTP:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            LogUtils.v("otp userStatus", userStatus);
                            CommonUtils.setUserStatus(response.getString(Constants.USER_STATUS));
                            String result = "0";
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                result = "1";
                                UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                                if (userData != null)
                                    new CommonUtils().setUserData(userData);

                            } else {
                                result = "0";
                                SharedPreferenceManger.setPrefVal(Constants.STATUS, "", SharedPreferenceManger.VALUE_TYPE.STRING);
                            }
                            switch (userStatus) {
                                case Constants.UVDN:
                                case Constants.EUVDN:
                                case Constants.GNAN:
                                case Constants.GNAV:
                                case Constants.GVAN:
                                case Constants.EGNAV:
                                case Constants.LUVDN:
                                case Constants.VOTP:
                                    intent = new Intent();
                                    intent.putExtra(Constants.MESSAGE, message);
                                    intent.putExtra(Constants.RESULT, result);
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                    break;

                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) & response.getString(Constants.STATUS_CODE).equalsIgnoreCase("0608")) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            deleteAll();
                            incorrectPIN();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) & response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4008")) {
                            //finish();
                            // Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            alertShowEndPage(response.getString(Constants.MESSAGE));

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            incorrectPIN();
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                            //mCallTimer();
                        } else {
                            deleteAll();
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        deleteAll();
                        // Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    deleteAll();
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;

            case RESEND_OTP:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            Toast.makeText(context, "OTP sent to registered mobile number.", Toast.LENGTH_LONG).show();
                            mCallTimer();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_unable_to_send_otp), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        // Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
            case STORE_PIN:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            //findViewById(R.id.change_pin_result_layout).setVisibility(View.VISIBLE);
                            //SessionId
                            callDeviceDetailApi();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        // Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
            case RESET_PIN:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            findViewById(R.id.change_pin_result_layout).setVisibility(View.VISIBLE);
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        // Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;

//----------------------Storing SessionId----------------------------------------------------------
            case FETCH_DETAILS:
                if (status == 1) {

//-------------------------------------------DKG----------------------------------------------------
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            SharedPreferenceManger.setPrefVal(Constants.SESSION_ID, response.getString(Constants.SESSION_ID), SharedPreferenceManger.VALUE_TYPE.STRING);
                            Log.e("cbashcashcbhj",""+response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_ACTIVE_IND));
                            SharedPreferenceManger.setPrefVal(Constants.REFERRAL_ACTIVE_IND, response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_ACTIVE_IND), SharedPreferenceManger.VALUE_TYPE.STRING);
                            SharedPreferenceManger.setPrefVal(Constants.REFERRAL_CODE,response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.REFERRAL_CODE), SharedPreferenceManger.VALUE_TYPE.STRING);


                            userDetails = response;
                            checkFetchDetails(userDetails);
                            //touchDone();

                        } else if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4034")) {
                            /*intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();*/
                            alertBox(context, response.getString(Constants.MESSAGE));


                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.EXCEPTION)) {
                            // Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            CommonUtils.alertBox(context, response.getString(Constants.MESSAGE));

                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        //   Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                        Log.e("ahchaghagvchs", "" + ex);
                    }

                }
                break;


        }

    }

    private void checkFetchDetails(JSONObject response) {
        try {
            statusCode = response.getString(Constants.STATUS_CODE);
            message = response.getString(Constants.MESSAGE);
            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                String userStatus = response.getString(Constants.USER_STATUS);
                LogUtils.v("message", message);
                LogUtils.v("userStatus", "ChangePinAct" + "   " + userStatus);

                if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                    UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                    if (userData.getStatus().equalsIgnoreCase("CR") || userData.getStatus().equalsIgnoreCase("N") || userData.getStatus().equalsIgnoreCase("B")) {
                        intent = new Intent();
                        intent.putExtra(Constants.MESSAGE, message);
                        intent.putExtra(Constants.RESULT, "1");
                        setResult(Activity.RESULT_OK, intent);
                        finish();


                    } else {
                        touchDone();

                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
    }


    private void alertShowEndPage(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogTheme);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void callDeviceDetailApi() {
        fetchDetails(enteredPassword);
    }

    private void onOTPToPIN() {
        screenLayout = Constants.SCREEN_LAYOUT_PIN;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < passwordLength; i++) {
                    simulateDeletePress();
                    passwordStack.clear();
                }
                checkScreenLayout();
            }
        }, 300);
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
    }

    /**
     * Return the incorrect input times.
     *
     * @return Incorrect input times.
     */
    public int getIncorrectInputTimes() {
        return incorrectInputTimes;
    }

    private void deleteAll() {
        for (int i = 0; i < passwordLength; i++) {
            simulateDeletePress();
            passwordStack.clear();
        }
    }

    /**
     * You can use this to reset the incorrect input times.
     *
     * @param incorrectInputTimes The incorrect input times.
     */
    public void setIncorrectInputTimes(int incorrectInputTimes) {
        this.incorrectInputTimes = incorrectInputTimes;
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    private void fetchDetails(String nowPasswordString) {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchDetails(CommonUtils.getUserId(), mobileNum, CommonUtils.getDeviceID(context), nowPasswordString, sessionTime), Constants.FETCH_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_DETAILS, Request.Method.PUT, this);
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_DETAILS.toString());
                CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_DETAILS.toString(), false);
            } else {
                Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resend_otp:
                deleteAll();
                resendOTP();
                break;
        }
    }


    /**
     * Resend OTP.
     */
    private void resendOTP() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTP(mobileNum, userPkId, LogoutCalling.getDeviceID(context)), Constants.RESEND_OTP_URL, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }


}





