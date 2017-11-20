package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
        sLauncherBadgeLists.add(SonyLauncherBadger.class);
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
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo:resolveInfos) {
            String packagename = resolveInfo.activityInfo.packageName;
            Log.d(TAG, "packagename: " + packagename);
            Badge badge = null;
            for (Class<? extends Badge> launcherBadge:sLauncherBadgeLists) {
                try {
                    badge = launcherBadge.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (badge != null && badge.getSupportedLaunchers().contains(packagename)) {
                    sCurrentLauncherBadge = badge;
                    return;
                }
            }
        }
    }
}
