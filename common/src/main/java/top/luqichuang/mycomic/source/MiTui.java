package top.luqichuang.mycomic.source;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
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
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MiTui extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MI_TUI;
    }

    @Override
    public Map<String, String> getImageHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", getIndex());
        return headers;
    }

    @Override
    public String getIndex() {
        return "https://www.imitui.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = "https://m.imitui.com/search/?keywords=" + searchString;
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
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
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected boolean isDESC() {
                return false;
            }

            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("div#comicName");
                String imgUrl = node.src("div#Cover img");
                String author = node.ownText("p.txtItme");
                String intro = node.ownText("p#full-des", "p#simple-des");
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
                String chapterUrl = node.href("a");
                if (chapterUrl.contains("html")) {
                    if (!chapterUrl.startsWith("http")) {
                        chapterUrl = getIndex() + chapterUrl;
                    } else {
                        chapterUrl = chapterUrl.replace("//m.", "//www.");
                    }
                    return new ChapterInfo(elementId, title, chapterUrl);
                } else {
                    return null;
                }
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String chapterImagesStr = StringUtil.match("chapterImages = \\[(.*?)\\]", html);
        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
        chapterPath = "";
        String chapterImageHost = StringUtil.match("var chapterImageHost = \"(.*?)\";", html);
        String[] urls = null;
        if (chapterImagesStr != null && !chapterImagesStr.equals("")) {
            urls = chapterImagesStr.split(",");
            String server = "https://resnode.yxtun.com/";
            if (chapterImageHost != null && !chapterImageHost.equals("")) {
                server = chapterImageHost;
            }
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                url = url.replace("\"", "").replace("\\", "");
                if (!url.startsWith("http")) {
                    urls[i] = server + chapterPath + url;
                } else {
                    urls[i] = url;
                }
            }
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("????????????", "https://m.imitui.com/rank/popularity/?page=1");
        map.put("????????????", "https://m.imitui.com/rank/click/?page=1");
        map.put("????????????", "https://m.imitui.com/rank/subscribe/?page=1");
        map.put("????????????", "https://m.imitui.com/update/?page=1");
        String text = "<ul><li><a class=\"\" href=\"/list/rexue/\">??????</a></li><li><a class=\"\" href=\"/list/xuanhuan/\">??????</a></li><li><a class=\"\" href=\"/list/xiuzhen/\">??????</a></li><li><a class=\"\" href=\"/list/gufeng/\">??????</a></li><li><a class=\"\" href=\"/list/lianai/\">??????</a></li><li><a class=\"\" href=\"/list/chuanyue/\">??????</a></li><li><a class=\"\" href=\"/list/dushi/\">??????</a></li><li><a class=\"\" href=\"/list/bazong/\">??????</a></li><li><a class=\"\" href=\"/list/xuanyi/\">??????</a></li><li><a class=\"\" href=\"/list/gaoxiao/\">??????</a></li><li><a class=\"\" href=\"/list/qihuan/\">??????</a></li><li><a class=\"\" href=\"/list/zongcai/\">??????</a></li><li><a class=\"\" href=\"/list/richang/\">??????</a></li><li><a class=\"\" href=\"/list/maoxian/\">??????</a></li><li><a class=\"\" href=\"/list/kehuan/\">??????</a></li><li><a class=\"\" href=\"/list/chunai/\">??????</a></li><li><a class=\"\" href=\"/list/mohuan/\">??????</a></li><li><a class=\"\" href=\"/list/zhanzheng/\">??????</a></li><li><a class=\"\" href=\"/list/qiangwei/\">??????</a></li><li><a class=\"\" href=\"/list/wuxia/\">??????</a></li><li><a class=\"\" href=\"/list/shenghuo/\">??????</a></li><li><a class=\"\" href=\"/list/dongzuo/\">??????</a></li><li><a class=\"\" href=\"/list/hougong/\">??????</a></li><li><a class=\"\" href=\"/list/youxi/\">??????</a></li><li><a class=\"\" href=\"/list/kongbu/\">??????</a></li><li><a class=\"\" href=\"/list/mangai/\">??????</a></li><li><a class=\"\" href=\"/list/zhenren/\">??????</a></li><li><a class=\"\" href=\"/list/xiaoyuan/\">??????</a></li><li><a class=\"\" href=\"/list/juqing/\">??????</a></li><li><a class=\"\" href=\"/list/lingyi/\">??????</a></li><li><a class=\"\" href=\"/list/shaonian/\">??????</a></li><li><a class=\"\" href=\"/list/tuili/\">??????</a></li><li><a class=\"\" href=\"/list/huaijiu/\">??????</a></li><li><a class=\"\" href=\"/list/qinggan/\">??????</a></li><li><a class=\"\" href=\"/list/ouxiang/\">??????</a></li><li><a class=\"\" href=\"/list/shaonv/\">??????</a></li><li><a class=\"\" href=\"/list/dujia/\">??????</a></li><li><a class=\"\" href=\"/list/nuexin/\">??????</a></li><li><a class=\"\" href=\"/list/baoxiao/\">??????</a></li><li><a class=\"\" href=\"/list/lizhi/\">??????</a></li><li><a class=\"\" href=\"/list/meishi/\">??????</a></li><li><a class=\"\" href=\"/list/fuchou/\">??????</a></li><li><a class=\"\" href=\"/list/caihong/\">??????</a></li><li><a class=\"\" href=\"/list/weimei/\">??????</a></li><li><a class=\"\" href=\"/list/zhiyu/\">??????</a></li><li><a class=\"\" href=\"/list/mingxing/\">??????</a></li><li><a class=\"\" href=\"/list/naodong/\">??????</a></li><li><a class=\"\" href=\"/list/mofa/\">??????</a></li><li><a class=\"\" href=\"/list/xiuxian/\">??????</a></li><li><a class=\"\" href=\"/list/zhongsheng/\">??????</a></li><li><a class=\"\" href=\"/list/xianxia/\">??????</a></li><li><a class=\"\" href=\"/list/moshi/\">??????</a></li><li><a class=\"\" href=\"/list/yineng/\">??????</a></li><li><a class=\"\" href=\"/list/nvzun/\">??????</a></li><li><a class=\"\" href=\"/list/qita/\">??????</a></li><li><a class=\"\" href=\"/list/yanqing/\">??????</a></li><li><a class=\"\" href=\"/list/danmei/\">??????</a></li><li><a class=\"\" href=\"/list/yundong/\">??????</a></li><li><a class=\"\" href=\"/list/gongdou/\">??????</a></li><li><a class=\"\" href=\"/list/guzhuang/\">??????</a></li><li><a class=\"\" href=\"/list/meishaonv/\">?????????</a></li><li><a class=\"\" href=\"/list/shenmo/\">??????</a></li><li><a class=\"\" href=\"/list/lishi/\">??????</a></li><li><a class=\"\" href=\"/list/jingxian/\">??????</a></li><li><a class=\"\" href=\"/list/jingji/\">??????</a></li><li><a class=\"\" href=\"/list/mengxi/\">??????</a></li><li><a class=\"\" href=\"/list/tiyu/\">??????</a></li><li><a class=\"\" href=\"/list/gedou/\">??????</a></li><li><a class=\"\" href=\"/list/jijia/\">??????</a></li><li><a class=\"\" href=\"/list/nuelian/\">??????</a></li><li><a class=\"\" href=\"/list/shuang/\">???</a></li><li><a class=\"\" href=\"/list/fuli/\">??????</a></li><li><a class=\"\" href=\"/list/qita2/\">??????</a></li><li><a class=\"\" href=\"/list/xiaojiangshi/\">?????????</a></li><li><a class=\"\" href=\"/list/jiangshi/\">??????</a></li><li><a class=\"\" href=\"/list/langman/\">??????</a></li><li><a class=\"\" href=\"/list/jinshouzhi/\">?????????</a></li><li><a class=\"\" href=\"/list/yujie/\">??????</a></li><li><a class=\"\" href=\"/list/zhandou/\">??????</a></li><li><a class=\"\" href=\"/list/egao/\">??????</a></li><li><a class=\"\" href=\"/list/shehui/\">??????</a></li><li><a class=\"\" href=\"/list/quanmou/\">??????</a></li><li><a class=\"\" href=\"/list/qingchun/\">??????</a></li><li><a class=\"\" href=\"/list/luoli/\">??????</a></li><li><a class=\"\" href=\"/list/tongren/\">??????</a></li><li><a class=\"\" href=\"/list/zhenhan/\">??????</a></li><li><a class=\"\" href=\"/list/riman/\">??????</a></li><li><a class=\"\" href=\"/list/junfa/\">??????</a></li><li><a class=\"\" href=\"/list/minguo/\">??????</a></li><li><a class=\"\" href=\"/list/tegong/\">??????</a></li><li><a class=\"\" href=\"/list/meinv/\">??????</a></li><li><a class=\"\" href=\"/list/jiandie/\">??????</a></li><li><a class=\"\" href=\"/list/anhei/\">??????</a></li><li><a class=\"\" href=\"/list/jiecao/\">??????</a></li><li><a class=\"\" href=\"/list/jingdian/\">??????</a></li><li><a class=\"\" href=\"/list/youmo/\">??????</a></li><li><a class=\"\" href=\"/list/tianchong/\">??????</a></li><li><a class=\"\" href=\"/list/shenhua/\">??????</a></li><li><a class=\"\" href=\"/list/riben/\">??????</a></li><li><a class=\"\" href=\"/list/yijiyuan/\">?????????</a></li><li><a class=\"\" href=\"/list/tiaoman/\">??????</a></li><li><a class=\"\" href=\"/list/LOL/\">LOL</a></li><li><a class=\"\" href=\"/list/zhongtian/\">??????</a></li><li><a class=\"\" href=\"/list/duanpian/\">??????</a></li><li><a class=\"\" href=\"/list/jingsong/\">??????</a></li><li><a class=\"\" href=\"/list/sige/\">??????</a></li><li><a class=\"\" href=\"/list/guoman/\">??????</a></li><li><a class=\"\" href=\"/list/youqudao/\">?????????</a></li><li><a class=\"\" href=\"/list/mengchong/\">??????</a></li><li><a class=\"\" href=\"/list/renxing/\">??????</a></li><li><a class=\"\" href=\"/list/chonghun/\">??????</a></li><li><a class=\"\" href=\"/list/xinqi/\">??????</a></li><li><a class=\"\" href=\"/list/xixiegui/\">?????????</a></li><li><a class=\"\" href=\"/list/shenjiemanhua/\">????????????</a></li><li><a class=\"\" href=\"/list/xueyuehua/\">?????????</a></li><li><a class=\"\" href=\"/list/mengqishi/\">?????????</a></li><li><a class=\"\" href=\"/list/shouer/\">??????</a></li><li><a class=\"\" href=\"/list/shaoer/\">??????</a></li><li><a class=\"\" href=\"/list/baihe/\">??????</a></li><li><a class=\"\" href=\"/list/chiji/\">??????</a></li><li><a class=\"\" href=\"/list/qiangzhan/\">??????</a></li><li><a class=\"\" href=\"/list/tezhongbing/\">?????????</a></li><li><a class=\"\" href=\"/list/xiongdi/\">??????</a></li><li><a class=\"\" href=\"/list/yishi/\">??????</a></li><li><a class=\"\" href=\"/list/xiongmei/\">??????</a></li><li><a class=\"\" href=\"/list/sanciyuan/\">?????????</a></li><li><a class=\"\" href=\"/list/meixing/\">??????</a></li><li><a class=\"\" href=\"/list/haomen/\">??????</a></li><li><a class=\"\" href=\"/list/hunchong/\">??????</a></li><li><a class=\"\" href=\"/list/kaigua/\">??????</a></li><li><a class=\"\" href=\"/list/xuexing/\">??????</a></li><li><a class=\"\" href=\"/list/qingsong/\">??????</a></li><li><a class=\"\" href=\"/list/yangcheng/\">??????</a></li><li><a class=\"\" href=\"/list/tishen/\">??????</a></li><li><a class=\"\" href=\"/list/nanshen/\">??????</a></li><li><a class=\"\" href=\"/list/qingqingshu/\">?????????</a></li><li><a class=\"\" href=\"/list/yishijie/\">?????????</a></li><li><a class=\"\" href=\"/list/nanchuannv/\">?????????</a></li><li><a class=\"\" href=\"/list/hunchuan/\">??????</a></li><li><a class=\"\" href=\"/list/yinan/\">??????</a></li><li><a class=\"\" href=\"/list/dujitang/\">?????????</a></li><li><a class=\"\" href=\"/list/pianyu/\">??????</a></li><li><a class=\"\" href=\"/list/manhuahui/\">?????????</a></li><li><a class=\"\" href=\"/list/longren/\">??????</a></li><li><a class=\"\" href=\"/list/xihuan/\">??????</a></li><li><a class=\"\" href=\"/list/zhaohuan/\">??????</a></li><li><a class=\"\" href=\"/list/yijie/\">??????</a></li><li><a class=\"\" href=\"/list/henxiyou/\">?????????</a></li><li><a class=\"\" href=\"/list/xiyouji/\">?????????</a></li><li><a class=\"\" href=\"/list/jianghu/\">??????</a></li><li><a class=\"\" href=\"/list/duantoudao/\">?????????</a></li><li><a class=\"\" href=\"/list/hanman/\">??????</a></li><li><a class=\"\" href=\"/list/bingjiao/\">??????</a></li><li><a class=\"\" href=\"/list/changpian/\">??????</a></li><li><a class=\"\" href=\"/list/chenlan/\">??????</a></li><li><a class=\"\" href=\"/list/aiqing/\">??????</a></li><li><a class=\"\" href=\"/list/nvqiang/\">??????</a></li><li><a class=\"\" href=\"/list/ranxiang/\">??????</a></li><li><a class=\"\" href=\"/list/tianshangkong/\">?????????</a></li><li><a class=\"\" href=\"/list/duzhiniao/\">?????????</a></li><li><a class=\"\" href=\"/list/xuezu/\">??????</a></li><li><a class=\"\" href=\"/list/mowang/\">??????</a></li><li><a class=\"\" href=\"/list/keai/\">??????</a></li><li><a class=\"\" href=\"/list/gongting/\">??????</a></li><li><a class=\"\" href=\"/list/hunlian/\">??????</a></li><li><a class=\"\" href=\"/list/meng/\">???</a></li><li><a class=\"\" href=\"/list/ashuai/\">??????</a></li><li><a class=\"\" href=\"/list/sanjiaolian/\">?????????</a></li><li><a class=\"\" href=\"/list/qianshi/\">??????</a></li><li><a class=\"\" href=\"/list/lunhui/\">??????</a></li><li><a class=\"\" href=\"/list/jingqi/\">??????</a></li><li><a class=\"\" href=\"/list/zhentan/\">??????</a></li><li><a class=\"\" href=\"/list/huanlexiang/\">?????????</a></li><li><a class=\"\" href=\"/list/zhichang/\">??????</a></li><li><a class=\"\" href=\"/list/gandong/\">??????</a></li><li><a class=\"\" href=\"/list/jiakong/\">??????</a></li><li><a class=\"\" href=\"/list/qingxiaoshuo/\">?????????</a></li><li><a class=\"\" href=\"/list/yanyi/\">??????</a></li><li><a class=\"\" href=\"/list/xingzhuanhuan/\">?????????</a></li><li><a class=\"\" href=\"/list/dongfang/\">??????</a></li><li><a class=\"\" href=\"/list/danmeiBL/\">??????BL</a></li><li><a class=\"\" href=\"/list/qingsonggaoxiao/\">????????????</a></li><li><a class=\"\" href=\"/list/tongrenmanhua/\">????????????</a></li><li><a class=\"\" href=\"/list/xiaoyuangaoxiaoshenghuo/\">??????????????????</a></li><li><a class=\"\" href=\"/list/shaonvaiqing/\">????????????</a></li><li><a class=\"\" href=\"/list/zhengju/\">??????</a></li><li><a class=\"\" href=\"/list/shaonao/\">??????</a></li><li><a class=\"\" href=\"/list/zhuangbi/\">??????</a></li><li><a class=\"\" href=\"/list/shengui/\">??????</a></li><li><a class=\"\" href=\"/list/weiniang/\">??????</a></li><li><a class=\"\" href=\"/list/gaoqingdanxing/\">????????????</a></li><li><a class=\"\" href=\"/list/gushimanhua/\">????????????</a></li><li><a class=\"\" href=\"/list/lianaishenghuoxuanhuan/\">??????????????????</a></li><li><a class=\"\" href=\"/list/xifangmohuan/\">????????????</a></li><li><a class=\"\" href=\"/list/jianniang/\">??????</a></li><li><a class=\"\" href=\"/list/zhaixi/\">??????</a></li><li><a class=\"\" href=\"/list/shangzhan/\">??????</a></li><li><a class=\"\" href=\"/list/shuangliu/\">??????</a></li><li><a class=\"\" href=\"/list/rexuemaoxian/\">????????????</a></li><li><a class=\"\" href=\"/list/keji/\">??????</a></li><li><a class=\"\" href=\"/list/wenxin/\">??????</a></li><li><a class=\"\" href=\"/list/jiating/\">??????</a></li><li><a class=\"\" href=\"/list/hunyin/\">??????</a></li><li><a class=\"\" href=\"/list/duanzi/\">??????</a></li><li><a class=\"\" href=\"/list/neihan/\">??????</a></li><li><a class=\"\" href=\"/list/jizhan/\">??????</a></li><li><a class=\"\" href=\"/list/yulequan/\">?????????</a></li><li><a class=\"\" href=\"/list/weilai/\">??????</a></li><li><a class=\"\" href=\"/list/chongwu/\">??????</a></li><li><a class=\"\" href=\"/list/bazonglianaixuanhuan/\">??????????????????</a></li><li><a class=\"\" href=\"/list/gushi/\">??????</a></li><li><a class=\"\" href=\"/list/yinyuewudao/\">????????????</a></li><li><a class=\"\" href=\"/list/nixi/\">??????</a></li><li><a class=\"\" href=\"/list/zhaohuanshou/\">?????????</a></li><li><a class=\"\" href=\"/list/kehuanmohuan/\">????????????</a></li><li><a class=\"\" href=\"/list/jiujie/\">??????</a></li><li><a class=\"\" href=\"/list/lunli/\">??????</a></li><li><a class=\"\" href=\"/list/lianaishenghuo/\">????????????</a></li><li><a class=\"\" href=\"/list/xinzuo/\">??????</a></li><li><a class=\"\" href=\"/list/lishimanhua/\">????????????</a></li><li><a class=\"\" href=\"/list/ertong/\">??????</a></li><li><a class=\"\" href=\"/list/zhentantuili/\">????????????</a></li><li><a class=\"\" href=\"/list/xiuzhenlianaijiakong/\">??????????????????</a></li><li><a class=\"\" href=\"/list/shougong/\">??????</a></li><li><a class=\"\" href=\"/list/qingnian/\">??????</a></li><li><a class=\"\" href=\"/list/qitamanhua/\">????????????</a></li><li><a class=\"\" href=\"/list/zhiyu2/\">??????</a></li><li><a class=\"\" href=\"/list/shishi/\">??????</a></li><li><a class=\"\" href=\"/list/xiuji/\">??????</a></li><li><a class=\"\" href=\"/list/xiangcun/\">??????</a></li><li><a class=\"\" href=\"/list/xingzhuan/\">??????</a></li><li><a class=\"\" href=\"/list/hunai/\">??????</a></li><li><a class=\"\" href=\"/list/siwang/\">??????</a></li><li><a class=\"\" href=\"/list/sishen/\">??????</a></li><li><a class=\"\" href=\"/list/shaonan/\">??????</a></li><li><a class=\"\" href=\"/list/xuanyijingsong/\">???????????????</a></li><li><a class=\"\" href=\"/list/baoxiaoxiju/\">????????????</a></li><li><a class=\"\" href=\"/list/dongzuogedou/\">????????????</a></li><li><a class=\"\" href=\"/list/gaibian/\">??????</a></li><li><a class=\"\" href=\"/list/AA/\">AA</a></li><li><a class=\"\" href=\"/list/lianaidanmei/\">????????????</a></li><li><a class=\"\" href=\"/list/heidao/\">??????</a></li><li><a class=\"\" href=\"/list/guiguai/\">??????</a></li><li><a class=\"\" href=\"/list/sangshi/\">??????</a></li><li><a class=\"\" href=\"/list/zhupu/\">??????</a></li><li><a class=\"\" href=\"/list/zhiyinmanke/\">????????????</a></li><li><a class=\"\" href=\"/list/maimeng/\">??????</a></li><li><a class=\"\" href=\"/list/nizhuan/\">??????</a></li><li><a class=\"\" href=\"/list/danvzhu/\">?????????</a></li><li><a class=\"\" href=\"/list/aimei/\">??????</a></li><li><a class=\"\" href=\"/list/shenghua/\">??????</a></li><li><a class=\"\" href=\"/list/qiwen/\">??????</a></li><li><a class=\"\" href=\"/list/zhaidou/\">??????</a></li><li><a class=\"\" href=\"/list/lanmu/\">??????</a></li><li><a class=\"\" href=\"/list/guaitan/\">??????</a></li><li><a class=\"\" href=\"/list/chongai/\">??????</a></li><li><a class=\"\" href=\"/list/huanxiang/\">??????</a></li><li><a class=\"\" href=\"/list/yizu/\">??????</a></li><li><a class=\"\" href=\"/list/tanan/\">??????</a></li><li><a class=\"\" href=\"/list/panni/\">??????</a></li><li><a class=\"\" href=\"/list/juwei/\">??????</a></li><li><a class=\"\" href=\"/list/yinv/\">??????</a></li><li><a class=\"\" href=\"/list/lieqi/\">??????</a></li><li><a class=\"\" href=\"/list/rigeng/\">??????</a></li><li><a class=\"\" href=\"/list/manman/\">??????</a></li><li><a class=\"\" href=\"/list/zhidou/\">??????</a></li><li><a class=\"\" href=\"/list/zhengnengliang/\">?????????</a></li><li><a class=\"\" href=\"/list/manhuayifan/\">????????????</a></li><li><a class=\"\" href=\"/list/nvwangdiankeng/\">????????????</a></li><li><a class=\"\" href=\"/list/mankezhan/\">?????????</a></li><li><a class=\"\" href=\"/list/samanhua/\">?????????</a></li><li><a class=\"\" href=\"/list/xiaoshuogaibian/\">????????????</a></li><li><a class=\"\" href=\"/list/shenshi/\">??????</a></li><li><a class=\"\" href=\"/list/kongbuxuanyi/\">????????????</a></li><li><a class=\"\" href=\"/list/huiben/\">??????</a></li><li><a class=\"\" href=\"/list/yinyue/\">??????</a></li><li><a class=\"\" href=\"/list/huxian/\">??????</a></li><li><a class=\"\" href=\"/list/sihoushijie/\">????????????</a></li><li><a class=\"\" href=\"/list/motong/\">??????</a></li><li><a class=\"\" href=\"/list/manhua/\">??????</a></li><li><a class=\"\" href=\"/list/mori/\">??????</a></li><li><a class=\"\" href=\"/list/xitong/\">??????</a></li><li><a class=\"\" href=\"/list/shenxian/\">??????</a></li><li><a class=\"\" href=\"/list/youyaoqi/\">?????????</a></li><li><a class=\"\" href=\"/list/guaiwu/\">??????</a></li><li><a class=\"\" href=\"/list/yaoguai/\">??????</a></li><li><a class=\"\" href=\"/list/shenhao/\">??????</a></li><li><a class=\"\" href=\"/list/bazongdushi/\">??????.??????</a></li><li><a class=\"\" href=\"/list/gaotian/\">??????</a></li><li><a class=\"\" href=\"/list/xianzhiji/\">?????????</a></li><li><a class=\"\" href=\"/list/dianjing/\">??????</a></li><li><a class=\"\" href=\"/list/unknown/\">??????</a></li><li><a class=\"\" href=\"/list/xiongdiqing/\">?????????</a></li><li><a class=\"\" href=\"/list/nuanmeng/\">??????</a></li><li><a class=\"\" href=\"/list/haokuai/\">??????</a></li><li><a class=\"\" href=\"/list/wanjie/\">??????</a></li><li><a class=\"\" href=\"/list/nvsheng/\">??????</a></li><li><a class=\"\" href=\"/list/lianzai/\">??????</a></li><li><a class=\"\" href=\"/list/nansheng/\">??????</a></li><li><a class=\"\" href=\"/list/futa/\">??????</a></li><li><a class=\"\" href=\"/list/bianshenlongyurentishiyanshenghuaweiji/\">??????;??????;????????????;????????????</a></li></ul>";
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
        String s = "https://m.imitui.com%sclick/?page=1";
        return SourceHelper.getRankMap(map, mapList, s);
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        List<ComicInfo> list = getInfoList(html);
        if (list.size() > 0) {
            return list;
        } else {
            JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
                @Override
                protected ComicInfo dealElement(JsoupNode node, int elementId) {
                    String title = node.ownText("a.txtA");
                    String author = node.ownText("span.info");
                    String updateTime = null;
                    String imgUrl = node.src("img");
                    String detailUrl = node.href("a.txtA");
                    return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
                }
            };
            return starter.startElements(html, "ul#comic-items li");
        }
    }

}