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
 * @date 2021/6/22 23:06
 * @ver 1.0
 */
@Deprecated
public class MiLiMiLi extends BaseVideoSource {

    @Override
    public VSourceEnum getVSourceEnum() {
        return VSourceEnum.MILI_MILI;
    }

    @Override
    public String getIndex() {
        return "http://www.milimili.cc";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/e/search/index.php";
        return NetUtil.postRequest(url, "show", "title,ftitle", "tbname", "movie", "tempid", "1", "keyboard", searchString);
    }

    @Override
    public Request buildRequest(String html, String tag, Map<String, Object> data, Map<String, Object> map) {
        if (CONTENT.equals(tag) && map.isEmpty()) {
            JsoupNode node = new JsoupNode(html);
            String url = node.href("link");
            map.put("url", url);
            return NetUtil.getRequest(url);
        }
        return null;
    }

    @Override
    public List<VideoInfo> getInfoList(String html) {
        JsoupStarter<VideoInfo> starter = new JsoupStarter<VideoInfo>() {
            @Override
            protected VideoInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("h2 a");
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
                String updateStatus = node.ownText("div.sinfo span:eq(2)");
                String updateTime = node.ownText("div.sinfo span:eq(1)");
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
        String url = node.src("iframe");
        try {
            if (StringUtil.count(url, "http") > 1) {
                url = url.substring(url.lastIndexOf("http"));
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
                "      <div class=\"box1 l\">\n" +
                "        <label>?????????/??????</label>\n" +
                "        <ul>\n" +
                "          <li>\n" +
                "            <a href=\"/rbdm/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/gcdm/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2019/\">2019</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2018/\">2018</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2017/\">2017</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2016/\">2016</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2015/\">2015</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/2014/\">2014</a></li>\n" +
                "        </ul>\n" +
                "      </div>\n" +
                "      <div class=\"box2 l\">\n" +
                "        <label>?????????</label>\n" +
                "        <ul>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/rexue/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/jingdian/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/hougong/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/gaoxiao/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/wuxia/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/yinv/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/xiaoyuan/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/kehuan/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/zhiyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/lianai/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/jizhan/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/mofa/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/shaonv/\">?????????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/shenhua/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/mengxi/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/richang/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/yundong/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/tonghua/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/maoxian/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/lishi/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/yinyue/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/tuili/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/ouxiang/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/zhichang/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/dushi/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/xixuegui/\">?????????</a></li>\n" +
                "        </ul>\n" +
                "      </div>\n" +
                "      <div class=\"box3 r\">\n" +
                "        <label>?????????</label>\n" +
                "        <ul>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/riyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/guoyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/yueyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/yingyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/hanyu/\">??????</a></li>\n" +
                "          <li>\n" +
                "            <a href=\"/tag/fangyan/\">??????</a></li>\n" +
                "        </ul>\n" +
                "      </div>\n" +
                "    </div>";
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
                String title = node.ownText("p.tname a");
                String author = null;
                String updateTime = node.ownText("p:eq(2)");
                String imgUrl = getIndex() + node.src("img");
                String detailUrl = getIndex() + node.href("a");
                return new VideoInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.imgs li");
    }
}
