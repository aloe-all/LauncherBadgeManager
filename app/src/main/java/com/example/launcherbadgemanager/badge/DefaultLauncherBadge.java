package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Arrays;
import java.util.List;


/**
 * 使用广播的方式更新 icon 角标数字
 * @author chenronggang
 * @since 2017/12/12
 * @version 9.1
 */
public class DefaultLauncherBadge implements Badge {
    private static final String TAG = DefaultLauncherBadge.class.getSimpleName();

    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
            Intent intent = new Intent(INTENT_ACTION);
            intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
            intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
            if (context != null && isHasBroadcastReceivers(context, intent)) {

                //通过广播的形式更新 icon 角标
                context.sendBroadcast(intent);
            }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("");
    }

    /**
     *  从给定的 intent 查询接受该 intent 的广播接收器 broadcast receivers
     * @param context
     * @param intent
     * @return true 存在 broadcast receivers
     */
    private boolean isHasBroadcastReceivers(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    /**
     *  判断 action android.intent.action.BADGE_COUNT_UPDATE 的广播接收器，是否存在
     * @param context
     * @return
     */
    public boolean isSupportBroadcast(Context context) {
        Intent intent = new Intent(INTENT_ACTION);
        return isHasBroadcastReceivers(context, intent);
    }
}
