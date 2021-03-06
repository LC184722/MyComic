package top.luqichuang.mycomic.source;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.jsoup.JsoupStarter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.util.DecryptUtil;
import top.luqichuang.common.util.NetUtil;
import top.luqichuang.common.util.SourceHelper;
import top.luqichuang.common.util.StringUtil;
import top.luqichuang.mycomic.model.BaseComicSource;
import top.luqichuang.mycomic.model.ComicInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/11/26 20:17
 * @ver 1.0
 */
public class MH118 extends BaseComicSource {
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
        String url = "http://www.ccshwy.com/statics/search.aspx?key=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
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
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
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
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = "http://m.ccshwy.com" + node.href("a");
                if (title == null) {
                    return null;
                }
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#mh-chapter-list-ol-0 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls;
        try {
            String chapterStr = StringUtil.match("qTcms_S_m_murl_e=\"(.*?)\"", html);
            chapterStr = DecryptUtil.decryptBase64(chapterStr);
            urls = chapterStr.split("\\$qingtiandy\\$");
        } catch (Exception e) {
            urls = null;
            e.printStackTrace();
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("????????????", "http://www.ccshwy.com/paihang/");
        map.put("????????????", "http://www.ccshwy.com/paihang1101/");
        map.put("????????????", "http://www.ccshwy.com/paihang1102/");
        map.put("????????????", "http://www.ccshwy.com/paihang1103/");
        map.put("????????????", "http://www.ccshwy.com/paihang1104/");

        map.put("??????", "http://www.ccshwy.com/wanjie/");
        map.put("?????????", "http://www.ccshwy.com/lianzai/");

        map.put("??????", "http://www.ccshwy.com/rihan/");
        map.put("??????", "http://www.ccshwy.com/neidi/");
        map.put("??????", "http://www.ccshwy.com/gangntai/");
        map.put("??????", "http://www.ccshwy.com/oumei/");

        map.put("??????", "http://www.ccshwy.com/caise/");
        map.put("??????", "http://www.ccshwy.com/heibai/");

        map.put("??????", "http://www.ccshwy.com/shaonianqu/");
        map.put("??????", "http://www.ccshwy.com/shaonvqu/");
        map.put("??????", "http://www.ccshwy.com/qingnian/");
        map.put("??????", "http://www.ccshwy.com/shaoer/");

        map.put("????????????", "http://www.ccshwy.com/gushimh/");
        map.put("????????????", "http://www.ccshwy.com/sigemh/");
        map.put("????????????", "http://www.ccshwy.com/huibenmh/");

        map.put("??????", "http://www.ccshwy.com/rexue/");
        map.put("??????", "http://www.ccshwy.com/gedou/");
        map.put("??????", "http://www.ccshwy.com/kehuan/");
        map.put("??????", "http://www.ccshwy.com/jingji/");
        map.put("??????", "http://www.ccshwy.com/gaoxiao/");
        map.put("??????", "http://www.ccshwy.com/tuili/");
        map.put("??????", "http://www.ccshwy.com/kongbu/");
        map.put("??????", "http://www.ccshwy.com/danmei/");
        map.put("??????", "http://www.ccshwy.com/lianai/");
        map.put("??????", "http://www.ccshwy.com/shenghuo/");
        map.put("??????", "http://www.ccshwy.com/zhanzheng/");
        map.put("??????", "http://www.ccshwy.com/gushi/");
        map.put("??????", "http://www.ccshwy.com/maoxian/");
        map.put("??????", "http://www.ccshwy.com/mohuan/");
        map.put("??????", "http://www.ccshwy.com/xuanhuan/");
        map.put("??????", "http://www.ccshwy.com/xiaoyuan/");
        map.put("??????", "http://www.ccshwy.com/xuanyi/");
        map.put("??????", "http://www.ccshwy.com/mengxi/");
        map.put("??????", "http://www.ccshwy.com/chuanyue/");
        map.put("??????", "http://www.ccshwy.com/hougong/");
        map.put("??????", "http://www.ccshwy.com/dushi/");
        map.put("??????", "http://www.ccshwy.com/wuxia/");
        map.put("??????", "http://www.ccshwy.com/lishi/");
        map.put("??????", "http://www.ccshwy.com/tongren/");
        map.put("??????", "http://www.ccshwy.com/lizhi/");
        map.put("??????", "http://www.ccshwy.com/baihe/");
        map.put("??????", "http://www.ccshwy.com/zhiyu/");
        map.put("??????", "http://www.ccshwy.com/jijia/");
        map.put("??????", "http://www.ccshwy.com/chunai/");
        map.put("??????", "http://www.ccshwy.com/meishi/");
        map.put("??????", "http://www.ccshwy.com/xuexing/");
        map.put("??????", "http://www.ccshwy.com/jiangshi/");
        map.put("??????", "http://www.ccshwy.com/egao/");
        map.put("??????", "http://www.ccshwy.com/nuexin/");
        map.put("??????", "http://www.ccshwy.com/dongzuo/");
        map.put("??????", "http://www.ccshwy.com/jingxian/");
        map.put("??????", "http://www.ccshwy.com/weimei/");
        map.put("??????", "http://www.ccshwy.com/zhenhan/");
        map.put("??????", "http://www.ccshwy.com/fuchou/");
        map.put("??????", "http://www.ccshwy.com/zhentan/");
        map.put("??????", "http://www.ccshwy.com/naodong/");
        map.put("??????", "http://www.ccshwy.com/qihuan/");
        map.put("??????", "http://www.ccshwy.com/gongdou/");
        map.put("??????", "http://www.ccshwy.com/baoxiao/");
        map.put("??????", "http://www.ccshwy.com/yundong/");
        map.put("??????", "http://www.ccshwy.com/qingchun/");
        map.put("??????", "http://www.ccshwy.com/lingyi/");
        map.put("??????", "http://www.ccshwy.com/gufeng/");
        map.put("??????", "http://www.ccshwy.com/quanmou/");
        map.put("??????", "http://www.ccshwy.com/jiecao/");
        map.put("??????", "http://www.ccshwy.com/mingxing/");
        map.put("??????", "http://www.ccshwy.com/anhei/");
        map.put("??????", "http://www.ccshwy.com/shehui/");
        map.put("??????", "http://www.ccshwy.com/langman/");
        map.put("??????", "http://www.ccshwy.com/lanmu/");
        map.put("??????", "http://www.ccshwy.com/xianxia/");
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
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