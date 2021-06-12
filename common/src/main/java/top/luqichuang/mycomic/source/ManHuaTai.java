package top.luqichuang.mycomic.source;

import com.alibaba.fastjson.JSONArray;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/10/17 14:31
 * @ver 1.0
 */
public class ManHuaTai extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MAN_HUA_TAI;
    }

    @Override
    public String getIndex() {
        return "https://m.manhuatai.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/sort/all.html?cache=false&search_key=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public Request buildRequest(String requestUrl, String html, String tag, Map<String, Object> map) {
        if (CONTENT.equals(tag)) {
            String url = "https://m.manhuatai.com/api/getchapterinfov2";
            if (!requestUrl.contains(url)) {
                String info = StringUtil.match("window.comicInfo=(\\{.*?\\})", html);
                Map<String, String> reqMap = new HashMap<>();
                reqMap.put("product_id", "2");
                reqMap.put("productname", "mht");
                reqMap.put("platformname", "wap");
                reqMap.put("comic_id", StringUtil.match("comic_id:(.*?),", info));
                reqMap.put("chapter_newid", StringUtil.match(",chapter_newid:\"(.*?)\",", info));
                reqMap.put("isWebp", "1");
                reqMap.put("quality", "high");
                return NetUtil.getRequest(url, reqMap);
            }
        }
        return super.buildRequest(requestUrl, html, tag, map);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("p.title");
                String author = null;
                String updateTime = null;
                String imgUrl = "https:" + node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.comic-item");
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
                String title = node.title("h1.name");
                String imgUrl = "https:" + node.attr("div#Cover img", "data-src");
                String author = node.attr("div.thumbnail img", "alt");
                String intro = node.ownText("p#js_desc_content");
                String updateStatus;
                String updateTime;
                try {
                    String text = node.ownText("p.comic-update-status");
                    String[] texts = text.split(" ");
                    updateStatus = texts[0];
                    updateTime = texts[1];
                } catch (Exception e) {
                    updateStatus = null;
                    updateTime = null;
                }
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#js_chapter_list li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsonNode node = new JsonNode(html);
        node.initConditions("data", "current_chapter");
        JSONArray chapterImgList = node.jsonArray("chapter_img_list");
        List<String> list = new ArrayList<>();
        if (chapterImgList != null) {
            for (Object o : chapterImgList) {
                list.add((String) o);
            }
        }
        return SourceHelper.getContentList(list, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("综合榜", "https://m.manhuatai.com/top/all.html");
        map.put("自制榜", "https://m.manhuatai.com/top/self.html");
        map.put("少年榜", "https://m.manhuatai.com/top/boy.html");
        map.put("少女榜", "https://m.manhuatai.com/top/girl.html");
        map.put("新作榜", "https://m.manhuatai.com/top/new.html");
        map.put("黑马榜", "https://m.manhuatai.com/top/dark.html");
        map.put("免费榜", "https://m.manhuatai.com/top/free.html");
        map.put("付费榜", "https://m.manhuatai.com/top/charge.html");
        map.put("完结榜", "https://m.manhuatai.com/top/finish.html");
        map.put("连载榜", "https://m.manhuatai.com/top/serialize.html");
        map.put("热血", "https://m.manhuatai.com/sort/rexue.html");
        map.put("机战", "https://m.manhuatai.com/sort/jizhan.html");
        map.put("运动", "https://m.manhuatai.com/sort/yundong.html");
        map.put("推理", "https://m.manhuatai.com/sort/tuili.html");
        map.put("冒险", "https://m.manhuatai.com/sort/maoxian.html");
        map.put("耽美", "https://m.manhuatai.com/sort/liaomei.html");
        map.put("百合", "https://m.manhuatai.com/sort/baihe.html");
        map.put("搞笑", "https://m.manhuatai.com/sort/gaoxiao.html");
        map.put("战争", "https://m.manhuatai.com/sort/zhanzhen.html");
        map.put("神魔", "https://m.manhuatai.com/sort/shenmo.html");
        map.put("忍者", "https://m.manhuatai.com/sort/renzhe.html");
        map.put("竞技", "https://m.manhuatai.com/sort/jingji.html");
        map.put("悬疑", "https://m.manhuatai.com/sort/xuanyi.html");
        map.put("社会", "https://m.manhuatai.com/sort/shehui.html");
        map.put("恋爱", "https://m.manhuatai.com/sort/lianai.html");
        map.put("宠物", "https://m.manhuatai.com/sort/chongwu.html");
        map.put("吸血", "https://m.manhuatai.com/sort/xixue.html");
        map.put("萝莉", "https://m.manhuatai.com/sort/luoli.html");
        map.put("后宫", "https://m.manhuatai.com/sort/hougong.html");
        map.put("御姐", "https://m.manhuatai.com/sort/yujie.html");
        map.put("霸总", "https://m.manhuatai.com/sort/bazong.html");
        map.put("玄幻", "https://m.manhuatai.com/sort/xuanhuan.html");
        map.put("古风", "https://m.manhuatai.com/sort/gufeng.html");
        map.put("历史", "https://m.manhuatai.com/sort/lishi.html");
        map.put("漫改", "https://m.manhuatai.com/sort/mangai.html");
        map.put("游戏", "https://m.manhuatai.com/sort/youxi.html");
        map.put("穿越", "https://m.manhuatai.com/sort/chuanyue.html");
        map.put("恐怖", "https://m.manhuatai.com/sort/kongbu.html");
        map.put("真人", "https://m.manhuatai.com/sort/zhenren.html");
        map.put("科幻", "https://m.manhuatai.com/sort/kehuan.html");
        map.put("都市", "https://m.manhuatai.com/sort/dushi.html");
        map.put("武侠", "https://m.manhuatai.com/sort/wuxia.html");
        map.put("修真", "https://m.manhuatai.com/sort/xiuzhen.html");
        map.put("生活", "https://m.manhuatai.com/sort/shenghuo.html");
        map.put("动作", "https://m.manhuatai.com/sort/dongzuo.html");
        map.put("防疫", "https://m.manhuatai.com/sort/fangyi.html");
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        List<ComicInfo> list = new ArrayList<>();
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                node.addElement("div.comic-item.top-1");
                node.addElement("div.comic-item.top-2");
                node.addElement("div.comic-item.top-3");
                for (Element element : node.getElements()) {
                    node.init(element);
                    String title = node.ownText("div.comic-detail a");
                    String author = null;
                    String updateTime = null;
                    String imgUrl = "https:" + node.attr("div.comic-images img", "data-src");
                    String detailUrl = getIndex() + node.href("div.comic-images a");
                    ComicInfo comicInfo = new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                    list.add(comicInfo);
                }
            }

            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("div.comic-detail a");
                String author = node.ownText("div.comic-author");
                String updateTime = null;
                String imgUrl = null;
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        starter.startInfo(html);
        list.addAll(starter.startElements(html, "ul.comic-list li.list"));
        if (!list.isEmpty()) {
            return list;
        } else {
            return new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("p.title");
                    String author = null;
                    String updateTime = node.ownText("span.chapter");
                    String imgUrl = "https:" + node.attr("img.img", "data-src");
                    String detailUrl = getIndex() + node.href("a");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            }.startElements(html, "ul.comic-sort li");
        }
    }

}
