package app.alansari;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.adapters.QuickSendRecyclerAdapter;
import app.alansari.customviews.ShimmerFrameLayout;
import app.alansari.fcm.FCMUtils;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.FetchEmailData;
import app.alansari.models.PopUpData;
import app.alansari.models.QuickSendModel;
import app.alansari.modules.accountmanagement.AccountManagementActivity;
import app.alansari.modules.accountmanagement.BankAccountActivity;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;
import app.alansari.modules.accountmanagement.WUBeneficiaryActivity;
import app.alansari.modules.accountmanagement.WUEnrollActivity;
import app.alansari.modules.branchlocator.BranchLocatorCityActivity;
import app.alansari.modules.currencycalculator.CurrencyConverterActivity;
import app.alansari.modules.currencycalculator.ForeignCurrencyActivity;
import app.alansari.modules.feedback.ContactUsActivity;
import app.alansari.modules.feedback.FaqActivity;
import app.alansari.modules.promotions.PromotionsActivity;
import app.alansari.modules.ratealert.RateAlertActivity;
import app.alansari.modules.sendmoney.PendingTransActivity;
import app.alansari.modules.sendmoney.TransactionHistoryActivity;
import app.alansari.modules.sendmoney.TransactionRateActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADD_QUICK_SEND;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DELETE_QUICK_SEND;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.POPUP;
import static app.alansari.Utils.Constants.mCheckEmailNo;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class DashboardActivity extends NavigationBaseActivity implements View.OnClickListener, CustomClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    String userEmailId = "";
    private Intent intent;
    private Context context;
    private String deletedQuickSendId;
    //Quick Send
    private AppCompatImageView ivClock, ivRightArrow;
    private TextView tvTotalPendingTransactions;
    private RecyclerView recyclerView;
    private QuickSendRecyclerAdapter recyclerAdapter;
    private String arexUserId;
    private String sessionTime;
    private Dialog dialog;
    private WebView webView;

    /*"REFERRAL_ACTIVE_IND":"Y",
    "REFERRAL_CODE":"TEST1",*/
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
                openMenuDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        context = this;
        checkSource();
        init();
        if (SharedPreferenceManger.justLoggedIn()) {
            fetchCountriesList();
        }
        /*if (TapTargetViewUtils.getTutStatus(context, Constants.TUT_DASHBOARD_SCREEN)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTargetViewUtils.getTargetView(findViewById(R.id.nav_menu), getString(R.string.tut_db_nav_menu_title), getString(R.string.tut_db_nav_menu_text)),
                            TapTargetViewUtils.getTargetView(findViewById(R.id.send_money_icon), getString(R.string.tut_db_send_money_title), getString(R.string.tut_db_send_money_text)),
                            TapTargetViewUtils.getTargetView(findViewById(R.id.my_recipients_layout), getString(R.string.tut_db_my_recipients_title), getString(R.string.tut_db_my_recipients_text)),
                            TapTargetViewUtils.getTargetView(findViewById(R.id.my_transactions_layout), getString(R.string.tut_db_my_transactions_title), getString(R.string.tut_db_my_transactions_text)),
                            TapTargetViewUtils.getTargetView(findViewById(R.id.pending_transactions_layout), getString(R.string.tut_db_pending_transactions_title), getString(R.string.tut_db_pending_transactions_text))
                    ).listener(TapTargetViewUtils.getTapTargetListener(context, Constants.TUT_DASHBOARD_SCREEN)).start();
        }*/ // TODO (REMOVED TUTORIALS)
        FCMUtils.checkFCM(context);
        recyclerAdapter = new QuickSendRecyclerAdapter(context, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        //showpopupDialog2(context);
        //showpopupDialog2(context);


        /*((RelativeLayout)findViewById(R.id.reg_header_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, Main2Activity.class));
            }
        });*/


    }

    private void checkSource() {
        try {
            if (getIntent().getExtras() != null && getIntent().getExtras().getString(Constants.SOURCE) != null) {
                if (getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_PAYMENT_MODE) || getIntent().getExtras().getString(Constants.SOURCE).equalsIgnoreCase(Constants.SOURCE_TRANSACTION_DETAILS)) {
                    intent = new Intent(context, TransactionRateActivity.class);
                    intent.putExtra(Constants.SOURCE, getIntent().getExtras().getString(Constants.SOURCE));
                    intent.putExtra(Constants.REM_TXN_PK_ID, getIntent().getExtras().getString(Constants.REM_TXN_PK_ID));
                    intent.putExtra(Constants.SERVICE_TYPE, getIntent().getExtras().getString(Constants.SERVICE_TYPE));
                    startActivity(intent);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        ivClock = (AppCompatImageView) findViewById(R.id.pending_transaction_clock);
        ivRightArrow = (AppCompatImageView) findViewById(R.id.pending_transaction_arrow);
        tvTotalPendingTransactions = (TextView) findViewById(R.id.pending_transaction);
        findViewById(R.id.nav_menu).setOnClickListener(this);
        findViewById(R.id.send_money_layout).setOnClickListener(this);
        findViewById(R.id.my_recipients_layout).setOnClickListener(this);
        findViewById(R.id.my_referral_layout).setOnClickListener(this);
        findViewById(R.id.transaction_tracker_layout).setOnClickListener(this);
        findViewById(R.id.my_transactions_layout).setOnClickListener(this);
        findViewById(R.id.pending_transactions_layout).setOnClickListener(this);

        //Active Referral Condition
        if (((String) SharedPreferenceManger.getPrefVal(Constants.REFERRAL_ACTIVE_IND, null, SharedPreferenceManger.VALUE_TYPE.STRING)).equalsIgnoreCase("Y")) {
            findViewById(R.id.my_referral_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.transaction_tracker_layout).setVisibility(View.GONE);
        } else{
            findViewById(R.id.my_referral_layout).setVisibility(View.GONE);
            findViewById(R.id.transaction_tracker_layout).setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (mCheckEmailNo == 1) {
            fetchEmail();
            mCheckEmailNo = 0;
        }

        fetchPopups();

        /*((ImageView)findViewById(R.id.wave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLunchAnotherApp();
            }
        });
*/
    }
//onLunchAnotherApp
    void fetchCountriesList() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCountriesList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ALL_COUNTRIES_URL, CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
        }
    }

    private void fetchEmail() {
        String userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (Validation.isValidString(userId)) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkId(userId, LogoutCalling.getDeviceID(context)), Constants.FETCH_EMAIL_NOTIFICATION_URL, FETCH_EMAIL_NOTIFICATION, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(FETCH_EMAIL_NOTIFICATION.toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_EMAIL_NOTIFICATION.toString());
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_EMAIL_NOTIFICATION.toString(), false);
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchPopups() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            //   CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.POPUP.toString(), false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPopUps((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), CommonUtils.getDeviceID(context), "A", (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING)), Constants.POPUPS_URL, POPUP, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(POPUP.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, POPUP.toString());
        } else {
            CommonUtils.setResponseToast(context, VIEW_STATE_ERROR);
        }
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        if (dataItem != null && dataItem instanceof QuickSendModel) {
            deletedQuickSendId = ((QuickSendModel) dataItem).getId();
            deleteQuickSend(deletedQuickSendId);
        } else {
            super.itemClicked(view, position, dataItem);
        }
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        String totalPendingTransactions = CommonUtils.getTotalPendingTransactions();
        LogUtils.d("ok", "TotalCounts " + totalPendingTransactions);
        if (totalPendingTransactions != null && !totalPendingTransactions.equalsIgnoreCase("0")) {
            tvTotalPendingTransactions.setText("You have " + totalPendingTransactions + " pending " + ((Integer.valueOf(totalPendingTransactions) > 1) ? "transactions" : "transaction"));
            tvTotalPendingTransactions.setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
            ivClock.setColorFilter(ContextCompat.getColor(context, R.color.colorE86768));
            ivRightArrow.setColorFilter(ContextCompat.getColor(context, R.color.color3F3F3F));
        } else {
            tvTotalPendingTransactions.setText("You have 0 pending transaction");
            tvTotalPendingTransactions.setTextColor(ContextCompat.getColor(context, R.color.color9E9E9E));
            ivClock.setColorFilter(ContextCompat.getColor(context, R.color.color9E9E9E));
            ivRightArrow.setColorFilter(ContextCompat.getColor(context, R.color.color9E9E9E));
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        ((ShimmerFrameLayout) findViewById(R.id.shimmer_view_container)).startShimmerAnimation();

        String totalPendingTransactions = CommonUtils.getTotalPendingTransactions();
        LogUtils.d("ok", "TotalCounts " + totalPendingTransactions);
        if (totalPendingTransactions != null && !totalPendingTransactions.equalsIgnoreCase("0")) {
            tvTotalPendingTransactions.setText("You have " + totalPendingTransactions + " pending " + ((Integer.valueOf(totalPendingTransactions) > 1) ? "transactions" : "transaction"));
            tvTotalPendingTransactions.setTextColor(ContextCompat.getColor(context, R.color.color3F3F3F));
            ivClock.setColorFilter(ContextCompat.getColor(context, R.color.colorE86768));
            ivRightArrow.setColorFilter(ContextCompat.getColor(context, R.color.color3F3F3F));
        } else {
            tvTotalPendingTransactions.setText("You have 0 pending transaction");
            tvTotalPendingTransactions.setTextColor(ContextCompat.getColor(context, R.color.color9E9E9E));
            ivClock.setColorFilter(ContextCompat.getColor(context, R.color.color9E9E9E));
            ivRightArrow.setColorFilter(ContextCompat.getColor(context, R.color.color9E9E9E));
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case ADD_QUICK_SEND:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<QuickSendModel> quickSendData = (ArrayList<QuickSendModel>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<QuickSendModel>>() {
                                }.getType());
                                if (quickSendData != null && quickSendData.size() > 0) {
                                    recyclerAdapter.addItemAt(quickSendData.get(0), 0);
                                    return;
                                }
                            }
                            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE_QUICK_SEND:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            for (int i = 0; i < recyclerAdapter.getItemCount() - 1; i++) {
                                if (recyclerAdapter.getItemAt(i).getId().equalsIgnoreCase(deletedQuickSendId)) {
                                    recyclerAdapter.delete(i);
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
            case FETCH_ALL_COUNTRIES:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CountryData> countryData = (ArrayList<CountryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CountryData>>() {
                                }.getType());
                                if (countryData != null && countryData.size() > 0) {
                                    SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                    SharedPreferenceManger.saveCountriesData(new Gson(), countryData);
                                    CountryData selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
                                    for (int i = 0; i < countryData.size(); i++) {
                                        if (countryData.get(i).getCountryCodeAREX().equalsIgnoreCase("91") || countryData.get(i).getCountryCodeCE().equalsIgnoreCase("091")) {
                                            SharedPreferenceManger.saveUAECountryData(new Gson(), countryData.get(i));
                                            break;
                                        }
                                        if (selectedCountry != null && countryData.get(i).getId().equalsIgnoreCase(selectedCountry.getId())) {
                                            SharedPreferenceManger.saveSelectedCountryData(new Gson(), countryData.get(i));
                                        }
                                    }
                                    SharedPreferenceManger.setPrefVal(Constants.JUST_LOGGED_IN, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                } else {
                                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;

            case EMAIL_NOTIFICATION:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            CommonUtils.setInvoiceEmail(userEmailId.trim());
                            Toast.makeText(context, "Email Saved Sucessfully", Toast.LENGTH_SHORT).show();
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //somethingWentWrongToast();
                }
                break;
            case FETCH_EMAIL_NOTIFICATION:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<FetchEmailData> fetchEmailData = (ArrayList<FetchEmailData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<FetchEmailData>>() {
                                }.getType());
                                if (fetchEmailData != null && fetchEmailData.size() > 0) {
                                    if (CommonUtils.isEmailValid(fetchEmailData.get(0).getINVOICEEMAIL())) {
                                        return;
                                    } else {
                                        if (Constants.mCheckBannerNo == 1) {
                                            showEmailSubmitDialog(context);
                                            Constants.mCheckBannerNo = 0;

                                        }

                                    }
                                } else {
                                    if (Constants.mCheckBannerNo == 1) {
                                        showEmailSubmitDialog(context);
                                        Constants.mCheckBannerNo = 0;

                                    }
                                }
                            }
                            // showEmailSubmitDialog(context);
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;

            case POPUP:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<PopUpData> popupData = (ArrayList<PopUpData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PopUpData>>() {
                                }.getType());
                                if (popupData != null && popupData.size() > 0) {
                                    for (int i = 0; i < popupData.size(); i++) {
                                        showpopupDialog(context, popupData);
                                    }
                                } else {
                                    // CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);

                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);
                    }


                }


                break;

            case FETCH_WU_SEND_MONEY:
                //   CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) && response.getInt(Constants.STATUS_CODE) == (Constants.REDIRECT_TO_TXN_BENEF_PAGE)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUBeneficiaryActivity.class)
                                    .putExtra(Constants.AREX_MEM_PK_ID, arexUserId)
                                    .putExtra(Constants.WU_CARD_NO, response.getString(Constants.WU_CARD_NO));
                            startActivity(wu);
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) && response.getInt(Constants.STATUS_CODE) == (Constants.REDIRECT_TO_ENROLL_PAGE)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUEnrollActivity.class).putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
                            startActivity(wu);
                        } else {
                            CommonUtils.hideLoading();
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.hideLoading();
                    }
                } else {
                    CommonUtils.hideLoading();
                }
                break;
            case TRAVEL_CARD_RELOAD:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            startActivity(new Intent(context, TravelCardReloadActivity.class));

                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            if (response.getString(Constants.STATUS_CODE).equalsIgnoreCase("256")) {
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(context, TravelCardReloadActivity.class));

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                break;
        }
    }

    private void somethingWentWrongToast() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    public void showEmailSubmitDialog(final Context mContext) {
        final Dialog dialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_email, null);
//--------------------------DKG---------------------------------------------------------------------
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
//--------------------------------------------------------------------------------------------------
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        final EditText etEmail = view.findViewById(R.id.email);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        Button btn_cancel = view.findViewById(R.id.dialog_btn_cancel);
        tvTitle.setText(getString(R.string.please_submit_email));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userEmailId = etEmail.getText().toString();
                if (CommonUtils.isEmailValid(userEmailId)) {
                    dialog.dismiss();
                    submitEmail(userEmailId);
                } else
                    etEmail.setError(mContext.getString(R.string.error_enter_valid_email));
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showpopupDialog(final Context mContext, final ArrayList<PopUpData> popupData) {
        final Dialog dialog = new Dialog(mContext, R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup2, null);

//--------------------------DKG---------------------------------------------------------------------
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
//--------------------------------------------------------------------------------------------------
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        ImageView ivimg = view.findViewById(R.id.iv_popup_img);
        TextView tvDescription = view.findViewById(R.id.dialog_description);
        ImageView close_btn = view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btn_clickhere = view.findViewById(R.id.dialog_btn_click_here);


        if (popupData.get(0).getMessage() != null || popupData.get(0).getMessage().equals("")) {
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
        if (popupData.get(0).getImageURL() != null || popupData.get(0).getImageURL().equals("")) {
            ivimg.setVisibility(View.VISIBLE);
        } else {
            ivimg.setVisibility(View.GONE);
        }
        tvTitle.setText(popupData.get(0).getTitle());
        tvDescription.setText(popupData.get(0).getMessage());
        if (popupData.get(0).getImageURL() != null || popupData.get(0).getImageURL().equals("")) {
            CommonUtils.setPromotionsImage(mContext, ivimg, popupData.get(0).getImageURL());
        } else {
            CommonUtils.setPromotionsImage(mContext, ivimg, popupData.get(0).getImageURL());
        }

        //if (popupData.get(0).getScreen_type_key() != null || popupData.get(0).getScreen_type_key().equals("")) {
        if (popupData.get(0).getScreen_type_key() != null) {
            btn_clickhere.setVisibility(View.VISIBLE);
        } else {
            btn_clickhere.setVisibility(View.GONE);
        }

        btn_clickhere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (popupData.get(0).getScreen_type_key().equals("SND_MNY")) {
                    intent = new Intent(context, BeneficiaryActivity.class);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                    startActivity(intent);
                } else if (popupData.get(0).getScreen_type_key().equals("WU")) {
                    arexUserId = CommonUtils.getMemPkId(Constants.AREX_MAPPING);
                    fetchWU();
                } else if (popupData.get(0).getScreen_type_key().equals("FRGN_CY")) {
                    startActivity(new Intent(context, ForeignCurrencyActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("RT_CLCTR")) {
                    startActivity(new Intent(context, CurrencyConverterActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("CC_PAY")) {
                    startActivity(new Intent(context, CreditCardPaymentActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("RT_ALERT")) {
                    startActivity(new Intent(context, RateAlertActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("TXN_HSTRY")) {
                    startActivity(new Intent(context, TransactionHistoryActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("TXN_PNDNG")) {
                    startActivity(new Intent(context, PendingTransActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("CNTCT")) {
                    startActivity(new Intent(context, ContactUsActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("BR_LOC")) {
                    startActivity(new Intent(context, BranchLocatorCityActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("FAQ")) {
                    startActivity(new Intent(context, FaqActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("PROMO")) {
                    startActivity(new Intent(context, PromotionsActivity.class));
                } else if (popupData.get(0).getScreen_type_key().equals("BNK_ACNTS")) {
                    startActivity(new Intent(context, BankAccountActivity.class));
                }else if(popupData.get(0).getScreen_type_key().equals("WEB")){
                    if(popupData.get(0).getImageURL()!=null)
                        openDialogBox(popupData.get(0).getPopUrl());
                    //startActivity(new Intent(context,).putExtra(Constants.URL,popupData.get(0).getPopUrl()));

                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void submitEmail(String email) {
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), email, "Y", LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
        AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
        AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
    }

    void fetchWU() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            //CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().loadWuNumber((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "", LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_WU_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
        }
    }

    void deleteQuickSend(String quickSendId) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().deleteQuickSend(quickSendId, userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.DELETE_QUICK_SEND_URL, DELETE_QUICK_SEND, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(DELETE_QUICK_SEND.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, DELETE_QUICK_SEND.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), DELETE_QUICK_SEND.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_menu:
                openMenuDrawer();
                break;
            case R.id.send_money_layout:
                intent = new Intent(context, BeneficiaryActivity.class);
                intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                startActivity(intent);
                break;
            case R.id.my_recipients_layout:
                intent = new Intent(context, AccountManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.my_transactions_layout:
                intent = new Intent(context, TransactionHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.pending_transactions_layout:
                intent = new Intent(context, PendingTransActivity.class);
                startActivity(intent);
                break;
            case R.id.my_referral_layout:
                intent = new Intent(context, MyReferralActivity.class);
                startActivity(intent);
                break;
            case R.id.transaction_tracker_layout:
                startActivity(new Intent(context, TransactionTrackerActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_BENEFICIARY_FOR_QUICK_SEND && resultCode == RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    addQuickSend((BeneficiaryData) data.getParcelableExtra(Constants.OBJECT));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        ((ShimmerFrameLayout) findViewById(R.id.shimmer_view_container)).stopShimmerAnimation();
        super.onPause();
    }

    void addQuickSend(BeneficiaryData beneficiaryData) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            for (int i = 0; i < recyclerAdapter.getItemCount() - 1; i++) {
                if (recyclerAdapter.getItemAt(i).getBeneficiaryId().equalsIgnoreCase(beneficiaryData.getBeneficiaryId())) {
                    Toast.makeText(context, "Beneficiary is already added in quick send.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addQuickSend((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), beneficiaryData, LogoutCalling.getDeviceID(context), sessionTime), Constants.ADD_QUICK_SEND_URL, ADD_QUICK_SEND, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(ADD_QUICK_SEND.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, ADD_QUICK_SEND.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), ADD_QUICK_SEND.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    public void showpopupDialog2(final Context mContext) {
       /* final Dialog dialog = new Dialog(mContext, R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup2, null);

//--------------------------DKG---------------------------------------------------------------------
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
//--------------------------------------------------------------------------------------------------
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        ImageView ivimg = view.findViewById(R.id.iv_popup_img);
        TextView tvDescription = view.findViewById(R.id.dialog_description);
        ImageView close_btn = view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();*/


        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("You cant delete this creadit card");
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(R.string.indemnity_form_text));
        // ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setVisibility(View.GONE);
        ((Button) myDialog.findViewById(R.id.dialog_btn_yes)).setText("DONE");

        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setVisibility(View.GONE);

        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
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

 /*   // check for the app if it exist in the phone it will lunch it otherwise, it will search for the app in google play app in the phone and to avoid any crash, if no google play app installed in the phone, it will search for the app in the google play store using the browser :
    public void onLunchAnotherApp() {

        String appPackageName = getApplicationContext().getPackageName();
        appPackageName="ae.sdg.uaepasssample";
        Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            onGoToAnotherInAppStore(intent, appPackageName);
        }

    }

    public void onGoToAnotherInAppStore(Intent intent, String appPackageName) {
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + appPackageName));
            startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {

            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            startActivity(intent);

        }

    }*/

    private void openDialogBox(String url) {
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_webview);
        dialog.setCanceledOnTouchOutside(false);
        webView=(WebView)dialog.findViewById(R.id.web_view);
        ImageView imageView=(ImageView)dialog.findViewById(R.id.close_btn);
        setWebView(webView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        try{
            webView.loadUrl(url);
            dialog.show();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setWebView(WebView webView) {
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


    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d("DKG ok", "URL " + url+ " ");



        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {

            }
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest reques, WebResourceError error) {
            Crashlytics.logException(new Exception("Web Error : " + error.toString()));


        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            try {
                CommonUtils.hideLoading();
            } catch (Exception e) {

            }

        }
    }


}