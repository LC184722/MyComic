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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/11/26 20:17
 * @ver 1.0
 */
public class MH118 extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MH_118;
    }

    @Override
    public String getIndex() {
        return "http://m.ccshwy.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "http://www.ccshwy.com/statics/search.aspx?key=";
        return NetUtil.getRequest(url + searchString);
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("li.title a");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("li.title a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div.cy_list_mh ul");
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div.title h1");
                String imgUrl = node.src("div#Cover img");
                String author = node.ownText("p.txtItme:eq(1)");
                String intro = node.ownText("p.txtDesc");
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
                String chapterUrl = "http://m.ccshwy.com" + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(comicInfo, starter.startElements(html, "ul#mh-chapter-list-ol-0 li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String[] urls;
        try {
            String chapterStr = StringUtil.match("qTcms_S_m_murl_e=\"(.*?)\"", html);
            chapterStr = DecryptUtil.decryptBase64(chapterStr);
            urls = chapterStr.split("\\$qingtiandy\\$");
        } catch (Exception e) {
            urls = null;
            e.printStackTrace();
        }
        return SourceHelper.getImageInfoList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("人气排行", "http://www.ccshwy.com/paihang/");
        map.put("日韩排行", "http://www.ccshwy.com/paihang1101/");
        map.put("港台排行", "http://www.ccshwy.com/paihang1102/");
        map.put("欧美排行", "http://www.ccshwy.com/paihang1103/");
        map.put("内地排行", "http://www.ccshwy.com/paihang1104/");

        map.put("完结", "http://www.ccshwy.com/wanjie/");
        map.put("连载中", "http://www.ccshwy.com/lianzai/");

        map.put("日韩", "http://www.ccshwy.com/rihan/");
        map.put("内地", "http://www.ccshwy.com/neidi/");
        map.put("港台", "http://www.ccshwy.com/gangntai/");
        map.put("欧美", "http://www.ccshwy.com/oumei/");

        map.put("彩色", "http://www.ccshwy.com/caise/");
        map.put("黑白", "http://www.ccshwy.com/heibai/");

        map.put("少年", "http://www.ccshwy.com/shaonianqu/");
        map.put("少女", "http://www.ccshwy.com/shaonvqu/");
        map.put("青年", "http://www.ccshwy.com/qingnian/");
        map.put("少儿", "http://www.ccshwy.com/shaoer/");

        map.put("故事漫画", "http://www.ccshwy.com/gushimh/");
        map.put("四格漫画", "http://www.ccshwy.com/sigemh/");
        map.put("绘本漫画", "http://www.ccshwy.com/huibenmh/");

        map.put("热血", "http://www.ccshwy.com/rexue/");
        map.put("格斗", "http://www.ccshwy.com/gedou/");
        map.put("科幻", "http://www.ccshwy.com/kehuan/");
        map.put("竞技", "http://www.ccshwy.com/jingji/");
        map.put("搞笑", "http://www.ccshwy.com/gaoxiao/");
        map.put("推理", "http://www.ccshwy.com/tuili/");
        map.put("恐怖", "http://www.ccshwy.com/kongbu/");
        map.put("耽美", "http://www.ccshwy.com/danmei/");
        map.put("恋爱", "http://www.ccshwy.com/lianai/");
        map.put("生活", "http://www.ccshwy.com/shenghuo/");
        map.put("战争", "http://www.ccshwy.com/zhanzheng/");
        map.put("故事", "http://www.ccshwy.com/gushi/");
        map.put("冒险", "http://www.ccshwy.com/maoxian/");
        map.put("魔幻", "http://www.ccshwy.com/mohuan/");
        map.put("玄幻", "http://www.ccshwy.com/xuanhuan/");
        map.put("校园", "http://www.ccshwy.com/xiaoyuan/");
        map.put("悬疑", "http://www.ccshwy.com/xuanyi/");
        map.put("萌系", "http://www.ccshwy.com/mengxi/");
        map.put("穿越", "http://www.ccshwy.com/chuanyue/");
        map.put("后宫", "http://www.ccshwy.com/hougong/");
        map.put("都市", "http://www.ccshwy.com/dushi/");
        map.put("武侠", "http://www.ccshwy.com/wuxia/");
        map.put("历史", "http://www.ccshwy.com/lishi/");
        map.put("同人", "http://www.ccshwy.com/tongren/");
        map.put("励志", "http://www.ccshwy.com/lizhi/");
        map.put("百合", "http://www.ccshwy.com/baihe/");
        map.put("治愈", "http://www.ccshwy.com/zhiyu/");
        map.put("机甲", "http://www.ccshwy.com/jijia/");
        map.put("纯爱", "http://www.ccshwy.com/chunai/");
        map.put("美食", "http://www.ccshwy.com/meishi/");
        map.put("血腥", "http://www.ccshwy.com/xuexing/");
        map.put("僵尸", "http://www.ccshwy.com/jiangshi/");
        map.put("恶搞", "http://www.ccshwy.com/egao/");
        map.put("虐心", "http://www.ccshwy.com/nuexin/");
        map.put("动作", "http://www.ccshwy.com/dongzuo/");
        map.put("惊险", "http://www.ccshwy.com/jingxian/");
        map.put("唯美", "http://www.ccshwy.com/weimei/");
        map.put("震撼", "http://www.ccshwy.com/zhenhan/");
        map.put("复仇", "http://www.ccshwy.com/fuchou/");
        map.put("侦探", "http://www.ccshwy.com/zhentan/");
        map.put("脑洞", "http://www.ccshwy.com/naodong/");
        map.put("奇幻", "http://www.ccshwy.com/qihuan/");
        map.put("宫斗", "http://www.ccshwy.com/gongdou/");
        map.put("爆笑", "http://www.ccshwy.com/baoxiao/");
        map.put("运动", "http://www.ccshwy.com/yundong/");
        map.put("青春", "http://www.ccshwy.com/qingchun/");
        map.put("灵异", "http://www.ccshwy.com/lingyi/");
        map.put("古风", "http://www.ccshwy.com/gufeng/");
        map.put("权谋", "http://www.ccshwy.com/quanmou/");
        map.put("节操", "http://www.ccshwy.com/jiecao/");
        map.put("明星", "http://www.ccshwy.com/mingxing/");
        map.put("暗黑", "http://www.ccshwy.com/anhei/");
        map.put("社会", "http://www.ccshwy.com/shehui/");
        map.put("浪漫", "http://www.ccshwy.com/langman/");
        map.put("霸总", "http://www.ccshwy.com/lanmu/");
        map.put("仙侠", "http://www.ccshwy.com/xianxia/");
        return map;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("li.title a");
                String author = node.ownText("li.sx span");
                String updateTime = node.ownText("li.sx span span:eq(2) a");
                String imgUrl = node.src("img");
                String detailUrl = getIndex() + node.href("li.title a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        List<ComicInfo> list = new ArrayList<>(starter.startElements(html, "div.cy_ph_list_mh ul"));
        if (!list.isEmpty()) {
            return list;
        } else {
            return new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("li.title a");
                    String author = null;
                    String updateTime = node.ownText("li.updata span");
                    String imgUrl = node.src("img");
                    String detailUrl = getIndex() + node.href("li.title a");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            }.startElements(html, "div.cy_list_mh ul");
        }
    }

}