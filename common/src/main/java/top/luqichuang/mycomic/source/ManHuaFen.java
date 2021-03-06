//package top.luqichuang.mycomic.source;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Request;
//import top.luqichuang.common.en.SourceEnum;
//import top.luqichuang.common.jsoup.JsoupNode;
//import top.luqichuang.common.jsoup.JsoupStarter;
//import top.luqichuang.common.model.ChapterInfo;
//import top.luqichuang.common.util.DecryptUtil;
//import top.luqichuang.common.util.NetUtil;
//import top.luqichuang.common.util.SourceHelper;
//import top.luqichuang.common.util.StringUtil;
//import top.luqichuang.mycomic.model.BaseSource;
//import top.luqichuang.mycomic.model.ComicInfo;
//import top.luqichuang.mycomic.model.ImageInfo;
//
///**
// * @author LuQiChuang
// * @desc
// * @date 2020/8/12 15:25
// * @ver 1.0
// */
//@Deprecated
//public class ManHuaFen extends BaseSource {
//
//    @Override
//    public SourceEnum getSourceEnum() {
//        return SourceEnum.MAN_HUA_FEN;
//    }
//
//    @Override
//    public boolean isValid() {
//        return false;
//    }
//
//    @Override
//    public String getIndex() {
//        return "https://m.manhuafen.com";
//    }
//
//    @Override
//    public Request getSearchRequest(String searchString) {
//        searchString = "https://m.manhuafen.com/search/?keywords=" + searchString;
//        return NetUtil.getRequest(searchString);
//    }
//
//    @Override
//    public List<ComicInfo> getComicInfoList(String html) {
//        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
//            @Override
//            protected ComicInfo dealElement(JsoupNode node, int elementId) {
//                String title = node.ownText("a.title");
//                String author = node.ownText("p.txtItme");
//                String updateTime = node.ownText("span.date");
//                String imgUrl = node.src("img");
//                String detailUrl = node.href("a.title");
//                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
//            }
//        };
//        return starter.startElements(html, "div.itemBox");
//    }
//
//    @Override
//    public void setComicDetail(ComicInfo comicInfo, String html) {
//        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
//            @Override
//            protected boolean isDESC() {
//                return false;
//            }
//
//            @Override
//            protected void dealInfo(JsoupNode node) {
//                String title = node.ownText("h1#comicName");
//                String imgUrl = node.src("div#Cover img");
//                String author = node.ownText("p.txtItme");
//                String intro = node.ownText("p#full-des", "p#simple-des");
//                String updateStatus = node.ownText("p.txtItme:eq(2) :eq(3)");
//                String updateTime = node.ownText("p.txtItme span.date");
//                try {
//                    intro = intro.substring(intro.indexOf(':') + 1);
//                } catch (Exception ignored) {
//                }
//                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
//            }
//
//            @Override
//            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
//                String title = node.ownText("span");
//                String chapterUrl = node.href("a");
//                if (chapterUrl.contains("html")) {
//                    if (!chapterUrl.startsWith("http")) {
//                        chapterUrl = getIndex() + chapterUrl;
//                    }
//                    return new ChapterInfo(elementId, title, chapterUrl);
//                } else {
//                    return null;
//                }
//            }
//        };
//        starter.startInfo(html);
//        SourceHelper.initChapterInfoList(comicInfo, starter.startElements(html, "ul#chapter-list-1 li"));
//    }
//
//    @Override
//    public List<ImageInfo> getImageInfoList(String html, int chapterId, Map<String, Object> map) {
//        String server = "https://img01.eshanyao.com/";
//        String chapterImagesEncodeStr = StringUtil.match("var chapterImages = \"(.*?)\";", html);
//        //var chapterPath = "images/comic/259/517692/";
//        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
//        String chapterImagesStr = decrypt(chapterImagesEncodeStr);
//        List<String> urlList = null;
//        if (chapterImagesStr != null) {
//            chapterImagesStr = chapterImagesStr.replaceAll("\\\\", "");
//            urlList = StringUtil.matchList("\"(.*?)\"", chapterImagesStr);
//            for (int i = 0; i < urlList.size(); i++) {
//                String url = urlList.get(i);
//                if (url.startsWith("http:")) {
//                    url = url.replace("%", "%25");
//                    url = "https://img01.eshanyao.com/showImage2.php?url=" + url;
//                } else if (!url.startsWith("https:")) {
//                    url = server + chapterPath + url;
//                }
//                urlList.set(i, url);
//            }
//        }
//        return SourceHelper.getImageInfoList(urlList, chapterId);
//    }
//
//    @Override
//    public Map<String, String> getRankMap() {
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("????????????", "https://m.manhuafen.com/rank/popularity/?page=1");
//        map.put("????????????", "https://m.manhuafen.com/rank/click/?page=1");
//        map.put("????????????", "https://m.manhuafen.com/rank/subscribe/?page=1");
//        map.put("????????????", "https://m.manhuafen.com/update/?page=1");
//        String text = "<ul><li><a class=\"\" href=\"/list/rexue/\">??????</a></li><li><a class=\"\" href=\"/list/maoxian/\">??????</a></li><li><a class=\"\" href=\"/list/xuanhuan/\">??????</a></li><li><a class=\"\" href=\"/list/gaoxiao/\">??????</a></li><li><a class=\"\" href=\"/list/lianai/\">??????</a></li><li><a class=\"\" href=\"/list/chongwu/\">??????</a></li><li><a class=\"\" href=\"/list/xinzuo/\">??????</a></li><li><a class=\"\" href=\"/list/shenmo/\">??????</a></li><li><a class=\"\" href=\"/list/jingji/\">??????</a></li><li><a class=\"\" href=\"/list/chuanyue/\">??????</a></li><li><a class=\"\" href=\"/list/mangai/\">??????</a></li><li><a class=\"\" href=\"/list/bazong/\">??????</a></li><li><a class=\"\" href=\"/list/dushi/\">??????</a></li><li><a class=\"\" href=\"/list/wuxia/\">??????</a></li><li><a class=\"\" href=\"/list/shehui/\">??????</a></li><li><a class=\"\" href=\"/list/gufeng/\">??????</a></li><li><a class=\"\" href=\"/list/kongbu/\">??????</a></li><li><a class=\"\" href=\"/list/dongfang/\">??????</a></li><li><a class=\"\" href=\"/list/gedou/\">??????</a></li><li><a class=\"\" href=\"/list/mofa/\">??????</a></li><li><a class=\"\" href=\"/list/qingxiaoshuo/\">?????????</a></li><li><a class=\"\" href=\"/list/mohuan/\">??????</a></li><li><a class=\"\" href=\"/list/shenghuo/\">??????</a></li><li><a class=\"\" href=\"/list/huanlexiang/\">?????????</a></li><li><a class=\"\" href=\"/list/lizhi/\">??????</a></li><li><a class=\"\" href=\"/list/yinyuewudao/\">????????????</a></li><li><a class=\"active\" href=\"/list/\">??????</a></li><li><a class=\"\" href=\"/list/meishi/\">??????</a></li><li><a class=\"\" href=\"/list/jiecao/\">??????</a></li><li><a class=\"\" href=\"/list/shengui/\">??????</a></li><li><a class=\"\" href=\"/list/aiqing/\">??????</a></li><li><a class=\"\" href=\"/list/xiaoyuan/\">??????</a></li><li><a class=\"\" href=\"/list/zhiyu/\">??????</a></li><li><a class=\"\" href=\"/list/qihuan/\">??????</a></li><li><a class=\"\" href=\"/list/xianxia/\">??????</a></li><li><a class=\"\" href=\"/list/yundong/\">??????</a></li><li><a class=\"\" href=\"/list/dongzuo/\">??????</a></li><li><a class=\"\" href=\"/list/rigeng/\">??????</a></li><li><a class=\"\" href=\"/list/lishi/\">??????</a></li><li><a class=\"\" href=\"/list/tuili/\">??????</a></li><li><a class=\"\" href=\"/list/xuanyi/\">??????</a></li><li><a class=\"\" href=\"/list/xiuzhen/\">??????</a></li><li><a class=\"\" href=\"/list/youxi/\">??????</a></li><li><a class=\"\" href=\"/list/zhanzheng/\">??????</a></li><li><a class=\"\" href=\"/list/hougong/\">??????</a></li><li><a class=\"\" href=\"/list/zhichang/\">??????</a></li><li><a class=\"\" href=\"/list/sige/\">??????</a></li><li><a class=\"\" href=\"/list/xingzhuanhuan/\">?????????</a></li><li><a class=\"\" href=\"/list/weiniang/\">??????</a></li><li><a class=\"\" href=\"/list/yanyi/\">??????</a></li><li><a class=\"\" href=\"/list/zhenren/\">??????</a></li><li><a class=\"\" href=\"/list/zazhi/\">??????</a></li><li><a class=\"\" href=\"/list/zhentan/\">??????</a></li><li><a class=\"\" href=\"/list/mengxi/\">??????</a></li><li><a class=\"\" href=\"/list/danmei/\">??????</a></li><li><a class=\"\" href=\"/list/baihe/\">??????</a></li><li><a class=\"\" href=\"/list/xifangmohuan/\">????????????</a></li><li><a class=\"\" href=\"/list/jizhan/\">??????</a></li><li><a class=\"\" href=\"/list/zhaixi/\">??????</a></li><li><a class=\"\" href=\"/list/renzhe/\">??????</a></li><li><a class=\"\" href=\"/list/luoli/\">luoli</a></li><li><a class=\"\" href=\"/list/yishijie/\">?????????</a></li><li><a class=\"\" href=\"/list/xixie/\">??????</a></li><li><a class=\"\" href=\"/list/qita/\">??????</a></li><li><a class=\"\" href=\"/list/zhandou/\">??????</a></li><li><a class=\"\" href=\"/list/weimei/\">??????</a></li><li><a class=\"\" href=\"/list/egao/\">??????</a></li><li><a class=\"\" href=\"/list/huanxiang/\">??????</a></li><li><a class=\"\" href=\"/list/juqing/\">??????</a></li><li><a class=\"\" href=\"/list/yujie/\">??????</a></li><li><a class=\"\" href=\"/list/tiyu/\">??????</a></li><li><a class=\"\" href=\"/list/jianniang/\">??????</a></li><li><a class=\"\" href=\"/list/chunai/\">??????</a></li><li><a class=\"\" href=\"/list/nuexin/\">??????</a></li><li><a class=\"\" href=\"/list/gaoqingdanxing/\">????????????</a></li><li><a class=\"\" href=\"/list/qitaremenmanhua/\">??????????????????</a></li><li><a class=\"\" href=\"/list/mankezhan/\">?????????</a></li><li><a class=\"\" href=\"/list/badaozongcai/\">????????????</a></li><li><a class=\"\" href=\"/list/weiweiyuanchuang/\">????????????</a></li><li><a class=\"\" href=\"/list/jingsong/\">??????</a></li><li><a class=\"\" href=\"/list/zhiyinmanke/\">????????????</a></li><li><a class=\"\" href=\"/list/sasadongman/\">????????????</a></li><li><a class=\"\" href=\"/list/mingxing/\">??????</a></li><li><a class=\"\" href=\"/list/donghua/\">??????</a></li><li><a class=\"\" href=\"/list/jingpin/\">??????</a></li><li><a class=\"\" href=\"/list/xiuji/\">??????</a></li><li><a class=\"\" href=\"/list/xiaoshuogaibian/\">????????????</a></li><li><a class=\"\" href=\"/list/AA/\">AA</a></li><li><a class=\"\" href=\"/list/fangyi/\">??????</a></li><li><a class=\"\" href=\"/list/xitong/\">??????</a></li><li><a class=\"\" href=\"/list/mori/\">??????</a></li><li><a class=\"\" href=\"/list/fuchou/\">??????</a></li><li><a class=\"\" href=\"/list/lingyi/\">??????</a></li><li><a class=\"\" href=\"/list/shenxian/\">??????</a></li><li><a class=\"\" href=\"/list/richang/\">??????</a></li><li><a class=\"\" href=\"/list/gaibian/\">??????</a></li><li><a class=\"\" href=\"/list/shaonv/\">??????</a></li><li><a class=\"\" href=\"/list/zongcai/\">??????</a></li><li><a class=\"\" href=\"/list/juwei/\">??????</a></li><li><a class=\"\" href=\"/list/jingqi/\">??????</a></li><li><a class=\"\" href=\"/list/danvzhu/\">?????????</a></li><li><a class=\"\" href=\"/list/zhongsheng/\">??????</a></li><li><a class=\"\" href=\"/list/dianjing/\">??????</a></li><li><a class=\"\" href=\"/list/naodong/\">??????</a></li><li><a class=\"\" href=\"/list/qingchun/\">??????</a></li><li><a class=\"\" href=\"/list/nixi/\">??????</a></li><li><a class=\"\" href=\"/list/quanmou/\">??????</a></li><li><a class=\"\" href=\"/list/yulequan/\">?????????</a></li><li><a class=\"\" href=\"/list/langman/\">??????</a></li><li><a class=\"\" href=\"/list/shenhao/\">??????</a></li><li><a class=\"\" href=\"/list/gaotian/\">??????</a></li><li><a class=\"\" href=\"/list/guaiwu/\">??????</a></li><li><a class=\"\" href=\"/list/yaoguai/\">??????</a></li><li><a class=\"\" href=\"/list/gongdou/\">??????</a></li><li><a class=\"\" href=\"/list/xiuxian/\">??????</a></li><li><a class=\"\" href=\"/list/jijia/\">??????</a></li><li><a class=\"\" href=\"/list/sangshi/\">??????</a></li><li><a class=\"\" href=\"/list/lieqi/\">??????</a></li><li><a class=\"\" href=\"/list/haokuai/\">??????</a></li><li><a class=\"\" href=\"/list/unknown/\">??????</a></li><li><a class=\"\" href=\"/list/futa/\">??????</a></li></ul>";
//        JsoupStarter<Map<String, String>> starter = new JsoupStarter<Map<String, String>>() {
//            @Override
//            protected Map<String, String> dealElement(JsoupNode node, int elementId) {
//                String name = node.ownText("a");
//                String url = node.href("a");
//                Map<String, String> map = new HashMap<>();
//                map.put("name", name);
//                map.put("url", url);
//                return map;
//            }
//        };
//        List<Map<String, String>> mapList = starter.startElements(text, "a");
//        String s = "https://m.manhuafen.com%sclick/?page=1";
//        return SourceHelper.getRankMap(map, mapList, s);
//    }
//
//    @Override
//    public List<ComicInfo> getRankComicInfoList(String html) {
//        List<ComicInfo> list = getComicInfoList(html);
//        if (list.size() > 0) {
//            return list;
//        } else {
//            JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
//                @Override
//                protected ComicInfo dealElement(JsoupNode node, int elementId) {
//                    String title = node.ownText("a.txtA");
//                    String author = node.ownText("span.info a");
//                    String updateTime = null;
//                    String imgUrl = node.src("img");
//                    String detailUrl = node.href("a.txtA");
//                    if (author != null) {
//                        author = "????????????" + author;
//                    }
//                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
//                }
//            };
//            return starter.startElements(html, "ul#comic-items li");
//        }
//    }
//
//    private String decrypt(String code) {
//        String key = "KA58ZAQ321oobbG1";
//        String iv = "A1B2C3DEF1G321oo";
//        return DecryptUtil.decryptAES(code, key, iv);
//    }
//
//}
