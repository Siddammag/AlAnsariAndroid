package app.alansari.modules.accountmanagement;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.progressbutton.CircularProgressButton;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserAccountBankData;
import app.alansari.models.UserAccountData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADD_USER_ACCOUNTS;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 17 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBankAccountActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private boolean isUpdate;
    private Intent intent;
    private Context context;
    private Button btnNext;
    private String mobileNumber;
    private UserAccountData userAccountData;
    private CircularProgressButton btnSave;
    private UserAccountBankData selectedBank;
    private Animation slideUpAnimation, slideDownAnimation;

    private View confirmLayout, confirmLayoutBg;
    private TextView tvAccountName, tvBankName, tvBranchName, tvIbanNumber;
    private EditText etAccountName, etBankName, etBranchName, etIbanNumber;
    private TextInputLayout accountNameLayout, bankNameLayout, branchNameLayout, ibanNumberLayout;

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

    private void onFinish() {
        Intent data = new Intent();
        if (confirmLayout.getVisibility() == View.VISIBLE) {
            if (btnSave.getProgress() == 100) {
                if (userAccountData != null) {
                    if (isUpdate)
                        userAccountData = null;
                    data.putExtra(Constants.OBJECT, userAccountData);
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
        confirmLayout.setVisibility(View.VISIBLE);
        if (visibility) {
            confirmLayout.startAnimation(slideDownAnimation);
        } else {
            confirmLayout.startAnimation(slideUpAnimation);
            confirmLayout.setVisibility(View.GONE);
        }
        confirmLayoutBg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(confirmLayoutBg, "alpha", visibility ? 0f : 1f, visibility ? 1f : 0f).setDuration(600).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.add_bank_account_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Bank");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_bank_account_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_bank_account_icon);
        //TODO Remove Line
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color024B54));
        init();
        Toolbar toolbarConfirm = (Toolbar) confirmLayout.findViewById(app.alansari.R.id.toolbar);
        ((TextView) toolbarConfirm.findViewById(app.alansari.R.id.toolbar_title)).setText("Bank Account Confirmation");
        toolbarConfirm.setNavigationIcon(app.alansari.R.drawable.ic_back_arrow);
        toolbarConfirm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });

        mobileNumber = (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (mobileNumber == null) {
            CommonUtils.registerAgain(context);
            return;
        }

        if (getIntent().getExtras() != null) {
            userAccountData = (UserAccountData) getIntent().getExtras().get(Constants.OBJECT);
            if (userAccountData != null) {
                isUpdate = true;
                setInitialData();
            }
        }
    }

    private void init() {
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_down_animation);

        accountNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.name_layout);
        bankNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.bank_name_layout);
        branchNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.branch_name_layout);
        ibanNumberLayout = (TextInputLayout) findViewById(app.alansari.R.id.iban_number_layout);
        etAccountName = (EditText) findViewById(app.alansari.R.id.name);
        etBankName = (EditText) findViewById(app.alansari.R.id.bank_name);
        etBranchName = (EditText) findViewById(app.alansari.R.id.branch_name);
        etIbanNumber = (EditText) findViewById(app.alansari.R.id.iban_number);
        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        tvAccountName = (TextView) findViewById(app.alansari.R.id.name_c);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name_c);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name_c);
        tvIbanNumber = (TextView) findViewById(app.alansari.R.id.iban_number_c);
        confirmLayout = findViewById(app.alansari.R.id.add_bank_account_confirm_layout);
        confirmLayoutBg = findViewById(app.alansari.R.id.add_bank_account_confirm_bg);
        btnSave = (CircularProgressButton) findViewById(app.alansari.R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
        btnSave.setOnClickListener(this);

        bankNameLayout.setOnClickListener(this);
        etBankName.setOnClickListener(this);
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
                if (s == etAccountName.getEditableText()) {
                    validateValues(etAccountName, accountNameLayout, "Invalid account name");
                } else if (s == etBankName.getEditableText()) {
                    validateValues(etBankName, bankNameLayout, "Invalid bank name");
                } else if (s == etIbanNumber.getEditableText()) {
                    validateValues(etIbanNumber, ibanNumberLayout, "IBAN should start with AE followed by 21 digits");
                }
            }
        };

        etAccountName.addTextChangedListener(textWatcher);
        etBankName.addTextChangedListener(textWatcher);
        etBranchName.addTextChangedListener(textWatcher);
        etIbanNumber.addTextChangedListener(textWatcher);

        etIbanNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && etIbanNumber.getText() != null && etIbanNumber.length() == 2 && etIbanNumber.getText().toString().contains("AE")) {
                    etIbanNumber.setSelection(2);
                }
            }
        });

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

    private void setInitialData() {
        try {
            etAccountName.setText(userAccountData.getAccountName());
            etBankName.setText(userAccountData.getBankName());
            etBranchName.setText(userAccountData.getBankBranchName());
            etIbanNumber.setText(userAccountData.getIBANNumber());
            selectedBank = new UserAccountBankData();
            selectedBank.setBankName(userAccountData.getBankName());
            selectedBank.setBankCode(userAccountData.getBankCode());

            etAccountName.setEnabled(false);
            etBankName.setEnabled(false);
            etBranchName.setEnabled(false);

            bankNameLayout.setOnClickListener(null);
            etBankName.setOnClickListener(null);


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            onFinish();
        }
    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.toolbar_right_icon:
                if (!validateValues(etBankName, bankNameLayout, "")) {
                    selectedBank = new UserAccountBankData();
                    selectedBank.setBankName("HDFC Dummy");
                    selectedBank.setBankCode("290");
                    etBankName.setText(selectedBank.getBankName());
                }
                break;
            case app.alansari.R.id.bank_name:
            case app.alansari.R.id.bank_name_layout:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST);
                startActivityForResult(intent, Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.next_btn:
                if (validateValues(etAccountName, accountNameLayout, "Invalid account name")) {
                    if (validateValues(etBankName, bankNameLayout, "Invalid bank name")) {
                        if (validateValues(etIbanNumber, ibanNumberLayout, "IBAN should start with AE followed by 21 digits")) {
                            if (etIbanNumber.length() == 23 && etIbanNumber.getText().toString().toLowerCase().startsWith("ae"))
                                verifyOTP();
                            else {
                                ibanNumberLayout.setError("IBAN should start with AE followed by 21 digits");
                                ibanNumberLayout.setErrorEnabled(true);
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
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case ADD_USER_ACCOUNTS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<UserAccountData> userAccountData = (ArrayList<UserAccountData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<UserAccountData>>() {
                                }.getType());
                                if (userAccountData != null && userAccountData.size() > 0) {
                                    btnSave.setProgress(100);
                                    this.userAccountData = userAccountData.get(0);
                                    return;
                                }
                            }
                            onAddUserAccountError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            btnSave.setProgress(-1);
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                            onFinish();
                        } else {
                            onAddUserAccountError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAddUserAccountError();
                    }
                } else {
                    onAddUserAccountError();
                }
                break;
        }
    }

    private void onAddUserAccountError() {
        btnSave.setProgress(-1);
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST && resultCode == Activity.RESULT_OK) {
                selectedBank = data.getParcelableExtra(Constants.OBJECT);
                if (selectedBank != null)
                    etBankName.setText(selectedBank.getBankName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    private void verifyOTP() {
//        Intent intent = new Intent(context, ChangePinActivity.class);
//        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
//        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
//        intent.putExtra(Constants.MOBILE_NUM, mobileNumber);
//        startActivityForResult(intent, Constants.REGISTER_MOBILE);
        confirmData();
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    private void confirmData() {
        btnNext.setVisibility(View.GONE);
        btnSave.setProgress(0);
        setConfirmLayoutVisibility(true);
        tvAccountName.setText(etAccountName.getText().toString().trim());
        tvBankName.setText(etBankName.getText().toString().trim());
        if (etBranchName.getText() != null && tvBranchName != null)
            tvBranchName.setText(etBranchName.getText().toString().trim());
        tvIbanNumber.setText(etIbanNumber.getText().toString().trim());
    }


    private void submitData() {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                btnSave.setProgress(50);
                JsonObjectRequest jsonObjReq = null;
                String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                if (userAccountData == null) {
                    jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addUserBankAccount((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), tvAccountName.getText().toString().trim(), selectedBank.getBankCode(), selectedBank.getBankName(), etBranchName.getText() != null ? etBranchName.getText().toString().trim() : "", tvIbanNumber.getText().toString().trim(),userPkId, LogoutCalling.getDeviceID(context),sessionTime), Constants.ADD_USER_ACCOUNTS_URL, ADD_USER_ACCOUNTS, Request.Method.PUT, this);
                } else {
                    jsonObjReq = new CallAddr().executeApi(new APIRequestParams().updateUserBankAccount((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), userAccountData.getId(), tvAccountName.getText().toString().trim(), selectedBank.getBankCode(), selectedBank.getBankName(), etBranchName.getText() != null ? etBranchName.getText().toString().trim() : "", tvIbanNumber.getText().toString().trim(),userPkId,LogoutCalling.getDeviceID(context),sessionTime), Constants.UPDATE_USER_ACCOUNTS_URL, ADD_USER_ACCOUNTS, Request.Method.PUT, this);
                }
                AppController.getInstance().getRequestQueue().cancelAll(ADD_USER_ACCOUNTS.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, ADD_USER_ACCOUNTS.toString());
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}