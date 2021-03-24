package com.qc.mycomic.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import top.luqichuang.common.util.DateUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.ImageInfo;
import top.luqichuang.mycomic.model.Source;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/30 9:18
 * @ver 1.0
 */
public class ComicHelper {

    public static void addComicInfo(Comic comic, ComicInfo comicInfo) {
        comic.getComicInfoList().add(comicInfo);
    }

    public static int sourceSize(Comic comic) {
        return comic.getComicInfoList().size();
    }

    public static Source source(Comic comic) {
        return SourceUtil.getSource(comic.getComicInfo().getSourceId());
    }

    public static String sourceName(Comic comic) {
        return source(comic).getSourceName();
    }

    public static void setInfoDetail(Comic comic, String html) {
        source(comic).setComicDetail(comic.getComicInfo(), html);
    }

    public static List<ImageInfo> getImageInfoList(Comic comic, String html, int chapterId, Map<String, Object> map) {
        return source(comic).getImageInfoList(html, chapterId, map);
    }

    public static boolean changeComicInfo(Comic comic) {
        return changeComicInfo(comic, comic.getSourceId());
    }

    public static boolean changeComicInfo(Comic comic, int sourceId) {
        for (ComicInfo info : comic.getComicInfoList()) {
            if (info.getSourceId() == sourceId) {
                comic.setComicInfo(info);
                comic.setSourceId(sourceId);
                return true;
            }
        }
        return false;
    }

    public static String toStringView(Comic comic) {
        if (comic.getComicInfo() != null) {
            return "标题：" + comic.getTitle() +
                    "\n漫画源：" + sourceName(comic) +
                    "\n作者：" + comic.getComicInfo().getAuthor() +
                    "\n上次阅读：" + DateUtil.format(comic.getDate()) +
                    "\n状态：" + comic.getComicInfo().getUpdateStatus() +
                    "\n简介：" + comic.getComicInfo().getIntro();
        } else {
            return "标题：" + comic.getTitle() +
                    "\n漫画源：" + sourceName(comic);
        }
    }

    /*-----------------------------------------------------------------------------------------*/

    public static boolean canLoad(ComicInfo comicInfo, boolean isLoadNext) {
        int id;
        if (isLoadNext) {
            id = comicInfo.getCurChapterId() + 1;
        } else {
            id = comicInfo.getCurChapterId() - 1;
        }
        boolean flag = checkChapterId(comicInfo, id);
        if (flag) {
            comicInfo.setCurChapterId(id);
        }
        return flag;
    }

    public static boolean checkChapterId(ComicInfo comicInfo, int chapterId) {
        return chapterId >= 0 && chapterId < comicInfo.getChapterInfoList().size();
    }

    /**
     * 获得当前章节id的chapterList position
     *
     * @return int
     */
    public static int getPosition(ComicInfo comicInfo) {
        return chapterIdToPosition(comicInfo, comicInfo.getCurChapterId());
    }

    /**
     * 获得指定章节id的chapterList position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int getPosition(ComicInfo comicInfo, int chapterId) {
        return chapterIdToPosition(comicInfo, chapterId);
    }

    /**
     * 设置chapterList章节position
     *
     * @param position position
     * @return void
     */
    public static void setPosition(ComicInfo comicInfo, int position) {
        comicInfo.setCurChapterId(positionToChapterId(comicInfo, position));
        initChapterTitle(comicInfo, position);
    }

    public static void newestChapter(ComicInfo comicInfo) {
        initChapterId(comicInfo, comicInfo.getChapterInfoList().size() - 1);
    }

    public static void initChapterId(ComicInfo comicInfo, int chapterId) {
        comicInfo.setCurChapterId(chapterId);
        initChapterTitle(comicInfo, chapterIdToPosition(comicInfo, chapterId));
    }

    private static void initChapterTitle(ComicInfo comicInfo, int position) {
        if (checkChapterId(comicInfo, position)) {
            comicInfo.setCurChapterTitle(comicInfo.getChapterInfoList().get(position).getTitle());
        }
    }

    /**
     * 章节position 转 comicInfo.getCurChapterId()
     *
     * @param position position
     * @return int
     */
    public static int positionToChapterId(ComicInfo comicInfo, int position) {
        return comicInfo.getChapterInfoList().get(position).getId();
    }

    /**
     * comicInfo.getCurChapterId() 转 章节position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int chapterIdToPosition(ComicInfo comicInfo, int chapterId) {
        int position;
        if (comicInfo.getOrder() == ComicInfo.DESC) {
            position = comicInfo.getChapterInfoList().size() - chapterId - 1;
        } else {
            position = chapterId;
        }
        return position;
    }

    public static int getNextChapterId(ComicInfo comicInfo) {
        return comicInfo.getCurChapterId() + 1;
    }

    public static int getPrevChapterId(ComicInfo comicInfo) {
        return comicInfo.getCurChapterId() - 1;
    }

    /*-----------------------------------------------------------------------------------------*/

    public static String toStringProgress(ImageInfo imageInfo) {
        return String.format(Locale.CHINA, "%d/%d", imageInfo.getCur() + 1, imageInfo.getTotal());
    }

    public static String toStringProgressDetail(ImageInfo imageInfo) {
        return String.format(Locale.CHINA, "%d-%d/%d", imageInfo.getChapterId(), imageInfo.getCur() + 1, imageInfo.getTotal());
    }

}