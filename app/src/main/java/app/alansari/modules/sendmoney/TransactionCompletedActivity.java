package app.alansari.modules.sendmoney;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import app.alansari.AppController;
import app.alansari.CreditCardPaymentActivity;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsData;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SEND_INVOICE_API;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 02 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TransactionCompletedActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private String sourceType;
    private TxnDetailsData txnDetailsData;

    private Dialog myDialog;
    private EditText etEmail;
    private TextInputLayout emailLayout;
    private Button btnSendAgain, btnSendInvoice;
    private AppCompatImageView ivPaymentMode, ivTxnStatus;
    private TextView tvTempTransactionNumber, tvPaymentMode, tvName, tvBankName, tvBranchName, tvAccountNumber, tvPaymentDate;
    private TextView tvTxnStatus, tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;
    private String message = "";
    private TxnDetailsCeCashPayout mPayout;
    private String sessionTime;

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
    public void onBackPressed() {
        if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_DETAILS)) {
            CommonUtils.goToDashBoard(context, getIntent().getExtras().getString(Constants.SOURCE), txnDetailsData.getId(), txnDetailsData.getServiceType());
        } else if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
            showFeedbackScreen();
            //showNewTransactionPage();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void showNewTransactionPage() {
        if (txnDetailsData.getTxnType().equalsIgnoreCase("CC")) {
            intent = new Intent(context, CreditCardPaymentActivity.class);
        } else {
            intent = new Intent(context, BeneficiaryActivity.class);
            intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
        }

        startActivity(intent);

    }

    private void showFeedbackScreen() {
       /* intent = new Intent(context, TransactionRateActivity.class);
        intent.putExtra(Constants.SOURCE, getIntent().getExtras().getString(Constants.SOURCE));
        intent.putExtra(Constants.REM_TXN_PK_ID, txnDetailsData.getId());
        if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
            intent.putExtra(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WU);
        } else {
            intent.putExtra(Constants.SERVICE_TYPE, txnDetailsData.getServiceType());
        }

        startActivity(intent); */

//        intent = new Intent(context, BeneficiaryActivity.class);
//        intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
//        intent.putExtra(Constants.SOURCE, Constants.TRANSACTION_STATUS_COMPLETED);

      /*  intent.putExtra(Constants.SOURCE, getIntent().getExtras().getString(Constants.SOURCE));
        intent.putExtra(Constants.REM_TXN_PK_ID, txnDetailsData.getId());
        if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
            intent.putExtra(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WU);
        } else {
            intent.putExtra(Constants.SERVICE_TYPE, txnDetailsData.getServiceType());
        }*/

        // startActivity(intent);

        intent = new Intent(context, TransactionRateActivity.class);
        intent.putExtra(Constants.SOURCE, getIntent().getExtras().getString(Constants.SOURCE));
        intent.putExtra(Constants.REM_TXN_PK_ID, txnDetailsData.getId());
        if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
            intent.putExtra(Constants.SERVICE_TYPE, Constants.TRANSACTION_TYPE_WU);
        } else {
            intent.putExtra(Constants.SERVICE_TYPE, txnDetailsData.getServiceType());
        }

        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.transaction_completed_activity);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_DETAILS)) {
            getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.svg_toolbar_home);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        }
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        init();
        if (getIntent().getExtras() != null) {
            txnDetailsData = getIntent().getExtras().getParcelable(Constants.OBJECT);

            if (!(getIntent().getExtras().getString(Constants.SOURCE_TYPE) == null)) {
                sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE);
            } else {
                sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            }

            // sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE);

            //String value = getIntent().getExtras().getString("Type");
            // Log.e("fknasdjkfnsjanf", "" + value);
            //Log.e("wjhfuihwbujf", "" + sourceType + "" + txnDetailsData.getTxnType() +" "+txnDetailsData.getInvoiceFlag());
            // Log.e("wjhfuihwbujf", "" + (txnDetailsData.getServiceType()));

            if (txnDetailsData != null) {
                setInitialData();
                return;
            }
        }

        finish();
    }

    private void init() {

        ivPaymentMode = (AppCompatImageView) findViewById(app.alansari.R.id.pay_mode_icon);
        tvTempTransactionNumber = (TextView) findViewById(app.alansari.R.id.temp_transaction_number);
        tvPaymentMode = (TextView) findViewById(app.alansari.R.id.pay_mode);

        tvName = (TextView) findViewById(app.alansari.R.id.name);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(app.alansari.R.id.account_number);
        tvPaymentDate = (TextView) findViewById(app.alansari.R.id.pay_date);


        ivTxnStatus = (AppCompatImageView) findViewById(app.alansari.R.id.txn_status_image);
        tvTxnStatus = (TextView) findViewById(app.alansari.R.id.txn_status);

        tvTotalReceive = (TextView) findViewById(app.alansari.R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);

        btnSendInvoice = (Button) findViewById(app.alansari.R.id.send_invoice_btn);
        btnSendAgain = (Button) findViewById(app.alansari.R.id.send_again_btn);
        btnSendInvoice.setOnClickListener(this);
        btnSendAgain.setOnClickListener(this);
    }

    private void setInitialData() {
        try {
            ((TextView) findViewById(app.alansari.R.id.total_receive_label)).setText(getString(app.alansari.R.string.receive_amount));
            ((TextView) findViewById(app.alansari.R.id.total_to_pay_label)).setText(getString(app.alansari.R.string.total_paid));
            if (sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD)) {
                ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(app.alansari.R.string.cardholder_name));
                ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.bank_name));
                ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.card_type));
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.card_number));
            }


            if (txnDetailsData != null) {
                if (txnDetailsData.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) && txnDetailsData.getTxnType().equalsIgnoreCase("TT")) {
                    txnDetailsData.setBeneficiaryData(txnDetailsData.getBeneficiaryDataTt());
                    txnDetailsData.setTransactionData(txnDetailsData.getTransactionDataTt());
                }

                tvTempTransactionNumber.setText(txnDetailsData.getTxnCode());
                // Log.e("wjhfuihwbujf", "" + sourceType + "" + txnDetailsData.getTxnType() +" "+txnDetailsData.getInvoiceFlag());

                btnSendAgain.setEnabled(true);
                if (txnDetailsData.getCreatedDate() != null && txnDetailsData.getCreatedDate().trim().length() > 0)
                    tvPaymentDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(txnDetailsData.getCreatedDate())));

                if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                    if (txnDetailsData.getWuBeneficiaryName() == null || txnDetailsData.getWuBeneficiaryName().length() == 0) {
                        String name = "";
                        if (txnDetailsData.getBeneficiaryDataWu().getReceiverNameType().equalsIgnoreCase("D")) {
                            name = CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverMiddleName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverLastName());
                        } else {
                            name = CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverLastName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverMiddleName());
                        }
                        tvName.setText(name);
                        tvBankName.setText(txnDetailsData.getAaeBankName());
                        tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionDataWu().getSendAmount()));
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionDataWu().getCurrencyDesc());
                        if (txnDetailsData.getNetTotal() != null && txnDetailsData.getNetTotal().length() > 0) {
                            tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getNetTotal()));
                        } else {
                            tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionDataWu().getTotalTxnAmount()));
                        }

                        tvTotalToPayCurrencyCode.setText("AED");
                    } else {
                        tvName.setText(txnDetailsData.getWuBeneficiaryName());
                        tvBankName.setText(txnDetailsData.getWuBankName());
                        tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getWuFcyAmt()));
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getWuCcyDesc());
                        tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getNetTotal()));
                        tvTotalToPayCurrencyCode.setText("AED");
                    }
                    ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText("MTCN");
                    if (txnDetailsData.getMtcn() != null && !txnDetailsData.getMtcn().equalsIgnoreCase("null") && txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                        tvBankName.setText(txnDetailsData.getMtcn());
                    } else {
                        tvBankName.setText("-");
                    }
                    tvBranchName.setText(txnDetailsData.getBranchName());

                    if (txnDetailsData.getTransactionDataWu() != null && txnDetailsData.getTransactionDataWu().getDestCountryDesc() != null) {
                        if (txnDetailsData.getTransactionDataWu().getDestCountryCode() != null && (txnDetailsData.getTransactionDataWu().getDestCountryCode().equalsIgnoreCase("092") || txnDetailsData.getTransactionDataWu().getDestCountryCode().equalsIgnoreCase("92"))) {
                            String state = "";
                            if (txnDetailsData.getBeneficiaryDataWu().getPayoutState() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutState().equalsIgnoreCase("null")) {
                                state = txnDetailsData.getBeneficiaryDataWu().getPayoutState();
                            }
                            tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc() + "-" + state);
                        } else if (txnDetailsData.getTransactionDataWu().getDestCountryCode() != null && txnDetailsData.getTransactionDataWu().getDestCountryCode().equalsIgnoreCase("125")) {
                            String state = "";
                            if (txnDetailsData.getBeneficiaryDataWu().getPayoutState() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutState().equalsIgnoreCase("null")) {
                                state = txnDetailsData.getBeneficiaryDataWu().getPayoutState();
                            }
                            String city = "";
                            if (txnDetailsData.getBeneficiaryDataWu().getPayoutCity() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutCity().equalsIgnoreCase("null")) {
                                city = txnDetailsData.getBeneficiaryDataWu().getPayoutCity();
                            }
                            tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc() + "-" + state + "/" + city);
                        } else {
                            tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc());
                        }
                    } else {
                        if (txnDetailsData.getBranchName() != null && txnDetailsData.getBranchName().equalsIgnoreCase("USA")) {
                            String state = "";
                            if (txnDetailsData.getPayoutState() != null && !txnDetailsData.getPayoutState().equalsIgnoreCase("null")) {
                                state = txnDetailsData.getPayoutState();
                            }
                            tvBranchName.setText(txnDetailsData.getBranchName() + "-" + state);
                        } else if (txnDetailsData.getBranchName() != null && txnDetailsData.getBranchName().equalsIgnoreCase("MEXICO")) {
                            String state = "";
                            if (txnDetailsData.getPayoutState() != null && !txnDetailsData.getPayoutState().equalsIgnoreCase("null")) {
                                state = txnDetailsData.getPayoutState();
                            }
                            String city = "";
                            if (txnDetailsData.getPayoutCity() != null && !txnDetailsData.getPayoutCity().equalsIgnoreCase("null")) {
                                city = txnDetailsData.getPayoutCity();
                            }
                            tvBranchName.setText(txnDetailsData.getBranchName() + "-" + state + "/" + city);
                        }
                    }

                    // Log.e("vnsdjkvbshbv",""+ txnDetailsData.getInvoiceFlag());

                    tvBranchName.setSelected(true);
                    ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                    if (txnDetailsData.getBenefMobileNumber() != null) {
                        tvAccountNumber.setText(txnDetailsData.getBenefMobileNumber());
                    } else if (txnDetailsData.getBeneficiaryDataWu() != null && txnDetailsData.getBeneficiaryDataWu().getReceiverContactNum() != null) {
                        tvAccountNumber.setText(txnDetailsData.getBeneficiaryDataWu().getReceiverContactNum());
                    } else {
                        tvAccountNumber.setText("-");
                    }
                    // btnSendInvoice.setEnabled(false);

                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                        // btnSendInvoice.setEnabled(true);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                        btnSendInvoice.setEnabled(true);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText(getString(R.string.txt_credit_card));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                    }

                } else if (txnDetailsData.getTxnType().equalsIgnoreCase("CP") && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
                    if (txnDetailsData.getServiceType().equalsIgnoreCase(Constants.AREX_MAPPING) && txnDetailsData.getTxnType().equalsIgnoreCase("TT")) {
                        txnDetailsData.setBeneficiaryData(txnDetailsData.getBeneficiaryDataTt());
                        txnDetailsData.setTransactionData(txnDetailsData.getTransactionDataTt());

                    }


                    ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.beneficiary_branch_payout));
                    ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(R.string.beneficiary_name));

                    tvBranchName.setText(txnDetailsData.getBeneficiaryData().getBranchName());
                    tvName.setText(txnDetailsData.getBeneficiaryData().getFull_name());
                    ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.beneficiary_bank_agent));
                    tvBankName.setText(txnDetailsData.getTransactionData().getAgent_name());

                    if (!sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD) && (txnDetailsData.getTxnRecType() != null && txnDetailsData.getTxnRecType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) || (txnDetailsData.getTxnType() != null && txnDetailsData.getTxnType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP))) {
                        ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                        // tvAccountNumber.setText(txnDetailsData.getBeneficiaryData().getMobileNumber());
                        if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getMobileNumber())) {
                            tvAccountNumber.setText(txnDetailsData.getBeneficiaryData().getMobileNumber());
                        } else if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getMobile_number())) {
                            tvAccountNumber.setText(txnDetailsData.getBeneficiaryData().getMobile_number());
                        }
                    } else {
                        tvAccountNumber.setText(sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD) ? Validation.getMaskedString(txnDetailsData.getBeneficiaryData().getAccountNumber()) : txnDetailsData.getBeneficiaryData().getAccountNumber());
                    }


                    tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getSend_amount()));
                    tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionData().getCcyName());
                    tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTotalTxnAmount()));
                    tvTotalToPayCurrencyCode.setText("AED");


                    if (txnDetailsData.getInvoiceFlag() != null && txnDetailsData.getInvoiceFlag().equalsIgnoreCase("Y")) {
                        if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                            btnSendInvoice.setEnabled(true);
                        }
                    } else {
                        btnSendInvoice.setEnabled(true);
                    }

                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText(getString(R.string.txt_credit_card));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                    }


                    if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Pending");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Expired");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Rejected");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_INCOMPLETE)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    }


                }


//--------------------------------------------DKG_--------------------------------------------------

                else if (txnDetailsData.getTxnType().equalsIgnoreCase("CC") && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
                    ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(app.alansari.R.string.cardholder_name));
                    ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.bank_name));
                    ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.card_type));
                    ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.card_number));

                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCard_holder_name())) {
                        tvName.setText(txnDetailsData.getBeneficiaryData().getCard_holder_name());
                    } else {
                        tvName.setText(txnDetailsData.getBeneficiaryData().getBeneficiaryName());
                    }

                    tvBankName.setText(txnDetailsData.getBeneficiaryData().getBankName());

                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCard_type())) {
                        tvBranchName.setText(txnDetailsData.getBeneficiaryData().getCard_type());
                    } else {
                        tvBranchName.setText(txnDetailsData.getBeneficiaryData().getBranchName());
                    }

                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCard_number())) {
                        tvAccountNumber.setText(Validation.getMaskedString(txnDetailsData.getBeneficiaryData().getCard_number()));
                    } else {
                        tvAccountNumber.setText(sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD) ? Validation.getMaskedString(txnDetailsData.getBeneficiaryData().getAccountNumber()) : txnDetailsData.getBeneficiaryData().getAccountNumber());

                    }


                    tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getTxnAmount()));
                    // tvTotalReceiveCurrencyCode.setText(txnDetailsData.getBeneficiaryData().getCcy_desc());

                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCcy_desc())) {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getBeneficiaryData().getCcy_desc());
                    } else {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionData().getCcyName());
                    }
                    tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTotalTxnAmount()));
                    tvTotalToPayCurrencyCode.setText("AED");


                    if (txnDetailsData.getInvoiceFlag() != null && txnDetailsData.getInvoiceFlag().equalsIgnoreCase("Y")) {
                        if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                            btnSendInvoice.setEnabled(true);
                        }
                    } else {
                        btnSendInvoice.setEnabled(true);
                    }

                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText(getString(R.string.txt_credit_card));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                    }


                    if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Pending");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Expired");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Rejected");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_INCOMPLETE)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    }


                }


//------------------------------------------BT------------------------------------------------------


                else if (txnDetailsData.getTxnType().equalsIgnoreCase("BT") && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
                    ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(R.string.account_number));
                    ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.beneficiary_branch_payout));
                    ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(R.string.beneficiary_name));
                    ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.beneficiary_bank_agent));

                    tvName.setText(txnDetailsData.getBeneficiaryData().getFull_name());
                    tvBankName.setText(txnDetailsData.getTransactionData().getRoutine_bank_name());
                    tvBranchName.setText(txnDetailsData.getBeneficiaryData().getBene_branch_name());
                    tvAccountNumber.setText((txnDetailsData.getBeneficiaryData().getBene_account_no()));


                    tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getTxnAmount()));
                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCcy_desc())) {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getBeneficiaryData().getCcy_desc());
                    } else {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionData().getCcyName());
                    }


                    tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTotalTxnAmount()));
                    tvTotalToPayCurrencyCode.setText("AED");


                    if (txnDetailsData.getInvoiceFlag() != null && txnDetailsData.getInvoiceFlag().equalsIgnoreCase("Y")) {
                        if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                            btnSendInvoice.setEnabled(true);
                        }
                    } else {
                        btnSendInvoice.setEnabled(true);
                    }

                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText(getString(R.string.txt_credit_card));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                    }


                    if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Pending");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Expired");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Rejected");
                        }
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                        btnSendInvoice.setEnabled(false);
                    } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_INCOMPLETE)) {
                        ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                        if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                            tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                        } else {
                            tvTxnStatus.setText("Transaction Completed");
                        }
                        //btnSendInvoice.setEnabled(true);
                        tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                    }


                }


//--------------------------------------------------------------------------------------------------
                


                else {
                    if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getBeneficiaryName())) {
                        tvName.setText(txnDetailsData.getBeneficiaryData().getBeneficiaryName());
                    } else {
                        tvName.setText(txnDetailsData.getBeneficiaryData().getBeneficiaryNameAr());
                    }
                    tvBankName.setText(txnDetailsData.getBeneficiaryData().getBankName());
                    tvBranchName.setText(txnDetailsData.getBeneficiaryData().getBranchName());
                    if (!sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD) && (txnDetailsData.getTxnRecType() != null && txnDetailsData.getTxnRecType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) || (txnDetailsData.getTxnType() != null && txnDetailsData.getTxnType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP))) {
                        ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                        //tvAccountNumber.setText(txnDetailsData.getBeneficiaryData().getMobileNumber());
                        tvAccountNumber.setText(txnDetailsData.getBeneficiaryData().getMobileNumber());
                    } else {
                        tvAccountNumber.setText(sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD) ? Validation.getMaskedString(txnDetailsData.getBeneficiaryData().getAccountNumber()) : txnDetailsData.getBeneficiaryData().getAccountNumber());
                    }

                    tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getTxnAmount()));
                    tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionData().getCcyName());
                    tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTotalTxnAmount()));
                    tvTotalToPayCurrencyCode.setText("AED");


                    // Log.e("vvacvah",""+txnDetailsData=getIntent().getExtras().getParcelable(Constants.OBJECT))


//----------------------------------------DKG--------------------------------------------------------

//--------------------------------------------------------------------------------------------------


                    /*else {
//--------------------------------------------------------------------------------------------------
                        if (txnDetailsData.getBeneficiaryData().getPayMode().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                            tvPaymentMode.setText("Pay at Branch");
                            ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                        } else if (txnDetailsData.getBeneficiaryData().getPayMode().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                            tvPaymentMode.setText("Bank Transfer");
                            ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                        } else if (txnDetailsData.getBeneficiaryData().getPayMode().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                            tvPaymentMode.setText("Priority Pay");
                            ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                        }

                    }*/
                }


                if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                        tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                    } else {
                        tvTxnStatus.setText("Transaction Completed");
                    }
                    //btnSendInvoice.setEnabled(true);
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                        tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                    } else {
                        tvTxnStatus.setText("Transaction Pending");
                    }
                    //btnSendInvoice.setEnabled(true);
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                    if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                        tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                    } else {
                        tvTxnStatus.setText("Transaction Expired");
                    }
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    btnSendInvoice.setEnabled(false);
                } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                    if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                        tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                    } else {
                        tvTxnStatus.setText("Transaction Rejected");
                    }
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    btnSendInvoice.setEnabled(false);
                } else if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_INCOMPLETE)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getRejectionMessage() != null && !TextUtils.isEmpty(txnDetailsData.getRejectionMessage())) {
                        tvTxnStatus.setText(txnDetailsData.getRejectionMessage());
                    } else {
                        tvTxnStatus.setText("Transaction Completed");
                    }
                    //btnSendInvoice.setEnabled(true);
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                }
                if (txnDetailsData.getServiceType().equalsIgnoreCase("CE")) {
                    ((TextView) findViewById(R.id.pay_mode_label)).setText("CE Number");
                    // Log.e("sdfhsuighf", "" + txnDetailsData.getServiceType()+" bnvgyhg"+ " "+txnDetailsData.getTxnType());
                   /* if (txnDetailsData.getServiceType().equalsIgnoreCase(Constants.CE_MAPPING) && txnDetailsData.getTxnType().equalsIgnoreCase("DT")) {
                        // txnDetailsData.setBeneficiaryData(txnDetailsData.getBeneficiaryDataTt());
                       // txnDetailsData.setTransactionData(txnDetailsData.getTransactionDataTt());
                    }*/
                    //txnDetailsData.setTransactionData(txnDetailsData.getTransactionDataTt());

                    tvPaymentMode.setText(txnDetailsData.getTransactionData().getDoc_number());
                    ivPaymentMode.setVisibility(View.GONE);

                } else {
                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText(getString(R.string.txt_credit_card));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                    }

                }


                if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_PENDING_LIST)) {
                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        btnSendInvoice.setEnabled(true);
                    }

                } else {
                    if (txnDetailsData.getInvoiceFlag() != null && txnDetailsData.getInvoiceFlag().equalsIgnoreCase("Y")) {
                        if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                            btnSendInvoice.setEnabled(true);
                        }
                    } else {
                        btnSendInvoice.setEnabled(false);
                    }
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            finish();
        }
       /* if (!Validation.isValidString(CommonUtils.getInvoiceEmail()) && btnSendInvoice.isEnabled()) {
            onClick(btnSendInvoice);
        }*/
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
            case app.alansari.R.id.send_again_btn:
                if (sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD)) {
                    intent = new Intent(context, CreditCardPaymentActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(context, BeneficiaryActivity.class);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                    startActivity(intent);
                    finish();
                }
                //showFeedbackScreen();
                break;
            case app.alansari.R.id.send_invoice_btn:
                showDialog(Constants.INVOICE_CONFIRM);
                break;
        }
    }

    private void showDialog(final String responseType) {
        myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
        emailLayout = (TextInputLayout) myDialog.findViewById(app.alansari.R.id.email_layout);
        etEmail = (EditText) myDialog.findViewById(app.alansari.R.id.email);

        if (responseType.equalsIgnoreCase(Constants.INVOICE_CONFIRM)) {
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Send e-receipt");
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_btn_yes)).setText(getString(app.alansari.R.string.btn_send));
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_btn_no)).setText(getString(app.alansari.R.string.btn_cancel));
            myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.VISIBLE);
            myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.GONE);

            String invoiceEmail = CommonUtils.getInvoiceEmail();
            if (!Validation.isValidString(invoiceEmail))
                invoiceEmail = CommonUtils.getUserEmail();

            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getResources().getString(app.alansari.R.string.send_invoice_confirm_text));
            emailLayout.setVisibility(View.VISIBLE);
            (myDialog.findViewById(app.alansari.R.id.email_view)).setVisibility(View.VISIBLE);
            etEmail.setText(invoiceEmail);
            etEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (etEmail.getText() != null && etEmail.getText().toString().trim().length() > 0) {
                        emailLayout.setError(null);
                        emailLayout.setErrorEnabled(false);
                    } else {
                        emailLayout.setError(getResources().getString(app.alansari.R.string.error_enter_valid_email));
                        emailLayout.setErrorEnabled(true);
                    }
                }
            });

        } else {
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Thank you!");
            if (!TextUtils.isEmpty(message)) {
                ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(message);
            } else {
                ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getResources().getString(app.alansari.R.string.send_invoice_completed_text));
            }
            message = "";
            myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
            myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
            emailLayout.setVisibility(View.GONE);
            (myDialog.findViewById(app.alansari.R.id.email_view)).setVisibility(View.GONE);
            ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText("Done");
        }

        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    if (etEmail.getText() != null && etEmail.getText().toString().trim().length() > 0 && Validation.isValidEmail(etEmail.getText().toString().trim())) {
                        sendInvoiceApi(etEmail.getText().toString().trim());
                        myDialog.dismiss();
                    } else {
                        emailLayout.setError(getResources().getString(app.alansari.R.string.error_enter_valid_email));
                        emailLayout.setErrorEnabled(true);
                    }
                }
            }
        });

        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    private void sendInvoiceApi(String email) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().sendInvoiceAPI(CommonUtils.getUserId(), txnDetailsData.getId(), email, txnDetailsData.getServiceType(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SEND_INVOICE_API_URL, SEND_INVOICE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(SEND_INVOICE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, SEND_INVOICE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SEND_INVOICE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case SEND_INVOICE_API:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            CommonUtils.setInvoiceEmail(etEmail.getText().toString().trim());
                            message = response.getString(Constants.MESSAGE);
                            showDialog(Constants.INVOICE_DONE);
                            btnSendInvoice.setEnabled(false);
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
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

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

}