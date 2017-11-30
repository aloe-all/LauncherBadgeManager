package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/16.
 */

public class LauncherBadgeManager {
    private static final String TAG = LauncherBadgeManager.class.getSimpleName();
    private static Badge sCurrentLauncherBadge;
    private static LauncherBadgeManager sLauncherBadgeManager;
    private static final LinkedList<Class<? extends Badge>> sLauncherBadgeLists = new LinkedList<Class<? extends Badge>>();
    static {
        sLauncherBadgeLists.add(HuaWeiLauncherBadge.class);
        sLauncherBadgeLists.add(XiaoMiLauncherBadge.class);
        sLauncherBadgeLists.add(SamSungLauncherBadge.class);
        sLauncherBadgeLists.add(SonyLauncherBadge.class);
        sLauncherBadgeLists.add(VivoLauncherBadge.class);
        sLauncherBadgeLists.add(OppoLauncherBadge.class);
        sLauncherBadgeLists.add(ZteLauncherBadge.class);
    }
    private LauncherBadgeManager() {}

    public static LauncherBadgeManager getInstance() {
        if (sLauncherBadgeManager == null) {
            synchronized (LauncherBadgeManager.class) {
                if (sLauncherBadgeManager == null) {
                    sLauncherBadgeManager = new LauncherBadgeManager();
                }
            }
        }
        return sLauncherBadgeManager;
    }

    public void applyUpdatelauncherBadge(Context context, int badgeCount) {
        Log.d(TAG, TAG + " applyUpdatelauncherBadge()");
        if (context == null) {
            Log.d(TAG, TAG + " context == null");
            return;
        }
        if (sCurrentLauncherBadge == null) {
            initBadge(context);
        }
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = launcherIntent.getComponent();
        if (sCurrentLauncherBadge != null) {
            sCurrentLauncherBadge.updateLauncherBadgeCount(context, componentName, badgeCount);
        }
    }

    private void initBadge (Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Intent.ACTION_MAIN 和 Intent.CATEGORY_HOME 过滤出该手机安装的符合条件的 app，一般为 launcher setting app
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo:resolveInfos) {
            String packagename = resolveInfo.activityInfo.packageName;
            Badge badge = null;
            for (Class<? extends Badge> launcherBadge:sLauncherBadgeLists) {
                try {
                    badge = launcherBadge.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 该手机的 launcher 包名
                if (badge != null && badge.getSupportedLaunchers().contains(packagename)) {
                    sCurrentLauncherBadge = badge;
                    return;
                }
            }
        }
        if (sCurrentLauncherBadge == null) {
            if (Build.MANUFACTURER.equalsIgnoreCase("ZTE"))
                sCurrentLauncherBadge = new ZteLauncherBadge();
        }
    }
}
