package com.m8.m8;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title, message, click_action, invoiceId, get_userID;
    JSONObject jsonObject1;
    public static String count,counter;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessage.getData();
        String data = remoteMessage.getData().get("message");
        Log.d("remotedata", data);
        Log.d("remotedata1", "" + remoteMessage.getData());

        // count = String.valueOf(remoteMessage.getData().size());
        //ShortcutBadger.applyCount(this, Integer.parseInt(count));
        //Log.d("count", count);

        try {
            jsonObject1 = new JSONObject(data);
            title = jsonObject1.getString("title");
            //Todo: add user id in all response
            //get_userID = jsonObject1.getString("user_id");
            //Log.d("remotedata", get_userID);
            message = jsonObject1.getString("body");
            //click_action = jsonObject1.getString("click_action");
            //invoiceId = jsonObject1.get("invoice_id").toString();
            //counter = jsonObject1.getString("booking_counter");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "121";
        CharSequence channelName = "m8sworld";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(getResources().getColor(R.color.colorPrimaryDark));
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(false);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Intent intent = new Intent("click_action");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("Inid", invoiceId);
//        intent.putExtra("msg", message);
//
//        intent.putExtra("user_id", get_userID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notificationBuilder.build());

    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}