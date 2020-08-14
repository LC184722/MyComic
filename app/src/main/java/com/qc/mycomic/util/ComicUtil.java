package com.qc.mycomic.util;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
            hisComicList = new LinkedList<>();
            favComicList = new LinkedList<>();
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

    public static void first(Comic comic) {
        List<Comic> list = getComicList(comic.getStatus());
        if (comic.isUpdate()) {
            list.remove(comic);
            list.add(0, comic);
        } else {
            if (list.size() == 0) {
                list.add(comic);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPriority() == 0) {
                        list.remove(comic);
                        if (i == 0) {
                            list.add(i, comic);
                        } else {
                            list.add(i - 1, comic);
                        }
                        break;
                    }
                }
            }
        }
    }

//    public static void sortList(int status) {
//        List<Comic> list = getComicList(status);
//        Comparator<Comic> comparator = new Comparator<Comic>() {
//            @Override
//            public int compare(Comic o1, Comic o2) {
//                long time1 = -1;
//                long time2 = -1;
//                if (o1.getDate() != null) {
//                    time1 = o1.getDate().getTime();
//                }
//                if (o2.getDate() != null) {
//                    time2 = o2.getDate().getTime();
//                }
//                if (o1.isUpdate() != o2.isUpdate()) {
//                    return !o1.isUpdate() ? 1 : -1;
//                } else if (time1 != time2) {
//                    Log.i("TAG", "compare1: " + o1.getTitle() + "  " + time1);
//                    Log.i("TAG", "compare2: " + o2.getTitle() + "  " + time2);
//                    Log.i("TAG", "compare: result = " + (time1 < time2 ? 1 : -1));
//                    return time1 < time2 ? 1 : -1;
//                } else {
//                    return 0;
//                }
//            }
//        };
//        sort(list, comparator);
//    }

    private static <T> void sort(List<T> list, Comparator<T> c) {
        Object[] a = list.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<T> i = list.listIterator();
        for (Object e : a) {
            i.next();
            i.set((T) e);
        }
    }


    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId) {
        return getImageInfoList(urls, chapterId, "");
    }

    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId, String urlPrev) {
        List<ImageInfo> list = new LinkedList<>();
        if (urls != null) {
            int i = 0;
            for (String url : urls) {
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, urlPrev + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

}
