package com.qc.common.util;

import android.content.Context;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import the.one.base.model.PopupItem;
import the.one.base.util.QMUIBottomSheetUtil;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @version 1.0
 * @desc
 * @date 2020/8/12 21:33
 */
public class PopupUtil {

    public static List<PopupItem> getPopupItemList(Map<?, String> map) {
        List<PopupItem> itemList = new ArrayList<>();
        for (String value : map.values()) {
            itemList.add(new PopupItem(value));
        }
        return itemList;
    }

    public static Map<Integer, String> getMap(List<? extends EntityInfo> infoList) {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (EntityInfo info : infoList) {
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                map.put(info.getSourceId(), SourceUtil.getSourceName(info.getSourceId()));
            } else {
                map.put(info.getSourceId(), SourceUtil.getNSourceName(info.getSourceId()) + '-' + info.getAuthor());
            }
        }
        return map;
    }

    public static void showSimpleBottomSheetList(Context context, Map<?, String> map, Object key, String title, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        List<PopupItem> list = getPopupItemList(map);
        int index = MapUtil.indexOf(map, key);
        QMUIBottomSheetUtil.showSimpleBottomSheetList(context, list, title, index, listener).show();
    }

}
