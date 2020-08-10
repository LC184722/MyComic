package com.qc.mycomic.util;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.MyMap;
import com.qc.mycomic.model.Source;
import com.qc.mycomic.source.ManHuaFen;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.source.PuFei;
import com.qc.mycomic.source.TengXun;

import java.util.LinkedList;
import java.util.List;

import the.one.base.model.PopupItem;

public class SourceUtil {

    private static MyMap<Integer, Source> map = new MyMap<>();

    private static List<Source> sourceList;

    static {
        map.put(Codes.MI_TUI, new MiTui());
        map.put(Codes.MAN_HUA_FEN, new ManHuaFen());
        map.put(Codes.PU_FEI, new PuFei());
        map.put(Codes.TENG_XUN, new TengXun());
        sourceList = map.getValueList();
    }

    public static Source getSource(int sourceId) {
        return map.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source source = getSource(sourceId);
        return source != null ? source.getSourceName() : null;
    }

    public static List<PopupItem> getPopupItemList(List<ComicInfo> comicInfoList) {
        List<PopupItem> list = new LinkedList<>();
        for (ComicInfo info : comicInfoList) {
            list.add(new PopupItem(getSourceName(info.getSourceId())));
        }
        return list;
    }

    public static int getPopupItemIndex(Comic comic) {
        int i = 0;
        for (ComicInfo comicInfo : comic.getComicInfoList()) {
            if (comicInfo.getSourceId() == comic.getSourceId()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int getSourceId(String sourceName) {
        if (sourceName != null) {
            for (Source source : sourceList) {
                if (sourceName.equals(source.getSourceName())) {
                    return source.getSourceId();
                }
            }
        }
        return -1;
    }

    public static int getSize() {
        return sourceList.size();
    }

    public static List<Source> getSourceList() {
        return sourceList;
    }
}
