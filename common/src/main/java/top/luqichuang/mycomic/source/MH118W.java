package top.luqichuang.mycomic.source;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.DecryptUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/11/26 20:17
 * @ver 1.0
 */
public class MH118W extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MH_118_2;
    }

    @Override
    public String getIndex() {
        return "http://m.xzcstjx.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "http://www.xzcstjx.com/statics/search.aspx?key=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("li.title a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("li.title a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.cy_list_mh ul");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.title h1");
                String imgUrl = node.src("div#Cover img");
                String author = node.ownText("p.txtItme:eq(1)");
                String intro = node.ownText("p.txtDesc");
                String updateStatus = node.ownText("p.txtItme:eq(2) :eq(3)");
                String updateTime = node.ownText("p.txtItme span.date");
                try {
                    intro = intro.substring(intro.indexOf(':') + 1);
                } catch (Exception ignored) {
                }
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = "http://m.xzcstjx.com" + node.href("a");
                if (title == null) {
                    return null;
                }
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#mh-chapter-list-ol-0 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        String prevUrl = "";
        try {
            String chapterStr = StringUtil.match("qTcms_S_m_murl_e=\"(.*?)\"", html);
            chapterStr = DecryptUtil.decryptBase64(chapterStr);
            String id = StringUtil.match("qTcms_S_m_id=\"(.*?)\"", html);
            prevUrl = "http://res.xzcstjx.com/statics/pic/?picid=" + id + "&p=";
            urls = chapterStr.split("\\$qingtiandy\\$");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SourceHelper.getContentList(urls, chapterId, prevUrl);
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