package top.luqichuang.common.model;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 14:54
 * @ver 1.0
 */
public interface Source<T extends EntityInfo> {

    String SEARCH = "search";
    String DETAIL = "detail";
    String CONTENT = "content";
    String RANK = "rank";

    int getSourceId();

    String getSourceName();

    String getIndex();

    default boolean isValid() {
        return true;
    }

    default String getCharsetName(String tag) {
        return "UTF-8";
    }

    Request getSearchRequest(String searchString);

    default Request getDetailRequest(String detailUrl) {
        return NetUtil.getRequest(detailUrl);
    }

    default Request getContentRequest(String imageUrl) {
        return NetUtil.getRequest(imageUrl);
    }

    default Request getRankRequest(String rankUrl) {
        return NetUtil.getRequest(rankUrl);
    }

    default Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        return null;
    }

    List<T> getInfoList(String html);

    void setInfoDetail(T info, String html, Map<String, Object> map);

    List<Content> getContentList(String html, int chapterId, Map<String, Object> map);

    Map<String, String> getRankMap();

    List<T> getRankInfoList(String html);

}
