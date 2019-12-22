package app.alansari.modules.accountmanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
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
import app.alansari.models.LoadDropDownField;
import app.alansari.models.missingparameter.BeneficiaryDynamicFieldsCeNew;
import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryTypeCeData;
import app.alansari.modules.accountmanagement.models.BusinessActivitiesCeData;
import app.alansari.modules.accountmanagement.models.BusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.CompanyTypeCeData;
import app.alansari.modules.accountmanagement.models.ContributionCeData;
import app.alansari.modules.accountmanagement.models.DistrictCeData;
import app.alansari.modules.accountmanagement.models.FundsSourceCeData;
import app.alansari.modules.accountmanagement.models.IdDateTypeCeData;
import app.alansari.modules.accountmanagement.models.IdProofCeData;
import app.alansari.modules.accountmanagement.models.ProfessionCeData;
import app.alansari.modules.accountmanagement.models.PurposeCeData;
import app.alansari.modules.accountmanagement.models.ResidentialStatusCeData;
import app.alansari.modules.accountmanagement.models.SubBusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.SubPurposeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseDateTypeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseTypeCeData;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOGIN;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.UPDATE_MISSING_FIELD_BENEFICIARY_CE;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 05 August, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class UpdateExistingBeneficiaryCeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, DatePickerDialog.OnDateSetListener , LogOutTimerUtil.LogOutListener  {

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
    private ArrayList<BeneficiaryDynamicFieldsCeNew> beneficiaryDynamicFieldsCeList;
    private String fieldId, errorMessage, selectedDOB, selectedDateOfBirth;

    private boolean isAutoFill;
    private BankData selectedBank;
    private BranchData selectedBranch;
    private AccountTypeData selectedAccountType;
    private CountryData selectedCountry, selectedNationality;
    private LinearLayout mLinLayError;
    private TextView mTv_error_message;

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
        mLinLayError=(LinearLayout)findViewById(app.alansari.R.id.xLinLayError);
        mTv_error_message=(TextView)findViewById(app.alansari.R.id.tv_error_message);

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
            inputLayout.setError(((BeneficiaryDynamicFieldsCeNew) editText.getTag()).getErrorMessage());
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

        // Check Min And Max Length
        for (int i = 0; i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFieldsCeNew) dynamicEditText[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFieldsCeNew) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase("T"))||((BeneficiaryDynamicFieldsCeNew) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT));
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    break;
                }
            if ((Validation.isValidEditTextValue(dynamicEditText[i])) && ((getEditTextMinLength(dynamicEditText[i]) == 0) || dynamicEditText[i].getText().toString().trim().length() >= getEditTextMinLength(dynamicEditText[i])) && ((getEditTextLength(dynamicEditText[i]) == 0) || dynamicEditText[i].getText().toString().trim().length() <= getEditTextLength(dynamicEditText[i]))) {
                layoutStatus = true;
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
            } else {
                layoutStatus = false;
                setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
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
            beneficiaryDynamicFieldsCeList = getIntent().getExtras().getParcelableArrayList(Constants.LIST);
            fieldId = getIntent().getExtras().getString(Constants.ID);
            errorMessage = getIntent().getExtras().getString(Constants.MESSAGE);
            if (dataObject != null && Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
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
            if (dataObject.getNationality() != null && dataObject.getNationalityId() != null) {
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
//--------------------------------------------------OLD---------------------------------------------
               /* if (view.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_7)) || fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_50))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        //intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_NATIONALITY);
                        intent.putExtra(Constants.NATIONALITY_NAME,"nation");
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId()));
                    } else if (((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId().equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_38))) {
                        break;
                    } else {
                        intent = new Intent(context, AddBeneficiaryCeDropdownActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId());
                        intent.putExtra(Constants.TITLE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldName());
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_44))) {
                            intent.putExtra(Constants.BANK_CODE, dataObject.getBankData().getBankCodeCE());
                            intent.putExtra(Constants.TRANSFER_TYPE, dataObject.getTransferType());
                        }
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId()));
                    }
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }*/

//--------------------------------------------------------------------------------------------------


                if (view.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldId();
                    Log.e("cjbhcvgc vgvc",""+fieldId);
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_NATIONALITY))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        intent.putExtra(Constants.NATIONALITY_NAME,"nation");
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldId()));
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
                    } else if (fieldId.equalsIgnoreCase("6")) {
                        intent = new Intent(context, AddBeneficiaryCeDropdownActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId());
                        intent.putExtra(Constants.TITLE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldName());
                        intent.putExtra("BANK_ID", ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getBankId());
                        intent.putExtra("FIELD_ID", ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldId());
                        intent.putExtra("FIELD_KEY", ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getField_key());
                        intent.putExtra("CURRENCY_ID", ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getCurrencyId());
                        intent.putExtra("COUNTRY_ID", ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getCountryId());

                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId()));
                    }else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_7)) || fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_50))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);

                        intent.putExtra(Constants.NATIONALITY_NAME,"nation");
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId()));
                    } else if (((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId().equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_38))) {
                        break;
                    } else {
                        intent = new Intent(context, AddBeneficiaryCeDropdownActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId());
                        intent.putExtra(Constants.TITLE, ((BeneficiaryDynamicFieldsCeNew) view.getTag()).getFieldName());
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_44))) {
                            intent.putExtra(Constants.BANK_CODE, dataObject.getBankData().getBankCodeCE());
                            intent.putExtra(Constants.TRANSFER_TYPE, dataObject.getTransferType());
                        }

                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) view.getTag()).getId()));

                    }

                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);


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
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addBeneficiaryJsonFormat(), Constants.UPDATE_MISSING_FIELD_BENEFICIARY_CE_URL, UPDATE_MISSING_FIELD_BENEFICIARY_CE, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(UPDATE_MISSING_FIELD_BENEFICIARY_CE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_MISSING_FIELD_BENEFICIARY_CE.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), ADD_BENEFICIARY.toString(), false);
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
        if (!Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
            return;
        }
        try {
            dynamicEditText = new EditText[beneficiaryDynamicFieldsCeList.size()];
            dynamicInputLayout = new TextInputLayout[beneficiaryDynamicFieldsCeList.size()];
            dynamicLayout.removeAllViews();
            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(app.alansari.R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(app.alansari.R.id.edit_text);

                if (beneficiaryDynamicFieldsCeList.get(i).getMinLength() == null || Integer.valueOf(beneficiaryDynamicFieldsCeList.get(i).getMinLength()) == 0) {
                    beneficiaryDynamicFieldsCeList.get(i).setMinLength("1");
                }
                if (beneficiaryDynamicFieldsCeList.get(i).getLength() == null) {
                    beneficiaryDynamicFieldsCeList.get(i).setLength("0");
                }

                beneficiaryDynamicFieldsCeList.get(i).setErrorMessage((checkValidBeneficiaryString(beneficiaryDynamicFieldsCeList.get(i).getLength()) ? "Required length is " + beneficiaryDynamicFieldsCeList.get(i).getLength() : "Invalid " + beneficiaryDynamicFieldsCeList.get(i).getFieldName().toLowerCase()));
                editText.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                inputLayout.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                editText.setTag(beneficiaryDynamicFieldsCeList.get(i));
                inputLayout.setTag(i);
                setFieldType(editText, inputLayout, beneficiaryDynamicFieldsCeList.get(i));
                if (i == beneficiaryDynamicFieldsCeList.size() - 1)
                    childLayout.findViewById(app.alansari.R.id.divider).setVisibility(View.INVISIBLE);

                dynamicEditText[i] = editText;
                dynamicInputLayout[i] = inputLayout;
                dynamicLayout.addView(childLayout);
            }
            dynamicLayout.setVisibility(View.VISIBLE);
            checkLayoutFilledStatus(null, null, false);

//-------------------------------------------DKG_---------------------------------------------------
            //Set Error Message
/*
            if (Validation.isValidString(fieldId) && Validation.isValidString(errorMessage)) {
                for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                    if (beneficiaryDynamicFieldsCeList.get(i).getId().equalsIgnoreCase(fieldId)) {
                        dynamicEditText[i].requestFocus();
                        setError(dynamicInputLayout[i], errorMessage);
                    }
                }
            }
*/
            mLinLayError.setVisibility(View.VISIBLE);
            mTv_error_message.setText(errorMessage);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
        }
    }

    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final BeneficiaryDynamicFieldsCeNew currentView) {
        if (currentView.getFieldType().equalsIgnoreCase("D")||currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_DROPDOWN)) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getFieldType().equalsIgnoreCase("T")||currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {
            if (currentView.getInputType() != null && currentView.getInputType().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
            if (getEditTextLength(editText) != 0)
                setTextLimit(editText, getEditTextLength(editText));
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
        LogUtils.d("ok", "Length");
        view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.valueOf(length))});
    }

    private int getEditTextLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) editText.getTag()).getLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    private int getEditTextMinLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCeNew) editText.getTag()).getMinLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnNext.setEnabled(false);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case UPDATE_MISSING_FIELD_BENEFICIARY_CE:
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
                                for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                                    if(beneficiaryDynamicFieldsCeList.get(i).getId()!=null){
                                        if (beneficiaryDynamicFieldsCeList.get(i).getId().equalsIgnoreCase(fieldPkId)) {
                                            dynamicEditText[i].requestFocus();
                                            setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                            return;
                                        }
                                    }else{
                                        if (beneficiaryDynamicFieldsCeList.get(i).getFieldId().equalsIgnoreCase(fieldPkId)) {
                                            dynamicEditText[i].requestFocus();
                                            setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                            return;
                                        }
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
                    } else if (requestCode == 6) {
                        LoadDropDownField selectedAccountType = data.getParcelableExtra(Constants.OBJECT);
                        if (position != -1)
                            setDataToEditText(position, selectedAccountType.getDisplay_key(), selectedAccountType.getDisplay_value());

                    }
                } /*else if (requestCode == Constants.FIELD_ID_BEN_TYPE) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    benType = data.getStringExtra(Constants.OBJECT);
                    if (position != -1)
                        setDataToEditText(position, benType, null);
                }*/ else if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    switch (requestCode) {
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_7:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_50:
                            selectedNationality = data.getParcelableExtra(Constants.OBJECT);
                            if (position != -1) {
                               // SharedPreferenceManger.saveSelectedCountryData(new Gson(), selectedNationality);
                                setDataToEditText(position, ((CountryData) object).getLatinName(), ((CountryData) object).getCountryCodeCE());
                            }
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_8:
                            if (position != -1)
                                setDataToEditText(position, ((FundsSourceCeData) object).getName(), ((FundsSourceCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_9:
                            if (position != -1)
                                setDataToEditText(position, ((PurposeCeData) object).getName(), ((PurposeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_12:
                            if (position != -1)
                                setDataToEditText(position, ((IdProofCeData) object).getIdTypeDesc(), ((IdProofCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_16:
                            if (position != -1)
                                setDataToEditText(position, ((IdDateTypeCeData) object).getName(), ((IdDateTypeCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_18:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_59:
                            if (position != -1)
                                setDataToEditText(position, ((ResidentialStatusCeData) object).getName(), ((ResidentialStatusCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_28:
                            if (position != -1)
                                setDataToEditText(position, ((SubPurposeCeData) object).getName(), ((SubPurposeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_29:
                            if (position != -1)
                                setDataToEditText(position, ((ProfessionCeData) object).getName(), ((ProfessionCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_32:
                            if (position != -1)
                                setDataToEditText(position, ((CompanyTypeCeData) object).getName(), ((CompanyTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_33:
                            if (position != -1)
                                setDataToEditText(position, ((TradeLicenseTypeCeData) object).getIdTypeDesc(), ((TradeLicenseTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_35:
                            if (position != -1)
                                setDataToEditText(position, ((TradeLicenseDateTypeCeData) object).getName(), ((TradeLicenseDateTypeCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_39:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessActivitiesCeData) object).getName(), ((BusinessActivitiesCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_44:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryBranchCeData) object).getName(), ((BeneficiaryBranchCeData) object).getCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_45:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryAccountTypeCeData) object).getAccountType(), ((BeneficiaryAccountTypeCeData) object).getAccountCode());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_54:
                            if (position != -1)
                                setDataToEditText(position, ((BankPurposeCeData) object).getPurposeDesc(), ((BankPurposeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_86:
                            if (position != -1)
                                setDataToEditText(position, ((DistrictCeData) object).getName(), ((DistrictCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_92:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessActivitiesCeData) object).getName(), ((BusinessActivitiesCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_94:
                            if (position != -1)
                                setDataToEditText(position, ((BeneficiaryTypeCeData) object).getBeneficiaryTypeDesc(), ((BeneficiaryTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                            if (position != -1)
                                setDataToEditText(position, ((ContributionCeData) object).getContributionAmount(), ((ContributionCeData) object).getContributionId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_98:
                            if (position != -1)
                                setDataToEditText(position, ((BusinessTypeCeData) object).getBusinessTypeDesc(), ((BusinessTypeCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_99:
                            if (position != -1)
                                setDataToEditText(position, ((SubBusinessTypeCeData) object).getName(), ((SubBusinessTypeCeData) object).getId());
                            break;


                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPositionFromId(String id) {
        for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
            if (id.equalsIgnoreCase(beneficiaryDynamicFieldsCeList.get(i).getId()))
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
            String transferType = "";
            if (dataObject.getModuleName().equalsIgnoreCase("AREX")) {
                if (dataObject.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            } else if (dataObject.getModuleName().equalsIgnoreCase("CE")) {
                if (dataObject.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            }
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.TRANSFER_TYPE,transferType);
            jsonObject.put(Constants.COUNTRY_CODE, dataObject.getCountryData().getCountryCodeCE());
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.CE_MAPPING));
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                try {
                    LogUtils.d("Input", beneficiaryDynamicFieldsCeList.get(i).getId() + " : " + checkValue(dynamicEditText[i].getText().toString().trim()));
                    if(beneficiaryDynamicFieldsCeList.get(i).getId()!=null){
                        jsonObject.put(beneficiaryDynamicFieldsCeList.get(i).getId(), checkValue(dynamicEditText[i].getText().toString().trim()));
                    }else{
                        jsonObject.put(beneficiaryDynamicFieldsCeList.get(i).getFieldId(), checkValue(dynamicEditText[i].getText().toString().trim()));

                    }

                    if (beneficiaryDynamicFieldsCeList.get(i).getFieldType().equalsIgnoreCase("D") || beneficiaryDynamicFieldsCeList.get(i).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_DROPDOWN)&& dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                        jsonObject.put(beneficiaryDynamicFieldsCeList.get(i).getId() + "_CODE", checkValue(dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString().trim()));
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