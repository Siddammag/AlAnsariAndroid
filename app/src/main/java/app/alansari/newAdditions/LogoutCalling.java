package app.alansari.newAdditions;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import app.alansari.AppController;
import app.alansari.LandingActivity;
import app.alansari.R;
import app.alansari.RegisterActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.Validation;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;
import app.alansari.preferences.SharedPreferenceManger;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.LOGOUT;
import static app.alansari.Utils.CommonUtils.getPIN;
import static app.alansari.Utils.CommonUtils.getUserId;
import static app.alansari.Utils.CommonUtils.getUserMobile;

public class LogoutCalling implements OnWebServiceResult {
    Context context;

    public void SessionTimeOutAPI(Context context) {
        this.context = context;
        String userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (Validation.isValidString(userId)) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userId, sessionTime, getDeviceID(context)), Constants.LOGOUT, LOGOUT, Request.Method.PUT, this);
                AppController.getInstance().getRequestQueue().cancelAll(LOGOUT);
                AppController.getInstance().addToRequestQueue(jsonObjReq, LOGOUT.toString());
                CommonUtils.showLoading(context, context.getString(app.alansari.R.string.please_wait), LOGOUT.toString(), false);
            } else {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getDeviceID(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;

    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case LOGOUT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            //CommonUtils.registerAgain(context);
                              DoLogout(context);
                            //Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void DoLogout(final Context context) {
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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                   //Toast.makeText(context, Constants.mSession_Logout_Message, Toast.LENGTH_SHORT).show();
                }
            });

         //((AppCompatActivity) context).finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
