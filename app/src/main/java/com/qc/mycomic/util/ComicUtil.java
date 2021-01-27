package com.qc.mycomic.util;

import java.util.ArrayList;
import java.util.List;

import top.luqichuang.common.mycomic.model.Comic;

/**
 * @author LuQiChuang
 * @desc 漫画信息工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ComicUtil {

    public static final int STATUS_HIS = 0;
    public static final int STATUS_FAV = 1;
    public static final int STATUS_ALL = 2;

    private static List<Comic> hisComicList;
    private static List<Comic> favComicList;
    private static List<Comic> comicList;

    public static List<Comic> initComicList(int status) {
        comicList = null;
        return getComicList(status);
    }

    public static List<Comic> getComicList(int status) {
        if (comicList == null) {
            comicList = DBUtil.findComicListByStatus(STATUS_ALL);
            hisComicList = new ArrayList<>();
            favComicList = new ArrayList<>();
            for (Comic comic : comicList) {
                if (comic.getStatus() == STATUS_HIS) {
                    hisComicList.add(comic);
                }
                if (comic.getStatus() == STATUS_FAV) {
                    favComicList.add(comic);
                }
            }
        }
        if (status == STATUS_HIS) {
            return hisComicList;
        } else if (status == STATUS_FAV) {
            return favComicList;
        } else {
            return comicList;
        }
    }

    public static List<Comic> getHisComicList() {
        return getComicList(STATUS_HIS);
    }

    public static List<Comic> getFavComicList() {
        return getComicList(STATUS_FAV);
    }

    public static List<Comic> getComicList() {
        return getComicList(STATUS_ALL);
    }

    public static void removeComic(Comic comic) {
        List<Comic> list = getComicList(comic.getStatus());
        list.remove(comic);
    }

    /**
     * 将comic置于链表第一个
     *
     * @param comic comic
     * @return void
     */
    public static void first(Comic comic) {
        List<Comic> list = getComicList(comic.getStatus());
        list.remove(comic);
        if (comic.isUpdate()) {
            list.add(0, comic);
        } else {
            if (list.isEmpty() || list.get(list.size() - 1).getPriority() != 0) {
                list.add(comic);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPriority() == 0) {
                        list.add(i, comic);
                        break;
                    }
                }
            }
        }
    }
}
