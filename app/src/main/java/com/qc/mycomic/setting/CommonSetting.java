package com.qc.mycomic.setting;

import com.qc.mycomic.model.MyMap;

import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/14 22:47
 * @ver 1.0
 */
public abstract class CommonSetting<T> {

    private MyMap<T, String> myMap;

    public abstract String getSaveStr();

    public abstract String getDefaultValue();

    public abstract T getRealValue();

    public String getData() {
        return SpUtil.getInstance().getString(getSaveStr(), getDefaultValue());
    }

    public void setData(String data) {
        SpUtil.getInstance().putString(getSaveStr(), data);
    }

    public MyMap<T, String> getMyMap() {
        if (myMap == null) {
            myMap = new MyMap<>();
            dealMyMap(myMap);
        }
        return myMap;
    }

    public abstract void dealMyMap(MyMap<T, String> myMap);

    public String getPreloadDesc() {
        return getMyMap().get(getRealValue());
    }

}
