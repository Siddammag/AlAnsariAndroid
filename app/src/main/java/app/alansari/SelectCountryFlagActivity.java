package app.alansari;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.core.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import app.alansari.adapters.SelectCountryRecyclerAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.customviews.carousellayoutmanager.CarouselLayoutManager;
import app.alansari.customviews.carousellayoutmanager.CarouselZoomPostLayoutListener;
import app.alansari.customviews.carousellayoutmanager.CenterScrollListener;
import app.alansari.listeners.CustomClickListener;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.CountryData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 19 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SelectCountryFlagActivity extends AppCompatActivity implements OnClickListener, OnWebServiceResult, CustomClickListener, LogOutTimerUtil.LogOutListener {

    AutoCompleteTextView etKeyword;
    private Gson gson;
    private boolean hideFirstItem = false;
    private int selectionType;
    private Context context;
    private View selectedItemBg;
    private RecyclerView recyclerView;
    private TextView tvEmpty, tvError;
    private ImageView ivError, ivEmpty;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CarouselLayoutManager layoutManager;
    private SelectCountryRecyclerAdapter recyclerAdapter;
    private boolean isForNationality = false;
    private String sessionTime;
    private String nationality = "";

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
        setContentView(R.layout.select_country_activity);
        context = SelectCountryFlagActivity.this;
        gson = new Gson();
        init();
        selectedItemBg.setVisibility(View.GONE);
        swipeRefreshLayout();

        selectionType = Constants.SELECT_ITEM_COUNTRY;
        if (getIntent().getExtras() != null) {
            hideFirstItem = getIntent().getBooleanExtra("hideFirstItem", false);
            selectionType = getIntent().getExtras().getInt(Constants.ITEM_TYPE, Constants.SELECT_ITEM_COUNTRY);
            isForNationality = getIntent().getExtras().getBoolean(Constants.IS_FOR_NATIONANLITY, false);
            nationality = getIntent().getExtras().getString(Constants.NATIONALITY_NAME);

        }

        //Set RecyclerView
        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new SelectCountryRecyclerAdapter(this, new ArrayList<CountryData>(), this, multiStateView, selectionType);
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

        if (selectionType == Constants.SELECT_ITEM_COUNTRY) {
            if (!(Boolean) SharedPreferenceManger.getPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN)) {
                fetchCountriesList();
            } else {
                getData();
            }
        } else {
            if (!(Boolean) SharedPreferenceManger.getPrefVal(Constants.FETCH_WU_COUNTRY_DATA_OFF, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN)) {
                fetchWuCountriesList();
            } else {
                getWuData();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    protected void onDestroy() {
        AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
        super.onDestroy();
        stopLogoutTimer();
    }

    private void getData() {
        ArrayList<CountryData> countryData = SharedPreferenceManger.loadCountriesData();
        if (countryData != null && countryData.size() > 0) {
            updateData(countryData);
        }
    }

    private void getWuData() {
        ArrayList<CountryData> countryData = SharedPreferenceManger.loadWuCountriesData();
        if (countryData != null && countryData.size() > 0) {
            updateData(countryData);
        }
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    if (selectionType == Constants.SELECT_ITEM_COUNTRY) {
                        fetchCountriesList();
                    } else {
                        fetchWuCountriesList();
                    }
                } else
                    onItemsLoadComplete();
            }
        });
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

    void fetchCountriesList() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                selectedItemBg.setVisibility(View.GONE);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                swipeRefreshLayout.setRefreshing(true);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCountriesList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ALL_COUNTRIES_URL, CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ALL_COUNTRIES.toString());
        } else {
            if (isEmpty())
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    public void searchCountry(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    private Boolean isEmpty() {
        return (recyclerAdapter.getItemCount() == 0);
    }

    @Override
    public void itemClicked(View view, int position, Object dataItem) {
        CommonUtils.hideKeyboard(context);
        if (layoutManager.getCenterItemPosition() == position) {
            if (dataItem != null) {
                if (selectionType == Constants.SELECT_ITEM_COUNTRY && !isForNationality)
                    if (/*!nationality.equalsIgnoreCase("nation")*/ true) {
                        // TODO (Hard coded string by Murari)
                        SharedPreferenceManger.saveSelectedCountryData(new Gson(), (CountryData) dataItem);
                    }
                Intent data = new Intent();
                data.putExtra(Constants.OBJECT, (CountryData) dataItem);
                if (getIntent().getIntExtra(Constants.ID, -1) != -1) {
                    data.putExtra(Constants.ID, getIntent().getIntExtra(Constants.ID, -1));
                }
                setResult(AppCompatActivity.RESULT_OK, data);
            } else {
                setResult(AppCompatActivity.RESULT_CANCELED, null);
            }
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_down);
    }

    void fetchWuCountriesList() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                selectedItemBg.setVisibility(View.GONE);
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                swipeRefreshLayout.setRefreshing(true);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchCountriesList((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context), sessionTime), Constants.FETCH_ALL_WU_COUNTRIES_URL, CommonUtils.SERVICE_TYPE.FETCH_ALL_WU_COUNTRIES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_ALL_WU_COUNTRIES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_ALL_WU_COUNTRIES.toString());
        } else {
            if (isEmpty())
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, ivError, null);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_ALL_COUNTRIES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CountryData> countryData = (ArrayList<CountryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CountryData>>() {
                                }.getType());
                                if (countryData != null && countryData.size() > 0) {
                                    updateData(countryData);
                                    SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                    SharedPreferenceManger.saveCountriesData(new Gson(), countryData);
                                    for (int i = 0; i < countryData.size(); i++) {
                                        if (countryData.get(i).getCountryCodeAREX().equalsIgnoreCase("91") || countryData.get(i).getCountryCodeCE().equalsIgnoreCase("091")) {
                                            SharedPreferenceManger.saveUAECountryData(new Gson(), countryData.get(i));
                                            break;
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
            case FETCH_ALL_WU_COUNTRIES:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<CountryData> countryData = (ArrayList<CountryData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<CountryData>>() {
                                }.getType());
                                if (countryData != null && countryData.size() > 0) {
                                    updateData(countryData);
                                    SharedPreferenceManger.setPrefVal(Constants.FETCH_WU_COUNTRY_DATA_OFF, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
                                    SharedPreferenceManger.saveWuCountriesData(new Gson(), countryData);
                                    for (int i = 0; i < countryData.size(); i++) {
                                        if (countryData.get(i).getCountryCodeAREX().equalsIgnoreCase("91") || countryData.get(i).getCountryCodeCE().equalsIgnoreCase("091")) {
                                            SharedPreferenceManger.saveWuUAECountryData(new Gson(), countryData.get(i));
                                            break;
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

    private void updateData(ArrayList<CountryData> countryData) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        if (hideFirstItem && countryData.get(0).getLatinName().trim().equalsIgnoreCase("ALL")) {
            countryData.remove(0);
        }
        recyclerAdapter.addArrayList(countryData);
        selectedItemBg.setVisibility(View.VISIBLE);
        etKeyword.setEnabled(true);
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doLogout() {
        boolean mlogout = CommonUtils.isAppOnForeground(context);
        if (mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


}
