package com.qc.mycomic.util;

import android.content.Context;
import android.view.View;

import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.setting.Setting;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import the.one.base.model.PopupItem;
import the.one.base.util.QMUIBottomSheetUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc
 * @date 2020/8/12 21:33
 */
public class PopupUtil {

    public static List<PopupItem> getPopupItemList(MyMap<?, String> myMap) {
        List<PopupItem> itemList = new ArrayList<>();
        for (Map.Entry<?, String> entry : myMap.entrySet()) {
            itemList.add(new PopupItem(entry.getValue()));
        }
        return itemList;
    }

    public static MyMap<Integer, String> getMyMap(List<ComicInfo> comicInfoList) {
        MyMap<Integer, String> myMap = new MyMap<>();
        for (ComicInfo comicInfo : comicInfoList) {
            myMap.put(comicInfo.getSourceId(), SourceUtil.getSourceName(comicInfo.getSourceId()));
        }
        return myMap;
    }

    public static void showSimpleBottomSheetList(Context context, MyMap<?, String> myMap, String title, Object key, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        List<PopupItem> list = getPopupItemList(myMap);
        int index = myMap.indexOf(key);
        QMUIBottomSheetUtil.showSimpleBottomSheetList(context, list, title, index, listener).show();
    }

}
