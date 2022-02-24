package top.luqichuang.mycomic.source;

import java.util.LinkedHashMap;
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
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
@Deprecated
public class PuFei extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.PU_FEI;
    }

    @Override
    public String getCharsetName(String tag) {
        return "GB2312";
    }

    @Override
    public String getIndex() {
        return "http://m.pufei8.com";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "http://m.pufei8.com/e/search/?searchget=1&tbname=mh&show=title,player,playadmin,bieming,pinyin,playadmin&tempid=4&keyboard=" + DecryptUtil.getGBKEncodeStr(searchString);
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h3");
                String author = node.ownText("dl dd");
                String updateTime = node.text("dl", 3, "dd");
                String imgUrl = node.attr("div.thumb img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul#detail li");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.main-bar.bar-bg1 h1");
                String imgUrl = node.src("div.thumb img");
                String author = node.ownText("div.book-detail dl:eq(3) dd");
                String intro = node.ownText("div#bookIntro p");
                String updateStatus = node.ownText("div.thumb i");
                String updateTime = node.ownText("div.book-detail dl:eq(2) dd");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div#chapterList2 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String encodeStr = StringUtil.match("cp=\"(.*?)\"", html);
        String[] urls = null;
        if (encodeStr != null) {
            urls = decodeStr(encodeStr);
        }
        String prevUrl = "http://res.img.scbrxhwl.com/";
        if (urls != null && urls.length > 0) {
            if (urls[0].startsWith("http")) {
                prevUrl = "";
            }
        }
        return SourceHelper.getContentList(urls, chapterId, prevUrl);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("风云榜", "http://m.pufei8.com/manhua/paihang.html");
        map.put("刚刚更新", "http://m.pufei8.com/manhua/update.html");
        map.put("少年热血", "http://m.pufei8.com/shaonianrexue/");
        map.put("少女爱情", "http://m.pufei8.com/shaonvaiqing/");
        map.put("武侠格斗", "http://m.pufei8.com/wuxiagedou/");
        map.put("科幻魔幻", "http://m.pufei8.com/wuxiagedou/");
        map.put("竞技体育", "http://m.pufei8.com/jingjitiyu/");
        map.put("搞笑喜剧", "http://m.pufei8.com/gaoxiaoxiju/");
        map.put("耽美BL", "http://m.pufei8.com/danmeirensheng/");
        map.put("侦探推理", "http://m.pufei8.com/zhentantuili/");
        map.put("恐怖灵异", "http://m.pufei8.com/kongbulingyi/");
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }

    private String[] decodeStr(String encodeStr) {
        String code = DecryptUtil.decryptBase64(encodeStr);
        String result = DecryptUtil.exeJsCode(code);
        if (result != null) {
            return result.split(",");
        } else {
            return new String[0];
        }
    }

}
