package top.luqichuang.common.model;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 19:22
 * @ver 1.0
 */
public abstract class EntityInfo extends LitePalSupport implements Serializable {

    public static final int DESC = 0;

    public static final int ASC = 1;

    public abstract int getId();

    public abstract void setId(int id);

    public abstract int getSourceId();

    public abstract void setSourceId(int sourceId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getAuthor();

    public abstract void setAuthor(String author);

    public abstract String getDetailUrl();

    public abstract void setDetailUrl(String detailUrl);

    public abstract String getImgUrl();

    public abstract void setImgUrl(String imgUrl);

    public abstract String getLocalImgUrl();

    public abstract void setLocalImgUrl(String localImgUrl);

    public abstract String getUpdateTime();

    public abstract void setUpdateTime(String updateTime);

    public abstract String getUpdateChapter();

    public abstract void setUpdateChapter(String updateChapter);

    public abstract String getUpdateStatus();

    public abstract void setUpdateStatus(String updateStatus);

    public abstract String getCurChapterTitle();

    public abstract void setCurChapterTitle(String curChapterTitle);

    public abstract String getIntro();

    public abstract void setIntro(String intro);

    public abstract int getCurChapterId();

    public abstract void setCurChapterId(int curChapterId);

    public abstract int getChapterNum();

    public abstract void setChapterNum(int chapterNum);

    public abstract int getOrder();

    public abstract void setOrder(int order);

    public abstract List<ChapterInfo> getChapterInfoList();

    public abstract void setChapterInfoList(List<ChapterInfo> chapterInfoList);
}
