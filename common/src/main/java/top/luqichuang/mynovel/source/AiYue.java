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
 * @date 2021/2/15 19:31
 * @ver 1.0
 */
public class AiYue extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.AI_YUE;
    }

    @Override
    public String getIndex() {
        return "http://www.hybooks.cn";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/top/?search=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag, Map<String, Object> map) {
        if (DETAIL.equals(tag)) {
            JsoupNode node = new JsoupNode(html);
            String url = node.href("a.all-catalog");
            return NetUtil.getRequest(url);
        }
        return null;
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("div.bookname a");
                String author = node.ownText("div.bookilnk span");
                String updateTime = node.ownText("div.bookilnk span:eq(3)");
                String imgUrl = node.src("img");
                String detailUrl = node.href("div.bookname a");
                try {
                    updateTime = updateTime.replace("更新时间 ", "");
                } catch (Exception ignored) {
                }
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.store_collist div.bookbox");
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
                String title = node.ownText("div.book-meta h1");
                String imgUrl = null;
                String author = node.ownText("div.book-meta span");
                String intro = null;
                String updateStatus = null;
                String updateTime = null;
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul.chapter-list li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.remove("p").html("div.content");
        content = SourceHelper.getCommonContent(content);
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "<div class=\"nr br sub\">\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/xuanhuan/\">玄幻小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/xianxia/\">仙侠小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/dushi/\">都市小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/lishi/\">历史小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/kehuan/\">科幻小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/youxi/\">游戏小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/qihuan/\">奇幻小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/zhichang/\">职场小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/wuxia/\">武侠小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/erciyuan/\">二次元类</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/tiyu/\">体育小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/lingyi/\">灵异小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/junshi/\">军事小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/duanpian/\">短篇小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/yijieqihuan/\">异界奇幻</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/tuwen/\">图文小说</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/xiandaiyanqing/\">现代言情</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/xuanhuanyanqing/\">玄幻言情</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/kehuankongjian/\">科幻空间</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/xianxiaqiyuan/\">仙侠奇缘</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/gudaiyanqing/\">古代言情</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/langmanqingchun/\">浪漫青春</a>\n" +
                "<a class=\"store \" href=\"http://www.hybooks.cn/tongrenxiaoshuo/\">同人小说</a>\n" +
                "</div>";
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), node.href("a"));
        }
        return map;
    }

    @Override
    public List<NovelInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}