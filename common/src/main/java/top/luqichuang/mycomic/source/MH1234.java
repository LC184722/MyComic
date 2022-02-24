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
 * @date 2021/7/28 13:27
 * @ver 1.0
 */
@Deprecated
public class MH1234 extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MH_1234;
    }

    @Override
    public String getIndex() {
        return "http://www.mh1234.com";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format(getIndex() + "/search/?keywords=%s", searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("dl a");
                String author = null;
                String updateTime = node.ownText("dd font");
                String imgUrl = node.src("a.cover img");
                String detailUrl = node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#dmList li");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.title h1");
                String imgUrl = node.src("img.pic");
                String author = node.ownText("div.info p", 1);
                String intro = node.ownText("div#intro1 p");
                String updateStatus = null;
                String updateTime = node.ownText("div.info span");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String prevUrl = "https://img.wszwhg.net/";
        String[] urls = null;
        String chapterImages = StringUtil.match("chapterImages = \\[(.*?)]", html);
        String chapterPath = StringUtil.match("chapterPath = \"(.*?)\"", html);
        try {
            chapterImages = chapterImages.replace("\"", "");
            urls = chapterImages.split(",");
            prevUrl += chapterPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SourceHelper.getContentList(urls, chapterId, prevUrl);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<ul class=\"nav_menu\">\t<li><a href=\"/comic/list/1/\" title=\"热血\">少年热血</a></li>\t<li><a href=\"/comic/list/2/\" title=\"格斗\">武侠格斗</a></li>\t<li><a href=\"/comic/list/3/\" title=\"科幻\">科幻魔幻</a></li>\t<li><a href=\"/comic/list/6/\" title=\"推理\">侦探推理</a></li>\t<li><a href=\"/comic/list/7/\" title=\"恐怖\">恐怖灵异</a></li>\t<li><a href=\"/comic/list/8/\" title=\"耽美\">耽美人生</a></li>\t<li><a href=\"/comic/list/9/\" title=\"少女\">少女爱情</a></li>\t<li><a href=\"/comic/list/10/\" title=\"恋爱\">恋爱生活</a></li>\t<li><a href=\"/comic/list/11/\" title=\"生活\">生活漫画</a></li>\t<li><a href=\"/comic/list/12/\" title=\"战争\">战争漫画</a></li></ul><div class=\"NfcharNav\">\t<a href=\"/comic/l/a.html\">A</a>\t<a href=\"/comic/l/b.html\">B</a>\t<a href=\"/comic/l/c.html\">C</a>\t<a href=\"/comic/l/d.html\">D</a>\t<a href=\"/comic/l/e.html\">E</a>\t<a href=\"/comic/l/f.html\">F</a>\t<a href=\"/comic/l/g.html\">G</a>\t<a href=\"/comic/l/h.html\">H</a>\t<a href=\"/comic/l/i.html\">I</a>\t<a href=\"/comic/l/j.html\">J</a>\t<a href=\"/comic/l/k.html\">K</a>\t<a href=\"/comic/l/l.html\">L</a>\t<a href=\"/comic/l/m.html\">M</a>\t<a href=\"/comic/l/n.html\">N</a>\t<a href=\"/comic/l/o.html\">O</a>\t<a href=\"/comic/l/p.html\">P</a>\t<a href=\"/comic/l/q.html\">Q</a>\t<a href=\"/comic/l/r.html\">R</a>\t<a href=\"/comic/l/s.html\">S</a>\t<a href=\"/comic/l/t.html\">T</a>\t<a href=\"/comic/l/u.html\">U</a>\t<a href=\"/comic/l/v.html\">V</a>\t<a href=\"/comic/l/w.html\">W</a>\t<a href=\"/comic/l/x.html\">X</a>\t<a href=\"/comic/l/y.html\">Y</a>\t<a href=\"/comic/l/z.html\">Z</a></div>";
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
    public List<ComicInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}
