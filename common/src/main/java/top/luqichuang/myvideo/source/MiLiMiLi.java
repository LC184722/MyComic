package top.luqichuang.myvideo.source;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/22 23:06
 * @ver 1.0
 */
public class MiLiMiLi extends BaseVideoSource {

    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.MILI_MILI;
    }

    @Override
    public String getIndex() {
        return "http://www.milimili.cc";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/e/search/index.php";
        return NetUtil.postRequest(url, "show", "title,ftitle", "tbname", "movie", "tempid", "1", "keyboard", searchString);
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            String url = node.href("link");
            map.put("url", url);
            return NetUtil.getRequest(url);
        }
        return null;
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("h2 a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.lpic li");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.rate.r h1");
                String imgUrl = node.src("div.thumb.l img");
                String author = null;
                String intro = node.ownText("div.info");
                String updateStatus = node.ownText("div.sinfo span:eq(2)");
                String updateTime = node.ownText("div.sinfo span:eq(1)");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.movurl li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String url = node.src("iframe");
        try {
            url = url.split("\\?url=", 2)[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
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
