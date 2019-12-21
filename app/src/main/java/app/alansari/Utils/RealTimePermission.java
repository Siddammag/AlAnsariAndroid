package app.alansari.Utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

/**
 * Created by Parveen Dala on 08 November, 2016
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class RealTimePermission {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;


    public static boolean checkPermissionLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public static boolean checkPermissionStorage(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }


    public static void requestLocationPermission(final Context context, final int PERMISSION_REQUEST_CODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.setContentView(app.alansari.R.layout.access_permission_dialog);
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Access your Location");
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getString(app.alansari.R.string.access_location_msg));
            myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                    myDialog.dismiss();
                }
            });

            myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.show();
        } else {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestStoragePermission(final Context context, final int PERMISSION_REQUEST_CODE) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AppCompatActivity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
            myDialog.setCanceledOnTouchOutside(true);
            myDialog.setContentView(app.alansari.R.layout.access_permission_dialog);
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Access Storage");
            ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getString(app.alansari.R.string.access_storage_msg));
            myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    myDialog.dismiss();
                }
            });

            myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.show();
        } else {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    public static void checkLocationSettings(ResultCallback<LocationSettingsResult> context, GoogleApiClient mGoogleApiClient, LocationRequest locationRequest) {
        // Check the location settings of the user and create the callback to react to the different possibilities
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
        result.setResultCallback(context);
    }

}
