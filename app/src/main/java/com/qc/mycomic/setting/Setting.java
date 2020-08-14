package com.qc.mycomic.setting;

import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.PopupUtil;

import java.util.List;
import java.util.Map;

import the.one.base.model.PopupItem;
import the.one.base.util.PopupWindowUtil;
import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc
 * @date 2020/8/12 21:14
 */
public class Setting {

    /*===默认漫画源============================================================*/
    public static String defaultSourceIdStr = "defaultSourceId";

    public static int getDefaultSourceId() {
        return SpUtil.getInstance().getInt(defaultSourceIdStr, Codes.MI_TUI);
    }

    public static void setDefaultSourceId(int defaultSourceId) {
        SpUtil.getInstance().putInt(defaultSourceIdStr, defaultSourceId);
    }
    /*===默认漫画源============================================================*/

    /*===预加载数量============================================================*/
    private static String preloadNumStr = "preloadNum";
    private static int preloadNumDefault = 0;

    private static MyMap<Integer, String> preloadMap;

    public static int getPreloadNum() {
        return SpUtil.getInstance().getInt(preloadNumStr, preloadNumDefault);
    }

    public static void setPreloadNum(int preloadNum) {
        SpUtil.getInstance().putInt(preloadNumStr, preloadNum);
    }

    public static MyMap<Integer, String> getPreloadMap() {
        if (preloadMap == null) {
            preloadMap = new MyMap<>();
            preloadMap.put(0, "关闭预加载");
            preloadMap.put(5, "5");
            preloadMap.put(10, "10");
            preloadMap.put(10000, "预加载所有");
        }
        return preloadMap;
    }

    public static String getPreloadDesc() {
        return getPreloadMap().get(getPreloadNum());
    }
    /*===预加载数量============================================================*/

}
