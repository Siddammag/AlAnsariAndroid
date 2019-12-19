package app.alansari.Utils;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;



public class LogOutTimerUtil {

    static final int LOGOUT_TIME = Constants.session_Time;
    public static Context context;
    // static final int LOGOUT_TIME = 30000;
    static Timer longTimer;

    public static synchronized void startLogoutTimer(final Context context, final LogOutListener logOutListener) {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }

        if (longTimer == null) {
            longTimer = new Timer();
            longTimer.schedule(new TimerTask() {
                public void run() {
                    cancel();
                    longTimer = null;
                    try {
                        boolean foreGround = new ForegroundCheckTask().execute(context).get();
                        if (foreGround) {
                            CommonUtils.setLogoutStatus(true);
                            logOutListener.doLogout();
                            //logoutApi(context);

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }, LOGOUT_TIME);
        }
    }

    public static synchronized void stopLogoutTimer() {
      //  new LogoutCalling().SessionTimeOutAPI(context);
    }


    public interface LogOutListener {
        void doLogout();
    }

    static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return true;
        }

    }
  /*  public static void logoutApi(Context context){
        String userId = (String) SharedPreferenceManger.getPrefVal(Constants.USER_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        String sessionTime = (String) SharedPreferenceManger.getPrefVal(Constants.SESSION_ID, null, SharedPreferenceManger.VALUE_TYPE.STRING);
        if (Validation.isValidString(userId)) {
            if (NetworkStatus.getInstance(context).isOnline2(context)) {
                JsonObjectRequest jsonObjReq = new CallAddr().executeApi(new APIRequestParams().userPkIdSessionOut(userId, sessionTime), Constants.LOGOUT, LOGOUT, Request.Method.PUT, context);
                AppController.getInstance().getRequestQueue().cancelAll(LOGOUT);
                AppController.getInstance().addToRequestQueue(jsonObjReq, LOGOUT.toString());
                CommonUtils.showLoading(context, context.getString(app.alansari.R.string.please_wait), LOGOUT.toString(), false);
            } else {
                Toast.makeText(context, context.getString(app.alansari.R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onResponse(int status, JSONObject response, CommonUtils.SERVICE_TYPE sType) {
        switch (sType) {
            case LOGOUT:
                CommonUtils.hideLoading();
                if (status == 1) {
                    try {
                        if (response.getString(Constants.STATUS_MSG).equals(Constants.SUCCESS)) {
                            CommonUtils.registerAgain(context);
                            Toast.makeText(context, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
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
    }*/
}


