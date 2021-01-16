package com.qc.mycomic.util;

import android.content.Context;

import com.qc.mycomic.model.ComicInfo;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import the.one.base.model.PopupItem;
import the.one.base.util.QMUIBottomSheetUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc
 * @date 2020/8/12 21:33
 */
public class PopupUtil {

    public static List<PopupItem> getPopupItemList(Map<?, String> myMap) {
        List<PopupItem> itemList = new ArrayList<>();
        for (Map.Entry<?, String> entry : myMap.entrySet()) {
            itemList.add(new PopupItem(entry.getValue()));
        }
        return itemList;
    }

    public static Map<Integer, String> getMyMap(List<ComicInfo> comicInfoList) {
        Map<Integer, String> myMap = new LinkedHashMap<>();
        for (ComicInfo comicInfo : comicInfoList) {
            myMap.put(comicInfo.getSourceId(), SourceUtil.getSourceName(comicInfo.getSourceId()));
        }
        return myMap;
    }

    public static void showSimpleBottomSheetList(Context context, Map<?, String> myMap, String title, Object key, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        List<PopupItem> list = getPopupItemList(myMap);
        int index = MapUtil.indexOf(myMap, key);
        QMUIBottomSheetUtil.showSimpleBottomSheetList(context, list, title, index, listener).show();
    }

    public static void showNumerousMultiChoiceDialog(Context context, Map<?, String> myMap, String title, Object key, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {

    }

}
