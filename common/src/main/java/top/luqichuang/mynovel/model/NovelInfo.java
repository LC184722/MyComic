package top.luqichuang.mynovel.model;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import top.luqichuang.common.model.ChapterInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NovelInfo extends LitePalSupport {

    public static final int DESC = 0;

    public static final int ASC = 1;

    private int id;

    private int nSourceId;

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

    public NovelInfo() {
    }

    public NovelInfo(int nSourceId, String title, String author, String detailUrl, String imgUrl, String updateTime) {
        this.nSourceId = nSourceId;
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
                ", nSourceId=" + nSourceId +
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
                ", chapterInfoList=" + chapterInfoList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NovelInfo info = (NovelInfo) o;
        if (nSourceId != info.nSourceId) return false;
        if (!Objects.equals(title, info.title)) return false;
        return Objects.equals(author, info.author);
    }

    @Override
    public int hashCode() {
        int result = nSourceId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<ChapterInfo> getChapterInfoList() {
        return chapterInfoList;
    }

    public void setChapterInfoList(List<ChapterInfo> chapterInfoList) {
        this.chapterInfoList = chapterInfoList;
    }
}
