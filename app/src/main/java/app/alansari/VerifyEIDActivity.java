package app.alansari;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.UserData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class VerifyEIDActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, DatePickerDialog.OnDateSetListener, LogOutTimerUtil.LogOutListener {

    private int count = 0;
    private Context context;
    private String mobileNum, userStatus;
    private int pinType, errorCount;
    private String selectedDOB, selectedExpiry, calExp, calDob;
    private TextView tvMobile;
    private Button btnChange, btnSubmit;
    private EditText etEidNumber, etEidExpiry, etEidDob;
    private TextInputLayout eidLayout, expiryLayout, dobLayout;

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
        setContentView(R.layout.verify_eid_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("CUSTOMER \nVERIFICATION");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getExtras() != null) {
            mobileNum = getIntent().getExtras().getString(Constants.MOBILE_NUM);
            userStatus = getIntent().getExtras().getString(Constants.USER_STATUS);
            pinType = getIntent().getExtras().getInt(Constants.TYPE, Constants.CHANGE_PIN);
        }
        init();
    }

    private void init() {

        btnChange = (Button) findViewById(R.id.change_btn);
        btnSubmit = (Button) findViewById(R.id.submit_btn);
        tvMobile = (TextView) findViewById(R.id.mobile);

        eidLayout = (TextInputLayout) findViewById(R.id.eid_number_layout);
        expiryLayout = (TextInputLayout) findViewById(R.id.eid_expiry_layout);
        dobLayout = (TextInputLayout) findViewById(R.id.eid_dob_layout);
        etEidNumber = (EditText) findViewById(R.id.eid_number);
        etEidExpiry = (EditText) findViewById(R.id.eid_expiry);
        etEidDob = (EditText) findViewById(R.id.eid_dob);

        btnChange.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvMobile.setText(mobileNum.substring(3));

        etEidExpiry.setOnClickListener(this);
        etEidDob.setOnClickListener(this);
        expiryLayout.setOnClickListener(this);
        dobLayout.setOnClickListener(this);
        findViewById(R.id.result_ok_btn).setOnClickListener(this);

        etEidNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    eidLayout.setErrorEnabled(true);
                    eidLayout.setError(getString(R.string.error_invalid_eid_number));
                } else {
                    eidLayout.setError(null);
                    eidLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= etEidNumber.getText().toString().length() && (etEidNumber.getText().toString().length() == 3 ||
                        etEidNumber.getText().toString().length() == 8 || etEidNumber.getText().toString().length() == 16)) {
                    etEidNumber.setText(etEidNumber.getText().toString() + "-");
                    int pos = etEidNumber.getText().length();
                    etEidNumber.setSelection(pos);
                } else if (count >= etEidNumber.getText().toString().length() && (etEidNumber.getText().toString().length() == 3
                        || etEidNumber.getText().toString().length() == 8 || etEidNumber.getText().toString().length() == 16)) {
                    etEidNumber.setText(etEidNumber.getText().toString().substring(0, etEidNumber.getText().toString().length() - 1));
                    int pos = etEidNumber.getText().length();
                    etEidNumber.setSelection(pos);
                }
                count = etEidNumber.getText().toString().length();
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
        super.onDestroy();
        stopLogoutTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.eid_expiry:
            case R.id.eid_expiry_layout:
                etEidNumber.clearFocus();
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putString(Constants.DATE, calExp);
                datePickerFragment.setArguments(args);
                datePickerFragment.show(getSupportFragmentManager(), "Set Expiry Date");
                break;
            case R.id.eid_dob:
            case R.id.eid_dob_layout:
                etEidNumber.clearFocus();
                DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                Bundle args1 = new Bundle();
                args1.putString(Constants.DATE, calDob);
                datePickerFragment1.setArguments(args1);
                datePickerFragment1.show(getSupportFragmentManager(), "Set Date Of Birth");
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(etEidNumber.getText().toString().trim())) {
                    eidLayout.setErrorEnabled(true);
                    eidLayout.setError(getString(R.string.error_invalid_eid_number));
                } else if (selectedExpiry == null) {
                    expiryLayout.setErrorEnabled(true);
                    expiryLayout.setError(getString(R.string.error_invalid_expiry_date));
                } else if (selectedDOB == null) {
                    dobLayout.setErrorEnabled(true);
                    dobLayout.setError(getString(R.string.error_invalid_date_of_birth));
                } else {
                    eidLayout.setErrorEnabled(false);
                    expiryLayout.setErrorEnabled(false);
                    dobLayout.setErrorEnabled(false);
                    submitData();
                }
                break;
            case R.id.result_ok_btn:
                onBackPressed();
                break;
        }
    }

    private void submitData() {
        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().verifyEID(mobileNum, etEidNumber.getText().toString().trim().replace("-", ""), selectedExpiry, selectedDOB, CommonUtils.getUserId(), CommonUtils.getDeviceID(context), userStatus, errorCount), Constants.VERIFY_EID_URL, CommonUtils.SERVICE_TYPE.VERIFY_EID, Request.Method.PUT, this);
        AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VERIFY_EID.toString());
        CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.VERIFY_EID.toString(), false);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (getSupportFragmentManager().findFragmentByTag("Set Expiry Date") != null) {
            calExp = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth).toString();
            selectedExpiry = calExp;//new StringBuilder().append(dayOfMonth).append("-").append(month + 1).append("-").append(year).toString();
            etEidExpiry.setText(dayOfMonth + " " + (CommonUtils.getMonthName(month + 1)) + ", " + year);
            expiryLayout.setError(null);
            expiryLayout.setErrorEnabled(false);
        } else if (getSupportFragmentManager().findFragmentByTag("Set Date Of Birth") != null) {
            calDob = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth).toString();
            selectedDOB = calDob;//new StringBuilder().append(dayOfMonth).append("-").append(month + 1).append("-").append(year).toString();
            etEidDob.setText(dayOfMonth + " " + (CommonUtils.getMonthName(month + 1)) + ", " + year);
            dobLayout.setError(null);
            dobLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case VERIFY_EID:
                if (status == 1) {
                    try {
                        if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                            UserData userData = new Gson().fromJson(response.getJSONArray(Constants.RESULT).getJSONObject(0).toString(), UserData.class);
                            if (userData != null) {
                                //CommonUtils.setUserData(userData);
                                new CommonUtils().setUserData(userData);
                                mobileNum = userData.getMobileNum();
                            }
                        }
                        String statusCode = response.getString(Constants.STATUS_CODE);
                        if (statusCode.equalsIgnoreCase("698") || statusCode.equalsIgnoreCase("699")) {
                            SharedPreferenceManger.setMessage(response.getString(Constants.MESSAGE));
                        }
                        SharedPreferenceManger.setMessage(response.getString(Constants.MESSAGE));
                        LogUtils.v("userStatus", "VerifyEidAct"+"   "+userStatus);

                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            String userStatus = response.getString(Constants.USER_STATUS);
                            switch (userStatus) {
                                case Constants.UVDV:
                                    CommonUtils.setUserStatus(userStatus);
                                    goToSetPIN(userStatus, mobileNum, Constants.UVDV_CODE);
                                    break;
                                case Constants.FPIN:
                                    goToSetPIN(userStatus, mobileNum, Constants.UVDV_CODE);
                                    break;
                                case Constants.UVDN:
                                   // CommonUtils.setUserStatus(userStatus);
                                    goToSetPIN2(userStatus, mobileNum, Constants.UVDN_CODE_NEW);
                                    LogUtils.v("userStatus", "VerifyEidAct2"+"   "+userStatus+" "+Constants.UVDN_CODE_NEW+"  "+Constants.UVDN);
                                    break;
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            errorCount++;
                            if (errorCount == 3) {
//----------------------------------------OLD CODE--------------------------------------------------
                                /*if (userStatus.equalsIgnoreCase(Constants.FPIN)) {
                                    if (context != null)
                                        Toast.makeText(context, getString(R.string.please_try_after_some_time), Toast.LENGTH_SHORT).show();
                                    goToBranch();//added for issue solving on 12 dec 2018
                                    finish();
                                } else
                                    goToSetPINForFailed(userStatus, mobileNum, Constants.UVDN_CODE);
                                    */
//--------------------------------------------------------------------------------------------------
//----------------------------------------------NEW Code--------------------------------------------
                                SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                goToBranchErrorTimes();//added for issue solving on 12 dec 2018
                                finish();

//--------------------------------------------------------------------------------------------------
                            } else {
                                if (context != null)
                                    if (response.getInt(Constants.STATUS_CODE) == Constants.EID_NUMBER_ERROR) {
                                        eidLayout.setErrorEnabled(true);
                                        eidLayout.setError(getString(R.string.error_invalid_eid_number));
                                    } else if (response.getInt(Constants.STATUS_CODE) == Constants.EID_DOB_ERROR) {
                                        dobLayout.setErrorEnabled(true);
                                        dobLayout.setError(getString(R.string.error_invalid_date_of_birth));
                                    } else if (response.getInt(Constants.STATUS_CODE) == Constants.EID_EXPIRY_DATE_ERROR) {
                                        expiryLayout.setErrorEnabled(true);
                                        expiryLayout.setError(getString(R.string.error_invalid_expiry_date));
                                    } else
                                        Toast.makeText(context, getString(R.string.error_eid_verification), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            if (context != null)
                                Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            goToSetPIN(userStatus, mobileNum, Constants.UVDN_CODE);
                        } else {
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void goToSetPIN2(String userStatus, String mobileNum, int uvdnCode) {
        Intent intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.TYPE, pinType);
        intent.putExtra(Constants.USER_STATUS, userStatus);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, uvdnCode);
    }

    private void goToSetPIN(String userStatus, String mobileNum, int reqCode) {
//        FCMUtils.checkFCM(context);
        Intent intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        intent.putExtra(Constants.TYPE, pinType);
        intent.putExtra(Constants.USER_STATUS, userStatus);
        intent.putExtra(Constants.MOBILE_NUM, mobileNum);
        startActivityForResult(intent, reqCode);
    }

    private void goToBranch() {
        try {
            SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            Intent intent = new Intent(context, GoToBranchActivity.class);
            intent.putExtra(Constants.PROFILE_UPDATE_FLAG,"N");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }



    private void goToBranchErrorTimes() {
        try {

            Intent intent = new Intent(context, GoToBranchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }











    private void onEIDVerificationError() {
        startActivity(new Intent(context, GoToBranchActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.UVDV_CODE && resultCode == Activity.RESULT_OK) {
            LogUtils.v("userStatus", "VerifyEidAct3Home"+"   "+userStatus+" "+Constants.UVDV_CODE);
            goToHome();
        } else if (requestCode == Constants.UVDN_CODE_NEW && resultCode == Activity.RESULT_OK) {
            LogUtils.v("userStatus", "VerifyEidAct3GoTo"+"   "+userStatus+" "+Constants.UVDN_CODE_NEW);
            goToBranch();
        }else if(requestCode== Constants.UVDN_CODE && resultCode==Activity.RESULT_OK){
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goToHome() {
        try {
            SharedPreferenceManger.setPrefVal(Constants.IS_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            SharedPreferenceManger.setPrefVal(Constants.JUST_LOGGED_IN, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, -21);
            if (getArguments() != null) {
                String date = getArguments().getString(Constants.DATE, null);
                if (date != null) {
                    c.setTime(CommonUtils.getDateFromString(date));
                }
            }
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, (VerifyEIDActivity) getActivity(), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            Calendar currentTime = Calendar.getInstance();
            if (getTag() != null && getTag().equalsIgnoreCase("Set Expiry Date")) {
                dialog.getDatePicker().setMinDate(currentTime.getTimeInMillis() - 10000);
            } else if (getTag() != null && getTag().equalsIgnoreCase("Set Date Of Birth")) {
                Calendar exp = Calendar.getInstance();
                exp.add(Calendar.YEAR, -21);
                dialog.getDatePicker().setMaxDate(exp.getTimeInMillis());
            }
            dialog.setTitle(getTag());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return dialog;
        }
    }
}