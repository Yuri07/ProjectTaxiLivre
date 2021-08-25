package com.rsm.yuri.projecttaxilivre.FirebaseService;

import android.content.Intent;

//import androidx.core.content.LocalBroadcastManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rsm.yuri.projecttaxilivre.FirebaseService.utils.NotificationUtils;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuri_ on 25/04/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("d", "MyFirebaseMessagingService - onMessageReceived()");
        Log.d("d", "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("d", "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("d", "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.d("d", "Exception: " + e.getMessage());
            }
        }

    }

    private void handleNotification(String message) {
        Log.d("d", "push json: ");
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.d("d", "push json: " + json.toString());

        try {

            String initiateTravel = json.getString("initiateTravel");
            String terminateTravel = json.getString("terminateTravel");


            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() initiateTravel: " + initiateTravel);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() terminateTravel " + terminateTravel);


            //Bundle args = new Bundle();
            Intent intent = new Intent(MainActivity.RECEIVER_INTENT);

            if(terminateTravel.equals("false") && initiateTravel.equals("true")){

                //args.putString("initiateTravel", initiateTravel);
                //intent.putExtras(args);
                intent.putExtra(MainActivity.DATA_INITIATE_TRAVEL_MSG_KEY,"true");

            }else if(terminateTravel.equals("true")){

                //args.putString("terminateTravel", terminateTravel);
                //intent.putExtras(args);
                intent.putExtra(MainActivity.DATA_TERMINATE_TRAVEL_MSG_KEY,"true");


            }

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            /*startActivity(new Intent(this, MainActivity.class));*/

            /*if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent("pushNotification");
                pushNotification.putExtra("requesterName", requesterName);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("requesterName", requesterName);

                // check for image attachment
                //if (TextUtils.isEmpty(imageUrl)) {
                    //showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                //} else {
                    // image is present, show notification with image
                //    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                //}
            }*/
        } catch (JSONException e) {
            Log.d("d", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.d("d", "Exception: " + e.getMessage());
        }
    }

}
