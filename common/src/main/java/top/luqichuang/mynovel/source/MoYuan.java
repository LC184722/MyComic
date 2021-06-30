package top.luqichuang.mynovel.source;

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
 * @date 2021/6/12 17:07
 * @ver 1.0
 */
public class MoYuan extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.MO_YUAN;
    }

    @Override
    public String getIndex() {
        return "https://www.mywenxue.cc";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/book/Search.aspx?id=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (DETAIL.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            String title = node.ownText("div.l2.r h1");
            String imgUrl = "http:" + node.src("div.pic img");
            String author = node.ownText("div.hl span");
            String intro = node.text("div.txt");
            String updateTime = node.ownText("div.hl span:eq(2)");
            map.put("title", title);
            map.put("imgUrl", imgUrl);
            map.put("author", author);
            map.put("intro", intro);
            map.put("updateTime", updateTime);
            String detailUrl = node.href("div.zjlistc a");
            return NetUtil.getRequest(detailUrl);
        }
        return super.buildRequest(html, tag, data, map);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.text("span", 1, "a");
                String author = node.text("span", 4, "a");
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = node.href("a");
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.wraptwo div.titone");
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
                String title = (String) map.get("title");
                String imgUrl = (String) map.get("imgUrl");
                String author = (String) map.get("author");
                String intro = (String) map.get("intro");
                String updateStatus = null;
                String updateTime = (String) map.get("updateTime");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.chapterList div.chaper0"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.html("div.txt");
        content = SourceHelper.getCommonContent(content, "<br>");
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        return null;
    }

    @Override
    public List<NovelInfo> getRankInfoList(String html) {
        return null;
    }
}
