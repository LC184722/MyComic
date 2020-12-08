package com.qc.mycomic.util;

import android.util.Log;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ImageInfo;
import com.qc.mycomic.model.MyMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * @author LuQiChuang
 * @desc 漫画信息工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class ComicUtil {

    public static final int STATUS_HIS = 0;
    public static final int STATUS_FAV = 1;
    public static final int STATUS_ALL = 2;

    private static List<Comic> hisComicList;
    private static List<Comic> favComicList;
    private static List<Comic> comicList;

    public static List<Comic> initComicList(int status) {
        comicList = null;
        return getComicList(status);
    }

    public static List<Comic> getComicList(int status) {
        if (comicList == null) {
            comicList = DBUtil.findComicListByStatus(STATUS_ALL);
            hisComicList = new ArrayList<>();
            favComicList = new ArrayList<>();
            for (Comic comic : comicList) {
                if (comic.getStatus() == STATUS_HIS) {
                    hisComicList.add(comic);
                }
                if (comic.getStatus() == STATUS_FAV) {
                    favComicList.add(comic);
                }
            }
        }
        if (status == STATUS_HIS) {
            return hisComicList;
        } else if (status == STATUS_FAV) {
            return favComicList;
        } else {
            return comicList;
        }
    }

    public static List<Comic> getHisComicList() {
        return getComicList(STATUS_HIS);
    }

    public static List<Comic> getFavComicList() {
        return getComicList(STATUS_FAV);
    }

    public static List<Comic> getComicList() {
        return getComicList(STATUS_ALL);
    }

    public static void removeComic(Comic comic) {
        List<Comic> list = getComicList(comic.getStatus());
        list.remove(comic);
    }

    /**
     * 将comic置于链表第一个
     *
     * @param comic comic
     * @return void
     */
    public static void first(Comic comic) {
        List<Comic> list = getComicList(comic.getStatus());
        list.remove(comic);
        if (comic.isUpdate()) {
            list.add(0, comic);
        } else {
            if (list.isEmpty() || list.get(list.size() - 1).getPriority() != 0) {
                list.add(comic);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPriority() == 0) {
                        list.add(i, comic);
                        break;
                    }
                }
            }
        }
    }

    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId) {
        return getImageInfoList(urls, chapterId, "");
    }

    public static List<ImageInfo> getImageInfoList(List<String> urlList, int chapterId) {
        return getImageInfoList(urlList, chapterId, "");
    }

    public static List<ImageInfo> getImageInfoList(String[] urls, int chapterId, String prevUrl) {
        List<ImageInfo> list = new ArrayList<>();
        if (urls != null) {
            int i = 0;
            for (String url : urls) {
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urls.length, prevUrl + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

    public static List<ImageInfo> getImageInfoList(List<String> urlList, int chapterId, String prevUrl) {
        List<ImageInfo> list = new ArrayList<>();
        if (urlList != null) {
            int i = 0;
            for (String url : urlList) {
                ImageInfo imageInfo = new ImageInfo(chapterId, i++, urlList.size(), prevUrl + url);
                list.add(imageInfo);
            }
        }
        return list;
    }

    public static String getHtml(Response response, int sourceId) {
        String html;
        try {
            if (sourceId == Codes.PU_FEI) {
                byte[] b = response.body().bytes(); //获取数据的bytes
                html = new String(b, "GB2312"); //然后将其转为gb2312
            } else {
                html = response.body().string();
            }
        } catch (Exception e) {
            html = "";
        }
        return html;
    }

    public static void generator(String s) {
//        s = "<ul>\n" +
//                "<li><a href=\"/all/\">全部</a></li>\n" +
//                "<li class=\"classid111\"><a title=\"热血\" href=\"/rexue/\">热血</a></li><li class=\"classid112\"><a title=\"格斗\" href=\"/gedou/\">格斗</a></li><li class=\"classid113\"><a title=\"科幻\" href=\"/kehuan/\">科幻</a></li><li class=\"classid114\"><a title=\"竞技\" href=\"/jingji/\">竞技</a></li><li class=\"classid115\"><a title=\"搞笑\" href=\"/gaoxiao/\">搞笑</a></li><li class=\"classid116\"><a title=\"推理\" href=\"/tuili/\">推理</a></li><li class=\"classid117\"><a title=\"恐怖\" href=\"/kongbu/\">恐怖</a></li><li class=\"classid118\"><a title=\"耽美\" href=\"/danmei/\">耽美</a></li><li class=\"classid119\"><a title=\"少女\" href=\"/shaonv/\">少女</a></li><li class=\"classid120\"><a title=\"恋爱\" href=\"/lianai/\">恋爱</a></li><li class=\"classid121\"><a title=\"生活\" href=\"/shenghuo/\">生活</a></li><li class=\"classid122\"><a title=\"战争\" href=\"/zhanzheng/\">战争</a></li><li class=\"classid123\"><a title=\"故事\" href=\"/gushi/\">故事</a></li><li class=\"classid124\"><a title=\"冒险\" href=\"/maoxian/\">冒险</a></li><li class=\"classid125\"><a title=\"魔幻\" href=\"/mohuan/\">魔幻</a></li><li class=\"classid126\"><a title=\"玄幻\" href=\"/xuanhuan/\">玄幻</a></li><li class=\"classid127\"><a title=\"校园\" href=\"/xiaoyuan/\">校园</a></li><li class=\"classid128\"><a title=\"悬疑\" href=\"/xuanyi/\">悬疑</a></li><li class=\"classid129\"><a title=\"萌系\" href=\"/mengxi/\">萌系</a></li><li class=\"classid130\"><a title=\"穿越\" href=\"/chuanyue/\">穿越</a></li><li class=\"classid131\"><a title=\"后宫\" href=\"/hougong/\">后宫</a></li><li class=\"classid132\"><a title=\"都市\" href=\"/dushi/\">都市</a></li><li class=\"classid133\"><a title=\"武侠\" href=\"/wuxia/\">武侠</a></li><li class=\"classid134\"><a title=\"历史\" href=\"/lishi/\">历史</a></li><li class=\"classid135\"><a title=\"同人\" href=\"/tongren/\">同人</a></li><li class=\"classid136\"><a title=\"励志\" href=\"/lizhi/\">励志</a></li><li class=\"classid137\"><a title=\"百合\" href=\"/baihe/\">百合</a></li><li class=\"classid138\"><a title=\"治愈\" href=\"/zhiyu/\">治愈</a></li><li class=\"classid139\"><a title=\"机甲\" href=\"/jijia/\">机甲</a></li><li class=\"classid140\"><a title=\"纯爱\" href=\"/chunai/\">纯爱</a></li><li class=\"classid141\"><a title=\"美食\" href=\"/meishi/\">美食</a></li><li class=\"classid142\"><a title=\"血腥\" href=\"/xuexing/\">血腥</a></li><li class=\"classid143\"><a title=\"僵尸\" href=\"/jiangshi/\">僵尸</a></li><li class=\"classid144\"><a title=\"恶搞\" href=\"/egao/\">恶搞</a></li><li class=\"classid145\"><a title=\"虐心\" href=\"/nuexin/\">虐心</a></li><li class=\"classid146\"><a title=\"动作\" href=\"/dongzuo/\">动作</a></li><li class=\"classid147\"><a title=\"惊险\" href=\"/jingxian/\">惊险</a></li><li class=\"classid148\"><a title=\"唯美\" href=\"/weimei/\">唯美</a></li><li class=\"classid149\"><a title=\"震撼\" href=\"/zhenhan/\">震撼</a></li><li class=\"classid150\"><a title=\"复仇\" href=\"/fuchou/\">复仇</a></li><li class=\"classid151\"><a title=\"侦探\" href=\"/zhentan/\">侦探</a></li><li class=\"classid152\"><a title=\"脑洞\" href=\"/naodong/\">脑洞</a></li><li class=\"classid153\"><a title=\"奇幻\" href=\"/qihuan/\">奇幻</a></li><li class=\"classid154\"><a title=\"宫斗\" href=\"/gongdou/\">宫斗</a></li><li class=\"classid155\"><a title=\"爆笑\" href=\"/baoxiao/\">爆笑</a></li><li class=\"classid156\"><a title=\"运动\" href=\"/yundong/\">运动</a></li><li class=\"classid157\"><a title=\"青春\" href=\"/qingchun/\">青春</a></li><li class=\"classid158\"><a title=\"灵异\" href=\"/lingyi/\">灵异</a></li><li class=\"classid159\"><a title=\"古风\" href=\"/gufeng/\">古风</a></li><li class=\"classid160\"><a title=\"权谋\" href=\"/quanmou/\">权谋</a></li><li class=\"classid161\"><a title=\"节操\" href=\"/jiecao/\">节操</a></li><li class=\"classid162\"><a title=\"明星\" href=\"/mingxing/\">明星</a></li><li class=\"classid163\"><a title=\"暗黑\" href=\"/anhei/\">暗黑</a></li><li class=\"classid164\"><a title=\"社会\" href=\"/shehui/\">社会</a></li><li class=\"classid165\"><a title=\"浪漫\" href=\"/langman/\">浪漫</a></li><li class=\"classid166\"><a title=\"霸总\" href=\"/lanmu/\">霸总</a></li><li class=\"classid167\"><a title=\"仙侠\" href=\"/xianxia/\">仙侠</a></li>\n" +
//                "</ul>";
        JsoupNode node = new JsoupNode(s);
        Elements elements = node.getElements("a");
        List<Map<String, String>> mapList = new ArrayList<>(elements.size());
        Map<String, String> map;
        for (Element element : elements) {
            node.init(element);
            String name = node.attr("a", "title");
            String url = node.href("a");
            map = new HashMap<>();
            map.put("name", name);
            map.put("url", url);
            mapList.add(map);
//            System.out.println("element = " + element);
        }
        printRankStr(mapList);
    }

    private static void printRankStr(List<Map<String, String>> mapList) {
        for (Map<String, String> map : mapList) {
            String name = map.get("name");
            String url = map.get("url");
            String s = "map.put(\"%s\", \"http://www.ccshwy.com%s\");";
            s = String.format(s, name, url);
            System.out.println(s);
        }
    }

    public static MyMap<String, String> getRankMyMap(List<Map<String, String>> mapList, String s) {
        MyMap<String, String> myMap = new MyMap<>();
        return getRankMyMap(myMap, mapList, s);
    }

    public static MyMap<String, String> getRankMyMap(MyMap<String, String> myMap, List<Map<String, String>> mapList, String s) {
        for (Map<String, String> map : mapList) {
            String name = map.get("name");
            String url = map.get("url");
            url = String.format(s, url);
            myMap.put(name, url);
        }
        return myMap;
    }

}
