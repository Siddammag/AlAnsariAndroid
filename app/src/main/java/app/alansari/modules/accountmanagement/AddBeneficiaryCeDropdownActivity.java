package app.alansari.modules.accountmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.LoadDropDownField;
import app.alansari.models.additionalforCe.ContributionCeList;
import app.alansari.models.additionalforCe.SourcesOfFundLists;
import app.alansari.modules.accountmanagement.adapters.AddBeneficiaryCeDropdownRecyclerAdapter;
import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryDynamicFields;
import app.alansari.modules.accountmanagement.models.BeneficiaryTypeCeData;
import app.alansari.modules.accountmanagement.models.BusinessActivitiesCeData;
import app.alansari.modules.accountmanagement.models.BusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.CompanyTypeCeData;
import app.alansari.modules.accountmanagement.models.ContributionCeData;
import app.alansari.modules.accountmanagement.models.DistrictCeData;
import app.alansari.modules.accountmanagement.models.FundsSourceCeData;
import app.alansari.modules.accountmanagement.models.IdProofCeData;
import app.alansari.modules.accountmanagement.models.ProfessionCeData;
import app.alansari.modules.accountmanagement.models.PurposeCeData;
import app.alansari.modules.accountmanagement.models.SubBusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.SubPurposeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseTypeCeData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_12;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_28;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_29;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_32;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_33;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_39;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_44;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_45;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_54;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_8;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_86;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_9;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_92;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_94;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_97;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_98;
import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CE_BENF_FIELD_ID_99;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.Load_Drop_Down;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;


/**
 * Created by Parveen Dala on 12 July, 2017
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class AddBeneficiaryCeDropdownActivity extends AppCompatActivity implements OnClickListener, OnWebServiceResult, CustomClickListener , LogOutTimerUtil.LogOutListener  {

    private int type;
    private String purposeId;
    private Context context;
    private String url;
    private CommonUtils.SERVICE_TYPE serviceType;
    private AutoCompleteTextView etKeyword;
    private RecyclerView recyclerView;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private AddBeneficiaryCeDropdownRecyclerAdapter recyclerAdapter;
    private String  BANK_ID,FIELD_ID,FIELD_KEY,CURRENCY_ID,COUNTRY_ID;
    private String sessionTime,userPkId;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        userPkId=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        etKeyword = (AutoCompleteTextView) findViewById(app.alansari.R.id.search_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(this));

        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));

        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);

        findViewById(app.alansari.R.id.close_btn).setOnClickListener(this);

        etKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etKeyword.getText().toString().trim().length() >= 0)
                    searchData(etKeyword.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etKeyword.getText().toString().trim().length() >= 0)
                        searchData(etKeyword.getText().toString().trim());
                    CommonUtils.hideKeyboard(context);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.select_item_activity);
        context = AddBeneficiaryCeDropdownActivity.this;
        init();
        type = Integer.valueOf(getIntent().getExtras().getString(Constants.ITEM_TYPE));
        if (type == Constants.SELECT_ITEM_BENF_FIELD_ID_28) {
            purposeId = getIntent().getExtras().getString(Constants.PURPOSE_ID);
        }
        etKeyword.setHint("Type / Select " + getIntent().getExtras().getString(Constants.TITLE));
        swipeRefreshLayout();

        recyclerAdapter = new AddBeneficiaryCeDropdownRecyclerAdapter(this, new ArrayList<>(), this, multiStateView);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

     /*  Log.("country_id","");
        intent.putExtra("bank_id","");
        intent.putExtra("currency_id","");
        intent.putExtra("feild_id","");
        intent.putExtra("feild_key","");*/
       // Log.e("fhuasgcvgbf",""+dataObject.getCountryData().getCountryCodeCE());


        fetchData();
    }

    @Override
    protected void onDestroy() {
        if (serviceType != null)
            AppController.getInstance().getRequestQueue().cancelAll(serviceType.toString());
        super.onDestroy();
        stopLogoutTimer();
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchData();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchData();
                break;
            case app.alansari.R.id.close_btn:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public void searchData(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        CommonUtils.hideKeyboard(context);
        if (dataItem != null) {
            Intent data = new Intent();
            data.putExtra(Constants.OBJECT, (Parcelable) dataItem);
            setResult(AppCompatActivity.RESULT_OK, data);

          //  Log.e("nsjfjs",""+position+"  "+ dataItem+" "+data);

        } else {
            setResult(AppCompatActivity.RESULT_CANCELED, null);
        }
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(app.alansari.R.anim.hold, app.alansari.R.anim.pull_out_to_down);
    }

    void fetchData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                swipeRefreshLayout.setRefreshing(true);
            }
            etKeyword.setEnabled(false);
            switch (type) {
               /* case Constants.SELECT_ITEM_BENF_FIELD_ID_8:
                    serviceType = FETCH_CE_BENF_FIELD_ID_8;
                   url = Constants.FETCH_CE_BENF_FIELD_ID_8_URL;
                   break;*/
                case Constants.SELECT_ITEM_BENF_FIELD_ID_9:
                    serviceType = FETCH_CE_BENF_FIELD_ID_9;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_9_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_12:
                    serviceType = FETCH_CE_BENF_FIELD_ID_12;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_12_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_16:
                    setLocalData((ArrayList<String>) Arrays.asList(getResources().getStringArray(app.alansari.R.array.id_date_type)));
                    return;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_18:
                    setLocalData((ArrayList<String>) Arrays.asList(getResources().getStringArray(app.alansari.R.array.residential_status)));
                    return;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_28:
                    serviceType = FETCH_CE_BENF_FIELD_ID_28;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_28_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_29:
                    serviceType = FETCH_CE_BENF_FIELD_ID_29;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_29_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_32:
                    serviceType = FETCH_CE_BENF_FIELD_ID_32;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_32_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_33:
                    serviceType = FETCH_CE_BENF_FIELD_ID_33;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_33_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_35:
                    setLocalData((ArrayList<String>) Arrays.asList(getResources().getStringArray(app.alansari.R.array.tl_date_type)));
                    return;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_39:
                    serviceType = FETCH_CE_BENF_FIELD_ID_39;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_39_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_44:
                    serviceType = FETCH_CE_BENF_FIELD_ID_44;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_44_URL;
                    JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCeBeneficiaryBranch_44(getIntent().getExtras().getString(Constants.BANK_CODE, ""), getIntent().getExtras().getString(Constants.TRANSFER_TYPE, ""),userPkId, LogoutCalling.getDeviceID(context),sessionTime), url, serviceType, Request.Method.POST, this);
                    AppController.getInstance().getRequestQueue().cancelAll(serviceType.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, serviceType.toString());
                    return;

                case Constants.SELECT_ITEM_BENF_FIELD_ID_6:
                    serviceType = Load_Drop_Down;
                    url = Constants.loadDropDownList;
                    BANK_ID=getIntent().getStringExtra("BANK_ID");
                    FIELD_ID=getIntent().getStringExtra("FIELD_ID");
                    FIELD_KEY=getIntent().getStringExtra("FIELD_KEY");
                    CURRENCY_ID=getIntent().getStringExtra("CURRENCY_ID");
                    COUNTRY_ID=getIntent().getStringExtra("COUNTRY_ID");

                    JsonObjectRequest jsonObjReq2 = new CallAddr().executeApi(new APIRequestParams()
                                  //  .fetchDropDownList(getIntent().getExtras().getString(Constants.BANK_CODE, ""), getIntent().getExtras().getString(Constants.COUNTRY_ID)),
                           //.fet(CommonUtils.getUserId(), dataObject.getCountryData().getId(), CommonUtils.getDefaultCurrencyData(dataObject.getCountryData()).getCurrencyCode(), dataObject.getBankData().getId()))
                            .fetchDropDownList(BANK_ID,FIELD_ID,FIELD_KEY,CURRENCY_ID,COUNTRY_ID,userPkId,LogoutCalling.getDeviceID(context),sessionTime),
                            url, serviceType, Request.Method.POST, this);
                    AppController.getInstance().getRequestQueue().cancelAll(serviceType.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq2, serviceType.toString());

                    return;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_45:
                    serviceType = FETCH_CE_BENF_FIELD_ID_45;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_45_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_54:
                    serviceType = FETCH_CE_BENF_FIELD_ID_54;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_54_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_59:
                    setLocalData((ArrayList<String>) Arrays.asList(getResources().getStringArray(app.alansari.R.array.residential_status)));
                    return;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_86:
                    serviceType = FETCH_CE_BENF_FIELD_ID_86;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_86_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_92:
                    serviceType = FETCH_CE_BENF_FIELD_ID_92;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_92_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_94:
                    serviceType = FETCH_CE_BENF_FIELD_ID_94;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_94_URL;
                    return;
               /* case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                    serviceType = FETCH_CE_BENF_FIELD_ID_97;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_97_URL;
                    break;*/
                case Constants.SELECT_ITEM_BENF_FIELD_ID_98:
                    serviceType = FETCH_CE_BENF_FIELD_ID_98;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_98_URL;
                    break;
                case Constants.SELECT_ITEM_BENF_FIELD_ID_99:
                    serviceType = FETCH_CE_BENF_FIELD_ID_99;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_99_URL;
                    break;
//new Philipline
                case Constants.SELECT_ITEM_BENF_FIELD_ID_88:
                    serviceType = FETCH_CE_BENF_FIELD_ID_8;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_88_URL;
                    break;

                case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                    serviceType = FETCH_CE_BENF_FIELD_ID_97;
                    url = Constants.FETCH_CE_BENF_FIELD_ID_97_URL;
                    break;
            }
            if (serviceType != null && url != null) {
                JsonObjectRequest jsonObjReq = null;
                if (type == Constants.SELECT_ITEM_BENF_FIELD_ID_28) {
                    jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchSubPurpose(String.valueOf(purposeId),userPkId,LogoutCalling.getDeviceID(context),sessionTime), url, serviceType, Request.Method.POST, this);
                    AppController.getInstance().getRequestQueue().cancelAll(serviceType.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, serviceType.toString());
                } else {
                    jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userPkId,sessionTime,LogoutCalling.getDeviceID(context)), url, serviceType, Request.Method.POST, this);
                    AppController.getInstance().getRequestQueue().cancelAll(serviceType.toString());
                    AppController.getInstance().addToRequestQueue(jsonObjReq, serviceType.toString());
                }

            } else {
                setViewState(VIEW_STATE_WRONG);
            }
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    private void setLocalData(ArrayList<String> dataList) {
        try {
            if (dataList != null && dataList.size() > 0) {
                recyclerAdapter.addArrayList(dataList);
                updateData();
            } else
                setViewState(VIEW_STATE_EMPTY);
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        if (status == Constants.RESPONSE_SUCCESS) {
            try {
                if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                    if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                        switch (type) {
                           /* case Constants.SELECT_ITEM_BENF_FIELD_ID_8:
                                ArrayList<FundsSourceCeData> fundsSourceCeDataList = (ArrayList<FundsSourceCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<FundsSourceCeData>>() {
                                }.getType());
                                if (fundsSourceCeDataList != null && fundsSourceCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(fundsSourceCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;*/
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_9:
                                ArrayList<PurposeCeData> purposeCeDataList = (ArrayList<PurposeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<PurposeCeData>>() {
                                }.getType());
                                if (purposeCeDataList != null && purposeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(purposeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;



                            case Constants.SELECT_ITEM_BENF_FIELD_ID_6:
                                ArrayList<LoadDropDownField> loadDropDownFields = (ArrayList<LoadDropDownField>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<LoadDropDownField>>() {
                                }.getType());
                                if (loadDropDownFields != null && loadDropDownFields.size() > 0) {
                                    recyclerAdapter.addArrayList(loadDropDownFields);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                               // Log.e("fchsjgbvus",""+response.toString());


                                break;




                            case Constants.SELECT_ITEM_BENF_FIELD_ID_12:
                                ArrayList<IdProofCeData> idProofCeDataList = (ArrayList<IdProofCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<IdProofCeData>>() {
                                }.getType());
                                if (idProofCeDataList != null && idProofCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(idProofCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_16:
                                //Not Required Local Data IdDateTypeCeData Data
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_18:
                                //Not Required Local Data Residential Status Data
                                break;

                            case Constants.SELECT_ITEM_BENF_FIELD_ID_28:
                                ArrayList<SubPurposeCeData> subPurposeCeDataList = (ArrayList<SubPurposeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<SubPurposeCeData>>() {
                                }.getType());
                                if (subPurposeCeDataList != null && subPurposeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(subPurposeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_29:
                                ArrayList<ProfessionCeData> professionCeDataList = (ArrayList<ProfessionCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ProfessionCeData>>() {
                                }.getType());
                                if (professionCeDataList != null && professionCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(professionCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_32:
                                ArrayList<CompanyTypeCeData> companyTypeCeDataList = (ArrayList<CompanyTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CompanyTypeCeData>>() {
                                }.getType());
                                if (companyTypeCeDataList != null && companyTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(companyTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_33:
                                ArrayList<TradeLicenseTypeCeData> tradeLicenseTypeCeDataList = (ArrayList<TradeLicenseTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<TradeLicenseTypeCeData>>() {
                                }.getType());
                                if (tradeLicenseTypeCeDataList != null && tradeLicenseTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(tradeLicenseTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_35:
                                //Not Required Local Data TradeLicenseDateTypeCeData Data
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_39:
                                ArrayList<BusinessActivitiesCeData> businessActivitiesCeDataList = (ArrayList<BusinessActivitiesCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BusinessActivitiesCeData>>() {
                                }.getType());
                                if (businessActivitiesCeDataList != null && businessActivitiesCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(businessActivitiesCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_44:
                                ArrayList<BeneficiaryBranchCeData> beneficiaryBranchCeDataList = (ArrayList<BeneficiaryBranchCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryBranchCeData>>() {
                                }.getType());
                                if (beneficiaryBranchCeDataList != null && beneficiaryBranchCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(beneficiaryBranchCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_45:
                                ArrayList<BeneficiaryAccountTypeCeData> beneficiaryAccountTypeCeDataList = (ArrayList<BeneficiaryAccountTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryAccountTypeCeData>>() {
                                }.getType());
                                if (beneficiaryAccountTypeCeDataList != null && beneficiaryAccountTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(beneficiaryAccountTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_54:
                                ArrayList<BankPurposeCeData> bankPurposeCeDataList = (ArrayList<BankPurposeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BankPurposeCeData>>() {
                                }.getType());
                                if (bankPurposeCeDataList != null && bankPurposeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(bankPurposeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_59:
                                //Not Required Local Data Residential Status Data
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_86:
                                ArrayList<DistrictCeData> districtCeDataList = (ArrayList<DistrictCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<DistrictCeData>>() {
                                }.getType());
                                if (districtCeDataList != null && districtCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(districtCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_92:
                                ArrayList<BusinessActivitiesCeData> businessActivitiesCeDataList2 = (ArrayList<BusinessActivitiesCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BusinessActivitiesCeData>>() {
                                }.getType());
                                if (businessActivitiesCeDataList2 != null && businessActivitiesCeDataList2.size() > 0) {
                                    recyclerAdapter.addArrayList(businessActivitiesCeDataList2);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_94:
                                ArrayList<BeneficiaryTypeCeData> beneficiaryTypeCeDataList = (ArrayList<BeneficiaryTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BeneficiaryTypeCeData>>() {
                                }.getType());
                                if (beneficiaryTypeCeDataList != null && beneficiaryTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(beneficiaryTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                           /* case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                                ArrayList<ContributionCeData> contributionCeDataList = (ArrayList<ContributionCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ContributionCeData>>() {
                                }.getType());
                                if (contributionCeDataList != null && contributionCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(contributionCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;*/
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_98:
                                ArrayList<BusinessTypeCeData> businessTypeCeDataList = (ArrayList<BusinessTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BusinessTypeCeData>>() {
                                }.getType());
                                if (businessTypeCeDataList != null && businessTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(businessTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_99:
                                ArrayList<SubBusinessTypeCeData> subBusinessTypeCeDataList = (ArrayList<SubBusinessTypeCeData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<SubBusinessTypeCeData>>() {
                                }.getType());
                                if (subBusinessTypeCeDataList != null && subBusinessTypeCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(subBusinessTypeCeDataList);
                                    updateData();
                                    return;
                                }
                                setViewState(VIEW_STATE_EMPTY);
                                break;

                                case Constants.SELECT_ITEM_BENF_FIELD_ID_88:
                                ArrayList<SourcesOfFundLists> subDataList = (ArrayList<SourcesOfFundLists>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<SourcesOfFundLists>>() {
                                }.getType());
                                if (subDataList != null && subDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(subDataList);
                                    updateData();
                                    return;
                                }
                            case Constants.SELECT_ITEM_BENF_FIELD_ID_97:
                                ArrayList<ContributionCeList> contributionCeDataList = (ArrayList<ContributionCeList>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<ContributionCeList>>() {
                                }.getType());
                                if (contributionCeDataList != null && contributionCeDataList.size() > 0) {
                                    recyclerAdapter.addArrayList(contributionCeDataList);
                                    updateData();
                                    return;
                                }

                                setViewState(VIEW_STATE_EMPTY);
                                break;
                        }
                    } else {
                        setViewState(VIEW_STATE_EMPTY);
                    }
                } else {
                    setViewState(VIEW_STATE_EMPTY);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                setViewState(VIEW_STATE_WRONG);
            } finally {
                onItemsLoadComplete();
            }
        } else if (status == Constants.RESPONSE_TIMEOUT_ERR) {
            setViewState(VIEW_STATE_ERROR);
        } else {
            setViewState(VIEW_STATE_WRONG);
        }
    }

    private void updateData() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        etKeyword.setEnabled(true);
    }

    private void setViewState(int viewState) {
        if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private JSONObject getAccountTypeResponse() {
        try {
            return new JSONObject("{\n" +
                    "  \"RESULT\": [\n" +
                    "    {\n" +
                    "      \"ACC_TYPE_PK_ID\": \"1\",\n" +
                    "      \"ACCOUNT_TYPE\": \"Savings\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"ACC_TYPE_PK_ID\": \"2\",\n" +
                    "      \"ACCOUNT_TYPE\": \"Current\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                    "  \"STATUS_CODE\": \"236\",\n" +
                    "  \"MESSAGE\": \"SUCCESS\"\n" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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

}
