package com.qc.mycomic.model;

import android.util.Log;

import com.qc.mycomic.util.Codes;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ComicInfo extends LitePalSupport {

    private int id;

    private int sourceId;

    private int curChapterId;

    private int chapterNum;

    private int order;

    private String title;

    private String author;

    private String detailUrl;

    private String imgUrl;

    private String localImgUrl;

    private String updateTime;

    private String updateChapter;

    private String updateStatus;

    private String curChapterTitle;

    private String intro;

    private List<ChapterInfo> chapterInfoList = new ArrayList<>();

    public ComicInfo() {

    }

    public ComicInfo(int sourceId, String title, String author, String detailUrl, String imgUrl, String updateTime) {
        this.sourceId = sourceId;
        this.title = title;
        this.author = author;
        this.detailUrl = detailUrl;
        this.imgUrl = imgUrl;
        this.updateTime = updateTime;
    }


    public void setDetail(String author, String updateTime, String updateStatus, String intro) {
        this.author = author;
        this.updateTime = updateTime;
        this.updateStatus = updateStatus;
        this.intro = intro;
    }

    public boolean canLoad(boolean isLoadNext) {
        int id;
        if (isLoadNext) {
            id = curChapterId + 1;
        } else {
            id = curChapterId - 1;
        }
        boolean flag = checkChapterId(id);
        if (flag) {
            curChapterId = id;
        }
        return flag;
    }

    public boolean checkChapterId(int chapterId) {
        return chapterId >= 0 && chapterId < chapterInfoList.size();
    }

    /**
     * 获得当前章节id的chapterList position
     *
     * @return int
     */
    public int getPosition() {
        return chapterIdToPosition(curChapterId);
    }

    /**
     * 获得指定章节id的chapterList position
     *
     * @param chapterId chapterId
     * @return int
     */
    public int getPosition(int chapterId) {
        return chapterIdToPosition(chapterId);
    }

    /**
     * 设置chapterList章节position
     *
     * @param position position
     * @return void
     */
    public void setPosition(int position) {
        curChapterId = positionToChapterId(position);
        initChapterTitle(position);
    }

    public void newestChapter() {
        initChapterId(chapterInfoList.size() - 1);
    }

    public void initChapterId(int chapterId) {
        this.curChapterId = chapterId;
        initChapterTitle(chapterIdToPosition(chapterId));
    }

    private void initChapterTitle(int position) {
        if (checkChapterId(position)) {
            curChapterTitle = chapterInfoList.get(position).getTitle();
        }
    }

    /**
     * 章节position 转 curChapterId
     *
     * @param position position
     * @return int
     */
    public int positionToChapterId(int position) {
        return chapterInfoList.get(position).getId();
    }

    /**
     * curChapterId 转 章节position
     *
     * @param chapterId chapterId
     * @return int
     */
    public int chapterIdToPosition(int chapterId) {
        int position;
        if (order == Codes.DESC) {
            position = chapterInfoList.size() - chapterId - 1;
        } else {
            position = chapterId;
        }
        return position;
    }

    /**
     * 获得当前的chapterInfo
     *
     * @return ChapterInfo
     */
    public ChapterInfo getChapterInfo() {
        return chapterInfoList.get(getPosition());
    }

    public int getNextChapterId() {
        return curChapterId + 1;
    }

    public int getPrevChapterId() {
        return curChapterId - 1;
    }

    @Override
    public String toString() {
        return "ComicInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", chapterInfoList=" + chapterInfoList.size() +
                '}';
    }

    public String toStringDetail() {
        return "ComicInfo{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", curChapterId=" + curChapterId +
                ", chapterNum=" + chapterNum +
                ", order=" + order +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", localImgUrl='" + localImgUrl + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateChapter='" + updateChapter + '\'' +
                ", updateStatus='" + updateStatus + '\'' +
                ", curChapterTitle='" + curChapterTitle + '\'' +
                ", intro='" + intro + '\'' +
                ", chapterInfoList=" + chapterInfoList +
                '}';
    }

    public void initChapterInfoList(List<ChapterInfo> list) {
        initChapterInfoList(list, Codes.DESC);
    }

    public void initChapterInfoList(List<ChapterInfo> list, int order) {
        this.chapterInfoList = list;
        if (!list.isEmpty()) {
            if (order == Codes.DESC) {
                this.updateChapter = list.get(0).getTitle();
            } else if (order == Codes.ASC) {
                this.updateChapter = list.get(list.size() - 1).getTitle();
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComicInfo info = (ComicInfo) o;
        if (sourceId != info.sourceId) return false;
        return Objects.equals(title, info.title);
    }

    @Override
    public int hashCode() {
        int result = sourceId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
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

    public int getCurChapterId() {
        return curChapterId;
    }

    public void setCurChapterId(int curChapterId) {
        this.curChapterId = curChapterId;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLocalImgUrl() {
        return localImgUrl;
    }

    public void setLocalImgUrl(String localImgUrl) {
        this.localImgUrl = localImgUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateChapter() {
        return updateChapter;
    }

    public void setUpdateChapter(String updateChapter) {
        this.updateChapter = updateChapter;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getCurChapterTitle() {
        return curChapterTitle;
    }

    public void setCurChapterTitle(String curChapterTitle) {
        this.curChapterTitle = curChapterTitle;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<ChapterInfo> getChapterInfoList() {
        return chapterInfoList;
    }

    public void setChapterInfoList(List<ChapterInfo> chapterInfoList) {
        this.chapterInfoList = chapterInfoList;
    }
}
