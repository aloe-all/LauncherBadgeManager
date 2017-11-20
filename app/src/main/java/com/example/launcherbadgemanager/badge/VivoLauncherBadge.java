package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/20.
 */

public class VivoLauncherBadge implements Badge {
    private static final String TAG = VivoLauncherBadge.class.getSimpleName();

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            intent.putExtra("className", componentName.getClassName());
            intent.putExtra("notificationNum", badgeCount);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.vivo.launcher", "com.bbk.launcher2");
    }
}
