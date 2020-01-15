package app.alansari.modules.accountmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import app.alansari.AppController;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.DatePickerFragment;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AccountTypeData;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.BranchData;
import app.alansari.models.CountryData;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.UPDATE_MISSING_FIELD_BENEFICIARY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;


/**
 * Created by Parveen Dala on 03 August, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class UpdateExistingBeneficiaryActivityWu extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, DatePickerDialog.OnDateSetListener , LogOutTimerUtil.LogOutListener  {

    private Intent intent;
    private Button btnNext;
    private Context context;
    private LinearLayout dynamicLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private BeneficiaryData dataObject, beneficiaryData;
    private TextView tvName, tvBankName, tvBranchName, tvAccountNumber;

    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<BeneficiaryDynamicFields> beneficiaryDynamicFields;
    private String fieldId, errorMessage, selectedDOB, selectedDateOfBirth;

    private boolean isAutoFill;
    private BankData selectedBank;
    private BranchData selectedBranch;
    private AccountTypeData selectedAccountType;
    private CountryData selectedCountry, selectedNationality;
    private String benType;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        tvName = (TextView) findViewById(app.alansari.R.id.name);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(app.alansari.R.id.account_number);

        dynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.account_info_layout);
        btnNext = (Button) findViewById(app.alansari.R.id.confirm_btn);
        btnNext.setOnClickListener(this);

        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnNext.setVisibility(View.GONE);
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }
        );
    }

    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (validated && !isAutoFill) {
            inputLayout.setError(((BeneficiaryDynamicFields) editText.getTag()).getErrorMessage());
            inputLayout.setErrorEnabled(true);
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        }
    }

    private void validateSingleField(EditText editText, TextInputLayout inputLayout) {
        if (checkLayoutFilledStatus(new EditText[]{editText}, new TextInputLayout[]{inputLayout}, true)) {
            checkLayoutFilledStatus(null, null, false);
        }
    }

    private boolean checkLayoutFilledStatus(EditText[] dynamicEditText, TextInputLayout[] dynamicInputLayout, boolean showMessage) {
        boolean layoutStatus = false, specialCharacterValidation = true;
        if (dynamicEditText == null) {
            dynamicEditText = this.dynamicEditText;
            dynamicInputLayout = this.dynamicInputLayout;
        }

        for (int i = 0; dynamicEditText != null && i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    layoutStatus = false;
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getIsMandatory().equalsIgnoreCase("0")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
                if ((dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength() != null && (!((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength().equalsIgnoreCase("0")))) {
                    if (dynamicEditText[i].getText().toString().trim().length() == Integer.valueOf(((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength())) {
                        setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                        layoutStatus = true;
                    } else {
                        setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                        layoutStatus = false;
                        break;
                    }
                } else {
                    setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                    layoutStatus = true;
                }
            } else {
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                layoutStatus = false;
                break;
            }
        }
        if ((showMessage && dynamicEditText.length == 1 && specialCharacterValidation && layoutStatus)) {
            return true;
        }

        if (specialCharacterValidation && layoutStatus)
            btnNext.setEnabled(true);
        else
            btnNext.setEnabled(false);
        return layoutStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.update_existing_beneficiary_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Beneficiary Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();

        if (CommonUtils.getUserMobile() == null) {
            CommonUtils.registerAgain(context);
        }
        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            beneficiaryDynamicFields = getIntent().getExtras().getParcelableArrayList(Constants.LIST);
            fieldId = getIntent().getExtras().getString(Constants.ID);
            errorMessage = getIntent().getExtras().getString(Constants.MESSAGE);
            if (dataObject != null && Validation.isValidList(beneficiaryDynamicFields)) {
                setInitialData();
                updateDynamicFields();
                return;
            }
        }
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setInitialData() {
        if (dataObject != null) {
            tvName.setText(dataObject.getName());
            tvBankName.setText(dataObject.getBankData().getBankName());
            tvBranchName.setText(dataObject.getBranchName());

            if (dataObject.getTransferType() != null && dataObject.getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                tvAccountNumber.setText(dataObject.getMobile());
            } else {
                tvAccountNumber.setText(dataObject.getAccountNumber());
            }

            if (dataObject.getCountryData() != null) {
                selectedCountry = dataObject.getCountryData();
            }

            if (Validation.isValidString(dataObject.getNationality()) && Validation.isValidString(dataObject.getNationalityId())) {
                selectedNationality = new CountryData();
                selectedNationality.setLatinName(dataObject.getNationality());
                selectedNationality.setArabicName(dataObject.getNationality());
                if (dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING))
                    selectedNationality.setCountryCodeAREX(dataObject.getNationalityId());
                else
                    selectedNationality.setCountryCodeCE(dataObject.getNationalityId());
            }

            if (dataObject.getBankData() != null)
                selectedBank = dataObject.getBankData();

            if (dataObject.getBranchData() != null)
                selectedBranch = dataObject.getBranchData();

            if (dataObject.getAccountType() != null) {
                selectedAccountType = new AccountTypeData();
                selectedAccountType.setName(dataObject.getAccountType());
            }
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            finish();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                updateDynamicFields();
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (view.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFields) view.getTag()).getFieldId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_NATIONALITY))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_NATIONALITY);
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFields) view.getTag()).getFieldId()));
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_BENF_BRANCH))) {
                        //Branch
                        if (selectedBank != null) {
                            intent = new Intent(context, SelectItemActivity.class);
                            intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_BENF_BANK_BRANCH_LIST);
                            intent.putExtra(Constants.SERVICE_TYPE, dataObject.getServiceTypeData().getMapping());
                            intent.putExtra(Constants.BANK_CODE, dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? selectedBank.getBankCodeAREX() : selectedBank.getBankCodeCE());
                            startActivityForResult(intent, Constants.AREX_FIELD_ID_BENF_BRANCH);
                            overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                        } else {
                            Toast.makeText(context, "Please select a valid bank.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_ACCOUNT_TYPE))) {
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ACCOUNT_TYPE);
                        intent.putExtra(Constants.BANK_CODE, selectedBank.getBankCodeAREX());
                        startActivityForResult(intent, Constants.AREX_FIELD_ID_ACCOUNT_TYPE);
                        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_DOB))) {
                        //DOB
                        DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                        Bundle args1 = new Bundle();
                        args1.putString(Constants.DATE, selectedDOB);
                        args1.putString(Constants.DATE_RESTRICTION, Constants.DATE_RESTRICTION_MAX);
                        datePickerFragment1.setArguments(args1);
                        datePickerFragment1.show(getSupportFragmentManager(), "Select DOB");
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_DATE_OF_BIRTH))) {
                        //Date Of Birth
                        DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                        Bundle args1 = new Bundle();
                        args1.putString(Constants.DATE, selectedDateOfBirth);
                        args1.putString(Constants.DATE_RESTRICTION, Constants.DATE_RESTRICTION_MAX);
                        datePickerFragment1.setArguments(args1);
                        datePickerFragment1.show(getSupportFragmentManager(), "Select Date Of Birth");
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.FIELD_ID_BEN_TYPE))) {
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                        intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(app.alansari.R.array.beneficiary_type))));
                        startActivityForResult(intent, Constants.FIELD_ID_BEN_TYPE);
                        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    }
                }
                break;
            case app.alansari.R.id.confirm_btn:
                submitData();
                break;
        }
    }

    private void onSuccess(BeneficiaryData beneficiaryData) {
        Intent data = new Intent();
        data.putExtra(Constants.OBJECT, beneficiaryData);
        setResult(AppCompatActivity.RESULT_OK, data);
        finish();
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = null;
            jsonObjReq = new CallAddr().executeApi(addBeneficiaryJsonFormat(), Constants.UPDATE_MISSING_FIELD_BENEFICIARY_URL, UPDATE_MISSING_FIELD_BENEFICIARY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(UPDATE_MISSING_FIELD_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_MISSING_FIELD_BENEFICIARY.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), UPDATE_MISSING_FIELD_BENEFICIARY.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
    }

    private void updateDynamicFields() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        if (!Validation.isValidList(beneficiaryDynamicFields)) {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
            return;
        }
        try {
            dynamicEditText = new EditText[beneficiaryDynamicFields.size()];
            dynamicInputLayout = new TextInputLayout[beneficiaryDynamicFields.size()];
            dynamicLayout.removeAllViews();
            for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                if (beneficiaryDynamicFields.get(i).isStatic())
                    continue;
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(app.alansari.R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(app.alansari.R.id.edit_text);

                if (beneficiaryDynamicFields.get(i).getMinLength() == null || Integer.valueOf(beneficiaryDynamicFields.get(i).getMinLength()) == 0) {
                    beneficiaryDynamicFields.get(i).setMinLength("1");
                }
                if (beneficiaryDynamicFields.get(i).getLength() == null) {
                    beneficiaryDynamicFields.get(i).setLength("0");
                }

                updateDynamicField(editText, inputLayout, i == (beneficiaryDynamicFields.size() - 1) ? childLayout.findViewById(app.alansari.R.id.divider) : null, beneficiaryDynamicFields.get(i), null);
                if (i == beneficiaryDynamicFields.size() - 1)
                    childLayout.findViewById(app.alansari.R.id.divider).setVisibility(View.INVISIBLE);

                if (checkValidBeneficiaryString(beneficiaryDynamicFields.get(i).getPrefix())) {
                    isAutoFill = true;
                    editText.setText(beneficiaryDynamicFields.get(i).getPrefix());
                    isAutoFill = false;
                    if (!beneficiaryDynamicFields.get(i).getLength().equalsIgnoreCase("0")) {
                        beneficiaryDynamicFields.get(i).setLength(String.valueOf(Integer.valueOf(beneficiaryDynamicFields.get(i).getLength()) + beneficiaryDynamicFields.get(i).getPrefix().length()));
                    }
                    setTextLimit(editText, Integer.valueOf(beneficiaryDynamicFields.get(i).getLength()));
                } else {
                    if (!beneficiaryDynamicFields.get(i).getLength().equalsIgnoreCase("0"))
                        setTextLimit(editText, Integer.valueOf(beneficiaryDynamicFields.get(i).getLength()));
                }

                dynamicEditText[i] = editText;
                dynamicInputLayout[i] = inputLayout;
                dynamicLayout.addView(childLayout);
            }
            dynamicLayout.setVisibility(View.VISIBLE);
            checkLayoutFilledStatus(null, null, false);

            //Set Error Message
            if (Validation.isValidString(fieldId) && Validation.isValidString(errorMessage)) {
                for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                    if (beneficiaryDynamicFields.get(i).getFieldId().equalsIgnoreCase(fieldId)) {
                        dynamicEditText[i].requestFocus();
                        setError(dynamicInputLayout[i], errorMessage);
                    }
                }
            }
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
        }
    }

    private void updateDynamicField(EditText editText, TextInputLayout inputLayout, View divider, BeneficiaryDynamicFields currentView, String errorMessage) {
        if (currentView != null) {
            if (errorMessage == null)
                currentView.setErrorMessage((checkValidBeneficiaryString(currentView.getLength()) ? "Required length is " + currentView.getLength() : "Invalid " + currentView.getFieldName().toLowerCase()));
            else
                currentView.setErrorMessage(errorMessage);
            editText.setTag(currentView);
            inputLayout.setTag(currentView);
        } else
            return;

        setVisibility(inputLayout, divider, currentView.getIsVisible().equalsIgnoreCase("1"));
        setMandatory(editText, inputLayout, currentView.getFieldName() + (currentView.getIsMandatory().equalsIgnoreCase("1") ? "*" : ""));
        setFieldType(editText, inputLayout, currentView);
    }

    private void setVisibility(View view, View divider, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
        if (divider != null)
            divider.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void setMandatory(EditText editText, TextInputLayout inputLayout, String hintText) {
        editText.setHint(hintText);
        inputLayout.setHint(hintText);
    }

    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final BeneficiaryDynamicFields currentView) {
        if (currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_DROPDOWN)) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {
            if (currentView.getInputType() != null && currentView.getInputType().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateSingleField(editText, inputLayout);
            }
        });
    }

    private void setTextLimit(EditText view, int length) {
        view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.valueOf(length))});
    }

    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnNext.setEnabled(false);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case UPDATE_MISSING_FIELD_BENEFICIARY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BeneficiaryData> beneficiaryData = (ArrayList<BeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryData>>() {
                                }.getType());
                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    this.beneficiaryData = beneficiaryData.get(0);
                                    this.beneficiaryData.setTransferType(dataObject.getTransferType());
                                    onSuccess(this.beneficiaryData);
                                    return;
                                }
                            }
                            onError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                            String fieldPkId = response.getString(Constants.ID);
                            if (Validation.isValidString(fieldPkId)) {
                                for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                                    if (beneficiaryDynamicFields.get(i).getFieldId().equalsIgnoreCase(fieldPkId)) {
                                        dynamicEditText[i].requestFocus();
                                        setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                        return;
                                    }
                                }
                            }
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
        }
    }

    private void onError() {
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (getSupportFragmentManager().findFragmentByTag("Select DOB") != null) {
            selectedDOB = new StringBuilder().append(dayOfMonth).append("-").append(month + 1).append("-").append(year).toString();
            int position = getPositionFromId(String.valueOf(Constants.AREX_FIELD_ID_DOB));
            if (position != -1)
                setDataToEditText(position, selectedDOB, null);
        } else if (getSupportFragmentManager().findFragmentByTag("Select Date Of Birth") != null) {
            selectedDateOfBirth = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(dayOfMonth).toString();
            int position = getPositionFromId(String.valueOf(Constants.AREX_FIELD_ID_DATE_OF_BIRTH));
            if (position != -1)
                setDataToEditText(position, selectedDateOfBirth, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    if (requestCode == Constants.AREX_FIELD_ID_NATIONALITY) {
                        selectedNationality = data.getParcelableExtra(Constants.OBJECT);
                        if (position != -1)
                            setDataToEditText(position, ((CountryData) object).getLatinName(), ((CountryData) object).getCountryCodeAREX());
                    } else if (requestCode == Constants.AREX_FIELD_ID_BENF_BRANCH) {
                        selectedBranch = data.getParcelableExtra(Constants.OBJECT);
                        if (position != -1)
                            setDataToEditText(position, ((BranchData) object).getBranchName(), ((BranchData) object).getBranchCode());

                        if (selectedBranch != null) {
                            isAutoFill = true;
                            int IFSCFieldPosition = getPositionFromId(String.valueOf(Constants.AREX_FIELD_ID_IFSC_CODE));
                            if (IFSCFieldPosition != -1)
                                if (dynamicEditText[IFSCFieldPosition].getVisibility() == View.VISIBLE && Validation.isValidString(selectedBranch.getEftCode())) {
                                    dynamicEditText[IFSCFieldPosition].setText(selectedBranch.getEftCode());
                                    dynamicEditText[IFSCFieldPosition].setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
                                    dynamicEditText[IFSCFieldPosition].setFocusable(false);
                                    dynamicEditText[IFSCFieldPosition].setPressed(false);
                                } else {
                                    dynamicEditText[IFSCFieldPosition].setText(null);
                                    dynamicEditText[IFSCFieldPosition].setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
                                    dynamicEditText[IFSCFieldPosition].setFocusable(true);
                                    dynamicEditText[IFSCFieldPosition].setPressed(true);
                                }
                            isAutoFill = false;
                        }
                    } else if (requestCode == Constants.AREX_FIELD_ID_ACCOUNT_TYPE) {
                        selectedAccountType = data.getParcelableExtra(Constants.OBJECT);
                        if (position != -1)
                            setDataToEditText(position, selectedAccountType.getName(), selectedAccountType.getCode());
                    }
                } else if (requestCode == Constants.FIELD_ID_BEN_TYPE) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    benType = data.getStringExtra(Constants.OBJECT);
                    if (position != -1)
                        setDataToEditText(position, benType, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPositionFromId(String id) {
        for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
            if (id.equalsIgnoreCase(beneficiaryDynamicFields.get(i).getFieldId()))
                return i;
        }
        return -1;
    }

    private void setDataToEditText(int position, String text, String code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }

    private void setDataToEditText(int position, String text, int code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }

    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private JSONObject addBeneficiaryJsonFormat() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.TRANSFER_TYPE, dataObject.getTransferType());
            jsonObject.put(Constants.COUNTRY_CODE, dataObject.getCountryData().getCountryCodeAREX());
            jsonObject.put(Constants.SERVICE_TYPE, dataObject.getServiceTypeData().getMapping());
            jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.AREX_MAPPING));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                try {
                    if (beneficiaryDynamicFields.get(i).getFieldId().equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_NATIONALITY))) {
                        dataObject.setNationality(selectedNationality.getNationality());
                        dataObject.setNationalityId(selectedNationality.getNationalityId());
                    }
                    LogUtils.d("Input", beneficiaryDynamicFields.get(i).getFieldId() + " : " + checkValue(dynamicEditText[i].getText().toString().trim()));
                    jsonObject.put(beneficiaryDynamicFields.get(i).getFieldId(), checkValue(dynamicEditText[i].getText().toString().trim()));
                    if (beneficiaryDynamicFields.get(i).getFieldId().equalsIgnoreCase("" + Constants.FIELD_ID_BEN_TYPE)) {
                        jsonObject.put(beneficiaryDynamicFields.get(i).getFieldId(), checkValue(dynamicEditText[i].getText().toString().trim()).substring(0, 1));
                    }
                    if (beneficiaryDynamicFields.get(i).getFieldType().equalsIgnoreCase("DROPDOWN") && dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                        jsonObject.put(beneficiaryDynamicFields.get(i).getFieldId() + "_CODE", checkValue(dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString().trim()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "updateBeneficiary :-  " + jsonObject.toString());
        return jsonObject;
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