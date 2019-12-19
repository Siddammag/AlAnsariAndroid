package app.alansari;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.FileUtils;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.RealTimePermission;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsData;
import app.alansari.models.transactionhistroy.WALLETDETAILSItem;
import app.alansari.modules.branchlocator.BranchLocatorCityActivity;
import app.alansari.modules.sendmoney.PaymentGatewayActivity;
import app.alansari.modules.sendmoney.TransactionCompletedActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SUBMIT_REFERENCE_NUM_REMITTANCE_API;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.UPDATE_TRAVEL_CARD_PAYMENT;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class TransactionTravelDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, TextView.OnEditorActionListener, LogOutTimerUtil.LogOutListener {

    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private String sourceType;
    private long currentTimeMills;
    private TxnDetailsData txnDetailsData;
    private Button btnNext, btnSaveToGallery;
    private Chronometer chronometer;
    private View amountSummaryLayout, clockLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private AppCompatImageView ivPaymentMode;
    private AutoCompleteTextView etReferenceNumber;
    private TextView tvTempTransactionNumber, tvPaymentMode, tvName, tvBankName, tvBranchName, tvAccountNumber, tvAAEBankAccountNumber, tvAAEBankName, tvClickToLearn, tvAaeIban;
    private TextView tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode, tvLearnToTranferMessage,tvPaymentDate;
    private ScrollView scrollView;
    private View bottomLayout;
    private Dialog learnToTransferDialog;
    private TextView tvLabelCurrency1,tvLabelCurrency2,tvLabelCurrency3,tvLabelCurrency4,tvLabelCurrency5,tvLabelCurrency6;
    private TextView tvLabelName1,tvLabelName2,tvLabelName3,tvLabelName4,tvLabelName5,tvLabelName6;
    private List<TxnDetailsData.TRANSACTIONHISTORYDETAILWCItem> itemList;

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
        if (getIntent().getExtras().getString(Constants.SOURCE) != null && (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE) || getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_BANK_PAYMENT_ACTIVITY))) {
            CommonUtils.goToDashBoard(context, getIntent().getExtras().getString(Constants.SOURCE), txnDetailsData.getId(), txnDetailsData.getServiceType());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RealTimePermission.STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeScreenShot();
                } else {
                }
        }
    }

    private void takeScreenShot() {
        try {
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = v1.getDrawingCache();
            FileUtils.saveScreenShot(context, bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, bottomLayout.getTop());
            }
        }, 500);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_travel_details);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getExtras().getString(Constants.SOURCE) != null && (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE) || getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_BANK_PAYMENT_ACTIVITY))) {
            getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.svg_toolbar_home);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        }

        init();
        if (getIntent().getExtras() != null) {
            txnDetailsData = getIntent().getParcelableExtra(Constants.OBJECT);
            sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            //sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE);

            //Log.e("cjijhcfs", "" + sourceType+" "+txnDetailsData.getInvoiceFlag());

           //Log.e("sfhnsjfgbsb",""+txnDetailsData.getTRANSACTIONHISTORYDETAILWC().size());
            //Log.e("sfhnsjfgbsb",""+txnDetailsData.getTRANSACTIONHISTORYDETAILWC().get(0).getTOTALVALUEAED());
            if (txnDetailsData != null) {
                setInitialData();
            }
        } else {
            onBackPressed();
        }
    }

    private void init() {
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        ivPaymentMode = (AppCompatImageView) findViewById(app.alansari.R.id.pay_mode_icon);
        tvTempTransactionNumber = (TextView) findViewById(app.alansari.R.id.temp_transaction_number);
        tvPaymentMode = (TextView) findViewById(app.alansari.R.id.pay_mode);

        tvName = (TextView) findViewById(app.alansari.R.id.name);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(app.alansari.R.id.account_number);

        tvAAEBankAccountNumber = (TextView) findViewById(app.alansari.R.id.aae_bank_account_number);
        tvAAEBankName = (TextView) findViewById(app.alansari.R.id.aae_bank_name);
        tvAaeIban = (TextView) findViewById(app.alansari.R.id.aae_iban_number);
        etReferenceNumber = (AutoCompleteTextView) findViewById(app.alansari.R.id.reference_number);

        clockLayout = findViewById(R.id.clock_layout);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        tvPaymentDate = (TextView) findViewById(app.alansari.R.id.pay_date);


        amountSummaryLayout = findViewById(app.alansari.R.id.amount_summary_layout);
        tvTotalReceive = (TextView) findViewById(app.alansari.R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);

        btnSaveToGallery = (Button) findViewById(app.alansari.R.id.save_to_gallery_btn);
        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        btnSaveToGallery.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        tvClickToLearn = (TextView) findViewById(app.alansari.R.id.click_to_learn);
        scrollView = findViewById(R.id.scroll_view);
        bottomLayout = findViewById(R.id.bottom_btn_layout);
        tvClickToLearn.setText(Html.fromHtml("<u>Click to learn how to make bank transfer</u>"));
        tvClickToLearn.setOnClickListener(this);

        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            btnNext.setVisibility(View.GONE);
                            amountSummaryLayout.setVisibility(View.GONE);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnNext.setVisibility(View.VISIBLE);
                                    amountSummaryLayout.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }

        );
        learnToTransferDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
        learnToTransferDialog.setCanceledOnTouchOutside(false);
        learnToTransferDialog.setContentView(R.layout.transfer_to_learn_dialog);
        ((TextView) learnToTransferDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.learn_to_transfer_title));
        tvLearnToTranferMessage = (TextView) learnToTransferDialog.findViewById(R.id.dialog_message);
        ((ImageView) learnToTransferDialog.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learnToTransferDialog.dismiss();
            }
        });
        etReferenceNumber.setOnEditorActionListener(this);

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
            if (sourceType.equalsIgnoreCase(Constants.TYPE_CREDIT_CARD)) {
                ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(app.alansari.R.string.cardholder_name));
                ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.bank_name));
                ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.card_type));
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.card_number));
            } else if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(R.string.mobile_number));
            }

            if (txnDetailsData != null) {

                tvLearnToTranferMessage.setText(txnDetailsData.getTransferFlow());
                if (Validation.isValidString(txnDetailsData.getAaeIbanNumber())) {
                    tvAaeIban.setText(txnDetailsData.getAaeIbanNumber());
                } else {
                    tvAaeIban.setText("-");
                }
                btnNext.setEnabled(true);
                if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                    String name = "";
                    if (txnDetailsData.getBeneficiaryDataWu().getReceiverNameType().equalsIgnoreCase("D")) {
                        name = (CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverMiddleName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverLastName()));
                    } else {
                        name = (CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverFirstName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverLastName()) + " " + CommonUtils.getValidString(txnDetailsData.getBeneficiaryDataWu().getReceiverMiddleName()));
                    }
                    tvName.setText(name);
                    tvBankName.setText("WESTERN UNION");
                    tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc());
                    if (txnDetailsData.getTransactionDataWu().getDestCountryCode().equalsIgnoreCase("092")) {
                        String state = "";
                        if (txnDetailsData.getBeneficiaryDataWu().getPayoutState() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutState().equalsIgnoreCase("null")) {
                            state = txnDetailsData.getBeneficiaryDataWu().getPayoutState();
                        }
                        tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc() + "-" + state);
                    } else if (txnDetailsData.getTransactionDataWu().getDestCountryCode().equalsIgnoreCase("125")) {
                        String state = "";
                        if (txnDetailsData.getBeneficiaryDataWu().getPayoutState() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutState().equalsIgnoreCase("null")) {
                            state = txnDetailsData.getBeneficiaryDataWu().getPayoutState();
                        }
                        String city = "";
                        if (txnDetailsData.getBeneficiaryDataWu().getPayoutCity() != null && !txnDetailsData.getBeneficiaryDataWu().getPayoutCity().equalsIgnoreCase("null")) {
                            city = txnDetailsData.getBeneficiaryDataWu().getPayoutCity();
                        }
                        tvBranchName.setText(txnDetailsData.getTransactionDataWu().getDestCountryDesc() + "-" + state + "/" + city);
                    }
                    tvBranchName.setSelected(true);
                    tvAccountNumber.setText(txnDetailsData.getBeneficiaryDataWu().getReceiverContactNum());
                    ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText("MTCN");
                    if (txnDetailsData.getMtcn() != null && !txnDetailsData.getMtcn().equalsIgnoreCase("null")) {
                        tvBankName.setText(txnDetailsData.getMtcn());
                    } else {
                        tvBankName.setText("-");
                    }



                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        findViewById(app.alansari.R.id.branch_pay_layout).setVisibility(View.VISIBLE);
                        findViewById(app.alansari.R.id.bank_pay_layout).setVisibility(View.GONE);
                        ((TextView) findViewById(app.alansari.R.id.visit_to_branch_msg)).setText(getResources().getString(app.alansari.R.string.visit_to_nearest_branch_msg));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                        etReferenceNumber.setVisibility(View.GONE);
                        btnSaveToGallery.setVisibility(View.VISIBLE);
                        btnNext.getLayoutParams().width = (int) getResources().getDimension(app.alansari.R.dimen.submit_btn_width);
                        btnNext.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.btn_primary_round));
                        btnNext.setText("Branch Locator");
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        findViewById(app.alansari.R.id.branch_pay_layout).setVisibility(View.GONE);
                        findViewById(app.alansari.R.id.bank_pay_layout).setVisibility(View.VISIBLE);
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                        etReferenceNumber.setVisibility(View.VISIBLE);
                        tvClickToLearn.setVisibility(View.VISIBLE);
                        findViewById(app.alansari.R.id.question_mark).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(app.alansari.R.id.question_mark)).setImageResource(R.drawable.ic_nav_question);
                        if (Validation.isValidRate(txnDetailsData.getTxnReferenceNum())) {
                            etReferenceNumber.setText(txnDetailsData.getTxnReferenceNum());
                            etReferenceNumber.setInputType(InputType.TYPE_NULL);
                            etReferenceNumber.setFocusable(false);
                            etReferenceNumber.setClickable(false);
                            etReferenceNumber.setFocusableInTouchMode(false);
                            btnNext.setEnabled(false);
                        } else {
                            etReferenceNumber.setEnabled(true);
                            btnNext.setEnabled(false);
                            etReferenceNumber.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                                        if ((!TextUtils.isEmpty(etReferenceNumber.getText().toString().trim())) && etReferenceNumber.getText().toString().trim().length() > 0) {
                                            btnNext.setEnabled(true);
                                        } else {
                                            btnNext.setEnabled(false);
                                        }
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                        setAAEBankDetails();
                        btnSaveToGallery.setVisibility(View.GONE);
                        btnNext.setTextSize(getResources().getDimensionPixelSize(app.alansari.R.dimen.dimens_14sp) / getResources().getDisplayMetrics().density);
                        btnNext.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.btn_confirm_green));
                        btnNext.setText("Confirm");
                    }


                    tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionDataWu().getSendAmount()));
                    tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionDataWu().getCurrencyDesc());
                    tvTotalToPay.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionDataWu().getTotalTxnAmount()));
                    tvTotalToPayCurrencyCode.setText("AED");

                    clockLayout.setVisibility(View.VISIBLE);
                    if (txnDetailsData.getCreatedDate() != null && txnDetailsData.getTxnExpLimit() != null) {
                        try {
                            currentTimeMills = txnDetailsData.getCurrentTime() != null ? Long.valueOf(txnDetailsData.getCurrentTime()) : System.currentTimeMillis();
                            initClock(Long.valueOf(txnDetailsData.getCreatedDate()), Float.valueOf(txnDetailsData.getTxnExpLimit()));
                            chronometer.setBase(currentTimeMills);
                            chronometer.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }













                else {
                    if (txnDetailsData.getServiceType().equalsIgnoreCase(Constants.TXN_TYPE_TRAVEL_CARD)) {
                        txnDetailsData.setTRANSACTIONHISTORYDETAILWC(txnDetailsData.getTRANSACTIONHISTORYDETAILWC());
                        txnDetailsData.setTransactionHistroyData(txnDetailsData.getTransactionHistroyData());

                    }
                   if (Validation.isValidString(String.valueOf(txnDetailsData.getTxnCode()))) {
                       tvTempTransactionNumber.setText(String.valueOf(txnDetailsData.getTxnCode()));
                        tvName.setText(String.valueOf(txnDetailsData.getTxnCode()));

                       ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.card_number));

                        ((TextView) findViewById(app.alansari.R.id.name_label)).setText(getString(app.alansari.R.string.cardholder_name));
                        ((TextView) findViewById(app.alansari.R.id.bank_name_label)).setText(getString(app.alansari.R.string.bank_name));
                        ((TextView) findViewById(app.alansari.R.id.branch_name_label)).setText(getString(app.alansari.R.string.card_type));

                       if (String.valueOf(txnDetailsData.getTransactionHistroyData()) != null && String.valueOf(txnDetailsData.getTransactionHistroyData().getCREATEDDATE()).trim().length() > 0)
                           tvPaymentDate.setText(CommonUtils.getDateFromMilliSec(Long.valueOf(String.valueOf(txnDetailsData.getTransactionHistroyData().getCREATEDDATE()))));


                    }  else {
                        tvName.setText(txnDetailsData.getBeneficiaryData().getBeneficiaryNameAr());
                    }



                        if (Validation.isValidString(txnDetailsData.getTransactionHistroyData().getCARDNUMBER())) {
                            tvAccountNumber.setText(Validation.getMaskedString(txnDetailsData.getTransactionHistroyData().getCARDNUMBER()));
                        } else {
                            tvAccountNumber.setText("");
                        }


                    if(txnDetailsData.getTRANSACTIONHISTORYDETAILWC()!=null && txnDetailsData.getTRANSACTIONHISTORYDETAILWC().size() >0){
                        itemList=txnDetailsData.getTRANSACTIONHISTORYDETAILWC();
                        for(int i=0;i<itemList.size();i++){
                            switch (i){
                                case 0:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency1.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName1.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                    }else{
                                        tvLabelCurrency1.setText("");
                                        tvLabelName1.setText("");
                                    }
                                    break;

                                case 1:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency2.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName2.setText("AED"+ "  "+(itemList.get(i).getTOTALVALUEAED()));
                                    }else{
                                        tvLabelCurrency2.setText("");
                                        tvLabelName2.setText("");
                                    }
                                    break;

                                case 2:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency3.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName3.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                    }else{
                                        tvLabelCurrency3.setText("");
                                        tvLabelName3.setText("");
                                    }
                                    break;

                                case 3:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency4.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName4.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                    }else{
                                        tvLabelCurrency4.setText("");
                                        tvLabelName4.setText("");
                                    }
                                    break;

                                case 4:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency5.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName5.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                    }else{
                                        tvLabelCurrency5.setText("");
                                        tvLabelName5.setText("");
                                    }
                                    break;

                                case 5:
                                    if((itemList.get(i).getCCYDESC()).length()>0 &&(itemList.get(i).getAMOUNT()) >= 0){
                                        tvLabelCurrency6.setText(itemList.get(i).getCCYDESC() +"  " +itemList.get(i).getAMOUNT());
                                        tvLabelName6.setText("AED"+ "  "+((itemList.get(i).getTOTALVALUEAED())));
                                    }else{
                                        tvLabelCurrency6.setText("");
                                        tvLabelName6.setText("");
                                    }
                                    break;


                            }

                        }

                    }








//------------------------DKG-----------------------------------------------------------------------
                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        tvPaymentMode.setText("Pay at Branch");
                        findViewById(app.alansari.R.id.branch_pay_layout).setVisibility(View.VISIBLE);
                        findViewById(app.alansari.R.id.bank_pay_layout).setVisibility(View.GONE);
                        ((TextView) findViewById(app.alansari.R.id.visit_to_branch_msg)).setText(getResources().getString(app.alansari.R.string.visit_to_nearest_branch_msg));
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_branch);
                        etReferenceNumber.setVisibility(View.GONE);
                        btnSaveToGallery.setVisibility(View.VISIBLE);
                        btnNext.getLayoutParams().width = (int) getResources().getDimension(app.alansari.R.dimen.submit_btn_width);
                        btnNext.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.btn_primary_round));
                        btnNext.setText("Branch Locator");
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        findViewById(app.alansari.R.id.branch_pay_layout).setVisibility(View.GONE);
                        findViewById(app.alansari.R.id.bank_pay_layout).setVisibility(View.VISIBLE);
                        tvPaymentMode.setText("Bank Transfer");
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_bank);
                        etReferenceNumber.setVisibility(View.VISIBLE);
                        tvClickToLearn.setVisibility(View.VISIBLE);
                        findViewById(app.alansari.R.id.question_mark).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(app.alansari.R.id.question_mark)).setImageResource(R.drawable.ic_nav_question);
                        if (Validation.isValidRate(txnDetailsData.getTxnReferenceNum())) {
                            etReferenceNumber.setText(txnDetailsData.getTxnReferenceNum());
                            etReferenceNumber.setInputType(InputType.TYPE_NULL);
                            etReferenceNumber.setFocusable(false);
                            etReferenceNumber.setClickable(false);
                            etReferenceNumber.setFocusableInTouchMode(false);
                            btnNext.setEnabled(false);
                        } else {
                            etReferenceNumber.setEnabled(true);
                            btnNext.setEnabled(false);
                            etReferenceNumber.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                                        if ((!TextUtils.isEmpty(etReferenceNumber.getText().toString().trim())) && etReferenceNumber.getText().toString().trim().length() > 0) {
                                            btnNext.setEnabled(true);
                                        } else {
                                            btnNext.setEnabled(false);
                                        }
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                        setAAEBankDetails();
                        btnSaveToGallery.setVisibility(View.GONE);
                        btnNext.setTextSize(getResources().getDimensionPixelSize(app.alansari.R.dimen.dimens_14sp) / getResources().getDisplayMetrics().density);
                        btnNext.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.btn_confirm_green));
                        btnNext.setText("Confirm");
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        tvPaymentMode.setText("Priority Pay");
                        findViewById(app.alansari.R.id.branch_pay_layout).setVisibility(View.VISIBLE);
                        findViewById(app.alansari.R.id.bank_pay_layout).setVisibility(View.GONE);
                        ((TextView) findViewById(app.alansari.R.id.visit_to_branch_msg)).setText(getResources().getString(R.string.complete_transaction_msg));
                        findViewById(app.alansari.R.id.branch_pay_layout).setPadding(0, (int) (getResources().getDimensionPixelSize(R.dimen.dimens_40) / getResources().getDisplayMetrics().density), 0, 0);
                        ivPaymentMode.setImageResource(app.alansari.R.drawable.svg_payment_mode_priority_pay);
                        etReferenceNumber.setVisibility(View.GONE);
                        btnSaveToGallery.setVisibility(View.GONE);
                        if (txnDetailsData.getTxnStatus().equalsIgnoreCase(Constants.TRANSACTION_STATUS_REJECTED)) {
                            btnNext.setTextSize(getResources().getDimensionPixelSize(app.alansari.R.dimen.dimens_14sp) / getResources().getDisplayMetrics().density);
                            btnNext.getLayoutParams().width = (int) getResources().getDimension(app.alansari.R.dimen.submit_btn_width);
                            btnNext.setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.btn_primary_round));
                            btnNext.setText("Complete Your Transaction");
                            btnNext.setEnabled(true);
                        } else {
                            btnNext.setText("Complete Your Transaction");
                            btnNext.setEnabled(false);
                        }
                    }


//--------------------------------------------------------------------------------------------------

                    if (txnDetailsData.getTxnType().equalsIgnoreCase("CP")) {
                        tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getSend_amount()));
                    } else if (txnDetailsData.getTxnType().equalsIgnoreCase("BT")) {
                        tvTotalReceive.setText(CommonUtils.addCommaToString(txnDetailsData.getTransactionData().getSend_amount()));

                    }
/*
                    if (Validation.isValidString(txnDetailsData.getTransactionData().getCcyName())) {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getTransactionData().getCcyName());
                    } else if (Validation.isValidString(txnDetailsData.getBeneficiaryData().getCcy_desc())) {
                        tvTotalReceiveCurrencyCode.setText(txnDetailsData.getBeneficiaryData().getCcy_desc());
                    }*/

                    tvTotalToPay.setText(CommonUtils.addCommaToString(String.valueOf(txnDetailsData.getTotalTxnAmount())));
                    tvTotalToPayCurrencyCode.setText("AED");
                    if (!txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        clockLayout.setVisibility(View.VISIBLE);
                        if (txnDetailsData.getCreatedDate() != null && txnDetailsData.getTxnExpLimit() != null) {
                            try {
                                currentTimeMills = txnDetailsData.getCurrentTime() != null ? Long.valueOf(txnDetailsData.getCurrentTime()) : System.currentTimeMillis();
                                initClock(Long.valueOf(txnDetailsData.getCreatedDate()), Float.valueOf(txnDetailsData.getTxnExpLimit()));
                                chronometer.setBase(currentTimeMills);
                                chronometer.start();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        clockLayout.setVisibility(View.GONE);
                    }
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setAAEBankDetails() {
        if (Validation.isValidString(txnDetailsData.getAaeBankName()))
            tvAAEBankName.setText(txnDetailsData.getAaeBankName());
        else
            tvAAEBankName.setText("-");
        if (Validation.isValidString(txnDetailsData.getAaeAccountNumber()))
            tvAAEBankAccountNumber.setText(txnDetailsData.getAaeAccountNumber());
        else
            tvAAEBankAccountNumber.setText("-");

    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.save_to_gallery_btn:
                if (!RealTimePermission.checkPermissionStorage(context)) {
                    RealTimePermission.requestStoragePermission(context, RealTimePermission.STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    takeScreenShot();
                }
                break;
            case app.alansari.R.id.next_btn:
                if (sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        startActivity(new Intent(context, BranchLocatorCityActivity.class));
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        if (etReferenceNumber.getText() != null && etReferenceNumber.getText().toString().trim().length() > 0) {
                            submitWuReferenceNumRemittanceApi();
                        } else {
                            Toast.makeText(context, "Please enter a valid reference number", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) {
                        startActivity(new Intent(context, BranchLocatorCityActivity.class));
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BANK_TRANSFER)) {
                        if (etReferenceNumber.getText() != null && etReferenceNumber.getText().toString().trim().length() > 0) {
                            submitReferenceNumTravelCardApi();
                        } else {
                            Toast.makeText(context, "Please enter a valid reference number", Toast.LENGTH_SHORT).show();
                        }
                    } else if (txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_PRIORITY_PAY)) {
                        LogUtils.v("Teja Url", "url :" + txnDetailsData.getUrl());
                        intent = new Intent(context, PaymentGatewayActivity.class);
                        intent.putExtra(Constants.OBJECT, txnDetailsData);
                        intent.putExtra(Constants.GATEWAY_URL, txnDetailsData.getUrl());
                        intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_DETAILS);
                        context.startActivity(intent);
                    }
                }


                break;
            case app.alansari.R.id.click_to_learn:
                learnToTransferDialog.show();
                break;
        }
    }

    private void initClock(final long transactionTime, Float transactionLimit) {
        transactionLimit = (transactionLimit * 60 * 60 * 1000);
        //chronometer.setBase(transactionTime);
        final Float finalTransactionLimit = transactionLimit;
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer cArg) {
                float time = (currentTimeMills - transactionTime);
                currentTimeMills = currentTimeMills + 1000;
                if (time > finalTransactionLimit) {
                    chronometer.setText("0 h : 0 m : 0 s");
                    chronometer.stop();
                    if ((txnDetailsData != null && txnDetailsData.getBeneficiaryData() != null && txnDetailsData.getTxnPayType() != null && !txnDetailsData.getTxnPayType().equalsIgnoreCase(Constants.PAYMENT_MODE_BRANCH_PAY)) || sourceType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                        btnNext.setEnabled(false);
                        etReferenceNumber.setEnabled(false);
                    }

                } else {
                    time = finalTransactionLimit - time;
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    chronometer.setText(h + " h" + " : " + (m < 10 ? "0" + m + " m" : m + " m") + " : " + (s < 10 ? "0" + s + " s" : s + " s"));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }
    private void submitReferenceNumRemittanceApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().submitReferenceNumRemittanceApi(CommonUtils.getUserId(), txnDetailsData.getId(), txnDetailsData.getTxnPayType(), txnDetailsData.getServiceType(), etReferenceNumber.getText().toString().trim(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SUBMIT_REFERENCE_NUM_REMITTANCE_API_URL, SUBMIT_REFERENCE_NUM_REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }
    private void submitReferenceNumTravelCardApi(){
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().submitReferenceNumTravelCardApi(CommonUtils.getUserId(), txnDetailsData.getId(), txnDetailsData.getTxnPayType(), txnDetailsData.getServiceType(), etReferenceNumber.getText().toString().trim(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SUBMIT_REFERENCE_NUM_REMITTANCE_API_URL, UPDATE_TRAVEL_CARD_PAYMENT, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(UPDATE_TRAVEL_CARD_PAYMENT.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_TRAVEL_CARD_PAYMENT.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), UPDATE_TRAVEL_CARD_PAYMENT.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }    private void submitWuReferenceNumRemittanceApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().submitReferenceNumRemittanceApi(CommonUtils.getUserId(), txnDetailsData.getId(), txnDetailsData.getTxnPayType(), Constants.TRANSACTION_TYPE_WU, etReferenceNumber.getText().toString().trim(), LogoutCalling.getDeviceID(context), sessionTime), Constants.WU_SUBMIT_REFERENCE_NUM_REMITTANCE_API_URL, SUBMIT_REFERENCE_NUM_REMITTANCE_API, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SUBMIT_REFERENCE_NUM_REMITTANCE_API.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case SUBMIT_REFERENCE_NUM_REMITTANCE_API:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            intent = new Intent(context, TransactionCompletedActivity.class);
                            intent.putExtra(Constants.OBJECT, txnDetailsData);
                            intent.putExtra(Constants.SOURCE_TYPE, sourceType);
                            if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_BANK_PAYMENT_ACTIVITY))
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_DETAILS);
                            else
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_PENDING_LIST);
                            context.startActivity(intent);
//------------------------------------------------------DKG-----------------------------------------
                            CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                            finish();

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
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
            case UPDATE_TRAVEL_CARD_PAYMENT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            intent = new Intent(context, TransactionPagePendingTravelCard.class);
                            intent.putExtra(Constants.OBJECT, txnDetailsData);

                            /*if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_BANK_PAYMENT_ACTIVITY))
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_DETAILS);
                            else
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_PENDING_LIST);*/
                            context.startActivity(intent);

//------------------------------------------------------DKG-----------------------------------------
                            CommonUtils.setTotalPendingTransactions(response.getString(Constants.ID));
                            finish();

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
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
