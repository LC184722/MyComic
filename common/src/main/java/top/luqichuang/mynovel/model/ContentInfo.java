package top.luqichuang.mynovel.model;

import java.io.Serializable;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ContentInfo implements Serializable {

    private int id;

    private int chapterId;

    private String content;

    private int status;

    public ContentInfo() {
    }

    public ContentInfo(int chapterId, String content) {
        this.chapterId = chapterId;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContentInfo{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", content='" + content + '\'' +
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
