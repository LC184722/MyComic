package top.luqichuang.myvideo.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/7/1 15:53
 * @ver 1.0
 */
public class AiYun extends BaseVideoSource {
    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.AI_YUN;
    }

    @Override
    public String getIndex() {
        return "https://www.iyunys.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/search?searchString=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h1 a");
                String author = node.ownText("li", 1);
                String updateTime = node.ownText("li", 6);
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.fed-deta-info");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.fed-part-eone.fed-font-xvi");
                String imgUrl = node.attr("a.fed-list-pics.fed-lazy.fed-part-2by3", "data-original");
                String author = node.ownText("div.fed-part-layout li.fed-col-md6", 1, "a");
                String intro = node.ownText("p.fed-padding.fed-part-both.fed-text-muted");
                String updateStatus = node.ownText("div.fed-part-layout li.fed-col-md6", 0, "a");
                String updateTime = node.ownText("div.fed-part-layout li.fed-col-md6", 4, "a");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String chapterUrl = getIndex() + "/ckplayer" + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul.fed-part-rows li.fed-padding.fed-col-xs4.fed-col-md3.fed-col-lg2"));
        SourceHelper.initChapterInfoMap(info, html, "ul li.fed-drop-btns.fed-padding.fed-col-xs3.fed-col-md2", "a", "div.all_data_list", "li");
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String url = StringUtil.match("\"urls\": \"(.*?)\",", html);
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"fed-casc-list fed-part-rows\">\t<dl>\t\t<dt>类别</dt>\t\t<dd><a class=\"fed-this fed-text-green\" href=\"/show\">全部</a></dd>\t\t<dd><a href=\"/show?bigCategoryId=1\">动漫</a></dd>\t\t<dd><a href=\"/show?bigCategoryId=2\">电影</a></dd>\t\t<dd><a href=\"/show?bigCategoryId=3\">电视剧</a></dd>\t\t<dd><a href=\"/show?bigCategoryId=4\">综艺</a></dd>\t</dl>\t<dl>\t\t<dt>状态</dt>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;status=1\">连载中</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;status=2\">已完结</a></dd>\t</dl>\t<dl>\t\t<dt>字母</dt>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10053\">A</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10055\">B</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10054\">C</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10044\">D</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10063\">E</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10057\">F</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10060\">G</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10046\">H</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10066\">I</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10050\">J</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10056\">K</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10052\">L</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10058\">M</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10059\">N</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10064\">O</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10049\">P</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10062\">Q</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10065\">R</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10048\">S</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10061\">T</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10067\">U</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10068\">V</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10051\">W</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10043\">X</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10045\">Y</a></dd>\t\t<dd><a href=\"/show?orderBy=weeklyCount&amp;charCategoryId=10047\">Z</a></dd>\t</dl></div>";
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
    public List<VideoInfo> getRankInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.fed-list-title");
                String author = null;
                String updateTime = node.ownText("span.fed-list-desc");
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.fed-list-item");
    }
}
