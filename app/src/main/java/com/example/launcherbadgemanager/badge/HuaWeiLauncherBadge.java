package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/16.
 */

public class HuaWeiLauncherBadge implements Badge {
    private static final String TAG = HuaWeiLauncherBadge.class.getSimpleName();
    private static final String HUAWEIBADGEURL = "content://com.huawei.android.launcher.settings/badge/";
    private static final String HUAWEIBADGEMETHOD = "change_badge";

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", componentName.getClassName());
            bundle.putInt("badgenumber", badgeCount);
            context.getContentResolver().call(Uri.parse(HUAWEIBADGEURL), HUAWEIBADGEMETHOD, null, bundle);
        } catch (Exception e) {
            Log.d(TAG, TAG + " Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.huawei.android.launcher");
    }
}
