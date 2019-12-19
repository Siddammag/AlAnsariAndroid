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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.adapters.PromoCodeAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.PromoCodeData;
import app.alansari.modules.accountmanagement.AddBeneficiaryCeDropdownActivity;
import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFieldsCe;
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
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 24 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AdditionalInfoCeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {


    private Intent intent;
    private Button btnConfirm;
    private Context context;
    private LinearLayout dynamicLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private BeneficiaryData dataObject, beneficiaryData;
    private TextView tvName, tvBankName, tvBranchName, tvAccountNumber;
    private TextView tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;
    private String purposeId;
    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<BeneficiaryDynamicFieldsCe> beneficiaryDynamicFieldsCeList;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private long timeStamp;

    private boolean isPromoListActive;
    private Dialog promoCodeDialog;
    private EditText etPromoCode;
    private RecyclerView promoRecyclerView;
    private TextView tvPromoMessage;
    private Button btnSave;
    private PromoCodeAdapter promoCodeAdapter;
    private ImageView ivChecked;
    private boolean isAgreed;
    private Dialog priorityConditionsDialog;
    private ScrollView scrollView;
    private RelativeLayout promocodeLayout;
    private String sessionTime;

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

    }    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (validated) {
            inputLayout.setError(((BeneficiaryDynamicFieldsCe) editText.getTag()).getErrorMessage());
            inputLayout.setErrorEnabled(true);
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
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
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditText[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase("T")))
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

        if (specialCharacterValidation && layoutStatus && isAgreed)
            btnConfirm.setEnabled(true);
        else
            btnConfirm.setEnabled(false);

        return layoutStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.additional_info_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Additional Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();
        if (getIntent() != null && getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
            if (dataObject != null) {
                setInitialData();
                fetchAdditionalInfoCe();
                return;
            }
        }
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        tvName = (TextView) findViewById(app.alansari.R.id.name);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name);
        tvBranchName = (TextView) findViewById(app.alansari.R.id.branch_name);
        tvAccountNumber = (TextView) findViewById(app.alansari.R.id.account_number);

        tvTotalReceive = (TextView) findViewById(app.alansari.R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);

        dynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.dynamic_layout);

        btnConfirm = (Button) findViewById(app.alansari.R.id.confirm_btn);
        ivChecked = (ImageView) findViewById(R.id.accept_image);
        scrollView = findViewById(app.alansari.R.id.scroll_view);
        promocodeLayout = findViewById(app.alansari.R.id.promocode_layout);
        ivChecked.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.accept_text).setOnClickListener(this);

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

        etPromoCode = (EditText) findViewById(app.alansari.R.id.promocode);
        promoCodeDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        promoCodeDialog.setCanceledOnTouchOutside(false);
        promoCodeDialog.setContentView(R.layout.promo_code_dialog);
        promoCodeAdapter = new PromoCodeAdapter(context);
        promoRecyclerView = (RecyclerView) promoCodeDialog.findViewById(app.alansari.R.id.recycler_view);
        tvPromoMessage = (TextView) promoCodeDialog.findViewById(app.alansari.R.id.message);
        btnSave = (Button) promoCodeDialog.findViewById(app.alansari.R.id.save);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        promoRecyclerView.setLayoutManager(mLayoutManager);
        promoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        promoRecyclerView.setAdapter(promoCodeAdapter);

        promoCodeDialog.findViewById(app.alansari.R.id.close_btn).setOnClickListener(new View.OnClickListener() {
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
    }

    private void setInitialData() {
        if (dataObject != null) {
            if (Validation.isValidString(dataObject.getName())) {
                tvName.setText(dataObject.getName());
            } else {
                tvName.setText(dataObject.getArabicName());
            }
            if (dataObject.getBankData() != null && dataObject.getBankData().getBankName() != null) {
                tvBankName.setText(dataObject.getBankData().getBankName());
            } else {
                tvBankName.setText("NA");
            }
            tvBranchName.setText(dataObject.getBranchName());

            if (dataObject.getTransferType() != null && dataObject.getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                tvAccountNumber.setText(dataObject.getMobile());
            } else {
                tvAccountNumber.setText(dataObject.getAccountNumber());
            }

            tvTotalReceive.setText(dataObject.getTxnAmountData().getYouGet());
            tvTotalReceiveCurrencyCode.setText(dataObject.getTxnAmountData().getYouGetCurrencyData().getName());
            tvTotalToPay.setText(dataObject.getTxnAmountData().getTotalToPay());
            tvTotalToPayCurrencyCode.setText(dataObject.getTxnAmountData().getYouSendCurrencyData().getName());
        }
    }

    void fetchAdditionalInfoCe() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            //JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAdditionalInfoCe(CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping()), dataObject.getTransferType(), dataObject.getTxnAmountData().getTotalToPay(), dataObject.getCountryData().getCountryCodeCE(), dataObject.getBankData().getBankCodeCE(), dataObject.getBeneficiaryId(), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ADDITIONAL_INFO_CE_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO_CE, Request.Method.POST, this);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAdditionalInfoCe(CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping()), dataObject.getTransferType(), dataObject.getTxnAmountData().getYouSend(), dataObject.getCountryData().getCountryCodeCE(), dataObject.getBankData().getBankCodeCE(), dataObject.getBeneficiaryId(), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ADDITIONAL_INFO_CE_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO_CE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO_CE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO_CE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                fetchAdditionalInfoCe();
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (v.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFieldsCe) v.getTag()).getId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_7)) || fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_50))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_NATIONALITY);
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCe) v.getTag()).getId()));
                    } else if (((BeneficiaryDynamicFieldsCe) v.getTag()).getId().equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_38))) {
                        break;
                    } else {
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_28))) {
                            int position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_9));
                            if (dynamicEditText[position].getText() == null || dynamicEditText[position].getText().toString().length() == 0) {
                                Toast.makeText(context, "Please Select Purpose of Transaction", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        intent = new Intent(context, AddBeneficiaryCeDropdownActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, ((BeneficiaryDynamicFieldsCe) v.getTag()).getId());
                        intent.putExtra(Constants.TITLE, ((BeneficiaryDynamicFieldsCe) v.getTag()).getFieldName());
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_44))) {
                            intent.putExtra(Constants.BANK_CODE, dataObject.getBankData().getBankCodeCE());
                            intent.putExtra(Constants.TRANSFER_TYPE, dataObject.getTransferType());
                        }
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_28))) {
                            intent.putExtra(Constants.PURPOSE_ID, purposeId);
                        }
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCe) v.getTag()).getId()));
                    }
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }
                break;
            case app.alansari.R.id.confirm_btn:
                if (btnConfirm.isEnabled()) {
                    saveAdditionalInfoCe();
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.accept_text:
                createConditionDialog();
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

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }    private void createConditionDialog() {
        priorityConditionsDialog = new Dialog(this, app.alansari.R.style.CustomDialogThemeLightBg);
        priorityConditionsDialog.setCanceledOnTouchOutside(false);
        priorityConditionsDialog.setContentView(R.layout.wu_terms_dialog);
        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.toolbar_title)).setText("TERMS AND CONDITIONS");
        CommonUtils.setLayoutFont(context, "HelveticaNeue-Medium.ttf", findViewById(R.id.toolbar_title));
        CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", findViewById(app.alansari.R.id.dialog_title), findViewById(R.id.accept_text));
        // tvText.setText(Html.fromHtml(context.getString(R.string.indemnity_form_text), null, new IndemnityActivity.UlTagHandler()));


        ((TextView) priorityConditionsDialog.findViewById(app.alansari.R.id.dialog_title)).setText(Html.fromHtml(getString(R.string.indemnity_form_text)));
        ((ImageView) priorityConditionsDialog.findViewById(app.alansari.R.id.close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priorityConditionsDialog.dismiss();
            }
        });
        priorityConditionsDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
    }    void saveAdditionalInfoCe() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalInfoJsonFormat(), Constants.ADD_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDynamicFields() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        if (!Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
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
                //editText.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                //inputLayout.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());

                editText.setHintTextColor(ContextCompat.getColor(context, app.alansari.R.color.color9B9B9B));
                CommonUtils.setInputTextLayoutColor(context, inputLayout);
                editText.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                inputLayout.setHint(null);

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
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
        }
    }

    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final BeneficiaryDynamicFieldsCe currentView) {
        if (currentView.getFieldType().equalsIgnoreCase("D")) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getFieldType().equalsIgnoreCase("T")) {
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

    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnConfirm.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    switch (requestCode) {
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_7:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_50:
                            dataObject.setNationality(((CountryData) object).getLatinName());
                            dataObject.setNationalityId(((CountryData) object).getCountryCodeCE());
                            if (position != -1)
                                setDataToEditText(position, ((CountryData) object).getLatinName(), ((CountryData) object).getCountryCodeCE());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_8:
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.smoothScrollTo(0, promocodeLayout.getTop());
                                }
                            });
                            if (position != -1)
                                setDataToEditText(position, ((FundsSourceCeData) object).getName(), ((FundsSourceCeData) object).getId());
                            break;
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_9:
                            if (position != -1) {
                                int subPosition = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_28));
                                if (subPosition != -1) {
                                    dynamicEditText[subPosition].setText("");
                                    dynamicEditText[subPosition].setHint(beneficiaryDynamicFieldsCeList.get(subPosition).getFieldName());
                                    dynamicInputLayout[subPosition].setHint(null);
                                }
                                purposeId = String.valueOf(((PurposeCeData) object).getId());
                                setDataToEditText(position, ((PurposeCeData) object).getName(), ((PurposeCeData) object).getId());
                            }
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
                    if (position != -1) {
                        dynamicInputLayout[position].setHint(beneficiaryDynamicFieldsCeList.get(position).getFieldName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    private int getEditTextLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    private void setDataToEditText(int position, String text, String code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }    private int getEditTextMinLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getMinLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    private void setDataToEditText(int position, String text, int code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, code);
    }

    private void fetchPromoCode() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.PROMO_CODE_URL, CommonUtils.SERVICE_TYPE.PROMO_CODE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void onSuccess() {
        dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));
        intent = new Intent(context, PaymentModeActivity.class);
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        startActivity(intent);
    }    private int getPositionFromId(String id) {
        for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
            if (id.equalsIgnoreCase(beneficiaryDynamicFieldsCeList.get(i).getId()))
                return i;
        }
        return -1;
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
            case FETCH_ADDITIONAL_INFO_CE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                beneficiaryDynamicFieldsCeList = (ArrayList<BeneficiaryDynamicFieldsCe>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryDynamicFieldsCe>>() {
                                }.getType());
                                if (Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
                                    findViewById(app.alansari.R.id.additionalinfolable).setVisibility(View.VISIBLE);
                                    updateDynamicFields();
                                } else {
                                    findViewById(app.alansari.R.id.additionalinfolable).setVisibility(View.GONE);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    multiStateView.setVisibility(View.INVISIBLE);
                                    btnConfirm.setEnabled(true);
                                }
                                if (response.has(Constants.PROMO_ACTIVE) && response.getString(Constants.PROMO_ACTIVE).equalsIgnoreCase("Y")) {
                                    findViewById(app.alansari.R.id.promocode_layout).setVisibility(View.VISIBLE);
                                    if (response.getString(Constants.PROMO_LIST_ACTIVE).equalsIgnoreCase("Y")) {
                                        isPromoListActive = true;
                                    } else {
                                        isPromoListActive = false;
                                    }
                                    fetchPromoCode();
                                } else {
                                    isPromoListActive = false;
                                    findViewById(app.alansari.R.id.promocode_layout).setVisibility(View.GONE);
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
            case ADD_ADDITIONAL_INFO:
                try {
                    CommonUtils.hideLoading();
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            onSuccess();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_LONG).show();
                            String fieldPkId = response.getString(Constants.ID);
                            if (Validation.isValidString(fieldPkId)) {
                                for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                                    if (beneficiaryDynamicFieldsCeList.get(i).getId().equalsIgnoreCase(fieldPkId)) {
                                        dynamicEditText[i].requestFocus();
                                        setError(dynamicInputLayout[i], response.getString(Constants.MESSAGE));
                                        return;
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
/*                if (status == 1 || status == 0) {
                    onSuccess();
                    break;
                }*/
                break;
        }
    }

    public void updateMessage(String promoMessage) {
        tvPromoMessage.setText(promoMessage);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private JSONObject addAdditionalInfoJsonFormat() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            dataObject.setPromoCode(etPromoCode.getText().toString());
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.MEM_PK_ID, checkValue(CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping())));
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_ID, checkValue(dataObject.getCountryData().getId()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue((dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE())));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.TOTAL_VALUE_AED, checkValue(dataObject.getTxnAmountData().getYouSend()));
            jsonObject.put(Constants.CHARGE_AMOUNT, checkValue(dataObject.getTxnAmountData().getFee()));
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getTotalToPay()));
            jsonObject.put(Constants.RATE, checkValue(dataObject.getTxnAmountData().getRate()));
            jsonObject.put(Constants.TXN_AMOUNT, checkValue(dataObject.getTxnAmountData().getYouGet()));
            jsonObject.put(Constants.CCY_ID, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_CODE, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getCurrencyCode()));
            jsonObject.put(Constants.CCY_DESC, checkValue(dataObject.getTxnAmountData().getYouGetCurrencyData().getName()));
            jsonObject.put(Constants.BANK_ID, checkValue(dataObject.getBankData().getId()));

            jsonObject.put(Constants.VAT_CHARGES, checkValue(dataObject.getVat()));
            jsonObject.put(Constants.VAT_DISCOUNT, checkValue(dataObject.getDiscount()));
            jsonObject.put(Constants.VAT_ROUNDINGOFF, checkValue(dataObject.getRoundoff()));

            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);

            timeStamp = System.currentTimeMillis();
            jsonObject.put(Constants.TIME_STAMP, timeStamp);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            //jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue("1"));
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                try {
                    JSONObject jsonObjectField = new JSONObject();
                    jsonObjectField.put(Constants.FIELD_PK_ID, beneficiaryDynamicFieldsCeList.get(i).getId());
                    jsonObjectField.put(Constants.FIELD_VALUE, checkValue(dynamicEditText[i].getText().toString().trim()));
                    if (dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                        jsonObjectField.put(Constants.FIELD_VALUE_CODE, checkValue((dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null) ? dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString() : null));
                    jsonArray.put(jsonObjectField);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            jsonObject.put(Constants.RESULT, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "saveAdditionalInfoCe :-  " + jsonObject.toString());
        return jsonObject;
    }




















}