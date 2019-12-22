package app.alansari.modules.accountmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

public class WUEnrollActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    private Context context;
    private EditText etWuNumber;
    private MultiStateView multiStateView;
    private TextView tvEmpty, tvError, tvErrorMessage;
    private boolean newUser = true;
    private String arexUserId = "";
    private String sessionTime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {

        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));

        etWuNumber = (EditText) findViewById(R.id.et_wu_number);
        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.send_btn).setOnClickListener(this);
        ((RadioGroup) findViewById(app.alansari.R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                etWuNumber.setText("");
                if (checkedId == R.id.rb_existing_user) {
                    newUser = false;
                    etWuNumber.setEnabled(true);
                    tvErrorMessage.setVisibility(View.GONE);
                    if (etWuNumber.getText().toString() != null && etWuNumber.getText().toString().length() == 9) {
                        findViewById(app.alansari.R.id.send_btn).setEnabled(true);
                    } else {
                        findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                    }
                } else if (checkedId == R.id.rb_new_user) {
                    newUser = true;
                    tvErrorMessage.setVisibility(View.GONE);
                    findViewById(app.alansari.R.id.send_btn).setEnabled(true);
                }
            }
        });
        etWuNumber.setEnabled(false);
        etWuNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etWuNumber.getText().toString() != null && etWuNumber.getText().toString().length() == 9) {
                    findViewById(app.alansari.R.id.send_btn).setEnabled(true);
                    newUser = false;
                } else {
                    findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                    newUser = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wu_enroll_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Western Union");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Send Money");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        if (getIntent() == null) {
            finish();
        } else {
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
        }

        init();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.send_btn:
                if (newUser) {
                    enrollWUCard();
                } else {
                    validateWuCard();
                }

                break;
        }
    }

    void enrollWUCard() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().enrollWUCard((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, LogoutCalling.getDeviceID(context),sessionTime), Constants.ENROLL_WU_CARD_URL, CommonUtils.SERVICE_TYPE.ENROLL_WU_CARD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ENROLL_WU_CARD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ENROLL_WU_CARD.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    void validateWuCard() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().validateWuCard((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, etWuNumber.getText().toString(),LogoutCalling.getDeviceID(context),sessionTime), Constants.VALIDATE_WU_CARD_URL, CommonUtils.SERVICE_TYPE.VALIDATE_WU_CARD, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.VALIDATE_WU_CARD.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VALIDATE_WU_CARD.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case ENROLL_WU_CARD:
                if (status == 1) {
                    try {
                        if (response.getInt(Constants.STATUS_CODE) == (Constants.WU_CARD_ENROLL_STATUS_SUCCESS)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUBeneficiaryActivity.class)
                                    .putExtra(Constants.AREX_MEM_PK_ID, arexUserId)
                                    .putExtra(Constants.WU_CARD_NO, response.getString(Constants.WU_CARD_NO));
                            startActivity(wu);
                            finish();
                        } else if (response.getInt(Constants.STATUS_CODE) == (Constants.WU_CARD_ENROLL_STATUS_FAIL)) {
                            CommonUtils.hideLoading();
                            findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                            tvErrorMessage.setText(response.getString(Constants.MESSAGE));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case VALIDATE_WU_CARD:
                if (status == 1) {
                    try {
                        if (response.getInt(Constants.STATUS_CODE) == (Constants.WU_CARD_ENROLL_STATUS_SUCCESS)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUBeneficiaryActivity.class)
                                    .putExtra(Constants.AREX_MEM_PK_ID, arexUserId)
                                    .putExtra(Constants.WU_CARD_NO, response.getString(Constants.WU_CARD_NO));
                            startActivity(wu);
                            finish();
                        } else if (response.getInt(Constants.STATUS_CODE) == (Constants.WU_CARD_ENROLL_STATUS_FAIL)) {
                            CommonUtils.hideLoading();
                            findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                            tvErrorMessage.setText(response.getString(Constants.MESSAGE));
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    findViewById(app.alansari.R.id.send_btn).setEnabled(false);
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
        }
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
