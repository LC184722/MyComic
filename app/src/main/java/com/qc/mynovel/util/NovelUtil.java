package com.qc.mynovel.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.luqichuang.mynovel.model.Novel;

/**
 * @author LuQiChuang
 * @desc 小说信息工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NovelUtil {

    public static final int STATUS_HIS = 0;
    public static final int STATUS_FAV = 1;
    public static final int STATUS_ALL = 2;

    private static List<Novel> hisNovelList;
    private static List<Novel> favNovelList;
    private static List<Novel> novelList;

    public static List<Novel> initNovelList(int status) {
        novelList = null;
        return getNovelList(status);
    }

    public static List<Novel> getNovelList(int status) {
        if (novelList == null) {
            novelList = DBUtil.findNovelListByStatus(STATUS_ALL);
            hisNovelList = new ArrayList<>();
            favNovelList = new ArrayList<>();
            for (Novel novel : novelList) {
                if (novel.getStatus() == STATUS_HIS) {
                    hisNovelList.add(novel);
                }
                if (novel.getStatus() == STATUS_FAV) {
                    favNovelList.add(novel);
                }
            }
        }
        if (status == STATUS_HIS) {
            return hisNovelList;
        } else if (status == STATUS_FAV) {
            return favNovelList;
        } else {
            return novelList;
        }
    }

    public static List<Novel> getHisNovelList() {
        return getNovelList(STATUS_HIS);
    }

    public static List<Novel> getFavNovelList() {
        return getNovelList(STATUS_FAV);
    }

    public static List<Novel> getNovelList() {
        return getNovelList(STATUS_ALL);
    }

    public static void removeNovel(Novel novel) {
        List<Novel> list = getNovelList(novel.getStatus());
        list.remove(novel);
    }

    /**
     * 将novel置于链表第一个
     *
     * @param novel novel
     * @return void
     */
    public static void first(Novel novel) {
        List<Novel> list = getNovelList(novel.getStatus());
        list.remove(novel);
        novel.setDate(new Date());
        if (novel.isUpdate()) {
            list.add(0, novel);
        } else {
            if (list.isEmpty() || list.get(list.size() - 1).getPriority() != 0) {
                list.add(novel);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPriority() == 0) {
                        list.add(i, novel);
                        break;
                    }
                }
            }
        }
    }
}
