//package top.luqichuang.mynovel.source;
//
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Request;
//import top.luqichuang.common.en.NSourceEnum;
//import top.luqichuang.common.jsoup.JsoupNode;
//import top.luqichuang.common.jsoup.JsoupStarter;
//import top.luqichuang.common.model.ChapterInfo;
//import top.luqichuang.common.util.NetUtil;
//import top.luqichuang.common.util.SourceHelper;
//import top.luqichuang.common.util.StringUtil;
//import top.luqichuang.mynovel.model.ContentInfo;
//import top.luqichuang.mynovel.model.NBaseSource;
//import top.luqichuang.mynovel.model.NovelInfo;
//
///**
// * @author LuQiChuang
// * @desc
// * @date 2021/2/14 20:05
// * @ver 1.0
// */
//@Deprecated
//public class QuanXiaoShuo extends NBaseSource {
//    @Override
//    public NSourceEnum getNSourceEnum() {
//        return NSourceEnum.QUAN_XIAO_SHUO;
//    }
//
//    @Override
//    public boolean isValid() {
//        return false;
//    }
//
//    @Override
//    public String getCharsetName() {
//        return "GBK";
//    }
//
//    @Override
//    public String getIndex() {
//        return "https://qxs.la";
//    }
//
//    @Override
//    public Request getSearchRequest(String searchString) {
//        String url = String.format("https://qxs.la/s_%s", searchString);
//        return NetUtil.getRequest(url);
//    }
//
//    @Override
//    public List<NovelInfo> getNovelInfoList(String html) {
//        JsoupStarter<NovelInfo> starter = new JsoupStarter<NovelInfo>() {
//            @Override
//            protected NovelInfo dealElement(JsoupNode node, int elementId) {
//                String title = node.ownText("li.cc2 a");
//                String author = node.ownText("li.cc4 a");
//                String updateTime = node.ownText("li.cc5");
//                String imgUrl = null;
//                String detailUrl = getIndex() + node.href("li.cc2 a");
//                return new NovelInfo(getNSourceId(), title, author, detailUrl, imgUrl, updateTime);
//            }
//        };
//        return starter.startElements(html, "div.main.list ul.list_content");
//    }
//
//    @Override
//    public void setNovelDetail(NovelInfo novelInfo, String html) {
//        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
//            @Override
//            protected boolean isDESC() {
//                return false;
//            }
//
//            @Override
//            protected void dealInfo(JsoupNode node) {
//                String title = node.ownText("div.text.t_c a");
//                String imgUrl = null;
//                String author = node.ownText("div.f_l.t_c.w2 a");
//                String intro = node.ownText("div.desc");
//                String updateStatus = null;
//                String updateTime = node.ownText("div.f_l.t_l.w4");
//                try {
//                    updateTime = StringUtil.match("\\???(.*?)\\???", updateTime);
//                } catch (Exception ignored) {
//                }
//                novelInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
//            }
//
//            @Override
//            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
//                String title = node.ownText("a");
//                String chapterUrl = getIndex() + node.href("a");
//                return new ChapterInfo(elementId, title, chapterUrl);
//            }
//        };
//        starter.startInfo(html);
//        SourceHelper.initChapterInfoList(novelInfo, starter.startElements(html, "div.chapter"));
//    }
//
//    @Override
//    public ContentInfo getContentInfo(String html, int chapterId, Map<String, Object> map) {
//        JsoupNode node = new JsoupNode(html);
//        String content = node.html("div#content");
//        node.init(content);
//        node.remove("div");
//        content = node.html("body");
//        content = SourceHelper.getCommonContent(content, "<br>");
//        try {
//            content = content.replace("www.TXTXiaZai.ORG", "");
//            content = content.replace("?????????????????????httpp???????????????", "");
//        } catch (Exception ignored) {
//        }
//        return new ContentInfo(chapterId, content);
//    }
//
//    @Override
//    public Map<String, String> getRankMap() {
//        Map<String, String> map = new LinkedHashMap<>();
//        String html = "<ul> \n" +
//                "\t\t\t\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/top/\">?????????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/qihuan/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/xuanhuan/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/yanqing/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/dushi/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/youxi/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/jingji/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/wuxia/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/lishi/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/junshi/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/kehuan/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/xianxia/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/lingyi/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/nvxing/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/qita/\">??????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/last1.htm\">????????????</a></li>\n" +
//                "\t\t\t<li><a href=\"//quanxs.net/new/\">????????????</a></li>\n" +
//                "\t\t</ul>";
//        JsoupNode node = new JsoupNode(html);
//        Elements elements = node.getElements("a");
//        for (Element element : elements) {
//            node.init(element);
//            map.put(node.ownText("a"), "http:" + node.href("a"));
//        }
//        return map;
//    }
//
//    @Override
//    public List<NovelInfo> getRankNovelInfoList(String html) {
//        return getNovelInfoList(html);
//    }
//}