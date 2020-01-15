package app.alansari.modules.sendmoney;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import app.alansari.AppController;
import app.alansari.DashboardActivity;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.TxnDetailsData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 02 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class TransactionRateActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private Context context;
    //private String sourceType;
    private TxnDetailsData txnDetailsData;
    private Button btnSubmit;
    private AppCompatRatingBar ratingBar;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.transaction_rate_activity);
        context = this;
        if (getIntent().getExtras() != null && getIntent().getExtras().getString(Constants.REM_TXN_PK_ID) != null && getIntent().getExtras().getString(Constants.SERVICE_TYPE) != null) {
            init();
        } else {
            onBackPressed();
        }
    }

    private void init() {
        ratingBar = (AppCompatRatingBar) findViewById(app.alansari.R.id.rating_bar);
        btnSubmit = (Button) findViewById(app.alansari.R.id.submit_btn);
        findViewById(app.alansari.R.id.close_btn).setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating > 0) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.SOURCE, Constants.SOURCE_SPLASH_ACTIVITY);
        startActivity(intent);
        finish();
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
            case app.alansari.R.id.close_btn:
                onBackPressed();
                break;
            case app.alansari.R.id.submit_btn:
                submitTransactionRating();
                break;
        }
    }

    private void submitTransactionRating() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().submitTransactionRating((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), getIntent().getExtras().getString(Constants.REM_TXN_PK_ID), getIntent().getExtras().getString(Constants.SERVICE_TYPE), String.valueOf(ratingBar.getRating()), LogoutCalling.getDeviceID(context), sessionTime), Constants.SUBMIT_TRANSACTION_RATING_URL, CommonUtils.SERVICE_TYPE.SUBMIT_TRANSACTION_RATING, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.SUBMIT_TRANSACTION_RATING.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.SUBMIT_TRANSACTION_RATING.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.SUBMIT_TRANSACTION_RATING.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case SUBMIT_TRANSACTION_RATING:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (ratingBar.getRating() >= 4)
                                showPlayStoreRateDialog(this);
                            else
                                navigateDashboard();
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

    public void showPlayStoreRateDialog(final Context mContext) {
        if (SharedPreferenceManger.getRateReviewTime() == null || isSixMonthGap()) {
            final Dialog dialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBg);
            dialog.setTitle("Rate " + mContext.getString(R.string.app_name));
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_play_store_review, null);
            TextView tvTitle = view.findViewById(R.id.dialog_title);
            Button btnYes = view.findViewById(R.id.btn_yes);
            Button btnNo = view.findViewById(R.id.btn_no);
            tvTitle.setText("If you enjoy using Al Ansari Exchange Mobile App, please take a moment to rate it. Thanks for your support!");

            btnYes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "app.alansari")));
                    } catch (ActivityNotFoundException e) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                    }
                    SharedPreferenceManger.saveRateReviewTime(Calendar.getInstance());
                    dialog.dismiss();
                }
            });


            btnNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    navigateDashboard();
                    SharedPreferenceManger.saveRateReviewTime(Calendar.getInstance());
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();
        } else
            navigateDashboard();
    }

    public void navigateDashboard() {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.SOURCE, Constants.SOURCE_SPLASH_ACTIVITY);
        startActivity(intent);
        finish();
    }

    public boolean isSixMonthGap() {
        if (SharedPreferenceManger.getRateReviewTime() != null) {
            long msDiff = Calendar.getInstance().getTimeInMillis() - SharedPreferenceManger.getRateReviewTime().getTimeInMillis();
            long days = TimeUnit.MILLISECONDS.toDays(msDiff);
            return days >= 180;
        }
        return false;
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }
}
