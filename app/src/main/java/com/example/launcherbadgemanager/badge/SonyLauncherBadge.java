package com.example.launcherbadgemanager.badge;

import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class SonyLauncherBadge implements Badge {
    private static final String TAG = SonyLauncherBadge.class.getSimpleName();

    private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
    private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
    private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
    private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";

    private static final String PROVIDER_CONTENT_URI = "content://com.sonymobile.home.resourceprovider/badge";
    private static final String PROVIDER_COLUMNS_BADGE_COUNT = "badge_count";
    private static final String PROVIDER_COLUMNS_PACKAGE_NAME = "package_name";
    private static final String PROVIDER_COLUMNS_ACTIVITY_NAME = "activity_name";
    private static final String SONY_HOME_PROVIDER_NAME = "com.sonymobile.home.resourceprovider";
    private final Uri BADGE_CONTENT_URI = Uri.parse(PROVIDER_CONTENT_URI);

    private AsyncQueryHandler mQueryHandler;

    private static void executeBadgeByBroadcast(Context context, ComponentName componentName,
                                                int badgeCount) {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, componentName.getPackageName());
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        intent.putExtra(INTENT_EXTRA_MESSAGE, String.valueOf(badgeCount));
        intent.putExtra(INTENT_EXTRA_SHOW_MESSAGE, badgeCount > 0);
        context.sendBroadcast(intent);
    }

    private void executeBadgeByContentProvider(Context context, ComponentName componentName,
                                               int badgeCount) {
        if (badgeCount < 0) {
            return;
        }

        final ContentValues contentValues = createContentValues(badgeCount, componentName);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (mQueryHandler == null) {
                mQueryHandler = new AsyncQueryHandler(
                        context.getApplicationContext().getContentResolver()) {
                };
            }
            insertBadgeAsync(contentValues);
        } else {
            insertBadgeSync(context, contentValues);
        }
    }


    private void insertBadgeAsync(final ContentValues contentValues) {
        mQueryHandler.startInsert(0, null, BADGE_CONTENT_URI, contentValues);
    }


    private void insertBadgeSync(final Context context, final ContentValues contentValues) {
        context.getApplicationContext().getContentResolver()
                .insert(BADGE_CONTENT_URI, contentValues);
    }


    private ContentValues createContentValues(final int badgeCount,
            final ComponentName componentName) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(PROVIDER_COLUMNS_BADGE_COUNT, badgeCount);
        contentValues.put(PROVIDER_COLUMNS_PACKAGE_NAME, componentName.getPackageName());
        contentValues.put(PROVIDER_COLUMNS_ACTIVITY_NAME, componentName.getClassName());
        return contentValues;
    }


    private static boolean sonyBadgeContentProviderExists(Context context) {
        boolean exists = false;
        ProviderInfo info = context.getPackageManager().resolveContentProvider(SONY_HOME_PROVIDER_NAME, 0);
        if (info != null) {
            exists = true;
        }
        return exists;
    }

    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        try {
            if (sonyBadgeContentProviderExists(context)) {
                executeBadgeByContentProvider(context, componentName, badgeCount);
            } else {
                executeBadgeByBroadcast(context, componentName, badgeCount);
            }
        } catch (Exception e) {
            Log.d(TAG, TAG + " Exception " + e.toString());
        }
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.sonyericsson.home", "com.sonymobile.home");
    }
}
