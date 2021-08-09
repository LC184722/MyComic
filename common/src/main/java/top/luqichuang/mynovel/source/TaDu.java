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
 * @date 2021/7/27 21:37
 * @ver 1.0
 */
public class TaDu extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.TA_DU;
    }

    @Override
    public String getIndex() {
        return "http://www.tadu.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search?query=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (DETAIL.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            String url = getIndex() + node.href("div.readBtn a:eq(1)");
            map.put("url", url);
            String imgUrl = node.src("a.bookImg img");
            String intro = node.ownText("div.bookIntro p");
            map.put("imgUrl", imgUrl);
            map.put("intro", intro);
            return NetUtil.getRequest(url);
        } else if (CONTENT.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            String url = node.attr("input#bookPartResourceUrl", "value");
            map.put("url", url);
            return NetUtil.getRequest(url);
        }
        return null;
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.text("a.bookNm");
                String author = node.text("a.authorNm");
                String updateTime = null;
                String imgUrl = node.attr("a.bookImg img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.bookList li");
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
                String title = node.ownText("div.boxCenter.directory h1");
                String imgUrl = (String) map.get("imgUrl");
                String author = node.ownText("div.itct span");
                String intro = (String) map.get("intro");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.chapter a"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String content = null;
        try {
            content = html;
            content = content.replace("callback({content:'", "").replace("'})", "");
            content = content.replace("<p>", "");
            content = SourceHelper.getCommonContent(content, "</p>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"libraryLink\">\t<div class=\"lkLine\">\t\t<a href=\"/store/98-a-0-5-a-20-p-1-98\" class=\"current\">男频</a>\t\t<a href=\"/store/122-a-0-5-a-20-p-1-122\">女频</a>\t\t<a href=\"/store/79-a-0-5-a-20-p-1-79\">出版</a>\t\t<a href=\"/store/270-a-0-5-a-20-p-1-270\">二次元</a>\t</div>\t<div class=\"lkLine classify\">\t\t<div>\t\t\t<a href=\"/store/99-a-0-5-a-20-p-1-98\">东方玄幻</a>\t\t\t<a href=\"/store/103-a-0-5-a-20-p-1-98\">现代都市</a>\t\t\t<a href=\"/store/135-a-0-5-a-20-p-1-98\">脑洞创意</a>\t\t\t<a href=\"/store/108-a-0-5-a-20-p-1-98\">历史架空</a>\t\t\t<a href=\"/store/113-a-0-5-a-20-p-1-98\">军事战争</a>\t\t\t<a href=\"/store/112-a-0-5-a-20-p-1-98\">游戏竞技</a>\t\t\t<a href=\"/store/109-a-0-5-a-20-p-1-98\">武侠仙侠</a>\t\t\t<a href=\"/store/111-a-0-5-a-20-p-1-98\">科幻末世</a>\t\t\t<a href=\"/store/128-a-0-5-a-20-p-1-98\">灵异悬疑</a>\t\t\t<a href=\"/store/107-a-0-5-a-20-p-1-98\">西方奇幻</a>\t\t\t<a href=\"/store/281-a-0-5-a-20-p-1-98\">短篇小说</a>\t\t</div>\t</div>\t<div class=\"lkLine\">\t\t<a href=\"/store/98-t-0-5-a-20-p-1-98\">连载</a>\t\t<a href=\"/store/98-f-0-5-a-20-p-1-98\">完结</a>\t</div>\t<div class=\"lkLine\">\t\t<a href=\"/store/98-a-0-5-f-20-p-1-98\">免费</a>\t\t<a href=\"/store/98-a-0-5-t-20-p-1-98\">VIP</a>\t</div>\t<div class=\"lkLine wordsNm\">\t\t<a href=\"/store/98-a-1-5-a-20-p-1-98\">10万以内</a>\t\t<a href=\"/store/98-a-2-5-a-20-p-1-98\">10万-30万</a>\t\t<a href=\"/store/98-a-3-5-a-20-p-1-98\">30万-50万</a>\t\t<a href=\"/store/98-a-4-5-a-20-p-1-98\">50万-100万</a>\t\t<a href=\"/store/98-a-5-5-a-20-p-1-98\">100万以上</a>\t\t<a href=\"/store/98-a-6-5-a-20-p-1-98\">100万-200万</a>\t\t<a href=\"/store/98-a-7-5-a-20-p-1-98\">200万以上</a>\t</div></div>";
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
        return getInfoList(html);
    }
}
