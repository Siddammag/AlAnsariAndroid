package app.alansari.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import app.alansari.AppController;
import app.alansari.CreditCardPaymentActivity;
import app.alansari.DashboardActivity;
import app.alansari.LoginActivity;
import app.alansari.modules.sendmoney.PendingTransActivity;
import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogUtils;
import app.alansari.modules.accountmanagement.BeneficiaryActivity;

/**
 * Created by Parveen Dala on 05 June, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    public static final int NOTIFICATION_ID_PENDING_TRANSACTION = 236;
    public static final int NOTIFICATION_ID_RATE_ALERT = 237;
    public static final int NOTIFICATION_ID_CREDIT_CARD = 238;
    private static final String TAG = "MyFirebaseMsgService";
    private static final String TYPE_PENDING_TRANSACTION = "PENDING_TRANSACTION";
    private static final String TYPE_RATE_ALERT = "RATE_ALERT";
    private static final String TYPE_CREDIT_CARD = "CREDIT_CARD";
    private static final String TYPE_LOGOUT = "LOGOUT";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogUtils.d(TAG, "From: " + remoteMessage.getFrom());
        LogUtils.d(TAG, "Message data payload: " + remoteMessage.getData());

        //Message data payload: {amount=14.14, CCY=26, user_pk_id=5}
//        if (1 == 1)
//            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //LogUtils.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (CommonUtils.getUserId() != null && remoteMessage.getData().get(Constants.USER_PK_ID).equalsIgnoreCase(CommonUtils.getUserId())) {
                String type = remoteMessage.getData().get(Constants.FCM_TYPE);
                Intent intent;
                int notificationId = 0;


                if (type.equalsIgnoreCase(TYPE_LOGOUT)) {
                    CommonUtils.registerAgain(this);
                    return;
                }

                if (isAppIsInBackground(this) || !AppController.isPinVerified()) {
                    LogUtils.d(TAG, "App In Backgroud ");
                    intent = new Intent(this, LoginActivity.class);
                } else {
                    LogUtils.d(TAG, "App In Foreground ");
                    intent = null;
                }

                if (type.equalsIgnoreCase(TYPE_PENDING_TRANSACTION)) {
                    if (intent == null)
                        intent = new Intent(this, PendingTransActivity.class);
                    intent.putExtra(Constants.TARGET, Constants.SOURCE_TRANSACTION_PENDING_LIST);
                    notificationId = NOTIFICATION_ID_PENDING_TRANSACTION;
                } else if (type.equalsIgnoreCase(TYPE_RATE_ALERT)) {
                    if (intent == null)
                        intent = new Intent(this, BeneficiaryActivity.class);
                    intent.putExtra(Constants.TARGET, Constants.SOURCE_BENEFICIARY_ACTIVITY);
                    intent.putExtra(Constants.INTENT_TYPE, Constants.ACTIVITY_FOR_SELECTION);
                    notificationId = NOTIFICATION_ID_RATE_ALERT;
                } else if (type.equalsIgnoreCase(TYPE_CREDIT_CARD)) {
                    if (intent == null)
                        intent = new Intent(this, CreditCardPaymentActivity.class);
                    intent.putExtra(Constants.TARGET, Constants.SOURCE_CREDIT_CARD_PAYMENT_ACTIVITY);
                    intent.putExtra(Constants.ID, remoteMessage.getData().get(Constants.ID));
                    notificationId = NOTIFICATION_ID_CREDIT_CARD;
                } else {
                    if (intent == null)
                        return;
                }
                intent.putExtra(Constants.SOURCE, Constants.SOURCE_FCM_ACTIVITY);
                //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                showBigNotification(notificationId, remoteMessage.getData().get(Constants.TITLE.toUpperCase()), remoteMessage.getData().get(Constants.MESSAGE), intent);
            }
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(app.alansari.R.drawable.ic_logo)
                .setContentTitle(getString(app.alansari.R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(int notificationId, String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        notificationId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.BigTextStyle bigPictureStyle = new NotificationCompat.BigTextStyle();
        bigPictureStyle.setBigContentTitle(title);
        //bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Notification notification;
        notification = mBuilder.setSmallIcon(app.alansari.R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(app.alansari.R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), app.alansari.R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }


    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigImageNotification(String message, String url, Intent intent) {
        String title = getString(app.alansari.R.string.app_name);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        if (url != null)
            bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Notification notification;
        notification = mBuilder.setSmallIcon(app.alansari.R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(app.alansari.R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), app.alansari.R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Notification notification;
        notification = mBuilder.setSmallIcon(app.alansari.R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(app.alansari.R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), app.alansari.R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

  /*  @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.d("DkgToken", "Refreshed token: " + refreshedToken);
        CommonUtils.setFCMToken(refreshedToken);
        CommonUtils.setFCMServerStatus(false);
        new FCMUtils().sendRegistrationToServer(this, refreshedToken);
    }*/


}
