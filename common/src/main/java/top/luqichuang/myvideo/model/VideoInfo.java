package top.luqichuang.myvideo.model;

import java.util.ArrayList;
import java.util.List;

import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.EntityInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/15 14:24
 * @ver 1.0
 */
public class VideoInfo extends EntityInfo {

    private int id;

    private int sourceId;

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

    private int curChapterId;

    private int chapterNum;

    private int order;

    private List<ChapterInfo> chapterInfoList = new ArrayList<>();

    public VideoInfo() {
    }

    public VideoInfo(int sourceId, String title, String author, String detailUrl, String imgUrl, String updateTime) {
        this.sourceId = sourceId;
        this.title = title;
        this.author = author;
        this.detailUrl = detailUrl;
        this.imgUrl = imgUrl;
        this.updateTime = updateTime;
    }

    public void setDetail(String title, String imgUrl, String author, String updateTime, String updateStatus, String intro) {
        if (this.title == null || this.title.trim().equals("")) {
            this.title = title;
        }
        if (this.imgUrl == null || this.imgUrl.trim().equals("")) {
            this.imgUrl = imgUrl;
        }
        this.author = author;
        this.updateTime = updateTime;
        this.updateStatus = updateStatus;
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", chapterInfoList=" + chapterInfoList.size() +
                '}';
    }

    @Override
    public String toStringDetail() {
        return "VideoInfo{" +
                "id=" + id +
                ", sourceId=" + sourceId +
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
                ", curChapterId=" + curChapterId +
                ", chapterNum=" + chapterNum +
                ", order=" + order +
                '}';
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
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getDetailUrl() {
        return detailUrl;
    }

    @Override
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String getLocalImgUrl() {
        return localImgUrl;
    }

    @Override
    public void setLocalImgUrl(String localImgUrl) {
        this.localImgUrl = localImgUrl;
    }

    @Override
    public String getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getUpdateChapter() {
        return updateChapter;
    }

    @Override
    public void setUpdateChapter(String updateChapter) {
        this.updateChapter = updateChapter;
    }

    @Override
    public String getUpdateStatus() {
        return updateStatus;
    }

    @Override
    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    @Override
    public String getCurChapterTitle() {
        return curChapterTitle;
    }

    @Override
    public void setCurChapterTitle(String curChapterTitle) {
        this.curChapterTitle = curChapterTitle;
    }

    @Override
    public String getIntro() {
        return intro;
    }

    @Override
    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public int getCurChapterId() {
        return curChapterId;
    }

    @Override
    public void setCurChapterId(int curChapterId) {
        this.curChapterId = curChapterId;
    }

    @Override
    public int getChapterNum() {
        return chapterNum;
    }

    @Override
    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public List<ChapterInfo> getChapterInfoList() {
        return chapterInfoList;
    }

    @Override
    public void setChapterInfoList(List<ChapterInfo> chapterInfoList) {
        this.chapterInfoList = chapterInfoList;
    }
}
