package com.qc.mycomic.util;

import java.util.LinkedList;
import java.util.List;

import the.one.base.model.PopupItem;

/**
 * @author LuQiChuang
 * @version 1.0
 * @description
 * @date 2020/8/12 21:33
 */
public class PopupUtil {

    public static List<PopupItem> getPopupItemList(List<String> list) {
        List<PopupItem> itemList = new LinkedList<>();
        for (String s : list) {
            itemList.add(new PopupItem(s));
        }
        return itemList;
    }

    public static List<PopupItem> getPopupItemList(String[] strings) {
        List<PopupItem> itemList = new LinkedList<>();
        for (String s : strings) {
            itemList.add(new PopupItem(s));
        }
        return itemList;
    }

}
