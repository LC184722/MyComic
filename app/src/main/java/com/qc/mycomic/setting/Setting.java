package com.qc.mycomic.setting;

import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.PopupUtil;

import java.util.List;
import java.util.Map;

import the.one.base.model.PopupItem;
import the.one.base.util.PopupWindowUtil;
import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc 设置抽象类
 * @date 2020/8/12 21:14
 */
public abstract class Setting {

    private MyMap<String, String> myMap;

    public abstract String getSaveStr();

    public abstract String getDefaultValue();

    public String getData() {
        return SpUtil.getInstance().getString(getSaveStr(), getDefaultValue());
    }

    public void setData(String data) {
        SpUtil.getInstance().putString(getSaveStr(), data);
    }

    public MyMap<String, String> getMyMap() {
        if (myMap == null) {
            myMap = new MyMap<>();
            dealMyMap(myMap);
        }
        return myMap;
    }

    public abstract void dealMyMap(MyMap<String, String> myMap);

    public String getDetailDesc() {
        return getMyMap().get(getData());
    }

}
