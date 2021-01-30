package top.luqichuang.common.mycomic.source;

import top.luqichuang.common.mycomic.en.SourceEnum;
import top.luqichuang.common.mycomic.jsoup.JsoupNode;
import top.luqichuang.common.mycomic.jsoup.JsoupStarter;
import top.luqichuang.common.mycomic.model.ChapterInfo;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.model.ImageInfo;
import top.luqichuang.common.mycomic.util.DecryptUtil;
import top.luqichuang.common.mycomic.util.NetUtil;
import top.luqichuang.common.mycomic.util.SourceHelper;
import top.luqichuang.common.mycomic.util.StringUtil;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TengXun extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.TENG_XUN;
    }

    @Override
    public String getIndex() {
        return "https://ac.qq.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://ac.qq.com/Comic/searchList?search=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.attr("img", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "ul.mod_book_list li");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {

            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h2.works-intro-title.ui-left strong");
                String imgUrl = node.src("div.works-cover.ui-left img");
                String author = node.ownText("div.works-intro span.first em");
                String intro = node.ownText("div.works-intro p.works-intro-short");
                String updateStatus = node.ownText("div.works-intro label.works-intro-status");
                String updateTime = node.ownText("span.ui-pl10");
                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(comicInfo, starter.startElements(html, "ol.chapter-page-all span"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String raw = StringUtil.match("DATA.*=.*'(.*?)',", html);
        String nonce = StringUtil.matchLast("window\\[.*?\\] *=(.*?);", html);
        if (nonce != null) {
            String[] docs = nonce.split("\\(\\)");
            for (String doc : docs) {
                String tmp = StringUtil.match("(\\(.*document.*\\)\\.toString)", doc);
                if (tmp == null) {
                    tmp = StringUtil.match("(\\(.*window.*\\)\\.toString)", doc);
                }
                if (tmp != null) {
                    nonce = nonce.replace(tmp + "()", "0");
                }
            }
            nonce = DecryptUtil.exeJsCode(nonce);
        }
        String data = DecryptUtil.exeJsFunction(getJsCode(), "decode", raw, nonce);
        List<String> urlList = null;
        if (data != null) {
            data = DecryptUtil.decryptBase64(data);
            if (data != null) {
                data = data.replaceAll("\\\\", "");
                urlList = StringUtil.matchList("pid(.*?)\"url\":\"(.*?)\"", data, 2);
            }
        }
        return SourceHelper.getImageInfoList(urlList, chapterId);
    }

    private String getJsCode() {
        return "function decode(T, N) {\n" +
                "\tvar len, locate, str;\n" +
                "\tT = T.split('');\n" +
                "\tN = N.match(/\\d+[a-zA-Z]+/g);\n" +
                "\tlen = N.length;\n" +
                "\twhile (len--) {\n" +
                "\t\tlocate = parseInt(N[len]) & 255;\n" +
                "\t\tstr = N[len].replace(/\\d+/g, '');\n" +
                "\t\tT.splice(locate, str.length)\n" +
                "\t}\n" +
                "\tT = T.join('');\n" +
                "\treturn T;\n" +
                "}";
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        long time = new Date().getTime();
        map.put("飙升榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=rise&page=1&pageSize=10&style=items");
        map.put("畅销榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=pay&page=1&pageSize=10&style=items");
        map.put("新作榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=new&page=1&pageSize=10&style=items");
        map.put("真香榜", "https://m.ac.qq.com/rank/index?t=" + time + "&type=hot&page=1&pageSize=10&style=items");
        map.put("免费", "https://ac.qq.com/Comic/all/search/hot/vip/1/page/1");
        map.put("付费", "https://ac.qq.com/Comic/all/search/hot/vip/2/page/1");
        map.put("条漫", "https://m.ac.qq.com/category/listAll/type/tm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("独家", "https://m.ac.qq.com/category/listAll/type/dj/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("完结", "https://m.ac.qq.com/category/listAll/type/wj/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("日漫", "https://m.ac.qq.com/category/listAll/type/rm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("恋爱", "https://m.ac.qq.com/category/listAll/type/na/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("玄幻", "https://m.ac.qq.com/category/listAll/type/xh/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("热血", "https://m.ac.qq.com/category/listAll/type/rx/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("悬疑", "https://m.ac.qq.com/category/listAll/type/xy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("少女", "https://m.ac.qq.com/category/listAll/type/sv/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("韩漫", "https://m.ac.qq.com/category/listAll/type/hm/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("科幻", "https://m.ac.qq.com/category/listAll/type/kh/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("逗比", "https://m.ac.qq.com/category/listAll/type/db/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("校园", "https://m.ac.qq.com/category/listAll/type/qcxy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("都市", "https://m.ac.qq.com/category/listAll/type/ds/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("治愈", "https://m.ac.qq.com/category/listAll/type/zy/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("恐怖", "https://m.ac.qq.com/category/listAll/type/kb/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        map.put("妖怪", "https://m.ac.qq.com/category/listAll/type/yg/rank/pgv?t=" + time + "&page=1&pageSize=15&style=items");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = new ArrayList<>();
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                node.addElement("div.top3-box-item1");
                node.addElement("div.top3-box-item2");
                node.addElement("div.top3-box-item3");
                for (Element element : node.getElements()) {
                    node.init(element);
                    String title = node.ownText("strong.comic-title");
                    String author = node.ownText("small.comic-update");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = getIndex() + node.href("a");
                    ComicInfo comicInfo = new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                    list.add(comicInfo);
                }
            }

            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("strong.comic-title");
                String author = node.ownText("small.comic-update");
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                try {
                    if (detailUrl.contains("comic/index")) {
                        detailUrl = detailUrl.replace("comic/index", "Comic/comicInfo");
                    }
                } catch (Exception ignored) {
                }
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        starter.startInfo(html);
        list.addAll(starter.startElements(html, "li.comic-item"));
        if (!list.isEmpty()) {
            return list;
        } else {
            return new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.title("h3.ret-works-title a");
                    String author = node.title("p.ret-works-author");
                    String updateTime = null;
                    String imgUrl = node.attr("img", "data-original");
                    String detailUrl = getIndex() + node.href("a");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            }.startElements(html, "ul.clearfix li");
        }
    }
}
