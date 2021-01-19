package com.qc.mycomic.source;

import com.qc.mycomic.en.SourceEnum;
import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.jsoup.JsoupStarter;
import com.qc.mycomic.model.ChapterInfo;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.en.Codes;
import com.qc.mycomic.util.ComicUtil;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import okhttp3.Request;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MiTui extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.MI_TUI;
    }

    @Override
    public String getIndex() {
        return "https://www.imitui.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        searchString = "https://m.imitui.com/search/?keywords=" + searchString;
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
                comicInfo.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
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
        comicInfo.initChapterInfoList(starter.startElements(html, "ul#chapter-list-1 li"));
    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
        String chapterImagesStr = StringUtil.match("chapterImages = \\[(.*?)\\]", html);
        String chapterPath = StringUtil.match("var chapterPath = \"(.*?)\";", html);
        String[] urls = null;
        if (chapterImagesStr != null && !chapterImagesStr.equals("")) {
            urls = chapterImagesStr.split(",");
            String server = "https://resnode.yxtun.com";
            for (int i = 0; i < urls.length; i++) {
                String url = urls[i];
                url = url.replace("\"", "").replace("\\", "");
                if (!url.startsWith("http")) {
                    urls[i] = server + "/" + chapterPath + url;
                } else {
                    urls[i] = url;
                }
            }
        }
        return ComicUtil.getImageInfoList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("人气排行", "https://m.imitui.com/rank/popularity/?page=1");
        map.put("点击排行", "https://m.imitui.com/rank/click/?page=1");
        map.put("订阅排行", "https://m.imitui.com/rank/subscribe/?page=1");
        map.put("刚刚更新", "https://m.imitui.com/update/?page=1");
        String text = "<ul><li><a class=\"\" href=\"/list/rexue/\">热血</a></li><li><a class=\"\" href=\"/list/xuanhuan/\">玄幻</a></li><li><a class=\"\" href=\"/list/xiuzhen/\">修真</a></li><li><a class=\"\" href=\"/list/gufeng/\">古风</a></li><li><a class=\"\" href=\"/list/lianai/\">恋爱</a></li><li><a class=\"\" href=\"/list/chuanyue/\">穿越</a></li><li><a class=\"\" href=\"/list/dushi/\">都市</a></li><li><a class=\"\" href=\"/list/bazong/\">霸总</a></li><li><a class=\"\" href=\"/list/xuanyi/\">悬疑</a></li><li><a class=\"\" href=\"/list/gaoxiao/\">搞笑</a></li><li><a class=\"\" href=\"/list/qihuan/\">奇幻</a></li><li><a class=\"\" href=\"/list/zongcai/\">总裁</a></li><li><a class=\"\" href=\"/list/richang/\">日常</a></li><li><a class=\"\" href=\"/list/maoxian/\">冒险</a></li><li><a class=\"\" href=\"/list/kehuan/\">科幻</a></li><li><a class=\"\" href=\"/list/chunai/\">纯爱</a></li><li><a class=\"\" href=\"/list/mohuan/\">魔幻</a></li><li><a class=\"\" href=\"/list/zhanzheng/\">战争</a></li><li><a class=\"\" href=\"/list/qiangwei/\">蔷薇</a></li><li><a class=\"\" href=\"/list/wuxia/\">武侠</a></li><li><a class=\"\" href=\"/list/shenghuo/\">生活</a></li><li><a class=\"\" href=\"/list/dongzuo/\">动作</a></li><li><a class=\"\" href=\"/list/hougong/\">后宫</a></li><li><a class=\"\" href=\"/list/youxi/\">游戏</a></li><li><a class=\"\" href=\"/list/kongbu/\">恐怖</a></li><li><a class=\"\" href=\"/list/mangai/\">漫改</a></li><li><a class=\"\" href=\"/list/zhenren/\">真人</a></li><li><a class=\"\" href=\"/list/xiaoyuan/\">校园</a></li><li><a class=\"\" href=\"/list/juqing/\">剧情</a></li><li><a class=\"\" href=\"/list/lingyi/\">灵异</a></li><li><a class=\"\" href=\"/list/shaonian/\">少年</a></li><li><a class=\"\" href=\"/list/tuili/\">推理</a></li><li><a class=\"\" href=\"/list/huaijiu/\">怀旧</a></li><li><a class=\"\" href=\"/list/qinggan/\">情感</a></li><li><a class=\"\" href=\"/list/ouxiang/\">偶像</a></li><li><a class=\"\" href=\"/list/shaonv/\">少女</a></li><li><a class=\"\" href=\"/list/dujia/\">独家</a></li><li><a class=\"\" href=\"/list/nuexin/\">虐心</a></li><li><a class=\"\" href=\"/list/baoxiao/\">爆笑</a></li><li><a class=\"\" href=\"/list/lizhi/\">励志</a></li><li><a class=\"\" href=\"/list/meishi/\">美食</a></li><li><a class=\"\" href=\"/list/fuchou/\">复仇</a></li><li><a class=\"\" href=\"/list/caihong/\">彩虹</a></li><li><a class=\"\" href=\"/list/weimei/\">唯美</a></li><li><a class=\"\" href=\"/list/zhiyu/\">治愈</a></li><li><a class=\"\" href=\"/list/mingxing/\">明星</a></li><li><a class=\"\" href=\"/list/naodong/\">脑洞</a></li><li><a class=\"\" href=\"/list/mofa/\">魔法</a></li><li><a class=\"\" href=\"/list/xiuxian/\">修仙</a></li><li><a class=\"\" href=\"/list/zhongsheng/\">重生</a></li><li><a class=\"\" href=\"/list/xianxia/\">仙侠</a></li><li><a class=\"\" href=\"/list/moshi/\">末世</a></li><li><a class=\"\" href=\"/list/yineng/\">异能</a></li><li><a class=\"\" href=\"/list/nvzun/\">女尊</a></li><li><a class=\"\" href=\"/list/qita/\">其它</a></li><li><a class=\"\" href=\"/list/yanqing/\">言情</a></li><li><a class=\"\" href=\"/list/danmei/\">耽美</a></li><li><a class=\"\" href=\"/list/yundong/\">运动</a></li><li><a class=\"\" href=\"/list/gongdou/\">宫斗</a></li><li><a class=\"\" href=\"/list/guzhuang/\">古装</a></li><li><a class=\"\" href=\"/list/meishaonv/\">美少女</a></li><li><a class=\"\" href=\"/list/shenmo/\">神魔</a></li><li><a class=\"\" href=\"/list/lishi/\">历史</a></li><li><a class=\"\" href=\"/list/jingxian/\">惊险</a></li><li><a class=\"\" href=\"/list/jingji/\">竞技</a></li><li><a class=\"\" href=\"/list/mengxi/\">萌系</a></li><li><a class=\"\" href=\"/list/tiyu/\">体育</a></li><li><a class=\"\" href=\"/list/gedou/\">格斗</a></li><li><a class=\"\" href=\"/list/jijia/\">机甲</a></li><li><a class=\"\" href=\"/list/nuelian/\">虐恋</a></li><li><a class=\"\" href=\"/list/shuang/\">爽</a></li><li><a class=\"\" href=\"/list/fuli/\">福利</a></li><li><a class=\"\" href=\"/list/qita2/\">其他</a></li><li><a class=\"\" href=\"/list/xiaojiangshi/\">小僵尸</a></li><li><a class=\"\" href=\"/list/jiangshi/\">僵尸</a></li><li><a class=\"\" href=\"/list/langman/\">浪漫</a></li><li><a class=\"\" href=\"/list/jinshouzhi/\">金手指</a></li><li><a class=\"\" href=\"/list/yujie/\">御姐</a></li><li><a class=\"\" href=\"/list/zhandou/\">战斗</a></li><li><a class=\"\" href=\"/list/egao/\">恶搞</a></li><li><a class=\"\" href=\"/list/shehui/\">社会</a></li><li><a class=\"\" href=\"/list/quanmou/\">权谋</a></li><li><a class=\"\" href=\"/list/qingchun/\">青春</a></li><li><a class=\"\" href=\"/list/luoli/\">萝莉</a></li><li><a class=\"\" href=\"/list/tongren/\">同人</a></li><li><a class=\"\" href=\"/list/zhenhan/\">震撼</a></li><li><a class=\"\" href=\"/list/riman/\">日漫</a></li><li><a class=\"\" href=\"/list/junfa/\">军阀</a></li><li><a class=\"\" href=\"/list/minguo/\">民国</a></li><li><a class=\"\" href=\"/list/tegong/\">特工</a></li><li><a class=\"\" href=\"/list/meinv/\">美女</a></li><li><a class=\"\" href=\"/list/jiandie/\">间谍</a></li><li><a class=\"\" href=\"/list/anhei/\">暗黑</a></li><li><a class=\"\" href=\"/list/jiecao/\">节操</a></li><li><a class=\"\" href=\"/list/jingdian/\">经典</a></li><li><a class=\"\" href=\"/list/youmo/\">幽默</a></li><li><a class=\"\" href=\"/list/tianchong/\">甜宠</a></li><li><a class=\"\" href=\"/list/shenhua/\">神话</a></li><li><a class=\"\" href=\"/list/riben/\">日本</a></li><li><a class=\"\" href=\"/list/yijiyuan/\">翼纪元</a></li><li><a class=\"\" href=\"/list/tiaoman/\">条漫</a></li><li><a class=\"\" href=\"/list/LOL/\">LOL</a></li><li><a class=\"\" href=\"/list/zhongtian/\">种田</a></li><li><a class=\"\" href=\"/list/duanpian/\">短篇</a></li><li><a class=\"\" href=\"/list/jingsong/\">惊悚</a></li><li><a class=\"\" href=\"/list/sige/\">四格</a></li><li><a class=\"\" href=\"/list/guoman/\">国漫</a></li><li><a class=\"\" href=\"/list/youqudao/\">有趣岛</a></li><li><a class=\"\" href=\"/list/mengchong/\">萌宠</a></li><li><a class=\"\" href=\"/list/renxing/\">人性</a></li><li><a class=\"\" href=\"/list/chonghun/\">宠婚</a></li><li><a class=\"\" href=\"/list/xinqi/\">新妻</a></li><li><a class=\"\" href=\"/list/xixiegui/\">吸血鬼</a></li><li><a class=\"\" href=\"/list/shenjiemanhua/\">神界漫画</a></li><li><a class=\"\" href=\"/list/xueyuehua/\">雪月花</a></li><li><a class=\"\" href=\"/list/mengqishi/\">梦骑士</a></li><li><a class=\"\" href=\"/list/shouer/\">兽耳</a></li><li><a class=\"\" href=\"/list/shaoer/\">少儿</a></li><li><a class=\"\" href=\"/list/baihe/\">百合</a></li><li><a class=\"\" href=\"/list/chiji/\">吃鸡</a></li><li><a class=\"\" href=\"/list/qiangzhan/\">枪战</a></li><li><a class=\"\" href=\"/list/tezhongbing/\">特种兵</a></li><li><a class=\"\" href=\"/list/xiongdi/\">兄弟</a></li><li><a class=\"\" href=\"/list/yishi/\">异世</a></li><li><a class=\"\" href=\"/list/xiongmei/\">兄妹</a></li><li><a class=\"\" href=\"/list/sanciyuan/\">三次元</a></li><li><a class=\"\" href=\"/list/meixing/\">美型</a></li><li><a class=\"\" href=\"/list/haomen/\">豪门</a></li><li><a class=\"\" href=\"/list/hunchong/\">婚宠</a></li><li><a class=\"\" href=\"/list/kaigua/\">开挂</a></li><li><a class=\"\" href=\"/list/xuexing/\">血腥</a></li><li><a class=\"\" href=\"/list/qingsong/\">轻松</a></li><li><a class=\"\" href=\"/list/yangcheng/\">养成</a></li><li><a class=\"\" href=\"/list/tishen/\">替身</a></li><li><a class=\"\" href=\"/list/nanshen/\">男神</a></li><li><a class=\"\" href=\"/list/qingqingshu/\">青青树</a></li><li><a class=\"\" href=\"/list/yishijie/\">异世界</a></li><li><a class=\"\" href=\"/list/nanchuannv/\">男穿女</a></li><li><a class=\"\" href=\"/list/hunchuan/\">魂穿</a></li><li><a class=\"\" href=\"/list/yinan/\">阴暗</a></li><li><a class=\"\" href=\"/list/dujitang/\">毒鸡汤</a></li><li><a class=\"\" href=\"/list/pianyu/\">片玉</a></li><li><a class=\"\" href=\"/list/manhuahui/\">漫画会</a></li><li><a class=\"\" href=\"/list/longren/\">龙刃</a></li><li><a class=\"\" href=\"/list/xihuan/\">喜欢</a></li><li><a class=\"\" href=\"/list/zhaohuan/\">召唤</a></li><li><a class=\"\" href=\"/list/yijie/\">异界</a></li><li><a class=\"\" href=\"/list/henxiyou/\">狠西游</a></li><li><a class=\"\" href=\"/list/xiyouji/\">西游记</a></li><li><a class=\"\" href=\"/list/jianghu/\">江湖</a></li><li><a class=\"\" href=\"/list/duantoudao/\">断头岛</a></li><li><a class=\"\" href=\"/list/hanman/\">韩漫</a></li><li><a class=\"\" href=\"/list/bingjiao/\">病娇</a></li><li><a class=\"\" href=\"/list/changpian/\">长篇</a></li><li><a class=\"\" href=\"/list/chenlan/\">陈岚</a></li><li><a class=\"\" href=\"/list/aiqing/\">爱情</a></li><li><a class=\"\" href=\"/list/nvqiang/\">女强</a></li><li><a class=\"\" href=\"/list/ranxiang/\">燃向</a></li><li><a class=\"\" href=\"/list/tianshangkong/\">天上空</a></li><li><a class=\"\" href=\"/list/duzhiniao/\">渡之鸟</a></li><li><a class=\"\" href=\"/list/xuezu/\">血族</a></li><li><a class=\"\" href=\"/list/mowang/\">魔王</a></li><li><a class=\"\" href=\"/list/keai/\">可爱</a></li><li><a class=\"\" href=\"/list/gongting/\">宫廷</a></li><li><a class=\"\" href=\"/list/hunlian/\">婚恋</a></li><li><a class=\"\" href=\"/list/meng/\">萌</a></li><li><a class=\"\" href=\"/list/ashuai/\">阿衰</a></li><li><a class=\"\" href=\"/list/sanjiaolian/\">三角恋</a></li><li><a class=\"\" href=\"/list/qianshi/\">前世</a></li><li><a class=\"\" href=\"/list/lunhui/\">轮回</a></li><li><a class=\"\" href=\"/list/jingqi/\">惊奇</a></li><li><a class=\"\" href=\"/list/zhentan/\">侦探</a></li><li><a class=\"\" href=\"/list/huanlexiang/\">欢乐向</a></li><li><a class=\"\" href=\"/list/zhichang/\">职场</a></li><li><a class=\"\" href=\"/list/gandong/\">感动</a></li><li><a class=\"\" href=\"/list/jiakong/\">架空</a></li><li><a class=\"\" href=\"/list/qingxiaoshuo/\">轻小说</a></li><li><a class=\"\" href=\"/list/yanyi/\">颜艺</a></li><li><a class=\"\" href=\"/list/xingzhuanhuan/\">性转换</a></li><li><a class=\"\" href=\"/list/dongfang/\">东方</a></li><li><a class=\"\" href=\"/list/danmeiBL/\">耽美BL</a></li><li><a class=\"\" href=\"/list/qingsonggaoxiao/\">轻松搞笑</a></li><li><a class=\"\" href=\"/list/tongrenmanhua/\">同人漫画</a></li><li><a class=\"\" href=\"/list/xiaoyuangaoxiaoshenghuo/\">校园搞笑生活</a></li><li><a class=\"\" href=\"/list/shaonvaiqing/\">少女爱情</a></li><li><a class=\"\" href=\"/list/zhengju/\">正剧</a></li><li><a class=\"\" href=\"/list/shaonao/\">烧脑</a></li><li><a class=\"\" href=\"/list/zhuangbi/\">装逼</a></li><li><a class=\"\" href=\"/list/shengui/\">神鬼</a></li><li><a class=\"\" href=\"/list/weiniang/\">伪娘</a></li><li><a class=\"\" href=\"/list/gaoqingdanxing/\">高清单行</a></li><li><a class=\"\" href=\"/list/gushimanhua/\">故事漫画</a></li><li><a class=\"\" href=\"/list/lianaishenghuoxuanhuan/\">恋爱生活玄幻</a></li><li><a class=\"\" href=\"/list/xifangmohuan/\">西方魔幻</a></li><li><a class=\"\" href=\"/list/jianniang/\">舰娘</a></li><li><a class=\"\" href=\"/list/zhaixi/\">宅系</a></li><li><a class=\"\" href=\"/list/shangzhan/\">商战</a></li><li><a class=\"\" href=\"/list/shuangliu/\">爽流</a></li><li><a class=\"\" href=\"/list/rexuemaoxian/\">热血冒险</a></li><li><a class=\"\" href=\"/list/keji/\">科技</a></li><li><a class=\"\" href=\"/list/wenxin/\">温馨</a></li><li><a class=\"\" href=\"/list/jiating/\">家庭</a></li><li><a class=\"\" href=\"/list/hunyin/\">婚姻</a></li><li><a class=\"\" href=\"/list/duanzi/\">段子</a></li><li><a class=\"\" href=\"/list/neihan/\">内涵</a></li><li><a class=\"\" href=\"/list/jizhan/\">机战</a></li><li><a class=\"\" href=\"/list/yulequan/\">娱乐圈</a></li><li><a class=\"\" href=\"/list/weilai/\">未来</a></li><li><a class=\"\" href=\"/list/chongwu/\">宠物</a></li><li><a class=\"\" href=\"/list/bazonglianaixuanhuan/\">霸总恋爱玄幻</a></li><li><a class=\"\" href=\"/list/gushi/\">故事</a></li><li><a class=\"\" href=\"/list/yinyuewudao/\">音乐舞蹈</a></li><li><a class=\"\" href=\"/list/nixi/\">逆袭</a></li><li><a class=\"\" href=\"/list/zhaohuanshou/\">召唤兽</a></li><li><a class=\"\" href=\"/list/kehuanmohuan/\">科幻魔幻</a></li><li><a class=\"\" href=\"/list/jiujie/\">纠结</a></li><li><a class=\"\" href=\"/list/lunli/\">伦理</a></li><li><a class=\"\" href=\"/list/lianaishenghuo/\">恋爱生活</a></li><li><a class=\"\" href=\"/list/xinzuo/\">新作</a></li><li><a class=\"\" href=\"/list/lishimanhua/\">历史漫画</a></li><li><a class=\"\" href=\"/list/ertong/\">儿童</a></li><li><a class=\"\" href=\"/list/zhentantuili/\">侦探推理</a></li><li><a class=\"\" href=\"/list/xiuzhenlianaijiakong/\">修真恋爱架空</a></li><li><a class=\"\" href=\"/list/shougong/\">手工</a></li><li><a class=\"\" href=\"/list/qingnian/\">青年</a></li><li><a class=\"\" href=\"/list/qitamanhua/\">其他漫画</a></li><li><a class=\"\" href=\"/list/zhiyu2/\">致郁</a></li><li><a class=\"\" href=\"/list/shishi/\">史诗</a></li><li><a class=\"\" href=\"/list/xiuji/\">秀吉</a></li><li><a class=\"\" href=\"/list/xiangcun/\">乡村</a></li><li><a class=\"\" href=\"/list/xingzhuan/\">性转</a></li><li><a class=\"\" href=\"/list/hunai/\">婚爱</a></li><li><a class=\"\" href=\"/list/siwang/\">死亡</a></li><li><a class=\"\" href=\"/list/sishen/\">死神</a></li><li><a class=\"\" href=\"/list/shaonan/\">少男</a></li><li><a class=\"\" href=\"/list/xuanyijingsong/\">悬疑、惊悚</a></li><li><a class=\"\" href=\"/list/baoxiaoxiju/\">爆笑喜剧</a></li><li><a class=\"\" href=\"/list/dongzuogedou/\">动作格斗</a></li><li><a class=\"\" href=\"/list/gaibian/\">改编</a></li><li><a class=\"\" href=\"/list/AA/\">AA</a></li><li><a class=\"\" href=\"/list/lianaidanmei/\">恋爱耽美</a></li><li><a class=\"\" href=\"/list/heidao/\">黑道</a></li><li><a class=\"\" href=\"/list/guiguai/\">鬼怪</a></li><li><a class=\"\" href=\"/list/sangshi/\">丧尸</a></li><li><a class=\"\" href=\"/list/zhupu/\">主仆</a></li><li><a class=\"\" href=\"/list/zhiyinmanke/\">知音漫客</a></li><li><a class=\"\" href=\"/list/maimeng/\">麦萌</a></li><li><a class=\"\" href=\"/list/nizhuan/\">逆转</a></li><li><a class=\"\" href=\"/list/danvzhu/\">大女主</a></li><li><a class=\"\" href=\"/list/aimei/\">暧昧</a></li><li><a class=\"\" href=\"/list/shenghua/\">生化</a></li><li><a class=\"\" href=\"/list/qiwen/\">奇闻</a></li><li><a class=\"\" href=\"/list/zhaidou/\">宅斗</a></li><li><a class=\"\" href=\"/list/lanmu/\">栏目</a></li><li><a class=\"\" href=\"/list/guaitan/\">怪谈</a></li><li><a class=\"\" href=\"/list/chongai/\">宠爱</a></li><li><a class=\"\" href=\"/list/huanxiang/\">幻想</a></li><li><a class=\"\" href=\"/list/yizu/\">异族</a></li><li><a class=\"\" href=\"/list/tanan/\">探案</a></li><li><a class=\"\" href=\"/list/panni/\">叛逆</a></li><li><a class=\"\" href=\"/list/juwei/\">橘味</a></li><li><a class=\"\" href=\"/list/yinv/\">乙女</a></li><li><a class=\"\" href=\"/list/lieqi/\">猎奇</a></li><li><a class=\"\" href=\"/list/rigeng/\">日更</a></li><li><a class=\"\" href=\"/list/manman/\">漫漫</a></li><li><a class=\"\" href=\"/list/zhidou/\">智斗</a></li><li><a class=\"\" href=\"/list/zhengnengliang/\">正能量</a></li><li><a class=\"\" href=\"/list/manhuayifan/\">漫画一番</a></li><li><a class=\"\" href=\"/list/nvwangdiankeng/\">女王点坑</a></li><li><a class=\"\" href=\"/list/mankezhan/\">漫客栈</a></li><li><a class=\"\" href=\"/list/samanhua/\">飒漫画</a></li><li><a class=\"\" href=\"/list/xiaoshuogaibian/\">小说改编</a></li><li><a class=\"\" href=\"/list/shenshi/\">绅士</a></li><li><a class=\"\" href=\"/list/kongbuxuanyi/\">恐怖悬疑</a></li><li><a class=\"\" href=\"/list/huiben/\">绘本</a></li><li><a class=\"\" href=\"/list/yinyue/\">音乐</a></li><li><a class=\"\" href=\"/list/huxian/\">狐仙</a></li><li><a class=\"\" href=\"/list/sihoushijie/\">死后世界</a></li><li><a class=\"\" href=\"/list/motong/\">墨瞳</a></li><li><a class=\"\" href=\"/list/manhua/\">漫画</a></li><li><a class=\"\" href=\"/list/mori/\">末日</a></li><li><a class=\"\" href=\"/list/xitong/\">系统</a></li><li><a class=\"\" href=\"/list/shenxian/\">神仙</a></li><li><a class=\"\" href=\"/list/youyaoqi/\">有妖气</a></li><li><a class=\"\" href=\"/list/guaiwu/\">怪物</a></li><li><a class=\"\" href=\"/list/yaoguai/\">妖怪</a></li><li><a class=\"\" href=\"/list/shenhao/\">神豪</a></li><li><a class=\"\" href=\"/list/bazongdushi/\">霸总.都市</a></li><li><a class=\"\" href=\"/list/gaotian/\">高甜</a></li><li><a class=\"\" href=\"/list/xianzhiji/\">限制级</a></li><li><a class=\"\" href=\"/list/dianjing/\">电竞</a></li><li><a class=\"\" href=\"/list/unknown/\">ゆり</a></li><li><a class=\"\" href=\"/list/xiongdiqing/\">兄弟情</a></li><li><a class=\"\" href=\"/list/nuanmeng/\">暖萌</a></li><li><a class=\"\" href=\"/list/haokuai/\">豪快</a></li><li><a class=\"\" href=\"/list/wanjie/\">完结</a></li><li><a class=\"\" href=\"/list/nvsheng/\">女生</a></li><li><a class=\"\" href=\"/list/lianzai/\">连载</a></li><li><a class=\"\" href=\"/list/nansheng/\">男生</a></li><li><a class=\"\" href=\"/list/futa/\">扶她</a></li><li><a class=\"\" href=\"/list/bianshenlongyurentishiyanshenghuaweiji/\">变身;龙鱼;人体实验;生化危机</a></li></ul>";
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
        return ComicUtil.getRankMyMap(map, mapList, s);
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