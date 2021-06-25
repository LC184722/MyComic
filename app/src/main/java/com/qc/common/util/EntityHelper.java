package com.qc.common.util;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.DateUtil;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 22:44
 * @ver 1.0
 */
public class EntityHelper {

    public static Source commonSource(Entity entity) {
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            return SourceUtil.getSource(entity.getSourceId());
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            return SourceUtil.getNSource(entity.getSourceId());
        } else {
            return SourceUtil.getVSource(entity.getSourceId());
        }
    }

    public static void addInfo(Entity entity, EntityInfo entityInfo) {
        ((List<EntityInfo>) entity.getInfoList()).add(entityInfo);
    }

    public static int sourceSize(Entity entity) {
        return entity.getInfoList().size();
    }

    public static String sourceName(Entity entity) {
        return commonSource(entity).getSourceName();
    }

    public static void setInfoDetail(Entity entity, String html, Map<String, Object> map) {
        commonSource(entity).setInfoDetail(entity.getInfo(), html, map);
    }

    public static List<Content> getContentList(Entity entity, String html, int chapterId, Map<String, Object> map) {
        return commonSource(entity).getContentList(html, chapterId, map);
    }

    public static boolean changeInfo(Entity entity, String[] ss) {
        int id = Integer.parseInt(ss[0]);
        int sourceId = Integer.parseInt(ss[1]);
        String author = ss[2];
        for (EntityInfo info : entity.getInfoList()) {
            if (info.getId() == id && info.getSourceId() == sourceId) {
                if (info.getAuthor() == null || Objects.equals(info.getAuthor(), author)) {
                    entity.setSourceId(sourceId);
                    entity.setInfo(info);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean changeInfo(Entity entity, int sourceId) {
        for (EntityInfo info : entity.getInfoList()) {
            if (info.getSourceId() == sourceId) {
                entity.setInfo(info);
                entity.setSourceId(sourceId);
                return true;
            }
        }
        return false;
    }

    public static String toStringView(Entity entity) {
        if (entity.getInfo() != null) {
            return "标题：" + entity.getTitle() +
                    "\n漫画源：" + sourceName(entity) +
                    "\n作者：" + entity.getAuthor() +
                    "\n上次阅读：" + DateUtil.format(entity.getDate()) +
                    "\n状态：" + entity.getInfo().getUpdateStatus() +
                    "\n简介：" + entity.getInfo().getIntro();
        } else {
            return "标题：" + entity.getTitle() +
                    "\n漫画源：" + sourceName(entity);
        }
    }

    /*-----------------------------------------------------------------------------------------*/

    public static boolean canLoad(EntityInfo entityInfo, boolean isLoadNext) {
        int id;
        if (isLoadNext) {
            id = entityInfo.getCurChapterId() + 1;
        } else {
            id = entityInfo.getCurChapterId() - 1;
        }
        boolean flag = checkChapterId(entityInfo, id);
        if (flag) {
            entityInfo.setCurChapterId(id);
        }
        return flag;
    }

    public static boolean checkChapterId(EntityInfo entityInfo, int chapterId) {
        return chapterId >= 0 && chapterId < entityInfo.getChapterInfoList().size();
    }

    /**
     * 获得当前章节id的chapterList position
     *
     * @return int
     */
    public static int getPosition(EntityInfo entityInfo) {
        return chapterIdToPosition(entityInfo, entityInfo.getCurChapterId());
    }

    /**
     * 获得指定章节id的chapterList position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int getPosition(EntityInfo entityInfo, int chapterId) {
        return chapterIdToPosition(entityInfo, chapterId);
    }

    /**
     * 设置chapterList章节position
     *
     * @param position position
     * @return void
     */
    public static void setPosition(EntityInfo entityInfo, int position) {
        entityInfo.setCurChapterId(positionToChapterId(entityInfo, position));
        initChapterTitle(entityInfo, position);
    }

    public static void newestChapter(EntityInfo entityInfo) {
        initChapterId(entityInfo, entityInfo.getChapterInfoList().size() - 1);
    }

    public static void initChapterId(EntityInfo entityInfo, int chapterId) {
        entityInfo.setCurChapterId(chapterId);
        initChapterTitle(entityInfo, chapterIdToPosition(entityInfo, chapterId));
    }

    private static void initChapterTitle(EntityInfo entityInfo, int position) {
        if (checkChapterId(entityInfo, position)) {
            entityInfo.setCurChapterTitle(entityInfo.getChapterInfoList().get(position).getTitle());
        }
    }

    /**
     * 章节position 转 entityInfo.getCurChapterId()
     *
     * @param position position
     * @return int
     */
    public static int positionToChapterId(EntityInfo entityInfo, int position) {
        return entityInfo.getChapterInfoList().get(position).getId();
    }

    /**
     * entityInfo.getCurChapterId() 转 章节position
     *
     * @param chapterId chapterId
     * @return int
     */
    public static int chapterIdToPosition(EntityInfo entityInfo, int chapterId) {
        int position;
        if (entityInfo.getOrder() == EntityInfo.DESC) {
            position = entityInfo.getChapterInfoList().size() - chapterId - 1;
        } else {
            position = chapterId;
        }
        return position;
    }

    public static int getNextChapterId(EntityInfo entityInfo) {
        return entityInfo.getCurChapterId() + 1;
    }

    public static int getPrevChapterId(EntityInfo entityInfo) {
        return entityInfo.getCurChapterId() - 1;
    }

    /*-----------------------------------------------------------------------------------------*/

    public static String toStringProgress(Content content) {
        return String.format(Locale.CHINA, "%d/%d", content.getCur() + 1, content.getTotal());
    }

    public static String toStringProgressDetail(Content content) {
        return String.format(Locale.CHINA, "%d-%d/%d", content.getChapterId(), content.getCur() + 1, content.getTotal());
    }
}
