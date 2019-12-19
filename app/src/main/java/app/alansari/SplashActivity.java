package app.alansari;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.ipcheck.ApiManager;
import app.alansari.ipcheck.NewGeoResponseModel;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.newAdditions.LogoutCalling;
import app.alansari.preferences.SharedPreferenceManger;
import app.alansari.preferences.SharedPreferenceManger.VALUE_TYPE;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.DYNAMIC_URLS;
import static app.alansari.Utils.Constants.BASE_URL_PRODUCTION;
import static app.alansari.Utils.Constants.BASE_URL_PRODUCTION_DYNAMIC;
import static app.alansari.Utils.Constants.BASE_URL_TESTING;
import static app.alansari.Utils.Constants.BASE_URL_TESTING_DYNAMIC;

/**
 * Created by Parveen Dala on 05 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener, OnWebServiceResult {

    public static String BASE_URLS;
    private Context context;
    private Dialog myDialog;
    private Handler handler;
    private ApiManager apiManager;
    private Runnable lObjRunnable;
    private GifImageView gifImageView;
    private GifDrawable gifDrawable;
    private boolean isAnimationDone, isConditionsChecked, isValidLocation;
    private int valueCheck = -1;


    private void init() {
        //printhashkey();
        apiManager = new ApiManager(AppController.getInstance().getRequestQueue());
        gifImageView = (GifImageView) findViewById(R.id.gif_image);
        gifDrawable = ((GifDrawable) gifImageView.getDrawable());
        gifDrawable.setLoopCount(1);
        gifDrawable.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationCompleted(int loopNumber) {
                isAnimationDone = true;
                onCheckLocationDone();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        context = SplashActivity.this;
        AppController.setIsPinVerified(false);
        String name = CommonUtils.getDeviceID(this);
        init();
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            checkCurrentLocation3();
        } else {
            showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.error_no_internet_found_splash));
        }


//--------------------------------TESTING-----------------------------------------------------------
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            valueCheck = 1;
            dynamicChangeUrl();
        } else {
            showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.error_no_internet_found_splash));
        }
//---------------------------Production-------------------------------------------------------------
       /* if (NetworkStatus.getInstance(context).isOnline2(context)) {
              valueCheck=2;
              dynamicChangeUrlProduction();
        } else {
            showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.error_no_internet_found_splash));
        }*/
//--------------------------------------------------------------------------------------------------

    }

    @Override
    protected void onDestroy() {
        if (handler != null && lObjRunnable != null) {
            handler.removeCallbacks(lObjRunnable);
        }
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        super.onDestroy();
    }

    private void dynamicChangeUrl() {
        try {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().dynamicUrls(LogoutCalling.getDeviceID(context)), Constants.DYNAMIC_URL, CommonUtils.SERVICE_TYPE.DYNAMIC_URLS, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(DYNAMIC_URLS.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, DYNAMIC_URLS.toString());
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void dynamicChangeUrlProduction() {
        try {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().dynamicUrls(LogoutCalling.getDeviceID(context)), Constants.DYNAMIC_URL_PRODUCTION, CommonUtils.SERVICE_TYPE.DYNAMIC_URLS_PRODUCTION, Request.Method.PUT, this);
            AppController.getInstance().getRequestQueue().cancelAll(CommonUtils.SERVICE_TYPE.DYNAMIC_URLS_PRODUCTION.toString());
            AppController.getInstance().addToRequestQueue(jsonObjReq, CommonUtils.SERVICE_TYPE.DYNAMIC_URLS_PRODUCTION.toString());
        } catch (Exception ex) {
            Toast.makeText(context, getString(app.alansari.R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCurrentLocation() {
        /*gifDrawable.reset();
        isAnimationDone = true;
        isValidLocation = true;
        isConditionsChecked = true;
        onCheckLocationDone();*/
        gifDrawable.reset();

        if (BASE_URL_TESTING.equalsIgnoreCase(BASE_URL_TESTING)) {
            try {
                apiManager.getGeoIpInfo(new Response.Listener<NewGeoResponseModel>() {
                    @Override
                    public void onResponse(NewGeoResponseModel response) {
                        LogUtils.d("ok", "Status " + response.getStatus());
                        LogUtils.d("fijchdcshbh", "Status " + response.getStatus());

                        if (BASE_URL_TESTING.equalsIgnoreCase(BASE_URL_TESTING)) {
                            isValidLocation = true;

                        } else if (response != null && response.getStatus() != null && response.getStatus().equals("Access Denied")) {
                            isValidLocation = false;
                        } else {
                            isValidLocation = true;
                        }
                        isConditionsChecked = true;
                        onCheckLocationDone();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                    resetState();
                        if (!isFinishing()) {
                            gifDrawable.stop();
                            onSuccess();
                        }
//                        showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.not_allowed_to_access_new));
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                resetState();
                // showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.not_allowed_to_access_new));
                if (!isFinishing()) {
                    gifDrawable.stop();
                    onSuccess();
                }
            }
        } else {
            isAnimationDone = true;
            isValidLocation = true;
            isConditionsChecked = true;
            onCheckLocationDone();
        }

    }



    private void resetState() {
        //gifDrawable.stop();
        isAnimationDone = false;
        isValidLocation = false;
        isConditionsChecked = false;
    }

    private void onCheckLocationDone() {
        if (isAnimationDone && isConditionsChecked) {
            //  gifDrawable.stop();
            if (isValidLocation) {
                onSuccess();
            } else {
                showDialogFull(Constants.LOCATION_RESTRICTED, getString(R.string.not_allowed_to_access_new));
            }
        }
    }

    private void onSuccess() {
        offlineDataReset();
        lObjRunnable = new Runnable() {
            @Override
            public void run() {
                /*Intent intent = null;
                if (CommonUtils.isLoggedIn() && CommonUtils.getUserId() != null && CommonUtils.getUserMobile() != null && CommonUtils.getPIN() != null) {
                    intent = new Intent(context, LoginActivity.class);
                } else {
                    intent = new Intent(context, RegisterActivity.class);
                }*/
                startActivity(new Intent(context, LandingActivity.class));
                finish();
            }
        };
        handler = new Handler();
        if (BASE_URL_TESTING.equalsIgnoreCase(BASE_URL_TESTING)) {
            handler.postDelayed(lObjRunnable, 7000);
        } else {
            handler.postDelayed(lObjRunnable, 7000);
        }

    }

    private void showDialogFull(String responseType, String text) {
        if (context == null && isFinishing())
            return;
        if (myDialog != null) {
            while (myDialog.isShowing()) {
                myDialog.dismiss();
            }
        }
        myDialog = new Dialog(context, R.style.InternetErrorDialog);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setContentView(R.layout.internet_error_dialog);

        ((TextView) myDialog.findViewById(R.id.sub_title)).setText(text);
        myDialog.findViewById(R.id.close_btn).setOnClickListener(this);
        myDialog.findViewById(R.id.assistance_call_icon).setOnClickListener(this);
        myDialog.findViewById(R.id.assistance_call).setOnClickListener(this);
        myDialog.findViewById(R.id.retry_btn).setOnClickListener(this);
        myDialog.findViewById(R.id.tv_call).setOnClickListener(this);
        myDialog.show();
        myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });
    }

    private void offlineDataReset() {
        SharedPreferenceManger.setPrefVal(Constants.FETCH_COUNTRY_DATA_OFF, false, VALUE_TYPE.BOOLEAN);
        SharedPreferenceManger.setPrefVal(Constants.FETCH_SERVICE_TYPE_DATA_OFF, false, VALUE_TYPE.BOOLEAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assistance_call_icon:
            case R.id.assistance_call:
            case R.id.tv_call:
                if (myDialog != null)
                    CommonUtils.goToDialPad(context, ((TextView) myDialog.findViewById(R.id.tv_call)).getText().toString().trim().replace("Call: +971 ", ""));
                break;
            case R.id.close_btn:
                onBackPressed();
                break;
            case R.id.retry_btn:
                if (NetworkStatus.getInstance(context).isOnline2(context)) {
                    checkCurrentLocation3();
                    if (myDialog != null)
                        myDialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case DYNAMIC_URLS:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            // if (Constants.BASE_URL_TESTING.equalsIgnoreCase(Constants.BASE_URL_TESTING)) {
                            BASE_URLS = BASE_URL_TESTING_DYNAMIC.concat(response.getString(Constants.MESSAGE).concat("/"));
                            Constants.TESTING_ENVIROMENT = 1;
                           /* } else {
                                BASE_URLS= BASE_URL_PRODUCTION_DYNAMIC.concat(response.getString(Constants.MESSAGE).concat("/"));
                            }*/
                        } else {
                            Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        BASE_URLS = BASE_URL_TESTING;

                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    //BASE_URLS=BASE_URL_TESTING;
                }

                break;
            case DYNAMIC_URLS_PRODUCTION:
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            BASE_URLS = BASE_URL_PRODUCTION_DYNAMIC.concat(response.getString(Constants.MESSAGE).concat("/"));
                            Constants.TESTING_ENVIROMENT = 0;
                        } else {
                            //Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                            BASE_URLS = BASE_URL_PRODUCTION;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //BASE_URLS="https://mobileapp.eexchange.ae:9108/ProjectGateway/api/";
                        BASE_URLS = BASE_URL_PRODUCTION;

                    }
                } else {
                    //Toast.makeText(context, getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    BASE_URLS = BASE_URL_PRODUCTION;
                }

        }

    }

    /*public void printhashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "app.alansari",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("SplashActivity:KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SplashActivity:Exc:", "PackageManager.NameNotFoundException");
        } catch (NoSuchAlgorithmException e) {
            Log.e("SplashActivity:Exc:", "NoSuchAlgorithmException");
        }

    }*/
    private void checkCurrentLocation2() {

        if (BASE_URL_TESTING.equalsIgnoreCase(BASE_URL_TESTING)) {
            try {
                apiManager.getGeoIpInfo(new Response.Listener<NewGeoResponseModel>() {
                    @Override
                    public void onResponse(NewGeoResponseModel response) {
                        LogUtils.d("ok", "Status " + response.getStatus());
                        LogUtils.d("fijchdcshbh", "Status " + response.getStatus());


                        if (BASE_URL_TESTING.equalsIgnoreCase(BASE_URL_TESTING)) {
                            isValidLocation = true;

                        } else if (response != null && response.getStatus() != null && response.getStatus().equals("Access Denied")) {
                            isValidLocation = false;
                        } else {
                            isValidLocation = true;
                        }
                        isConditionsChecked = true;
                        onCheckLocationDone();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!isFinishing()) {
                            onSuccess();
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                resetState();
                //showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.not_allowed_to_access_new));
                if (!isFinishing()) {
                    gifDrawable.stop();
                    onSuccess();
                }
            }
        } else {
            isAnimationDone = true;
            isValidLocation = true;
            isConditionsChecked = true;
            onCheckLocationDone();
        }

    }


    private void checkCurrentLocation3() {
        apiManager.getGeoIpInfo(new Response.Listener<NewGeoResponseModel>() {
            @Override
            public void onResponse(NewGeoResponseModel response) {
                LogUtils.d("ok", "Status " + response.getStatus());
                LogUtils.d("fijchdcshbh", "Status " + response.getStatus()+" "+valueCheck);

                if (valueCheck==1) {
                    isValidLocation = true;

                } else if (response != null && response.getStatus() != null && response.getStatus().equals("Access Denied")) {
                    isValidLocation = false;
                    showDialogFull(Constants.LOCATION_NO_INTERNET, getString(R.string.not_allowed_to_access_new));
                } else {
                    isValidLocation = true;
                }
                isConditionsChecked = true;

                onCheckLocationDone();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (!isFinishing()) {
                    onSuccess();
                }
            }
        });




       /* RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "https://mobileapp.eexchange.ae:9108/ProjectGateway/api/user/mobilenumberregistration";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        queue.add(getRequest);*/
//--------------------------------------------------------------------------------------------------


    }

}