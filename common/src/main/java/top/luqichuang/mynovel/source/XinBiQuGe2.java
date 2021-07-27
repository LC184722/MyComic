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
 * @date 2021/7/27 9:30
 * @ver 1.0
 */
public class XinBiQuGe2 extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.XIN_BI_QU_GE_2;
    }

    @Override
    public String getIndex() {
        return "https://www.xbiquge.la";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/modules/article/waps.php";
        return NetUtil.postRequest(url, "searchkey", searchString);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("td.even a");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("td.even", 1);
                String updateTime = node.ownText("td.odd", 1);
                String imgUrl = null;
                String detailUrl = node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#main tr");
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
                String title = node.ownText("div#info h1");
                String imgUrl = node.src("div#fmimg img");
                String author = node.ownText("div#info p");
                String intro = node.ownText("div#intro p", 1);
                String updateStatus = null;
                String updateTime = node.ownText("div#info p", 2);
                try {
                    author = author.replace("作 者：", "");
                    updateTime = updateTime.replace("最后更新：", "");
                } catch (Exception ignored) {
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
        String content = node.remove("p").html("div#content");
        content = SourceHelper.getCommonContent(content);
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<ul>\t<li><a href=\"/xuanhuanxiaoshuo/\">玄幻小说</a></li>\t<li><a href=\"/xiuzhenxiaoshuo/\">修真小说</a></li>\t<li><a href=\"/dushixiaoshuo/\">都市小说</a></li>\t<li><a href=\"/chuanyuexiaoshuo/\">穿越小说</a></li>\t<li><a href=\"/wangyouxiaoshuo/\">网游小说</a></li>\t<li><a href=\"/kehuanxiaoshuo/\">科幻小说</a></li></ul>";
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
                String title = node.ownText("span.s2 a");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("span.s5");
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = node.href("span.s2 a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.r li");
    }
}
