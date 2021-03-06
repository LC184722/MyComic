package top.luqichuang.mynovel.model;

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
public class Novel extends Entity {

    private int id;

    private int nSourceId;

    private String title;

    private int status;

    private String author;

    private NovelInfo novelInfo;

    private List<NovelInfo> novelInfoList = new ArrayList<>();

    private int priority;

    private boolean isUpdate;

    private Date date;

    public Novel() {
    }

    public Novel(NovelInfo novelInfo) {
        this.nSourceId = novelInfo.getNSourceId();
        this.title = novelInfo.getTitle();
        this.novelInfo = novelInfo;
        this.novelInfoList.add(novelInfo);
    }

    @Override
    public String toString() {
        return "Novel{" +
                "id=" + id +
                ", nSourceId=" + nSourceId +
                ", novelInfo=" + novelInfo +
                ", title='" + title + '\'' +
                ", novelInfoList=" + novelInfoList +
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
        Novel novel = (Novel) o;
        return Objects.equals(title, novel.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getSourceId() {
        return nSourceId;
    }

    @Override
    public void setSourceId(int sourceId) {
        this.nSourceId = sourceId;
    }

    public int getNSourceId() {
        return nSourceId;
    }

    public void setNSourceId(int nSourceId) {
        this.nSourceId = nSourceId;
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
        return novelInfo;
    }

    @Override
    public void setInfo(EntityInfo entityInfo) {
        this.novelInfo = (NovelInfo) entityInfo;
    }

    @Override
    public List<? extends EntityInfo> getInfoList() {
        return this.novelInfoList;
    }

    @Override
    public void setInfoList(List<? extends EntityInfo> infoList) {
        this.novelInfoList = (List<NovelInfo>) infoList;
    }

    @Override
    public String getCurChapterTitle() {
        return novelInfo.getCurChapterTitle();
    }

    @Override
    public String getUpdateChapter() {
        return novelInfo.getUpdateChapter();
    }

    @Override
    public String getImgUrl() {
        return novelInfo.getImgUrl();
    }

    @Override
    public int getInfoId() {
        return novelInfo.getId();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public NovelInfo getNovelInfo() {
        return novelInfo;
    }

    public void setNovelInfo(NovelInfo novelInfo) {
        this.novelInfo = novelInfo;
    }

    public List<NovelInfo> getNovelInfoList() {
        return novelInfoList;
    }

    public void setNovelInfoList(List<NovelInfo> novelInfoList) {
        this.novelInfoList = novelInfoList;
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
