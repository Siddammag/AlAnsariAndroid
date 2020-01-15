package app.alansari.modules.sendmoney;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.DashboardActivity;
import app.alansari.PaymentGatewayActivity2;
import app.alansari.R;
import app.alansari.TransactionHistroyCompletedActivity;
import app.alansari.TransactionTravelCompDetailsActivity;
import app.alansari.TransactionTravelDetailsActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.PaymentModeData;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsCreditCardData;
import app.alansari.models.TxnDetailsData;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.models.WuRateChargesResponse;
import app.alansari.models.getCharges.ResultItem;
import app.alansari.models.transactionhistroy.TxnDetailsHistroyTravelCard;
import app.alansari.models.travalcardvalidateflag.TravelCardAdapterItem;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;
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
 * AlAnsari
 */
public class PaymentModeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    private Intent intent;
    private Context context;
    private Toolbar toolbar;
    private String sourceType, totalToPay;
    private String wuCardNo;
    private String arexUserId;
    private String benefType;
    private BeneficiaryData dataObjectBeneficiary;
    private WUBeneficiaryData wuBeneficiaryData;
    private TravelCardInfo dataObject;
    private ArrayList<ResultItem> travelCardResultModels;
    private ArrayList<TravelCardAdapterItem> itemList;
    private JSONObject sendWuMoneyResponse;
    private String TOTAL_WU_POINTS = "0";
    private WuRateChargesResponse wuRateChargesResponse;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private Dialog pendingTransactionDialog;
    private Dialog priorityConditionsDialog;
    private TextView tvTotalToPay, tvTotalToPayCurrencyCode;
    private TextView btnBranchPay, btnBankPay, btnPriorityPay;
    private TextView tvBranchPayLabel, tvBranchPayDesc, tvBankPayLabel, tvBankPayDesc, tvPriorityPayLabel, tvPriorityPayDesc;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String isWu = "N";
    private String isWc="NO";
    private long timeStamp;
    private ImageView ivChecked;
    private boolean isAgreed;
    private String sessionTime;
    private String transactionPurpose,sourceOfPrupose;
    private String vatCharges,vatDiscount;
    private ArrayList<PaymentModeData> paymentModeData;
    private String charge;
    private String diagAedAmount, diagCharges, diagTotalPay;
    private FirebaseAnalytics mFirebaseAnalytics;


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
        setContentView(app.alansari.R.layout.payment_mode_activity);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Payment Mode");
        ((ImageView) findViewById(app.alansari.R.id.close)).setVisibility(View.VISIBLE);
        ((ImageView) findViewById(app.alansari.R.id.close)).setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
        if (getIntent().getExtras() != null) {
            sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            if (getIntent().getStringExtra(Constants.IS_WU) != null && getIntent().getStringExtra(Constants.IS_WU).equalsIgnoreCase("Y")) {
                isWu = getIntent().getStringExtra(Constants.IS_WU);
                arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
                wuBeneficiaryData = getIntent().getExtras().getParcelable(Constants.OBJECT);
                wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
                TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
                wuRateChargesResponse = getIntent().getExtras().getParcelable(Constants.wuRateChargesResponse);
                timeStamp = getIntent().getLongExtra(Constants.TIME_STAMP, 0);
                benefType = getIntent().getStringExtra(Constants.BENEF_TYPE);
                try {
                    sendWuMoneyResponse = new JSONObject(getIntent().getStringExtra(Constants.WU_RATE_CHARGE_RESPONSE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(getIntent().getStringExtra(Constants.IS_TRAVEL) != null && getIntent().getStringExtra(Constants.IS_TRAVEL).equalsIgnoreCase("Y")){
                isWc="YES";
                dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
                travelCardResultModels = getIntent().getExtras().getParcelableArrayList(Constants.RESPONSE_INTENT);
                itemList = getIntent().getExtras().getParcelableArrayList(Constants.PROFILE_UPDATE_FLAG);
                transactionPurpose=getIntent().getStringExtra(Constants.Transaction_Purpose);
                sourceOfPrupose= getIntent().getStringExtra(Constants.Source_Of_Prupose);
                vatCharges=getIntent().getStringExtra(Constants.VAT_CHARGES);
                vatDiscount=getIntent().getStringExtra(Constants.VAT_DISCOUNT);
                charge=getIntent().getStringExtra(Constants.CHARGES);
                diagAedAmount = getIntent().getStringExtra("totalToPay");
                diagCharges = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
                diagTotalPay = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
                TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
                TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);


            } else {
                dataObjectBeneficiary = getIntent().getExtras().getParcelable(Constants.OBJECT);
            }
            TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);

            if (dataObjectBeneficiary != null) {
                tvTotalToPay.setText(CommonUtils.addCommaToString(dataObjectBeneficiary.getTxnAmountData().getTotalToPay()));
                tvTotalToPayCurrencyCode.setText("AED");
                fetchPaymentModes();
                return;
            } else if (wuBeneficiaryData != null) {
                try {
                    tvTotalToPay.setText(CommonUtils.addCommaToString(sendWuMoneyResponse.getString(Constants.GROSS_TOTAL_AMOUNT)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvTotalToPayCurrencyCode.setText("AED");
                fetchWuPaymentModes();
                return;
            } else if (dataObject != null) {
                try {
                    tvTotalToPay.setText(CommonUtils.addCommaToString(getIntent().getStringExtra("totalToPay")));
                    tvTotalToPayCurrencyCode.setText("AED");
                    fetchWcPaymentModes();
                    return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        finish();
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        tvBranchPayLabel = (TextView) findViewById(app.alansari.R.id.branch_pay_label);
        tvBranchPayDesc = (TextView) findViewById(app.alansari.R.id.branch_pay_desc);
        tvBankPayLabel = (TextView) findViewById(app.alansari.R.id.bank_pay_label);
        tvBankPayDesc = (TextView) findViewById(app.alansari.R.id.bank_pay_desc);
        tvPriorityPayLabel = (TextView) findViewById(app.alansari.R.id.priority_pay_label);
        tvPriorityPayDesc = (TextView) findViewById(app.alansari.R.id.priority_pay_desc);

        btnBranchPay = (TextView) findViewById(app.alansari.R.id.branch_pay_continue_btn);
        btnBankPay = (TextView) findViewById(app.alansari.R.id.bank_pay_continue_btn);
        btnPriorityPay = (TextView) findViewById(app.alansari.R.id.priority_pay_continue_btn);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        btnBranchPay.setOnClickListener(this);
        btnBankPay.setOnClickListener(this);
        btnPriorityPay.setOnClickListener(this);
    }
    //1 replaceAll(",", "") TOTAL_VALUE_AED
    private void fetchPaymentModes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //here
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPaymentModes(CommonUtils.getUserId(), dataObjectBeneficiary.getTxnAmountData().getYouSend(), dataObjectBeneficiary.getTxnAmountData().getTotalToPay(),dataObjectBeneficiary.getTxnAmountData().getFee(), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_PAYMENT_MODES_URL, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }
    //2 replaceAll(",", "")
    private void fetchWuPaymentModes() {
        try {
            //here
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuPaymentModes(CommonUtils.getUserId(), sendWuMoneyResponse.getString(Constants.ORIGINAL_PRINCIPLE_AMOUNT), sendWuMoneyResponse.getString(Constants.GROSS_TOTAL_AMOUNT),sendWuMoneyResponse.getString(Constants.CHARGES), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_PAYMENT_MODES_URL, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
            } else {
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //3 replaceAll(",", "")
    private void fetchWcPaymentModes(){
        try {
            //here
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWcPaymentModes(CommonUtils.getUserId(), travelCardResultModels.get(0).getAEDAMOUNT(), travelCardResultModels.get(0).getAEDAMOUNT(),charge, LogoutCalling.getDeviceID(context), sessionTime), Constants.GET_PAYMENT_MODES_WC_URL, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES, Request.Method.POST, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_PAYMENT_MODES.toString());
            } else {
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                if (isWu.equalsIgnoreCase("Y")) {
                    fetchWuPaymentModes();
                }else if(isWc.equalsIgnoreCase("YES")){
                    fetchWcPaymentModes();
                } else {
                    fetchPaymentModes();
                }
                break;
            case app.alansari.R.id.branch_pay_continue_btn:
                if (Validation.isValidString(totalToPay))
                    dataObjectBeneficiary.getTxnAmountData().setTotalToPay(totalToPay);
                totalToPay = null;
                pendingTransactionDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
                pendingTransactionDialog.setCanceledOnTouchOutside(false);
                pendingTransactionDialog.setContentView(app.alansari.R.layout.generic_single_btn_dialog);
                ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.pm_pay_at_branch_dialog_title));
                ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.pm_pay_at_branch_dialog_text));
                pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isWu.equalsIgnoreCase("Y")) {
                            wuPerformSendMoney();
                        }else if(isWc.equalsIgnoreCase("YES")){
                            wcPerformSendMoney();
                        } else {
                            dataObjectBeneficiary.setPaymentModeData((PaymentModeData) btnBranchPay.getTag());
                            if (sourceType.equalsIgnoreCase(Constants.TYPE_SEND_MONEY))
                                remittanceApi();
                            else
                                remittanceCreditCardApi();
                        }

                        pendingTransactionDialog.dismiss();
                    }
                });
                pendingTransactionDialog.show();
                break;
            case app.alansari.R.id.bank_pay_continue_btn:
                if(isWc.equalsIgnoreCase("YES")) {
                    intent = new Intent(context, PaymentDetailsBankPaymentActivity.class);
                    intent.putExtra(Constants.OBJECT, dataObject);
                    intent.putExtra(Constants.IS_TRAVEL, "y");
                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                    intent.putExtra(Constants.RESPONSE_INTENT, travelCardResultModels);
                    intent.putExtra(Constants.PROFILE_UPDATE_FLAG, itemList);
                    intent.putExtra(Constants.Transaction_Purpose, transactionPurpose);
                    intent.putExtra(Constants.Source_Of_Prupose, sourceOfPrupose);
                    intent.putExtra(Constants.VAT_CHARGES,vatCharges);
                    intent.putExtra(Constants.VAT_DISCOUNT,vatDiscount);
                    intent.putExtra("totalToPay",getIntent().getStringExtra("totalToPay"));

                }else {
                    if (dataObjectBeneficiary == null) {
                        dataObjectBeneficiary = new BeneficiaryData();
                    }
                    if (Validation.isValidString(totalToPay))
                        dataObjectBeneficiary.getTxnAmountData().setTotalToPay(totalToPay);
                    totalToPay = null;
                    dataObjectBeneficiary.setPaymentModeData((PaymentModeData) btnBankPay.getTag());
                    intent = new Intent(context, PaymentDetailsBankPaymentActivity.class);
                    intent.putExtra(Constants.OBJECT, dataObjectBeneficiary);
                    intent.putExtra(Constants.SOURCE_TYPE, sourceType);
                    intent.putExtra(Constants.IS_WU, isWu);
                    if (isWu.equalsIgnoreCase("Y")) {
                        intent.putExtra(Constants.BENEF_TYPE, benefType);
                        intent.putExtra(Constants.WU_RATE_CHARGE_RESPONSE, sendWuMoneyResponse.toString());
                        intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
                        intent.putExtra(Constants.TIME_STAMP, timeStamp);
                        intent.putExtra(Constants.OBJECT, wuBeneficiaryData);
                        intent.putExtra(Constants.wuRateChargesResponse, wuRateChargesResponse);
                        intent.putExtra(Constants.MODE_DESCRIPTION, getModeOfDescription("BANK TRANSFER"));
                        intent.putExtra(Constants.WU_PROMO_CODE, getIntent().getStringExtra(Constants.WU_PROMO_CODE));
                        intent.putExtra(Constants.BENEF_PK_ID, getIntent().getStringExtra(Constants.BENEF_PK_ID));
                        intent.putExtra(Constants.TOTAL_WU_POINTS, getIntent().getStringExtra(Constants.TOTAL_WU_POINTS));
                        intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE, getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE));
                    }
                }
                context.startActivity(intent);
                break;

            case app.alansari.R.id.priority_pay_continue_btn:
                PaymentModeData paymentMode = (PaymentModeData) btnPriorityPay.getTag();
                if(isWc.equalsIgnoreCase("YES")) {
                    pendingTransactionDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
                    pendingTransactionDialog.setCanceledOnTouchOutside(false);
                    pendingTransactionDialog.setContentView(app.alansari.R.layout.pp_mode_dialog_box);
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.pm_priority_pay_dialog_title));
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.pm_priority_transaction_pending_text));
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.sending_amount)).setText(diagAedAmount + " AED");
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.charge)).setText(diagCharges + " AED");
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.txn_amount)).setText(diagTotalPay + " AED");
                    pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pendingTransactionDialog.dismiss();
                        }
                    });
                    pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // dataObjectBeneficiary.setPaymentModeData((PaymentModeData) btnPriorityPay.getTag());
                            pendingTransactionDialog.dismiss();
                            priorityConditionsDialog.show();
                        }
                    });
                    pendingTransactionDialog.show();





                }else {
                    if (paymentMode != null) {
                        if (paymentMode.getStatus() != null && paymentMode.getStatus().equalsIgnoreCase("-1")) {
                            CommonUtils.showLimitDialog(context, paymentMode.getMessage());
                            return;
                        }

                        if (Validation.isValidString(paymentMode.getAdditionalCharges()) && Validation.isValidString(paymentMode.getTotalTxnAmount())) {
                            if (!Validation.isValidString(totalToPay))
                                totalToPay = dataObjectBeneficiary.getTxnAmountData().getTotalToPay();
                            dataObjectBeneficiary.getTxnAmountData().setTotalToPay(paymentMode.getTotalTxnAmount());
                        }

                        pendingTransactionDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
                        pendingTransactionDialog.setCanceledOnTouchOutside(false);
                        pendingTransactionDialog.setContentView(app.alansari.R.layout.pp_mode_dialog_box);
                        ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.pm_priority_pay_dialog_title));
                        ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.pm_priority_transaction_pending_text));

                        ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.sending_amount)).setText(dataObjectBeneficiary.getTxnAmountData().getYouSend() + " AED");
                        String totalCharge = null;
                        if (Validation.isValidRate(dataObjectBeneficiary.getTxnAmountData().getFee())) {
                            if (Validation.isValidRate(paymentMode.getAdditionalCharges()))
                                totalCharge = String.valueOf(String.format("%.2f", (Float.valueOf(dataObjectBeneficiary.getTxnAmountData().getTotalToPay()) - Float.valueOf(dataObjectBeneficiary.getTxnAmountData().getYouSend()))));
                            else
                                totalCharge = dataObjectBeneficiary.getTxnAmountData().getFee();
                        } else {
                            if (Validation.isValidRate(paymentMode.getAdditionalCharges()))
                                totalCharge = String.valueOf(Float.valueOf(dataObjectBeneficiary.getTxnAmountData().getFee()) + Float.valueOf(paymentMode.getAdditionalCharges()));
                            else
                                totalCharge = "0";
                        }
                        ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.charge)).setText(TOTAL_PRIORITY_PAY_CHARGES_PP + " AED");
                        ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.txn_amount)).setText(TOTAL_AMOUNT_PP + " AED");
                        pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pendingTransactionDialog.dismiss();
                            }
                        });
                        pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataObjectBeneficiary.setPaymentModeData((PaymentModeData) btnPriorityPay.getTag());
                                /*remittanceApiGateway();*/
                                pendingTransactionDialog.dismiss();
                                priorityConditionsDialog.show();
                            }
                        });
                        pendingTransactionDialog.show();
                    }
                }
                break;
            case app.alansari.R.id.accept_image:
                if (isAgreed) {
                    ivChecked.setImageResource(0);
                    isAgreed = false;
                } else {
                    ivChecked.setImageResource(R.drawable.svg_success);
                    isAgreed = true;
                }
                break;
            case R.id.submit_btn:
                if (isAgreed) {
                    if(isWc.equalsIgnoreCase("YES")) {
                        remittanceApiGatewayTravelCardReload();
                    }else
                        remittanceApiGateway();
                    priorityConditionsDialog.dismiss();
                } else
                    Toast.makeText(context, context.getResources().getString(R.string.error_indemnity_form_text), Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_btn:
                priorityConditionsDialog.dismiss();
                break;
            case app.alansari.R.id.close:
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
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
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case REMITTANCE_API_GATEWAY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                if (dataObjectBeneficiary.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                    txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                    }.getType());
                                } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    txnDetailsData.get(0).setTotalTxnAmount(dataObjectBeneficiary.getTxnAmountData().getTotalToPay());
                                    if(isWc.equalsIgnoreCase("YES"))
                                        txnDetailsData.get(0).setServiceType("AREX");
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
            case REMITTANCE_API:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                if (dataObjectBeneficiary.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                    txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                    }.getType());
                                } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    mFirebaseAnalytics.logEvent("Remittance_PayAtBranch_Complete", null);
                                    Log.i("Remit_PayAtBranch_Comp", "Success in Remittance_PayAtBranch_Complete ");

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

            case REMITTANCE_CREDIT_CARD_API:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsCreditCardData> txnDetailsCreditCardData = (ArrayList<TxnDetailsCreditCardData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCreditCardData>>() {
                                }.getType());
                                ArrayList<TxnDetailsData> txnDetailsData = CommonUtils.getTxnDetailsDataFromCreditCard(txnDetailsCreditCardData);
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {

                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_PAYMENT_MODE);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CREDIT_CARD);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    // Log.e("jnsjvbhshvb2",""+txnDetailsData.get(0).getInvoiceFlag());
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
            case FETCH_PAYMENT_MODES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<PaymentModeData> paymentModeData = (ArrayList<PaymentModeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PaymentModeData>>() {
                                }.getType());
                                if (paymentModeData != null && paymentModeData.size() > 0) {
                                    updateData(paymentModeData);
                                } else {
                                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                                }
                            } else {
                                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                            }
                        } else {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvEmpty, null, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case WU_PERFORM_SEND_MONEY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                }.getType());
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_PAYMENT_MODE);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    context.startActivity(intent);
                                    // Log.e("jnsjvbhshvb3",""+txnDetailsData.get(0).getInvoiceFlag());
                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }
                            CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
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
            case SUBMIT_WIRE_CARD_RELOAD_EXT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData = null;
                                txnDetailsData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {}.getType());

                                //txnDetailsData = CommonUtils.getTxnDetailsDataTravelCard(txnDetailsTravelCardReload);
                                Log.e("kfcsdjfbcjh", "" + txnDetailsData.size());
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    /*mFirebaseAnalytics.logEvent("WC_PayAtBranch_Complete", null);
                                    Log.i("WC_PayAtBranch_Complete", "Success in PayAtBranch_Complete ");
                                    */intent = new Intent(context, TransactionTravelCompDetailsActivity.class);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
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
            //-------------------------------------------------PP----------------------------------------------
          /*  case REMITTANCE_API_GATEWAY:
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
                break;*/
            case REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                //if (dataObjectBeneficiary.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                }.getType());
                               /* } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }*/
                                if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    // txnDetailsData.get(0).setTotalTxnAmount(dataObjectBeneficiary.getTxnAmountData().getTotalToPay());
                                    if(isWc.equalsIgnoreCase("YES"))
                                        txnDetailsData.get(0).setServiceType("AREX");
                                    intent = new Intent(context, PaymentGatewayActivity2.class);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetailsData.get(0).getUrl());
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_PAYMENT_MODE);
                                    //intent.putExtra(Constants.SOURCE, Constants.TYPE_SEND_MONEY);
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

    private void updateData(ArrayList<PaymentModeData> paymentModeData) {
        try {
            if (paymentModeData != null) {
                this.paymentModeData = paymentModeData;
                findViewById(app.alansari.R.id.branch_pay_c_layout).setVisibility(View.GONE);
                findViewById(app.alansari.R.id.bank_pay_c_layout).setVisibility(View.GONE);
                findViewById(app.alansari.R.id.priority_pay_c_layout).setVisibility(View.GONE);
                for (int i = 0; i < paymentModeData.size(); i++) {
                    if (paymentModeData.get(i).getStatus().equalsIgnoreCase("1")) {
                        if (paymentModeData.get(i).getId().equalsIgnoreCase("1")) {
                            tvBranchPayLabel.setText(paymentModeData.get(i).getName());
                            tvBranchPayDesc.setText(paymentModeData.get(i).getDescription());
                            btnBranchPay.setTag(paymentModeData.get(i));
                            findViewById(app.alansari.R.id.branch_pay_c_layout).setVisibility(View.VISIBLE);
                        } else if (paymentModeData.get(i).getId().equalsIgnoreCase("2")) {
                            tvBankPayLabel.setText(paymentModeData.get(i).getName());
                            tvBankPayDesc.setText(paymentModeData.get(i).getDescription());
                            btnBankPay.setTag(paymentModeData.get(i));
                            findViewById(app.alansari.R.id.bank_pay_c_layout).setVisibility(View.VISIBLE);
                        } else if (paymentModeData.get(i).getId().equalsIgnoreCase("3") && sourceType.equalsIgnoreCase(Constants.TYPE_SEND_MONEY)) {
                            tvPriorityPayLabel.setText(paymentModeData.get(i).getName());
                            tvPriorityPayDesc.setText(paymentModeData.get(i).getDescription());
                            btnPriorityPay.setTag(paymentModeData.get(i));
                            findViewById(app.alansari.R.id.priority_pay_c_layout).setVisibility(View.VISIBLE);
                            createConditionDialog(paymentModeData.get(i).getMessage());
                        }
                    }
                }
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }
    private void wuPerformSendMoney() {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String WU_PROMO_CODE = getIntent().getStringExtra(Constants.WU_PROMO_CODE);
                String MODE_PK_ID = "1";
                String BENEF_TYPE = benefType;
                String BENEF_PK_ID = getIntent().getStringExtra(Constants.BENEF_PK_ID);
                if (BENEF_PK_ID == null) {
                    BENEF_PK_ID = "";
                }
                String WU_LOOKUP_PROMO_CODE = "";
                String TRANSFER_TYPE = "CP";
                String MODE_NAME = "PB";
                String IS_WU_LOOKUP_PROMO_CODE_SELECTED = "";
                String SERVICE_TYPE = "WU";
                String WU_SERVICE_TYPE = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
                String PGS_FLAG = "N";
                String PGS_BANK_CODE = "";
                String TERMS_AND_CONDITION_FLAG = "true";
                String TEST_QUESTION_FLAG = wuRateChargesResponse.getISTESTQUESTIONAVAILABLE();
                String TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
                if (TEST_QUESTION_FLAG == null) {
                    TEST_QUESTION_FLAG = "P";
                }
                String PROMO_DISCOUNT_AMOUNT = wuRateChargesResponse.getPROMODISCOUNTAMOUNT();
                if (PROMO_DISCOUNT_AMOUNT == null) {
                    PROMO_DISCOUNT_AMOUNT = "0.00";
                }
                JSONObject input = new APIRequestParams().wuPerformSendMoney(CommonUtils.getUserId(), arexUserId, wuBeneficiaryData.getReceiverCountryCode(),
                        wuBeneficiaryData.getArexCountryCode(), WU_SERVICE_TYPE, wuBeneficiaryData.getReceiverCurrencyCode(), wuBeneficiaryData.getArexCurrencyCode(),
                        wuRateChargesResponse.getREFERENCENO(), SERVICE_TYPE, TRANSFER_TYPE, MODE_PK_ID, MODE_NAME, getModeOfDescription("PAY AT BRANCH"),
                        sendWuMoneyResponse.getString(Constants.ORIGINAL_PRINCIPLE_AMOUNT), wuRateChargesResponse.getGROSSTOTALAMOUNT(), wuRateChargesResponse.getDESTPRINCIPALAMOUNT(),
                        sendWuMoneyResponse.getString(Constants.RATE), sendWuMoneyResponse.getString(Constants.CHARGES), wuRateChargesResponse.getVATCHARGES(),
                        PROMO_DISCOUNT_AMOUNT, timeStamp, PGS_FLAG, PGS_BANK_CODE, TERMS_AND_CONDITION_FLAG, WU_PROMO_CODE, wuRateChargesResponse.getTAXRATE(),
                        wuRateChargesResponse.getTAXWORKSHEET(), IS_WU_LOOKUP_PROMO_CODE_SELECTED, wuRateChargesResponse.getFEEENQTXNTYPE(), WU_LOOKUP_PROMO_CODE,
                        BENEF_PK_ID, TOTAL_WU_POINTS, TEST_QUESTION_FLAG, BENEF_TYPE, LogoutCalling.getDeviceID(context), sessionTime);
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.WU_PERFORM_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.WU_PERFORM_SEND_MONEY, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.WU_PERFORM_SEND_MONEY.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.WU_PERFORM_SEND_MONEY.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.WU_PERFORM_SEND_MONEY.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createConditionDialog(String message) {
        priorityConditionsDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
        priorityConditionsDialog.setCanceledOnTouchOutside(false);
        priorityConditionsDialog.setContentView(R.layout.priority_condition_dialog);
        ((Toolbar) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar)).setBackground(getResources().getDrawable(R.drawable.login_register_header_bg));
        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.text)).setText(message);
        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar_title)).setText("TERMS AND CONDITIONS");
        ivChecked = (ImageView) priorityConditionsDialog.findViewById(R.id.accept_image);
        ivChecked.setOnClickListener(this);
        priorityConditionsDialog.findViewById(R.id.accept_text).setOnClickListener(this);
        priorityConditionsDialog.findViewById(R.id.submit_btn).setOnClickListener(this);
        priorityConditionsDialog.findViewById(R.id.back_btn).setOnClickListener(this);
    }

    private JSONObject getResponse() throws JSONException {
        return new JSONObject("{\n" +
                "  \"STATUS_CODE\": 257,\n" +
                "  \"MESSAGE\": \"SUCCESS\",\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"RESULT\": [\n" +
                "    {\n" +
                "      \"MODE_PK_ID\": 1,\n" +
                "      \"MODE_NAME\": \"PAY AT BRANCH\",\n" +
                "      \"MAPPING\": \"PB\",\n" +
                "      \"STATUS\": \"1\",\n" +
                "      \"MODE_DESCRIPTION\": \"simply dummy text of the printing and type setting industry\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"MODE_PK_ID\": 2,\n" +
                "      \"MODE_NAME\": \"BANK TRANSFER\",\n" +
                "      \"MAPPING\": \"BT\",\n" +
                "      \"STATUS\": \"1\",\n" +
                "      \"MODE_DESCRIPTION\": \"simply dummy text of the printing and type setting industry\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"MODE_PK_ID\": 3,\n" +
                "      \"MODE_NAME\": \"PRIORITY PAY\",\n" +
                "      \"MAPPING\": \"PP\",\n" +
                "      \"STATUS\": \"1\",\n" +
                "      \"MODE_DESCRIPTION\": \"simply dummy text of the printing and type setting industry\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

    private String getResponseGatewayAPI() {
        try {
            return "{\n" +
                    "  \"CHANNEL_ID\": null,\n" +
                    "  \"TOTAL_VALUE_AED\": null,\n" +
                    "  \"RATE\": null,\n" +
                    "  \"BRANCH_CODE\": null,\n" +
                    "  \"CREATED_DATE\": 1487593759000,\n" +
                    "  \"CREATED_BY\": null,\n" +
                    "  \"REM_TXN_PK_ID\": 58,\n" +
                    "  \"TXN_TYPE\": \"TT\",\n" +
                    "  \"REM_TXN_REF_NUM\": null,\n" +
                    "  \"TXN_STATUS\": \"PENDING\",\n" +
                    "  \"MODIFIED_DATE\": null,\n" +
                    "  \"SERVICE_TYPE\": \"AREX\",\n" +
                    "  \"TRANSACTION_DATA\": null,\n" +
                    "  \"BENEFICIARY_DATA\": null,\n" +
                    "  \"TRANSACTION_DATA_TT\": {\n" +
                    "    \"COUNTRY_CODE\": null,\n" +
                    "    \"STATUS\": null,\n" +
                    "    \"BANK_NAME\": \"SYNDICATE BANK\",\n" +
                    "    \"TOTAL_VALUE_AED\": \"500\",\n" +
                    "    \"TXN_AMOUNT\": \"84515\",\n" +
                    "    \"RATE\": \"0\",\n" +
                    "    \"CHARGE_AMOUNT\": \"33\",\n" +
                    "    \"COMM_AMOUNT\": null,\n" +
                    "    \"DISC_AMOUNT\": null,\n" +
                    "    \"ROUNDED_OFF_AMOUNT\": null,\n" +
                    "    \"NET_TOTAL\": null,\n" +
                    "    \"CCY_CODE\": \"26\",\n" +
                    "    \"CCY_DESC\": \"INR\",\n" +
                    "    \"BRANCH_TYPE\": null,\n" +
                    "    \"BRANCH_CODE\": \"0122\",\n" +
                    "    \"BRANCH_NAME\": \"BYNDOOR\",\n" +
                    "    \"CREATED_DATE\": 1487593759000,\n" +
                    "    \"BANK_CODE\": \"9999042260026\",\n" +
                    "    \"COUNTRY_DESC\": null,\n" +
                    "    \"CREATED_BY\": null,\n" +
                    "    \"REM_TT_SALE_PK_ID\": 51,\n" +
                    "    \"COST_PRICE\": null,\n" +
                    "    \"REM_TXN_FK_ID\": 58,\n" +
                    "    \"SALE_TYPE\": null\n" +
                    "  },\n" +
                    "  \"BENEFICIARY_DATA_TT\": {\n" +
                    "    \"BANK_NAME\": null,\n" +
                    "    \"BRANCH_NAME\": null,\n" +
                    "    \"ID_NUMBER\": null,\n" +
                    "    \"PLACE_OF_ISSUE\": null,\n" +
                    "    \"DATE_OF_ISSUE\": null,\n" +
                    "    \"BENF_NAME_EN\": \"YUSUF JI IMRAN\",\n" +
                    "    \"BENF_NAME_AR\": \" \",\n" +
                    "    \"BENF_CONTACT_NUM\": null,\n" +
                    "    \"BENF_ADDRESS\": \"MAIN ROAD,BYNDOOR,KUNDAPUR TQ,UDUPI DIST., KARNATAKA-576 2 \",\n" +
                    "    \"BENF_BANK\": \"SYNDICATE BANK\",\n" +
                    "    \"BENF_BRANCH\": \"BYNDOOR\",\n" +
                    "    \"BENF_ACC_NUM\": \"01222200074180\",\n" +
                    "    \"BENF_NATIONALITY\": null,\n" +
                    "    \"DEST_COUNTRY\": null,\n" +
                    "    \"ADD_TO_MEM_CARD\": null,\n" +
                    "    \"PAYMENT_MODE\": \"PRIORITY PAY\",\n" +
                    "    \"DEST_COUNTRY_CODE\": \"26\",\n" +
                    "    \"BENF_NATIONALITY_CODE\": null,\n" +
                    "    \"ID_TYPE\": null,\n" +
                    "    \"ID_EXPIRY_DATE\": null,\n" +
                    "    \"TT_BENF_NO_PK_ID\": 51,\n" +
                    "    \"TT_SALE_TXN_FK_ID\": \"51\",\n" +
                    "    \"BRANCH_ADDRESS\": null,\n" +
                    "    \"ROUTING_TYPE\": null,\n" +
                    "    \"ROUTING_CODE\": null,\n" +
                    "    \"BENF_ACC_TYPE\": null,\n" +
                    "    \"BENF_DOB\": null,\n" +
                    "    \"BENF_ACC_TYPE_DESC\": null,\n" +
                    "    \"SPONSER_NAME\": null\n" +
                    "  },\n" +
                    "  \"MODIFIED_BY\": null,\n" +
                    "  \"BENEFICIARY_ID\": null,\n" +
                    "  \"COST_PRICE\": null,\n" +
                    "  \"AREX_MEM_CARD_NUM\": null,\n" +
                    "  \"CE_MEM_CARD_NUM\": null,\n" +
                    "  \"CE_MEM_CARD_FK_ID\": null,\n" +
                    "  \"GTW_USER_MSTR_FK_ID\": \"1\",\n" +
                    "  \"TXN_CODE\": \"961049\",\n" +
                    "  \"CHANNEL\": \"M \",\n" +
                    "  \"AREX_MEM_CARD_FK_ID\": \"32348\",\n" +
                    "  \"TOTAL_TXN_AMT\": \"533\",\n" +
                    "  \"TXN_REC_TYPE\": \"BT\",\n" +
                    "  \"TXN_PAY_TYPE\": \"PRIORITY PAY\",\n" +
                    "  \"TXN_EXP_LIMIT\": \"4\",\n" +
                    "  \"TXN_EXP_DESC\": \"Expire in 4 Hours\",\n" +
                    "  \"EXP_MODIFIED_DATE\": null,\n" +
                    "  \"EXP_MODIFIED_BY\": null\n" +
                    "}";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }    private String getModeOfDescription(String type) {
        String desc = "";
        for (int i = 0; i < paymentModeData.size(); i++) {
            if (paymentModeData.get(i).getName().equalsIgnoreCase(type)) {
                desc = paymentModeData.get(i).getDescription();
                break;
            }
        }
        return desc;
    }








    private void remittanceApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JSONObject input = new APIRequestParams().remittanceApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObjectBeneficiary, LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.REMITTANCE_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void remittanceCreditCardApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JSONObject input = new APIRequestParams().remittanceCreditCardApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObjectBeneficiary, LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.REMITTANCE_CREDIT_CARD_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_CREDIT_CARD_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_CREDIT_CARD_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_CREDIT_CARD_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_CREDIT_CARD_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void wcPerformSendMoney() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalWcJsonFormat(), Constants.SUBMIT_WIRE_CARD_RELOAD_EXT_URL, CommonUtils.SERVICE_TYPE.SUBMIT_WIRE_CARD_RELOAD_EXT, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.SUBMIT_WIRE_CARD_RELOAD_EXT.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.SUBMIT_WIRE_CARD_RELOAD_EXT.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.SUBMIT_WIRE_CARD_RELOAD_EXT.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }


    private void remittanceApiGateway() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            dataObjectBeneficiary.getTxnAmountData().setTotalToPay(TOTAL_AMOUNT_PP);
            JSONObject input = new APIRequestParams().priorityPayremittanceApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObjectBeneficiary, TOTAL_AMOUNT_PP, TOTAL_PRIORITY_PAY_CHARGES_PP, LogoutCalling.getDeviceID(context), sessionTime);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.REMITTANCE_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }



    private void remittanceApiGatewayTravelCardReload() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            // dataObjectBeneficiary.getTxnAmountData().setTotalToPay(TOTAL_AMOUNT_PP);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalWcRemittanceApiGateway(), Constants.SUBMIT_WIRE_CARD_RELOAD_EXT_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private JSONObject addAdditionalWcJsonFormat() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.TOTAL_VALUE_AED,getIntent().getStringExtra("totalToPay"));
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());

            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, dataObject.getWCPKID());
            jsonObject.put(Constants.SERVICE_TYPE,Constants.TRANSACTION_TYPE_WC);

            jsonObject.put(Constants.SOURCE_OF_FUND, checkValue(transactionPurpose));
            jsonObject.put(Constants.TXN_PURPOSE,    checkValue(sourceOfPrupose));
            jsonObject.put(Constants.GTW_TXN_TYPE,"C");
            jsonObject.put(Constants.MODE_NAME,Constants.PAYMENT_MODE_BRANCH_PAY);
            jsonObject.put(Constants.RELOAD_CHARGES,itemList.get(0).getResultItem().getReloadSlabCharge());
            jsonObject.put(Constants.VAT_CHARGES,vatCharges);
            jsonObject.put(Constants.VAT_DISCOUNT,vatDiscount);

            jsonObject.put(Constants.RELOAD_CHARGES, checkValue(itemList.get(0).getResultItem().getReloadSlabCharge()));
            timeStamp = System.currentTimeMillis();


            JSONArray jsonArray = new JSONArray();
            if (itemList != null) {
                for (int i = 0; i < itemList.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.USER_PK_ID, CommonUtils.getUserId());
                        jsonObjectField.put(Constants.AED_VALUE, itemList.get(i).getResultItem().getAEDAMOUNT());
                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, itemList.get(i).getResultItem().getWCACCOUNTNUMBER());
                        jsonObjectField.put(Constants.RATE, itemList.get(i).getResultItem().getRATE());
                        jsonObjectField.put(Constants.FCY_VALUE, itemList.get(i).getResultItem().getFCYAMOUNT());
                        jsonObjectField.put(Constants.CCY_DESC, itemList.get(i).getTravelCardFlag().getCCYDESC());
                        jsonObjectField.put(Constants.CCY_CODE, itemList.get(i).getResultItem().getCCYCODE());

                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
            jsonObject.put(Constants.WC_WALLET_DETAILS_REQUEST, jsonArray);

            JSONArray jsonArray2 = new JSONArray();
            try {
                JSONObject jsonObjectField = new JSONObject();
                jsonObjectField.put(Constants.CARD_NUMBER, dataObject.getCARDNUMBER());
                jsonArray2.put(jsonObjectField);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            jsonObject.put(Constants.WC_CARDS_DETAILS_REQUEST, jsonArray2);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;
    }
    private String checkValue(String value) {
        return value != null ? value : "";
    }


    private JSONObject addAdditionalWcRemittanceApiGateway() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.TOTAL_VALUE_AED,getIntent().getStringExtra("totalToPay"));
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());

            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, dataObject.getWCPKID());
            jsonObject.put(Constants.SERVICE_TYPE,Constants.TRANSACTION_TYPE_WC);

            jsonObject.put(Constants.SOURCE_OF_FUND, checkValue(transactionPurpose));
            jsonObject.put(Constants.TXN_PURPOSE,    checkValue(sourceOfPrupose));
            jsonObject.put(Constants.GTW_TXN_TYPE,Constants.PAYMENT_MODE_PRIORITY_PAY);
            jsonObject.put(Constants.MODE_NAME,Constants.PAYMENT_MODE_PRIORITY_PAY);
            jsonObject.put(Constants.RELOAD_CHARGES,itemList.get(0).getResultItem().getReloadSlabCharge());
            jsonObject.put(Constants.VAT_CHARGES,vatCharges);
            jsonObject.put(Constants.VAT_DISCOUNT,vatDiscount);

            jsonObject.put(Constants.TOTAL_AMOUNT_PP, checkValue(TOTAL_AMOUNT_PP));
            jsonObject.put(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, checkValue(TOTAL_PRIORITY_PAY_CHARGES_PP));

            jsonObject.put(Constants.RELOAD_CHARGES, checkValue(itemList.get(0).getResultItem().getReloadSlabCharge()));
            timeStamp = System.currentTimeMillis();


            JSONArray jsonArray = new JSONArray();
            if (itemList != null) {
                for (int i = 0; i < itemList.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.USER_PK_ID, CommonUtils.getUserId());
                        jsonObjectField.put(Constants.AED_VALUE, itemList.get(i).getResultItem().getAEDAMOUNT());
                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, itemList.get(i).getResultItem().getWCACCOUNTNUMBER());
                        jsonObjectField.put(Constants.RATE, itemList.get(i).getResultItem().getRATE());
                        jsonObjectField.put(Constants.FCY_VALUE, itemList.get(i).getResultItem().getFCYAMOUNT());
                        jsonObjectField.put(Constants.CCY_DESC, itemList.get(i).getTravelCardFlag().getCCYDESC());
                        jsonObjectField.put(Constants.CCY_CODE, itemList.get(i).getResultItem().getCCYCODE());

                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
            jsonObject.put(Constants.WC_WALLET_DETAILS_REQUEST, jsonArray);

            JSONArray jsonArray2 = new JSONArray();
            try {
                JSONObject jsonObjectField = new JSONObject();
                jsonObjectField.put(Constants.CARD_NUMBER, dataObject.getCARDNUMBER());
                jsonArray2.put(jsonObjectField);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            jsonObject.put(Constants.WC_CARDS_DETAILS_REQUEST, jsonArray2);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;
    }

}