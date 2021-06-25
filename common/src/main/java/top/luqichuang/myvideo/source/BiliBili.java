package top.luqichuang.myvideo.source;

import com.alibaba.fastjson.JSON;

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
        String url = null;
        Content content = new Content(chapterId);
        content.setUrl(url);
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
