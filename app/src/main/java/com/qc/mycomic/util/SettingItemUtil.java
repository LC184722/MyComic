package com.qc.mycomic.util;

import com.qc.mycomic.en.SettingEnum;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.mycomic.model.Source;
import top.luqichuang.common.mycomic.util.MapUtil;
import top.luqichuang.common.mycomic.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/16 23:36
 * @ver 1.0
 */
public class SettingItemUtil {

    public static Map<Object, String> getMap(SettingEnum settingEnum) {
        Map<Object, String> map = new LinkedHashMap<>();
        if (settingEnum == SettingEnum.DEFAULT_SOURCE) {
            for (Source source : SourceUtil.getSourceList()) {
                map.put(source.getSourceId(), source.getSourceName());
            }
        } else if (settingEnum == SettingEnum.PRELOAD_NUM) {
            map.put(0, "关闭预加载");
            map.put(5, "预加载5页");
            map.put(10, "预加载10页");
            map.put(10000, "预加载所有");
        }
        return map;
    }

    public static Object getDefaultKey(SettingEnum settingEnum) {
        Map<Object, String> map = getMap(settingEnum);
        if (!map.isEmpty()) {
            return MapUtil.getFirst(map).getKey();
        }
        return "";
    }

    public static String getDefaultDesc(SettingEnum settingEnum) {
        Map<Object, String> map = getMap(settingEnum);
        if (!map.isEmpty()) {
            return MapUtil.getValueByIndex(map, 0);
        }
        return "";
    }

    public static String getDesc(SettingEnum settingEnum, Object key) {
        Map<Object, String> map = getMap(settingEnum);
        return getDesc(map, key);
    }

    public static String getDesc(Map<Object, String> map, Object key) {
        if (!map.isEmpty()) {
            return map.get(key);
        }
        return "";
    }

}
