package app.alansari.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.alansari.AppController;
import app.alansari.DashboardActivity;
import app.alansari.LandingActivity;
import app.alansari.R;
import app.alansari.RegisterActivity;
import app.alansari.customviews.MultiStateView;
import app.alansari.models.CountryData;
import app.alansari.models.DeviceDetailsData;
import app.alansari.models.QuickSendModel;
import app.alansari.models.TxnDetailsCeCashPayout;
import app.alansari.models.TxnDetailsCreditCardData;
import app.alansari.models.TxnDetailsData;
import app.alansari.models.UserData;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class CommonUtils {

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static ProgressDialog pDialog;
    private static boolean previousNetworkStatus = true;

    public static int getPrimaryColor(Context context) {
        TypedArray a = context.obtainStyledAttributes(new TypedValue().data, new int[]{app.alansari.R.attr.colorPrimary});
        int primaryColor = a.getColor(0, -1);
        a.recycle();
        return primaryColor;
    }

    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(float px, Resources resources) {
        float dp = px / ((float) resources.getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) dp;
    }

    public static void hideKeyboard(Context mContext) {
        try {
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context mContext, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO Remove
    public static void dummyLogin(Context context) {
        UserData userData = new UserData();
        userData.setMobileNum("971904141141");
        userData.setUserId("1");
        userData.setArexMemId("32348");
        userData.setCeMemId("319240");
        userData.setExcUserName("Parveen");
        userData.setStatus("V");
        userData.setUserType("I");
        userData.setTotalPendingTransactions("2");
        userData.setQuickSendData(new ArrayList<QuickSendModel>());
        // CommonUtils.setUserData(userData);
    }

    public void setUserData(UserData userData) {
        try {
            if (validateValue(userData.getUserId())) {
                SharedPreferenceManger.setPrefVal(Constants.USER_ID, userData.getUserId(), SharedPreferenceManger.VALUE_TYPE.STRING);
//                if (validateValue(userData.getArexMemId()))
                SharedPreferenceManger.setPrefVal(Constants.AREX_MEM_ID, userData.getArexMemId(), SharedPreferenceManger.VALUE_TYPE.STRING);
//                if (validateValue(userData.getCeMemId()))
                SharedPreferenceManger.setPrefVal(Constants.CE_MEM_ID, userData.getCeMemId(), SharedPreferenceManger.VALUE_TYPE.STRING);
                //if (validateValue(userData.getExcUserName()))
                SharedPreferenceManger.setPrefVal(Constants.EXC_USER_NAME, userData.getExcUserName(), SharedPreferenceManger.VALUE_TYPE.STRING);
                SharedPreferenceManger.setPrefVal(Constants.INVOICE_EMAIL, userData.getInvoiceEmail(), SharedPreferenceManger.VALUE_TYPE.STRING);
                //if (validateValue(userData.getStatus()))
                SharedPreferenceManger.setPrefVal(Constants.STATUS, userData.getStatus(), SharedPreferenceManger.VALUE_TYPE.STRING);
                //if (validateValue(userData.getUserType()))
                SharedPreferenceManger.setPrefVal(Constants.USER_TYPE, userData.getUserType(), SharedPreferenceManger.VALUE_TYPE.STRING);
                // if (validateValue(userData.getMobileNum()))
                SharedPreferenceManger.setPrefVal(Constants.MOBILE_NUM, userData.getMobileNum(), SharedPreferenceManger.VALUE_TYPE.STRING);
                //   if (validateValue(userData.getReferenceNum()))
                SharedPreferenceManger.setPrefVal(Constants.GO_TO_BRANCH_REF_NUM, userData.getReferenceNum(), SharedPreferenceManger.VALUE_TYPE.STRING);
            }
            SharedPreferenceManger.setPrefVal(Constants.TOTAL_PENDING_TRANSACTIONS, userData.getTotalPendingTransactions(), SharedPreferenceManger.VALUE_TYPE.STRING);
            SharedPreferenceManger.saveQuickSendData(new Gson(), (ArrayList<QuickSendModel>) userData.getQuickSendData());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean validateValue(String value) {
        return (value != null && value.length() > 0);
    }

    public static String getMemPkId(String serviceType) {
        if (serviceType.equalsIgnoreCase(Constants.AREX_MAPPING))
            return (String) SharedPreferenceManger.getPrefVal(Constants.AREX_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        else
            return (String) SharedPreferenceManger.getPrefVal(Constants.CE_MEM_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static String getUserStatus() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.USER_STATUS, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void setUserStatus(String userStatus) {
        SharedPreferenceManger.setPrefVal(Constants.USER_STATUS, userStatus, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static String getStatus() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.STATUS, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static String getUserEmail() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.EXC_USER_NAME, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static String getInvoiceEmail() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.INVOICE_EMAIL, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void setInvoiceEmail(String invoiceEmail) {
        SharedPreferenceManger.setPrefVal(Constants.INVOICE_EMAIL, invoiceEmail, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static boolean getEmailNotificationSettings() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.SETTINGS_EMAIL_NOTIFICATIONS, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setEmailNotificationSettings(boolean emailNotificationSettings) {
        SharedPreferenceManger.setPrefVal(Constants.SETTINGS_EMAIL_NOTIFICATIONS, emailNotificationSettings, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static boolean getSmsNotificationSettings() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.SETTINGS_SMS_NOTIFICATIONS, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setSmsNotificationSettings(boolean smsNotificationSettings) {
        SharedPreferenceManger.setPrefVal(Constants.SETTINGS_SMS_NOTIFICATIONS, smsNotificationSettings, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static boolean getAaeAlertsSettings() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.SETTINGS_AAE_ALERTS, true, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }


    // Settings

    public static void setAaeAlertsSettings(boolean aaeAlerts) {
        SharedPreferenceManger.setPrefVal(Constants.SETTINGS_AAE_ALERTS, aaeAlerts, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static String getFCMToken() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.FCM_TOKEN, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    /**
     * Firebase
     *
     * @param fcmToken
     */
    public static void setFCMToken(String fcmToken) {
        SharedPreferenceManger.setPrefVal(Constants.FCM_TOKEN, fcmToken, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static boolean getFCMServerStatus() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.FCM_TOKEN_SERVER_STATUS_NEW, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setFCMServerStatus(boolean fcmTokenServerStatus) {
        SharedPreferenceManger.setPrefVal(Constants.FCM_TOKEN_SERVER_STATUS_NEW, fcmTokenServerStatus, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static boolean getLogoutStatus() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.INACTIVITY_LOGOUT, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static void setLogoutStatus(boolean checkStatus) {
        SharedPreferenceManger.setPrefVal(Constants.INACTIVITY_LOGOUT, checkStatus, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);

    }

    //--------------------------------------------------------------------------------------------------
    public static String getTotalPendingTransactions() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.TOTAL_PENDING_TRANSACTIONS, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void setTotalPendingTransactions(String totalPendingTransactions) {
        LogUtils.d("ok", "SetTotalTxn " + totalPendingTransactions);
        if (totalPendingTransactions != null && !totalPendingTransactions.equalsIgnoreCase("null"))
            SharedPreferenceManger.setPrefVal(Constants.TOTAL_PENDING_TRANSACTIONS, totalPendingTransactions, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static DeviceDetailsData getDeviceDetails(Context context) {
        DeviceDetailsData deviceDetailsData = new DeviceDetailsData();
        deviceDetailsData.setDeviceId(getDeviceID(context));
        deviceDetailsData.setDeviceMac(DeviceInfo.getDeviceInfo(context, DeviceInfo.Device.DEVICE_MAC_ADDRESS, false));
        deviceDetailsData.setDevicePushId("");
        deviceDetailsData.setDeviceType(DeviceInfo.getDeviceInfo(context, DeviceInfo.Device.DEVICE_TYPE, false));
        deviceDetailsData.setDeviceName(DeviceInfo.getDeviceInfo(context, DeviceInfo.Device.DEVICE_MANUFACTURE, false));
        deviceDetailsData.setDeviceOs(DeviceInfo.getDeviceInfo(context, DeviceInfo.Device.DEVICE_VERSION, false));
        return deviceDetailsData;
    }

    //Saving Inactivity------------------------------------------------------------------------------

    /*****
     * PARVEEN
     ******/
    public static String getDeviceID(Context context) {
        String imei;
       /* TelephonyManager tm;
        try {
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            imei = tm.getDeviceId();
        } catch (Exception e) {
            imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }*/
        imei = LogoutCalling.getDeviceID(context);
        return imei;
    }

    public static ArrayList<TxnDetailsData> getTxnDetailsData(ArrayList<TxnDetailsCeCashPayout> txnDetailsCeCashPayout) {
        if (txnDetailsCeCashPayout != null && txnDetailsCeCashPayout.size() > 0) {
            ArrayList<TxnDetailsData> txnDetailsData = new ArrayList<>();
            for (int i = 0; i < txnDetailsCeCashPayout.size(); i++) {
                try {
                    txnDetailsData.add(txnDetailsCeCashPayout.get(i).getTxnDetailsData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
            return txnDetailsData;
        } else {
            return null;
        }
    }


   /* public static ArrayList<TxnDetailsData> getTxnDetailsDataTravelCard(ArrayList<TxnDetailsHistroyTravelCard> txnDetailsTravelCardReload) {
        if (txnDetailsTravelCardReload != null && txnDetailsTravelCardReload.size() > 0) {
            ArrayList<TxnDetailsData> txnDetailsData = new ArrayList<>();
            for (int i = 0; i < txnDetailsTravelCardReload.size(); i++) {
                try {
                    txnDetailsData.add(txnDetailsTravelCardReload.get(i).getTxnDetailsData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
            return txnDetailsData;
        } else {
            return null;
        }
    }*/


    public static ArrayList<TxnDetailsData> getTxnDetailsDataFromCreditCard(ArrayList<TxnDetailsCreditCardData> txnDetailsCreditCardData) {
        if (txnDetailsCreditCardData != null && txnDetailsCreditCardData.size() > 0) {
            ArrayList<TxnDetailsData> txnDetailsData = new ArrayList<>();
            for (int i = 0; i < txnDetailsCreditCardData.size(); i++) {
                try {
                    txnDetailsData.add(txnDetailsCreditCardData.get(i).getTxnDetailsData());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
            return txnDetailsData;
        } else {
            return null;
        }
    }

    /**
     * Get Default Currency Data
     *
     * @param countryData
     * @return
     */

    public static CountryData.CurrencyData getDefaultCurrencyData(CountryData countryData) {
        if (countryData != null && countryData.getCurrencyData() != null) {
            for (int i = 0; i < countryData.getCurrencyData().size(); i++) {
                if (countryData.getCurrencyData().get(i).getDefaultStatus().equalsIgnoreCase("1")) {
                    return countryData.getCurrencyData().get(i);
                }
            }
            if (countryData.getCurrencyData().size() > 0)
                return countryData.getCurrencyData().get(0);
        }
        return null;
    }

    public static void setCountryFlagImage(Context context, AppCompatImageView imageView, String url) {
        if (url != null && url.trim().length() > 0) {
            if (Constants.BASE_URL.equalsIgnoreCase(Constants.BASE_URL_TESTING)) {
                Picasso.get()
                        .load(Constants.COUNTRY_FLAG_IMAGE_BASE_URL_TEST + url)
                        .placeholder(app.alansari.R.drawable.flag_placeholder)
                        .error(app.alansari.R.drawable.flag_placeholder)
                        .into(imageView);

                // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                // Log.e("gcjhzgbhzb",""+Constants.COUNTRY_FLAG_IMAGE_BASE_URL_TEST + url);
            } else {
                Picasso.get()
                        .load(Constants.COUNTRY_FLAG_IMAGE_BASE_URL + url)
                        .placeholder(app.alansari.R.drawable.flag_placeholder)
                        .error(app.alansari.R.drawable.flag_placeholder)
                        .into(imageView);
            }

        } else {
            Picasso.get()
                    .load(app.alansari.R.drawable.flag_placeholder)
                    .into(imageView);
        }
    }

    public static void setBeneficiaryImage(Context context, CircleImageView imageView, String url) {
        if (url != null && url.trim().length() > 0) {
            Picasso.get()
                    .load(url)
                    .placeholder(app.alansari.R.drawable.svg_beneficiary_edit_pic)
                    .error(app.alansari.R.drawable.svg_beneficiary_edit_pic)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(app.alansari.R.drawable.svg_beneficiary_edit_pic)
                    .placeholder(app.alansari.R.drawable.svg_beneficiary_edit_pic)
                    .error(app.alansari.R.drawable.svg_beneficiary_edit_pic)
                    .into(imageView);
        }
    }

    public static void setPromotionsImage(Context context, ImageView imageView, String url) {
        if (url != null && url.trim().length() > 0) {
            /*Picasso.with(context)
                    .load(url)
                    .e(app.alansari.R.drawable.adds_bg)
                    .into(imageView);*/

            GlideApp
                    .with(context)
                    .load(url)
                    .error(app.alansari.R.drawable.adds_bg)
                    .into(imageView);
        } else {
            /*Picasso.with(context)
                    .load(app.alansari.R.drawable.adds_bg)
                    .placeholder(app.alansari.R.drawable.adds_bg)
                    .e(app.alansari.R.drawable.adds_bg)
                    .into(imageView);*/

            GlideApp
                    .with(context)
                    .load(app.alansari.R.drawable.adds_bg)
                    .placeholder(app.alansari.R.drawable.adds_bg)
                    .error(app.alansari.R.drawable.adds_bg)
                    .into(imageView);
        }


    }

    public static void setCountryMonumentImage(Context context, AppCompatImageView imageView, String url) {
        if (url != null && url.trim().length() > 0) {
            Picasso.get()
                    .load(Constants.COUNTRY_FLAG_IMAGE_BASE_URL + url)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(app.alansari.R.color.color9E9E9E)
                    .into(imageView);
        }
    }

    public static void showLimitDialog(Context context, String message) {
        final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);

        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Transaction Alert!");
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(message);

        myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
        myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
        ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText("OK");

        myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public static boolean isNumeric(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            PackageManager manager = context.getPackageManager();
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException nnf) {
            nnf.printStackTrace();
        }
        return info;
    }

    public static void setLayoutFont(Context context, String fontName, View... params) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
            for (View tv : params) {
                if (tv instanceof TextView)
                    ((TextView) tv).setTypeface(tf);
                else if (tv instanceof Button)
                    ((Button) tv).setTypeface(tf);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void applyFontForToolbarTitle(AppCompatActivity context) {
        Toolbar toolbar = (Toolbar) context.findViewById(app.alansari.R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                LogUtils.d("ok", "KK ");
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
                if (tv.getText().equals(context.getSupportActionBar().getTitle())) {
                    LogUtils.d("ok", "Yes " + context.getTitle());
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }

    public static String getNumberSuffix(String number) {
        if (number != null) {
            if (number.substring(number.length() - 1).contains("1"))
                return "st";
            else if (number.substring(number.length() - 1).contains("2"))
                return "nd";
            else if (number.substring(number.length() - 1).contains("3"))
                return "rd";
            else return "th";
        }
        return "";
    }

    public static String getMonthName(int monthId) {
        switch (monthId) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "MM";
        }
    }

    public static void showLoading(Context mContext, String message, final String tag, boolean cancelable) {
        try {
            pDialog = new ProgressDialog(mContext, app.alansari.R.style.AppTheme_Loading_Dialog);
            pDialog.setMessage(message);
            pDialog.setCancelable(cancelable);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AppController.getInstance().getRequestQueue().cancelAll(tag);
                    dialog.dismiss();
                }
            });
            pDialog.show();
        } catch (Exception e) {
            Crashlytics.logException(new Exception("Web Error : " + e.getMessage().toString()));
        }

    }

    public static void hideLoading() {
        try {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static boolean isDialogVisible() {
        if (pDialog != null)
            return pDialog.isShowing();
        return false;
    }

    /**********
     * * PARVEEN Er
     *
     * @param multiStateView
     * @param viewState
     * @param tvView
     * @param ivView
     * @param msg
     */
    public static void setViewState(MultiStateView multiStateView, int viewState, TextView tvView, ImageView ivView, String msg) {
        try {
            LogUtils.d("ok", "Set " + viewState);
            switch (viewState) {
                case MultiStateView.VIEW_STATE_CONTENT:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    break;
                case MultiStateView.VIEW_STATE_LOADING:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    break;
                case MultiStateView.VIEW_STATE_EMPTY:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    if (msg != null)
                        tvView.setText(msg);
                    else
                        tvView.setText("Sorry! No data found");
                    //	ivView.setImageResource(R.drawable.icn_empty);
                    break;
                case MultiStateView.VIEW_STATE_EMPTY_BANK:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    if (msg != null)
                        tvView.setText(msg);
                    else
                        tvView.setText("Kindly call 600 54 6000 for assistance in adding your bank");
                    //	ivView.setImageResource(R.drawable.icn_empty);
                    break;
                case MultiStateView.VIEW_STATE_ERROR:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    if (msg != null)
                        tvView.setText(msg);
                    else
                        tvView.setText("Please check your internet connection!");
                    //	ivView.setImageResource(R.drawable.icn_no_internet);
                    break;
                case MultiStateView.VIEW_STATE_WRONG:
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    if (msg != null)
                        tvView.setText(msg);
                    else
                        tvView.setText("Sorry! Something went wrong");
                    //	ivView.setImageResource(R.drawable.icn_wrong);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setResponseToast(Context context, int viewState) {
        try {
            switch (viewState) {
                case MultiStateView.VIEW_STATE_EMPTY:
                    Toast.makeText(context, context.getString(app.alansari.R.string.empty_result), Toast.LENGTH_SHORT).show();
                    break;
                case MultiStateView.VIEW_STATE_ERROR:
                    Toast.makeText(context, context.getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    break;
                case MultiStateView.VIEW_STATE_WRONG:
                    Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if (ConnectionResult.SUCCESS == googleApiAvailability.isGooglePlayServicesAvailable(context)) {
            return true;
        } else {
            try {
                googleApiAvailability.getErrorDialog((AppCompatActivity) context, 0, CommonUtils.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "Google Play Services Not Available.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    /**
     * Demonstrates converting a {@link Drawable} to a {@link BitmapDescriptor},
     * for use as a marker icon.
     */
    public static BitmapDescriptor vectorToBitmap(Context context, @DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Showing the status in Snackbar
    public static void showSnack(Context context, boolean isConnected, View view) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        if (isConnected) {
            textView.setText("Good! Connected to Internet");
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            textView.setText("No Internet Connection");
            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.login_change_btn_bg));
        }
        textView.setTextColor(Color.WHITE);

        if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sbView.getLayoutParams();
            params.setMargins(params.leftMargin,
                    params.topMargin,
                    params.rightMargin,
                    params.bottomMargin + getNavigationHeight(context));

            sbView.setLayoutParams(params);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();

    }

    public static int getNavigationHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Get Branch Open Status
     *
     * @param timeString
     * @return
     */
    public static boolean getBranchStatus(String timeString) {
        if (timeString != null) {
            String[] timeArray = timeString.split(",");
            for (int i = 0; i < timeArray.length; i++) {
                if (timeArray[i].split(":")[0] != null && timeArray[i].split(":")[0].equalsIgnoreCase(String.valueOf(CommonUtils.getCurrentDayOfWeek()))) {
                    int length = timeArray[i].split(":").length;
                    if (length == 5) {
                        if (CommonUtils.checkTimeSpans(timeArray[i].split(":")[1] + ":" + timeArray[i].split(":")[2], timeArray[i].split(":")[3] + ":" + timeArray[i].split(":")[4])) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return WeekDay name
     */
    public static int getCurrentDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param firstTime
     * @param secondTime
     * @return
     */
    public static boolean checkTimeSpans(String firstTime, String secondTime) {
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new SimpleDateFormat("HH:mm").parse(firstTime));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new SimpleDateFormat("HH:mm").parse(secondTime));

            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(new SimpleDateFormat("HH:mm").parse(getCurrentTime()));

            if ((calendar3.getTime().after(calendar1.getTime())) && (calendar3.getTime().before(calendar2.getTime()))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * @return Current Date
     */
    public static String getCurrentTime() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            return df.format(c.getTime());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get Branch Open Timings
     *
     * @param timeString
     * @return
     */
    public static String getBranchTimings(String timeString) {
        if (timeString != null) {
            String[] timeArray = timeString.split(",");
            for (int i = 0; i < timeArray.length; i++) {
                try {
                    if (timeArray[i].split(":")[0] != null && timeArray[i].split(":")[0].equalsIgnoreCase(String.valueOf(CommonUtils.getCurrentDayOfWeek()))) {
                        String time = CommonUtils.convertTimeToAmPm(timeArray[i].split(":")[1] + ":" + timeArray[i].split(":")[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[i].split(":")[3] + ":" + timeArray[i].split(":")[4] + ":00");
                        if (timeArray[i + 1] != null && timeArray[i + 1].split(":")[0] != null && timeArray[i + 1].split(":")[0].equalsIgnoreCase(String.valueOf(CommonUtils.getCurrentDayOfWeek()))) {
                            time = time + " & " + CommonUtils.convertTimeToAmPm(timeArray[i + 1].split(":")[1] + ":" + timeArray[i + 1].split(":")[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[i + 1].split(":")[3] + ":" + timeArray[i + 1].split(":")[4] + ":00");
                        }
                        if (timeArray[i + 2] != null && timeArray[i + 2].split(":")[0] != null && timeArray[i + 2].split(":")[0].equalsIgnoreCase(String.valueOf(CommonUtils.getCurrentDayOfWeek()))) {
                            time = time + " & " + CommonUtils.convertTimeToAmPm(timeArray[i + 2].split(":")[1] + ":" + timeArray[i + 2].split(":")[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[i + 2].split(":")[3] + ":" + timeArray[i + 2].split(":")[4] + ":00");
                        }
                        return time;
                    }
                } catch (Exception e) {
                    return "Unable To get time";
                }
            }
        }
        return "";
    }

    public static String convertTimeToAmPm(String time) {
        try {
            return new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm:ss").parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Get Branch Open Timings
     *
     * @param timeString
     * @return
     */
    public static String getBranchTimingsNew(String timeString) {
        if (timeString != null) {
            String day1Time = "";
            String day2Time = "";
            String day3Time = "";
            String day4Time = "";
            String day5Time = "";
            String day6Time = "";
            String day7Time = "";
            String[] dayArray = timeString.split(",");
            for (int i = 0; i < dayArray.length; i++) {
                String[] timeArray = dayArray[i].split(":");
                if (timeArray != null && timeArray.length == 5) {
                    switch (timeArray[0]) {
                        case "1":
                            if (day1Time.length() == 0) {
                                day1Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day1Time = day1Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "2":
                            if (day2Time.length() == 0) {
                                day2Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day2Time = day2Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "3":
                            if (day3Time.length() == 0) {
                                day3Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day3Time = day3Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "4":
                            if (day4Time.length() == 0) {
                                day4Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day4Time = day4Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "5":
                            if (day5Time.length() == 0) {
                                day5Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day5Time = day5Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "6":
                            if (day6Time.length() == 0) {
                                day6Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day6Time = day6Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        case "7":
                            if (day7Time.length() == 0) {
                                day7Time = CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            } else {
                                day7Time = day7Time + " & " + CommonUtils.convertTimeToAmPm(timeArray[1] + ":" + timeArray[2] + ":00") + " to " + CommonUtils.convertTimeToAmPm(timeArray[3] + ":" + timeArray[4] + ":00");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            switch (CommonUtils.getCurrentDayOfWeek()) {
                case 1:
                    return day1Time;
                case 2:
                    return day2Time;
                case 3:
                    return day3Time;
                case 4:
                    return day4Time;
                case 5:
                    return day5Time;
                case 6:
                    return day6Time;
                case 7:
                    return day7Time;
                default:
                    break;
            }
        }
        return "";
    }

    /**
     * Get Branch Open Timings
     *
     * @param timeString
     * @return
     */
    public static String getTimings(String timeString) {
        ArrayList<String> dayList = new ArrayList<>();
        ArrayList<String> timeList = new ArrayList<>();
        String[] daySplit = timeString.split(",");

        for (int i = 0; i < daySplit.length; i++)
            dayList.add(daySplit[i]);

        for (int i = 0; i < dayList.size(); i++)
            if (dayList.get(i).startsWith(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + ":"))
                timeList.add(dayList.get(i).substring(2, dayList.get(i).length()));

        String finalTime = "";
        for (int i = 0; i < timeList.size(); i++) {
            String[] tempTime = timeList.get(i).split(":");
            if (finalTime.length() == 0) {
                finalTime = convertTimeToAmPm(tempTime[0] + ":" + tempTime[1] + ":00") + " to " + convertTimeToAmPm(tempTime[2] + ":" + tempTime[3] + ":00");
            } else {
                finalTime = finalTime + " & " + convertTimeToAmPm(tempTime[0] + ":" + tempTime[1] + ":00") + " to " + convertTimeToAmPm(tempTime[2] + ":" + tempTime[3] + ":00");
            }
        }
        return (finalTime);
    }

    public static void setInputTextLayoutColor(Context context, TextInputLayout textInputLayout) {
        try {
            Field field = textInputLayout.getClass().getDeclaredField("mFocusedTextColor");
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    ContextCompat.getColor(context, app.alansari.R.color.colorPrimary
                    )
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(textInputLayout, myList);

            Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            fDefaultTextColor.setAccessible(true);
            fDefaultTextColor.set(textInputLayout, myList);

            Method method = textInputLayout.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(textInputLayout, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addCommaAfterThousand(EditText editText, TextWatcher textWatcher, String originalString) {
        editText.removeTextChangedListener(textWatcher);
        try {
            int currentSelection = editText.getSelectionStart();
            int length = editText.getText().length();
            if (originalString.contains(",")) {
                originalString = originalString.replaceAll(",", "");
            }

            String afterDecimal = "";
            if (originalString.contains(".")) {
                afterDecimal = originalString.substring(originalString.indexOf("."));
                originalString = originalString.substring(0, originalString.indexOf("."));
            }

            String formattedString = addCommaToStringForEditText(originalString) + afterDecimal;

            editText.setText(formattedString);
            if (editText.getText().length() > length)
                currentSelection++;
            else if (editText.getText().length() < length && currentSelection != 0)
                currentSelection--;

            editText.setSelection(currentSelection);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        editText.addTextChangedListener(textWatcher);
    }

    public static String addCommaAfterThousand(String originalString) {
        try {

            if (originalString.contains(",")) {
                originalString = originalString.replaceAll(",", "");
            }

            String afterDecimal = "";
            if (originalString.contains(".")) {
                afterDecimal = originalString.substring(originalString.indexOf("."));
                originalString = originalString.substring(0, originalString.indexOf("."));
            }

            return addCommaToStringForEditText(originalString) + afterDecimal;

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return originalString;
    }


    //Comma

    public static String addCommaToStringForEditText(String value) {
        try {
            if (!Validation.isValidString(value))
                return value;

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            return formatter.format(Double.parseDouble(value));
        } catch (Exception ex) {
            ex.printStackTrace();
            return String.valueOf(value);
        }
    }

    public static String addCommaToString(String value) {
        try {
            if (!Validation.isValidString(value))
                return value;

            String afterDecimal = "";
            if (value.contains(".")) {
                afterDecimal = value.substring(value.indexOf("."));
                if (afterDecimal.equalsIgnoreCase(".00"))
                    afterDecimal = "";
                value = value.substring(0, value.indexOf("."));
            }

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            return formatter.format(Double.parseDouble(value)) + afterDecimal;
        } catch (Exception ex) {
            ex.printStackTrace();
            return String.valueOf(value);
        }
    }

    public static String getTextFromTextView(TextView textView) {
        if (Validation.isValidTextViewValue(textView))
            return textView.getText().toString().trim().replaceAll(",", "");
        else
            return null;
    }

    public static String getTextFromEditText(EditText editText) {
        if (Validation.isValidEditTextValue(editText))
            return editText.getText().toString().trim().replaceAll(",", "");
        else
            return null;
    }

    public static String getDecimalFormattedString(double value) {
        if (isDoubleContainsDecimal(value))
            return String.format(Locale.US, "%.2f", value);
        else
            return String.valueOf(value);
    }

    public static boolean isDoubleContainsDecimal(double value) {
        return ((String.valueOf(value)).contains("."));
    }

    /**
     * Time Methods
     */

    public static String getDateFromMilliSec(long milliSeconds) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * @param date
     * @return
     */
    public static String getStringFromDate(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(date);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @param date
     * @return
     */
    public static Date getDateFromString(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(date);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @return Current Date
     */
    public static String getCurrentDateTime() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(c.getTime());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @param context
     * @param mobileNumber
     */
    public static void goToDialPad(Context context, String mobileNumber) {
        try {
            Log.e("bsvhs", "" + mobileNumber);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobileNumber));
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Go To Register Screen
     *
     * @param context
     */
    public static void registerAgain(Context context) {
        try {
            Intent intent;
            AppController.setIsPinVerified(false);
            if (CommonUtils.isLoggedIn() && getUserId() != null && getUserMobile() != null && getPIN() != null) {
                intent = new Intent(context, LandingActivity.class);
            } else {
                intent = new Intent(context, RegisterActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            CommonUtils.setLogoutStatus(false);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //------------------------------------Inactivity Logout---------------------------------------------

    public static boolean isLoggedIn() {
        return (boolean) SharedPreferenceManger.getPrefVal(Constants.IS_LOGGED_IN, false, SharedPreferenceManger.VALUE_TYPE.BOOLEAN);
    }

    public static String getUserId() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }


//--------------------------------------------------------------------------------------------------

    public static String getUserMobile() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.MOBILE_NUM, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void setUserMobile(String mobile) {
        LogUtils.d("ok", "Set mobile " + mobile);
        SharedPreferenceManger.setPrefVal(Constants.MOBILE_NUM, mobile, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static String getPIN() {
        return (String) SharedPreferenceManger.getPrefVal(Constants.PIN, null, SharedPreferenceManger.VALUE_TYPE.STRING);
    }

    public static void registerAgainOpen(final Context context) {
        try {
            new LogoutCalling().SessionTimeOutAPI(context);
           /* Intent intent;
            AppController.setIsPinVerified(false);
            if (CommonUtils.isLoggedIn() && getUserId() != null && getUserMobile() != null && getPIN() != null) {
                intent = new Intent(context, LandingActivity.class);
            } else {
                intent = new Intent(context, RegisterActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            CommonUtils.setLogoutStatus(false);
            context.startActivity(intent);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, Constants.mSession_Logout_Message, Toast.LENGTH_SHORT).show();
                }
            });
            ((AppCompatActivity) context).finish();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Go To DashBoard Screen
     *
     * @param context
     */


    public static void goToDashBoard(Context context) {
        try {
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Go To DashBoard Screen
     *
     * @param context
     */
    public static void goToDashBoard(Context context, String source, String txnId, String serviceType) {
        try {
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.putExtra(Constants.SOURCE, source);
            intent.putExtra(Constants.REM_TXN_PK_ID, txnId);
            intent.putExtra(Constants.SERVICE_TYPE, serviceType);
            intent.putExtra(Constants.SOURCE, source);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Logout
     *
     * @param context
     */
    public static void logout(Context context) {
        try {
            SharedPreferenceManger.clearPreferences();
            AppController.setIsPinVerified(false);
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File getPictureDirectory(String filename) {
        final File picturesDir;

        // File file = new File(context.getFilesDir(), filename);

        if (Build.PRODUCT.equals("NOOKcolor")) {
            // The nook color uses a slightly different path for images and the usual constant for the path does not work
            picturesDir = new File("/mnt/media");
        } else if (Build.PRODUCT.equals("NOOKTablet")) {
            picturesDir = new File("/mnt/media");
        } else {
            picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        //picturesDir.mkdirs();
        File dir = new File(picturesDir, filename);
        if (!dir.exists()) {
            LogUtils.i("ok", "'" + dir + "' does not exist, trying to create ...");
            if (dir.mkdirs() == false) {
                LogUtils.e("ok", "Failed to create directory '" + dir + "'!");
                return null;
            }
        }
        return dir;
    }

    public static String getSuccessUrl() {
      /*  if (Constants.BASE_URL.equalsIgnoreCase(Constants.BASE_URL_TESTING)) {
            return "https://barshaoffice.dyndns.org:9107/ProjectGateway/remittance/InternationalMoneyTransferPay.action";
        } else {
            return "https://mobileapp.eexchange.ae:9108/ProjectGateway/remittance/InternationalMoneyTransferPay.action";
        }*/
        if (Constants.TESTING_ENVIROMENT == 1) {
            return "https://barshaoffice.dyndns.org:9107/ProjectGateway/remittance/InternationalMoneyTransferPay.action";

        } else {
            return "https://mobileapp.eexchange.ae:9108/ProjectGateway/remittance/InternationalMoneyTransferPay.action";
        }

    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        LogUtils.v("Image Size", "Size : " + baos.size());
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;

    }


    public static String encodeTobase64Profile(Bitmap bitmap) {
       /* Bitmap bm=BitmapFactory.decodeFile(absolutePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        LogUtils.v("Image Size", "Size : " + baos.size());
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return  imageEncoded;*/


        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
        bitmap.copyPixelsToBuffer(buffer);
        byte[] data = buffer.array();
        String imageEncoded = Base64.encodeToString(data, Base64.DEFAULT);
        return imageEncoded;


    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap modifyOrientation2(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

       /* Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }*/

        return rotateImage(bitmap, 90);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static String getPath(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static boolean isEmailValid(String email) {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context inContext, Uri uri) {
        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String getCharacters(String value) {
        String name = "";
        try {
            value = value.replace("  ", " ");
            if (value.split(" ").length >= 1) {
                name = String.valueOf(value.split(" ")[0].trim().toUpperCase().charAt(0) + "" + (value.split(" ").length > 1 ? value.split(" ")[1].toUpperCase().charAt(0) : ""));
            } else {
                name = String.valueOf(value.trim().toUpperCase().charAt(0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            name = String.valueOf(value.trim().toUpperCase().charAt(0));
        }
        return name;
    }

    public static String getValidString(String value) {
        if (value != null && !TextUtils.isEmpty(value) && !value.equalsIgnoreCase("null")) {
            return value;
        }
        return "";
    }

    public static void showpopupDialog2(final Context mContext) {

        final Dialog myDialog = new Dialog(mContext, app.alansari.R.style.CustomDialogThemeLightBg);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("You cant delete this creadit card");
        // ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText(context.getResources().getString(app.alansari.R.string.confirm_delete_beneficiary));
        ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setVisibility(View.GONE);
        ((Button) myDialog.findViewById(R.id.dialog_btn_yes)).setText("DONE");

        myDialog.findViewById(app.alansari.R.id.dialog_btn_no).setVisibility(View.GONE);

        myDialog.findViewById(app.alansari.R.id.dialog_btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public static void createAlertDialogBox(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("On post execute")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // Setting OK Button

        AlertDialog dialog = builder.create();
        /*
          new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(context, Constants.mSession_Logout_Message, Toast.LENGTH_SHORT).show();
                    final Dialog myDialog = new Dialog(context, app.alansari.R.style.CustomDialogThemeLightBg);
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.setContentView(app.alansari.R.layout.confirm_delete_item_generic_dialog);

                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_title)).setText("Transaction Alert!");
                    ((TextView) myDialog.findViewById(app.alansari.R.id.dialog_text)).setText("znfjksnjbn");

                    myDialog.findViewById(app.alansari.R.id.btn_layout).setVisibility(View.GONE);
                    myDialog.findViewById(app.alansari.R.id.single_btn).setVisibility(View.VISIBLE);
                    ((TextView) myDialog.findViewById(app.alansari.R.id.single_btn)).setText("OK");

                    myDialog.findViewById(app.alansari.R.id.single_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            AppController.setIsPinVerified(false);
                            if (CommonUtils.isLoggedIn() && getUserId() != null && getUserMobile() != null && getPIN() != null) {
                                intent = new Intent(context, LandingActivity.class);
                            } else {
                                intent = new Intent(context, RegisterActivity.class);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            CommonUtils.setLogoutStatus(false);
                            context.startActivity(intent);
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show();




                }
            });*/
    }

    public static void alertBox(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogTheme);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();


    }

    public void saveBitmap(Context context, Bitmap bitmap) {
        File imagePath = CommonUtils.getAlbumStorageDir("AL_Ansari_" + System.currentTimeMillis() + ".png");//CommonUtils.getPictureDirectory("AL_Ansari_" + System.currentTimeMillis());
        if (imagePath != null) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Toast.makeText(context, "Saved to Gallery.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                LogUtils.e("GREC", e.getMessage(), e);
            } catch (IOException e) {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                LogUtils.e("GREC", e.getMessage(), e);
            }
        } else {
            Toast.makeText(context, context.getString(app.alansari.R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public static File getAlbumStorageDir(String fileName) {
        if (isExternalStorageWritable()) {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AlAnsari");
            if (!file.mkdirs()) {
                LogUtils.e("ImageSaver", "Directory not created");
            }
            return new File(file, fileName);
        }
        return null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public enum SERVICE_TYPE {

        SAVE_FCM_TOKEN, SEND_OTP, VERIFY_OTP, RESEND_OTP, LOGIN, REGISTER_EEXCHANGE_SOAP, REGISTRATION_EEXCHANGE, REGISTER_EX_MOBILE,
        FETCH_ALL_COUNTRIES, FETCH_ALL_BANKS, FETCH_BENF_BANK, FETCH_BENF_BANK_BRANCH, FETCH_USER_ACCOUNTS, FETCH_USER_ACCOUNT_BANK_LIST, FETCH_USER_ACCOUNT_BANK_BRANCH_LIST, FETCH_CREDIT_CARD_BANK_LIST, FETCH_ACCOUNT_TYPE,
        REGISTER_MOBILE, VERIFY_EID, FETCH_DETAILS, ADD_QUICK_SEND, DELETE_QUICK_SEND, FETCH_BENEFICIARY_VALUE, FETCH_BENEFICIARY_INSTANT, FETCH_BENEFICIARY_DETAILS, ADD_BANK_ACCOUNT, FETCH_BANK_ACCOUNTS, FETCH_SELECT_DATA, FETCH_PENDING_TRANSACTION, FETCH_TRANSACTION_HISTORY,
        FETCH_SERVICE_TYPES, ENROLL_WU_CARD, VALIDATE_WU_CARD, FETCH_WU_SEND_MONEY, FETCH_WU_BENEFICIARY, WU_CALCULATE_CURRENCY_SEND_MONEY, CALCULATE_CURRENCY_SEND_MONEY, CALCULATE_CURRENCY_CREDIT_CARD, CHECK_TXN_LIMITS, FETCH_ADDITIONAL_INFO, FETCH_ADDITIONAL_INFO_CE, ADD_ADDITIONAL_INFO, FETCH_PAYMENT_MODES, FETCH_AAE_BANK_DETAILS, REMITTANCE_API, REMITTANCE_CREDIT_CARD_API, REMITTANCE_API_GATEWAY, SET_TXN_COMPLETED_REMITTANCE_API, SET_TXN_REJECTED_REMITTANCE_API, SUBMIT_TRANSACTION_RATING, SUBMIT_REFERENCE_NUM_REMITTANCE_API, SEND_INVOICE_API, FETCH_TRANSACTIONS_REMITTANCE_API, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_API, FETCH_TRANSACTIONS_REMITTANCE_CREDIT_CARD_API, FETCH_TRANSACTIONS_HISTORY_REMITTANCE_CREDIT_CARD_API,
        FETCH_TRANSACTION_MODE_IN_COUNTRY, FETCH_SERVICE_TYPE_IN_COUNTRY,
        FETCH_BENEFICIARY_FIELDS_AREX, FETCH_BENEFICIARY_FIELDS_CE, VALIDATE_BENEFICIARY, ADD_BENEFICIARY, VALIDATE_BENEFICIARY_CE, ADD_BENEFICIARY_CE, UPDATE_MISSING_FIELD_BENEFICIARY, UPDATE_MISSING_FIELD_BENEFICIARY_CE, DELETE_BENEFICIARY, ADD_USER_ACCOUNTS, UPDATE_USER_ACCOUNTS, DELETE_USER_ACCOUNTS, FETCH_CREDIT_CARDS, ADD_CREDIT_CARDS, UPDATE_CREDIT_CARDS, DELETE_CREDIT_CARDS,
        FETCH_CE_CURRENCY_DATA, CALCULATE_CURRENCY_CALCULATOR, FOREIGN_CURRENCY, FETCH_SAVED_RATE_ALERTS, FETCH_CURRENT_RATE, SAVE_RATE_ALERT, DELETE_RATE_ALERT, EMAIL_NOTIFICATION, FETCH_EMAIL_NOTIFICATION, TRAVEL_CARD_RELOAD, VALIDATE_TRAVEL_CARD_RELOAD, ADD_TRAVEL_CARD_RELOAD, SUBMIT_ADD_TRAVEL_CARD, DELETE_TRAVEL_CARD, VALIDATE_SELECT_WIRECARD, FETCH_WIRE_CARD_BALANCE, GET_CREATES_SEND_CHARGES, LOAD_WC_TXN_HISTORY, FETCH_WC_ADDITIONAL_INFO, VALIDATE_WIRE_CARD_RELOAD, GET_PAYMENT_MODES, SUBMIT_WIRE_CARD_RELOAD_EXT, UPDATE_TRAVEL_CARD_PAYMENT, FETCH_CHARGES_SENDMONEY,REMITTANCE_API_GATEWAY_TRAVEL_CARD_RELOAD,
        FETCH_BRANCH_CITIES, FETCH_BRANCH_IN_CITY, CONTACT_US, FAQ, FEEDBACK, REQUEST_CALL_BACK,
        SET_FUND_COMPLETED_REMITTANCE_API, SET_FUND_REJECTED_REMITTANCE_API, ADDS, POPUP, CURRENCY_URL, PROMO_CODE, Load_Drop_Down,
        //CE Dynamic Fields
        FETCH_CE_BENF_FIELD_ID_8, FETCH_CE_BENF_FIELD_ID_9, FETCH_CE_BENF_FIELD_ID_12, FETCH_CE_BENF_FIELD_ID_16, FETCH_CE_BENF_FIELD_ID_18,
        FETCH_CE_BENF_FIELD_ID_28, FETCH_CE_BENF_FIELD_ID_29, FETCH_CE_BENF_FIELD_ID_32, FETCH_CE_BENF_FIELD_ID_33,
        FETCH_CE_BENF_FIELD_ID_35, FETCH_CE_BENF_FIELD_ID_38, FETCH_CE_BENF_FIELD_ID_39, FETCH_CE_BENF_FIELD_ID_44, FETCH_CE_BENF_FIELD_ID_45, FETCH_CE_BENF_FIELD_ID_50,
        FETCH_CE_BENF_FIELD_ID_54, FETCH_CE_BENF_FIELD_ID_59, FETCH_CE_BENF_FIELD_ID_86, FETCH_CE_BENF_FIELD_ID_92,
        FETCH_CE_BENF_FIELD_ID_94, FETCH_CE_BENF_FIELD_ID_97, FETCH_CE_BENF_FIELD_ID_98, FETCH_CE_BENF_FIELD_ID_99, FETCH_ALL_WU_COUNTRIES, WU_CURRENCY_URL, FETCH_WU_ADDITIONAL_INFO,
        WU_VALIDATE_BENEFICIARY, WU_ADD_BENEFICIARY, WU_FETCH_BENEFICIARY_DETAILS, WU_FETCH_SOURCE_OF_FUNDS, WU_FETCH_TRANSACTION_PURPOSE, WU_FETCH_TERMS_AND_CONDITIONS, WU_FETCH_SERVICE_TYPES,
        WU_VALIDATE_SEND_MONEY, FETCH_WU_PAYMENT_MODES, WU_PERFORM_SEND_MONEY, WU_COUNTRY_INFO, WU_FETCH_STATE, ALL_PENDING_TRANSACTION_API, LOAD_MY_ID_DETAILS, UPDATE_MY_ID_DETAILS, STORE_PIN, RESET_PIN, LOGOUT, CUSTOMER_PROFILE, RESEND_OTP_EXCEPTION,
        LOAD_WIRE_CARD_RELOAD, DYNAMIC_URLS, DYNAMIC_URLS_PRODUCTION, TRANSACTION_TRACKER, TRANSACTION_TRACKER_POST, LOAD_SERVICE_TYPE_LIST, FETCH_PAYMENT_MODES_PRELOGIN, FETCH_CHARGES_SENDMONEY_PRE_LOGIN, FETCH_CHARGES_SENDMONEY_POST_LOGIN,REFERRAL_POP_UP_CONTENT,MY_PROFILE_TEMPLATE,PROFESIONAL_AND_DESIGANATION,COUNTRY_TYPE
    }

    private static class LongTask extends AsyncTask<Void, Void, String> {
        Context activity;

        public LongTask(Context mainActivity) {
            super();
            this.activity = mainActivity;
        }

        @Override
        protected String doInBackground(Void... params) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
            }
            return "Hello";
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setTitle(result);
            alertDialog.setMessage("On post execute");
            alertDialog.setCancelable(false);
            // Setting OK Button
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    /*In this stressful and rush life. You have spent your precious time to make me happy from your heartiest wishes on my birthday. Thank you dear to make my day.*/
}

