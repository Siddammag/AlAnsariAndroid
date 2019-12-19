package app.alansari.fcm;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.LogUtils;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;


/**
 * Created by Parveen Dala on 05 June, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "FCM";


    //@Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.d(TAG, "Refreshed token: " + refreshedToken);
        CommonUtils.setFCMToken(refreshedToken);
        CommonUtils.setFCMServerStatus(false);
        new FCMUtils().sendRegistrationToServer(this, refreshedToken);
    }
    // [END refresh_token]


}
