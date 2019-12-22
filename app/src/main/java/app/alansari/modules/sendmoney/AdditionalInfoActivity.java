package app.alansari.modules.sendmoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
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
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.adapters.PromoCodeAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AdditionalInfoData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.PromoCodeData;
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
public class AdditionalInfoActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {


    private Intent intent;
    private Button btnConfirm;
    private Context context;

    private LinearLayout dynamicLayout;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private BeneficiaryData dataObject;
    private TextView tvName, tvBankName, tvBranchName, tvAccountNumber;
    private TextView tvTotalReceive, tvTotalReceiveCurrencyCode, tvTotalToPay, tvTotalToPayCurrencyCode;
    private RelativeLayout promocodeLayout;
    private ScrollView scrollView;

    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<AdditionalInfoData> additionalInfoFields;
    private String fieldId, errorMessage, selectedDOB, selectedDateOfBirth;

    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private long timeStamp;
    private boolean isAutoFill, isPromoListActive;
    private Dialog promoCodeDialog;
    private EditText etPromoCode;
    private RecyclerView promoRecyclerView;
    private TextView tvPromoMessage;
    private Button btnSave;
    private PromoCodeAdapter promoCodeAdapter;
    private ImageView ivChecked;
    private boolean isAgreed;
    private Dialog priorityConditionsDialog;

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
        if (validated && !isAutoFill) {
            inputLayout.setError(((AdditionalInfoData) editText.getTag()).getErrorMessage());
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

        for (int i = 0; dynamicEditText != null && i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getFieldType() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((AdditionalInfoData) dynamicEditText[i].getTag()).getIsMandatory().equalsIgnoreCase("0")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
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
        setContentView(app.alansari.R.layout.additional_info_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Additional Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        init();
        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            TOTAL_AMOUNT_PP = getIntent().getStringExtra(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = getIntent().getStringExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
            if (dataObject != null) {
                setInitialData();
                fetchAdditionalInfo();
                return;
            }
        }
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        finish();
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

        tvTotalReceive = (TextView) findViewById(app.alansari.R.id.total_receive);
        tvTotalReceiveCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_receive_currency_code);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);

        dynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.dynamic_layout);
        btnConfirm = (Button) findViewById(app.alansari.R.id.confirm_btn);
        promocodeLayout = findViewById(app.alansari.R.id.promocode_layout);
        scrollView = findViewById(app.alansari.R.id.scroll_view);
        ivChecked = (ImageView) findViewById(R.id.accept_image);
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
            tvBankName.setText(dataObject.getBankData().getBankName());
            tvBranchName.setText(dataObject.getBranchName());


            if (dataObject.getTransferType() != null && dataObject.getTransferType().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) {
                ((TextView) findViewById(app.alansari.R.id.account_number_label)).setText(getString(app.alansari.R.string.mobile_number));
                tvAccountNumber.setText(dataObject.getMobile());
            } else {
                tvAccountNumber.setText(dataObject.getAccountNumber());
            }

            String recAmt = CommonUtils.addCommaToString(dataObject.getTxnAmountData().getYouGet());
            if (recAmt.equalsIgnoreCase("")) {
                tvTotalReceive.setText(dataObject.getTxnAmountData().getYouGet());
            } else {
                tvTotalReceive.setText(recAmt);
            }

//            tvTotalReceive.setText(CommonUtils.addCommaToString(dataObject.getTxnAmountData().getYouGet()));
            tvTotalReceiveCurrencyCode.setText(dataObject.getTxnAmountData().getYouGetCurrencyData().getName());
            tvTotalToPay.setText(CommonUtils.addCommaToString(dataObject.getTxnAmountData().getTotalToPay()));
            tvTotalToPayCurrencyCode.setText(dataObject.getTxnAmountData().getYouSendCurrencyData().getName());
        }
    }

    void fetchAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //TODO Update 26 ->
            //dataObject.getBankData().getId();
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAdditionalInfo((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject.getCountryData().getId(), dataObject.getBankData().getId(), dataObject.getServiceTypeData().getId(), dataObject.getTransferType(), CommonUtils.getMemPkId(dataObject.getServiceTypeData().getMapping()), dataObject.getBeneficiaryId(), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                fetchAdditionalInfo();
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (v.getTag() != null) {
                    AdditionalInfoData currentView = ((AdditionalInfoData) v.getTag());
                    intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
                    intent.putExtra(Constants.ID, Integer.valueOf(currentView.getId()));
                    intent.putExtra(Constants.TITLE, currentView.getTitle());
                    intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<AdditionalInfoData.ValuesData>) currentView.getValuesData());
                    startActivityForResult(intent, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }
                break;
            case app.alansari.R.id.confirm_btn:
                if (btnConfirm.isEnabled()) {
                    addAdditionalInfo();
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
            case app.alansari.R.id.promocode_layout:
                promoCodeDialog.show();
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
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
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

    private void fetchPromoCode() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchPromoCode((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.PROMO_CODE_URL, CommonUtils.SERVICE_TYPE.PROMO_CODE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.PROMO_CODE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
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
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(app.alansari.R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(app.alansari.R.id.edit_text);

                updateDynamicField(editText, inputLayout, i == (additionalInfoFields.size() - 1) ? childLayout.findViewById(app.alansari.R.id.divider) : null, additionalInfoFields.get(i), null);
                if (i == additionalInfoFields.size() - 1)
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

    private void updateDynamicField(EditText editText, TextInputLayout inputLayout, View divider, AdditionalInfoData currentView, String errorMessage) {
        if (currentView != null) {
            if (errorMessage == null)
                currentView.setErrorMessage("Invalid " + currentView.getTitle().toLowerCase());
            else
                currentView.setErrorMessage(errorMessage);
            editText.setTag(currentView);
            inputLayout.setTag(currentView);
        } else
            return;

        setMandatory(editText, inputLayout, currentView.getTitle() + (currentView.getIsMandatory().equalsIgnoreCase("1") ? "*" : ""));
        setFieldType(editText, inputLayout, currentView);
    }

    private void setMandatory(EditText editText, TextInputLayout inputLayout, String hintText) {
        editText.setHintTextColor(ContextCompat.getColor(context, app.alansari.R.color.color9B9B9B));
        CommonUtils.setInputTextLayoutColor(context, inputLayout);
        editText.setHint(hintText);
        inputLayout.setHint(null);
    }

    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final AdditionalInfoData currentView) {
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

    private void setError(TextInputLayout inputLayout, String msg) {
        inputLayout.setError(msg);
        inputLayout.setErrorEnabled(true);
        btnConfirm.setEnabled(false);
    }

    private void selectFromDropdown(int titleId, ArrayList<AdditionalInfoData.ValuesData> list) {
        intent = new Intent(context, SelectItemActivity.class);
        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
        intent.putExtra(Constants.ID, titleId);
        intent.putParcelableArrayListExtra(Constants.LIST, list);
        startActivityForResult(intent, Constants.SELECT_ITEM_ADDITIONAL_FIELD);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_ADDITIONAL_FIELD && resultCode == Activity.RESULT_OK && data.getExtras().getInt(Constants.ID, -1) != -1 && data.getParcelableExtra(Constants.OBJECT) != null) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, promocodeLayout.getTop());
                    }
                });
                int fieldPosition = getPositionFromId(String.valueOf(data.getExtras().getInt(Constants.ID, -1)));
                dynamicEditText[fieldPosition].setText(((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getTitle());
                dynamicEditText[fieldPosition].setTag(app.alansari.R.id.VIEW_TAG_CODE_ID, ((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getId());
                dynamicInputLayout[fieldPosition].setHint(additionalInfoFields.get(fieldPosition).getTitle());
                //Log.e("jbfchsvhv",""+(((AdditionalInfoData.ValuesData) data.getParcelableExtra(Constants.OBJECT)).getTitle()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPositionFromId(String id) {
        for (int i = 0; i < additionalInfoFields.size(); i++) {
            if (id.equalsIgnoreCase(additionalInfoFields.get(i).getId()))
                return i;
        }
        return -1;
    }

    private void onSuccess() {
        dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));
        intent = new Intent(context, PaymentModeActivity.class);
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
        intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
        intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
        startActivity(intent);
    }
    void addAdditionalInfo() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(addAdditionalInfoJsonFormat(), Constants.ADD_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.ADD_ADDITIONAL_INFO.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void setError(TextView editText, TextView textView, String msg) {
        editText.setError(msg);
        textView.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
        btnConfirm.setEnabled(false);
    }    private String checkValue(String value) {
        return value != null ? value : "";
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
            case FETCH_ADDITIONAL_INFO:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                additionalInfoFields = (ArrayList<AdditionalInfoData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AdditionalInfoData>>() {
                                }.getType());
                                if (additionalInfoFields != null && additionalInfoFields.size() > 0) {
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
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));

                        } else {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));
                            //CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
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
                                for (int i = 0; i < additionalInfoFields.size(); i++) {
                                    if (additionalInfoFields.get(i).getId().equalsIgnoreCase(fieldPkId)) {
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
            if (additionalInfoFields != null) {
                for (int i = 0; i < additionalInfoFields.size(); i++) {
                    try {
                        JSONObject jsonObjectField = new JSONObject();
                        jsonObjectField.put(Constants.FIELD_PK_ID, additionalInfoFields.get(i).getId());
                        jsonObjectField.put(Constants.FIELD_VALUE, checkValue(dynamicEditText[i].getText().toString().trim()));
                        if (dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                            jsonObjectField.put(Constants.FIELD_VALUE_CODE, checkValue((dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null) ? dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString() : null));
                        jsonArray.put(jsonObjectField);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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

    public void updateMessage(String promoMessage) {
        tvPromoMessage.setText(promoMessage);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }
















}