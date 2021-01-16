package com.qc.mycomic.setting;

import java.util.Map;
import java.util.LinkedHashMap;

import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc 设置抽象类
 * @date 2020/8/12 21:14
 */
public abstract class Setting {

    private Map<String, String> myMap;

    public abstract String getSaveStr();

    public abstract String getDefaultValue();

    public String getData() {
        return SpUtil.getInstance().getString(getSaveStr(), getDefaultValue());
    }

    public void setData(String data) {
        SpUtil.getInstance().putString(getSaveStr(), data);
    }

    public Map<String, String> getMyMap() {
        if (myMap == null) {
            myMap = new LinkedHashMap<>();
            dealMyMap(myMap);
        }
        return myMap;
    }

    public abstract void dealMyMap(Map<String, String> myMap);

    public String getDetailDesc() {
        return getMyMap().get(getData());
    }

}
