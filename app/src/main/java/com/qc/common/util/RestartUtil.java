package com.qc.common.util;

import android.app.Activity;
import android.content.Intent;

import com.qc.common.ui.activity.LauncherActivity;

/**
 * @author LuQiChuang
 * @desc 重启工具
 * @date 2020/8/18 10:02
 * @ver 1.0
 */
public class RestartUtil {

    public static void restart(Activity activity) {
        Intent intent = new Intent(activity, LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        activity.finish();
        activity.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
