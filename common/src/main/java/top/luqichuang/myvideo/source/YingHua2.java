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
import top.luqichuang.myvideo.model.BaseVideoSource;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/24 22:33
 * @ver 1.0
 */
public class YingHua2 extends BaseVideoSource {

    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.YING_HUA_2;
    }

    @Override
    public String getIndex() {
        return "http://www.yhdm.so";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search/%s/";
        return NetUtil.getRequest(String.format(url, searchString));
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h2 a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.lpic li");
    }

    @Override
    public void setInfoDetail(VideoInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.rate.r h1");
                String imgUrl = node.src("div.thumb.l img");
                String author = null;
                String intro = node.ownText("div.info");
                String updateStatus = null;
                String updateTime = node.remove("div.sinfo span label").text("div.sinfo span");
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
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String url = node.attr("div.bofang div", "data-vid");
        if (url != null) {
            url = url.split("\\$")[0];
        }
        Content content = new Content(chapterId);
        content.setUrl(url);
        return SourceHelper.getContentList(content);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<div class=\"area box\"><div class=\"box1 l\"><label>按地区/时间</label>\n" +
                "<ul><li><a href=\"/japan/\">日本</a></li><li><a href=\"/china/\">大陆</a></li><li><a href=\"/american/\">美国</a></li><li><a href=\"/england/\">英国</a></li><li><a href=\"/2021/\">2021</a></li><li><a href=\"/2020/\">2020</a></li><li><a href=\"/2019/\">2019</a></li><li><a href=\"/2018/\">2018</a></li></ul></div><div class=\"box2 l\"><label>按类型</label><ul><li><a href=\"/66/\">热血</a></li><li><a href=\"/64/\">格斗</a></li><li><a href=\"/91/\">恋爱</a></li><li><a href=\"/70/\">校园</a></li><li><a href=\"/67/\">搞笑</a></li><li><a href=\"/111/\">LOLI</a></li><li><a href=\"/83/\">神魔</a></li><li><a href=\"/81/\">机战</a></li><li><a href=\"/75/\">科幻</a></li><li><a href=\"/74/\">真人</a></li><li><a href=\"/84/\">青春</a></li><li><a href=\"/73/\">魔法</a></li><li><a href=\"/72/\">美少女</a></li><li><a href=\"/102/\">神话</a></li><li><a href=\"/61/\">冒险</a></li><li><a href=\"/69/\">运动</a></li><li><a href=\"/62/\">竞技</a></li><li><a href=\"/103/\">童话</a></li><li><a href=\"/63/\">亲子</a></li><li><a href=\"/95/\">教育</a></li><li><a href=\"/85/\">励志</a></li><li><a href=\"/77/\">剧情</a></li><li><a href=\"/79/\">社会</a></li><li><a href=\"/99/\">后宫</a></li><li><a href=\"/80/\">战争</a></li><li><a href=\"/119/\">吸血鬼</a></li></ul></div>\n" +
                "<div class=\"box3 r\"><label>按语言</label><ul><li><a href=\"/29/\">日语</a></li><li><a href=\"/30/\">国语</a></li><li><a href=\"/31/\">粤语</a></li><li><a href=\"/32/\">英语</a></li><li><a href=\"/33/\">韩语</a></li><li><a href=\"/34/\">方言</a></li></ul></div>\n" +
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
        return getInfoList(html);
    }
}