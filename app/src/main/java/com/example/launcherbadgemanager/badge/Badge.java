package com.example.launcherbadgemanager.badge;

import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by chenronggang on 2017/11/16.
 */

public interface Badge {

    /**
     *  更新 launcher 快捷方式的角标
     * @param context
     * @param componentName 桌面图标对应的应用入口Activity类
     * @param badgeCount 角标数量
     */
    void updateLauncherBadgeCount(Context context, ComponentName componentName, int badgeCount);

    /**
     *
     * @return 支持的launcher 包名
     */
    List<String> getSupportedLaunchers();
}
