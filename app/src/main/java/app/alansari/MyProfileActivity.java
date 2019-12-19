package app.alansari;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.adapters.MyProfileAdapter;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnCustomClickListenerBoth;
import app.alansari.listeners.OnWebServiceResult;


import app.alansari.models.BranchLocatorCityData;
import app.alansari.models.CountryData;
import app.alansari.models.ProfessionalLoadList;
import app.alansari.models.gender.GenderSelection;
import app.alansari.models.profiledetails.ProfileDetails;

import app.alansari.modules.accountmanagement.models.BankPurposeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryAccountTypeCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryBranchCeData;
import app.alansari.modules.accountmanagement.models.BeneficiaryTypeCeData;
import app.alansari.modules.accountmanagement.models.BusinessActivitiesCeData;
import app.alansari.modules.accountmanagement.models.BusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.CompanyTypeCeData;
import app.alansari.modules.accountmanagement.models.ContributionCeData;
import app.alansari.modules.accountmanagement.models.DistrictCeData;
import app.alansari.modules.accountmanagement.models.FundsSourceCeData;
import app.alansari.modules.accountmanagement.models.IdDateTypeCeData;
import app.alansari.modules.accountmanagement.models.IdProofCeData;
import app.alansari.modules.accountmanagement.models.ProfessionCeData;
import app.alansari.modules.accountmanagement.models.ResidentialStatusCeData;
import app.alansari.modules.accountmanagement.models.SubBusinessTypeCeData;
import app.alansari.modules.accountmanagement.models.SubPurposeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseDateTypeCeData;
import app.alansari.modules.accountmanagement.models.TradeLicenseTypeCeData;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.MY_PROFILE_TEMPLATE;
import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_EMPTY;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_ERROR;
import static app.alansari.customviews.MultiStateView.VIEW_STATE_WRONG;

public class MyProfileActivity extends NavigationBaseActivity implements View.OnClickListener, OnWebServiceResult, LogOutTimerUtil.LogOutListener, OnCustomClickListenerBoth {
    private Context context;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private MultiStateView multiStateView;
    private LinearLayoutManager linearLayoutManager;
    private MyProfileAdapter recyclerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("My Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        findViewById(R.id.nav_menu).setOnClickListener(this);
        init();
       /* swipeRefreshLayout.setColorSchemeColors(CommonUtils.getPrimaryColor(this));
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout();*/

        recyclerAdapter = new MyProfileAdapter(this, new ArrayList<ProfileDetails.TEMPLATELISTItem>(), this);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchProfileData();
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
            case R.id.empty_button:
            case R.id.error_button:
                fetchProfileData();
                break;
            case R.id.nav_menu:
                openMenuDrawer();
                break;
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        recyclerAdapter = new MyProfileAdapter(this, new ArrayList<ProfileDetails.TEMPLATELISTItem>(), this);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchProfileData();
    }*/

   /* private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    fetchProfileData();
                } else
                    onItemsLoadComplete();
            }
        });
    }
    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }*/

    private void init() {


       // swipeRefreshLayout = (SwipeRefreshLayout) findViewById(app.alansari.R.id.swipeRefreshLayout);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateViewBank);
        multiStateView.findViewById(R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }

    private void fetchProfileData() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            /*if (!swipeRefreshLayout.isRefreshing()) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            }*/
            String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().myProfile((String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING), sessionTime,LogoutCalling.getDeviceID(context)), Constants.MY_PROFILE_TEMPLATE_URL, MY_PROFILE_TEMPLATE, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(MY_PROFILE_TEMPLATE.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, MY_PROFILE_TEMPLATE.toString());
        } else {
            setViewState(VIEW_STATE_ERROR);
        }
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
            case MY_PROFILE_TEMPLATE:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<ProfileDetails> myProfileData = (ArrayList<ProfileDetails>) new Gson().fromJson(response.getJSONArray("RESULT").toString(), new TypeToken<ArrayList<ProfileDetails>>() {
                                }.getType());
                                ((TextView)findViewById(R.id.card_holder_name)).setText(myProfileData.get(0).getFIRSTNAME()+" "+myProfileData.get(0).getLASTNAME());
                                ((TextView)findViewById(R.id.card_type_membership_no)).setText(myProfileData.get(0).getMEMSHIPCARDNO());
                                ((TextView)findViewById(R.id.card_type_point)).setText(myProfileData.get(0).getLOYALTYPOINTS());

                                if (myProfileData != null && myProfileData.size() > 0) {
                                    recyclerAdapter.addArrayList(myProfileData.get(0).getTEMPLATELIST());
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
                       // onItemsLoadComplete();
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

    @Override
    public void itemClickedListerners(View view, int position, Object dataItem) {
        //if(((ProfileDetails.TEMPLATELISTItem) dataItem).getCATEGORYID().equalsIgnoreCase("500"))
       // startActivity(new Intent(context, MyEmiratesIdActivity.class).putExtra(Constants.OBJECT, (ProfileDetails.TEMPLATELISTItem) dataItem));

        if(position==5) {
            Intent intent=new Intent(context, MyEmiratesIdActivity.class);
            intent.putExtra(Constants.OBJECT, (ProfileDetails.TEMPLATELISTItem) dataItem);
            startActivityForResult(intent,Constants.UPDATED_DATA);
        }else {
             Intent intent=new Intent(context, MyProfileDetails.class);
             intent.putExtra(Constants.OBJECT, (ProfileDetails.TEMPLATELISTItem) dataItem);
             startActivityForResult(intent,Constants.UPDATED_DATA);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == Constants.UPDATED_DATA) {
                    fetchProfileData();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();

        }
    }
}
