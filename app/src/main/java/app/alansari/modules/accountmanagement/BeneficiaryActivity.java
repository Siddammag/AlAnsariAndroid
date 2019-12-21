package app.alansari.modules.accountmanagement;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.alansari.AdditionalInfoCESelectModeActivity;
import app.alansari.AdditionalInfoSelectModeActivity;
import app.alansari.AppController;
import app.alansari.BuildConfig;
import app.alansari.PaymentSelectModeActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.customviews.flatbutton.ButtonFlat;
import app.alansari.customviews.progressbar.CircleProgressBar;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.CountryData;
import app.alansari.models.ServiceTypeData;
import app.alansari.models.TxnAmountData;
import app.alansari.models.missingparameter.BeneficiaryDynamicFieldsCeNew;
import app.alansari.models.servicetype.RESULTDTO;
import app.alansari.modules.accountmanagement.adapters.BeneficiaryRecyclerAdapter;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.modules.sendmoney.adapters.SendMoneyCurrencyCodeRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.preferences.SharedPreferenceManger.VALUE_TYPE;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_INSTANT;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CHARGES_SENDMONEY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOAD_SERVICE_TYPE_LIST;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.UPDATE_MISSING_FIELD_BENEFICIARY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA;

/**
 * Created by Parveen Dala on 13 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BeneficiaryActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, CustomClickListener, LogOutTimerUtil.LogOutListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ArrayList<ServiceTypeData> serviceTypeDatas;
    private Gson gson;
    private Context context;
    private String startCountCe = "1";
    private String startCountArex = "1";
    private Toolbar toolbar;
    private boolean isCountryRunning;
    private CountryData selectedCountry;
    private String source, userId, arexUserId, ceUserId, AREXMapping, CEMapping, intentType = Constants.ACTIVITY_NO_RESULT;

    // MultiState View
    private TextView tvEmpty, tvError, tvEmptyValue, tvErrorValue;
    private MultiStateView multiStateView, multiStateViewValue;

    //Recycler View
    private View btnArexArrow, btnArexLeftArrow;
    private CarouselLayoutManager layoutManagerValue;
    private RecyclerView recyclerViewValue;
    private BeneficiaryRecyclerAdapter recyclerAdapterValue;

    private AppCompatImageView ivFlag;
    private TextView tvCountry;
    private RelativeLayout layoutWesternUnion;
    private NestedScrollView nestedScrollview;

    private Dialog imagePickDialog;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private String base64 = "";
    private BeneficiaryData uploadBen = null;
    private ButtonFlat valueEmptyButton;
    private String sessionTime;
    private RelativeLayout layoutMessage, layoutSendMoney, layoutpayment, layoutService;
    //-----------------------New-------------------------
    private int currentDirection = 1;
    private boolean isCalculatingCurrency = false;
    private BeneficiaryData mSelectDetails;


    private View view, fragmentDataCoverLayout;
    private AppCompatImageView ivSuccess;
    private EditText etSend, etGet;
    //private BankData bankData;
    private CountryData countryData;
    private ServiceTypeData serviceTypeData;
    private CircleProgressBar progressBar;
    private CountryData.CurrencyData selectedCurrencyData;
    private String totalToPay, charge, rate;
    private String transferType;
    private TextView tvSendCode, tvGetCode, tvRate, tvTimeMessage, tvMessageBottom;
    private TextWatcher textWatcher;

    // Currency Code Scroll
    private CarouselLayoutManager layoutManagerCurrencyCode;
    private RecyclerView recyclerViewCurrencyCode;
    private SendMoneyCurrencyCodeRecyclerAdapter recyclerAdapterCurrencyCode;
    int temp = 0;
    int check = 0;
    int temp2 = 0;
    int check2 = 0;
    private String serviceType = "";
    private AutoCompleteTextView tvPaymentType, tvServiceType;
    private LinearLayout layoutBottom;
    private TextView tvTotalToPay, tvTotalToPayCurrencyCode, tvFeeAED, tvVat, tvDiscount, tvRoundOff;
    private Button btnSend;
    private JSONObject jsonObjectRequest, jsonObjectRequestPayment, jsonObjectInputChectTxnList;
    private String vat;
    private String rounding;
    private String discount;
    private String TOTAL_PRIORITY_PAY_CHARGES_PP;
    private String TOTAL_AMOUNT_PP;
    private String modeDescription, name, id;
    private BeneficiaryData dataObject;
    private TextView tvTopMessage, tvBottomMessage;
    private int ceMode = 0;
    private String ceServiceId = "";
    private ArrayList<BeneficiaryData> dataObjectsCountry;
    private String screenType;
    private String bankCodeAll, memPkIdAll;
    private ScrollView scrollView;
    private Intent intent;
    private String countryCode, ccyCode,ccyId;
    private BeneficiaryData selectionQuickSend;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        ivFlag = (AppCompatImageView) findViewById(R.id.flag);
        tvCountry = (TextView) findViewById(R.id.country_name);


        btnArexLeftArrow = findViewById(R.id.arex_left_arrow_btn);
        btnArexArrow = findViewById(R.id.arex_arrow_btn);

        layoutWesternUnion = findViewById(R.id.rl_wu);
        nestedScrollview = findViewById(R.id.nested_scrollview);
        btnArexLeftArrow.setOnClickListener(this);
        btnArexArrow.setOnClickListener(this);


        ivFlag.setOnClickListener(this);
        tvCountry.setOnClickListener(this);

        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));

        multiStateViewValue = (MultiStateView) findViewById(R.id.multiStateView_value);

        valueEmptyButton = multiStateViewValue.findViewById(R.id.empty_button);
        setViewStateOnClick();
        tvEmptyValue = ((TextView) multiStateViewValue.findViewById(R.id.empty_textView));
        tvErrorValue = ((TextView) multiStateViewValue.findViewById(R.id.error_textView));


        recyclerViewValue = (RecyclerView) findViewById(R.id.recyclerView);
//--------------------------------------------------------------------------------------------------

        layoutManagerValue = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManagerValue.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManagerValue.setMaxVisibleItems(1);
        layoutManagerValue.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
               /* if (adapterPosition >= 0 && (selectedCurrencyData == null || !selectedCurrencyData.getCurrencyCode().equalsIgnoreCase(recyclerAdapterCurrencyCode.getItemAt(adapterPosition).getCurrencyCode()))) {
                    selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(adapterPosition);
                    calculateCurrency();
                }*/
                Log.e("cxdfzeredsx", "" + adapterPosition);
                offScreen();
            }
        });
        recyclerViewValue.setLayoutManager(layoutManagerValue);
        recyclerViewValue.setHasFixedSize(true);

        recyclerAdapterValue = new BeneficiaryRecyclerAdapter(this, new ArrayList<BeneficiaryData>(), this, intentType);
        recyclerViewValue.setAdapter(recyclerAdapterValue);

        recyclerViewValue.addOnScrollListener(new CenterScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


//--------------------------------------------------------------------------------------------------
       /* layoutManagerValue = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManagerValue.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManagerValue.setMaxVisibleItems(1);
        recyclerViewValue.setLayoutManager(layoutManagerValue);
        recyclerViewValue.setHasFixedSize(true);
        recyclerAdapterValue = new BeneficiaryRecyclerAdapter(this, new ArrayList<BeneficiaryData>(), this, intentType);
        recyclerViewValue.setAdapter(recyclerAdapterValue);
        recyclerViewValue.addOnScrollListener(new CenterScrollListener());
        recyclerViewValue.setNestedScrollingEnabled(false);*/

        //New implement
        newInit();
        layoutMessage = (RelativeLayout) findViewById(R.id.rl_tv);
        layoutSendMoney = (RelativeLayout) findViewById(R.id.send_money_layout);
        layoutService = (RelativeLayout) findViewById(R.id.rel_lay_Service);
        layoutpayment = (RelativeLayout) findViewById(R.id.rel_lay_payment);
        ivSuccess.setVisibility(View.GONE);
        layoutpayment.setVisibility(View.GONE);


        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.iv_wu).setOnClickListener(this);
        findViewById(R.id.click_to_change).setOnClickListener(this);

        layoutManagerValue.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                if (adapterPosition > 0) {
                    btnArexLeftArrow.setVisibility(View.VISIBLE);
                } else {
                    btnArexLeftArrow.setVisibility(View.GONE);
                }
                if (adapterPosition < recyclerAdapterValue.getItemCount() - 1) {
                    btnArexArrow.setVisibility(View.VISIBLE);
                } else {
                    btnArexArrow.setVisibility(View.GONE);
                    if (Integer.parseInt(startCountArex) >= recyclerAdapterValue.getItemCount()) {
                        fetchBeneficiary(AREXMapping);
                    }
                }
            }
        });


        imagePickDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        imagePickDialog.setCanceledOnTouchOutside(true);
        imagePickDialog.setContentView(R.layout.image_pick_dialog);
        imagePickDialog.findViewById(app.alansari.R.id.camera).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkCameraPermission();
            }
        });

        imagePickDialog.findViewById(app.alansari.R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkGalleryPermission();
            }
        });

        imagePickDialog.findViewById(app.alansari.R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
            }
        });
    }

    private void newInit() {
        etSend = (EditText) findViewById(app.alansari.R.id.send);
        etGet = (EditText) findViewById(app.alansari.R.id.get);
        tvSendCode = (TextView) findViewById(app.alansari.R.id.send_code);
        tvGetCode = (TextView) findViewById(app.alansari.R.id.get_code);
        tvRate = (TextView) findViewById(app.alansari.R.id.rate);
        tvTimeMessage = (TextView) findViewById(app.alansari.R.id.time_message);
        tvMessageBottom = (TextView) findViewById(app.alansari.R.id.message);
        progressBar = (CircleProgressBar) findViewById(app.alansari.R.id.progress_bar);
        ivSuccess = (AppCompatImageView) findViewById(app.alansari.R.id.success_image);
        fragmentDataCoverLayout = findViewById(app.alansari.R.id.fragment_data_layout_cover);
        tvPaymentType = (AutoCompleteTextView) findViewById(app.alansari.R.id.payment_type_text);
        tvServiceType = (AutoCompleteTextView) findViewById(app.alansari.R.id.service_type_text);
        scrollView = (ScrollView) findViewById(app.alansari.R.id.scrollView);

        tvTopMessage = (TextView) findViewById(R.id.tv_top_message);
        tvBottomMessage = (TextView) findViewById(R.id.tv_bottom_message);
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        fragmentDataCoverLayout.setVisibility(View.GONE);
        fragmentDataCoverLayout.setOnClickListener(this);
        tvPaymentType.setOnClickListener(this);
        tvServiceType.setOnClickListener(this);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setSendBtnState(false);
                if (s == etSend.getEditableText()) {
                    if (s.length() > 8) {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_18sp));
                    }else if(s.length() > 5){
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    } else {
                        etSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etSend)) {
                        etGet.setText("");
                        progressBar.setVisibility(View.GONE);
                        ivSuccess.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etSend, textWatcher, s.toString());
//--------------------------------------------------------------------------------------------------
/*
                    if (etSend.getText().toString().length() > temp2) {
                        if (!etSend.getText().toString().contains("."))
                            etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() - 1)});
                        else
                            etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 1)});

                    }*/

                    if (!etSend.getText().toString().contains(".")) {
                        //etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 1)});
                        etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        check2 = 0;
                    } else if (check2 == 0) {
                        check2 = 1;
                        etSend.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etSend.getText().toString().length() + 2)});
                    }


//--------------------------------------------------------------------------------------------------
                } else if (s == etGet.getEditableText()) {
                    if (s.length() > 8) {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_18sp));
                    } else if(s.length() > 5){
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                    }else {
                        etGet.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                    }
                    if (!isCalculatingCurrency && Validation.isValidEditTextValue(etGet)) {
                        etSend.setText("");
                        progressBar.setVisibility(View.GONE);
                        ivSuccess.setVisibility(View.GONE);
                    }
                    if (Validation.isValidString(s.toString()))
                        CommonUtils.addCommaAfterThousand(etGet, textWatcher, s.toString());

//--------------------------------------------------------------------------------------------------

                    /*if (etGet.getText().toString().length() < temp) {
                        if (!etGet.getText().toString().contains("."))
                            etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() - 1)});
                        else
                            etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 1)});

                    }*/

                    if (!etGet.getText().toString().contains(".")) {
                        // etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 1)});
                        etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                        check = 0;
                    } else if (check == 0) {
                        check = 1;
                        etGet.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etGet.getText().toString().length() + 2)});
                    }

//--------------------------------------------------------------------------------------------------


                }
                totalToPay = "0.00";
                charge = "0.00";
                if (context instanceof BeneficiaryActivity)
                    onSuccessInCalculation(totalToPay, charge);
            }
        };


        EditText.OnEditorActionListener editiorListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        CommonUtils.hideKeyboard(context);
                        if (v.getId() == etSend.getId()) {
                            if (Validation.isValidEditTextValue(etSend)) {
                                currentDirection = 1;
                                calculateCurrency();
                            }
                            return true;
                        } else if (v.getId() == etGet.getId()) {
                            if (Validation.isValidEditTextValue(etGet)) {
                                currentDirection = 2;
                                calculateCurrency();
                            }
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        };

        etSend.addTextChangedListener(textWatcher);
        etGet.addTextChangedListener(textWatcher);
        etSend.setOnEditorActionListener(editiorListener);
        etGet.setOnEditorActionListener(editiorListener);

        // Currency Code Scroll
        recyclerViewCurrencyCode = (RecyclerView) findViewById(app.alansari.R.id.recyclerView_currency_code);
        layoutManagerCurrencyCode = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManagerCurrencyCode.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManagerCurrencyCode.setMaxVisibleItems(1);

        layoutManagerCurrencyCode.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                if (adapterPosition >= 0 && (selectedCurrencyData == null || !selectedCurrencyData.getCurrencyCode().equalsIgnoreCase(recyclerAdapterCurrencyCode.getItemAt(adapterPosition).getCurrencyCode()))) {
                    selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(adapterPosition);
                    Log.e("cxdfzeredsx", "" + selectedCurrencyData.getCurrencyCode());
                    calculateCurrency();
                }
            }
        });
        recyclerViewCurrencyCode.setLayoutManager(layoutManagerCurrencyCode);
        recyclerViewCurrencyCode.setHasFixedSize(true);
        recyclerAdapterCurrencyCode = new SendMoneyCurrencyCodeRecyclerAdapter(context, new ArrayList<CountryData.CurrencyData>(), this);
        recyclerViewCurrencyCode.setAdapter(recyclerAdapterCurrencyCode);
        recyclerViewCurrencyCode.addOnScrollListener(new CenterScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        layoutBottom = (LinearLayout) findViewById(R.id.bottom_layout);
        tvFeeAED = (TextView) findViewById(app.alansari.R.id.fee_aed);
        tvVat = (TextView) findViewById(app.alansari.R.id.vat_label);
        tvDiscount = (TextView) findViewById(app.alansari.R.id.discount_label);
        tvRoundOff = (TextView) findViewById(app.alansari.R.id.roundoff_label);
        tvTotalToPay = (TextView) findViewById(app.alansari.R.id.total_to_pay);
        tvTotalToPayCurrencyCode = (TextView) findViewById(app.alansari.R.id.total_to_pay_currency_code);
        btnSend = (Button) findViewById(app.alansari.R.id.send_btn);
        btnSend.setOnClickListener(this);
    }

    public void setSendBtnState(boolean state) {
        btnSend.setEnabled(state);
    }

    public void setTransparentCoverVisibility(int visibility) {
        findViewById(app.alansari.R.id.transparent_view).setVisibility(visibility);
    }

    public void hideVat() {
        tvVat.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);
        tvRoundOff.setVisibility(View.GONE);

        tvVat.setText("");
        tvDiscount.setText("");
        tvRoundOff.setText("");
    }

    //Enter Amount and Calculate Currency
    private void calculateCurrency() {
        try {
            if ((currentDirection == 1 && Validation.isValidEditTextValue(etSend)) || (currentDirection == 2 && Validation.isValidEditTextValue(etGet))) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    JsonObjectRequest jsonObjReq = null;
                    String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    String transferType = "";

                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                        if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    }
                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        serviceType = mSelectDetails.getModuleName();
                    } else {
                        serviceType = mSelectDetails.getModuleName();
                    }

                    if (currentDirection == 1) {
                        jsonObjReq = new CallAddr().executeApi(fetchRateSendMoney(
                                memPkIdAll, mSelectDetails.getCountryCode(), bankCodeAll, transferType, serviceType,
                                CommonUtils.getTextFromEditText(etSend), "91",selectedCurrencyData.getCurrencyCode(),
                                userPkId, LogoutCalling.getDeviceID(context), sessionTime, mSelectDetails.getBeneficiaryId()),
                                Constants.FETCH_RATE_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);

                    } else {
                        jsonObjReq = new CallAddr().executeApi(fetchRateSendMoney(
                               /* serviceType, mSelectDetails.getCountryCode(), bankCodeAll, transferType, mSelectDetails.getModuleName(),
                                CommonUtils.getTextFromEditText(etGet), mSelectDetails.getCountryCode(),
                                "91", userPkId, LogoutCalling.getDeviceID(context), sessionTime, mSelectDetails.getBeneficiaryId()*/
                                memPkIdAll, mSelectDetails.getCountryCode(), bankCodeAll, transferType, serviceType,
                                CommonUtils.getTextFromEditText(etGet), selectedCurrencyData.getCurrencyCode(), "91",
                                userPkId, LogoutCalling.getDeviceID(context), sessionTime, mSelectDetails.getBeneficiaryId()),
                                Constants.FETCH_RATE_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY, Request.Method.PUT, this);

                    }
                    cancelPendingRequests();
                    setSendBtnState(false);
                    isCalculatingCurrency = true;
                    //setTransparentCoverVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    ivSuccess.setVisibility(View.GONE);
//Gone while scolling currency----------------------------------------------------------------------
                    tvPaymentType.setText("");
                    layoutBottom.setVisibility(View.GONE);
//--------------------------------------------------------------------------------------------------

                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY.toString());
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                //setTransparentCoverVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Log.e("dsdjbsjdbvsd", "" + ex);
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }
    //done
    public JSONObject fetchRateSendMoney(String memPkId, String countryCode, String bankCode,
                                         String transferType, String serviceType, String amount,
                                         String fromCCY, String toCCY, String userId, String deviceId, String sessionTime, String MemBenefPkId) {
        jsonObjectRequest = new JSONObject();
        try {
            jsonObjectRequest.put(Constants.MEM_PK_ID, memPkId);
            jsonObjectRequest.put(Constants.COUNTRY_CODE, countryCode);
            jsonObjectRequest.put(Constants.BANK_CODE, bankCode);
            jsonObjectRequest.put(Constants.TRANSFER_TYPE, transferType);
            jsonObjectRequest.put(Constants.SERVICE_TYPE, serviceType);
            jsonObjectRequest.put(Constants.AMOUNT, amount);
            jsonObjectRequest.put(Constants.FROM_CCY, fromCCY);
            jsonObjectRequest.put(Constants.TO_CCY, toCCY);
            jsonObjectRequest.put(Constants.USER_PK_ID, checkValue(userId));
            jsonObjectRequest.put(Constants.DEVICE_ID, deviceId);
            jsonObjectRequest.put(Constants.SESSION_ID, sessionTime);
            jsonObjectRequest.put(Constants.MEM_BENEF_PK_ID, MemBenefPkId);
            jsonObjectRequest.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            if (ceMode == 1) jsonObjectRequest.put(Constants.CE_SERVICE_ID, ceServiceId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "calculateCurrencySendMoney:-  " + jsonObjectRequest.toString());
        return jsonObjectRequest;
    }
    //done
    public JSONObject checkTxnLimits(String userId, String memPkId, String beneficiaryId, String countryCode, String serviceType, String transferType, String totalValueAED, String txnAmount, String totalTxnAmout, String rate, String chargeAmount, String ccyCode, String deviceId, String sessionTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_PK_ID, userId);
            jsonObject.put(Constants.MEM_PK_ID, memPkId);
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, beneficiaryId);
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);

            jsonObject.put(Constants.CCY_CODE, ccyCode);
            jsonObject.put(Constants.CHARGE_AMOUNT, chargeAmount);
            jsonObject.put(Constants.COUNTRY_CODE, countryCode);
            jsonObject.put(Constants.RATE, rate);
            jsonObject.put(Constants.SERVICE_TYPE, serviceType);
            jsonObject.put(Constants.TXN_AMOUNT, txnAmount);
            jsonObject.put(Constants.TRANSFER_TYPE, transferType);
            jsonObject.put(Constants.TOTAL_VALUE_AED, totalValueAED);
            jsonObject.put(Constants.TOTAL_TXN_AMOUNT, totalTxnAmout);
            if (ceMode == 1) jsonObject.put(Constants.CE_SERVICE_ID, ceServiceId);
            jsonObject.put(Constants.MODE_DESCRIPTION, modeDescription);
            jsonObject.put(Constants.MODE_NAME, name);
            jsonObject.put(Constants.MODE_PK_ID, id);
            jsonObject.put(Constants.DEVICE_ID, deviceId);
            jsonObject.put(Constants.SESSION_ID, sessionTime);
            jsonObject.put(Constants.APP_VERSION, BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "checkTxnLimits:-  " + jsonObject.toString());
        return jsonObject;
    }

    public void onSuccessInCalculation(String amountToPay, String exchangeRate) {
        tvTotalToPay.setText(amountToPay);
        tvTotalToPayCurrencyCode.setText("AED");
        tvFeeAED.setText("Fee : AED " + exchangeRate);
    }

    public void setVat(JSONObject response) {
        try {
            String vatLable = response.getString(Constants.VAT_CHARGES_CODE);
            String roundingLable = response.getString(Constants.VAT_ROUNDINGOFF_CODE);
            String discountLable = response.getString(Constants.VAT_DISCOUNT_CODE);
            charge = response.getString(Constants.CHARGES_ON_US);
            totalToPay = response.getString(Constants.TOTAL_AMOUNT);
            onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);

            vat = response.getString(Constants.VAT_CHARGES);
            rounding = response.getString(Constants.VAT_ROUNDINGOFF);
            discount = response.getString(Constants.VAT_DISCOUNT);
            TOTAL_AMOUNT_PP = response.getString(Constants.TOTAL_AMOUNT_PP);
            TOTAL_PRIORITY_PAY_CHARGES_PP = response.getString(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP);
            Log.e("dcbshdcbhsdbv",""+response.getString("ADDITIONAL_CHARGES_PP")+"  "+TOTAL_PRIORITY_PAY_CHARGES_PP);

            if (vat != null && (!vat.equalsIgnoreCase("0.0") && !vat.equalsIgnoreCase("0.00"))) {
                vat = CommonUtils.getDecimalFormattedString(Double.parseDouble(vat));
                tvVat.setText(vatLable + " : AED " + vat);
                tvVat.setVisibility(View.VISIBLE);
            } else {
                tvVat.setText(null);
                tvVat.setVisibility(View.GONE);
            }
            if (discount != null && (!discount.equalsIgnoreCase("0.0") && !discount.equalsIgnoreCase("0.00"))) {
                discount = CommonUtils.getDecimalFormattedString(Double.parseDouble(discount));
                tvDiscount.setText(discountLable + " : AED " + discount);
                tvDiscount.setVisibility(View.VISIBLE);
            } else {
                tvDiscount.setText(null);
                tvDiscount.setVisibility(View.GONE);
            }
            if (rounding != null && (!rounding.equalsIgnoreCase("0.0") && !rounding.equalsIgnoreCase("0.00"))) {
                rounding = CommonUtils.getDecimalFormattedString(Double.parseDouble(rounding));
                tvRoundOff.setText(roundingLable + " : AED " + rounding);
                tvRoundOff.setVisibility(View.VISIBLE);
            } else {
                tvRoundOff.setText(null);
                tvRoundOff.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void cancelPendingRequests() {
        AppController.getInstance().cancelPendingRequests(CommonUtils.SERVICE_TYPE.CALCULATE_CURRENCY_SEND_MONEY.toString());
        onErrorInCalculation();
    }

    private void onErrorInCECurrency() {
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        recyclerAdapterCurrencyCode.clear();
        selectedCurrencyData = null;
        fragmentDataCoverLayout.setVisibility(View.VISIBLE);
        //Toast.makeText(context, "Unable to Fetch Currency For Selected Country!", Toast.LENGTH_SHORT).show();
    }

    private void onErrorInCalculation() {
        isCalculatingCurrency = false;
        progressBar.setVisibility(View.GONE);
        ivSuccess.setVisibility(View.GONE);
        //  ((SendMoneyActivity) getActivity()).setTransparentCoverVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beneficiary_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        ((TextView) findViewById(R.id.toolbar_title)).setText("Select Beneficiary");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, VALUE_TYPE.STRING);
        arexUserId = CommonUtils.getMemPkId(Constants.AREX_MAPPING);
        ceUserId = CommonUtils.getMemPkId(Constants.CE_MAPPING);
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);






        if (getIntent().getExtras() != null) {
            intentType = getIntent().getExtras().getString(Constants.INTENT_TYPE, Constants.ACTIVITY_NO_RESULT);
            source = getIntent().getExtras().getString(Constants.SOURCE, null);
        }
        gson = new Gson();
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        init();
        if (!(Boolean) SharedPreferenceManger.getPrefVal(Constants.FETCH_SERVICE_TYPE_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN)) {
            serviceTypeDatas = null;
        } else {
            serviceTypeDatas = SharedPreferenceManger.loadServiceTypeData();
        }
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

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(BeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(BeneficiaryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(BeneficiaryActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (BeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (BeneficiaryActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(BeneficiaryActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            pickCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        pickCamera();
                    } else {
                        Snackbar.make(BeneficiaryActivity.this.findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickPhotos();
                } else {
                    Snackbar.make(BeneficiaryActivity.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(BeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                                }
                            }).show();
                }
                break;
        }
    }

    private void pickCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void pickPhotos() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_IMAGE_PICK);
    }

    private void setViewStateOnClick() {
        multiStateViewValue.findViewById(R.id.empty_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountArex = "1";
                fetchBeneficiary(AREXMapping);
            }
        });
        multiStateViewValue.findViewById(R.id.error_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountArex = "1";
                fetchBeneficiary(AREXMapping);
            }
        });

        valueEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountCe = "1";
                fetchBeneficiary(CEMapping);
            }
        });

    }

    private void setSelectedCountryData() {
        if (selectedCountry == null) {
            multiStateViewValue.setVisibility(View.GONE);
            openCountryList();
        } else {
            CommonUtils.setCountryFlagImage(context, ivFlag, selectedCountry.getFlag());
            tvCountry.setText("(" + selectedCountry.getLatinName() + ")");
            // tvValueRate.setText("AED = " + selectedCountry.getCcyAREX());
            setServiceDataLayout();
        }

    }

    private void setServiceDataLayout() {
        if (serviceTypeDatas != null) {
            multiStateViewValue.setVisibility(View.GONE);
            for (int i = 0; i < serviceTypeDatas.size(); i++) {
                if (serviceTypeDatas.get(i).getStatus().equalsIgnoreCase("1")) {
                    if (serviceTypeDatas.get(i).getId().equalsIgnoreCase("1")) {
                        multiStateViewValue.setVisibility(View.VISIBLE);
                        AREXMapping = serviceTypeDatas.get(i).getMapping();
                        fetchBeneficiary(AREXMapping);
                    }
                }
            }
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } else {
            fetchServiceTypes();
        }
    }

    private void openCountryList() {
        if (!isCountryRunning) {
            isCountryRunning = true;
            Intent intent = new Intent(context, SelectCountryFlagActivity.class);
            startActivityForResult(intent, Constants.SELECT_ITEM_COUNTRY);
            overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.arex_left_arrow_btn:
                    if (recyclerAdapterValue.getItemCount() > 0)
                        layoutManagerValue.smoothScrollToPosition(recyclerViewValue, new RecyclerView.State(), layoutManagerValue.getCenterItemPosition() - 1);
                    break;
                case R.id.arex_arrow_btn:
                    if (recyclerAdapterValue.getItemCount() > layoutManagerValue.getCenterItemPosition() + 1)
                        layoutManagerValue.smoothScrollToPosition(recyclerViewValue, new RecyclerView.State(), layoutManagerValue.getCenterItemPosition() + 1);
                    break;
                case R.id.flag:
                case R.id.click_to_change:
                case R.id.country_name:
                    String beneficiaryId = getIntent().getExtras().getString(Constants.ID, null);
                    if (beneficiaryId != null)
                        Log.e("cnscbsdh","sdjbshbv");
                    else
                        openCountryList();

                    break;
                case R.id.empty_button:
                case R.id.error_button:
                    fetchServiceTypes();
                    break;
                case app.alansari.R.id.send_btn:
                    validateExistingBeneficiary();
                    break;
                case R.id.fab:
                    Intent intent = new Intent(context, AddBeneficiarySelectTypeActivity.class);
                    intent.putExtra(Constants.OBJECT, (BeneficiaryData) null);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
                    startActivityForResult(intent, Constants.ADD_NEW_BENEFICIARY);
                    break;
                case R.id.iv_wu:
                    fetchWU();
                    break;
                case app.alansari.R.id.payment_type_text:
                case app.alansari.R.id.payment_type_card_view:
                    //dataObject.setAdditionalInfoTimeStamp(String.valueOf(timeStamp));

                    String transferType = "";
                    countryCode= mSelectDetails.getCountryCode();
                    ccyCode=  selectedCurrencyData.getCurrencyCode();




                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                        if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                            transferType = "BT";
                        } else {
                            transferType = "CP";
                        }

                    }

                    Intent intentData = new Intent(context, PaymentSelectModeActivity.class);
                    intentData.putExtra(Constants.AED_AMOUNT, etSend.getText().toString().trim());
                    intentData.putExtra(Constants.FCY_AMOUNT, etGet.getText().toString().trim());
                    intentData.putExtra(Constants.RATE, rate);
                    intentData.putExtra(Constants.OBJECT, jsonObjectRequest.toString());
                    intentData.putExtra(Constants.CE_SERVICE_ID, ceServiceId);
                    intentData.putExtra(Constants.BANK_CODE, bankCodeAll);
                    intentData.putExtra(Constants.SCREEN_TYPE, screenType);
                    intentData.putExtra(Constants.ID, mSelectDetails.getBeneficiaryId());
                    intentData.putExtra(Constants.TRANSFER_TYPE, transferType);

                    intentData.putExtra(Constants.COUNTRY_CODE, mSelectDetails.getCountryCode());
                    intentData.putExtra(Constants.CCY_CODE,  selectedCurrencyData.getCurrencyCode());


                    intentData.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
                    startActivityForResult(intentData, Constants.PAYMENT_SELECTION_MODE);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    break;
                case app.alansari.R.id.service_type_text:
                case app.alansari.R.id.service_type_card_view:
                    Intent i = new Intent(context, SelectItemActivity.class);
                    i.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD);
                    i.putExtra(Constants.ID, Integer.valueOf(70));
                    i.putExtra(Constants.TITLE, Constants.SERVICE_TYPE);
                    i.putExtra(Constants.BENEF_PK_ID, mSelectDetails.getBeneficiaryId());
                    i.putExtra(Constants.COUNTRY_CODE, mSelectDetails.getCountryCode());
                    startActivityForResult(i, Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD);
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        Intent data;
        if (intentType.equalsIgnoreCase(Constants.ACTIVITY_FOR_RESULT)) {
            if (dataItem != null) {
                data = new Intent();
                data.putExtra(Constants.OBJECT, (BeneficiaryData) dataItem);
                setResult(AppCompatActivity.RESULT_OK, data);
            } else {
                setResult(AppCompatActivity.RESULT_CANCELED, null);
            }
            onBackPressed();
        } else if (intentType.equals(Constants.ACTIVITY_FOR_SELECTION)) {
            if (dataItem != null) {
                mSelectDetails = ((BeneficiaryData) dataItem);
                if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                    layoutService.setVisibility(View.VISIBLE);
                    layoutSendMoney.setVisibility(View.GONE);
                    layoutWesternUnion.setVisibility(View.GONE);
                    layoutMessage.setVisibility(View.GONE);
                    memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    bankCodeAll = mSelectDetails.getBankData().getBankCodeCE();
                    screenType = "CE";
                    ceMode = 1;
                    checkingApi();


                } else if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                    layoutService.setVisibility(View.GONE);
                    layoutSendMoney.setVisibility(View.VISIBLE);
                    layoutWesternUnion.setVisibility(View.GONE);
                    layoutMessage.setVisibility(View.GONE);
                    screenType = "AREX";
                    // ceMode=0;
                    memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    bankCodeAll = mSelectDetails.getBankData().getBankCodeAREX();
                }


                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBankData().getBankCodeCE());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBeneficiaryId());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getCountryCode());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBankData().getAREX_BT());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getModuleName());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBeneficiaryId());
                Log.e("cbchjvhjcbsd", "" + mSelectDetails.getModuleName());
                Log.e("cbchjvhjcbsd22", " " + mSelectDetails.getServiceTypeData().getMapping());

//Modification
                if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                    if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                        tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                        tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtValue());
                    } else {
                        tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                        tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtInstant());
                    }

                } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                    if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                        tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                        tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpValue());
                    } else {
                        tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                        tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpInstant());
                    }

                }

                setCurrencyOnCreated();

            }
        } else {
            if (dataItem != null && view.getId() != app.alansari.R.id.edit_btn && view.getId() != app.alansari.R.id.pic_layout) {
               /* data = new Intent(context, SendMoneyActivity.class);
                data.putExtra(Constants.OBJECT, (BeneficiaryData) dataItem);
                startActivity(data);*/
               /* layoutSendMoney.setVisibility(View.VISIBLE);
                layoutWesternUnion.setVisibility(View.GONE);
                layoutMessage.setVisibility(View.GONE);*/


                if (dataItem != null) {
                    mSelectDetails = ((BeneficiaryData) dataItem);
                    if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                        layoutService.setVisibility(View.VISIBLE);
                        layoutSendMoney.setVisibility(View.GONE);
                        layoutWesternUnion.setVisibility(View.GONE);
                        layoutMessage.setVisibility(View.GONE);
                        memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        bankCodeAll = mSelectDetails.getBankData().getBankCodeCE();
                        screenType = "CE";
                        ceMode = 1;
                        checkingApi();


                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        layoutService.setVisibility(View.GONE);
                        layoutSendMoney.setVisibility(View.VISIBLE);
                        layoutWesternUnion.setVisibility(View.GONE);
                        layoutMessage.setVisibility(View.GONE);
                        screenType = "AREX";
                        // ceMode=0;
                        memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                        bankCodeAll = mSelectDetails.getBankData().getBankCodeAREX();
                    }


                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBankData().getBankCodeCE());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBeneficiaryId());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getCountryCode());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBankData().getAREX_BT());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getModuleName());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getBeneficiaryId());
                    Log.e("cbchjvhjcbsd", "" + mSelectDetails.getModuleName());
                    Log.e("cbchjvhjcbsd22", " " + mSelectDetails.getServiceTypeData().getMapping());


                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                        if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtValue());
                        } else {
                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtInstant());
                        }

                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                        if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpValue());
                        } else {
                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpInstant());
                        }

                    }

                    setCurrencyOnCreated();

                }

            } else {

                /*for Ben Image*/
               /* uploadBen = (BeneficiaryData) dataItem;
                imagePickDialog.show();*/
            }
//            data = new Intent(context, AddBeneficiaryActivity.class);
//            data.putExtra(Constants.OBJECT, (BeneficiaryData) dataItem);
//            data.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
//            startActivityForResult(data, Constants.ADD_NEW_BENEFICIARY);
        }
    }

    private void checkingApi() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    JsonObjectRequest  jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceType(userId, memPkIdAll, mSelectDetails.getBeneficiaryId(),mSelectDetails.getCountryCode(), LogoutCalling.getDeviceID(context), sessionTime), Constants.LOAD_SERVICE_TYPE_LIST_URL, LOAD_SERVICE_TYPE_LIST, Request.Method.POST, BeneficiaryActivity.this);
                    AppController.getInstance().getRequestQueue().cancelAll(LOAD_SERVICE_TYPE_LIST.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, LOAD_SERVICE_TYPE_LIST.toString());

                }
            },2000);*/
            JsonObjectRequest  jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceType(userId, memPkIdAll, mSelectDetails.getBeneficiaryId(),mSelectDetails.getCountryCode(), LogoutCalling.getDeviceID(context), sessionTime), Constants.LOAD_SERVICE_TYPE_LIST_URL, LOAD_SERVICE_TYPE_LIST, Request.Method.POST, BeneficiaryActivity.this);
            AppController.getInstance().getRequestQueue().cancelAll(LOAD_SERVICE_TYPE_LIST.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, LOAD_SERVICE_TYPE_LIST.toString());

            // CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.LOAD_SERVICE_TYPE_LIST.toString(), false);

        }else{
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setCurrencyOnCreated() {
        if (mSelectDetails.getModuleName().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            if (mSelectDetails.getCountryData() != null && mSelectDetails.getCountryData().getCurrencyData() != null && mSelectDetails.getCountryData().getCurrencyData().size() > 0) {
                recyclerAdapterCurrencyCode.addArrayList(mSelectDetails.getCountryData().getCurrencyData());
                setDefaultCurrency();
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
            }
        } else {
            if (mSelectDetails.getCountryData() != null && mSelectDetails.getBankData() != null) {
                fetchCeCurrencyData();
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    /*private void setCurrencyOnCreated2() {
        if (serviceTypeData.getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            if (countryData.getCurrencyData() != null && countryData.getCurrencyData() != null && countryData.getCurrencyData().size() > 0) {
                recyclerAdapterCurrencyCode.addArrayList(countryData.getCurrencyData());
                setDefaultCurrency();
                fragmentDataCoverLayout.setVisibility(View.GONE);
            } else {
                fragmentDataCoverLayout.setVisibility(View.VISIBLE);
                Toast.makeText(context, getString(app.alansari.R.string.currency_not_available_for_selected_country), Toast.LENGTH_LONG).show();
            }
        } else {
            if (countryData != null && bankData != null && transferType != null) {
                fetchCeCurrencyData();
            } else {
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                fragmentDataCoverLayout.setVisibility(View.VISIBLE);
                return;
            }
        }
    }*/


    private void setDefaultCurrency() {
        selectedCurrencyData = recyclerAdapterCurrencyCode.getItemCount() > 0 ? recyclerAdapterCurrencyCode.getItemAt(0) : null;
        for (int i = 0; i < recyclerAdapterCurrencyCode.getItemCount(); i++) {
            if (recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus() == null || recyclerAdapterCurrencyCode.getItemAt(i).getDefaultStatus().equalsIgnoreCase("1")) {
                selectedCurrencyData = recyclerAdapterCurrencyCode.getItemAt(i);
                layoutManagerCurrencyCode.scrollToPosition(i);
                break;
            }
        }
    }

    private void fetchCeCurrencyData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String transferType = "";
            if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCeCurrencyData(mSelectDetails.getBankData().getBankCodeCE(), mSelectDetails.getCountryData().getCountryCodeCE(), CommonUtils.getMemPkId(mSelectDetails.getServiceTypeData().getMapping()), transferType, "", userId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_CE_CURRENCY_DATA_URL, FETCH_CE_CURRENCY_DATA, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_CE_CURRENCY_DATA.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CE_CURRENCY_DATA.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_CE_CURRENCY_DATA.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }
    //done
    private void validateExistingBeneficiary() {
        try {
            LogUtils.d("iudsfusfvugdr", "" + rate + " " + jsonObjectRequest + " " + jsonObjectRequestPayment);
            if (mSelectDetails.getTxnAmountData() != null) {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    JSONObject input = checkTxnLimits(
                            CommonUtils.getUserId(), memPkIdAll,
                            mSelectDetails.getBeneficiaryId(),
                            //jsonObjectRequest.getString("COUNTRY_CODE"),
                            countryCode,
                            jsonObjectRequest.getString("SERVICE_TYPE"), jsonObjectRequest.getString("TRANSFER_TYPE"),
                            jsonObjectRequestPayment.getString("AED_AMOUNT"), jsonObjectRequestPayment.getString("FCY_AMOUNT"),
                            jsonObjectRequestPayment.getString(Constants.TOTAL_AMOUNT), rate, jsonObjectRequestPayment.getString(Constants.CHARGES_ON_US),
                            //jsonObjectRequest.getString("TO_CCY")
                            ccyCode, LogoutCalling.getDeviceID(context), sessionTime);
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(input, Constants.CHECK_TXN_LIMITS_URL, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS, Request.Method.PUT, this);
                    AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString());
                    CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.CHECK_TXN_LIMITS.toString(), false);
                    jsonObjectInputChectTxnList = new JSONObject(input.toString());

                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please enter valid amount.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (isTaskRoot() && source != null && source.equalsIgnoreCase(Constants.SOURCE_FCM_ACTIVITY)) {
            CommonUtils.goToDashBoard(context);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
      /*  selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
        startCountArex = "1";
        startCountCe = "1";
        setSelectedCountryData();
        ;*/

        recyclerAdapterValue.clear();
        fetchBeneficiary(AREXMapping);
        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();

        setSelectedCountryData();



//        selectedCountry = SharedPreferenceManger.loadSelectedCountryData();
//        startCountArex = "1";
//        startCountCe = "1";
//        setSelectedCountryData();

       // fetchBeneficiary(CEMapping);

       /* startCountCe = "1";
        startCountArex = "1";
        selectedCountry = data.getParcelableExtra(Constants.OBJECT);
        setSelectedCountryData();*/


//        String beneficiaryId = getIntent().getExtras().getString(Constants.ID, null);
//        /*if (beneficiaryId != null) {
//            fetchBeneficiaryDetails(beneficiaryId, getIntent().getExtras().getString(Constants.SERVICE_TYPE, Constants.AREX_MAPPING));
//            return;
//        }*/
//        //fetchBeneficiaryDetails(beneficiaryId, getIntent().getExtras().getString(Constants.SERVICE_TYPE, Constants.AREX_MAPPING));
//        if (Integer.parseInt(startCountArex) >= recyclerAdapterValue.getItemCount()) {
//            fetchBeneficiary(AREXMapping);
//        }
        super.onResume();



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.ADD_NEW_BENEFICIARY && resultCode == Activity.RESULT_OK) {
                startCountCe = "1";
                startCountArex = "1";
                fetchBeneficiary(AREXMapping);
                fetchBeneficiary(CEMapping);
            } else if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                if (resultCode == Activity.RESULT_OK) {
                    startCountCe = "1";
                    startCountArex = "1";
                    selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                    setSelectedCountryData();

                    //123456
                } else {
                    if (selectedCountry == null)
                        onBackPressed();
                }
                isCountryRunning = false;
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap selectedImage = (Bitmap) extras.get("data");
                Uri imageUri = CommonUtils.getImageUri(getApplicationContext(), selectedImage);
                Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getRealPathFromURI(context, imageUri));
                base64 = CommonUtils.encodeTobase64(newBitmap);
                uploadPic();
            } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getPath(imageUri, context));
                    base64 = CommonUtils.encodeTobase64(newBitmap);
                    uploadPic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Constants.PAYMENT_SELECTION_MODE && resultCode == Activity.RESULT_OK) {
                tvPaymentType.setText(data.getStringExtra(Constants.NAME));
                modeDescription = data.getStringExtra(Constants.DESCRIPTION);
                name = data.getStringExtra(Constants.NAME_TYPE);
                id = data.getStringExtra(Constants.ID);
                layoutBottom.setVisibility(View.VISIBLE);
                setSendBtnState(true);
                jsonObjectRequestPayment = new JSONObject(data.getStringExtra(Constants.OBJECT));
                setVat(jsonObjectRequestPayment);
                //dataObject.setTxnAmountData((viewPager.getCurrentItem())).getTxnData());
                performSmoothScroll();


            } else if (requestCode == Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD && resultCode == Activity.RESULT_OK) {
                tvServiceType.setText(((RESULTDTO) data.getParcelableExtra(Constants.OBJECT)).getDisplayValue());
                Log.e("vbdzgcvhd", "" + ((RESULTDTO) data.getParcelableExtra(Constants.OBJECT)));
                ceMode = 1;
                ceServiceId = ((RESULTDTO) data.getParcelableExtra(Constants.OBJECT)).getDisplayKey();
                layoutSendMoney.setVisibility(View.VISIBLE);
                layoutWesternUnion.setVisibility(View.GONE);
                layoutMessage.setVisibility(View.GONE);
                performSmoothScroll();

            } else if (requestCode == Constants.UPDATE_MISSING_FIELD_BENEFICIARY && resultCode == Activity.RESULT_OK) {
                if (data != null && data.getParcelableExtra(Constants.OBJECT) != null) {
                    dataObject = data.getParcelableExtra(Constants.OBJECT);
                    dataObject.setTxnAmountData(getTxnData());
                    validateExistingBeneficiary();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TxnAmountData getTxnData() {
        if ((!TextUtils.isEmpty(etSend.getText().toString().trim()) && etSend.getText().toString().trim().length() > 0) && ((!TextUtils.isEmpty(etGet.getText().toString().trim()) && etGet.getText().toString().trim().length() > 0))) {
            TxnAmountData currencyData = new TxnAmountData();
            currencyData.setYouSend(CommonUtils.getTextFromEditText(etSend));
            currencyData.setYouGet(CommonUtils.getTextFromEditText(etGet));
            currencyData.setTotalToPay(totalToPay);
            currencyData.setFee(charge);
            currencyData.setRate(rate);
            currencyData.setYouSendCurrencyData(new CountryData.CurrencyData().getUAECurrencyData());
            currencyData.setYouGetCurrencyData(selectedCurrencyData);
            return currencyData;
        } else return null;
    }

    private void uploadPic() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = null;
            if (uploadBen.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
                jsonObjReq = new CallAddr().executeApi(addBeneficiaryJsonFormat(), Constants.UPDATE_MISSING_FIELD_BENEFICIARY_URL, UPDATE_MISSING_FIELD_BENEFICIARY, Request.Method.PUT, this);
            } else {
                jsonObjReq = new CallAddr().executeApi(addBeneficiaryJsonFormat(), Constants.UPDATE_MISSING_FIELD_BENEFICIARY_CE_URL, UPDATE_MISSING_FIELD_BENEFICIARY, Request.Method.PUT, this);
            }

            AppController.getInstance().getRequestQueue().cancelAll(UPDATE_MISSING_FIELD_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, UPDATE_MISSING_FIELD_BENEFICIARY.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), UPDATE_MISSING_FIELD_BENEFICIARY.toString(), false);
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private JSONObject addBeneficiaryJsonFormat() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            if ((uploadBen.getAccountNumber() == null || TextUtils.isEmpty(uploadBen.getAccountNumber().trim()) || uploadBen.getAccountNumber().trim().length() == 0) &&
                    (uploadBen.getIBANNumber() == null || TextUtils.isEmpty(uploadBen.getIBANNumber().trim()) || uploadBen.getIBANNumber().trim().length() == 0)) {
                jsonObject.put(Constants.TRANSFER_TYPE, "CP");
            } else {
                jsonObject.put(Constants.TRANSFER_TYPE, "BT");
            }
            jsonObject.put(Constants.COUNTRY_CODE, uploadBen.getCountryData().getCountryCodeAREX());
            jsonObject.put(Constants.SERVICE_TYPE, uploadBen.getServiceTypeData().getMapping());
            if (uploadBen.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
                jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.AREX_MAPPING));
            } else {
                jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.CE_MAPPING));
            }
            jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(uploadBen.getBeneficiaryId()));
            jsonObject.put(Constants.BENF_IMAGE, base64);


            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiary :-  " + jsonObject.toString());
        return jsonObject;
    }

    private String checkValue(String value) {
        return value != null ? value : "";
    }


    void fetchServiceTypes() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchServiceTypes((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_SERVICE_TYPES_URL, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_SERVICE_TYPES.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    void fetchWU() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().loadWuNumber((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "", LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_WU_SEND_MONEY_URL, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_SEND_MONEY.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void fetchChargesSendMoney() {
        //FETCH_CHARGES_SENDMONEY_URL
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchChargesSendMoney(
                    "", "", "", "", "",
                    "", "", "", "", "", "", userId,
                    LogoutCalling.getDeviceID(context), sessionTime, ""),
                    Constants.FETCH_CHARGES_SENDMONEY_URL, FETCH_CHARGES_SENDMONEY, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_CHARGES_SENDMONEY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CHARGES_SENDMONEY.toString());

            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), FETCH_CHARGES_SENDMONEY.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    //Caling Opening
    private void fetchBeneficiary(String serviceType) {
        if (serviceType == null) {
            return;
        }
        if (getIntent().getExtras() != null) {
            String beneficiaryId = getIntent().getExtras().getString(Constants.ID, null);
            if (beneficiaryId != null) {
                String serviceTypes = getIntent().getExtras().getString(Constants.SERVICE_TYPE, Constants.AREX_MAPPING);
                intentType = getIntent().getExtras().getString(Constants.INTENT_TYPE, Constants.ACTIVITY_NO_RESULT);
                source = getIntent().getExtras().getString(Constants.SOURCE, null);
                layoutWesternUnion.setVisibility(View.GONE);
                String startCount = "1";
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    if (serviceType.equalsIgnoreCase(AREXMapping)) {
                        startCount = startCountArex;
                    } else {
                        startCount = startCountCe;
                    }
                    if (startCount.equalsIgnoreCase("1")) {
                        multiStateViewValue.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    } else {
                        CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
                    }
                    String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiaryDetails(beneficiaryId, serviceTypes, userId, LogoutCalling.getDeviceID(context), sessionTime),
                            Constants.FETCH_BENEFICIARY_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS, Request.Method.POST, this);
                    AppController.getInstance().cancelPendingRequests(FETCH_BENEFICIARY_DETAILS);
                    AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_BENEFICIARY_DETAILS.toString());

                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    CommonUtils.setViewState(multiStateViewValue, VIEW_STATE_ERROR, tvErrorValue, null, null);
                }
            } else {
                String startCount = "1";
                if (selectedCountry != null) {
                    if (NetworkStatus.getInstance(context).isOnline2(context)) {
                        if (serviceType.equalsIgnoreCase(AREXMapping)) {
                            startCount = startCountArex;
                        } else {
                            startCount = startCountCe;
                        }
                        if (startCount.equalsIgnoreCase("1")) {
                            multiStateViewValue.setViewState(MultiStateView.VIEW_STATE_LOADING);
                        } else {
                            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
                        }
                        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

                        JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiary2(userId,
                                (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null,
                                  SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null,
                                   SharedPreferenceManger.VALUE_TYPE.STRING),
                                serviceType.equalsIgnoreCase(AREXMapping) ? arexUserId : ceUserId, serviceType.equals(AREXMapping) ? selectedCountry.getCountryCodeAREX() : selectedCountry.getCountryCodeCE(), serviceType, startCount, LogoutCalling.getDeviceID(context), sessionTime),
                                Constants.FETCH_BENEFICIARY_URL, FETCH_BENEFICIARY_INSTANT, Request.Method.POST, this);
                        //Siddu..

                        AppController.getInstance().cancelPendingRequests(FETCH_BENEFICIARY_INSTANT);
                        AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_BENEFICIARY_INSTANT.toString());



                /* JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiary2(userId, (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), serviceType.equalsIgnoreCase(AREXMapping) ? arexUserId : ceUserId, serviceType.equals(AREXMapping) ? selectedCountry.getCountryCodeAREX() : selectedCountry.getCountryCodeCE(), serviceType, startCount, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_BENEFICIARY_URL, (serviceType.equalsIgnoreCase(AREXMapping) ? FETCH_BENEFICIARY_VALUE : FETCH_BENEFICIARY_INSTANT), Request.Method.POST, this);
                AppController.getInstance().cancelPendingRequests((serviceType.equalsIgnoreCase(AREXMapping) ? FETCH_BENEFICIARY_VALUE : FETCH_BENEFICIARY_INSTANT).toString());
                AppController.getInstance().addToRequestQueue(jsonObjReq, (serviceType.equalsIgnoreCase(AREXMapping) ? FETCH_BENEFICIARY_VALUE : FETCH_BENEFICIARY_INSTANT).toString());
            */

                    } else {
                        CommonUtils.setViewState(multiStateViewValue, VIEW_STATE_ERROR, tvErrorValue, null, null);
                    }
                } else {
                    openCountryList();
                }

            }
            offScreen();
        }
    }

    private void fetchBeneficiaryDetails(String beneficiaryId, String serviceType) {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiaryDetails(beneficiaryId, serviceType, userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_BENEFICIARY_DETAILS_URL, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString());
            // CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_DETAILS.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }


    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        performSmoothScroll();
        switch (sType) {
            case FETCH_WU_SEND_MONEY:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) && response.getInt(Constants.STATUS_CODE) == (Constants.REDIRECT_TO_TXN_BENEF_PAGE)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUBeneficiaryActivity.class)
                                    .putExtra(Constants.AREX_MEM_PK_ID, arexUserId)
                                    .putExtra(Constants.WU_CARD_NO, response.getString(Constants.WU_CARD_NO));
                            startActivity(wu);
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS) && response.getInt(Constants.STATUS_CODE) == (Constants.REDIRECT_TO_ENROLL_PAGE)) {
                            CommonUtils.hideLoading();
                            Intent wu = new Intent(context, WUEnrollActivity.class).putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
                            startActivity(wu);
                        } else {
                            CommonUtils.hideLoading();
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.hideLoading();
                    }
                } else {
                    CommonUtils.hideLoading();
                }
                break;
            case UPDATE_MISSING_FIELD_BENEFICIARY:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            CommonUtils.hideLoading();
                            startCountCe = "1";
                            startCountArex = "1";
                            fetchBeneficiary(AREXMapping);
                            fetchBeneficiary(CEMapping);
                        } else {
                            CommonUtils.hideLoading();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case FETCH_SERVICE_TYPES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                serviceTypeDatas = (ArrayList<ServiceTypeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ServiceTypeData>>() {
                                }.getType());
                                if (serviceTypeDatas != null && serviceTypeDatas.size() > 0) {
                                    setServiceDataLayout();
                                    SharedPreferenceManger.saveServiceTypeData(gson, serviceTypeDatas);
                                    SharedPreferenceManger.setPrefVal(Constants.FETCH_SERVICE_TYPE_DATA_OFF, true, VALUE_TYPE.BOOLEAN);
                                } else {
                                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                                }
                            } else {
                                CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                            }
                        } else {
                            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
//--------------------------------------------------------------------------------------------------
            case CALCULATE_CURRENCY_SEND_MONEY:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            String amount = response.getString(Constants.AMOUNT);
                            // if (Validation.isValidRate(amount) && Validation.isValidRate(response.getString(Constants.TOTAL_AMOUNT)) && Validation.isValidRate(response.getString(Constants.RATE)) && Validation.isValidRate(response.getString(Constants.CHARGES_ON_US))) {
                            if (Validation.isValidRate(amount) && Validation.isValidRate(response.getString(Constants.RATE))) {

                               /* Double amountDouble = Double.valueOf(amount);
                                Double totalAmountDouble = Double.valueOf(response.getString(Constants.TOTAL_AMOUNT));*/
                                Double rateDouble = Double.valueOf(response.getString(Constants.RATE));
                              /*  Double chargeDouble = Double.valueOf(response.getString(Constants.CHARGES_ON_US));
                                Double vat = Double.valueOf(response.getString(Constants.VAT_CHARGES));
                                Double discount = Double.valueOf(response.getString(Constants.VAT_DISCOUNT));
                                Double roundOff = Double.valueOf(response.getString(Constants.VAT_ROUNDINGOFF));*/
                                if (currentDirection == 1) {
                                    etGet.setText(amount);
                                } else {
                                    etSend.setText(amount);
                                }
                                NumberFormat nf = NumberFormat.getInstance(Locale.US);
                                nf.setMinimumFractionDigits(10);
                                rate = String.valueOf(nf.format(rateDouble));//CommonUtils.getDecimalFormattedString(rateDouble);
                                setRateData(rate);
                                progressBar.setVisibility(View.GONE);
                                ivSuccess.setVisibility(View.VISIBLE);
                                layoutpayment.setVisibility(View.VISIBLE);

                                /*charge = response.getString(Constants.CHARGES_ON_US);
                                totalToPay = response.getString(Constants.TOTAL_AMOUNT);*/
                                /*setRateData(rate);
                                if (getActivity() instanceof SendMoneyActivity) {
                                    ((SendMoneyActivity) getActivity()).onSuccessInCalculation(CommonUtils.addCommaToString(totalToPay), charge);
                                    ((SendMoneyActivity) getActivity()).setVat(response);
                                    ((SendMoneyActivity) getActivity()).setSendBtnState(true);
                                }*/
                                // onSuccessInCalculation();
                            } else {
                                onAPIError();
                            }
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE) || response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            //  Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                            onErrorInCalculation();
                        } else {
                            onAPIError();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onAPIError();
                    }
                } else {
                    onAPIError();
                }
                break;
//--------------------------------------------------------------------------------------------------
            case FETCH_CE_CURRENCY_DATA:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CountryData.CurrencyData> currencyList = (ArrayList<CountryData.CurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CountryData.CurrencyData>>() {
                                }.getType());
                                if (Validation.isValidList(currencyList)) {
                                    setCurrencyList(currencyList);
                                    return;
                                } else {
                                    onCurrencyListEmpty();
                                }
                            } else {
                                onCurrencyListEmpty();
                            }
                        } else {
                            onErrorInCECurrency();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onErrorInCECurrency();
                    }
                } else {
                    onErrorInCECurrency();
                }
                break;
//--------------------------------send button-------------------------------------------------------
            case CHECK_TXN_LIMITS:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            onSuccess();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.REJECTED)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        }else if(response.getString(Constants.STATUS_MSG).equals("INFO")){
                            showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.MISSING)) {
                            if (response.get(Constants.RESULT) != null && !response.getString(Constants.RESULT).equalsIgnoreCase("null") && Validation.isValidJsonArray(response.getJSONArray(Constants.RESULT))) {
                                onBeneficiaryMissingFields(response.getJSONArray(Constants.RESULT), response.getString(Constants.ID), response.getString(Constants.MESSAGE));
                                //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context, "Unable to proceed: " + response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            CommonUtils.showLimitDialog(context, response.getString(Constants.MESSAGE));
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;


//--------------------------------------------------------------------------------------------------
            case FETCH_CHARGES_SENDMONEY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {


                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case FETCH_BENEFICIARY_INSTANT:
                if (status == 1) {
                    try {
                        CommonUtils.hideLoading();
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            layoutWesternUnion.setVisibility(View.VISIBLE);
                            String message= response.getString(Constants.MESSAGE);
                            if(!message.equalsIgnoreCase("null"))
                                ((TextView) findViewById(R.id.tv_message)).setText(response.getString(Constants.MESSAGE));
                            else
                                ((TextView) findViewById(R.id.tv_message)).setText(getString(R.string.benificary_default_text));

                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BeneficiaryData> beneficiaryData = (ArrayList<BeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryData>>() {
                                }.getType());
                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    if (startCountArex.equalsIgnoreCase("1")) {
                                        recyclerAdapterValue.clear();
                                    }
                                    startCountArex = response.getString(Constants.NEXT_RECORD);
                                    if (beneficiaryData.size() > 1)
                                        btnArexArrow.setVisibility(View.VISIBLE);
                                    else
                                        btnArexArrow.setVisibility(View.GONE);
                                    recyclerAdapterValue.addArrayList(beneficiaryData);
                                    multiStateViewValue.setViewState(MultiStateView.VIEW_STATE_CONTENT);

                                } else {
                                    setViewState(sType, VIEW_STATE_EMPTY);
                                }
                            } else {
                                setViewState(sType, VIEW_STATE_EMPTY);
                            }
                        } else {
                            setViewState(sType, VIEW_STATE_EMPTY);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(sType, VIEW_STATE_WRONG);
                    }
                } else {
                    setViewState(sType, VIEW_STATE_WRONG);
                }
                break;

//--------------------------------------------------------------------------------------------------
            case FETCH_BENEFICIARY_DETAILS:
                if (status == 1) {
                    try {
                        CommonUtils.hideLoading();
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            findViewById(R.id.fab).setVisibility(View.GONE);
                            findViewById(R.id.click_to_change).setVisibility(View.GONE);
                            layoutWesternUnion.setVisibility(View.GONE);
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BeneficiaryData> beneficiaryData = (ArrayList<BeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryData>>() {
                                }.getType());

                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    if (startCountArex.equalsIgnoreCase("1")) {
                                        recyclerAdapterValue.clear();
                                    }
                                    startCountArex = response.getString(Constants.NEXT_RECORD);
                                    if (beneficiaryData.size() > 1)
                                        btnArexArrow.setVisibility(View.VISIBLE);
                                    else
                                        btnArexArrow.setVisibility(View.GONE);
                                    recyclerAdapterValue.addArrayList(beneficiaryData);
                                    multiStateViewValue.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    //((TextView) findViewById(R.id.tv_message)).setText(response.getString(Constants.MESSAGE));
                                    layoutWesternUnion.setVisibility(View.GONE);
                                    mSelectDetails = beneficiaryData.get(0);
                                    if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                                        layoutService.setVisibility(View.VISIBLE);
                                        layoutSendMoney.setVisibility(View.GONE);
                                        layoutWesternUnion.setVisibility(View.GONE);
                                        layoutMessage.setVisibility(View.GONE);
                                        memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                                        bankCodeAll = mSelectDetails.getBankData().getBankCodeCE();
                                        screenType = "CE";
                                        ceMode = 1;
                                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                                        layoutService.setVisibility(View.GONE);
                                        layoutSendMoney.setVisibility(View.VISIBLE);
                                        layoutWesternUnion.setVisibility(View.GONE);
                                        layoutMessage.setVisibility(View.GONE);
                                        screenType = "AREX";
                                        // ceMode=0;
                                        memPkIdAll = (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                                        bankCodeAll = mSelectDetails.getBankData().getBankCodeAREX();
                                    }

                                    if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                                        if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtValue());
                                        } else {
                                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitBtInstant());
                                        }

                                    } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                                        if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceBtDesc());
                                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpValue());
                                        } else {
                                            tvTopMessage.setText(mSelectDetails.getBankData().getServiceCpDesc());
                                            tvBottomMessage.setText(mSelectDetails.getBankData().getRemitCpInstant());
                                        }

                                    }


                                    Log.e("Test2e3 ", "" + beneficiaryData.get(0));
                                    Log.e("Test2e3 ", "" + mSelectDetails.getModuleName());
                                    Log.e("Test2e3", "" + mSelectDetails.getBankData().getBankCodeCE());
                                    Log.e("Test2e3", "" + mSelectDetails.getBeneficiaryId());
                                    Log.e("Test2e3", "" + mSelectDetails.getCountryCode());
                                    Log.e("Test2e3", "" + mSelectDetails.getBankData().getAREX_BT());
                                    Log.e("Test2e3", "" + mSelectDetails.getModuleName());
                                    Log.e("Test2e3", "" + mSelectDetails.getBeneficiaryId());
                                    Log.e("Test2e3", "" + mSelectDetails.getModuleName());
                                    Log.e("Test2e3", " " + mSelectDetails.getServiceTypeData().getMapping());

                                    selectionQuickSend = beneficiaryData.get(0);
                                    setCurrencyOnCreated();
                                    CommonUtils.setCountryFlagImage(context, ivFlag, selectionQuickSend.getCountryData().getFlag());
                                    tvCountry.setText("(" + selectionQuickSend.getCountryData().getLatinName() + ")");


                                } else {
                                    setViewState(sType, VIEW_STATE_EMPTY);


                                }
                            } else {
                                setViewState(sType, VIEW_STATE_EMPTY);

                            }
                        } else {
                            setViewState(sType, VIEW_STATE_EMPTY);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(sType, VIEW_STATE_WRONG);

                    }
                } else {
                    setViewState(sType, VIEW_STATE_WRONG);
                }
                break;
//-------------------------------------------CheckApi()---------------------------------------------
            case LOAD_SERVICE_TYPE_LIST:
                if (status == 1) {
                    try {
                        CommonUtils.hideLoading();
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<RESULTDTO> branchData = (ArrayList<RESULTDTO>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<RESULTDTO>>() {
                                }.getType());
                                if (branchData != null && branchData.size() > 0) {
                                    if(branchData.size() == 1){
                                        tvServiceType.setText(branchData.get(0).getDisplayValue());
                                        ceMode = 1;
                                        ceServiceId = branchData.get(0).getDisplayKey();
                                        layoutSendMoney.setVisibility(View.VISIBLE);
                                        layoutWesternUnion.setVisibility(View.GONE);
                                        layoutMessage.setVisibility(View.GONE);
                                        performSmoothScroll();
                                        return;

                                    }else{
                                        Intent i = new Intent(context, SelectItemActivity.class);
                                        i.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD);
                                        i.putExtra(Constants.ID, Integer.valueOf(70));
                                        i.putExtra(Constants.TITLE, Constants.SERVICE_TYPE);
                                        i.putExtra(Constants.BENEF_PK_ID, mSelectDetails.getBeneficiaryId());
                                        i.putExtra(Constants.COUNTRY_CODE, mSelectDetails.getCountryCode());
                                        startActivityForResult(i, Constants.SELECT_ITEM_LOAD_SERVICE_TYPE_FIELD);
                                        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                                        return;
                                    }


                                }
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;

        }
    }

    private void onSuccess() {
        if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
            Intent intent = new Intent(context, AdditionalInfoSelectModeActivity.class);

            dataObject = mSelectDetails;
            dataObject.setVat(vat);
            dataObject.setDiscount(discount);
            dataObject.setRoundoff(rounding);


            dataObject.setServiceTypeData(mSelectDetails.getServiceTypeData());
            // dataObject.setTransferType(mSelectDetails.getTransferType());
            dataObject.setBankData(mSelectDetails.getBankData());
            String transferType = "";
            if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            }
            dataObject.setTransferType(transferType);
            intent.putExtra(Constants.MODE_NAME, name);
            intent.putExtra(Constants.MODE_PK_ID, id);
            intent.putExtra(Constants.TRANSFER_TYPE, transferType);
            intent.putExtra(Constants.OBJECT, dataObject);
            intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            intent.putExtra(Constants.TOTAL_RECIEVE_CURRENCY, selectedCurrencyData.getName());
            intent.putExtra(Constants.TOTAL_RECIEVE, etGet.getText().toString().trim());
            intent.putExtra(Constants.PAYMENT_DATA, jsonObjectRequestPayment.toString());
            intent.putExtra(Constants.DESCRIPTION, modeDescription);
            intent.putExtra(Constants.CE_SERVICE_ID, ceServiceId);
            intent.putExtra(Constants.RESPONSE_INTENT, jsonObjectInputChectTxnList.toString());
            intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
            intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
            intent.putExtra(Constants.CCY_CODE,ccyCode);
            intent.putExtra(Constants.CCY_ID,ccyCode);
            startActivity(intent);
        } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
            Intent intent = new Intent(context, AdditionalInfoCESelectModeActivity.class);
            dataObject = mSelectDetails;
            dataObject.setVat(vat);
            dataObject.setDiscount(discount);
            dataObject.setRoundoff(rounding);

            dataObject.setServiceTypeData(mSelectDetails.getServiceTypeData());

            dataObject.setBankData(mSelectDetails.getBankData());

            String transferType = "";
            if (mSelectDetails.getModuleName().equalsIgnoreCase("AREX")) {
                if (mSelectDetails.getBankData().getAREX_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            } else if (mSelectDetails.getModuleName().equalsIgnoreCase("CE")) {
                if (mSelectDetails.getBankData().getCE_BT().equalsIgnoreCase("1")) {
                    transferType = "BT";
                } else {
                    transferType = "CP";
                }

            }
            dataObject.setTransferType(transferType);
            intent.putExtra(Constants.MODE_NAME, name);
            intent.putExtra(Constants.MODE_PK_ID, id);
            intent.putExtra(Constants.TRANSFER_TYPE, transferType);
            intent.putExtra(Constants.OBJECT, dataObject);
            intent.putExtra(Constants.SOURCE_TYPE, Constants.TYPE_SEND_MONEY);
            intent.putExtra(Constants.TOTAL_RECIEVE_CURRENCY, selectedCurrencyData.getName());
            intent.putExtra(Constants.TOTAL_RECIEVE, etGet.getText().toString().trim());
            intent.putExtra(Constants.TOTAL_AMOUNT_PP, TOTAL_AMOUNT_PP);
            intent.putExtra(Constants.PAYMENT_DATA, jsonObjectRequestPayment.toString());
            intent.putExtra(Constants.DESCRIPTION, modeDescription);
            intent.putExtra(Constants.CE_SERVICE_ID, ceServiceId);
            intent.putExtra(Constants.RESPONSE_INTENT, jsonObjectInputChectTxnList.toString());
            intent.putExtra(Constants.TOTAL_PRIORITY_PAY_CHARGES_PP, TOTAL_PRIORITY_PAY_CHARGES_PP);
            intent.putExtra(Constants.CCY_CODE,ccyCode);
            intent.putExtra(Constants.CCY_ID,ccyCode);
            startActivity(intent);


        }
    }

    private void onBeneficiaryMissingFields(JSONArray jsonArray, String fieldId, String message) {
        LogUtils.d("ok", "On Beneficiary Fields Missing");
        if (mSelectDetails.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING)) {
            intent = new Intent(context, UpdateExistingBeneficiaryActivity.class);
            intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<BeneficiaryDynamicFields>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeneficiaryDynamicFields>>() {
            }.getType()));
        } else {
            intent = new Intent(context, UpdateExistingBeneficiaryCeActivity.class);
            intent.putParcelableArrayListExtra(Constants.LIST, (ArrayList<BeneficiaryDynamicFieldsCeNew>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeneficiaryDynamicFieldsCeNew>>() {
            }.getType()));
        }
        dataObject = mSelectDetails;
        //Added
        dataObject.setServiceTypeData(mSelectDetails.getServiceTypeData());
        //
        intent.putExtra(Constants.OBJECT, dataObject);
        intent.putExtra(Constants.ID, fieldId);
        intent.putExtra(Constants.MESSAGE, message);
        startActivityForResult(intent, Constants.UPDATE_MISSING_FIELD_BENEFICIARY);
    }

    private void setCurrencyList(ArrayList<CountryData.CurrencyData> currencyList) {
        LogUtils.d("ok", "Set CE Currency " + currencyList.size());
        recyclerAdapterCurrencyCode.addArrayList(currencyList);
        setDefaultCurrency();
    }

    private void onCurrencyListEmpty() {
        recyclerAdapterCurrencyCode.clear();
        selectedCurrencyData = null;

    }

    private void performSmoothScroll() {
        try {
            nestedScrollview.post(new Runnable() {
                @Override
                public void run() {
                    nestedScrollview.smoothScrollTo(0, layoutWesternUnion.getTop());
                }
            });
            /*nestedScrollview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nestedScrollview.smoothScrollTo(0, multiStateView.getTop());
                }
            }, 2000);*/
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void onAPIError() {
        try {
            if (context != null)
                Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
            onErrorInCalculation();
        } catch (Exception ex) {

        }
    }

    private void setRateData(String currentRate) {
        if (currentRate != null) {
            //TODO Use String
            tvRate.setVisibility(View.VISIBLE);
            try {
                //tvRate.setText("Exchange Rate AED = " + selectedCurrencyData.getName() + " " + currentRate);
                tvRate.setText("Exchange Rate "+ " " + currentRate);


            } catch (Exception ex) {
                tvRate.setVisibility(View.GONE);
            }
        } else {
            tvRate.setVisibility(View.GONE);
        }
    }

    private void setViewState(CommonUtils.SERVICE_TYPE sType, int viewState) {
        //if (recyclerAdapterValue.getItemCount() == 0) {
        if (viewState == VIEW_STATE_EMPTY) {
            valueEmptyButton.setVisibility(View.GONE);
            CommonUtils.setViewState(multiStateViewValue, viewState, tvEmptyValue, null, sType == FETCH_BENEFICIARY_INSTANT ? getString(R.string.empty_beneficiary_arex) : getString(R.string.empty_beneficiary_ce));
        }
        if (viewState == VIEW_STATE_WRONG)
            CommonUtils.setViewState(multiStateViewValue, viewState, tvErrorValue, null, getString(R.string.error_beneficiary));
        if (viewState == VIEW_STATE_ERROR)
            CommonUtils.setViewState(multiStateViewValue, viewState, tvErrorValue, null, null);
        //}
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
        if (CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    private void offScreen() {
        layoutpayment.setVisibility(View.GONE);
        layoutBottom.setVisibility(View.GONE);
        layoutWesternUnion.setVisibility(View.VISIBLE);
        layoutSendMoney.setVisibility(View.GONE);
        layoutService.setVisibility(View.GONE);
        layoutMessage.setVisibility(View.VISIBLE);
        tvRate.setVisibility(View.GONE);
        resetAll();
        hideVat();
    }

    private void resetAll() {
        etGet.setText("");
        etSend.setText("");
        tvPaymentType.setText("");
        tvServiceType.setText("");
        ivSuccess.setVisibility(View.GONE);

    }

    public void showLimitDialog(Context context, String message) {
        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);

        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Transaction Alert!");
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(message);

        myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
        myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText("OK");

        myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSuccess();
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
}
