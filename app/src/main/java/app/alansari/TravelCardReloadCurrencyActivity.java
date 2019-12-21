package app.alansari;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.adapters.TravelCardCurrencyAdapter;
import app.alansari.adapters.TravelCardReloadDynamicListAdapter;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;
import app.alansari.models.getCharges.ResultItem;
import app.alansari.models.travalcardvalidateflag.TravelCardAdapterItem;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;
import app.alansari.models.travelcardAdditionInfo.TravelCardIntent;
import app.alansari.models.travelcardreloadcurrency.TravelCardCurrencyModel;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_WC_ADDITIONAL_INFO;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_WIRE_CARD_BALANCE;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.GET_CREATES_SEND_CHARGES;


public class TravelCardReloadCurrencyActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult {

    int topReading, BottomReading = 0;
    boolean clickCheck = false;
    private Context context;
    private TravelCardInfo travelCardInfo;
    private ArrayList<TravelCardFlag> travelCardFlags, originalFlagList;
    private ArrayList<TravelCardAdapterItem> travelCardFlags2;
    private TextView cardNumber;
    private View headerValueLayout, headerInstantLayout;
    private TextView tvValueTitle, tvInstantTitle;
    private RecyclerView recyclerViewValue, recyclerViewInstant;
    private ImageView headerImageViewValue, headerImageViewInstant;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private TravelCardCurrencyAdapter adapter;
    private TravelCardReloadDynamicListAdapter dynamicListAdapter;
    private Button btnSend;
    public boolean isCalculatingCurrency = false;
    private TravelCardFlag selectedCurrency = null;
    private TextView tvTotalToPay, tvTotalToPayCurrencyCode, tvFeeAED, tvVat, tvDiscount, tvRoundOff;
    private String totalToPay, charge, rate;
    private String vat;
    private String rounding;
    private String discount;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private ArrayList<ResultItem> travelCardResultModels;
    private NestedScrollView scrollView;
    private String message;
    private String vatCharges, vatDiscount;
    private TextView tvEmpty;
    private RelativeLayout relLayCard;
    private int clickedPosition = -1;
    ArrayList<TravelCardIntent> travelCardIntents;
    private String diagCharges, diagTotalPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_card_reload_currency);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Travel Card");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Reload");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);

        init();
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        boolean hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        }
        travelCardInfo = getIntent().getExtras().getParcelable(Constants.OBJECT);
        cardNumber.setText(travelCardInfo.getCARDNUMBER());
        originalFlagList = new ArrayList<>();
        originalFlagList = getIntent().getExtras().getParcelableArrayList(Constants.PROFILE_UPDATE_FLAG);
        travelCardFlags = new ArrayList<>();
        travelCardFlags.addAll(originalFlagList);
    }


    public void init() {
        travelCardFlags2 = new ArrayList<>();
        travelCardFlags2.add(new TravelCardAdapterItem());
        travelCardIntents = new ArrayList<>();

        cardNumber = findViewById(R.id.card_num);
        headerValueLayout = findViewById(R.id.header_1);
        headerInstantLayout = findViewById(R.id.header_2);

        headerValueLayout.setOnClickListener(this);
        headerInstantLayout.setOnClickListener(this);


        tvEmpty = ((TextView) findViewById(R.id.tv_empty));
        relLayCard = (RelativeLayout) findViewById(R.id.rel_lay_card);


        btnSend = (Button) findViewById(app.alansari.R.id.send_btn);
        btnSend.setOnClickListener(this);

        tvValueTitle = (TextView) headerValueLayout.findViewById(R.id.title);
        tvValueTitle.setText("View Balance");

        tvInstantTitle = (TextView) headerInstantLayout.findViewById(R.id.title);
        tvInstantTitle.setText("Top up Travel Card");

        headerImageViewValue = (ImageView) headerValueLayout.findViewById(R.id.account_arrow_image);
        headerImageViewInstant = (ImageView) headerInstantLayout.findViewById(R.id.account_arrow_image);
        //headerImageViewInstant.setVisibility(View.GONE);
        //headerImageViewInstant.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));

        tvFeeAED = (TextView) findViewById(app.alansari.R.id.fee_aed);
        tvVat = (TextView) findViewById(app.alansari.R.id.vat_label);
        tvDiscount = (TextView) findViewById(app.alansari.R.id.discount_label);
        tvRoundOff = (TextView) findViewById(app.alansari.R.id.roundoff_label);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        recyclerViewValue = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewValue.setLayoutManager(linearLayoutManager);
        recyclerViewValue.setHasFixedSize(true);
        adapter = new TravelCardCurrencyAdapter(this, new ArrayList<TravelCardCurrencyModel>());
        recyclerViewValue.setAdapter(adapter);


        recyclerViewValue.addOnScrollListener(new CenterScrollListener());
        recyclerViewInstant = (RecyclerView) findViewById(R.id.recyclerView_2);
        recyclerViewInstant.setHasFixedSize(true);
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewInstant.setLayoutManager(linearLayoutManager2);
        refreshRecyclerView();
        recyclerViewInstant.addOnScrollListener(new CenterScrollListener());

        recyclerViewValue.setNestedScrollingEnabled(false);
        recyclerViewInstant.setNestedScrollingEnabled(false);


        scrollView = findViewById(R.id.nested_scrollview);

        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);

        relLayCard.setVisibility(View.GONE);

    }

    public void calculateCurrency(int currentDirection, EditText editText) {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JSONObject requestParams;
                String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                if (currentDirection == 1)
                    requestParams = fetchCreateCharges(currentDirection, editText);
                else
                    // requestParams = fetchCreateCharges2(editText);
                    requestParams = fetchCreateCharges(currentDirection, editText);


                isCalculatingCurrency = true;
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(requestParams, Constants.GET_CREATES_SEND_CHARGES_URL, GET_CREATES_SEND_CHARGES, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(GET_CREATES_SEND_CHARGES.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, GET_CREATES_SEND_CHARGES.toString());
                //      CommonUtils.showLoading(context, getString(R.string.please_wait), GET_CREATES_SEND_CHARGES.toString(), false);

            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject fetchCreateCharges(int currentDirection, EditText editText) {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, travelCardInfo.getWCPKID());
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);


            JSONArray jsonArray = new JSONArray();
            if (travelCardFlags2 != null) {
                for (int i = 0; i < travelCardFlags2.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        if (travelCardFlags2.get(i).getDirection() == 1) {
                            jsonObjectField.put(Constants.FROM_CCY, travelCardFlags2.get(i).getTravelCardFlag().getCCYCODE());
                            jsonObjectField.put(Constants.TO_CCY, "91");
                            //from
                            jsonObjectField.put(Constants.FCY_AMOUNT, travelCardFlags2.get(i).getFromCurrency());
                            if (travelCardFlags2.get(i).getToCurrency() != null) {
                                jsonObjectField.put(Constants.AED_AMOUNT, travelCardFlags2.get(i).getToCurrency());
                            } else {
                                jsonObjectField.put(Constants.AED_AMOUNT, "0.00");
                            }
                        } else if (travelCardFlags2.get(i).getDirection() == 2) {
                            jsonObjectField.put(Constants.FROM_CCY, "91");
                            jsonObjectField.put(Constants.TO_CCY, travelCardFlags2.get(i).getTravelCardFlag().getCCYCODE());
                            //to
                            if (travelCardFlags2.get(i).getFromCurrency() != null) {
                                jsonObjectField.put(Constants.FCY_AMOUNT, travelCardFlags2.get(i).getFromCurrency());
                            } else {
                                jsonObjectField.put(Constants.FCY_AMOUNT, "0.00");
                            }

                            jsonObjectField.put(Constants.AED_AMOUNT, travelCardFlags2.get(i).getToCurrency());
                        }
                        jsonObjectField.put(Constants.CCY_CODE, travelCardFlags2.get(i).getTravelCardFlag().getCCYCODE());
                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, travelCardInfo.getAGENTACCOUNTNUM());
                        jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());


                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            jsonObject.put(Constants.WC_RATES_AND_CHARGES_REQUEST, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;

    }

    private JSONObject fetchCreateCharges2(EditText editText) {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);


            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, travelCardInfo.getWCPKID());
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);


            JSONArray jsonArray = new JSONArray();
            if (travelCardFlags2 != null) {
                for (int i = 0; i < travelCardFlags2.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();

                        jsonObjectField.put(Constants.FROM_CCY, "91");
                        jsonObjectField.put(Constants.TO_CCY, travelCardFlags2.get(i).getTravelCardFlag().getCCYCODE());
                        jsonObjectField.put(Constants.FCY_AMOUNT, "0.00");
                        jsonObjectField.put(Constants.AED_AMOUNT, travelCardFlags2.get(i).getToCurrency());
                        jsonObjectField.put(Constants.CCY_CODE, travelCardFlags2.get(i).getTravelCardFlag().getCCYCODE());

                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, travelCardInfo.getAGENTACCOUNTNUM());
                        jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());


                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            jsonObject.put(Constants.WC_RATES_AND_CHARGES_REQUEST, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;

    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        super.onResponse(status, response, sType);
        switch (sType) {
            case FETCH_WIRE_CARD_BALANCE:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        message = response.getString(Constants.MESSAGE);
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                tvEmpty.setVisibility(View.GONE);
                                ArrayList<TravelCardCurrencyModel> travelCardCurrencyModels = (ArrayList<TravelCardCurrencyModel>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TravelCardCurrencyModel>>() {
                                }.getType());
                                if (travelCardCurrencyModels.size() > 0 && travelCardCurrencyModels != null) {
                                    adapter.addArrayList(travelCardCurrencyModels);
                                    return;
                                }
                            }
                            //setViewState(VIEW_STATE_EMPTY, "");
                            tvEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText(message);
                        } else {
                            //setViewState(VIEW_STATE_WRONG, "");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        tvEmpty.setVisibility(View.VISIBLE);
                        tvEmpty.setText(message);
                        //setViewState(VIEW_STATE_WRONG, message);
                    } finally {
                    }
                } else {
                    // setViewState(VIEW_STATE_WRONG, "");
                }
                break;
            case GET_CREATES_SEND_CHARGES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray("result") != null && response.getJSONArray("result").length() > 0) {
                                travelCardResultModels = (ArrayList<ResultItem>) new Gson().fromJson(response.getJSONArray("result").toString(), new TypeToken<ArrayList<ResultItem>>() {
                                }.getType());


                                travelCardFlags2.get(clickedPosition).setResultItem(travelCardResultModels.get(clickedPosition));
                                if (travelCardFlags2.get(travelCardFlags2.size() - 1).getResultItem() != null) {
                                    travelCardFlags2.add(new TravelCardAdapterItem());
                                }
                                refreshRecyclerView();
                                travelCardFlags2.get(clickedPosition).setShowSingleLine(true);
                                Double totalAmountDouble = Double.valueOf(response.getString(Constants.TOTAL_AMOUNT));
                                Double rateDouble = Double.valueOf(String.valueOf(travelCardResultModels.get(0).getRATE()));

                                Double chargeDouble = Double.valueOf(response.getString(Constants.CHARGES_ON_US));
                                vatCharges = (response.getString(Constants.VAT_CHARGES));
                                vatDiscount = (response.getString(Constants.VAT_DISCOUNT));
                                Double roundOff = Double.valueOf(response.getString(Constants.VAT_ROUNDINGOFF));
//Credit card---------------------------------------------------------------------------------------
                                diagCharges = response.getString(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
                                diagTotalPay = response.getString(Constants.TOTAL_AMOUNT_PP);
//--------------------------------------------------------------------------------------------------

                                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                                nf.setMinimumFractionDigits(10);
                                rate = String.valueOf(nf.format(rateDouble));
                                charge = String.valueOf(chargeDouble);
                                totalToPay = String.valueOf(totalAmountDouble);
                                //setRateData(rate);
                                onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);
                                setVat(response);
                                setSendBtnState(true);
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.smoothScrollTo(0, scrollView.FOCUS_DOWN);
                                    }
                                });
                            } else {
                                onAPIError();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                            //Toast.makeText(this, "Sidduuu111111111111111111111111", Toast.LENGTH_SHORT).show();

                            onErrorInCalculation();
                            removeItem(clickedPosition);
                            if (travelCardFlags2.get(travelCardFlags2.size() - 1).getResultItem() != null) {
                                travelCardFlags2.add(new TravelCardAdapterItem());
                            }
                            refreshRecyclerView();

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
            case FETCH_WC_ADDITIONAL_INFO:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            additionInfoPage();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.STATUS));

                            //Toast.makeText(this, "Sidduuu2222222222222222222222222", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void refreshRecyclerView() {
        if (dynamicListAdapter == null) {
            dynamicListAdapter = new TravelCardReloadDynamicListAdapter(this, travelCardFlags2);
            dynamicListAdapter.setShowCurrencyPosition(0);
            recyclerViewInstant.setAdapter(dynamicListAdapter);
        } else {
            dynamicListAdapter.setShowCurrencyPosition(clickedPosition);
            dynamicListAdapter.notifyDataSetChanged();
        }

    }


    private void fetchWireCardBalance() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {

            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWireCardBalance(userPkId, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), travelCardInfo.getWCPKID(), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_WIRE_CARD_BALANCE_URL, FETCH_WIRE_CARD_BALANCE, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_WIRE_CARD_BALANCE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_WIRE_CARD_BALANCE.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), FETCH_WIRE_CARD_BALANCE.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
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

    public void setSendBtnState(boolean state) {

        btnSend.setEnabled(state);
    }

    private void onSuccessInCalculation() {
        isCalculatingCurrency = false;
        // ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private void onAPIError() {
        if (context != null)
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        onErrorInCalculation();
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        //((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private Boolean isEmpty() {
        return adapter.getItemCount() == 0;
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

    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case R.id.header_1:
                if (topReading == 0) {
                    fetchWireCardBalance();
                    headerImageViewValue.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_up_arrow));
                    relLayCard.setVisibility(View.VISIBLE);
                    topReading = 1;
                    BottomReading = 0;
                    clickCheck = true;
                } else if (topReading == 1) {
                    topReading = 0;
                    BottomReading = 1;
                    headerImageViewValue.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_down_arrow));
                    relLayCard.setVisibility(View.GONE);
                    clickCheck = false;

                }


                break;
            case R.id.header_2:
                if (topReading == 0) {
                    refreshRecyclerView();
                    recyclerViewInstant.setVisibility(View.VISIBLE);
                    headerImageViewInstant.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_up_arrow));
                    topReading = 1;
                    BottomReading = 0;
                    clickCheck = true;

                } else if (topReading == 1) {
                    recyclerViewInstant.setVisibility(View.GONE);
                    headerImageViewInstant.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_down_arrow));
                    //refreshRecyclerView();
                    topReading = 0;
                    BottomReading = 1;
                    clickCheck = false;
                }



                break;

            case R.id.select_currency:
                //openTravelCardCurrencyList();
                break;

            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchWireCardBalance();
                break;

            case app.alansari.R.id.send_btn:
                fetchAdditionalInfo();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_CURRENCY) {
                if (resultCode == RESULT_OK) {

                    Log.e("fsvjbjhvbsfj", "" + data.getParcelableExtra(Constants.OBJECT));
                    selectedCurrency = data.getParcelableExtra(Constants.OBJECT);
                    travelCardFlags2.get(clickedPosition).setTravelCardFlag(selectedCurrency);
                    refreshRecyclerView();

//                    regCurrencyLayout.setVisibility(View.VISIBLE);
//                    tvAdd.setVisibility(View.GONE);
//                    tvSelectCurrency.setText(selectedCurrency.getCCYDESC());
//                    CommonUtils.setCountryFlagImage(context, ivFromFlag, selectedCurrency.getFLAG());
//                    ivToFlag.setImageResource(R.drawable.svg_flag_aed);
//                    tvFromCode.setText(selectedCurrency.getISOCCYCODE());
//                    tvToCode.setText("AED");
//                    ivFromFlag.setVisibility(View.VISIBLE);
//                    ivToFlag.setVisibility(View.VISIBLE);
//                    tvToCode.setVisibility(View.VISIBLE);
//                    tvFromCode.setVisibility(View.VISIBLE);
//                    tvDelete.setVisibility(View.VISIBLE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetAmount();
    }

    public void openTravelCardCurrencyList(int position) {
        clickedPosition = position;
        Intent intent = new Intent(context, SelectCurrencyFlagTravelCardActivity.class);
        travelCardFlags.clear();

        LinkedHashMap<String, TravelCardFlag> temp = new LinkedHashMap<>();

        for (int i = 0; i < originalFlagList.size(); i++) {
            temp.put(originalFlagList.get(i).getISOCCYCODE(), originalFlagList.get(i));
        }

        for (int j = 0; j < travelCardFlags2.size(); j++) {
            if (travelCardFlags2.get(j).getTravelCardFlag() != null) {
                Log.e("Flag", "Flag : " + travelCardFlags2.get(j).getTravelCardFlag().getISOCCYCODE());
                temp.remove(travelCardFlags2.get(j).getTravelCardFlag().getISOCCYCODE());
            }
        }
        travelCardFlags.addAll(temp.values());
        intent.putExtra(Constants.OBJECT, travelCardFlags);
        startActivityForResult(intent, Constants.SELECT_ITEM_CURRENCY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);


    }

    private void resetAmount() {
        isCalculatingCurrency = true;
        onSuccessInCalculation("0.00", "0.00");
        // hideVat();
        setSendBtnState(false);
        isCalculatingCurrency = false;
    }

    public void hideVat() {
        tvVat.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
        tvRoundOff.setVisibility(View.GONE);

        tvVat.setText("");
        tvDiscount.setText("");
        tvRoundOff.setText("");
    }


    private void additionInfoPage() {
        Log.e("TravekCradInfo", "" + travelCardInfo);
        Log.e("TravelCardFlag2", "" + travelCardFlags2);
        Log.e("TravelCardModels", "" + travelCardResultModels);
        Log.e("TotalToPay", "" + totalToPay);


        Intent intent = new Intent(context, AdditionalInfoTravelCardActivity.class);
        intent.putExtra(Constants.OBJECT, travelCardInfo);
        intent.putExtra(Constants.PROFILE_UPDATE_FLAG, travelCardFlags2);
        intent.putExtra(Constants.RESPONSE_INTENT, travelCardResultModels);
        intent.putExtra("to", "");
        intent.putExtra("from", "");
        intent.putExtra("toFlag", selectedCurrency.getISOCCYCODE());
        intent.putExtra("fromFlag", "AED");
        intent.putExtra("totalToPay", totalToPay);
        intent.putExtra(Constants.VAT_CHARGES, vatCharges);
        intent.putExtra(Constants.VAT_DISCOUNT, vatDiscount);
        intent.putExtra(Constants.CHARGES, charge);

        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, diagCharges);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, diagTotalPay);
        startActivity(intent);

    }

    void fetchAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JSONObject requestParams;
            requestParams = fetchCreateCharges3();
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(requestParams, Constants.FETCH_WC_ADDITIONAL_INFO_URL, FETCH_WC_ADDITIONAL_INFO, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_WC_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_WC_ADDITIONAL_INFO.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), FETCH_WC_ADDITIONAL_INFO.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject fetchCreateCharges3() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, travelCardInfo.getWCPKID());
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            JSONArray jsonArray = new JSONArray();
            if (travelCardResultModels != null) {
                for (int i = 0; i < travelCardResultModels.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.AED_VALUE, travelCardResultModels.get(i).getAEDAMOUNT());
                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, travelCardResultModels.get(i).getWCACCOUNTNUMBER());
                        jsonObjectField.put(Constants.RATE, travelCardResultModels.get(i).getRATE());
                        jsonObjectField.put(Constants.FCY_VALUE, travelCardResultModels.get(i).getFCYAMOUNT());
                        jsonObjectField.put(Constants.CCY_CODE, travelCardResultModels.get(i).getCCYCODE());
                        jsonObjectField.put(Constants.USER_PK_ID, CommonUtils.getUserId());

                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            jsonObject.put(Constants.WC_WALLET_DETAILS_REQUEST, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;

    }


    public void removeItem(int position) {
        //clickedPosition = position;
        travelCardFlags2.remove(position);
        //travelCardFlags2.remove()
        // travelCardFlags2.remove(clickedPosition).setResultItem(travelCardResultModels.remove(clickedPosition));
        //Log.e("cdbchjbsdchjb",""+travelCardFlags2.size());
        //resetAmount();

//        if (travelCardFlags2 == null || travelCardFlags2.size() == 0) {
//            travelCardFlags2.add(new TravelCardAdapterItem());
//            setSendBtnState(false);
//        } else if (travelCardFlags2.size() > 0 && travelCardFlags2.get(travelCardFlags2.size() - 1).getResultItem() != null) {
//            travelCardFlags2.add(new TravelCardAdapterItem());
//            setSendBtnState(true);
//        }

        //calculate price.
        double totalToPayDouble = 0.0d;
        for (TravelCardAdapterItem travelCardAdapterItem : travelCardFlags2){
            if(travelCardAdapterItem.getToCurrency() != null){
                totalToPayDouble+=Double.valueOf(travelCardAdapterItem.getToCurrency());
            }
        }

        totalToPay = totalToPayDouble > 0 ? String.format("%.2f", totalToPayDouble) : "0.00";
        onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);
        refreshRecyclerView();

        setSendBtnState(totalToPayDouble > 0 ? true : false);

        for (int i = 0; i < travelCardFlags2.size(); i++) {
            Log.e("fjfjgbfjhg", "" + travelCardFlags2.get(i).getTravelCardFlag());
            Log.e("cbsdjcbsjbvjs", "" + travelCardFlags2.get(i).getResultItem());
        }



        //Delete Api
        //deleteAndUpdate();


    }

    private void deleteAndUpdate() {

    }
}

