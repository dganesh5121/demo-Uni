package com.techindiana.school.parent.Fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techindiana.school.parent.ActivitySplash;
import com.techindiana.school.parent.R;

import org.json.JSONObject;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            try {
                // Check if message contains a notification payload.
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                    sendNotification(getString(R.string.app_name), remoteMessage.getNotification().getBody());
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        try {
            //Log data to Log Cat
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "ProfileResp Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject data = new JSONObject(remoteMessage.getData());

                    String title = data.getString("title");
                    String message = data.getString("message");

                    sendNotification(title, message);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param
     * @param
     */
    private void sendNotification(String ttl, String messageBody) {
        Intent intent = new Intent(this, ActivitySplash.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.splash_screen_logo);
            notificationBuilder.setColor(getResources().getColor(R.color.white));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.logo);
        }
        notificationBuilder.setContentTitle(ttl)
                .setContentText(messageBody)
                //  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


