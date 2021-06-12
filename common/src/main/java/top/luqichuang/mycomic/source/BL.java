package top.luqichuang.mycomic.source;

import org.jsoup.select.Elements;

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
 * @date 2021/1/24 19:48
 * @ver 1.0
 */
public class BL extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.BL;
    }

    @Override
    public String getIndex() {
        return "https://www.kanbl.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://www.kanbl.com/search?keyword=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag, Map<String, Object> map) {
        if (CONTENT.equals(tag)) {
            try {
                JsoupNode node = new JsoupNode(html);
                node.init(node.getElements("select.selectpage option").last());
                int pageMax = Integer.parseInt(node.attr("option", "value"));
                int page;
                if (!requestUrl.contains("?page=")) {
                    page = 2;
                } else {
                    page = Integer.parseInt(StringUtil.match("\\?page=(\\d+)", requestUrl)) + 1;
                    requestUrl = requestUrl.split("\\?")[0];
                }
                if (page <= pageMax) {
                    List<Content> list = (List<Content>) map.get("list");
                    if (list == null) {
                        list = getContentList(html, -1, null);
                        map.put("list", list);
                    } else {
                        list.addAll(getContentList(html, -1, null));
                    }
                    return NetUtil.getRequest(requestUrl + "?page=" + page);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return super.buildRequest(requestUrl, html, tag, map);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("h3.title a");
                String author = null;
                String updateTime = node.ownText("span.chapter");
                String imgUrl = node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("h3.title a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#comicListBox li");
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
                String title = node.ownText("h1.title");
                String imgUrl = node.attr("div.comic-cover img", "data-src");
                String author = node.ownText("span.update-time");
                String intro = node.ownText("div.desc-con");
                String updateStatus = null;
                String updateTime = node.ownText("p.update");
                try {
                    author = author.substring(author.indexOf('：') + 1).trim();
                    updateTime = updateTime.substring(updateTime.indexOf(':') + 1).trim();
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#chapterList li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("div.comiclist img");
        String[] urls = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            node.init(elements.get(i));
            urls[i] = node.src("img");
        }
        if (map != null) {
            List<Content> lList = SourceHelper.getContentList(urls, chapterId);
            List<Content> list = (List<Content>) map.get("list");
            if (list != null) {
                list.addAll(lList);
                int i = 0;
                for (Content imageInfo : list) {
                    imageInfo.setChapterId(chapterId);
                    imageInfo.setCur(i++);
                    imageInfo.setTotal(list.size());
                }
                return list;
            }
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        return null;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        return null;
    }
}
