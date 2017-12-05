package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/20.
 */

public class OppoLauncherBadge implements Badge {
    private static final String TAG = OppoLauncherBadge.class.getSimpleName();

    private static final String PROVIDER_CONTENT_URI = "content://com.android.badge/badge";
    private static final String INTENT_ACTION = "com.oppo.unsettledevent";
    private static final String INTENT_EXTRA_PACKAGENAME = "pakeageName";
    private static final String INTENT_EXTRA_BADGE_COUNT = "number";
    private static final String INTENT_EXTRA_BADGE_UPGRADENUMBER = "upgradeNumber";
    private static final String INTENT_EXTRA_BADGEUPGRADE_COUNT = "app_badge_count";
    private static final String INTENT_EXTRA_BADGEUPGRADE_METHOD = "setAppBadgeCount";

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {

            // clear badge
            if (badgeCount == 0) {
                badgeCount = -1;
            }
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_BADGE_UPGRADENUMBER, badgeCount);
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            } else {
                Bundle extras = new Bundle();
                extras.putInt(INTENT_EXTRA_BADGEUPGRADE_COUNT, badgeCount);
                context.getContentResolver().call(Uri.parse(PROVIDER_CONTENT_URI), INTENT_EXTRA_BADGEUPGRADE_METHOD, null, extras);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.oppo.launcher");
    }

    private boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
}
