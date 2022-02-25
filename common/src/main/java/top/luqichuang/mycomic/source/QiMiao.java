package top.luqichuang.mycomic.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2022/2/25 10:41
 * @ver 1.0
 */
public class QiMiao extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.QI_MIAO;
    }

    @Override
    public String getIndex() {
        return "http://www.qimiaomh.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/search.html?keyword=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.classification");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.title");
                String imgUrl = node.src("div.ctdbLeft img");
                String author = node.ownText("p.author");
                String intro = node.ownText("p#worksDesc");
                String updateStatus = node.ownText("a.status h2");
                String updateTime = node.ownText("span.date");
                try {
                    updateTime = updateTime.replace("更新：", "");
                } catch (Exception ignored) {
                }
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul.comic-content-c"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        String zImg = StringUtil.match("z_img='\\[(.*?)\\]", html);
        if (zImg != null) {
            zImg = zImg.replace("\"", "");
            zImg = zImg.replace("lyffny", "yzrbhb");
            urls = zImg.split(",");
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"mainBody_nav\">\t<ul class=\"clearfix\" id=\"meau_nav\">\t\t<li><a href=\"/list-1-7-----updatetime--1.html\">热血</a></li>\t\t<li><a href=\"/list-1-8-----updatetime--1.html\">恋爱</a></li>\t\t<li><a href=\"/list-1-9-----updatetime--1.html\">青春</a></li>\t\t<li><a href=\"/list-1-10-----updatetime--1.html\">彩虹</a></li>\t\t<li><a href=\"/list-1-11-----updatetime--1.html\">冒险</a></li>\t\t<li><a href=\"/list-1-12-----updatetime--1.html\">后宫</a></li>\t\t<li><a href=\"/list-1-13-----updatetime--1.html\">悬疑</a></li>\t\t<li><a href=\"/list-1-14-----updatetime--1.html\">玄幻</a></li>\t\t<li><a href=\"/list-1-16-----updatetime--1.html\">穿越</a></li>\t\t<li><a href=\"/list-1-17-----updatetime--1.html\">都市</a></li>\t\t<li><a href=\"/list-1-18-----updatetime--1.html\">腹黑</a></li>\t\t<li><a href=\"/list-1-19-----updatetime--1.html\">爆笑</a></li>\t\t<li><a href=\"/list-1-20-----updatetime--1.html\">少年</a></li>\t\t<li><a href=\"/list-1-21-----updatetime--1.html\">奇幻</a></li>\t\t<li><a href=\"/list-1-22-----updatetime--1.html\">古风</a></li>\t\t<li><a href=\"/list-1-23-----updatetime--1.html\">妖恋</a></li>\t\t<li><a href=\"/list-1-24-----updatetime--1.html\">元气</a></li>\t\t<li><a href=\"/list-1-25-----updatetime--1.html\">治愈</a></li>\t\t<li><a href=\"/list-1-26-----updatetime--1.html\">励志</a></li>\t\t<li><a href=\"/list-1-27-----updatetime--1.html\">日常</a></li>\t</ul>\t<ul class=\"clearfix\" id=\"area_nav\">\t\t<li><a href=\"/list-1-1-----updatetime--1.html\">国漫</a></li>\t\t<li><a href=\"/list-1-2-----updatetime--1.html\">日漫</a></li>\t\t<li><a href=\"/list-1-3-----updatetime--1.html\">韩漫</a></li>\t</ul>\t<ul class=\"clearfix\" id=\"status_nav\">\t\t<li><a href=\"/list-1------updatetime-1-1.html\">连载</a></li>\t\t<li><a href=\"/list-1------updatetime-0-1.html\">完结</a></li>\t</ul></div>";
        Map<String, String> map = new LinkedHashMap<>();
        map.put("最近更新", "http://www.qimiaomh.com/list-1------updatetime--1.html");
        map.put("排行榜", "http://www.qimiaomh.com/list-1------hits--1.html");
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), getIndex() + node.href("a"));
        }
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}
