package app.alansari;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.adapters.PaymentDetailAdapterPayType;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.flatbutton.ButtonFlat;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AAEBankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.TravelCardReloadModel.TravelCardInfo;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsCreditCardData;
import app.alansari.models.TxnDetailsData;
import app.alansari.models.UserAccountData;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.models.WuRateChargesResponse;
import app.alansari.models.getCharges.ResultItem;
import app.alansari.models.travalcardvalidateflag.TravelCardAdapterItem;
import app.alansari.modules.accountmanagement.AddBankAccountActivity;
import app.alansari.modules.sendmoney.PaymentGatewayActivity;
import app.alansari.modules.sendmoney.TransactionDetailsActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_INSTANT;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

public class PaymentDetailsSelectBankPaymentActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, CustomClickListener, LogOutTimerUtil.LogOutListener {

    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private String sourceType;
    private UserAccountData selectedUserAccount;
    private BeneficiaryData dataObject;

    private Button btnProceed;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private AppCompatImageView btnAddBank;
    private Dialog pendingTransactionDialog;
    private TextView tvBankName, tvAAEBankAccountNumber, tvAAEBankName, tvConditions, tvAssistanceCall, tvMessage, tvAaeIban;
    private String PGS_FLAG = "", alertMessage = "", message = "";
    private String isWu = "N";
    private WuRateChargesResponse wuRateChargesResponse;
    private WUBeneficiaryData wuBeneficiaryData;
    private String arexUserId;
    private String MODE_DESCRIPTION;
    private JSONObject sendWuMoneyResponse;
    private long timeStamp;
    private String benefType;
    private AAEBankData aaeBankData;
    private String sessionTime;
    private String isWc = "NO";
    private String transactionPurpose, sourceOfPrupose;
    private String vatCharges, vatDiscount;
    private ArrayList<ResultItem> travelCardResultModels;
    private ArrayList<TravelCardAdapterItem> itemList;
    private TravelCardInfo dataObjects;
    private String CcyDesc;
    private String ceServiceId = "";
    private String payType = "";
    private MultiStateView multiStateViewValue;
    private ButtonFlat valueEmptyButton;
    private TextView tvEmptyValue2, tvErrorValue2;
    private RecyclerView recyclerViewValue;
    //private CarouselLayoutManager layoutManagerValue;
    private LinearLayoutManager layoutManagerValue;
    private PaymentDetailAdapterPayType paymentDetailAdapterPayType;
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
        setContentView(R.layout.activity_payment_details_select_bank_payment);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Payments Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        message = context.getString(R.string.bt_payment_details_msg);
        init();
        if (getIntent().getExtras() != null) {
            sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            ceServiceId = getIntent().getStringExtra(Constants.CE_SERVICE_ID);
            CcyDesc = getIntent().getStringExtra(Constants.TOTAL_RECIEVE_CURRENCY);
            isWu = getIntent().getExtras().getString(Constants.IS_WU, "N");
            if (isWu.equalsIgnoreCase("Y")) {
                benefType = getIntent().getStringExtra(Constants.BENEF_TYPE);
                wuRateChargesResponse = getIntent().getExtras().getParcelable(Constants.wuRateChargesResponse);
                wuBeneficiaryData = getIntent().getExtras().getParcelable(Constants.OBJECT);
                timeStamp = getIntent().getLongExtra(Constants.TIME_STAMP, 0);
                arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
                MODE_DESCRIPTION = getIntent().getStringExtra(Constants.MODE_DESCRIPTION);
                try {
                    sendWuMoneyResponse = new JSONObject(getIntent().getStringExtra(Constants.WU_RATE_CHARGE_RESPONSE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (wuBeneficiaryData != null) {
                    setInitialData();
                    return;
                }
            } else if (getIntent().getStringExtra(Constants.IS_TRAVEL) != null && getIntent().getStringExtra(Constants.IS_TRAVEL).equalsIgnoreCase("Y")) {
                isWu = "WC";
                isWc = "YES";
                dataObjects = getIntent().getExtras().getParcelable(Constants.OBJECT);
                travelCardResultModels = getIntent().getExtras().getParcelableArrayList(Constants.RESPONSE_INTENT);
                itemList = getIntent().getExtras().getParcelableArrayList(Constants.PROFILE_UPDATE_FLAG);

                transactionPurpose = getIntent().getStringExtra(Constants.Transaction_Purpose);
                sourceOfPrupose = getIntent().getStringExtra(Constants.Source_Of_Prupose);
                vatCharges = getIntent().getStringExtra(Constants.VAT_CHARGES);
                vatDiscount = getIntent().getStringExtra(Constants.VAT_DISCOUNT);
                setInitialData();
                return;

            } else {
                dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
                if (dataObject != null) {
                    setInitialData();
                    return;
                }
            }
        }
        finish();
    }

    private void init() {
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        btnAddBank = (AppCompatImageView) findViewById(app.alansari.R.id.add_bank_btn);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvAAEBankAccountNumber = (TextView) findViewById(app.alansari.R.id.aae_bank_account_number);
        tvAaeIban = (TextView) findViewById(app.alansari.R.id.aae_iban);
        tvAAEBankName = (TextView) findViewById(app.alansari.R.id.aae_bank_name);
        tvConditions = (TextView) findViewById(app.alansari.R.id.aae_bank_conditions);
        tvAssistanceCall = (TextView) findViewById(app.alansari.R.id.assistance_call);
        tvMessage = (TextView) findViewById(app.alansari.R.id.enter_ref_num_msg);

        multiStateViewValue = (MultiStateView) findViewById(R.id.multiStateViewBank);

        valueEmptyButton = multiStateViewValue.findViewById(R.id.empty_button);
        setViewStateOnClick();
        tvEmptyValue2 = ((TextView) multiStateViewValue.findViewById(R.id.empty_textView));
        tvErrorValue2 = ((TextView) multiStateViewValue.findViewById(R.id.error_textView));
        recyclerViewValue = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManagerValue = new LinearLayoutManager(context);
        layoutManagerValue.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewValue.setLayoutManager(layoutManagerValue);
        recyclerViewValue.setHasFixedSize(true);
        paymentDetailAdapterPayType = new PaymentDetailAdapterPayType(this, new ArrayList<UserAccountData.PAYTYPEBTLISTItem>(),selectedUserAccount, this);
        recyclerViewValue.setAdapter(paymentDetailAdapterPayType);


        tvBankName.setOnClickListener(this);
        btnAddBank.setOnClickListener(this);
        tvAssistanceCall.setOnClickListener(this);
        btnProceed = (Button) findViewById(app.alansari.R.id.proceed_btn);
        btnProceed.setOnClickListener(this);
    }


    private void setViewStateOnClick() {
        multiStateViewValue.findViewById(R.id.empty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        multiStateViewValue.findViewById(R.id.error_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        valueEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setViewState(CommonUtils.SERVICE_TYPE sType, int viewState) {
        //if (recyclerAdapterValue.getItemCount() == 0) {
        if (viewState == VIEW_STATE_EMPTY) {
            valueEmptyButton.setVisibility(View.GONE);
            CommonUtils.setViewState(multiStateViewValue, viewState, tvEmptyValue2, null, sType == FETCH_BENEFICIARY_INSTANT ? getString(R.string.empty_beneficiary_arex) : getString(R.string.empty_beneficiary_ce));
        }
        if (viewState == VIEW_STATE_WRONG)
            CommonUtils.setViewState(multiStateViewValue, viewState, tvErrorValue2, null, getString(R.string.error_beneficiary));
        if (viewState == VIEW_STATE_ERROR)
            CommonUtils.setViewState(multiStateViewValue, viewState, tvErrorValue2, null, null);
        //}
    }


    private void setInitialData() {
        if (dataObject != null) {
            tvAAEBankAccountNumber.setVisibility(View.INVISIBLE);
            tvAAEBankName.setVisibility(View.INVISIBLE);
        } else if (wuBeneficiaryData != null) {
            tvAAEBankAccountNumber.setVisibility(View.INVISIBLE);
            tvAAEBankName.setVisibility(View.INVISIBLE);
        } else if (dataObjects != null) {
            tvAAEBankAccountNumber.setVisibility(View.INVISIBLE);
            tvAAEBankName.setVisibility(View.INVISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.assistance_call:
                CommonUtils.goToDialPad(context, tvAssistanceCall.getText().toString().trim());
                break;
            case app.alansari.R.id.proceed_btn:
                pendingTransactionDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
                pendingTransactionDialog.setCanceledOnTouchOutside(false);
                pendingTransactionDialog.setContentView(app.alansari.R.layout.generic_single_btn_dialog);
                if (PGS_FLAG.equalsIgnoreCase("Y")) {
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Bank Transfer");
                    ((TextView) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_text)).setText(alertMessage);
                    ((Button) pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn)).setText("Proceed");
                }
                pendingTransactionDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isWu.equalsIgnoreCase("Y")) {
                            wuPerformSendMoney();
                        } else if (isWc.equalsIgnoreCase("YES")) {
                            wcPerformSendMoney();
                        } else {
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
            case app.alansari.R.id.bank_name:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_USER_ACCOUNT);
                if (isWc.equalsIgnoreCase("YES")) {
                    intent.putExtra(Constants.IS_WU, "");
                } else {
                    intent.putExtra(Constants.IS_WU, getIntent().getStringExtra(Constants.IS_WU));
                }
                startActivityForResult(intent, Constants.SELECT_ITEM_USER_ACCOUNT);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.add_bank_btn:
                intent = new Intent(context, AddBankAccountActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_USER_ACCOUNT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_USER_ACCOUNT && resultCode == Activity.RESULT_OK) {
                selectedUserAccount = data.getParcelableExtra(Constants.OBJECT);
                //Log.e("sfcbshvcsjv",""+selectedUserAccount.getFTLEARNMOREURL());
                //Log.e("sdbchjsdhcvs",""+selectedUserAccount.getPAYTYPEBTLIST().get(0).getPrimaryKeyValue());
                setViewList(selectedUserAccount.getPAYTYPEBTLIST());
                setSelectedUserAccountData();
                tvMessage.setVisibility(View.VISIBLE);
                btnProceed.setEnabled(false);
            } else if (requestCode == Constants.REQUEST_CODE_ADD_USER_ACCOUNT && resultCode == Activity.RESULT_OK) {
                selectedUserAccount = data.getParcelableExtra(Constants.OBJECT);
                setViewList(selectedUserAccount.getPAYTYPEBTLIST());
                setSelectedUserAccountData();
                tvMessage.setVisibility(View.VISIBLE);
                btnProceed.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setSelectedUserAccountData() {
        if (selectedUserAccount != null) {
            tvBankName.setText(selectedUserAccount.getBankName());
            if (dataObject != null)
                dataObject.setUserAccountData(selectedUserAccount);
            if (selectedUserAccount.getPAYTYPEBTLIST() != null)
                payType = selectedUserAccount.getPAYTYPEBTLIST().get(0).getPrimaryKeyValue();
            else
                payType = "";
            fetchAAEBankDetails(selectedUserAccount.getBankCode());
        }
    }

    //fetchAEE BAnk
    void fetchAAEBankDetails(String bankCode) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAAEBankDetails((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), bankCode, isWu, LogoutCalling.getDeviceID(context), sessionTime, payType), Constants.FETCH_AAE_BANK_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_AAE_BANK_DETAILS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_AAE_BANK_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_AAE_BANK_DETAILS.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_AAE_BANK_DETAILS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_AAE_BANK_DETAILS:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<AAEBankData> aaeBankData = (ArrayList<AAEBankData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AAEBankData>>() {
                                }.getType());
                                if (aaeBankData != null && aaeBankData.size() > 0) {
                                    setAAEBankData(aaeBankData.get(0));
                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }

                                if (isWu.equalsIgnoreCase("Y") || isWu.equalsIgnoreCase("N") || isWu.equalsIgnoreCase("WC")) {
                                    if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("1406")) {
                                        findViewById(R.id.aae_bank_details_label).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.aae_bank_account_name).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.aae_bank_account_number).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.aae_iban).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.aae_bank_name).setVisibility(View.INVISIBLE);

                                        // tvMessage.setText(response.getString(Constants.MESSAGE));
                                        tvMessage.setText(aaeBankData.get(0).getPgsMessage());
                                        tvMessage.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                                        btnProceed.setEnabled(false);
                                        final Dialog messageDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                                        messageDialog.setCanceledOnTouchOutside(false);
                                        messageDialog.setContentView(R.layout.dialog_non_pgs_error);
                                        ((TextView) messageDialog.findViewById(app.alansari.R.id.dialog_title)).setText(aaeBankData.get(0).getPgsMessage());
                                        messageDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                messageDialog.dismiss();
                                            }
                                        });
                                        messageDialog.show();
                                    } else {
                                        if (PGS_FLAG.equalsIgnoreCase("Y")) {
                                            findViewById(R.id.aae_bank_details_label).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.aae_bank_account_name).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.aae_bank_account_number).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.aae_iban).setVisibility(View.INVISIBLE);
                                            findViewById(R.id.aae_bank_name).setVisibility(View.INVISIBLE);
                                            tvMessage.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                                            btnProceed.setEnabled(true);
                                        } else {
                                            findViewById(R.id.aae_bank_details_label).setVisibility(View.VISIBLE);
                                            findViewById(R.id.aae_bank_account_name).setVisibility(View.VISIBLE);
                                            findViewById(R.id.aae_bank_account_number).setVisibility(View.VISIBLE);
                                            findViewById(R.id.aae_iban).setVisibility(View.VISIBLE);
                                            findViewById(R.id.aae_bank_name).setVisibility(View.VISIBLE);
                                            tvMessage.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                                            btnProceed.setEnabled(true);
                                        }
                                    }
                                } else {
                                    findViewById(R.id.aae_bank_details_label).setVisibility(View.VISIBLE);
                                    findViewById(R.id.aae_bank_account_name).setVisibility(View.VISIBLE);
                                    findViewById(R.id.aae_bank_account_number).setVisibility(View.VISIBLE);
                                    findViewById(R.id.aae_iban).setVisibility(View.VISIBLE);
                                    findViewById(R.id.aae_bank_name).setVisibility(View.VISIBLE);
                                    tvMessage.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
                                    btnProceed.setEnabled(true);
                                }
                            } else {
                                Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));
                            // Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));

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
                                if (dataObject.getServiceTypeData().getMapping().equals(Constants.AREX_MAPPING)) {
                                    txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                    }.getType());
                                } else {
                                    ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout = (ArrayList<TxnDetailsCeCashPayout>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsCeCashPayout>>() {
                                    }.getType());
                                    txnDetailsData = CommonUtils.getTxnDetailsData(txnDetailsCeCashPayout);
                                }
                                if (dataObject.getPgsFlag().equalsIgnoreCase("Y")) {
                                    JSONArray array = response.getJSONArray(Constants.RESULT);
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    String url = jsonObject.getString(Constants.URL);
                                    LogUtils.v("Teja Url", "url :" + url);
                                    TxnDetailsData txnDetails = txnDetailsData.get(0);
                                    txnDetails.setUrl(url);
                                    txnDetails.setSuccessUrl(CommonUtils.getSuccessUrl());
                                    intent = new Intent(context, PaymentGatewayActivity.class);
                                    intent.putExtra(Constants.OBJECT, txnDetails);
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetails.getUrl());
                                    intent.putExtra(Constants.SOURCE, Constants.TYPE_SEND_MONEY);
                                    startActivity(intent);
                                    pendingTransactionDialog.dismiss();
                                } else if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_BANK_PAYMENT_ACTIVITY);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    startActivity(intent);
                                    pendingTransactionDialog.dismiss();
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
                                if (dataObject.getPgsFlag().equalsIgnoreCase("Y")) {
                                    JSONArray array = response.getJSONArray(Constants.RESULT);
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    String url = jsonObject.getString(Constants.URL);
                                    LogUtils.v("Teja Url", "url :" + url);
                                    TxnDetailsData txnDetails = txnDetailsData.get(0);
                                    txnDetails.setUrl(url);
                                    txnDetails.setSuccessUrl(CommonUtils.getSuccessUrl());
                                    intent = new Intent(context, PaymentGatewayActivity.class);
                                    intent.putExtra(Constants.OBJECT, txnDetails);
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetails.getUrl());
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CREDIT_CARD);
                                    intent.putExtra(Constants.SOURCE, Constants.TYPE_SEND_MONEY);
                                    startActivity(intent);
                                    pendingTransactionDialog.dismiss();
                                } else if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_BANK_PAYMENT_ACTIVITY);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_CREDIT_CARD);
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

            case SUBMIT_WIRE_CARD_RELOAD_EXT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData = null;
                                txnDetailsData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                }.getType());
                                if (PGS_FLAG.equalsIgnoreCase("Y")) {
                                    JSONArray array = response.getJSONArray(Constants.RESULT);
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    String url = jsonObject.getString(Constants.URL);
                                    LogUtils.v("Teja Url", "url :" + url);
                                    TxnDetailsData txnDetails = txnDetailsData.get(0);
                                    txnDetails.setUrl(url);
                                    txnDetails.setSuccessUrl(CommonUtils.getSuccessUrl());

                                    //Siddu123
                                    intent = new Intent(context, PaymentGatewayActivity2.class);
                                    intent.putExtra(Constants.OBJECT, txnDetails);
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetails.getUrl());
                                    intent.putExtra(Constants.SOURCE, Constants.TYPE_SEND_MONEY);
                                    startActivity(intent);
                                    pendingTransactionDialog.dismiss();
                                } else if (txnDetailsData != null && txnDetailsData.size() > 0) {

                                    intent = new Intent(context, TransactionTravelCompDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, "Payment");
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    context.startActivity(intent);
                                    pendingTransactionDialog.dismiss();
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

            case WU_PERFORM_SEND_MONEY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<TxnDetailsData> txnDetailsData;
                                txnDetailsData = (ArrayList<TxnDetailsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TxnDetailsData>>() {
                                }.getType());
                                if (PGS_FLAG.equalsIgnoreCase("Y")) {
                                    JSONArray array = response.getJSONArray(Constants.RESULT);
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    String url = jsonObject.getString(Constants.URL);
                                    LogUtils.v("Teja Url", "url :" + url);
                                    TxnDetailsData txnDetails = txnDetailsData.get(0);
                                    txnDetails.setUrl(url);
                                    txnDetails.setSuccessUrl(CommonUtils.getSuccessUrl());
                                    intent = new Intent(context, PaymentGatewayActivity.class);
                                    intent.putExtra(Constants.OBJECT, txnDetails);
                                    intent.putExtra(Constants.GATEWAY_URL, txnDetails.getUrl());
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                                    intent.putExtra(Constants.SOURCE, Constants.TYPE_SEND_MONEY);
                                    startActivity(intent);
                                    pendingTransactionDialog.dismiss();
                                } else if (txnDetailsData != null && txnDetailsData.size() > 0) {
                                    intent = new Intent(context, TransactionDetailsActivity.class);
                                    intent.putExtra(Constants.SOURCE, Constants.SOURCE_BANK_PAYMENT_ACTIVITY);
                                    intent.putExtra(Constants.SOURCE_TYPE, Constants.TRANSACTION_TYPE_WU);
                                    intent.putExtra(Constants.OBJECT, txnDetailsData.get(0));
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }

        }

    }

    private void remittanceApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(remittanceApiCall((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject, LogoutCalling.getDeviceID(context), sessionTime), Constants.REMITTANCE_API_URL, CommonUtils.SERVICE_TYPE.REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.REMITTANCE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAAEBankData(AAEBankData aaeBankData) {
        if (aaeBankData != null) {
            this.aaeBankData = aaeBankData;
            tvAAEBankAccountNumber.setText("Account no: " + aaeBankData.getAccountNumber());
            tvAAEBankName.setText("Bank name: " + aaeBankData.getBankName());
            tvAaeIban.setText("IBAN: " + aaeBankData.getIBANNumber());
            tvAAEBankAccountNumber.setVisibility(View.VISIBLE);
            tvAAEBankName.setVisibility(View.VISIBLE);
            if (dataObject != null) {
                dataObject.setPgsFlag(aaeBankData.getGtwPgsFlag());
                dataObject.setPgsBankCode(aaeBankData.getPgsBankCode());
            }

            if (aaeBankData.getGtwPgsFlag() != null && aaeBankData.getGtwPgsFlag().equalsIgnoreCase("Y")) {
                PGS_FLAG = "Y";
                alertMessage = aaeBankData.getAlertMessage();
                tvMessage.setText(aaeBankData.getPgsMessage());
            } else {
                if (dataObject != null) {
                    dataObject.setPgsFlag("");
                    dataObject.setPgsBankCode("");
                }
                PGS_FLAG = "N";
                alertMessage = "";
                tvMessage.setText(message);
                tvMessage.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color3F3F3F));
            }

            if (aaeBankData.getIsSameBank().equalsIgnoreCase("1"))
                tvConditions.setVisibility(View.GONE);
            else
                tvConditions.setVisibility(View.VISIBLE);

            if (dataObject != null)
                dataObject.setAaeBankData(aaeBankData);
            btnProceed.setEnabled(true);
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    private void remittanceCreditCardApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JSONObject input = new APIRequestParams().remittanceCreditCardApi((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject, LogoutCalling.getDeviceID(context), sessionTime);
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


    private void wuPerformSendMoney() {
        try {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                String CHARGE_AMOUNT = sendWuMoneyResponse.getString(Constants.CHARGES);
                String AAE_ACCOUNT_NAME = aaeBankData.getAccountName();
                String FEE_ENQ_TXN_TYPE = wuRateChargesResponse.getFEEENQTXNTYPE();
                String AAE_ACCOUNT_NUM = aaeBankData.getAccountNumber();
                String PGS_FLAG = this.PGS_FLAG;
                String RATE = sendWuMoneyResponse.getString(Constants.RATE);
                String WU_SERVICE_TYPE = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
                String BENEF_TYPE = benefType;
                String TOTAL_VALUE_AED = sendWuMoneyResponse.getString(Constants.ORIGINAL_PRINCIPLE_AMOUNT);
                String WU_PROMO_CODE = getIntent().getStringExtra(Constants.WU_PROMO_CODE);
                String BENEF_PK_ID = getIntent().getStringExtra(Constants.BENEF_PK_ID);
                if (BENEF_PK_ID == null) {
                    BENEF_PK_ID = "";
                }
                long TIME_STAMP = timeStamp;
                String IS_WU_LOOKUP_PROMO_CODE_SELECTED = "";
                String MODE_PK_ID = "2";
                String AREX_MEM_PK_ID = arexUserId;
                String WU_CCY_CODE = wuBeneficiaryData.getReceiverCurrencyCode();
                String AREX_CCY_CODE = wuBeneficiaryData.getArexCurrencyCode();
                String TAX_RATE = wuRateChargesResponse.getTAXRATE();
                String USER_PK_ID = CommonUtils.getUserId();
                String ACC_FK_ID = selectedUserAccount.getId();
                String MODE_NAME = "BT";
                String MODE_DESCRIPTION = this.MODE_DESCRIPTION;
                String WU_PARAM_REF_NO = wuRateChargesResponse.getREFERENCENO();
                String USER_ACC_NUM = null;
                String SERVICE_TYPE = "WU";
                String TAX_WORKSHEET = wuRateChargesResponse.getTAXWORKSHEET();
                String ACCOUNT_HOLDER_NAME = selectedUserAccount.getAccountName();
                String PGS_BANK_CODE = aaeBankData.getPgsBankCode();
                String TEST_QUESTION_FLAG = wuRateChargesResponse.getISTESTQUESTIONAVAILABLE();
                if (TEST_QUESTION_FLAG == null) {
                    TEST_QUESTION_FLAG = "P";
                }
                String AREX_CODE = aaeBankData.getAREXCode();
                String TOTAL_NET_AMOUNT = wuRateChargesResponse.getGROSSTOTALAMOUNT();
                String WU_COUNTRY_CODE = wuBeneficiaryData.getReceiverCountryCode();
                String VAT_CHARGES = wuRateChargesResponse.getVATCHARGES();
                String TXN_AMOUNT = wuRateChargesResponse.getDESTPRINCIPALAMOUNT();
                String TRANSFER_TYPE = "CP";
                String AAE_BANK_NAME = aaeBankData.getBankName();
                String AREX_COUNTRY_CODE = wuBeneficiaryData.getArexCountryCode();
                String TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
                String WU_LOOKUP_PROMO_CODE = "";

                JSONObject input = new APIRequestParams().wuBankTransfer(CHARGE_AMOUNT, AAE_ACCOUNT_NAME, FEE_ENQ_TXN_TYPE, AAE_ACCOUNT_NUM, PGS_FLAG, RATE, WU_SERVICE_TYPE, BENEF_TYPE,
                        TOTAL_VALUE_AED, WU_PROMO_CODE, BENEF_PK_ID, TIME_STAMP, IS_WU_LOOKUP_PROMO_CODE_SELECTED, MODE_PK_ID, AREX_MEM_PK_ID, WU_CCY_CODE, AREX_CCY_CODE, TAX_RATE,
                        USER_PK_ID, ACC_FK_ID, MODE_NAME, MODE_DESCRIPTION, WU_PARAM_REF_NO, USER_ACC_NUM, SERVICE_TYPE, TAX_WORKSHEET, ACCOUNT_HOLDER_NAME, PGS_BANK_CODE, TEST_QUESTION_FLAG,
                        AREX_CODE, TOTAL_NET_AMOUNT, WU_COUNTRY_CODE, VAT_CHARGES, TXN_AMOUNT, TRANSFER_TYPE, AAE_BANK_NAME, AREX_COUNTRY_CODE, TOTAL_WU_POINTS, WU_LOOKUP_PROMO_CODE, LogoutCalling.getDeviceID(context), sessionTime);
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


    private JSONObject addAdditionalWcJsonFormat() {
        String ACC_FK_ID = selectedUserAccount.getId();
        String AAE_ACCOUNT_NAME = aaeBankData.getAccountName();
        String AAE_ACCOUNT_NUM = aaeBankData.getAccountNumber();
        String ACCOUNT_HOLDER_NAME = selectedUserAccount.getAccountName();
        String AAE_BANK_NAME = aaeBankData.getBankName();
        String AREX_CODE = aaeBankData.getAREXCode();
        String PGS_BANK_CODE = aaeBankData.getPgsBankCode();
        String USER_ACC_NUM = null;
        String PGS_FLAG = this.PGS_FLAG;


        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.TXN_PURPOSE, checkValue(sourceOfPrupose));
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.TOTAL_VALUE_AED, getIntent().getStringExtra("totalToPay"));
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.MODE_NAME, Constants.PAYMENT_MODE_BANK_TRANSFER);

            jsonObject.put(Constants.AREX_MEM_PK_ID, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING));
            jsonObject.put(Constants.WC_PK_ID, dataObjects.getWCPKID());
            jsonObject.put(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WC);

            jsonObject.put(Constants.SOURCE_OF_FUND, checkValue(transactionPurpose));

            jsonObject.put(Constants.GTW_TXN_TYPE, "B");


            jsonObject.put(Constants.RELOAD_CHARGES, itemList.get(0).getResultItem().getReloadSlabCharge());
            jsonObject.put(Constants.VAT_CHARGES, vatCharges);
            jsonObject.put(Constants.VAT_DISCOUNT, vatDiscount);
//--------------------------------------------------------------------------------------------------


            jsonObject.put(Constants.ACC_FK_ID, ACC_FK_ID);
            jsonObject.put(Constants.AAE_ACCOUNT_NAME, AAE_ACCOUNT_NAME);
            jsonObject.put(Constants.AAE_ACCOUNT_NUM, AAE_ACCOUNT_NUM);
            jsonObject.put(Constants.USER_ACC_NUM, USER_ACC_NUM);
            jsonObject.put(Constants.ACCOUNT_HOLDER_NAME, ACCOUNT_HOLDER_NAME);
            jsonObject.put(Constants.AAE_BANK_NAME, AAE_BANK_NAME);
            jsonObject.put(Constants.AREX_CODE, AREX_CODE);
            jsonObject.put(Constants.PGS_BANK_CODE, PGS_BANK_CODE);
            jsonObject.put(Constants.PGS_FLAG, PGS_FLAG);

            // jsonObject.put(Constants.RELOAD_CHARGES, checkValue(travelCardResultModels.get(0).getReloadSlabCharge()));
            timeStamp = System.currentTimeMillis();


           /* JSONArray jsonArray = new JSONArray();
            if (travelCardResultModels != null) {
                for (int i = 0; i < travelCardResultModels.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.AED_VALUE, travelCardResultModels.get(i).getAEDAMOUNT());
                        jsonObjectField.put(Constants.WC_ACCOUNT_NUMBER, travelCardResultModels.get(i).getWCACCOUNTNUMBER());
                        jsonObjectField.put(Constants.RATE, travelCardResultModels.get(i).getRATE());
                        jsonObjectField.put(Constants.FCY_VALUE, travelCardResultModels.get(i).getFCYAMOUNT());
                        jsonObjectField.put(Constants.CCY_DESC, itemList.get(0).getCCYDESC());
                        jsonObjectField.put(Constants.USER_PK_ID, CommonUtils.getUserId());
                        jsonObjectField.put(Constants.CCY_CODE, travelCardResultModels.get(i).getCCYCODE());

                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
            jsonObject.put(Constants.WC_WALLET_DETAILS_REQUEST, jsonArray);*/


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
                jsonObjectField.put(Constants.CARD_NUMBER, dataObjects.getCARDNUMBER());
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

    public JSONObject remittanceApiCall(String userId, String arexMemPkId, String ceMemPkId, BeneficiaryData dataObject, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.CE_SERVICE_ID, ceServiceId);
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

            jsonObject.put(Constants.CCY_ID, checkValue(getIntent().getStringExtra(Constants.CCY_ID)));
            jsonObject.put(Constants.CCY_CODE, checkValue(getIntent().getStringExtra(Constants.CCY_CODE)));
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

    private void setViewList(List<UserAccountData.PAYTYPEBTLISTItem> paytypebtlist) {
        if (selectedUserAccount.getPAYTYPEBTLIST() != null)
            paymentDetailAdapterPayType.addArrayList(paytypebtlist,selectedUserAccount);
        else
            paymentDetailAdapterPayType.clear();
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        if (dataItem != null) {
            UserAccountData.PAYTYPEBTLISTItem obj=  (UserAccountData.PAYTYPEBTLISTItem) dataItem;
            payType =obj.getPrimaryKeyValue();
            fetchAAEBankDetails(selectedUserAccount.getBankCode());
        } else {
            Toast.makeText(context, "Please select once again", Toast.LENGTH_SHORT).show();
        }
    }
}
