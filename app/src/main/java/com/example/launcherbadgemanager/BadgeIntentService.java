package com.example.launcherbadgemanager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.launcherbadgemanager.badge.XiaoMiLauncherBadge;

import java.util.Random;

public class BadgeIntentService extends IntentService {
    private static final String TAG = BadgeIntentService.class.getSimpleName();
    private static int notificationId = 0;

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, TAG + "onStart");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, TAG + "onHandleIntent");
        if (intent != null) {
            int badgeCount = intent.getIntExtra("badgeCount", 0);
            if (mNotificationManager != null) {
                mNotificationManager.cancel(notificationId);
                Random random = new Random();
                notificationId = random.nextInt(255);
            }

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            XiaoMiLauncherBadge.applyNotification(getApplicationContext(), notification, badgeCount);

            if (mNotificationManager != null) {
                mNotificationManager.notify(notificationId, notification);
            }
        }
    }
}
