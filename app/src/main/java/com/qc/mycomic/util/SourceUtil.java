package com.qc.mycomic.util;

import androidx.annotation.Nullable;

import com.qc.mycomic.en.Codes;
import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.setting.SettingConstant;
import com.qc.mycomic.source.BiliBili;
import com.qc.mycomic.source.MH118;
import com.qc.mycomic.source.ManHuaFen;
import com.qc.mycomic.source.ManHuaTai;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.source.OH;
import com.qc.mycomic.source.PuFei;
import com.qc.mycomic.source.TengXun;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SourceUtil {

    private static Map<Integer, Source> map = SourceEnum.getMAP();

    private static List<Source> sourceList = new ArrayList<>();

    static {
        sourceList.addAll(map.values());
    }

    public static Map<Integer, Source> getMap() {
        return map;
    }

    public static Source getSource(int sourceId) {
        return map.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source source = map.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static List<Source> getSourceList() {
        return sourceList;
    }

    public static int size() {
        return sourceList.size();
    }
}
