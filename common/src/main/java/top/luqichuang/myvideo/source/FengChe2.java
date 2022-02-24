package top.luqichuang.myvideo.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.DecryptUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/25 22:41
 * @ver 1.0
 */
public class FengChe2 extends BaseVideoSource {
    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.FENG_CHE_2;
    }

    @Override
    public String getIndex() {
        return "https://www.92wuc.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search/-------------.html?wd=%s&submit=";
        return NetUtil.getRequest(String.format(url, searchString));
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h4.vodlist_title a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.attr("a.vodlist_thumb.lazyload", "data-original");
                String detailUrl = getIndex() + node.href("a");
                if (imgUrl != null && imgUrl.startsWith("//")) {
                    imgUrl = "http:" + imgUrl;
                }
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.searchlist_item");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h2.title");
                String imgUrl = node.attr("a.vodlist_thumb.lazyload", "data-original");
                String author = node.ownText("div.content_detail li.data", 3);
                String intro = node.ownText("div.content_desc.full_text.clearfix span");
                String updateStatus = null;
                String updateTime = node.ownText("div.content_detail li.data a");
                if (imgUrl != null && imgUrl.startsWith("//")) {
                    imgUrl = "http:" + imgUrl;
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul.list_scroll.content_playlist.clearfix li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String playerData = StringUtil.match("player_data=(.*?)</script>", html);
        JsonNode node = new JsonNode(playerData);
        String url = null;
        try {
            url = node.string("url").replace("\\", "");
            url = DecryptUtil.unescape(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"wrapper_fl\" id=\"hl01\">\t<div class=\"scroller\">\t\t<ul class=\"screen_list clearfix\">\t\t\t<li class=\"text hidden_xs\"><span class=\"text_muted\">分类</span></li>\t\t\t<li></li>\t\t\t<li><a href=\"/vodshow/13-----------.html\">日本动漫</a></li>\t\t\t<li><a href=\"/vodshow/14-----------.html\">国产动漫</a></li>\t\t\t<li><a href=\"/vodshow/15-----------.html\">欧美动漫</a></li>\t\t</ul>\t</div></div><div class=\"wrapper_fl\" id=\"hl02\">\t<div class=\"scroller\">\t\t<ul class=\"screen_list clearfix\">\t\t\t<li class=\"text hidden_xs\"><span class=\"text_muted\">类型</span></li>\t\t\t<li></li>\t\t\t<li><a href=\"/vodshow/2---%E6%88%98%E6%96%97--------.html\">战斗</a></li>\t\t\t<li><a href=\"/vodshow/2---%E8%90%9D%E8%8E%89--------.html\">萝莉</a></li>\t\t\t<li><a href=\"/vodshow/2---%E5%90%8E%E5%AE%AB--------.html\">后宫</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%A0%A1%E5%9B%AD--------.html\">校园</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%81%8B%E7%88%B1--------.html\">恋爱</a></li>\t\t\t<li><a href=\"/vodshow/2---%E7%AB%9E%E6%8A%80--------.html\">竞技</a></li>\t\t\t<li><a href=\"/vodshow/2---%E9%9D%92%E6%98%A5--------.html\">青春</a></li>\t\t\t<li><a href=\"/vodshow/2---%E5%B0%91%E5%A5%B3--------.html\">少女</a></li>\t\t\t<li><a href=\"/vodshow/2---%E8%BF%90%E5%8A%A8--------.html\">运动</a></li>\t\t\t<li><a href=\"/vodshow/2---%E7%83%AD%E8%A1%80--------.html\">热血</a></li>\t\t\t<li><a href=\"/vodshow/2---%E7%A5%9E%E9%AD%94--------.html\">神魔</a></li>\t\t\t<li><a href=\"/vodshow/2---%E5%A5%87%E5%B9%BB--------.html\">奇幻</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%B2%BB%E6%84%88--------.html\">治愈</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%90%9E%E7%AC%91--------.html\">搞笑</a></li>\t\t\t<li><a href=\"/vodshow/2---%E7%99%BE%E5%90%88--------.html\">百合</a></li>\t\t\t<li><a href=\"/vodshow/2---%E5%86%92%E9%99%A9--------.html\">冒险</a></li>\t\t\t<li><a href=\"/vodshow/2---%E9%AD%94%E6%B3%95--------.html\">魔法</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%9C%BA%E6%88%98--------.html\">机战</a></li>\t\t\t<li><a href=\"/vodshow/2---%E6%B3%A1%E9%9D%A2%E7%95%AA--------.html\">泡面番</a></li>\t\t\t<li><a href=\"/vodshow/2---%E8%BD%BB%E5%B0%8F%E8%AF%B4--------.html\">轻小说</a></li>\t\t</ul>\t</div></div><div class=\"wrapper_fl\" id=\"hl04\">\t<div class=\"scroller\">\t\t<ul class=\"screen_list clearfix\">\t\t\t<li class=\"text hidden_xs\"><span class=\"text_muted\">年份</span></li>\t\t\t<li></li>\t\t\t<li><a href=\"/vodshow/2-----------2020.html\">2020</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2019.html\">2019</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2018.html\">2018</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2017.html\">2017</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2016.html\">2016</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2015.html\">2015</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2014.html\">2014</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2013.html\">2013</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2012.html\">2012</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2011.html\">2011</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2010.html\">2010</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2009.html\">2009</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2008.html\">2008</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2007.html\">2007</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2006.html\">2006</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2005.html\">2005</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2004.html\">2004</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2003.html\">2003</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2002.html\">2002</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2001.html\">2001</a></li>\t\t\t<li><a href=\"/vodshow/2-----------2000.html\">2000</a></li>\t\t</ul>\t</div></div><div class=\"wrapper_fl\" id=\"hl06\">\t<div class=\"scroller\">\t\t<ul class=\"screen_list clearfix\">\t\t\t<li class=\"text hidden_xs\"><span class=\"text_muted\">字母</span></li>\t\t\t<li></li>\t\t\t<li><a href=\"/vodshow/2-----A------.html\">A</a></li>\t\t\t<li><a href=\"/vodshow/2-----B------.html\">B</a></li>\t\t\t<li><a href=\"/vodshow/2-----C------.html\">C</a></li>\t\t\t<li><a href=\"/vodshow/2-----D------.html\">D</a></li>\t\t\t<li><a href=\"/vodshow/2-----E------.html\">E</a></li>\t\t\t<li><a href=\"/vodshow/2-----F------.html\">F</a></li>\t\t\t<li><a href=\"/vodshow/2-----G------.html\">G</a></li>\t\t\t<li><a href=\"/vodshow/2-----H------.html\">H</a></li>\t\t\t<li><a href=\"/vodshow/2-----I------.html\">I</a></li>\t\t\t<li><a href=\"/vodshow/2-----J------.html\">J</a></li>\t\t\t<li><a href=\"/vodshow/2-----K------.html\">K</a></li>\t\t\t<li><a href=\"/vodshow/2-----L------.html\">L</a></li>\t\t\t<li><a href=\"/vodshow/2-----M------.html\">M</a></li>\t\t\t<li><a href=\"/vodshow/2-----N------.html\">N</a></li>\t\t\t<li><a href=\"/vodshow/2-----O------.html\">O</a></li>\t\t\t<li><a href=\"/vodshow/2-----P------.html\">P</a></li>\t\t\t<li><a href=\"/vodshow/2-----Q------.html\">Q</a></li>\t\t\t<li><a href=\"/vodshow/2-----R------.html\">R</a></li>\t\t\t<li><a href=\"/vodshow/2-----S------.html\">S</a></li>\t\t\t<li><a href=\"/vodshow/2-----T------.html\">T</a></li>\t\t\t<li><a href=\"/vodshow/2-----U------.html\">U</a></li>\t\t\t<li><a href=\"/vodshow/2-----V------.html\">V</a></li>\t\t\t<li><a href=\"/vodshow/2-----W------.html\">W</a></li>\t\t\t<li><a href=\"/vodshow/2-----X------.html\">X</a></li>\t\t\t<li><a href=\"/vodshow/2-----Y------.html\">Y</a></li>\t\t\t<li><a href=\"/vodshow/2-----Z------.html\">Z</a></li>\t\t\t<li><a href=\"/vodshow/2-----0-9------.html\">0-9</a></li>\t\t</ul>\t</div></div>";
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
                String title = node.ownText("p.vodlist_title a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.attr("a.vodlist_thumb.lazyload", "data-original");
                String detailUrl = getIndex() + node.href("a");
                if (imgUrl != null && imgUrl.startsWith("//")) {
                    imgUrl = "http:" + imgUrl;
                }
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.vodlist_item");
    }
}
