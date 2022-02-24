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
 * @date 2021/7/1 21:26
 * @ver 1.0
 */
public class MiKanShu extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.MI_KAN_SHU;
    }

    @Override
    public String getIndex() {
        return "https://www.mikanshu.com";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/search?searchString=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h1 a");
                String author = node.ownText("li", 3);
                String updateTime = node.ownText("li", 4);
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.fed-deta-info");
    }

    @Override
    public void setInfoDetail(NovelInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.fed-part-eone.fed-font-xvi");
                String imgUrl = node.attr("a.fed-list-pics.fed-lazy.fed-part-2by3", "data-original");
                String author = node.ownText("div.fed-part-layout li.fed-col-md6", 1, "a");
                String intro = node.ownText("p.fed-padding.fed-part-both.fed-text-muted");
                String updateStatus = node.ownText("div.fed-part-layout li.fed-col-md6", 0, "a");
                String updateTime = node.ownText("div.fed-part-layout li.fed-col-md6", 2, "a");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.all_data_list ul.fed-part-rows li"));
        SourceHelper.initChapterInfoMap(info, html, "ul li.fed-drop-btns.fed-padding.fed-col-xs4.fed-col-md3", "a", "div.all_data_list", "li");
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.remove("div.box").html("div#bookContent");
        try {
            content = content.replace("<p>", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        content = SourceHelper.getCommonContent(content, "</p>");
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"fed-casc-list fed-part-rows\">\t<dl style=\"white-space:nowrap\">\t\t<dd><a class=\"fed-this fed-text-green\" href=\"/show?orderBy=weeklyCount\">全部</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10001\">玄幻</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10004\">东方玄幻</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10101\">都市</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10002\">异世大陆</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10104\">古代言情</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10103\">现代言情</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10036\">异术超能</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10046\">轻小说</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10028\">总裁豪门</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10024\">穿越时空</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10102\">都市高手</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10129\">游戏</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10100\">仙侠</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10120\">科幻</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10011\">都市生活</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10003\">玄幻奇幻</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10013\">虚拟网游</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10106\">女尊女强</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10339\">穿越奇情</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10112\">重生异能</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10351\">豪门世家</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10074\">游戏异界</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10009\">都市小说</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10022\">都市异能</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10072\">时空穿梭</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10026\">现代修真</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10118\">历史</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10328\">高武世界</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10019\">末世危机</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;mainCategoryId=10322\">衍生同人</a></dd>\t</dl>\t<dl>\t\t<dt>状态</dt>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;status=1\">连载中</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;status=2\">已完结</a></dd>\t</dl>\t<dl>\t\t<dt>字母</dt>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10213\">A</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10206\">B</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10205\">C</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10215\">D</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10219\">E</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10217\">F</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10198\">G</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10212\">H</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10211\">J</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10216\">K</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10200\">L</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10218\">M</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10214\">N</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10362\">O</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10202\">P</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10199\">Q</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10208\">R</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10210\">S</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10201\">T</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10203\">W</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10207\">X</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10209\">Y</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10204\">Z</a></dd>\t</dl></div>";
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
                String title = node.ownText("a.fed-list-title");
                String author = null;
                String updateTime = node.ownText("span.fed-list-desc");
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.fed-list-item");
    }
}
