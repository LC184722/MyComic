package com.qc.common.en;

import com.qc.common.constant.AppConstant;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.en.SourceEnum;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/16 21:45
 * @ver 1.0
 */
public enum SettingEnum {

    DEFAULT_SOURCE("defaultSource", SourceEnum.MI_TUI.ID),
    PRELOAD_NUM("preloadNum", 10000),
    READ_CONTENT("readContent", AppConstant.COMIC_CODE),
    DEFAULT_NOVEL_SOURCE("defaultNSource", NSourceEnum.XIN_BI_QU_GE.ID),
    IS_FULL_SCREEN("isFullScreen", true),
    NOVEL_FONT_SIZE("novelFontSize", 20),
    NOVEL_AUTO_SPEED("novelAutoSpeed", 4),
    ;

    public final String KEY;
    public final Object DEFAULT_VALUE;

    SettingEnum(String KEY, Object DEFAULT_VALUE) {
        this.KEY = KEY;
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }
}
