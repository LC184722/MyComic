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
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.mynovel.model.BaseNovelSource;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 23:23
 * @ver 1.0
 */
public class XinBiQuGe extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.XIN_BI_QU_GE;
    }

    @Override
    public String getIndex() {
        return "https://www.vbiquge.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/search.php?keyword=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.result-game-item-title-link span");
                String author = node.ownText("p.result-game-item-info-tag span", 1);
                String updateTime = node.ownText("span.result-game-item-info-tag-title", 4);
                String imgUrl = node.src("img");
                String detailUrl = node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.result-item");
    }

    @Override
    public void setInfoDetail(NovelInfo info, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div#info h1");
                String imgUrl = node.src("div#fmimg img");
                String author = node.ownText("div#info p");
                String intro = node.text("div#intro");
                String updateStatus = node.ownText("div#info p", 1);
                String updateTime = node.ownText("div#info p", 2);
                try {
                    author = author.substring(author.indexOf('：') + 1);
                    updateTime = updateTime.substring(updateTime.indexOf('：') + 1);
                    updateStatus = updateStatus.substring(updateStatus.indexOf('：') + 1).replace(",", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div#list dd"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.html("div#content");
        content = SourceHelper.getCommonContent(content, "<br>");
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "\t\t\t<li><a href=\"/xclass/1/1.html\">玄幻奇幻</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/2/1.html\">武侠仙侠</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/3/1.html\">都市言情</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/4/1.html\">历史军事</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/5/1.html\">科幻灵异</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/6/1.html\">网游竞技</a></li>\n" +
                "\t\t\t<li><a href=\"/xclass/7/1.html\">女频频道</a></li>\n" +
                "\t\t\t<li><a href=\"/quanben/\">完本小说</a></li>\n" +
                "\t\t\t<li><a href=\"/xbqgph.html\">排行榜单</a></li>";
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
        List<NovelInfo> list;
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span.s2 a");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("span.s5");
                String updateTime = node.ownText("span.s3 a");
                String imgUrl = null;
                String detailUrl = getIndex() + node.href("span.s2 a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        list = starter.startElements(html, "div.l li");
        if (list.isEmpty()) {
            list = starter.startElements(html, "div.novelslist2 li");
        }
        return list;
    }
}
