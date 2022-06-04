package top.luqichuang.mycomic.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
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
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2022/6/1 11:50
 * @ver 1.0
 */
public class BaoZi extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.BAO_ZI;
    }

    @Override
    public String getIndex() {
        return "https://cn.baozimh.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search?q=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String author = node.ownText("a.comics-card__info small");
                String updateTime = null;
                String imgUrl = node.src("amp-img");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.comics-card");
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
                String title = node.ownText("h1.comics-detail__title");
                String imgUrl = node.src("div.de-info__box amp-img");
                String author = node.ownText("h2.comics-detail__author");
                String intro = node.ownText("p.comics-detail__desc");
                String updateStatus = node.ownText("div.tag-list span");
                String updateTime = node.ownText("div.supporting-text em");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.pure-g[id] div.comics-chapters"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("ul.comic-contain amp-img");
        if (!elements.isEmpty()) {
            urls = new String[elements.size()];
            for (int i = 0; i < urls.length; i++) {
                node.init(elements.get(i));
                urls[i] = node.src("amp-img");
            }
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div style=\"margin-top: 60px;\" data-v-3cb735e6=\"\">\t<div class=\"classify-nav\" data-v-3cb735e6=\"\">\t\t<div class=\"nav\" data-v-3cb735e6=\"\"><a href=\"/classify?type=all&amp;region=cn&amp;state=all&amp;filter=%2a\"\t\t\t\tclass=\"item\" data-v-3cb735e6=\"\">国漫\t\t\t</a><a href=\"/classify?type=all&amp;region=jp&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">日本\t\t\t</a><a href=\"/classify?type=all&amp;region=kr&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">韩国\t\t\t</a><a href=\"/classify?type=all&amp;region=en&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">欧美\t\t\t</a></div>\t</div>\t<div class=\"classify-nav\" data-v-3cb735e6=\"\">\t\t<div class=\"nav pure-form\" data-v-3cb735e6=\"\">\t\t\t<a href=\"/classify?type=all&amp;region=all&amp;state=serial&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">连载中\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=pub&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">已完结\t\t\t</a>\t\t</div>\t</div>\t<div class=\"classify-nav\" data-v-3cb735e6=\"\">\t\t<div class=\"nav\" data-v-3cb735e6=\"\"><a href=\"/classify?type=lianai&amp;region=all&amp;state=all&amp;filter=%2a\"\t\t\t\tclass=\"item\" data-v-3cb735e6=\"\">恋爱\t\t\t</a><a href=\"/classify?type=chunai&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">纯爱\t\t\t</a><a href=\"/classify?type=gufeng&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">古风\t\t\t</a><a href=\"/classify?type=yineng&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">异能\t\t\t</a><a href=\"/classify?type=xuanyi&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">悬疑\t\t\t</a><a href=\"/classify?type=juqing&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">剧情\t\t\t</a><a href=\"/classify?type=kehuan&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">科幻\t\t\t</a><a href=\"/classify?type=qihuan&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">奇幻\t\t\t</a><a href=\"/classify?type=xuanhuan&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">玄幻\t\t\t</a><a href=\"/classify?type=chuanyue&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">穿越\t\t\t</a><a href=\"/classify?type=mouxian&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">冒险\t\t\t</a><a href=\"/classify?type=tuili&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">推理\t\t\t</a><a href=\"/classify?type=wuxia&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">武侠\t\t\t</a><a href=\"/classify?type=gedou&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">格斗\t\t\t</a><a href=\"/classify?type=zhanzheng&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">战争\t\t\t</a><a href=\"/classify?type=rexie&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">热血\t\t\t</a><a href=\"/classify?type=gaoxiao&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">搞笑\t\t\t</a><a href=\"/classify?type=danuzhu&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">大女主\t\t\t</a><a href=\"/classify?type=dushi&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">都市\t\t\t</a><a href=\"/classify?type=zongcai&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">总裁\t\t\t</a><a href=\"/classify?type=hougong&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">后宫\t\t\t</a><a href=\"/classify?type=richang&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">日常\t\t\t</a><a href=\"/classify?type=hanman&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">韩漫\t\t\t</a><a href=\"/classify?type=shaonian&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">少年\t\t\t</a><a href=\"/classify?type=qita&amp;region=all&amp;state=all&amp;filter=%2a\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">其它\t\t\t</a></div>\t</div>\t<div class=\"classify-nav\" data-v-3cb735e6=\"\">\t\t<div class=\"nav\" data-v-3cb735e6=\"\"><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=ABCD\"\t\t\t\tclass=\"item\" data-v-3cb735e6=\"\">ABCD\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=EFGH\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">EFGH\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=IJKL\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">IJKL\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=MNOP\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">MNOP\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=QRST\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">QRST\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=UVW\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">UVW\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=XYZ\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">XYZ\t\t\t</a><a href=\"/classify?type=all&amp;region=all&amp;state=all&amp;filter=0-9\" class=\"item\"\t\t\t\tdata-v-3cb735e6=\"\">0-9\t\t\t</a></div>\t</div></div>";
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
    public List<ComicInfo> getRankInfoList(String html) {
        return getInfoList(html);
    }
}
