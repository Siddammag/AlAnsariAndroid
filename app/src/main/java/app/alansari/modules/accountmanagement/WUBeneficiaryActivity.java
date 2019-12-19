package app.alansari.modules.accountmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.OnLoadMoreListener;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BeneficiaryData;
import app.alansari.models.WUBeneficiaryData;
import app.alansari.modules.accountmanagement.adapters.WUBeneficiaryRecyclerAdapter;
import app.alansari.modules.sendmoney.WUSendMoneyActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;

public class WUBeneficiaryActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, CustomClickListener , LogOutTimerUtil.LogOutListener  {
    private Context context;
    private AutoCompleteTextView etKeyword;
    private MultiStateView multiStateView;
    private TextView tvError, tvEmpty;
    private WUBeneficiaryRecyclerAdapter wuBeneficiaryRecyclerAdapter;
    private RecyclerView recyclerView;
    private String arexUserId, wuCardNo;

    private ArrayList<WUBeneficiaryData> wuBeneficiaryList;
    private LinearLayoutManager linearLayoutManager;
    private String startCount = "";
    private String TOTAL_WU_POINTS = "0";
    private boolean loadMore = true;
    private String sessionTime;
    private String promoCode;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.wu_beneficiary_activity);
        context = this;
        if (getIntent() == null) {
            finish();
        } else {
            arexUserId = getIntent().getStringExtra(Constants.AREX_MEM_PK_ID);
            wuCardNo = getIntent().getStringExtra(Constants.WU_CARD_NO);
        }
        init();
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

    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        etKeyword = (AutoCompleteTextView) findViewById(app.alansari.R.id.search_view);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        findViewById(app.alansari.R.id.close_btn).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));
        ((EditText) findViewById(R.id.et_wu_number)).setText(wuCardNo);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        wuBeneficiaryList = new ArrayList<>();
        wuBeneficiaryRecyclerAdapter = new WUBeneficiaryRecyclerAdapter(context, wuBeneficiaryList, this, recyclerView, multiStateView,promoCode);
        wuBeneficiaryRecyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMore) {
                    //fetchBeneficiary();
                }
            }
        });
        recyclerView.setAdapter(wuBeneficiaryRecyclerAdapter);

        etKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etKeyword.getText().toString().trim().length() >= 0)
                    searchData(etKeyword.getText().toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
    public void onStart() {
        super.onStart();
        startCount = "";
        fetchBeneficiary();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }


    public void searchData(String keyword) {
        wuBeneficiaryRecyclerAdapter.getFilter().filter(keyword);
    }


    private void fetchBeneficiary() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (wuBeneficiaryRecyclerAdapter.getItemCount() == 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                CommonUtils.showLoading(context, getString(app.alansari.R.string.please_wait), null, false);
            }
            etKeyword.setEnabled(false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchWUBeneficiary((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), arexUserId, "", startCount, LogoutCalling.getDeviceID(context),sessionTime),
                    Constants.FETCH_WU_BENEFICIARY_URL, CommonUtils.SERVICE_TYPE.FETCH_WU_BENEFICIARY, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_WU_BENEFICIARY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_WU_BENEFICIARY.toString());
        } else {
            CommonUtils.setViewState(multiStateView, VIEW_STATE_ERROR, tvError, null, null);
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.empty_button:
                case R.id.error_button:
                    startCount = "";
                    fetchBeneficiary();
                    break;
                case R.id.fab:
                    Intent intent = new Intent(context, WUAddBeneficiarySelectTypeActivity.class);
                    intent.putExtra(Constants.OBJECT, (BeneficiaryData) null);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_RESULT);
                    intent.putExtra(Constants.WU_CARD_NO, wuCardNo);
                    intent.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
                    intent.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
                    startActivityForResult(intent, Constants.ADD_NEW_BENEFICIARY);
                    break;
                case app.alansari.R.id.close_btn:
                    finish();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case FETCH_WU_BENEFICIARY:
                if (status == 1) {
                    try {
                        CommonUtils.hideLoading();
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                if (startCount.equalsIgnoreCase("")) {
                                    wuBeneficiaryRecyclerAdapter.clear();
                                }
                                startCount = response.getString(Constants.WU_CARD_NEXT_RECEIVER_CONTEXT);
                                if (response.getString(Constants.WU_RECEIVER_MORE_FLAG).equalsIgnoreCase("Y")) {
                                    loadMore = true;
                                } else {
                                    loadMore = false;
                                }
                                LogUtils.v("startCount", "startCount:" + startCount);
                                wuCardNo = response.getString(Constants.WU_CARD_NO);
                                promoCode=response.getString(Constants.WU_LOOKUP_PROMO_CODE);
                                TOTAL_WU_POINTS = response.getString(Constants.WU_TOTAL_POINTS_EARNED);
                                JSONArray resultArray = response.getJSONArray(Constants.RESULT);
                                JSONObject resultObj = resultArray.getJSONObject(0);
                                if (resultObj.getString(Constants.WU_RECEIVER_LIST) != null && resultObj.getString(Constants.WU_RECEIVER_LIST) != "null") {
                                    JSONArray list = resultObj.getJSONArray(Constants.WU_RECEIVER_LIST);
                                    etKeyword.setEnabled(true);
                                    wuBeneficiaryList = (ArrayList<WUBeneficiaryData>) new Gson().fromJson(list.toString(), new TypeToken<ArrayList<WUBeneficiaryData>>() {
                                    }.getType());
                                    if (wuBeneficiaryList != null && wuBeneficiaryList.size() > 0) {
                                        wuBeneficiaryRecyclerAdapter.addArrayList(wuBeneficiaryList);
                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    } else {
                                        CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, "");
                                    }
                                } else {
                                    multiStateView.findViewById(R.id.empty_button).setVisibility(View.GONE);
                                    multiStateView.findViewById(R.id.error_button).setVisibility(View.GONE);
                                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, getString(R.string.empty_beneficiary_wu));
                                }
                            } else {
                                CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, null);
                            }
                        } else {
                            CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvEmpty, null, response.getString(Constants.MESSAGE));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvError, null, null);
                    }
                } else {
                    CommonUtils.setViewState(multiStateView, VIEW_STATE_EMPTY, tvError, null, "");
                }
                break;
        }

    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        Intent data = new Intent(context, WUSendMoneyActivity.class);
        data.putExtra(Constants.OBJECT, (WUBeneficiaryData) dataItem);
        data.putExtra(Constants.BENEF_TYPE, "EXIST");
        data.putExtra(Constants.WU_CARD_NO, wuCardNo);
        data.putExtra(Constants.AREX_MEM_PK_ID, arexUserId);
        data.putExtra(Constants.TOTAL_WU_POINTS, TOTAL_WU_POINTS);
        data.putExtra(Constants.WU_LOOKUP_PROMO_CODE,promoCode);
        startActivity(data);
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

