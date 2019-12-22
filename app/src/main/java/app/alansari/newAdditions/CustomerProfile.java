package app.alansari.newAdditions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
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
import app.alansari.R;
import app.alansari.RegisterActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CUSTOMER_PROFILE;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class CustomerProfile extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    private int count = 0;
    private int pinType, errorCount;
    private Context context;
    private String mobileNum, userStatus;
    private TextView tvMobile;
    private Button btnChange, btnSubmit;
    private EditText etEidName, etEmail, etCompanyName;
    private TextInputLayout eidNameLayout, emailLayout, companyLayout;
    private String userPkId, sessionTime;

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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("CUSTOMER \nPROFILE");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getExtras() != null) {
            mobileNum = getIntent().getExtras().getString(Constants.MOBILE_NUM);
            userStatus = getIntent().getExtras().getString(Constants.USER_STATUS);
            pinType = getIntent().getExtras().getInt(Constants.TYPE, Constants.CHANGE_PIN);
        }
        userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        init();
    }


    private void init() {
        btnChange = (Button) findViewById(R.id.change_btn);
        btnSubmit = (Button) findViewById(R.id.submit_btn);
        tvMobile = (TextView) findViewById(R.id.mobile);

        eidNameLayout = (TextInputLayout) findViewById(R.id.eid_name_layout);
        emailLayout = (TextInputLayout) findViewById(R.id.eid_email_layout);
        companyLayout = (TextInputLayout) findViewById(R.id.eid_company_layout);
        etEidName = (EditText) findViewById(R.id.eid_name);
        etEmail = (EditText) findViewById(R.id.eid_email);
        etCompanyName = (EditText) findViewById(R.id.eid_company_name);

        btnChange.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvMobile.setText(mobileNum.substring(3));
        findViewById(R.id.result_ok_btn).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }


//------------------In Activity Part----------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                Intent i=new Intent(context,RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(etEidName.getText().toString().trim())) {
                    eidNameLayout.setErrorEnabled(true);
                    eidNameLayout.setError("Invalid Name");
                } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Invalid Email");
                    eidNameLayout.setErrorEnabled(false);
                } else if (!CommonUtils.isEmailValid(etEmail.getText().toString().trim())) {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("Invalid Email");
                } else if (TextUtils.isEmpty(etCompanyName.getText().toString().trim())) {
                    companyLayout.setErrorEnabled(true);
                    companyLayout.setError("Invalid Company Name");
                    eidNameLayout.setErrorEnabled(false);
                    emailLayout.setErrorEnabled(false);
                } else {
                    eidNameLayout.setErrorEnabled(false);
                    emailLayout.setErrorEnabled(false);
                    companyLayout.setErrorEnabled(false);
                    submitData();
                }
                break;
            case R.id.result_ok_btn:
                clearAllBack();
                break;


        }
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userName = etEidName.getText().toString().trim();
            String emailId = etEmail.getText().toString().trim();
            String workComapantName = etCompanyName.getText().toString();
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().customerProfile(emailId, workComapantName, userName, userPkId, LogoutCalling.getDeviceID(context)), Constants.CUSTOMER_PROFILE_URL, CUSTOMER_PROFILE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CUSTOMER_PROFILE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CUSTOMER_PROFILE.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CUSTOMER_PROFILE.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }

       /* Intent intent = new Intent();
        intent.putExtra(Constants.MESSAGE, getIntent().getStringExtra("message"));
        intent.putExtra(Constants.RESULT, "1");
        setResult(Activity.RESULT_OK, intent);
        finish();*/
    }
    public void clearAllBack(){
        super.onBackPressed();
        finish();
    }


 /*   @Override
    public void onBackPressed() {
        clearAllBack();
        super.onBackPressed();

    }*/

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case CUSTOMER_PROFILE:
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            //CommonUtils.setInvoiceEmail(userEmailId.trim());
                            //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra(Constants.MESSAGE, getIntent().getStringExtra("message"));
                            intent.putExtra(Constants.RESULT, "1");
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else
                            somethingWentWrongToast(response.getString(Constants.MESSAGE));
                    } else
                        somethingWentWrongToast(response.getString(Constants.MESSAGE));
                } catch (Exception ex) {
                    somethingWentWrongToast(getString(R.string.error_something_wrong));
                    ex.printStackTrace();

                }
                break;

        }

    }

    private void somethingWentWrongToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }
}




