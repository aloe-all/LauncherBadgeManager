package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chenronggang on 2017/11/16.
 */

public class SamSungLauncherBadge implements Badge {
    private static final String TAG = SamSungLauncherBadge.class.getSimpleName();

    private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
    private static final String[] CONTENT_PROJECTION = new String[]{"_id", "class"};
    private DefaultLauncherBadge mDefaultLauncherBadge;

    public SamSungLauncherBadge() {
        if (Build.VERSION.SDK_INT >= 21) {
            mDefaultLauncherBadge = new DefaultLauncherBadge();
        }
    }
    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {
        if (mDefaultLauncherBadge != null && mDefaultLauncherBadge.isSupported(context)) {
            mDefaultLauncherBadge.updateLauncherBadgeCount(context, componentName, badgeCount);
        } else {
            Uri mUri = Uri.parse(CONTENT_URI);
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(mUri, CONTENT_PROJECTION, "package=?", new String[]{componentName.getPackageName()}, null);
                if (cursor != null) {
                    String entryActivityName = componentName.getClassName();
                    boolean entryActivityExist = false;
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        ContentValues contentValues = getContentValues(componentName, badgeCount, false);
                        contentResolver.update(mUri, contentValues, "_id=?", new String[]{String.valueOf(id)});
                        if (entryActivityName.equals(cursor.getString(cursor.getColumnIndex("class")))) {
                            entryActivityExist = true;
                        }
                    }
                    if (!entryActivityExist) {
                        ContentValues contentValues = getContentValues(componentName, badgeCount, true);
                        contentResolver.insert(mUri, contentValues);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, TAG + " Exception " + e.toString());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    private ContentValues getContentValues(ComponentName componentName, int badgeCount, boolean isInsert) {
        ContentValues contentValues = new ContentValues();
        if (isInsert) {
            contentValues.put("package", componentName.getPackageName());
            contentValues.put("class", componentName.getClassName());
        }
        contentValues.put("badgecount", badgeCount);
        return contentValues;
    }

    @Override
    public List<String> getSupportedLaunchers() {
        return Arrays.asList("com.sec.android.app.launcher", "com.sec.android.app.twlauncher");
    }
}
