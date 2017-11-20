package com.example.launcherbadgemanager.badge;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.launcherbadgemanager.BadgeIntentService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/16.
 */

public class XiaoMiLauncherBadge implements Badge {
    private static final String TAG = XiaoMiLauncherBadge.class.getSimpleName();
    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        context.startService(
                new Intent(context, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
        );
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.miui.home");
    }

    /**
     * @param context      Caller context
     * @param notification
     * @param badgeCount
     */
    public static void applyNotification(Context context, Notification notification, int badgeCount) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, badgeCount);
        } catch (Exception e) {
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }
}
