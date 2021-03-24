package top.luqichuang.mycomic.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.json.JsonNode;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseSource;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/2/26 11:33
 * @ver 1.0
 */
public class AiYouMan extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.AI_YOU_MAN;
    }

    @Override
    public String getIndex() {
        return "https://www.iyouman.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://m.iyouman.com/sort/all.html?cache=false&search_key=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
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
        return starter.startElements(html, "ul#js_comicSortList li");
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
                String title = node.ownText("div.content h1");
                String imgUrl = "https:" + node.attr("div.detail-cover img", "data-src");
                String author = node.ownText("div.auth p.name");
                String intro = node.ownText("p.desc-content");
                String updateStatus = null;
                String updateTime = null;
                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("p.name");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(comicInfo, starter.startElements(html, "ol#j_chapter_list li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId, Map<String, Object> map) {
        String curChapterStr = StringUtil.match("current_chapter:(\\{.*?\\}),", html);
        JsonNode node = new JsonNode(curChapterStr);
        String rule = node.string("rule");
        int endNum = 50;
        try {
            endNum = Integer.parseInt(node.string("end_num"));
        } catch (NumberFormatException ignored) {
        }
        String chapterDomain = node.string("chapter_domain");
        String url = "https://mhpic." + chapterDomain + rule + "-aym.high.webp";
        String[] urls = new String[endNum];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = url.replace("$$", String.valueOf(i + 1));
        }
        return SourceHelper.getImageInfoList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "<ul class=\"acgn-rank-list acgn-clearfix\"><li class=\"item active\"><i class=\"icon-active-rabbit active-rank\"></i> <a href=\"all.html\" title=\"漫画综合榜\" target=\"_blank\">综合榜</a></li><li class=\"item\"><a href=\"self.html\" title=\"漫画自制榜\" target=\"_blank\">自制榜</a></li><li class=\"item\"><a href=\"boy.html\" title=\"漫画少年榜\" target=\"_blank\">少年榜</a></li><li class=\"item\"><a href=\"girl.html\" title=\"漫画少女榜\" target=\"_blank\">少女榜</a></li><li class=\"item\"><a href=\"new.html\" title=\"漫画新作榜\" target=\"_blank\">新作榜</a></li><li class=\"item\"><a href=\"dark.html\" title=\"漫画黑马榜\" target=\"_blank\">黑马榜</a></li><li class=\"item\"><a href=\"free.html\" title=\"漫画免费榜\" target=\"_blank\">免费榜</a></li><li class=\"item\"><a href=\"charge.html\" title=\"漫画付费榜\" target=\"_blank\">付费榜</a></li><li class=\"item\"><a href=\"finish.html\" title=\"漫画完结榜\" target=\"_blank\">完结榜</a></li><li class=\"item\"><a href=\"serialize.html\" title=\"漫画连载榜\" target=\"_blank\">连载榜</a></li></ul>";
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), getIndex() + "/top/" + node.href("a"));
        }
        html = "<dd class=\"acgn-bd\"><a href=\"/sort/rexue.html\" class=\"acgn-sort-attr\" data-tag=\"4-23-rexue\" data-title=\"热血漫画\" title=\"热血漫画\"><span>热血</span></a><a href=\"/sort/jizhan.html\" class=\"acgn-sort-attr\" data-tag=\"4-24-jizhan\" data-title=\"机战漫画\" title=\"机战漫画\"><span>机战</span></a><a href=\"/sort/yundong.html\" class=\"acgn-sort-attr\" data-tag=\"4-27-yundong\" data-title=\"运动漫画\" title=\"运动漫画\"><span>运动</span></a><a href=\"/sort/tuili.html\" class=\"acgn-sort-attr\" data-tag=\"4-28-tuili\" data-title=\"推理漫画\" title=\"推理漫画\"><span>推理</span></a><a href=\"/sort/maoxian.html\" class=\"acgn-sort-attr\" data-tag=\"4-30-maoxian\" data-title=\"冒险漫画\" title=\"冒险漫画\"><span>冒险</span></a><a href=\"/sort/liaomei.html\" class=\"acgn-sort-attr\" data-tag=\"4-31-liaomei\" data-title=\"耽美漫画\" title=\"耽美漫画\"><span>耽美</span></a><a href=\"/sort/baihe.html\" class=\"acgn-sort-attr\" data-tag=\"4-32-baihe\" data-title=\"百合漫画\" title=\"百合漫画\"><span>百合</span></a><a href=\"/sort/gaoxiao.html\" class=\"acgn-sort-attr\" data-tag=\"4-33-gaoxiao\" data-title=\"搞笑漫画\" title=\"搞笑漫画\"><span>搞笑</span></a><a href=\"/sort/zhanzhen.html\" class=\"acgn-sort-attr\" data-tag=\"4-34-zhanzhen\" data-title=\"战争漫画\" title=\"战争漫画\"><span>战争</span></a><a href=\"/sort/shenmo.html\" class=\"acgn-sort-attr\" data-tag=\"4-35-shenmo\" data-title=\"神魔漫画\" title=\"神魔漫画\"><span>神魔</span></a><a href=\"/sort/renzhe.html\" class=\"acgn-sort-attr\" data-tag=\"4-38-renzhe\" data-title=\"忍者漫画\" title=\"忍者漫画\"><span>忍者</span></a><a href=\"/sort/jingji.html\" class=\"acgn-sort-attr\" data-tag=\"4-39-jingji\" data-title=\"竞技漫画\" title=\"竞技漫画\"><span>竞技</span></a><a href=\"/sort/xuanyi.html\" class=\"acgn-sort-attr\" data-tag=\"4-41-xuanyi\" data-title=\"悬疑漫画\" title=\"悬疑漫画\"><span>悬疑</span></a><a href=\"/sort/shehui.html\" class=\"acgn-sort-attr\" data-tag=\"4-42-shehui\" data-title=\"社会漫画\" title=\"社会漫画\"><span>社会</span></a><a href=\"/sort/lianai.html\" class=\"acgn-sort-attr\" data-tag=\"4-43-lianai\" data-title=\"恋爱漫画\" title=\"恋爱漫画\"><span>恋爱</span></a><a href=\"/sort/chongwu.html\" class=\"acgn-sort-attr\" data-tag=\"4-44-chongwu\" data-title=\"宠物漫画\" title=\"宠物漫画\"><span>宠物</span></a><a href=\"/sort/xixue.html\" class=\"acgn-sort-attr\" data-tag=\"4-45-xixue\" data-title=\"吸血漫画\" title=\"吸血漫画\"><span>吸血</span></a><a href=\"/sort/luoli.html\" class=\"acgn-sort-attr\" data-tag=\"4-46-luoli\" data-title=\"萝莉漫画\" title=\"萝莉漫画\"><span>萝莉</span></a><a href=\"/sort/hougong.html\" class=\"acgn-sort-attr\" data-tag=\"4-47-hougong\" data-title=\"后宫漫画\" title=\"后宫漫画\"><span>后宫</span></a><a href=\"/sort/yujie.html\" class=\"acgn-sort-attr\" data-tag=\"4-48-yujie\" data-title=\"御姐漫画\" title=\"御姐漫画\"><span>御姐</span></a><a href=\"/sort/bazong.html\" class=\"acgn-sort-attr\" data-tag=\"4-51-bazong\" data-title=\"霸总漫画\" title=\"霸总漫画\"><span>霸总</span></a><a href=\"/sort/xuanhuan.html\" class=\"acgn-sort-attr\" data-tag=\"4-54-xuanhuan\" data-title=\"玄幻漫画\" title=\"玄幻漫画\"><span>玄幻</span></a><a href=\"/sort/gufeng.html\" class=\"acgn-sort-attr\" data-tag=\"4-55-gufeng\" data-title=\"古风漫画\" title=\"古风漫画\"><span>古风</span></a><a href=\"/sort/lishi.html\" class=\"acgn-sort-attr\" data-tag=\"4-58-lishi\" data-title=\"历史漫画\" title=\"历史漫画\"><span>历史</span></a><a href=\"/sort/mangai.html\" class=\"acgn-sort-attr\" data-tag=\"4-59-mangai\" data-title=\"漫改漫画\" title=\"漫改漫画\"><span>漫改</span></a><a href=\"/sort/youxi.html\" class=\"acgn-sort-attr\" data-tag=\"4-60-youxi\" data-title=\"游戏漫画\" title=\"游戏漫画\"><span>游戏</span></a><a href=\"/sort/chuanyue.html\" class=\"acgn-sort-attr\" data-tag=\"4-61-chuanyue\" data-title=\"穿越漫画\" title=\"穿越漫画\"><span>穿越</span></a><a href=\"/sort/kongbu.html\" class=\"acgn-sort-attr\" data-tag=\"4-62-kongbu\" data-title=\"恐怖漫画\" title=\"恐怖漫画\"><span>恐怖</span></a><a href=\"/sort/zhenren.html\" class=\"acgn-sort-attr\" data-tag=\"4-63-zhenren\" data-title=\"真人漫画\" title=\"真人漫画\"><span>真人</span></a><a href=\"/sort/kehuan.html\" class=\"acgn-sort-attr\" data-tag=\"4-66-kehuan\" data-title=\"科幻漫画\" title=\"科幻漫画\"><span>科幻</span></a><a href=\"/sort/dushi.html\" class=\"acgn-sort-attr\" data-tag=\"4-67-dushi\" data-title=\"都市漫画\" title=\"都市漫画\"><span>都市</span></a><a href=\"/sort/wuxia.html\" class=\"acgn-sort-attr\" data-tag=\"4-70-wuxia\" data-title=\"武侠漫画\" title=\"武侠漫画\"><span>武侠</span></a><a href=\"/sort/xiuzhen.html\" class=\"acgn-sort-attr\" data-tag=\"4-71-xiuzhen\" data-title=\"修真漫画\" title=\"修真漫画\"><span>修真</span></a><a href=\"/sort/shenghuo.html\" class=\"acgn-sort-attr\" data-tag=\"4-72-shenghuo\" data-title=\"生活漫画\" title=\"生活漫画\"><span>生活</span></a><a href=\"/sort/dongzuo.html\" class=\"acgn-sort-attr\" data-tag=\"4-73-dongzuo\" data-title=\"动作漫画\" title=\"动作漫画\"><span>动作</span></a><a href=\"/sort/fangyi.html\" class=\"acgn-sort-attr\" data-tag=\"4-142-fangyi\" data-title=\"防疫漫画\" title=\"防疫漫画\"><span>防疫</span></a></dd>";
        node = new JsoupNode(html);
        elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.title("a"), getIndex() + node.href("a"));
        }
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h3 a");
                String author = null;
                String updateTime = null;
                String imgUrl = "https:" + node.attr("img", "data-src");
                String detailUrl = getIndex() + node.href("h3 a");
                try {
                    title = title.split("\\. ", 2)[1];
                } catch (Exception ignored) {
                }
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        List<ComicInfo> list = starter.startElements(html, "ul.comic-sort-list li");
        if (!list.isEmpty()) {
            return list;
        } else {
            starter = new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("h3 a");
                    String author = null;
                    String updateTime = node.ownText("div.acgn-desc a");
                    String imgUrl = "https:" + node.attr("img", "data-src");
                    String detailUrl = getIndex() + node.href("h3 a");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            };
            return starter.startElements(html, "ul#J_comicList li");
        }
    }
}