package com.qc.common.util;

import com.qc.common.constant.AppConstant;
import com.qc.common.en.SettingEnum;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.NovelInfo;

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
            for (Source<ComicInfo> source : SourceUtil.getSourceList()) {
                map.put(source.getSourceId(), source.getSourceName());
            }
        } else if (settingEnum == SettingEnum.DEFAULT_NOVEL_SOURCE) {
            for (Source<NovelInfo> source : SourceUtil.getNSourceList()) {
                map.put(source.getSourceId(), source.getSourceName());
            }
        } else if (settingEnum == SettingEnum.PRELOAD_NUM) {
            map.put(0, "关闭预加载");
            map.put(5, "预加载5页");
            map.put(10, "预加载10页");
            map.put(10000, "预加载所有");
        } else if (settingEnum == SettingEnum.READ_CONTENT) {
            map.put(AppConstant.COMIC_CODE, "漫画");
            map.put(AppConstant.READER_CODE, "小说");
            map.put(AppConstant.VIDEO_CODE, "番剧");
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
