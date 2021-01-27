package com.qc.mycomic.util;

import com.alibaba.fastjson.JSONObject;
import com.qc.mycomic.en.SettingEnum;

import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/16 21:39
 * @ver 1.0
 */
public class SettingUtil {

    private static final JSONObject JSON_OBJECT;

    private static final String SAVE_STR = "json";

    static {
        JSON_OBJECT = JSONObject.parseObject(getData());
    }

    private static String getData() {
        return SpUtil.getInstance().getString(SAVE_STR, "{}");
    }

    private static void save() {
        SpUtil.getInstance().putString(SAVE_STR, JSON_OBJECT.toJSONString());
    }

    public static Object getSettingKey(SettingEnum settingEnum) {
        Object key = JSON_OBJECT.get(settingEnum.KEY);
        if (key == null) {
            key = settingEnum.DEFAULT_VALUE;
            JSON_OBJECT.put(settingEnum.KEY, settingEnum.DEFAULT_VALUE);
            save();
        }
        return key;
    }

    public static String getSettingDesc(SettingEnum settingEnum) {
        String desc = (String) JSON_OBJECT.get(settingEnum.KEY + "Desc");
        if (desc == null) {
            desc = SettingItemUtil.getDefaultDesc(settingEnum);
        }
        return desc;
    }

    public static void putSetting(SettingEnum settingEnum, Object value) {
        putSetting(settingEnum, value, "");
    }

    public static void putSetting(SettingEnum settingEnum, Object value, String desc) {
        JSON_OBJECT.put(settingEnum.KEY, value);
        JSON_OBJECT.put(settingEnum.KEY + "Desc", desc);
        save();
    }
}
