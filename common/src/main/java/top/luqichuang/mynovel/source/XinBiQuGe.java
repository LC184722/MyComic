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
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.NBaseSource;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/11 14:12
 * @ver 1.0
 */
public class XinBiQuGe extends NBaseSource {
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
        String url = "https://www.vbiquge.com/search.php?keyword=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getNovelInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.result-game-item-title-link span");
                String author = node.ownText("p.result-game-item-info-tag span", 1);
                String updateTime = node.ownText("span.result-game-item-info-tag-title", 4);
                String imgUrl = node.src("img");
                String detailUrl = node.href("a");
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.result-item");
    }

    @Override
    public void setNovelDetail(NovelInfo novelInfo, String html) {
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
                novelInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(novelInfo, starter.startElements(html, "div#list dd"));
    }

    @Override
    public ContentInfo getContentInfo(String html, int chapterId) {
        JsoupNode node = new JsoupNode(html);
        String content = node.html("div#content");
        content = SourceHelper.getCommonContent(content, "<br>");
        return new ContentInfo(chapterId, content);
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
    public List<NovelInfo> getRankNovelInfoList(String html) {
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
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        list = starter.startElements(html, "div.l li");
        if (list.isEmpty()) {
            list = starter.startElements(html, "div.novelslist2 li");
        }
        return list;
    }
}
