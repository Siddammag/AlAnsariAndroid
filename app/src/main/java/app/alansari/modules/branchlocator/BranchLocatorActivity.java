package app.alansari.modules.branchlocator;

import android.Manifest;
import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.RealTimePermission;
import app.alansari.Utils.Validation;
import app.alansari.customviews.MultiStateView;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.models.BranchLocatorData;
import app.alansari.modules.branchlocator.adapters.BranchLocatorRecyclerAdapter;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SUCCESS;

/**
 * Created by Parveen Dala on 04 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> , LogOutTimerUtil.LogOutListener  {

    static final int REQUEST_CHECK_SETTINGS = 19994;
    private static final long UPDATE_INTERVAL = 20000;
    private static final long FASTEST_INTERVAL = 10000;
    LocationRequest mLocationRequest;
    private String cityId, selectedBranchId;
    private Context context;
    private ImageView btnSearch;
    private TextView tvEmpty, tvError;
    private RecyclerView recyclerView;
    private AutoCompleteTextView etSearch;
    private MultiStateView multiStateView;
    private LinearLayoutManager linearLayoutManager;
    private BranchLocatorRecyclerAdapter recyclerAdapter;
    //Top Details Layout Data
    private BranchLocatorData selectedBranch;
    private AppCompatImageView ivOpenStatus;
    private TextView tvName, tvAddress, tvOpenStatus;
    private View detailsLayout, mapLayout;
    private AppCompatImageView btnSwitchToList;
    private HashMap<Marker, Long> mHashMap = new HashMap<Marker, Long>();
    // Location Data
    private GoogleMap map;
    private Location mLocation;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        etSearch = (AutoCompleteTextView) findViewById(app.alansari.R.id.search_view);
        btnSearch = (ImageView) findViewById(app.alansari.R.id.search_btn);
        detailsLayout = findViewById(app.alansari.R.id.details_layout);
        detailsLayout.setOnClickListener(this);
        detailsLayout.setVisibility(View.GONE);
        mapLayout = findViewById(app.alansari.R.id.map_layout);
        mapLayout.setVisibility(View.GONE);
        tvName = (TextView) findViewById(app.alansari.R.id.name);
        ivOpenStatus = (AppCompatImageView) findViewById(app.alansari.R.id.open_status_icon);
        tvAddress = (TextView) findViewById(app.alansari.R.id.address);
        tvOpenStatus = (TextView) findViewById(app.alansari.R.id.open_status);
        btnSwitchToList = (AppCompatImageView) findViewById(app.alansari.R.id.fab);
        btnSwitchToList.setOnClickListener(this);
        findViewById(app.alansari.R.id.navigation_icon).setOnClickListener(this);

        (findViewById(app.alansari.R.id.swipeRefreshLayout)).setEnabled(false);
        multiStateView = (MultiStateView) findViewById(app.alansari.R.id.multiState_rview);
        multiStateView.findViewById(app.alansari.R.id.empty_button).setOnClickListener(this);
        multiStateView.findViewById(app.alansari.R.id.error_button).setOnClickListener(this);
        tvEmpty = ((TextView) multiStateView.findViewById(app.alansari.R.id.empty_textView));
        tvError = ((TextView) multiStateView.findViewById(app.alansari.R.id.error_textView));
        recyclerView = (RecyclerView) findViewById(app.alansari.R.id.recyclerView);

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
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (Validation.isValidAutoCompleteTextValue(etSearch))
                        searchAccount(etSearch.getText().toString().trim());
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
        setContentView(app.alansari.R.layout.branch_locator_activity);
        context = BranchLocatorActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Branches");
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();

        recyclerView.setPadding(0, 0, 0, 0);
        recyclerAdapter = new BranchLocatorRecyclerAdapter(this, new ArrayList<BranchLocatorData>(), multiStateView);
        recyclerView.setAdapter(recyclerAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(app.alansari.R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (getIntent().getExtras() != null) {
            cityId = getIntent().getExtras().getString(Constants.ID, null);
            if (cityId != null) {
                fetchBranches();
                return;
            }
        }
        onBackPressed();
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

    void fetchBranches() {
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            etSearch.setEnabled(false);
            btnSwitchToList.setVisibility(View.GONE);
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            String userPk=(String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().fetchBranchInCity(cityId,userPk, LogoutCalling.getDeviceID(context)), Constants.FETCH_BRANCH_IN_CITY_URL, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_IN_CITY, Request.Method.POST, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.FETCH_BRANCH_IN_CITY.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.FETCH_BRANCH_IN_CITY.toString());
        } else {
            if (isEmpty()) {
                mapLayout.setVisibility(View.GONE);
                btnSwitchToList.setImageResource(app.alansari.R.drawable.icn_map_blue);
                btnSwitchToList.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.GONE);
                CommonUtils.setViewState(multiStateView, MultiStateView.VIEW_STATE_ERROR, tvError, null, null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.empty_button:
            case app.alansari.R.id.error_button:
                fetchBranches();
                break;
            case app.alansari.R.id.search_btn:
                if (!TextUtils.isEmpty(etSearch.getText().toString().trim()) && etSearch.getText().toString().trim().length() > 0)
                    searchAccount(etSearch.getText().toString().trim());
                CommonUtils.hideKeyboard(context);
                break;
            case app.alansari.R.id.fab:
                if (mapLayout.getVisibility() == View.GONE) {
                    showMap();
                } else {
                    mapLayout.setVisibility(View.GONE);
                    btnSwitchToList.setImageResource(app.alansari.R.drawable.icn_map_blue);
                }
                break;
            case app.alansari.R.id.navigation_icon:
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:" + selectedBranch.getLatitude()
                                    + "," + selectedBranch.getLongitude()
                                    + "?q=" + selectedBranch.getLatitude()
                                    + "," + selectedBranch.getLongitude()
                                    + "(" + selectedBranch.getName() + ")"));
                    intent.setComponent(new ComponentName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    try {
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    }
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void showMap() {
        if (!CommonUtils.isGooglePlayServicesAvailable(context)) {
            LogUtils.d("ok", "Play Service Erro");
            return;
        }

        if (!RealTimePermission.checkPermissionLocation(context)) {
            RealTimePermission.requestLocationPermission(context, RealTimePermission.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }

        if (detailsLayout.getVisibility() == View.VISIBLE)
            btnSwitchToList.setVisibility(View.GONE);
        mapLayout.setVisibility(View.VISIBLE);
        btnSwitchToList.setImageResource(app.alansari.R.drawable.icn_list_blue);
        setMapData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RealTimePermission.LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted();
                } else {
                }
        }
    }

    private void permissionGranted() {
        buildGoogleApiClient();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        createLocationRequest();
        setLocationEnabledTrue();
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void createLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    private void setLocationEnabledTrue() {
        if (map != null) {
            if (RealTimePermission.checkPermissionLocation(context)) {
                map.setMyLocationEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        stopLocationUpdates();
    }

    protected void startLocationUpdates() {
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LogUtils.d("ok", "On Connected");
        RealTimePermission.checkLocationSettings(this, mGoogleApiClient, new LocationRequest());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap myMap) {
        this.map = myMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        setLocationEnabledTrue();
        if (mLocation != null) {
            LatLng currentLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937, 78.9629), 10));
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (detailsLayout.getVisibility() == View.VISIBLE) {
                    detailsLayout.setVisibility(View.GONE);
                    btnSwitchToList.setVisibility(View.VISIBLE);
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String clickedBranchId = String.valueOf(mHashMap.get(marker));
                if (clickedBranchId.equals(selectedBranchId)) {
                    if (detailsLayout.getVisibility() == View.GONE) {
                        detailsLayout.setVisibility(View.VISIBLE);
                        btnSwitchToList.setVisibility(View.GONE);
                    } else {
                        detailsLayout.setVisibility(View.GONE);
                        btnSwitchToList.setVisibility(View.VISIBLE);
                    }
                } else {
                    selectedBranchId = clickedBranchId;
                    for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
                        if (recyclerAdapter.getItemAt(i).getId().equals(selectedBranchId)) {
                            selectedBranch = recyclerAdapter.getItemAt(i);
                            detailsLayout.setVisibility(View.VISIBLE);
                            btnSwitchToList.setVisibility(View.GONE);
                            tvName.setText(recyclerAdapter.getItemAt(i).getName());
                            tvAddress.setText(recyclerAdapter.getItemAt(i).getAddressLine1());
                            if (recyclerAdapter.getItemAt(i).getOpeningHours() != null) {
                                tvOpenStatus.setText("Open Now");
                                tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
                                ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_open);
                            } else {
                                tvOpenStatus.setText("Closed Now");
                                tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
                                ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_close);
                            }
                            break;
                        }
                    }
                }
                return true;
            }
        });
        setMapData();
    }

    private void setMapData() {
        LogUtils.d("ok", "Set Map Data..  Ready " + recyclerAdapter.getItemCount());
        try {
            if (map != null && recyclerAdapter != null) {
                for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
                    createMarker(recyclerAdapter.getItemAt(i));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void createMarker(BranchLocatorData branchData) {
        if (branchData != null && branchData.getLatitude() != null && branchData.getLongitude() != null) {
            if (mHashMap.size() == 0) {
                if (mLocation != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 17));
                } else
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(branchData.getLatitude()), Double.valueOf(branchData.getLongitude())), 17));
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(branchData.getLatitude()), Double.valueOf(branchData.getLongitude())))
                    .anchor(0.5f, 0.5f)
                    .title(branchData.getName())
                    .icon(CommonUtils.vectorToBitmap(context, app.alansari.R.drawable.svg_map_pin_marked, ContextCompat.getColor(context, app.alansari.R.color.colorE86768)))
                    .snippet(branchData.getAddressLine1()));
            mHashMap.put(marker, Long.valueOf(branchData.getId()));
        }
    }

    public void searchAccount(String keyword) {
        recyclerAdapter.getFilter().filter(keyword);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        if (mapLayout.getVisibility() == View.VISIBLE) {
            btnSwitchToList.setImageResource(app.alansari.R.drawable.icn_list_blue);
            btnSwitchToList.setVisibility(View.VISIBLE);
            mapLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            //overridePendingTransition(R.anim.hold, R.anim.pump_top_to_down);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case AppCompatActivity.RESULT_OK:
                            // All required changes were successfully made
                            startLocationUpdates();
                            break;
                        case AppCompatActivity.RESULT_CANCELED:
                            // The user was asked to change settings, but chose not to
                            LogUtils.d("ok", "Denied");
//                            mapLayout.setVisibility(View.GONE);
//                            btnSwitchToList.setImageResource(R.drawable.icn_map_blue);
                            break;
                        default:
                            break;
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        switch (locationSettingsResult.getStatus().getStatusCode()) {
            case SUCCESS:
                startLocationUpdates();
                break;

            case RESOLUTION_REQUIRED:
                try {
                    LogUtils.d("ok", "Open Setting");
                    locationSettingsResult.getStatus().startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // oops
                }
                break;

            case SETTINGS_CHANGE_UNAVAILABLE:
                // more oops
                LogUtils.d("ok", "Unavailable");
                break;
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
            case FETCH_BRANCH_IN_CITY:
                if (status == Constants.RESPONSE_DUMMY) {
                   // Toast.makeText(context, getString(app.alansari.R.string.dummy_data), Toast.LENGTH_SHORT).show();
                    status = 1;
                    try {
                        response = getResponse();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (status == Constants.RESPONSE_SUCCESS) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            btnSwitchToList.setVisibility(View.VISIBLE);
                            detailsLayout.setVisibility(View.GONE);
                            if (map != null) {
                                map.clear();
                                mHashMap.clear();
                            }
                            if (response.getJSONArray(Constants.RESULT) != null && response.getJSONArray(Constants.RESULT).length() > 0) {
                                ArrayList<BranchLocatorData> branchLocatorData = (ArrayList<BranchLocatorData>) new Gson().fromJson(response.getJSONArray(Constants.RESULT).toString(), new TypeToken<ArrayList<BranchLocatorData>>() {
                                }.getType());
                                if (branchLocatorData != null && branchLocatorData.size() > 0) {
                                    etSearch.setEnabled(true);
                                    recyclerAdapter.addArrayList(branchLocatorData);
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
                "      \"STORE_CODE\": 1,\n" +
                "      \"STORE_NAME\": \"ABELA BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"ABELA SUPER STORE\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 667 3774\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE\",\n" +
                "      \"OPENING_HOURS\": \"1:08:00:23:00,2:08:00:23:00,3:08:00:23:00,4:08:00:23:00,5:08:00:23:00,6:14:00:23:00,7:08:00:23:00\",\n" +
                "      \"LATITUDE\": \"24.475626\",\n" +
                "      \"LONGITUDE\": \"54.346511\",\n" +
                "      \"IMAGES\": \"http://www.ecns.cn/visual/2012/01-11/U364P886T1D5911F12DT20120111152444.jpg\", " +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"ABELA@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 667 3653\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 2,\n" +
                "      \"STORE_NAME\": \"ABU DHABI MALL BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"ABU DHABI MALL, TOURIST CLUB AREA\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 645 4800\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:09:00:23:00,2:09:00:23:00,3:09:00:23:00,4:09:00:23:00,5:09:00:23:00,6:15:30:23:00,7:09:00:23:00\",\n" +
                "      \"LATITUDE\": \"24.495955\",\n" +
                "      \"LONGITUDE\": \"54.383375\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"AUHMALL@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 645 4900\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 3,\n" +
                "      \"STORE_NAME\": \"AL FALAH PLAZA BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"AL FALAH PLAZA, OLD PASSPORT ROAD\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 641 4014\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:00:23:30,2:08:00:23:30,3:08:00:23:30,4:08:00:23:30,5:08:00:23:30,6:08:30:11:30,6:14:30:23:00,7:08:00:23:30\",\n" +
                "      \"LATITUDE\": \"24.478253\",\n" +
                "      \"LONGITUDE\": \"54.372094\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"ALFALAH@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 641 4015\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 4,\n" +
                "      \"STORE_NAME\": \"AL WAHDA MALL BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"SHEIKH HAZAA BIN ZAYED STREET\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": null,\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 443 7066\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:09:00:22:00,2:09:00:22:00,3:09:00:22:00,4:09:00:22:00,5:09:00:23:00,6:09:00:23:00,7:09:00:23:00\",\n" +
                "      \"LATITUDE\": \"24.470392\",\n" +
                "      \"LONGITUDE\": \"54.372882\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"ALWAHDAMALL@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 443 7128\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 5,\n" +
                "      \"STORE_NAME\": \"BAIN AL JESRAIN BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"BAIN AL JESRAIN CO-OP SOCIETY, MUSSAFAH\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 558 8925\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:00:23:00,2:08:00:23:00,3:08:00:23:00,4:08:00:23:00,5:08:00:23:00,6:17:00:23:00,7:08:00:23:00\",\n" +
                "      \"LATITUDE\": \"24.407352\",\n" +
                "      \"LONGITUDE\": \"54.505984\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"ALJESRAINAUH@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 558 6290\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 6,\n" +
                "      \"STORE_NAME\": \"BANIYAS BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"BANIYAS CO-OP SOCIETY\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": null,\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 583 2355\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:00:23:00,2:08:00:23:00,3:08:00:23:00,4:08:00:23:00,5:08:00:23:00,6:08:00:12:00,6:14:00:23:00,7:08:00:23:00\",\n" +
                "      \"LATITUDE\": \"24.287368\",\n" +
                "      \"LONGITUDE\": \"54.641124\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"BANIYAS@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 583 2096\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 7,\n" +
                "      \"STORE_NAME\": \"BIDA ZAYED BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"MAIN STREET, BIDA ZAYED\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 884 7101\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:30:13:00,1:16:30:22:00,2:08:30:13:00,2:16:30:22:00,3:08:30:13:00,3:16:30:22:00,4:08:30:13:00,4:16:30:22:00,5:08:30:13:00,5:16:30:22:00,6:16:00:22:00,7:08:30:13:00,7:16:30:22:00\",\n" +
                "      \"LATITUDE\": \"23.648927\",\n" +
                "      \"LONGITUDE\": \"53.70615\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"BIDAZAYED@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 884 8473\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 8,\n" +
                "      \"STORE_NAME\": \"BUTEEN BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"BUTEEN CO-OP SOCIETY\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 666 9822\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:00:00:00,2:08:00:00:00,3:08:00:00:00,4:08:00:00:00,5:08:00:00:00,6:08:00:00:00,7:08:00:00:00\",\n" +
                "      \"LATITUDE\": \"24.461764\",\n" +
                "      \"LONGITUDE\": \"54.359895\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"BUTEEN@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 665 6751\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 9,\n" +
                "      \"STORE_NAME\": \"DALMA MALL BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"1ST FLOOR, NEXT TO AUH ISLAMIC BANK, DALMA MALL, MUSSAFAH\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 550 3798\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:09:00:00:00,2:09:00:00:00,3:09:00:00:00,4:09:00:00:00,5:09:00:00:00,6:09:00:00:00,7:09:00:00:00\",\n" +
                "      \"LATITUDE\": \"24.332942\",\n" +
                "      \"LONGITUDE\": \"54.523987\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"DALMAAUH@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 550 3865\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 10,\n" +
                "      \"STORE_NAME\": \"ELECTRA BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"ELECTRA STREET\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": \"AUH\",\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 639 7518\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:30:23:30,2:08:30:23:30,3:08:30:23:30,4:08:30:23:30,5:08:30:23:30,6:08:30:12:00,6:14:00:23:30,7:08:30:23:30\",\n" +
                "      \"LATITUDE\": \"24.490233\",\n" +
                "      \"LONGITUDE\": \"54.367797\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"ELECTRAAUH@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 639 7782\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"STORE_CODE\": 11,\n" +
                "      \"STORE_NAME\": \"HAMDAN BRANCH\",\n" +
                "      \"ADDRESS_LINE_1\": \"HAMDAN STREET\",\n" +
                "      \"ADDRESS_LINE_2\": \"P. O.BOX: 325\",\n" +
                "      \"CITY\": \"ABU DHABI\",\n" +
                "      \"DISTRICT\": null,\n" +
                "      \"STATE\": null,\n" +
                "      \"COUNTRY\": \"AE\",\n" +
                "      \"POSTAL_CODE\": null,\n" +
                "      \"MAIN_PHONE\": \"02 643 0512\",\n" +
                "      \"HOME_PAGE\": \"HTTP://WWW.ALANSARIEXCHANGE.COM\",\n" +
                "      \"CATEGORIES\": \"CURRENCY EXCHANGE SERVICE,MONEY TRANSFER SERVICES\",\n" +
                "      \"OPENING_HOURS\": \"1:08:30:23:30,2:08:30:23:30,3:08:30:23:30,4:08:30:23:30,5:08:30:23:30,6:16:30:23:00,7:08:30:23:30\",\n" +
                "      \"LATITUDE\": \"24.495305\",\n" +
                "      \"LONGITUDE\": \"54.370393\",\n" +
                "      \"IMAGES\": null,\n" +
                "      \"DESCRIPTION\": \"MONEY TRANSFER, FOREIGN EXCHANGE, REMITTANCE, UTILITY PAYMENTS\",\n" +
                "      \"EMAIL\": \"HAMDANAUH@ALANSARI.AE\",\n" +
                "      \"ALTERNATE_PHONE\": null,\n" +
                "      \"MOBILE\": null,\n" +
                "      \"FAX\": \"02 643 0542\",\n" +
                "      \"PAYMENT_TYPES\": \"CASH,MASTERCARD,VISA\",\n" +
                "      \"AD_ICON_URL\": null,\n" +
                "      \"AD_PHONE\": null,\n" +
                "      \"AD_LANDING_PAGE_URL\": null,\n" +
                "      \"CITY_ID\": \"0\"\n" +
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