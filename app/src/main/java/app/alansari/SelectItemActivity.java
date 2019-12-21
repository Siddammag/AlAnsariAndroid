package app.alansari;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.core.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.adapters.SelectItemRecyclerAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AccountTypeData;
import app.alansari.models.AdditionalInfoData;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.BranchData;
import app.alansari.models.BranchLocatorCityData;
import app.alansari.models.CreditCardBankData;
import app.alansari.models.CurrencyData;
import app.alansari.models.ProfessionalLoadList;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TransactionModeData;
import app.alansari.models.UserAccountBankData;
import app.alansari.models.UserAccountData;
import app.alansari.models.WuCurrencyData;
import app.alansari.models.additioninfowc.RESULTItem;
import app.alansari.models.gender.GenderSelection;
import app.alansari.models.servicetype.RESULTDTO;
import app.alansari.modules.accountmanagement.AddBankAccountActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CURRENCY_URL;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_ACCOUNT_TYPE;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_ALL_BANKS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENF_BANK;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENF_BANK_BRANCH;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CREDIT_CARD_BANK_LIST;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_USER_ACCOUNTS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_USER_ACCOUNT_BANK_LIST;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOAD_SERVICE_TYPE_LIST;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.PROFESIONAL_AND_DESIGANATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_CURRENCY_URL;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_SERVICE_TYPES;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_SOURCE_OF_FUNDS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_TRANSACTION_PURPOSE;
import static app.alansari.Utils.Constants.SELECT_COUNTRY_TYPE;
import static app.alansari.Utils.Constants.SELECT_ITEM_ADDITIONAL_FIELD_GENDER;
import static app.alansari.Utils.Constants.SELECT_ITEM_ADDITIONAL_TRAVEL_CARD_FIELD;
import static app.alansari.Utils.Constants.SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE;
import static app.alansari.Utils.Constants.SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE;
import static app.alansari.Utils.Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE;
import static app.alansari.Utils.Constants.SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY;
import static app.alansari.Utils.Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD;
import static app.alansari.Utils.Constants.SELECT_PROFESIONAL_AND_DESIGANATION;
import static app.alansari.Utils.Constants.WU_SELECT_BENEFICIARY_NAME_TYPE;
import static app.alansari.Utils.Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD;
import static app.alansari.Utils.Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE;
import static app.alansari.Utils.Constants.WU_SELECT_ITEM_CURRENCY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY_BANK;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;


/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SelectItemActivity extends AppCompatActivity implements OnClickListener, OnWebServiceResult, CustomClickListener, LogOutTimerUtil.LogOutListener {

    String userPkIdAll, sessionTimeAll,memPkIdAll;
    private int type;
    private Context context;
    private AutoCompleteTextView etKeyword;
    private RecyclerView recyclerView;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private SelectItemRecyclerAdapter recyclerAdapter;
    private UserAccountData selectedUserAccount;
    private BeneficiaryData mSelectDetails;

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

    private void init() {
        etKeyword = (AutoCompleteTextView) findViewById(R.id.search_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(this));

        multiStateView = (MultiStateView) findViewById(R.id.multiState_rview);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        findViewById(R.id.close_btn).setOnClickListener(this);

        etKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Validation.isValidAutoCompleteTextValue(etKeyword))
                    searchData(etKeyword.getText().toString().trim());
                else
                    searchData("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validation.isValidAutoCompleteTextValue(etKeyword))
                        searchData(etKeyword.getText().toString().trim());
                    CommonUtils.hideKeyboard(context);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item_activity);
        context = SelectItemActivity.this;
        init();
        type = getIntent().getExtras().getInt(Constants.ITEM_TYPE);
        etKeyword.setHint("Type / " + getHintText());
        swipeRefreshLayout();
        recyclerAdapter = new SelectItemRecyclerAdapter(this, new ArrayList<>(), this, multiStateView);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (getIntent().getIntExtra(Constants.ITEM_TYPE, -1) == Constants.SELECT_ITEM_USER_ACCOUNT) {
            findViewById(R.id.add_bank_btn).setVisibility(View.VISIBLE);
            findViewById(R.id.add_bank_btn).setOnClickListener(this);
        } else {
            findViewById(R.id.add_bank_btn).setVisibility(View.GONE);
        }
        userPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        memPkIdAll= (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        sessionTimeAll = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        fetchData();
        setHeader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
        switch (type) {
            case Constants.SELECT_ITEM_BANK:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_ALL_BANKS.toString());
                break;
            case Constants.SELECT_ITEM_BENF_BANK_LIST:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_BENF_BANK.toString());
                break;
            case Constants.SELECT_ITEM_BENF_BANK_BRANCH_LIST:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_BENF_BANK_BRANCH.toString());
                break;
            case Constants.SELECT_ITEM_USER_ACCOUNT:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_USER_ACCOUNTS.toString());
                break;
            case Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_USER_ACCOUNT_BANK_LIST.toString());
                break;
            case Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST:
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_CREDIT_CARD_BANK_LIST.toString());
                break;
        }
        super.onDestroy();
        stopLogoutTimer();
    }

    private String getHintText() {
        switch (type) {
            case Constants.SELECT_ITEM_STATE:
                return "Select state ";
            case Constants.SELECT_ITEM_COUNTRY:
                return "Select country ";
            case Constants.SELECT_ITEM_BANK:
            case Constants.SELECT_ITEM_BENF_BANK_LIST:
                return "Select bank ";
            case Constants.SELECT_ITEM_BRANCH:
            case Constants.SELECT_ITEM_BENF_BANK_BRANCH_LIST:
                return "Select branch ";
            case Constants.SELECT_ITEM_ACCOUNT_TYPE:
                return "Select account type ";
            case Constants.SELECT_ITEM_ADDITIONAL_FIELD:
                return "Select " + getIntent().getExtras().getString(Constants.TITLE, "").toLowerCase();

            case Constants.SELECT_ITEM_ADDITIONAL_TRAVEL_CARD_FIELD:
                return "Select " + getIntent().getExtras().getString(Constants.TITLE, "").toLowerCase();
            case Constants.SELECT_ITEM_USER_ACCOUNT:
                return "Select UAE bank account ";
            case Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST:
                findViewById(R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, R.drawable.am_bank_account_header_bg));
                CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, R.color.color024B54));
                return "Select bank name ";
            case Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST:
                findViewById(R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, R.drawable.am_credit_card_header_bg));
                CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, R.color.color1E6AB3));
                return "Select bank name ";
            case SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY:
                return "Select payment day ";
            case SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE:
                return "Select transaction mode ";
            case SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE:
                return "Select service type ";
            case WU_SELECT_ITEM_ADDITIONAL_FIELD:
                return "Select " + getIntent().getExtras().getString(Constants.TITLE, "").toLowerCase();
            case WU_SELECT_ITEM_CURRENCY:
                return "Select currency type ";
            case WU_SELECT_BENEFICIARY_NAME_TYPE:
                return "Select name type ";
            case SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD:
                return "Select type";
            case SELECT_ITEM_ADDITIONAL_FIELD_GENDER:
                return "Select type";
            case SELECT_PROFESIONAL_AND_DESIGANATION:
                return "Select type";
            case SELECT_COUNTRY_TYPE:
                return "Select country type";
        }
        return "Select";
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchData();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_button:
            case R.id.error_button:
                fetchData();
                break;
            case R.id.close_btn:
                onBackPressed();
                break;
            case app.alansari.R.id.add_bank_btn:
                Intent intent = new Intent(context, AddBankAccountActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_USER_ACCOUNT);
                break;
            default:
                break;
        }
    }

    void fetchData() {
        if (type == Constants.SELECT_ITEM_ADDITIONAL_FIELD) {
            setAdditionalData();
        } else if (type == Constants.SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE) {
            setAdditionalData();
        } else if (type == Constants.SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE) {
            setAdditionalData();
        } else if (type == Constants.SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY) {
            setAdditionalData();
        } else if (type == Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE) {
            setAdditionalData();
        } else if (type == WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE) {
            setAdditionalData();
        } else if (type == WU_SELECT_BENEFICIARY_NAME_TYPE) {
            setAdditionalData();
        } else if (type == SELECT_ITEM_ADDITIONAL_TRAVEL_CARD_FIELD) {
            setAdditionalData();
        }else if(type == SELECT_ITEM_ADDITIONAL_FIELD_GENDER){
            setAdditionalData();
        }

//--------Adding dynamic api call-------------------------------------------------------------------
        else if(type==SELECT_PROFESIONAL_AND_DESIGANATION){
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
                etKeyword.setEnabled(false);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuSourceOfFunds((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.LOAD_PROFESSIONLIST_URL, PROFESIONAL_AND_DESIGANATION, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(PROFESIONAL_AND_DESIGANATION.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, PROFESIONAL_AND_DESIGANATION.toString());


            } else {
                setViewState(VIEW_STATE_ERROR);
            }

        }else if (type==SELECT_COUNTRY_TYPE){
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addingParameters((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)), Constants.FETCH_BRANCH_CITIES_URL, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES.toString());
            } else {
                setViewState(MultiStateView.VIEW_STATE_ERROR);
            }

        }

//--------END Call----------------------------------------------------------------------------------
        else if(type == SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD){
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
                etKeyword.setEnabled(false);
                JsonObjectRequest jsonObjReq = null;
                switch (getIntent().getIntExtra(Constants.ID, 0)) {
                    case 70:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceType(userPkIdAll,memPkIdAll, getIntent().getStringExtra(Constants.BENEF_PK_ID), getIntent().getStringExtra(Constants.COUNTRY_CODE), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.LOAD_SERVICE_TYPE_LIST_URL, LOAD_SERVICE_TYPE_LIST, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(LOAD_SERVICE_TYPE_LIST.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, LOAD_SERVICE_TYPE_LIST.toString());
                        break;
                }
            } else {
                setViewState(VIEW_STATE_ERROR);
            }
        } else if (type == WU_SELECT_ITEM_ADDITIONAL_FIELD) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
                etKeyword.setEnabled(false);

                JsonObjectRequest jsonObjReq = null;
                switch (getIntent().getIntExtra(Constants.ID, 0)) {
                    case Constants.WU_SELECT_ITEM_FUNDS_SOURCE:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuSourceOfFunds((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.FETCH_SOURCE_OF_FUNDS_URL, WU_FETCH_SOURCE_OF_FUNDS, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SOURCE_OF_FUNDS.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SOURCE_OF_FUNDS.toString());
                        break;
                    case Constants.WU_SELECT_ITEM_TRANSACTION_PURPOSE:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuSourceOfFunds((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.FETCH_TRANSACTION_PURPOSE_URL, WU_FETCH_TRANSACTION_PURPOSE, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_TRANSACTION_PURPOSE.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_TRANSACTION_PURPOSE.toString());
                        break;
                    case Constants.WU_SELECT_ITEM_COUNTRY_OF_BIRTH:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuSourceOfFunds((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.FETCH_SOURCE_OF_FUNDS_URL, WU_FETCH_SOURCE_OF_FUNDS, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SOURCE_OF_FUNDS.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SOURCE_OF_FUNDS.toString());
                        break;
                    case Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuServices(getIntent().getStringExtra(Constants.COUNTRY_CODE), ((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING)), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.WU_FETCH_SERVICE_TYPES_URL, WU_FETCH_SERVICE_TYPES, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SERVICE_TYPES.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SERVICE_TYPES.toString());
                        break;
                    case Constants.WU_SELECT_ITEM_CITY:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCity(getIntent().getStringExtra(Constants.WU_COUNTRY_STATE_CODE), userPkIdAll, LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.WU_FETCH_STATE_URL, WU_FETCH_SERVICE_TYPES, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SERVICE_TYPES.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SERVICE_TYPES.toString());
                        break;
                    case Constants.WU_SELECT_ITEM_CITY_STATE:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCity(getIntent().getStringExtra(Constants.WU_COUNTRY_STATE_CODE), userPkIdAll, LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.WU_FETCH_STATE_URL, WU_FETCH_SERVICE_TYPES, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_SERVICE_TYPES.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_SERVICE_TYPES.toString());
                        break;
                }
            } else {
                setViewState(VIEW_STATE_ERROR);
            }
        } else {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
                etKeyword.setEnabled(false);

                JsonObjectRequest jsonObjReq = null;
                switch (type) {
                    case Constants.SELECT_ITEM_BANK:
                        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBanksList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), getIntent().getExtras().getString(Constants.COUNTRY_ID), getIntent().getExtras().getString(Constants.TYPE), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ALL_BANKS_URL, FETCH_ALL_BANKS, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_ALL_BANKS.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_ALL_BANKS.toString());
                        break;
                    case Constants.SELECT_ITEM_BENF_BANK_LIST:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBenfBankList(getIntent().getExtras().getString(Constants.COUNTRY_ID), userPkIdAll, LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.FETCH_BENF_BANK_URL, FETCH_BENF_BANK, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_BENF_BANK.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_BENF_BANK.toString());
                        break;
                    case Constants.SELECT_ITEM_BENF_BANK_BRANCH_LIST:
                        String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        String sessionTimes = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBenfBankBranchList(getIntent().getExtras().getString(Constants.SERVICE_TYPE, Constants.AREX_MAPPING), getIntent().getExtras().getString(Constants.BANK_CODE, ""), userPkId, LogoutCalling.getDeviceID(context), sessionTimes), Constants.FETCH_BENF_BANK_BRANCH_URL, FETCH_BENF_BANK_BRANCH, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_BENF_BANK_BRANCH.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_BENF_BANK_BRANCH.toString());
                        break;
                    case Constants.SELECT_ITEM_USER_ACCOUNT:
                        String sessionTime2 = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

                        if (getIntent().getStringExtra(Constants.IS_WU).equalsIgnoreCase("Y")) {
                            jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchUserAccountList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime2), Constants.FETCH_WU_USER_ACCOUNTS_URL, FETCH_USER_ACCOUNTS, Request.Method.POST, this);
                        } else {
                            jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchUserAccountList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime2), Constants.FETCH_USER_ACCOUNTS_URL, FETCH_USER_ACCOUNTS, Request.Method.POST, this);
                        }
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_USER_ACCOUNTS.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_USER_ACCOUNTS.toString());
                        break;
                    case Constants.SELECT_ITEM_USER_ACCOUNT_BANK_LIST:
                        String userPkId2 = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        String sessionTime3 = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userPkId2, sessionTime3, LogoutCalling.getDeviceID(context)), Constants.FETCH_USER_ACCOUNT_BANK_LIST_URL, FETCH_USER_ACCOUNT_BANK_LIST, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_USER_ACCOUNT_BANK_LIST.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_USER_ACCOUNT_BANK_LIST.toString());
                        break;
                    case Constants.SELECT_ITEM_CREDIT_CARD_BANK_LIST:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userPkIdAll, sessionTimeAll, LogoutCalling.getDeviceID(context)), Constants.FETCH_CREDIT_CARD_BANK_LIST_URL, FETCH_CREDIT_CARD_BANK_LIST, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_CREDIT_CARD_BANK_LIST.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CREDIT_CARD_BANK_LIST.toString());
                        break;
//                case Constants.SELECT_ITEM_BRANCH:
//                    jsonObjReq = CallAddr.fetchAllBanks(null, null, CommonUtils.SERVICE_TYPE.FETCH_ALL_BANKS, this);
//                    AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_BANKS.toString());
//                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ALL_BANKS.toString());
//                    break;
                    case Constants.SELECT_ITEM_ACCOUNT_TYPE:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAccountType(getIntent().getExtras().getString(Constants.BANK_CODE, ""), userPkIdAll, LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.FETCH_ACCOUNT_TYPE_URL, FETCH_ACCOUNT_TYPE, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(FETCH_ACCOUNT_TYPE.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_ACCOUNT_TYPE.toString());
                        //onResponse(1, getAccountTypeResponse(), FETCH_ACCOUNT_TYPE);
                        break;
                    case Constants.SELECT_ITEM_CURRENCY:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCountriesList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, "0", SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTimeAll), Constants.CURRENCY_URL, CURRENCY_URL, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(CURRENCY_URL.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, CURRENCY_URL.toString());
                        break;

                        //Siddu
                    case WU_SELECT_ITEM_CURRENCY:
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCurrencyList(getIntent().getStringExtra(Constants.COUNTRY_ID), getIntent().getStringExtra(Constants.WU_ISO_COUNTRY_CODE), userPkIdAll, LogoutCalling.getDeviceID(context), sessionTimeAll),

                                Constants.WU_CURRENCY_URL, WU_CURRENCY_URL, Request.Method.POST, this);
                        AppController.getInstance().getRequestQueue().cancelAll(WU_CURRENCY_URL.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, WU_CURRENCY_URL.toString());
                        break;
                }
            } else {
                setViewState(VIEW_STATE_ERROR);
            }
        }
    }

    private void setAdditionalData() {
        try {
            ArrayList dataList = null;
            if (type == SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY) {
                dataList = getIntent().getExtras().getStringArrayList(Constants.LIST);
            } else if (type == SELECT_ITEM_ADD_BENEFICIARY_TYPE) {
                dataList = getIntent().getExtras().getStringArrayList(Constants.LIST);
            } else if (type == WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE) {
                dataList = getIntent().getExtras().getStringArrayList(Constants.LIST);
            } else if (type == WU_SELECT_BENEFICIARY_NAME_TYPE) {
                dataList = getIntent().getExtras().getStringArrayList(Constants.LIST);
            } else if (type == SELECT_ITEM_ADDITIONAL_TRAVEL_CARD_FIELD) {
                dataList = getIntent().getExtras().getStringArrayList(Constants.LIST);
            }else if(type == SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD) {
                dataList = getIntent().getExtras().getIntegerArrayList(Constants.LIST);
            }else if(type==SELECT_ITEM_ADDITIONAL_FIELD_GENDER){
                dataList = getIntent().getExtras().getParcelableArrayList(Constants.LIST);
            } else {
                dataList = getIntent().getExtras().getParcelableArrayList(Constants.LIST);
            }
            if (Validation.isValidList(dataList)) {
                recyclerAdapter.addArrayList(dataList);
                updateData();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
        if (isEmpty()) {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
        }
    }

    private void setViewState(int viewState) {
        if (isEmpty()) {
            if (viewState == VIEW_STATE_EMPTY)
                if (type == Constants.SELECT_ITEM_BANK) {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY_BANK, tvEmpty, null, null);
                } else {
                    if (type == Constants.SELECT_ITEM_USER_ACCOUNT) {
                        multiStateView.findViewById(R.id.empty_button).setVisibility(View.GONE);
                    }
                    CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getEmptyMessage());
                }
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    private void updateData() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        etKeyword.setEnabled(true);
        setHeader();
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private String getEmptyMessage() {
        switch (type) {
            case Constants.SELECT_ITEM_USER_ACCOUNT:
                return getString(R.string.empty_user_account);
            case Constants.SELECT_ITEM_BANK:
                if (getIntent().getExtras().getString(Constants.TYPE) != null && getIntent().getExtras().getString(Constants.TYPE).equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP))
                    return getString(R.string.empty_bank_cash_pickup);
            default:
                return getString(R.string.empty_result);
        }
    }

    private void setHeader() {
        if (getIntent().getStringExtra(Constants.DEFAULT_HEADER) != null && getIntent().getStringExtra(Constants.DEFAULT_HEADER).equalsIgnoreCase("1")) {
            etKeyword.setCompoundDrawables(null, null, null, null);
            etKeyword.setEnabled(false);
            etKeyword.setHint(etKeyword.getHint().toString().replace("Type / ", ""));
        }
    }

    public void searchData(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_USER_ACCOUNT && resultCode == Activity.RESULT_OK) {
                selectedUserAccount = data.getParcelableExtra(Constants.OBJECT);
                data.putExtra(Constants.OBJECT, selectedUserAccount);
                setResult(AppCompatActivity.RESULT_OK, data);
                onBackPressed();
            } else if (requestCode == Constants.REQUEST_CODE_ADD_USER_ACCOUNT && resultCode == Activity.RESULT_OK) {
                selectedUserAccount = data.getParcelableExtra(Constants.OBJECT);
                data.putExtra(Constants.OBJECT, selectedUserAccount);
                setResult(AppCompatActivity.RESULT_OK, data);
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_down);
    }


    //Sidduuu
    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        CommonUtils.hideKeyboard(context);
        if (dataItem != null) {
            Intent data = new Intent();
            if (dataItem instanceof BankData)
                data.putExtra(Constants.OBJECT, (BankData) dataItem);
            else if (dataItem instanceof BranchData)
                data.putExtra(Constants.OBJECT, (BranchData) dataItem);
            else if (dataItem instanceof UserAccountData)
                data.putExtra(Constants.OBJECT, (UserAccountData) dataItem);
            else if (dataItem instanceof UserAccountBankData)
                data.putExtra(Constants.OBJECT, (UserAccountBankData) dataItem);
            else if (dataItem instanceof CreditCardBankData)
                data.putExtra(Constants.OBJECT, (CreditCardBankData) dataItem);
            else if (dataItem instanceof AdditionalInfoData.ValuesData) {
                data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (AdditionalInfoData.ValuesData) dataItem);
            } else if (dataItem instanceof TransactionModeData) {
                data.putExtra(Constants.OBJECT, (TransactionModeData) dataItem);
            } else if (dataItem instanceof ServiceTypeData) {
                data.putExtra(Constants.OBJECT, (ServiceTypeData) dataItem);
            } else if (dataItem instanceof AccountTypeData) {
                data.putExtra(Constants.OBJECT, (AccountTypeData) dataItem);
            } else if (dataItem instanceof WuCurrencyData) {
                if (type == WU_SELECT_ITEM_ADDITIONAL_FIELD) {
                    data.putExtra(Constants.ID, getIntent().getIntExtra(Constants.ID, 0));
                    data.putExtra(Constants.OBJECT, (WuCurrencyData) dataItem);
                } else {
                    data.putExtra(Constants.OBJECT, (WuCurrencyData) dataItem);
                }
            } else if (type == SELECT_ITEM_CREDIT_CARD_PAYMENT_DAY) {
                data.putExtra(Constants.OBJECT, (String) dataItem);
            } else if (type == SELECT_ITEM_ADD_BENEFICIARY_TYPE) {
                data.putExtra(Constants.OBJECT, (String) dataItem);
            } else if (type == WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE) {
                data.putExtra(Constants.OBJECT, (String) dataItem);
            } else if (type == WU_SELECT_BENEFICIARY_NAME_TYPE) {
                data.putExtra(Constants.OBJECT, (String) dataItem);
            } else if (type == Constants.SELECT_ITEM_CURRENCY) {
                data.putExtra(Constants.OBJECT, (CurrencyData) dataItem);

            } else if (dataItem instanceof RESULTItem.TXNPURPOSELISTItem) {
                data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (RESULTItem.TXNPURPOSELISTItem) dataItem);

            } else if (dataItem instanceof RESULTItem.SOURCEOFFUNDItem) {
                data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (RESULTItem.SOURCEOFFUNDItem) dataItem);

            } else if (dataItem instanceof RESULTDTO) {
                data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (RESULTDTO) dataItem);

            }else if(type==SELECT_ITEM_ADDITIONAL_FIELD_GENDER) {
               //data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (GenderSelection) dataItem);

            }
            else if(type==SELECT_COUNTRY_TYPE){
               // data.putExtra(Constants.ID, getIntent().getExtras().getInt(Constants.ID, -1));
                data.putExtra(Constants.OBJECT, (BranchLocatorCityData) dataItem);
            }
            else if(type==SELECT_PROFESIONAL_AND_DESIGANATION){
                data.putExtra(Constants.OBJECT,(ProfessionalLoadList)dataItem);
            }

            setResult(AppCompatActivity.RESULT_OK, data);
        } else {
            setResult(AppCompatActivity.RESULT_CANCELED, null);
        }
        onBackPressed();
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_ALL_BANKS:
            case FETCH_BENF_BANK:
            case FETCH_BENF_BANK_BRANCH:
            case FETCH_USER_ACCOUNTS:
            case FETCH_USER_ACCOUNT_BANK_LIST:
            case FETCH_USER_ACCOUNT_BANK_BRANCH_LIST:
            case FETCH_CREDIT_CARD_BANK_LIST:
            case CURRENCY_URL:
            case WU_CURRENCY_URL:
            case WU_FETCH_SOURCE_OF_FUNDS:
            case WU_FETCH_TRANSACTION_PURPOSE:
            case WU_FETCH_SERVICE_TYPES:
            case FETCH_ACCOUNT_TYPE:
            case LOAD_SERVICE_TYPE_LIST:
            case PROFESIONAL_AND_DESIGANATION:
            case FETCH_BRANCH_CITIES:
                if (status == Constants.RESPONSE_SUCCESS) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            switch (sType) {
                                case FETCH_ALL_BANKS:
                                case FETCH_BENF_BANK:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<BankData> bankData = (ArrayList<BankData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BankData>>() {
                                        }.getType());
                                        if (bankData != null && bankData.size() > 0) {
                                            recyclerAdapter.addArrayList(bankData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case FETCH_BENF_BANK_BRANCH:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<BranchData> branchData = (ArrayList<BranchData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BranchData>>() {
                                        }.getType());
                                        if (branchData != null && branchData.size() > 0) {
                                            recyclerAdapter.addArrayList(branchData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
//-------------------------------------New Bank-----------------------------------------------------
                                case FETCH_USER_ACCOUNTS:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<UserAccountData> userAccountData = (ArrayList<UserAccountData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<UserAccountData>>() {
                                        }.getType());
                                        if (userAccountData != null && userAccountData.size() > 0) {
                                            recyclerAdapter.addArrayList(userAccountData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
//--------------------------------------------------------------------------------------------------
                                case FETCH_USER_ACCOUNT_BANK_LIST:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<UserAccountBankData> bankData = (ArrayList<UserAccountBankData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<UserAccountBankData>>() {
                                        }.getType());
                                        if (bankData != null && bankData.size() > 0) {
                                            recyclerAdapter.addArrayList(bankData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case FETCH_CREDIT_CARD_BANK_LIST:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<CreditCardBankData> bankData = (ArrayList<CreditCardBankData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CreditCardBankData>>() {
                                        }.getType());
                                        if (bankData != null && bankData.size() > 0) {
                                            recyclerAdapter.addArrayList(bankData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case FETCH_ACCOUNT_TYPE:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<AccountTypeData> accountTypeData = (ArrayList<AccountTypeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AccountTypeData>>() {
                                        }.getType());
                                        if (accountTypeData != null && accountTypeData.size() > 0) {
                                            recyclerAdapter.addArrayList(accountTypeData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case CURRENCY_URL:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<CurrencyData> currencyData = (ArrayList<CurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CurrencyData>>() {
                                        }.getType());
                                        if (currencyData != null && currencyData.size() > 0) {
                                            recyclerAdapter.addArrayList(currencyData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;

                                 //Sidduuu
                                case WU_CURRENCY_URL:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                                        }.getType());
                                        if (currencyData != null && currencyData.size() > 0) {
                                            recyclerAdapter.addArrayList(currencyData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case WU_FETCH_SOURCE_OF_FUNDS:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                                        }.getType());
                                        if (currencyData != null && currencyData.size() > 0) {
                                            recyclerAdapter.addArrayList(currencyData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case WU_FETCH_TRANSACTION_PURPOSE:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                                        }.getType());
                                        if (currencyData != null && currencyData.size() > 0) {
                                            recyclerAdapter.addArrayList(currencyData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case WU_FETCH_SERVICE_TYPES:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                                        }.getType());
                                        if (currencyData != null && currencyData.size() > 0) {
                                            if (getIntent().getIntExtra(Constants.ID, 0) == Constants.WU_SELECT_ITEM_CITY_STATE || getIntent().getIntExtra(Constants.ID, 0) == Constants.WU_SELECT_ITEM_CITY) {
                                                recyclerAdapter.addArrayList(currencyData);
                                                updateData();
                                                return;
                                            } else {
                                                recyclerAdapter.addArrayList(currencyData);
                                                updateData();
                                                return;
                                            }
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case LOAD_SERVICE_TYPE_LIST:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<RESULTDTO> branchData = (ArrayList<RESULTDTO>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<RESULTDTO>>() {
                                        }.getType());
                                        if (branchData != null && branchData.size() > 0) {
                                            recyclerAdapter.addArrayList(branchData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    setViewState(VIEW_STATE_EMPTY);
                                    break;
                                case PROFESIONAL_AND_DESIGANATION:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<ProfessionalLoadList> branchData = (ArrayList<ProfessionalLoadList>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ProfessionalLoadList>>() {
                                        }.getType());
                                        if (branchData != null && branchData.size() > 0) {
                                            recyclerAdapter.addArrayList(branchData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    break;
                                case FETCH_BRANCH_CITIES:
                                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                        ArrayList<BranchLocatorCityData> branchLocatorCityData = (ArrayList<BranchLocatorCityData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BranchLocatorCityData>>() {
                                        }.getType());
                                        if (branchLocatorCityData != null && branchLocatorCityData.size() > 0) {
                                            recyclerAdapter.addArrayList(branchLocatorCityData);
                                            updateData();
                                            return;
                                        }
                                    }
                                    break;


                            }
                        } else {
                            setViewState(VIEW_STATE_EMPTY);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_WRONG);
                    } finally {
                        onItemsLoadComplete();
                    }
                } else if (status == Constants.RESPONSE_TIMEOUT_ERR) {
                    setViewState(VIEW_STATE_ERROR);
                } else {
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
        }
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


}