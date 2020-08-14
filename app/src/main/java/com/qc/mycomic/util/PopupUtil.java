package com.qc.mycomic.util;

import android.content.Context;
import android.view.View;

import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.setting.Setting;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.LinkedList;
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


    public static void main(String[] args) {

//        List<PopupItem> list = SourceUtil.getPopupItemList();
//        int index = SourceUtil.getPopupItemIndex(defaultSourceId);
//        QMUIBottomSheetUtil.showSimpleBottomSheetList(getContext(), list, "选择默认漫画源", index, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
//            @Override
//            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
//                int sourceId = SourceUtil.getSourceId(tag);
//                Setting.setDefaultSourceId(sourceId);
//                v1.setDetailText(tag);
//                dialog.dismiss();
//            }
//        }).show();
    }

    public static void showSimpleBottomSheetList(Context context, MyMap<?, ?> myMap, String title, Object key, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        List<PopupItem> list = getPopupItemList(myMap);
        int index = myMap.indexOf(key);
        QMUIBottomSheetUtil.showSimpleBottomSheetList(context, list, title, index, listener).show();
    }

    public static List<PopupItem> getPopupItemList(MyMap<?, ?> myMap) {
        List<PopupItem> itemList = new LinkedList<>();
        for (Map.Entry<?, ?> entry : myMap.entrySet()) {
            itemList.add(new PopupItem(entry.getValue().toString()));
        }
        return itemList;
    }

}
