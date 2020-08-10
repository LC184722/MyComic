package com.qc.mycomic.model;

public class ChapterInfo {

    private int id;

    private String title;

    private String chapterUrl;

    private int status;

    public ChapterInfo(int id, String title, String chapterUrl) {
        this.id = id;
        this.title = title;
        this.chapterUrl = chapterUrl;
    }

    public ChapterInfo(String title, String chapterUrl) {
        this(title, chapterUrl, 0);
    }

    public ChapterInfo(String title, String chapterUrl, Integer status) {
        this.title = title;
        this.chapterUrl = chapterUrl;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ChapterInfo{" +
                "title='" + title + '\'' +
                ", chapterUrl='" + chapterUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
