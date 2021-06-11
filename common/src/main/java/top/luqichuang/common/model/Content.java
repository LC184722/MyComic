package top.luqichuang.common.model;

import java.io.Serializable;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 9:15
 * @ver 1.0
 */
public class Content implements Serializable {

    private int id;

    private int chapterId;

    private int cur;

    private int total;

    private String url;

    private String content;

    private int status;

    public Content() {
    }

    public Content(int chapterId, String content) {
        this.chapterId = chapterId;
        this.content = content;
    }

    public Content(int chapterId, int cur, int total, String url) {
        this.chapterId = chapterId;
        this.cur = cur;
        this.total = total;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Content{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", cur=" + cur +
                ", total=" + total +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getCur() {
        return cur;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
