package com.qc.mycomic.setting;

import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.PopupUtil;

import java.util.List;

import the.one.base.model.PopupItem;
import the.one.base.util.PopupWindowUtil;
import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @description
 * @date 2020/8/12 21:14
 */
public class Setting {

    /*===默认漫画源============================================================*/
    public static String defaultSourceIdStr = "defaultSourceId";

    private static List<PopupItem> preloadNumItemList;

    public static int getDefaultSourceId() {
        return SpUtil.getInstance().getInt(defaultSourceIdStr, Codes.MI_TUI);
    }

    public static void setDefaultSourceId(int defaultSourceId) {
        SpUtil.getInstance().putInt(defaultSourceIdStr, defaultSourceId);
    }
    /*===默认漫画源============================================================*/

    /*===预加载数量============================================================*/
    public static String preloadNumStr = "preloadNum";
    public static int preloadNumDefault = 0;
    private static String[] strings = {"关闭预加载", "5", "10", "预加载所有"};
    private static int[] preloadNums = {0, 5, 10, 10000};

    public static int getPreloadNum() {
        return SpUtil.getInstance().getInt(preloadNumStr, preloadNumDefault);
    }

    public static void setPreloadNum(int preloadNum) {
        SpUtil.getInstance().putInt(preloadNumStr, preloadNum);
    }

    public static List<PopupItem> getPreloadNumItemList() {
        if (preloadNumItemList == null) {
            preloadNumItemList = PopupUtil.getPopupItemList(strings);
        }
        return preloadNumItemList;
    }

    public static int getPreloadNumIndex() {
        int num = getPreloadNum();
        for (int i = 0; i < preloadNums.length; i++) {
            int preloadNum = preloadNums[i];
            if (preloadNum == num) {
                return i;
            }
        }
        return preloadNumDefault;
    }

    public static String getPreloadNumTag() {
        return strings[getPreloadNumIndex()];
    }

    public static int getPreloadNumByTag(String tag) {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (tag.equals(string)) {
                return preloadNums[i];
            }
        }
        return preloadNumDefault;
    }
    /*===预加载数量============================================================*/

}
