package app.alansari.modules.travelcardreload;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.ChangePinActivity;
import app.alansari.R;
import app.alansari.Utils.AsteriskPasswordTransformationMethod;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.progressbutton.CircularProgressButton;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CreditCardData;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_TRAVEL_CARD;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class AddTravelCardReload extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(false);
    }

    private Animation slideUpAnimation, slideDownAnimation;
    private boolean isUpdate;
    private Intent intent;
    private Context context;
    private Button btnNext;
    private CircularProgressButton btnSave;
    private View confirmLayoutBg;
    private TextView tvName, tvCardNumber1, tvCardNumber2, tvCardNumber3, tvCardNumber4;
    private EditText etName, etCardNumber;
    private TextInputLayout nameLayout, cardNumberLayout;
    private ScrollView scrollView;
    private String responseProfileUpdateFlag;
    TravelCardInfo travelCardInfo;



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
        setContentView(R.layout.activity_add_travel_card_reload);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("New Card");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
        init();
        if (getIntent() != null) {
            try {
                responseProfileUpdateFlag = getIntent().getStringExtra(Constants.PROFILE_UPDATE_FLAG);
                if (responseProfileUpdateFlag.equalsIgnoreCase("N")) {
                    nameLayout.setVisibility(View.GONE);
                } else {
                    nameLayout.setVisibility(View.VISIBLE);
                }
                Log.e("fsjbchsb", "" + responseProfileUpdateFlag);
            } catch (Exception ex) {
                ex.printStackTrace();
                nameLayout.setVisibility(View.VISIBLE);
                Log.e("fsjbchsbnull", "" + responseProfileUpdateFlag);
            }
        }
    }

    private void init() {

        scrollView = findViewById(app.alansari.R.id.scrollview);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_down_animation);
        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        nameLayout = (TextInputLayout) findViewById(app.alansari.R.id.name_layout);
        cardNumberLayout = (TextInputLayout) findViewById(app.alansari.R.id.card_num_layout);
        etName = (EditText) findViewById(app.alansari.R.id.name);
        etCardNumber = (EditText) findViewById(app.alansari.R.id.card_num);
        tvName = (TextView) findViewById(app.alansari.R.id.name_c);
        tvCardNumber1 = (TextView) findViewById(app.alansari.R.id.card_num_1);
        tvCardNumber2 = (TextView) findViewById(app.alansari.R.id.card_num_2);
        tvCardNumber3 = (TextView) findViewById(app.alansari.R.id.card_num_3);
        tvCardNumber4 = (TextView) findViewById(app.alansari.R.id.card_num_4);
        tvCardNumber2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        tvCardNumber3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        confirmLayoutBg = findViewById(app.alansari.R.id.add_credit_card_confirm_bg);
        btnSave = (CircularProgressButton) findViewById(app.alansari.R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
        btnSave.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == etName.getEditableText()) {
                    if (validateValues(etName, nameLayout, getString(app.alansari.R.string.invalid_cardholder_name))) {
                        tvName.setText(etName.getText().toString().trim());
                    } else {
                        tvName.setText(null);
                    }
                } else if (s == etCardNumber.getEditableText()) {
                    if (validateValues(etCardNumber, cardNumberLayout, getString(app.alansari.R.string.invalid_card_number))) {
                        String cardNumber = etCardNumber.getText().toString().trim();
                        cardNumber = cardNumber.replaceAll("\\s+", "");
                        if (cardNumber.length() > 4) {
                            tvCardNumber1.setText(cardNumber.substring(0, 4));
                            if (cardNumber.length() > 8) {
                                tvCardNumber2.setText(cardNumber.substring(4, 8));
                                if (cardNumber.length() > 12) {
                                    tvCardNumber3.setText(cardNumber.substring(8, 12));
                                    if (cardNumber.length() > 16) {
                                        tvCardNumber4.setText(cardNumber.substring(12, 16));
                                    } else {
                                        tvCardNumber4.setText(cardNumber.substring(12));
                                    }
                                } else {
                                    tvCardNumber3.setText(cardNumber.substring(8));
                                    tvCardNumber4.setText(null);
                                }
                            } else {
                                tvCardNumber2.setText(cardNumber.substring(4));
                                tvCardNumber3.setText(null);
                                tvCardNumber4.setText(null);
                            }
                        } else {
                            tvCardNumber1.setText(cardNumber);
                            tvCardNumber2.setText(null);
                            tvCardNumber3.setText(null);
                            tvCardNumber4.setText(null);
                        }

//-------------------------------Checking Validation 16 digit---------------------------------------
/*

                        if (cardNumber.length() == 16) {
                            validateCardType(cardNumber);
                        } else {
                            cardNumberLayout.setError(null);
                            cardNumberLayout.setErrorEnabled(false);
//                            etSchemeName.setText(null);
                            tvSchemeName.setText(null);
                            ivCardType.setVisibility(View.INVISIBLE);
                        }
*/
                    } else {
                        tvCardNumber1.setText(null);
                        tvCardNumber2.setText(null);
                        tvCardNumber3.setText(null);
                        tvCardNumber4.setText(null);


                    }
                }
            }
        };

        etName.addTextChangedListener(textWatcher);
        etCardNumber.addTextChangedListener(textWatcher);


        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnNext.setVisibility(View.GONE);
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }
        );
    }

    private boolean validateValues(EditText editText, TextInputLayout inputLayout, String msg) {
        if (editText.getText() != null && editText.getText().toString().trim().length() > 0) {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        } else {
            inputLayout.setError(msg);
            inputLayout.setErrorEnabled(true);
            return false;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.next_btn:
                if (validateValues(etName, nameLayout, getString(app.alansari.R.string.invalid_cardholder_name))) {
                    if (validateValues(etCardNumber, cardNumberLayout, getString(app.alansari.R.string.invalid_card_number))) {
                        //verifyOTP();
                        validateWireCard();
                    }
                }
                break;
            case app.alansari.R.id.dialog_btn:
                if (btnSave.getProgress() == -1) {
                    btnSave.setProgress(0);
                    generateOTP();
                } else if (btnSave.getProgress() == 100) {
                    onFinish();
                } else if (btnSave.getProgress() > 0 && btnSave.getProgress() < 100) {
                } else {
                    generateOTP();
                }
                break;
        }

    }

    private void validateWireCard() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = null;
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addTravelCardReload(CommonUtils.getUserId(), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), etName.getText().toString().trim(), etCardNumber.getText().toString().trim(), LogoutCalling.getDeviceID(context), sessionTime), Constants.VALIDATE_TRAVEL_CARD_RELOAD_URL, CommonUtils.SERVICE_TYPE.VALIDATE_TRAVEL_CARD_RELOAD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.VALIDATE_TRAVEL_CARD_RELOAD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VALIDATE_TRAVEL_CARD_RELOAD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.VALIDATE_TRAVEL_CARD_RELOAD.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void generateOTP() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionId = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTPAfterLogin(CommonUtils.getUserMobile(), userPkId, LogoutCalling.getDeviceID(context), sessionId), Constants.RESEND_OTP_URL_WEBSERVICE, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void onFinish() {
        Intent data = new Intent();
        if (btnSave.getVisibility() == View.VISIBLE) {
            if (btnSave.getProgress() == 100) {
                if (travelCardInfo != null) {
                    if (isUpdate)
                        travelCardInfo = null;
                    data.putExtra(Constants.OBJECT, travelCardInfo);
                    setResult(AppCompatActivity.RESULT_OK, data);
                } else {
                    setResult(AppCompatActivity.RESULT_CANCELED, data);
                }
                finish();
            } else if (btnSave.getProgress() == 0 || btnSave.getProgress() == -1) {
                setConfirmLayoutVisibility(false);
                btnNext.setVisibility(View.VISIBLE);
            }
        } else {
            setResult(AppCompatActivity.RESULT_CANCELED, data);
            finish();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case VALIDATE_TRAVEL_CARD_RELOAD:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            confirmData();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAddTravelCardError();
                    }
                } else {
                    onAddTravelCardError();
                }
                break;
            case RESEND_OTP:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            verifyOTP();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE) && response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4032")) {
                            btnSave.setProgress(0);
                            submitData();

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
            case SUBMIT_ADD_TRAVEL_CARD:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TravelCardInfo> travelCardInfo= (ArrayList<TravelCardInfo>)new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(),new TypeToken<ArrayList<TravelCardInfo>>(){}.getType());
                                if(travelCardInfo.size()>0 && travelCardInfo !=null){
                                    btnSave.setProgress(100);
                                    this.travelCardInfo = travelCardInfo.get(0);
                                    return;
                                }
                            }
                            onAddTravelCardError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            btnSave.setProgress(-1);
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            onFinish();
                        } else {
                            onAddTravelCardError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAddTravelCardError();
                    }
                } else {
                    onAddTravelCardError();
                }
                break;

        }

    }

    private void confirmData() {
        btnSave.setProgress(0);
        btnNext.setVisibility(View.GONE);
        setConfirmLayoutVisibility(true);
    }

    private void onAddTravelCardError() {
        btnSave.setProgress(-1);
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    private void verifyOTP() {
//        Intent intent = new Intent(context, ChangePinActivity.class);
//        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
//        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
//        intent.putExtra(Constants.MOBILE_NUM, mobileNumber);
//        startActivityForResult(intent, Constants.REGISTER_MOBILE);

        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, Constants.VOTP);
        intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
        startActivityForResult(intent, Constants.VOTP_CODE);

    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            btnSave.setProgress(50);
            JsonObjectRequest jsonObjReq = null;
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addTravelCard(CommonUtils.getUserId(), etName.getText().toString().trim(), etCardNumber.getText().toString().trim(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SUBMIT_ADD_TRAVEL_CARD_URL, CommonUtils.SERVICE_TYPE.SUBMIT_ADD_TRAVEL_CARD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.SUBMIT_ADD_TRAVEL_CARD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.SUBMIT_ADD_TRAVEL_CARD.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setConfirmLayoutVisibility(boolean visibility) {
        btnSave.setVisibility(View.VISIBLE);
        if (visibility) {
            ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Confirmation");
            btnSave.startAnimation(slideDownAnimation);
        } else {
            ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("New Card");
            btnSave.setVisibility(View.GONE);
        }
        confirmLayoutBg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(confirmLayoutBg, "alpha", visibility ? 0f : 1f, visibility ? 1f : 0f).setDuration(600).start();
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Constants.VOTP_CODE) {
                    btnSave.setProgress(0);
                    submitData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

}
