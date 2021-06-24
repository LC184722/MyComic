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
 * @date 2021/6/24 18:33
 * @ver 1.0
 */
public class FengChe extends BaseVideoSource {
    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.FENG_CHE;
    }

    @Override
    public String getIndex() {
        return "http://www.fengchedm.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/common/search.aspx?key=%s";
        return NetUtil.getRequest(String.format(url, searchString));
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("p a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.imgs li");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("span.names");
                String imgUrl = node.src("div.tpic.l img");
                String author = null;
                String intro = node.ownText("div.info");
                String updateStatus = null;
                String updateTime = node.ownText("div.alex span:eq(3) a");
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "div.movurl li"));
        SourceHelper.initChapterInfoMap(info, html, "div.tabs li strong:eq(0),div.movurl ul");
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String url = null;
        try {
            String playType = "";
            String source = StringUtil.match("var source=\"(.*?)\"", html);
            String key = "";
            if ("".equals(source)) {
                playType = StringUtil.match("var playtype=\"(.*?)\"", html);
                key = StringUtil.match("id_(.*?).html", html);
            } else {
                String s = StringUtil.match("var dm456 = \"(.*?)\"", html);
                String[] ss = s.split("###");
                playType = ss[1];
                key = ss[3];
            }
            if ("iask".equals(playType)) {
                url = "http://ask.ivideo.sina.com.cn/v_play_ipad.php?vid=" + key + "&uid=1&pid=1&tid=334&plid=4001&prid=ja_7_2184731619&referrer=http%3A%2F%2Fvideo.sina.com.cn&ran=493&r=video.sina.com.cn&v=4.1.43.10&p=i&k=58e";
            } else if ("youku".equals(playType)) {
                url = "http://player.youku.com/embed/" + key;
            } else {
                url = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"area box\">\n" +
                "<div class=\"box1 l\">\n" +
                "<label>按地区/时间</label><ul><li><a href=\"/type1/137-0-0-0-1.html\">国产</a></li><li><a href=\"/type1/138-0-0-0-1.html\">日本</a></li><li><a href=\"/type1/139-0-0-0-1.html\">欧美</a></li><li><a href=\"/type1/140-0-0-0-1.html\">韩国</a></li></ul><ul><li><a href=\"/type1/0-0-1-0-1.html\">2020</a></li><li><a href=\"/type1/0-0-2-0-1.html\">2019</a></li><li><a href=\"/type1/0-0-3-0-1.html\">2018</a></li><li><a href=\"/type1/0-0-4-0-1.html\">2017</a></li></ul></div>\n" +
                "<div class=\"box2 l\">\n" +
                "<label>按类型</label><ul><li><a href=\"/type1/0-1-0-0-1.html\">热血</a></li><li><a href=\"/type1/0-2-0-0-1.html\">格斗</a></li><li><a href=\"/type1/0-3-0-0-1.html\">恋爱</a></li><li><a href=\"/type1/0-4-0-0-1.html\">校园</a></li><li><a href=\"/type1/0-5-0-0-1.html\">搞笑</a></li><li><a href=\"/type1/0-6-0-0-1.html\">LOLI</a></li><li><a href=\"/type1/0-7-0-0-1.html\">神魔</a></li><li><a href=\"/type1/0-8-0-0-1.html\">机战</a></li><li><a href=\"/type1/0-9-0-0-1.html\">科幻</a></li><li><a href=\"/type1/0-10-0-0-1.html\">真人</a></li><li><a href=\"/type1/0-11-0-0-1.html\">青春</a></li><li><a href=\"/type1/0-12-0-0-1.html\">魔法</a></li><li><a href=\"/type1/0-13-0-0-1.html\">美少女</a></li><li><a href=\"/type1/0-14-0-0-1.html\">神话</a></li><li><a href=\"/type1/0-15-0-0-1.html\">冒险</a></li><li><a href=\"/type1/0-16-0-0-1.html\">运动</a></li><li><a href=\"/type1/0-17-0-0-1.html\">竞技</a></li><li><a href=\"/type1/0-18-0-0-1.html\">童话</a></li><li><a href=\"/type1/0-19-0-0-1.html\">亲子</a></li><li><a href=\"/type1/0-20-0-0-1.html\">教育</a></li><li><a href=\"/type1/0-21-0-0-1.html\">励志</a></li><li><a href=\"/type1/0-22-0-0-1.html\">剧情</a></li><li><a href=\"/type1/0-23-0-0-1.html\">社会</a></li><li><a href=\"/type1/0-24-0-0-1.html\">后宫</a></li><li><a href=\"/type1/0-25-0-0-1.html\">战争</a></li><li><a href=\"/type1/0-26-0-0-1.html\">吸血鬼</a></li></ul></div>\n" +
                "<div class=\"box3 r\">\n" +
                "<label>按语言</label><ul>\n" +
                "<li><a href=\"/type2/0-1-0-0-1.html\">日语</a></li><li><a href=\"/type2/0-2-0-0-1.html\">国语</a></li><li><a href=\"/type2/0-3-0-0-1.html\">粤语</a></li><li><a href=\"/type2/0-4-0-0-1.html\">英语</a></li><li><a href=\"/type2/0-5-0-0-1.html\">韩语</a></li><li><a href=\"/type2/0-6-0-0-1.html\">方言</a></li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "</div>";
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
                String title = node.ownText("h2 a");
                String author = null;
                String updateTime = node.ownText("font");
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.pics li");
    }
}
