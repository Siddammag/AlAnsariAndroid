package app.alansari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.transactiontracker.TrackerResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.TRANSACTION_TRACKER;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.TRANSACTION_TRACKER_POST;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class TransactionTrackerActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {
    private Context context;
    private EditText tvTransactionMode;
    private Button trackTransfer;
    private boolean checkLoginStaus;
         //88023000006644
        //88023000006654
       //192.168.1.1
      //88023000006662

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_tracker);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Transaction");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Tracker");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.dimens_5sp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_bank_account_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_bank_account_icon);
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color024B54));
        checkLoginStaus = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (checkLoginStaus) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        }
        init();
    }

    public void init() {
        trackTransfer = (Button) findViewById(R.id.track_transfer);
        tvTransactionMode = (EditText) findViewById(R.id.transaction_mode_text);
        trackTransfer.setOnClickListener(this);




        tvTransactionMode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.track_transfer:
                checkStausApis();
                break;
        }
    }

    private void checkStausApis() {
        if (tvTransactionMode.getText() != null && tvTransactionMode.getText().toString().length() > 0) {
            if (checkLoginStaus) {
                submitDataPreLogin();
            } else {
                submitDataPostLogin();
            }
            ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.GONE);
        } else {
            Toast.makeText(context, "Please Enter Transaction Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitDataPreLogin() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().transactionTrackerPre(tvTransactionMode.getText().toString().trim(), LogoutCalling.getDeviceID(context)), Constants.TRANSACTION_TRACKER_PRE_URL, TRANSACTION_TRACKER, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(TRANSACTION_TRACKER.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, TRANSACTION_TRACKER.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), TRANSACTION_TRACKER.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitDataPostLogin() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().transactionTrackerPost(
                    (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                    (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                    (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                    tvTransactionMode.getText().toString(),(String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                    LogoutCalling.getDeviceID(context)),
                    Constants.TRANSACTION_TRACKER_POST_URL, TRANSACTION_TRACKER_POST, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(TRANSACTION_TRACKER_POST.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, TRANSACTION_TRACKER_POST.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), TRANSACTION_TRACKER_POST.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case TRANSACTION_TRACKER:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                Toast.makeText(context, response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.trnas_status)).setText(response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.MESSAGE));
                                return;
                            }
                            //onError();
                        } else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE))
                            ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.trnas_status)).setText(response.getString(Constants.MESSAGE));
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    //else
                       //onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.GONE);
                    onError();
                }
                break;
            case TRANSACTION_TRACKER_POST:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                               // Toast.makeText(context, response.getJSONArray(Constants.RESULT).getJSONObject(0).getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                ArrayList<TrackerResult> trackerResults = (ArrayList<TrackerResult>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TrackerResult>>() {
                                }.getType());
                                if (trackerResults != null && trackerResults.size() > 0) {
                                    startActivity(new Intent(context, TransactionTrackerDetailActivity.class).putParcelableArrayListExtra(Constants.LIST, trackerResults).putExtra(Constants.TRANSACTION_TYPE_VALUE,tvTransactionMode.getText().toString().trim()));
                                    finish();
                                    return;
                                }
                            }
                            onError();
                        } else if(response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE))
                            ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.trnas_status)).setText(response.getString(Constants.MESSAGE));
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else
                        onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ((TextView) findViewById(R.id.trnas_status)).setVisibility(View.GONE);
                    onError();
                }
                break;
        }

    }

    private void onError() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }
}
