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
import android.widget.DatePicker;
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

import org.json.JSONArray;
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
import app.alansari.models.WUBeneficiaryData;
import app.alansari.modules.accountmanagement.models.WUBeneficiaryDybamicFields;
import app.alansari.modules.sendmoney.WUSendMoneyActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADD_BENEFICIARY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_WU_ADDITIONAL_INFO;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_ADD_BENEFICIARY;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.WU_VALIDATE_BENEFICIARY;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 27 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class WUAddBeneficiaryActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, DatePickerDialog.OnDateSetListener , LogOutTimerUtil.LogOutListener  {

    private Button btnNext;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private CircularProgressButton btnSave;
    private TextView tvNameTop, tvCountryTop;
    private View topInfoLayout, accountCardView, confirmLayout, confirmLayoutBg;
    private ImageView ivAccountLoading, ivAccountArrow;
    private LinearLayout accountExpandLayout, confirmDynamicLayout;
    private CircleImageView profilePic;
    private RelativeLayout picLayout;
    private Dialog imagePickDialog;
    //Dyanmic
    private EditText[] dynamicEditText;
    private TextInputLayout[] dynamicInputLayout;
    private ArrayList<WUBeneficiaryDybamicFields> WUBeneficiaryDybamicFields;

    //Variables
    private Intent intent;
    private Context context;
    private JSONObject jsonObject;
    private String sourceType, selectedDOB, selectedDateOfBirth;
    private boolean isUpdate, isAutoFill, accountLayoutStatus;

    private BankData selectedBank;
    private BranchData selectedBranch;
    private BeneficiaryData beneficiaryData;
    private WUBeneficiaryData dataObject;
    private AccountTypeData selectedAccountType;
    private CountryData selectedCountry, selectedNationality;
    private Animation slideUpAnimation, slideDownAnimation;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static final int PICK_FROM_GALLERY = 122;
    private String base64 = "";
    private String wuCardNo;
    private String arexUserId;
    private String TOTAL_WU_POINTS = "0";
    private String BENEF_PK_ID = "";
    private String sessionTime;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        picLayout = (RelativeLayout) findViewById(R.id.profile_pic_layout);
        profilePic = (CircleImageView) findViewById(R.id.beneficiary_pic);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));

        topInfoLayout = findViewById(R.id.top_info_layout);
        accountExpandLayout = (LinearLayout) findViewById(R.id.account_info_layout);
        tvNameTop = (TextView) findViewById(R.id.name_top);
        tvCountryTop = (TextView) findViewById(R.id.country_top);

        ivAccountLoading = (ImageView) findViewById(R.id.account_loading_image);
        ivAccountArrow = (ImageView) findViewById(R.id.account_arrow_image);


        btnNext = (Button) findViewById(R.id.next_btn);

        findViewById(R.id.account_title_layout).setOnClickListener(this);
        accountCardView = findViewById(R.id.account_card_view);


        btnNext.setOnClickListener(this);
        picLayout.setOnClickListener(this);
        picLayout.setVisibility(View.GONE);

        //Confirm Layout
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_animation);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_animation);

        confirmDynamicLayout = (LinearLayout) findViewById(R.id.confirm_dynamic_layout);
        confirmLayout = findViewById(R.id.wu_add_beneficiary_confirm_layout);
        confirmLayoutBg = findViewById(R.id.add_beneficiary_confirm_bg);
        btnSave = (CircularProgressButton) findViewById(R.id.dialog_btn);
        btnSave.setIndeterminateProgressMode(true);
        btnSave.setStrokeColor(ContextCompat.getColor(context, R.color.colorWhite));
        btnSave.setOnClickListener(this);

        imagePickDialog = new Dialog(context, R.style.CustomDialogThemeLightBg);
        imagePickDialog.setCanceledOnTouchOutside(true);
        imagePickDialog.setContentView(R.layout.image_pick_dialog);
        imagePickDialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkCameraPermission();
            }
        });

        imagePickDialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
                checkGalleryPermission();
            }
        });

        imagePickDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog.dismiss();
            }
        });
    }

    private void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(WUAddBeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WUAddBeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            pickPhotos();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(WUAddBeneficiaryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(WUAddBeneficiaryActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (WUAddBeneficiaryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (WUAddBeneficiaryActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(WUAddBeneficiaryActivity.this.findViewById(android.R.id.content),
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
                        Snackbar.make(WUAddBeneficiaryActivity.this.findViewById(android.R.id.content),
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
                    Snackbar.make(WUAddBeneficiaryActivity.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(WUAddBeneficiaryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
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

    private void setErrorLayout(boolean validated, EditText editText, TextInputLayout inputLayout) {
        if (inputLayout != null && editText != null) {
            if (validated && !isAutoFill) {
                inputLayout.setError(((WUBeneficiaryDybamicFields) editText.getTag()).getErrorMessage());
                inputLayout.setErrorEnabled(true);
            } else {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
        }
    }

    private void validateSingleField(EditText editText, TextInputLayout inputLayout) {
        if (checkLayoutFilledStatus(new EditText[]{editText}, new TextInputLayout[]{inputLayout}, true)) {
            checkLayoutFilledStatus(null, null, false);
        }
    }


    private boolean checkLayoutFilledStatus(EditText[] dynamicEditText, TextInputLayout[] dynamicInputLayout, boolean showMessage) {
        int type;
        boolean specialCharacterValidation = true;
        type = 2;
        if (dynamicEditText == null) {
            dynamicEditText = this.dynamicEditText;
            dynamicInputLayout = this.dynamicInputLayout;
        }

        for (int i = 0; dynamicEditText != null && i < dynamicEditText.length; i++) {
            if (Validation.isValidEditTextValue(dynamicEditText[i]) && (dynamicEditText[i].getTag() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getInputtype() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getInputtype().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)))
                if (!Validation.validateSpecialCharacters(dynamicEditText[i], dynamicInputLayout[i])) {
                    specialCharacterValidation = false;
                    setLayoutStatus(type, false);
                    break;
                }
            if ((dynamicEditText[i].getTag() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()) != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getMandatory() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getMandatory().equalsIgnoreCase("N")) || Validation.isValidEditTextValue(dynamicEditText[i])) {
                if ((dynamicEditText[i].getTag() != null && ((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getLength() != null && (!((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getLength().equalsIgnoreCase("0")))) {
                    if (dynamicEditText[i].getText().toString().trim().length() == Integer.valueOf(((WUBeneficiaryDybamicFields) dynamicEditText[i].getTag()).getLength())) {
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
        if (specialCharacterValidation && (showMessage && dynamicEditText.length == 1 && ((showMessage && (type == 2 && accountLayoutStatus))))) {
            return true;
        }

        if (specialCharacterValidation && accountLayoutStatus) {
            ivAccountLoading.setImageResource(R.drawable.ic_success);
        } else {
            ivAccountLoading.setImageResource(R.drawable.ic_loading);
        }

        if (specialCharacterValidation && accountLayoutStatus) {
            btnNext.setEnabled(true);
            return true;
        } else {
            btnNext.setEnabled(false);
            return false;
        }
    }

    private void setLayoutStatus(int type, boolean status) {
        if (type == 1) {
//            personalLayoutStatus = status;
        } else
            accountLayoutStatus = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wu_add_beneficiary_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("New");
        ((TextView) findViewById(R.id.toolbar_title_2)).setText("Beneficiary");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        findViewById(R.id.toolbar_layout).setBackground(ContextCompat.getDrawable(context, R.drawable.am_beneficiary_header_bg));
        ((AppCompatImageView) findViewById(R.id.toolbar_right_icon)).setImageResource(R.drawable.svg_am_beneficiary_icon);
        ((CircleImageView) findViewById(R.id.beneficiary_pic)).setImageResource(R.drawable.ic_new_user);
        findViewById(R.id.nav_menu).setVisibility(View.GONE);
        CommonUtils.setStatusBarColor(getWindow(), ContextCompat.getColor(context, R.color.color086687));

        init();
        Toolbar toolbarConfirm = (Toolbar) confirmLayout.findViewById(R.id.toolbar);
        ((TextView) toolbarConfirm.findViewById(R.id.toolbar_title)).setText("Beneficiary Confirmation");
        toolbarConfirm.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarConfirm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });

        if (CommonUtils.getUserMobile() == null) {
            CommonUtils.registerAgain(context);
        }


        if (getIntent().getExtras() != null) {
            wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
            TOTAL_WU_POINTS = getIntent().getStringExtra(Constants.TOTAL_WU_POINTS);
            accountCardView.setVisibility(View.GONE);
            setInitialData();
            fetchBeneficiaryFields();
        } else {
            onBackPressed();
        }

    }

    private void setInitialData() {
        isAutoFill = true;
        checkLayoutFilledStatus(null, null, false);
        isAutoFill = false;
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
            case R.id.error_button:
            case R.id.empty_button:
                fetchBeneficiaryFields();
                break;
            case R.id.account_title_layout:
                if (accountExpandLayout.getVisibility() == View.VISIBLE) {
                    ivAccountArrow.setImageResource(R.drawable.ic_down_arrow);
                    accountExpandLayout.setVisibility(View.GONE);
                } else {
                    accountExpandLayout.setVisibility(View.VISIBLE);
                    ivAccountArrow.setImageResource(R.drawable.ic_up_arrow);

                    int type = 0;

                    if (type == 1)
                        topInfoLayout.setVisibility(View.VISIBLE);
                    else
                        topInfoLayout.setVisibility(View.GONE);
                }
                checkLayoutFilledStatus(null, null, false);
                break;
            case R.id.nationality_layout:
                break;
            case R.id.beneficiary_type_layout:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.beneficiary_type))));
                startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
                break;
            case R.id.beneficiary_type:
                intent = new Intent(context, SelectItemActivity.class);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                intent.putStringArrayListExtra(Constants.LIST, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.beneficiary_type))));
                startActivityForResult(intent, Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE);
                overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
                break;
            case R.id.nationality:
                intent = new Intent(context, SelectCountryFlagActivity.class);
                intent.putExtra("hideFirstItem", true);
                intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_NATIONALITY);
                startActivityForResult(intent, Constants.SELECT_ITEM_NATIONALITY);
                overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
                break;
            case R.id.input_layout:
            case R.id.edit_text:
                if (view.getTag() != null) {
                    String fieldId = ((WUBeneficiaryDybamicFields) view.getTag()).getFieldId();
                    if (fieldId.equalsIgnoreCase(String.valueOf(Constants.AREX_FIELD_ID_ACCOUNT_TYPE))) {
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.SELECT_ITEM_ACCOUNT_TYPE);
                        intent.putExtra(Constants.BANK_CODE, selectedBank.getBankCodeAREX());
                        startActivityForResult(intent, Constants.AREX_FIELD_ID_ACCOUNT_TYPE);
                        overridePendingTransition(R.anim.pump_top_to_up, R.anim.hold);
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
                    } else if (fieldId.equalsIgnoreCase(String.valueOf(Constants.WU_FIELD_ID_NAME_TYPE))) {
                        intent = new Intent(context, SelectItemActivity.class);
                        intent.putExtra(Constants.ITEM_TYPE, Constants.WU_SELECT_ITEM_ADD_BENEFICIARY_SERVICE_TYPE);
                        ArrayList<String> receiverNameTypeList = new ArrayList<>();
                        receiverNameTypeList.add("First Name/Last Name");
                        receiverNameTypeList.add("Paternal Name/Maternal Name");
                        intent.putStringArrayListExtra(Constants.LIST, receiverNameTypeList);
                        startActivityForResult(intent, Constants.WU_FIELD_ID_NAME_TYPE);
                        overridePendingTransition(app.alansari.R.anim.pump_top_to_up, app.alansari.R.anim.hold);
                    }
                }
                break;
            case R.id.next_btn:
                validateData();
                break;
            case R.id.profile_pic_layout:
//                imagePickDialog.show();
                break;
            case R.id.dialog_btn:
                if (btnSave.getProgress() == -1) {
                    btnSave.setProgress(0);
                    generateOTP();
                } else if (btnSave.getProgress() == 100) {
                    Intent intent = new Intent(context, WUSendMoneyActivity.class);
                    intent.putExtra(Constants.BENEF_TYPE, "ADD");
                    intent.putExtra(Constants.WU_CARD_NO, wuCardNo);
                    intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
                    intent.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
                    intent.putExtra(Constants.WU_SELECTED_COUNTRY, getIntent().getParcelableExtra(Constants.WU_SELECTED_COUNTRY));
                    intent.putExtra(Constants.BENEF_PK_ID, BENEF_PK_ID);
                    intent.putExtra(Constants.NAME_TYPE, getIntent().getStringExtra(Constants.NAME_TYPE));
                    intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE, getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE));
                    intent.putExtra(Constants.BENEFICIARY_SERVICE_TYPE_NAME, getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE_NAME));
                    intent.putExtra(Constants.OBJECT, dataObject);
                    startActivity(intent);
                } else if (btnSave.getProgress() > 0 && btnSave.getProgress() < 100) {
                } else {
                    generateOTP();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
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

    private void generateOTP() {
        try {
            String userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            String sessionId = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().resendOTPAfterLogin(CommonUtils.getUserMobile(),userPkId, LogoutCalling.getDeviceID(context),sessionId), Constants.RESEND_OTP_URL_WEBSERVICE, CommonUtils.SERVICE_TYPE.RESEND_OTP, Request.Method.PUT, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.RESEND_OTP.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), CommonUtils.SERVICE_TYPE.RESEND_OTP.toString(), false);
        } catch (Exception ex) {
            Toast.makeText(context, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
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

        try {
            confirmDynamicLayout.removeAllViews();
            for (int i = 0; i < WUBeneficiaryDybamicFields.size(); i++) {
                final View childLayout = LayoutInflater.from(context).inflate(R.layout.add_beneficiary_ce_confirm_dialog_layout, null);
                TextView label = (TextView) childLayout.findViewById(R.id.label);
                TextView text = (TextView) childLayout.findViewById(R.id.text);
                label.setText(WUBeneficiaryDybamicFields.get(i).getFieldLable());
                text.setText(dynamicEditText[i].getText().toString().trim());
                confirmDynamicLayout.addView(childLayout);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void validateData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            addBeneficiaryJsonFormat(true);
            LogUtils.d("validateData", "addBeneficiary :-  " + jsonObject.toString());
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.WU_VALIDATE_BENEFICIARY_URL, WU_VALIDATE_BENEFICIARY, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_VALIDATE_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_VALIDATE_BENEFICIARY.toString());
            CommonUtils.showLoading(context, getString(R.string.please_wait), WU_VALIDATE_BENEFICIARY.toString(), false);
        } else {
            Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            btnSave.setProgress(50);
            addBeneficiaryJsonFormat(false);
            LogUtils.d("submitData", "addBeneficiary :-  " + jsonObject.toString());
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(jsonObject, Constants.WU_ADD_BENEFICIARY_URL, WU_ADD_BENEFICIARY, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(WU_ADD_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, WU_ADD_BENEFICIARY.toString());
        } else {
            Toast.makeText(context, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchBeneficiaryFields() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String serviceType = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
            String isSecurityQueAvailable = "N";
            String aedAmt = "0";
            String isBeneficiary = "Y";
            String isTnx = "N";
            String nameType = getIntent().getStringExtra(Constants.NAME_TYPE);
            String benType = "ADD";

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWuBeneficiaryFields(CommonUtils.getUserId(), getIntent().getStringExtra(Constants.WU_COUNTRY_CODE),
                    getIntent().getStringExtra(Constants.AREX_COUNTRY_CODE), serviceType, isSecurityQueAvailable, aedAmt, isBeneficiary, isTnx, nameType, benType,LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_WU_ADDITIONAL_INFO_URL, FETCH_WU_ADDITIONAL_INFO, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_WU_ADDITIONAL_INFO.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_WU_ADDITIONAL_INFO.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private boolean checkValidBeneficiaryString(String string) {
        if (string != null && !string.equalsIgnoreCase("0") && string.length() > 0)
            return true;
        else
            return false;
    }

    private void updateStaticFields() {
        try {
            if (WUBeneficiaryDybamicFields != null && WUBeneficiaryDybamicFields.size() == 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    private void updateDynamicFields() {
        updateStaticFields();
        if (!Validation.isValidList(WUBeneficiaryDybamicFields)) {
            return;
        }
        try {
            dynamicEditText = new EditText[WUBeneficiaryDybamicFields.size()];
            dynamicInputLayout = new TextInputLayout[WUBeneficiaryDybamicFields.size()];
            accountExpandLayout.removeAllViews();
            for (int i = 0; i < WUBeneficiaryDybamicFields.size(); i++) {
                if (WUBeneficiaryDybamicFields.get(i).isStatic())
                    continue;
                final View childLayout = LayoutInflater.from(context).inflate(R.layout.add_beneficiary_dynamic_view, null);
                TextInputLayout inputLayout = (TextInputLayout) childLayout.findViewById(R.id.input_layout);
                final EditText editText = (EditText) childLayout.findViewById(R.id.edit_text);

                /*delete this line once Field Id Comes in Response*/
                WUBeneficiaryDybamicFields.get(i).setFieldId(WUBeneficiaryDybamicFields.get(i).getFieldId());

                if (WUBeneficiaryDybamicFields.get(i).getMinlength() == null || Integer.valueOf(WUBeneficiaryDybamicFields.get(i).getMinlength()) == 0) {
                    WUBeneficiaryDybamicFields.get(i).setMinlength("1");
                }
                if (WUBeneficiaryDybamicFields.get(i).getLength() == null) {
                    WUBeneficiaryDybamicFields.get(i).setLength("0");
                }

                updateDynamicField(editText, inputLayout, i == (WUBeneficiaryDybamicFields.size() - 1) ? childLayout.findViewById(R.id.divider) : null, WUBeneficiaryDybamicFields.get(i), null);
                if (i == WUBeneficiaryDybamicFields.size() - 1)
                    childLayout.findViewById(R.id.divider).setVisibility(View.INVISIBLE);

                if (checkValidBeneficiaryString(WUBeneficiaryDybamicFields.get(i).getPrefix())) {
                    isAutoFill = true;
                    editText.setText(WUBeneficiaryDybamicFields.get(i).getPrefix());
                    isAutoFill = false;
                    if (!WUBeneficiaryDybamicFields.get(i).getLength().equalsIgnoreCase("0")) {
                        WUBeneficiaryDybamicFields.get(i).setLength(String.valueOf(Integer.valueOf(WUBeneficiaryDybamicFields.get(i).getLength()) + WUBeneficiaryDybamicFields.get(i).getPrefix().length()));
                    }
                    setTextLimit(editText, Integer.valueOf(WUBeneficiaryDybamicFields.get(i).getMaxlength()));
                } else {
                    if (!WUBeneficiaryDybamicFields.get(i).getMaxlength().equalsIgnoreCase("0"))
                        setTextLimit(editText, Integer.valueOf(WUBeneficiaryDybamicFields.get(i).getMaxlength()));
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

    private void updateDynamicField(EditText editText, TextInputLayout inputLayout, View divider, WUBeneficiaryDybamicFields currentView, String errorMessage) {
        if (currentView != null) {
            if (errorMessage == null)
                currentView.setErrorMessage((currentView.getLength() != null && checkValidBeneficiaryString(currentView.getLength()) ? "Required length is " + currentView.getLength() : "Invalid " + currentView.getFieldLable().toLowerCase()));
            else
                currentView.setErrorMessage(errorMessage);
            editText.setTag(currentView);
            inputLayout.setTag(currentView);
        } else
            return;

        if (currentView.getIsVisible() != null) {
            setVisibility(inputLayout, divider, currentView.getIsVisible().equalsIgnoreCase("1"));
        }

        setMandatory(editText, inputLayout, currentView.getFieldLable() + (currentView.getMandatory() != null && (currentView.getMandatory().equalsIgnoreCase("Y")) ? "*" : ""));
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

    private void setFieldType(final EditText editText, final TextInputLayout inputLayout, final WUBeneficiaryDybamicFields currentView) {
        LogUtils.v("setFieldType", currentView.getFieldLable() + "::" + currentView.getInputtype());
        if (currentView.getInputtype() != null && (currentView.getInputtype().equalsIgnoreCase(Constants.FIELD_TYPE_DROPDOWN) || currentView.getInputtype().equalsIgnoreCase("D"))) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_NULL);
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(this);
            inputLayout.setHintAnimationEnabled(false);
        } else if (currentView.getInputtype() != null && (currentView.getInputtype().equalsIgnoreCase(Constants.FIELD_TYPE_TEXT) || currentView.getInputtype().equalsIgnoreCase("T") || currentView.getInputtype().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER))) {
            if (currentView.getInputtype().equalsIgnoreCase(Constants.INPUT_TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
            }
            editText.setFocusable(true);
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setOnClickListener(null);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
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
            case FETCH_WU_ADDITIONAL_INFO:
                try {
                    if (status == 1) {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getString(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                WUBeneficiaryDybamicFields = (ArrayList<WUBeneficiaryDybamicFields>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<WUBeneficiaryDybamicFields>>() {
                                }.getType());
                                if (Validation.isValidList(WUBeneficiaryDybamicFields)) {
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
                            Toast.makeText(context, getString(R.string.error_unable_to_process), Toast.LENGTH_SHORT).show();
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
            case WU_VALIDATE_BENEFICIARY:
            case WU_ADD_BENEFICIARY:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (sType == WU_VALIDATE_BENEFICIARY) {
                                confirmData();
                                return;
                            }
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                JSONArray resultArray = response.getJSONArray(Constants.RESULT);
                                JSONObject resultObject = resultArray.getJSONObject(0);
                                BENEF_PK_ID = resultObject.getString("BENEF_PK_ID");
                                btnSave.setProgress(100);
                                return;
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
                                for (int i = 0; i < WUBeneficiaryDybamicFields.size(); i++) {
                                    if (WUBeneficiaryDybamicFields.get(i).getFieldId().equalsIgnoreCase(fieldPkId)) {
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
        Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
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
                if (requestCode == Constants.VOTP_CODE) {
                    btnSave.setProgress(0);
                    submitData();
                } else if (requestCode == Constants.SELECT_ITEM_ADD_BENEFICIARY_TYPE) {


                } else if (requestCode == Constants.WU_FIELD_ID_NAME_TYPE) {
                    int position = getPositionFromId(String.valueOf(requestCode));
                    String value = data.getStringExtra(Constants.OBJECT);
                    setDataToEditText(position, value, value);
                } else if (data.getParcelableExtra(Constants.OBJECT) != null) {
                    if (requestCode == Constants.SELECT_ITEM_COUNTRY) {
                        selectedCountry = data.getParcelableExtra(Constants.OBJECT);
                        // if (selectedCountry != null)
                        //  etCountry.setText(selectedCountry.getLatinName());
                        return;
                    } else if (requestCode == Constants.SELECT_ITEM_NATIONALITY) {
                        selectedNationality = data.getParcelableExtra(Constants.OBJECT);
                        if (selectedNationality != null)

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

    private int getPositionFromId(String id) {
        for (int i = 0; i < WUBeneficiaryDybamicFields.size(); i++) {
            if (id.equalsIgnoreCase(WUBeneficiaryDybamicFields.get(i).getFieldId()))
                return i;
        }
        return -1;
    }

    private void setDataToEditText(int position, String text, String code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(R.id.VIEW_TAG_CODE_ID, code);
    }

    private void setDataToEditText(int position, String text, int code) {
        dynamicEditText[position].setText(text);
        dynamicEditText[position].setTag(R.id.VIEW_TAG_CODE_ID, code);
    }

    private String checkValue(String value) {
        return value != null ? value : "";
    }

    private JSONObject addBeneficiaryJsonFormat(boolean addBenType) {
        dataObject = new WUBeneficiaryData();
        jsonObject = new JSONObject();
        try {
            String SERVICE_TYPE = getIntent().getStringExtra(Constants.BENEFICIARY_SERVICE_TYPE);
            if (addBenType) {
                jsonObject.put(Constants.BENEF_TYPE, "ADD");
            }
            jsonObject.put(Constants.SERVICE_CODE, SERVICE_TYPE);
            jsonObject.put(Constants.AREX_COUNTRY_CODE, getIntent().getStringExtra(Constants.AREX_COUNTRY_CODE));
            jsonObject.put(Constants.WU_COUNTRY_CODE, getIntent().getStringExtra(Constants.WU_COUNTRY_CODE));
            jsonObject.put(Constants.USER_PK_ID, CommonUtils.getUserId());
            jsonObject.put(Constants.DEVICE_ID, LogoutCalling.getDeviceID(context));
            jsonObject.put(Constants.SESSION_ID, sessionTime);


            JSONArray fieldArray = new JSONArray();
            for (int i = 0; i < WUBeneficiaryDybamicFields.size(); i++) {
                try {
                    JSONObject fieldData = new JSONObject();
                    if (WUBeneficiaryDybamicFields.get(i).getFieldId().equalsIgnoreCase("40")) {
                        if (dynamicEditText[i].getText().toString().trim().equalsIgnoreCase("First Name/Last Name")) {
                            fieldData.put(Constants.FIELD_VALUE, "D");
                        } else {
                            fieldData.put(Constants.FIELD_VALUE, "M");
                        }
                    } else {
                        fieldData.put(Constants.FIELD_VALUE, checkValue(dynamicEditText[i].getText().toString().trim()));
                    }
                    fieldData.put(Constants.FIELD_ID, WUBeneficiaryDybamicFields.get(i).getFieldId());
                    fieldData.put(Constants.FIELD_VALUE_CODE, WUBeneficiaryDybamicFields.get(i).getFieldId());
                    fieldArray.put(fieldData);


                    if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("First")) {
                        dataObject.setReceiverFirstName(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Last")) {
                        dataObject.setReceiverLastName(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Middle")) {
                        dataObject.setReceiverMiddleName(dynamicEditText[i].getText().toString().trim());
                    }else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Given")) {
                        dataObject.setReceiverFirstName(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Paternal")) {
                        dataObject.setReceiverLastName(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Maternal")) {
                        dataObject.setReceiverMiddleName(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("ISD")) {
                        dataObject.setReceiverContactIsdCode(dynamicEditText[i].getText().toString().trim());
                    } else if (WUBeneficiaryDybamicFields.get(i).getFieldLable().contains("Mobile")) {
                        dataObject.setReceiverContactNumber(dynamicEditText[i].getText().toString().trim());
                    }
                    dataObject.setReceiverNameType(getIntent().getStringExtra(Constants.NAME_TYPE));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            JSONObject nameTypeObj = new JSONObject();
            nameTypeObj.put(Constants.FIELD_VALUE, getIntent().getStringExtra(Constants.NAME_TYPE));
            nameTypeObj.put(Constants.FIELD_ID, "40");
            nameTypeObj.put(Constants.FIELD_VALUE_CODE, "40");
            fieldArray.put(nameTypeObj);
            jsonObject.put("FIELDS", fieldArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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