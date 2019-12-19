package app.alansari;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.adapters.PromoCodeAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AdditionalInfoData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.PaymentModeData;
import app.alansari.models.PromoCodeData;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsData;
import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFieldsCe;
import app.alansari.modules.accountmanagement.models.BeneficiaryTypeCeData;
import app.alansari.modules.accountmanagement.models.BusinessActivitiesCeData;
import app.alansari.modules.accountmanagement.models.BusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.CompanyTypeCeData;
import app.alansari.modules.accountmanagement.models.ContributionCeData;
import app.alansari.modules.accountmanagement.models.DistrictCeData;
import app.alansari.modules.accountmanagement.models.FundsSourceCeData;
import app.alansari.modules.accountmanagement.models.IdDateTypeCeData;
import app.alansari.modules.accountmanagement.models.IdProofCeData;
import app.alansari.modules.accountmanagement.models.ProfessionCeData;
import app.alansari.modules.accountmanagement.models.PurposeCeData;
import app.alansari.modules.accountmanagement.models.ResidentialStatusCeData;
import app.alansari.modules.accountmanagement.models.SubBusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.SubPurposeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseDateTypeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseTypeCeData;
import app.alansari.modules.sendmoney.PaymentDetailsBankPaymentActivity;
import app.alansari.modules.sendmoney.PaymentGatewayActivity;
import app.alansari.modules.sendmoney.PaymentModeActivity;
import app.alansari.modules.sendmoney.TransactionDetailsActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class AdditionalInfoSelectModeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {


    private Intent intent;
    private Button btnConfirm;
    private Context context;

    private LinearLayout dynamicLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private BeneficiaryData dataObject;
    private TextView tvName, tvBankName, tvBranchName, tvAccountNumber;
    private TextView tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;
    private RelativeLayout promocodeLayout;
    private ScrollView scrollView;

    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<AdditionalInfoData> additionalInfoFields;
    private String fieldId, errorMessage, selectedDOB, selectedDateOfBirth;

    private String TOTAL_PRIORITY_PAY_CHARGES_PP,ADDITIONAL_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private long timeStamp;
    private boolean isAutoFill, isPromoListActive;
    private Dialog promoCodeDialog;
    private EditText etPromoCode;
    private RecyclerView promoRecyclerView;
    private TextView tvPromoMessage;
    private Button btnSave;
    private PromoCodeAdapter promoCodeAdapter;
    private ImageView ivChecked;
    private boolean isAgreed;
    private Dialog priorityConditionsDialog;
    private String name, id, transferType, modeDescription;
    private String totalToPay, charge;
    private Dialog pendingTransactionDialog;
    private CountryData selectedCountry;
    private String vat;
    private String rounding;
    private String discount;
    private JSONObject jsonObjectInputChectTxnList;
    private String CcyDesc,ccyId,ccyCode;
    private String sourceType;
    private String isWu = "N";
    private String ceServiceId="";
    private ArrayList<BeneficiaryDynamicFieldsCe> beneficiaryDynamicFieldsCeList;
    private String purposeId;

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

    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (validated && !isAutoFill) {
            inputLayout.setError(((AdditionalInfoData) editText.getTag()).getErrorMessage());
            inputLayout.setErrorEnabled(true);
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        }
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
    private void validateSingleField(EditText editText, TextInputLayout inputLayout) {
        if (checkLayoutFilledStatus(new EditText[]{editText}, new TextInputLayout[]{inputLayout}, true)) {
            checkLayoutFilledStatus(null, null, false);
        }
    }

    private void validateSingleFieldCE(EditText editText, TextInputLayout inputLayout) {
        if (checkLayoutFilledStatusCE(new EditText[]{editText}, new TextInputLayout[]{inputLayout}, true)) {
            checkLayoutFilledStatusCE(null, null, false);
        }
    }

    private boolean checkLayoutFilledStatusCE(EditText[] dynamicEditText, TextInputLayout[] dynamicInputLayout, boolean showMessage) {
        boolean layoutStatus = false, specialCharacterValidation = true;
        if (dynamicEditText == null) {
            dynamicEditText = this.dynamicEditText;
            dynamicInputLayout = this.dynamicInputLayout;
        }

        // Check Min And Max Length
        for (int i = 0; i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditText[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase("T")))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    break;
                }
            if ((Validation.isValidEditTextValue(dynamicEditText[i])) && ((getEditTextMinLength(dynamicEditText[i]) == 0) || dynamicEditText[i].getText().toString().trim().length() >= getEditTextMinLength(dynamicEditText[i])) && ((getEditTextLength(dynamicEditText[i]) == 0) || dynamicEditText[i].getText().toString().trim().length() <= getEditTextLength(dynamicEditText[i]))) {
                layoutStatus = true;
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
            } else {
                layoutStatus = false;
                setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                break;
            }
        }

        if ((showMessage && dynamicEditText.length == 1 && specialCharacterValidation && layoutStatus)) {
            return true;
        }
        if (specialCharacterValidation && layoutStatus && isAgreed)
            btnConfirm.setEnabled(true);
        else
            btnConfirm.setEnabled(false);

        return layoutStatus;
    }

    private boolean checkLayoutFilledStatus(EditText[] dynamicEditText, TextInputLayout[] dynamicInputLayout, boolean showMessage) {
        boolean layoutStatus = false, specialCharacterValidation = true;
        if (dynamicEditText == null) {
            dynamicEditText = this.dynamicEditText;
            dynamicInputLayout = this.dynamicInputLayout;
        }

        for (int i = 0; dynamicEditText != null && i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getFieldType() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getIsMandatory().equalsIgnoreCase("0")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
                setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                layoutStatus = true;
            } else {
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                layoutStatus = false;
                break;
            }
        }
        if ((showMessage && dynamicEditText.length == 1 && specialCharacterValidation && layoutStatus)) {
            return true;
        }

        if (specialCharacterValidation && layoutStatus && isAgreed)
            btnConfirm.setEnabled(true);
        else
            btnConfirm.setEnabled(false);
        return layoutStatus;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info_select_mode);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Additional Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();
        try {
            if (getIntent().getExtras() != null) {
                sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
                selectedCountry = dataObject.getCountryData();
                TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
                TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
                modeDescription = getIntent().getStringExtra(Constants.DESCRIPTION);
                jsonObjectInputChectTxnList = new JSONObject(getIntent().getStringExtra(Constants.RESPONSE_INTENT));
                if (dataObject != null) {
                    setInitialData();
                    fetchAdditionalInfo();
                    return;
                }

            /* String recAmt = CommonUtils.addCommaToString(dataObject.getTxnAmountData().getYouGet());
            if (recAmt.equalsIgnoreCase("")) {
                tvTotalReceive.setText(dataObject.getTxnAmountData().getYouGet());
            } else {
                tvTotalReceive.setText(recAmt);
            }

            tvTotalReceive.setText(CommonUtils.addCommaToString(dataObject.getTxnAmountData().getYouGet()));
            tvTotalReceiveCurrencyCode.setText(dataObject.getTxnAmountData().getYouGetCurrencyData().getName());
            tvTotalToPay.setText(CommonUtils.addCommaToString(dataObject.getTxnAmountData().getTotalToPay()));
            tvTotalToPayCurrencyCode.setText(dataObject.getTxnAmountData().getYouSendCurrencyData().getName());*/

                Log.e("cbshbchsdbc", "" + dataObject.getCountryData());
            }
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        tvName = (TextView) findViewById(app.alansari.R.id.name);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(app.alansari.R.id.account_number);

        tvTotalReceive = (TextView) findViewById(app.alansari.R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);

        dynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.dynamic_layout);
        btnConfirm = (Button) findViewById(app.alansari.R.id.confirm_btn);
        promocodeLayout = findViewById(app.alansari.R.id.promocode_layout);
        scrollView = findViewById(app.alansari.R.id.scroll_view);
        ivChecked = (ImageView) findViewById(R.id.accept_image);
        ivChecked.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.accept_text).setOnClickListener(this);
        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnConfirm.setVisibility(View.GONE);
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnConfirm.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }
        );

        etPromoCode = (EditText) findViewById(app.alansari.R.id.promocode);
        promoCodeDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        promoCodeDialog.setCanceledOnTouchOutside(false);
        promoCodeDialog.setContentView(R.layout.promo_code_dialog);
        promoCodeAdapter = new PromoCodeAdapter(context);
        promoRecyclerView = (RecyclerView) promoCodeDialog.findViewById(app.alansari.R.id.recycler_view);
        tvPromoMessage = (TextView) promoCodeDialog.findViewById(app.alansari.R.id.message);
        btnSave = (Button) promoCodeDialog.findViewById(app.alansari.R.id.save);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        promoRecyclerView.setLayoutManager(mLayoutManager);
        promoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        promoRecyclerView.setAdapter(promoCodeAdapter);

        promoCodeDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeDialog.dismiss();
            }
        });
        etPromoCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isPromoListActive) {
                    promoCodeDialog.show();
                    etPromoCode.clearFocus();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPromoCode.setText(promoCodeAdapter.getCode());
                dataObject.setPromoCode(promoCodeAdapter.getPromoCode());
                promoCodeDialog.dismiss();
            }
        });

    }

    private void setInitialData() {
        if (dataObject != null) {
            if (Validation.isValidString(dataObject.getName())) {
                tvName.setText(dataObject.getName());
            } else {
                tvName.setText(dataObject.getArabicName());
            }
            tvBankName.setText(dataObject.getBankData().getBankName());
            tvBranchName.setText(dataObject.getBranchName());


            if (dataObject.getTransferType() != null && dataObject.getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                tvAccountNumber.setText(dataObject.getMobile());
            } else {
                tvAccountNumber.setText(dataObject.getAccountNumber());
            }



//---------------------------AMOUNT-----------------------------------------------------------------
            try {
                setVat(new JSONObject(getIntent().getStringExtra(Constants.PAYMENT_DATA)));
                String recAmt = CommonUtils.addCommaToString(getIntent().getStringExtra(Constants.TOTAL_RECIEVE));
                if (recAmt.equalsIgnoreCase("")) {
                    tvTotalReceive.setText(getIntent().getStringExtra(Constants.TOTAL_RECIEVE));
                } else {
                    tvTotalReceive.setText(recAmt);
                }
                tvTotalReceive.setText(CommonUtils.addCommaToString(getIntent().getStringExtra(Constants.TOTAL_RECIEVE)));
                tvTotalReceiveCurrencyCode.setText(getIntent().getStringExtra(Constants.TOTAL_RECIEVE_CURRENCY));
                tvTotalToPay.setText(CommonUtils.addCommaToString(totalToPay));
                tvTotalToPayCurrencyCode.setText("AED");
                Log.e("fkshvbshb", "" + jsonObjectInputChectTxnList);

                name = getIntent().getStringExtra(Constants.MODE_NAME);
                id = getIntent().getStringExtra(Constants.MODE_PK_ID);
                transferType = getIntent().getStringExtra(Constants.TRANSFER_TYPE);
                dataObject.setTransferType(transferType);
                ceServiceId=getIntent().getStringExtra(Constants.CE_SERVICE_ID);


                String rate = jsonObjectInputChectTxnList.getString(Constants.RATE);
                String txnAmount = jsonObjectInputChectTxnList.getString(Constants.TXN_AMOUNT);
                String totalValueAed = jsonObjectInputChectTxnList.getString(Constants.TOTAL_VALUE_AED);
                dataObject.getTxnAmountData().setRate(rate);
                dataObject.getTxnAmountData().setYouGet(txnAmount);
                dataObject.getTxnAmountData().setYouSend(totalValueAed);


                //ccyId = dataObject.getCountryData().getId();
                ccyId=getIntent().getStringExtra(Constants.CCY_ID);
                ccyCode=getIntent().getStringExtra(Constants.CCY_CODE);
                CcyDesc = getIntent().getStringExtra(Constants.TOTAL_RECIEVE_CURRENCY);



              /*  dataObject.getTxnAmountData().getYouSend();
                dataObject.getTxnAmountData().getFee();
                dataObject.getTxnAmountData().getTotalToPay();
                dataObject.getTxnAmountData().getRate();
                dataObject.getTxnAmountData().getYouGet();*/


//--------------------------------------------------------------------------------------------------
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void setVat(JSONObject response) {
        try {
            String vatLable = response.getString(Constants.VAT_CHARGES_CODE);
            String roundingLable = response.getString(Constants.VAT_ROUNDINGOFF_CODE);
            String discountLable = response.getString(Constants.VAT_DISCOUNT_CODE);
            charge = response.getString(Constants.CHARGES_ON_US);
            totalToPay = response.getString(Constants.TOTAL_AMOUNT);

            vat = response.getString(Constants.VAT_CHARGES);
            rounding = response.getString(Constants.VAT_ROUNDINGOFF);
            discount = response.getString(Constants.VAT_DISCOUNT);
            // TOTAL_AMOUNT_PP = response.getString(Constants.TOTAL_AMOUNT_PP);
            TOTAL_AMOUNT_PP = response.getString(Constants.TOTAL_AMOUNT);
            TOTAL_PRIORITY_PAY_CHARGES_PP = response.getString(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
            ADDITIONAL_CHARGES_PP=response.getString(Constants.ADDITIONAL_CHARGES_PP);


            dataObject.setVat(vat);
            dataObject.setRoundoff(rounding);
            dataObject.setDiscount(discount);
            dataObject.getTxnAmountData().setRate(charge);
            dataObject.getTxnAmountData().setFee(charge);
            dataObject.getTxnAmountData().setTotalToPay(totalToPay);




          /*  dataObject.getTxnAmountData().getYouSend());
            ncusguhs*/

            /*if (vat != null && (!vat.equalsIgnoreCase("0.0") && !vat.equalsIgnoreCase("0.00"))) {
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
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void fetchAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //TODO Update 26 ->
            //dataObject.getBankData().getId();
            JsonObjectRequest jsonObjReq = null;
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            if (dataObject.getModuleName().equalsIgnoreCase("AREX")) {
                jsonObjReq = new CallAddr().executeApi(fetchAdditionalInfo(
                        (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                        dataObject.getCountryData().getId(), dataObject.getBankData().getId(), dataObject.getServiceTypeData().getId(),
                        transferType, CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping()),
                        dataObject.getBeneficiaryId(), LogoutCalling.getDeviceID(context), sessionTime),
                        Constants.FETCH_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO.toString());
            }

        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void remittanceApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {

            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JSONObject input = remittanceApiCall((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject, LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.REMITTANCE_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                fetchAdditionalInfo();
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (v.getTag() != null) {
                    AdditionalInfoData currentView = ((AdditionalInfoData) v.getTag());
                    intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
                    intent.putExtra(Constants.ID, Integer.valueOf(currentView.getId()));
                    intent.putExtra(Constants.TITLE, currentView.getTitle());
                    intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<AdditionalInfoData.ValuesData>) currentView.getValuesData());
                    startActivityForResult(intent, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }
                break;
            case app.alansari.R.id.confirm_btn:
                if (btnConfirm.isEnabled()) {
                    addAdditionalInfo();

                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
            case app.alansari.R.id.promocode_layout:
                promoCodeDialog.show();
                break;
            case R.id.accept_text:
                createConditionDialog();
                break;
            case R.id.accept_image:
                if (isAgreed) {
                    ivChecked.setImageResource(0);
                    isAgreed = false;
                } else {
                    ivChecked.setImageResource(R.drawable.svg_success);
                    isAgreed = true;
                }
                checkLayoutFilledStatus(null, null, false);
                break;
        }
    }

    private void checkingBanking() {
        if (name.equalsIgnoreCase("PB")) {
            pendingTransactionDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
            pendingTransactionDialog.setCanceledOnTouchOutside(false);
            pendingTransactionDialog.setContentView(app.alansari.R.layout.generic_single_btn_dialog);
            ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.pm_pay_at_branch_dialog_title));
            ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.pm_pay_at_branch_dialog_text));
            pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remittanceApi();
                    pendingTransactionDialog.dismiss();

                }

            });
            pendingTransactionDialog.show();
        } else if (name.equalsIgnoreCase("BT")) {
            if (dataObject == null) {
                dataObject = new BeneficiaryData();
            }
            if (Validation.isValidString(totalToPay))
                dataObject.getTxnAmountData().setTotalToPay(totalToPay);
            totalToPay = null;
            //dataObject.setPaymentModeData((PaymentModeData).btnBankPay.getTag());
            dataObject.setPaymentModeData((PaymentModeData) (PaymentSelectModeActivity.btnBankPay).getTag());
            intent = new Intent(context, PaymentDetailsSelectBankPaymentActivity.class);
            intent.putExtra(Constants.OBJECT, dataObject);
            intent.putExtra(Constants.SOURCE_TYPE, sourceType);
            intent.putExtra(Constants.IS_WU, isWu);
            intent.putExtra(Constants.CE_SERVICE_ID,ceServiceId);
            intent.putExtra(Constants.TOTAL_RECIEVE_CURRENCY, CcyDesc);
            intent.putExtra(Constants.CCY_CODE,ccyCode);
            intent.putExtra(Constants.CCY_ID,ccyCode);
            context.startActivity(intent);

        } else if (name.equalsIgnoreCase("PP")) {
            remittanceApiGateway();
        }
    }


    private void remittanceApiGateway() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            // dataObject.getTxnAmountData().setTotalToPay(TOTAL_AMOUNT_PP);
            JSONObject input = priorityPayremittanceApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject, "", TOTAL_PRIORITY_PAY_CHARGES_PP, LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.REMITTANCE_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    private void createConditionDialog() {
        priorityConditionsDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
        priorityConditionsDialog.setCanceledOnTouchOutside(false);
        priorityConditionsDialog.setContentView(R.layout.wu_terms_dialog);
        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar_title)).setText("TERMS AND CONDITIONS");
        CommonUtils.setLayoutFont(context, "HelveticaNeue-Medium.ttf", findViewById(R.id.toolbar_title));
        CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", findViewById(app.alansari.R.id.dialog_title), findViewById(R.id.accept_text));
        // tvText.setText(Html.fromHtml(context.getString(R.string.indemnity_form_text), null, new IndemnityActivity.UlTagHandler()));


        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.dialog_title)).setText(Html.fromHtml(getString(R.string.indemnity_form_text)));
        ((ImageView) priorityConditionsDialog.findViewById(app.alansari.R.id.close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priorityConditionsDialog.dismiss();
            }
        });
        priorityConditionsDialog.show();
    }

    private void fetchPromoCode() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.PROMO_CODE_URL, CommonUtils.SERVICE_TYPE.PROMO_CODE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
    }

    private void updateDynamicFields() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        if (additionalInfoFields == null) {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
            return;
        }
        try {
            dynamicEditText = new EditText[additionalInfoFields.size()];
            dynamicInputLayout = new TextInputLayout[additionalInfoFields.size()];
            dynamicLayout.removeAllViews();
            for (int i = 0; i < additionalInfoFields.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(app.alansari.R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(app.alansari.R.id.edit_text);

                updateDynamicField(editText, inputLayout, i == (additionalInfoFields.size() - 1) ? childLayout.findViewById(app.alansari.R.id.divider) : null, additionalInfoFields.get(i), null);
                if (i == additionalInfoFields.size() - 1)
                    childLayout.findViewById(app.alansari.R.id.divider).setVisibility(View.INVISIBLE);

                dynamicEditText[i] = editText;
                dynamicInputLayout[i] = inputLayout;
                dynamicLayout.addView(childLayout);
            }
            dynamicLayout.setVisibility(View.VISIBLE);
            checkLayoutFilledStatus(null, null, false);

            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
        }
    }

    private void updateDynamicField(EditText editText, TextInputLayout inputLayout, View divider, AdditionalInfoData currentView, String errorMessage) {
        if (currentView != null) {
            if (errorMessage == null)
                currentView.setErrorMessage("Invalid " + currentView.getTitle().toLowerCase());
            else
                currentView.setErrorMessage(errorMessage);
            editText.setTag(currentView);
            inputLayout.setTag(currentView);
        } else
            return;

        setMandatory(editText, inputLayout, currentView.getTitle() + (currentView.getIsMandatory().equalsIgnoreCase("1") ? "*" : ""));
        setFieldType(editText, inputLayout, currentView);
    }

    private void setMandatory(EditText editText, TextInputLayout inputLayout, String hintText) {
        editText.setHintTextColor(ContextCompat.getColor(context, app.alansari.R.color.color9B9B9B));
        CommonUtils.setInputTextLayoutColor(context, inputLayout);
        editText.setHint(hintText);
        inputLayout.setHint(null);
    }
    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final AdditionalInfoData currentView) {
        if (currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_DROPDOWN)) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {
            if (currentView.getInputType() != null && currentView.getInputType().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateSingleField(editText, inputLayout);
            }
        });
    }


    private void setFieldTypeCE(final EditText editText, final TextInputLayout inputLayout, final BeneficiaryDynamicFieldsCe currentView) {
        if (currentView.getFieldType().equalsIgnoreCase("D")) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getFieldType().equalsIgnoreCase("T")) {
            if (currentView.getInputType() != null && currentView.getInputType().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
            if (getEditTextLength(editText) != 0)
                setTextLimit(editText, getEditTextLength(editText));
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateSingleFieldCE(editText, inputLayout);
            }
        });
    }

    private void setTextLimit(EditText view, int length) {
        LogUtils.d("ok", "Length");
        view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.valueOf(length))});
    }
    private int getEditTextLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getLength());
        } catch (Exception ex) {
            return 0;
        }
    }
    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnConfirm.setEnabled(false);
    }

    private void selectFromDropdown(int titleId, ArrayList<AdditionalInfoData.ValuesData> list) {
        intent = new Intent(context, SelectItemActivity.class);
        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
        intent.putExtra(Constants.ID, titleId);
        intent.putParcelableArrayListExtra(Constants.LIST, list);
        startActivityForResult(intent, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_ADDITIONAL_FIELD && resultCode == Activity.RESULT_OK && data.getExtras().getInt(Constants.ID, -1) != -1 && data.getParcelableExtra(Constants.OBJECT) != null) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, promocodeLayout.getTop());
                    }
                });
                int fieldPosition = getPositionFromId(String.valueOf(data.getExtras().getInt(Constants.ID, -1)));
                dynamicEditText[fieldPosition].setText(((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getTitle());
                dynamicEditText[fieldPosition].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, ((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getId());
                dynamicInputLayout[fieldPosition].setHint(additionalInfoFields.get(fieldPosition).getTitle());
                //Log.e("jbfchsvhv",""+(((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getTitle()));
            }
            else if (resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    switch (requestCode) {
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_7:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_50:
                            dataObject.setNationality(((CountryData) object).getLatinName());
                            dataObject.setNationalityId(((CountryData) object).getCountryCodeCE());
                            if (position != -1)
                                setDataToEditText(position, ((CountryData) object).getLatinName(), ((CountryData) object).getCountryCodeCE());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_8:
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.smoothScrollTo(0, promocodeLayout.getTop());
                                }
                            });
                            if (position != -1)
                                setDataToEditText(position, ((FundsSourceCeData) object).getName(), ((FundsSourceCeData) object).getId());                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_9:
                            if (position != -1) {
                                int subPosition = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_28));
                                if (subPosition != -1) {
                                    dynamicEditText[subPosition].setText("");
                                    dynamicEditText[subPosition].setHint(beneficiaryDynamicFieldsCeList.get(subPosition).getFieldName());
                                    dynamicInputLayout[subPosition].setHint(null);
                                }
                                purposeId = String.valueOf(((PurposeCeData) object).getId());
                                setDataToEditText(position, ((PurposeCeData) object).getName(), ((PurposeCeData) object).getId());
                            }
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_12:
                            if (position != -1)
                                setDataToEditText(position, ((IdProofCeData) object).getIdTypeDesc(), ((IdProofCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_16:
                            if (position != -1)
                                setDataToEditText(position, ((IdDateTypeCeData) object).getName(), ((IdDateTypeCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_18:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_59:
                            if (position != -1)
                                setDataToEditText(position, ((ResidentialStatusCeData) object).getName(), ((ResidentialStatusCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_28:
                            if (position != -1)
                                setDataToEditText(position, ((SubPurposeCeData) object).getName(), ((SubPurposeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_29:
                            if (position != -1)
                                setDataToEditText(position, ((ProfessionCeData) object).getName(), ((ProfessionCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_32:
                            if (position != -1)
                                setDataToEditText(position, ((CompanyTypeCeData) object).getName(), ((CompanyTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_33:
                            if (position != -1)
                                setDataToEditText(position, ((TradeLicenseTypeCeData) object).getIdTypeDesc(), ((TradeLicenseTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_35:
                            if (position != -1)
                                setDataToEditText(position, ((TradeLicenseDateTypeCeData) object).getName(), ((TradeLicenseDateTypeCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_39:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessActivitiesCeData) object).getName(), ((BusinessActivitiesCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_44:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryBranchCeData) object).getName(), ((BeneficiaryBranchCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_45:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryAccountTypeCeData) object).getAccountType(), ((BeneficiaryAccountTypeCeData) object).getAccountCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_54:
                            if (position != -1)
                                setDataToEditText(position, ((BankPurposeCeData) object).getPurposeDesc(), ((BankPurposeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_86:
                            if (position != -1)
                                setDataToEditText(position, ((DistrictCeData) object).getName(), ((DistrictCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_92:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessActivitiesCeData) object).getName(), ((BusinessActivitiesCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_94:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryTypeCeData) object).getBeneficiaryTypeDesc(), ((BeneficiaryTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                            if (position != -1)
                                setDataToEditText(position, ((ContributionCeData) object).getContributionAmount(), ((ContributionCeData) object).getContributionId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_98:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessTypeCeData) object).getBusinessTypeDesc(), ((BusinessTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_99:
                            if (position != -1)
                                setDataToEditText(position, ((SubBusinessTypeCeData) object).getName(), ((SubBusinessTypeCeData) object).getId());
                            break;
                    }
                    if (position != -1) {
                        dynamicInputLayout[position].setHint(beneficiaryDynamicFieldsCeList.get(position).getFieldName());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setDataToEditText(int position, String text, String code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }    private int getEditTextMinLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getMinLength());
        } catch (Exception ex) {
            return 0;
        }
    }
    private void setDataToEditText(int position, String text, int code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }


    private int getPositionFromId(String id) {
        for (int i = 0; i < additionalInfoFields.size(); i++) {
            if (id.equalsIgnoreCase(additionalInfoFields.get(i).getId()))
                return i;
        }
        return -1;
    }

    private void onSuccess() {
        dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));
        intent = new Intent(context, PaymentModeActivity.class);
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        startActivity(intent);
    }

    void addAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalInfoJsonFormat(), Constants.ADD_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void setError(TextView editText, TextView textView, String msg) {
        editText.setError(msg);
        textView.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
        btnConfirm.setEnabled(false);
    }

    private String checkValue(String value) {
        return value != null ? value : "";
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case PROMO_CODE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<PromoCodeData> promoCodeData = (ArrayList<PromoCodeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PromoCodeData>>() {
                                }.getType());
                                promoCodeAdapter.addArrayList(promoCodeData);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case FETCH_ADDITIONAL_INFO:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                additionalInfoFields = (ArrayList<AdditionalInfoData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AdditionalInfoData>>() {
                                }.getType());
                                if (additionalInfoFields != null && additionalInfoFields.size() > 0) {
                                    findViewById(app.alansari.R.id.additionalinfolable).setVisibility(View.VISIBLE);
                                    updateDynamicFields();
                                } else {
                                    findViewById(app.alansari.R.id.additionalinfolable).setVisibility(View.GONE);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    multiStateView.setVisibility(View.INVISIBLE);
                                    btnConfirm.setEnabled(true);
                                }
                                if (response.has(Constants.PROMO_ACTIVE) && response.getString(Constants.PROMO_ACTIVE).equalsIgnoreCase("Y")) {
                                    findViewById(app.alansari.R.id.promocode_layout).setVisibility(View.VISIBLE);
                                    if (response.getString(Constants.PROMO_LIST_ACTIVE).equalsIgnoreCase("Y")) {
                                        isPromoListActive = true;
                                    } else {
                                        isPromoListActive = false;
                                    }
                                    fetchPromoCode();
                                } else {
                                    isPromoListActive = false;
                                    findViewById(app.alansari.R.id.promocode_layout).setVisibility(View.GONE);
                                }
                            } else {
                                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                multiStateView.setVisibility(View.INVISIBLE);
                                btnConfirm.setEnabled(true);
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));

                        } else {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));
                            //CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case ADD_ADDITIONAL_INFO:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            //onSuccess();
                            dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));
                            dataObject.getPaymentModeData().setId(id);
                            dataObject.getPaymentModeData().setMapping(name);
                            dataObject.getPaymentModeData().setDescription(modeDescription);

                            checkingBanking();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                            String fieldPkId = response.getString(Constants.ID);
                            if (Validation.isValidString(fieldPkId)) {
                                for (int i = 0; i < additionalInfoFields.size(); i++) {
                                    if (additionalInfoFields.get(i).getId().equalsIgnoreCase(fieldPkId)) {
                                        dynamicEditText[i].requestFocus();
                                        setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                        return;
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
/*                if (status == 1 || status == 0) {
                    onSuccess();
                    break;
                }*/
                break;
//-----------------------------------------------Calling Pay At Branch------------------------------
            case REMITTANCE_API:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                //if (dataObjectBeneficiary.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                }.getType());
                              /*  } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }*/
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_PAYMENT_MODE);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    //Log.e("jnsjvbhshvb",""+txnDetailsData.get(0).getInvoiceFlag());
                                    context.startActivity(intent);

                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }
                            CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
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
//---------------------------------------PP---------------------------------------------------------
            case REMITTANCE_API_GATEWAY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                if (dataObject.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                    txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                    }.getType());
                                } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    txnDetailsData.get(0).setTotalTxnAmount(dataObject.getTxnAmountData().getTotalToPay());
                                    intent = new Intent(context, PaymentGatewayActivity.class);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetailsData.get(0).getUrl());
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_PAYMENT_MODE);
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }
                            CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
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


        }
    }

    private JSONObject addAdditionalInfoJsonFormat() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            dataObject.setPromoCode(etPromoCode.getText().toString());
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.MEM_PK_ID, checkValue(CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping())));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));


            jsonObject.put(Constants.CCY_ID, checkValue(ccyId));
            jsonObject.put(Constants.CCY_CODE, checkValue(ccyCode));
            jsonObject.put(Constants.CCY_DESC, checkValue(CcyDesc));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            jsonObject.put(Constants.MODE_NAME, name);
            jsonObject.put(Constants.MODE_PK_ID, id);
            jsonObject.put(Constants.MODE_DESCRIPTION, modeDescription);
            //jsonObject.put("ADDITIONAL_CHARGES_PP", ADDITIONAL_CHARGES_PP);
            jsonObject.put(Constants.ADDITIONAL_CHARGES_PP, ADDITIONAL_CHARGES_PP);



            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            timeStamp = System.currentTimeMillis();
            jsonObject.put(Constants.TIME_STAMP, timeStamp);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            //jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue("1"));
            JSONArray jsonArray = new JSONArray();
            if (additionalInfoFields != null) {
                for (int i = 0; i < additionalInfoFields.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.FIELD_PK_ID, additionalInfoFields.get(i).getId());
                        jsonObjectField.put(Constants.FIELD_VALUE, checkValue(dynamicEditText[i].getText().toString().trim()));
                        if (dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                            jsonObjectField.put(Constants.FIELD_VALUE_CODE, checkValue((dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null) ? dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString() : null));
                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            jsonObject.put(Constants.RESULT, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;
    }

    public void updateMessage(String promoMessage) {
        tvPromoMessage.setText(promoMessage);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    public JSONObject fetchAdditionalInfo(String userId, String countryId, String bankId, String serviceId, String transferType, String arexId, String benId, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexId);
            jsonObject.put(Constants.BANK_ID, bankId);
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.COUNTRY_ID, countryId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, benId);
            jsonObject.put(Constants.MODE_NAME, name);
            jsonObject.put(Constants.MODE_PK_ID, id);
            jsonObject.put(Constants.SERVICE_ID, serviceId);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);

            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAdditionalInfo:-  " + jsonObject.toString());
        return jsonObject;
    }
   /* public JSONObject fetchAdditionalInfoCE(String ceServiceId, String agentCode, String countryCode,String benId,String mempkId,String transferType, String userId,  String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.CE_SERVICE_ID,ceServiceId);
            jsonObject.put(Constants.DEST_AGENT_CODE,agentCode);
            jsonObject.put(Constants.DEST_COUNTRY_CODE,countryCode);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, benId);
            jsonObject.put(Constants.MEM_PK_ID, checkValue(mempkId));
            jsonObject.put(Constants.MODE_NAME, name);
            jsonObject.put(Constants.MODE_PK_ID, id);
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "fetchAdditionalInfo:-  " + jsonObject.toString());
        return jsonObject;
    }*/



    public JSONObject remittanceApiCall(String userId, String arexMemPkId, String ceMemPkId, BeneficiaryData dataObject, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, checkValue(arexMemPkId));
            jsonObject.put(Constants.CE_MEM_PK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.ADDITIONAL_INFO_TITLE, checkValue(dataObject.getAdditionalFieldTitle()));
            jsonObject.put(Constants.ADDITIONAL_INFO_VALUE, checkValue(dataObject.getAdditionalFieldValue()));
            jsonObject.put(Constants.MODE_PK_ID, checkValue(dataObject.getPaymentModeData().getId()));
            jsonObject.put(Constants.MODE_NAME, checkValue(dataObject.getPaymentModeData().getMapping()));
            jsonObject.put(Constants.MODE_DESCRIPTION, checkValue(dataObject.getPaymentModeData().getDescription()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));

            /*jsonObject.put(Constants.CCY_ID, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.CCY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.CCY_DESC, checkValue(CcyDesc));*/

            jsonObject.put(Constants.CCY_ID, checkValue(ccyId));
            jsonObject.put(Constants.CCY_CODE, checkValue(ccyCode));
            jsonObject.put(Constants.CCY_DESC, checkValue(CcyDesc));

            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));
            jsonObject.put(Constants.BRANCH_ID, checkValue(dataObject.getBranchData().getId()));
            jsonObject.put(Constants.TIME_STAMP, checkValue(dataObject.getAdditionalInfoTimeStamp()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            if (dataObject.getPromoCode() != null && !TextUtils.isEmpty(dataObject.getPromoCode())) {
                jsonObject.put(Constants.PROMO_CODE, dataObject.getPromoCode());
            }

            if (dataObject.getPgsFlag() != null && !TextUtils.isEmpty(dataObject.getPgsFlag())) {
                jsonObject.put(Constants.PGS_FLAG, checkValue(dataObject.getPgsFlag()));
                jsonObject.put(Constants.PGS_BANK_CODE, checkValue(dataObject.getPgsBankCode()));
            } else {
                jsonObject.put(Constants.PGS_FLAG, "N");
                jsonObject.put(Constants.PGS_BANK_CODE, "");
            }

            if (dataObject.getUserAccountData() != null) {
                jsonObject.put(Constants.ACC_FK_ID, checkValue(dataObject.getUserAccountData().getId()));
                jsonObject.put(Constants.USER_ACC_NUM, checkValue(dataObject.getUserAccountData().getAccountNumber()));
                jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, checkValue(dataObject.getUserAccountData().getAccountName()));
            }
            if (dataObject.getAaeBankData() != null) {
                jsonObject.put(Constants.AAE_BANK_NAME, checkValue(dataObject.getAaeBankData().getBankName()));
                jsonObject.put(Constants.AAE_ACCOUNT_NUM, checkValue(dataObject.getAaeBankData().getAccountNumber()));
                jsonObject.put(Constants.AAE_ACCOUNT_NAME, checkValue(dataObject.getAaeBankData().getAccountName()));
                jsonObject.put(Constants.AREX_CODE, checkValue(dataObject.getAaeBankData().getAREXCode()));
            }
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "remittancePayAtBranchApi:-  " + jsonObject.toString());
        return jsonObject;
    }


    public JSONObject priorityPayremittanceApi(String userId, String arexMemPkId, String ceMemPkId, BeneficiaryData dataObject, String TOTAL_AMOUNT_PPs, String TOTAL_PRIORITY_PAY_CHARGES_PP, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObject.put(Constants.AREX_MEM_PK_ID, checkValue(arexMemPkId));
            jsonObject.put(Constants.CE_MEM_PK_ID, checkValue(ceMemPkId));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.ADDITIONAL_INFO_TITLE, checkValue(dataObject.getAdditionalFieldTitle()));
            jsonObject.put(Constants.ADDITIONAL_INFO_VALUE, checkValue(dataObject.getAdditionalFieldValue()));

            jsonObject.put(Constants.MODE_PK_ID, checkValue(dataObject.getPaymentModeData().getId()));
            jsonObject.put(Constants.MODE_NAME, checkValue(dataObject.getPaymentModeData().getMapping()));
            jsonObject.put(Constants.MODE_DESCRIPTION, checkValue(dataObject.getPaymentModeData().getDescription()));
            jsonObject.put("ADDTL_CHARGES", ADDITIONAL_CHARGES_PP);



            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));


            jsonObject.put(Constants.CCY_ID, checkValue(ccyId));
            jsonObject.put(Constants.CCY_CODE, checkValue(ccyCode));
            jsonObject.put(Constants.CCY_DESC, checkValue(CcyDesc));


            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));
            jsonObject.put(Constants.BRANCH_ID, checkValue(dataObject.getBranchData().getId()));
            jsonObject.put(Constants.TIME_STAMP, checkValue(dataObject.getAdditionalInfoTimeStamp()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            //jsonObject.put(Constants.TOTAL_AMOUNT_PP, checkValue(TOTAL_AMOUNT_PP));
            jsonObject.put(Constants.TOTAL_AMOUNT_PP, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, checkValue(TOTAL_PRIORITY_PAY_CHARGES_PP));

            if (dataObject.getUserAccountData() != null) {
                jsonObject.put(Constants.ACC_FK_ID, checkValue(dataObject.getUserAccountData().getId()));
                jsonObject.put(Constants.USER_ACC_NUM, checkValue(dataObject.getUserAccountData().getAccountNumber()));
                jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, checkValue(dataObject.getUserAccountData().getAccountName()));
            }
            if (dataObject.getAaeBankData() != null) {
                jsonObject.put(Constants.AAE_BANK_NAME, checkValue(dataObject.getAaeBankData().getBankName()));
                jsonObject.put(Constants.AAE_ACCOUNT_NUM, checkValue(dataObject.getAaeBankData().getAccountNumber()));
                jsonObject.put(Constants.AAE_ACCOUNT_NAME, checkValue(dataObject.getAaeBankData().getAccountName()));
                jsonObject.put(Constants.AREX_CODE, checkValue(dataObject.getAaeBankData().getAREXCode()));
            }
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "remittancePayAtBranchApi:-  " + jsonObject.toString());
        return jsonObject;
    }


}