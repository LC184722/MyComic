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

    private static final String[] VIDEO_TAB_BARS = {
            "我的番剧",
            "搜索番剧",
            "个人中心",
    };

    private static final String[] COMMON_TAB_BARS = {
            "主页",
            "搜索",
            "个人",
    };

    public static String[] getTabBars() {
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            return COMIC_TAB_BARS;
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            return NOVEL_TAB_BARS;
        } else if (TmpData.contentCode == AppConstant.VIDEO_CODE) {
            return VIDEO_TAB_BARS;
        } else {
            return COMMON_TAB_BARS;
        }
    }

}
