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
 * @date 2021/2/16 19:48
 * @ver 1.0
 */
public class XuanShu extends NBaseSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.XUAN_SHU;
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag) {
        if (DETAIL.equals(tag)) {
            JsoupNode node = new JsoupNode(html);
            String url = node.href("li.downAddress_li:eq(1) a");
            return NetUtil.getRequest(url);
        }
        return super.buildRequest(requestUrl, html, tag);
    }

    @Override
    public String getIndex() {
        return "http://www.iddwx.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "http://www.iddwx.com/search.html";
        return NetUtil.postRequest(url, "searchkey", searchString);
    }

    @Override
    public List<NovelInfo> getNovelInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String author = node.ownText("a");
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = node.href("a");
                try {
                    author = author.split("作者：")[1];
                    node.init(html);
                    updateTime = node.ownText("span.oldDate", getSize() - elementId - 1);
                } catch (Exception ignored) {
                }
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#searchmain div.searchTopic");
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
                String title = node.ownText("div.view_t");
                String imgUrl = null;
                String author = node.ownText("div.view_info");
                String intro = null;
                String updateStatus = null;
                String updateTime = node.ownText("div.view_info");
                try {
                    title = title.split("_")[0];
                    author = author.split(":")[1].split(" ")[0];
                    updateTime = updateTime.split("上传时间:")[1];
                } catch (Exception ignored) {
                }
                novelInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = node.href("a");
                try {
                    title = title.split(" ", 2)[1];
                } catch (Exception ignored) {
                }
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(novelInfo, starter.startElements(html, "div.read_list a"));
    }

    @Override
    public ContentInfo getContentInfo(String html, int chapterId) {
        JsoupNode node = new JsoupNode(html);
        String content = node.remove("div.view_page").html("div#view_content_txt");
        content = SourceHelper.getCommonContent(content);
        return new ContentInfo(chapterId, content);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "<ul id=\"globalNavUL\">\n" +
                "                        <li><a href=\"http://www.iddwx.com/soft1/\" title=\"玄幻奇幻\">玄幻奇幻</a></li><li><a href=\"http://www.iddwx.com/soft2/\" title=\"仙侠修真\">仙侠修真</a></li><li><a href=\"http://www.iddwx.com/soft3/\" title=\"穿越言情\">穿越言情</a></li><li><a href=\"http://www.iddwx.com/soft4/\" title=\"都市官场\">都市官场</a></li><li><a href=\"http://www.iddwx.com/soft5/\" title=\"历史架空\">历史架空</a></li><li><a href=\"http://www.iddwx.com/soft6/\" title=\"网游同人\">网游同人</a></li><li><a href=\"http://www.iddwx.com/soft7/\" title=\"科幻战争\">科幻战争</a></li><li><a href=\"http://www.iddwx.com/soft8/\" title=\"名著其他\">名著其他</a></li>\n" +
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
                String title = node.title("a");
                String author = null;
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = node.href("a");
                try {
                    node.init(html);
                    author = node.ownText("div.mainListBottom", getSize() - elementId - 1, "div.mainRunSystem:eq(1)");
                    updateTime = node.ownText("div.mainListBottom", getSize() - elementId - 1, "div.mainRunSystem a");
                } catch (Exception ignored) {
                }
                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul#mainlistUL div.mainListInfo");
    }
}