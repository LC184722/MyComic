package top.luqichuang.mynovel.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.DecryptUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.mynovel.model.BaseNovelSource;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/7/27 15:54
 * @ver 1.0
 */
public class ShuBen extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.SHU_BEN;
    }

    @Override
    public String getIndex() {
        return "http://www.jiaotongshigu.org";
    }

    @Override
    public String getCharsetName(String tag) {
        return "GBK";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/modules/article/search.php?searchkey=" + DecryptUtil.getGBKEncodeStr(searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("td a");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("td#booklast a");
                String updateTime = node.ownText("td", 3, "p");
                String imgUrl = null;
                String detailUrl = node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.pcon tr");
    }

    @Override
    public void setInfoDetail(NovelInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.text-center");
                String imgUrl = getIndex() + node.src("div.info1 img");
                String author = node.ownText("h3.text-center a");
                String intro = node.ownText("div#intro p", 1);
                String updateStatus = null;
                String updateTime = null;
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.panel-body li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.html("div.panel-body.content-body.content-ext");
        content = SourceHelper.getCommonContent(content);
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<ul class=\"nav navbar-nav\">\t<li class=\"navitem\" nav=\"cat_1\"><a href=\"/list/1-1.html\">玄幻</a></li>\t<li class=\"navitem\" nav=\"cat_2\"><a href=\"/list/2-1.html\">仙侠</a></li>\t<li class=\"navitem\" nav=\"cat_3\"><a href=\"/list/3-1.html\">言情</a></li>\t<li class=\"navitem\" nav=\"cat_4\"><a href=\"/list/4-1.html\">历史</a></li>\t<li class=\"navitem\" nav=\"cat_5\"><a href=\"/list/5-1.html\">网游</a></li>\t<li class=\"navitem\" nav=\"cat_6\"><a href=\"/list/6-1.html\">科幻</a></li>\t<li class=\"navitem\" nav=\"cat_7\"><a href=\"/list/7-1.html\">恐怖</a></li>\t<li class=\"navitem\" nav=\"cat_8\"><a href=\"/list/8-1.html\">其他</a></li></ul>";
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
    public List<NovelInfo> getRankInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h4 a");
                String author = node.ownText("div.book_author a");
                String updateTime = null;
                String imgUrl = node.src("a img");
                String detailUrl = node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.panel-body div.media");
    }
}
