package top.luqichuang.mynovel.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.mynovel.model.BaseNovelSource;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 23:11
 * @ver 1.0
 */
public class K17 extends BaseNovelSource {
    @Override
    public NSourceEnum getNSourceEnum() {
        return NSourceEnum.K17;
    }

    @Override
    public String getIndex() {
        return "https://www.17k.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("https://search.17k.com/search.xhtml?c.st=0&c.q=%s", searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<NovelInfo> getInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("div.textmiddle a");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("span.ls a");
                String updateTime = node.ownText("ul code", 1);
                String imgUrl = node.src("img");
                String detailUrl = "https:" + node.href("div.textmiddle a");
                try {
                    detailUrl = detailUrl.replace("book", "list");
                } catch (Exception ignored) {
                }
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.textlist");
    }

    @Override
    public void setInfoDetail(NovelInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.Title");
                String imgUrl = null;
                String author = node.ownText("div.Author a");
                String intro = null;
                String updateStatus = null;
                String updateTime = null;
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "dl.Volume dd a"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        JsoupNode node = new JsoupNode(html);
        String content = node.html("div#readArea div.p");
        if (content == null) {
            content = node.html("div.Article div.content");
        }
        node.init(content);
        node.remove("p.copy", "div#banner_content", "div.qrcode", "div.chapter_text_ad");
        content = node.html("body");
        try {
            content = content.replaceAll("<!--(.*?)-->", "");
            content = content.replace("<p>", "");
            content = SourceHelper.getCommonContent(content, "</p>");
        } catch (Exception ignored) {
        }
        return SourceHelper.getContentList(new Content(chapterId, content));
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String html = "<div class=\"list\">\n" +
                "        <div class=\"Title2\">\n" +
                "            <h2 class=\"tit\">热门作品榜单</h2>\n" +
                "        </div>\n" +
                "        <ul>\n" +
                "        \t<li><a href=\"/top/refactor/top100/01_subscribe/01_subscribe__top_100_pc.html\" target=\"_blank\">畅销榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/14_recommend/14_recommend_top_100_pc.html\" target=\"_blank\">推荐票榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/16_gift/16_gift_top_100_pc.html\" target=\"_blank\">礼物榜</a></li>\n" +
                "\n" +
                "\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/18_popularityListScore/18_popularityListScore_top_100_pc.html\" target=\"_blank\">人气榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/18_popularityListScore/18_popularityListScore_finishBook_top_100_pc.html\" target=\"_blank\">完本榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/06_vipclick/06_click_freeBook_top_100_pc.html\" target=\"_blank\">免费榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/17_honor/17_honor_top_100_pc.html\" target=\"_blank\">荣誉榜</a></li>\n" +
                "\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/10_bookshelf/10_bookshelf_top_100_pc.html\" target=\"_blank\">收藏榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/09_totalwords/09_totalwords_top_100_pc.html\" target=\"_blank\">字数榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/12_hotcomment/12_hotcomment_top_100_pc.html\" target=\"_blank\">热评榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/11_frequentlyupdate/11_frequentlyupdate_top_100_pc.html\" target=\"_blank\">更新榜</a></li>\n" +
                "\t\t\n" +
                "\n" +
                "\t\t<!--作废-->\n" +
                "\t\t<!-- <li><a href=\"/top/refactor/top100/13_completedbookclick/13_completedbookclick_top_100_pc.html\" target=\"_blank\">完本点击榜</a></li> -->\n" +
                "\t\t<!-- <li><a href=\"/top/refactor/top100/01_subscribe/01_subscribe_finished_top_100_pc.html\" target=\"_blank\">完本订阅榜</a></li> -->\n" +
                "\t\t<!-- <li><a href=\"/top/refactor/top100/15_hongbao/15_hongbao_top_100_pc.html\" target=\"_blank\">全站红包榜</a></li> -->\n" +
                "\t\t<!-- <li><a href=\"/top/refactor/top100/14_recommend/14_recommend_newUser_top_100_pc.html\" target=\"_blank\">新书推荐榜</a></li> -->\n" +
                "\t\t<!--<li><a href=\"/top/refactor/top100/08_staruser/08_staruser_top_100_pc.html\" target=\"_blank\">全站明星用户榜</a></li>-->\n" +
                "\t\t<!--<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_cnl_cool_top_100_pc.html\" target=\"_blank\">个性会员点击榜</a></li>-->\n" +
                "        </ul>\n" +
                "\n" +
                "\n" +
                "        <div class=\"Title2\">\n" +
                "            <h2 class=\"tit\">新作品榜单</h2>\n" +
                "        </div>\n" +
                "        <ul>\n" +
                "        \t<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_new_top_100_pc.html\" target=\"_blank\">新作品点击榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_newUser_new_top_100_pc.html\" target=\"_blank\">新人作品点击榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/01_subscribe/01_subscribe_newBook_top_100_pc.html\" target=\"_blank\">新作品订阅榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/01_subscribe/01_subscribe_newUser_newBook_top_100_pc.html\" target=\"_blank\">新人作品订阅榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/14_recommend/14_recommend_newUser_top_100_pc.html\" target=\"_blank\">新人推荐票榜</a></li>\n" +
                "\t\t</ul>\n" +
                "\n" +
                "        <div class=\"Title2\">\n" +
                "            <h2 class=\"tit\">精选作品榜单</h2>\n" +
                "        </div>\n" +
                "        <ul>\n" +
                "        \t<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_serialWithLong_top_100_pc.html\" target=\"_blank\">作品点击榜</a></li>\n" +
                "\t        <li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_cnl_man_top_100_pc.html\" target=\"_blank\">男生作品榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_cnl_mm_top_100_pc.html\" target=\"_blank\">女生作品榜</a></li>\n" +
                "\t\t\t<li><a href=\"/top/refactor/top100/06_vipclick/06_vipclick_noSign_top_100_pc.html\" target=\"_blank\">潜力作品榜</a></li>\n" +
                "        </ul>\n" +
                "</div>";
        JsoupNode node = new JsoupNode(html);
        Elements elements = node.getElements("a");
        for (Element element : elements) {
            node.init(element);
            map.put(node.ownText("a"), getIndex() + node.href("a"));
        }
        return map;
    }

    @Override
    public List<NovelInfo> getRankInfoList(String html) {
        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
            @Override
            protected NovelInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.red");
                if (title == null) {
                    return null;
                }
                String author = node.ownText("td:eq(5) a");
                String updateTime = node.ownText("td:eq(3) a");
                String imgUrl = null;
                String detailUrl = "https:" + node.href("a.red");
                try {
                    detailUrl = detailUrl.replace("book", "list");
                } catch (Exception ignored) {
                }
                return new NovelInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.TYPE[style=\"display: block;\"] div.BOX[style=\"display: block;\"] tr");
    }
}
