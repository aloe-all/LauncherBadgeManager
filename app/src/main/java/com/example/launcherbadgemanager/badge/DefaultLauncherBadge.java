package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.Arrays;
import java.util.List;



public class DefaultLauncherBadge implements Badge {
    private static final String TAG = DefaultLauncherBadge.class.getSimpleName();

    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    boolean isSupported(Context context) {
        Intent intent = new Intent(INTENT_ACTION);
        return canResolveBroadcast(context, intent);
    }

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("");
    }

    public static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
}
