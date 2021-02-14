package com.qc.common.util;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import the.one.base.model.PopupItem;
import the.one.base.util.QMUIBottomSheetUtil;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mynovel.model.NovelInfo;

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

    public static Map<Integer, String> getMap(List<ComicInfo> comicInfoList) {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (ComicInfo comicInfo : comicInfoList) {
            map.put(comicInfo.getSourceId(), SourceUtil.getSourceName(comicInfo.getSourceId()));
        }
        return map;
    }

    public static Map<Integer, String> getNMap(List<NovelInfo> novelInfoList) {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (NovelInfo novelInfo : novelInfoList) {
            map.put(novelInfo.getNSourceId(), NSourceUtil.getNSourceName(novelInfo.getNSourceId()));
        }
        return map;
    }

    public static void showSimpleBottomSheetList(Context context, Map<?, String> map, Object key, String title, QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        List<PopupItem> list = getPopupItemList(map);
        int index = MapUtil.indexOf(map, key);
        QMUIBottomSheetUtil.showSimpleBottomSheetList(context, list, title, index, listener).show();
    }

}
