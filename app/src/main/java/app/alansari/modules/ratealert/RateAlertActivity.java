package app.alansari.modules.ratealert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Random;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CountryData;
import app.alansari.models.RateAlertModel;
import app.alansari.modules.ratealert.adapters.RateAlertRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_RATE_ALERT;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CURRENT_RATE;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_SAVED_RATE_ALERTS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SAVE_RATE_ALERT;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 21 February, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class RateAlertActivity extends NavigationBaseActivity implements View.OnClickListener, CustomClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private Gson gson;
    private Context context;
    private String deletedRateAlertId;
    private CountryData selectedCountry;
    private CountryData toCountry;

    //View
    private Button btnSave;
    private EditText etAmount;
    private AppCompatImageView ivFlag, ivToFlag, ivFromFlag, ivSwap;
    private TextView tvCountryName, tvCurrentRate, tvAlertWhen, tvToCode, tvFromCode;
    private CountryData.CurrencyData selectedCurrencyData;

    private RecyclerView recyclerView;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private LinearLayoutManager linearLayoutManager;
    private RateAlertRecyclerAdapter recyclerAdapter;
    private String userPkId, sessionTime;


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
        setContentView(app.alansari.R.layout.rate_alert_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Rate");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Alert");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        gson = new Gson();
        init();
        setInitialData();
        fetchSavedRateAlerts();
    }

    private void init() {
        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        ivToFlag = (AppCompatImageView) findViewById(app.alansari.R.id.to_flag);
        ivFromFlag = (AppCompatImageView) findViewById(app.alansari.R.id.from_flag);
        ivSwap = (AppCompatImageView) findViewById(app.alansari.R.id.swap);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name);
        tvToCode = (TextView) findViewById(app.alansari.R.id.to_code);
        tvFromCode = (TextView) findViewById(app.alansari.R.id.from_code);

        etAmount = (EditText) findViewById(app.alansari.R.id.amount);
        tvCurrentRate = (TextView) findViewById(app.alansari.R.id.current_rate);
        tvAlertWhen = (TextView) findViewById(app.alansari.R.id.alert_when_label);
        btnSave = (Button) findViewById(app.alansari.R.id.dialog_btn);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);

        ivFlag.setOnClickListener(this);
        ivSwap.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.click_to_change).setOnClickListener(this);


        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isValidEditTextValue(etAmount)) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
                if (Validation.isValidString(s.toString()))
                    CommonUtils.addCommaAfterThousand(etAmount, this, s.toString());
            }
        });

        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnSave.setVisibility(View.GONE);
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnSave.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }
        );


        recyclerAdapter = new RateAlertRecyclerAdapter(context, new ArrayList<RateAlertModel>(), this);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setInitialData() {
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        toCountry = SharedPreferenceManger.loadSelectedCountryData();
        if (selectedCountry != null && !selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL"))
            setCountryData();
        else
            openCountryList();
    }

    private void fetchSavedRateAlerts() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchSavedRateAlerts((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_SAVED_RATE_ALERTS_URL, FETCH_SAVED_RATE_ALERTS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_SAVED_RATE_ALERTS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_SAVED_RATE_ALERTS.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    private void setCountryData() {
        cancelPendingRequests();
        if (selectedCountry != null) {
            if (selectedCountry.getCurrencyData() != null) {
                CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
                tvCountryName.setText(selectedCountry.getLatinName() + "\nclick to change country");
                tvToCode.setText(CommonUtils.getDefaultCurrencyData(toCountry).getName());
                tvFromCode.setText("AED");
                ivFromFlag.setImageResource(R.drawable.svg_flag_aed);
                CommonUtils.setCountryFlagImage(context, ivToFlag, toCountry.getFlag());
                selectedCurrencyData = CommonUtils.getDefaultCurrencyData(selectedCountry);
                onCountryChanged();
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_SHORT).show();
            }
        } else {
            openCountryList();
        }
    }

    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
        intent.putExtra("hideFirstItem", true);
        startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private void setViewState(int viewState) {
        if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, (viewState == VIEW_STATE_EMPTY ? "You don't have any saved Rate Alert" : null));
        else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    private void cancelPendingRequests() {
        AppController.getInstance().cancelPendingRequests(CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_CALCULATOR.toString());
    }

    private void onCountryChanged() {
        tvCurrentRate.setText(null);
        if (selectedCurrencyData != null) {
            tvAlertWhen.setText("Alert when AED = " + selectedCurrencyData.getName());
            fetchCurrentRate();
        } else {
            tvAlertWhen.setText("Alert when AED");
            Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country_try_again), Toast.LENGTH_LONG).show();
        }
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private void fetchCurrentRate() {
        if (selectedCurrencyData != null && selectedCurrencyData.getCurrencyCode() != null) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCurrentRate(selectedCurrencyData.getCurrencyCode(), "91", userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CURRENT_RATE_URL, FETCH_CURRENT_RATE, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_CURRENT_RATE.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CURRENT_RATE.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_CURRENT_RATE.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country_try_again), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        if (dataItem != null && dataItem instanceof RateAlertModel) {
            deletedRateAlertId = ((RateAlertModel) dataItem).getId();
            deleteRateAlert();
        } else {
            super.itemClicked(view, position, dataItem);
        }
    }

    private void deleteRateAlert() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteRateAlert(deletedRateAlertId, userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.DELETE_RATE_ALERT_URL, DELETE_RATE_ALERT, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(DELETE_RATE_ALERT.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_RATE_ALERT.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), DELETE_RATE_ALERT.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_CURRENT_RATE:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (Validation.isValidRate(response.getString(Constants.RATE))) {
//                                setRateData(CommonUtils.getDecimalFormattedString(Double.valueOf(response.getString(Constants.RATE))));
                                setRateData(response.getString(Constants.RATE));
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
            case FETCH_SAVED_RATE_ALERTS:
                if (status == Constants.RESPONSE_DUMMY) {
                    Toast.makeText(context, getString(app.alansari.R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        response = new JSONObject(getResponse());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<RateAlertModel> rateAlertData = (ArrayList<RateAlertModel>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<RateAlertModel>>() {
                                }.getType());
                                if (rateAlertData != null && rateAlertData.size() > 0) {
                                    recyclerAdapter.addArrayList(rateAlertData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    return;
                                }
                            }
                            setViewState(VIEW_STATE_EMPTY);
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }

                    } else {
                        setViewState(VIEW_STATE_WRONG);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
            case SAVE_RATE_ALERT:
                CommonUtils.hideLoading();
       /*         if (status == Constants.RESPONSE_DUMMY) {
                    Toast.makeText(context, getString(R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        response = new JSONObject(getResponseOnSave());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<RateAlertModel> rateAlertData = (ArrayList<RateAlertModel>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<RateAlertModel>>() {
                                }.getType());
                                if (rateAlertData != null && rateAlertData.size() > 0) {
                                    recyclerAdapter.addItemAt(rateAlertData.get(0), 0);
                                    linearLayoutManager.scrollToPosition(0);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    etAmount.setText(null);
                                    return;
                                }
                            }
                            somethingWentWrongToast();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            somethingWentWrongToast();
                        }

                    } else {
                        somethingWentWrongToast();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
            case DELETE_RATE_ALERT:
                CommonUtils.hideLoading();
              /*  if (status == Constants.RESPONSE_DUMMY) {
                    Toast.makeText(context, getString(R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        response = new JSONObject(getResponse());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
                                if (recyclerAdapter.getItemAt(i).getId().equalsIgnoreCase(deletedRateAlertId)) {
                                    recyclerAdapter.delete(i);
                                    if (isEmpty()) {
                                        setViewState(VIEW_STATE_EMPTY);
                                    }
                                    break;
                                }
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            somethingWentWrongToast();
                        }
                    } else {
                        somethingWentWrongToast();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
        }
    }

    private void setRateData(String rate) {
        if (selectedCountry != null && selectedCurrencyData != null && rate != null) {
            //TODO Use String
            tvCurrentRate.setVisibility(View.VISIBLE);
            try {
                tvCurrentRate.setText("Current Rate 1 " + tvFromCode.getText() + " = " + tvToCode.getText() + " " + rate);
            } catch (Exception ex) {
                tvCurrentRate.setVisibility(View.GONE);
            }
        } else {
            tvCurrentRate.setVisibility(View.GONE);
        }
    }

    private void somethingWentWrongToast() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    private String getResponse() {
        return "{\n" +
                "  \"RESULT\": [\n" +
                "    {\n" +
                "      \"RATE_ALERT_PK_ID\": \"12\",\n" +
                "      \"SOURCE_CCY_CODE\": \"12\",\n" +
                "      \"SOURCE_CCY_NAME\": \"AED\",\n" +
                "      \"SOURCE_AMOUNT\": \"1\",\n" +
                "      \"DESC_CCY_CODE\": \"65\",\n" +
                "      \"DESC_CCY_NAME\": \"INR\",\n" +
                "      \"AMOUNT\": \"456\",\n" +
                "      \"STATUS\": \"A\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"RATE_ALERT_PK_ID\": \"13\",\n" +
                "      \"SOURCE_CCY_CODE\": \"12\",\n" +
                "      \"SOURCE_CCY_NAME\": \"AED\",\n" +
                "      \"SOURCE_AMOUNT\": \"1\",\n" +
                "      \"DESC_CCY_CODE\": \"92\",\n" +
                "      \"DESC_CCY_NAME\": \"USD\",\n" +
                "      \"AMOUNT\": \"15\",\n" +
                "      \"STATUS\": \"A\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"RATE_ALERT_PK_ID\": \"14\",\n" +
                "      \"SOURCE_CCY_CODE\": \"12\",\n" +
                "      \"SOURCE_CCY_NAME\": \"AED\",\n" +
                "      \"SOURCE_AMOUNT\": \"1\",\n" +
                "      \"DESC_CCY_CODE\": \"32\",\n" +
                "      \"DESC_CCY_NAME\": \"CCA\",\n" +
                "      \"AMOUNT\": \"56\",\n" +
                "      \"STATUS\": \"A\"\n" +
                "    },{\n" +
                "      \"RATE_ALERT_PK_ID\": \"15\",\n" +
                "      \"SOURCE_CCY_CODE\": \"12\",\n" +
                "      \"SOURCE_CCY_NAME\": \"AED\",\n" +
                "      \"SOURCE_AMOUNT\": \"1\",\n" +
                "      \"DESC_CCY_CODE\": \"42\",\n" +
                "      \"DESC_CCY_NAME\": \"LAK\",\n" +
                "      \"AMOUNT\": \"102\",\n" +
                "      \"STATUS\": \"A\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"USER_STATUS\": null,\n" +
                "  \"OTP\": null,\n" +
                "  \"AMOUNT\": null,\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"256\",\n" +
                "  \"MESSAGE\": \"SUCCESS\"\n" +
                "}";
    }

    private CountryData.CurrencyData getCurrencyData(CountryData countryData) {
        if (countryData.getCurrencyData() != null) {
            for (int i = 0; i < countryData.getCurrencyData().size(); i++) {
                if (countryData.getCurrencyData().get(i).getDefaultStatus().equalsIgnoreCase("1")) {
                    return countryData.getCurrencyData().get(i);
                }
            }
        }
        return null;
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
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchSavedRateAlerts();
                break;
            case app.alansari.R.id.dialog_btn:
                if (Validation.isValidEditTextValue(etAmount)) {
                    saveRateAlert();
                }
                break;
            case app.alansari.R.id.swap:
                if (CommonUtils.getDefaultCurrencyData(toCountry).getName().equalsIgnoreCase(tvToCode.getText().toString())) {
                    tvFromCode.setText(CommonUtils.getDefaultCurrencyData(toCountry).getName());
                    CommonUtils.setCountryFlagImage(context, ivFromFlag, toCountry.getFlag());
                    tvToCode.setText("AED");
                    ivToFlag.setImageResource(R.drawable.svg_flag_aed);
                    tvAlertWhen.setText("Alert when " + selectedCurrencyData.getName() + " = AED");
                    fetchSwapCurrentRate();
                } else {
                    tvToCode.setText(CommonUtils.getDefaultCurrencyData(toCountry).getName());
                    CommonUtils.setCountryFlagImage(context, ivToFlag, toCountry.getFlag());
                    tvFromCode.setText("AED");
                    ivFromFlag.setImageResource(R.drawable.svg_flag_aed);
                    tvAlertWhen.setText("Alert when AED = " + selectedCurrencyData.getName());
                    fetchCurrentRate();
                }
                break;
        }
    }

    private void saveRateAlert() {
        if (selectedCurrencyData != null && selectedCurrencyData.getCurrencyCode() != null) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                if (tvToCode.getText().toString().equalsIgnoreCase(CommonUtils.getDefaultCurrencyData(toCountry).getName())) {
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().saveRateAlert((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), selectedCurrencyData.getCurrencyCode(), selectedCurrencyData.getName(), "91", "AED", CommonUtils.getTextFromEditText(etAmount), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.SAVE_RATE_ALERT_URL, SAVE_RATE_ALERT, Request.Method.PUT, this);
                    AppController.getInstance().getRequestQueue().cancelAll(SAVE_RATE_ALERT.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, SAVE_RATE_ALERT.toString());
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SAVE_RATE_ALERT.toString(), false);
                } else {
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().saveRateAlert((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), "91", "AED", selectedCurrencyData.getCurrencyCode(), selectedCurrencyData.getName(), CommonUtils.getTextFromEditText(etAmount), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.SAVE_RATE_ALERT_URL, SAVE_RATE_ALERT, Request.Method.PUT, this);
                    AppController.getInstance().getRequestQueue().cancelAll(SAVE_RATE_ALERT.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, SAVE_RATE_ALERT.toString());
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SAVE_RATE_ALERT.toString(), false);
                }

            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country_try_again), Toast.LENGTH_LONG).show();
        }
    }

    private void fetchSwapCurrentRate() {
        if (selectedCurrencyData != null && selectedCurrencyData.getCurrencyCode() != null) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCurrentRate("91", selectedCurrencyData.getCurrencyCode(), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CURRENT_RATE_URL, FETCH_CURRENT_RATE, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_CURRENT_RATE.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CURRENT_RATE.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_CURRENT_RATE.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country_try_again), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    toCountry = data.getParcelableExtra(Constants.OBJECT);
                    setCountryData();
                } else {
                    if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getResponseRate() {
        float fee = new Random().nextFloat() * (75f - 15f) + 15f;
        float rate = new Random().nextFloat() * (25f - 0f) + 0f;
        return "{\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"217\",\n" +
                "  \"MESSAGE\": \"Fetch Amount successful\",\n" +
                "  \"AMOUNT\": \"217\", \n" +
                "  \"RATE\": \"" + rate + "\",\n" +
                "  \"FEE\": \"" + fee + "\"\n" +
                "}";
    }

    private String getResponseOnSave() {
        return "{\n" +
                "  \"RESULT\": [\n" +
                "    {\n" +
                "      \"RATE_ALERT_PK_ID\": \"1\",\n" +
                "      \"SOURCE_CCY_CODE\": \"12\",\n" +
                "      \"SOURCE_CCY_NAME\": \"AED\",\n" +
                "      \"SOURCE_AMOUNT\": \"1\",\n" +
                "      \"DESC_CCY_CODE\": \"32\",\n" +
                "      \"DESC_CCY_NAME\": \"INR\",\n" +
                "      \"AMOUNT\": \"94\",\n" +
                "      \"STATUS\": \"A\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"USER_STATUS\": null,\n" +
                "  \"OTP\": null,\n" +
                "  \"AMOUNT\": null,\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"256\",\n" +
                "  \"MESSAGE\": \"SUCCESS\"\n" +
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
