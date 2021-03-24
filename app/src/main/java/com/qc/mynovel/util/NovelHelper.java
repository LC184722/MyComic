package com.qc.mynovel.util;

import top.luqichuang.common.util.DateUtil;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.NSource;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/30 9:18
 * @ver 1.0
 */
public class NovelHelper {

    public static void addNovelInfo(Novel novel, NovelInfo novelInfo) {
        novel.getNovelInfoList().add(novelInfo);
    }

    public static int nSourceSize(Novel novel) {
        return novel.getNovelInfoList().size();
    }

    public static NSource nSource(Novel novel) {
        return NSourceUtil.getNSource(novel.getNovelInfo().getNSourceId());
    }

    public static String nSourceName(Novel novel) {
        return nSource(novel).getNSourceName();
    }

    public static void setInfoDetail(Novel novel, String html) {
        nSource(novel).setNovelDetail(novel.getNovelInfo(), html);
    }

    public static ContentInfo getContentInfo(Novel novel, String html, int chapterId) {
        return nSource(novel).getContentInfo(html, chapterId, null);
    }

    public static boolean changeNovelInfo(Novel novel, int nSourceId) {
        return changeNovelInfo(novel, nSourceId, null);
    }

    public static boolean changeNovelInfo(Novel novel, int nSourceId, String author) {
        for (NovelInfo info : novel.getNovelInfoList()) {
            if (info.getNSourceId() == nSourceId) {
                if (author != null) {
                    if (author.equals(info.getAuthor())) {
                        novel.setAuthor(author);
                        novel.setNovelInfo(info);
                        novel.setNSourceId(nSourceId);
                        return true;
                    }
                } else {
                    novel.setNovelInfo(info);
                    novel.setNSourceId(nSourceId);
                    return true;
                }
            }
        }
        return false;
    }

    public static String toStringView(Novel novel) {
        if (novel.getNovelInfo() != null) {
            return "标题：" + novel.getTitle() +
                    "\n小说源：" + nSourceName(novel) +
                    "\n作者：" + novel.getNovelInfo().getAuthor() +
                    "\n上次阅读：" + DateUtil.format(novel.getDate()) +
                    "\n状态：" + novel.getNovelInfo().getUpdateStatus() +
                    "\n简介：" + novel.getNovelInfo().getIntro();
        } else {
            return "标题：" + novel.getTitle() +
                    "\n小说源：" + nSourceName(novel);
        }
    }

    /*-----------------------------------------------------------------------------------------*/

    public static boolean canLoad(NovelInfo novelInfo, boolean isLoadNext) {
        int id;
        if (isLoadNext) {
            id = novelInfo.getCurChapterId() + 1;
        } else {
            id = novelInfo.getCurChapterId() - 1;
        }
        boolean flag = checkChapterId(novelInfo, id);
        if (flag) {
            initChapterId(novelInfo, id);
        }
        return flag;
    }

    public static boolean checkChapterId(NovelInfo novelInfo, int chapterId) {
        return chapterId >= 0 && chapterId < novelInfo.getChapterInfoList().size();
    }

    /**
     * 获得当前章节id的chapterList position
     *
     * @return int
     */
    public static int getPosition(NovelInfo novelInfo) {
        return chapterIdToPosition(novelInfo, novelInfo.getCurChapterId());
    }

    /**
     * 获得指定章节id的chapterList position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int getPosition(NovelInfo novelInfo, int chapterId) {
        return chapterIdToPosition(novelInfo, chapterId);
    }

    /**
     * 设置chapterList章节position
     *
     * @param position position
     * @return void
     */
    public static void setPosition(NovelInfo novelInfo, int position) {
        novelInfo.setCurChapterId(positionToChapterId(novelInfo, position));
        initChapterTitle(novelInfo, position);
    }

    public static void newestChapter(NovelInfo novelInfo) {
        initChapterId(novelInfo, novelInfo.getChapterInfoList().size() - 1);
    }

    public static void initChapterId(NovelInfo novelInfo, int chapterId) {
        novelInfo.setCurChapterId(chapterId);
        initChapterTitle(novelInfo, chapterIdToPosition(novelInfo, chapterId));
    }

    private static void initChapterTitle(NovelInfo novelInfo, int position) {
        if (checkChapterId(novelInfo, position)) {
            novelInfo.setCurChapterTitle(novelInfo.getChapterInfoList().get(position).getTitle());
        }
    }

    /**
     * 章节position 转 novelInfo.getCurChapterId()
     *
     * @param position position
     * @return int
     */
    public static int positionToChapterId(NovelInfo novelInfo, int position) {
        return novelInfo.getChapterInfoList().get(position).getId();
    }

    /**
     * novelInfo.getCurChapterId() 转 章节position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int chapterIdToPosition(NovelInfo novelInfo, int chapterId) {
        int position;
        if (novelInfo.getOrder() == NovelInfo.DESC) {
            position = novelInfo.getChapterInfoList().size() - chapterId - 1;
        } else {
            position = chapterId;
        }
        return position;
    }

    public static int getNextChapterId(NovelInfo novelInfo) {
        return novelInfo.getCurChapterId() + 1;
    }

    public static int getPrevChapterId(NovelInfo novelInfo) {
        return novelInfo.getCurChapterId() - 1;
    }

}