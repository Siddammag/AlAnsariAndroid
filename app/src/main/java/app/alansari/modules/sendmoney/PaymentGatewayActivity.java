package app.alansari.modules.sendmoney;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import im.delight.android.webview.AdvancedWebView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SET_FUND_COMPLETED_REMITTANCE_API;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SET_FUND_REJECTED_REMITTANCE_API;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SET_TXN_COMPLETED_REMITTANCE_API;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SET_TXN_REJECTED_REMITTANCE_API;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 24 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class PaymentGatewayActivity extends AppCompatActivity implements OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    String sessionTime;
    private boolean requesting;
    private Context context;
    //private WebView webView;
    private AdvancedWebView webView;

    private TextView tvTitle;
    private View transparentView;
    private TxnDetailsData dataObject;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private String url, successUrl, errorUrl;

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
        final Dialog exitDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
        ((TextView) exitDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.payment_gateway_exit_dialog_title));
        ((TextView) exitDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.payment_gateway_exit_dialog_text));
        ((Button) exitDialog.findViewById(app.alansari.R.id.dialog_btn_yes)).setText(getString(R.string.btn_yes));
        ((Button) exitDialog.findViewById(app.alansari.R.id.dialog_btn_no)).setText(getString(app.alansari.R.string.btn_no));
        exitDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTxnCompletedRemittanceApi(Constants.TRANSACTION_STATUS_REJECTED);
                if (getIntent().getExtras().getString(Constants.SOURCE) != null && (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE) || getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY))) {
                    CommonUtils.goToDashBoard(context, null, null, null);
                } else {
                    finish();
                }

                exitDialog.dismiss();
            }
        });

        exitDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }

    private void setTxnCompletedRemittanceApi(final String status) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (requesting) {
                return;
            }
            requesting = true;


            if (getIntent().getExtras().getString(Constants.SOURCE_TYPE) != null && getIntent().getExtras().getString(Constants.SOURCE_TYPE).equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                new Handler().postDelayed(new Runnable() {
                    @Override

                    public void run() {
                        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().setTxnCompletedRemittanceApi(
                                CommonUtils.getUserId(), dataObject.getId(), status, dataObject.getServiceType(), LogoutCalling.getDeviceID(context), sessionTime), Constants.WU_SET_TXN_UAE_PG_COMPLETED_REMITTANCE_API_URL,
                                (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) ? SET_TXN_COMPLETED_REMITTANCE_API : SET_TXN_REJECTED_REMITTANCE_API,
                                Request.Method.PUT, PaymentGatewayActivity.this);
                        AppController.getInstance().addToRequestQueue(jsonObjReq, SET_TXN_COMPLETED_REMITTANCE_API.toString());
                        if (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED))
                            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SET_TXN_COMPLETED_REMITTANCE_API.toString(), false);
                    }
                }, 2000);

            } else {
                if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY)){
                    new Handler().postDelayed(new Runnable() {
                        @Override

                        public void run() {
                            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().setTxnCompletedRemittanceApi(
                                    CommonUtils.getUserId(), dataObject.getId(), status, dataObject.getServiceType(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SET_TXN_UAE_PG_COMPLETED_REMITTANCE_API_URL,
                                    (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) ? SET_TXN_COMPLETED_REMITTANCE_API : SET_TXN_REJECTED_REMITTANCE_API,
                                    Request.Method.PUT, PaymentGatewayActivity.this);
                            AppController.getInstance().addToRequestQueue(jsonObjReq, SET_TXN_COMPLETED_REMITTANCE_API.toString());
                            if (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED))
                                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SET_TXN_COMPLETED_REMITTANCE_API.toString(), false);
                        }
                    }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override

                        public void run() {
                            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().setTxnCompletedRemittanceApi(
                                    CommonUtils.getUserId(), dataObject.getId(), status, dataObject.getServiceType(), LogoutCalling.getDeviceID(context), sessionTime), Constants.SET_TXN_COMPLETED_REMITTANCE_API_URL,
                                    (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) ? SET_TXN_COMPLETED_REMITTANCE_API : SET_TXN_REJECTED_REMITTANCE_API,
                                    Request.Method.PUT, PaymentGatewayActivity.this);
                            AppController.getInstance().addToRequestQueue(jsonObjReq, SET_TXN_COMPLETED_REMITTANCE_API.toString());
                            if (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED))
                                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SET_TXN_COMPLETED_REMITTANCE_API.toString(), false);
                        }
                    }, 2000);
                }
            }


        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.payment_gateway_activity);
        context = this;
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Priority Pay");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
        setWebView();
        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            url = getIntent().getStringExtra(Constants.GATEWAY_URL).replaceAll(" ", "%20");
            successUrl = dataObject.getSuccessUrl();
            errorUrl = dataObject.getFailureUrl();
            LogUtils.v("Teja Url", "url :" + url);
            LogUtils.d("DKG ok", "URL23 "+successUrl);
            /*if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY)) {
                tvTitle.setText("UAE PGS Payment Gateway");

                if (Constants.BASE_URL.equalsIgnoreCase(Constants.BASE_URL_TESTING) || Constants.BASE_URL.equalsIgnoreCase(Constants.WU_BASE_URL_TESTING)) {
                    *//*Test*//*
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL_TEST1 + url);
                } else {
                    *//*Live*//*
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL1 + url);
                }
            } else {
                if (Constants.BASE_URL.equalsIgnoreCase(Constants.BASE_URL_TESTING)) {
                    *//*Test*//*
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL_TEST + url);
                } else {
                    *//*Live*//*
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL + url);
                }
            }*/

            if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY)) {
                tvTitle.setText("UAE PGS Payment Gateway");
                if (Constants.TESTING_ENVIROMENT==1) {
                    //Test
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL_TEST1 + url);
                } else {
                    //Live
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL1 + url);
                }
            } else {
                if (Constants.TESTING_ENVIROMENT==1) {
                    //Test
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL_TEST + url);
                } else {
                    //Live
                    webView.loadUrl(Constants.PAYMENT_GATEWAY_BASE_URL + url);
                }
            }
        }
    }

    private void init() {

        tvTitle = (TextView) findViewById(app.alansari.R.id.toolbar_title);
        webView = (AdvancedWebView) findViewById(app.alansari.R.id.web_view);
        transparentView = findViewById(app.alansari.R.id.transparent_view);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
//        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
//        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        transparentView.setVisibility(View.GONE);
    }

    private void setWebView() {
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        //webView.getSettings().setSupportZoom(false);

        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.getSettings().setDomStorageEnabled(true);
        //webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY)) {
                    tvTitle.setText("UAE PGS Payment Gateway");
                } else {
                    tvTitle.setText(title);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        try {
            CommonUtils.hideLoading();
        } catch (Exception e) {
            Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
        }
        super.onDestroy();
        stopLogoutTimer();
    }

    private void setFundCompletedRemittanceApi(final String status) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (requesting) {
                return;
            }
            final String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            requesting = true;
            new Handler().postDelayed(new Runnable() {
                @Override

                public void run() {
                    JsonObjectRequest jsonObjReq = null;
                    if (getIntent().getExtras().getString(Constants.SOURCE_TYPE) != null && getIntent().getExtras().getString(Constants.SOURCE_TYPE).equalsIgnoreCase(Constants.TRANSACTION_TYPE_WU)) {
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().setFundCompletedRemittanceApi(
                                CommonUtils.getUserId(),
                                dataObject.getTxnCode(),
                                getServiceType(dataObject.getServiceType()),
                                LogoutCalling.getDeviceID(context), sessionTime),
                                Constants.WU_SET_FUND_COMPLETED_REMITTANCE_API_URL,
                                (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) ? SET_FUND_COMPLETED_REMITTANCE_API : SET_FUND_REJECTED_REMITTANCE_API,
                                Request.Method.PUT, PaymentGatewayActivity.this);
                    } else {
                        jsonObjReq = new CallAddr().executeApi(new APIRequestParams().setFundCompletedRemittanceApi(
                                CommonUtils.getUserId(),
                                dataObject.getTxnCode(),
                                getServiceType(dataObject.getServiceType()),
                                LogoutCalling.getDeviceID(context), sessionTime),
                                Constants.SET_FUND_COMPLETED_REMITTANCE_API_URL,
                                (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING)) ? SET_FUND_COMPLETED_REMITTANCE_API : SET_FUND_REJECTED_REMITTANCE_API,
                                Request.Method.PUT, PaymentGatewayActivity.this);
                    }

                    AppController.getInstance().addToRequestQueue(jsonObjReq, SET_FUND_COMPLETED_REMITTANCE_API.toString());
//                    if (status.equalsIgnoreCase(Constants.TRANSACTION_STATUS_COMPLETED))
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), SET_FUND_COMPLETED_REMITTANCE_API.toString(), false);
                }
            }, 2000);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private String getServiceType(String service) {
        switch (service) {
            case Constants.AREX_MAPPING:
                return Constants.TRANSACTION_TYPE_VALUE;
            case Constants.CE_MAPPING:
                return Constants.TRANSACTION_TYPE_INSTANT;
            default:
                break;
        }
        return service;
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case SET_TXN_COMPLETED_REMITTANCE_API:
                requesting = false;
                try {
                    CommonUtils.hideLoading();
                } catch (Exception e) {
                    Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
                }
                transparentView.setVisibility(View.GONE);
                if (status == 1) {
                    try {
                        //  if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) || response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            Intent intent = new Intent(context, TransactionCompletedActivity.class);
                            intent.putExtra(Constants.OBJECT, dataObject);
                            intent.putExtra(Constants.SOURCE_TYPE, getIntent().getExtras().getString(Constants.SOURCE_TYPE));
                            if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE))
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_DETAILS);
                            else
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_PENDING_LIST);
                            context.startActivity(intent);
                            finish();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            //Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case SET_TXN_REJECTED_REMITTANCE_API:
                LogUtils.d("onResponse", "SET_TXN_REJECTED_REMITTANCE_API :  " + response);
                requesting = false;
                try {
                    CommonUtils.hideLoading();
                } catch (Exception e) {
                    Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
                }

                break;
            case SET_FUND_COMPLETED_REMITTANCE_API:
                LogUtils.d("onResponse", "SET_FUND_COMPLETED_REMITTANCE_API :  " + response);
                requesting = false;
                try {
                    CommonUtils.hideLoading();
                } catch (Exception e) {
                    Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
                }
                transparentView.setVisibility(View.GONE);
                if (status == 1) {
                    try {
                         if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) || response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                        //if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            dataObject.setRejectionMessage(response.getString(Constants.MESSAGE));
                            if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                                dataObject.setTxnStatus(Constants.TRANSACTION_STATUS_COMPLETED);
                            } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                                dataObject.setTxnStatus(Constants.TRANSACTION_STATUS_REJECTED);
                            }
                            Intent intent = new Intent(context, TransactionCompletedActivity.class);
                            intent.putExtra(Constants.OBJECT, dataObject);
                            intent.putExtra(Constants.SOURCE_TYPE, getIntent().getExtras().getString(Constants.SOURCE_TYPE));
                            if (getIntent().getExtras().getString(Constants.SOURCE) != null && getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE))
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_DETAILS);
                            else
                                intent.putExtra(Constants.SOURCE, Constants.SOURCE_TRANSACTION_PENDING_LIST);
                            context.startActivity(intent);
                            finish();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            //Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case SET_FUND_REJECTED_REMITTANCE_API:
                LogUtils.d("onResponse", "SET_FUND_REJECTED_REMITTANCE_API :  " + response);
                requesting = false;
                try {
                    CommonUtils.hideLoading();
                } catch (Exception e) {
                    Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
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

    // Use When the user clicks a link from a web page in your WebView
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d("Web ok", "URL " + url);
            if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.TYPE_SEND_MONEY)) {
/*                if (!CommonUtils.isDialogVisible()) {
//                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
                }*/
                /*if (url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST1) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST2)
                        || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST3) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST4)
                        || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST5) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST6)
                        || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST7) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST8)) {
                    CommonUtils.hideLoading();
                }*/
                //if (url.equalsIgnoreCase(successUrl)) {
                if (url.equalsIgnoreCase(successUrl)||url.equalsIgnoreCase(Constants.UAE_PGS_PAYMENT_SUCESS_URL)) {

                    webView.setVisibility(View.GONE);
                    transparentView.setVisibility(View.VISIBLE);
                    setFundCompletedRemittanceApi(Constants.TRANSACTION_STATUS_PENDING);
                } else if (url.equalsIgnoreCase(errorUrl)) {
                    transparentView.setVisibility(View.VISIBLE);
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_PAYMENT_URL_TEST)) {
                    try {
                        CommonUtils.hideLoading();
                    } catch (Exception e) {
                        Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
                    }
                }
                //if (url.equalsIgnoreCase(successUrl)) {
                if (url.equalsIgnoreCase(successUrl)|| url.equalsIgnoreCase(Constants.UAE_PGS_PAYMENT_SUCESS_URL)) {
                    webView.setVisibility(View.GONE);
                    transparentView.setVisibility(View.VISIBLE);
                    setTxnCompletedRemittanceApi(Constants.TRANSACTION_STATUS_PENDING);
                } else if (url.equalsIgnoreCase(errorUrl) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_SESSION_TIME_OUT_URL) || url.equalsIgnoreCase(Constants.PAYMENT_GATEWAY_SESSION_TIME_OUT_URL_TEST)) {
                    transparentView.setVisibility(View.VISIBLE);
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {
                Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
            }
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest reques, WebResourceError error) {
            Crashlytics.logException(new Exception("Web Error : " + error.toString()));
            if (!error.toString().contains("com.android.webview.chromium")) {
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                finish();
            }

        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {
                Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
            }
//            handler.proceed();

            final Dialog exitDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            exitDialog.setCanceledOnTouchOutside(true);
            exitDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
            ((TextView) exitDialog.findViewById(app.alansari.R.id.dialog_title)).setText("");
            ((TextView) exitDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.notification_error_ssl_cert_invalid));
            ((Button) exitDialog.findViewById(app.alansari.R.id.dialog_btn_yes)).setText("Continue");
            ((Button) exitDialog.findViewById(app.alansari.R.id.dialog_btn_no)).setText("Cancel");
            exitDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.proceed();
                    exitDialog.dismiss();
                }
            });

            exitDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.cancel();
                    exitDialog.dismiss();
                    onBackPressed();
                }
            });
            exitDialog.show();
        }
    }


}