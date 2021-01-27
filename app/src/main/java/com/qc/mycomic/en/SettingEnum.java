package com.qc.mycomic.en;

import top.luqichuang.common.mycomic.en.SourceEnum;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/16 21:45
 * @ver 1.0
 */
public enum SettingEnum {

    DEFAULT_SOURCE("defaultSource", SourceEnum.MI_TUI.ID),
    PRELOAD_NUM("preloadNum", 0),
    ;

    public final String KEY;
    public final Object DEFAULT_VALUE;

    SettingEnum(String KEY, Object DEFAULT_VALUE) {
        this.KEY = KEY;
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }
}
