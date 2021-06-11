package com.qc.common.self;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 16:44
 * @ver 1.0
 */
public class CommonData {

    private static final String[] COMIC_TAB_BARS = {
            "我的画架",
            "搜索漫画",
            "个人中心",
    };

    private static final String[] NOVEL_TAB_BARS = {
            "我的书架",
            "搜索小说",
            "个人中心",
    };

    public static String[] getTabBars() {
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            return COMIC_TAB_BARS;
        } else {
            return NOVEL_TAB_BARS;
        }
    }

}
