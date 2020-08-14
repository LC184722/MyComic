package com.qc.mycomic.setting;

import com.qc.mycomic.model.MyMap;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/14 22:59
 * @ver 1.0
 */
public class PreloadNumSetting extends CommonSetting<Integer> {

    private static PreloadNumSetting setting = new PreloadNumSetting();

    public static PreloadNumSetting getInstance() {
        return setting;
    }

    @Override
    public String getSaveStr() {
        return "preloadNum";
    }

    @Override
    public String getDefaultValue() {
        return "0";
    }

    @Override
    public Integer getRealValue() {
        return Integer.parseInt(getData());
    }

    @Override
    public void dealMyMap(MyMap<Integer, String> myMap) {
        myMap.put(0, "关闭预加载");
        myMap.put(5, "5");
        myMap.put(10, "10");
        myMap.put(10000, "预加载所有");
    }
}
