package top.luqichuang.myvideo.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
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
 * @date 2021/6/24 22:33
 * @ver 1.0
 */
@Deprecated
public class YingHua2 extends BaseVideoSource {

    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.YING_HUA_2;
    }

    @Override
    public String getIndex() {
        return "http://www.yhdm.so";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search/%s/";
        return NetUtil.getRequest(String.format(url, searchString));
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
                String updateStatus = null;
                String updateTime = node.remove("div.sinfo span label").text("div.sinfo span");
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
        String url = node.attr("div.bofang div", "data-vid");
        if (url != null) {
            url = url.split("\\$")[0];
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"area box\"><div class=\"box1 l\"><label>?????????/??????</label>\n" +
                "<ul><li><a href=\"/japan/\">??????</a></li><li><a href=\"/china/\">??????</a></li><li><a href=\"/american/\">??????</a></li><li><a href=\"/england/\">??????</a></li><li><a href=\"/2021/\">2021</a></li><li><a href=\"/2020/\">2020</a></li><li><a href=\"/2019/\">2019</a></li><li><a href=\"/2018/\">2018</a></li></ul></div><div class=\"box2 l\"><label>?????????</label><ul><li><a href=\"/66/\">??????</a></li><li><a href=\"/64/\">??????</a></li><li><a href=\"/91/\">??????</a></li><li><a href=\"/70/\">??????</a></li><li><a href=\"/67/\">??????</a></li><li><a href=\"/111/\">LOLI</a></li><li><a href=\"/83/\">??????</a></li><li><a href=\"/81/\">??????</a></li><li><a href=\"/75/\">??????</a></li><li><a href=\"/74/\">??????</a></li><li><a href=\"/84/\">??????</a></li><li><a href=\"/73/\">??????</a></li><li><a href=\"/72/\">?????????</a></li><li><a href=\"/102/\">??????</a></li><li><a href=\"/61/\">??????</a></li><li><a href=\"/69/\">??????</a></li><li><a href=\"/62/\">??????</a></li><li><a href=\"/103/\">??????</a></li><li><a href=\"/63/\">??????</a></li><li><a href=\"/95/\">??????</a></li><li><a href=\"/85/\">??????</a></li><li><a href=\"/77/\">??????</a></li><li><a href=\"/79/\">??????</a></li><li><a href=\"/99/\">??????</a></li><li><a href=\"/80/\">??????</a></li><li><a href=\"/119/\">?????????</a></li></ul></div>\n" +
                "<div class=\"box3 r\"><label>?????????</label><ul><li><a href=\"/29/\">??????</a></li><li><a href=\"/30/\">??????</a></li><li><a href=\"/31/\">??????</a></li><li><a href=\"/32/\">??????</a></li><li><a href=\"/33/\">??????</a></li><li><a href=\"/34/\">??????</a></li></ul></div>\n" +
                "</div>";
        Map<String, String> map = new LinkedHashMap<>();
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), getIndex() + node.href("a"));
        }
        return map;
    }

    @Override
    public List<VideoInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}