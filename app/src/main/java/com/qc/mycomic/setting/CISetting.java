package com.qc.mycomic.setting;

import java.util.Map;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/12/20 18:41
 * @ver 1.0
 */
public class CISetting extends Setting {
    @Override
    public String getSaveStr() {
        return "compressImage";
    }

    @Override
    public String getDefaultValue() {
        return "1";
    }

    @Override
    public void dealMyMap(Map<String, String> myMap) {
        myMap.put("0", "高清画质(高内存占用)");
        myMap.put("1", "标清画质(中内存占用)");
        myMap.put("2", "流畅画质(低内存占用)");
    }
}
