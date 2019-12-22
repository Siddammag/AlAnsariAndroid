package app.alansari.modules.promotions;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.R;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.AddsData;
import app.alansari.modules.promotions.adapter.PromotionAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.ADDS;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by FuGenX-test on 29-03-2018.
 */

public class PromotionsActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {
    private Context context;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private PromotionAdapter promotionAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(context));
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout();
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchAdds();
                } else
                    onItemsLoadComplete();
            }
        });
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotions_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Promotions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        init();
        promotionAdapter = new PromotionAdapter(context, multiStateView, new ArrayList<AddsData>());
        recyclerView.setAdapter(promotionAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchAdds();
    }

    private void fetchAddsOld() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAds((String)
                    SharedPreferenceManger.getPrefVal(Constants.USER_ID, null,
                    SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)),
                    Constants.AFTER_LOGIN_ADDS_URL, ADDS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(ADDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, ADDS.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    private void fetchAdds() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);

            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchAdsAfterLogin((String)
                            SharedPreferenceManger.getPrefVal(Constants.USER_ID, null,
                            SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context),
                    sessionTime

                    ),
                    Constants.AFTER_LOGIN_ADDS_URL, ADDS, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(ADDS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, ADDS.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    private Boolean isEmpty() {
        return promotionAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        else {
            CommonUtils.setResponseToast(context, viewState);
        }
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
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchAdds();
                break;
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case ADDS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<AddsData> addsData = (ArrayList<AddsData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<AddsData>>() {
                                }.getType());
                                if (addsData != null && addsData.size() > 0) {
                                    promotionAdapter.addArrayList(addsData);
                                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
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
                    } finally {
                        onItemsLoadComplete();
                    }
                } else {
                    setViewState(VIEW_STATE_WRONG);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }
}
