package app.alansari;

import android.content.Context;
import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.Validation;
import app.alansari.adapters.SelectCurrencyRecyclerAdapter;
import app.alansari.adapters.SelectCurrencyTravelCardRecyclerAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CurrencyData;
import app.alansari.models.travalcardvalidateflag.TravelCardFlag;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.CURRENCY_URL;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

public class SelectCurrencyFlagTravelCardActivity  extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, CustomClickListener, LogOutTimerUtil.LogOutListener {

    AutoCompleteTextView etKeyword;
    private Gson gson;
    private int selectionType;
    private Context context;
    private View selectedItemBg;
    private RecyclerView recyclerView;
    private TextView tvEmpty, tvError;
    private ImageView ivError, ivEmpty;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CarouselLayoutManager layoutManager;
    private SelectCurrencyTravelCardRecyclerAdapter recyclerAdapter;
    private String sessionTime;
    private ArrayList<TravelCardFlag> travelCardFlags;


    private void init() {
        sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

        etKeyword = (AutoCompleteTextView) findViewById(R.id.search_view);
        selectedItemBg = findViewById(R.id.selected_item_bg);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(this));

        multiStateView = (MultiStateView) findViewById(R.id.multiState_rview);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));
        ivEmpty = ((ImageView) multiStateView.findViewById(R.id.empty_imageView));
        ivError = ((ImageView) multiStateView.findViewById(R.id.error_imageView));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        findViewById(R.id.search_btn).setOnClickListener(this);

        etKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Validation.isValidAutoCompleteTextValue(etKeyword))
                    searchCountry(etKeyword.getText().toString().trim());
                else
                    searchCountry("");
            }
        });

        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (Validation.isValidAutoCompleteTextValue(etKeyword))
                        searchCountry(etKeyword.getText().toString().trim());
                    CommonUtils.hideKeyboard(context);
                    return true;
                }
                return false;
            }
        });


        KeyboardVisibilityEvent.setEventListener((AppCompatActivity) context, new

                KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        recyclerAdapter.isKeyboardVisible = isOpen;
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }

        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency_flag_travel_card);
        context = SelectCurrencyFlagTravelCardActivity.this;
        gson = new Gson();
        init();
        swipeRefreshLayout();

        selectionType = Constants.SELECT_ITEM_COUNTRY;
        if (getIntent().getExtras() != null) {
            selectionType = getIntent().getExtras().getInt(Constants.ITEM_TYPE, Constants.SELECT_ITEM_COUNTRY);
        }
        travelCardFlags = getIntent().getExtras().getParcelableArrayList(Constants.OBJECT);

        //Set RecyclerView
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new SelectCurrencyTravelCardRecyclerAdapter(this, travelCardFlags, this, multiStateView, selectionType);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnScrollListener(new CenterScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //recyclerAdapter.onCenterItemChanged();
                }
            }
        });

        fetchCountriesList();
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchCountriesList();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_down);
    }

    @Override
    protected void onDestroy() {
        AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
        super.onDestroy();
        stopLogoutTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_button:
            case R.id.error_button:
                fetchCountriesList();
                break;
            case R.id.search_btn:
                if (!TextUtils.isEmpty(etKeyword.getText().toString().trim()) && etKeyword.getText().toString().trim().length() > 0)
                    searchCountry(etKeyword.getText().toString().trim());
                CommonUtils.hideKeyboard(context);
                break;
            default:
                break;
        }
    }

    public void searchCountry(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        CommonUtils.hideKeyboard(context);
        if (layoutManager.getCenterItemPosition() == position) {
            if (dataItem != null) {
                Intent data = new Intent();
                data.putExtra(Constants.OBJECT, (TravelCardFlag) dataItem);
                setResult(AppCompatActivity.RESULT_OK, data);
                //deleteList((TravelCardFlag) dataItem);


            } else {
                setResult(AppCompatActivity.RESULT_CANCELED, null);
            }
            onBackPressed();
        }
    }
    private void deleteList(TravelCardFlag dataItem) {

        for (int i = 0; i < travelCardFlags.size(); i++) {
            if (recyclerAdapter.getItemAt(i).getCCYCODE().equalsIgnoreCase(dataItem.getCCYCODE())) {
                travelCardFlags.remove(i);
                recyclerAdapter.delete(i);


            }
        }
    }
    void fetchCountriesList() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                selectedItemBg.setVisibility(View.GONE);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                swipeRefreshLayout.setRefreshing(true);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCountriesList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, "0", SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),sessionTime), Constants.CURRENCY_URL, CURRENCY_URL, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.CURRENCY_URL.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.CURRENCY_URL.toString());
        } else {
            if (isEmpty())
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

   /* private void updateData(ArrayList<TravelCardFlag> countryData) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        recyclerAdapter.addArrayList(countryData);
        selectedItemBg.setVisibility(View.VISIBLE);
        etKeyword.setEnabled(true);
    }*/

    private Boolean isEmpty() {
        return (recyclerAdapter.getItemCount() == 0);
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case CURRENCY_URL:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CurrencyData> countryData = (ArrayList<CurrencyData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CurrencyData>>() {
                                }.getType());
                                if (countryData != null && countryData.size() > 0) {
                                    //updateData(countryData);
                                } else {
                                    if (isEmpty()) {
                                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, ivEmpty, null);
                                    }
                                }

                            } else {
                                if (isEmpty()) {
                                    CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, ivEmpty, null);
                                }
                            }
                        } else {
                            if (isEmpty()) {
                                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_EMPTY, tvEmpty, ivEmpty, null);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (isEmpty()) {
                            CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, ivError, null);
                        }
                    } finally {
                        onItemsLoadComplete();
                    }
                } else {
                    if (isEmpty()) {
                        CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_WRONG, tvError, ivError, null);
                    }
                }
                break;
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
