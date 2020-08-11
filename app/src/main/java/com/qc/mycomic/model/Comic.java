package com.qc.mycomic.model;

import com.qc.mycomic.util.DateUtil;
import com.qc.mycomic.util.SourceUtil;

import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Comic extends LitePalSupport {

    private int id;

    private int sourceId;

    private ComicInfo comicInfo;

    private String title;

    private List<ComicInfo> comicInfoList = new ArrayList<>();

    private int status;

    private int priority;

    private boolean isUpdate;

    private Date date;

    public Comic() {
    }

    public Comic(ComicInfo comicInfo) {
        this.sourceId = comicInfo.getSourceId();
        this.title = comicInfo.getTitle();
        this.comicInfo = comicInfo;
        addComicInfo(comicInfo);
    }

    public void addComicInfo(ComicInfo comicInfo) {
        comicInfoList.add(comicInfo);
    }

    public int getSourceSize() {
        return comicInfoList.size();
    }

    public Source getSource() {
        return SourceUtil.getSource(comicInfo.getSourceId());
    }

    public String getSourceName() {
        return getSource().getSourceName();
    }

    public void setInfoDetail(String html) {
        getSource().setComicDetail(comicInfo, html);
    }

    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        return getSource().getImageInfoList(html, chapterId);
    }

    public boolean changeComicInfo() {
        boolean flag = false;
        for (ComicInfo info : comicInfoList) {
            if (info.getSourceId() == sourceId) {
                comicInfo = info;
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean changeComicInfo(int sourceId) {
        boolean flag = false;
        for (ComicInfo info : comicInfoList) {
            if (info.getSourceId() == sourceId) {
                this.comicInfo = info;
                this.sourceId = sourceId;
                flag = true;
                break;
            }
        }
        return flag;
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

    public String toStringView() {
        if (getComicInfo() != null) {
            return "标题：" + title +
                    "\n漫画源：" + getSourceName() +
                    "\n作者：" + getComicInfo().getAuthor() +
                    "\n上次阅读：" + DateUtil.format(getDate()) +
                    "\n状态：" + getComicInfo().getUpdateStatus() +
                    "\n简介：" + getComicInfo().getIntro();
        } else {
            return "标题：" + title +
                    "\n漫画源：" + getSourceName();
        }
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

    public ComicInfo getComicInfo() {
        return comicInfo;
    }

    public void setComicInfo(ComicInfo comicInfo) {
        this.comicInfo = comicInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ComicInfo> getComicInfoList() {
        return comicInfoList;
    }

    public void setComicInfoList(List<ComicInfo> comicInfoList) {
        this.comicInfoList = comicInfoList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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