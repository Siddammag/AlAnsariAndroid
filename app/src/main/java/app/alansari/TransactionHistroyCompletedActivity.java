package app.alansari;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.transactionhistroy.TxnDetailsHistroyTravelCard;
import app.alansari.models.transactionhistroy.WALLETDETAILSItem;
import app.alansari.modules.sendmoney.TransactionRateActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SEND_INVOICE_API;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class TransactionHistroyCompletedActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private String sourceType;
    private TxnDetailsHistroyTravelCard txnDetailsData;

    private Dialog myDialog;
    private EditText etEmail;
    private TextInputLayout emailLayout;
    private Button btnSendAgain, btnSendInvoice;
    private AppCompatImageView ivPaymentMode, ivTxnStatus;
    private TextView tvTempTransactionNumber, tvPaymentMode, tvName, tvBankName, tvBranchName, tvAccountNumber, tvPaymentDate;
    private TextView tvTxnStatus, tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;
    private String message = "";
    private String sessionTime;
    private TextView tvLabelCurrency1,tvLabelCurrency2,tvLabelCurrency3,tvLabelCurrency4,tvLabelCurrency5,tvLabelCurrency6;
    private TextView tvLabelName1,tvLabelName2,tvLabelName3,tvLabelName4,tvLabelName5,tvLabelName6;
    private List<WALLETDETAILSItem> itemList;

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
        // showFeedbackScreen();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_histroy_completed);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_DETAILS)) {
            getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.svg_toolbar_home);
        } else {*/
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        //}
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        init();
        if (getIntent().getExtras() != null) {
            txnDetailsData = getIntent().getExtras().getParcelable(Constants.OBJECT);
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





        tvLabelCurrency1=(TextView)findViewById(R.id.tv_label_currency1);
        tvLabelCurrency2=(TextView)findViewById(R.id.tv_label_currency2);
        tvLabelCurrency3=(TextView)findViewById(R.id.tv_label_currency3);
        tvLabelCurrency4=(TextView)findViewById(R.id.tv_label_currency4);
        tvLabelCurrency5=(TextView)findViewById(R.id.tv_label_currency5);
        tvLabelCurrency6=(TextView)findViewById(R.id.tv_label_currency6);


        tvLabelName1=(TextView)findViewById(R.id.tv_label_name1);
        tvLabelName2=(TextView)findViewById(R.id.tv_label_name2);
        tvLabelName3=(TextView)findViewById(R.id.tv_label_name3);
        tvLabelName4=(TextView)findViewById(R.id.tv_label_name4);
        tvLabelName5=(TextView)findViewById(R.id.tv_label_name5);
        tvLabelName6=(TextView)findViewById(R.id.tv_label_name6);
        itemList=new ArrayList<>();



    }

    private void setInitialData() {
        try {
            ((TextView) findViewById(app.alansari.R.id.total_receive_label)).setText(getString(app.alansari.R.string.receive_amount));
            ((TextView) findViewById(app.alansari.R.id.total_to_pay_label)).setText(getString(app.alansari.R.string.total_to_pay));


            ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.card_number));


            if (txnDetailsData != null) {
                tvTempTransactionNumber.setText(String.valueOf(txnDetailsData.getDOCNUMBER()));
                btnSendAgain.setEnabled(true);
                if (String.valueOf(txnDetailsData.getCREATEDDATE()) != null && String.valueOf(txnDetailsData.getCREATEDDATE()).trim().length() > 0)
                    tvPaymentDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(String.valueOf(txnDetailsData.getCREATEDDATE()))));

                if (Validation.isValidString(txnDetailsData.getCARDNUMBER())) {
                    tvAccountNumber.setText(txnDetailsData.getCARDNUMBER());
                }


                //tvTotalToPay.setText(CommonUtils.addCommaToString(String.valueOf(txnDetailsData.getTOTALAMOUNT())));
               // tvTotalToPayCurrencyCode.setText("AED");


                if (txnDetailsData.getINVOICEFLAG() != null && txnDetailsData.getINVOICEFLAG().equalsIgnoreCase("Y")) {
                    if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        btnSendInvoice.setEnabled(true);
                    }
                } else {
                    btnSendInvoice.setEnabled(true);
                }

                if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                    tvPaymentMode.setText("Pay at Branch");
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                } else if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                    tvPaymentMode.setText("Bank Transfer");
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                } else if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                    tvPaymentMode.setText(getString(R.string.txt_credit_card));
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                }
                tvTotalToPay.setText(CommonUtils.addCommaToString(String.valueOf(txnDetailsData.getTOTALAMOUNT())));
                tvTotalToPayCurrencyCode.setText("AED");

                if(txnDetailsData.getWALLETDETAILS()!=null && txnDetailsData.getWALLETDETAILS().size() >0){
                    itemList=txnDetailsData.getWALLETDETAILS();
                    for(int i=0;i<itemList.size();i++){
                        switch (i){
                            case 0:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency1.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName1.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                }else{
                                    tvLabelCurrency1.setText("");
                                    tvLabelName1.setText("");
                                }
                                break;

                            case 1:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency2.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName2.setText("AED"+ "  "+(itemList.get(i).getTOTALVALUEAED()));
                                }else{
                                    tvLabelCurrency2.setText("");
                                    tvLabelName2.setText("");
                                }
                                break;

                            case 2:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency3.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName3.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                }else{
                                    tvLabelCurrency3.setText("");
                                    tvLabelName3.setText("");
                                }
                                break;

                            case 3:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency4.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName4.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                }else{
                                    tvLabelCurrency4.setText("");
                                    tvLabelName4.setText("");
                                }
                                break;

                            case 4:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency5.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName5.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                }else{
                                    tvLabelCurrency5.setText("");
                                    tvLabelName5.setText("");
                                }
                                break;

                            case 5:
                                if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getaMOUNT()) >= 0){
                                    tvLabelCurrency6.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getaMOUNT());
                                    tvLabelName6.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                }else{
                                    tvLabelCurrency6.setText("");
                                    tvLabelName6.setText("");
                                }
                                break;


                        }

                    }

                }



                if (txnDetailsData.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getSTATUSMSG() != null && !TextUtils.isEmpty(txnDetailsData.getSTATUSMSG())) {
                        tvTxnStatus.setText(txnDetailsData.getSTATUSMSG());
                    } else {
                        tvTxnStatus.setText("Transaction Completed");
                    }

                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                } else if (txnDetailsData.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getSTATUSMSG() != null && !TextUtils.isEmpty(txnDetailsData.getSTATUSMSG())) {
                        tvTxnStatus.setText(txnDetailsData.getSTATUSMSG());
                    } else {
                        tvTxnStatus.setText("Transaction Pending");
                    }

                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                } else if (txnDetailsData.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_EXPIRED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                    if (txnDetailsData.getSTATUSMSG() != null && !TextUtils.isEmpty(txnDetailsData.getSTATUSMSG())) {
                        tvTxnStatus.setText(txnDetailsData.getSTATUSMSG());
                    } else {
                        tvTxnStatus.setText("Transaction Expired");
                    }
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    btnSendInvoice.setEnabled(false);
                } else if (txnDetailsData.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_rate_alert_close_icon);
                    if (txnDetailsData.getSTATUSMSG() != null && !TextUtils.isEmpty(txnDetailsData.getSTATUSMSG())) {
                        tvTxnStatus.setText(txnDetailsData.getSTATUSMSG());
                    } else {
                        tvTxnStatus.setText("Transaction Rejected");
                    }
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                    btnSendInvoice.setEnabled(false);
                } else if (txnDetailsData.getTXNSTATUS().equalsIgnoreCase(Constants.TRANSACTION_STATUS_INCOMPLETE)) {
                    ivTxnStatus.setImageResource(app.alansari.R.drawable.svg_success);
                    if (txnDetailsData.getSTATUSMSG() != null && !TextUtils.isEmpty(txnDetailsData.getSTATUSMSG())) {
                        tvTxnStatus.setText(txnDetailsData.getSTATUSMSG());
                    } else {
                        tvTxnStatus.setText("Transaction Completed");
                    }
                    //btnSendInvoice.setEnabled(true);
                    tvTxnStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                }

                if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                    tvPaymentMode.setText("Pay at Branch");
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                } else if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                    tvPaymentMode.setText("Bank Transfer");
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                } else if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                    tvPaymentMode.setText(getString(R.string.txt_credit_card));
                    ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                }


                if (txnDetailsData.getINVOICEFLAG() != null && txnDetailsData.getINVOICEFLAG().equalsIgnoreCase("Y")) {
                    if (txnDetailsData.getTXNPAYTYPE().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        btnSendInvoice.setEnabled(true);
                    }
                } else {
                    btnSendInvoice.setEnabled(false);
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            finish();
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
            case app.alansari.R.id.send_again_btn:
                intent = new Intent(context, TravelCardReloadActivity.class);
                startActivity(intent);
                finish();
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
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().sendInvoiceAPI(CommonUtils.getUserId(), String.valueOf(txnDetailsData.getId()), email, txnDetailsData.getSERVICETYPE(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SEND_INVOICE_API_URL, SEND_INVOICE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(SEND_INVOICE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, SEND_INVOICE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SEND_INVOICE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void showFeedbackScreen() {
        intent = new Intent(context, TransactionRateActivity.class);
        intent.putExtra(Constants.SOURCE, getIntent().getExtras().getString(Constants.SOURCE));
        intent.putExtra(Constants.REM_TXN_PK_ID, txnDetailsData.getId());
        //intent.putExtra(Constants.SERVICE_TYPE, txnDetailsData.getSERVICETYPE());
        intent.putExtra(Constants.SERVICE_TYPE, "AREX");
       // Log.e("cbjkcbsdjz",""+txnDetailsData.getId()+" "+txnDetailsData.getSERVICETYPE());
        startActivity(intent);


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