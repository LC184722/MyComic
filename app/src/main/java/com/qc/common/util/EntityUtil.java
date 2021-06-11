package com.qc.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.luqichuang.common.model.Entity;

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
