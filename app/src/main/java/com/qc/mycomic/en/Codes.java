package com.qc.mycomic.en;

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

    public static final String SHELF_IMG_PATH = APP_PATH + "/ShelfImg";

    public static final String IMG_PATH = APP_PATH + "/Image";

    public static String versionTag = null;

    public static int versionCode = 0;

    public static final int STATUS_HIS = 0;

    public static final int STATUS_FAV = 1;

    public static final int STATUS_ALL = 2;

    public static final int DESC = 0;

    public static final int ASC = 1;

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36";

    public static final String USER_AGENT_WEB = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";

    public static final int NORMAL = 0;

    public static final int READER_TO_CHAPTER = 1;

    public static final int SEARCH_TO_CHAPTER = 2;

    public static final int RANK_TO_CHAPTER = 3;

    public static int toStatus = NORMAL;

}
