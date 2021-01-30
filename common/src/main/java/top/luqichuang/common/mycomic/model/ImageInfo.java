package top.luqichuang.common.mycomic.model;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ImageInfo {

    private int id;

    private int chapterId;

    private int cur;

    private int total;

    private String url;

    private int status;

    public ImageInfo() {
    }

    public ImageInfo(int chapterId, int cur, int total, String url) {
        this.chapterId = chapterId;
        this.cur = cur;
        this.total = total;
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", cur=" + cur +
                ", total=" + total +
                ", url='" + url + '\'' +
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
