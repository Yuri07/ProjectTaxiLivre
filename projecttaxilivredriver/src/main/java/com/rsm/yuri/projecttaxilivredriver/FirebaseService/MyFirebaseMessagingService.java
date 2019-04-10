package com.rsm.yuri.projecttaxilivredriver.FirebaseService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rsm.yuri.projecttaxilivredriver.FirebaseService.utils.NotificationUtils;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainOnTravelRequestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by yuri_ on 25/04/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("d", "onMessageReceived()");

        //Log.d("d", "From: " + remoteMessage.getFrom());

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
            //JSONObject data = json.getJSONObject("data");

            String requesterEmail = json.getString("requesterEmail");
            String requesterName = json.getString("requesterName");
            String placeOriginAddress = json.getString("placeOriginAddress");
            String placeDestinoAddress = json.getString("placeDestinoAddress");
            double latOrigem = json.getDouble("latOrigem");
            double longOrigem = json.getDouble("longOrigem");
            double latDestino = json.getDouble("latDestino");
            double longDestino = json.getDouble("longDestino");
            String travelDate = json.getString("travelDate");
            double travelPrice = json.getDouble("travelPrice");
            //JSONObject payload = json.getJSONObject("payload");

            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() requesterEmail: " + requesterEmail);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() requesterName " + requesterName);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() placeOriginAddress: " + placeOriginAddress);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() placeDestinoAddress " + placeDestinoAddress);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() latOrigem: " + latOrigem);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() longOrigem: " + longOrigem);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() latDestino: " + latDestino);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() longDestino: " + longDestino);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() travelDate: " + travelDate);
            Log.d("d", "MyFirebaseMessagingService.handleDataMessage() travelPrice: " + travelPrice);

            /*Intent i = new Intent();
            i.setClass(this, MainActivity.class);*/
            Bundle args = new Bundle();
            args.putString("requesterEmail", requesterEmail);
            args.putString("requesterName", requesterName);
            args.putString("placeOriginAddress", placeOriginAddress);
            args.putString("placeDestinoAddress", placeDestinoAddress);
            args.putDouble("latOrigem", latOrigem);
            args.putDouble("longOrigem", longOrigem);
            args.putDouble("latDestino", latDestino);
            args.putDouble("longDestino", longDestino);
            args.putString("travelDate", travelDate);
            args.putDouble("travelPrice", travelPrice);
            /*i.putExtras(args);
            i.putExtra(MainActivity.DATA_REQUEST_TRAVEL_MSG_KEY, "true");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_FROM_BACKGROUND);
            startActivity(i);*/

            Intent intent = new Intent(MainActivity.RECEIVER_INTENT);
            intent.putExtras(args);
            intent.putExtra(MainActivity.DATA_REQUEST_TRAVEL_MSG_KEY, MainActivity.DATA_REQUEST_TRAVEL_MSG);
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

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
