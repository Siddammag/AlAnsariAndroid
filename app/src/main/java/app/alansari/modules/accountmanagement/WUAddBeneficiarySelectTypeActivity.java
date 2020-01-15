package app.alansari.modules.accountmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TransactionModeData;
import app.alansari.models.WuCurrencyData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_TRANSACTION_MODE_IN_COUNTRY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_SERVICE_TYPES;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 23 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class WUAddBeneficiarySelectTypeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    ArrayList<ServiceTypeData> serviceTypeData;
    ArrayList<TransactionModeData> transactionModeData;
    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private Button btnNext;
    private BeneficiaryData dataObject;
    private CountryData selectedCountry;
    private BankData selectedBank;
    private WuCurrencyData selectedCurrency;
    private ServiceTypeData selectedServiceType;
    private TransactionModeData selectedTransactionMode;
    private int transactionModeStatus, serviceTypeStatus;
    private ArrayList<String> receiverNameTypeList;

    private TextView tvCountry;
    private AppCompatImageView ivFlag;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private View receiverNameTypeView, currencyView, serviceTypeView;
    private TextView tvReceiverNameLabel, tvCurrencyLabel, tvServiceTypeLabel;
    private AutoCompleteTextView tvReceiverName, tvCurrency, tvServiceType;
    private String wuCardNo;
    private String arexUserId;
    private String serviceType = "";
    private String serviceTypeName = "";
    private String TOTAL_WU_POINTS = "0";
    private String sessionTime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        tvCountry = (TextView) findViewById(app.alansari.R.id.country_name);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        receiverNameTypeView = findViewById(R.id.receiver_name_type_card_view);
        currencyView = findViewById(app.alansari.R.id.currency_card_view);
        serviceTypeView = findViewById(app.alansari.R.id.service_type_card_view);
        tvReceiverName = (AutoCompleteTextView) findViewById(app.alansari.R.id.receiver_name_type_text);
        tvCurrency = (AutoCompleteTextView) findViewById(app.alansari.R.id.currency_text);
        tvServiceType = (AutoCompleteTextView) findViewById(app.alansari.R.id.service_type_text);
        tvReceiverNameLabel = (TextView) findViewById(app.alansari.R.id.receiver_name_type_label);
        tvCurrencyLabel = (TextView) findViewById(app.alansari.R.id.currency_label);
        tvServiceTypeLabel = (TextView) findViewById(app.alansari.R.id.service_type_label);

        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        btnNext.setOnClickListener(this);

        ivFlag.setOnClickListener(this);
        tvCountry.setOnClickListener(this);

        receiverNameTypeView.setOnClickListener(this);
        tvReceiverName.setOnClickListener(this);
        currencyView.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        serviceTypeView.setOnClickListener(this);
        tvServiceType.setOnClickListener(this);

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

        receiverNameTypeList = new ArrayList<>();
        receiverNameTypeList.add("FIRST NAME/LAST NAME");
        receiverNameTypeList.add("PATERNAL NAME/MATERNAL NAME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.wu_add_beneficiary_select_type_activity);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, app.alansari.R.color.colorWhite));
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Add New Receiver");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
        if (getIntent().getExtras() != null) {
            wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
            TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
            ((EditText) findViewById(R.id.et_wu_number)).setText(wuCardNo);
        }
        setSelectedCountryData();
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

    private void setSelectedCountryData() {
        if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
            multiStateView.setVisibility(View.GONE);
            SharedPreferenceManger.setPrefVal(Constants.FETCH_WU_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            openCountryList();
        } else {
            CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
            tvCountry.setText("Receiving country (" + selectedCountry.getLatinName() + ")");
            multiStateView.setVisibility(View.VISIBLE);

            tvReceiverName.setText(null);
            tvServiceType.setText(null);
            selectedTransactionMode = null;
            selectedServiceType = null;
            btnNext.setEnabled(false);

            fetchTransactionModes();
            fetchServiceTypes();
        }
    }

    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
//        intent.putExtra("hideFirstItem", true);
        intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_COUNTRY);
        startActivityForResult(intent, Constants.WU_SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private ServiceTypeData getServiceType(String serviceType) {
        for (int i = 0; i < serviceTypeData.size(); i++) {
            if (serviceTypeData.get(i).getMapping().equalsIgnoreCase(serviceType))
                return serviceTypeData.get(i);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchTransactionModes();
                fetchServiceTypes();
                break;
            case app.alansari.R.id.flag:
            case app.alansari.R.id.country_name:
                openCountryList();
                break;
            case app.alansari.R.id.receiver_name_type_text:
            case app.alansari.R.id.receiver_name_type_card_view:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_BENEFICIARY_NAME_TYPE);
                intent.putStringArrayListExtra(Constants.LIST, receiverNameTypeList);
                intent.putExtra(Constants.DEFAULT_HEADER, "1");
                startActivityForResult(intent, Constants.WU_SELECT_BENEFICIARY_NAME_TYPE);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.currency_text:
            case app.alansari.R.id.currency_card_view:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_CURRENCY);
                intent.putExtra(Constants.COUNTRY_ID, selectedCountry.getCountryCodeAREX());
                intent.putExtra(Constants.WU_ISO_COUNTRY_CODE, selectedCountry.getWuCountryCode());
                intent.putExtra(Constants.DEFAULT_HEADER, "1");
                startActivityForResult(intent, Constants.WU_SELECT_ITEM_CURRENCY);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.service_type_text:
            case app.alansari.R.id.service_type_card_view:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD);
                intent.putExtra(Constants.TITLE, "service type ");
                intent.putExtra(Constants.ID, Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                intent.putExtra(Constants.COUNTRY_CODE, selectedCountry.getCountryCodeAREX());
                intent.putExtra(Constants.DEFAULT_HEADER, "1");
                startActivityForResult(intent, Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.next_btn:
                checkAllFields();
                if (btnNext.isEnabled()) {
                    goToBeneficiaryScreen();
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void goToBeneficiaryScreen() {
        intent = new Intent(context, WUAddBeneficiaryActivity.class);
        intent.putExtra(Constants.AREX_COUNTRY_CODE, selectedCountry.getCountryCodeAREX());
        intent.putExtra(Constants.WU_COUNTRY_CODE, selectedCountry.getWuCountryCode());
        if (tvReceiverName.getText().toString().equals("FIRST NAME/LAST NAME")) {
            intent.putExtra(Constants.NAME_TYPE, "D");
        } else {
            intent.putExtra(Constants.NAME_TYPE, "M");
        }
        intent.putExtra(Constants.WU_CARD_NO, wuCardNo);
        intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
        intent.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
        intent.putExtra(Constants.WU_SELECTED_COUNTRY, selectedCountry);
        intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE, serviceType);
        intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE_NAME, serviceTypeName);
        startActivity(intent);
        finish();
    }

    void fetchTransactionModes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //TODO Update 26 ->
            //dataObject.getBankData().getId();
//            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTransactionModes((String) SharedPreferenceManger.getPrefVal(context, Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject.getCountryData().getId(), "26", dataObject.getServiceTypeData().getId(), dataObject.getTransferType()), Constants.FETCH_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO, Request.Method.POST, this);
            //JsonObjectRequest jsonObjReq = new CallAddr().executeApi(null, Constants.FETCH_TRANSACTION_MODE_IN_COUNTRY_URL, FETCH_TRANSACTION_MODE_IN_COUNTRY, Request.Method.POST, this);
            //AppController.getInstance().getRequestQueue().cancelAll(FETCH_TRANSACTION_MODE_IN_COUNTRY.toString());
            //AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_TRANSACTION_MODE_IN_COUNTRY.toString());
            onResponse(0, null, FETCH_TRANSACTION_MODE_IN_COUNTRY);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    void fetchServiceTypes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceTypes((String) SharedPreferenceManger.getPrefVal(context, Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING)), Constants.FETCH_SERVICE_TYPE_IN_COUNTRY_URL, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY, Request.Method.POST, this);
            //AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY.toString());
            //AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY.toString());
            onResponse(0, null, FETCH_SERVICE_TYPE_IN_COUNTRY);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void updateServiceType() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuServices(selectedCountry.getCountryCodeAREX(),((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING)), LogoutCalling.getDeviceID(context),sessionTime), Constants.WU_FETCH_SERVICE_TYPES_URL, WU_FETCH_SERVICE_TYPES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SERVICE_TYPES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SERVICE_TYPES.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.WU_SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    setSelectedCountryData();
                    selectedCurrency = null;
                    tvCurrencyLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    tvCurrency.setText("");
                    tvServiceTypeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    tvServiceType.setText("");
//                    updateServiceType();
                } else {
                    if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.WU_SELECT_BENEFICIARY_NAME_TYPE && resultCode == Activity.RESULT_OK) {
                if (data.getStringExtra(Constants.OBJECT) != null) {
                    tvReceiverName.setText(data.getStringExtra(Constants.OBJECT));
                    tvReceiverNameLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
                    checkAllFields();
                }
            } else if (requestCode == Constants.WU_SELECT_ITEM_CURRENCY && resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    selectedCurrency = data.getParcelableExtra(Constants.OBJECT);
                    tvCurrency.setText(selectedCurrency.getName());
                    tvCurrencyLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));

                    ArrayList<CountryData.CurrencyData> currencyData = new ArrayList<CountryData.CurrencyData>();
                    CountryData.CurrencyData currency = new CountryData.CurrencyData();
                    currency.setCurrencyCode(selectedCurrency.getCurrencyCode());
                    currency.setName(selectedCurrency.getWuCurrencyCode());
                    currencyData.add(currency);
                    selectedCountry.setCurrencyData(currencyData);

                    checkAllFields();
                }
            } else if (requestCode == Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE && resultCode == Activity.RESULT_OK) {
                WuCurrencyData wuCurrencyData = data.getParcelableExtra(Constants.OBJECT);
                tvServiceType.setText(wuCurrencyData.getDisplayValue());
                serviceType = wuCurrencyData.getDisplayKey();
                serviceTypeName = wuCurrencyData.getDisplayValue();
                tvServiceTypeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
                checkAllFields();
            } else if (requestCode == Constants.ADD_NEW_BENEFICIARY) {
                if (resultCode == Activity.RESULT_OK) {
                    data.putExtra(Constants.OBJECT, data.getParcelableExtra(Constants.OBJECT));
                    setResult(AppCompatActivity.RESULT_OK, data);
                } else {
                    setResult(AppCompatActivity.RESULT_CANCELED, data);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetBankData() {
        selectedBank = null;
        tvCurrency.setText("Beneficiary Bank / Agents");
        tvCurrencyLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
    }

    private void resetServiceType() {
        selectedServiceType = null;
        tvServiceType.setText("Service Type");
        tvServiceTypeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
    }

    private void checkAllFields() {
        if (tvServiceType.getText().toString() != null && (receiverNameTypeList.indexOf(tvReceiverName.getText().toString()) != -1) && selectedCurrency != null) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    private void setViewState(int viewState) {
        if (transactionModeStatus == -1 || serviceTypeStatus == -1) {
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_TRANSACTION_MODE_IN_COUNTRY:
                if (status == 0) {
                    status = 1;
                    response = fetchTransactionModesResponse();
                }
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                transactionModeData = (ArrayList<TransactionModeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TransactionModeData>>() {
                                }.getType());
                                if (transactionModeData != null && transactionModeData.size() > 0) {
                                    transactionModeStatus = 1;
                                    if (serviceTypeStatus == 1) {
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    }
                                } else {
                                    transactionModeStatus = -1;
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            } else {
                                setViewState(VIEW_STATE_EMPTY);
                            }
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_WRONG);
                    }
                } else {
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
            case FETCH_SERVICE_TYPE_IN_COUNTRY:
                if (status == 0) {
                    status = 1;
                    response = fetchServiceTypeResponse();
                }
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                serviceTypeData = (ArrayList<ServiceTypeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ServiceTypeData>>() {
                                }.getType());
                                if (serviceTypeData != null && serviceTypeData.size() > 0) {
                                    serviceTypeStatus = 1;
                                    if (transactionModeStatus == 1) {
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    }
                                } else {
                                    serviceTypeStatus = -1;
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            } else {
                                serviceTypeStatus = -1;
                                setViewState(VIEW_STATE_EMPTY);
                            }
                        } else {
                            serviceTypeStatus = -1;
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        serviceTypeStatus = -1;
                        setViewState(VIEW_STATE_WRONG);
                    }
                } else {
                    serviceTypeStatus = -1;
                    setViewState(VIEW_STATE_WRONG);
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

    private JSONObject fetchTransactionModesResponse() {
        try {
            return new JSONObject("{\n" +
                    "  \"STATUS_CODE\": 257,\n" +
                    "  \"MESSAGE\": \"SUCCESS\",\n" +
                    "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "  \"RESULT\": [\n" +
                    "    {\n" +
                    "      \"SERVICE_MAPPING\": \"BT\",\n" +
                    "      \"STATUS\": \"1\",\n" +
                    "      \"SERVICE_PK_ID\": 2,\n" +
                    "      \"SERVICE_NAME\": \"Bank Transfer\",\n" +
                    "      \"REMIT_TIME\": \"1 Day\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"SERVICE_MAPPING\": \"CP\",\n" +
                    "      \"STATUS\": \"1\",\n" +
                    "      \"SERVICE_PK_ID\": 1,\n" +
                    "      \"SERVICE_NAME\": \"Cash Pick up\",\n" +
                    "      \"REMIT_TIME\": \"1-3 Days\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject fetchServiceTypeResponse() {
        try {
            return new JSONObject("{\n" +
                    "    \"RESULT\": [\n" +
                    "        {\n" +
                    "            \"STATUS\": \"1\",\n" +
                    "            \"SERVICE_PK_ID\": 1,\n" +
                    "            \"SERVICE_NAME\": \"Value Transfer\",\n" +
                    "            \"SERVICE_MAPPING\": \"AREX\",\n" +
                    "            \"REMIT_TIME\": \"1-3 Days\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"STATUS\": \"1\",\n" +
                    "            \"SERVICE_PK_ID\": 2,\n" +
                    "            \"SERVICE_NAME\": \"Instant Transfer\",\n" +
                    "            \"SERVICE_MAPPING\": \"CE\",\n" +
                    "            \"REMIT_TIME\": \"1 Day\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"AMOUNT\": null,\n" +
                    "    \"MESSAGE\": \"SUCCESS\",\n" +
                    "    \"OTP\": null,\n" +
                    "    \"USER_STATUS\": null,\n" +
                    "    \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "    \"STATUS_CODE\": 257\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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
