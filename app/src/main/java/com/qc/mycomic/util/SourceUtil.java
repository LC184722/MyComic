package com.qc.mycomic.util;

import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.source.BiliBili;
import com.qc.mycomic.source.MH118;
import com.qc.mycomic.source.ManHuaFen;
import com.qc.mycomic.source.ManHuaTai;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.source.OH;
import com.qc.mycomic.source.PuFei;
import com.qc.mycomic.source.TengXun;

import java.util.List;

/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SourceUtil {

    private static MyMap<Integer, Source> map = new MyMap<>();

    private static List<Source> sourceList;

    static {
        map.put(Codes.MI_TUI, new MiTui());
        map.put(Codes.MAN_HUA_FEN, new ManHuaFen());
        map.put(Codes.PU_FEI, new PuFei());
        map.put(Codes.TENG_XUN, new TengXun());
        map.put(Codes.BILI_BILI, new BiliBili());
        map.put(Codes.OH, new OH());
        map.put(Codes.MAN_HUA_TAI, new ManHuaTai());
        map.put(Codes.MH_118, new MH118());
        sourceList = map.getValueList();
    }

    public static MyMap<Integer, Source> getMap() {
        return map;
    }

    public static Source getSource(int sourceId) {
        return map.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source source = getSource(sourceId);
        return source != null ? source.getSourceName() : null;
    }

    public static int size() {
        return sourceList.size();
    }

    public static List<Source> getSourceList() {
        return sourceList;
    }

}
