package top.luqichuang.mynovel.model;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc asd
 * @date 2020/8/12 16:23
 * @ver 1.0
 */
public interface NSource {

    String SEARCH = "search";
    String DETAIL = "detail";
    String CONTENT = "content";
    String RANK = "rank";

    /**
     * 获得小说源ID
     *
     * @return int
     */
    int getNSourceId();

    /**
     * 获得小说源名称
     *
     * @return String
     */
    String getNSourceName();

    /**
     * 获得小说源主页
     *
     * @return String
     */
    String getIndex();

    /**
     * 小说源是否有效
     *
     * @return boolean
     */
    boolean isValid();

    /**
     * 网页编码格式
     *
     * @return String
     */
    String getCharsetName();

    /**
     * 获得搜索小说request
     *
     * @param searchString searchString
     * @return Request
     */
    Request getSearchRequest(String searchString);

    /**
     * 获得小说详情request
     *
     * @param detailUrl detailUrl
     * @return Request
     */
    Request getDetailRequest(String detailUrl);

    /**
     * 获得小说阅读页request
     *
     * @param imageUrl imageUrl
     * @return Request
     */
    Request getContentRequest(String imageUrl);

    /**
     * 用于发送二次Request
     *
     * @param requestUrl requestUrl
     * @param html       html
     * @param tag        表明阶段
     * @return Request
     */
    Request buildRequest(String requestUrl, String html, String tag);


    /**
     * 获得小说排行榜request
     *
     * @param rankUrl rankUrl
     * @return Request
     */
    Request getRankRequest(String rankUrl);

    /**
     * 获得小说信息链表
     *
     * @param html html
     * @return List<NovelInfo>
     */
    List<NovelInfo> getNovelInfoList(String html);

    /**
     * 设置小说详情
     *
     * @param novelInfo novelInfo
     * @param html      html
     * @return void
     */
    void setNovelDetail(NovelInfo novelInfo, String html);

    /**
     * 获得小说图片信息链表
     *
     * @param html      html
     * @param chapterId chapterId
     * @return List<ImageInfo>
     */
    ContentInfo getContentInfo(String html, int chapterId);

    /**
     * 获得排行榜、分类等链接
     *
     * @return MyMap<String, String>
     */
    Map<String, String> getRankMap();

    /**
     * 获得排行榜的小说信息链表
     *
     * @param html html
     * @return List<NovelInfo>
     */
    List<NovelInfo> getRankNovelInfoList(String html);
}
