package app.alansari.modules.accountmanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TransactionModeData;
import app.alansari.network.NetworkStatus;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_TRANSACTION_MODE_IN_COUNTRY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 23 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBeneficiarySelectTypeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    ArrayList<ServiceTypeData> serviceTypeData;
    ArrayList<TransactionModeData> transactionModeData;
    private Intent intent;
    private Toolbar toolbar;
    private Context context;
    private Button btnNext;
    private BeneficiaryData dataObject;
    private CountryData selectedCountry;
    private BankData selectedBank;
    private ServiceTypeData selectedServiceType;
    private TransactionModeData selectedTransactionMode;
    private int transactionModeStatus, serviceTypeStatus;

    private TextView tvCountry;
    private AppCompatImageView ivFlag;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private View transactionModeView, bankNameView, serviceTypeView;
    private TextView tvTransactionModeLabel, tvBankNameLabel, tvServiceTypeLabel;
    private AutoCompleteTextView tvTransactionMode, tvBankName, tvServiceType;
    private NestedScrollView nestedScrollview;
    private TextView tvLabel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {

        nestedScrollview = findViewById(R.id.nested_scrollview);
        ivFlag = (AppCompatImageView) findViewById(app.alansari.R.id.flag);
        tvCountry = (TextView) findViewById(app.alansari.R.id.country_name);

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        transactionModeView = findViewById(app.alansari.R.id.transaction_mode_card_view);
        bankNameView = findViewById(app.alansari.R.id.bank_name_card_view);
        serviceTypeView = findViewById(app.alansari.R.id.service_type_card_view);
        tvTransactionMode = (AutoCompleteTextView) findViewById(app.alansari.R.id.transaction_mode_text);
        tvBankName = (AutoCompleteTextView) findViewById(app.alansari.R.id.bank_name_text);
        tvServiceType = (AutoCompleteTextView) findViewById(app.alansari.R.id.service_type_text);
        tvTransactionModeLabel = (TextView) findViewById(app.alansari.R.id.transaction_mode_label);
        tvBankNameLabel = (TextView) findViewById(app.alansari.R.id.bank_name_label);
        tvServiceTypeLabel = (TextView) findViewById(app.alansari.R.id.service_type_label);
        tvLabel=(TextView)findViewById(app.alansari.R.id.text_view_label);

        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        btnNext.setOnClickListener(this);

        ivFlag.setOnClickListener(this);
        tvCountry.setOnClickListener(this);

        transactionModeView.setOnClickListener(this);
        tvTransactionMode.setOnClickListener(this);
        bankNameView.setOnClickListener(this);
        tvBankName.setOnClickListener(this);
        serviceTypeView.setOnClickListener(this);
        tvServiceType.setOnClickListener(this);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.add_beneficiary_select_type_activity);
        context = this;
        toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, app.alansari.R.color.colorWhite));
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Send Money Beneficiary");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
//        if (getIntent().getExtras() != null) {
//            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
//            if (dataObject != null) {
//            }
//        }
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        setSelectedCountryData();
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

    private void setSelectedCountryData() {
        if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
            multiStateView.setVisibility(View.GONE);
            openCountryList();
        } else {
            CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
            tvCountry.setText("Receiving country (" + selectedCountry.getLatinName() + ")");
            multiStateView.setVisibility(View.VISIBLE);

            tvTransactionMode.setText(null);
            tvServiceType.setText(null);
            selectedTransactionMode = null;
            selectedServiceType = null;
            btnNext.setEnabled(false);
            if(selectedCountry.getBackground()!=null){
                tvLabel.setVisibility(View.VISIBLE);
                tvLabel.setText("** "+selectedCountry.getBackground());
            }else{
                tvLabel.setVisibility(View.GONE);
            }


            //DKG
            tvBankName.setText(null);


            fetchTransactionModes();
            fetchServiceTypes();
        }
    }

    private void openCountryList() {
        Intent intent = new Intent(context, SelectCountryFlagActivity.class);
        intent.putExtra("hideFirstItem", true);
        startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
    }

    private ServiceTypeData getServiceType(String serviceType) {
        for (int i = 0; i < serviceTypeData.size(); i++) {
            if (serviceTypeData.get(i).getMapping().equalsIgnoreCase(serviceType))
                return serviceTypeData.get(i);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchTransactionModes();
                fetchServiceTypes();
                break;
            case app.alansari.R.id.flag:
            case app.alansari.R.id.country_name:
                openCountryList();
                break;
            case app.alansari.R.id.transaction_mode_text:
            case app.alansari.R.id.transaction_mode_card_view:
                if (transactionModeData != null) {
                    intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE);
                    intent.putParcelableArrayListExtra(Constants.LIST, transactionModeData);
                    startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }
                break;
            case app.alansari.R.id.bank_name_text:
            case app.alansari.R.id.bank_name_card_view:
                if (selectedTransactionMode != null) {
                    intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_BANK);
                    intent.putExtra(Constants.COUNTRY_ID, selectedCountry.getId());
                    intent.putExtra(Constants.TYPE, selectedTransactionMode.getMapping());
                    startActivityForResult(intent, Constants.SELECT_ITEM_BANK);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.select_valid_transaction_mode), Toast.LENGTH_SHORT).show();
                }
                break;
            case app.alansari.R.id.service_type_text:
            case app.alansari.R.id.service_type_card_view:
                if (selectedBank != null) {
                    if (serviceTypeData != null) {
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                        ArrayList<ServiceTypeData> serviceTypeUpdated = new ArrayList<>();
                        if (selectedTransactionMode != null) {


                            if (selectedTransactionMode.getMapping().equalsIgnoreCase(Constants.TRANSFER_TYPE_BANK_TRANSFER)) {
                                if (selectedBank.getAREX_BT().equalsIgnoreCase("1"))
                                    serviceTypeUpdated.add(getServiceType(Constants.AREX_MAPPING));
                                if (selectedBank.getCE_BT().equalsIgnoreCase("1"))
                                    serviceTypeUpdated.add(getServiceType(Constants.CE_MAPPING));


                            } else if (selectedTransactionMode.getMapping().equalsIgnoreCase(Constants.TRANSFER_TYPE_CASH_PICK_UP)) { // If selected this only instant is comming...
                                if (selectedBank.getAREX_CP().equalsIgnoreCase("1"))
                                    serviceTypeUpdated.add(getServiceType(Constants.AREX_MAPPING));
                                if (selectedBank.getCE_CP().equalsIgnoreCase("1"))
                                    serviceTypeUpdated.add(getServiceType(Constants.CE_MAPPING));
                            }
                        }
                        intent.putParcelableArrayListExtra(Constants.LIST, serviceTypeUpdated);
                        startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.select_valid_bank), Toast.LENGTH_SHORT).show();
                }
                break;
            case app.alansari.R.id.next_btn:
                checkAllFields();
                if (btnNext.isEnabled()) {
                    if (selectedServiceType.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) || selectedBank.getBeneficiaryType() == 0) {
                        goToBeneficiaryScreen(1);
                    } else {
                        showSelectBeneficiaryDialog();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_mandatory_fields), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void goToBeneficiaryScreen(int beneficiaryType) {
        dataObject = new BeneficiaryData();
        dataObject.setTransferType(selectedTransactionMode.getMapping());
        dataObject.setBankData(selectedBank);
        dataObject.setServiceTypeData(selectedServiceType);
        dataObject.setCountryData(selectedCountry);
        dataObject.setBeneficiaryType(beneficiaryType);

        if (selectedServiceType.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING))
            intent = new Intent(context, AddBeneficiaryActivity.class);
        else
            intent = new Intent(context, AddBeneficiaryCeActivity.class);

        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
//        startActivity(intent);
        startActivityForResult(intent, Constants.ADD_NEW_BENEFICIARY);
    }

    void fetchTransactionModes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //TODO Update 26 ->
            //dataObject.getBankData().getId();
//            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchTransactionModes((String) SharedPreferenceManger.getPrefVal(context, Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), dataObject.getCountryData().getId(), "26", dataObject.getServiceTypeData().getId(), dataObject.getTransferType()), Constants.FETCH_ADDITIONAL_INFO_URL, CommonUtils.SERVICE_TYPE.FETCH_ADDITIONAL_INFO, Request.Method.POST, this);
            //JsonObjectRequest jsonObjReq = new CallAddr().executeApi(null, Constants.FETCH_TRANSACTION_MODE_IN_COUNTRY_URL, FETCH_TRANSACTION_MODE_IN_COUNTRY, Request.Method.POST, this);
            //AppController.getInstance().getRequestQueue().cancelAll(FETCH_TRANSACTION_MODE_IN_COUNTRY.toString());
            //AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_TRANSACTION_MODE_IN_COUNTRY.toString());
            onResponse(0, null, FETCH_TRANSACTION_MODE_IN_COUNTRY);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    void fetchServiceTypes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            //JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceTypes((String) SharedPreferenceManger.getPrefVal(context, Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING)), Constants.FETCH_SERVICE_TYPE_IN_COUNTRY_URL, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY, Request.Method.POST, this);
            //AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY.toString());
            //AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPE_IN_COUNTRY.toString());
            onResponse(0, null, FETCH_SERVICE_TYPE_IN_COUNTRY);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    setSelectedCountryData();
                } else {
                    if (selectedCountry == null || selectedCountry.getLatinName().trim().equalsIgnoreCase("ALL")) {
                        onBackPressed();
                        Toast.makeText(context, "Incomplete data", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constants.SELECT_ITEM_ADD_BENEFICIARY_TRANSACTION_MODE && resultCode == Activity.RESULT_OK) {
                nestedScrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollview.smoothScrollTo(0, serviceTypeView.getTop());
                    }
                });
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    selectedTransactionMode = data.getParcelableExtra(Constants.OBJECT);
                    tvTransactionMode.setText(selectedTransactionMode.getName());
                    tvTransactionModeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
                    resetBankData();
                    resetServiceType();
                    checkAllFields();
                }
            } else if (requestCode == Constants.SELECT_ITEM_BANK && resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    selectedBank = data.getParcelableExtra(Constants.OBJECT);
                    tvBankName.setText(selectedBank.getBankName());
                    tvBankNameLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
                    resetServiceType();
                    checkAllFields();
                }
            } else if (requestCode == Constants.SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE && resultCode == Activity.RESULT_OK) {
                if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    selectedServiceType = data.getParcelableExtra(Constants.OBJECT);
                    tvServiceType.setText(selectedServiceType.getName());
                    tvServiceTypeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color1E6AB3));
                    checkAllFields();
                }
            } else if (requestCode == Constants.ADD_NEW_BENEFICIARY) {
                if (resultCode == Activity.RESULT_OK) {
                    data.putExtra(Constants.OBJECT, data.getParcelableExtra(Constants.OBJECT));
                    setResult(AppCompatActivity.RESULT_OK, data);
                } else {
                    setResult(AppCompatActivity.RESULT_CANCELED, data);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetBankData() {
        selectedBank = null;
        tvBankName.setText("Beneficiary Bank / Agents");
        tvBankNameLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
    }

    private void resetServiceType() {
        selectedServiceType = null;
        tvServiceType.setText("Service Type");
        tvServiceTypeLabel.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
    }

    private void checkAllFields() {
        if (selectedTransactionMode != null && selectedBank != null && selectedServiceType != null) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    private void setViewState(int viewState) {
        if (transactionModeStatus == -1 || serviceTypeStatus == -1) {
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_TRANSACTION_MODE_IN_COUNTRY:
                if (status == 0) {
                    status = 1;
                    response = fetchTransactionModesResponse();
                }
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                transactionModeData = (ArrayList<TransactionModeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TransactionModeData>>() {
                                }.getType());
                                if (transactionModeData != null && transactionModeData.size() > 0) {
                                    transactionModeStatus = 1;
                                    if (serviceTypeStatus == 1) {
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    }
                                } else {
                                    transactionModeStatus = -1;
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            } else {
                                setViewState(VIEW_STATE_EMPTY);
                            }
                        } else {
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(VIEW_STATE_WRONG);
                    }
                } else {
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
            case FETCH_SERVICE_TYPE_IN_COUNTRY:
                if (status == 0) {
                    status = 1;
                    response = fetchServiceTypeResponse();
                }
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                serviceTypeData = (ArrayList<ServiceTypeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ServiceTypeData>>() {
                                }.getType());
                                if (serviceTypeData != null && serviceTypeData.size() > 0) {
                                    serviceTypeStatus = 1;
                                    if (transactionModeStatus == 1) {
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    }
                                } else {
                                    serviceTypeStatus = -1;
                                    setViewState(VIEW_STATE_EMPTY);
                                }
                            } else {
                                serviceTypeStatus = -1;
                                setViewState(VIEW_STATE_EMPTY);
                            }
                        } else {
                            serviceTypeStatus = -1;
                            setViewState(VIEW_STATE_WRONG);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        serviceTypeStatus = -1;
                        setViewState(VIEW_STATE_WRONG);
                    }
                } else {
                    serviceTypeStatus = -1;
                    setViewState(VIEW_STATE_WRONG);
                }
                break;
        }
    }

    private JSONObject fetchTransactionModesResponse() {
        try {
            return new JSONObject("{\n" +
                    "  \"STATUS_CODE\": 257,\n" +
                    "  \"MESSAGE\": \"SUCCESS\",\n" +
                    "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "  \"RESULT\": [\n" +
                    "    {\n" +
                    "      \"SERVICE_MAPPING\": \"BT\",\n" +
                    "      \"STATUS\": \"1\",\n" +
                    "      \"SERVICE_PK_ID\": 2,\n" +
                    "      \"SERVICE_NAME\": \"Bank Transfer\",\n" +
                    "      \"REMIT_TIME\": \"1 Day\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"SERVICE_MAPPING\": \"CP\",\n" +
                    "      \"STATUS\": \"1\",\n" +
                    "      \"SERVICE_PK_ID\": 1,\n" +
                    "      \"SERVICE_NAME\": \"Cash Pick up\",\n" +
                    "      \"REMIT_TIME\": \"1-3 Days\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject fetchServiceTypeResponse() {
        try {
            return new JSONObject("{\n" +
                    "    \"RESULT\": [\n" +
                    "        {\n" +
                    "            \"STATUS\": \"1\",\n" +
                    "            \"SERVICE_PK_ID\": 1,\n" +
                    "            \"SERVICE_NAME\": \"Value Transfer\",\n" +
                    "            \"SERVICE_MAPPING\": \"AREX\",\n" +
                    "            \"REMIT_TIME\": \"1-3 Days\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"STATUS\": \"1\",\n" +
                    "            \"SERVICE_PK_ID\": 2,\n" +
                    "            \"SERVICE_NAME\": \"Instant Transfer\",\n" +
                    "            \"SERVICE_MAPPING\": \"CE\",\n" +
                    "            \"REMIT_TIME\": \"1 Day\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"AMOUNT\": null,\n" +
                    "    \"MESSAGE\": \"SUCCESS\",\n" +
                    "    \"OTP\": null,\n" +
                    "    \"USER_STATUS\": null,\n" +
                    "    \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "    \"STATUS_CODE\": 257\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showSelectBeneficiaryDialog() {
        String[] beneficiaryTypes = getResources().getStringArray(app.alansari.R.array.beneficiary_type_ce);
        if (beneficiaryTypes == null) {
            return;
        }
        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(app.alansari.R.layout.select_beneficiary_type_dialog);
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Select Beneficiary Type");
        LinearLayout dynamicLayout = (LinearLayout) myDialog.findViewById(app.alansari.R.id.dynamic_layout);
        dynamicLayout.removeAllViews();
        for (int i = 0; i < beneficiaryTypes.length; i++) {
            View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.item_beneficiary_type, null);
            TextView title = (TextView) childLayout.findViewById(app.alansari.R.id.title);
            title.setText(beneficiaryTypes[i]);
            childLayout.setTag(i + 1);
            if (i == beneficiaryTypes.length - 1) {
                childLayout.findViewById(app.alansari.R.id.divider).setVisibility(View.GONE);
            }
            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null) {
                        goToBeneficiaryScreen(Integer.valueOf(v.getTag().toString()));
                    }
                    myDialog.dismiss();
                }
            });
            dynamicLayout.addView(childLayout);
        }

        myDialog.findViewById(app.alansari.R.id.dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
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
