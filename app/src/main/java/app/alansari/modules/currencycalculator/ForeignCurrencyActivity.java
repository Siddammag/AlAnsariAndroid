package app.alansari.modules.currencycalculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectCurrencyFlagActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CountryData;
import app.alansari.models.CurrencyData;
import app.alansari.modules.branchlocator.BranchLocatorCityActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.REQUEST_CALL_BACK;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;


/**
 * Created by Parveen Dala on 21 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class ForeignCurrencyActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private Gson gson;
    private Context context;
    private int currentDirection = 1;
    private CountryData selectedCountryFrom, selectedCountryTo;
    private CurrencyData selectedCurrency = null;
    private boolean isCalculatingCurrency = false;

    //View
    private Dialog requestCallBackDialog;
    private EditText etFrom, etTo;
    private TextWatcher textWatcher;
    private CircleProgressBar progressBar;
    private View amountFragmentLayoutCover;
    private AppCompatImageView ivFromFlag, ivToFlag, btnSwap;
    private CountryData.CurrencyData selectedCurrencyDataFrom, selectedCurrencyDataTo;
    private TextView tvFromCode, tvToCode, tvRate, tvAssistanceCall, tvSelectCurrency, tvBuy, tvSell, tvCurrency;
    // 1 For SEND, 2 For GET
    private int selectCountryType = 1;
    private String FCY_TYP = "B";
    private Dialog noCurrencyDialog;

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

    private void init() {

        etFrom = (EditText) findViewById(app.alansari.R.id.from);
        etTo = (EditText) findViewById(app.alansari.R.id.to);
        tvFromCode = (TextView) findViewById(app.alansari.R.id.from_code);
        tvToCode = (TextView) findViewById(app.alansari.R.id.to_code);
        ivFromFlag = (AppCompatImageView) findViewById(app.alansari.R.id.from_flag);
        ivToFlag = (AppCompatImageView) findViewById(app.alansari.R.id.to_flag);
        tvRate = (TextView) findViewById(app.alansari.R.id.rate);
        tvSelectCurrency = (TextView) findViewById(app.alansari.R.id.select_currency);
        tvCurrency = (TextView) findViewById(app.alansari.R.id.currency);
        tvBuy = (TextView) findViewById(app.alansari.R.id.buy);
        tvSell = (TextView) findViewById(app.alansari.R.id.sell);

        tvAssistanceCall = (TextView) findViewById(app.alansari.R.id.assistance_call);

        amountFragmentLayoutCover = findViewById(app.alansari.R.id.amount_fragment_layout_cover);

        progressBar = (CircleProgressBar) findViewById(app.alansari.R.id.progress_bar);
        btnSwap = (AppCompatImageView) findViewById(app.alansari.R.id.swap_btn);


        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvSelectCurrency.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);

        progressBar.setVisibility(View.GONE);
        btnSwap.setVisibility(View.GONE);
        tvAssistanceCall.setOnClickListener(this);
        findViewById(app.alansari.R.id.branch_locator_btn).setOnClickListener(this);

        ivFromFlag.setImageResource(0);
        ivToFlag.setImageResource(0);
        tvFromCode.setText(null);
        tvToCode.setText(null);

        ivFromFlag.setVisibility(View.INVISIBLE);
        ivToFlag.setVisibility(View.INVISIBLE);
        tvFromCode.setVisibility(View.INVISIBLE);
        tvToCode.setVisibility(View.INVISIBLE);

        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == etFrom.getEditableText()) {
                    if (s.length() > 5) {
                        etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etFrom.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etFrom)) {
                        etTo.setText("");
                        progressBar.setVisibility(View.GONE);
                        btnSwap.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etFrom, textWatcher, s.toString());
                } else if (s == etTo.getEditableText()) {
                    if (s.length() > 5) {
                        etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etTo.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etTo)) {
                        etFrom.setText("");
                        progressBar.setVisibility(View.GONE);
                        btnSwap.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etTo, textWatcher, s.toString());
                }
            }
        };

        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    CommonUtils.hideKeyboard(context);
                    if (v.getId() == etFrom.getId()) {
                        if (Validation.isValidEditTextValue(etFrom)) {
                            currentDirection = 1;
                            calculateCurrency();
                        }
                        return true;
                    } else if (v.getId() == etTo.getId()) {
                        if (Validation.isValidEditTextValue(etTo)) {
                            currentDirection = 2;
                            calculateCurrency();
                        }
                        return true;
                    }
                }
                return false;
            }
        };

        etFrom.addTextChangedListener(textWatcher);
        etTo.addTextChangedListener(textWatcher);
        etFrom.setOnEditorActionListener(editiorListener);
        etTo.setOnEditorActionListener(editiorListener);

//        etTo.setEnabled(false);
        noCurrencyDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        noCurrencyDialog.setCanceledOnTouchOutside(false);
        noCurrencyDialog.setContentView(R.layout.adds_dialog);
        ((TextView) noCurrencyDialog.findViewById(app.alansari.R.id.dialog_title)).setText(context.getString(app.alansari.R.string.currency_not_selected));
        noCurrencyDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noCurrencyDialog.dismiss();
                etFrom.clearFocus();
                etTo.clearFocus();
            }
        });
        etFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && selectedCurrency == null) {
                    noCurrencyDialog.show();
                }
            }
        });
        etTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && selectedCurrency == null) {
                    noCurrencyDialog.show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.foreign_currency_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Foreign");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Currency");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        gson = new Gson();
        init();
        boolean hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        }
        setInitialData();
    }



    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FOREIGN_CURRENCY:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            LogUtils.d("ok", "Amount   " + response.getString(Constants.AMOUNT));
                            LogUtils.d("ok", "Rate   " + response.getString(Constants.RATE));
                            if (Validation.isValidRate(response.getString(Constants.AMOUNT)) && Validation.isValidRate(response.getString(Constants.RATE))) {
                                isCalculatingCurrency = true;
                                Double amountDouble = Double.valueOf(response.getString(Constants.AMOUNT));
                                Double rateDouble = Double.valueOf(response.getString(Constants.RATE));
                                /*setRateData(String.valueOf(rateDouble));
                                setAmountData(CommonUtils.getDecimalFormattedString(amountDouble));*/
                                setRateData(response.getString(Constants.RATE));
                                setAmountData(response.getString(Constants.AMOUNT));
                                onSuccessInCalculation();
                            } else {
                                onAPIError();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            // Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                           /* final Dialog pendingTransactionDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                            pendingTransactionDialog.setCanceledOnTouchOutside(false);
                            pendingTransactionDialog.setContentView(R.layout.adds_dialog);
                            ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(response.getString(Constants.MESSAGE));
                            pendingTransactionDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pendingTransactionDialog.dismiss();
                                }
                            });
                            pendingTransactionDialog.show();*/
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                            onErrorInCalculation();
                        } else {
                            onAPIError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAPIError();
                    }
                } else {
                    onAPIError();
                }
                break;
            case REQUEST_CALL_BACK:
                CommonUtils.hideLoading();
//                status = 1;
//                response = getResponse2();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            requestCallBackDialog = getRequestDialog();
                            ((TextView) requestCallBackDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Request Call Back");
                            ((TextView) requestCallBackDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.thank_you_for_call_back));
                            ((Button) requestCallBackDialog.findViewById(app.alansari.R.id.dialog_btn)).setText("Done");
                            requestCallBackDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestCallBackDialog.dismiss();
                                }
                            });
                            requestCallBackDialog.show();
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            onErrorInCalculation();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        onErrorInCalculation();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    onErrorInCalculation();
                }
                break;
        }
    }

    private void setInitialData() {
        selectedCountryTo = SharedPreferenceManger.loadSelectedCountryData();
        if (selectedCountryTo != null && !selectedCountryTo.getLatinName().trim().equalsIgnoreCase("ALL"))
            setCountryData(2);
        else {
            selectCountryType = 3;
            openCurrencyList();
        }
        setFromInitialData();
    }
    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
        intent.putExtra("hideFirstItem", true);
        startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private void setFromInitialData() {
        selectedCountryFrom = SharedPreferenceManger.loadUAECountryData();
        if (selectedCountryFrom != null) {
            setCountryData(1);
        } else {
            CommonUtils.setCountryFlagImage(context, ivFromFlag, null);
            tvFromCode.setText(null);
        }
    }

    private void setCountryData(int type) {
        cancelPendingRequests();
        if (type == 1) {
            if (selectedCountryFrom != null) {
                resetAmount();
                if (selectedCountryFrom.getCurrencyData() != null) {
                    selectedCurrencyDataFrom = CommonUtils.getDefaultCurrencyData(selectedCountryFrom);
                    if (selectedCurrencyDataFrom != null) {
                        setCurrencyCodeFlagData(type);
                    } else {
                        tvFromCode.setText(null);
                        ivFromFlag.setImageResource(0);
                        Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
                        selectCountryType = 1;
                        openCountryList();
                    }
                } else {
                    setFragmentCoverVisibility(View.VISIBLE);
                    tvFromCode.setText(null);
                    ivFromFlag.setImageResource(0);
                    Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
                    selectCountryType = 1;
                    openCountryList();
                }
            } else {
                selectCountryType = 1;
                openCountryList();
            }
        } else if (type == 2) {
            if (selectedCountryTo != null) {
                resetAmount();
                if (selectedCountryTo.getCurrencyData() != null) {
                    selectedCurrencyDataTo = CommonUtils.getDefaultCurrencyData(selectedCountryTo);
                    if (selectedCurrencyDataTo != null) {
                        setCurrencyCodeFlagData(type);
                    } else {
                        tvToCode.setText(null);
                        ivToFlag.setImageResource(0);
                        Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
                        selectCountryType = 2;
                        openCountryList();
                    }
                } else {
                    tvToCode.setText(null);
                    ivToFlag.setImageResource(0);
                    Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
                    selectCountryType = 2;
                    openCountryList();
                }
            } else {
                selectCountryType = 2;
                openCountryList();
            }
        }
    }
    private void openCurrencyList() {
        Intent intent = new Intent(context, SelectCurrencyFlagActivity.class);
//        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_CURRENCY);
        startActivityForResult(intent, Constants.SELECT_ITEM_CURRENCY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private void setRateData(String rate) {
        if ((selectedCountryFrom != null && selectedCountryTo != null && selectedCurrencyDataFrom != null && selectedCurrencyDataTo != null) || selectedCurrency != null) {
            //TODO Use String
            tvRate.setVisibility(View.VISIBLE);
            try {
                tvRate.setText("Exchange Rate " + selectedCurrency.getShortName() + " = AED " + rate);
            } catch (Exception ex) {
                tvRate.setVisibility(View.GONE);
            }
        } else {
            tvRate.setVisibility(View.GONE);
        }
    }

    private void resetAmount() {
        etFrom.setText(null);
        etTo.setText(null);
        tvRate.setText(null);
        tvRate.setVisibility(View.GONE);
    }

    public void setFragmentCoverVisibility(int visibility) {
        amountFragmentLayoutCover.setVisibility(visibility);
    }

    private Dialog getRequestDialog() {
        if (requestCallBackDialog == null) {
            requestCallBackDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
            requestCallBackDialog.setCanceledOnTouchOutside(false);
            requestCallBackDialog.setContentView(app.alansari.R.layout.generic_single_btn_dialog);
        }
        return requestCallBackDialog;
    }

    private void swapData() {
        //String temp = etFrom.getText().toString();
        //etFrom.setText(etTo.getText().toString());
        //etTo.setText(temp);
        //temp = null;

        CountryData tempCountryData = selectedCountryFrom;
        selectedCountryFrom = selectedCountryTo;
        selectedCountryTo = tempCountryData;
        tempCountryData = null;

        CountryData.CurrencyData tempCurrencyData = selectedCurrencyDataFrom;
        selectedCurrencyDataFrom = selectedCurrencyDataTo;
        selectedCurrencyDataTo = tempCurrencyData;
        tempCurrencyData = null;

        setCurrencyCodeFlagData(1);
        setCurrencyCodeFlagData(2);
        calculateCurrency();
    }

    private void setCurrencyCodeFlagData(int type) {
        if (type == 1) {
            tvFromCode.setText(selectedCurrencyDataFrom.getName());
            if (selectedCountryFrom.getFlag() != null) {
                CommonUtils.setCountryFlagImage(context, ivFromFlag, selectedCountryFrom.getFlag());
            } else {
                CommonUtils.setCountryFlagImage(context, ivFromFlag, null);
            }
        } else if (type == 2) {
            tvToCode.setText(selectedCurrencyDataTo.getName());
            if (selectedCountryTo.getFlag() != null) {
                CommonUtils.setCountryFlagImage(context, ivToFlag, selectedCountryTo.getFlag());
            } else {
                CommonUtils.setCountryFlagImage(context, ivToFlag, null);
            }
        }
    }

    private void calculateCurrency() {
        try {
            if ((currentDirection == 1 && Validation.isValidEditTextValue(etFrom)) || (currentDirection == 2 && Validation.isValidEditTextValue(etTo))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    JSONObject requestParams;
                    String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    if (currentDirection == 1)
                        requestParams = new APIRequestParams().foreignCurrency(selectedCurrency.getCURRENCYCODE(), "91", FCY_TYP, CommonUtils.getTextFromEditText(etFrom), userPkId, LogoutCalling.getDeviceID(context), sessionTime);
                    else
                        requestParams = new APIRequestParams().foreignCurrency("91", selectedCurrency.getCURRENCYCODE(), FCY_TYP, CommonUtils.getTextFromEditText(etTo), userPkId, LogoutCalling.getDeviceID(context), sessionTime);
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(requestParams, Constants.FOREIGN_CURRENCY_URL, CommonUtils.SERVICE_TYPE.FOREIGN_CURRENCY, Request.Method.PUT, this);
                    cancelPendingRequests();
                    isCalculatingCurrency = true;
                    setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    btnSwap.setVisibility(View.GONE);
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FOREIGN_CURRENCY.toString());
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                setTransparentCoverVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelPendingRequests() {
        AppController.getInstance().cancelPendingRequests(CommonUtils.SERVICE_TYPE.FOREIGN_CURRENCY.toString());
        onErrorInCalculation();
    }

    public void setTransparentCoverVisibility(int visibility) {
        findViewById(app.alansari.R.id.transparent_view).setVisibility(visibility);
    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.branch_locator_btn:
                startActivity(new Intent(context, BranchLocatorCityActivity.class).putExtra(Constants.HIDE_BURGER_MENU, getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false)));
                break;
           /* case R.id.call_btn:
                requestCallBackDialog = getRequestDialog();
                ((TextView) requestCallBackDialog.findViewById(R.id.pending_title)).setText("Request Call Back");
                ((TextView) requestCallBackDialog.findViewById(R.id.pending_text)).setText(getString(R.string.request_for_call_back) + " " + CommonUtils.getUserMobile(context).substring(3));
                ((Button) requestCallBackDialog.findViewById(R.id.save_btn)).setText("Request Call");
                requestCallBackDialog.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCallBack();
                        requestCallBackDialog.dismiss();
                    }
                });
                requestCallBackDialog.show();
                break;*/
            case app.alansari.R.id.assistance_call:
                CommonUtils.goToDialPad(context, tvAssistanceCall.getText().toString().trim());
                break;
            case app.alansari.R.id.from_flag:
            case app.alansari.R.id.from_code:
                selectCountryType = 1;
                openCountryList();
                break;
            case app.alansari.R.id.to_flag:
            case app.alansari.R.id.to_code:
                selectCountryType = 2;
                openCountryList();
                break;
            case app.alansari.R.id.currency:
                selectCountryType = 3;
                openCurrencyList();
                break;
            case app.alansari.R.id.select_currency:
                selectCountryType = 3;
                openCurrencyList();
                break;
            case app.alansari.R.id.buy:
                FCY_TYP = "B";
                tvBuy.setBackgroundColor(getResources().getColor(R.color.colorPrimary_blue));
                tvBuy.setTextColor(getResources().getColor(R.color.white));
                tvSell.setBackgroundColor(0);
                tvSell.setTextColor(getResources().getColor(R.color.color373A49));
/*                if (Validation.isValidEditTextValue(etFrom)) {
                    currentDirection = 1;
                    calculateCurrency();
                }*/
                calculateCurrency();
                break;
            case app.alansari.R.id.sell:
                FCY_TYP = "S";
                tvSell.setBackgroundColor(getResources().getColor(R.color.colorPrimary_blue));
                tvSell.setTextColor(getResources().getColor(R.color.white));
                tvBuy.setBackgroundColor(0);
                tvBuy.setTextColor(getResources().getColor(R.color.color373A49));
/*                if (Validation.isValidEditTextValue(etFrom)) {
                    currentDirection = 1;
                    calculateCurrency();
                }*/
                calculateCurrency();
                break;
        }
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        btnSwap.setVisibility(View.GONE);
        setTransparentCoverVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    if (selectCountryType == 1) {
                        selectedCountryFrom = data.getParcelableExtra(Constants.OBJECT);
                    } else if (selectCountryType == 2) {
                        selectedCountryTo = data.getParcelableExtra(Constants.OBJECT);
                    }
                    setCountryData(selectCountryType);
                    if (selectedCountryFrom == null) {
                        setFromInitialData();
                    }
                } else {
                    if (selectedCountryTo == null || selectedCountryTo.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.SELECT_ITEM_CURRENCY) {
                if (selectCountryType == 3) {
                    selectedCurrency = data.getParcelableExtra(Constants.OBJECT);
                    tvSelectCurrency.setText(selectedCurrency.getNAME());
                    CommonUtils.setCountryFlagImage(context, ivFromFlag, selectedCurrency.getFlag());
                    ivToFlag.setImageResource(R.drawable.svg_flag_aed);
                    tvFromCode.setText(selectedCurrency.getShortName());
                    tvToCode.setText("AED");
                    ivFromFlag.setVisibility(View.VISIBLE);
                    ivToFlag.setVisibility(View.VISIBLE);
                    tvToCode.setVisibility(View.VISIBLE);
                    tvFromCode.setVisibility(View.VISIBLE);
                    resetAmount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestCallBack() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().requestCallBack((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), CommonUtils.getUserMobile()), Constants.REQUEST_CALL_BACK_URL, REQUEST_CALL_BACK, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(REQUEST_CALL_BACK.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, REQUEST_CALL_BACK.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), REQUEST_CALL_BACK.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccessInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        btnSwap.setVisibility(View.VISIBLE);
        setTransparentCoverVisibility(View.GONE);
    }

    private void setAmountData(String amount) {
        if (currentDirection == 1) {
            etTo.setText(amount);
        } else {
            etFrom.setText(amount);
        }
    }

    private void onAPIError() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        onErrorInCalculation();
    }

    private JSONObject getResponse2() {
        try {
            return new JSONObject("{\n" +
                    "  \"RESULT\": null,\n" +
                    "  \"AMOUNT\": \"123\",\n" +
                    "  \"RATE\": null,\n" +
                    "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "  \"STATUS_CODE\": 257,\n" +
                    "  \"MESSAGE\": \"SUCCESS\",\n" +
                    "  \"FEE\": null\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponse(String amount) {
        float fee = new Random().nextFloat() * (75f - 15f) + 15f;
        float rate = new Random().nextFloat() * (25f - 0f) + 0f;
        return "{\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"217\",\n" +
                "  \"MESSAGE\": \"Fetch Amount successful\",\n" +
                "  \"AMOUNT\": \"" + (Float.valueOf(amount) * rate) + "\",\n" +
                "  \"RATE\": \"" + rate + "\",\n" +
                "  \"FEE\": \"" + fee + "\"\n" +
                "}";
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
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }








}
