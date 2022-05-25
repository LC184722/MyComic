package top.luqichuang.mycomic.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
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
 * @date 2022/5/25 13:32
 * @ver 1.0
 */
public class DaShu extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.DA_SHU;
    }

    @Override
    public String getIndex() {
        return "https://www.dashuhuwai.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search?types=comic&searhword=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("div.ti a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.alllist dt");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.bookname");
                String imgUrl = node.src("div.img img");
                String author = node.ownText("div.shuxin dd a");
                String intro = node.ownText("div#setnmh-bookms div.ms");
                String updateStatus = node.ownText("div.shuxin dd:eq(2)");
                String updateTime = node.ownText("div.shuxin dl:eq(1) dd");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#ul_chapter1 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        String js = StringUtil.matchLast("(eval\\(function.*?\\{\\}\\)\\))", html);
        if (js != null) {
            js = js.replaceAll("\\}\\('(.*?)\\[", "}('[");
            String code = DecryptUtil.exeJsCode(js);
            if (code != null) {
                urls = code.split(",");
                System.out.println("urls = " + Arrays.toString(urls));
            }
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"tkcon ty\">\t<a class=\"on\" href=\"https://www.dashuhuwai.com/category/all/\">全部</a>\t<a href=\"https://www.dashuhuwai.com/category/maoxianrexue/\" class=\"\">冒险热血</a>\t<a href=\"https://www.dashuhuwai.com/category/xuanhuankehuan/\" class=\"\">玄幻科幻</a>\t<a href=\"https://www.dashuhuwai.com/category/zhentantuili/\" class=\"\">侦探推理</a>\t<a href=\"https://www.dashuhuwai.com/category/danmeiaiqing/\" class=\"\">耽美爱情</a>\t<a href=\"https://www.dashuhuwai.com/category/shenghuomanhua/\" class=\"\">生活漫画</a>\t<a href=\"https://www.dashuhuwai.com/category/qita/\" class=\"\">其他</a>\t<a href=\"https://www.dashuhuwai.com/category/wuxiagedou/\" class=\"\">武侠格斗</a></div>";
        Map<String, String> map = new LinkedHashMap<>();
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), node.href("a"));
        }
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}
