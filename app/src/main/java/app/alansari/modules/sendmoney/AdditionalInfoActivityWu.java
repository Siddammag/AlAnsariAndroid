package app.alansari.modules.sendmoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.adapters.PromoCodeAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CountryData;
import app.alansari.models.PromoCodeData;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.models.WuCurrencyData;
import app.alansari.models.WuRateChargesResponse;
import app.alansari.modules.accountmanagement.models.WUBeneficiaryDybamicFields;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_FETCH_STATE;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 24 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AdditionalInfoActivityWu extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {


    private Intent intent;
    private Button btnConfirm;
    private Context context;

    private LinearLayout dynamicLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private WUBeneficiaryData dataObject;
    private JSONObject sendMoneyResponse;
    private TextView tvName, tvBankName, tvAccountNumber, tvMobile;
    private TextView tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;

    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<WUBeneficiaryDybamicFields> additionalInfoFields;
    private String fieldId, errorMessage, selectedDOB, selectedDateOfBirth;

    private String wuCardNo;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String arexUserId;
    private String BENEF_PK_ID = "";
    private String benefType = "";
    private long timeStamp;
    private boolean isAutoFill, isPromoListActive;
    private Dialog promoCodeDialog;
    private EditText etPromoCode;
    private RecyclerView promoRecyclerView;
    private TextView tvPromoMessage;
    private Button btnSave;
    private PromoCodeAdapter promoCodeAdapter;
    private WuRateChargesResponse wuRateChargesResponse;
    private String TOTAL_WU_POINTS = "0";
    private ImageView ivChecked;
    private TextView tvTerms;
    private boolean isAgreed;
    private Dialog priorityConditionsDialog;
    private ScrollView mScrollViewAdditionInfo;

    String tempMobileCode = "";
    String tempMobileNumber = "";
    private String sessionTime,userPkId;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    private void init() {
        userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));
        mScrollViewAdditionInfo=(ScrollView) findViewById(R.id.xScrollViewAdditionInfo);

        tvName = (TextView) findViewById(R.id.name);
        tvMobile = (TextView) findViewById(R.id.mobile_number);
        tvBankName = (TextView) findViewById(R.id.bank_name);
        tvAccountNumber = (TextView) findViewById(R.id.account_number);

        tvTotalReceive = (TextView) findViewById(R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(R.id.total_to_pay_currency_code);

        dynamicLayout = (LinearLayout) findViewById(R.id.dynamic_layout);
        btnConfirm = (Button) findViewById(R.id.confirm_btn);
        btnConfirm.setOnClickListener(this);

        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnConfirm.setVisibility(View.GONE);
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnConfirm.setVisibility(View.VISIBLE);
                                }
                            }, 100);
                        }
                    }
                }
        );

        etPromoCode = (EditText) findViewById(R.id.promocode);
        promoCodeDialog = new Dialog(context, R.style.CustomDialogThemeLightBg);
        promoCodeDialog.setCanceledOnTouchOutside(false);
        promoCodeDialog.setContentView(R.layout.promo_code_dialog);
        promoCodeAdapter = new PromoCodeAdapter(context);
        promoRecyclerView = (RecyclerView) promoCodeDialog.findViewById(R.id.recycler_view);
        tvPromoMessage = (TextView) promoCodeDialog.findViewById(R.id.message);
        btnSave = (Button) promoCodeDialog.findViewById(R.id.save);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        promoRecyclerView.setLayoutManager(mLayoutManager);
        promoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        promoRecyclerView.setAdapter(promoCodeAdapter);

        promoCodeDialog.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeDialog.dismiss();
            }
        });
        etPromoCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isPromoListActive) {
                    promoCodeDialog.show();
                    etPromoCode.clearFocus();
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPromoCode.setText(promoCodeAdapter.getCode());
                dataObject.setPromoCode(promoCodeAdapter.getPromoCode());
                promoCodeDialog.dismiss();
            }
        });

        tvTerms = (TextView) findViewById(R.id.accept_text);
        tvTerms.setOnClickListener(this);
        ivChecked = (ImageView) findViewById(R.id.accept_image);
        ivChecked.setOnClickListener(this);
    }

    private void fetchTermsAndConditions() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),LogoutCalling.getDeviceID(context),sessionTime), Constants.WU_FETCH_TERMS_AND_CONDITIONS_URL, CommonUtils.SERVICE_TYPE.WU_FETCH_TERMS_AND_CONDITIONS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.WU_FETCH_TERMS_AND_CONDITIONS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.WU_FETCH_TERMS_AND_CONDITIONS.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (validated && !isAutoFill) {
            inputLayout.setError(((WUBeneficiaryDybamicFields) editText.getTag()).getErrorMessage());
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
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getInputtype() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getInputtype().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getMandatory().equalsIgnoreCase("N")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
                setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                layoutStatus = true;
            } else {
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                layoutStatus = false;
                break;
            }
        }
        if ((showMessage && dynamicEditText.length == 1 && specialCharacterValidation && layoutStatus)) {
            return true;
        }

        if (specialCharacterValidation && layoutStatus && isAgreed)
            btnConfirm.setEnabled(true);
        else
            btnConfirm.setEnabled(false);
        return layoutStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wu_additional_info_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("Additional Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        init();
        if (getIntent().getExtras() != null) {
            BENEF_PK_ID = getIntent().getStringExtra(Constants.BENEF_PK_ID);
            benefType = getIntent().getStringExtra(Constants.BENEF_TYPE);
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
            TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
            wuRateChargesResponse = getIntent().getExtras().getParcelable(Constants.wuRateChargesResponse);
            try {
                sendMoneyResponse = new JSONObject(getIntent().getStringExtra(Constants.WU_RATE_CHARGE_RESPONSE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
            if (dataObject != null) {
                setInitialData();
                fetchAdditionalInfo();
                return;
            }
        }
        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void fetchPromoCode() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),LogoutCalling.getDeviceID(context),sessionTime), Constants.PROMO_CODE_URL, CommonUtils.SERVICE_TYPE.PROMO_CODE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
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



    private void setInitialData() {
        if (dataObject != null) {

            if (dataObject.getReceiverNameType().equalsIgnoreCase("D")) {
                tvName.setText(CommonUtils.getValidString(dataObject.getReceiverFirstName()) + " " + CommonUtils.getValidString(dataObject.getReceiverMiddleName()) + " " + CommonUtils.getValidString(dataObject.getReceiverLastName()));
            } else {
                tvName.setText(CommonUtils.getValidString(dataObject.getReceiverFirstName()) + " " + CommonUtils.getValidString(dataObject.getReceiverLastName()) + " " + CommonUtils.getValidString(dataObject.getReceiverMiddleName()));
            }

            String number = CommonUtils.getValidString(dataObject.getReceiverContactNumber());
            if (number != null && number.length() > 0) {
                String code = CommonUtils.getValidString(dataObject.getReceiverContactIsdCode());
                tvMobile.setText(code + "-" + number);
            } else {
                tvMobile.setText("-");
            }
            if (dataObject != null && dataObject.getAdditionalInfoList() != null && dataObject.getAdditionalInfoList().size() > 0) {
                for (int i = 0; i < dataObject.getAdditionalInfoList().size(); i++) {
                    if (dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("53")) {
                        tempMobileCode = dataObject.getAdditionalInfoList().get(i).getFieldValue();
                    } else if (dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("43")) {
                        tempMobileNumber = dataObject.getAdditionalInfoList().get(i).getFieldValue();
                    }
                }
            }

            tvBankName.setText("Western Union");

            try {
                String recAmt = CommonUtils.addCommaToString(sendMoneyResponse.getString(Constants.DEST_PRINCIPAL_AMOUNT));
                if (recAmt.equalsIgnoreCase("")) {
                    tvTotalReceive.setText(sendMoneyResponse.getString(Constants.DEST_PRINCIPAL_AMOUNT));
                } else {
                    tvTotalReceive.setText(recAmt);
                }
                tvTotalReceiveCurrencyCode.setText(dataObject.getReceiverCurrencyCode());
                tvTotalToPay.setText(CommonUtils.addCommaToString(sendMoneyResponse.getString(Constants.GROSS_TOTAL_AMOUNT)));
                tvTotalToPayCurrencyCode.setText("AED");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_button:
            case R.id.empty_button:
                fetchAdditionalInfo();
                break;
            case R.id.input_layout:
            case R.id.edit_text:
                if (v.getTag() != null) {
                    WUBeneficiaryDybamicFields currentView = ((WUBeneficiaryDybamicFields) v.getTag());
                    if (Integer.valueOf(currentView.getFieldId()) == 47) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_COUNTRY);
                        intent.putExtra(Constants.ID, Integer.valueOf(currentView.getFieldId()));
                        intent.putExtra(Constants.IS_FOR_NATIONANLITY, true);
                        startActivityForResult(intent, Constants.WU_SELECT_ITEM_COUNTRY);
                    } else {
                        String payoutStateCity = "";
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD);
                        intent.putExtra(Constants.ID, Integer.valueOf(currentView.getFieldId()));
                        intent.putExtra(Constants.TITLE, currentView.getFieldLable());
                        intent.putExtra(Constants.WU_COUNTRY_CODE, dataObject.getReceiverCountryCode());
                        intent.putExtra(Constants.WU_COUNTRY_STATE_CODE, dataObject.getArexCountryCode());
                        intent.putExtra(Constants.STATE_CODE, payoutStateCity);
                        startActivityForResult(intent, Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD);
                    }

                    overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
                }
                break;
            case R.id.confirm_btn:
                if (btnConfirm.isEnabled()) {
                    addAdditionalInfo();
                } else {
                    Toast.makeText(context, getString(R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.promocode_layout:
                promoCodeDialog.show();
                break;
            case R.id.accept_text:
                fetchTermsAndConditions();
                break;
            case R.id.accept_image:
                if (isAgreed) {
                    ivChecked.setImageResource(0);
                    isAgreed = false;
                } else {
                    ivChecked.setImageResource(R.drawable.svg_success);
                    isAgreed = true;
                }
                checkLayoutFilledStatus(null, null, false);
                break;
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
        if (additionalInfoFields == null) {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
            return;
        }
        try {
            dynamicEditText = new EditText[additionalInfoFields.size()];
            dynamicInputLayout = new TextInputLayout[additionalInfoFields.size()];
            dynamicLayout.removeAllViews();
            for (int i = 0; i < additionalInfoFields.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(R.id.edit_text);

                updateDynamicField(editText, inputLayout, i == (additionalInfoFields.size() - 1) ? childLayout.findViewById(R.id.divider) : null, additionalInfoFields.get(i), null);
                if (i == additionalInfoFields.size() - 1)
                    childLayout.findViewById(R.id.divider).setVisibility(View.INVISIBLE);

                dynamicEditText[i] = editText;
                dynamicInputLayout[i] = inputLayout;
                dynamicLayout.addView(childLayout);
            }
            dynamicLayout.setVisibility(View.VISIBLE);
            checkLayoutFilledStatus(null, null, false);

            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
        }
    }

    private void updateDynamicField(EditText editText, TextInputLayout inputLayout, View divider, WUBeneficiaryDybamicFields currentView, String errorMessage) {
        if (currentView != null) {
            if (errorMessage == null)
                currentView.setErrorMessage("Invalid " + currentView.getFieldLable().toLowerCase());
            else
                currentView.setErrorMessage(errorMessage);
            editText.setTag(currentView);
            inputLayout.setTag(currentView);
        } else
            return;

        setMandatory(editText, inputLayout, currentView.getFieldLable() + (currentView.getMandatory().equalsIgnoreCase("Y") ? "*" : ""));
        setFieldType(editText, inputLayout, currentView);
    }

    private void setMandatory(EditText editText, TextInputLayout inputLayout, String hintText) {
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.color9B9B9B));
        CommonUtils.setInputTextLayoutColor(context, inputLayout);
        editText.setHint(hintText);
        inputLayout.setHint(null);
    }


    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final WUBeneficiaryDybamicFields currentView) {
        if (currentView.getInputtype().equalsIgnoreCase("D") || currentView.getInputtype().equalsIgnoreCase("DROPDOWN")) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getInputtype().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {
            if (currentView.getInputtype() != null && currentView.getInputtype().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
        } else if (currentView.getInputtype().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
            if (currentView.getInputtype() != null && currentView.getInputtype().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
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
                if (editText.getText().length() > 0) {
                    inputLayout.setHint(editText.getHint());
                } else {
                    inputLayout.setHint(null);
                }

            }
        });
    }

    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnConfirm.setEnabled(false);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.WU_SELECT_ITEM_ADDITIONAL_FIELD && resultCode == Activity.RESULT_OK && data.getExtras().getInt(Constants.ID, -1) != -1 && data.getParcelableExtra(Constants.OBJECT) != null) {
                int fieldPosition = getPositionFromId(String.valueOf(data.getExtras().getInt(Constants.ID, -1)));
                if (((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getName() == null) {
                    dynamicEditText[fieldPosition].setText(((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getDisplayValue());
                    if (data.getExtras().getInt(Constants.ID, -1) == 75) {
                        dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, ((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getDisplayKey());
                    } else {
                        dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, ((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getDisplayKey());
                    }
                } else {
                    dynamicEditText[fieldPosition].setText(((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getName());
                    dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, ((WuCurrencyData) data.getParcelableExtra(Constants.OBJECT)).getName());
                }
                dynamicInputLayout[fieldPosition].setHint(additionalInfoFields.get(fieldPosition).getFieldLable());
            } else if (requestCode == Constants.WU_SELECT_ITEM_COUNTRY && resultCode == Activity.RESULT_OK) {
                int fieldPosition = getPositionFromId(String.valueOf(data.getExtras().getInt(Constants.ID, -1)));
                dynamicEditText[fieldPosition].setText(((CountryData) data.getParcelableExtra(Constants.OBJECT)).getLatinName());
                dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, ((CountryData) data.getParcelableExtra(Constants.OBJECT)).getCountryCodeAREX());
                dynamicInputLayout[fieldPosition].setHint(additionalInfoFields.get(fieldPosition).getFieldLable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPositionFromId(String id) {
        for (int i = 0; i < additionalInfoFields.size(); i++) {
            if (id.equalsIgnoreCase(additionalInfoFields.get(i).getFieldId()))
                return i;
        }
        return -1;
    }

    void fetchAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            String SERVICE_TYPE = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
            String isSecurityQuestAvailable = null;
            String adeAmount = null;
            try {
                adeAmount = sendMoneyResponse.getString(Constants.GROSS_TOTAL_AMOUNT);
                if (sendMoneyResponse.getString(Constants.IS_TEST_QUESTION_AVAILABLE).equalsIgnoreCase("Y")) {
                    isSecurityQuestAvailable = sendMoneyResponse.getString(Constants.IS_TEST_QUESTION_AVAILABLE);
                } else {
                    isSecurityQuestAvailable = "N";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String isBen = "N";
            String isTxn = "Y";
            String nameType = dataObject.getReceiverNameType();
            String benType = benefType;

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(
                    new APIRequestParams().fetchAdditionalInfoWu((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING),
                            getIntent().getStringExtra(Constants.WU_COUNTRY_CODE), getIntent().getStringExtra(Constants.AREX_COUNTRY_CODE), SERVICE_TYPE, isSecurityQuestAvailable, adeAmount
                            , isBen, isTxn, nameType, benType, LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_WU_ADDITIONAL_INFO_URL,
                    CommonUtils.SERVICE_TYPE.FETCH_WU_ADDITIONAL_INFO, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_ADDITIONAL_INFO.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    void addAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalInfoJsonFormat(), Constants.WU_VALIDATE_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.WU_VALIDATE_SEND_MONEY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.WU_VALIDATE_SEND_MONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.WU_VALIDATE_SEND_MONEY.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.WU_VALIDATE_SEND_MONEY.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }


    private void updatePayoutStateCity() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuCity(dataObject.getArexCurrencyCode(),userPkId,LogoutCalling.getDeviceID(context),sessionTime), Constants.WU_FETCH_STATE_URL, WU_FETCH_STATE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_FETCH_STATE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_FETCH_STATE.toString());
        } else {
            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }

    }

    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private JSONObject addAdditionalInfoJsonFormat() {
        String SERVICE_TYPE = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
        JSONObject jsonObject = new JSONObject();
        try {
            dataObject.setPromoCode(etPromoCode.getText().toString());
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.AREX_MEM_PK_ID, arexUserId);
            jsonObject.put(Constants.BENEF_PK_ID, "");
            jsonObject.put(Constants.CCY_DESC, "");
            jsonObject.put(Constants.BENEF_TYPE, benefType);
            if (benefType.equalsIgnoreCase("ADD")) {
                jsonObject.put(Constants.BENEF_PK_ID, BENEF_PK_ID);
            }
            jsonObject.put(Constants.WU_COUNTRY_CODE, dataObject.getReceiverCountryCode());
            jsonObject.put(Constants.AREX_COUNTRY_CODE, getIntent().getStringExtra(Constants.AREX_COUNTRY_CODE));
            jsonObject.put(Constants.WU_SERVICE_TYPE, SERVICE_TYPE);
            jsonObject.put(Constants.WU_CCY_CODE, dataObject.getReceiverCurrencyCode());
            jsonObject.put(Constants.AREX_CCY_CODE, dataObject.getArexCurrencyCode());
            jsonObject.put(Constants.WU_PARAM_REF_NO, wuRateChargesResponse.getREFERENCENO());
            jsonObject.put(Constants.FEE_ENQ_TXN_TYPE, wuRateChargesResponse.getFEEENQTXNTYPE());
            jsonObject.put(Constants.WU_PROMO_CODE, getIntent().getStringExtra(Constants.WU_PROMO_CODE));
            jsonObject.put(Constants.WU_LOOKUP_PROMO_CODE, "");
            jsonObject.put(Constants.IS_WU_LOOKUP_PROMO_CODE_SELECTED, "N");
            jsonObject.put(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
            jsonObject.put(Constants.SERVICE_TYPE, "WU");
            jsonObject.put(Constants.TRANSFER_TYPE, "CP");
            jsonObject.put(Constants.MODE_PK_ID, "");
            jsonObject.put(Constants.MODE_NAME, "");
            jsonObject.put(Constants.MODE_DESCRIPTION, "");
            jsonObject.put(Constants.TERMS_AND_CONDITION_FLAG, "Y");
            jsonObject.put(Constants.TEST_QUESTION_FLAG, "P");
            jsonObject.put(Constants.TOTAL_VALUE_AED, wuRateChargesResponse.getORIGINALPRINCIPLEAMOUNT());
            jsonObject.put(Constants.TOTAL_NET_AMOUNT, wuRateChargesResponse.getGROSSTOTALAMOUNT());
            jsonObject.put(Constants.TXN_AMOUNT, wuRateChargesResponse.getDESTPRINCIPALAMOUNT());
            jsonObject.put(Constants.RATE, wuRateChargesResponse.getRATE());
            jsonObject.put(Constants.CHARGE_AMOUNT, wuRateChargesResponse.getCHARGES());
            jsonObject.put(Constants.VAT_CHARGES, wuRateChargesResponse.getVATCHARGES());
            jsonObject.put(Constants.TAX_WORKSHEET, wuRateChargesResponse.getTAXWORKSHEET());
            jsonObject.put(Constants.TAX_RATE, wuRateChargesResponse.getTAXRATE());
            jsonObject.put(Constants.PROMO_DISCOUNT_AMOUNT, wuRateChargesResponse.getPROMODISCOUNTAMOUNT());
            jsonObject.put(Constants.TEST_QUESTION_FLAG, wuRateChargesResponse.getISTESTQUESTIONAVAILABLE());

            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            timeStamp = System.currentTimeMillis();
            jsonObject.put(Constants.TIME_STAMP, timeStamp);

            JSONArray jsonArray = new JSONArray();
            if (additionalInfoFields != null) {
                for (int i = 0; i < additionalInfoFields.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.FIELD_PK_ID, additionalInfoFields.get(i).getFieldId());
                        jsonObjectField.put(Constants.FIELD_VALUE, checkValue(dynamicEditText[i].getText().toString().trim()));
                        if (dynamicEditText[i].getTag(R.id.VIEW_TAG_CODE_ID) != null)
                            jsonObjectField.put(Constants.FIELD_VALUE_CODE, checkValue((dynamicEditText[i].getTag(R.id.VIEW_TAG_CODE_ID) != null) ? dynamicEditText[i].getTag(R.id.VIEW_TAG_CODE_ID).toString() : null));
                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (dataObject.getAdditionalInfoList() != null) {
                for (int i = 0; i < dataObject.getAdditionalInfoList().size(); i++) {
                    if (!dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("75")
                            && !dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("43")
                            && !dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("53")) {
                        try {
                            JSONObject jsonObjectField = new JSONObject();
                            jsonObjectField.put(Constants.FIELD_PK_ID, dataObject.getAdditionalInfoList().get(i).getFieldId());
                            jsonObjectField.put(Constants.FIELD_VALUE, dataObject.getAdditionalInfoList().get(i).getFieldValue());
                            jsonObjectField.put(Constants.FIELD_VALUE_CODE, dataObject.getAdditionalInfoList().get(i).getFieldId());
                            jsonArray.put(jsonObjectField);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            jsonObject.put(Constants.RESULT, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfo :-  " + jsonObject.toString());
        return jsonObject;
    }

    private void onSuccess() {
        dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));
        intent = new Intent(context, PaymentModeActivity.class);
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.IS_WU, "Y");
        intent.putExtra(Constants.BENEF_TYPE, benefType);
        intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        intent.putExtra(Constants.WU_RATE_CHARGE_RESPONSE, sendMoneyResponse.toString());
        intent.putExtra(Constants.WU_CARD_NO, wuCardNo);
        intent.putExtra(Constants.wuRateChargesResponse, wuRateChargesResponse);
        intent.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
        intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
        intent.putExtra(Constants.TIME_STAMP, timeStamp);
        intent.putExtra(Constants.WU_PROMO_CODE, getIntent().getStringExtra(Constants.WU_PROMO_CODE));
        intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE, getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE));
        intent.putExtra(Constants.BENEF_PK_ID, BENEF_PK_ID);
        startActivity(intent);
    }

    private void setError(TextView editText, TextView textView, String msg) {
        editText.setError(msg);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorE86768));
        btnConfirm.setEnabled(false);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case PROMO_CODE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<PromoCodeData> promoCodeData = (ArrayList<PromoCodeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PromoCodeData>>() {
                                }.getType());
                                promoCodeAdapter.addArrayList(promoCodeData);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {

                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case WU_FETCH_TERMS_AND_CONDITIONS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            createConditionDialog(response.getString(Constants.MESSAGE));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {

                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case FETCH_WU_ADDITIONAL_INFO:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                additionalInfoFields = (ArrayList<WUBeneficiaryDybamicFields>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WUBeneficiaryDybamicFields>>() {
                                }.getType());
                                if (additionalInfoFields != null && additionalInfoFields.size() > 0) {
                                    findViewById(R.id.additionalinfolable).setVisibility(View.VISIBLE);
                                    updateDynamicFields();
                                    if (dataObject.getArexCountryCode().equalsIgnoreCase("125")) {
                                        updatePayoutStateCity();
                                    }
                                    for (int i = 0; i < additionalInfoFields.size(); i++) {
                                        int fieldPosition = getPositionFromId(additionalInfoFields.get(i).getFieldId());
                                        if (additionalInfoFields.get(i).getFieldId().equalsIgnoreCase("53")) {
                                            dynamicEditText[fieldPosition].setText(tempMobileCode);
                                            dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, tempMobileCode);
                                        } else if (additionalInfoFields.get(i).getFieldId().equalsIgnoreCase("43")) {
                                            dynamicEditText[fieldPosition].setText(tempMobileNumber);
                                            dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, tempMobileNumber);
                                        }
                                    }
                                } else {
                                    findViewById(R.id.additionalinfolable).setVisibility(View.GONE);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    multiStateView.setVisibility(View.INVISIBLE);
                                    btnConfirm.setEnabled(true);
                                }
                                if (response.has(Constants.PROMO_ACTIVE) && response.getString(Constants.PROMO_ACTIVE).equalsIgnoreCase("Y")) {
                                    findViewById(R.id.promocode_layout).setVisibility(View.VISIBLE);
                                    if (response.getString(Constants.PROMO_LIST_ACTIVE).equalsIgnoreCase("Y")) {
                                        isPromoListActive = true;
                                    } else {
                                        isPromoListActive = false;
                                    }
                                    fetchPromoCode();
                                } else {
                                    isPromoListActive = false;
                                    findViewById(R.id.promocode_layout).setVisibility(View.GONE);
                                }
                            } else {
                                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                multiStateView.setVisibility(View.INVISIBLE);
                                btnConfirm.setEnabled(true);
                            }
                        } else {

                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case WU_VALIDATE_SEND_MONEY:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if ((response.getInt(Constants.STATUS_CODE) == Constants.WU_RATE_CHARGE_SUCCESS_CODE) && response.getString(Constants.MESSAGE).equalsIgnoreCase("Validation successful")) {
                            onSuccess();
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();

                            String fieldPkId = response.getString(Constants.ID);
                            if (Validation.isValidString(fieldPkId)) {
                                for (int i = 0; i < additionalInfoFields.size(); i++) {
                                    if (additionalInfoFields.get(i).getFieldId().equalsIgnoreCase(fieldPkId)) {
                                        dynamicEditText[i].requestFocus();
                                        setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                        return;
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
/*                if (status == 1 || status == 0) {
                    onSuccess();
                    break;
                }*/
                break;
            case WU_FETCH_STATE:
                try {
                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0 && dataObject.getAdditionalInfoList() != null) {
                        String payoutStateCity = "";
                        for (int i = 0; i < dataObject.getAdditionalInfoList().size(); i++) {
                            if (dataObject.getAdditionalInfoList().get(i).getFieldId().equalsIgnoreCase("75")) {
                                payoutStateCity = dataObject.getAdditionalInfoList().get(i).getFieldValue();
                            }
                        }
                        ArrayList<WuCurrencyData> currencyData = (ArrayList<WuCurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WuCurrencyData>>() {
                        }.getType());
                        if (currencyData != null && currencyData.size() > 0) {
                            if (payoutStateCity != null && payoutStateCity.length() > 0) {
                                for (int i = 0; i < currencyData.size(); i++) {
                                    if (currencyData.get(i).getDisplayKey().equalsIgnoreCase(payoutStateCity)) {
                                        int fieldPosition = getPositionFromId(String.valueOf("75"));
                                        dynamicEditText[fieldPosition].setTag(R.id.VIEW_TAG_CODE_ID, currencyData.get(i).getDisplayKey());
                                        dynamicEditText[fieldPosition].setText(currencyData.get(i).getDisplayValue());
                                        dynamicInputLayout[fieldPosition].setHint(additionalInfoFields.get(fieldPosition).getFieldLable());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void updateMessage(String promoMessage) {
        tvPromoMessage.setText(promoMessage);
    }

    private void createConditionDialog(String message) {
        priorityConditionsDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
        priorityConditionsDialog.setCanceledOnTouchOutside(false);
        priorityConditionsDialog.setContentView(R.layout.wu_terms_dialog);
        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar_title)).setText("TERMS AND CONDITIONS");
        CommonUtils.setLayoutFont(context, "HelveticaNeue-Medium.ttf", findViewById(R.id.toolbar_title));
        CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", findViewById(app.alansari.R.id.dialog_title), findViewById(R.id.accept_text));
        // tvText.setText(Html.fromHtml(context.getString(R.string.indemnity_form_text), null, new IndemnityActivity.UlTagHandler()));


        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.dialog_title)).setText(Html.fromHtml(message));
        ((ImageView) priorityConditionsDialog.findViewById(app.alansari.R.id.close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priorityConditionsDialog.dismiss();
            }
        });
        priorityConditionsDialog.show();
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
        mScrollViewAdditionInfo.post(new Runnable() {
            @Override
            public void run() {
                mScrollViewAdditionInfo.smoothScrollTo(0, btnConfirm.getTop());
            }
        });

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