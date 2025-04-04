package top.luqichuang.mycomic.source;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.json.JsonNode;
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
 * @date 2021/6/10 15:29
 * @ver 1.0
 */
public class OH extends BaseComicSource {
    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.OH;
    }

    @Override
    public Map<String, String> getImageHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", getIndex());
        headers.put("User-Agent", NetUtil.USER_AGENT_WEB);
        return headers;
    }

    @Override
    public String getIndex() {
        return "https://www.cocomanga.com";
    }

    @Override
    public Request getSearchRequest(String searchString) {
        String url = String.format("%s/search?searchString=%s", getIndex(), searchString);
        return NetUtil.getRequest(url);
    }

    @Override
    public List<ComicInfo> getInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("h1 a");
                String author = node.ownText("ul li.fed-col-xs6", 1);
                String updateTime = node.ownText("ul li.fed-col-xs6", 2);
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "dl.fed-deta-info");
    }

    @Override
    public void setInfoDetail(ComicInfo info, String html, Map<String, Object> map) {
        JsoupStarter<ChapterInfo> starter = new JsoupStarter<ChapterInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {
                String title = node.ownText("h1.fed-part-eone.fed-font-xvi");
                String imgUrl = node.attr("a.fed-list-pics.fed-lazy.fed-part-2by3", "data-original");
                String author = node.ownText("div.fed-part-layout li.fed-col-md6", 1, "a");
                String intro = node.ownText("p.fed-padding.fed-part-both.fed-text-muted");
                String updateStatus = node.ownText("div.fed-part-layout li.fed-col-md6", 0, "a");
                String updateTime = node.ownText("div.fed-part-layout li.fed-col-md6", 2, "a");
                info.setDetail(title, imgUrl, author, updateTime, updateStatus, intro);
            }

            @Override
            protected ChapterInfo dealElement(JsoupNode node, int elementId) {
                String title = node.title("a");
                String chapterUrl = getIndex() + node.href("a");
                return new ChapterInfo(elementId, title, chapterUrl);
            }
        };
        starter.startInfo(html);
        SourceHelper.initChapterInfoList(info, starter.startElements(html, "ul.fed-part-rows li.fed-col-lg3"));
        SourceHelper.initChapterInfoMap(info, html, "div.fed-drop-boxs.fed-drop-tops.fed-matp-v li", "a", "div.fed-play-item.fed-drop-item", "div.all_data_list li");
    }

    @Override
    public List<Content> getContentList(String html, int chapterId, Map<String, Object> map) {
        String[] urls = null;
        String chapterImagesStr = StringUtil.match("C_DATA='(.*?)'", html);
        String result = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(chapterImagesStr), "fw122587mkertyui");
        if (result != null) {
            try {
                String mhInfo = StringUtil.match("mh_info=(\\{.*?\\})", result);
                String mhImage = StringUtil.match("image_info=(\\{.*?\\})", result);
                JsonNode node = new JsonNode(mhImage);
                String direct = node.string("urls__direct");
                if (direct != null && !direct.equals("")) {
                    direct = DecryptUtil.decryptBase64(direct);
                    urls = direct.split("\\|SEPARATER\\|");
                } else {
                    node.init(mhInfo);
                    String server = "https://img.cocomanga.com/comic/";
                    String imgPath = node.string("enc_code2");
                    imgPath = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(imgPath), "fw125gjdi9ertyui");
                    imgPath = DecryptUtil.getUtf8EncodeStr(imgPath);
//                    imgPath = imgPath.replace("%3D", "=");
                    String encCode1 = node.string("enc_code1");
                    encCode1 = DecryptUtil.decryptAES(DecryptUtil.decryptBase64(encCode1), "fw122587mkertyui");
                    int total = encCode1 != null ? Integer.parseInt(encCode1) : 50;
                    urls = new String[total];
                    for (int i = 0; i < total; i++) {
                        urls[i] = server + imgPath + String.format(Locale.CHINA, "%04d.jpg", i + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SourceHelper.getContentList(urls, chapterId);
    }

    @Override
    public Map<String, String> getRankMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("月点击", "https://www.cocomanhua.com/show?orderBy=monthlyCount&page=1");
        map.put("周点击", "https://www.cocomanhua.com/show?orderBy=weeklyCount&page=1");
        map.put("日点击", "https://www.cocomanhua.com/show?orderBy=dailyCount&page=1");
        map.put("收录日", "https://www.cocomanhua.com/show?orderBy=create&page=1");
        map.put("更新日", "https://www.cocomanhua.com/show?orderBy=update&page=1");
        map.put("连载中", "https://www.cocomanhua.com/show?orderBy=weeklyCount&status=1&page=1");
        map.put("已完结", "https://www.cocomanhua.com/show?orderBy=weeklyCount&status=2&page=1");
        map.put("玄幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10024&page=1");
        map.put("热血", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10023&page=1");
        map.put("恋爱", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10126&page=1");
        map.put("都市", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10124&page=1");
        map.put("古风", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10143&page=1");
        map.put("冒险", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10210&page=1");
        map.put("穿越", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10129&page=1");
        map.put("爆笑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10201&page=1");
        map.put("搞笑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10122&page=1");
        map.put("奇幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10242&page=1");
        map.put("校园", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10131&page=1");
        map.put("少年", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10321&page=1");
        map.put("修真", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10133&page=1");
        map.put("霸总", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10127&page=1");
        map.put("其他", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10560&page=1");
        map.put("动作", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10125&page=1");
        map.put("生活", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10142&page=1");
        map.put("少女", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10301&page=1");
        map.put("后宫", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10138&page=1");
        map.put("少男", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10641&page=1");
        map.put("逆转", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10702&page=1");
        map.put("武侠", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10139&page=1");
        map.put("重生", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10461&page=1");
        map.put("科幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10181&page=1");
        map.put("总裁", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10306&page=1");
        map.put("剧情", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10480&page=1");
        map.put("大女主", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10706&page=1");
        map.put("悬疑", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10183&page=1");
        map.put("魔幻", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10227&page=1");
        map.put("恐怖", "https://www.cocomanhua.com/show?orderBy=weeklyCount&mainCategoryId=10185&page=1");
        return map;
    }

    @Override
    public List<ComicInfo> getRankInfoList(String html) {
        JsoupStarter<ComicInfo> starter = new JsoupStarter<ComicInfo>() {
            @Override
            protected void dealInfo(JsoupNode node) {

            }

            @Override
            protected ComicInfo dealElement(JsoupNode node, int elementId) {
                String title = node.ownText("a.fed-list-title");
                String author = null;
                String updateTime = node.ownText("span.fed-list-desc");
                String imgUrl = node.attr("a", "data-original");
                String detailUrl = getIndex() + node.href("a");
                return new ComicInfo(getSourceId(), title, author, detailUrl, imgUrl, updateTime);
            }
        };
        return starter.startElements(html, "li.fed-list-item");
    }
}
