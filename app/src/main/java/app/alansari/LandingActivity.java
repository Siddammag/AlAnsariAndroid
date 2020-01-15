package app.alansari;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.github.demono.AutoScrollViewPager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.adapters.AddsAdapter;
import app.alansari.customviews.ShimmerFrameLayout;
import app.alansari.fcm.FCMUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AddsData;
import app.alansari.models.FetchEmailData;
import app.alansari.models.PopUpData;
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
import app.alansari.newAdditions.Main2Activity;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADDS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.POPUP;
import static app.alansari.Utils.Constants.mCheckEmailNo;
import static app.alansari.Utils.Constants.mOtpTimer;
import static app.alansari.Utils.Constants.mSession_Logout_Message;
import static app.alansari.Utils.Constants.session_Time;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class LandingActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    String userEmailId = "";
    private Intent intent;
    private Context context;
    private AutoScrollViewPager viewPager;
    private AddsAdapter addsAdapter;
    private ProgressBar progressBar;
    private Dialog myDialog, appVersionDialog;
    private String statusCode, message, session_Timer;
    private String arexUserId;
    private String sessionTime;
    private CountDownTimer mCountDownTimer;
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (!CommonUtils.isLoggedIn() && CommonUtils.getUserId() == null && CommonUtils.getUserMobile() == null && CommonUtils.getPIN() == null) {
            FCMUtils.setFcmToken(context);
        }
        checkSource();
        init();
        fetchAdds();
        fetchPopups();

//--------------------------DKG---------------------------------------------------------------------
        appVersionDialogBox();
        /*if (CommonUtils.getUserId() == null && CommonUtils.getUserMobile() == null && CommonUtils.getPIN() == null) {
            new FCMUtils().sendRegistrationToServer(context, CommonUtils.getFCMToken());
       } */


        if (!CommonUtils.isLoggedIn() && CommonUtils.getUserId() == null && CommonUtils.getUserMobile() == null && CommonUtils.getPIN() == null) {
            new FCMUtils().sendRegistrationToServerNew(context, CommonUtils.getFCMToken());
        }

        /*int sum=2/0;
        Log.e("cnzc s cs",""+sum);*/

//--------------------------------------------------------------------------------------------------
        //fetchEmail();
//        viewPager.startAutoScroll();




    }

    private void OnWaitForOtp(int Seconds) {


        mCountDownTimer = new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                Log.e("sv snvbshbvjk", "" + "in: " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));

            }

            public void onFinish() {

            }
        };
        mCountDownTimer.start();

    }


    private void checkSource () {
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

    private void init () {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        findViewById(R.id.send_money_layout).setOnClickListener(this);
        findViewById(R.id.rate_calculator_layout).setOnClickListener(this);
        findViewById(R.id.transaction_tracker_layout).setOnClickListener(this);
        findViewById(R.id.get_in_touch_layout).setOnClickListener(this);
        findViewById(R.id.currency_convertor_layout).setOnClickListener(this);
        findViewById(R.id.branch_locator_layout).setOnClickListener(this);
        viewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewPager.setVisibility(View.GONE);
    }

    private void fetchAdds () {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().
                    fetchAdsNew((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                            LogoutCalling.getDeviceID(context)), Constants.ADDS_URL, ADDS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(ADDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, ADDS.toString());
        } else {
            CommonUtils.setResponseToast(context, VIEW_STATE_ERROR);
        }
    }

    private void fetchPopups () {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.POPUP.toString(), false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPopUps((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), CommonUtils.getDeviceID(context), "B", (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING)), Constants.POPUPS_URL, POPUP, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(POPUP.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, POPUP.toString());
        } else {
            CommonUtils.setResponseToast(context, VIEW_STATE_ERROR);
        }
    }

    private void appVersionDialogBox () {
        if (appVersionDialog == null) {
            appVersionDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            appVersionDialog.setCanceledOnTouchOutside(false);
            appVersionDialog.setCancelable(false);
            appVersionDialog.setContentView(R.layout.app_version_dialog);
            appVersionDialog.findViewById(app.alansari.R.id.update_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appVersionDialog.dismiss();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                    }
                }
            });
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        ((ShimmerFrameLayout) findViewById(R.id.shimmer_view_container)).startShimmerAnimation();
        if (addsAdapter == null || addsAdapter.getItemCount() == 0 || viewPager.getChildCount() == 0) {
            fetchAdds();
        }
        mCheckEmailNo = 1;
        Constants.mCheckBannerNo = 1;
    }

    @Override
    public void onResponse (int status, JSONObject response, CommonUtils.SERVICE_TYPE sType){
        switch (sType) {
            case ADDS:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        statusCode = response.getString(Constants.STATUS_CODE);
                        message = response.getString(Constants.MESSAGE);
                        if (session_Timer != null && Long.parseLong(session_Timer) > 0)
                            Constants.SESSION_LOG_OUT = Long.getLong(session_Timer);
                        if (statusCode.equalsIgnoreCase("799")) {
                            ((TextView) appVersionDialog.findViewById(app.alansari.R.id.dialog_title)).setText(message);
                            appVersionDialog.show();
                            return;
                        }

                        try {
                            if (session_Time > 0) {
                                session_Time = Integer.parseInt(response.getString("SESSION_TIMER"));
                                session_Time = session_Time * 1000;
                            } else {
                                session_Time = Integer.parseInt(response.getString("SESSION_TIMER"));
                            }
                            session_Timer = response.getString("SESSION_TIMER");
                            mOtpTimer = response.getInt("OTP_TIMER");
                            mSession_Logout_Message = response.getString("SESSION_TIMER_MSG");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<AddsData> addsData = (ArrayList<AddsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AddsData>>() {
                                }.getType());
                                if (addsData != null && addsData.size() > 0) {
                                    addsAdapter = new AddsAdapter(context, addsData);
                                    viewPager.setAdapter(addsAdapter);
                                    progressBar.setVisibility(View.GONE);
                                    viewPager.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                            //CommonUtils.setResponseToast(context, VIEW_STATE_EMPTY);
                        } else {
                            CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);
                            progressBar.setVisibility(View.GONE);
                            ArrayList<AddsData> addsData = new ArrayList<>();
                            addsData.add(new AddsData("", ""));
                            addsAdapter = new AddsAdapter(context, addsData);
                            viewPager.setAdapter(addsAdapter);
                            viewPager.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);
                        //showDialogFull(Constants.LOCATION_RESTRICTED, getString(R.string.not_allowed_to_access_new));
                        progressBar.setVisibility(View.GONE);
                        ArrayList<AddsData> addsData = new ArrayList<>();
                        addsData.add(new AddsData("", ""));
                        addsAdapter = new AddsAdapter(context, addsData);
                        viewPager.setAdapter(addsAdapter);
                        viewPager.setVisibility(View.VISIBLE);
                    }
                } else {
                    CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);
                    //showDialogFull(Constants.LOCATION_RESTRICTED, getString(R.string.not_allowed_to_access_new));
                    progressBar.setVisibility(View.GONE);
                    ArrayList<AddsData> addsData = new ArrayList<>();
                    addsData.add(new AddsData("", ""));
                    addsAdapter = new AddsAdapter(context, addsData);
                    viewPager.setAdapter(addsAdapter);
                    viewPager.setVisibility(View.VISIBLE);
                }

                break;
            case POPUP:
                CommonUtils.hideLoading();
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

                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // CommonUtils.setResponseToast(context, VIEW_STATE_WRONG);
                    }


                }
                break;
            case FETCH_ALL_COUNTRIES:
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
                    somethingWentWrongToast();
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
                                    } else
                                        showEmailSubmitDialog(context);
                                } else
                                    showEmailSubmitDialog(context);
                            }
                            showEmailSubmitDialog(context);
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;

            case FETCH_WU_SEND_MONEY:
                CommonUtils.hideLoading();
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
        }
    }

    private void showDialogFull (String responseType, String text){
        if (context == null && isFinishing())
            return;
        if (myDialog != null) {
            while (myDialog.isShowing()) {
                myDialog.dismiss();
            }
        }
        myDialog = new Dialog(context, R.style.InternetErrorDialog);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.internet_error_dialog);

        ((TextView) myDialog.findViewById(R.id.sub_title)).setText(text);
        myDialog.findViewById(R.id.close_btn).setOnClickListener(this);
        myDialog.findViewById(R.id.assistance_call_icon).setOnClickListener(this);
        myDialog.findViewById(R.id.assistance_call).setOnClickListener(this);
        myDialog.findViewById(R.id.retry_btn).setOnClickListener(this);
        myDialog.findViewById(R.id.tv_call).setOnClickListener(this);
        if (!((Activity) context).isFinishing()) {
            myDialog.show();
        }
        myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });

    }

    public void showpopupDialog ( final Context mContext, final ArrayList<PopUpData> popupData){
        final Dialog dialog = new Dialog(mContext, R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup2, null);
//--------------------------DKG---------------------------------------------------------------------
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
//--------------------------------------------------------------------------------------------------
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        ImageView ivimg = view.findViewById(R.id.iv_popup_img);
        TextView tvDescription = view.findViewById(R.id.dialog_description);
        Button btn_clickhere = view.findViewById(R.id.dialog_btn_click_here);
        ImageView close_btn = view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (popupData.get(0).getScreen_type_key() != null) {
            btn_clickhere.setVisibility(View.VISIBLE);
        } else {
            btn_clickhere.setVisibility(View.GONE);
        }
        if (popupData.get(0).getMessage() != null || popupData.get(0).getMessage().equals("")) {
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
        if (popupData.get(0).getImageURL() != null) {
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
        btn_clickhere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (popupData.get(0).getScreen_type_key().equals("SND_MNY")) {
                    if (CommonUtils.isLoggedIn()) {
                        intent = new Intent(context, BeneficiaryActivity.class);
                        intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                        startActivity(intent);
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("WU")) {
                    if (CommonUtils.isLoggedIn()) {
                        arexUserId = CommonUtils.getMemPkId(Constants.AREX_MAPPING);
                        fetchWU();
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("FRGN_CY")) {
                    startActivity(new Intent(context, ForeignCurrencyActivity.class));  //without login
                } else if (popupData.get(0).getScreen_type_key().equals("RT_CLCTR")) {
                    startActivity(new Intent(context, CurrencyConverterActivity.class));  //without login
                } else if (popupData.get(0).getScreen_type_key().equals("CC_PAY")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, CreditCardPaymentActivity.class));
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("RT_ALERT")) {
                    if (CommonUtils.isLoggedIn()) {
                        mFirebaseAnalytics.logEvent("Prelogin_RateCalculator", null);
                        Log.i("Prelogin_RateCalculator", "Success in Landing Screen");
                        startActivity(new Intent(context, RateAlertActivity.class));
                    } else {
                        // login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("TXN_HSTRY")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, TransactionHistoryActivity.class));
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("TXN_PNDNG")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, PendingTransActivity.class));
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("CNTCT")) {
                    startActivity(new Intent(context, ContactUsActivity.class));  //without login
                } else if (popupData.get(0).getScreen_type_key().equals("BR_LOC")) {
                    startActivity(new Intent(context, BranchLocatorCityActivity.class)); //without login
                } else if (popupData.get(0).getScreen_type_key().equals("FAQ")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, FaqActivity.class));
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("PROMO")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, PromotionsActivity.class));
                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("BNK_ACNTS")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, BankAccountActivity.class));
                    } else {
                        //login here
                    }
//---------------------------------New Implement----------------------------------------------------
                } else if (popupData.get(0).getScreen_type_key().equals("REG")) {
                    if (CommonUtils.isLoggedIn()) {
                        startActivity(new Intent(context, RegisterActivity.class));
                       /* SharedPreferenceManger.clearPreferences();
                        AppController.setIsPinVerified(false);
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.putExtra("goto", "registration");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        ((AppCompatActivity) context).finish();*/

                    } else {
                        //login here
                    }
                } else if (popupData.get(0).getScreen_type_key().equals("LOGIN_PG")) {
                    if (CommonUtils.isLoggedIn()) {
                        mFirebaseAnalytics.logEvent("Login", null);
                        Log.i("Login","Success in Login");
                        startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        //login here
                    }
//--------------------------------------------------------------------------------------------------

                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void somethingWentWrongToast () {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    public void showEmailSubmitDialog ( final Context mContext){
        final Dialog dialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBgPopUP);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_email, null);
        TextView tvTitle = view.findViewById(R.id.dialog_title);
        final EditText etEmail = view.findViewById(R.id.email);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        tvTitle.setText(getString(R.string.please_submit_email));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userEmailId = etEmail.getText().toString();
                if (CommonUtils.isEmailValid(userEmailId)) {
                    submitEmail(userEmailId);
                    dialog.dismiss();
                } else
                    etEmail.setError(mContext.getString(R.string.error_enter_valid_email));
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    void fetchWU () {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().loadWuNumber((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "", LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_WU_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
        }
    }

    private void submitEmail (String email){
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), email, "Y", LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
        AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
        AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
    }

    private void fetchEmail () {
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

    @Override
    public void onPause () {
        ((ShimmerFrameLayout) findViewById(R.id.shimmer_view_container)).stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public void onClick (View view){
        switch (view.getId()) {
            case R.id.nav_menu:
                break;
            case R.id.send_money_layout:
                Intent intent = null;
                SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                if (CommonUtils.isLoggedIn() && CommonUtils.getUserId() != null && CommonUtils.getUserMobile() != null && CommonUtils.getPIN() != null) {
                    mFirebaseAnalytics.logEvent("Login", null);
                    Log.i("Login", "Success in Login");
                    intent = new Intent(context, LoginActivity.class);
                } else {
                    intent = new Intent(context, RegisterActivity.class);
                }
                startActivity(intent);
                //Crashlytics.getInstance().crash();
                break;
            case R.id.rate_calculator_layout:
                mFirebaseAnalytics.logEvent("Prelogin_RateCalculator", null);
                Log.i("Prelogin_RateCalculator", "Success in Landing Screen");
                intent = new Intent(context, CurrencyConverterActivity.class);
                intent.putExtra(Constants.HIDE_BURGER_MENU, true);
                startActivity(intent);
                break;
            //88023000006643
            //88023000006647
            case R.id.transaction_tracker_layout:
                mFirebaseAnalytics.logEvent("Prelogin_TransactionTracker", null);
                Log.i("Prelogin_Transa", "Success in Landing Screen");
                startActivity(new Intent(context,TransactionTrackerActivity.class).putExtra(Constants.HIDE_BURGER_MENU,true));
                break;
            case R.id.get_in_touch_layout:
                intent = new Intent(context, ContactUsActivity.class);
                intent.putExtra(Constants.HIDE_BURGER_MENU, true);
                startActivity(intent);
                break;
            case R.id.branch_locator_layout:
                intent = new Intent(context, BranchLocatorCityActivity.class);
                intent.putExtra(Constants.HIDE_BURGER_MENU, true);
                startActivity(intent);
                break;
            case R.id.currency_convertor_layout:
                intent = new Intent(context, ForeignCurrencyActivity.class);
                intent.putExtra(Constants.HIDE_BURGER_MENU, true);
                startActivity(intent);
                break;
            case R.id.retry_btn:
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchAdds();
                    if (myDialog != null)
                        myDialog.dismiss();
                }
                break;
            case R.id.close_btn:
                if (myDialog != null)
                    myDialog.dismiss();
                finish();
                break;
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        stopLogoutTimer();

    }

    @Override
    public void doLogout () {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());

        }
    }









   /* public AppController getApp() {
        return (AppController) this.getApplication();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
      //  getApp().touch();
        Log.d("TAG", "User interaction to " + this.toString());
    }
*/

   /* private void fetchUpPopup() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPopUps((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), CommonUtils.getDeviceID(context), "B", (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING)), Constants.FETCH_UP_POPUP, Pop_Ups, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(Pop_Ups.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, Pop_Ups.toString());
        } else {
            CommonUtils.setResponseToast(context, VIEW_STATE_ERROR);
        }

    }*/


}