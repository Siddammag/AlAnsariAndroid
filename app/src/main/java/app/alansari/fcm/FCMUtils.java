package app.alansari.fcm;



import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.IOException;

import app.alansari.AppController;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.listeners.OnWebServiceResult;
import app.alansari.network.APIRequestParams;
import app.alansari.network.CallAddr;
import app.alansari.network.NetworkStatus;

import static app.alansari.Utils.CommonUtils.SERVICE_TYPE.SAVE_FCM_TOKEN;


/**
 * Created by Parveen Dala on 05 June, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class FCMUtils implements OnWebServiceResult {
    private String TAG = "FCM";
    private Context context;

    public static void checkFCM(Context context) {
        LogUtils.d("FCM", "getFCMToken : " + CommonUtils.getFCMToken());
        LogUtils.d("FCM", "getFCMServerStatus : " + CommonUtils.getFCMServerStatus());

        if (CommonUtils.getUserId() != null)
            return;
        if (CommonUtils.getFCMToken() == null || !CommonUtils.getFCMServerStatus()) {
            LogUtils.d("FCM", "FCM Not OK");
            AsyncTaskRunner runner = new AsyncTaskRunner(context);
            runner.execute("");
        } else
            LogUtils.d("FCM", "FCM All OK " + CommonUtils.getFCMToken());
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param fcmToken The new token.
     */
    public void sendRegistrationToServer(Context context, String fcmToken) {
        this.context = context;
        // TODO: Implement this method to send token to your app server.
        if (NetworkStatus.getInstance(context).isOnline2(context) && CommonUtils.getUserId() != null && !TextUtils.isEmpty(CommonUtils.getUserId()) && !TextUtils.isEmpty(fcmToken)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().saveFCMToServer(CommonUtils.getUserId(), CommonUtils.getDeviceID(context), fcmToken),
                    Constants.SAVE_FCM_TOKEN_URL, SAVE_FCM_TOKEN, Request.Method.POST, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, SAVE_FCM_TOKEN.toString());
        } else
            LogUtils.d(TAG, "FCM To Server:- Internet Error");
    }

    public void sendRegistrationToServerNew(Context context, String fcmToken) {
        this.context = context;
        // TODO: Implement this method to send token to your app server.
        if (NetworkStatus.getInstance(context).isOnline2(context)) {
            JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().saveFCMToServer(CommonUtils.getUserId(),
                    CommonUtils.getDeviceID(context), fcmToken), Constants.SAVE_FCM_TOKEN_URL, SAVE_FCM_TOKEN, Request.Method.POST, this);
            AppController.getInstance().addToRequestQueue(jsonObjReq, SAVE_FCM_TOKEN.toString());
        } else
            LogUtils.d(TAG, "FCM To Server:- Internet Error");
    }

    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        CommonUtils.hideLoading();
        switch (sType) {
            case SAVE_FCM_TOKEN:
                try {
                    if (status == 1 && response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                        CommonUtils.setFCMServerStatus(true);  //change to true
                    } else {
                        LogUtils.d(TAG, "FCM To Server:- Server Error");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LogUtils.d(TAG, "FCM To Server:- Server Error Exception");
                }
                break;
        }
    }


    private static class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private Context mContext;

        public AsyncTaskRunner(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            LogUtils.d("FCM", "Refreshed token: " + refreshedToken);
            CommonUtils.setFCMToken(refreshedToken);
            CommonUtils.setFCMServerStatus(false);
            new FCMUtils().sendRegistrationToServer(mContext, refreshedToken);
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

    public static void setFcmToken(Context mContext){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.d("FCM", "Refreshed token: " + refreshedToken);
        CommonUtils.setFCMToken(refreshedToken);
        CommonUtils.setFCMServerStatus(false);
        new FCMUtils().sendRegistrationToServer(mContext, refreshedToken);
    }
}