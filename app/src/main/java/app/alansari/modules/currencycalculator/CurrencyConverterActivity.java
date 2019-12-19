package app.alansari.modules.currencycalculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import app.alansari.AppController;
import app.alansari.LoginActivity;
import app.alansari.NavigationBaseActivity;
import app.alansari.PaymentSelectModeActivity;
import app.alansari.R;
import app.alansari.RegisterActivity;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CountryData;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.modules.sendmoney.adapters.SendMoneyCurrencyCodeRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 14 February, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class CurrencyConverterActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private Gson gson;
    private Context context;
    private int currentDirection = 1;
    private String selectedTransferType;
    private String currencyCode, ccyCode;
    private CountryData selectedCountry;
    private boolean isCalculatingCurrency = false;

    //View
    private AppCompatImageView ivFlag, ivSuccess;
    private EditText etSend, etGet;
    private TextView tvCountryName;
    private TextWatcher textWatcher;
    private TabLayout tabLayoutTransferType;
    private View amountFragmentLayoutCover;
    private CardView cardView;
    private CircleProgressBar progressBar;
    private CountryData.CurrencyData selectedCurrencyData, selectedCurrencyFrom;
    private TextView tvSendCode, tvGetCode, tvRate, tvRateOther, tvTimeMessage, tvMessageBottom;

    // Currency Code Scroll
    private CarouselLayoutManager layoutManagerCurrencyCode;
    private RecyclerView recyclerViewCurrencyCode;
    private SendMoneyCurrencyCodeRecyclerAdapter recyclerAdapterCurrencyCode;
    private boolean hideMenu;
    private String userPkId, sessionTime;
    private RelativeLayout layoutpayment;
    private AutoCompleteTextView tvPaymentType;
    private LinearLayout layoutBottom;
    private TextView tvTotalToPay, tvTotalToPayCurrencyCode, tvFeeAED, tvVat, tvDiscount, tvRoundOff;
    private Button btnSend;
    private String vat;
    private String rounding;
    private String discount;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String totalToPay, charge, rate;
    private String serviceType;
    private String modeDescription,name,id;
    private JSONObject jsonObjectRequestPayment;



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
        userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name);
        tabLayoutTransferType = (TabLayout) findViewById(app.alansari.R.id.tab_layout_transfer_type);
        amountFragmentLayoutCover = findViewById(app.alansari.R.id.amount_fragment_layout_cover);
        cardView = (CardView) findViewById(app.alansari.R.id.amount_cal_card_view);
        etSend = (EditText) findViewById(app.alansari.R.id.send);
        etGet = (EditText) findViewById(app.alansari.R.id.get);
        tvSendCode = (TextView) findViewById(app.alansari.R.id.send_code);
        tvGetCode = (TextView) findViewById(app.alansari.R.id.get_code);
        tvRate = (TextView) findViewById(app.alansari.R.id.rate);
        tvRateOther = (TextView) findViewById(app.alansari.R.id.rate_other);
        tvTimeMessage = (TextView) findViewById(app.alansari.R.id.time_message);
        tvMessageBottom = (TextView) findViewById(app.alansari.R.id.message);
        progressBar = (CircleProgressBar) findViewById(app.alansari.R.id.progress_bar);
        ivSuccess = (AppCompatImageView) findViewById(app.alansari.R.id.success_image);
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);

        ivFlag.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);
        findViewById(app.alansari.R.id.send_now_btn).setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.click_to_change).setOnClickListener(this);


        layoutpayment = (RelativeLayout) findViewById(R.id.rel_lay_payment);
        layoutpayment.setVisibility(View.GONE);
        tvPaymentType = (AutoCompleteTextView) findViewById(app.alansari.R.id.payment_type_text);
        tvPaymentType.setOnClickListener(this);
        layoutBottom = (LinearLayout) findViewById(R.id.bottom_layout);
        tvFeeAED = (TextView) findViewById(app.alansari.R.id.fee_aed);
        tvVat = (TextView) findViewById(app.alansari.R.id.vat_label);
        tvDiscount = (TextView) findViewById(app.alansari.R.id.discount_label);
        tvRoundOff = (TextView) findViewById(app.alansari.R.id.roundoff_label);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        btnSend = (Button) findViewById(app.alansari.R.id.send_btn);
        btnSend.setOnClickListener(this);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == etSend.getEditableText()) {
                    if (s.length() > 5) {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etSend)) {
                        etGet.setText("");
                        progressBar.setVisibility(View.GONE);
                        ivSuccess.setVisibility(View.GONE);
                        resetScreen();
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etSend, textWatcher, s.toString());
                } else if (s == etGet.getEditableText()) {
                    if (s.length() > 5) {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etGet)) {
                        etSend.setText("");
                        progressBar.setVisibility(View.GONE);
                        ivSuccess.setVisibility(View.GONE);
                        resetScreen();
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etGet, textWatcher, s.toString());
                }
            }
        };

        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        CommonUtils.hideKeyboard(context);
                        if (v.getId() == etSend.getId()) {
                            if (Validation.isValidEditTextValueZero(etSend)) {
                                currentDirection = 1;
                                calculateCurrency();
                            }else{
                                resetScreen();
                                Toast.makeText(context,"Please enter some amount",Toast.LENGTH_SHORT).show();

                            }
                            return true;
                        } else if (v.getId() == etGet.getId()) {
                            if (Validation.isValidEditTextValueZero(etGet)) {
                                currentDirection = 2;
                                calculateCurrency();
                            }else{
                                resetScreen();
                                Toast.makeText(context,"Please enter some amount",Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        };

        etSend.addTextChangedListener(textWatcher);
        etGet.addTextChangedListener(textWatcher);
        etSend.setOnEditorActionListener(editiorListener);
        etGet.setOnEditorActionListener(editiorListener);

        // Currency Code Scroll
        recyclerViewCurrencyCode = (RecyclerView) findViewById(app.alansari.R.id.recyclerView_currency_code);
        layoutManagerCurrencyCode = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManagerCurrencyCode.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManagerCurrencyCode.setMaxVisibleItems(1);

        layoutManagerCurrencyCode.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                if (adapterPosition >= 0 && (selectedCurrencyData == null || !selectedCurrencyData.getCurrencyCode().equalsIgnoreCase(recyclerAdapterCurrencyCode.getItemAt(adapterPosition).getCurrencyCode()))) {
                    selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(adapterPosition);
                    calculateCurrency();
                }
    }
});
        recyclerViewCurrencyCode.setLayoutManager(layoutManagerCurrencyCode);
        recyclerViewCurrencyCode.setHasFixedSize(true);
        recyclerAdapterCurrencyCode = new SendMoneyCurrencyCodeRecyclerAdapter(context, new ArrayList<CountryData.CurrencyData>(), this);
        recyclerViewCurrencyCode.setAdapter(recyclerAdapterCurrencyCode);
        recyclerViewCurrencyCode.addOnScrollListener(new CenterScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.currency_converter_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Rate Calculator");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        gson = new Gson();
        init();
        hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
            findViewById(app.alansari.R.id.send_now_btn).setVisibility(View.GONE);
        }
        setInitialData();

    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_CE_CURRENCY_DATA:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CountryData.CurrencyData> currencyList = (ArrayList<CountryData.CurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CountryData.CurrencyData>>() {
                                }.getType());
                                if (Validation.isValidList(currencyList)) {
                                    setCurrencyList(currencyList);
                                    return;
                                } else {
                                    onAPIError();
                                }
                            } else {
                                onAPIError();
                            }
                        } else {
                            if (response.getString(Constants.STATUS_CODE).equals("812") || response.getString(Constants.STATUS_CODE).equals("813")) {
                                final Dialog cashPickUpDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                                cashPickUpDialog.setCanceledOnTouchOutside(false);
                                cashPickUpDialog.setContentView(R.layout.app_version_dialog);
                                ((TextView) cashPickUpDialog.findViewById(app.alansari.R.id.dialog_title)).setText(response.getString(Constants.MESSAGE));
                                ((Button) cashPickUpDialog.findViewById(app.alansari.R.id.update_btn)).setText("OK");
                                cashPickUpDialog.findViewById(app.alansari.R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashPickUpDialog.dismiss();
                                    }
                                });
                                cashPickUpDialog.show();

                                etGet.setEnabled(false);
                                etSend.setEnabled(false);
                                cardView.setAlpha(.2f);
                                onErrorInCalculation();
                                return;
                            } else {
                                onAPIError();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAPIError();
                    }
                } else {
                    onAPIError();
                }
                break;
            case CALCULATE_CURRENCY_CALCULATOR:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            LogUtils.d("ok", "Amount   " + response.getString(Constants.AMOUNT));
                            LogUtils.d("ok", "Rate   " + response.getString(Constants.RATE));
                            if (Validation.isValidRate(response.getString(Constants.AMOUNT)) && Validation.isValidRate(response.getString(Constants.RATE))) {
                                Double amountDouble = Double.valueOf(response.getString(Constants.AMOUNT));
                                Double rateDouble = Double.valueOf(response.getString(Constants.RATE));
                                Double getRateDouble = Double.valueOf(response.getString(Constants.GET_RATE));
//                                setRateData(String.valueOf(rateDouble), String.valueOf(getRateDouble));
                                setRateData(String.valueOf(response.getString(Constants.RATE)), response.getString(Constants.GET_RATE));
                                rate = String.valueOf(response.getString(Constants.RATE));
                                setAmountData(CommonUtils.getDecimalFormattedString(amountDouble));
                                onSuccessInCalculation();
                                onActivePaymentMode();
                            } else {
                                onAPIError();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            // Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
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
        }
    }

    private void onActivePaymentMode() {
        layoutpayment.setVisibility(View.VISIBLE);

    }

    private void setInitialData() {
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        if (selectedCountry != null && !selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL"))
            setCountryData();
        else
            openCountryList();

        setTransferTypeTabLayout();
    }
    private void setRateData(String rate, String getRate) {
        if (selectedCountry != null && selectedCurrencyData != null) {
            //TODO Use String
            tvRate.setVisibility(View.VISIBLE);
            tvRateOther.setVisibility(View.VISIBLE);
            try {
                tvRate.setText("Exchange Rate\n1 AED = " + getRate + " " + selectedCurrencyData.getName());
                tvRateOther.setText("Exchange Rate\n1 " + selectedCurrencyData.getName() + " = " + rate + " AED");
            } catch (Exception ex) {
                tvRate.setVisibility(View.GONE);
                tvRateOther.setVisibility(View.GONE);
            }
        } else {
            tvRate.setVisibility(View.GONE);
            tvRateOther.setVisibility(View.GONE);
        }
    }

    private void setTransferTypeTabLayout() {
        tabLayoutTransferType.addTab(tabLayoutTransferType.newTab());
        tabLayoutTransferType.addTab(tabLayoutTransferType.newTab());
        LinearLayout newTab = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.send_money_transfer_type_tab_view, null);
        ((AppCompatTextView) newTab.findViewById(app.alansari.R.id.tab_text)).setText("Bank Transfer");
        ((AppCompatImageView) newTab.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
        tabLayoutTransferType.getTabAt(0).setCustomView(newTab);
        LinearLayout newTab2 = (LinearLayout) LayoutInflater.from(this).inflate(app.alansari.R.layout.send_money_transfer_type_tab_view, null);
        ((AppCompatTextView) newTab2.findViewById(app.alansari.R.id.tab_text)).setText("Cash Pick-up");
        ((AppCompatImageView) newTab2.findViewById(app.alansari.R.id.tab_icon)).setImageResource(app.alansari.R.drawable.ic_nav_money);
        ((AppCompatImageView) newTab2.findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
        tabLayoutTransferType.getTabAt(1).setCustomView(newTab2);

        CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) newTab2.findViewById(app.alansari.R.id.tab_text));
        selectedTransferType = getTransferType();

        tabLayoutTransferType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", (AppCompatTextView) tab.getCustomView().findViewById(app.alansari.R.id.tab_text));
                ((AppCompatImageView) tab.getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.colorBlack));
                CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) tabLayoutTransferType.getTabAt(tab.getPosition() == 0 ? 1 : 0).getCustomView().findViewById(app.alansari.R.id.tab_text));
                ((AppCompatImageView) tabLayoutTransferType.getTabAt(tab.getPosition() == 0 ? 1 : 0).getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                selectedTransferType = getTransferType();
                resetAmount();
                if (tab.getPosition() == 1) {
                    fetchCeCurrencyData();
                } else {
                    selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
                    setCountryData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public String getTransferType() {
        return tabLayoutTransferType.getSelectedTabPosition() == 0 ? "BT" : "CP";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.flag:
            case app.alansari.R.id.click_to_change:
            case app.alansari.R.id.country_name:
                openCountryList();
                break;
            case app.alansari.R.id.send_now_btn:
                if (CommonUtils.isLoggedIn() && CommonUtils.getUserId() != null && CommonUtils.getUserMobile() != null && CommonUtils.getPIN() != null) {
                    if (!(context instanceof BeneficiaryActivity)) {
                        Intent intent = new Intent(context, BeneficiaryActivity.class);
                        intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.login_to_use), Toast.LENGTH_SHORT).show();
                }
                break;
            case app.alansari.R.id.payment_type_text:
            case app.alansari.R.id.payment_type_card_view:
                if (hideMenu) {
                    if (selectedTransferType.equalsIgnoreCase("BT")) {
                        serviceType = "AREX";
                    } else if (selectedTransferType.equalsIgnoreCase("CP")) {
                        serviceType = "CE";

                    }
                    if (currentDirection == 1)
                        ccyCode = selectedCurrencyData.getCurrencyCode();
                    else
                        ccyCode = selectedCurrencyData.getCurrencyCode();

                    Intent intentData = new Intent(context, PaymentSelectModeActivity.class);
                    intentData.putExtra(Constants.SCREEN_TYPE, "CURRENCY_CALC_PRE_LOGIN");
                    intentData.putExtra(Constants.SERVICE_TYPE, serviceType);
                    intentData.putExtra(Constants.RATE, rate);
                    intentData.putExtra(Constants.TRANSFER_TYPE, selectedTransferType);
                    intentData.putExtra(Constants.AED_AMOUNT, etSend.getText().toString().trim());
                    intentData.putExtra(Constants.FCY_AMOUNT, etGet.getText().toString().trim());
                    intentData.putExtra(Constants.COUNTRY_CODE, currencyCode);
                    intentData.putExtra(Constants.CCY_CODE, ccyCode);
                    intentData.putExtra(Constants.PRE_LOGIN, Constants.PRE_LOGIN);
                    intentData.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CALCULATOR);
                    startActivityForResult(intentData, Constants.PAYMENT_SELECTION_MODE);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }else{
                    if (selectedTransferType.equalsIgnoreCase("BT")) {
                        serviceType = "AREX";
                    } else if (selectedTransferType.equalsIgnoreCase("CP")) {
                        serviceType = "CE";

                    }
                    if (currentDirection == 1)
                        ccyCode = selectedCurrencyData.getCurrencyCode();
                    else
                        ccyCode = selectedCurrencyData.getCurrencyCode();



                    /*Intent intentData = new Intent(context, PaymentSelectModeActivity.class);
                    intentData.putExtra(Constants.SCREEN_TYPE, "CURRENCY_CALC_PRE_LOGIN");
                    intentData.putExtra(Constants.SERVICE_TYPE, serviceType);
                    intentData.putExtra(Constants.RATE, rate);
                    intentData.putExtra(Constants.TRANSFER_TYPE, selectedTransferType);
                    intentData.putExtra(Constants.AED_AMOUNT, etSend.getText().toString().trim());
                    intentData.putExtra(Constants.FCY_AMOUNT, etGet.getText().toString().trim());
                    intentData.putExtra(Constants.COUNTRY_CODE, currencyCode);
                    intentData.putExtra(Constants.CCY_CODE, ccyCode);
                    intentData.putExtra(Constants.PRE_LOGIN, Constants.PRE_LOGIN);
                    intentData.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CALCULATOR);
                    startActivityForResult(intentData, Constants.PAYMENT_SELECTION_MODE);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);









                    String transferType="";


                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                        if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    }*/




                    Intent intentData = new Intent(context, PaymentSelectModeActivity.class);
                    intentData.putExtra(Constants.AED_AMOUNT, etSend.getText().toString().trim());
                    intentData.putExtra(Constants.FCY_AMOUNT, etGet.getText().toString().trim());
                    intentData.putExtra(Constants.SCREEN_TYPE, "CURRENCY_CALC_POSTLOGIN");
                    intentData.putExtra(Constants.RATE,rate);
                    intentData.putExtra(Constants.SERVICE_TYPE, serviceType);
                    intentData.putExtra(Constants.COUNTRY_CODE, currencyCode);
                    intentData.putExtra(Constants.CCY_CODE, ccyCode);
                    intentData.putExtra(Constants.POST_LOGIN, Constants.POST_LOGIN);
                    intentData.putExtra(Constants.TRANSFER_TYPE, selectedTransferType);
                    intentData.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CALCULATOR_POST_LOGIN);
                    startActivityForResult(intentData, Constants.PAYMENT_SELECTION_MODE);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);






                }

                break;
            case app.alansari.R.id.send_btn:
                if (hideMenu) {
                    Intent intent = null;
                    SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                    if (CommonUtils.isLoggedIn() && CommonUtils.getUserId() != null && CommonUtils.getUserMobile() != null && CommonUtils.getPIN() != null) {
                        intent = new Intent(context, LoginActivity.class);
                    } else {
                        intent = new Intent(context, RegisterActivity.class);
                    }
                    startActivity(intent);
                }else{
                    try {
                        startActivity(new Intent(context, BeneficiaryActivity.class).putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
        intent.putExtra("hideFirstItem", true);
        startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    SharedPreferenceManger.saveSelectedCountryData(new Gson(), selectedCountry);
                    setCountryData();
                    resetScreen();
                    if (tabLayoutTransferType.getSelectedTabPosition() == 1) {
                        fetchCeCurrencyData();
                    }
                } else {
                    if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.PAYMENT_SELECTION_MODE && resultCode == Activity.RESULT_OK) {
                if (hideMenu) {
                   // btnSend.setText("Sign up\n (or) Login");
                    setSendBtnState(true);
                    layoutBottom.setVisibility(View.VISIBLE);
                    tvPaymentType.setText(data.getStringExtra(Constants.NAME));
                    modeDescription = data.getStringExtra(Constants.DESCRIPTION);
                    name = data.getStringExtra(Constants.NAME_TYPE);
                    id = data.getStringExtra(Constants.ID);
                    jsonObjectRequestPayment = new JSONObject(data.getStringExtra(Constants.OBJECT));
                    setVat(jsonObjectRequestPayment);
                }else {
                    btnSend.setText("NEXT");
                    setSendBtnState(true);
                    layoutBottom.setVisibility(View.VISIBLE);
                    tvPaymentType.setText(data.getStringExtra(Constants.NAME));
                    modeDescription = data.getStringExtra(Constants.DESCRIPTION);
                    name = data.getStringExtra(Constants.NAME_TYPE);
                    id = data.getStringExtra(Constants.ID);
                    jsonObjectRequestPayment = new JSONObject(data.getStringExtra(Constants.OBJECT));
                    setVat(jsonObjectRequestPayment);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCountryData() {
        cancelPendingRequests();
        if (selectedCountry != null) {
            selectedCurrencyFrom = CommonUtils.getDefaultCurrencyData(selectedCountry);
            if (selectedCurrencyFrom != null) {
                tvSendCode.setText("AED");
            } else {
                openCountryList();
                return;
            }
            resetAmount();
            if (selectedCountry.getCurrencyData() != null) {
                CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
                tvCountryName.setText("(" + selectedCountry.getLatinName() + ")");
                recyclerAdapterCurrencyCode.addArrayList(selectedCountry.getCurrencyData());
                setDefaultCurrency();
            } else {
                setFragmentCoverVisibility(View.VISIBLE);
                recyclerAdapterCurrencyCode.clear();
                Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
            }
        } else {
            openCountryList();
        }
    }

    private void fetchCeCurrencyData() {

        if (hideMenu) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCeCurrencyData("", selectedCountry.getCountryCodeCE(), CommonUtils.getMemPkId(selectedTransferType), selectedTransferType, "RATE_CALC", userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CE_CURRENCY_DATA_URL_PRE, FETCH_CE_CURRENCY_DATA, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_CE_CURRENCY_DATA.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CE_CURRENCY_DATA.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }

        } else {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCeCurrencyData("", selectedCountry.getCountryCodeCE(), CommonUtils.getMemPkId(selectedTransferType), selectedTransferType, "RATE_CALC", userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CE_CURRENCY_DATA_URL, FETCH_CE_CURRENCY_DATA, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_CE_CURRENCY_DATA.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CE_CURRENCY_DATA.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cancelPendingRequests() {
        AppController.getInstance().cancelPendingRequests(CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CALCULATOR.toString());
        onErrorInCalculation();
    }


    private void resetAmount() {
        etSend.setText(null);
        etGet.setText(null);
        tvRate.setText(null);
        tvRateOther.setText(null);
        etGet.setEnabled(true);
        etSend.setEnabled(true);
        cardView.setAlpha(1f);
        tvPaymentType.setText(null);
        layoutpayment.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);

    }

    private void setDefaultCurrency() {
        selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(0) : null;
        for (int i = 0; i < recyclerAdapterCurrencyCode.getItemCount(); i++) {
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                layoutManagerCurrencyCode.scrollToPosition(i);
                final int finalI = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewCurrencyCode.scrollToPosition(finalI);
                    }
                }, 200);

                break;
            }
        }
    }

    public void setFragmentCoverVisibility(int visibility) {
        amountFragmentLayoutCover.setVisibility(visibility);
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        setTransparentCoverVisibility(View.GONE);
    }

    public void setTransparentCoverVisibility(int visibility) {
        findViewById(app.alansari.R.id.transparent_view).setVisibility(visibility);
    }

    private void calculateCurrency() {
        try {

            /*if (tabLayoutTransferType.getSelectedTabPosition() == 1) {
                final Dialog cashPickUpDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                cashPickUpDialog.setCanceledOnTouchOutside(false);
                cashPickUpDialog.setContentView(R.layout.app_version_dialog);
                ((TextView) cashPickUpDialog.findViewById(app.alansari.R.id.dialog_title)).setText("For cash pickup, check our rates in Send money");
                ((Button) cashPickUpDialog.findViewById(app.alansari.R.id.update_btn)).setText("OK");
                cashPickUpDialog.findViewById(app.alansari.R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cashPickUpDialog.dismiss();
                    }
                });
                cashPickUpDialog.show();

                etGet.setEnabled(false);
                etSend.setEnabled(false);
                cardView.setAlpha(.2f);
                return;
            } else {
                etGet.setEnabled(true);
                etSend.setEnabled(true);
                cardView.setAlpha(1f);
            }*/
            resetAll();
            if (tabLayoutTransferType.getSelectedTabPosition() == 0) {
                currencyCode = selectedCountry.getCountryCodeAREX();
            } else {
                currencyCode = selectedCountry.getCountryCodeCE();
            }

            if ((currentDirection == 1 && Validation.isValidEditTextValue(etSend)) || (currentDirection == 2 && Validation.isValidEditTextValue(etGet))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    JSONObject requestParams;
                    JsonObjectRequest jsonObjReq;
                    String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

                    if (currentDirection == 1)
                        requestParams = new APIRequestParams().calculateCurrencyCalculator("91", selectedCurrencyData.getCurrencyCode(), CommonUtils.getTextFromEditText(etSend), selectedTransferType, currencyCode, userPkId, LogoutCalling.getDeviceID(context), sessionTime);
                    else
                        requestParams = new APIRequestParams().calculateCurrencyCalculator(selectedCurrencyData.getCurrencyCode(), "91", CommonUtils.getTextFromEditText(etGet), selectedTransferType, currencyCode, userPkId, LogoutCalling.getDeviceID(context), sessionTime);


                    if (hideMenu)
                        jsonObjReq = new CallAddr().executeApi(requestParams, Constants.CALCULATE_CURRENCY_CALCULATOR_URL_PRE, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CALCULATOR, Request.Method.PUT, this);
                    else
                        jsonObjReq = new CallAddr().executeApi(requestParams, Constants.CALCULATE_CURRENCY_CALCULATOR_URL, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CALCULATOR, Request.Method.PUT, this);


                    cancelPendingRequests();
                    isCalculatingCurrency = true;
                    setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    ivSuccess.setVisibility(View.GONE);
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CALCULATOR.toString());
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

    private void onSuccessInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.VISIBLE);
        setTransparentCoverVisibility(View.GONE);
    }

    private void setAmountData(String amount) {
        if (currentDirection == 1) {
            etGet.setText(amount);
        } else {
            etSend.setText(amount);
        }
    }

    private void setCurrencyList(ArrayList<CountryData.CurrencyData> currencyList) {
        LogUtils.d("ok", "Set CE Currency " + currencyList.size());
        recyclerAdapterCurrencyCode.addArrayList(currencyList);
        setDefaultCurrency();
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

    private void resetAll() {
        tvPaymentType.setText("");
        tvPaymentType.setText(null);
        setSendBtnState(false);
        layoutBottom.setVisibility(View.GONE);


    }

    public void onSuccessInCalculation(String amountToPay, String exchangeRate) {
        tvTotalToPay.setText(amountToPay);
        tvTotalToPayCurrencyCode.setText("AED");
        tvFeeAED.setText("Fee : AED " + exchangeRate);
    }

    public void hideVat() {
        tvVat.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
        tvRoundOff.setVisibility(View.GONE);

        tvVat.setText("");
        tvDiscount.setText("");
        tvRoundOff.setText("");
    }

    public void setVat(JSONObject response) {
        try {
            String vatLable = response.getString(Constants.VAT_CHARGES_CODE);
            String roundingLable = response.getString(Constants.VAT_ROUNDINGOFF_CODE);
            String discountLable = response.getString(Constants.VAT_DISCOUNT_CODE);
            charge = response.getString(Constants.CHARGES_ON_US);
            totalToPay = response.getString(Constants.TOTAL_AMOUNT);
            onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);

            vat = response.getString(Constants.VAT_CHARGES);
            rounding = response.getString(Constants.VAT_ROUNDINGOFF);
            discount = response.getString(Constants.VAT_DISCOUNT);
            TOTAL_AMOUNT_PP = response.getString(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = response.getString(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);

            if (vat != null && (!vat.equalsIgnoreCase("0.0") && !vat.equalsIgnoreCase("0.00"))) {
                vat = CommonUtils.getDecimalFormattedString(Double.parseDouble(vat));
                tvVat.setText(vatLable + " : AED " + vat);
                tvVat.setVisibility(View.VISIBLE);
            } else {
                tvVat.setText(null);
                tvVat.setVisibility(View.GONE);
            }
            if (discount != null && (!discount.equalsIgnoreCase("0.0") && !discount.equalsIgnoreCase("0.00"))) {
                discount = CommonUtils.getDecimalFormattedString(Double.parseDouble(discount));
                tvDiscount.setText(discountLable + " : AED " + discount);
                tvDiscount.setVisibility(View.VISIBLE);
            } else {
                tvDiscount.setText(null);
                tvDiscount.setVisibility(View.GONE);
            }
            if (rounding != null && (!rounding.equalsIgnoreCase("0.0") && !rounding.equalsIgnoreCase("0.00"))) {
                rounding = CommonUtils.getDecimalFormattedString(Double.parseDouble(rounding));
                tvRoundOff.setText(roundingLable + " : AED " + rounding);
                tvRoundOff.setVisibility(View.VISIBLE);
            } else {
                tvRoundOff.setText(null);
                tvRoundOff.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setSendBtnState(boolean state) {
        btnSend.setEnabled(state);
    }

    public void resetScreen(){
        tvPaymentType.setText("");
        layoutpayment.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        resetAll();
        hideVat();
        ivSuccess.setVisibility(View.GONE);
        hideVat();
    }

}
