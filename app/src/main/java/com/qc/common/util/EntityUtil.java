package com.qc.common.util;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 18:51
 * @ver 1.0
 */
public class EntityUtil {

    public static final int STATUS_HIS = 0;
    public static final int STATUS_FAV = 1;
    public static final int STATUS_ALL = 2;

    private static List<Entity> hisEntityList;
    private static List<Entity> favEntityList;
    private static List<Entity> entityList;

    public static List<Entity> initEntityList(int status) {
        entityList = null;
        SourceUtil.init();
        List<Source> sourceList;
        Collection<Integer> ids;
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            sourceList = (List) SourceUtil.getSourceList();
            ids = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.COMIC_SOURCE_OPEN);
            Collection<Integer> totalIds = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.COMIC_SOURCE_TOTAL);
            Collection<Integer> allIds = new HashSet<>(SourceEnum.getMAP().keySet());
            allIds.removeAll(totalIds);
            if (!allIds.isEmpty()) {
                ids.addAll(allIds);
                SettingUtil.putSetting(SettingEnum.COMIC_SOURCE_OPEN, ids);
            }
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            sourceList = (List) SourceUtil.getNSourceList();
            ids = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.NOVEL_SOURCE_OPEN);
            Collection<Integer> totalIds = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.NOVEL_SOURCE_TOTAL);
            Collection<Integer> allIds = new HashSet<>(NSourceEnum.getMAP().keySet());
            allIds.removeAll(totalIds);
            if (!allIds.isEmpty()) {
                ids.addAll(allIds);
                SettingUtil.putSetting(SettingEnum.NOVEL_SOURCE_OPEN, ids);
            }
        } else {
            sourceList = (List) SourceUtil.getVSourceList();
            ids = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.VIDEO_SOURCE_OPEN);
            Collection<Integer> totalIds = (Collection<Integer>) SettingUtil.getSettingKey(SettingEnum.VIDEO_SOURCE_TOTAL);
            Collection<Integer> allIds = new HashSet<>(VSourceEnum.getMAP().keySet());
            allIds.removeAll(totalIds);
            if (!allIds.isEmpty()) {
                ids.addAll(allIds);
                SettingUtil.putSetting(SettingEnum.VIDEO_SOURCE_OPEN, ids);
            }
        }
        Iterator<Source> iterator = sourceList.iterator();
        while (iterator.hasNext()) {
            int n = iterator.next().getSourceId();
            if (!ids.contains(n)) {
                iterator.remove();
            }
        }
        return getEntityList(status);
    }

    public static List<Entity> getEntityList(int status) {
        if (entityList == null) {
            entityList = (List<Entity>) DBUtil.findListByStatus(STATUS_ALL);
            hisEntityList = new ArrayList<>();
            favEntityList = new ArrayList<>();
            for (Entity entity : entityList) {
                if (entity.getStatus() == STATUS_HIS) {
                    hisEntityList.add(entity);
                }
                if (entity.getStatus() == STATUS_FAV) {
                    favEntityList.add(entity);
                }
            }
        }
        if (status == STATUS_HIS) {
            return hisEntityList;
        } else if (status == STATUS_FAV) {
            return favEntityList;
        } else {
            return entityList;
        }
    }

    public static List<Entity> getHisEntityList() {
        return getEntityList(STATUS_HIS);
    }

    public static List<Entity> getFavEntityList() {
        return getEntityList(STATUS_FAV);
    }

    public static List<Entity> getEntityList() {
        return getEntityList(STATUS_ALL);
    }

    public static void removeEntity(Entity entity) {
        List<Entity> list = getEntityList(entity.getStatus());
        list.remove(entity);
    }

    /**
     * 将comic置于链表第一个
     *
     * @param entity entity
     * @return void
     */
    public static void first(Entity entity) {
        List<Entity> list = getEntityList(entity.getStatus());
        list.remove(entity);
        entity.setDate(new Date());
        if (entity.isUpdate()) {
            list.add(0, entity);
        } else {
            if (list.isEmpty() || list.get(list.size() - 1).getPriority() != 0) {
                list.add(entity);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPriority() == 0) {
                        list.add(i, entity);
                        break;
                    }
                }
            }
        }
        DBUtil.save(entity, DBUtil.SAVE_CUR);
    }

}
