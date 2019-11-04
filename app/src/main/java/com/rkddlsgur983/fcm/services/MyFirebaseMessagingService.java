package com.rkddlsgur983.fcm.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rkddlsgur983.fcm.R;
import com.rkddlsgur983.fcm.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FIREBASE_MESSAGING";
    private static final String TOKEN = "TOKEN";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            /* 파이어베이스 콘솔에서 푸시 데이터를 발신 하는 경우
             * Foreground 상태(앱이 켜져있을 경우)에만
             * 푸시 데이터 수신이 가능하다.
             */
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title, body);
        } else if (remoteMessage.getData().size() > 0) {
            if (!handleNow()) {
                /* 필수적이지 않지만 푸시 메시지의 크기가 4KB를 초과하거나
                 * 10초 이상이 소요될 경우 태스크를 비동기로 처리하는 것이 좋습니다.
                 */
                scheduleJob();
            }
            /* 커스텀 API에서 푸시 데이터를 발신하는 경우
             * Foreground, Background 모두 푸시 데이터 수신이 가능하다.
             */
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("content");

            wakeUpApp();
            sendNotification(title, body);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TOKEN, token);
    }

    private void scheduleJob() {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
    }

    private boolean handleNow() {
        return true;
    }

    private void wakeUpApp() {

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:Wakelock");
        wakeLock.acquire(3000);
    }

    private void sendNotification(String title, String body) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{300, 200, 300, 200})
                        .setLights(Color.BLUE, 1, 1)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
