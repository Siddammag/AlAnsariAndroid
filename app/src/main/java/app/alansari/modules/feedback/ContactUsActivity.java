package app.alansari.modules.feedback;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.FetchEmailData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CONTACT_US;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_EMAIL_NOTIFICATION;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 17 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class ContactUsActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    private Context context;
    private Button btnSubmit;
    private String mobileNumber;
    private boolean isEditingEnabled;
    private EditText etName, etMobile, etEmail, etSubject, etMessage;
    private TextInputLayout nameLayout, emailLayout, mobileLayout, subjectLayout, messageLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        nameLayout = (TextInputLayout) findViewById(app.alansari.R.id.name_layout);
        mobileLayout = (TextInputLayout) findViewById(app.alansari.R.id.mobile_layout);
        emailLayout = (TextInputLayout) findViewById(app.alansari.R.id.email_layout);
        subjectLayout = (TextInputLayout) findViewById(app.alansari.R.id.subject_layout);
        messageLayout = (TextInputLayout) findViewById(app.alansari.R.id.message_layout);
        etName = (EditText) findViewById(app.alansari.R.id.name);
        etMobile = (EditText) findViewById(app.alansari.R.id.mobile);
        etEmail = (EditText) findViewById(app.alansari.R.id.email);
        etSubject = (EditText) findViewById(app.alansari.R.id.subject);
        etMessage = (EditText) findViewById(app.alansari.R.id.message);

        btnSubmit = (Button) findViewById(app.alansari.R.id.submit_btn);
        btnSubmit.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditingEnabled) {
                    if (s == etName.getEditableText()) {
                        validateValues(etName, nameLayout, getString(app.alansari.R.string.invalid_name));
                    } else if (s == etMobile.getEditableText()) {
                        validateValues(etMobile, mobileLayout, getString(app.alansari.R.string.invalid_phone_number));
                    } else if (s == etEmail.getEditableText()) {
                        validateValues(etEmail, emailLayout, getString(app.alansari.R.string.invalid_email_id));
                    } else if (s == etSubject.getEditableText()) {
                        validateValues(etSubject, subjectLayout, getString(app.alansari.R.string.invalid_subject));
                    } else if (s == etMessage.getEditableText()) {
                        validateValues(etMessage, messageLayout, getString(app.alansari.R.string.invalid_message));
                    }
                }
            }
        };

        etName.addTextChangedListener(textWatcher);
        etMobile.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etSubject.addTextChangedListener(textWatcher);
        etMessage.addTextChangedListener(textWatcher);

//        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new
//
//                KeyboardVisibilityEventListener() {
//                    @Override
//                    public void onVisibilityChanged(boolean isOpen) {
//                        if (isOpen)
//                            btnSubmit.setVisibility(View.GONE);
//                        else {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    btnSubmit.setVisibility(View.VISIBLE);
//                                }
//                            }, 100);
//                        }
//                    }
//                }
//        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.contact_us_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Contact Us");
        findViewById(app.alansari.R.id.toolbar_title_2).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.login_register_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_message_contact_us);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
        init();
        mobileNumber = (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        boolean hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        }

        setInitialData();
    }

    private void setInitialData() {
        etMobile.setText(mobileNumber);
        isEditingEnabled = true;
        fetchEmail();
    }

    private void fetchEmail() {
        if ((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING) != null)
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkId((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)), Constants.FETCH_EMAIL_NOTIFICATION_URL, FETCH_EMAIL_NOTIFICATION, Request.Method.PUT, this);
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

    private boolean validateValues(EditText editText, TextInputLayout inputLayout, String msg) {
        if (editText.getText() != null && editText.getText().toString().trim().length() > 0) {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        } else {
            inputLayout.setError(msg);
            inputLayout.setErrorEnabled(true);
            return false;
        }
    }

    private boolean validateMobile() {
        if (etMobile.getText() != null && etMobile.getText().toString().trim().length() > 8) {
            mobileLayout.setError(null);
            mobileLayout.setErrorEnabled(false);
            return true;
        } else {
            mobileLayout.setError(getString(app.alansari.R.string.invalid_phone_number));
            mobileLayout.setErrorEnabled(true);
            return false;
        }
    }

    private boolean validateEmail() {
        if (etEmail.getText() != null && etEmail.getText().toString().trim().length() > 0 && Validation.isValidEmail(etEmail.getText().toString().trim())) {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);
            return true;
        } else {
            emailLayout.setError(getString(app.alansari.R.string.invalid_email_id));
            emailLayout.setErrorEnabled(true);
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
            case app.alansari.R.id.submit_btn:
                if (validateValues(etName, nameLayout, getString(app.alansari.R.string.invalid_name))) {
                    if (validateMobile()) {
                        if (validateEmail()) {
                            if (validateValues(etSubject, subjectLayout, getString(app.alansari.R.string.invalid_subject))) {
                                if (validateValues(etMessage, messageLayout, getString(app.alansari.R.string.invalid_message))) {
                                    submitData();
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().contactUs((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), etName.getText().toString().trim(), etMobile.getText().toString().trim(), etEmail.getText().toString().trim(), etSubject.getText().toString().trim(), etMessage.getText().toString().trim(),LogoutCalling.getDeviceID(context)), Constants.CONTACT_US_URL, CONTACT_US, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CONTACT_US.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CONTACT_US.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CONTACT_US.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        isEditingEnabled = false;
        etName.setText(null);
        etMobile.setText(mobileNumber);
        etEmail.setText(null);
        etSubject.setText(null);
        etMessage.setText(null);
        CommonUtils.hideKeyboard(context);
        isEditingEnabled = true;
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case CONTACT_US:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            final Dialog successDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
                            successDialog.setCanceledOnTouchOutside(false);
                            successDialog.setContentView(app.alansari.R.layout.generic_single_btn_dialog);
                            ((TextView) successDialog.findViewById(app.alansari.R.id.dialog_title)).setText(getString(app.alansari.R.string.contact_us_dialog_title));
                            ((TextView) successDialog.findViewById(app.alansari.R.id.dialog_text)).setText(getString(app.alansari.R.string.contact_us_dialog_text_new));
                            successDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    resetForm();
                                    successDialog.dismiss();
                                }
                            });
                            successDialog.show();
                        } else {
                            onError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onError();
                    }
                } else {
                    onError();
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
                                    return;
                                }
                            }
                            onError();
                        } else
                            onError();
                    } else
                        onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    onError();
                }
                break;
        }
    }

    private void onError() {
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