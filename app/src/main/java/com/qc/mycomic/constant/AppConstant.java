package com.qc.mycomic.constant;

import the.one.base.util.SdCardUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/27 21:39
 * @ver 1.0
 */
public interface AppConstant {

    String NORMAL_PATH = SdCardUtil.getNormalSDCardPath();

    String APP_PATH = NORMAL_PATH + "/MyComic";

    String SHELF_IMG_PATH = APP_PATH + "/ShelfImg";

    String IMG_PATH = APP_PATH + "/Image";
}
