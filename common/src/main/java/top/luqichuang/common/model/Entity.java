package top.luqichuang.common.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 18:13
 * @ver 1.0
 */
public abstract class Entity extends LitePalSupport implements Serializable {

    public abstract String getCurChapterTitle();

    public abstract String getUpdateChapter();

    public abstract String getImgUrl();

    public abstract int getInfoId();

    public abstract String getAuthor();

    public abstract int getId();

    public abstract void setId(int id);

    public abstract int getSourceId();

    public abstract void setSourceId(int sourceId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract int getStatus();

    public abstract void setStatus(int status);

    public abstract EntityInfo getInfo();

    public abstract void setInfo(EntityInfo entityInfo);

    public abstract List<? extends EntityInfo> getInfoList();

    public abstract void setInfoList(List<? extends EntityInfo> infoList);

    public abstract int getPriority();

    public abstract void setPriority(int priority);

    public abstract boolean isUpdate();

    public abstract void setUpdate(boolean update);

    public abstract Date getDate();

    public abstract void setDate(Date date);
}
