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
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mynovel.model.ContentInfo;
import top.luqichuang.mynovel.model.NBaseSource;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/12 20:02
 * @ver 1.0
 */
@Deprecated
public class QuanShu extends NBaseSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.QUAN_SHU;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getCharsetName() {
        return "GBK";
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag) {
        if (DETAIL.equals(tag)) {
            JsoupNode node = new JsoupNode(html);
            String url = node.href("div.b-oper a");
            return NetUtil.getRequest(url);
        }
        return super.buildRequest(requestUrl, html, tag);
    }

    @Override
    public String getIndex() {
        return "http://www.quanshuwang.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("http://www.quanshuwang.com/modules/article/search.php?searchkey=%s&searchtype=articlename&searchbuttom.x=0&searchbuttom.y=0", StringUtil.getGBKDecodedStr(searchString));
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getNovelInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span.l a");
                String author = node.ownText("span.l a", 1);
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = node.href("a");
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.seeWell li");
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
                String title = node.ownText("div.chapName strong");
                String imgUrl = null;
                String author = node.ownText("span.r");
                String intro = null;
                String updateStatus = null;
                String updateTime = null;
                try {
                    author = author.replace("作者：", "");
                } catch (Exception ignored) {
                }
                novelInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(novelInfo, starter.startElements(html, "div.dirconone li"));
    }

    @Override
    public ContentInfo getContentInfo(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.remove("script").html("div#content");
        content = SourceHelper.getCommonContent(content);
        return new ContentInfo(chapterId, content);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "<ul class=\"channel-nav-list\">\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/1_1.html\">玄幻魔法</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/2_1.html\">武侠修真</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/3_1.html\">纯爱耽美</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/4_1.html\">都市言情</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/5_1.html\">职场校园</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/6_1.html\">穿越重生</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/7_1.html\">历史军事</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/8_1.html\">网游动漫</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/9_1.html\">恐怖灵异</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/10_1.html\">科幻小说</a></li>\n" +
                "  <li><a href=\"http://www.quanshuwang.com/list/11_1.html\">美文名著</a></li>\n" +
                "        </ul>";
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), node.href("a"));
        }
        return map;
    }

    @Override
    public List<NovelInfo> getRankNovelInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span.l a");
                String author = node.ownText("span.l a", 1);
                String updateTime = node.ownText("span.s3 a");
                String imgUrl = node.src("img");
                String detailUrl = node.href("span.l a");
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.seeWell li");
    }
}