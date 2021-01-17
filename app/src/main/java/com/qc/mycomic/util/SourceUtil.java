package com.qc.mycomic.util;

import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.model.Source;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SourceUtil {

    private static final Map<Integer, Source> MAP = SourceEnum.getMAP();

    private static final List<Source> SOURCE_LIST = new ArrayList<>();

    static {
        for (Source source : MAP.values()) {
            if (source.isValid()) {
                SOURCE_LIST.add(source);
            }
        }
    }

    public static Source getSource(int sourceId) {
        return MAP.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source source = MAP.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static List<Source> getSourceList() {
        return SOURCE_LIST;
    }

    public static int size() {
        return SOURCE_LIST.size();
    }
}
