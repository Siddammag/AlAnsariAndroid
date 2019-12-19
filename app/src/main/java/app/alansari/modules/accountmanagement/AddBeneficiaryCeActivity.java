package app.alansari.modules.accountmanagement;

import android.Manifest;
import android.animation.ObjectAnimator;
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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.ChangePinActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.progressbutton.CircularProgressButton;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AccountTypeData;
import app.alansari.models.BankData;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.BranchData;
import app.alansari.models.CountryData;
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
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 12 July, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBeneficiaryCeActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener {

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Button btnNext;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private CircularProgressButton btnSave;
    private TextView tvBankName, tvCountryName;
    private ImageView ivAccountLoading, ivAccountArrow;
    private Animation slideUpAnimation, slideDownAnimation;
    private View topInfoLayout, confirmLayout, confirmLayoutBg;
    private LinearLayout accountExpandLayout, confirmDynamicLayout;
    private CircleImageView profilePic;
    private RelativeLayout picLayout;
    private Dialog imagePickDialog;
    //dynamic Layout
    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private Intent intent;
    private Context context;
    private JSONObject jsonObject;
    private boolean isUpdate, isAutoFill;
    private String sourceType, ceMemId;
    private BankData selectedBank;
    private BranchData selectedBranch;
    private AccountTypeData selectedAccountType;
    private BeneficiaryData dataObject, beneficiaryData;
    private boolean personalLayoutStatus, accountLayoutStatus;
    private CountryData selectedCountry, selectedNationality;
    private ArrayList<BeneficiaryDynamicFieldsCe> beneficiaryDynamicFieldsCeList;
    private String base64 = "";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.add_beneficiary_ce_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("New");
        ((TextView) findViewById(app.alansari.R.id.toolbar_title_2)).setText("Beneficiary");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, app.alansari.R.drawable.am_beneficiary_header_bg));
        ((AppCompatImageView) findViewById(app.alansari.R.id.toolbar_right_icon)).setImageResource(app.alansari.R.drawable.svg_am_beneficiary_icon);
        ((CircleImageView) findViewById(app.alansari.R.id.beneficiary_pic)).setImageResource(R.drawable.ic_new_user);
        //TODO Remove Line
        findViewById(app.alansari.R.id.toolbar_right_icon).setOnClickListener(this);
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, app.alansari.R.color.color086687));

        init();
        Toolbar toolbarConfirm = (Toolbar) confirmLayout.findViewById(app.alansari.R.id.toolbar);
        ((TextView) toolbarConfirm.findViewById(app.alansari.R.id.toolbar_title)).setText("Beneficiary Confirmation");
        toolbarConfirm.setNavigationIcon(app.alansari.R.drawable.ic_back_arrow);
        toolbarConfirm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });

        ceMemId = CommonUtils.getMemPkId(Constants.CE_MAPPING);
        if (CommonUtils.getUserMobile() == null) {
            CommonUtils.registerAgain(context);
        }

        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_BENEFICIARY);
            if (dataObject != null) {
                if (sourceType.equalsIgnoreCase(Constants.TYPE_BENEFICIARY) && dataObject.getBeneficiaryId() != null && dataObject.getBeneficiaryId().length() > 0) {
                    isUpdate = true;
                }
                selectedCountry = dataObject.getCountryData();
                selectedBank = dataObject.getBankData();
                fetchBeneficiaryFields();
                return;
            }
        }
        onBackPressed();
    }

    private void init() {
        picLayout = (RelativeLayout) findViewById(R.id.profile_pic_layout);
        profilePic = (CircleImageView) findViewById(R.id.beneficiary_pic);
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        topInfoLayout = findViewById(app.alansari.R.id.top_info_layout);
        accountExpandLayout = (LinearLayout) findViewById(app.alansari.R.id.account_info_layout);

        ivAccountLoading = (ImageView) findViewById(app.alansari.R.id.account_loading_image);
        ivAccountArrow = (ImageView) findViewById(app.alansari.R.id.account_arrow_image);
        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);
        findViewById(app.alansari.R.id.account_title_layout).setOnClickListener(this);
        btnNext.setOnClickListener(this);
        picLayout.setOnClickListener(this);
        picLayout.setVisibility(View.GONE);

        //Confirm Layout
        confirmLayout = findViewById(app.alansari.R.id.add_beneficiary_confirm_layout);
        confirmLayoutBg = findViewById(app.alansari.R.id.add_beneficiary_confirm_bg);


        confirmDynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.confirm_dynamic_layout);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name_c);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name_c);


        btnSave = (CircularProgressButton) findViewById(app.alansari.R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
        btnSave.setOnClickListener(this);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_down_animation);

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

    private void onFinish() {
        Intent data = new Intent();
        if (confirmLayout.getVisibility() == View.VISIBLE) {
            if (btnSave.getProgress() == 100) {
                if (beneficiaryData != null) {
                    if (isUpdate)
                        beneficiaryData = null;
                    data.putExtra(Constants.OBJECT, beneficiaryData);
                    setResult(AppCompatActivity.RESULT_OK, data);
                } else {
                    setResult(AppCompatActivity.RESULT_CANCELED, data);
                }
                finish();
            } else if (btnSave.getProgress() == 0 || btnSave.getProgress() == -1) {
                setConfirmLayoutVisibility(false);
                btnNext.setVisibility(View.VISIBLE);
            }
        } else {
            setResult(AppCompatActivity.RESULT_CANCELED, data);
            finish();
        }
    }    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (validated && !isAutoFill) {
            inputLayout.setError(((BeneficiaryDynamicFieldsCe) editText.getTag()).getErrorMessage());
            inputLayout.setErrorEnabled(true);
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        }
    }

    private void fetchBeneficiaryFields() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiaryFieldsCe(CommonUtils.getMemPkId(Constants.CE_MAPPING), dataObject.getTransferType(), dataObject.getCountryData().getCountryCodeCE(), dataObject.getBankData().getBankCodeCE(), dataObject.getBeneficiaryType(), userPkId, LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_BENEFICIARY_FIELDS_CE_URL, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_FIELDS_CE, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_FIELDS_CE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_FIELDS_CE.toString());
        } else {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddBeneficiaryCeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(AddBeneficiaryCeActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (AddBeneficiaryCeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (AddBeneficiaryCeActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(AddBeneficiaryCeActivity.this.findViewById(android.R.id.content),
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
    }    private boolean checkLayoutFilledStatus(EditText[] dynamicEditTextTemp, TextInputLayout[] dynamicInputLayoutTemp, boolean showMessage) {
        boolean specialCharacterValidation = true;

        if (dynamicEditTextTemp == null) {
            dynamicEditTextTemp = this.dynamicEditText;
            dynamicInputLayoutTemp = this.dynamicInputLayout;
        }

        // Check Min And Max Length
        for (int i = 0; i < dynamicEditTextTemp.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditTextTemp[i]) && dynamicEditTextTemp[i].getTag() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditTextTemp[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFieldsCe) dynamicEditTextTemp[i].getTag()).getFieldType().equalsIgnoreCase("T"))
                if (!Validation.validateSpecialCharacters(dynamicEditTextTemp[i], dynamicInputLayoutTemp[i])) {
                    specialCharacterValidation = false;
                    continue;
                }

            if ((Validation.isValidEditTextValue(dynamicEditTextTemp[i])) && ((getEditTextMinLength(dynamicEditTextTemp[i]) == 0) || dynamicEditTextTemp[i].getText().toString().trim().length() >= getEditTextMinLength(dynamicEditTextTemp[i])) && ((getEditTextLength(dynamicEditTextTemp[i]) == 0) || dynamicEditTextTemp[i].getText().toString().trim().length() <= getEditTextLength(dynamicEditTextTemp[i]))) {
                accountLayoutStatus = true;
                setErrorLayout(showMessage, dynamicEditTextTemp[i], dynamicInputLayoutTemp[i]);
            } else {
                accountLayoutStatus = false;
                setErrorLayout(false, dynamicEditTextTemp[i], dynamicInputLayoutTemp[i]);
                break;
            }
        }

        if ((showMessage && dynamicEditTextTemp.length == 1 && specialCharacterValidation && accountLayoutStatus)) {
            LogUtils.d("ok", "YEs Return");
            return true;
        }

        if (specialCharacterValidation && accountLayoutStatus) {
            ivAccountLoading.setImageResource(app.alansari.R.drawable.ic_success);
        } else {
            ivAccountLoading.setImageResource(app.alansari.R.drawable.ic_loading);
        }

        if (specialCharacterValidation && accountLayoutStatus)
            btnNext.setEnabled(true);
        else
            btnNext.setEnabled(false);

        return accountLayoutStatus;
    }

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(AddBeneficiaryCeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddBeneficiaryCeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    private void setConfirmLayoutVisibility(boolean visibility) {
        confirmLayout.setVisibility(View.VISIBLE);
        if (visibility) {
            confirmLayout.startAnimation(slideDownAnimation);
        } else {
            confirmLayout.startAnimation(slideUpAnimation);
            confirmLayout.setVisibility(View.GONE);
        }
        confirmLayoutBg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(confirmLayoutBg, "alpha", visibility ? 0f : 1f, visibility ? 1f : 0f).setDuration(600).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                fetchBeneficiaryFields();
                break;
            case app.alansari.R.id.account_title_layout:
                if (accountExpandLayout.getVisibility() == View.VISIBLE) {
                    ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
                    accountExpandLayout.setVisibility(View.GONE);
                } else {
                    accountExpandLayout.setVisibility(View.VISIBLE);
                    ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_up_arrow);
                }
                checkLayoutFilledStatus(null, null, false);
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (view.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFieldsCe) view.getTag()).getId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_7)) || fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_50))) {
                        intent = new Intent(context, SelectCountryFlagActivity.class);
                        intent.putExtra("hideFirstItem", true);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_COUNTRY);
                        intent.putExtra(Constants.IS_FOR_NATIONANLITY, true);
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCe) view.getTag()).getId()));
                    } else if (((BeneficiaryDynamicFieldsCe) view.getTag()).getId().equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_38))) {
                        break;
                    } else {
                        intent = new Intent(context, AddBeneficiaryCeDropdownActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, ((BeneficiaryDynamicFieldsCe) view.getTag()).getId());
                        intent.putExtra(Constants.TITLE, ((BeneficiaryDynamicFieldsCe) view.getTag()).getFieldName());
                        if (fieldId.equalsIgnoreCase(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_44))) {
                            intent.putExtra(Constants.BANK_CODE, dataObject.getBankData().getBankCodeCE());
                            intent.putExtra(Constants.TRANSFER_TYPE, dataObject.getTransferType());
                        }
                        startActivityForResult(intent, Integer.valueOf(((BeneficiaryDynamicFieldsCe) view.getTag()).getId()));
                    }
                    overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                }
                break;
            case app.alansari.R.id.next_btn:
                validateData();
                break;
            case app.alansari.R.id.profile_pic_layout:
                imagePickDialog.show();
                break;
            case app.alansari.R.id.dialog_btn:
                if (btnSave.getProgress() == -1) {
                    btnSave.setProgress(0);
                    generateOTP();
                } else if (btnSave.getProgress() == 100) {
                    onFinish();
                } else if (btnSave.getProgress() > 0 && btnSave.getProgress() < 100) {
                } else {
                    generateOTP();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

    private void verifyOTP() {
        intent = new Intent(context, ChangePinActivity.class);
        intent.putExtra(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_OTP);
        intent.putExtra(Constants.TYPE, Constants.NO_PIN);
        intent.putExtra(Constants.USER_STATUS, Constants.VOTP);
        intent.putExtra(Constants.MOBILE_NUM, CommonUtils.getUserMobile());
        startActivityForResult(intent, Constants.VOTP_CODE);
    }

    private void confirmData() {
        btnSave.setProgress(0);
        btnNext.setVisibility(View.GONE);
        setConfirmLayoutVisibility(true);
        tvBankName.setText(selectedBank.getBankName());
        tvCountryName.setText(selectedCountry.getLatinName());
        try {
            confirmDynamicLayout.removeAllViews();
            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_ce_confirm_dialog_layout, null);
                TextView label = (TextView) childLayout.findViewById(app.alansari.R.id.label);
                TextView text = (TextView) childLayout.findViewById(app.alansari.R.id.text);
                label.setText(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                text.setText(dynamicEditText[i].getText().toString().trim());
                confirmDynamicLayout.addView(childLayout);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setInitialData() {
        try {
            isAutoFill = true;
            if (dataObject != null && (sourceType.equalsIgnoreCase(Constants.TYPE_SEND_MONEY) || isUpdate)) {
                if (dataObject.getName() != null && dataObject.getName().length() > 0) {
                    int position = getPositionFromId("41"); //First Name
                    if (position != -1 && dynamicEditText != null)
                        setDataToEditText(position, dataObject.getName().split(" ")[0], null);
                    position = getPositionFromId("42");  //Last Name
                    if (position != -1 && dynamicEditText != null)
                        setDataToEditText(position, (dataObject.getName().split(" ").length > 1) ? dataObject.getName().split(" ")[1] : null, null);
                }

                if (dataObject.getAccountNumber() != null && dataObject.getAccountNumber().length() > 0) {
                    int position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_46)); //Acc Number
                    if (position != -1 && dynamicEditText != null) {
                        setDataToEditText(position, dataObject.getAccountNumber(), null);
                        dynamicEditText[position].setEnabled(false);
                    }
                }

                if (dataObject.getBranchName() != null && dataObject.getBranchName().length() > 0) {
                    int position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_44)); //Branch Name
                    if (position != -1 && dynamicEditText != null) {
//                        setDataToEditText(position, dataObject.getBranchName(), null);
                        dynamicEditText[position].setEnabled(true);
                    }
                }

                if (dataObject.getNationality() != null && dataObject.getNationalityId() != null) {
                    int position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_7)); //Nationality
                    if (position == -1)
                        position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_50));
                    if (position != -1 && dynamicEditText != null) {
                        selectedNationality = new CountryData();
                        selectedNationality.setLatinName(dataObject.getNationality());
                        selectedNationality.setArabicName(dataObject.getNationality());
                        selectedNationality.setNationality(dataObject.getNationality());
                        selectedNationality.setCountryCodeCE(dataObject.getNationalityId());
                        setDataToEditText(position, selectedNationality.getLatinName(), selectedNationality.getCountryCodeCE());
                    }
                }


            } else {
                selectedCountry = dataObject.getCountryData();
                //etCountry.setText(dataObject.getCountryData().getLatinName());
                selectedBank = dataObject.getBankData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            checkLayoutFilledStatus(null, null, false);
            isAutoFill = false;
        }
    }    private void generateOTP() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionId = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTPAfterLogin(CommonUtils.getUserMobile(), userPkId, LogoutCalling.getDeviceID(context),sessionId), Constants.RESEND_OTP_URL_WEBSERVICE, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
    }

    private void updateDynamicFields() {
        if (!Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
            return;
        }
        try {
            dynamicEditText = new EditText[beneficiaryDynamicFieldsCeList.size()];
            dynamicInputLayout = new TextInputLayout[beneficiaryDynamicFieldsCeList.size()];
            accountExpandLayout.removeAllViews();
            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(app.alansari.R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(app.alansari.R.id.edit_text);
                if (i == beneficiaryDynamicFieldsCeList.size() - 1)
                    childLayout.findViewById(app.alansari.R.id.divider).setVisibility(View.INVISIBLE);

                if (beneficiaryDynamicFieldsCeList.get(i).getMinLength() == null || Integer.valueOf(beneficiaryDynamicFieldsCeList.get(i).getMinLength()) == 0) {
                    beneficiaryDynamicFieldsCeList.get(i).setMinLength("1");
                }
                if (beneficiaryDynamicFieldsCeList.get(i).getLength() == null) {
                    beneficiaryDynamicFieldsCeList.get(i).setLength("0");
                }
                if (!beneficiaryDynamicFieldsCeList.get(i).getFieldType().equalsIgnoreCase("D")) {
                    beneficiaryDynamicFieldsCeList.get(i).setErrorMessage((checkValidBeneficiaryString(beneficiaryDynamicFieldsCeList.get(i).getLength()) ? "Required length is " + beneficiaryDynamicFieldsCeList.get(i).getLength() : "Invalid " + beneficiaryDynamicFieldsCeList.get(i).getFieldName().toLowerCase()));
                }
                editText.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                inputLayout.setHint(beneficiaryDynamicFieldsCeList.get(i).getFieldName());
                editText.setTag(beneficiaryDynamicFieldsCeList.get(i));
                inputLayout.setTag(i);
                setFieldType(editText, inputLayout, beneficiaryDynamicFieldsCeList.get(i));
                dynamicEditText[i] = editText;
                dynamicInputLayout[i] = inputLayout;
                accountExpandLayout.addView(childLayout);
            }
            setInitialData();
            //checkLayoutFilledStatus(null, null, false);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
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
    }    private void validateData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            addBeneficiaryJsonFormat();
            try {
                jsonObject.put(Constants.ACTION, Constants.BENEFICIARY_ACTION_VALIDATE);
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
                jsonObject.put(Constants.SESSION_ID, sessionTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.ADD_BENEFICIARY_CE_URL, CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
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
            case FETCH_BENEFICIARY_FIELDS_CE:
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getString(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                beneficiaryDynamicFieldsCeList = (ArrayList<BeneficiaryDynamicFieldsCe>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryDynamicFieldsCe>>() {
                                }.getType());
                                if (Validation.isValidList(beneficiaryDynamicFieldsCeList)) {
                                    updateDynamicFields();
                                } else {
                                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                                }
                            } else {
                                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, null, null);
                            }
                        } else {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                        }
                    } else {
                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, null, null);
                }
                break;
            case RESEND_OTP:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            verifyOTP();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE) && response.getString(Constants.STATUS_CODE).equalsIgnoreCase("4032")) {
                            btnSave.setProgress(0);
                            submitData();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.OTP_FAILURE)) {
                            Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, getString(app.alansari.R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
                }
                break;
            case VALIDATE_BENEFICIARY_CE:
            case ADD_BENEFICIARY_CE:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (sType == CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY_CE) {
                                confirmData();
                                return;
                            }
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BeneficiaryData> beneficiaryData = (ArrayList<BeneficiaryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryData>>() {
                                }.getType());
                                if (beneficiaryData != null && beneficiaryData.size() > 0) {
                                    btnSave.setProgress(100);
                                    this.beneficiaryData = beneficiaryData.get(0);
                                    this.beneficiaryData.setTransferType(dataObject.getTransferType());
                                    return;
                                }
                            }
                            onError();
                        } else if (response.getString(Constants.STATUS_MSG).equals(Constants.FAILURE)) {
                            if (sType == CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE) {
                                btnSave.setProgress(-1);
                                onFinish();
                            }
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
        btnSave.setProgress(-1);
        Toast.makeText(context, getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Constants.VOTP_CODE) {
                    btnSave.setProgress(0);
                    submitData();
                } else if (requestCode == Constants.ADD_NEW_BENEFICIARY_CONFIRM) {
                    if (dataObject != null) {
                        data = new Intent();
                        data.putExtra(Constants.OBJECT, dataObject);
                        setResult(AppCompatActivity.RESULT_OK, data);
                        onBackPressed();
                    } else {
                        setResult(AppCompatActivity.RESULT_CANCELED, null);
                    }
                    return;
                } else if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                        selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                        // if (selectedCountry != null)
                        //  etCountry.setText(selectedCountry.getLatinName());
                        return;
                    }
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    switch (requestCode) {
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_7:
                        case Constants.SELECT_ITEM_BENF_FIELD_ID_50:
                            selectedNationality = data.getParcelableExtra(Constants.OBJECT);
                            if (position != -1)
                                setDataToEditText(position, ((CountryData) object).getLatinName(), ((CountryData) object).getCountryCodeCE());
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
                } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedImage = (Bitmap) extras.get("data");
                    Uri imageUri = CommonUtils.getImageUri(getApplicationContext(), selectedImage);
                    Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getRealPathFromURI(context, imageUri));
                    profilePic.setImageBitmap(newBitmap);
                    base64 = CommonUtils.encodeTobase64(newBitmap);
                } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream, null, options);
                        Bitmap newBitmap = CommonUtils.modifyOrientation(selectedImage, CommonUtils.getPath(imageUri, context));
                        profilePic.setImageBitmap(newBitmap);
                        base64 = CommonUtils.encodeTobase64(newBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
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
                        Snackbar.make(AddBeneficiaryCeActivity.this.findViewById(android.R.id.content),
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
                    Snackbar.make(AddBeneficiaryCeActivity.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(AddBeneficiaryCeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
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
    }    private int getEditTextLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    public void pickPhotos() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_IMAGE_PICK);
    }    private int getEditTextMinLength(EditText editText) {
        try {
            return Integer.valueOf(((BeneficiaryDynamicFieldsCe) editText.getTag()).getMinLength());
        } catch (Exception ex) {
            return 0;
        }
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            btnSave.setProgress(50);
            if (jsonObject == null)
                addBeneficiaryJsonFormat();
            try {
                jsonObject.put(Constants.ACTION, Constants.BENEFICIARY_ACTION_ADD);
                String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
                jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
                jsonObject.put(Constants.SESSION_ID, sessionTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.ADD_BENEFICIARY_CE_URL, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY_CE.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
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

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }





    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private JSONObject addBeneficiaryJsonFormat() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.CE_MAPPING));
            if (isUpdate) {
                jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            }
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue(dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE()));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.BANK_CODE, checkValue(dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getBankData().getBankCodeAREX() : dataObject.getBankData().getBankCodeCE()));
            jsonObject.put(Constants.BANK_NAME, checkValue(dataObject.getBankData().getBankName()));
            for (int i = 0; i < beneficiaryDynamicFieldsCeList.size(); i++) {
                try {
                    LogUtils.d("Input", beneficiaryDynamicFieldsCeList.get(i).getId() + " : " + checkValue(dynamicEditText[i].getText().toString().trim()));
                    jsonObject.put(beneficiaryDynamicFieldsCeList.get(i).getId(), checkValue(dynamicEditText[i].getText().toString().trim()));
                    if (beneficiaryDynamicFieldsCeList.get(i).getFieldType().equalsIgnoreCase("D") && dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                        jsonObject.put(beneficiaryDynamicFieldsCeList.get(i).getId() + "_CODE", checkValue(dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString().trim()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (base64 != null && !TextUtils.isEmpty(base64))
                jsonObject.put(Constants.BENF_IMAGE, base64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiaryCe :-  " + jsonObject.toString());
        return jsonObject;
    }










}
