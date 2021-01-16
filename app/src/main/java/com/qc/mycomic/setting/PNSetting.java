package com.qc.mycomic.setting;

import java.util.Map;

/**
 * @author LuQiChuang
 * @desc 预加载数量设置
 * @date 2020/8/15 12:50
 * @ver 1.0
 */
public class PNSetting extends Setting {

    @Override
    public String getSaveStr() {
        return "preloadNum";
    }

    @Override
    public String getDefaultValue() {
        return "0";
    }

    @Override
    public void dealMyMap(Map<String, String> myMap) {
        myMap.put("0", "关闭预加载");
        myMap.put("5", "预加载5页");
        myMap.put("10", "预加载10页");
        myMap.put("10000", "预加载所有");
    }

}
