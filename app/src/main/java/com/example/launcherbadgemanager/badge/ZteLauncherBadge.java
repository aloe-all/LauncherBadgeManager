package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/30.
 */

public class ZteLauncherBadge implements Badge {
    private static final String TAG = ZteLauncherBadge.class.getSimpleName();

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            Bundle extra = new Bundle();
            extra.putInt("app_badge_count", badgeCount);
            extra.putString("app_badge_component_name", componentName.flattenToString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                context.getContentResolver().call(
                        Uri.parse("content://com.android.launcher3.cornermark.unreadbadge"),
                        "setAppUnreadCount", null, extra);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, TAG + "Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("");     // com.android.launcher3 android原生launcher包名，不能区分具体那个厂商
    }
}
