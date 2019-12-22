package app.alansari.modules.sendmoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BankData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.models.WuCurrencyData;
import app.alansari.models.WuRateChargesResponse;
import app.alansari.modules.accountmanagement.UpdateExistingBeneficiaryActivityWu;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.modules.sendmoney.fragments.WUSendMoneyFragment;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_SERVICE_TYPES;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class WUSendMoneyActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private Intent intent;
    private Context context;
    private Gson gson;
    private WUBeneficiaryData dataObject;
    private String wuCardNo;
    private String arexUserId;
    private BankData selectedBank;
    private CountryData selectedCountry;
    private String vat;
    private String rounding;
    private String discount;
    private String netCharge;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String BENEF_PK_ID;
    private JSONObject sendMoneyResponse;

    private TextView tvWuNumber, tvCountryName, tvTotalToPay, tvTotalToPayCurrencyCode, tvFeeAED, tvVat, tvDiscount, tvRoundOff, tvDisclaimer;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private Button btnSend;
    private AppCompatImageView ivFlag;
    private EditText etPromoCode;
    private AutoCompleteTextView tvServiceType;
    private ArrayList<String> serviceTypeUpdated;
    private WuRateChargesResponse wuRateChargesResponse;
    private String TOTAL_WU_POINTS = "0";
    private String benefType = "";
    private String payoutStateCity = "";
    private String serviceType = "";
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
    protected void onStart() {
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.wu_send_money_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Send");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Money");

        //TODO: Remove Dummy Lines
        findViewById(app.alansari.R.id.toolbar_title_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, AdditionalInfoActivityWu.class);
                intent.putExtra(Constants.OBJECT, SharedPreferenceManger.getSendMoneyDataDummy());
                startActivity(intent);

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        gson = new Gson();
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        init();

        if (getIntent().getExtras() != null) {
            wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
            TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
            benefType = getIntent().getStringExtra(Constants.BENEF_TYPE);
            tvWuNumber.setText(wuCardNo);
            if (benefType.equalsIgnoreCase("EXIST")) {
                dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
                selectedCountry = new CountryData();
                selectedCountry.setArabicName(dataObject.getReceiverCountryName());
                selectedCountry.setLatinName(dataObject.getReceiverCountryName());
                selectedCountry.setCcyAREX(dataObject.getArexCurrencyCode());
                selectedCountry.setCountryCodeAREX(dataObject.getArexCountryCode());
                selectedCountry.setWuCountryCode(dataObject.getReceiverCountryCode());

                if(!getIntent().getStringExtra(Constants.WU_LOOKUP_PROMO_CODE).equalsIgnoreCase("")){
                    etPromoCode.setText(getIntent().getStringExtra(Constants.WU_LOOKUP_PROMO_CODE));
                    etPromoCode.setEnabled(false);
                }else{
                    etPromoCode.setEnabled(true);
                }



                ArrayList<CountryData.CurrencyData> currencyData = new ArrayList<CountryData.CurrencyData>();
                CountryData.CurrencyData data = new CountryData.CurrencyData();
                data.setCurrencyCode(dataObject.getArexCurrencyCode());
                data.setName(dataObject.getReceiverCurrencyCode());
                currencyData.add(data);
                selectedCountry.setCurrencyData(currencyData);
                setInitialData();
                fetchWuBeneficiaryDetails((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "", dataObject.getReceiverIndexNumber());
                updateServiceType();
            } else {
                dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
                BENEF_PK_ID = getIntent().getStringExtra(Constants.BENEF_PK_ID);
                selectedCountry = getIntent().getParcelableExtra(Constants.WU_SELECTED_COUNTRY);
                dataObject.setArexCountryCode(selectedCountry.getCountryCodeAREX());
                dataObject.setReceiverCountryCode(selectedCountry.getWuCountryCode());
                dataObject.setArexCurrencyCode(selectedCountry.getCurrencyData().get(0).getCurrencyCode());
                dataObject.setReceiverCurrencyCode(selectedCountry.getCurrencyData().get(0).getName());
                dataObject.setReceiverNameType(getIntent().getStringExtra(Constants.NAME_TYPE));
                tvServiceType.setText(getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE_NAME));
                serviceType = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
                tvServiceType.setOnClickListener(null);
                setInitialData();
            }
        } else {
            onBackPressed();
        }

    }

    private void init() {
        etPromoCode = (EditText) findViewById(app.alansari.R.id.et_wu_promo_code);
        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        tvWuNumber = (TextView) findViewById(app.alansari.R.id.tv_wu_number);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name);
        btnSend = (Button) findViewById(app.alansari.R.id.send_btn);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        tvFeeAED = (TextView) findViewById(app.alansari.R.id.fee_aed);
        tvVat = (TextView) findViewById(app.alansari.R.id.vat_label);
        tvDiscount = (TextView) findViewById(app.alansari.R.id.discount_label);
        tvRoundOff = (TextView) findViewById(app.alansari.R.id.roundoff_label);
        tvServiceType = (AutoCompleteTextView) findViewById(app.alansari.R.id.service_type_text);

        viewPager = (ViewPager) findViewById(app.alansari.R.id.pager);
        btnSend.setOnClickListener(this);
        ivFlag.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);
        tvServiceType.setOnClickListener(this);
        tvVat.setVisibility(View.GONE);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.tv_country_info).setOnClickListener(this);

        etPromoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetAmount();
                ((TextView) findViewById(R.id.promo_code_message)).setText("");
                tvVat.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    private void setInitialData() {
        setCountryData();
        tvCountryName.setText("Receiving country (" + selectedCountry.getLatinName() + ")");
        ArrayList<ServiceTypeData> finalServiceTypeList = new ArrayList<>();
        if (adapter == null) {
            adapter = new FragmentPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
        } else {
            viewPager.setAdapter(adapter);
        }
    }

    private void fetchWuBeneficiaryDetails(String userId, String arexUserId, String wuCardNumber, String receiverIndexNumber) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuBeneficiaryDetails(userId, arexUserId, wuCardNumber, receiverIndexNumber, LogoutCalling.getDeviceID(context), sessionTime), Constants.WU_FETCT_BENEFICIARY_DETAILS_URL, CommonUtils.SERVICE_TYPE.WU_FETCH_BENEFICIARY_DETAILS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.WU_FETCH_BENEFICIARY_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.WU_FETCH_BENEFICIARY_DETAILS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.WU_FETCH_BENEFICIARY_DETAILS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void updateServiceType() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuServices(dataObject.getArexCountryCode(), ((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING)), LogoutCalling.getDeviceID(context), sessionTime), Constants.WU_FETCH_SERVICE_TYPES_URL, WU_FETCH_SERVICE_TYPES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SERVICE_TYPES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SERVICE_TYPES.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void resetAmount() {
        if (adapter != null && adapter.getCount() > 0)
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getFragment(i) instanceof WUSendMoneyFragment && (WUSendMoneyFragment) adapter.getFragment(i) != null) {
                    ((WUSendMoneyFragment) adapter.getFragment(i)).resetFromActivity();
                }
            }
        btnSend.setEnabled(false);
    }

    private void setCountryData() {
        boolean isFirstTime = false;
        if (selectedCountry != null) {
            CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
            tvCountryName.setText("Receiving country (" + selectedCountry.getLatinName() + ")");
            if (!isFirstTime) {
                resetAmount();
            }
        }
    }    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.tv_country_info:
                showCountryInfo();
                break;
            case app.alansari.R.id.send_btn:
                checkTxtLimit();
                break;
            case app.alansari.R.id.flag:
            case app.alansari.R.id.country_name:
//                openWuCountryList();
                break;
            case app.alansari.R.id.service_type_text:
            case app.alansari.R.id.service_type_card_view:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.DEFAULT_HEADER, "1");
                intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD);
                intent.putExtra(Constants.ID, Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                intent.putExtra(Constants.COUNTRY_CODE, dataObject.getArexCountryCode());
                startActivityForResult(intent, Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case WU_COUNTRY_INFO:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            try {
                                JSONArray result = response.getJSONArray(Constants.RESULT);
                                String data = "";
                                JSONObject dataObject = result.getJSONObject(0);
                                JSONArray countryInfo = dataObject.getJSONArray("countryInfo");
                                for (int i = 0; i < countryInfo.length(); i++) {
                                    if (countryInfo.getJSONObject(i).getString("displayValue") == null || countryInfo.getJSONObject(i).getString("displayValue").equalsIgnoreCase("null")) {
                                        data = data + "\n" + "--";
                                    } else {
                                        data = data + "\n" + countryInfo.getJSONObject(i).getString("displayValue");
                                    }
                                }
                                final Dialog messageDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBgCountry);
                                messageDialog.setCanceledOnTouchOutside(false);
                                messageDialog.setContentView(R.layout.dialog_country_info);
                                ((TextView) messageDialog.findViewById(app.alansari.R.id.dialog_title)).setText(data);
                                messageDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        messageDialog.dismiss();
                                    }
                                });
                                messageDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case CHECK_TXN_LIMITS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            startAdditionalInfoScreen();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.MISSING)) {
                            if (response.get(Constants.RESULT) != null && !response.getString(Constants.RESULT).equalsIgnoreCase("null") && Validation.isValidJsonArray(response.getJSONArray(Constants.RESULT))) {
                                onBeneficiaryMissingFields(response.getJSONArray(Constants.RESULT), response.getString(Constants.ID), response.getString(Constants.MESSAGE));
                                //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context, "Unable to proceed: " + response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case WU_FETCH_BENEFICIARY_DETAILS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (Validation.isValidJsonArray(response.getJSONArray(Constants.RESULT))) {
                                ArrayList<WUBeneficiaryData> beneficiaryData = (ArrayList<WUBeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WUBeneficiaryData>>() {
                                }.getType());
                                dataObject.setAdditionalInfoList(beneficiaryData.get(0).getAdditionalInfoList());
                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    if (beneficiaryData.get(0).getCountryDetails() != null) {
                                        selectedCountry.setFlag(beneficiaryData.get(0).getCountryDetails().getFLAG());
                                        selectedCountry.setLatinName(beneficiaryData.get(0).getCountryDetails().getLATINNAME());
                                        setInitialData();
                                        return;
                                    }
                                }
                            }
                            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            if (response.getInt(Constants.STATUS_CODE) == 1402 || response.getInt(Constants.STATUS_CODE) == 1403) {
                                final Dialog messageDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                                messageDialog.setCanceledOnTouchOutside(false);
                                messageDialog.setContentView(R.layout.adds_dialog);
                                ((TextView) messageDialog.findViewById(app.alansari.R.id.dialog_title)).setText((response.getString(Constants.MESSAGE)));
                                messageDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        messageDialog.dismiss();
                                        onBackPressed();
                                    }
                                });
                                messageDialog.show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
            case WU_FETCH_SERVICE_TYPES:
                try {
                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                        }.getType());
                        if (currencyData != null && currencyData.size() > 0) {
                            if (currencyData.size() == 1) {
                                tvServiceType.setText(currencyData.get(0).getDisplayValue());
                                serviceType = currencyData.get(0).getDisplayKey();
                            } else {
                                tvServiceType.performClick();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void startAdditionalInfoScreen() {
        intent = new Intent(context, AdditionalInfoActivityWu.class);
        dataObject.setVat(vat);
        dataObject.setBankData(selectedBank);
        intent.putExtra(Constants.AREX_COUNTRY_CODE, selectedCountry.getCountryCodeAREX());
        intent.putExtra(Constants.WU_COUNTRY_CODE, dataObject.getReceiverCountryCode());
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.WU_RATE_CHARGE_RESPONSE, sendMoneyResponse.toString());
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
        intent.putExtra(Constants.WU_CARD_NO, wuCardNo);
        intent.putExtra(Constants.wuRateChargesResponse, wuRateChargesResponse);
        intent.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
        intent.putExtra(Constants.BENEF_PK_ID, BENEF_PK_ID);
        intent.putExtra(Constants.BENEF_TYPE, benefType);
        intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE, serviceType);
        intent.putExtra(Constants.WU_PROMO_CODE, etPromoCode.getText().toString());
        startActivity(intent);
    }

    private void onBeneficiaryMissingFields(JSONArray jsonArray, String fieldId, String message) {
        LogUtils.d("ok", "On Beneficiary Fields Missing");
        intent = new Intent(context, UpdateExistingBeneficiaryActivityWu.class);
        intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<BeneficiaryDynamicFields>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeneficiaryDynamicFields>>() {
        }.getType()));
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.ID, fieldId);
        intent.putExtra(Constants.MESSAGE, message);
        startActivityForResult(intent, Constants.UPDATE_MISSING_FIELD_BENEFICIARY);
    }

    public void setTransparentCoverVisibility(int visibility) {
        findViewById(app.alansari.R.id.transparent_view).setVisibility(visibility);
    }

    public void onSuccessInCalculation(String amountToPay, String exchangeRate) {
        tvTotalToPay.setText(amountToPay);
        tvTotalToPayCurrencyCode.setText("AED");
        tvFeeAED.setText("Fee AED " + exchangeRate);
    }

    public void setVat(JSONObject response) {
        try {
            sendMoneyResponse = response;
//            String vatLabel = response.getString(Constants.VAT_CHARGES_STR);
            String promoMessage = "";

            if (response.getString(Constants.PROMO_REQ_MSG) != null && response.getString(Constants.PROMO_REQ_MSG).length() > 0) {
                promoMessage = response.getString(Constants.PROMO_REQ_MSG);
                ((TextView) findViewById(R.id.promo_code_message)).setText("promo desc : " + promoMessage);
            } else {
                ((TextView) findViewById(R.id.promo_code_message)).setText("");
            }

            String vatLabel = "VAT CHARGES";

            vat = response.getString(Constants.VAT_CHARGES);
            discount = response.getString(Constants.PROMO_DISCOUNT_AMOUNT);
            netCharge = response.getString(Constants.CHARGES);

            if (vat != "null" && vat != null && (!vat.equalsIgnoreCase("0.0") && !vat.equalsIgnoreCase("0.00"))) {
                vat = CommonUtils.getDecimalFormattedString(Double.parseDouble(vat));
                tvVat.setText(vatLabel + " : AED " + vat);
                tvVat.setVisibility(View.VISIBLE);
            } else {
                tvVat.setText(null);
                tvVat.setVisibility(View.GONE);
            }
            if (discount != null && discount != "null" && discount.length() > 0 && (!discount.equalsIgnoreCase("0.0") && !discount.equalsIgnoreCase("0.00"))) {
                discount = CommonUtils.getDecimalFormattedString(Double.parseDouble(discount));
                tvDiscount.setText("Discount : AED " + discount);
                tvDiscount.setVisibility(View.VISIBLE);
            } else {
                tvDiscount.setText(null);
                tvDiscount.setVisibility(View.GONE);
            }
            if (rounding != null && (!rounding.equalsIgnoreCase("0.0") && !rounding.equalsIgnoreCase("0.00"))) {
                rounding = CommonUtils.getDecimalFormattedString(Double.parseDouble(rounding));
                tvRoundOff.setText("Round : AED " + rounding);
                tvRoundOff.setVisibility(View.VISIBLE);
            } else {
                tvRoundOff.setText(null);
                tvRoundOff.setVisibility(View.GONE);
            }


            if (promoMessage.length() > 0) {
                tvFeeAED.setText("Discount AED : " + discount);
                tvVat.setText("Net charges after discount AED " + netCharge);
                tvVat.setVisibility(View.VISIBLE);
                if (vat == null || vat.equalsIgnoreCase("null")) {
                    tvDiscount.setText("VAT charge : AED 0");
                } else {
                    tvDiscount.setText("VAT charge : AED " + vat);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setSendBtnState(boolean state) {
        btnSend.setEnabled(state);
    }    private void showCountryInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCountryInfo(CommonUtils.getUserId(), arexUserId, dataObject.getReceiverCountryCode(), dataObject.getReceiverCurrencyCode(), "WU", LogoutCalling.getDeviceID(context), sessionTime), Constants.WU_COUNTRY_INFO_URL, CommonUtils.SERVICE_TYPE.WU_COUNTRY_INFO, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.WU_COUNTRY_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.WU_COUNTRY_INFO.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.WU_COUNTRY_INFO.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.UPDATE_MISSING_FIELD_BENEFICIARY && resultCode == Activity.RESULT_OK) {
                if (data != null && data.getParcelableExtra(Constants.OBJECT) != null) {
                    dataObject = data.getParcelableExtra(Constants.OBJECT);
                    LogUtils.v("additionalInfoList", "additionalInfoList : " + dataObject.getAdditionalInfoList().size());
                    dataObject.setTxnAmountData(((WUSendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())).getTxnData());
                    checkTxtLimit();
                }
            } else if (requestCode == Constants.WU_SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    setInitialData();
                } else {
                    if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE && resultCode == Activity.RESULT_OK) {
                WuCurrencyData wuCurrencyData = data.getParcelableExtra(Constants.OBJECT);
                tvServiceType.setText(wuCurrencyData.getDisplayValue());
                serviceType = wuCurrencyData.getDisplayKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openWuCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
//        intent.putExtra("hideFirstItem", true);
        intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_COUNTRY);
        startActivityForResult(intent, Constants.WU_SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }    private void checkTxtLimit() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String totalTxtAmt = "";
            String txtAmt = "";
            String rate = "";
            String totalValueAed = "";
            String charge = "";
            String serviceType = "WU";
            String transferType = "BT";
            String countryCode = dataObject.getArexCurrencyCode() + "#" + dataObject.getReceiverCountryCode();
            try {
                charge = sendMoneyResponse.getString(Constants.CHARGES);
                totalTxtAmt = sendMoneyResponse.getString(Constants.GROSS_TOTAL_AMOUNT);
                txtAmt = sendMoneyResponse.getString(Constants.DEST_PRINCIPAL_AMOUNT);
                rate = sendMoneyResponse.getString(Constants.RATE);
                totalValueAed = sendMoneyResponse.getString(Constants.ORIGINAL_PRINCIPLE_AMOUNT);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JSONObject input = new APIRequestParams().checkTxnLimitsWu(charge, countryCode, arexUserId, rate, serviceType, totalTxtAmt, totalValueAed, transferType, txtAmt, CommonUtils.getUserId(), LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.CHECK_TXN_LIMITS_URL, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPromoCode() {
        return etPromoCode.getText().toString();
    }

    public void setRateChargesParams(WuRateChargesResponse wuRateChargesResponse) {
        this.wuRateChargesResponse = wuRateChargesResponse;
    }

    public String getBenefType() {
        return benefType;
    }

    public void clearCharges() {
        tvVat.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
    }

    public String getServiceType() {
        return serviceType;
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        Bundle bundle;
        Fragment[] fragmentArray;


        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentArray = new Fragment[1];
        }

        public Fragment getFragment(int position) {
            return fragmentArray[position];
        }


        public void setServiceTypeData(ArrayList<ServiceTypeData> serviceTypeData) {
            fragmentArray = new Fragment[serviceTypeData.size()];
        }


        @Override
        public Fragment getItem(int position) {
            fragmentArray[position] = new WUSendMoneyFragment();
            bundle = new Bundle();
            bundle.putParcelable(Constants.OBJECT, dataObject);
            bundle.putParcelable(Constants.COUNTRY_DATA, selectedCountry);
            bundle.putString(Constants.AREX_MEM_PK_ID, arexUserId);
            fragmentArray[position].setArguments(bundle);
            return fragmentArray[position];
        }

        @Override
        public int getCount() {
            return fragmentArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Cash Pick-up";
        }
    }







}
