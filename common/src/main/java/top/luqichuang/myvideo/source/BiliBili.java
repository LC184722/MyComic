package top.luqichuang.myvideo.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.json.JsonStarter;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/24 22:57
 * @ver 1.0
 */
public class BiliBili extends BaseVideoSource {
    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.BILI_BILI;
    }

    @Override
    public String getIndex() {
        return "https://www.bilibili.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://search.bilibili.com/bangumi?keyword=%s";
        return NetUtil.getRequest(String.format(url, searchString));
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            String json = StringUtil.match("__INITIAL_STATE__=(.*?);", html);
            JsonNode node = new JsonNode(json);
            JSONArray array = node.jsonArray("epList");
            Integer chapterId = (Integer) data.get("chapterId");
            if (chapterId == null) {
                chapterId = 0;
            }
            if (array != null && !array.isEmpty()) {
                node.init((JSONObject) array.get(chapterId));
                String api = "https://api.bilibili.com/x/player/playurl?otype=json&fnver=0&fnval=2&player=1&qn=64&bvid=%s&cid=%s";
                String bvid = node.string("bvid");
                String cid = node.string("cid");
                String referer = "https://www.bilibili.com/bangumi/play/ep" + node.string("id");
                String url = String.format(api, bvid, cid);
                map.put("referer", referer);
                return NetUtil.getRequest(url);
            }
        }
        return null;
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String author = null;
                String updateTime = node.text("div.left-block", 1, "span.value");
                String imgUrl = null;
                String detailUrl = "https:" + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#bangumi-list li.bangumi-item");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.media-right a");
                String imgUrl = node.src("div#media_module img");
                String author = null;
                String intro = node.ownText("span.absolute");
                String updateStatus = node.ownText("span.pub-info");
                String updateTime = node.remove("div.sinfo span label").text("div.sinfo span");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }
        };
        starter.startInfo(html);

        JsonStarter<ChapterInfo> jsonStarter = new JsonStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected ChapterInfo dealDataList(JsonNode node, int dataId) {
                String title = node.string("titleFormat");
                String chapterUrl = String.format("https://www.bilibili.com/bangumi/play/ep%s", node.string("id"));
                return new ChapterInfo(dataId, title, chapterUrl);
            }
        };
        String jsonArr = StringUtil.match("\"epList\":(.*?),\"epInfo\"", html);
        SourceHelper.initChapterInfoList(info, jsonStarter.startDataList(JSON.parseArray(jsonArr)));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsonNode node = new JsonNode(html);
        node.init(node.string("data"));
        JSONArray array = node.jsonArray("durl");
        String url = null;
        if (array != null && !array.isEmpty()) {
            node.init((JSONObject) array.get(0));
            url = node.string("url");
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        String[] headers = {"Referer:" + map.get("referer")};
        content.getMap().put("headers", headers);
        return SourceHelper.getContentList(content);
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
