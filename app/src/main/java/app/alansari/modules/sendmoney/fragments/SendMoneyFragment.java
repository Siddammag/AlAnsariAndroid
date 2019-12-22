package app.alansari.modules.sendmoney.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BankData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TxnAmountData;
import app.alansari.modules.sendmoney.SendMoneyActivity;
import app.alansari.modules.sendmoney.adapters.SendMoneyCurrencyCodeRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA;

/**
 * Created by Parveen Dala on 14 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class SendMoneyFragment extends Fragment implements View.OnClickListener, OnWebServiceResult, CustomClickListener {

    private Runnable lObjRunnable;
    private int currentDirection = 1;
    private boolean isCalculatingCurrency = false;


    private View view, fragmentDataCoverLayout;
    private Context context;
    private AppCompatImageView ivSuccess;
    private EditText etSend, etGet;
    private BankData bankData;
    private CountryData countryData;
    private ServiceTypeData serviceTypeData;
    private CircleProgressBar progressBar;
    private CountryData.CurrencyData selectedCurrencyData;
    private String totalToPay, charge, rate;
    private String transferType;
    private TextView tvSendCode, tvGetCode, tvRate, tvTimeMessage, tvMessageBottom;
    private TextWatcher textWatcher;

    // Currency Code Scroll
    private CarouselLayoutManager layoutManagerCurrencyCode;
    private RecyclerView recyclerViewCurrencyCode;
    private SendMoneyCurrencyCodeRecyclerAdapter recyclerAdapterCurrencyCode;
    private String userPkId, sessionTime;
    int temp = 0;
    int check = 0;
    int temp2 = 0;
    int check2 = 0;


    private void init() {
        userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        etSend = (EditText) view.findViewById(app.alansari.R.id.send);
        etGet = (EditText) view.findViewById(app.alansari.R.id.get);
        tvSendCode = (TextView) view.findViewById(app.alansari.R.id.send_code);
        tvGetCode = (TextView) view.findViewById(app.alansari.R.id.get_code);
        tvRate = (TextView) view.findViewById(app.alansari.R.id.rate);
        tvTimeMessage = (TextView) view.findViewById(app.alansari.R.id.time_message);
        tvMessageBottom = (TextView) view.findViewById(app.alansari.R.id.message);
        progressBar = (CircleProgressBar) view.findViewById(app.alansari.R.id.progress_bar);
        ivSuccess = (AppCompatImageView) view.findViewById(app.alansari.R.id.success_image);
        fragmentDataCoverLayout = view.findViewById(app.alansari.R.id.fragment_data_layout_cover);
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        fragmentDataCoverLayout.setVisibility(View.GONE);
        fragmentDataCoverLayout.setOnClickListener(this);


        /*textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((SendMoneyActivity) getActivity()).setSendBtnState(false);
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
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etGet, textWatcher, s.toString());
                }
                totalToPay = "0.00";
                charge = "0.00";
                if (getActivity() instanceof SendMoneyActivity)
                    ((SendMoneyActivity) getActivity()).onSuccessInCalculation(totalToPay, charge);
            }
        };*/


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((SendMoneyActivity) getActivity()).setSendBtnState(false);
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
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etSend, textWatcher, s.toString());
//--------------------------------------------------------------------------------------------------
/*
                    if (etSend.getText().toString().length() > temp2) {
                        if (!etSend.getText().toString().contains("."))
                            etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() - 1)});
                        else
                            etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 1)});

                    }*/

                    if (!etSend.getText().toString().contains(".")) {
                        //etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 1)});
                        etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        check2 = 0;
                    } else if (check2 == 0) {
                        check2 = 1;
                        etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 2)});
                    }


//--------------------------------------------------------------------------------------------------
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
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etGet, textWatcher, s.toString());

//--------------------------------------------------------------------------------------------------

                    /*if (etGet.getText().toString().length() < temp) {
                        if (!etGet.getText().toString().contains("."))
                            etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() - 1)});
                        else
                            etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 1)});

                    }*/

                    if (!etGet.getText().toString().contains(".")) {
                        // etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 1)});
                        etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        check = 0;
                    } else if (check == 0) {
                        check = 1;
                        etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 2)});
                    }

//--------------------------------------------------------------------------------------------------


                }
                totalToPay = "0.00";
                charge = "0.00";
                if (getActivity() instanceof SendMoneyActivity)
                    ((SendMoneyActivity) getActivity()).onSuccessInCalculation(totalToPay, charge);
            }
        };


        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        CommonUtils.hideKeyboard(context);
                        if (v.getId() == etSend.getId()) {
                            if (Validation.isValidEditTextValue(etSend)) {
                                currentDirection = 1;
                                calculateCurrency();
                            }
                            return true;
                        } else if (v.getId() == etGet.getId()) {
                            if (Validation.isValidEditTextValue(etGet)) {
                                currentDirection = 2;
                                calculateCurrency();
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
        recyclerViewCurrencyCode = (RecyclerView) view.findViewById(app.alansari.R.id.recyclerView_currency_code);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(app.alansari.R.layout.send_money_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            countryData = getArguments().getParcelable(Constants.COUNTRY_DATA);
            bankData = getArguments().getParcelable(Constants.BANK_DATA);
            serviceTypeData = getArguments().getParcelable(Constants.SERVICE_TYPE_DATA);
            transferType = getArguments().getString(Constants.TRANSFER_TYPE);
            if (serviceTypeData != null) {
                if (transferType.equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) {
                    if (serviceTypeData.getName().equalsIgnoreCase(Constants.VALUE_TRANSFER)) {
                        tvTimeMessage.setText(bankData.getRemitBtValue());
                    } else if (serviceTypeData.getName().equalsIgnoreCase(Constants.INSTANT_TRANSFER)) {
                        tvTimeMessage.setText(bankData.getRemitBtInstant());
                    }
                } else if (transferType.equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                    if (serviceTypeData.getName().equalsIgnoreCase(Constants.VALUE_TRANSFER)) {
                        tvTimeMessage.setText(bankData.getRemitCpValue());
                    } else if (serviceTypeData.getName().equalsIgnoreCase(Constants.INSTANT_TRANSFER)) {
                        tvTimeMessage.setText(bankData.getRemitCpInstant());
                    }
                }
            } else {
                ((SendMoneyActivity) getActivity()).setFragmentCoverVisibility(View.VISIBLE);
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                return;
            }
            setCurrencyOnCreated();
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrencyOnCreated() {
        if (serviceTypeData.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            if (countryData != null && countryData.getCurrencyData() != null && countryData.getCurrencyData().size() > 0) {
                recyclerAdapterCurrencyCode.addArrayList(countryData.getCurrencyData());
                setDefaultCurrency();
                fragmentDataCoverLayout.setVisibility(View.GONE);
            } else {
                fragmentDataCoverLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
            }
        } else {
            if (countryData != null && bankData != null && transferType != null) {
                fetchCeCurrencyData();
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                fragmentDataCoverLayout.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    private void setCurrencyList(ArrayList<CountryData.CurrencyData> currencyList) {
        LogUtils.d("ok", "Set CE Currency " + currencyList.size());
        recyclerAdapterCurrencyCode.addArrayList(currencyList);
        setDefaultCurrency();
        fragmentDataCoverLayout.setVisibility(View.GONE);
    }

    private void setDefaultCurrency() {
        selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(0) : null;
        for (int i = 0; i < recyclerAdapterCurrencyCode.getItemCount(); i++) {
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus() == null || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                layoutManagerCurrencyCode.scrollToPosition(i);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.fragment_data_layout_cover:
                setCurrencyOnCreated();
                break;
        }
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
    }

    public TxnAmountData getTxnData() {
        if ((!TextUtils.isEmpty(etSend.getText().toString().trim()) && etSend.getText().toString().trim().length() > 0) && ((!TextUtils.isEmpty(etGet.getText().toString().trim()) && etGet.getText().toString().trim().length() > 0))) {
            TxnAmountData currencyData = new TxnAmountData();
            currencyData.setYouSend(CommonUtils.getTextFromEditText(etSend));
            currencyData.setYouGet(CommonUtils.getTextFromEditText(etGet));
            currencyData.setTotalToPay(totalToPay);
            currencyData.setFee(charge);
            currencyData.setRate(rate);
            currencyData.setYouSendCurrencyData(new CountryData.CurrencyData().getUAECurrencyData());
            currencyData.setYouGetCurrencyData(selectedCurrencyData);
            return currencyData;
        } else return null;
    }

    public void resetFromActivity() {
        cancelPendingRequests();
        resetAmount();
    }

    private void setRateData(String currentRate) {
        if (countryData != null && selectedCurrencyData != null && currentRate != null) {
            //TODO Use String
            tvRate.setVisibility(View.VISIBLE);
            try {
                tvRate.setText("Exchange Rate AED = " + selectedCurrencyData.getName() + " " + currentRate);
            } catch (Exception ex) {
                tvRate.setVisibility(View.GONE);
            }
        } else {
            tvRate.setVisibility(View.GONE);
        }
    }

    private void resetAmount() {
        isCalculatingCurrency = true;
        etSend.setText("");
        etGet.setText("");
        tvRate.setText("");
        ((SendMoneyActivity) getActivity()).onSuccessInCalculation("0.00", "0.00");
        ((SendMoneyActivity) getActivity()).hideVat();
        isCalculatingCurrency = false;
    }

    @Override
    public void onDestroy() {
        resetAmount();
        super.onDestroy();
    }

    private void fetchCeCurrencyData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCeCurrencyData(bankData.getBankCodeCE(), countryData.getCountryCodeCE(), CommonUtils.getMemPkId(serviceTypeData.getMapping()), transferType, "", userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CE_CURRENCY_DATA_URL, FETCH_CE_CURRENCY_DATA, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_CE_CURRENCY_DATA.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CE_CURRENCY_DATA.toString());
            fragmentDataCoverLayout.setVisibility(View.GONE);
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateCurrency() {
        try {
            if ((currentDirection == 1 && Validation.isValidEditTextValue(etSend)) || (currentDirection == 2 && Validation.isValidEditTextValue(etGet))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    String countryCode = serviceTypeData.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? countryData.getCountryCodeAREX() : countryData.getCountryCodeCE();
                    JsonObjectRequest jsonObjReq = null;
                    String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    if (currentDirection == 1) {
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().calculateCurrencySendMoney(CommonUtils.getMemPkId(serviceTypeData.getMapping()), countryCode, serviceTypeData.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? bankData.getBankCodeAREX() : bankData.getBankCodeCE(), ((SendMoneyActivity) getActivity()).getTransferType(), serviceTypeData.getMapping(), CommonUtils.getTextFromEditText(etSend), "91", selectedCurrencyData.getCurrencyCode(), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.CALCULATE_CURRENCY_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);
                    } else {
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().calculateCurrencySendMoney(CommonUtils.getMemPkId(serviceTypeData.getMapping()), countryCode, serviceTypeData.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? bankData.getBankCodeAREX() : bankData.getBankCodeCE(), ((SendMoneyActivity) getActivity()).getTransferType(), serviceTypeData.getMapping(), CommonUtils.getTextFromEditText(etGet), selectedCurrencyData.getCurrencyCode(), "91", userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.CALCULATE_CURRENCY_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);
                    }
                    cancelPendingRequests();
                    ((SendMoneyActivity) getActivity()).setSendBtnState(false);
                    isCalculatingCurrency = true;
                    ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    ivSuccess.setVisibility(View.GONE);
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY.toString());
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelPendingRequests() {
        AppController.getInstance().cancelPendingRequests(CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY.toString());
        onErrorInCalculation();
    }

    private void onErrorInCECurrency() {
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        recyclerAdapterCurrencyCode.clear();
        selectedCurrencyData = null;
        fragmentDataCoverLayout.setVisibility(View.VISIBLE);
        //Toast.makeText(context, "Unable to Fetch Currency For Selected Country!", Toast.LENGTH_SHORT).show();
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private void onSuccessInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.VISIBLE);
        ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private void onCurrencyListEmpty() {
        recyclerAdapterCurrencyCode.clear();
        selectedCurrencyData = null;
        fragmentDataCoverLayout.setVisibility(View.VISIBLE);
//        ((SendMoneyActivity) getActivity()).setFragmentCoverVisibility(View.VISIBLE);
        // Toast.makeText(context, "Currency Not Available For Selected Country!", Toast.LENGTH_SHORT).show();
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
                                    onCurrencyListEmpty();
                                }
                            } else {
                                onCurrencyListEmpty();
                            }
                        } else {
                            onErrorInCECurrency();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onErrorInCECurrency();
                    }
                } else {
                    onErrorInCECurrency();
                }
                break;
            case CALCULATE_CURRENCY_SEND_MONEY:
                if (status == 1) {
                    try {
                        //response = new JSONObject(getResponse(currentDirection == 1 ? etSend.getText().toString().trim() : etGet.getText().toString().trim()));
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            String amount = response.getString(Constants.AMOUNT);
                            if (Validation.isValidRate(amount) && Validation.isValidRate(response.getString(Constants.TOTAL_AMOUNT)) && Validation.isValidRate(response.getString(Constants.RATE)) && Validation.isValidRate(response.getString(Constants.CHARGES_ON_US))) {
                                Double amountDouble = Double.valueOf(amount);
                                Double totalAmountDouble = Double.valueOf(response.getString(Constants.TOTAL_AMOUNT));
                                Double rateDouble = Double.valueOf(response.getString(Constants.RATE));
                                Double chargeDouble = Double.valueOf(response.getString(Constants.CHARGES_ON_US));
                                Double vat = Double.valueOf(response.getString(Constants.VAT_CHARGES));
                                Double discount = Double.valueOf(response.getString(Constants.VAT_DISCOUNT));
                                Double roundOff = Double.valueOf(response.getString(Constants.VAT_ROUNDINGOFF));
                                if (currentDirection == 1) {
//                                    etGet.setText(CommonUtils.getDecimalFormattedString(amountDouble));
                                    etGet.setText(amount);
                                } else {
//                                    etSend.setText(CommonUtils.getDecimalFormattedString(amountDouble));
                                    etSend.setText(amount);
                                }
                                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                                nf.setMinimumFractionDigits(10);
/*                                System.out.println(nf.format(rateDouble));
                                System.out.println("round off : "+Double.valueOf(rateDouble).longValue());*/
                                rate = String.valueOf(nf.format(rateDouble));//CommonUtils.getDecimalFormattedString(rateDouble);
//                                charge = CommonUtils.getDecimalFormattedString(chargeDouble);
                                charge = response.getString(Constants.CHARGES_ON_US);
//                                totalToPay = CommonUtils.getDecimalFormattedString(totalAmountDouble);
                                totalToPay = response.getString(Constants.TOTAL_AMOUNT);
                                setRateData(rate);
                                if (getActivity() instanceof SendMoneyActivity) {
                                    ((SendMoneyActivity) getActivity()).onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);
                                    ((SendMoneyActivity) getActivity()).setVat(response);
                                    ((SendMoneyActivity) getActivity()).setSendBtnState(true);
                                }
                                onSuccessInCalculation();
                            } else {
                                onAPIError();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            //  Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
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

    private void onAPIError() {
        try {
            if (context != null)
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            onErrorInCalculation();
        } catch (Exception ex) {

        }
    }

    private String getResponse(String amount) {
        float fee = 15f;//new Random().nextFloat() * (75f - 0f) + 15f;
        float rate = 14f;//new Random().nextFloat() * (25f - 0f) + 0f;
        return "{\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"217\",\n" +
                "  \"MESSAGE\": \"Fetch Amount successful\",\n" +
                "  \"AMOUNT\": \"" + (Float.valueOf(amount) * rate) + "\",\n" +
                "  \"TOTAL_AMOUNT\": \"" + amount + "\",\n" +
                "  \"RATE\": \"" + rate + "\",\n" +
                "  \"CHARGES_ON_US\": \"" + fee + "\"\n" +
                "}";
    }
}