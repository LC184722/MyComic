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
                    intro = intro.substring(intro.indexOf('：') + 1);
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
                    chapterUrl = chapterUrl.replace("//m.", "//www.");
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
            String server = "https://res6.sisimanhua.com";
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
        String html = "<ul>\t<li><a class=\"\" href=\"/list/rexue/\">热血</a></li>\t<li><a class=\"\" href=\"/list/wuxiagedou/\">武侠格斗</a></li>\t<li><a class=\"\" href=\"/list/lianaishenghuo/\">恋爱生活</a></li>\t<li><a class=\"\" href=\"/list/baihenvxing/\">百合女性</a></li>\t<li><a class=\"\" href=\"/list/shenghuomanhua/\">生活漫画</a></li>\t<li><a class=\"\" href=\"/list/shaonvaiqing/\">少女爱情</a></li>\t<li><a class=\"\" href=\"/list/gaoxiao/\">搞笑</a></li>\t<li><a class=\"\" href=\"/list/zhanzheng/\">战争</a></li>\t<li><a class=\"\" href=\"/list/dongzuo/\">动作</a></li>\t<li><a class=\"\" href=\"/list/baoxiao/\">爆笑</a></li>\t<li><a class=\"\" href=\"/list/lianai/\">恋爱</a></li>\t<li><a class=\"\" href=\"/list/xiaoyuan/\">校园</a></li>\t<li><a class=\"\" href=\"/list/shenghuo/\">生活</a></li>\t<li><a class=\"\" href=\"/list/gufeng/\">古风</a></li>\t<li><a class=\"\" href=\"/list/dushi/\">都市</a></li>\t<li><a class=\"\" href=\"/list/maoxian/\">冒险</a></li>\t<li><a class=\"\" href=\"/list/xuanyi/\">悬疑</a></li>\t<li><a class=\"\" href=\"/list/kongbu/\">恐怖</a></li>\t<li><a class=\"\" href=\"/list/juqing/\">剧情</a></li>\t<li><a class=\"\" href=\"/list/hougong/\">后宫</a></li>\t<li><a class=\"\" href=\"/list/lizhi/\">励志</a></li>\t<li><a class=\"\" href=\"/list/lingyi/\">灵异</a></li>\t<li><a class=\"\" href=\"/list/fangyi/\">防疫</a></li>\t<li><a class=\"\" href=\"/list/shehui/\">社会</a></li>\t<li><a class=\"\" href=\"/list/bazong/\">霸总</a></li>\t<li><a class=\"\" href=\"/list/kehuan/\">科幻</a></li>\t<li><a class=\"\" href=\"/list/xuanhuan/\">玄幻</a></li>\t<li><a class=\"\" href=\"/list/shenmo/\">神魔</a></li>\t<li><a class=\"\" href=\"/list/xixie/\">吸血</a></li>\t<li><a class=\"\" href=\"/list/chuanyue/\">穿越</a></li>\t<li><a class=\"\" href=\"/list/tongren/\">同人</a></li>\t<li><a class=\"\" href=\"/list/jiakong/\">架空</a></li>\t<li><a class=\"\" href=\"/list/jingji/\">竞技</a></li>\t<li><a class=\"\" href=\"/list/danmei/\">耽美</a></li>\t<li><a class=\"\" href=\"/list/qiangwei/\">蔷薇</a></li>\t<li><a class=\"\" href=\"/list/xiuzhen/\">修真</a></li>\t<li><a class=\"\" href=\"/list/zhiyu/\">治愈</a></li>\t<li><a class=\"\" href=\"/list/zhenren/\">真人</a></li>\t<li><a class=\"\" href=\"/list/mohuan/\">魔幻</a></li>\t<li><a class=\"\" href=\"/list/xiaoshuogaibian/\">小说改编</a></li>\t<li><a class=\"\" href=\"/list/lishi/\">历史</a></li>\t<li><a class=\"\" href=\"/list/wuxia/\">武侠</a></li>\t<li><a class=\"\" href=\"/list/qihuan/\">奇幻</a></li>\t<li><a class=\"\" href=\"/list/luoli/\">萝莉</a></li>\t<li><a class=\"\" href=\"/list/mangai/\">漫改</a></li>\t<li><a class=\"\" href=\"/list/yundong/\">运动</a></li>\t<li><a class=\"\" href=\"/list/rigeng/\">日更</a></li>\t<li><a class=\"\" href=\"/list/youxi/\">游戏</a></li>\t<li><a class=\"\" href=\"/list/qingchun/\">青春</a></li>\t<li><a class=\"\" href=\"/list/mengxi/\">萌系</a></li>\t<li><a class=\"\" href=\"/list/yujie/\">御姐</a></li>\t<li><a class=\"\" href=\"/list/fuchou/\">复仇</a></li>\t<li><a class=\"\" href=\"/list/xinzuo/\">新作</a></li>\t<li><a class=\"\" href=\"/list/nuexin/\">虐心</a></li>\t<li><a class=\"\" href=\"/list/chunai/\">纯爱</a></li>\t<li><a class=\"\" href=\"/list/jingpin/\">精品</a></li>\t<li><a class=\"\" href=\"/list/tuili/\">推理</a></li>\t<li><a class=\"\" href=\"/list/gedou/\">格斗</a></li>\t<li><a class=\"\" href=\"/list/zhenhan/\">震撼</a></li>\t<li><a class=\"\" href=\"/list/jiangshi/\">僵尸</a></li>\t<li><a class=\"\" href=\"/list/egao/\">恶搞</a></li>\t<li><a class=\"\" href=\"/list/weimei/\">唯美</a></li>\t<li><a class=\"\" href=\"/list/qingsong/\">轻松</a></li>\t<li><a class=\"\" href=\"/list/chongwu/\">宠物</a></li>\t<li><a class=\"\" href=\"/list/zhichang/\">职场</a></li>\t<li><a class=\"\" href=\"/list/weilai/\">未来</a></li>\t<li><a class=\"\" href=\"/list/yishijie/\">异世界</a></li>\t<li><a class=\"\" href=\"/list/jingqi/\">惊奇</a></li>\t<li><a class=\"\" href=\"/list/gongting/\">宫廷</a></li>\t<li><a class=\"\" href=\"/list/shuangliu/\">爽流</a></li>\t<li><a class=\"\" href=\"/list/nixi/\">逆袭</a></li>\t<li><a class=\"\" href=\"/list/xianxia/\">仙侠</a></li>\t<li><a class=\"\" href=\"/list/jijia/\">机甲</a></li>\t<li><a class=\"\" href=\"/list/meishi/\">美食</a></li>\t<li><a class=\"\" href=\"/list/naodong/\">脑洞</a></li>\t<li><a class=\"\" href=\"/list/mingxing/\">明星</a></li>\t<li><a class=\"\" href=\"/list/jingxian/\">惊险</a></li>\t<li><a class=\"\" href=\"/list/gongdou/\">宫斗</a></li>\t<li><a class=\"\" href=\"/list/richang/\">日常</a></li>\t<li><a class=\"\" href=\"/list/gandong/\">感动</a></li>\t<li><a class=\"\" href=\"/list/langman/\">浪漫</a></li>\t<li><a class=\"\" href=\"/list/shishi/\">史诗</a></li>\t<li><a class=\"\" href=\"/list/jiating/\">家庭</a></li>\t<li><a class=\"\" href=\"/list/zhengju/\">正剧</a></li>\t<li><a class=\"\" href=\"/list/duanzi/\">段子</a></li>\t<li><a class=\"\" href=\"/list/wenxin/\">温馨</a></li>\t<li><a class=\"\" href=\"/list/xiangcun/\">乡村</a></li>\t<li><a class=\"\" href=\"/list/gaozhishang/\">高智商</a></li>\t<li><a class=\"\" href=\"/list/xuanyituili/\">悬疑推理</a></li>\t<li><a class=\"\" href=\"/list/jizhi/\">机智</a></li>\t<li><a class=\"\" href=\"/list/zhaohuanshou/\">召唤兽</a></li>\t<li><a class=\"\" href=\"/list/zhuangbi/\">装逼</a></li>\t<li><a class=\"\" href=\"/list/zhandou/\">战斗</a></li>\t<li><a class=\"\" href=\"/list/haomen/\">豪门</a></li>\t<li><a class=\"\" href=\"/list/yineng/\">异能</a></li>\t<li><a class=\"\" href=\"/list/shenhua/\">神话</a></li>\t<li><a class=\"\" href=\"/list/zhongkouwei/\">重口味</a></li>\t<li><a class=\"\" href=\"/list/xuexing/\">血腥</a></li>\t<li><a class=\"\" href=\"/list/doubi/\">逗比</a></li>\t<li><a class=\"\" href=\"/list/sangshi/\">丧尸</a></li>\t<li><a class=\"\" href=\"/list/yulequan/\">娱乐圈</a></li>\t<li><a class=\"\" href=\"/list/keji/\">科技</a></li>\t<li><a class=\"\" href=\"/list/shaonao/\">烧脑</a></li>\t<li><a class=\"\" href=\"/list/zhentantuili/\">侦探推理</a></li>\t<li><a class=\"\" href=\"/list/qita/\">其它</a></li>\t<li><a class=\"\" href=\"/list/moshi/\">末世</a></li>\t<li><a class=\"\" href=\"/list/tiyu/\">体育</a></li>\t<li><a class=\"\" href=\"/list/quanmou/\">权谋</a></li>\t<li><a class=\"\" href=\"/list/shangzhan/\">商战</a></li>\t<li><a class=\"\" href=\"/list/neihan/\">内涵</a></li>\t<li><a class=\"\" href=\"/list/huanlexiang/\">欢乐向</a></li>\t<li><a class=\"\" href=\"/list/aiqing/\">爱情</a></li>\t<li><a class=\"\" href=\"/list/qingxiaoshuo/\">轻小说</a></li>\t<li><a class=\"\" href=\"/list/yinyuewudao/\">音乐舞蹈</a></li>\t<li><a class=\"\" href=\"/list/baihe/\">百合</a></li>\t<li><a class=\"\" href=\"/list/xingzhuanhuan/\">性转换</a></li>\t<li><a class=\"\" href=\"/list/sige/\">四格</a></li>\t<li><a class=\"\" href=\"/list/dongfang/\">东方</a></li>\t<li><a class=\"\" href=\"/list/shengui/\">神鬼</a></li>\t<li><a class=\"\" href=\"/list/xiuzhenlianaijiakong/\">修真恋爱架空</a></li>\t<li><a class=\"\" href=\"/list/qingsonggaoxiao/\">轻松搞笑</a></li>\t<li><a class=\"\" href=\"/list/tongrenmanhua/\">同人漫画</a></li>\t<li><a class=\"\" href=\"/list/caihong/\">彩虹</a></li>\t<li><a class=\"\" href=\"/list/zhentan/\">侦探</a></li>\t<li><a class=\"\" href=\"/list/mofa/\">魔法</a></li>\t<li><a class=\"\" href=\"/list/jiujie/\">纠结</a></li>\t<li><a class=\"\" href=\"/list/hunyin/\">婚姻</a></li>\t<li><a class=\"\" href=\"/list/gaoxiaoxiju/\">搞笑喜剧</a></li>\t<li><a class=\"\" href=\"/list/kongbulingyi/\">恐怖灵异</a></li>\t<li><a class=\"\" href=\"/list/weiniang/\">伪娘</a></li>\t<li><a class=\"\" href=\"/list/qita2/\">其他</a></li>\t<li><a class=\"\" href=\"/list/zhaixi/\">宅系</a></li>\t<li><a class=\"\" href=\"/list/zhanzhengmanhua/\">战争漫画</a></li>\t<li><a class=\"\" href=\"/list/jingjitiyu/\">竞技体育</a></li>\t<li><a class=\"\" href=\"/list/jiecao/\">节操</a></li>\t<li><a class=\"\" href=\"/list/danmeirensheng/\">耽美人生</a></li>\t<li><a class=\"\" href=\"/list/gushimanhua/\">故事漫画</a></li>\t<li><a class=\"\" href=\"/list/kuaikanmanhua/\">快看漫画</a></li>\t<li><a class=\"\" href=\"/list/hanguomanhua/\">韩国漫画</a></li>\t<li><a class=\"\" href=\"/list/shaonv/\">少女</a></li>\t<li><a class=\"\" href=\"/list/AA/\">AA</a></li>\t<li><a class=\"\" href=\"/list/jingsong/\">惊悚</a></li>\t<li><a class=\"\" href=\"/list/xifangmohuan/\">西方魔幻</a></li>\t<li><a class=\"\" href=\"/list/xingzhuan/\">性转</a></li>\t<li><a class=\"\" href=\"/list/jianniang/\">舰娘</a></li>\t<li><a class=\"\" href=\"/list/jizhan/\">机战</a></li>\t<li><a class=\"\" href=\"/list/gaibian/\">改编</a></li>\t<li><a class=\"\" href=\"/list/huiben/\">绘本</a></li>\t<li><a class=\"\" href=\"/list/shaonian/\">少年</a></li>\t<li><a class=\"\" href=\"/list/kongbuxuanyi/\">恐怖悬疑</a></li>\t<li><a class=\"\" href=\"/list/zhengnengliang/\">正能量</a></li>\t<li><a class=\"\" href=\"/list/zhiyu2/\">致郁</a></li>\t<li><a class=\"\" href=\"/list/xiuji/\">秀吉</a></li>\t<li><a class=\"\" href=\"/list/zongcai/\">总裁</a></li>\t<li><a class=\"\" href=\"/list/lunli/\">伦理</a></li>\t<li><a class=\"\" href=\"/list/juwei/\">橘味</a></li>\t<li><a class=\"\" href=\"/list/anhei/\">暗黑</a></li>\t<li><a class=\"\" href=\"/list/qingnian/\">青年</a></li>\t<li><a class=\"\" href=\"/list/danmeiBL/\">耽美BL</a></li>\t<li><a class=\"\" href=\"/list/yanyi/\">颜艺</a></li>\t<li><a class=\"\" href=\"/list/zhidou/\">智斗</a></li>\t<li><a class=\"\" href=\"/list/gaoqingdanxing/\">高清单行</a></li>\t<li><a class=\"\" href=\"/list/zhupu/\">主仆</a></li>\t<li><a class=\"\" href=\"/list/lieqi/\">猎奇</a></li>\t<li><a class=\"\" href=\"/list/yinv/\">乙女</a></li>\t<li><a class=\"\" href=\"/list/maimeng/\">麦萌</a></li>\t<li><a class=\"\" href=\"/list/heidao/\">黑道</a></li>\t<li><a class=\"\" href=\"/list/rexuemaoxian/\">热血冒险</a></li>\t<li><a class=\"\" href=\"/list/ertong/\">儿童</a></li>\t<li><a class=\"\" href=\"/list/tougao/\">投稿</a></li>\t<li><a class=\"\" href=\"/list/manhuadao/\">漫画岛</a></li>\t<li><a class=\"\" href=\"/list/rexuedongzuo/\">热血动作</a></li>\t<li><a class=\"\" href=\"/list/mhuaquan/\">mhuaquan</a></li>\t<li><a class=\"\" href=\"/list/donghua/\">动画</a></li>\t<li><a class=\"\" href=\"/list/sigeduoge/\">四格多格</a></li>\t<li><a class=\"\" href=\"/list/zhongsheng/\">重生</a></li>\t<li><a class=\"\" href=\"/list/nizhuan/\">逆转</a></li>\t<li><a class=\"\" href=\"/list/aimei/\">暧昧</a></li>\t<li><a class=\"\" href=\"/list/danvzhu/\">大女主</a></li>\t<li><a class=\"\" href=\"/list/shenghua/\">生化</a></li>\t<li><a class=\"\" href=\"/list/yizu/\">异族</a></li>\t<li><a class=\"\" href=\"/list/ouxiang/\">偶像</a></li>\t<li><a class=\"\" href=\"/list/qiwen/\">奇闻</a></li>\t<li><a class=\"\" href=\"/list/meishaonv/\">美少女</a></li>\t<li><a class=\"\" href=\"/list/dujia/\">独家</a></li>\t<li><a class=\"\" href=\"/list/guzhuang/\">古装</a></li>\t<li><a class=\"\" href=\"/list/chongai/\">宠爱</a></li>\t<li><a class=\"\" href=\"/list/huanxiang/\">幻想</a></li>\t<li><a class=\"\" href=\"/list/guaitan/\">怪谈</a></li>\t<li><a class=\"\" href=\"/list/panni/\">叛逆</a></li>\t<li><a class=\"\" href=\"/list/huaijiu/\">怀旧</a></li>\t<li><a class=\"\" href=\"/list/qinggan/\">情感</a></li>\t<li><a class=\"\" href=\"/list/nvzun/\">女尊</a></li>\t<li><a class=\"\" href=\"/list/tanan/\">探案</a></li>\t<li><a class=\"\" href=\"/list/zhaidou/\">宅斗</a></li>\t<li><a class=\"\" href=\"/list/xuezu/\">血族</a></li>\t<li><a class=\"\" href=\"/list/yanqing/\">言情</a></li>\t<li><a class=\"\" href=\"/list/nvqiang/\">女强</a></li>\t<li><a class=\"\" href=\"/list/zhongtian/\">种田</a></li>\t<li><a class=\"\" href=\"/list/mowang/\">魔王</a></li>\t<li><a class=\"\" href=\"/list/meixing/\">美型</a></li>\t<li><a class=\"\" href=\"/list/fuli/\">福利</a></li>\t<li><a class=\"\" href=\"/list/huxian/\">狐仙</a></li>\t<li><a class=\"\" href=\"/list/chonghun/\">宠婚</a></li>\t<li><a class=\"\" href=\"/list/xuanyijingsong/\">悬疑、惊悚</a></li>\t<li><a class=\"\" href=\"/list/guiguai/\">鬼怪</a></li>\t<li><a class=\"\" href=\"/list/shouer/\">兽耳</a></li>\t<li><a class=\"\" href=\"/list/shaoer/\">少儿</a></li>\t<li><a class=\"\" href=\"/list/xiaojiangshi/\">小僵尸</a></li>\t<li><a class=\"\" href=\"/list/henxiyou/\">狠西游</a></li>\t<li><a class=\"\" href=\"/list/xiyouji/\">西游记</a></li>\t<li><a class=\"\" href=\"/list/qingqingshu/\">青青树</a></li>\t<li><a class=\"\" href=\"/list/nanshen/\">男神</a></li>\t<li><a class=\"\" href=\"/list/tianchong/\">甜宠</a></li>\t<li><a class=\"\" href=\"/list/bingjiao/\">病娇</a></li>\t<li><a class=\"\" href=\"/list/shenshi/\">绅士</a></li>\t<li><a class=\"\" href=\"/list/sihoushijie/\">死后世界</a></li>\t<li><a class=\"\" href=\"/list/motong/\">墨瞳</a></li>\t<li><a class=\"\" href=\"/list/riman/\">日漫</a></li>\t<li><a class=\"\" href=\"/list/mori/\">末日</a></li>\t<li><a class=\"\" href=\"/list/xitong/\">系统</a></li>\t<li><a class=\"\" href=\"/list/xiuxian/\">修仙</a></li>\t<li><a class=\"\" href=\"/list/shenxian/\">神仙</a></li>\t<li><a class=\"\" href=\"/list/guaiwu/\">怪物</a></li>\t<li><a class=\"\" href=\"/list/shenhao/\">神豪</a></li>\t<li><a class=\"\" href=\"/list/nuelian/\">虐恋</a></li>\t<li><a class=\"\" href=\"/list/bazongdushi/\">霸总.都市</a></li>\t<li><a class=\"\" href=\"/list/yaoguai/\">妖怪</a></li>\t<li><a class=\"\" href=\"/list/gaotian/\">高甜</a></li>\t<li><a class=\"\" href=\"/list/wanjie/\">完结</a></li>\t<li><a class=\"\" href=\"/list/dianjing/\">电竞</a></li>\t<li><a class=\"\" href=\"/list/unknown/\">ゆり</a></li>\t<li><a class=\"\" href=\"/list/xiongdiqing/\">兄弟情</a></li>\t<li><a class=\"\" href=\"/list/haokuai/\">豪快</a></li>\t<li><a class=\"\" href=\"/list/nuanmeng/\">暖萌</a></li>\t<li><a class=\"\" href=\"/list/shaonan/\">少男</a></li>\t<li><a class=\"\" href=\"/list/youmo/\">幽默</a></li>\t<li><a class=\"\" href=\"/list/qinqing/\">亲情</a></li>\t<li><a class=\"\" href=\"/list/juxi/\">橘系</a></li>\t<li><a class=\"\" href=\"/list/nvsheng/\">女生</a></li>\t<li><a class=\"\" href=\"/list/nansheng/\">男生</a></li>\t<li><a class=\"\" href=\"/list/futa/\">扶她</a></li>\t<li><a class=\"\" href=\"/list/gushi/\">故事</a></li>\t<li><a class=\"\" href=\"/list/yangcheng/\">养成</a></li>\t<li><a class=\"\" href=\"/list/huanxi/\">欢喜</a></li>\t<li><a class=\"\" href=\"/list/beiou/\">北欧</a></li>\t<li><a class=\"\" href=\"/list/fuhei/\">腹黑</a></li>\t<li><a class=\"\" href=\"/list/xihuan/\">西幻</a></li>\t<li><a class=\"\" href=\"/list/gudai/\">古代</a></li>\t<li><a class=\"\" href=\"/list/xianzhiji/\">限制级</a></li>\t<li><a class=\"\" href=\"/list/jifu/\">基腐</a></li>\t<li><a class=\"\" href=\"/list/youmogaoxiao/\">幽默搞笑</a></li>\t<li><a class=\"\" href=\"/list/qihuanmaoxian/\">奇幻冒险</a></li>\t<li><a class=\"\" href=\"/list/langmanaiqing/\">浪漫爱情</a></li>\t<li><a class=\"\" href=\"/list/BL/\">BL</a></li>\t<li><a class=\"\" href=\"/list/gufengchuanyue/\">古风穿越</a></li>\t<li><a class=\"\" href=\"/list/zongheqita/\">综合其它</a></li>\t<li><a class=\"\" href=\"/list/TS/\">TS</a></li>\t<li><a class=\"\" href=\"/list/hanman/\">韩漫</a></li>\t<li><a class=\"\" href=\"/list/youyaoqi/\">有妖气</a></li>\t<li><a class=\"\" href=\"/list/gaoxiaoegao/\">搞笑恶搞</a></li>\t<li><a class=\"\" href=\"/list/kejin/\">氪金</a></li>\t<li><a class=\"\" href=\"/list/furui/\">福瑞</a></li>\t<li><a class=\"\" href=\"/list/gongtingdongfang/\">宫廷东方</a></li>\t<li><a class=\"\" href=\"/list/fanai/\">泛爱</a></li>\t<li><a class=\"\" href=\"/list/shengcun/\">生存</a></li>\t<li><a class=\"\" href=\"/list/2021dasai/\">2021大赛</a></li>\t<li><a class=\"\" href=\"/list/zhiyinmanke/\">知音漫客</a></li>\t<li><a class=\"\" href=\"/list/manhuayifan/\">漫画一番</a></li>\t<li><a class=\"\" href=\"/list/nvwangdiankeng/\">女王点坑</a></li>\t<li><a class=\"\" href=\"/list/xiandai/\">现代</a></li>\t<li><a class=\"\" href=\"/list/chaojiyingxiong/\">超级英雄</a></li>\t<li><a class=\"\" href=\"/list/wuxiaxianxia/\">武侠仙侠</a></li>\t<li><a class=\"\" href=\"/list/xuanyilingyi/\">悬疑灵异</a></li>\t<li><a class=\"\" href=\"/list/junfa/\">军阀</a></li>\t<li><a class=\"\" href=\"/list/hunlian/\">婚恋</a></li>\t<li><a class=\"\" href=\"/list/shenjiemanhua/\">神界漫画</a></li>\t<li><a class=\"\" href=\"/list/tianshangkong/\">天上空</a></li>\t<li><a class=\"\" href=\"/list/duzhiniao/\">渡之鸟</a></li>\t<li><a class=\"\" href=\"/list/ranxiang/\">燃向</a></li>\t<li><a class=\"\" href=\"/list/changpian/\">长篇</a></li>\t<li><a class=\"\" href=\"/list/chenlan/\">陈岚</a></li>\t<li><a class=\"\" href=\"/list/manhuahui/\">漫画会</a></li>\t<li><a class=\"\" href=\"/list/longren/\">龙刃</a></li>\t<li><a class=\"\" href=\"/list/pianyu/\">片玉</a></li>\t<li><a class=\"\" href=\"/list/dujitang/\">毒鸡汤</a></li>\t<li><a class=\"\" href=\"/list/yinan/\">阴暗</a></li>\t<li><a class=\"\" href=\"/list/guoman/\">国漫</a></li>\t<li><a class=\"\" href=\"/list/kaigua/\">开挂</a></li>\t<li><a class=\"\" href=\"/list/hunchong/\">婚宠</a></li>\t<li><a class=\"\" href=\"/list/tiaoman/\">条漫</a></li>\t<li><a class=\"\" href=\"/list/youqudao/\">有趣岛</a></li>\t<li><a class=\"\" href=\"/list/xiongmei/\">兄妹</a></li>\t<li><a class=\"\" href=\"/list/sanciyuan/\">三次元</a></li>\t<li><a class=\"\" href=\"/list/jingdian/\">经典</a></li>\t<li><a class=\"\" href=\"/list/chiji/\">吃鸡</a></li>\t<li><a class=\"\" href=\"/list/qiangzhan/\">枪战</a></li>\t<li><a class=\"\" href=\"/list/tezhongbing/\">特种兵</a></li>\t<li><a class=\"\" href=\"/list/xinqi/\">新妻</a></li>\t<li><a class=\"\" href=\"/list/riben/\">日本</a></li>\t<li><a class=\"\" href=\"/list/mengchong/\">萌宠</a></li>\t<li><a class=\"\" href=\"/list/duanpian/\">短篇</a></li>\t<li><a class=\"\" href=\"/list/LOL/\">LOL</a></li></ul>";
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
