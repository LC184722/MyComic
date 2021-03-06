package top.luqichuang.mycomic.source;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
 * @date 2022/5/25 13:32
 * @ver 1.0
 */
public class SiSi extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.SI_SI;
    }

    @Override
    public String getIndex() {
        return "https://m.sisimanhua.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = getIndex() + "/search/?keywords=" + searchString;
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
                String imgUrl = node.attr("img", "mip-img");
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
            protected void dealInfo(JsoupNode node) {
                String title = node.attr("div#Cover mip-img", "alt");
                String imgUrl = node.src("div#Cover mip-img");
                String author = node.ownText("div.pic dl:eq(2) dd");
                String intro = node.ownText("p.txtDesc");
                String updateStatus = null;
                String updateTime = node.ownText("div.pic dl:eq(4) dd");
                try {
                    intro = intro.substring(intro.indexOf('???') + 1);
                } catch (Exception ignored) {
                }
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("span");
                String chapterUrl = node.href("a");
                if (chapterUrl != null && chapterUrl.contains("html")) {
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
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        try {
            JsoupNode node = new JsoupNode(html);
            String baseUrl = "https://res6.sisimanhua.com/image/view/%s/%s.webp";
            String cId = StringUtil.match(getIndex() + ".*?" + "(\\d+).html", html);
            int total = Integer.parseInt(node.ownText("span#k_total"));
            urls = new String[total];
            for (int i = 0; i < urls.length; i++) {
                urls[i] = String.format(baseUrl, cId, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        String html = "<ul>\t<li><a class=\"\" href=\"/list/rexue/\">??????</a></li>\t<li><a class=\"\" href=\"/list/wuxiagedou/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/lianaishenghuo/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/baihenvxing/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/shenghuomanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/shaonvaiqing/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/gaoxiao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhanzheng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/dongzuo/\">??????</a></li>\t<li><a class=\"\" href=\"/list/baoxiao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/lianai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiaoyuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenghuo/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gufeng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/dushi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/maoxian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xuanyi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/kongbu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/juqing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/hougong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/lizhi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/lingyi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/fangyi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shehui/\">??????</a></li>\t<li><a class=\"\" href=\"/list/bazong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/kehuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xuanhuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenmo/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xixie/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chuanyue/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tongren/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jiakong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jingji/\">??????</a></li>\t<li><a class=\"\" href=\"/list/danmei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qiangwei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiuzhen/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhiyu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhenren/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mohuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiaoshuogaibian/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/lishi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/wuxia/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qihuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/luoli/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mangai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yundong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/rigeng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/youxi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qingchun/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mengxi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yujie/\">??????</a></li>\t<li><a class=\"\" href=\"/list/fuchou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xinzuo/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nuexin/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chunai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jingpin/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tuili/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gedou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhenhan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jiangshi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/egao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/weimei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qingsong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chongwu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhichang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/weilai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yishijie/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/jingqi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gongting/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shuangliu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nixi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xianxia/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jijia/\">??????</a></li>\t<li><a class=\"\" href=\"/list/meishi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/naodong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mingxing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jingxian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gongdou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/richang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gandong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/langman/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shishi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jiating/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhengju/\">??????</a></li>\t<li><a class=\"\" href=\"/list/duanzi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/wenxin/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiangcun/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gaozhishang/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/xuanyituili/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/jizhi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhaohuanshou/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/zhuangbi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhandou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/haomen/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yineng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenhua/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhongkouwei/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/xuexing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/doubi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/sangshi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yulequan/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/keji/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shaonao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhentantuili/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/qita/\">??????</a></li>\t<li><a class=\"\" href=\"/list/moshi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tiyu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/quanmou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shangzhan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/neihan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huanlexiang/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/aiqing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qingxiaoshuo/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/yinyuewudao/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/baihe/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xingzhuanhuan/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/sige/\">??????</a></li>\t<li><a class=\"\" href=\"/list/dongfang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shengui/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiuzhenlianaijiakong/\">??????????????????</a></li>\t<li><a class=\"\" href=\"/list/qingsonggaoxiao/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/tongrenmanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/caihong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhentan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mofa/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jiujie/\">??????</a></li>\t<li><a class=\"\" href=\"/list/hunyin/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gaoxiaoxiju/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/kongbulingyi/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/weiniang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qita2/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhaixi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhanzhengmanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/jingjitiyu/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/jiecao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/danmeirensheng/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/gushimanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/kuaikanmanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/hanguomanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/shaonv/\">??????</a></li>\t<li><a class=\"\" href=\"/list/AA/\">AA</a></li>\t<li><a class=\"\" href=\"/list/jingsong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xifangmohuan/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/xingzhuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jianniang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/jizhan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gaibian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huiben/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shaonian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/kongbuxuanyi/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/zhengnengliang/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/zhiyu2/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiuji/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zongcai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/lunli/\">??????</a></li>\t<li><a class=\"\" href=\"/list/juwei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/anhei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qingnian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/danmeiBL/\">??????BL</a></li>\t<li><a class=\"\" href=\"/list/yanyi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhidou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gaoqingdanxing/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/zhupu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/lieqi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yinv/\">??????</a></li>\t<li><a class=\"\" href=\"/list/maimeng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/heidao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/rexuemaoxian/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/ertong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tougao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/manhuadao/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/rexuedongzuo/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/mhuaquan/\">mhuaquan</a></li>\t<li><a class=\"\" href=\"/list/donghua/\">??????</a></li>\t<li><a class=\"\" href=\"/list/sigeduoge/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/zhongsheng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nizhuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/aimei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/danvzhu/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/shenghua/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yizu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/ouxiang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qiwen/\">??????</a></li>\t<li><a class=\"\" href=\"/list/meishaonv/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/dujia/\">??????</a></li>\t<li><a class=\"\" href=\"/list/guzhuang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chongai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huanxiang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/guaitan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/panni/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huaijiu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qinggan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nvzun/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tanan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhaidou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xuezu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yanqing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nvqiang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/zhongtian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mowang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/meixing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/fuli/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huxian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chonghun/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xuanyijingsong/\">???????????????</a></li>\t<li><a class=\"\" href=\"/list/guiguai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shouer/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shaoer/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiaojiangshi/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/henxiyou/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/xiyouji/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/qingqingshu/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/nanshen/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tianchong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/bingjiao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenshi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/sihoushijie/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/motong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/riman/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mori/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xitong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiuxian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenxian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/guaiwu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenhao/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nuelian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/bazongdushi/\">??????.??????</a></li>\t<li><a class=\"\" href=\"/list/yaoguai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gaotian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/wanjie/\">??????</a></li>\t<li><a class=\"\" href=\"/list/dianjing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/unknown/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xiongdiqing/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/haokuai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nuanmeng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shaonan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/youmo/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qinqing/\">??????</a></li>\t<li><a class=\"\" href=\"/list/juxi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nvsheng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/nansheng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/futa/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gushi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/yangcheng/\">??????</a></li>\t<li><a class=\"\" href=\"/list/huanxi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/beiou/\">??????</a></li>\t<li><a class=\"\" href=\"/list/fuhei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xihuan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gudai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/xianzhiji/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/jifu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/youmogaoxiao/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/qihuanmaoxian/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/langmanaiqing/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/BL/\">BL</a></li>\t<li><a class=\"\" href=\"/list/gufengchuanyue/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/zongheqita/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/TS/\">TS</a></li>\t<li><a class=\"\" href=\"/list/hanman/\">??????</a></li>\t<li><a class=\"\" href=\"/list/youyaoqi/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/gaoxiaoegao/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/kejin/\">??????</a></li>\t<li><a class=\"\" href=\"/list/furui/\">??????</a></li>\t<li><a class=\"\" href=\"/list/gongtingdongfang/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/fanai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shengcun/\">??????</a></li>\t<li><a class=\"\" href=\"/list/2021dasai/\">2021??????</a></li>\t<li><a class=\"\" href=\"/list/zhiyinmanke/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/manhuayifan/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/nvwangdiankeng/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/xiandai/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chaojiyingxiong/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/wuxiaxianxia/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/xuanyilingyi/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/junfa/\">??????</a></li>\t<li><a class=\"\" href=\"/list/hunlian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/shenjiemanhua/\">????????????</a></li>\t<li><a class=\"\" href=\"/list/tianshangkong/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/duzhiniao/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/ranxiang/\">??????</a></li>\t<li><a class=\"\" href=\"/list/changpian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chenlan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/manhuahui/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/longren/\">??????</a></li>\t<li><a class=\"\" href=\"/list/pianyu/\">??????</a></li>\t<li><a class=\"\" href=\"/list/dujitang/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/yinan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/guoman/\">??????</a></li>\t<li><a class=\"\" href=\"/list/kaigua/\">??????</a></li>\t<li><a class=\"\" href=\"/list/hunchong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tiaoman/\">??????</a></li>\t<li><a class=\"\" href=\"/list/youqudao/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/xiongmei/\">??????</a></li>\t<li><a class=\"\" href=\"/list/sanciyuan/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/jingdian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/chiji/\">??????</a></li>\t<li><a class=\"\" href=\"/list/qiangzhan/\">??????</a></li>\t<li><a class=\"\" href=\"/list/tezhongbing/\">?????????</a></li>\t<li><a class=\"\" href=\"/list/xinqi/\">??????</a></li>\t<li><a class=\"\" href=\"/list/riben/\">??????</a></li>\t<li><a class=\"\" href=\"/list/mengchong/\">??????</a></li>\t<li><a class=\"\" href=\"/list/duanpian/\">??????</a></li>\t<li><a class=\"\" href=\"/list/LOL/\">LOL</a></li></ul>";
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
    public List<ComicInfo> getRankInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.txtA");
                String author = null;
                String updateTime = null;
                String imgUrl = node.src("mip-img");
                String detailUrl = node.href("a.txtA");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "div#w0 li");
    }
}
