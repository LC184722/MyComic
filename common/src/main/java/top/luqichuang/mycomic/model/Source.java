package top.luqichuang.mycomic.model;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc asd
 * @date 2020/8/12 16:23
 * @ver 1.0
 */
public interface Source {

    String SEARCH = "search";
    String DETAIL = "detail";
    String IMAGE = "image";
    String RANK = "rank";

    /**
     * 获得漫画源ID
     *
     * @return int
     */
    int getSourceId();

    /**
     * 获得漫画源名称
     *
     * @return String
     */
    String getSourceName();

    /**
     * 获得漫画源主页
     *
     * @return String
     */
    String getIndex();

    /**
     * 漫画源是否有效
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

    //获得搜索漫画request

    /**
     * 获得搜索漫画request
     *
     * @param searchString searchString
     * @return Request
     */
    Request getSearchRequest(String searchString);

    /**
     * 获得漫画详情request
     *
     * @param detailUrl detailUrl
     * @return Request
     */
    Request getDetailRequest(String detailUrl);

    /**
     * 获得漫画阅读页request
     *
     * @param imageUrl imageUrl
     * @return Request
     */
    Request getImageRequest(String imageUrl);

    /**
     * 用于发送二次Request
     *
     * @param requestUrl requestUrl
     * @param html       html
     * @param tag        表明阶段
     * @param map
     * @return Request
     */
    Request buildRequest(String requestUrl, String html, String tag, Map<String, Object> map);


    /**
     * 获得漫画排行榜request
     *
     * @param rankUrl rankUrl
     * @return Request
     */
    Request getRankRequest(String rankUrl);

    /**
     * 获得漫画信息链表
     *
     * @param html html
     * @return List<ComicInfo>
     */
    List<ComicInfo> getComicInfoList(String html);

    /**
     * 设置漫画详情
     *
     * @param comicInfo comicInfo
     * @param html      html
     * @return void
     */
    void setComicDetail(ComicInfo comicInfo, String html);

    /**
     * 获得漫画图片信息链表
     *
     * @param html      html
     * @param chapterId chapterId
     * @param map       map
     * @return List<ImageInfo>
     */
    List<ImageInfo> getImageInfoList(String html, int chapterId, Map<String, Object> map);

    /**
     * 获得排行榜、分类等链接
     *
     * @return MyMap<String, String>
     */
    Map<String, String> getRankMap();

    /**
     * 获得排行榜的漫画信息链表
     *
     * @param html html
     * @return List<ComicInfo>
     */
    List<ComicInfo> getRankComicInfoList(String html);
}
