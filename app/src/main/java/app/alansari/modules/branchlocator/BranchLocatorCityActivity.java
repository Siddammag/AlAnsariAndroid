package app.alansari.modules.branchlocator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BranchLocatorCityData;
import app.alansari.modules.branchlocator.adapters.BranchLocatorCityRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorCityActivity extends NavigationBaseActivity implements OnWebServiceResult, View.OnClickListener , LogOutTimerUtil.LogOutListener  {

    private Context context;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private BranchLocatorCityRecyclerAdapter recyclerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(context));
        swipeRefreshLayout();
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.branch_locator_city_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Emirates");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        boolean hideMenu = getIntent().getBooleanExtra(Constants.HIDE_BURGER_MENU, false);
        if (hideMenu) {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        } else {
            findViewById(app.alansari.R.id.nav_menu).setVisibility(View.VISIBLE);
            findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        }
        init();
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerAdapter = new BranchLocatorCityRecyclerAdapter(context, new ArrayList<BranchLocatorCityData>());
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        fetchBranchCities();
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

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchBranchCities();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchBranchCities();
                break;
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
        }
    }

    void fetchBranchCities() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing() && recyclerAdapter.getItemCount() <= 0) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            } else {
                swipeRefreshLayout.setRefreshing(true);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addingParameters((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)), Constants.FETCH_BRANCH_CITIES_URL, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_CITIES.toString());
        } else {
            setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == MultiStateView.VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FETCH_BRANCH_CITIES:
/*
                if (status == Constants.RESPONSE_DUMMY) {
                    Toast.makeText(context, getString(app.alansari.R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        response = getResponse();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
*/
                if (status == Constants.RESPONSE_SUCCESS) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BranchLocatorCityData> branchLocatorCityData = (ArrayList<BranchLocatorCityData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BranchLocatorCityData>>() {
                                }.getType());
                                if (branchLocatorCityData != null && branchLocatorCityData.size() > 0) {
                                    recyclerAdapter.addArrayList(branchLocatorCityData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                                    return;
                                }
                            }
                            setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        } else {
                            setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setViewState(MultiStateView.VIEW_STATE_WRONG);
                    } finally {
                        onItemsLoadComplete();
                    }
                } else if (status == Constants.RESPONSE_TIMEOUT_ERR) {
                    setViewState(MultiStateView.VIEW_STATE_ERROR);
                } else {
                    setViewState(MultiStateView.VIEW_STATE_WRONG);
                }
                break;
        }
    }

    private JSONObject getResponse() throws JSONException {
        return new JSONObject("{\n" +
                "  \"RESULT\": [\n" +
                "    {\n" +
                "      \"ID\": \"1\",\n" +
                "      \"NAME\": \"Bangaluru\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ID\": \"2\",\n" +
                "      \"NAME\": \"Delhi\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ID\": \"3\",\n" +
                "      \"NAME\": \"Mumbai\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ID\": \"4\",\n" +
                "      \"NAME\": \"Bhiwani\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"USER_STATUS\": null,\n" +
                "  \"OTP\": null,\n" +
                "  \"AMOUNT\": null,\n" +
                "  \"STATUS_MSG\": \"SUCCESS\",\n" +
                "  \"STATUS_CODE\": \"256\",\n" +
                "  \"MESSAGE\": \"SUCCESS\"\n" +
                "}");
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