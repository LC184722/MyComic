package com.qc.mycomic.util;

import android.util.Log;

import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;

/**
 * @author LuQiChuang
 * @description 数据库连接工具
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class DBUtil {

    public static final String TAG = "DBUtil";

    /**
     * 保存该漫画信息及所有漫画源信息
     *
     * @param comic 保存漫画
     * @return void
     */
    public static void saveData(Comic comic) {
        saveData(comic, true);
    }

    /**
     * 保存漫画信息
     *
     * @param comic    当前漫画
     * @param needInfo 是否保存所有漫画源信息
     * @return void
     */
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

    /**
     * 保存漫画源信息
     *
     * @param comicInfo 当前漫画源
     * @return void
     */
    public static void saveData(ComicInfo comicInfo) {
        if (comicInfo != null) {
            new Thread(() -> comicInfo.saveOrUpdate("title = ? and sourceId = ?", comicInfo.getTitle(), String.valueOf(comicInfo.getSourceId()))).start();
            Log.i(TAG, "saveComicInfo: " + comicInfo.getTitle() + "->" + SourceUtil.getSourceName(comicInfo.getSourceId()));
        }
    }

    /**
     * 删除漫画
     *
     * @param comic 漫画
     * @return void
     */
    public static void deleteData(Comic comic) {
        if (comic != null) {
            new Thread(comic::delete).start();
        }
    }

    /**
     * 根据status查询漫画
     *
     * @param status 漫画状态,0 - 收藏，1 - 历史，2 - 所有
     * @return List<Comic>
     */
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

    /**
     * 根据漫画标题查找信息表里所有信息
     *
     * @param title 漫画标题
     * @return List<ComicInfo>
     */
    public static List<ComicInfo> findComicInfoListByTitle(String title) {
        return LitePal.where("title = ?", title).find(ComicInfo.class);
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        return LitePal.findAll(clazz);
    }

}
