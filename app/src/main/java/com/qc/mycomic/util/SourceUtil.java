package com.qc.mycomic.util;

import com.qc.mycomic.model.MyMap;
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
import java.util.List;
import java.util.Map;

import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SourceUtil {

    private static MyMap<Integer, Source> map = new MyMap<>();

    private static List<Source> sourceList = new ArrayList<>();

    static {
        map.put(Codes.MI_TUI, new MiTui());
        map.put(Codes.MAN_HUA_FEN, new ManHuaFen());
        map.put(Codes.PU_FEI, new PuFei());
        map.put(Codes.TENG_XUN, new TengXun());
        map.put(Codes.BILI_BILI, new BiliBili());
        map.put(Codes.OH, new OH());
        map.put(Codes.MAN_HUA_TAI, new ManHuaTai());
        map.put(Codes.MH_118, new MH118());
        String sourceStr = SpUtil.getInstance().getString(SettingConstant.SOURCE_STR, "");
        reloadSourceList(sourceStr);
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

    public static void reloadSourceList(String sourceStr) {
        if (sourceStr.length() < map.size()) {
            sourceStr = String.format("%-" + map.size() + "s", sourceStr).replace(' ', '1');
        } else if (sourceStr.length() > map.size()) {
            sourceStr = sourceStr.substring(0, map.size());
        }
        sourceList.clear();
        for (int i = 0; i < sourceStr.length(); i++) {
            char ch = sourceStr.charAt(i);
            if (ch != '0') {
                sourceList.add(map.getByIndex(i).getValue());
            }
        }
        SpUtil.getInstance().putString(SettingConstant.SOURCE_STR, sourceStr);
    }

    public static String getSourceStr() {
        StringBuilder sourceStr = new StringBuilder();
        int index = 0;
        for (Integer integer : map.keySet()) {
            if (index < sourceList.size() && sourceList.get(index).getSourceId() == integer) {
                sourceStr.append('1');
                index += 1;
            } else {
                sourceStr.append('0');
            }
        }
        return sourceStr.toString();
    }

    public static int size() {
        return sourceList.size();
    }

    public static int maxSize() {
        return map.size();
    }

    public static List<Source> getSourceList() {
        return sourceList;
    }

    public static List<String> getAllSourceNameList() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, Source> entry : map.entrySet()) {
            list.add(entry.getValue().getSourceName());
        }
        return list;
    }

    public static String[] getAllSourceNameArray() {
        String[] strings = new String[map.size()];
        int i = 0;
        for (Map.Entry<Integer, Source> entry : map.entrySet()) {
            strings[i++] = entry.getValue().getSourceName();
        }
        return strings;
    }

    public static int[] getSourceIntArr() {
        String sourceStr = getSourceStr();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < sourceStr.length(); i++) {
            if (sourceStr.charAt(i) == '1') {
                list.add(i);
            }
        }
        int[] intArr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            intArr[i] = list.get(i);
        }
        return intArr;
    }

    public static int getSourceStrSize() {
        String sourceStr = SpUtil.getInstance().getString(SettingConstant.SOURCE_STR, getSourceStr());
        return getSourceStrSize(sourceStr);
    }

    public static int getSourceStrSize(String sourceStr) {
        int size = 0;
        for (char c : sourceStr.toCharArray()) {
            if (c == '1') {
                size++;
            }
        }
        return size;
    }

}
