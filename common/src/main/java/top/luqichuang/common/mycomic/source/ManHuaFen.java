package top.luqichuang.common.mycomic.source;

import top.luqichuang.common.mycomic.en.SourceEnum;
import top.luqichuang.common.mycomic.jsoup.JsoupNode;
import top.luqichuang.common.mycomic.jsoup.JsoupStarter;
import top.luqichuang.common.mycomic.model.ChapterInfo;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.model.ImageInfo;
import top.luqichuang.common.mycomic.util.DecryptUtil;
import top.luqichuang.common.mycomic.util.NetUtil;
import top.luqichuang.common.mycomic.util.SourceHelper;
import top.luqichuang.common.mycomic.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ManHuaFen extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MAN_HUA_FEN;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getIndex() {
        return "https://m.manhuafen.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://m.manhuafen.com/search/?keywords=" + searchString;
        return NetUtil.getRequest(searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.title");
                String author = node.ownText("p.txtItme");
                String updateTime = node.ownText("span.date");
                String imgUrl = node.src("img");
                String detailUrl = node.href("a.title");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.itemBox");
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
                String title = node.ownText("h1#comicName");
                String imgUrl = node.src("div#Cover img");
                String author = node.ownText("p.txtItme");
                String intro = node.ownText("p#full-des", "p#simple-des");
                String updateStatus = node.ownText("p.txtItme:eq(2) :eq(3)");
                String updateTime = node.ownText("p.txtItme span.date");
                try {
                    intro = intro.substring(intro.indexOf(':') + 1);
                } catch (Exception ignored) {
                }
                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = node.href("a");
                if (chapterUrl.contains("html")) {
                    if (!chapterUrl.startsWith("http")) {
                        chapterUrl = getIndex() + chapterUrl;
                    }
                    return new ChapterInfo(elementId, title, chapterUrl);
                } else {
                    return null;
                }
            }
        };
        starter.startInfo(html);
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String server = "https://img01.eshanyao.com/";
        String chapterImagesEncodeStr = StringUtil.match("var chapterImages = \"(.*?)\";", html);
        //var chapterPath = "images/comic/259/517692/";
        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
        String chapterImagesStr = decrypt(chapterImagesEncodeStr);
        List<String> urlList = null;
        if (chapterImagesStr != null) {
            chapterImagesStr = chapterImagesStr.replaceAll("\\\\", "");
            urlList = StringUtil.matchList("\"(.*?)\"", chapterImagesStr);
            for (int i = 0; i < urlList.size(); i++) {
                String url = urlList.get(i);
                if (url.startsWith("http:")) {
                    url = url.replace("%", "%25");
                    url = "https://img01.eshanyao.com/showImage2.php?url=" + url;
                } else if (!url.startsWith("https:")) {
                    url = server + chapterPath + url;
                }
                urlList.set(i, url);
            }
        }
        return SourceHelper.getImageInfoList(urlList, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("人气排行", "https://m.manhuafen.com/rank/popularity/?page=1");
        map.put("点击排行", "https://m.manhuafen.com/rank/click/?page=1");
        map.put("订阅排行", "https://m.manhuafen.com/rank/subscribe/?page=1");
        map.put("刚刚更新", "https://m.manhuafen.com/update/?page=1");
        String text = "<ul><li><a class=\"\" href=\"/list/rexue/\">热血</a></li><li><a class=\"\" href=\"/list/maoxian/\">冒险</a></li><li><a class=\"\" href=\"/list/xuanhuan/\">玄幻</a></li><li><a class=\"\" href=\"/list/gaoxiao/\">搞笑</a></li><li><a class=\"\" href=\"/list/lianai/\">恋爱</a></li><li><a class=\"\" href=\"/list/chongwu/\">宠物</a></li><li><a class=\"\" href=\"/list/xinzuo/\">新作</a></li><li><a class=\"\" href=\"/list/shenmo/\">神魔</a></li><li><a class=\"\" href=\"/list/jingji/\">竞技</a></li><li><a class=\"\" href=\"/list/chuanyue/\">穿越</a></li><li><a class=\"\" href=\"/list/mangai/\">漫改</a></li><li><a class=\"\" href=\"/list/bazong/\">霸总</a></li><li><a class=\"\" href=\"/list/dushi/\">都市</a></li><li><a class=\"\" href=\"/list/wuxia/\">武侠</a></li><li><a class=\"\" href=\"/list/shehui/\">社会</a></li><li><a class=\"\" href=\"/list/gufeng/\">古风</a></li><li><a class=\"\" href=\"/list/kongbu/\">恐怖</a></li><li><a class=\"\" href=\"/list/dongfang/\">东方</a></li><li><a class=\"\" href=\"/list/gedou/\">格斗</a></li><li><a class=\"\" href=\"/list/mofa/\">魔法</a></li><li><a class=\"\" href=\"/list/qingxiaoshuo/\">轻小说</a></li><li><a class=\"\" href=\"/list/mohuan/\">魔幻</a></li><li><a class=\"\" href=\"/list/shenghuo/\">生活</a></li><li><a class=\"\" href=\"/list/huanlexiang/\">欢乐向</a></li><li><a class=\"\" href=\"/list/lizhi/\">励志</a></li><li><a class=\"\" href=\"/list/yinyuewudao/\">音乐舞蹈</a></li><li><a class=\"active\" href=\"/list/\">科幻</a></li><li><a class=\"\" href=\"/list/meishi/\">美食</a></li><li><a class=\"\" href=\"/list/jiecao/\">节操</a></li><li><a class=\"\" href=\"/list/shengui/\">神鬼</a></li><li><a class=\"\" href=\"/list/aiqing/\">爱情</a></li><li><a class=\"\" href=\"/list/xiaoyuan/\">校园</a></li><li><a class=\"\" href=\"/list/zhiyu/\">治愈</a></li><li><a class=\"\" href=\"/list/qihuan/\">奇幻</a></li><li><a class=\"\" href=\"/list/xianxia/\">仙侠</a></li><li><a class=\"\" href=\"/list/yundong/\">运动</a></li><li><a class=\"\" href=\"/list/dongzuo/\">动作</a></li><li><a class=\"\" href=\"/list/rigeng/\">日更</a></li><li><a class=\"\" href=\"/list/lishi/\">历史</a></li><li><a class=\"\" href=\"/list/tuili/\">推理</a></li><li><a class=\"\" href=\"/list/xuanyi/\">悬疑</a></li><li><a class=\"\" href=\"/list/xiuzhen/\">修真</a></li><li><a class=\"\" href=\"/list/youxi/\">游戏</a></li><li><a class=\"\" href=\"/list/zhanzheng/\">战争</a></li><li><a class=\"\" href=\"/list/hougong/\">后宫</a></li><li><a class=\"\" href=\"/list/zhichang/\">职场</a></li><li><a class=\"\" href=\"/list/sige/\">四格</a></li><li><a class=\"\" href=\"/list/xingzhuanhuan/\">性转换</a></li><li><a class=\"\" href=\"/list/weiniang/\">伪娘</a></li><li><a class=\"\" href=\"/list/yanyi/\">颜艺</a></li><li><a class=\"\" href=\"/list/zhenren/\">真人</a></li><li><a class=\"\" href=\"/list/zazhi/\">杂志</a></li><li><a class=\"\" href=\"/list/zhentan/\">侦探</a></li><li><a class=\"\" href=\"/list/mengxi/\">萌系</a></li><li><a class=\"\" href=\"/list/danmei/\">耽美</a></li><li><a class=\"\" href=\"/list/baihe/\">百合</a></li><li><a class=\"\" href=\"/list/xifangmohuan/\">西方魔幻</a></li><li><a class=\"\" href=\"/list/jizhan/\">机战</a></li><li><a class=\"\" href=\"/list/zhaixi/\">宅系</a></li><li><a class=\"\" href=\"/list/renzhe/\">忍者</a></li><li><a class=\"\" href=\"/list/luoli/\">luoli</a></li><li><a class=\"\" href=\"/list/yishijie/\">异世界</a></li><li><a class=\"\" href=\"/list/xixie/\">吸血</a></li><li><a class=\"\" href=\"/list/qita/\">其他</a></li><li><a class=\"\" href=\"/list/zhandou/\">战斗</a></li><li><a class=\"\" href=\"/list/weimei/\">唯美</a></li><li><a class=\"\" href=\"/list/egao/\">恶搞</a></li><li><a class=\"\" href=\"/list/huanxiang/\">幻想</a></li><li><a class=\"\" href=\"/list/juqing/\">剧情</a></li><li><a class=\"\" href=\"/list/yujie/\">御姐</a></li><li><a class=\"\" href=\"/list/tiyu/\">体育</a></li><li><a class=\"\" href=\"/list/jianniang/\">舰娘</a></li><li><a class=\"\" href=\"/list/chunai/\">纯爱</a></li><li><a class=\"\" href=\"/list/nuexin/\">虐心</a></li><li><a class=\"\" href=\"/list/gaoqingdanxing/\">高清单行</a></li><li><a class=\"\" href=\"/list/qitaremenmanhua/\">其他热门漫画</a></li><li><a class=\"\" href=\"/list/mankezhan/\">漫客栈</a></li><li><a class=\"\" href=\"/list/badaozongcai/\">霸道总裁</a></li><li><a class=\"\" href=\"/list/weiweiyuanchuang/\">薇薇原创</a></li><li><a class=\"\" href=\"/list/jingsong/\">惊悚</a></li><li><a class=\"\" href=\"/list/zhiyinmanke/\">知音漫客</a></li><li><a class=\"\" href=\"/list/sasadongman/\">飒飒动漫</a></li><li><a class=\"\" href=\"/list/mingxing/\">明星</a></li><li><a class=\"\" href=\"/list/donghua/\">动画</a></li><li><a class=\"\" href=\"/list/jingpin/\">精品</a></li><li><a class=\"\" href=\"/list/xiuji/\">秀吉</a></li><li><a class=\"\" href=\"/list/xiaoshuogaibian/\">小说改编</a></li><li><a class=\"\" href=\"/list/AA/\">AA</a></li><li><a class=\"\" href=\"/list/fangyi/\">防疫</a></li><li><a class=\"\" href=\"/list/xitong/\">系统</a></li><li><a class=\"\" href=\"/list/mori/\">末日</a></li><li><a class=\"\" href=\"/list/fuchou/\">复仇</a></li><li><a class=\"\" href=\"/list/lingyi/\">灵异</a></li><li><a class=\"\" href=\"/list/shenxian/\">神仙</a></li><li><a class=\"\" href=\"/list/richang/\">日常</a></li><li><a class=\"\" href=\"/list/gaibian/\">改编</a></li><li><a class=\"\" href=\"/list/shaonv/\">少女</a></li><li><a class=\"\" href=\"/list/zongcai/\">总裁</a></li><li><a class=\"\" href=\"/list/juwei/\">橘味</a></li><li><a class=\"\" href=\"/list/jingqi/\">惊奇</a></li><li><a class=\"\" href=\"/list/danvzhu/\">大女主</a></li><li><a class=\"\" href=\"/list/zhongsheng/\">重生</a></li><li><a class=\"\" href=\"/list/dianjing/\">电竞</a></li><li><a class=\"\" href=\"/list/naodong/\">脑洞</a></li><li><a class=\"\" href=\"/list/qingchun/\">青春</a></li><li><a class=\"\" href=\"/list/nixi/\">逆袭</a></li><li><a class=\"\" href=\"/list/quanmou/\">权谋</a></li><li><a class=\"\" href=\"/list/yulequan/\">娱乐圈</a></li><li><a class=\"\" href=\"/list/langman/\">浪漫</a></li><li><a class=\"\" href=\"/list/shenhao/\">神豪</a></li><li><a class=\"\" href=\"/list/gaotian/\">高甜</a></li><li><a class=\"\" href=\"/list/guaiwu/\">怪物</a></li><li><a class=\"\" href=\"/list/yaoguai/\">妖怪</a></li><li><a class=\"\" href=\"/list/gongdou/\">宫斗</a></li><li><a class=\"\" href=\"/list/xiuxian/\">修仙</a></li><li><a class=\"\" href=\"/list/jijia/\">机甲</a></li><li><a class=\"\" href=\"/list/sangshi/\">丧尸</a></li><li><a class=\"\" href=\"/list/lieqi/\">猎奇</a></li><li><a class=\"\" href=\"/list/haokuai/\">豪快</a></li><li><a class=\"\" href=\"/list/unknown/\">ゆり</a></li><li><a class=\"\" href=\"/list/futa/\">扶她</a></li></ul>";
        JsoupStarter<Map<String, String>> starter = new JsoupStarter<Map<String, String>>() {
            @Override
            protected Map<String, String> dealElement(JsoupNode node, int elementId) {
                String name = node.ownText("a");
                String url = node.href("a");
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("url", url);
                return map;
            }
        };
        List<Map<String, String>> mapList = starter.startElements(text, "a");
        String s = "https://m.manhuafen.com%sclick/?page=1";
        return SourceHelper.getRankMap(map, mapList, s);
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        List<ComicInfo> list = getComicInfoList(html);
        if (list.size() > 0) {
            return list;
        } else {
            JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("a.txtA");
                    String author = node.ownText("span.info a");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = node.href("a.txtA");
                    if (author != null) {
                        author = "更新至：" + author;
                    }
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            };
            return starter.startElements(html, "ul#comic-items li");
        }
    }

    private String decrypt(String code) {
        String key = "KA58ZAQ321oobbG1";
        String iv = "A1B2C3DEF1G321oo";
        return DecryptUtil.decryptAES(code, key, iv);
    }

}
