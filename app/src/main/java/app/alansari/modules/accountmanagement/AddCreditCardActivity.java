package app.alansari.modules.accountmanagement;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Arrays;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.AsteriskPasswordTransformationMethod;
import app.alansari.Utils.CardType;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.customviews.progressbutton.CircularProgressButton;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CreditCardBankData;
import app.alansari.models.CreditCardData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddCreditCardActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Animation slideUpAnimation, slideDownAnimation;
    private boolean isUpdate;
    private Intent intent;
    private Context context;
    private Button btnNext;
    private String mobileNumber;
    private CreditCardData creditCardData;
    private CircularProgressButton btnSave;
    private CreditCardBankData selectedBank;
    private View confirmLayout, confirmLayoutBg;
    private AppCompatImageView btnMinus, btnPlus, ivCardType;
    private TextView tvName, tvCardNumber1, tvCardNumber2, tvCardNumber3, tvCardNumber4, tvSchemeName, tvBankName, tvPaymentDate, tvReminder;
    private EditText etName, etCardNumber, etSchemeName, etBankName, etPaymentDate;
    private TextInputLayout nameLayout, cardNumberLayout, schemeNameLayout, bankNameLayout, paymentDateLayout,scheme_name_layout;
    private ScrollView scrollView;
    private LinearLayout layoutReminder;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {

        scrollView = findViewById(app.alansari.R.id.scrollview);
        layoutReminder = findViewById(app.alansari.R.id.layout_reminder);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_down_animation);

        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);

        nameLayout = (TextInputLayout) findViewById(app.alansari.R.id.name_layout);
        cardNumberLayout = (TextInputLayout) findViewById(app.alansari.R.id.card_num_layout);
        schemeNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.scheme_name_layout);
        bankNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.bank_name_layout);
        paymentDateLayout = (TextInputLayout) findViewById(app.alansari.R.id.payment_date_layout);
        scheme_name_layout= (TextInputLayout)findViewById(R.id.scheme_name_layout);
        etName = (EditText) findViewById(app.alansari.R.id.name);
        etCardNumber = (EditText) findViewById(app.alansari.R.id.card_num);
        etSchemeName = (EditText) findViewById(app.alansari.R.id.scheme_name);
        etBankName = (EditText) findViewById(app.alansari.R.id.bank_name);
        etPaymentDate = (EditText) findViewById(app.alansari.R.id.payment_date);

        tvName = (TextView) findViewById(app.alansari.R.id.name_c);
        tvCardNumber1 = (TextView) findViewById(app.alansari.R.id.card_num_1_c);
        tvCardNumber2 = (TextView) findViewById(app.alansari.R.id.card_num_2_c);
        tvCardNumber3 = (TextView) findViewById(app.alansari.R.id.card_num_3_c);
        tvCardNumber4 = (TextView) findViewById(app.alansari.R.id.card_num_4_c);

        tvCardNumber2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        tvCardNumber3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        tvSchemeName = (TextView) findViewById(app.alansari.R.id.card_type_name_c);
        ivCardType = (AppCompatImageView) findViewById(app.alansari.R.id.card_type_c);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name_c);

        tvReminder = (TextView) findViewById(app.alansari.R.id.reminder);
        btnMinus = (AppCompatImageView) findViewById(app.alansari.R.id.minus_btn);
        btnPlus = (AppCompatImageView) findViewById(app.alansari.R.id.plus_btn);
        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);

        confirmLayout = findViewById(app.alansari.R.id.add_credit_card_confirm_layout);
        confirmLayoutBg = findViewById(app.alansari.R.id.add_credit_card_confirm_bg);
        btnSave = (CircularProgressButton) findViewById(app.alansari.R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
        btnSave.setOnClickListener(this);

        bankNameLayout.setOnClickListener(this);
        etBankName.setOnClickListener(this);
        etPaymentDate.setOnClickListener(this);
        paymentDateLayout.setOnClickListener(this);
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
                } else if (s == etBankName.getEditableText()) {
                    if (validateValues(etBankName, bankNameLayout, getString(app.alansari.R.string.invalid_bank_name))) {
                        tvBankName.setText(etBankName.getText().toString().trim());
                    } else {
                        tvBankName.setText(null);
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
//                        etSchemeName.setText(null);
                        tvSchemeName.setText(null);
                        ivCardType.setVisibility(View.INVISIBLE);
                    }
                } else if (s == etPaymentDate.getEditableText()) {
                    validateValues(etPaymentDate, paymentDateLayout, getString(app.alansari.R.string.invalid_payment_date));
                }
            }
        };

        etName.addTextChangedListener(textWatcher);
        etCardNumber.addTextChangedListener(textWatcher);
        etBankName.addTextChangedListener(textWatcher);
        etPaymentDate.addTextChangedListener(textWatcher);

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

    private void validateCardType(String cardNumber) {
        String cardType = CardType.getCardType(cardNumber);
        if (cardType != null) {
            cardNumberLayout.setError(null);
            cardNumberLayout.setErrorEnabled(false);
//            etSchemeName.setText(cardType);
            tvSchemeName.setText(cardType);
            ivCardType.setImageResource(CardType.getCardLogo(cardType));
            ivCardType.setVisibility(View.VISIBLE);
        } else {
            cardNumberLayout.setError(getString(app.alansari.R.string.invalid_card_number));
            cardNumberLayout.setErrorEnabled(true);
//            etSchemeName.setText(null);
            tvSchemeName.setText(null);
            ivCardType.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.add_credit_card_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("New Card");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
        init();

        mobileNumber = (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (mobileNumber == null) {
            //CommonUtils.registerAgain(context);
        }

        if (getIntent().getExtras() != null) {
            creditCardData = (CreditCardData) getIntent().getExtras().get(Constants.OBJECT);
            if (creditCardData != null) {
                isUpdate = true;
                setInitialData();
            }
        }
    }

    private void setInitialData() {
        try {
            etName.setText(creditCardData.getName());
            etCardNumber.setText(creditCardData.getCardNumber());
            etBankName.setText(creditCardData.getBankName());
            etSchemeName.setText(creditCardData.getCardTypeName());
            if (Validation.isValidString(creditCardData.getPaymentDate()))
                etPaymentDate.setText(creditCardData.getPaymentDate() + CommonUtils.getNumberSuffix(creditCardData.getPaymentDate()) + " of every month");
            selectedBank = new CreditCardBankData();
            selectedBank.setBankName(creditCardData.getBankName());
            selectedBank.setBankCode(creditCardData.getBankCode());
            if (!TextUtils.isEmpty(creditCardData.getReminder()) && !creditCardData.getReminder().equalsIgnoreCase("null")) {
                tvReminder.setText(creditCardData.getReminder());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            //onFinish();
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.bank_name:
            case app.alansari.R.id.bank_name_layout:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST);
                startActivityForResult(intent, Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.payment_date:
            case app.alansari.R.id.payment_date_layout:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY);
                intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(app.alansari.R.array.credit_card_payment_date))));
                startActivityForResult(intent, Constants.SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                // new DatePickerFragment().show(getSupportFragmentManager(), "Set Payment Date");
                break;
            case app.alansari.R.id.minus_btn:
                if (tvReminder.getText() != null && Integer.valueOf(tvReminder.getText().toString()) > 0) {
                    tvReminder.setText(String.valueOf(Integer.valueOf(tvReminder.getText().toString()) - 1));
                }
                break;
            case app.alansari.R.id.plus_btn:
                if (tvReminder.getText() != null && Integer.valueOf(tvReminder.getText().toString()) < 100) {
                    tvReminder.setText(String.valueOf(Integer.valueOf(tvReminder.getText().toString()) + 1));
                }
                break;
            case app.alansari.R.id.next_btn:
                if (validateValues(etName, nameLayout, getString(app.alansari.R.string.invalid_cardholder_name))) {
                    if (validateValues(etCardNumber, cardNumberLayout, getString(app.alansari.R.string.invalid_card_number))) {
                     //   if (etCardNumber.getText().length() != 16 || etSchemeName.getText() == null || etSchemeName.getText().toString().trim().length() <= 0) {
                        if (etSchemeName.getText() == null || etSchemeName.getText().toString().trim().length() <= 0) {
                            //(TextInputLayout)findViewById(R.id.scheme_name_layout).setError(getString(app.alansari.R.string.invalid_card_number));
                            //(TextInputLayout)findViewById(R.id.scheme_name_layout).setErrorEnabled(true);
                            Toast.makeText(context, "Please Enter Bank Name", Toast.LENGTH_SHORT).show();
                        } else {
                            if (validateValues(etBankName, bankNameLayout, getString(app.alansari.R.string.invalid_bank_name))) {
                                if (validateValues(etPaymentDate, paymentDateLayout, getString(app.alansari.R.string.invalid_payment_date))) {
                                    verifyOTP();
                                }
                            }
                        }
                    }
                }
                break;
            case app.alansari.R.id.dialog_btn:
                if (btnSave.getProgress() == -1) {
                    btnSave.setProgress(0);
                    submitData();
                } else if (btnSave.getProgress() == 100) {
                    onFinish();
                } else if (btnSave.getProgress() > 0 && btnSave.getProgress() < 100) {
                } else {
                    submitData();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    private void onFinish() {
        Intent data = new Intent();
        if (btnSave.getVisibility() == View.VISIBLE) {
            if (btnSave.getProgress() == 100) {
                if (creditCardData != null) {
                    if (isUpdate)
                        creditCardData = null;
                    data.putExtra(Constants.OBJECT, creditCardData);
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


    private void verifyOTP() {
//        Intent intent = new Intent(context, ChangePinActivity.class);
//        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
//        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
//        intent.putExtra(Constants.MOBILE_NUM, mobileNumber);
//        startActivityForResult(intent, Constants.REGISTER_MOBILE);
        confirmData();
    }

    private void confirmData() {
        btnSave.setProgress(0);
        btnNext.setVisibility(View.GONE);
        setConfirmLayoutVisibility(true);
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            btnSave.setProgress(50);
            JsonObjectRequest jsonObjReq = null;
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            if (creditCardData == null) {
                jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addCreditCard(CommonUtils.getUserId(), CommonUtils.getMemPkId(Constants.AREX_MAPPING), etName.getText().toString().trim(), etCardNumber.getText().toString().trim(), etSchemeName.getText().toString(), selectedBank.getBankCode(), selectedBank.getBankName(), getDate(etPaymentDate.getText().toString().trim()), tvReminder.getText().toString(), LogoutCalling.getDeviceID(context),sessionTime), Constants.ADD_CREDIT_CARDS_URL, CommonUtils.SERVICE_TYPE.ADD_CREDIT_CARDS, Request.Method.PUT, this);
            } else {
                jsonObjReq = new CallAddr().executeApi(new APIRequestParams().updateCreditCard(CommonUtils.getUserId(), CommonUtils.getMemPkId(Constants.AREX_MAPPING), creditCardData.getId(), etName.getText().toString().trim(), etCardNumber.getText().toString().trim(), etSchemeName.getText().toString(), selectedBank.getBankCode(), selectedBank.getBankName(), getDate(etPaymentDate.getText().toString().trim()), tvReminder.getText().toString(),LogoutCalling.getDeviceID(context),sessionTime), Constants.UPDATE_CREDIT_CARDS_URL, CommonUtils.SERVICE_TYPE.ADD_CREDIT_CARDS, Request.Method.PUT, this);
            }
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_CREDIT_CARDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_CREDIT_CARDS.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private String getDate(String date) {
        return date.replaceAll("[^0-9]", "");
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case ADD_CREDIT_CARDS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CreditCardData> creditCardData = (ArrayList<CreditCardData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CreditCardData>>() {
                                }.getType());
                                if (creditCardData != null && creditCardData.size() > 0) {
                                    btnSave.setProgress(100);
                                    this.creditCardData = creditCardData.get(0);
                                    return;
                                }
                            }
                            onAddCreditCardError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            btnSave.setProgress(-1);
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            onFinish();
                        } else {
                            onAddCreditCardError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAddCreditCardError();
                    }
                } else {
                    onAddCreditCardError();
                }
                break;
        }
    }


    private void onAddCreditCardError() {
        btnSave.setProgress(-1);
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST && resultCode == Activity.RESULT_OK) {
                selectedBank = data.getParcelableExtra(Constants.OBJECT);
                if (selectedBank != null) {
                    etBankName.setText(selectedBank.getBankName());
                    etSchemeName.setText(selectedBank.getCreditCardType());
                }

            }
            if (requestCode == Constants.SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY && resultCode == Activity.RESULT_OK) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, layoutReminder.getBottom());
                    }
                });
                if (data.getStringExtra(Constants.OBJECT) != null)
                    etPaymentDate.setText(data.getStringExtra(Constants.OBJECT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout=CommonUtils.isAppOnForeground(context);
        if(mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

}