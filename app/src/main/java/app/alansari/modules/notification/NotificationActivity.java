package app.alansari.modules.notification;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.FetchEmailData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.EMAIL_NOTIFICATION;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_EMAIL_NOTIFICATION;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 06 March, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class NotificationActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, CompoundButton.OnCheckedChangeListener , LogOutTimerUtil.LogOutListener  {

    private Context context;
    private TextView tvEmailNotification, tvSmsNotification, tvAaeAlerts;
    private SwitchCompat btnEmailNotification, btnSmsNotification, btnAaeAlerts;
    private RelativeLayout emailLayout;
    private Button btnSave;
    private EditText etEmail;
    private boolean clickedSave;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        tvEmailNotification = (TextView) findViewById(app.alansari.R.id.email_notification);
        tvSmsNotification = (TextView) findViewById(app.alansari.R.id.sms_notification);
        tvAaeAlerts = (TextView) findViewById(app.alansari.R.id.aae_alert);
        btnEmailNotification = (SwitchCompat) findViewById(app.alansari.R.id.email_notification_switch);
        btnSmsNotification = (SwitchCompat) findViewById(app.alansari.R.id.sms_notification_switch);
        btnAaeAlerts = (SwitchCompat) findViewById(app.alansari.R.id.aae_alert_switch);
        emailLayout = (RelativeLayout) findViewById(app.alansari.R.id.email_layout);
        btnSave = (Button) findViewById(app.alansari.R.id.save);
        etEmail = (EditText) findViewById(app.alansari.R.id.email);

        tvEmailNotification.setOnClickListener(this);
        tvSmsNotification.setOnClickListener(this);
        tvAaeAlerts.setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.save).setOnClickListener(this);

        btnEmailNotification.setChecked(CommonUtils.getEmailNotificationSettings());
        btnSmsNotification.setChecked(CommonUtils.getSmsNotificationSettings());
        btnAaeAlerts.setChecked(CommonUtils.getAaeAlertsSettings());
        if (CommonUtils.getEmailNotificationSettings()) {
            btnSave.setVisibility(View.VISIBLE);
            etEmail.setVisibility(View.VISIBLE);
        } else {
            btnSave.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
        }
        btnEmailNotification.setOnCheckedChangeListener(this);
        btnSmsNotification.setOnCheckedChangeListener(this);
        btnAaeAlerts.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.notification_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Notifications");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();
        fetchEmail();
    }

    private void fetchEmail() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkId((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),LogoutCalling.getDeviceID(context)), Constants.FETCH_EMAIL_NOTIFICATION_URL, FETCH_EMAIL_NOTIFICATION, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_EMAIL_NOTIFICATION.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_EMAIL_NOTIFICATION.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_EMAIL_NOTIFICATION.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == btnEmailNotification) {
            CommonUtils.setEmailNotificationSettings(isChecked);
            if (isChecked) {
                btnSave.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
            } else {
                btnSave.setVisibility(View.GONE);
                etEmail.setVisibility(View.GONE);
            }
        } else if (buttonView == btnSmsNotification)
            CommonUtils.setSmsNotificationSettings(isChecked);
        else if (buttonView == btnAaeAlerts)
            CommonUtils.setAaeAlertsSettings(isChecked);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.email_notification:
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    if (!btnEmailNotification.isChecked()) {
                        if (etEmail.getText().toString() != null && !TextUtils.isEmpty(etEmail.getText().toString().trim()) && CommonUtils.isEmailValid(etEmail.getText().toString())) {
                            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), etEmail.getText().toString(), "Y", LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
                            AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
                            AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
                            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
                        } else {
                            Toast.makeText(context, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), etEmail.getText().toString(), "N",LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
                        AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
                        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
                btnEmailNotification.setChecked(!btnEmailNotification.isChecked());
                break;
            case app.alansari.R.id.sms_notification:
                btnSmsNotification.setChecked(!btnSmsNotification.isChecked());
                break;
            case app.alansari.R.id.aae_alert:
                btnAaeAlerts.setChecked(!btnAaeAlerts.isChecked());
                break;
            case app.alansari.R.id.save:
                CommonUtils.hideKeyboard(context);
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    if (etEmail.getText().toString() != null && !TextUtils.isEmpty(etEmail.getText().toString().trim()) && CommonUtils.isEmailValid(etEmail.getText().toString())) {
                        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().emailNotification((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), etEmail.getText().toString(), "Y",LogoutCalling.getDeviceID(context)), Constants.EMAIL_NOTIFICATION_URL, EMAIL_NOTIFICATION, Request.Method.PUT, this);
                        AppController.getInstance().getRequestQueue().cancelAll(EMAIL_NOTIFICATION.toString());
                        AppController.getInstance().addToRequestQueue(jsonObjReq, EMAIL_NOTIFICATION.toString());
                        clickedSave = true;
                        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), EMAIL_NOTIFICATION.toString(), false);
                    } else {
                        Toast.makeText(context, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void onError() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case EMAIL_NOTIFICATION:
                CommonUtils.hideLoading();
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (clickedSave) {
                                CommonUtils.setInvoiceEmail(etEmail.getText().toString().trim());
                                Toast.makeText(context, "Email Saved Sucessfully", Toast.LENGTH_SHORT).show();
                            }
                            clickedSave = false;

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
                                    etEmail.setText(fetchEmailData.get(0).getINVOICEEMAIL());
                                    if (fetchEmailData.get(0).getEMAILALERTIND() != null && fetchEmailData.get(0).getEMAILALERTIND().equalsIgnoreCase("Y")) {
                                        btnEmailNotification.setChecked(true);
                                    } else {
                                        btnEmailNotification.setChecked(false);
                                    }
                                    return;
                                }
                            }
                            somethingWentWrongToast();
                        } else
                            somethingWentWrongToast();
                    } else
                        somethingWentWrongToast();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    somethingWentWrongToast();
                }
                break;
        }
    }


    private void somethingWentWrongToast() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
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
        if(CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout=CommonUtils.isAppOnForeground(context);
        if(mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }
}