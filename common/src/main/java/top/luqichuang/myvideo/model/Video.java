package top.luqichuang.myvideo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/15 14:23
 * @ver 1.0
 */
public class Video extends Entity {

    private int id;

    private int sourceId;

    private String title;

    private int status;

    private VideoInfo info;

    private List<VideoInfo> infoList = new ArrayList<>();

    private int priority;

    private boolean isUpdate;

    private Date date;

    public Video() {
    }

    public Video(VideoInfo info) {
        this.sourceId = info.getSourceId();
        this.title = info.getTitle();
        this.info = info;
        this.infoList.add(info);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", info=" + info +
                ", title='" + title + '\'' +
                ", infoList=" + infoList +
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
        Video video = (Video) o;
        return Objects.equals(title, video.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

    @Override
    public String getCurChapterTitle() {
        return info.getCurChapterTitle();
    }

    @Override
    public String getUpdateChapter() {
        return info.getUpdateChapter();
    }

    @Override
    public String getImgUrl() {
        return info.getImgUrl();
    }

    @Override
    public int getInfoId() {
        return info.getId();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getSourceId() {
        return sourceId;
    }

    @Override
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public void setAuthor(String author) {
    }

    @Override
    public VideoInfo getInfo() {
        return info;
    }

    @Override
    public void setInfo(EntityInfo entityInfo) {
        this.info = (VideoInfo) entityInfo;
    }

    @Override
    public List<VideoInfo> getInfoList() {
        return infoList;
    }

    @Override
    public void setInfoList(List<? extends EntityInfo> infoList) {
        this.infoList = (List<VideoInfo>) infoList;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isUpdate() {
        return isUpdate;
    }

    @Override
    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
}
