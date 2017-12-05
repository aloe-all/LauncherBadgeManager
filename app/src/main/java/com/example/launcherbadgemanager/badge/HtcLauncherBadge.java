package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/12/1.
 */

public class HtcLauncherBadge implements Badge {
    private static final String TAG = HtcLauncherBadge.class.getSimpleName();

    public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
    public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
    public static final String PACKAGENAME = "packagename";
    public static final String COUNT = "count";
    public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
    public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            Intent notifationIntent = new Intent(INTENT_SET_NOTIFICATION);
            notifationIntent.putExtra(EXTRA_COMPONENT, componentName.flattenToShortString());
            notifationIntent.putExtra(EXTRA_COUNT, badgeCount);

            Intent shortcutIntent = new Intent(INTENT_UPDATE_SHORTCUT);
            shortcutIntent.putExtra(PACKAGENAME, componentName.getPackageName());
            shortcutIntent.putExtra(COUNT, badgeCount);

            if(canResolveBroadcast(context, notifationIntent) || canResolveBroadcast(context, shortcutIntent)) {
                context.sendBroadcast(notifationIntent);
                context.sendBroadcast(shortcutIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.htc.launcher");
    }

    private boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
}
