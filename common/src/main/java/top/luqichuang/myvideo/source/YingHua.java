package top.luqichuang.myvideo.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.DecryptUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
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
        return "http://www.imomoe.live";
    }

    @Override
    public String getCharsetName(String tag) {
        return "gb2312";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search.asp?page=1&searchword=%s&searchtype=-1";
        return NetUtil.getRequest(String.format(url, DecryptUtil.getGBKEncodeStr(searchString)));
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            for (Element element : node.getElements("script")) {
                node.init(element);
                String src = node.src("script");
                if (src.contains("playdata")) {
                    String url = getIndex() + src;
                    map.put("url", url);
                    return NetUtil.getRequest(url);
                }
            }
        }
        return null;
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
        return starter.startElements(html, "div.fire.l div.pics li");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("span.names");
                String imgUrl = node.src("div.tpic.l img");
                String author = null;
                String intro = node.ownText("div.info");
                String updateStatus = null;
                String updateTime = node.ownText("div.alex span:eq(3) a");
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
        SourceHelper.initChapterInfoMap(info, html, "div.tabs li:eq(0),div.movurl ul", false);
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        List<String> list = new ArrayList<>();
        String[] ss = html.split(",");
        for (String s : ss) {
            String t = StringUtil.match("\\$(.*?)\\$", s);
            if (t != null) {
                list.add(t);
            }
        }
        String url = null;
        try {
            url = list.get(chapterId);
        } catch (Exception e) {
            if (!list.isEmpty()) {
                url = list.get(0);
            }
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"area box\">\n" +
                "<div class=\"box1 l\">\n" +
                "<label>?????????/??????</label>\n" +
                "<ul>\n" +
                "<li><a href=\"/so.asp?page=1&amp;dq=%C8%D5%B1%BE&amp;pl=time\">??????</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;dq=%B4%F3%C2%BD&amp;pl=time\">??????</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;dq=%C3%C0%B9%FA&amp;pl=time\">??????</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;dq=%D3%A2%B9%FA&amp;pl=time\">??????</a></li>\n" +
                "</ul>\n" +
                "<ul>\n" +
                "<li><a href=\"/so.asp?page=1&amp;nf=2016&amp;pl=time\">2016</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;nf=2015&amp;pl=time\">2015</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;nf=2014&amp;pl=time\">2014</a></li>\n" +
                "<li><a href=\"/so.asp?page=1&amp;nf=2013&amp;pl=time\">2013</a></li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "<div class=\"box2 l\">\n" +
                "<label>?????????</label>\n" +
                "<ul>\n" +
                "<li><a href=\"/search.asp?searchword=%C8%C8%D1%AA\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%B8%F1%B6%B7\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C1%B5%B0%AE\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%D0%A3%D4%B0\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%B8%E3%D0%A6\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=LOLI\">LOLI</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C9%F1%C4%A7\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BB%FA%D5%BD\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BF%C6%BB%C3\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%D5%E6%C8%CB\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C7%E0%B4%BA\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C4%A7%B7%A8\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C3%C0%C9%D9%C5%AE\">?????????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C9%F1%BB%B0\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C3%B0%CF%D5\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%D4%CB%B6%AF\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BE%BA%BC%BC\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%CD%AF%BB%B0\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C7%D7%D7%D3\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BD%CC%D3%FD\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C0%F8%D6%BE\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BE%E7%C7%E9\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%C9%E7%BB%E1\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%BA%F3%B9%AC\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%D5%BD%D5%F9\">??????</a></li>\n" +
                "<li><a href=\"/search.asp?searchword=%CE%FC%D1%AA%B9%ED\">?????????</a></li>\n" +
                "</ul>\n" +
                "</div>\n" +
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
