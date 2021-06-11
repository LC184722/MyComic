package top.luqichuang.mycomic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class Comic extends Entity {

    private int id;

    private int sourceId;

    private String title;

    private int status;

    private ComicInfo comicInfo;

    private List<ComicInfo> comicInfoList = new ArrayList<>();

    private int priority;

    private boolean isUpdate;

    private Date date;

    public Comic() {
    }

    public Comic(ComicInfo comicInfo) {
        this.sourceId = comicInfo.getSourceId();
        this.title = comicInfo.getTitle();
        this.comicInfo = comicInfo;
        this.comicInfoList.add(comicInfo);
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", comicInfo=" + comicInfo +
                ", title='" + title + '\'' +
                ", comicInfoList=" + comicInfoList.size() +
                ", status=" + status +
                ", priority=" + priority +
                ", isUpdate=" + isUpdate +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return Objects.equals(title, comic.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String getCurChapterTitle() {
        return comicInfo.getCurChapterTitle();
    }

    @Override
    public String getUpdateChapter() {
        return comicInfo.getUpdateChapter();
    }

    @Override
    public String getImgUrl() {
        return comicInfo.getImgUrl();
    }

    @Override
    public int getInfoId() {
        return comicInfo.getId();
    }

    @Override
    public String getAuthor() {
        return comicInfo.getAuthor();
    }

    @Override
    public void setAuthor(String author) {
        comicInfo.setAuthor(author);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public EntityInfo getInfo() {
        return comicInfo;
    }

    @Override
    public void setInfo(EntityInfo entityInfo) {
        this.comicInfo = (ComicInfo) entityInfo;
    }

    @Override
    public List<? extends EntityInfo> getInfoList() {
        return comicInfoList;
    }

    @Override
    public void setInfoList(List<? extends EntityInfo> infoList) {
        this.comicInfoList = (List<ComicInfo>) infoList;
    }

    public ComicInfo getComicInfo() {
        return comicInfo;
    }

    public void setComicInfo(ComicInfo comicInfo) {
        this.comicInfo = comicInfo;
    }

    public List<ComicInfo> getComicInfoList() {
        return comicInfoList;
    }

    public void setComicInfoList(List<ComicInfo> comicInfoList) {
        this.comicInfoList = comicInfoList;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
