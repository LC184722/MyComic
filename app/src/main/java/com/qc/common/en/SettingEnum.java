package com.qc.common.en;

import com.qc.common.constant.AppConstant;

import java.util.LinkedHashMap;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.en.VSourceEnum;

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
    DEFAULT_NOVEL_SOURCE("defaultNSource", NSourceEnum.AI_YUE.ID),
    IS_FULL_SCREEN("isFullScreen", true),
    NOVEL_FONT_SIZE("novelFontSize", 20),
    NOVEL_AUTO_SPEED("novelAutoSpeed", 4),
    VIDEO_PROGRESS("videoProgress", new LinkedHashMap<>()),
    DEFAULT_VIDEO_SOURCE("defaultVSource", VSourceEnum.YING_HUA.ID),
    COMIC_SOURCE_OPEN("comicSourceOpen", SourceEnum.getMAP().keySet()),
    NOVEL_SOURCE_OPEN("novelSourceOpen", NSourceEnum.getMAP().keySet()),
    VIDEO_SOURCE_OPEN("videoSourceOpen", VSourceEnum.getMAP().keySet()),
    ;

    public final String KEY;
    public final Object DEFAULT_VALUE;

    SettingEnum(String KEY, Object DEFAULT_VALUE) {
        this.KEY = KEY;
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }
}
