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
    private static final String SAMSUNG_LAUNCHER_PACKAGENAME = "com.sec.android.app.launcher";
    private static final String SAMSUNG_TW_LAUNCHER_PACKAGENAME = "com.sec.android.app.twlauncher";
    private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
    private static final String[] CONTENT_PROJECTION = new String[]{"_id", "class"};
    private DefaultLauncherBadge mDefaultLauncherBadge;

    /**
     *  三星 Build.VERSION_CODES.LOLLIPOP 21 及以上的版本使用广播的形式更新角标，使用 {@link #mDefaultLauncherBadge}
     *  Build.VERSION_CODES.LOLLIPOP 21 以下的版本的使用更新数据库的形式更新角标
     */
    public SamSungLauncherBadge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDefaultLauncherBadge = new DefaultLauncherBadge();
        }
    }
    @Override
    public void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount) {

        //发送广播的的方式 更新角标
        if (mDefaultLauncherBadge != null && mDefaultLauncherBadge.isSupportBroadcast(context)) {
            mDefaultLauncherBadge.updateLauncherBadgeCount(context, componentName, badgeCount);
        } else {

            // 更新数据库里的表的方式 更新角标
            Uri badgeUri = Uri.parse(CONTENT_URI);
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = contentResolver.query(badgeUri, CONTENT_PROJECTION, "package=?", new String[]{componentName.getPackageName()}, null);
                if (cursor != null) {
                    String launcherActivityName = componentName.getClassName();
                    boolean isLauncherActivityExist = false;
                    while (cursor.moveToNext()) {

                        //获取表的第一列 "_id" 的值
                        int id = cursor.getInt(0);
                        ContentValues contentValues = getContentValues(componentName, badgeCount, false);

                        //更新 badge角标
                        contentResolver.update(badgeUri, contentValues, "_id=?", new String[]{String.valueOf(id)});
                        if (launcherActivityName.equals(cursor.getString(cursor.getColumnIndex("class")))) {
                            isLauncherActivityExist = true;
                        }
                    }

                    // 表里没有手百的 角标记录
                    if (!isLauncherActivityExist) {
                        ContentValues contentValues = getContentValues(componentName, badgeCount, true);

                        // 插入展示 badge 角标
                        contentResolver.insert(badgeUri, contentValues);
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

    /**
     *  组织 ContentValues
     * @param componentName
     * @param badgeCount
     * @param isInsert true，向表中新插入一条记录；false 更新 已有记录的 badgecount
     * @return
     */
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
        return Arrays.asList(SAMSUNG_LAUNCHER_PACKAGENAME, SAMSUNG_TW_LAUNCHER_PACKAGENAME);
    }
}
