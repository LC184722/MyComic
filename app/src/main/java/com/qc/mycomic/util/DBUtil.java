package com.qc.mycomic.util;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

public class DBUtil {

    public static final String TAG = "DBUtil";

    public static void saveData(Comic comic) {
        saveData(comic, true);
    }

    public static void saveData(Comic comic, boolean needInfo) {
        if (comic != null) {
            new Thread(() -> {
                comic.saveOrUpdate("title = ?", comic.getTitle());
                Log.i(TAG, "saveComic: " + comic.getTitle() + " p = " + comic.getPriority());
                if (!comic.getComicInfoList().isEmpty() && needInfo) {
                    for (ComicInfo info : comic.getComicInfoList()) {
                        DBUtil.saveData(info);
                    }
                }
            }).start();
        }
    }

    public static void saveData(ComicInfo comicInfo) {
        if (comicInfo != null) {
            new Thread(() -> comicInfo.saveOrUpdate("title = ? and sourceId = ?", comicInfo.getTitle(), String.valueOf(comicInfo.getSourceId()))).start();
            Log.i(TAG, "saveComicInfo: " + comicInfo.getTitle() + "->" + SourceUtil.getSourceName(comicInfo.getSourceId()));
        }
    }

    public static void deleteData(Comic comic) {
        if (comic != null) {
            new Thread(comic::delete).start();
        }
    }

    public static List<Comic> findComicListByStatus(int status) {
        List<Comic> list;
        String order = "priority DESC, date DESC";
        if (status == Codes.STATUS_ALL) {
            list = LitePal.order(order).find(Comic.class);
        } else {
            list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Comic.class);
        }
        List<Comic> dList = new LinkedList<>();
        for (Comic comic : list) {
            List<ComicInfo> infoList = findComicInfoListByTitle(comic.getTitle());
            for (ComicInfo info : infoList) {
                comic.addComicInfo(info);
                if (comic.getSourceId() == info.getSourceId()) {
                    comic.setComicInfo(info);
                }
                //更改detailUrl
//                String url = info.getDetailUrl();
//                String index = SourceUtil.getSource(info.getSourceId()).getIndex();
//                if (!url.startsWith(index)) {
//                    String tmp = url.substring(url.indexOf('/', url.indexOf('.')));
//                    url = index + tmp;
//                    info.setDetailUrl(url);
//                    saveData(info);
//                }
                //end
            }
            if (comic.getComicInfo() == null) {
                if (comic.getComicInfoList().size() == 0) {
                    dList.add(comic);
                } else {
                    comic.setComicInfo(comic.getComicInfoList().get(0));
                }
            }
        }
        if (dList.size() > 0) {
            list.removeAll(dList);
        }
        return list;
    }

    public static List<ComicInfo> findComicInfoListByTitle(String title) {
        return LitePal.where("title = ?", title).find(ComicInfo.class);
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        return LitePal.findAll(clazz);
    }

}
