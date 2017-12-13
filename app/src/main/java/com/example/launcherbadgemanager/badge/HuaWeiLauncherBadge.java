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
    private static final String HUAWEI_BADGEURL = "content://com.huawei.android.launcher.settings/badge/";
    private static final String HUAWEI_BADGE_METHOD = "change_badge";
    private static final String HUAWEI_PACKAGE_NAME = "com.huawei.android.launcher";

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
            Bundle bundle = new Bundle();

            // 应用包名
            bundle.putString("package", context.getPackageName());

            // 桌面图标对应的应用入口Activity类
            bundle.putString("class", componentName.getClassName());

            // 角标数字
            bundle.putInt("badgenumber", badgeCount);
        try {
            context.getContentResolver().call(Uri.parse(HUAWEI_BADGEURL), HUAWEI_BADGE_METHOD, null, bundle);
        } catch (Exception e) {
            Log.d(TAG, TAG + " Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList(HUAWEI_PACKAGE_NAME);
    }
}
