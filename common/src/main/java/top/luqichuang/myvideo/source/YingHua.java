package top.luqichuang.myvideo.source;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/15 14:25
 * @ver 1.0
 */
public class YingHua extends BaseVideoSource {

    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.YING_HUA;
    }

    @Override
    public String getIndex() {
        return "http://www.imomoe.la";
    }

    @Override
    public String getCharsetName(String tag) {
        return "gb2312";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        return NetUtil.postRequest(getIndex() + "/search.asp", "searchword", searchString);
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h2 a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.pics li");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {

    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        return null;
    }

    @Override
    public Map<String, String> getRankMap() {
        return null;
    }

    @Override
    public List<VideoInfo> getRankInfoList(String html) {
        return null;
    }
}
