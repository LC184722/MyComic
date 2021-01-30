package top.luqichuang.common.mycomic.model;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class ChapterInfo {

    private int id;

    private String title;

    private String chapterUrl;

    private int status;

    public ChapterInfo() {
    }

    public ChapterInfo(int id, String title, String chapterUrl) {
        this.id = id;
        this.title = title;
        this.chapterUrl = chapterUrl;
    }

    public ChapterInfo(String title, String chapterUrl) {
        this.title = title;
        this.chapterUrl = chapterUrl;
    }

    @Override
    public String toString() {
        return "ChapterInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", chapterUrl='" + chapterUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
