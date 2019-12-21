package app.alansari.modules.feedback;

import app.alansari.AppController;
import app.alansari.NavigationBaseActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.FaqData;
import app.alansari.modules.feedback.adapters.FaqRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;

import android.content.Context;
import android.os.Bundle;
import androidx.core.widget.SwipeRefreshLayout;
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

import org.json.JSONObject;

import java.util.ArrayList;

import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.FAQ;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

/**
 * Created by Parveen Dala on 20 February, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class FaqActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult , LogOutTimerUtil.LogOutListener  {

    private Context context;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private FaqRecyclerAdapter recyclerAdapter;

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
        recyclerView.setPadding(0, 0, 0, 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.faq_activity);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("FAQ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        findViewById(app.alansari.R.id.nav_menu).setOnClickListener(this);
        init();

        recyclerAdapter = new FaqRecyclerAdapter(context, new ArrayList<FaqData>(), multiStateView);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchFaqAccounts();
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
                    fetchFaqAccounts();
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
                fetchFaqAccounts();
                break;
            case app.alansari.R.id.nav_menu:
                openMenuDrawer();
                break;
        }
    }

    private void fetchFaqAccounts() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().addingParameters((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), LogoutCalling.getDeviceID(context)), Constants.FAQ_URL, FAQ, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(FAQ.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, FAQ.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
    }

    public void searchAccount(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    private Boolean isEmpty() {
        return recyclerAdapter.getItemCount() == 0;
    }

    private void setViewState(int viewState) {
        if (isEmpty())
            CommonUtils.setViewState(multiStateView, viewState, (viewState == VIEW_STATE_EMPTY ? tvEmpty : tvError), null, null);
        else {
            CommonUtils.setResponseToast(context, viewState);
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case FAQ:
                // status = 1;
                // response = DummyResponse.getFaq();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<FaqData> faqData = (ArrayList<FaqData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<FaqData>>() {
                                }.getType());
                                if (faqData != null && faqData.size() > 0) {
                                    faqData.get(0).setIsReading(1);
                                    recyclerAdapter.addArrayList(faqData);
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