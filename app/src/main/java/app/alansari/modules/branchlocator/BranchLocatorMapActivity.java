package app.alansari.modules.branchlocator;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.Utils.RealTimePermission;
import app.alansari.Utils.Validation;
import app.alansari.models.BranchLocatorData;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;
import static com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SUCCESS;

/**
 * Created by Parveen Dala on 07 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class BranchLocatorMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, ResultCallback<LocationSettingsResult>, GoogleApiClient.OnConnectionFailedListener , LogOutTimerUtil.LogOutListener  {

    static final int REQUEST_CHECK_SETTINGS = 19994;
    BranchLocatorData dataObject;
    private GoogleMap map;
    private Context context;
    private View detailsLayout;
    private AppCompatImageView ivOpenStatus;
    private TextView tvName, tvAddress, tvOpenStatus;
    // Location Data
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void init() {
        detailsLayout = findViewById(app.alansari.R.id.details_layout);
        detailsLayout.setOnClickListener(this);
        detailsLayout.setVisibility(View.GONE);
        tvName = (TextView) findViewById(app.alansari.R.id.name);
        ivOpenStatus = (AppCompatImageView) findViewById(app.alansari.R.id.open_status_icon);
        tvAddress = (TextView) findViewById(app.alansari.R.id.address);
        tvOpenStatus = (TextView) findViewById(app.alansari.R.id.open_status);
        findViewById(app.alansari.R.id.navigation_icon).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.alansari.R.layout.branch_locator_map_activity);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context = BranchLocatorMapActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(app.alansari.R.id.toolbar);
        ((TextView) findViewById(app.alansari.R.id.toolbar_title)).setText("Branches");
        findViewById(app.alansari.R.id.nav_menu).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(app.alansari.R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();

        if (getIntent().getExtras() == null)
            return;
        else {
            dataObject = getIntent().getExtras().getParcelable(Constants.OBJECT);
            if (dataObject == null) {
                return;
            }
        }

        if (!RealTimePermission.checkPermissionLocation(context)) {
            RealTimePermission.requestLocationPermission(context, RealTimePermission.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }

        if (!CommonUtils.isGooglePlayServicesAvailable(context)) {
            LogUtils.d("ok", "Play Service Erro");
            return;
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(app.alansari.R.id.map);
        mapFragment.getMapAsync(this);

        tvName.setText(dataObject.getName());
        if (Validation.isValidString(dataObject.getAddressLine1())) {
            if (Validation.isValidString(dataObject.getAddressLine2()))
                tvAddress.setText(dataObject.getAddressLine1() + " " + dataObject.getAddressLine2());
            else
                tvAddress.setText(dataObject.getAddressLine1());
        } else {
            tvAddress.setText("");
        }


        if (CommonUtils.getBranchStatus(dataObject.getOpeningHours())) {
            tvOpenStatus.setText("Open Now");
            tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.color62A930));
            ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_open);
        } else {
            tvOpenStatus.setText("Closed Now");
            tvOpenStatus.setTextColor(ContextCompat.getColor(context, app.alansari.R.color.colorE86768));
            ivOpenStatus.setImageResource(app.alansari.R.drawable.svg_branch_close);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onMapReady(GoogleMap myMap) {
        this.map = myMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        setLocationEnabledTrue();
        if ((dataObject.getLatitude() != null) && (dataObject.getLongitude() != null)) {
            LatLng currentLocation = new LatLng(Double.valueOf(dataObject.getLatitude()), Double.valueOf(dataObject.getLongitude()));
            map.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .anchor(0.5f, 0.5f)
                    .icon(CommonUtils.vectorToBitmap(context, app.alansari.R.drawable.svg_map_pin_marked, ContextCompat.getColor(context, app.alansari.R.color.colorE86768))));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937, 78.9629), 10));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (detailsLayout.getVisibility() == View.GONE) {
                    detailsLayout.setVisibility(View.VISIBLE);
                } else {
                    detailsLayout.setVisibility(View.GONE);
                }
                return true;
            }
        });

        detailsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case app.alansari.R.id.navigation_icon:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("geo:" + dataObject.getLatitude()
                                    + "," + dataObject.getLongitude()
                                    + "?q=" + dataObject.getLatitude()
                                    + "," + dataObject.getLongitude()
                                    + "(" + dataObject.getName() + ")"));
                    intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    try {
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (ActivityNotFoundException anfe) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case AppCompatActivity.RESULT_OK:
                            // All required changes were successfully made
                            LogUtils.d("ok", "Granted");
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
                LogUtils.d("ok", "Open Setting No");
                break;
            case RESOLUTION_REQUIRED:
                try {
                    LogUtils.d("ok", "Open Setting");
                    locationSettingsResult.getStatus().startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case SETTINGS_CHANGE_UNAVAILABLE:
                // more oops
                LogUtils.d("ok", "Unavailable");
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        RealTimePermission.checkLocationSettings(this, mGoogleApiClient, new LocationRequest());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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