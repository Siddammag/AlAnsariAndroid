package app.alansari.modules.accountmanagement;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import app.alansari.AppController;
import app.alansari.ChangePinActivity;
import app.alansari.R;
import app.alansari.SelectCountryFlagActivity;
import app.alansari.SelectItemActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.DatePickerFragment;
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
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_BENEFICIARY_FIELDS_AREX;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.VALIDATE_BENEFICIARY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 27 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBeneficiaryActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, DatePickerDialog.OnDateSetListener, LogOutTimerUtil.LogOutListener {

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private Button btnNext;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private CircularProgressButton btnSave;
    private TextView tvNameTop, tvCountryTop;
    private View topInfoLayout, accountCardView, confirmLayout, confirmLayoutBg;
    private TextView tvAccountName, tvBankName, tvCountryName;
    private EditText etFirstName, etLastName, etCountry, etNationality, etArabicName, etCompanyName, etBeneficiaryType;
    private TextInputLayout firstNameLayout, lastNameLayout, countryLayout, nationalityLayout, arabicLayout, companyLayout, benTypeLayout;
    private ImageView ivPersonalLoading, ivPersonalArrow, ivAccountLoading, ivAccountArrow;
    private LinearLayout personalExpandLayout, accountExpandLayout, confirmDynamicLayout;
    private CircleImageView profilePic;
    private RelativeLayout picLayout;
    private Dialog imagePickDialog;
    //Dyanmic
    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<BeneficiaryDynamicFields> beneficiaryDynamicFields;
    //Variables
    private Intent intent;
    private Context context;
    private JSONObject jsonObject;
    private String sourceType, selectedDOB, selectedDateOfBirth;
    private boolean isUpdate, isAutoFill, personalLayoutStatus, accountLayoutStatus;
    private BankData selectedBank;
    private BranchData selectedBranch;
    private BeneficiaryData dataObject, beneficiaryData;
    private AccountTypeData selectedAccountType;
    private CountryData selectedCountry, selectedNationality;
    private Animation slideUpAnimation, slideDownAnimation;
    private String base64 = "";
    private TextView tvLabel;

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

    private void init() {
        picLayout = (RelativeLayout) findViewById(R.id.profile_pic_layout);
        profilePic = (CircleImageView) findViewById(R.id.beneficiary_pic);
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiStateView);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        topInfoLayout = findViewById(app.alansari.R.id.top_info_layout);
        personalExpandLayout = (LinearLayout) findViewById(app.alansari.R.id.personal_info_layout);
        accountExpandLayout = (LinearLayout) findViewById(app.alansari.R.id.account_info_layout);
        tvNameTop = (TextView) findViewById(app.alansari.R.id.name_top);
        tvCountryTop = (TextView) findViewById(app.alansari.R.id.country_top);
        tvLabel=(TextView)findViewById(app.alansari.R.id.text_view_label);

        ivPersonalLoading = (ImageView) findViewById(app.alansari.R.id.personal_loading_image);
        ivPersonalArrow = (ImageView) findViewById(app.alansari.R.id.personal_arrow_image);
        ivAccountLoading = (ImageView) findViewById(app.alansari.R.id.account_loading_image);
        ivAccountArrow = (ImageView) findViewById(app.alansari.R.id.account_arrow_image);

        firstNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.first_name_layout);
        lastNameLayout = (TextInputLayout) findViewById(app.alansari.R.id.last_name_layout);
        countryLayout = (TextInputLayout) findViewById(app.alansari.R.id.country_layout);
        nationalityLayout = (TextInputLayout) findViewById(app.alansari.R.id.nationality_layout);
        arabicLayout = (TextInputLayout) findViewById(R.id.arabic_name_layout);
        companyLayout = (TextInputLayout) findViewById(R.id.company_name_layout);
        benTypeLayout = (TextInputLayout) findViewById(app.alansari.R.id.beneficiary_type_layout);

        etFirstName = (EditText) findViewById(app.alansari.R.id.first_name);
        etLastName = (EditText) findViewById(app.alansari.R.id.last_name);
        etArabicName = (EditText) findViewById(app.alansari.R.id.arabic_name);
        etCompanyName = (EditText) findViewById(app.alansari.R.id.company_name);
        etCountry = (EditText) findViewById(app.alansari.R.id.country);
        etNationality = (EditText) findViewById(app.alansari.R.id.nationality);
        etBeneficiaryType = (EditText) findViewById(app.alansari.R.id.beneficiary_type);

        btnNext = (Button) findViewById(app.alansari.R.id.next_btn);

        findViewById(app.alansari.R.id.personal_title_layout).setOnClickListener(this);
        findViewById(app.alansari.R.id.account_title_layout).setOnClickListener(this);
        accountCardView = findViewById(app.alansari.R.id.account_card_view);
        countryLayout.setOnClickListener(this);
        etCountry.setOnClickListener(this);
        nationalityLayout.setOnClickListener(this);
        etNationality.setOnClickListener(this);
        etBeneficiaryType.setOnClickListener(this);
        benTypeLayout.setOnClickListener(this);

        btnNext.setOnClickListener(this);
        picLayout.setOnClickListener(this);
        picLayout.setVisibility(View.GONE);

        //Confirm Layout
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), app.alansari.R.anim.slide_down_animation);

        tvAccountName = (TextView) findViewById(app.alansari.R.id.name_c);
        tvBankName = (TextView) findViewById(app.alansari.R.id.bank_name_c);
        tvCountryName = (TextView) findViewById(app.alansari.R.id.country_name_c);

        confirmDynamicLayout = (LinearLayout) findViewById(app.alansari.R.id.confirm_dynamic_layout);
        confirmLayout = findViewById(app.alansari.R.id.add_beneficiary_confirm_layout);
        confirmLayoutBg = findViewById(app.alansari.R.id.add_beneficiary_confirm_bg);
        btnSave = (CircularProgressButton) findViewById(app.alansari.R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, app.alansari.R.color.colorWhite));
        btnSave.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isAutoFill) {
                    if (s == etFirstName.getEditableText()) {
                        validateSingleField(etFirstName, firstNameLayout);
                    } else if (s == etLastName.getEditableText()) {
                        validateSingleField(etLastName, lastNameLayout);
                    } else if (s == etCountry.getEditableText()) {
                        validateSingleField(etCountry, countryLayout);
                    } else if (s == etNationality.getEditableText()) {
                        validateSingleField(etNationality, nationalityLayout);
                    }
                }
            }
        };

        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etCountry.addTextChangedListener(textWatcher);
        etNationality.addTextChangedListener(textWatcher);

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

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(AddBeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddBeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddBeneficiaryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(AddBeneficiaryActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (AddBeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (AddBeneficiaryActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(AddBeneficiaryActivity.this.findViewById(android.R.id.content),
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.add_beneficiary_activity);
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

        if (CommonUtils.getUserMobile() == null) {
            CommonUtils.registerAgain(context);
        }
        ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
        accountExpandLayout.setVisibility(View.GONE);


        if (getIntent().getExtras() != null) {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            sourceType = getIntent().getExtras().getString(Constants.SOURCE_TYPE, Constants.TYPE_BENEFICIARY);
            if (dataObject != null) {
                if (sourceType.equalsIgnoreCase(Constants.TYPE_BENEFICIARY) && dataObject.getBeneficiaryId() != null && dataObject.getBeneficiaryId().length() > 0) {
                    isUpdate = true;
                }
                accountCardView.setVisibility(View.GONE);
                setInitialData();
                fetchBeneficiaryFields();
                return;
            }
        }
        onBackPressed();
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

    private void setInitialData() {
        isAutoFill = true;
        if (dataObject != null && (sourceType.equalsIgnoreCase(Constants.TYPE_SEND_MONEY) || isUpdate)) {
            if (dataObject.getName() != null && dataObject.getName().length() > 0) {
                etFirstName.setText(dataObject.getName().split(" ")[0]);
                etLastName.setText((dataObject.getName().split(" ").length > 1) ? dataObject.getName().split(" ")[1] : null);
            }

            //  if (dataObject.getAddress() != null)
            //etAddress.setText(dataObject.getAddress());
            //  if (dataObject.getMobile() != null)
            //    etMobile.setText(dataObject.getMobile());
            //if (dataObject.getIBANNumber() != null)
            //etIbanNumber.setText(dataObject.getIBANNumber());
            // if (dataObject.getAccountNumber() != null)
            //  etAccountNumber.setText(dataObject.getAccountNumber());

            if (dataObject.getAccountNumber() != null && dataObject.getAccountNumber().length() > 0 && beneficiaryDynamicFields != null) {
                int position = getPositionFromId(String.valueOf(Constants.SELECT_ITEM_BENF_FIELD_ID_10)); //Acc Number
                if (position != -1 && dynamicEditText != null) {
                    setDataToEditText(position, dataObject.getAccountNumber(), null);
                    dynamicEditText[position].setEnabled(false);
                }
            }

            if (dataObject.getCountryData() != null) {
                selectedCountry = dataObject.getCountryData();
                etCountry.setText(selectedCountry.getLatinName());
            }
            if (dataObject.getNationality() != null && dataObject.getNationalityId() != null) {
                selectedNationality = new CountryData();
                selectedNationality.setLatinName(dataObject.getNationality());
                selectedNationality.setArabicName(dataObject.getNationality());
                selectedNationality.setNationality(dataObject.getNationality());
                if (dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING))
                    selectedNationality.setCountryCodeAREX(dataObject.getNationalityId());
                else
                    selectedNationality.setCountryCodeCE(dataObject.getNationalityId());
                etNationality.setText(selectedNationality.getLatinName());
            }

            if (dataObject.getBankData() != null) {
                selectedBank = dataObject.getBankData();
            }

            //  if (dataObject.getBranchData() != null) {
            //      selectedBranch = dataObject.getBranchData();
            //      etBranchName.setText(selectedBranch.getBranchName());
            //   }

//            if (dataObject.getAccountType() != null) {
//                selectedAccountType = new AccountTypeData();
//                selectedAccountType.setName(dataObject.getAccountType());
//            }
        } else {
            selectedCountry = dataObject.getCountryData();
            etCountry.setText(dataObject.getCountryData().getLatinName());
            selectedBank = dataObject.getBankData();
        }
        checkLayoutFilledStatus(null, null, false);
        isAutoFill = false;
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
        if (etBeneficiaryType.getText().toString().equalsIgnoreCase("Individual")) {
            if (selectedBank != null && (selectedBank.getArabicNameReq().equalsIgnoreCase("YES") || selectedBank.getArabicNameReq().equalsIgnoreCase("Y"))) {
                tvAccountName.setText(etArabicName.getText().toString());
            } else {
                tvAccountName.setText(etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim());
            }
        } else if (etBeneficiaryType.getText().toString().equalsIgnoreCase("Corporate")) {
            tvAccountName.setText(etCompanyName.getText().toString().trim());
        }

        tvBankName.setText(selectedBank.getBankName());
        tvCountryName.setText(selectedCountry.getLatinName());
        try {
            confirmDynamicLayout.removeAllViews();
            for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(app.alansari.R.layout.add_beneficiary_ce_confirm_dialog_layout, null);
                TextView label = (TextView) childLayout.findViewById(app.alansari.R.id.label);
                TextView text = (TextView) childLayout.findViewById(app.alansari.R.id.text);
                label.setText(beneficiaryDynamicFields.get(i).getFieldName());
                text.setText(dynamicEditText[i].getText().toString().trim());
                confirmDynamicLayout.addView(childLayout);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateStaticFields() {
        try {
            if (selectedBank != null && (selectedBank.getArabicNameReq().equalsIgnoreCase("YES") || selectedBank.getArabicNameReq().equalsIgnoreCase("Y"))) {
                updateDynamicField(etArabicName, arabicLayout, null, new BeneficiaryDynamicFields("Full name in arabic", "1", "1", "ARABIC", true), getString(app.alansari.R.string.invalid_first_name));
                updateDynamicField(etCompanyName, companyLayout, null, new BeneficiaryDynamicFields("Company Name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_company_name));
                updateDynamicField(etFirstName, firstNameLayout, null, new BeneficiaryDynamicFields("First name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_first_name));
                updateDynamicField(etLastName, lastNameLayout, null, new BeneficiaryDynamicFields("Second and Last name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_last_name));
                updateDynamicField(etCountry, countryLayout, findViewById(app.alansari.R.id.country_divider), new BeneficiaryDynamicFields("Country", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_country));
                updateDynamicField(etNationality, nationalityLayout, findViewById(app.alansari.R.id.nationality_divider), new BeneficiaryDynamicFields("Nationality", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_nationality));
            } else {
                updateDynamicField(etArabicName, arabicLayout, null, new BeneficiaryDynamicFields("Full name in arabic", "0", "0", "ARABIC", true), getString(app.alansari.R.string.invalid_first_name));
                updateDynamicField(etCompanyName, companyLayout, null, new BeneficiaryDynamicFields("Company Name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_company_name));
                updateDynamicField(etFirstName, firstNameLayout, null, new BeneficiaryDynamicFields("First name", "1", "1", "TEXT", true), getString(app.alansari.R.string.invalid_first_name));
                updateDynamicField(etLastName, lastNameLayout, null, new BeneficiaryDynamicFields("Second and Last name", "1", "1", "TEXT", true), getString(app.alansari.R.string.invalid_last_name));
                updateDynamicField(etCountry, countryLayout, findViewById(app.alansari.R.id.country_divider), new BeneficiaryDynamicFields("Country", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_country));
                updateDynamicField(etNationality, nationalityLayout, findViewById(app.alansari.R.id.nationality_divider), new BeneficiaryDynamicFields("Nationality", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_nationality));
            }

            if (beneficiaryDynamicFields != null && beneficiaryDynamicFields.size() == 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void updateDynamicFields() {
        updateStaticFields();
        if (!Validation.isValidList(beneficiaryDynamicFields)) {
            return;
        }
        try {
            dynamicEditText = new EditText[beneficiaryDynamicFields.size()];
            dynamicInputLayout = new TextInputLayout[beneficiaryDynamicFields.size()];
            accountExpandLayout.removeAllViews();
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
                accountExpandLayout.addView(childLayout);
            }
            setInitialData();
            accountCardView.setVisibility(View.VISIBLE);
            checkLayoutFilledStatus(null, null, false);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
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
            case FETCH_BENEFICIARY_FIELDS_AREX:
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getString(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                /* if(response.getString(Constants.MESSAGE)!=null) {
                                  tvLabel.setVisibility(View.VISIBLE);
                                  tvLabel.setText(response.getString(Constants.SCREEN_MSG));
                              }else
                                  tvLabel.setVisibility(View.GONE);
*/
                                String message= response.getString(Constants.MESSAGE);
                                if(!message.equalsIgnoreCase("null")){
                                    tvLabel.setVisibility(View.VISIBLE);
                                    tvLabel.setText(response.getString(Constants.SCREEN_MSG));
                                }else
                                    tvLabel.setVisibility(View.GONE);

                                beneficiaryDynamicFields = (ArrayList<BeneficiaryDynamicFields>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryDynamicFields>>() {
                                }.getType());
                                if (Validation.isValidList(beneficiaryDynamicFields)) {
                                    updateDynamicFields();
                                } else {
                                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                                }
                            } else {
                                CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                            }
                        } else {
                            CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                        }
                    } else {
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_WRONG, tvError, null, null);
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
            case VALIDATE_BENEFICIARY:
            case ADD_BENEFICIARY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (sType == VALIDATE_BENEFICIARY) {
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
                            if (sType == ADD_BENEFICIARY) {
                                btnSave.setProgress(-1);
                                onFinish();
                            }
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
        btnSave.setProgress(-1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Constants.VOTP_CODE) {
                    btnSave.setProgress(0);
                    submitData();
                } else if (requestCode == Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE) {
                    topInfoLayout.setVisibility(View.GONE);
                    etBeneficiaryType.setText(data.getStringExtra(Constants.OBJECT));
                    if (etBeneficiaryType.getText().toString().equalsIgnoreCase("Individual")) {
                        updateDynamicField(etCompanyName, companyLayout, null, new BeneficiaryDynamicFields("Company Name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_company_name));
                        updateDynamicField(etNationality, nationalityLayout, findViewById(app.alansari.R.id.nationality_divider), new BeneficiaryDynamicFields("Nationality", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_nationality));
                        if (selectedBank != null && (selectedBank.getArabicNameReq().equalsIgnoreCase("YES") || selectedBank.getArabicNameReq().equalsIgnoreCase("Y"))) {
                            updateDynamicField(etArabicName, arabicLayout, null, new BeneficiaryDynamicFields("Full name in arabic", "1", "1", "ARABIC", true), getString(app.alansari.R.string.invalid_first_name));
                            updateDynamicField(etFirstName, firstNameLayout, null, new BeneficiaryDynamicFields("First name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_first_name));
                            updateDynamicField(etLastName, lastNameLayout, null, new BeneficiaryDynamicFields("Second and Last name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_last_name));
                        } else {
                            updateDynamicField(etArabicName, arabicLayout, null, new BeneficiaryDynamicFields("Full name in arabic", "0", "0", "ARABIC", true), getString(app.alansari.R.string.invalid_first_name));
                            updateDynamicField(etFirstName, firstNameLayout, null, new BeneficiaryDynamicFields("First name", "1", "1", "TEXT", true), getString(app.alansari.R.string.invalid_first_name));
                            updateDynamicField(etLastName, lastNameLayout, null, new BeneficiaryDynamicFields("Second and Last name", "1", "1", "TEXT", true), getString(app.alansari.R.string.invalid_last_name));
                        }
                        etCompanyName.setText("");
                    } else {
                        updateDynamicField(etArabicName, arabicLayout, null, new BeneficiaryDynamicFields("Full name in arabic", "0", "0", "ARABIC", true), getString(app.alansari.R.string.invalid_first_name));
                        updateDynamicField(etCompanyName, companyLayout, null, new BeneficiaryDynamicFields("Company Name", "1", "1", "TEXT", true), getString(app.alansari.R.string.invalid_company_name));
                        updateDynamicField(etFirstName, firstNameLayout, null, new BeneficiaryDynamicFields("First name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_first_name));
                        updateDynamicField(etLastName, lastNameLayout, null, new BeneficiaryDynamicFields("Second and Last name", "0", "0", "TEXT", true), getString(app.alansari.R.string.invalid_last_name));
                        updateDynamicField(etNationality, nationalityLayout, findViewById(app.alansari.R.id.nationality_divider), new BeneficiaryDynamicFields("Country of Incorporation", "1", "1", "DROPDOWN", true), getString(app.alansari.R.string.invalid_incorporation));
                        etFirstName.setText("");
                        etLastName.setText("");
                    }
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
                    } else if (requestCode == Constants.SELECT_ITEM_NATIONALITY) {
                        selectedNationality = data.getParcelableExtra(Constants.OBJECT);
                        if (selectedNationality != null)
                            etNationality.setText(selectedNationality.getNationality());
                        return;
                    }
                    int position = getPositionFromId(String.valueOf(requestCode));
                    Object object = data.getParcelableExtra(Constants.OBJECT);
                    if (requestCode == Constants.AREX_FIELD_ID_BENF_BRANCH) {
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
                        Snackbar.make(AddBeneficiaryActivity.this.findViewById(android.R.id.content),
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
                    Snackbar.make(AddBeneficiaryActivity.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(AddBeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
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
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.ADD_BENEFICIARY_URL, ADD_BENEFICIARY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(ADD_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, ADD_BENEFICIARY.toString());
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
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
    }    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case app.alansari.R.id.error_button:
            case app.alansari.R.id.empty_button:
                fetchBeneficiaryFields();
                break;
            case app.alansari.R.id.personal_title_layout:
                if (personalExpandLayout.getVisibility() == View.VISIBLE) {
                    ivPersonalArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
                    personalExpandLayout.setVisibility(View.GONE);
                } else {
                    personalExpandLayout.setVisibility(View.VISIBLE);
                    ivPersonalArrow.setImageResource(app.alansari.R.drawable.ic_up_arrow);
                    accountExpandLayout.setVisibility(View.GONE);
                    ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
                    checkLayoutFilledStatus(null, null, false);
                }
                checkLayoutFilledStatus(null, null, false);
                break;
            case app.alansari.R.id.account_title_layout:
                if (accountExpandLayout.getVisibility() == View.VISIBLE) {
                    ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);
                    accountExpandLayout.setVisibility(View.GONE);
                } else {
                    accountExpandLayout.setVisibility(View.VISIBLE);
                    ivAccountArrow.setImageResource(app.alansari.R.drawable.ic_up_arrow);
                    personalExpandLayout.setVisibility(View.GONE);
                    ivPersonalArrow.setImageResource(app.alansari.R.drawable.ic_down_arrow);

                    int type = 0;
                    if (etFirstName.getText() != null && etFirstName.getText().toString().trim().length() > 0) {
                        tvNameTop.setText(etFirstName.getText().toString().trim());
                        type = 1;
                    } else if (etCompanyName.getText() != null && etCompanyName.getText().toString().trim().length() > 0) {
                        type = 1;
                        tvNameTop.setText(etCompanyName.getText().toString().trim());
                    } else {
                        tvNameTop.setText(null);
                    }
                    if (etLastName.getText() != null && etLastName.getText().toString().trim().length() > 0) {
                        tvNameTop.setText(tvNameTop.getText().toString().trim() + " " + etLastName.getText().toString().trim());
                        type = 1;
                    }
                    if (etCountry.getText() != null && etCountry.getText().toString().trim().length() > 0) {
                        tvCountryTop.setText(etCountry.getText().toString().trim());
                    } else {
                        tvCountryTop.setText(null);
                    }
                    if (type == 1)
                        topInfoLayout.setVisibility(View.VISIBLE);
                    else
                        topInfoLayout.setVisibility(View.GONE);

                    checkLayoutFilledStatus(null, null, false);
                }
                checkLayoutFilledStatus(null, null, false);
                break;
            case app.alansari.R.id.nationality_layout:
                break;
            case app.alansari.R.id.beneficiary_type_layout:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(app.alansari.R.array.beneficiary_type))));
                startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.beneficiary_type:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(app.alansari.R.array.beneficiary_type))));
                startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.nationality:
                SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                intent = new Intent(context, SelectCountryFlagActivity.class);
                intent.putExtra("hideFirstItem", true);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_COUNTRY);
                intent.putExtra(Constants.IS_FOR_NATIONANLITY, true);
                startActivityForResult(intent, Constants.SELECT_ITEM_NATIONALITY);
                overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                break;
            case app.alansari.R.id.input_layout:
            case app.alansari.R.id.edit_text:
                if (view.getTag() != null) {
                    String fieldId = ((BeneficiaryDynamicFields) view.getTag()).getFieldId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_BENF_BRANCH))) {
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
                    }
                }
                break;
            case app.alansari.R.id.next_btn:
                validateData();
                break;
            case app.alansari.R.id.profile_pic_layout:
//                imagePickDialog.show();
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

    private JSONObject addBeneficiaryJsonFormat() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.CHANNEL_ID, Constants.CHANNEL_ID_VALUE);
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.MEM_PK_ID, CommonUtils.getMemPkId(Constants.AREX_MAPPING));
            if (isUpdate) {
                jsonObject.put(Constants.MEM_BENF_DTL_PK_ID, checkValue(dataObject.getBeneficiaryId()));
            }
            jsonObject.put(Constants.SERVICE_TYPE, checkValue(dataObject.getServiceTypeData().getMapping()));
            jsonObject.put(Constants.TRANSFER_TYPE, checkValue(dataObject.getTransferType()));
            jsonObject.put(Constants.COUNTRY_CODE, checkValue(dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getCountryData().getCountryCodeAREX() : dataObject.getCountryData().getCountryCodeCE()));
            jsonObject.put(Constants.COUNTRY_NAME, checkValue(dataObject.getCountryData().getLatinName()));
            jsonObject.put(Constants.BANK_CODE, checkValue(dataObject.getServiceTypeData().getMapping().equalsIgnoreCase(Constants.AREX_MAPPING) ? dataObject.getBankData().getBankCodeAREX() : dataObject.getBankData().getBankCodeCE()));
            jsonObject.put(Constants.BANK_NAME, checkValue(dataObject.getBankData().getBankName()));
            if (etBeneficiaryType.getText().toString().equalsIgnoreCase("Individual")) {
                jsonObject.put("41", "I");
                if (selectedBank != null && (selectedBank.getArabicNameReq().equalsIgnoreCase("YES") || selectedBank.getArabicNameReq().equalsIgnoreCase("Y"))) {
                    jsonObject.put(Constants.BENE_ARABIC_NAME, checkValue(etArabicName.getText().toString().trim()));
                } else {
                    jsonObject.put(Constants.FIRST_NAME, checkValue(etFirstName.getText().toString().trim()));
                    jsonObject.put(Constants.LAST_NAME, checkValue(etLastName.getText().toString().trim()));
                }
            } else if (etBeneficiaryType.getText().toString().equalsIgnoreCase("Corporate")) {
                jsonObject.put("41", "C");
                if (selectedBank != null && (selectedBank.getArabicNameReq().equalsIgnoreCase("YES") || selectedBank.getArabicNameReq().equalsIgnoreCase("Y"))) {
                    jsonObject.put(Constants.BENE_ARABIC_NAME, checkValue(etCompanyName.getText().toString().trim()));
                } else {
                    jsonObject.put(Constants.FIRST_NAME, checkValue(etCompanyName.getText().toString().trim()));
                }
            }

            jsonObject.put(Constants.NATIONALITY_CODE, checkValue(selectedNationality.getCountryCodeAREX()));
            jsonObject.put(Constants.NATIONALITY_NAME, checkValue(selectedNationality.getNationality()));
            for (int i = 0; i < beneficiaryDynamicFields.size(); i++) {
                try {
                    LogUtils.d("Input", beneficiaryDynamicFields.get(i).getFieldId() + " : " + checkValue(dynamicEditText[i].getText().toString().trim()));
                    jsonObject.put(beneficiaryDynamicFields.get(i).getFieldId(), checkValue(dynamicEditText[i].getText().toString().trim()));
                    if (beneficiaryDynamicFields.get(i).getFieldType().equalsIgnoreCase("DROPDOWN") && dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID) != null)
                        jsonObject.put(beneficiaryDynamicFields.get(i).getFieldId() + "_CODE", checkValue(dynamicEditText[i].getTag(app.alansari.R.id.VIEW_TAG_CODE_ID).toString().trim()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (base64 != null && !TextUtils.isEmpty(base64))
                jsonObject.put(Constants.BENF_IMAGE, base64);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d("ok", "addBeneficiary :-  " + jsonObject.toString());
        return jsonObject;
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
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
            if (currentView.getInputType().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
        } else if (currentView.getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {

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

    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private void validateSingleField(EditText editText, TextInputLayout inputLayout) {
        if (checkLayoutFilledStatus(new EditText[]{editText}, new TextInputLayout[]{inputLayout}, true)) {
            checkLayoutFilledStatus(null, null, false);
        }
    }

    private boolean checkLayoutFilledStatus(EditText[] dynamicEditText, TextInputLayout[] dynamicInputLayout, boolean showMessage) {
        int type;
        boolean specialCharacterValidation = true;
        if (personalExpandLayout.getVisibility() == View.VISIBLE) {
            type = 1;
            if (dynamicEditText == null) {
                dynamicEditText = new EditText[]{etFirstName, etLastName, etCountry, etNationality, etArabicName, etCompanyName, etBeneficiaryType};
                dynamicInputLayout = new TextInputLayout[]{firstNameLayout, lastNameLayout, countryLayout, nationalityLayout, arabicLayout, companyLayout, benTypeLayout};
            }
        } else {
            type = 2;
            if (dynamicEditText == null) {
                dynamicEditText = this.dynamicEditText;
                dynamicInputLayout = this.dynamicInputLayout;
            }
        }

        for (int i = 0; dynamicEditText != null && i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getFieldType() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getFieldType().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    setLayoutStatus(type, false);
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getIsMandatory().equalsIgnoreCase("0")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
                if ((dynamicEditText[i].getTag() != null && ((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength() != null && (!((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength().equalsIgnoreCase("0")))) {
                    if (dynamicEditText[i].getText().toString().trim().length() == Integer.valueOf(((BeneficiaryDynamicFields) dynamicEditText[i].getTag()).getLength())) {
                        setLayoutStatus(type, true);
                        setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                    } else {
                        setLayoutStatus(type, false);
                        setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                        break;
                    }
                } else {
                    setLayoutStatus(type, true);
                    setErrorLayout(false, dynamicEditText[i], dynamicInputLayout[i]);
                }
            } else {
                setLayoutStatus(type, false);
                setErrorLayout(showMessage, dynamicEditText[i], dynamicInputLayout[i]);
                break;
            }
        }
        if (specialCharacterValidation && (showMessage && dynamicEditText.length == 1 && ((type == 1 && personalLayoutStatus)) || (showMessage && (type == 2 && accountLayoutStatus)))) {
            return true;
        }

        if (type == 1) {
            if (specialCharacterValidation && personalLayoutStatus) {
                ivPersonalLoading.setImageResource(app.alansari.R.drawable.ic_success);
            } else {
                ivPersonalLoading.setImageResource(app.alansari.R.drawable.ic_loading);
            }
        } else if (type == 2) {
            if (specialCharacterValidation && accountLayoutStatus) {
                ivAccountLoading.setImageResource(app.alansari.R.drawable.ic_success);
            } else {
                ivAccountLoading.setImageResource(app.alansari.R.drawable.ic_loading);
            }
        }

        if (specialCharacterValidation && personalLayoutStatus && accountLayoutStatus) {
            btnNext.setEnabled(true);
            return true;
        } else {
            btnNext.setEnabled(false);
            return false;
        }
    }

    private void setLayoutStatus(int type, boolean status) {
        if (type == 1)
            personalLayoutStatus = status;
        else
            accountLayoutStatus = status;
    }

    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        try {
            if (inputLayout != null && editText != null) {
                if (validated && !isAutoFill) {
                    inputLayout.setError(((BeneficiaryDynamicFields) editText.getTag()).getErrorMessage());
                    inputLayout.setErrorEnabled(true);
                } else {
                    inputLayout.setError(null);
                    inputLayout.setErrorEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void generateOTP() {
        try {
            String userPkId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionId = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTPAfterLogin(CommonUtils.getUserMobile(), userPkId, LogoutCalling.getDeviceID(context), sessionId), Constants.RESEND_OTP_URL_WEBSERVICE, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
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






    private void validateData() {
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
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.ADD_BENEFICIARY_URL, VALIDATE_BENEFICIARY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(VALIDATE_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, VALIDATE_BENEFICIARY.toString());
            CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), VALIDATE_BENEFICIARY.toString(), false);
        } else {
            Toast.makeText(context, getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchBeneficiaryFields() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBeneficiaryFields(CommonUtils.getUserId(), dataObject.getCountryData().getId(), CommonUtils.getDefaultCurrencyData(dataObject.getCountryData()).getCurrencyCode(), dataObject.getBankData().getId(), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_BENEFICIARY_FIELDS_URL, FETCH_BENEFICIARY_FIELDS_AREX, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_BENEFICIARY_FIELDS_AREX.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_BENEFICIARY_FIELDS_AREX.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }


}