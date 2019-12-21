package app.alansari.modules.sendmoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.core.app.FragmentStatePagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.BranchData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.modules.accountmanagement.AddBeneficiaryActivity;
import app.alansari.modules.accountmanagement.AddBeneficiaryCeActivity;
import app.alansari.modules.accountmanagement.UpdateExistingBeneficiaryActivity;
import app.alansari.modules.accountmanagement.UpdateExistingBeneficiaryCeActivity;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFieldsCe;
import app.alansari.modules.sendmoney.fragments.SendMoneyFragment;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 24 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class SendMoneyActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    ArrayList<ServiceTypeData> availableServiceTypeDataList;
    private Gson gson;
    private Intent intent;
    private Context context;
    private String selectedTransferType;
    private BankData selectedBank;
    private CountryData selectedCountry;
    private ServiceTypeData selectedServiceType;
    //Views
    private Button btnSend;
    private AppCompatImageView ivFlag;
    private TextView tvCountryName, tvTotalToPay, tvTotalToPayCurrencyCode, tvFeeAED, tvVat, tvDiscount, tvRoundOff, tvDisclaimer;
    private AutoCompleteTextView tvBankName;
    private TabLayout tabLayoutTransferType, tabLayout;
    private View amountFragmentLayoutCover;

    private ViewPager viewPager;
    private BeneficiaryData dataObject;
    private FragmentPagerAdapter adapter;
    private String vat;
    private String rounding;
    private String discount;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String sessionTime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        tvDisclaimer = (TextView) findViewById(app.alansari.R.id.disclaimer);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name);
        tvBankName = (AutoCompleteTextView) findViewById(app.alansari.R.id.bank_name);
        tabLayoutTransferType = (TabLayout) findViewById(app.alansari.R.id.tab_layout_transfer_type);

        amountFragmentLayoutCover = findViewById(app.alansari.R.id.amount_fragment_layout_cover);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);

        tvVat = (TextView) findViewById(app.alansari.R.id.vat_label);
        tvDiscount = (TextView) findViewById(app.alansari.R.id.discount_label);
        tvRoundOff = (TextView) findViewById(app.alansari.R.id.roundoff_label);

        tvFeeAED = (TextView) findViewById(app.alansari.R.id.fee_aed);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        btnSend = (Button) findViewById(app.alansari.R.id.send_btn);

        tabLayout = (TabLayout) findViewById(app.alansari.R.id.tab_layout);
        viewPager = (ViewPager) findViewById(app.alansari.R.id.pager);

        ivFlag.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);
        tvBankName.setOnClickListener(this);
        tvBankName.setEnabled(false);/*disabled as per client requirement*/
        btnSend.setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.send_money_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Send");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Money");

        //TODO: Remove Dummy Lines
        findViewById(app.alansari.R.id.toolbar_title_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, AdditionalInfoActivity.class);
                intent.putExtra(Constants.OBJECT, SharedPreferenceManger.getSendMoneyDataDummy());
                startActivity(intent);

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        gson = new Gson();
        init();

        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            if (dataObject != null) {
                setInitialData();
                return;
            } else {
                String beneficiaryId = getIntent().getExtras().getString(Constants.ID, null);
                if (beneficiaryId != null) {
                    fetchBeneficiaryDetails(beneficiaryId, getIntent().getExtras().getString(Constants.SERVICE_TYPE, Constants.AREX_MAPPING));
                    return;
                }
            }
        }
        onBackPressed();
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

    private void setInitialData() {
        availableServiceTypeDataList = SharedPreferenceManger.loadServiceTypeData();
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        if (dataObject != null && availableServiceTypeDataList != null && availableServiceTypeDataList.size() > 0) {
            if (dataObject.getCountryData() != null) {
                selectedCountry = dataObject.getCountryData();
                setCountryData();
            } else
                openCountryList();
            setTransferTypeTabLayout();
            if (dataObject.getBankData() != null) {
                setBankData();
            } else {
                resetBankData();
            }

            if (dataObject.getServiceTypeData() != null) {
//                setServiceTypeData();
            } else {
                onBackPressed();
            }
        } else {
            fetchServiceTypes();
        }
    }

    void fetchServiceTypes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES.toString(), false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceTypes((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_SERVICE_TYPES_URL, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
        startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private void setCountryData() {
        boolean isFirstTime = false;
        if (selectedCountry == null) {
            selectedCountry = dataObject.getCountryData();
            isFirstTime = true;
        }
        if (selectedCountry != null) {
            CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
            tvCountryName.setText("Receiving country (" + selectedCountry.getLatinName() + ")");
            if (!isFirstTime) {
                resetAmount();
                resetBankData();
                //TODO Update Currency List
            }
        }
    }

    private void setBankData() {
        boolean isFirstTime = false;
        if (selectedBank == null) {
            selectedBank = dataObject.getBankData();
            isFirstTime = true;
        }
        if (selectedBank != null) {
            tvBankName.setText(selectedBank.getBankName());
            setServiceTypeData();
            if (selectedCountry.getCurrencyData() != null)
                setFragmentCoverVisibility(View.GONE);
            if (!isFirstTime) {
                resetAmount();
            }
        }
    }

    private void resetBankData() {
        selectedBank = null;
        tvBankName.setText(null);
        setFragmentCoverVisibility(View.VISIBLE);
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

        if ((dataObject.getTransferType() != null && dataObject.getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) || (dataObject.getAccountNumber() != null && dataObject.getAccountNumber().length() > 0) || (dataObject.getIBANNumber() != null && dataObject.getIBANNumber().length() > 0)) {
            CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) newTab2.findViewById(app.alansari.R.id.tab_text));
            if (dataObject.getBankData() != null && dataObject.getBankData().getServiceBtDesc() != null) {
                tvDisclaimer.setText(dataObject.getBankData().getServiceBtDesc());
            } else {
                tvDisclaimer.setText("");
            }
        } else {
            CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) newTab.findViewById(app.alansari.R.id.tab_text));
            tabLayoutTransferType.getTabAt(1).select();
            if (dataObject.getBankData() != null && dataObject.getBankData().getServiceCpDesc() != null) {
                tvDisclaimer.setText(dataObject.getBankData().getServiceCpDesc());
            } else {
                tvDisclaimer.setText("");
            }
        }
        selectedTransferType = getTransferType();
        dataObject.setTransferType(getTransferType());

        tabLayoutTransferType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", (AppCompatTextView) tab.getCustomView().findViewById(app.alansari.R.id.tab_text));
                ((AppCompatImageView) tab.getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.colorBlack));
                CommonUtils.setLayoutFont(context, "Roboto-Light.ttf", (AppCompatTextView) tabLayoutTransferType.getTabAt(tab.getPosition() == 0 ? 1 : 0).getCustomView().findViewById(app.alansari.R.id.tab_text));
                ((AppCompatImageView) tabLayoutTransferType.getTabAt(tab.getPosition() == 0 ? 1 : 0).getCustomView().findViewById(app.alansari.R.id.tab_icon)).setColorFilter(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                selectedTransferType = getTransferType();
                resetBankData();
                resetAmount();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*tabs selections disabled as per client requirement*/
        tabLayoutTransferType.clearOnTabSelectedListeners();
        LinearLayout tabStrip = ((LinearLayout) tabLayoutTransferType.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    private void setServiceTypeData() {

        ArrayList<ServiceTypeData> finalServiceTypeList = new ArrayList<>();
        finalServiceTypeList.clear();
        try {
            for (int i = 0; i < availableServiceTypeDataList.size(); i++) {
                if (availableServiceTypeDataList.get(i).getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) && !finalServiceTypeList.contains(availableServiceTypeDataList.get(i))) {
                    if (getTransferType() != null && ((getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER) && selectedBank.getAREX_BT().equalsIgnoreCase("1")) || (getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP) && selectedBank.getAREX_CP().equalsIgnoreCase("1")))) {
                        finalServiceTypeList.add(availableServiceTypeDataList.get(i));
                    }
                } else if (availableServiceTypeDataList.get(i).getMapping().equalsIgnoreCase(Constants.CE_MAPPING) && !finalServiceTypeList.contains(availableServiceTypeDataList.get(i))) {
                    if (getTransferType() != null && ((getTransferType() != null && getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER) && selectedBank.getCE_BT().equalsIgnoreCase("1")) || (getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP) && selectedBank.getCE_CP().equalsIgnoreCase("1")))) {
                        finalServiceTypeList.add(availableServiceTypeDataList.get(i));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.d("ok", "getTransferType() " + getTransferType());
            LogUtils.d("ok", "bank " + selectedBank.getAREX_BT());
            LogUtils.d("ok", "bank2 " + selectedBank.getCE_BT());
        }


        if (adapter == null) {
            adapter = new FragmentPagerAdapter(getSupportFragmentManager(), finalServiceTypeList);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            selectedServiceType = dataObject.getServiceTypeData();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getServiceTypeData(i).getId().equalsIgnoreCase(selectedServiceType.getId())) {
                    final int finalI = i;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(finalI);
                        }
                    });
                    break;
                }
            }
        } else {
            adapter.setServiceTypeData(finalServiceTypeList);
            viewPager.setAdapter(adapter);
            selectedServiceType = adapter.getServiceTypeData(viewPager.getCurrentItem());
        }

        if (tabLayout.getTabCount() > 1) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                resetAmount();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayoutTransferType.clearOnTabSelectedListeners();
    }

    private void resetAmount() {
        if (adapter != null && adapter.getCount() > 0)
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getFragment(i) instanceof SendMoneyFragment && (SendMoneyFragment) adapter.getFragment(i) != null) {
                    ((SendMoneyFragment) adapter.getFragment(i)).resetFromActivity();
                }
            }
        btnSend.setEnabled(false);
    }

    public void setSendBtnState(boolean state) {
        btnSend.setEnabled(state);
    }

    public void onSuccessInCalculation(String amountToPay, String exchangeRate) {
        tvTotalToPay.setText(amountToPay);
        tvTotalToPayCurrencyCode.setText("AED");
        tvFeeAED.setText("Fee : AED " + exchangeRate);
    }

    public void setVat(JSONObject response) {
        try {
            String vatLable = response.getString(Constants.VAT_CHARGES_CODE);
            String roundingLable = response.getString(Constants.VAT_ROUNDINGOFF_CODE);
            String discountLable = response.getString(Constants.VAT_DISCOUNT_CODE);

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

    public void hideVat() {
        tvVat.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
        tvRoundOff.setVisibility(View.GONE);

        tvVat.setText("");
        tvDiscount.setText("");
        tvRoundOff.setText("");
    }

    public String getTransferType() {
        return tabLayoutTransferType.getSelectedTabPosition() == 0 ? "BT" : "CP";
    }

    public void setTransparentCoverVisibility(int visibility) {
        findViewById(app.alansari.R.id.transparent_view).setVisibility(visibility);
    }

    public void setFragmentCoverVisibility(int visibility) {
        amountFragmentLayoutCover.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.send_btn:
                selectedServiceType = adapter.getServiceTypeData(viewPager.getCurrentItem());
                if (dataObject != null && adapter != null && viewPager != null) {
                    if (((SendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())) != null && ((SendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())).getTxnData() != null) {
                        dataObject.setTxnAmountData(((SendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())).getTxnData());
                        //TODO:
                        if (isDataChangedManually()) {
                            addNewBeneficiary();
                        } else {
                            dataObject.setCountryData(selectedCountry);
                            dataObject.setBankData(selectedBank);
                            dataObject.setTransferType(getTransferType());
                            dataObject.setServiceTypeData(selectedServiceType);
                            SharedPreferenceManger.setSendMoneyDataDummy(dataObject);
                            validateExistingBeneficiary();
                        }
                    }
                }
                break;
            case app.alansari.R.id.flag:
            case app.alansari.R.id.country_name:
//                openCountryList();
                break;
            case app.alansari.R.id.bank_name:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_BANK);
                intent.putExtra(Constants.COUNTRY_ID, selectedCountry.getId());
                intent.putExtra(Constants.TYPE, getTransferType());
                startActivityForResult(intent, Constants.SELECT_ITEM_BANK);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
        }
    }

    private boolean isNotNull(Object object, String message) {
        if (selectedCountry == null) {
            Toast.makeText(context, getString(app.alansari.R.string.select_valid_bank), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isSelectedDataPrefect() {
        return (isNotNull(selectedCountry, getString(app.alansari.R.string.select_valid_country)) &&
                isNotNull(selectedTransferType, getString(app.alansari.R.string.select_valid_transfer_type))
                && isNotNull(selectedBank, getString(app.alansari.R.string.select_valid_bank))
                && isNotNull(selectedServiceType, getString(app.alansari.R.string.select_valid_service_type)));
    }

    private boolean isDataChangedManually() {
        LogUtils.d("ok", "Initial Country " + (dataObject.getCountryData() != null ? dataObject.getCountryData().getLatinName() : "Null"));
        LogUtils.d("ok", (selectedCountry != null ? selectedCountry.getLatinName() : "Null"));
        LogUtils.d("ok", "Initial Transfer_Type " + dataObject.getTransferType());
        LogUtils.d("ok", "" + selectedTransferType);
        LogUtils.d("ok", "Initial Bank " + (dataObject.getBankData() != null ? dataObject.getBankData().getBankName() : "Null"));
        LogUtils.d("ok", (selectedBank != null ? selectedBank.getBankName() : "Null"));
        LogUtils.d("ok", "Initial Service_Type " + (dataObject.getServiceTypeData() != null ? dataObject.getServiceTypeData().getMapping() : "Null"));
        LogUtils.d("ok", (selectedServiceType != null ? selectedServiceType.getMapping() : "Null"));
        if (isSelectedDataPrefect()) {
            try {
                if (dataObject.getCountryData() != null && dataObject.getCountryData().getId().equals(selectedCountry.getId()) && dataObject.getBankData() != null && dataObject.getBankData().getId().equals(selectedBank.getId()) && dataObject.getTransferType().equalsIgnoreCase(selectedTransferType) && dataObject.getServiceTypeData() != null && dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(selectedServiceType.getMapping())) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else
            return true;
    }

    private void addNewBeneficiary() {
        LogUtils.d("ok", "On Add New Beneficiary");
        try {
            if (isSelectedDataPrefect()) {
                BeneficiaryData tempBeneficiaryData = new Gson().fromJson(new Gson().toJson(dataObject), BeneficiaryData.class);
                if (tempBeneficiaryData.getBankData() != null && tempBeneficiaryData.getBankData().getId() != null && tempBeneficiaryData.getBankData().getId().equals(selectedBank.getId())) {
                    tempBeneficiaryData.setBranchData(new BranchData());
                    tempBeneficiaryData.getBranchData().setBranchCode(tempBeneficiaryData.getBranchCode());
                    tempBeneficiaryData.getBranchData().setBranchName(tempBeneficiaryData.getBranchName());
                } else {
                    tempBeneficiaryData.setBranchName(null);
                    tempBeneficiaryData.setBranchCode(null);
                    tempBeneficiaryData.setBranchData(null);
                    tempBeneficiaryData.setAccountNumber(null);
                    tempBeneficiaryData.setIfscCode(null);
                    tempBeneficiaryData.setIBANNumber(null);
                    tempBeneficiaryData.setSwift(null);
                    tempBeneficiaryData.setRoutingCode(null);
                    tempBeneficiaryData.setInstitutionCode(null);
                    tempBeneficiaryData.setBsb(null);
                    tempBeneficiaryData.setTransitCode(null);
                    tempBeneficiaryData.setSortCode(null);
                }

                tempBeneficiaryData.setCountryData(selectedCountry);
                tempBeneficiaryData.setBankData(selectedBank);
                tempBeneficiaryData.setTransferType(getTransferType());
                tempBeneficiaryData.setServiceTypeData(selectedServiceType);

                if (selectedServiceType.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING))
                    intent = new Intent(context, AddBeneficiaryActivity.class);
                else
                    intent = new Intent(context, AddBeneficiaryCeActivity.class);

                intent.putExtra(Constants.OBJECT, tempBeneficiaryData);
                intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
                startActivityForResult(intent, Constants.ADD_NEW_BENEFICIARY);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void onBeneficiaryMissingFields(JSONArray jsonArray, String fieldId, String message) {
        LogUtils.d("ok", "On Beneficiary Fields Missing");
        if (selectedServiceType.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            intent = new Intent(context, UpdateExistingBeneficiaryActivity.class);
            intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<BeneficiaryDynamicFields>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeneficiaryDynamicFields>>() {
            }.getType()));
        } else {
            intent = new Intent(context, UpdateExistingBeneficiaryCeActivity.class);
            intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<BeneficiaryDynamicFieldsCe>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeneficiaryDynamicFieldsCe>>() {
            }.getType()));
        }
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.ID, fieldId);
        intent.putExtra(Constants.MESSAGE, message);
        startActivityForResult(intent, Constants.UPDATE_MISSING_FIELD_BENEFICIARY);
    }

    private void validateExistingBeneficiary() {
        LogUtils.d("ok", "Validate TXN");
        if (dataObject.getTxnAmountData() != null) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                JSONObject input = new APIRequestParams().checkTxnLimits(CommonUtils.getUserId(), CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping()),
                        dataObject.getBeneficiaryId(), dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE(),
                        dataObject.getServiceTypeData().getMapping(), dataObject.getTransferType(), dataObject.getTxnAmountData().getYouSend(),
                        dataObject.getTxnAmountData().getYouGet(), dataObject.getTxnAmountData().getTotalToPay(), dataObject.getTxnAmountData().getRate(),
                        dataObject.getTxnAmountData().getFee(), dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode(),LogoutCalling.getDeviceID(context),sessionTime);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.CHECK_TXN_LIMITS_URL, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Please enter valid amount.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuccess() {
        if (dataObject.getCountryData().getCountryCodeAREX().equalsIgnoreCase("44") && dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            showExtraInformationDialog("Send Money", getString(app.alansari.R.string.malaysia_msg));
            return;
        }
        if (isSelectedDataPrefect()) {
            startAdditionalInfoScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    setCountryData();
                } else {
                    if (selectedCountry == null) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.SELECT_ITEM_BANK && resultCode == Activity.RESULT_OK) {
                selectedBank = data.getParcelableExtra(Constants.OBJECT);
                setBankData();
            } else if (requestCode == Constants.ADD_NEW_BENEFICIARY && resultCode == Activity.RESULT_OK) {
                if (data != null && data.getParcelableExtra(Constants.OBJECT) != null) {
                    dataObject = data.getParcelableExtra(Constants.OBJECT);
                    dataObject.setTxnAmountData(((SendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())).getTxnData());

                }
            } else if (requestCode == Constants.UPDATE_MISSING_FIELD_BENEFICIARY && resultCode == Activity.RESULT_OK) {
                if (data != null && data.getParcelableExtra(Constants.OBJECT) != null) {
                    dataObject = data.getParcelableExtra(Constants.OBJECT);
                    dataObject.setTxnAmountData(((SendMoneyFragment) adapter.getFragment(viewPager.getCurrentItem())).getTxnData());
                    validateExistingBeneficiary();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAdditionalInfoScreen() {
        if (selectedServiceType.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING))
            intent = new Intent(context, AdditionalInfoActivity.class);
        else
            intent = new Intent(context, AdditionalInfoCeActivity.class);

        dataObject.setVat(vat);
        dataObject.setDiscount(discount);
        dataObject.setRoundoff(rounding);

        dataObject.setServiceTypeData(selectedServiceType);
        dataObject.setTransferType(selectedTransferType);
        dataObject.setBankData(selectedBank);

        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        startActivity(intent);
    }

    private void fetchBeneficiaryDetails(String beneficiaryId, String serviceType) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiaryDetails(beneficiaryId, serviceType,userPkId, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_BENEFICIARY_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case FETCH_BENEFICIARY_DETAILS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (Validation.isValidJsonArray(response.getJSONArray(Constants.RESULT))) {
                                ArrayList<BeneficiaryData> beneficiaryData = (ArrayList<BeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryData>>() {
                                }.getType());
                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    dataObject = beneficiaryData.get(0);
                                    setInitialData();
                                    return;
                                }
                            }
                            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            onBackPressed();
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
            case CHECK_TXN_LIMITS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            onSuccess();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.MISSING)) {
                            if (response.get(Constants.RESULT) != null && !response.getString(Constants.RESULT).equalsIgnoreCase("null") && Validation.isValidJsonArray(response.getJSONArray(Constants.RESULT))) {
                                onBeneficiaryMissingFields(response.getJSONArray(Constants.RESULT), response.getString(Constants.ID), response.getString(Constants.MESSAGE));

                              //  Log.e("vcjkgbvjhsbv",""+ response.getString(Constants.ID)+" "+response.getString("COUNTRY_ID"));
                                /*Log.e("vcjkgbvjhsbv",""+ response.getString(Constants.ID)+" "+response.getString("CURRENCY_ID"));
                                Log.e("vcjkgbvjhsbv",""+ response.getString(Constants.ID)+" "+response.getString("BANK_ID"));
                                Log.e("vcjkgbvjhsbv",""+ response.getString(Constants.ID)+" "+response.getString("FIELD_ID"));
                                Log.e("vcjkgbvjhsbv",""+ response.getString(Constants.ID)+" "+response.getString("FIELD_KEY"));
*/


                                //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context, "Unable to proceed: " + response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)){
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
            case FETCH_SERVICE_TYPES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                availableServiceTypeDataList = (ArrayList<ServiceTypeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ServiceTypeData>>() {
                                }.getType());
                                if (availableServiceTypeDataList != null && availableServiceTypeDataList.size() > 0) {
                                    SharedPreferenceManger.saveServiceTypeData(gson, availableServiceTypeDataList);
                                    SharedPreferenceManger.setPrefVal(Constants.FETCH_SERVICE_TYPE_DATA_OFF, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                    setInitialData();
                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
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
        }
    }

    private void showExtraInformationDialog(String title, String message) {
        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(app.alansari.R.layout.sample_msg_dialog);

        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText(title);
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(message);

        myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
        myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText(getString(app.alansari.R.string.btn_continue));

        myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectedDataPrefect()) {
                    startAdditionalInfoScreen();
                }
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        Bundle bundle;
        Fragment[] fragmentArray;
        ArrayList<ServiceTypeData> serviceTypeData;


        public FragmentPagerAdapter(FragmentManager fm, ArrayList<ServiceTypeData> serviceTypeData) {
            super(fm);
            fragmentArray = new Fragment[serviceTypeData.size()];
            this.serviceTypeData = new ArrayList<>();
            this.serviceTypeData.addAll(serviceTypeData);
        }

        public Fragment getFragment(int position) {
            return fragmentArray[position];
        }


        public void setServiceTypeData(ArrayList<ServiceTypeData> serviceTypeData) {
            this.serviceTypeData.clear();
            this.serviceTypeData.addAll(serviceTypeData);
            fragmentArray = new Fragment[serviceTypeData.size()];
        }

        public ServiceTypeData getServiceTypeData(int position) {
            return serviceTypeData.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            fragmentArray[position] = new SendMoneyFragment();
            bundle = new Bundle();
            bundle.putParcelable(Constants.COUNTRY_DATA, selectedCountry);
            bundle.putParcelable(Constants.BANK_DATA, selectedBank);
            bundle.putString(Constants.TRANSFER_TYPE, selectedTransferType);
            bundle.putParcelable(Constants.SERVICE_TYPE_DATA, serviceTypeData.get(position));
            fragmentArray[position].setArguments(bundle);
            return fragmentArray[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return serviceTypeData.get(position).getName();
        }

        @Override
        public int getCount() {
            return serviceTypeData.size();
        }
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