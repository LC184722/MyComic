package com.qc.mycomic.util;

import the.one.base.util.SdCardUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class Codes {

    public static final String TAG = "TAG";

    public static final String NORMAL_PATH = SdCardUtil.getNormalSDCardPath();

    public static final String APP_PATH = NORMAL_PATH + "/MyComic";

    public static final String IMG_PATH = APP_PATH + "/ShelfImg";

    public static String versionTag = null;

    public static int versionCode = 0;

    public static final int STATUS_HIS = 0;

    public static final int STATUS_FAV = 1;

    public static final int STATUS_ALL = 2;

    public static final int DESC = 0;

    public static final int ASC = 1;

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36";

    public static final String USER_AGENT_WEB = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";

    public static boolean isFirstLoadWebView = true;

    public static final int NORMAL = 0;

    public static final int READER_TO_CHAPTER = 1;

    public static final int SEARCH_TO_CHAPTER = 2;

    public static final int RANK_TO_CHAPTER = 3;

    public static int toStatus = NORMAL;

    public static final int MI_TUI = 1;

    public static final String MI_TUI_STRING = "米推漫画";

    public static final int MAN_HUA_FEN = 2;

    public static final String MAN_HUA_FEN_STRING = "漫画粉";

    public static final int PU_FEI = 3;

    public static final String PU_FEI_STRING = "扑飞漫画";

    public static final int TENG_XUN = 4;

    public static final String TENG_XUN_STRING = "腾讯动漫";

    public static final int BILI_BILI = 5;

    public static final String BILI_BILI_STRING = "哔哩哔哩";

    public static final int OH = 6;

    public static final String OH_STRING = "oh漫画";

    public static final int MAN_HUA_TAI = 7;

    public static final String MAN_HUA_TAI_STRING = "漫画台";

    public static final int MH_118 = 8;

    public static final String MH_118_STRING = "118漫画";

}
