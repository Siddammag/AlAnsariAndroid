package app.alansari.modules.sendmoney.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
import app.alansari.models.WUBeneficiaryData;
import app.alansari.models.WuCurrencyData;
import app.alansari.models.WuRateChargesResponse;
import app.alansari.modules.sendmoney.WUSendMoneyActivity;
import app.alansari.modules.sendmoney.adapters.SendMoneyCurrencyCodeRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_CURRENCY_URL;

/**
 * Created by Parveen Dala on 14 October, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */
public class WUSendMoneyFragment extends Fragment implements View.OnClickListener, OnWebServiceResult, CustomClickListener {

    private Runnable lObjRunnable;
    private int currentDirection = 1;
    private boolean isCalculatingCurrency = false;

    private String arexUserId;
    private CountryData countryData;
    private CountryData.CurrencyData selectedCurrencyData;

    private CountryData selectedCurrencyData1;
    private BankData bankData;
    private ServiceTypeData serviceTypeData;

    private View view, fragmentDataCoverLayout;
    private Context context;
    private AppCompatImageView ivSuccess;
    private EditText etSend, etGet;
    private WUBeneficiaryData dataObject;
    private CircleProgressBar progressBar;
    private String totalToPay, charge, rate;
    private String transferType;
    private TextView tvSendCode, tvGetCode, tvRate, tvTimeMessage, tvMessageBottom;
    private TextWatcher textWatcher;

    // Currency Code Scroll
    private CarouselLayoutManager layoutManagerCurrencyCode;
    private RecyclerView recyclerViewCurrencyCode;
    private SendMoneyCurrencyCodeRecyclerAdapter recyclerAdapterCurrencyCode;
    private String sessionTime,userPkId;

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
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


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((WUSendMoneyActivity) getActivity()).setSendBtnState(false);
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
                tvRate.setVisibility(View.GONE);
                ((WUSendMoneyActivity) getActivity()).clearCharges();
                if (getActivity() instanceof WUSendMoneyActivity)
                    ((WUSendMoneyActivity) getActivity()).onSuccessInCalculation(totalToPay, charge);
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
                if (adapterPosition >= 0 && (selectedCurrencyData == null
                        || (selectedCurrencyData.getCurrencyCode() != null
                                && recyclerAdapterCurrencyCode.getItemAt(adapterPosition) != null
                                && recyclerAdapterCurrencyCode.getItemAt(adapterPosition).getCurrencyCode() != null
                                && !selectedCurrencyData.getCurrencyCode().equalsIgnoreCase(recyclerAdapterCurrencyCode.getItemAt(adapterPosition).getCurrencyCode())))) {
                    selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(adapterPosition);


                    dataObject.setReceiverCurrencyCode(selectedCurrencyData.getName());

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
        view = mInflater.inflate(app.alansari.R.layout.wu_send_money_fragment, container, false);
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
            dataObject = getArguments().getParcelable(Constants.OBJECT);
            countryData = getArguments().getParcelable(Constants.COUNTRY_DATA);
            arexUserId = getArguments().getString(Constants.AREX_MEM_PK_ID);
            serviceTypeData = null;
            fetchCeCurrencyData();
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCeCurrencyData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCurrencyList(countryData.getCountryCodeAREX(), countryData.getWuCountryCode(),userPkId,LogoutCalling.getDeviceID(context),sessionTime),
                    Constants.WU_CURRENCY_URL, WU_CURRENCY_URL, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_CURRENCY_URL.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_CURRENCY_URL.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrencyOnCreated() {
        if (countryData != null && countryData.getCurrencyData() != null && countryData.getCurrencyData().size() > 0) {
            recyclerAdapterCurrencyCode.addArrayList(countryData.getCurrencyData());
            setDefaultCurrency();
            fragmentDataCoverLayout.setVisibility(View.GONE);
        } else {
            fragmentDataCoverLayout.setVisibility(View.VISIBLE);
            Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
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
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus() == null
                   || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                break;
            }

        }


        /*selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(0) : null;
        for (int i = 0; i <= recyclerAdapterCurrencyCode.getItemCount(); i++) {
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus() == null
                    || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                recyclerAdapterCurrencyCode.notifyDataSetChanged();
                //break;
            }else{
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                recyclerAdapterCurrencyCode.notifyDataSetChanged();
            }



        }*/

    }
    private void setDefaultCurrencyNew() {
        selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(0) : null;
        for (int i = 0; i < recyclerAdapterCurrencyCode.getItemCount(); i++) {


            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus()== null
                    || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus()
                    .equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                recyclerAdapterCurrencyCode.notifyDataSetChanged();
                Toast.makeText(context, "Siddu11", Toast.LENGTH_SHORT).show();
                break;


            }else{
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerAdapterCurrencyCode.getItemId(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                recyclerAdapterCurrencyCode.notifyDataSetChanged();
                Toast.makeText(context, "Siddu22", Toast.LENGTH_SHORT).show();
            }


          /* if (!recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equals("")
                    && recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equals("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerViewCurrencyCode.scrollToPosition(i);
               // break;
            }*/

          /*  if ( recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equals("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerViewCurrencyCode.scrollToPosition(i);
                break;
            }*/

           /* if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equals("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                //layoutManagerCurrencyCode.scrollToPosition(i);
                recyclerViewCurrencyCode.scrollToPosition(i);
               // recyclerAdapterCurrencyCode.getItemId(i);
                //recyclerAdapterCurrencyCode.notifyDataSetChanged();

                break;
            }*/
        }

    }

    private void setDefaultCurrency(int pos) {
        selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(pos) : null;
        for (int i = 0; i < recyclerAdapterCurrencyCode.getItemCount(); i++) {
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus() == null || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                recyclerViewCurrencyCode.smoothScrollToPosition(i);
                recyclerAdapterCurrencyCode.notifyDataSetChanged();
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
        ((WUSendMoneyActivity) getActivity()).onSuccessInCalculation("0.00", "0.00");
        isCalculatingCurrency = false;
    }

    @Override
    public void onDestroy() {
        resetAmount();
        super.onDestroy();
    }

    private void calculateCurrency() {
        try {
            if (((WUSendMoneyActivity) getActivity()).getServiceType().equalsIgnoreCase("")) {
                Toast.makeText(context, "Select Service Type!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if ((currentDirection == 1 && Validation.isValidEditTextValue(etSend)) || (currentDirection == 2 && Validation.isValidEditTextValue(etGet))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    JsonObjectRequest jsonObjReq = null;
                    String promoCode = ((WUSendMoneyActivity) getActivity()).getPromoCode();
                    String BENEF_TYPE = ((WUSendMoneyActivity) getActivity()).getBenefType();
                    String CLIENT_IP_ADDRESS = "somthing";
                    String SERVICE_TYPE = ((WUSendMoneyActivity) context).getServiceType();
                    String TOTAL_VALUE_AED_DISP = "";
                    String FCY_AMOUNT = "";
                    String USE_MY_WU_PROMO_CODE = "";
                    String WU_LOOKUP_PROMO_CODE = "";

                    // Sidduuuuuuu
                    if (currentDirection == 1) {
                        TOTAL_VALUE_AED_DISP = CommonUtils.getTextFromEditText(etSend);
                        jsonObjReq = new CallAddr().executeApi(fetchRateAndChargeData(dataObject.getArexCurrencyCode(), dataObject.getArexCountryCode(), BENEF_TYPE,
                                CLIENT_IP_ADDRESS, FCY_AMOUNT, arexUserId, promoCode, SERVICE_TYPE, TOTAL_VALUE_AED_DISP, USE_MY_WU_PROMO_CODE,
                                CommonUtils.getUserId(), dataObject.getReceiverCurrencyCode(), dataObject.getReceiverCountryCode(), WU_LOOKUP_PROMO_CODE, LogoutCalling.getDeviceID(context),sessionTime),
                                Constants.WU_CALCULATE_CURRENCY_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.WU_CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);
                    } else {
                        FCY_AMOUNT = CommonUtils.getTextFromEditText(etGet);
                        jsonObjReq = new CallAddr().executeApi(fetchRateAndChargeData(dataObject.getArexCurrencyCode(), dataObject.getArexCountryCode(), BENEF_TYPE,
                                CLIENT_IP_ADDRESS, FCY_AMOUNT, arexUserId, promoCode, SERVICE_TYPE, TOTAL_VALUE_AED_DISP, USE_MY_WU_PROMO_CODE,
                                CommonUtils.getUserId(), dataObject.getReceiverCurrencyCode(), dataObject.getReceiverCountryCode(), WU_LOOKUP_PROMO_CODE, LogoutCalling.getDeviceID(context),sessionTime),
                                Constants.WU_CALCULATE_CURRENCY_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.WU_CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);
                    }
                    cancelPendingRequests();
                    ((WUSendMoneyActivity) getActivity()).setSendBtnState(false);
                    isCalculatingCurrency = true;
                    ((WUSendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    ivSuccess.setVisibility(View.GONE);
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY.toString());
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                ((WUSendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        fragmentDataCoverLayout.setVisibility(View.VISIBLE);
        //Toast.makeText(context, "Unable to Fetch Currency For Selected Country!", Toast.LENGTH_SHORT).show();
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        ((WUSendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private void onSuccessInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.VISIBLE);
        ((WUSendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    private void onCurrencyListEmpty() {
        recyclerAdapterCurrencyCode.clear();
        fragmentDataCoverLayout.setVisibility(View.VISIBLE);
//        ((WUSendMoneyActivity) getActivity()).setFragmentCoverVisibility(View.VISIBLE);
        Toast.makeText(context, "Currency Not Available For Selected Country!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case WU_CURRENCY_URL:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                            ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(),
                                    new TypeToken<ArrayList<WuCurrencyData>>() {
                            }.getType());
                            if (currencyData != null && currencyData.size() > 0) {
                                ArrayList<CountryData.CurrencyData> newCurrencyData = new ArrayList<CountryData.CurrencyData>();
                                for (int i = 0; i < currencyData.size(); i++) {
                                    CountryData.CurrencyData data = new CountryData.CurrencyData();

                                   /* if(currencyData.get(i).getDefaultStatus().equals("0")) {
                                        //setDefaultCurrency(0);
                                        data.setCurrencyCode(currencyData.get(i).getCurrencyCode());
                                    }else {
                                        //setDefaultCurrency(1);
                                        data.setCurrencyCode(currencyData.get(i).getCurrencyCode());
                                    }*/
                                    data.setCurrencyCode(currencyData.get(i).getCurrencyCode());
                                    data.setName(currencyData.get(i).getWuCurrencyCode());
                                    data.setDefaultStatus(currencyData.get(i).getDefaultStatus());

                                    newCurrencyData.add(data);

                                }
                                countryData.setCurrencyData(newCurrencyData);
                                recyclerAdapterCurrencyCode.addArrayList(newCurrencyData); //siddu
                                setDefaultCurrency();

                                fragmentDataCoverLayout.setVisibility(View.GONE);
                                return;
                            }
                        } else {
                            onCurrencyListEmpty();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onErrorInCECurrency();
                    }
                }
                break;
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
            case WU_CALCULATE_CURRENCY_SEND_MONEY:
                if (status == 1) {
                    try {
                        //response = new JSONObject(getResponse(currentDirection == 1 ? etSend.getText().toString().trim() : etGet.getText().toString().trim()));
                        if (response.getInt(Constants.STATUS_CODE) == Constants.WU_RATE_CHARGE_SUCCESS_CODE) {

                            Gson gson = new Gson();
                            Type type = new TypeToken<WuRateChargesResponse>() {
                            }.getType();
                            WuRateChargesResponse wuRateChargesResponse = gson.fromJson(response.toString(), type);
                            String amount = response.getString(Constants.ORIGINAL_PRINCIPLE_AMOUNT);
                            String DEST_PRINCIPAL_AMOUNT = response.getString(Constants.DEST_PRINCIPAL_AMOUNT);
                            NumberFormat nf = NumberFormat.getInstance(Locale.US);
                            nf.setMinimumFractionDigits(10);

                            Double rateDouble = Double.valueOf(response.getString(Constants.RATE));
                            rate = String.valueOf(nf.format(rateDouble));
                            etGet.setText(CommonUtils.getDecimalFormattedString(Double.valueOf(DEST_PRINCIPAL_AMOUNT)));
                            etSend.setText(CommonUtils.getDecimalFormattedString(Double.valueOf(amount)));
                            setRateData(rate);
                            totalToPay = response.getString(Constants.GROSS_TOTAL_AMOUNT);
                            charge = response.getString(Constants.CHARGES);
                            if (getActivity() instanceof WUSendMoneyActivity) {
                                ((WUSendMoneyActivity) getActivity()).onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);
                                ((WUSendMoneyActivity) getActivity()).setVat(response);
                                ((WUSendMoneyActivity) getActivity()).setRateChargesParams(wuRateChargesResponse);
                                ((WUSendMoneyActivity) getActivity()).setSendBtnState(true);
                            }

                            onSuccessInCalculation();

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

    private void onAPIError() {
        if (context != null)
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        onErrorInCalculation();
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


    public JSONObject fetchRateAndChargeData(String arexCcyCode, String arexCountryCode, String benefType, String clientIpAdd, String fcyAmt, String arexMemPkId, String otherPromoCode,
                                             String serviceType, String totValAedDisp, String myWuPromoCode, String userPkId, String wuCcyCode, String wuCountryCode, String wulookUpPromoCode,String deviceId,String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.AREX_CCY_CODE, arexCcyCode);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, arexCountryCode);
            jsonObject.put(Constants.BENEF_TYPE, benefType);
            jsonObject.put(Constants.CHANNEL_ID, "M");
            jsonObject.put(Constants.CLIENT_IP_ADDRESS, clientIpAdd);
            jsonObject.put(Constants.FCY_AMOUNT, fcyAmt);
            jsonObject.put(Constants.MEM_PK_ID, arexMemPkId);
            jsonObject.put(Constants.OTHER_PROMO_CODE, otherPromoCode);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.TOTAL_VALUE_AED_DISP, totValAedDisp);
            jsonObject.put(Constants.USE_MY_WU_PROMO_CODE, myWuPromoCode);
            jsonObject.put(Constants.USER_PK_ID, userPkId);
            jsonObject.put(Constants.WU_CCY_CODE, wuCcyCode);
            jsonObject.put(Constants.WU_COUNTRY_CODE, wuCountryCode);
            if(!otherPromoCode.equalsIgnoreCase("")){
                jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, "Y");
            }else{
                jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, "N");
            }

            //jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, wulookUpPromoCode);

            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchRateAndChargeData:-  " + jsonObject.toString());
        return jsonObject;
    }
}