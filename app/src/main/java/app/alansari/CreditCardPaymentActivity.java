package app.alansari;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.adapters.CreditCardPaymentStackAdapter;
import app.alansari.adapters.PromoCodeAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.flatbutton.ButtonFlat;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CreditCardData;
import app.alansari.models.PromoCodeData;
import app.alansari.modules.accountmanagement.AddCreditCardActivity;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FETCH_CREDIT_CARDS;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class CreditCardPaymentActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, CardStackView.ItemExpendListener , LogOutTimerUtil.LogOutListener  {

    private Intent intent;
    private Context context;
    private ImageView btnSearch;
    private AutoCompleteTextView etSearch;
    private TextView tvEmpty, tvError;
    private MultiStateView multiStateView;

    // Stack
    private String creditCardId, source;
    private CardStackView mStackView;
    private CreditCardPaymentStackAdapter stackAdapter;


    //Promo Dialog
    private Dialog promoCodeDialog;
    private RecyclerView promoRecyclerView;
    private TextView tvPromoMessage;
    private Button btnSave;
    private PromoCodeAdapter promoCodeAdapter;
    private ButtonFlat emptyButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        etSearch = (AutoCompleteTextView) findViewById(R.id.search_view);
        btnSearch = (ImageView) findViewById(R.id.search_btn);
        multiStateView = (MultiStateView) findViewById(R.id.multiState_rview);
        emptyButton = multiStateView.findViewById(R.id.empty_button);
        emptyButton.setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));

        findViewById(R.id.fab).setOnClickListener(this);
        findViewById(R.id.nav_menu).setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isValidAutoCompleteTextValue(etSearch))
                    searchAccount(etSearch.getText().toString().trim());
                else
                    searchAccount("");
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validation.isValidAutoCompleteTextValue(etSearch))
                        searchAccount(etSearch.getText().toString().trim());
                    CommonUtils.hideKeyboard(context);
                    return true;
                }
                return false;
            }
        });


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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_payment_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("Credit Card Payment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        if (getIntent() != null && getIntent().getExtras() != null) {
            creditCardId = getIntent().getExtras().getString(Constants.ID, null);
            source = getIntent().getExtras().getString(Constants.SOURCE, null);
        }
        init();

        mStackView = (CardStackView) findViewById(R.id.stackview);
        mStackView.setItemExpendListener(this);
        mStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(mStackView));
        stackAdapter = new CreditCardPaymentStackAdapter(this, multiStateView);
        mStackView.setAdapter(stackAdapter);
        mStackView.setNumBottomShow(3);
        fetchCreditCards();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_menu:
                openMenuDrawer();
                break;
            case R.id.fab:
                intent = new Intent(context, AddCreditCardActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_CREDIT_CARD);
                break;
            case R.id.empty_button:
            case R.id.error_button:
                fetchCreditCards();
                break;
            case R.id.search_btn:
                if (!TextUtils.isEmpty(etSearch.getText().toString().trim()) && etSearch.getText().toString().trim().length() > 0)
                    searchAccount(etSearch.getText().toString().trim());
                CommonUtils.hideKeyboard(context);
                break;
        }
    }

    private void fetchCreditCards() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            etSearch.setEnabled(false);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCreditCardList(CommonUtils.getUserId(), CommonUtils.getMemPkId(Constants.AREX_MAPPING), LogoutCalling.getDeviceID(context),sessionTime), Constants.FETCH_CREDIT_CARDS_URL, FETCH_CREDIT_CARDS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FETCH_CREDIT_CARDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FETCH_CREDIT_CARDS.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    public void searchAccount(String keyword) {
        stackAdapter.getFilter().filter(keyword);
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() && source != null && source.equalsIgnoreCase(Constants.SOURCE_FCM_ACTIVITY)) {
            CommonUtils.goToDashBoard(context);
        } else {
            super.onBackPressed();
        }
    }

    private Boolean isEmpty() {
        return stackAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty()) {
            if (viewState == VIEW_STATE_EMPTY) {
                emptyButton.setVisibility(View.GONE);
                CommonUtils.setViewState(multiStateView, viewState, tvEmpty, null, getString(R.string.empty_credit_card_list));
            }
            if (viewState == VIEW_STATE_WRONG)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, getString(R.string.error_credit_card_list));
            if (viewState == VIEW_STATE_ERROR)
                CommonUtils.setViewState(multiStateView, viewState, tvError, null, null);
        } else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_CREDIT_CARDS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CreditCardData> creditCardData = (ArrayList<CreditCardData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CreditCardData>>() {
                                }.getType());
                                if (creditCardData != null && creditCardData.size() > 0) {
                                    stackAdapter.setArrayData(creditCardData);
                                    stackAdapter.updateData(creditCardData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    etSearch.setEnabled(true);
                                    if (Validation.isValidString(creditCardId))
                                        for (int i = 0; i < creditCardData.size(); i++) {
                                            if (creditCardData.get(i).getId().equalsIgnoreCase(creditCardId)) {
                                                mStackView.updateSelectPosition(i);
                                                return;
                                            }
                                        }

                                    return;
                                }
                            }
                            setViewState(VIEW_STATE_EMPTY);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.REQUEST_CODE_ADD_CREDIT_CARD && resultCode == RESULT_OK) {
                etSearch.setText("");
                fetchCreditCards();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemExpend(boolean expend) {
        //findViewById(R.id.search_card_view).setVisibility(expend ? View.GONE : View.VISIBLE);
    }
    
    public void setPromoData(ArrayList<PromoCodeData> promoCodeData) {
        promoCodeAdapter.addArrayList(promoCodeData);
    }


    public void setListners(final EditText etPromoCode, final boolean isPromoListActive) {

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
                stackAdapter.setPromoCode(promoCodeAdapter.getPromoCode());
                promoCodeDialog.dismiss();
            }
        });

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